package bot.command

import bot.core.service.chatgpt.ChatGPTService
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent

class GnomeChatCommand: Command(
    name = "chat",
    description = "Talk with a REAL GNOME - definitely not an AI",
    hasSubCommand = true
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        event.message.channel.withTyping {
            event.message.channel.createMessage(ChatGPTService.prompt(subCommand!!, true))
        }
    }
}
