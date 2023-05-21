package bot.trigger

import bot.constants.GNOME_ASCII
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object GnomeRegexTrigger: Trigger(
    name = "Gnome Regex",
    description = "Sends out a gnome ascii when theres a g*n*o*m*e message"
) {
    // I have no idea what I'm doing
    val regex = Regex(".*g.*n.*o.*m.*e.*", RegexOption.DOT_MATCHES_ALL)

    override suspend fun register(kordInstance: Kord) {
        kordInstance.on<MessageCreateEvent> {
            val message = this.message.content
            if (message.matches(regex)) {
                this.message.channel.createMessage(GNOME_ASCII.random())
            }
        }
    }
}
