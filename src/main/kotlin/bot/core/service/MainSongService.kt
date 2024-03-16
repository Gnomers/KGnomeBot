package bot.core.service

import bot.logging.Loggable

object MainSongService : Loggable {
    private var mainSong: String? = null

    fun getSong() = mainSong

    fun setSong(song: String) {
        logger.info("Song of the day is being set to song=${song}")
        mainSong = song
    }
}
