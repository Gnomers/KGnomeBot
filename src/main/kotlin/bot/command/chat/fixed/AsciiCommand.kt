package bot.command.chat.fixed

import bot.command.Command
import bot.constants.GNOME_ASCII
import bot.constants.MONKI_ASCII
import dev.kord.core.event.message.MessageCreateEvent

object AsciiCommand: Command(
    name = "ascii",
    description = "Sends out a random ascii"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        val theChosenOne = (GNOME_ASCII + MONKI_ASCII).random()
        event.message.channel.createMessage(theChosenOne)
    }
}