

    ToLaserBlade 1.12.2-1.1.0


動作環境

    Java 8
    Minecraft 1.12.2
    Minecraft Forge 1.12.2-14.23.3.2655+


インストール方法

    JARファイルをmodsフォルダ内に置く


追加されるアイテム

    Laser B1ade / レーザーブレード

        種別： 剣
        攻撃速度： 2.8
        攻撃力： 5 (❤×2.5)
        エンチャント： エンチャントテーブルで剣タイプのエンチャントが可能

        レーザーブレイドを模して、木の棒にレッドストーンを塗った剣。
        バニラの剣よりも攻撃速度が少し速く、石の剣並みの攻撃力と、鉄の剣並みの耐久力がある。
        右クリックでレッドストーントーチに関連した隠し機能が使用できる。

        作業台 (Crafting)： r = Redstone Torch, s = Stick

            r
            r
            s


    Laser Blade / レーザーブレイド    (Class 1)

        種別： 剣
        攻撃速度： 2.8
        攻撃力： 6 (❤×3)
        エンチャント： エンチャントテーブルで剣タイプのエンチャントが可能
        刃の色： 赤

        古代文明の超技術で作られたかもしれない、レーザーの刃を持つ《絶対に刃こぼれしない》剣。
        レッドストーンの力でイオン添加ガラスを励起させ発生した光をダイヤモンドのレンズに通してレッドストーンの力場で棒状にしているという。
        なお、本来は切削用の工具であった。

        ちなみに金床で、エンチャントされていないレーザーブレイドの表示名を「おたから」または「GIFT」に変更すると……

        作業台 (Crafting)： i = Iron Ingot, d = Diamond, S = Stained Glass, r = Redstone (dust)

              i d
            i S i
            r i


    Laser Blade / レーザーブレイド    (Class 2)

        種別： 剣
        攻撃速度： 2.8
        攻撃力： 7 (❤×3.5)
        エンチャント： エンチャントテーブルで剣タイプのエンチャントが可能
        刃の色： 赤

        レーザー媒質をイオン添加ガラスからグロウストーンに変更して、出力を強化したレーザーブレイド。
        そのおかげか、攻撃力はダイヤモンドの剣並みになった。
        古代文明が栄えまだグロウストーンが地上でよく採れた時代にはこの発振方式が主流であったとある古文書は伝える。

        0.1.0以前からの引継ぎの場合は、残念ながら相応に弱体化してしまう。

        作業台 (Crafting)： i = Iron Ingot, d = Diamond, g = Glowstone Dust, r = Redstone (dust)

              i d
            i g i
            r i


    Laser Blade / レーザーブレイド    (Class 3)
    
        種別： 剣
        攻撃速度： 4
        攻撃力： 10 (❤×5)
        エンチャント： Smite (アンデッド特効) V をクラフト時に自動付与
        刃の色： 赤

        グロウストーンとダイヤモンドを増量して、出力をさらに強化したレーザーブレイド。
        ついでにレッドストーンを大量に添加することによって、攻撃時の出力回復速度も向上させた。
        出力強化の過程でたぶん光属性を持ち、特にアンデッドに対して強くなっている。

        作業台 (Crafting)： i = Iron Ingot, D = Diamond Block, G = Glowstone (block), R = Redstone Block

              i D
            i G i
            R i


    Laser Blade / レーザーブレイド    (Class 4)

        種別： 剣
        攻撃速度： 4
        攻撃力： 14 (❤×7)
        エンチャント： Smite (アンデッド特効) X、Sweeping Edge (範囲ダメージ増加) III を追加付与。ただしSmiteと相反するエンチャントは消滅する
        刃の色： 赤

        魔術的なレアアイテムを組み込んで、出力をさらにさらに強化したレーザーブレイド。
        攻撃力が増加し、アンデッドに対してもさらに強くなった。
        最早これは工具というレベル、ではない。

        金床 (Anvil)：
        
            Laser Blade (Class 1-3) + Nether Star : Cost level 20


    Laser Blade / レーザーブレイド    (Dyeing 刃染色レシピ)

        攻撃力／攻撃速度／エンチャント： クラフト前のものを保持
        刃の色： クラフト時にプレイヤーがいるバイオームによって変化

            赤 (Plains, Forest, Swampland, ...）
            黄 (Jungle, ...）
            緑 (Taiga, Extreme Hills, ...）
            青 (Ice Plains, ...）
            藍 (Cold Taiga, ...）
            紫 (Desert, Mesa, ...）
            茜 (Savanna, ...)
            白 (Hell)
            ？ (???)

        GUIのクラフトスロット内では元の色のままであるが実際にクラフトすると染色される。
        クラフトスロットから取り出すときはマウスでドラッグすること。（Shiftクリックで取り出してしまうと染色されない）
        クラフト後、アイテムの情報が更新されたタイミングで刃の色が変化する。

        クラフト (Crafting)： L = Laser Blade

            L



リソースパックによるモデルの差し替え

    リソースパックで以下のOBJモデルを差し替えることで、レーザーブレイドのモデルを差し替えることができる。

        assets/tolaserblade/models/item/laser_blade.obj

    ToLaserBladeでは以下に示す特定のOBJグループ（gステートメントで指定）のみがゲーム内で描画される。

        Hilt            通常通り描画されるパーツ。ゲーム内では陰影や環境光の影響を受ける
        Blade_core      刃部分の芯となるパーツ。ゲーム内では加算合成で描画される。ただし特定のバイオームで刃を染色すると減算合成で描画される
        Blade_halo_1    Blade_coreの周囲を覆っているパーツ。ゲーム内では刃の色によって染色され、加算合成で描画される
        Blade_halo_2    Blade_halo_1のさらに周囲を覆っているパーツ。Blade_halo_1と同様に描画される

    MTLファイルについては以下のパスに配置し、OBJファイル内でmtllibステートメントによって指定する。

        assets/tolaserblade/models/item/laser_blade.mtl

        # OBJファイル内
        mtllib laser_blade.mtl

    ちなみに、MTLファイルのみを編集してリソースパックで差し替えるだけでも、描画されるモデルの色や透明度を変えることができる。

    テクスチャは以下のPNGファイルを差し替えることで変更できる。

        assets/tolaserblade/textures/items/laser_blade.png

    OBJファイルでテクスチャファイルを使う場合、上記テクスチャのみ使用可能。
    このテクスチャをMTLファイル内で指定するにはmap_Kdステートメントで以下のように設定する（モデラーから出力後テキストエディタなどで編集）。

        map_Kd tolaserblade:items/laser_blade


    当mod同梱のリソースパックは、クリエイティブ・コモンズ 表示 - 非営利 4.0 国際 ライセンス (CC BY-NC 4.0) の下で配布されている。
    ライセンスについて詳しくは、assets/tolaserblade/LICENSE.txt または https://creativecommons.org/licenses/by-nc/4.0/deed.ja を参照のこと。


Copyright 2018 Iunius118
https://github.com/Iunius118/ToLaserBlade
