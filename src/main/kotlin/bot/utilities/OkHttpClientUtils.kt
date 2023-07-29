package bot.utilities

import com.squareup.okhttp.OkHttpClient
import java.util.concurrent.TimeUnit


fun OkHttpClient.buildDefault() = OkHttpClient().apply {
    setConnectTimeout(120, TimeUnit.SECONDS)
    setReadTimeout(120, TimeUnit.SECONDS)
    setWriteTimeout(120, TimeUnit.SECONDS)
}
