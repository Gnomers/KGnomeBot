package bot.utilities

import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.kordLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


// Disgusting extension, needs refac
public inline fun <reified T : Event> Kord.onIgnoringBots(
    scope: CoroutineScope = this,
    noinline consumer: suspend T.() -> Unit
): Job =
    events.buffer(Channel.UNLIMITED).filterIsInstance<T>()
        // ignores the message event if the author is a bot
        .filterNot { it is MessageCreateEvent && it.message.author?.isBot == true }
        // ignores the voice channel event if the member is a bot
        .filterNot { it is VoiceStateUpdateEvent && it.state.getMember().isBot }
        .onEach { event ->
            scope.launch { runCatching { consumer(event) }.onFailure { kordLogger.catching(it) } }
        }
        .launchIn(scope)

suspend fun Guild.getFirstPopulatedChannel() = this.voiceStates.firstOrNull()?.getChannelOrNull()