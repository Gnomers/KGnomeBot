package bot.utilities

import dev.kord.core.entity.VoiceState


fun changedMute(oldState: VoiceState?, newState: VoiceState) =
    oldState?.let {
        oldState.isSelfMuted != newState.isSelfMuted ||
                oldState.isMuted != newState.isMuted
    } ?: false // if oldState is null, they couldn't change an in-voice state

fun changedDeaf(oldState: VoiceState?, newState: VoiceState) =
    oldState?.let {
        oldState.isSelfDeafened != newState.isSelfDeafened ||
                oldState.isDeafened != newState.isDeafened
    } ?: false // if oldState is null, they couldn't change an in-voice state

fun changedStreaming(oldState: VoiceState?, newState: VoiceState) =
    oldState?.let {
        oldState.isSelfStreaming != newState.isSelfStreaming
    } ?: false // if oldState is null, they couldn't change an in-voice state

fun changedVideo(oldState: VoiceState?, newState: VoiceState) =
    oldState?.let {
        oldState.isSelfVideo != newState.isSelfVideo
    } ?: false // if oldState is null, they couldn't change an in-voice state

// this does not consider connects or disconnects
fun isCommonVoiceChatAction(oldState: VoiceState?, newState: VoiceState) =
    changedDeaf(oldState = oldState, newState = newState) ||
        changedMute(oldState = oldState, newState = newState) ||
        changedStreaming(oldState = oldState, newState = newState) ||
        changedVideo(oldState = oldState, newState = newState)

suspend fun isDisconnect(oldState: VoiceState?, newState: VoiceState) =
    oldState?.getChannelOrNull() != null && newState.getChannelOrNull() == null