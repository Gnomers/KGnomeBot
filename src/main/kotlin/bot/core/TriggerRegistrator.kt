package bot.core

import bot.getKordInstance
import bot.trigger.Trigger
import dev.kord.core.kordLogger
import org.reflections.Reflections

object TriggerRegistrator {

    suspend fun registerTriggers() {
        val registeredTriggers = mutableListOf<Trigger>()
        kordLogger.info("Starting TriggerRegistrator")

        val subClasses = Reflections("bot.trigger").getSubTypesOf(Trigger::class.java)
        subClasses.forEach { clazz ->
            kordLogger.info("Registering trigger for class=${clazz.simpleName}")
            val instance = clazz.kotlin.objectInstance!!
            instance.register(getKordInstance())
            registeredTriggers.add(instance)
        }

        kordLogger.info("TriggerRegistrator finished with triggers=${registeredTriggers.map { it.name }}")
    }
}