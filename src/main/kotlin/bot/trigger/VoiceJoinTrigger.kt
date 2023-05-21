package bot.trigger

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on

object VoiceJoinTrigger: Trigger(
    name = "channel_joined",
    description = "Sends out a woo or monki when someone joins the chat"
) {
    val randomSoundList = listOf(Sound.WOO, Sound.HM_MONKI)

    override suspend fun register(kordInstance: Kord) {
        kordInstance.on<VoiceStateUpdateEvent> {
            val member = state.getMember()
            if(state.channelId == null || member.isBot) {
                // we don't serve bots here
                return@on
            }
            val sound = randomSoundList.random()
            member.getVoiceState().getChannelOrNull()?.let {
                SoundPlayerManager.playSoundOnChannel(it, sound)
            }
        }
    }
}
