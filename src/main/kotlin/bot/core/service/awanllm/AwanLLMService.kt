package bot.core.service.awanllm

import bot.constants.AWAN_LLM_KEY_ENV_VAR
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

object AwanLLMService : Loggable {
    private val TOKEN = (dotenv()[AWAN_LLM_KEY_ENV_VAR] ?: null)
        ?.also { logger.info("$AWAN_LLM_KEY_ENV_VAR length=${it.length}") }
        ?: also { logger.info("$AWAN_LLM_KEY_ENV_VAR is null") }
    private val client = OkHttpClient().buildDefault()

    private val mapper = ObjectMapper().registerKotlinModule()
    private const val ERROR_MESSAGE = "An error occurred... SOMEONE HELP THE GNOME!!!"

    val MODELS = listOf(
//        "Awanllm-Llama-3-8B-Cumulus", // "Desculpe, mas não posso continuar essa conversa. Posso ajudar com outra coisa?"
        "Awanllm-Llama-3-8B-Dolfin"
    )

    val PROMPT = object {}.javaClass.classLoader.getResource("PROMPT_MINI.txt")!!.readText()
        .lines().filterNot { line -> line.startsWith("//") }
        .joinToString(separator = "\n")
        .replace("{BEGIN_OF_TEXT}", "<|begin_of_text|>")
        .replace("{START_HEADER_ID}", "<|start_header_id|>")
        .replace("{END_HEADER_ID}", "<|end_header_id|>")
        .replace("{EOT_ID}", "<|eot_id|>")
        .replace("\n", "")
        .replace("\r", "")
        // putting \n only where the prompt NEEDS it
        .replace("{LINE_BREAK}", "\n")



    fun prompt(input: String): String {
        val completePrompt = PROMPT.replace("{input}", input)
        val randomTemperature = Random.nextFloat()
        val mediaType = MediaType.parse("application/json")
        val model = MODELS.random()
//        val model = "Awanllm-Llama-3-8B-Cumulus"

        val req = AwanLLMRequest(
            model = model,
            prompt = completePrompt,
            repetitionPenalty = 1.1,
            temperature = randomTemperature,
            topP = 0.9,
            topK = 40,
            maxTokens = 1024,
            stream = false
        )

        val body = RequestBody.create(
            mediaType,
            mapper.writeValueAsString(req)
        )
        val request = Request.Builder()
            .url("https://api.awanllm.com/v1/completions")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $TOKEN")
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
            val responseBody = mapper.readValue<AwanLLMResponse>(content)


            responseBody.choices.firstOrNull()?.text?.removeSurrounding("\"")?.withTemperatureAndModel(randomTemperature, model) ?: ERROR_MESSAGE
        } catch (e: Exception) {
            logger.error("Error calling AwanLLM. error=${e.message}", e)
            return ERROR_MESSAGE
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
            println("R: ${AwanLLMService.prompt(it)}")
            println("------------")
        }
}
