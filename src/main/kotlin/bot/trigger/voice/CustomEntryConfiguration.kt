package bot.trigger.voice

import com.fasterxml.jackson.annotation.JsonProperty

data class CustomEntryConfiguration(
    val data: List<Data>
) {
    data class Data(
        @JsonProperty("user_id")
        val userId: String,
        val sound: String
    )
}
