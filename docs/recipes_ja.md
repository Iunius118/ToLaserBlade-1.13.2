# ToLaserBlade v1.13.2-1.5.0.2 (Beta)

## ToLaserBlade ダウンロード

- [ダウンロードサイト（Curse Forge）](https://minecraft.curseforge.com/projects/tolaserblade)

## ToLaserBlade アイテム解説

以下の解説は、バージョン 1.13.2-1.5.0.2 時点での仕様に基づいて書かれています。

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
- 耐久度：32000
- エンチャントテーブルでのエンチャントは不可
- （設定ファイルの変更で有効にすると）右マウスボタンで盾のようにガード（ブロッキング）することができる

#### クラス 1

<img src="img/recipe_laserblade_class_1.png" title="Recipe Laser Blade class 1 A"> <img src="img/recipe_laserblade_class_1_dyeing.png" title="Recipe Laser Blade class 1 B">

>古代文明の超技術で作られたかもしれないレーザーの刃を持つ《絶対に刃こぼれしない》剣。レッドストーンの力で励起させたイオン添加ガラスの発する光を反射によって増幅し、ダイヤモンドのレンズで収束させてからレッドストーンの力場に閉じ込めることで棒状にしているという。なお本来の用途は切削用の工具であった。

- 攻撃速度：2.8
- 攻撃力：6
- 地上（オーバーワールド）の素材のみでクラフト可能
- 中央はガラスまたは色付きガラス。色付きガラスの場合はその色に応じて刃の周辺部分の色が変わる
- エンチャント：アンデッド特効（Smite） I
- クラフトした時点でエンチャントされている

#### クラス 2

<img src="img/recipe_laserblade_class_2.png" title="Recipe Laser Blade Class 2">

>レーザー媒質をイオン添加ガラスからグロウストーンに変更して出力を強化したレーザーブレイド。そのおかげか攻撃力はダイヤモンドの剣並みになった。古代文明が栄えまだグロウストーンが地上でよく採れた時代にはこの発振方式が主流であったとある古文書は伝える。

- 攻撃速度：2.8
- 攻撃力：7
- エンチャント：アンデッド特効（Smite） II
- クラフトした時点でエンチャントされている

#### 刃染色レシピ

<img src="img/recipe_laserblade_biome_dyeing.png" title="Laser Blade dyeing by biome">

- 単体クラフトすると刃の周辺部分の色がプレイヤーのいるバイオーム（多くはその基準温度）に応じて変化する
- 刃の色は全部で9色
- クラフトスロットからはマウスでドラッグして取り出すこと（Shiftクリックの場合は染色されない）
- クラフト後アイテムの情報が更新されるまでは何色に染色されたのかはわからない
- 性能はクラフト前のものが引き継がれる

#### 金床での刃染色

<img src="img/recipe_laserblade_anvil_dyeing_1.png" title="Laser Blade dyeing by dye">

- 金床で染料と合成するとその色に応じて刃の中心部分の色を変更することができる


<img src="img/recipe_laserblade_anvil_dyeing_2.png" title="Laser Blade dyeing by stained glass">

- 金床で色付きガラスと合成するとその色に応じて刃の周辺部分の色を変更することができる

#### 金床での合成

<img src="img/combine_laserblades_example.png" title="Combine Laser Blades"> <img src="img/combine_laserblade_and_core.png" title="Combine Laser Blade and Core"> <img src="img/combine_laserblade_and_enchantedbook.png" title="Combine Laser Blade and Enchanted Book">

- A（レーザーブレイド、レーザーブレイドのコア）＋ B（レーザーブレイド、レーザーブレイドのコア、エンチャントの本）→ AにBの持っているエンチャントを合成
- ダメージ増加（Sharpness）は攻撃力に、耐久力（Unbreaking）は攻撃速度に変換される

#### 金床での強化

<img src="img/upgrade_laserblade_example.png" title="Upgrade Laser Blade Example">

- 金床で特定のアイテムと一定のレベルを消費することによりレーザーブレイドを強化することができる

<img src="img/upgrade_laserblade_and_cost_1.5.0.0_ja.png" title="Upgrade Laser Blade and Cost">

#### 金床での修理

<img src="img/repair_laserblade.png" title="Repair Laser Blade">

- 残りの耐久値に応じた数の鉄インゴットとレベルを消費する

### Laser Blade Core / レーザーブレイドのコア

<img src="img/laserblade_core.png" title="Laser Blade Core">

>レーザーブレイドのコアとなるレーザー発振器。レッドストーンのエネルギーは非常に強力であると同時にとても安定しているため、（我々の期待に反して）手荒に扱っても大爆発を起こしたりするようなことはない。レーザーデバイスの規制が強化される以前は電気街や観光地の土産物屋で簡単に入手することができたという。

- レーザーブレイドが消耗により破壊された際に手に入る

<img src="img/extract_core.png" title="Extract Core">

- また金床で鉄の斧を消費して強制的にコアを取り出すこともできる

<img src="img/repair_laserblade_core.png" title="Repair Laser Blade Core">

- コアは金床で鉄インゴットとレベルを消費してレーザーブレイドに戻すことができる

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
