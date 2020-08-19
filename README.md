# HangmanIK
###### Developed by Chris Jardine

## *Verison 1.0*
The initial commit has basic functionality, allowing the bot owner to enter a word of their choice or generate a random word. At the
moment, the "random" words are stored in a local file, though I've considered hooking into a word generating API.<br>
The project hasn't been fully tested, so there are most likely some annoying bugs.

---

## Connecting to Twitch
This program allows you to manually enter login credentials on startup or to locally store your information to load on startup.<br>
To store the information locally, navigate to `src\main\resources\config.txt`.<br>
As the instructions will state, **do not** change the format of the lines.<br>
In short, make sure your channel contains the `#` and OAuth contains `ouath:`, otherwise it will not work.<br>
If you need to generate an OAuth for Twitch, you can do so [**here**](https://twitchapps.com/tmi/).

---

## Adding Random Words
To add or remove words from the dictionary, navigate to `src\main\resources\dictionary.txt`.<br>
**Make sure each word is on its own line!**<br>
Phrases can be used rather than single words. Hyphenated words are also allowed.

---

## Plans for Future Releases
- REFACTORING
- Add a leaderboard
- Allow login credentials to be saved from the GUI
- Allow console logs within the GUI
- Run the game continuously if desired
- Allow program to run in the system tray as desired
- Show hangman on an overlay (this will probably take some time)
- Allow words to be added to and from the dictionary from the GUI
- Better icon artwork