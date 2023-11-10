package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object PixRegexTrigger: Trigger(
    name = "monki_regex",
    description = "Sends a monki ascii when theres a message that reminds of our ape friend"
) {
    val regex = Regex(".*pix.*|.*nada ainda.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                SoundPlayerManager.playSoundForMessage(this, Sound.E_O_PIX)
            }
        }
    }
}
