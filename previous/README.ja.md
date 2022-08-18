# Fix MC-248936

*他の言語で読む: [English](README.md), [한국어](README.ko.md), 日本語.*

--------

紹介
--------
- このMODはMinecraft 1.13以上のクライアントをmacOSにて起動する際、 **Dockのアイコンがロードできない[問題](https://bugs.mojang.com/browse/MC-248936)** を解決します。
- また、 Apple Siliconの機械にてMinecraft 1.18未満のクライアントを起動する際、発生する `GLFW error 65548: Cocoa: Regular windows do not have icons on macOS` エラーを解決します。
- macOS Big Sur以上のOSをお使いになっているプレイヤーさんのため、 ９つのアイコンが含まれているアイコンパックも用意されています。
  ![icon packs](https://user-images.githubusercontent.com/45729082/159682087-7deeb3ec-5d9a-42b6-a0ce-c6fd502a4017.png)
- クライアントコマンド `/changeicon <icon name>` を使ってDockのアイコンの変更ができ、ご所望の場合 **カスタムリソースパック** を通してアイコンを追加できます。
  ![preview](https://user-images.githubusercontent.com/45729082/159741680-813d91b8-82e2-4d7a-bead-9cd1402e4710.gif)


注意！
--------
- [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api) が必要です。
- Java 11以上のみ、お使いできます。


ビルド
--------
1. ターミナルにて `./gradlew build` を入力します。
2. `build/libs/` ディレクトリにてMODを抽出します。


ダウンロード
--------
- 右側のReleases欄にてダウンロードできます。


寄与
--------
- オープンソース生態系の発展のため、すべてのソースコードを公開し、MODの発展のための貢献を歓迎します。
