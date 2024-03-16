package bot.trigger.chat

import bot.constants.GOD_ASCII
import bot.logging.Loggable
import bot.trigger.Trigger
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object GodGauRegexTrigger: Trigger(
    name = "god_regex",
    description = "Sends a god, god gau ascii when theres a message containing god"
), Loggable {
    val regex = Regex(".*god.*", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent>(logger = logger) {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                this.message.channel.createMessage(GOD_ASCII.random())
            }
        }
    }
}
