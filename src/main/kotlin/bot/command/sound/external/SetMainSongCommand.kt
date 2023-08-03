package bot.command.sound.external

import bot.command.Command
import bot.core.service.MainSongService
import dev.kord.core.event.message.MessageCreateEvent


class SetMainSongCommand: Command(
    name = "setsong",
    description = "Sets the main song for Gnome to play",
    hasSubCommand = true
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        MainSongService.setSong(subCommand!!)
        event.message.channel.createMessage("Gnome now has its main song set to \"${subCommand}\"")
    }
}