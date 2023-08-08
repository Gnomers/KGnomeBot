package bot.core.service.imagedraw

import bot.constants.HUGGING_FACE_TOKEN_ENV_VAR
import bot.utilities.buildDefault
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import dev.kord.core.kordLogger
import dev.kord.rest.Image
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.apache.http.HttpStatus.SC_OK

object ImageDrawingService {
    private val TOKEN = (dotenv()[HUGGING_FACE_TOKEN_ENV_VAR] ?: null)
        ?.also { kordLogger.info("$HUGGING_FACE_TOKEN_ENV_VAR length=${it.length}") }
        ?: also { kordLogger.info("$HUGGING_FACE_TOKEN_ENV_VAR is null") }

    val models = mapOf(
        // accurate
        "Realistic_Vision_V1.4" to "https://api-inference.huggingface.co/models/SG161222/Realistic_Vision_V1.4",
        "StableDiffusion_v1.5" to "https://api-inference.huggingface.co/models/runwayml/stable-diffusion-v1-5",
        "openjourney" to "https://api-inference.huggingface.co/models/prompthero/openjourney",
        "Animagine_XL" to "https://api-inference.huggingface.co/models/Linaqruf/animagine-xl",
    )

    //SG161222/Realistic_Vision_V1.4
    //runwayml/stable-diffusion-v1-5
    //prompthero/openjourney

    suspend fun draw(description: String): Map<String, Image> {
        val client = OkHttpClient().buildDefault()
        val mediaType = "text/plain"
        val responses = mutableMapOf<String, Image>()
        coroutineScope {
            models.forEach {
                launch {
                    try {
                        val request = Request.Builder()
                            .url(it.value)
                            .post(RequestBody.create(MediaType.parse(mediaType), description))
                            .addHeader("Authorization", "Bearer $TOKEN")
                            .addHeader("Content-Type", mediaType)
                            .build()

                        kordLogger.info("Sending request to model=${it.key} with prompt=$description")
                        val response = client.newCall(request).execute()
                        val status = response.code()
                        val body = response.body()
                        if (status != SC_OK) {
                            kordLogger.warn("ImageDraw response from model=${it.key} came with status=$status and body=${body.string()}")
                            return@launch
                        }
                        val bytes = body.bytes()

                        kordLogger.info("Successful drawing operation for model=${it.key}")
                        responses[it.key] = Image.raw(bytes, Image.Format.JPEG)
                    } catch (e: Exception) {
                        kordLogger.error("An error occured on model=${it.key}, ignoring it", e)
                    }
                }
            }
        }

        return responses
    }
}