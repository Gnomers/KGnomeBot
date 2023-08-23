package bot.command.voice.external

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent

object PlayCommand: Command(
    name = "play",
    description = "Let Gnome practice his DJ skills!",
    hasSubCommand = true
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playYoutubeForMessage(event, subCommand!!)
    }
}