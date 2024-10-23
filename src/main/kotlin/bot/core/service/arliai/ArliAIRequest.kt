package bot.core.service.awanllm

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ArliAIRequest(
    val model: String,
    val prompt: String,
    val repetitionPenalty: Double,
    val temperature: Float,
    val topP: Double,
    val topK: Int,
    val maxTokens: Int,
    val stream: Boolean
)