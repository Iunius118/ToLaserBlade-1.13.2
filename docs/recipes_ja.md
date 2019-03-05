## ToLaserBlade アイテム解説

以下の解説は、バージョン 1.13.2-1.4.1.0 時点での仕様に基づいて書かれています。

- [パージョン 1.12.2-1.3.1.0 の解説はこちら](https://github.com/Iunius118/ToLaserBlade/blob/1.12.2_1.3.1/docs/recipes_ja.md)

### Laser B1ade / レーザーブレード  (Prop / 模造品)

<img src="img/recipe_laserb1ade.png" title="Laser B1ade recipe">

>レーザーブレイドを模して木の棒にレッドストーンを塗った剣。バニラの剣よりも少しだけ攻撃力の回復が早く、石の剣並みの攻撃力と鉄の剣並みの耐久力がある。右クリックでレッドストーントーチに関連した隠し機能が使用できる。

- タイプ：剣
- 攻撃速度：2.8
- 攻撃力：5
- 耐久度：255
- エンチャントテーブルで剣タイプのエンチャントが可能
- 耐久度が半分以上残っているとき、地面や壁を右クリックすると耐久度を消費してレッドストーントーチを設置する
- 耐久度が半分未満のとき、設置されたレッドストーントーチを右クリックするとそれを消費して耐久度を回復する
- 耐久度が半分以上のとき、設置されたレッドストーントーチを右クリックするとそれを回収する

### Laser Blade / レーザーブレイド

- タイプ：剣
- 耐久度：∞
- クラフト方法によって性能がクラス1～4まで変化する
- （設定ファイルの変更で有効にすると）右マウスボタンで盾のようにガード（ブロッキング）することができる

#### クラス 1

<img src="img/recipe_laserblade_class_1.png" title="Recipe Laser Blade class 1 A"> <img src="img/recipe_laserblade_class_1_dyeing.png" title="Recipe Laser Blade class 1 B">

>古代文明の超技術で作られたかもしれないレーザーの刃を持つ《絶対に刃こぼれしない》剣。レッドストーンの力によって励起したイオン添加ガラスの発する光を反射によって増幅し、ダイヤモンドのレンズで収束させてからレッドストーンの力場に閉じ込めることで棒状にしているという。なお本来の用途は切削用の工具であった。

- 攻撃速度：2.8
- 攻撃力：6
- 地上（オーバーワールド）の素材のみでクラフト可能
- 中央はガラスまたは色付きガラス。色付きガラスの場合はその色に応じて刃の周辺部分の色が変わる
- エンチャントテーブルで剣タイプのエンチャントが可能

#### クラス 2

<img src="img/recipe_laserblade_class_2.png" title="Recipe Laser Blade Class 2">

>レーザー媒質をイオン添加ガラスからグロウストーンに変更して出力を強化したレーザーブレイド。そのおかげか攻撃力はダイヤモンドの剣並みになった。古代文明が栄えまだグロウストーンが地上でよく採れた時代にはこの発振方式が主流であったとある古文書は伝える。

- 攻撃速度：2.8
- 攻撃力：7
- エンチャントテーブルで剣タイプのエンチャントが可能

#### クラス 3

<img src="img/recipe_laserblade_class_3.png" title="Recipe Laser Blade Class 3">

>グロウストーンとダイヤモンドを増量して出力をさらに強化したレーザーブレイド。ついでにレッドストーンを大量に添加することによって攻撃時の出力回復速度も向上させた。出力強化の過程でたぶん光属性を持ち、特にアンデッドに対して強くなっている。

- 攻撃速度：4
- 攻撃力：10
- エンチャント：アンデッド特効 (Smite) V
- クラフトした時点でエンチャントされている

クラス1～2のレーザーブレイド（ただし未エンチャントに限る）は、以下のように金床を使ってクラス3相当に強化することが可能。

<img src="img/recipe_laserblade_class_3_gift_x.png" title="Name GIFT or おたから">

- 未エンチャントのレーザーブレイドの名前を金床で特定のワードに変更する
- ヒント：かな4文字［A5=お, T2U, A1L=か, W1R］

#### クラス 4

<img src="img/recipe_laserblade_class_4.png" title="Recipe Laser Blade Class 4">

>魔術的なレアアイテムを組み込んで出力をさらにさらに強化したレーザーブレイド。攻撃力がさらにさらに増加し、アンデッドに対してもさらにさらに強くなった。もはやこれは工具というレベルではない。

- 攻撃速度：4
- 攻撃力：14
- エンチャント：アンデッド特効 (Smite) X、範囲ダメージ増加 (Sweeping Edge) III
- 強化前のレーザーブレイドにアンデッド特効と相反するエンチャントが付いていた場合は、アンデッド特効で上書きされる

#### クラス 4+

<img src="img/recipe_laserblade_class_4_plus.png" title="Recipe Laser Blade Class 4 plus">

- 金床でクラス4のレーザーブレイドとバニラのmobの頭やプレイヤーの頭を合成すると、攻撃力をさらに上げることができる。

#### 刃染色レシピ

<img src="img/recipe_laserblade_biome_dyeing.png" title="Laser Blade dyeing by biome">

- 単体クラフトすると刃の周辺部分の色がプレイヤーのいるバイオーム（多くはその基準温度）に応じて変化する
- 刃の色は全部で9色
- クラフトスロットからはマウスでドラッグして取り出すこと（Shiftクリックの場合は染色されない）
- クラフト後アイテムの情報が更新されるまでは何色に染色されたのかはわからない
- 性能はクラフト前のものが引き継がれる

<img src="img/recipe_laserblade_anvil_dyeing_1.png" title="Laser Blade dyeing by dye">

- 金床で染料と合成するとその色に応じて刃の中心部分の色を変更することができる


<img src="img/recipe_laserblade_anvil_dyeing_2.png" title="Laser Blade dyeing by stained glass">

- 金床で色付きガラスと合成するとその色に応じて刃の周辺部分の色を変更することができる

## ToLaserBlade 設定項目

設定ファイルはmod導入後の起動時にゲームフォルダの`config`フォルダ内に自動生成される。

### tolaserblade-common.toml

サーバー・クライアント共通の設定ファイル。

- enableBlockingWithLaserBlade
  - 真偽値、デフォルトは`false`
  - レーザーブレイド装備時に右マウスボタンでガードすることが可能か（`true` / `false`）
  - マルチプレイワールドではサーバー側の設定が優先される

### tolaserblade-client.toml

クライアント側限定の設定ファイル。

- enableLaserBlade3DModel
  - 真偽値、デフォルトは`true`
  - レーザーブレイドの描画に、`true`のときは3D（OBJ）モデルを使用し、`false`のときは2D（JSON）モデルを使用する
  - この設定はクライアント側のみで使用される
  <!-- - ゲーム内のmodオプション設定GUIでも変更可能 -->

---
Copyright 2019 Iunius118
