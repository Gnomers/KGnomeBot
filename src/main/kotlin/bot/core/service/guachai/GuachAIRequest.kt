package bot.core.service.guachai

data class GuachAIRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean,
    val options: Options
) {
    data class Options(
        val temperature: Float
    )
}