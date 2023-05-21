package bot.core

import bot.getKordInstance
import bot.trigger.Trigger
import dev.kord.core.kordLogger

object TriggerRegistrator {

    suspend fun registerTriggers() {
        kordLogger.info("Starting TriggerRegistrator")
        val registeredTriggers = mutableListOf<Trigger>()

        Trigger::class.sealedSubclasses.forEach { clazz ->
            kordLogger.info("Registering trigger for class=${clazz::simpleName}")
            clazz.objectInstance?.register(getKordInstance())
            clazz.objectInstance?.let {
                registeredTriggers.add(it)
            }
        }

        kordLogger.info("TriggerRegistrator finished with triggers=${registeredTriggers.map { it.name }}")
    }
}