package bot.command.chat.dynamic

import bot.command.Command
import bot.core.service.chatgpt.ChatGPTService
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent

object ChatGPTCommand: Command(
    name = "gpt",
    description = "Boring chat without the Gnome magic.",
    hasSubCommand = true
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        event.message.channel.withTyping {
            event.message.channel.createMessage(ChatGPTService.prompt(subCommand!!, false))
        }
    }
}