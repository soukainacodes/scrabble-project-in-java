# Scrabble® - Java Edition

## 📚 Overview

This is a custom implementation of the classic Scrabble® game developed in Java 22. It supports single and two-player modes, user accounts, ranking, custom dictionaries and tile bags, save/load game functionality, and in-game assistance tools.

---

## ✨ Features

- ✅ User registration and login system
- ✅ Profile management (username, password, avatar)
- ✅ Create and manage custom dictionaries and tile bags
- ✅ Play 1v1 (human vs human) or vs AI
- ✅ Save and resume games
- ✅ Automatic scoring and validation
- ✅ Leaderboard ranking
- ✅ Special bonus squares (DL, TL, DP, TP)
- ✅ Joker tile (#) with letter assignment
- ✅ In-game hints

---

## 🚀 Installation & Execution

### 1. Download & Extract

- Download the compressed project archive.
- Extract the folder to your preferred directory.

### 2. Requirements

- Java Runtime Environment **22** or higher must be installed and properly configured in your PATH.

### 3. Run the Application

Open your terminal, navigate to the extracted folder, and run:

```bash
make runcode_main
````

After execution, the login screen will appear.

---

## 👤 Account Management

* **Create Account**: Register with a unique username and password.
* **Login**: Log in with existing credentials.
* **Change Username/Password**: Accessible from your profile.
* **Change Avatar**: Upload valid image files (png, jpg).
* **Delete Account**: This action is irreversible and will delete all associated data.

---

## 📂 Resource Management

Manage your dictionaries and tile bags:

* **View Resources**: See available word lists and tile sets.
* **Add Resource**: Manually or from file.
* **Edit Resource**: Update word lists or tile sets.
* **Delete Resource**: Remove any unused sets.

---

## 🏆 Ranking

View the global leaderboard. It displays all players with scores above zero in descending order.

---

## 🎮 Game Play

### Create New Game

* Start a new game from the **Play** menu.
* Set a unique game identifier.
* Choose player mode: single player vs AI or 2 players.
* Select your resource set (dictionary + tile bag).

### Load Saved Game

* Load your last saved game or select from saved games list.

### Game Screen

* Drag tiles to form valid words horizontally or vertically.
* Use bonus squares wisely!
* Options:

  * **Swap Tiles (Reset)**: Exchange tiles with the bag.
  * **Pass Turn**: Skip your turn.
  * **Help**: Get a suggested valid word.
  * **Use Joker (#)**: Assign a letter when placing the joker tile.
  * **End Turn**: Validate your move.
  * **Exit**: Save and quit, or abandon the game.

---

## 📝 Basic Game Rules

* Form valid words using your rack letters.
* Use the board’s special squares to maximize points.
* First word must cross the center square.
* Words must connect with existing words on the board.
* Jokers represent any letter but score 0 points.
* Bonus: Use all 7 tiles in a single turn → +50 points!

---

## 📸 Screenshots


### Login Screen
![screenshot1](https://github.com/user-attachments/assets/087227f1-ec63-4dea-b66a-c5ada2060f23)

### Player's account and settings
![screenshot2](https://github.com/user-attachments/assets/00806246-712c-422e-afaf-64b34f47ada6)


### Resource Management
![screenshot3](https://github.com/user-attachments/assets/b7e1e199-3f07-4344-af56-bd0a212d1c87)
![screenshot5](https://github.com/user-attachments/assets/33b5b26e-070b-4fe8-93aa-970df9d94bf7)


### In-Game Play
![screenshot7](https://github.com/user-attachments/assets/b2c912e0-f789-47c7-8377-46f0ffecd50f)

![screenshot8](https://github.com/user-attachments/assets/5f3383af-7214-4e3a-b147-9818a0cd4ea3)

### Ranking Screen

![screenshot6](https://github.com/user-attachments/assets/ea9d0067-c7a7-4c3c-8632-3c4ee7584758)


### End of game

![screenshot9](https://github.com/user-attachments/assets/2f46516b-2b70-41e2-96ca-b6a31e4e3c83)

---

## ⚙️ Tech Stack

* Java 22
* Makefile for running main class
* Custom resources format

---

## 🏁 Authors

* Soukaïna Mahboub Mehboub (`soukaina.mahboub`)
* Alexandre Vinent Padrol (`alexandre.vinent`)


---

## 📜 License

This project is for educational purposes and not affiliated with the official Scrabble® brand.

---

## 🔗 Useful Links

* [Official Scrabble Rules](https://scrabble.hasbro.com/en-us/rules)


Enjoy playing and expanding your vocabulary! 🧩✨


