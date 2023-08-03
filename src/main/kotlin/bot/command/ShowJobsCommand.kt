package bot.command

import bot.core.JobRegistrator
import dev.kord.core.event.message.MessageCreateEvent

class ShowJobsCommand: Command(
    name = "jobs",
    description = "Shows all current jobs"
) {
    override suspend fun invoke(event: MessageCreateEvent, subCommand: String?) {
        var response = StringBuilder()
        response.appendLine("Hello there, old chum! Here are the jobs on this bot:")
        JobRegistrator.runningJobs.values.forEach { job ->
            response.appendLine("[${job.executionDelaySeconds} seconds] ${job.name} -> ${job.description}")
        }
        event.message.channel.createMessage(response.toString())
    }
}