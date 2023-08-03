package bot.command.chat.fixed

import bot.command.Command
import bot.trigger.Trigger
import dev.kord.core.event.message.MessageCreateEvent

class ShowTriggersCommand: Command(
    name = "triggers",
    description = "Shows all current triggers"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        var response = StringBuilder()
        response.appendLine("Hello there, old chum! Here are the triggers on this bot:")
        Trigger::class.sealedSubclasses.forEach { clazz ->
            response.appendLine("${clazz.objectInstance?.name} -> ${clazz.objectInstance?.description}")
        }
        event.message.channel.createMessage(response.toString())
    }
}