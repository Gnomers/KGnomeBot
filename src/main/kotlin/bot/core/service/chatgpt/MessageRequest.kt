package bot.core.service.chatgpt

data class MessageRequest(
    val messages: List<MessageData>,
    val model: String = "gpt-3.5-turbo",
    val stream: Boolean = false
)

//  Extra unused fields:
//    "temperature": 1
//    "presence_penalty": 0
//    "top_p": 1
//    "frequency_penalty": 0