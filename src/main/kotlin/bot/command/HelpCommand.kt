package bot.command

import bot.core.CommandHandler
import dev.kord.core.event.message.MessageCreateEvent

class HelpCommand: Command(
    name = "help",
    description = "displays information on all commands"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        var response = StringBuilder()
        response.appendLine("Hello there, old chum! Here are the commands and their descriptions:")
        CommandHandler.registeredCommands.forEach {
            // who cares about commands with multiple arguments?
            response.appendLine("!gnome ${it.key} -> ${it.value.description}")
        }
        event.message.channel.createMessage(response.toString())
    }
}