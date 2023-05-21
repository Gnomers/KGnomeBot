package bot.command

import bot.trigger.Trigger
import dev.kord.core.event.message.MessageCreateEvent

class TriggersCommand: Command(
    name = "triggers",
    description = "Shows all current triggers"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        var response = StringBuilder()
        response.appendLine("Hello there, old chum! Here are the triggers on this bot:")
        Trigger::class.sealedSubclasses.forEach { clazz ->
            response.appendLine("${clazz.objectInstance?.name} -> ${clazz.objectInstance?.description}")
        }
        event.message.channel.createMessage(response.toString())
    }
}