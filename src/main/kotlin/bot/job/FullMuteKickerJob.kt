package bot.job

import bot.kordInstance
import bot.utilities.disconnect
import bot.utilities.isFullMuted
import dev.kord.common.entity.Snowflake
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.toList


object FullMuteKickerJob : Job(
    name = "full_mute_kicker",
    description = "Kicks everyone who is fully muted (no mic and no sound)",
    // 1 hour
    executionDelaySeconds = 3600
) {

    val ROUNDS_BEFORE_KICKING = 1

    // Could've used a collection of VoiceStates, but it is not a good class to check for equality.
    // Also it would (PROBABLY) be reset if the user re-entered between the checks
    private val roundsMuted = hashMapOf<Snowflake, Int>()

    override suspend fun execute() {
        kordInstance
            .guilds
            .flatMapMerge { it -> it.voiceStates.also { println(it.toList()) } }
            .collect {
                val member = it.getMember()
                if (it.isFullMuted()) {
                    // kinda weird but ok
                    roundsMuted[it.userId] = roundsMuted.getOrDefault(it.userId, 0)

                    when {
                        roundsMuted[it.userId] == ROUNDS_BEFORE_KICKING -> {
                            roundsMuted[it.userId] = 0
                            member.disconnect()
                            logger.info("User ${member.username} was disconnected after of ${(ROUNDS_BEFORE_KICKING * executionDelaySeconds) / 60} minutes being fully muted.")
                        }

                        else -> {
                            roundsMuted[it.userId] = roundsMuted[it.userId]!! + 1
                            logger.info("Incremented rounds for ${it.userId}. Rounds: $roundsMuted")
                        }
                    }
                } else {
                    roundsMuted[it.userId] = 0
                }
            }
    }
}
