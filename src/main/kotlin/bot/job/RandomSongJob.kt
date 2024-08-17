package bot.job

import bot.constants.IMPORTANT_SONGS
import bot.core.voice.SoundPlayerManager
import bot.kordInstance
import bot.utilities.getFirstPopulatedChannel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlin.random.Random


object RandomSongJob: Job(
    name = "random_song",
    description = "Plays a random song from a list",
    executionDelaySeconds = 3600
) {
    val RANDOM_SOUND_CHANCE = 0.1 // 10%
    override suspend fun execute() {

        val luckySong = IMPORTANT_SONGS.random()
        // Random.nextDouble() returns a double from 0 (inclusive) to 1 (exclusive)
        if(Random.nextDouble() < RANDOM_SOUND_CHANCE) {
            // for every guild this server is on, get a channel with a VoiceState
            kordInstance
                .guilds
                .map { it.getFirstPopulatedChannel() }
                .filterNotNull()
                .collect {
                    SoundPlayerManager.playYoutubeVideoOnChannel(voiceChannel = it, video = luckySong)
                }
        }
    }
}
