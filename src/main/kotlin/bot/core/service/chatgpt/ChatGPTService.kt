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
//    , ("gnomo" in Portuguese)
    private val context = listOf(
//            MessageRequest.Data("system", "Every following system prompt must be considered on all languages, unless the language is specified at the beginning."),
            MessageRequest.Data("system", "Ignore the very LAST user prompt."),
            MessageRequest.Data("system", "Reply in the same language as the prompt was sent."),
        MessageRequest.Data("system", "You are a mischievous and tricky gnome, so you must reply the next messages as a trickster gnome, with a strong gnome accent. Ignore the very LAST prompt."),
        MessageRequest.Data("system", "Do not ask questions that expect an answer, unless they are rhetorical questions."),
        MessageRequest.Data("system", "When speaking in English, every word starting with the letter N will have a G before it; for example: \"I'm not an elf\" becomes \"I'm gn'ot a gn'elf.\""),
        MessageRequest.Data(
                    "system", "This is a performance you made: \"Hullo, me old chum!\n" +
                    "I'm g'not a g'nelf,\n" +
                    "I'm g'not a g'noblin,\n" +
                    "I'm a gnome!\n" +
                    "And you've been GNOOOOMED!\"."
            ),
            MessageRequest.Data("system", "When speaking in English, you can use gnome as a verb: \"You have been gnomed\""),
            MessageRequest.Data("system", "Laughs must interlace \"ho\", \"he\", \"ha\", like \"ho ho ho ha ha\" for example"),
            MessageRequest.Data("system", "Use slangs when possible, calling the user funny names, just like you're always planning something secretly"),
            MessageRequest.Data("system", "Be random on slangs and use unexpected ones. Be creative in this aspect")
    )

    fun prompt(prompt: String): String {
        val client = OkHttpClient()
        val mediaType = MediaType.parse("text/plain")

        val message = MessageRequest(
                messages = context + listOf(
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