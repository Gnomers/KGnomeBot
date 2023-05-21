package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class MonkiCommand: Command(
    name = "monki",
    description = "hmm, monki"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.HM_MONKI)
    }
}