package bot.core.service

import dev.kord.core.kordLogger

object MainSongService {
    private var mainSong: String? = null

    fun getSong() = mainSong

    fun setSong(song: String) {
        kordLogger.info("Song of the day is being set to song=${song}")
        mainSong = song
    }
}
