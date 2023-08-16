package bot.job

import bot.constants.IMPORTANT_SONGS
import bot.core.voice.SoundPlayerManager
import bot.getKordInstance
import bot.utilities.getFirstPopulatedChannel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import kotlin.random.Random


class RandomSongJob: Job(
    name = "random_song",
    description = "Plays a random song from a list",
    executionDelaySeconds = 3600
) {
    val RANDOM_SOUND_CHANCE = 0.08 // 8%
    override suspend fun execute() {

        val songOfTheDay = IMPORTANT_SONGS.random(Random(LocalDate.now().dayOfYear))
        // Random.nextDouble() returns a double from 0 (inclusive) to 1 (exclusive)
        if(Random.nextDouble() < RANDOM_SOUND_CHANCE) {
            // for every guild this server is on, get a channel with a VoiceState
            getKordInstance()
                .guilds
                .map { it.getFirstPopulatedChannel() }
                .filterNotNull()
                .collect {
                    SoundPlayerManager.playYoutubeVideoOnChannel(voiceChannel = it, video = songOfTheDay)
                }

        }
    }
}
