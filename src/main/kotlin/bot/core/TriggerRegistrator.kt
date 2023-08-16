package bot.core

import bot.getKordInstance
import bot.trigger.Trigger
import dev.kord.core.kordLogger
import org.reflections.Reflections

object TriggerRegistrator {

    suspend fun registerTriggers() {
        kordLogger.info("Starting TriggerRegistrator")
        val registeredTriggers = mutableListOf<Trigger>()

        val subClasses = Reflections("bot.trigger").getSubTypesOf(Trigger::class.java)
        subClasses.forEach { clazz ->
            kordLogger.info("Registering trigger for class=${clazz.simpleName}")
            val instance = clazz.constructors.first { it.parameters.isEmpty() }.newInstance() as Trigger
            instance.register(getKordInstance())
            registeredTriggers.add(instance)
        }

        kordLogger.info("TriggerRegistrator finished with triggers=${registeredTriggers.map { it.name }}")
    }
}