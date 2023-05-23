package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class PeidaoCommand: Command(
    name = "peidao",
    description = "DEU PRA OUVIR?"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.BIG_OL_FART)
    }
}