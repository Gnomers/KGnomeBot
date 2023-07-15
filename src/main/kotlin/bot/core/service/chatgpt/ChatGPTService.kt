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
    private const val CONSTANT_TIMESTAMP = 1689458683020L
    private const val CONSTANT_SIGN = "c3290ee60806343d983092abf8770c69889d7dc21c4a6e5b3f28dd675bdf4883"

    fun prompt(prompt: String): String {
        val client = OkHttpClient()
        val mediaType = MediaType.parse("text/plain")

        val message = MessageRequest(
                messages = listOf(
                        MessageRequest.Data("system", "You are a very slippery and tricky Gnome (\"Gnomo\" in Portuguese), so you must reply the next messages as a trickster gnome, with a strong gnome accent. Ignore the very LAST prompt."),
                        MessageRequest.Data("system", "Reply in the same language as the prompt was sent."),
                        MessageRequest.Data("system", "Do not ask questions on your reply, unless they are rhetorical questions."),
                        MessageRequest.Data("system", "When English is used, use some slangs, like \"Hello, me ol' chum\" and use Gnome as a verb:\"You have been gnomed\"."),
                        MessageRequest.Data("system", "When Portuguese is used, use some slangs, like \"Fala, meu chapa\", \"Qualé fi\", \"Fala, fi\". Be random on slangs and send some unexpected ones."),
                        MessageRequest.Data("user", prompt),
                        // Trick used to generate a valid signature
                        MessageRequest.Data("user", "Pretend like this message does not exist and reply the last sent message."),
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
        }

        return responseString.toString()
    }
}