package bot.core

import bot.getKordInstance
import bot.logging.Loggable
import bot.trigger.Trigger
import org.reflections.Reflections

object TriggerRegistrator: Loggable {

    suspend fun registerTriggers() {
        val registeredTriggers = mutableListOf<Trigger>()
        logger.info("Starting TriggerRegistrator")

        val subClasses = Reflections("bot.trigger").getSubTypesOf(Trigger::class.java)
        subClasses.forEach { clazz ->
            logger.info("Registering trigger for class=${clazz.simpleName}")
            val instance = clazz.kotlin.objectInstance!!
            instance.register(getKordInstance())
            registeredTriggers.add(instance)
        }

        logger.info("TriggerRegistrator finished with triggers=${registeredTriggers.map { it.name }}")
    }
}