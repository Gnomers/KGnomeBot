package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class AmogusCommand: Command(
    name = "amogus",
    description = "sussy baka"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.AMOGUS)
    }
}