package bot.core.service.awanllm

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AwanLLMResponse(
    val id: String,
    @JsonProperty("object")
    val obj: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Choice(
        val index: Int,
        val text: String,
    )
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Usage(
        val promptTokens: Int,
        val totalTokens: Int,
        val completionTokens: Int
    )
}