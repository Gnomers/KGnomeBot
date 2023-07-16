package bot.command

import bot.core.service.chatgpt.ChatGPTService
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent

class ChatGPTCommand: Command(
    name = "chat",
    description = "Talk with a REAL GNOME - definitely not an AI"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        val message = event.message.content.split("chat ", limit = 2)[1]
        event.message.channel.withTyping {
            event.message.channel.createMessage(ChatGPTService.prompt(message))
        }
    }
}