package bot.core.voice

import bot.constants.USER_MUST_BE_IN_VOICE_CHANNEL
import bot.utilities.Sound
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent
import com.sedmelluq.discord.lavaplayer.player.event.TrackExceptionEvent
import com.sedmelluq.discord.lavaplayer.player.event.TrackStuckEvent
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.kordLogger
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import dev.kord.voice.exception.VoiceConnectionInitializationException
import kotlinx.coroutines.runBlocking

@OptIn(KordVoice::class)
object SoundPlayerManager {

    // here we keep track of active voice connections
    var actualConnection: VoiceConnection? = null

    // Idk if this way of managing VoiceConnection may lead to some leaks, lets hope not
    suspend fun playSoundOnChannel(channel: BaseVoiceChannelBehavior, sound: Sound) {
        val player = createPlayer()
        player.playTrack(sound.getTrack())

        runCatching {
            actualConnection = channel.connect {
                // the audio provider should provide frames of audio
                audioProvider { AudioFrame.fromData(player.provide().data) }
            }
        }.onFailure {
            if (it is VoiceConnectionInitializationException) {
                kordLogger.error("An error occurred, shutting down connection causing exception=${it}")
                stop()
            }
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

    private fun createPlayer() = DefaultAudioPlayerManager().createPlayer().apply {
        this.addListener {
            when(it) {
                is TrackEndEvent, is TrackExceptionEvent, is TrackStuckEvent -> runBlocking {
                    stop()
                }
            }
        }
    }

    suspend fun stop() {
        actualConnection?.shutdown()
    }

}
