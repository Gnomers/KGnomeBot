package bot.job

import bot.core.voice.SoundPlayerManager
import bot.getKordInstance
import bot.utilities.Sound
import bot.utilities.getFirstPopulatedChannel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlin.random.Random


class RandomSoundJob: Job(
    name = "random_sound_job",
    description = "Plays a random sound",
    executionDelaySeconds = 600
) {
    val RANDOM_SOUND_CHANCE = 0.15
    val SOUNDS = listOf(Sound.WOO, Sound.CS, Sound.HM_MONKI, Sound.BIG_OL_FART)
    override suspend fun execute() {
        // Random.nextDouble() returns a double from 0 (inclusive) to 1 (exclusive)
        if(Random.nextDouble() < RANDOM_SOUND_CHANCE) {
            // for every guild this server is on, get a channel with a VoiceState
            getKordInstance()
                .guilds
                .map { it.getFirstPopulatedChannel() }
                .filterNotNull()
                .collect {
                    SoundPlayerManager.playSoundOnChannel(it, SOUNDS.random())
                }

        }
    }
}
