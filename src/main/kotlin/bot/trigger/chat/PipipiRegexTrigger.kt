package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object PipipiRegexTrigger: Trigger(
    name = "pipipi_regex",
    description = "Sends a \"Pi pi pii, (...)\" when theres a `.*pi.*` message"
), Loggable {
    // I have no idea what I'm doing
    val regex = Regex(".*pi.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                val sound = Sound.PIPIPI
                SoundPlayerManager.playSoundForMessage(this, sound)
            }
        }
    }
}
