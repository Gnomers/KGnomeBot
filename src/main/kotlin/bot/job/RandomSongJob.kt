package bot.job

//TODO YouTube integration stopped working... Need to revisit this
//object RandomSongJob: Job(
//    name = "random_song",
//    description = "Plays a random song from a list",
//    executionDelaySeconds = 3600
//) {
//    val RANDOM_SOUND_CHANCE = 0.1 // 10%
//    override suspend fun execute() {
//
//        val luckySong = IMPORTANT_SONGS.random()
//        // Random.nextDouble() returns a double from 0 (inclusive) to 1 (exclusive)
//        if(Random.nextDouble() < RANDOM_SOUND_CHANCE) {
//            // for every guild this server is on, get a channel with a VoiceState
//            kordInstance
//                .guilds
//                .map { it.getFirstPopulatedChannel() }
//                .filterNotNull()
//                .collect {
//                    SoundPlayerManager.playYoutubeVideoOnChannel(voiceChannel = it, video = luckySong)
//                }
//        }
//    }
//}
