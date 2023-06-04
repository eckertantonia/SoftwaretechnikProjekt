# SWTP Team 3

## Installation & Usage

1. Clone the [repository](https://scm.mi.hs-rm.de/rhodecode/2022swtpro/2022swtpro03/swtp-3)
2. `cd swtp-3`
3. `/gradlew build`
4. `java -jar build/libs/swtp-3-0.0.1-SNAPSHOT.jar`
5. Open a browser and go to `localhost:8080`
6. Enter username and press play
7. Join a server

or

1. Clone the [repository](https://scm.mi.hs-rm.de/rhodecode/2022swtpro/2022swtpro03/swtp-3)
2. `cd swtp-3`
3. `./gradlew bootRun`
4. open new terminal
5. `cd swtp-3/frontend`
6. `npm install`
7. `npx vite` or `npm run dev`
8. Open a browser and go to `localhost:5173`
9. Enter username and press play
10. Join a server

## Keyboad Shortcuts

### 2D

| Key | Action       |
| --- | ------------ |
| `R` | Rotate Block |

### 3D

| Key          | Action           |
| ------------ | ---------------- |
| `W`          | Move forward     |
| `A`          | Move left        |
| `S`          | Move backward    |
| `D`          | Move right       |
| `ArrowUp`    | Move forward     |
| `ArrowLeft`  | Move left        |
| `ArrowDown`  | Move backward    |
| `ArrowRight` | Move right       |
| `C`          | Switch camera    |
| `H`          | Honk/Bell        |
| `1`          | Select 1. song   |
| `2`          | Select 2. song   |
| `P`          | Play/Pause music |
| `ESC`        | Leave 3D View    |

## Links

- [Repository](https://scm.mi.hs-rm.de/rhodecode/2022swtpro/2022swtpro03/swtp-3)
- [Taiga](https://taiga.mi.hs-rm.de/project/admin-2022swtpro03/timeline)
- [Jenkins](https://build3.intern.mi.hs-rm.de/)
- [SonarQube](https://build3.intern.mi.hs-rm.de/sonar)

## Configuration

If one wishes to modify the users experience the `swtp.config.json` file can be modified. It holds all configuration options for the game. If it is modified the .jar must be rebuilt for the changes to take effect, if the application is being run using `bootRun` and `vite` a site refresh should reflect new changes.