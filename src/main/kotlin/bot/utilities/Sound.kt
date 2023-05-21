package bot.utilities

import com.sedmelluq.discord.lavaplayer.container.ogg.OggAudioTrack
import com.sedmelluq.discord.lavaplayer.source.local.LocalSeekableInputStream
import com.sedmelluq.discord.lavaplayer.track.AudioReference
import com.sedmelluq.discord.lavaplayer.track.info.AudioTrackInfoBuilder
import java.io.File


enum class Sound(val file: File) {
    WOO("/audio/woo.ogg".toFile()),
    SPEECH("/audio/speech.ogg".toFile()),
    WOHOHO("/audio/wohoho.ogg".toFile()),
    HM_MONKI("/audio/monki.ogg".toFile()),
    GNOME_POWER("/audio/gnome_power.ogg".toFile());

    fun getTrack(): OggAudioTrack =
        this.file.toOggAudioTrack()
}

private fun String.toFile(): File =
    File(Sound::class.java.getResource(this).file)

private fun File.toOggAudioTrack(): OggAudioTrack {
    val localSeekableInputStream = LocalSeekableInputStream(this)
    val trackInfo = AudioTrackInfoBuilder.create(AudioReference.NO_TRACK, localSeekableInputStream).build()

    return OggAudioTrack(trackInfo, localSeekableInputStream)
}