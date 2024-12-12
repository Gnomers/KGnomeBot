package bot.utilities

import bot.core.exception.UnknownFileFormatExcpetion
import com.sedmelluq.discord.lavaplayer.container.mp3.Mp3AudioTrack
import com.sedmelluq.discord.lavaplayer.container.ogg.OggAudioTrack
import com.sedmelluq.discord.lavaplayer.tools.io.NonSeekableInputStream
import com.sedmelluq.discord.lavaplayer.track.AudioReference
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.info.AudioTrackInfoBuilder

// TODO refactor to make this sound loading thing a bit more dynamic by reading files
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
    YURI_MARTINS("audio/yuri_martins.mp3"),
    VIGOR_133X("audio/vigor_133x.mp3"),
    QUE_CHEIRO("audio/que_cheiro.mp3"),
    PALMEIRAS("audio/palmeiras.mp3"),
    RONALDINHO_SOCCER("audio/ronaldinho_soccer.mp3"),
    JOHN_ELDEN_RING("audio/john_elden_ring.mp3"),
    E_O_PIX("audio/e_o_pix.mp3"),
    SHADOW_WIZARD("audio/shadow_wizard.mp3"),
    USSR_ANTHEM("audio/ussr_anthem.mp3"),
    MORTAL_KOMBAT("audio/mortal_kombat.mp3"),
    THANKS_FOR_THE_RIDE("audio/thanks_for_the_ride.mp3"),
    LEPRECHAUN("audio/leprechaun.mp3"),
    SON_OF_BEITCH("audio/son_of_beitch.mp3"),
    PIPIPI("audio/pipipi.mp3"),
    AMOGUS("audio/amogus.mp3"),
    IN_NOMINE_PATRIS("audio/in_nomine_patris.mp3"),
    TA_LIGADO("audio/ta_ligado.mp3"),
    SEU_MADRUGA_NOSSA("audio/seu_madruga_nossa.mp3");

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
            HM_MONKI,
            BIG_OL_FART,
            AMOGUS,
            PALMEIRAS,
            THANKS_FOR_THE_RIDE,
            SON_OF_BEITCH,
            RAP_DO_VEGETA,
            PIPIPI,
            SEU_MADRUGA_NOSSA,
            TA_LIGADO,
            IN_NOMINE_PATRIS
        )
    }
}
