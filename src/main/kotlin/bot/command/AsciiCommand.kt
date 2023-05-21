package bot.command

import bot.constants.GNOME_ASCII
import bot.constants.MONKI_ASCII
import dev.kord.core.event.message.MessageCreateEvent

class AsciiCommand: Command(
    name = "ascii",
    description = "sends out a random ascii"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        val theChosenOne = (GNOME_ASCII + MONKI_ASCII).random()
        event.message.channel.createMessage(theChosenOne)
    }
}