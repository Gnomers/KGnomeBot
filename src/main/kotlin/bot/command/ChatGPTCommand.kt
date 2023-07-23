package bot.command

import bot.core.service.chatgpt.ChatGPTService
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent

class ChatGPTCommand: Command(
    name = "gpt",
    description = "Boring chat without the Gnome magic."
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        val message = event.message.content.split("gpt ", limit = 2)[1]
        event.message.channel.withTyping {
            event.message.channel.createMessage(ChatGPTService.prompt(message, false))
        }
    }
}