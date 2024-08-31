package bot.core.voice

import bot.constants.USER_MUST_BE_IN_VOICE_CHANNEL
import bot.logging.Loggable
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.isValidURL
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent
import com.sedmelluq.discord.lavaplayer.player.event.TrackExceptionEvent
import com.sedmelluq.discord.lavaplayer.player.event.TrackStuckEvent
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.runBlocking


@OptIn(KordVoice::class)
object SoundPlayerManager: Loggable {

    // here we keep track of active voice connections
    val connections: MutableMap<Snowflake, VoiceConnection> = mutableMapOf()
    val players: MutableMap<AudioPlayer, VoiceConnection> = mutableMapOf()

    // Idk if this way of managing VoiceConnection may lead to some leaks, lets hope not
    suspend fun playSoundOnChannel(voiceChannel: BaseVoiceChannelBehavior, sound: Sound) {
        val player = createPlayerManager().createCustomPlayer()

        val guildId = prepareGuildConnection(voiceChannel, player)

        player.playTrack(sound.getTrack())

        providePlayerSound(voiceChannel, player, guildId)
    }

    suspend fun playYoutubeVideoOnChannel(chatChannel: MessageChannelBehavior? = null, voiceChannel: BaseVoiceChannelBehavior, video: String) {

        val playerManager = createPlayerManager()
        val player = createPlayerManager().createCustomPlayer()

        val guildId = prepareGuildConnection(voiceChannel, player)

        val searchString = if (video.isValidURL()) video else "ytsearch:$video"
        playerManager.loadItem(searchString, createYoutubeResultHandler(player, chatChannel))

        providePlayerSound(voiceChannel, player, guildId)
    }

    private suspend fun providePlayerSound(voiceChannel: BaseVoiceChannelBehavior, player: AudioPlayer, guildId: Snowflake) {
        runCatching {
            val connection = voiceChannel.connect {
                // the audio provider should provide frames of audio
                audioProvider {
                    AudioFrame.fromData(
                        player.provide()?.data
                    )
                }
            }
            connections[guildId] = connection
            players[player] = connection
        }.onFailure {
            logger.error("An error occurred, shutting down connection causing exception=${it}")
            stop(guildId)
        }
    }

    suspend fun playSoundForMessage(event: MessageCreateEvent, sound: Sound) {
        val member = event.member
        val voiceChannel = member?.getVoiceState()?.getChannelOrNull()

        if (event.message.content.isCommand() && (member == null || voiceChannel == null)) {
            event.message.channel.createMessage(USER_MUST_BE_IN_VOICE_CHANNEL)
            return
        }

        voiceChannel?.let { playSoundOnChannel(it, sound) }
    }

    suspend fun playYoutubeForMessage(event: MessageCreateEvent, video: String) {
        val member = event.member
        val voiceChannel = member?.getVoiceState()?.getChannelOrNull()

        if (event.message.content.isCommand() && (member == null || voiceChannel == null)) {
            event.message.channel.createMessage(USER_MUST_BE_IN_VOICE_CHANNEL)
            return
        }

        voiceChannel?.let { playYoutubeVideoOnChannel(event.message.channel, it, video) }
    }

    private fun createPlayerManager(): DefaultAudioPlayerManager {
        val local: AudioSourceManager = LocalAudioSourceManager()
        val youtube: AudioSourceManager = YoutubeAudioSourceManager()

        return DefaultAudioPlayerManager()
            .also { it.registerSourceManager(youtube) }
            .also { it.registerSourceManager(local) }
    }

    private fun DefaultAudioPlayerManager.createCustomPlayer(): AudioPlayer {
        return this
            .createPlayer()
            .apply {
                this.addListener {
                    when (it) {
                        is TrackEndEvent -> runBlocking {
                            logger.info("Track ended, stopping player. track=${it.track.info.title}")
                            stop(players[it.player]!!.data.guildId)
                        }
                        is TrackExceptionEvent, is TrackStuckEvent -> runBlocking {
                            logger.error("An unexpected event occurred while playing an Audio. event=$it")
                            stop(players[it.player]!!.data.guildId)
                        }
                    }
                }
            }
    }

    suspend fun stop(guildId: Snowflake) {
        connections.remove(guildId)!!.shutdown()
    }

    private fun createYoutubeResultHandler(player: AudioPlayer, channel: MessageChannelBehavior? = null) = object :
        AudioLoadResultHandler {

        override fun trackLoaded(track: AudioTrack?) {
            runBlocking {
                logger.info("Now playing: ${track?.info?.title}")
                channel?.createMessage("Now playing: ${track?.info?.title}")
            }
            player.playTrack(track)
        }

        override fun playlistLoaded(playlist: AudioPlaylist?) {
            val firstTrack = playlist?.tracks?.firstOrNull()
            firstTrack?.let {
                runBlocking {
                    logger.info("Now playing: ${it.info?.title}")
                    channel?.createMessage("Now playing: ${it.info?.title}")
                }
                player.playTrack(it)
            }
        }

        override fun noMatches() {
            runBlocking {
                logger.warn("Couldn't find the video")
                channel?.createMessage("Oh no, even with my magic powers I could not find that video :(")
            }
        }

        override fun loadFailed(exception: FriendlyException?) {
            runBlocking {
                channel?.createMessage("Oh no, I've been gnomed, something went very wrong")
            }
            logger.error("Something went wrong loading the video")
            runBlocking {
                stop(players[player]!!.data.guildId)
            }
        }
    }

    private suspend fun prepareGuildConnection(voiceChannel: BaseVoiceChannelBehavior, player: AudioPlayer): Snowflake {
        val guildId = voiceChannel.guildId
        if (connections.contains(guildId)) {
            connections.remove(guildId)!!.shutdown()
        }
        if(players.contains(player)) {
            players.remove(player)!!.shutdown()
        }
        return guildId
    }

}
