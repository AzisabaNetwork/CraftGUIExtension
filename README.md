# CraftGUI Extension

## 概要
アジ鯖のLife鯖にあるCraftGUIの後継開発版

変換するアイテムと変換に必要なアイテムをConfigから設定できます

## 動作環境
NMSを使用しているため，Minecraft Ver 1.15.2のみ使用可能です

## Configの設定
```yaml
# CraftGUI Extension Configuration
configVersion: 1.4

# 外部ファイルを参照したリロード時の参照先URL
ConfigURL: "url here"

# GUIに表示するアイテムの設定
Items:
  # ページ番号
  page1:
    # スロット番号 (1ページにつき0～44まで設定可能です)
    0:
      # 変換を有効にするかどうか
      enabled: true
      # アイテムID
      material: SPONGE
      # アイテムの表示名
      displayName: '&eスポンジ'
      # Loreの設定
      # Loresセクションで設定した項目を設定してください
      lore: 'CommonLore'
      # エンチャントするかどうか
      enchanted: false
      # カスタムモデルデータ番号
      model: 0
      # 変換に必要なアイテム
      requiredItems:
          # バニラアイテムであればtype, MythicMobsアイテムであればmmidを設定してください
        - type: YELLOW_DYE
          # アイテムの表示名
          # mmidを設定したとき二，変換するアイテムのmmidがnullの場合はこの項目が使用されます
          displayName: '&e黄色の染料'
          # 変換に必要なアイテムの個数
          amount: 4
        - type: GRAVEL
          displayName: '&f砂利'
          amount: 2
        - type: SAND
          displayName: '&f砂'
          amount: 2
        - type: HAY_BLOCK
          displayName: '&f俵'
          amount: 1
      # 変換する際に付与するアイテム
      resultItems:
          # バニラアイテムであればtype，MythicMobsアイテムであればmmidを設定してください
        - type: SPONGE
          # アイテムの表示名
          displayName: '&fスポンジ'
          # 付与する個数
          amount: 1

# Loreの設定
Lores:
  # Loreの設定名
  CommonLore:
    - '&f左クリックで1回変換します'
    - '&f右クリックで上限まで変換します'
```

## 使い方
- `/craftgui`：CraftGUI Extensionを開きます

- `/craftgui register`：アイテム登録GUIを開きます

  ※登録しても表示されないバグがあります(修正予定)

- `/craftgui config reload`：config.ymlを再読込します

- `/craftgui config reload --external`：外部リンクを参照して，configファイルを更新します（複数鯖導入時など更新が面倒なとき用）

## ライセンス
[GNU General Public License v3.0](LICENSE)
