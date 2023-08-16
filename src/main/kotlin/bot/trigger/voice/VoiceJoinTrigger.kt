package bot.trigger.voice

import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.*
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlin.random.Random

class VoiceJoinTrigger: Trigger(
    name = "channel_joined",
    description = "Sends out a random sound when someone joins the chat"
) {
    private val randomSoundList = listOf(Sound.WOO, Sound.HM_MONKI, Sound.CS, Sound.AMOGUS, Sound.BANDIDO, Sound.FALICEU)
    private val TRIGGER_CHANCE = 0.2 // 20%


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
            if(Random.nextDouble() < TRIGGER_CHANCE) {
                val sound = randomSoundList.random()
                member.getVoiceState().getChannelOrNull()?.let {
                    SoundPlayerManager.playSoundOnChannel(it, sound)
                }
            }
        }
    }
}
