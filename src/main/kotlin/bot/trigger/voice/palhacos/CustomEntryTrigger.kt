package bot.trigger.voice.palhacos

import bot.constants.CUSTOM_ENTRY_ENV_VAR
import bot.core.CustomEntryConfiguration
import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.kordLogger
import io.github.cdimascio.dotenv.dotenv

class CustomEntryTrigger : Trigger(
    name = "custom_entry",
    description = "Plays a configured sound when an user joins the voice chat"
) {
    val om = ObjectMapper().registerKotlinModule()

    val config = dotenv()[CUSTOM_ENTRY_ENV_VAR]?.let {
        om.readValue(it, CustomEntryConfiguration::class.java)
    }
    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent> {
            val member = state.getMember()
            if(state.channelId == null ||
                member.isBot ||
                // we will ignore any mute/deaf/live/in-voice changes
                changedDeaf(oldState = this.old, newState = this.state) ||
                changedMute(oldState = this.old, newState = this.state) ||
                changedStreaming(oldState = this.old, newState = this.state)
            ) {
                return@onIgnoringBots
            }

//            val sound = member.id
             val match = config?.data?.firstOrNull {
                it.userId == member.id.toString()
             }

            runCatching {
                return@runCatching match?.sound?.let {
                    Sound.valueOf(it)
                }
            }.getOrNull()?.let { sound ->
                kordLogger.error("CustomEntry sound=${match?.sound} is invalid.")
                member.getVoiceState().getChannelOrNull()?.let { channel ->
                    SoundPlayerManager.playSoundOnChannel(channel, sound)
                }
            }
        }
    }


}