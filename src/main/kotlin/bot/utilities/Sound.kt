package bot.utilities

import bot.core.exception.UnknownFileFormatExcpetion
import com.sedmelluq.discord.lavaplayer.container.mp3.Mp3AudioTrack
import com.sedmelluq.discord.lavaplayer.container.ogg.OggAudioTrack
import com.sedmelluq.discord.lavaplayer.tools.io.NonSeekableInputStream
import com.sedmelluq.discord.lavaplayer.track.AudioReference
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.info.AudioTrackInfoBuilder


enum class Sound(val path: String) {
    WOO("audio/woo.ogg"),
    BIG_OL_FART("audio/big_ol_fart.ogg"),
    HM_MONKI("audio/monki.ogg"),
    CS("audio/ce_ess.ogg"),
    GNOME_POWER("audio/gnome_power.ogg"),
    BANDIDO("audio/risadinha_de_bandido.mp3"),
    BALBOA("audio/balboa.mp3"),
    FALICEU("audio/faliceu.mp3"),
    AMOGUS("audio/amogus.mp3");

    fun getTrack(): AudioTrack {
        val nonSeekableInputStream = NonSeekableInputStream(
            this::class.java.classLoader.getResourceAsStream(this.path)
        )
        val audioTrackInfo = AudioTrackInfoBuilder.create(AudioReference.NO_TRACK, nonSeekableInputStream).build()

        return when(this.path.split(".").last()) {
            "ogg" -> OggAudioTrack(
                audioTrackInfo,
                nonSeekableInputStream
            )

            "mp3" -> Mp3AudioTrack(
                audioTrackInfo,
                nonSeekableInputStream
            )

            else -> throw UnknownFileFormatExcpetion("File format \"${this.path}\" unrecognized")
        }
    }
}