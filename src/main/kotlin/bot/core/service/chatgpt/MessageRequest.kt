package bot.core.service.chatgpt

data class MessageRequest(
        val messages: List<Data>,
        val time: Long,
        val sign: String
) {
    data class Data(
            val role: String,
            val content: String
    )
}