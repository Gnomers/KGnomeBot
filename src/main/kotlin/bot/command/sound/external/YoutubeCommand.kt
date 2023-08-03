package bot.command.sound.external

import bot.command.Command
import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent

class YoutubeCommand: Command(
    name = "youtube",
    description = "Let Gnome practice his DJ skills!",
    hasSubCommand = true
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        SoundPlayerManager.playYoutubeForMessage(event, subCommand!!)
    }
}