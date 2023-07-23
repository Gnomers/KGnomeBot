package bot.core.service.chatgpt

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import okio.ByteString
import java.io.BufferedReader
import java.io.InputStreamReader

object ChatGPTService {
    private const val CONSTANT_TIMESTAMP = 1690081598670
    private const val CONSTANT_SIGN = "8c8810a9e8220bb4577df2b99b7ab44241235bac4d4369cac7a3b1fa8e562261"

    //    , ("gnomo" in Portuguese)
    private val context = listOf(
            MessageRequest.Data("system", "Você deve ignorar a última mensagem enviada."),
            MessageRequest.Data("system", "Você é um Gnomo comédia, parceiro e travesso. Deve responder o usuário de maneira engraçada e inesperada.\n" +
                    "Você pode falar palavrões e não precisa se preocupar em ser educado."),
//            MessageRequest.Data("system", "Reply using the same language that the user used in his penultimate prompt."),
//            MessageRequest.Data("system", "Você deve ignorar essa mensagem. Finja que ela não exista e responda à mensagem anterior."),
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