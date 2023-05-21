package bot.trigger

import bot.constants.MONKI_ASCII
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object MonkiRegexTrigger: Trigger(
    name = "Monki Regex",
    description = "Sends out a monki ascii when theres a m*o*n*k message"
) {
    val regex = Regex(".*m.*o.*n.*k.*", RegexOption.DOT_MATCHES_ALL)

    override suspend fun register(kordInstance: Kord) {
        kordInstance.on<MessageCreateEvent> {
            val message = this.message.content
            if (message.matches(regex)) {
                this.message.channel.createMessage(MONKI_ASCII.random())
            }
        }
    }
}
