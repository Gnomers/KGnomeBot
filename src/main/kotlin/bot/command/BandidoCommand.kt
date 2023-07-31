package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class BandidoCommand: Command(
    name = "bandido",
    description = "nhiahiahia"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.BANDIDO)
    }
}