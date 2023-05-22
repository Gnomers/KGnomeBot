package bot.utilities

import com.sedmelluq.discord.lavaplayer.container.ogg.OggAudioTrack
import com.sedmelluq.discord.lavaplayer.source.local.LocalSeekableInputStream
import com.sedmelluq.discord.lavaplayer.track.AudioReference
import com.sedmelluq.discord.lavaplayer.track.info.AudioTrackInfoBuilder
import java.io.File
import java.io.FileOutputStream


enum class Sound(val path: String) {
    WOO("/audio/woo.ogg"),
    SPEECH("/audio/speech.ogg"),
    LAUGH("/audio/wohoho.ogg"),
    HM_MONKI("/audio/monki.ogg"),
    CS("/audio/ce_ess.ogg"),
    GNOME_POWER("/audio/gnome_power.ogg");

    fun getTrack(): OggAudioTrack =
        accessResourceAsFile(this.path)
            .toOggAudioTrack()

}
private fun File.toOggAudioTrack(): OggAudioTrack {
    val localSeekableInputStream = LocalSeekableInputStream(this)
    val trackInfo = AudioTrackInfoBuilder.create(AudioReference.NO_TRACK, localSeekableInputStream).build()

    return OggAudioTrack(trackInfo, localSeekableInputStream)
}

private fun accessResourceAsFile(resourceName: String): File {
    // If it doesn't exist, please fix it
    val resourceUrl = object {}.javaClass.getResource(resourceName)!!

    val inputStream = resourceUrl.openStream()
    val tempFile = File.createTempFile("temp", null)

    FileOutputStream(tempFile).use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    return tempFile
}