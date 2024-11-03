package bot.core.service.guachai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GuachAIResponse(
    val response: String
)