package bot.core

import bot.command.Command
import bot.constants.UNKNOWN_COMMAND
import bot.core.exception.DuplicateCommandExcpetion
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.kordLogger

object CommandHandler {
    // name -> command class
    val registeredCommands: MutableMap<String, Command> = mutableMapOf()

    init {
        kordLogger.info("Starting CommandHandler")
        Command::class.sealedSubclasses.forEach { clazz ->
            val instance = clazz.constructors.first { it.parameters.isEmpty() }.call()

            registeredCommands.getOrDefault(instance.name, null)?.let {
                // command already exists
                throw DuplicateCommandExcpetion("Command ${instance.name} is duplicated")
            }
            kordLogger.info("Adding \"${instance.name}\" command to the command list")
            registeredCommands[instance.name.lowercase()] = instance
        }

        kordLogger.info("CommandHandler created with commands=${registeredCommands.keys}")
    }

    suspend fun handle(event: MessageCreateEvent, args: List<String>) {
        val message = event.message

        // identify command
        val sentCommand = args.firstOrNull()?.lowercase()
        sentCommand?.let {
            // invoke handler
            registeredCommands.getOrDefault(sentCommand, null)?.let {
                kordLogger.info("Invoking command=${sentCommand} with content=\"${message.content}\"")
                it.invoke(event)
            } ?: event.message.channel.createMessage(UNKNOWN_COMMAND)
        } ?: message.channel.createMessage(bot.constants.NO_COMMAND_ISSUED)
    }
}