package bot.trigger

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import bot.utilities.changedDeaf
import bot.utilities.changedMute
import bot.utilities.changedStreaming
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.user.VoiceStateUpdateEvent

object VoiceJoinTrigger: Trigger(
    name = "channel_joined",
    description = "Sends out a woo or monki when someone joins the chat"
) {
    val randomSoundList = listOf(Sound.WOO, Sound.HM_MONKI)

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
            val sound = randomSoundList.random()
            member.getVoiceState().getChannelOrNull()?.let {
                SoundPlayerManager.playSoundOnChannel(it, sound)
            }
        }
    }
}
