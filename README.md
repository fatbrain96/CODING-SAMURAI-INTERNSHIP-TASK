# CODING-SAMURAI-INTERNSHIP-TASK

This repository contains the projects I developed during my internship.  
Each project is organized in its own folder.

---

## 📂 Projects

### 1. [Online Quiz Application](./OnlineQuizApplication)
- **Language/Tech:** Java (Swing, JSON API)
- **Description:**  
  A quiz game that fetches questions dynamically from an online API and presents them in a GUI.  
  Features score tracking and result display.

---

### 2. [ATM Project](./ATM)
- **Language/Tech:** Java  
- **Description:**  
  A simple ATM simulation system with features like:
  - Balance inquiry  
  - Deposit  
  - Withdrawal  
  - PIN verification  

  Focuses on **OOP concepts** and clean code structure.

---

## 🚀 How to Run

### Online Quiz Application
```bash
cd OnlineQuizApplication
javac -cp "lib/json-20230227.jar;." src/main/*.java
java -cp "lib/json-20230227.jar;out" main.QuizApplication
