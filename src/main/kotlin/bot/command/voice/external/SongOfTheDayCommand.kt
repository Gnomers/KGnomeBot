package bot.command.voice.external

import bot.command.Command
import bot.constants.IMPORTANT_SONGS
import bot.core.voice.SoundPlayerManager
import bot.utilities.ofTheDay
import dev.kord.core.event.message.MessageCreateEvent


object SongOfTheDayCommand: Command(
    name = "sotd",
    description = "Plays one of the good ol' songs"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        val songOfTheDay = IMPORTANT_SONGS.ofTheDay()
        SoundPlayerManager.playYoutubeForMessage(event, songOfTheDay)
    }
}