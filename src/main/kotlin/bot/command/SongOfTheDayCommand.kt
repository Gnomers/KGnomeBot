package bot.command

import bot.constants.IMPORTANT_SONGS
import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent

class SongOfTheDayCommand: Command(
    name = "sotd",
    description = "Plays one of the good ol' songs"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        val songOfTheDay = IMPORTANT_SONGS.random()
        SoundPlayerManager.playYoutubeForMessage(event, songOfTheDay)
    }
}