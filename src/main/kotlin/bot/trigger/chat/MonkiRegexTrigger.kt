package bot.trigger.chat

import bot.constants.MONKI_ASCII
import bot.trigger.Trigger
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

class MonkiRegexTrigger: Trigger(
    name = "monki_regex",
    description = "Sends a monki ascii when theres a message that reminds of our ape friend"
) {
    val regex = Regex(".*m.*o.*n.*k.*e.*y.*|.*m.*o.*n.*k.*i.*|.*m.*a.*c.*a.*c.*o.*|.*m.*a.*m.*a.*c.*o.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                this.message.channel.createMessage(MONKI_ASCII.random())
            }
        }
    }
}
