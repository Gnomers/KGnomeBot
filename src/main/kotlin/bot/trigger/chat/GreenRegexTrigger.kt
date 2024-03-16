package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object GreenRegexTrigger: Trigger(
    name = "green_regex",
    description = "\"A friend with weed is a friend indeed\" - Leprechaun"
), Loggable {
    val regex = Regex(".*green.*|.*weed.*|.*erva.*|.*maconha.*|.*verde.*|.*verdinha.*|.*marconha.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
    val POSSIBLE_SOUNDS = listOf(Sound.LEPRECHAUN, Sound.QUE_CHEIRO)
    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                val sound = POSSIBLE_SOUNDS.random()
                SoundPlayerManager.playSoundForMessage(this, sound)
            }
        }
    }
}
