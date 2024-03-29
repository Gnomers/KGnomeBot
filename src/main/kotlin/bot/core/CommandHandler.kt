package bot.core

import bot.command.Command
import bot.constants.NO_SUBCOMMAND
import bot.constants.UNKNOWN_COMMAND
import bot.core.exception.DuplicateClassExcpetion
import bot.logging.Loggable
import dev.kord.core.event.message.MessageCreateEvent
import org.reflections.Reflections

object CommandHandler : Loggable {
    // name -> command class
    val registeredCommands: MutableMap<String, Command> = mutableMapOf()

    fun registerCommands() {
        val subClasses = Reflections("bot.command").getSubTypesOf(Command::class.java)
        subClasses.forEach { clazz ->
            val instance = clazz.kotlin.objectInstance!!

            registeredCommands.getOrDefault(instance.name, null)?.let {
                // command already exists
                throw DuplicateClassExcpetion("Command ${instance.name} is duplicated")
            }
            logger.info("Adding \"${instance.name}\" command to the command list")
            registeredCommands[instance.name.lowercase()] = instance
        }

        logger.info("CommandHandler created with commands=${registeredCommands.keys}")
    }

    suspend fun handle(event: MessageCreateEvent, args: List<String>) {
        val message = event.message

        // identify command
        val sentCommand = args.firstOrNull()?.lowercase()
        sentCommand?.let {
            // invoke handler
            registeredCommands.getOrDefault(sentCommand, null)?.let {
                logger.info("Invoking command=${sentCommand} with content=\"${message.content}\"")

                val subCommand = runCatching {
                    event.message.content.split("${it.name} ", limit = 2)[1]
                }.getOrNull()


                if (!it.hasSubCommand || subCommand != null) {
                    it.invoke(event, subCommand)
                } else {
                    event.message.channel.createMessage(NO_SUBCOMMAND)
                }
            } ?: event.message.channel.createMessage(UNKNOWN_COMMAND)
        } ?: message.channel.createMessage(bot.constants.NO_COMMAND_ISSUED)
    }
}