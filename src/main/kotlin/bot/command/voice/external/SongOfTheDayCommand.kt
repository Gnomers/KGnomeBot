package bot.command.voice.external

import bot.command.Command
import bot.constants.IMPORTANT_SONGS
import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent
import java.time.LocalDate
import kotlin.random.Random


class SongOfTheDayCommand: Command(
    name = "sotd",
    description = "Plays one of the good ol' songs"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        val songOfTheDay = IMPORTANT_SONGS.random(Random(LocalDate.now().dayOfYear))
        SoundPlayerManager.playYoutubeForMessage(event, songOfTheDay)
    }
}