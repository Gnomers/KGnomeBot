package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class GnomePowerCommand: Command(
    name = "power",
    description = "ADRENALINE IS PUMPING"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.GNOME_POWER)
    }
}