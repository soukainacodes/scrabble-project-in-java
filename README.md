![image](https://github.com/user-attachments/assets/50136039-8402-49f0-8c6d-6bb2e6106446)# Scrabble® - Java Edition

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

[Login Screen](capturas/screenshot1.png) 

### Resource Management

[Resource Management](capturas/screenshot3.png)

### In-Game Play

[In-Game Play](capturas/screenshot8.png) 

### Ranking Screen

[Ranking Screen](capturas/screenshot6.png) 

### End of game

[End of Game](capturas/screenshot9.png)

---

## ⚙️ Tech Stack

* Java 22
* Makefile for running main class
* Custom resources format

---

## 🏁 Authors

* Alexandre Vinent Padrol (`alexandre.vinent`)
* Soukaïna Mahboub Mehboub (`soukaina.mahboub`)

---

## 📜 License

This project is for educational purposes and not affiliated with the official Scrabble® brand.

---

## 🔗 Useful Links

* [Official Scrabble Rules](https://scrabble.hasbro.com/en-us/rules)


Enjoy playing and expanding your vocabulary! 🧩✨


