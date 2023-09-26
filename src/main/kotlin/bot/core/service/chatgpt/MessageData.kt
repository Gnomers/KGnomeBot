package bot.core.service.chatgpt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageData(
    val role: String,
    val content: String
)