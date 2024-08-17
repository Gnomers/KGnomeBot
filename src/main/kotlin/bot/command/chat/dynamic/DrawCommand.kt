package bot.command.chat.dynamic

import bot.command.Command
import bot.core.exception.TryAgainLaterException
import bot.core.service.imagedraw.ImageDrawingService
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.channel.withTyping
import dev.kord.core.event.message.MessageCreateEvent
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.utils.io.ByteReadChannel
import java.net.SocketTimeoutException

object DrawCommand: Command(
    name = "draw",
    description = "Let gnome show you his artistic skills",
    hasSubCommand = true
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        runCatching {
            event.message.channel.withTyping {
                val imagesMap = ImageDrawingService.draw(subCommand!!)
                event.message.channel.createMessage {
                    imagesMap.forEach {
                        this.addFile("image-${it.key}.jpeg", ChannelProvider { ByteReadChannel(it.value.data) })
                    }
                }
            }
        }.onFailure {
            when(it) {
                is TryAgainLaterException, is SocketTimeoutException -> event.message.channel.createMessage(it.message ?: "He he ha, try again later")
                else -> event.message.channel.createMessage("He he, something wrong happened, couldn't draw that for ye")
            }
        }
    }
}
