package bot.trigger.voice

import bot.constants.CUSTOM_ENTRY_ENV_VAR
import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommonVoiceChatAction
import bot.utilities.onIgnoringBots
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.kordLogger
import io.github.cdimascio.dotenv.dotenv
import net.iharder.Base64

class CustomEntryTrigger : Trigger(
    name = "custom_entry",
    description = "Plays a configured sound when an user joins the voice chat"
) {
    val om = ObjectMapper().registerKotlinModule()

    val config = runCatching {
        dotenv()[CUSTOM_ENTRY_ENV_VAR]?.let {
            kordLogger.info("Parsing $CUSTOM_ENTRY_ENV_VAR=$it")
            om.readValue(Base64.decode(it), CustomEntryConfiguration::class.java)
        }
    }.getOrNull()

    override suspend fun register(kordInstance: Kord) {
        if (config?.data == null) {
            kordLogger.warn("Ignoring CustomEntryTrigger registration because $CUSTOM_ENTRY_ENV_VAR is empty or invalid")
            return
        }
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent> {
            val member = state.getMember()
            if(state.channelId == null ||
                member.isBot ||
                // we will ignore any mute/deaf/live/in-voice changes
                isCommonVoiceChatAction(this.old, this.state)
            ) {
                return@onIgnoringBots
            }

            val matchedData = config.data.firstOrNull {
                it.userId == member.id.toString()
            }?.also { kordLogger.info("User has a CustomEntryConfig match. sound=${it.sound}") }

            runCatching {
                // find a rule that matches with the user that just joined
                matchedData?.let {
                    // parse the sound
                    val sound = Sound.valueOf(it.sound)
                    member.getVoiceState().getChannelOrNull()?.let { channel ->
                        SoundPlayerManager.playSoundOnChannel(channel, sound)
                    }
                    // if the sound is invalid, log error and life goes on
                } ?: kordLogger.error("CustomEntry sound=${matchedData?.sound} is invalid.")
            }
        }
    }


}