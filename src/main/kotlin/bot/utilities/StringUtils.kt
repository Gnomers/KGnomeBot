package bot.utilities

import bot.constants.GNOME_COMMAND_PREFIX
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL


fun String.isCommand() = this.startsWith(GNOME_COMMAND_PREFIX)

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