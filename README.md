# Jungle Chess in Java

A desktop implementation of Jungle Chess (斗兽棋) built for SUSTech CS109. It uses Java Swing for the board and menus. The project includes game logic, an AI player, and networking code.

## Run

From the repository root, with a JDK installed and a desktop graphical session available:

```bash
javac -encoding UTF-8 -sourcepath src -d build src/Main.java
java -cp build Main
```

You can also open the project in an IDE, mark `src/` as a source directory, set the working directory to the repository root, and run [Main.java](src/Main.java). Board and audio assets are in [resource](resource).
