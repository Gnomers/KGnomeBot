package bot.job

import bot.kordInstance
import bot.utilities.disconnect
import bot.utilities.isFullMuted
import dev.kord.common.entity.Snowflake
import kotlinx.coroutines.flow.flatMapMerge


object FullMuteKickerJob : Job(
    name = "full_mute_kicker",
    description = "Kicks everyone who is fully muted (no mic and no sound)",
    // 40 min
    executionDelaySeconds = 2400
) {

    val ROUNDS_BEFORE_KICKING = 1

    // Could've used a collection of VoiceStates, but it is not a good class to check for equality.
    // Also, it would (PROBABLY) be reset if the user re-entered between the checks
    private val roundsMuted = hashMapOf<Snowflake, Int>()

    override suspend fun execute() {
        kordInstance
            .guilds
            .flatMapMerge { it.voiceStates }
            .collect {
                val member = it.getMember()
                if (it.isFullMuted()) {
                    // kinda weird but ok
                    roundsMuted[it.userId] = roundsMuted.getOrDefault(it.userId, 0)

                    // they gotta go
                    if (roundsMuted[it.userId] == ROUNDS_BEFORE_KICKING) {
                        logger.info("[FullMuteKickerJob] User ${member.username} is about to be disconnected.") // log for troubleshooting purposes
                        roundsMuted[it.userId] = 0
                        val postDisconnect = member.disconnect()
                        logger.info("[FullMuteKickerJob] User ${member.username} was disconnected after of ${(ROUNDS_BEFORE_KICKING * executionDelaySeconds) / 60} minutes being fully muted. postDisconnectMember=$postDisconnect")
                    } else {
                        // not their final life, increase count
                        roundsMuted[it.userId] = roundsMuted[it.userId]!! + 1
                    }
                } else {
                    // not muted, reset count
                    roundsMuted[it.userId] = 0
                }
            }
    }
}

