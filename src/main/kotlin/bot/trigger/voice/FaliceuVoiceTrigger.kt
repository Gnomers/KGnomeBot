package bot.trigger.voice

import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isDisconnect
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

object FaliceuVoiceTrigger : Trigger(
    name = "channel_left",
    description = "Sends out a \"faliceu\" when someone leaves the voice chat"
) {

    private val TRIGGER_CHANCE = 0.5 // 50%
    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent> {
            delay(500)
            if (isDisconnect(this.old, this.state) && Random.nextDouble() < TRIGGER_CHANCE) {
                this.old?.getChannelOrNull()?.let {
                    it.voiceStates.firstOrNull()?.let { _ ->
                        SoundPlayerManager.playSoundOnChannel(it, Sound.FALICEU)
                    }
                }
            }
        }
    }
}
