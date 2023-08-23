package bot.command.chat.dynamic

import bot.command.Command
import bot.core.service.imagedescriptor.ImageDescriptorService
import bot.utilities.isValidURL
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent

private const val INVALID_IMAGE = "Ho ho ho ha ha, you can't gnome me! Please provide an image or a valid image URL."

object DescribeCommand: Command(
    name = "describe",
    description = "Describes an image",
    hasSubCommand = false
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        val message = event.message
        val attachment = message.attachments.firstOrNull()
        if (subCommand == null && attachment == null) {
            event.message.channel.createMessage(INVALID_IMAGE)
        }
        event.message.channel.withTyping {
            val attachment = attachment
            val url = attachment?.let {
                if (it.isImage) it.url
                else null
            } ?: subCommand!!

            if (url.isValidURL()) {
                val description = ImageDescriptorService.describe(url)
                event.message.channel.createMessage(description)
            } else {
                event.message.channel.createMessage(INVALID_IMAGE)
            }
        }
    }
}