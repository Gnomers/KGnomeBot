package bot.command

import bot.core.service.imagedescriptor.ImageDescriptorService
import bot.utilities.isValidURL
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent

class DescribeCommand: Command(
    name = "describe",
    description = "Describes an image"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        event.message.channel.withTyping {
            val message = event.message
            val attachment = message.attachments.firstOrNull()
            val url = attachment?.let {
                if (it.isImage) it.url
                else null
            } ?: message.content.split("describe ", limit = 2).last()

            if (url.isValidURL()) {
                val description = ImageDescriptorService.describe(url)
                event.message.channel.createMessage(description)
            } else {
                event.message.channel.createMessage("Ho ho ho ha ha, you can't gnome me! Please provide an image or a valid image URL.")
            }
        }
    }
}