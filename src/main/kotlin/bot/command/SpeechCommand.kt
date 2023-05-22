package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class SpeechCommand: Command(
    name = "speech",
    description = "Says the famous \"I'm gnot a gnelf\" speech"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.SPEECH)
    }
}