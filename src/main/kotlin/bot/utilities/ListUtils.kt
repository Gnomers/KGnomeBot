package bot.utilities

import java.time.LocalDate
import kotlin.random.Random


fun <T> List<T>.ofTheDay() = this.random(Random(LocalDate.now().toEpochDay()))
