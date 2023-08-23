package bot.command.chat.fixed

import bot.command.Command
import bot.trigger.Trigger
import dev.kord.core.event.message.MessageCreateEvent
import org.reflections.Reflections

object ShowTriggersCommand: Command(
    name = "triggers",
    description = "Shows all current triggers"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        var response = StringBuilder()
        val subClasses = Reflections("bot.trigger").getSubTypesOf(Trigger::class.java)
        response.appendLine("Hello there, old chum! Here are the triggers on this bot:")
        subClasses.forEach { clazz ->
            response.appendLine("${clazz.kotlin.objectInstance?.name} -> ${clazz.kotlin.objectInstance?.description}")
        }
        event.message.channel.createMessage(response.toString())
    }
}