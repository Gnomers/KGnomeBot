# KGnome Bot

![gnome.png](gnome.png)

I'm gnot a gnelf, I'm a Discord bot, written in Kotlin with Kord!

## How do I create a new command?
Head into the package `bot.command` and create a new "SomethingCommand" that extends "Command", give it a name (which will be used to invoke from the text chat) and a description (which will be shown on !gnome help).  
For example:
```
class HelloCommand: Command(
    name = "hello", // will trigger when you say !gnome hello
    description = "displays information on all commands"
) {
    override suspend fun invoke(event: MessageCreateEvent) {
        event.message.channel.createMessage("Hello!")
    }
}
```
This command can be invoked by typing `!gnome hello` and will automatically appear on `!gnome help`. 

## How do I create a new trigger?
Triggers are a bit more tricky, as they use Kord's event system.  
Head into the `bot.trigger` package and create a object that extends `Trigger`; inside its `register` method, create a listener for an event by using Kord's event system:  
```
object HelloTrigger: Trigger(
    name = "hello_trigger",
    description = "Triggers a \"hello\" whenever someone sends a message" 
) {
    override suspend fun register(kordInstance: Kord) {
        // when a message is sent 
        kordInstance.on<MessageCreateEvent> {
            this.message.channel.createMessage("Hello!")
        }
    }
}
```
Just like the commands, these triggers will be registered automatically and can be shown on `!gnome triggers` command.

## Who registers the triggers and commands?
Triggers are registered before Kord starts running, it is done in `KGnomeRunner.kt`, here:
```
TriggerRegistrator.registerTriggers()
```
Which triggers `TriggerRegistrator`, who is responsible for all triggers' registering.
  
The commands are registered in a similar manner, but not before Kord's execution. They are created by the `CommandHandler`, who finds all classes that implement `Command` directly and maps them to a `name -> Command` map.

## Sound adding and playing
Adding new sounds is as easy as it sounds (no pun intended), you just add a .ogg file into `resources/audio`, and add it to `bot.utilities.Sound` just like the others.   
To emit sounds as our favorite Gnome, you can use `SoundPlayerManager`, a very barebones and bad-implemented sound manager who uses `LavaPlayer` underneath.  
There are methods to play sounds on certain channels, responding to certain messages and to stop playing whatever the bot is playing.

## Multiple connections
As the SoundPlayerManager contains a single VoiceConnection instead of a VoiceConnection map or list, I believe it won't work as expected on multiple channels at once.

## Unit and integration tests
What?