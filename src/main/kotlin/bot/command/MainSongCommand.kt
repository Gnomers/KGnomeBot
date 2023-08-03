package bot.command

import bot.core.service.MainSongService
import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent


class MainSongCommand: Command(
    name = "song",
    description = "Plays the latest hit"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        val song = MainSongService.getSong()
        song?.let {
            SoundPlayerManager.playYoutubeForMessage(event, it)
        } ?: event.message.channel.createMessage("There is no song set yet. Set a new song with \"!gnome setsong NEW_SONG_HERE\"!")
    }
}