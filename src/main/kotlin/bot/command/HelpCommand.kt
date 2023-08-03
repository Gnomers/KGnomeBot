package bot.command

import bot.constants.GNOME_COMMAND_PREFIX
import bot.core.CommandHandler
import dev.kord.core.event.message.MessageCreateEvent

class HelpCommand: Command(
    name = "help",
    description = "Displays information on all commands"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        var response = StringBuilder()
        response.appendLine("Hello there, old chum! Here are the commands and their descriptions:")
        CommandHandler.registeredCommands.forEach {
            // who cares about commands with multiple arguments?
            response.appendLine("$GNOME_COMMAND_PREFIX ${it.key} -> ${it.value.description}")
        }
        event.message.channel.createMessage(response.toString())
    }
}