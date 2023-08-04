package bot.utilities

import bot.constants.GNOME_COMMAND_PREFIXES
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL


fun String.isCommand() = this.startsWithAny(GNOME_COMMAND_PREFIXES)

fun String.isValidURL(): Boolean {
    return try {
        URL(this).toURI()
        true
    } catch (e: MalformedURLException) {
        false
    } catch (e: URISyntaxException) {
        false
    }
}

fun String.startsWithAny(prefixes: List<String>): Boolean  {
    return prefixes.any {
        this.startsWith(it)
    }
}
