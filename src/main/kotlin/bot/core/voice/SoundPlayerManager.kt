package bot.core.voice

import bot.constants.USER_MUST_BE_IN_VOICE_CHANNEL
import bot.utilities.Sound
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent
import com.sedmelluq.discord.lavaplayer.player.event.TrackExceptionEvent
import com.sedmelluq.discord.lavaplayer.player.event.TrackStuckEvent
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.kordLogger
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.runBlocking


@OptIn(KordVoice::class)
object SoundPlayerManager {

    // here we keep track of active voice connections
    val connections: MutableMap<Snowflake, VoiceConnection> = mutableMapOf()
    val players: MutableMap<AudioPlayer, VoiceConnection> = mutableMapOf()

    // Idk if this way of managing VoiceConnection may lead to some leaks, lets hope not
    suspend fun playSoundOnChannel(channel: BaseVoiceChannelBehavior, sound: Sound) {
        val guildId = channel.guildId
        if (connections.contains(guildId)) {
            connections.remove(guildId)!!.shutdown()
        }
        val player = createPlayer()

        player.playTrack(sound.getTrack())

        runCatching {
            val connection = channel.connect {
                // the audio provider should provide frames of audio
                audioProvider { AudioFrame.fromData(player.provide().data) }
            }
            connections[guildId] = connection
            players[player] = connection
        }.onFailure {
            kordLogger.error("An error occurred, shutting down connection causing exception=${it}")
            stop(guildId)
        }
    }

    suspend fun playSoundForMessage(event: MessageCreateEvent, sound: Sound) {
        val member = event.member
        val voiceChannel = member?.getVoiceState()?.getChannelOrNull()

        if (member == null || voiceChannel == null) {
            event.message.channel.createMessage(USER_MUST_BE_IN_VOICE_CHANNEL)
            return
        }

        playSoundOnChannel(voiceChannel, sound)
    }

    private fun createPlayer(): AudioPlayer {
        val sourceManager: AudioSourceManager = LocalAudioSourceManager()

        return DefaultAudioPlayerManager()
            .also { it.registerSourceManager(sourceManager) }
            .createPlayer()
            .apply {
                this.addListener {
                    when (it) {
                        is TrackEndEvent, is TrackExceptionEvent, is TrackStuckEvent -> runBlocking {
                            stop(players[it.player]!!.data.guildId)
                        }
                    }
                }
            }
    }

    suspend fun stop(guildId: Snowflake) {
        connections.remove(guildId)!!.shutdown()
    }

}
