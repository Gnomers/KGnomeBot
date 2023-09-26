package bot.core.service.chatgpt

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageResponse(
        val choices: List<ResponseData>
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ResponseData(
        val message: MessageData
    )
}