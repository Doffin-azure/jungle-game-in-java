# Jungle Chess in Java

A desktop implementation of Jungle Chess (斗兽棋) built for SUSTech CS109. It uses Java Swing for the board and menus. The project includes game logic, an AI player, and networking code.

## Run

From the repository root, with a JDK installed and a desktop graphical session available:

```bash
javac -encoding UTF-8 -sourcepath src -d build src/Main.java
java -cp build Main
```

Keep the working directory at the repository root so the game can find its board and audio assets in [resource](resource).
