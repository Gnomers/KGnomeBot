package bot.trigger.chat

import bot.core.voice.SoundPlayerManager
import bot.trigger.Trigger
import bot.utilities.Sound
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object GreenRegexTrigger: Trigger(
    name = "green_regex",
    description = "\"A friend with weed is a friend indeed\" - Leprechaun"
) {
    val regex = Regex(".*green.*|.*weed.*|.*erva.*|.*maconha.*|.*verde.*|.*verdinha.*|.*marconha.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                SoundPlayerManager.playSoundForMessage(this, Sound.QUE_CHEIRO)
            }
        }
    }
}
