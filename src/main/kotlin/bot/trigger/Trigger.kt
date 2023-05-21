package bot.trigger

import dev.kord.core.Kord

sealed class Trigger(
    val name: String,
    val description: String
) {
    abstract suspend fun register(kordInstance: Kord)
}