package bot.trigger

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import bot.utilities.isDisconnect
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.coroutines.delay

object FaliceuVoiceTrigger: Trigger(
    name = "channel_left",
    description = "Sends out a \"faliceu\" when someone joins the chat"
) {

    private const val RANDOM_VOICE_JOIN_CHANCE = 0.2
    override suspend fun register(kordInstance: Kord) {
        val faliceu = Sound.FALICEU
        kordInstance.onIgnoringBots<VoiceStateUpdateEvent> {
            delay(1000)
            if (isDisconnect(this.old, this.state)) {
                this.old?.getChannelOrNull()?.let {
                    SoundPlayerManager.playSoundOnChannel(it, faliceu)
                }
            }
        }
    }
}
