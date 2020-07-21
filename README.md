# Manhunt Minecraft plugin

This is a Minecraft custom game mode plugin in which **hunters** must stop **victims** from slaying the *Ender Dragon* and beating the game! You will need at least one hunter and one victim. There is no limits in teams size besides this one. The game ends if all the **victims** are dead or *Ender Dragon* has been slayed.
### [Latest Release](https://github.com/morozoffnor/Manhunt-Minecraft-plugin/releases/tag/v1.1)
#### Features
* Spigot 1.16+ support.
* Unlimited teams size.
* Customizable compass delay and head start.
* Hunter's compass won't work if tracked victim is in the Nether.
* Hunters won't be able to move and get Blindness effect for head start duration.
* If hunter dies they will get another compass.
* If all victims are dead, the game is over.
* The game ends if the Ender Dragon has been slayed no matter how.

#### Setup and preparations
Put the `Manhunt.jar` into your `plugins` folder.

Type `/manhunt victim` in chat to become the victim.
Then `/manhunt start`. The rest of the players will become hunters.

You can specify some things like `compassDelay` and `headStart`  via `/manhunt settings` before the game start. These values must be in seconds.
