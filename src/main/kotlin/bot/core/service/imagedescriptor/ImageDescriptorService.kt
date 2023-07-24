package bot.core.service.imagedescriptor

import bot.constants.HUGGING_FACE_TOKEN_ENV_VAR
import bot.utilities.isValidURL
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import dev.kord.core.kordLogger
import io.github.cdimascio.dotenv.dotenv
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.http.HttpStatus

object ImageDescriptorService {
    private val TOKEN = dotenv()[HUGGING_FACE_TOKEN_ENV_VAR] ?: null

    val models = mapOf(
        // accurate
        "BLIP" to "https://api-inference.huggingface.co/models/Salesforce/blip-image-captioning-large",
        // less accurate
        "NLPConnect" to "https://api-inference.huggingface.co/models/nlpconnect/vit-gpt2-image-captioning",
        // clusterfvck
        "Flickr8k" to "https://api-inference.huggingface.co/models/atasoglu/vit-gpt2-flickr8k"
    )

    // not accurate but funny
    const val URL = "https://api-inference.huggingface.co/models/nlpconnect/vit-gpt2-image-captioning"
    fun describe(imageUrl: String): String {
        if (!imageUrl.isValidURL()) return "Hello there, old chum, this URL doesn't seem quite right! Ho ho he ha ha"
        if (TOKEN == null) return "Hello there, old chum, this feature isn't ready yet!"

        val client = OkHttpClient()
        val body = RequestBody.create(MediaType.parse("text/plain"), imageUrl)

        val finalResponse = StringBuilder()
        models.forEach {
            finalResponse.append("${it.key}: ")

            val request = Request.Builder()
                .url(it.value)
                .post(body)
                .addHeader("Authorization", "Bearer $TOKEN")
                .addHeader("Content-Type", "text/plain")
                .build()

            kordLogger.info("Request: method=${request.method()} body=${request.body()} url=${request.url()}}")

            val response = client.newCall(request).execute()
            val responseBody = response.body().string()
            val statusCode = response.code()

            kordLogger.info("Response: status=${statusCode} body=$responseBody")

            finalResponse.append(
                when (statusCode) {
                    HttpStatus.SC_SERVICE_UNAVAILABLE -> return "He he he, I'm thinking... please wait and ask again!"
                    HttpStatus.SC_OK -> Json.parseToJsonElement(responseBody)
                        .jsonArray.first()
                        .jsonObject["generated_text"]
                        ?.jsonPrimitive
                        ?.content ?: "I have no idea what that is, old chum."

                    else -> "I have no idea what that is, old chum."
                }
            )
            finalResponse.appendLine()
        }

        return finalResponse.toString()
    }
}