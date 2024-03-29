package bot.command.voice.simple

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import bot.utilities.Sound
import dev.kord.core.event.message.MessageCreateEvent

object RandomSoundCommand: Command(
    name = "random",
    description = "Plays a random sound"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playSoundForMessage(event, Sound.values().random())
    }
}