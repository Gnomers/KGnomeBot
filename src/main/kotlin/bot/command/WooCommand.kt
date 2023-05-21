package bot.command

import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

class WooCommand: Command(
    name = "woo",
    description = "woos"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.playSoundForMessage(event = event, sound = Sound.WOO)
    }
}