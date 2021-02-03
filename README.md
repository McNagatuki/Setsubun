# Setsubun Plugin

## Description
節分用のプラグイン  
プレイヤーは鬼と一般人に分かれます。  
鬼は金棒を持ち、プレイヤーのせん滅を目指す。  
プレイヤーはなるべく鬼から逃げる。そのさい、豆（雪玉）を投げることが出来る。  
雪玉に当たった鬼は短時間の間足が遅くなる（鬼は外）。  
またプレイヤーに当たった場合は若干早くなる（福は内）。

## Usage

### command

- setsubun
  - on  
    プラグインの初期化処理
  - off  
    プラグインの終了処理
  - add "Player"  
    プレイヤーを鬼に追加
  - del "Player"  
    プレイヤーを鬼から消去
  - clear  
    全ての鬼を消去
  - start  
    ゲーム開始
  - start "second"  
    ゲーム開始（終了時間を秒で指定）  
    鬼には金棒が、一般人には豆64個が配られる。
  - stop  
    ゲーム終了
  - set
    - mame <amount>  
      豆の配布数を設定  
      (default: 256) 
    - oni_speed "0.0 ~ 1.0"  
      鬼の移動速度を設定（通常プレイヤーが0.2）  
      (default:0.4) 
    - oni_potion_time "ticks"  
      鬼の減速時間を設定  
      (default: 60) 
    - oni_potion_amp "level"  
      鬼の原則度合いを設定  
      (default: 3) 
    - player_potion_time "ticks"  
      プレイヤーの加速時間を設定  
      (default: 40) 
    - player_potion_amp "level"  
      プレイヤーをの加速度合いを設定  
      (default: 1) 
  - give
    - "Player" mame "amount"  
      Playerに豆をamountだけ配布する
    - "Player" kanabo "amount"  
      Playerに金棒をamountだけ配布する

### example

#### case 1

- 鬼は"Player1"と"Player2"
- ゲーム時間は3分(自動で終了)

```
/setsubun on
/setsubun add Player1
/setsubun add Player2
/setsubun start 180
/setsubun off
```

#### case 2
 
 - 鬼は"Player1"
 - ゲーム終了は手動で行う
 
```
/setsubun on
/setsubun add Player1
/setsubun start
/setsubun stop
/setsubun off
```
