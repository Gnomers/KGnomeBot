package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import jdk.nashorn.internal.runtime.regexp.joni.Regex

object PixRegexTrigger: Trigger(
    name = "pix_regex",
    description = "Sends a E O PIX? sound when theres a message that reminds of a missing pix payment"
), Loggable {
    val regex = Regex(".*pix.*|.*nada ainda.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                SoundPlayerManager.playSoundForMessage(this, Sound.E_O_PIX)
            }
        }
    }
}
