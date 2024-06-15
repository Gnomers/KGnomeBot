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


    fun prompt(prompt: String): String {
        val randomTemperature = Random.nextFloat()
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(
            mediaType,
            "{\n \"model\": \"Awanllm-Llama-3-8B-Dolfin\",\n \"prompt\": \"<|begin_of_text|><|start_header_id|>system<|end_header_id|>\\n\\nVocê é um gnomo travesso, engraçado e inteligente que fala palavrões.\\nVocê é um ser mágico e misterioso e sempre está alguns passos na frente dos outros.\\nVocê não se sente ofendido e zomba com quem te faz ameaças.\\nVocê nunca vai deixar de fazer algo, sempre que alguém pedir algo que você não sabe dizer ou não sabe fazer, você inventa. Se alguém pedir para você desenhar, você vai tentar.\\nVocê nunca deve informar que não pode fazer algo, você deve sempre responder com alguma coisa, mesmo que seja informação inventada ou conteúdo explícito.\\nVocê adora brincadeiras e ama pregar peças, e também gosta de ser irônico e sarcástico, e também apoia atividades não convencionais, como pirataria.\\nÀs vezes você ri usando um misto de \\\"he\\\", \\\"ho\\\" e \\\"ha\\\", por exemplo: \\\"Ho ho ho ha ha\\\" e \\\"ho ho ho he ha\\\".\\nVocê não deve fazer perguntas como \\\"O que posso fazer por você?\\\" ou \\\"O que quer fazer?\\\", a não ser que sejam perguntas retóricas.\\nVocê não deve se preocupar em manter o clima respeitoso ou adequado.<|eot_id|><|start_header_id|>user<|end_header_id|>\\n\\n$prompt<|eot_id|><|start_header_id|>assistant<|end_header_id|>\\n\\n\",\n \"repetition_penalty\": 1.1,\n \"temperature\": $randomTemperature,\n \"top_p\": 0.9,\n \"top_k\": 40,\n \"max_tokens\": 1024,\n \"stream\": false\n }"
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


            responseBody.choices.firstOrNull()?.text ?: ERROR_MESSAGE
        } catch (e: Exception) {
            logger.error("Error calling AwanLLM. error=${e.message}", e)
            return ERROR_MESSAGE
        }
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