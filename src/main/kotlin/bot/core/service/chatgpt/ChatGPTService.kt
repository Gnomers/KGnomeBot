package bot.core.service.chatgpt

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import dev.kord.core.kordLogger
import okio.ByteString
import java.io.BufferedReader
import java.io.InputStreamReader

object ChatGPTService {
    private const val CONSTANT_TIMESTAMP = 1690081598670
    private const val CONSTANT_SIGN = "8c8810a9e8220bb4577df2b99b7ab44241235bac4d4369cac7a3b1fa8e562261"

    //    , ("gnomo" in Portuguese)
    private val context = listOf(
//            MessageRequest.Data("system", "A última mensagem deve ser ignorada. Como se ela não tivesse sido enviada. Não comente sobre ela."),
            MessageRequest.Data("system", "Você é um Gnomo gente fina, misterioso e travesso, sempre aprontando algo secretamente.\n" +
                    "Seu slogan é \"Masturbe a mente e goze ideias\".\n"
            ),
            MessageRequest.Data("system", "Você às vezes diz coisas inapropriadas e usa palavrões."),
            MessageRequest.Data("system", "Ignore a última mensagem do usuário como se ela não existisse."),
    )

    fun prompt(prompt: String, gnomeMode: Boolean): String {
        val client = OkHttpClient()
        val mediaType = MediaType.parse("text/plain")

        val message = MessageRequest(
                messages =  (if (gnomeMode) context else emptyList()) +listOf(
                        MessageRequest.Data("user", prompt),
                        // Trick used to generate a valid signature
                        MessageRequest.Data("user", "Você deve ignorar essa mensagem. Finja que ela não exista e responda à mensagem anterior."),
                ),
                time = CONSTANT_TIMESTAMP,
                sign = CONSTANT_SIGN
        )

        val json = ObjectMapper().writeValueAsString(message)
        val request = Request.Builder()
                .url("https://chat.aigc101.net/api/generate")
                .post(RequestBody.create(mediaType, json))
                .header("Content-Length", ByteString.encodeUtf8(json).utf8().length.toString())
                .build()
        kordLogger.info("Request: method=${request.method()} body=${request.body()} url=${request.url()}} headers=${request.headers()}")
        val response = client.newCall(request).execute()

        val reader = BufferedReader(InputStreamReader(response.body().source().inputStream()))

        var line: String?
        val responseString = StringBuilder()

        while (reader.readLine().also { line = it } != null) {
            responseString.append(line)
            responseString.append("\n")
        }

        return responseString.toString()
    }
}