package bot.trigger.voice

import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isDisconnect
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.coroutines.delay

class FaliceuVoiceTrigger: Trigger(
    name = "channel_left",
    description = "Sends out a \"faliceu\" when someone leaves the voice chat"
) {

    override suspend fun register(kordInstance: Kord) {
        val faliceu = Sound.FALICEU
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent> {
            delay(500)
            if (isDisconnect(this.old, this.state)) {
                this.old?.getChannelOrNull()?.let {
                    SoundPlayerManager.playSoundOnChannel(it, faliceu)
                }
            }
        }
    }
}
