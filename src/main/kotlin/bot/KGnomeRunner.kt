/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package bot

import bot.constants.DISCORD_TOKEN_ENV_VAR_NAME
import bot.constants.GNOME_COMMAND_PREFIX
import bot.core.CommandHandler
import bot.core.JobRegistrator
import bot.core.TriggerRegistrator
import bot.utilities.onIgnoringBots
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.kordLogger
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

private lateinit var kordInstance: Kord

class KGnomeRunner
suspend fun main(vararg args: String) {

    kordInstance = Kord(token = args.firstOrNull()
        ?: System.getenv(DISCORD_TOKEN_ENV_VAR_NAME)
        ?: error("token required"))

    kordInstance.onIgnoringBots<MessageCreateEvent> {
        val content = this.message.content
        kordLogger.info("Incoming message \"${content}\"")
        if (content.startsWith(GNOME_COMMAND_PREFIX)) {
            CommandHandler.handle(
                event = this,
                args = content
                    .split(" ")
                    .drop(1)
            )
        }
    }

    TriggerRegistrator.registerTriggers()
    JobRegistrator.registerJobs()

    kordInstance.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}

fun getKordInstance() = kordInstance