package bot.job

import bot.logging.Loggable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


abstract class Job(
    val name: String,
    val description: String,
    val executionDelaySeconds: Long // the job will execute every X seconds
): Loggable {

    private var registered = false

    // Called by the handler, no one should override this one
    final fun register() {
        // do not allow multiple registrations
        if(registered) return

        val executionInMillis = executionDelaySeconds * 1000

        CoroutineScope( Dispatchers.Default ).async {
            if (executionInMillis > 1000) {
                while (isActive) {
                    execute()
                    delay(executionInMillis)
                }
            } else {
                logger.info("Job of name=${name} will not be registered, as its delay would be less than a second.")
                execute()
            }
        }

        registered = true
    }

    // Implementations must override this one
    abstract suspend fun execute()
}
