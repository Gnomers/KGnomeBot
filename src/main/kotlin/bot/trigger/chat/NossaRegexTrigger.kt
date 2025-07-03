package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object NossaRegexTrigger: Trigger(
    name = "nossa_regex",
    description = "Sends a special sound when someone is in disbelief ('nossa')"
), Loggable {
    // I have no idea what I'm doing
    val regex = Regex(".*nossa.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                var sound = listOf(Sound.E_O_PIX, Sound.CREMOSO).random()
                SoundPlayerManager.playSoundForMessage(this, sound)
            }
        }
    }
}
