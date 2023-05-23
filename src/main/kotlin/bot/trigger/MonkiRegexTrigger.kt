package bot.trigger

import bot.constants.MONKI_ASCII
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object MonkiRegexTrigger: Trigger(
    name = "monki_regex",
    description = "Sends a monki ascii when theres a `m*o*n*k` message"
) {
    val regex = Regex(".*m.*o.*n.*k.*", RegexOption.DOT_MATCHES_ALL)

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                this.message.channel.createMessage(MONKI_ASCII.random())
            }
        }
    }
}
