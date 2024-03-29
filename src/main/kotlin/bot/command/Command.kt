package bot.command

import dev.kord.core.event.message.MessageCreateEvent

abstract class Command(
    val name: String,
    val description: String,
    val hasSubCommand: Boolean = false
) {
    abstract suspend fun invoke(event: MessageCreateEvent, subCommand: String?)
}
