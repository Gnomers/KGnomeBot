package bot.command

import dev.kord.core.event.message.MessageCreateEvent

sealed class Command(
    val name: String,
    val description: String
) {
    abstract suspend fun invoke(event: MessageCreateEvent)
}
