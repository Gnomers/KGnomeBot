package bot.command.voice.simple

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class MonkiCommand: Command(
    name = "monki",
    description = "HMM, MONKI"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.HM_MONKI)
    }
}