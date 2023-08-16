package bot.command.voice.simple

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class PeidaoCommand: Command(
    name = "peidao",
    description = "DEU PRA OUVIR?"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.BIG_OL_FART)
    }
}