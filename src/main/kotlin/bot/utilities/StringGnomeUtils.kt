package bot.utilities

import bot.constants.GNOME_COMMAND_PREFIX

fun String.isCommand() = this.startsWith(GNOME_COMMAND_PREFIX)