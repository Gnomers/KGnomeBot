package bot.trigger.voice

import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommonVoiceChatAction
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlin.random.Random

class VoiceJoinTrigger: Trigger(
    name = "channel_joined",
    description = "Sends out a random sound when someone joins the chat"
) {
    private val TRIGGER_CHANCE = 0.2 // 20%

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent> {
            val member = state.getMember()
            if(state.channelId == null ||
                member.isBot ||
                // we will ignore any mute/deaf/live/in-voice changes
                isCommonVoiceChatAction(this.old, this.state)
            ) {
                return@onIgnoringBots
            }
            if(Random.nextDouble() < TRIGGER_CHANCE) {
                val sound = Sound.COMMON_SOUND_LIST.random()
                member.getVoiceState().getChannelOrNull()?.let {
                    SoundPlayerManager.playSoundOnChannel(it, sound)
                }
            }
        }
    }
}
