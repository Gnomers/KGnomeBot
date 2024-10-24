package bot.command.chat.fixed

import bot.command.Command
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

object ListSoundsCommand: Command(
    name = "sounds",
    description = "Lists all current sounds"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        var response = StringBuilder()
        response.appendLine("Hello there, old chum! Here are the sounds on this bot:")
        Sound.entries.sorted().forEach { sound ->
            response.appendLine(sound.name)
        }
        event.message.channel.createMessage(response.toString())
    }
}