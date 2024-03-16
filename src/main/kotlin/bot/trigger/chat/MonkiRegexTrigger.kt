package bot.trigger.chat

import bot.constants.MONKI_ASCII
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object MonkiRegexTrigger: Trigger(
    name = "monki_regex",
    description = "Sends a monki ascii when theres a message that reminds of our ape friend"
), Loggable {
    val regex = Regex(".*m.*o.*n.*k.*e.*y.*|.*monki.*|.*m.*a.*c.*a.*c.*o.*|.*mamaco.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                this.message.channel.createMessage(MONKI_ASCII.random())
            }
        }
    }
}
