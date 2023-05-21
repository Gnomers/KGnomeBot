package bot.command

import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent

class StopCommand: Command(
    name = "stop",
    description = "Stops current sound"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        SoundPlayerManager.stop()
    }
}