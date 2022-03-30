# Fix MC-248936

*Read in other languages: English, [한국어](README.ko.md), [日本語](README.ja.md).*

--------

Introduction
--------
- This mod fixes the [issue](https://bugs.mojang.com/browse/MC-248936) that Minecraft icon does not appear while running in macOS Dock on Minecraft 1.13 client or above.
- Also, this mod fixes the `GLFW error 65548: Cocoa: Regular windows do not have icons on macOS` error when you run Minecraft 1.17.1 client or lower on Apple Silicon devices.
- This mod provides icon pack containing 9 icons for players who run on macOS Big Sur or above.
  ![icon packs](https://user-images.githubusercontent.com/45729082/159682087-7deeb3ec-5d9a-42b6-a0ce-c6fd502a4017.png)
- You can change Dock icon using client command `/changeicon <icon name>` or add icon using **Custom Resource Pack**, if you want.
  ![preview](https://user-images.githubusercontent.com/45729082/159741680-813d91b8-82e2-4d7a-bead-9cd1402e4710.gif)


Caution!
--------
- [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api) is required.
- This mod is only available on Java 11 or above.


Build
--------
1. Type `./gradlew build` in Terminal.
2. extract mod from `build/libs/` directory.


Download
--------
- You can download it from right release tab.


Contribution
--------
- We open our source code to public for community development, and we appreciate contribution for mod development.
