package bot.trigger

import bot.constants.GOD_ASCII
import bot.utilities.isCommand
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent

object GodGauRegexTrigger: Trigger(
    name = "god_regex",
    description = "Sends a god, god gau ascii when theres a message containing god"
) {
    val regex = Regex(".*god.*", RegexOption.DOT_MATCHES_ALL)

    override suspend fun register(kordInstance: Kord) {
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            val message = this.message.content
            if (!message.isCommand() && message.matches(regex)) {
                this.message.channel.createMessage(GOD_ASCII.random())
            }
        }
    }
}
