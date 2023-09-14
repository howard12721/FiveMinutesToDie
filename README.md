# FiveMinutesToDie

5分後に死ぬ

Spigot 1.20

## 💡 使い方

### 💬コマンド

| コマンド              | 引数 | 説明                | パーミッション |
|-------------------|----|-------------------|---------|
| /timebomb enable  |    | 爆弾を起動します          | `OP`    |
| /timebomb disable |    | 爆弾を停止します          | `OP`    |
| /timebomb reload  |    | 設定ファイルの再読み込みを行います | `OP`    |

### 💬設定ファイル
 `plugins/FiveMinutesToDie/config.yml` を編集することで動作をカスタマイズ出来ます
 ```yml
 timelimit: 300.0 #制限時間 単位:秒

 bossbar-timer:
   enabled: false # タイマーを画面上に表示するかどうか true: 有効 / false: 無効
   display: "§l残り時間: §a§l%MINUTES%分%SECONDS%秒" # 残り時間の表示 %MINUTES%が残り分数 %SECONDS%が残り秒数に置き換えられます

 death:
   explosion-radius: 10 # 時間切れ時の爆発の威力 [参考] クリーパー: 3 TNT: 4 帯電クリーパー: 6
   death-message:
     enabled: true # 死亡時のメッセージ変更機能を有効にするかどうか true: 有効 / false: 無効
     content: "%PLAYER%は時間切れになったため死亡した" # 死亡時のメッセージ %PLAYER%がプレイヤー名に置き換えられます 変更しない場合は""に設定してください

 penalty:
   sneak: 0 # 1mスニークするごとにどれだけ時間を減らすか 単位: 秒
   walk: 0 # 1m歩くごとにどれだけ時間を減らすか 単位: 秒
   sprint: 0 # 1m歩くごとにどれだけ時間を減らすか 単位: 秒
   swim: 1.0 # 1m泳ぐごとにどれだけ時間を減らすか 単位: 秒
   climb: 0 # 1mはしごやツタを上るごとにどれだけ時間を減らすか 単位: 秒
   jump: 1.0 # ジャンプ1回ごとにどれだけ時間を減らすか 単位: 秒
   damage: 1.0 # 1ダメージを受けるごとにどれだけ時間を減らすか 単位:秒
 ```