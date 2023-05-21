package bot.trigger

import bot.constants.GOD_ASCII
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object GodGauRegexTrigger: Trigger(
    name = "god_regex",
    description = "Sends out a god, god gau ascii when theres a message containing god"
) {
    val regex = Regex(".*god.*", RegexOption.DOT_MATCHES_ALL)

    override suspend fun register(kordInstance: Kord) {
        kordInstance.on<MessageCreateEvent> {
            if (message.author?.isBot == true) return@on
            val message = this.message.content
            if (message.matches(regex)) {
                this.message.channel.createMessage(GOD_ASCII.random())
            }
        }
    }
}
