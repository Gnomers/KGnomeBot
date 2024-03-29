package bot.core.service.imagedescriptor

import bot.constants.HUGGING_FACE_TOKEN_ENV_VAR
import bot.logging.Loggable
import bot.utilities.buildDefault
import bot.utilities.isValidURL
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.http.HttpStatus

object ImageDescriptorService: Loggable {
    private val TOKEN = (dotenv()[HUGGING_FACE_TOKEN_ENV_VAR] ?: null)
        ?.also { logger.info("$HUGGING_FACE_TOKEN_ENV_VAR length=${it.length}") }
        ?: also { logger.info("$HUGGING_FACE_TOKEN_ENV_VAR is null") }

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
    suspend fun describe(imageUrl: String): String {
        if (!imageUrl.isValidURL()) return "Hello there, old chum, this URL doesn't seem quite right! Ho ho he ha ha"
        if (TOKEN == null) return "Hello there, old chum, this feature isn't ready yet!"

        val client = OkHttpClient().buildDefault()
        val body = RequestBody.create(MediaType.parse("text/plain"), imageUrl)

        val responses = mutableListOf<String>()
        coroutineScope {
            models.forEach {
                launch {
                    val thisResponse = StringBuilder()
                    thisResponse.append("${it.key}: ")

                    val request = Request.Builder()
                        .url(it.value)
                        .post(body)
                        .addHeader("Authorization", "Bearer $TOKEN")
                        .addHeader("Content-Type", "text/plain")
                        .build()

                    logger.info("Request: method=${request.method()} body=${request.body()} url=${request.url()}}")

                    val response = client.newCall(request).execute()
                    val responseBody = response.body().string()
                    val statusCode = response.code()

                    logger.info("Response: status=${statusCode} body=$responseBody")

                    thisResponse.append(
                        when (statusCode) {
                            HttpStatus.SC_SERVICE_UNAVAILABLE -> "He he he, I'm thinking... please wait and ask again!"
                            HttpStatus.SC_OK -> Json.parseToJsonElement(responseBody)
                                .jsonArray.first()
                                .jsonObject["generated_text"]
                                ?.jsonPrimitive
                                ?.content ?: "I have no idea what that is, old chum. Maybe you were gnomed?"

                            else -> "I have no idea what that is, old chum. Maybe you were gnomed?"
                        }
                    )
                    responses.add(thisResponse.toString())
                }
            }
        }

        return responses.joinToString("\n")
    }
}