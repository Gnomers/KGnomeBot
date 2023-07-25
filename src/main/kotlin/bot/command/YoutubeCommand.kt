package bot.command

import bot.core.voice.SoundPlayerManager
import dev.kord.core.event.message.MessageCreateEvent

class YoutubeCommand: Command(
    name = "youtube",
    description = "Let Gnome practice his DJ skills!"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        val message = event.message.content.split("youtube ", limit = 2)[1]
        SoundPlayerManager.playYoutubeForMessage(event, message)
    }
}