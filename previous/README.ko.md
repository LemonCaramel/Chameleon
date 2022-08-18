# Fix MC-248936

*다른 언어로 읽기: [English](README.md), 한국어, [日本語](README.ja.md).*

--------

소개
--------
- 이 모드는 Minecraft 1.12.2 상위 클라이언트가 macOS에서 실행될 때, **독 아이콘을 로드하지 못하는 [문제](https://bugs.mojang.com/browse/MC-248936)** 를 해결합니다.
- 또한, Apple Silicon 기기에서 Minecraft 1.18 미만 클라이언트를 실행했을 때 발생하는 `GLFW error 65548: Cocoa: Regular windows do not have icons on macOS` 오류를 해결합니다.
- macOS Big Sur 이상 OS를 사용하는 플레이어를 위한 9개의 아이콘이 포함된 아이콘 팩이 준비되어 있습니다.
  ![icon packs](https://user-images.githubusercontent.com/45729082/159682087-7deeb3ec-5d9a-42b6-a0ce-c6fd502a4017.png)
- 클라이언트 명령어 `/changeicon <icon name>`를 사용하여 Dock의 아이콘을 변경할 수 있으며, 원하는 경우 **사용자 지정 리소스팩**을 사용하여 아이콘을 추가할 수도 있습니다.
  ![preview](https://user-images.githubusercontent.com/45729082/159741680-813d91b8-82e2-4d7a-bead-9cd1402e4710.gif)


주의!
--------
- [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api) 가 필요합니다.
- Java 11 이상에서만 사용 가능합니다.


빌드
--------
1. 터미널에서 `./gradlew build` 를 입력합니다.
2. `build/libs/` 디렉토리에서 모드를 추출합니다.


다운로드
--------
- 오른쪽 릴리즈 탭에서 다운로드할 수 있습니다.


기여
--------
- 오픈소스 생태계의 발전을 위해 모든 소스코드를 공개하며, 모드의 발전을 위한 기여를 환영합니다.
