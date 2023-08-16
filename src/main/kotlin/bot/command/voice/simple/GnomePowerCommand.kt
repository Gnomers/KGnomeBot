package bot.command.voice.simple

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class GnomePowerCommand: Command(
    name = "power",
    description = "ADRENALINE IS PUMPING"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.GNOME_POWER)
    }
}