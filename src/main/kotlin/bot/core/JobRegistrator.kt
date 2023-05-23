package bot.core

import bot.core.exception.DuplicateClassExcpetion
import bot.job.Job
import dev.kord.core.kordLogger

object JobRegistrator {

    // name -> Job class
    val runningJobs: MutableMap<String, Job> = mutableMapOf()
    suspend fun registerJobs() {
        kordLogger.info("Starting JobRegistrator")

        Job::class.sealedSubclasses.forEach { clazz ->
            val instance = clazz.constructors.first { it.parameters.isEmpty() }.call()

            runningJobs.getOrDefault(instance.name, null)?.let {
                // command already exists
                throw DuplicateClassExcpetion("Job ${instance.name} is duplicated")
            }

            // This will create the coroutine inside it
            instance.register()

            kordLogger.info("Adding \"${instance.name}\" job to the job list")
            runningJobs[instance.name.lowercase()] = instance
        }

        kordLogger.info("JobRegistrator finished with jobs=${runningJobs.values.map { it.name }}")
    }
}

