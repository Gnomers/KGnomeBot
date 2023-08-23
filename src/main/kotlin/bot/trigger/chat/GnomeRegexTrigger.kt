package bot.trigger.chat

import bot.constants.GNOME_ASCII
import bot.trigger.Trigger
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object GnomeRegexTrigger: Trigger(
    name = "gnome_regex",
    description = "Sends a gnome ascii when theres a `g*n*o*m*e` message"
) {
    // I have no idea what I'm doing
    val regex = Regex(".*g.*n.*o.*m.*e.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                this.message.channel.createMessage(GNOME_ASCII.random())
            }
        }
    }
}
