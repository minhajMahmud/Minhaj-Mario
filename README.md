# 🎮 Super Mario Game

A **2D platformer** inspired by the classic Nintendo **Super Mario** series. Experience nostalgic gameplay with modern twists, including multiple levels, enemies, power-ups, and boss fights!

---

## 📜 Project Overview

The **Super Mario Game** project is a run-type platform game where players control Mario (Minhaj) to overcome obstacles, defeat enemies, and reach the goal point. Built using **modular code structure** for scalability and flexibility.

### ✅ Key Features:

* **Classic Gameplay:** Run, jump, collect coins, and defeat enemies.
* **Multiple Levels:** Increasing difficulty with unique designs.
* **Enemies & Boss Fights:** Big bosses and small enemies with unique attack patterns.
* **Coins & Power-Ups:** Collect items to score points and boost abilities.
* **Retro Visuals:** Pixelated graphics for a nostalgic feel.
* **Modern Implementation:** Smooth controls and modular code for easy updates.

---

## 🎯 Objectives

* Traverse maps with increasing difficulty.
* Defeat enemies including bosses.
* Collect diamonds and coins for points.
* Complete levels by reaching the destination point.

---

## 🕹 Target Users

* Children
* Casual gamers
* Anyone who loves platformer escape & run games

---

## 📂 Project Structure

```
SuperMarioGame/
│
├── assets/           # Images, sprites, sound effects
├── src/              # Source code files
│   ├── Main.java
│   ├── GameEngine.java
│   ├── Player.java
│   ├── Enemy.java
│   └── ...
└── README.md
```

---

## ⚙️ Technologies Used

* **Language:** Java (JavaFX or Swing)
* **IDE:** IntelliJ IDEA / NetBeans / Eclipse
* **Graphics:** Pixel art sprites
* **Sound:** Retro background music and effects

---

## ▶️ How to Run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/your-username/super-mario-game.git
   cd super-mario-game
   ```
2. **Compile and run the game:**

   ```bash
   javac src/*.java
   java -cp src Main
   ```

---

## 🧰 Build Windows Installer (.exe)

This repository includes `build-installer.ps1` to create an installable Windows `.exe` using `jpackage`.

It will:

* Compile all Java files from `source/`
* Create `Minhaj-Mario.jar`
* Copy `images/` and `maps/`
* Build installer output in `dist/`

Run from project root:

```powershell
powershell -ExecutionPolicy Bypass -File .\build-installer.ps1
```

If WiX is not installed, you can still build a portable package (contains `Minhaj-Mario.exe`) with:

```powershell
powershell -ExecutionPolicy Bypass -File .\build-installer.ps1 -PackageType app-image
```

Portable output path:

* `dist/Minhaj-Mario/Minhaj-Mario.exe`

After completion, install using the `.exe` file generated inside the `dist/` folder.

### 🚀 Publish `.exe` via GitHub Releases

This repo includes a GitHub Actions workflow at `.github/workflows/release-exe.yml`.

* On pushing a tag like `v1.0.0`, it builds the Windows installer `.exe` and attaches it to a GitHub Release.
* You can also run it manually from the **Actions** tab.

Example tag flow:

```bash
git tag v1.0.0
git push origin v1.0.0
```

---

## 🎮 Controls

| Action     | Key   |
| ---------- | ----- |
| Move Left  | ←     |
| Move Right | →     |
| Jump       | Space |
| Pause      | P     |

---

## 🖼 Screenshots

## 🧩 Future Enhancements

* Add multiplayer support
* Introduce more levels and worlds
* Implement leaderboard system
* Mobile-friendly version

---

## 👨‍💻 Author

**Mohammad Mahmudul Hasan**
🎓 Student, B.Sc. in Software Engineering
📧 Email: [mahmudul2517@student.nstu.edu.bd](mailto:mahmudul2517@student.nstu.edu.bd)

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!

---

