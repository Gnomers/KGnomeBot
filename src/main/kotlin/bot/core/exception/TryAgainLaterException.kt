package bot.core.exception

class TryAgainLaterException(override val message: String): RuntimeException(message)