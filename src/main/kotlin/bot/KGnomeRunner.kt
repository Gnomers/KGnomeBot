/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package bot

import bot.constants.DISCORD_TOKEN_ENV_VAR
import bot.constants.GNOME_COMMAND_PREFIXES
import bot.core.CommandHandler
import bot.core.JobRegistrator
import bot.core.TriggerRegistrator
import bot.utilities.onIgnoringBots
import bot.utilities.startsWithAny
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.kordLogger
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import io.github.cdimascio.dotenv.dotenv
import java.util.*


private lateinit var kordInstance: Kord

class KGnomeRunner

suspend fun main(vararg args: String) {
    Locale.setDefault(Locale("pt", "BR"))

    kordInstance = Kord(
        token = args.firstOrNull()?.also { kordLogger.info("$DISCORD_TOKEN_ENV_VAR taken from VM args") }
            ?: dotenv()[DISCORD_TOKEN_ENV_VAR]?.also { kordLogger.info("$DISCORD_TOKEN_ENV_VAR found in dotenv") }
            ?: error("token required")
    )

    kordInstance.onIgnoringBots<MessageCreateEvent> {
        val content = this.message.content
        kordLogger.info("Incoming message \"${content}\"")
        if (content.startsWithAny(GNOME_COMMAND_PREFIXES)) {
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
    CommandHandler.registerCommands()

    kordInstance.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}


fun getKordInstance() = kordInstance