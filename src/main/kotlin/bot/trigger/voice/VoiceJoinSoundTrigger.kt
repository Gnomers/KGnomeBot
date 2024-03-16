package bot.trigger.voice

import bot.constants.CUSTOM_ENTRY_ENV_VAR
import bot.core.voice.SoundPlayerManager
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.onIgnoringBots
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import io.github.cdimascio.dotenv.dotenv
import net.iharder.Base64
import kotlin.random.Random

object VoiceJoinSoundTrigger : Trigger(
    name = "voice_join_sound",
    description = "Plays a configured sound or a random sound when an user joins the voice chat"
), Loggable {
    val om = ObjectMapper().registerKotlinModule()

    val config = runCatching {
        dotenv()[CUSTOM_ENTRY_ENV_VAR]?.let {
            logger.info("Parsing $CUSTOM_ENTRY_ENV_VAR=$it")
            om.readValue(Base64.decode(it), CustomEntryConfiguration::class.java)
        }
    }.getOrNull()

    val TRIGGER_CHANCE = 0.2 // 20%

    override suspend fun register(kordInstance: Kord) {
        if (config?.data == null) {
            logger.warn("Ignoring CustomEntryTrigger registration because $CUSTOM_ENTRY_ENV_VAR is empty or invalid")
            return
        }
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent>(logger = logger) {
            val member = state.getMember()
            if(state.channelId == null ||
                member.isBot ||
                // we will ignore any mute/deaf/live/in-voice changes

                //isCommonVoiceChatAction(this.old, this.state)
                // TEST - UNCOMMENT ABOVE LINE LATER
                this.old?.getChannelOrNull() != null
            ) {
                return@onIgnoringBots
            }

            runCatching {
                // Has a config match
                config.data.firstOrNull {
                    it.userId == member.id.toString()
                }?.let {
                    logger.info("User id=${member.id} tip=${it.tip} has a CustomEntryConfig match. sounds=${it.sounds} event=${this.javaClass} customContext=${this.customContext}")
                    // parse the sound
                    val sound = Sound.valueOf(it.sounds.random())
                    member.getVoiceState().getChannelOrNull()?.let { channel ->
                        SoundPlayerManager.playSoundOnChannel(channel, sound)
                    }
                    return@onIgnoringBots
                }

                // Has no config match
                logger.info("User id=${member.id} has no CustomEntryConfig, checking random sound chance")
                if(Random.nextDouble() < TRIGGER_CHANCE) {
                    logger.info("Triggered ${this::class.simpleName} chance")
                    // TODO avoid playing a sound when it is a custom_entry_sound. Only play it if the user is already in the channel
                    val sound = Sound.COMMON_SOUND_LIST.random()
                    member.getVoiceState().getChannelOrNull()?.let {
                        SoundPlayerManager.playSoundOnChannel(it, sound)
                    }
                }
                return@onIgnoringBots
            }.onFailure {
                logger.error("An error occured on ${this::class.simpleName}, error=${it}", it)
            }

        }
    }


}