# ToLaserBlade

[<img src="docs/img/tolaserblade_1.png" title="The Laser Blade" width="427">](docs/img/tolaserblade_1.png)

ToLaserBlade（T. o. Laser Blade）はMinecraftの世界に剣タイプのツール「レーザーブレイド」（Laser Blade）を追加するmodです。

## Versions

- v 1.0.0 for Minecraft 1.12.2 [[Readme](https://github.com/Iunius118/ToLaserBlade/blob/1.12.2_1.0.0/src/main/resources/README_ToLaserBlade.txt)]
- v 0.0.6 for Minecraft 1.11.2 [[Readme](https://github.com/Iunius118/ToLaserBlade/blob/1.11.2_0.0.6/src/main/resources/README_ToLaserBlade.txt)]
- v 0.0.5 for Minecraft 1.9.4 - 1.10.2 [[Readme](https://github.com/Iunius118/ToLaserBlade/blob/0.0.5/src/main/resources/README_ToLaserBlade.txt)]

## Downloads

- [ToLaserBlade-1.12.2-1.0.0.jar (from GitHub)](https://github.com/Iunius118/ToLaserBlade/releases/download/v1.12.2-1.0.0/ToLaserBlade-1.12.2-1.0.0.jar) for Minecraft 1.12.2 and Forge 1.12.2-14.23.3.2655+

その他のバージョンは[Releases](https://github.com/Iunius118/ToLaserBlade/releases)ページからダウンロードしてください。

## Description

以下の解説は、バージョン1.0.0時点での仕様に基づいて書かれています。

### Laser B1ade / レーザーブレード

<img src="docs/img/recipe_laserb1ade.png" title="Laser B1ade recipe">

- タイプ：剣
- 攻撃速度：2.8
- 攻撃力：5
- 耐久度：255
- 耐久度が半分以上残っているとき、地面や壁を右クリックすると耐久度を消費してレッドストーントーチを設置する
- 耐久度が半分未満のとき、設置されたレッドストーントーチを右クリックするとそれを消費して耐久度を回復する
- 耐久度が半分以上のとき、設置されたレッドストーントーチを右クリックするとそれを回収する

### Laser Blade / レーザーブレイド

- タイプ：剣
- 耐久度：∞
- クラフト方法によって性能が変化する

#### 基本レシピ

<img src="docs/img/recipe_laserblade.png" title="Laser B1ade recipe 1">

- 攻撃速度：2.8
- 攻撃力：7

#### 廉価版レシピ

<img src="docs/img/recipe_laserblade_o.png" title="Laser B1ade recipe 2">

- 攻撃速度：2.8
- 攻撃力：6
- 地上（オーバーワールド）の素材のみでクラフト可能
- 中心の色付きガラスはどの色のものでもよい

#### 強化版レシピ

<img src="docs/img/recipe_laserblade_v.png" title="Laser B1ade recipe 3">

- 攻撃速度：4
- 攻撃力：10
- エンチャント：アンデッド特効 (Smite) V
- クラフトした時点でエンチャントされている

#### 金床強化 1

<img src="docs/img/recipe_laserblade_gift.png" title="name GIFT or おたから">

- 攻撃速度：4
- 攻撃力：10
- エンチャント：アンデッド特効 (Smite) V
- 未エンチャントのレーザーブレイドの名前を金床で特定のワードに変更する
- 廉価版レシピのレーザーブレイドを強化版レシピ相当に強化することが可能

#### 金床強化 2

<img src="docs/img/recipe_laserblade_x.png" title="Much Stronger Laser B1ade">

- 攻撃速度：4
- 攻撃力：14
- エンチャント：アンデッド特効 (Smite) X、範囲ダメージ増加 (Sweeping Edge) III
- 強化前のレーザーブレイドにアンデッド特効と相反するエンチャントが付いていた場合は、アンデッド特効で上書きされる

#### 刃染色レシピ

<img src="docs/img/recipe_laserblade_dyeing.png" title="Laser B1ade dyeing">

- 性能はクラフト前のものが引き継がれる
- 刃の色はクラフトした時点でプレイヤーがいたバイオームよって決定される
- クラフトスロットからはマウスでドラッグして取り出すこと（Shiftクリックの場合は染色されない）
- クラフト後アイテムの情報が更新されるまでは何色に染色されたのかはわからない
- 刃の色は全部で8色

---
Copyright 2018 Iunius118
