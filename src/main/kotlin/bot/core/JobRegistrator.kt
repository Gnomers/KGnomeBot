package bot.core

import bot.core.exception.DuplicateClassExcpetion
import bot.job.Job
import bot.logging.Loggable
import org.reflections.Reflections

object JobRegistrator: Loggable {

    // name -> Job class
    val runningJobs: MutableMap<String, Job> = mutableMapOf()
    suspend fun registerJobs() {
        logger.info("Starting JobRegistrator")

        val subClasses = Reflections("bot.job").getSubTypesOf(Job::class.java)
        subClasses.forEach { clazz ->
            val instance = clazz.kotlin.objectInstance!!

            runningJobs.getOrDefault(instance.name, null)?.let {
                // command already exists
                throw DuplicateClassExcpetion("Job ${instance.name} is duplicated")
            }

            // This will create the coroutine inside it
            instance.register()

            logger.info("Adding \"${instance.name}\" job to the job list")
            runningJobs[instance.name.lowercase()] = instance
        }

        logger.info("JobRegistrator finished with jobs=${runningJobs.values.map { it.name }}")
    }
}

