package bot.job

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


sealed class Job(
    val name: String,
    val description: String,
    val executionDelaySeconds: Long // the job will execute every X seconds
) {

    private var registered = false

    // Called by the handler, no one should override this one
    final fun register() {
        // do not allow multiple registrations
        if(registered) return

        val executionInMillis = executionDelaySeconds * 1000

        CoroutineScope( Dispatchers.Default ).async {
            if (executionInMillis > 0) {
                while (isActive) {
                    execute()
                    delay(executionInMillis)
                }
            } else {
                execute()
            }
        }

        registered = true
    }

    // Implementations must override this one
    abstract suspend fun execute()
}
