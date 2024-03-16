package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object SonOfBeitchRegexTrigger: Trigger(
    name = "son_of_beitch",
    description = "Cute speech from a cute character from Half-Life's mod \"They Hunger\""
), Loggable {
    val regex = Regex(".*fdp.*|.*bitch.*|.*puta.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                SoundPlayerManager.playSoundForMessage(this, Sound.SON_OF_BEITCH)
            }
        }
    }
}
