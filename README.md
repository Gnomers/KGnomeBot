# KGnome Bot

![gnome.png](gnome.png)

I'm gnot a gnelf, I'm a Discord bot, written in Kotlin with Kord!

## Running the bot
To run this bot, you'll first need to set some environment variables for the project, like the Discord Auth Token.
You can see which environment variables we are using if you look into the Dockerfile, like this:
```
RUN echo DISCORD_AUTH_TOKEN=${DISCORD_AUTH_TOKEN} >> .env
RUN echo HUGGING_FACE_AUTH_TOKEN=${HUGGING_FACE_AUTH_TOKEN} >> .env
RUN echo CUSTOM_ENTRY_CONFIG_BASE64=${CUSTOM_ENTRY_CONFIG_BASE64} >> .env
RUN echo ANOTHER_ENV_VAR=${ANOTHER_ENV_VAR} >> .env
```

Create a file named ".env" inside the root folder: `(...)/kgnome/.env`.

This .env file is a will be used to set the environment variables the bot will use.
For example:
```
DISCORD_AUTH_TOKEN = MY_DISCORD_AUTH_TOKEN
HUGGING_FACE_AUTH_TOKEN = MY_HUGGING_FACE_AUTH_TOKEN
ANOTHER_ENV_VAR = ANOTHER_ENV_VAR_VALUE
```

Every variable other than `DISCORD_AUTH_TOKEN` is optional.

After setting all the environment variables, just run the `main` method inside KGnomeRunner.kt.

Alternatively, you can build and run the kgnome.jar file:
```
./gradlew clean build
java -jar build/libs/kgnome.jar
```

## What is this Custom Entry Config?
Custom Entry is a feature that plays a sound when a certain user connects to the voice chat.
The environment variable CUSTOM_ENTRY_CONFIG_BASE64 expects a JSON converted to Base64 as follows:
```json
{
  "data": [
    {
      "user_id": "snowflake_id",
      "sound": "SOUND_ENUM_NAME"
    },
    {
      "user_id": "snowflake_id_2",
      "sound": "SOUND_ENUM_NAME"
    }
  ]
}
```
This JSON would be added in the envvar as: ewogICJkYXRhIjogWwogICAgewogICAgICAidXNlcl9pZCI6ICJzbm93Zmxha2VfaWQiLAogICAgICAic291bmQiOiAiU09VTkRfRU5VTV9OQU1FIgogICAgfSwKICAgIHsKICAgICAgInVzZXJfaWQiOiAic25vd2ZsYWtlX2lkXzIiLAogICAgICAic291bmQiOiAiU09VTkRfRU5VTV9OQU1FIgogICAgfQogIF0KfQ==

## How do I create a new command?
Head into the package `bot.command` and create a new "SomethingCommand" that extends "Command", give it a name (which will be used to invoke from the text chat) and a description (which will be shown on !gnome help).  
**Note: The prefix `!gnome` can be changed on `bot.constants.Configuration`**    
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
        kordInstance.onIgnoringBots<MessageCreateEvent> {
            this.message.channel.createMessage("Hello!")
        }
    }
}
```
Just like the commands, these triggers will be registered automatically and can be shown on `!gnome triggers` command.
  
**Note: The `onIgnoringBots` is an extension that ignores bots for `MessageCreateEvent` and `VoiceStateUpdateEvent`, it is located in `bot.utilities.KordExtensions.kt`**

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
