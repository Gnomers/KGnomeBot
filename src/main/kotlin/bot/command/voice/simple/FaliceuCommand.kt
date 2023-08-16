package bot.command.voice.simple

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class FaliceuCommand: Command(
    name = "faliceu",
    description = "Plays a \"Faliceu\""
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.FALICEU)
    }
}