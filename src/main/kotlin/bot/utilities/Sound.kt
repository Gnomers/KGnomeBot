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
    RAP_DO_VEGETA("audio/rap_do_vegeta.mp3"),
    PALMEIRAS("audio/palmeiras.mp3"),
    RONALDINHO_SOCCER("audio/ronaldinho_soccer.mp3"),
    JOHN_ELDEN_RING("audio/john_elden_ring.mp3"),
    E_O_PIX("audio/e_o_pix.mp3"),
    USSR_ANTHEM("audio/ussr_anthem.mp3"),
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

    companion object {
        val COMMON_SOUND_LIST = listOf(
            WOO,
            CS,
            HM_MONKI,
            BIG_OL_FART,
            AMOGUS,
            PALMEIRAS,
            FALICEU,
            RONALDINHO_SOCCER,
            JOHN_ELDEN_RING
        )
    }
}
