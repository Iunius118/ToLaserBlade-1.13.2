

    ToLaserBlade 1.12-0.1.1


前提mod（必須）

    Minecraft 1.12
    Minecraft Forge 1.12-14.21.1.2387以降


インストール方法

    JARファイルをmodsフォルダ内に置く


追加されるアイテム

    Laser B1ade / レーザーブレード

        種別：　剣
        攻撃力：　5 （❤×2.5）
        攻撃速度：　2.8
        エンチャント：　エンチャントテーブルで剣タイプのエンチャントが可能。金床でエンチャントの合成も可能

        レーザーブレイドを模して、木の棒にレッドストーンを塗った剣。
        バニラの剣よりも攻撃速度が少し早く、石の剣並みの攻撃力と、鉄の剣並みの耐久力がある。
        右クリックでレッドストーントーチに関連した隠し機能が使用できる。

        レシピ：　r = Redstone Torch, s = Stick

            r
            r
            s


    Laser Blade / レーザーブレイド

        種別：　剣
        攻撃力：　7 （❤×3.5）
        攻撃速度：　2.8
        エンチャント：　エンチャントテーブルで剣タイプのエンチャントが可能。金床でエンチャントの合成も可能
        刃の色：　赤

        古代文明の技術で作られたかもしれない、レーザーの刃を持つ剣。
        ダイヤの剣並みの攻撃力を持ち、絶対に刃こぼれしない。
        なお、本来は切削用の工具であった。

	前バージョンからの引継ぎの場合は、残念ながら相応に弱体化してしまう。

        レシピ： i = Iron Ingot, d = Diamond, g = Glowstone Dust, r = Redstone (dust)

             id
            igi
            ri


    Laser Blade / レーザーブレイド    （強い）
    
        種別：　剣
        攻撃力：　10 （❤×5）
        攻撃速度：　4
        エンチャント：　アンデッド特効（Smite） V をクラフト時に自動付与。金床でエンチャントの合成が可能
        刃の色：　赤

        グロウストーンとダイヤモンドを増量して、出力を強化したレーザーブレイド。
        さらにレッドストーンを大量に添加することによって、攻撃時の出力回復速度を向上させた。
        出力強化の過程でたぶん光属性を持ち、特にアンデッドに対して強くなっている。

        レシピ： I = Iron Block, D = Diamond Block, G = Glowstone (block), R = Redstone Block

             ID
            IGI
            RI


    Laser Blade / レーザーブレイド    （もっと強い）

        種別：　剣
        攻撃力：　14（❤×7）
        攻撃速度：　4
        エンチャント：　アンデッド特効（Smite） X、範囲ダメージ増加（Sweeping Edge） III をクラフト時に自動付与。金床でエンチャントの合成が可能
        刃の色：　赤

        魔術的なレアアイテムを使用して、出力をさらに強化したレーザーブレイド。
        攻撃力が増加し、アンデッドに対してもさらに強くなった。
        もはやこれは工具ではなく、剣である。

        レシピ： d = Diamond, E = End Crystal, s = Nether Star, R = Redstone Block

             dE
            dsd
            Rd


    Laser Blade / レーザーブレイド    （刃染色レシピ）

        攻撃力・攻撃速度・エンチャント： クラフト前のものを保持
        刃の色： クラフト時にプレイヤーがいるバイオームによって変化

            赤（Plains, Forest, Swampland など）
            黄（Jungle など）
            緑（Taiga, Extreme Hills など）
            青（Ice Plains など）
            藍（Cold Taiga など）
            紫（Desert, Savanna, Mesa など）
            白（Hell)
            ？（???)

        GUIのクラフトスロット内では元の色のままだが、実際にクラフトした時点で染色される。
        また、クラフトスロットからはマウスでドラッグして取り出すこと。（Shiftクリックでは染色されない）
        バイオームの境界付近では位置ずれによって想定と異なる色に染色されることがあるが、アイテム情報が更新されると直る。

        レシピ： L = Laser Blade

            L



リソースパックによるモデルの差し替え

    リソースパックで以下のOBJモデルを差し替えることで、レーザーブレイドのモデルを差し替えることができる

        assets/tolaserblade/models/item/laser_blade.obj

    ToLaserBladeでは以下に示す特定のOBJグループ（gステートメントで指定）のみがゲーム内で描画される

        Hilt            通常通り描画されるパーツ。ゲーム内では陰影や環境光の影響を受ける
        Blade_core      刃部分の芯となるパーツ。ゲーム内では加算合成で描画される。ただし特定のバイオームで刃を染色すると減算合成で描画される
        Blade_halo_1    Blade_coreの周囲を覆っているパーツ。ゲーム内では刃の色によって染色され、加算合成で描画される
        Blade_halo_2    Blade_halo_1のさらに周囲を覆っているパーツ。Blade_halo_1と同様に描画される

    MTLファイルについては以下のパスに配置し、OBJファイル内でmtllibステートメントによって指定する

        assets/tolaserblade/models/item/laser_blade.mtl

        # OBJファイル内
        mtllib laser_blade.mtl

    ちなみに、MTLファイルのみを編集してリソースパックで差し替えるだけでも、描画されるモデルの色や透明度を変えることができる

    テクスチャは以下のPNGファイルを差し替えることで変更できる

        assets/tolaserblade/textures/items/laser_blade.png

    OBJファイルでテクスチャファイルを使う場合、上記テクスチャのみ使用可能
    このテクスチャをMTLファイル内で指定するにはmap_Kdステートメントで以下のように設定する（モデラーから出力後テキストエディタなどで要編集）

        map_Kd tolaserblade:items/laser_blade

    当mod同梱のリソースパックは Creative Commons の Attribution-ShareAlike（BY-SA、表示-継承）4.0 ライセンスで配布されている
    ライセンスについて詳しくは、assets/tolaserblade/LICENSE.txt または https://creativecommons.org/licenses/by-sa/4.0/deed.ja を参照のこと


Copyright 2017 Iunius118
https://github.com/Iunius118/ToLaserBlade
