# POKERFOX
![Pokerfox](https://github.com/mamanigrasso/PartyPoker/blob/master/app/src/main/res/drawable-xxhdpi/fox.png)

## Game Description


POKERFOX is a round / real-time card game for two to six players with cheating features to increase the fun factor. 
Each player gets two cards and betting chips. The players put a different amount of money (chips) on the odds of winning 
their own hand without knowing about the opponent's hand. The chips of a game (pot) used by the players wins the player with 
the strongest hand. This opens up the opportunity to win by bluffing even with weak cards. 
The goal is to win as many chips as possible from other players.

## Cheating

Cheating is allowed once per round. Click on the Cheat button to select one of the three different cheat options(EYE, DEAD MANS HAND or Probability).
But beware, other players can clap you up by clicking the big round red button with the call sign and selecting the player who cheats.
If the player is correct, then the round ends for the cheating player and the player who pressed the button gets 1/5 of the chips from the player who has cheated.

* Eye - The Eye Cheat option gives you access to one card of any player you choose.

* Dead man's Hand - With this special cheat option, it is possible to select any card of a full deck and change it with one of the two cards on your hand.

* Probability - This cheating option can be used in the round when the flop is visible but the turn and the river are still not visible. Probability cheating allows you gettin the probability for the best hand the selected player can get with one another card (with the turn or the river). It shows you the probability for what the selected player can have when all cards are visible from the combination "three of a kind" up to the "royal flush".

## Preview Scene

![Screenshot1](https://user-images.githubusercontent.com/37117215/41647391-861c76ee-7476-11e8-8847-9cc061b588f9.JPG width="200" height="400")
![Screenshot2](https://user-images.githubusercontent.com/37117215/41647225-16be96a6-7476-11e8-8d2a-731f99dd363b.JPG width="200" height="400")


## Technical Details

POKERFOX is a multiplayer Android game which was developed using Android Studio and the programming language
Java and Android SDK version 26. The multiplayer component was implemented using the 3rd party framework called Salut.
PokerFox can be played on any Android device, the only requirement is that WifiDirect is enabled on al participating devices.

* [Android Studio](https://developer.android.com/studio/) 
* [Salut Network](https://salut-a-toi.org/)

## Other

* Build : [![Build status](https://travis-ci.com/mamanigrasso/PartyPoker.svg?branch=master)](https://travis-ci.com/mamanigrasso/PartyPoker) 
* Code Quality : [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.sonarqube.examples.standard-sqscanner-travis-project&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.sonarqube.examples.standard-sqscanner-travis-project)

## Authors

* [Kogler Andreas](https://github.com/andreaskog/)
* [Langer Manuel](https://github.com/manlanger/)
* [Schmid Timo](https://github.com/dalton666/)
* [Manigrasso Marco](https://github.com/mamanigrasso/)
