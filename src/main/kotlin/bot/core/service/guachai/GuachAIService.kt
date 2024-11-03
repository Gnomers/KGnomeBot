package bot.core.service.guachai

import bot.constants.GENERIC_ERROR_MESSAGE
import bot.constants.GUACHAI_AUTH
import bot.constants.GUACHAI_URL
import bot.logging.Loggable
import bot.utilities.buildDefault
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import io.github.cdimascio.dotenv.dotenv
import kotlin.random.Random

object GuachAIService: Loggable {
    val URL = dotenv()[GUACHAI_URL] ?: null
    val AUTH = dotenv()[GUACHAI_AUTH] ?: null

    private val client = OkHttpClient().buildDefault()
    private val mapper = ObjectMapper().registerKotlinModule()


    val PROMPT = object {}.javaClass.classLoader.getResource("PROMPT_GENERIC.txt")!!.readText()
        .lines().filterNot { line -> line.startsWith("//") }
        .joinToString(separator = "\n")
//        .replace("{BEGIN_OF_TEXT}", "")
//        .replace("{START_HEADER_ID}", "")
//        .replace("{END_HEADER_ID}", "")
//        .replace("{EOT_ID}", "")
//        .replace("\n", "")
//        .replace("\r", "")
//        // putting \n only where the prompt NEEDS it
//        .replace("{LINE_BREAK}", "\n")

    fun prompt(input: String): String {
        val completePrompt = PROMPT.replace("{input}", input)
        val randomTemperature = Random.nextFloat()
        val mediaType = MediaType.parse("application/json")
        val model = "gnome"
        val req = GuachAIRequest(
            model = model,
            prompt = completePrompt,
            stream = false,
            options = GuachAIRequest.Options(
                temperature = randomTemperature
            )
        )

        val body = RequestBody.create(
            mediaType,
            mapper.writeValueAsString(req)
        )
        val request = Request.Builder()
            .url(URL)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", AUTH)
            .build()

        logger.info(
            "Request: method=${request.method()} body=${request.body()} url=${request.url()}} headers=${
                request.headers().toMultimap().filterNot { it.key == "Authorization" }
            }"
        )

        return try {
            val response = client.newCall(request).execute()

            val content = response.body().string()
            logger.info("Response: body=$content")
            val responseBody = mapper.readValue<GuachAIResponse>(content)

            responseBody.response.withTemperatureAndModel(randomTemperature, model)
        } catch (e: Exception) {
            logger.error("Error calling GuachAI. error=${e.message}", e)
            return GENERIC_ERROR_MESSAGE
        }
    }

    private fun String.withTemperatureAndModel(temp: Float, model: String): String {
        return "$this [$temp]"
    }
}

// Area to quickly test prompts when running locally
fun main() {
    listOf<String>(
        "boa noite"
    )
        .forEach {
            println("Q: $it")
            println("R: ${GuachAIService.prompt(it)}")
            println("------------")
        }
}