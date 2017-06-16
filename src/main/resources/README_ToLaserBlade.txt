
	ToLaserBlade 1.12-0.0.6-b0

	*** これはベータテスト版です。ご理解の上ご使用ください。 ***


前提mod（必須）
	Minecraft 1.12
	Minecraft Forge 1.12-14.21.0.2331以降

インストール方法
	JARファイルをmodsフォルダ内に置く

追加されるアイテム

	Laser Blade / レーザーブレイド
		種別: 剣
		攻撃力: 14（❤×7）
		攻撃速度: 4

		・基本レシピ
			エンチャント： エンチャントテーブルで剣タイプのエンチャントが可能
			刃の色： 赤
			レシピ： i = Iron Ingot, d = Diamond, g = Glowstone Dust, r = Redstone (Dust)

				 id
				igi
				ri 

		・アンデッド特効レシピ
			エンチャント： アンデッド特効（Smite）X をクラフト時に自動付与
			刃の色： 赤
			レシピ： i = Iron Ingot, D = Diamond Block, G = Glowstone (Block), R = Redstone Block

				 iD
				iGi
				Ri 

		・刃染色レシピ
			エンチャント： クラフト前のものを保持
			刃の色： クラフト時にプレイヤーがいるバイオームによって変化（GUIのクラフトスロット内では元の色のままだが実際にクラフトした時点で色が変わる）
				赤（Plains, Forest, Swampland など）
				黄（Jungle など）
				緑（Taiga, Extreme Hills など）
				青（Ice Plains など）
				藍（Cold Taiga など）
				紫（Desert, Savanna, Mesa など）
				白（Hell)
				？（???)
			レシピ： L = Laser Blade

				L


リソースパックによるモデルの差し替え

	リソースパックで以下のOBJモデルを差し替えることで、レーザーブレイドのモデルを差し替えることができる

		assets/tolaserblade/models/item/laser_blade.obj

	ToLaserBladeでは以下に示す特定のOBJグループ（gステートメントで指定）のみがゲーム内で描画される

		Hilt	通常通り描画されるパーツ。ゲーム内では陰影や環境光の影響を受ける
		Blade_core	刃部分の芯となるパーツ。ゲーム内では加算合成で描画される。ただし特定のバイオームで刃を染色すると減算合成で描画される
		mat_Blade_halo_1	Blade_coreの周囲を覆っているパーツ。ゲーム内では刃の色によって染色され、加算合成で描画される
		mat_Blade_halo_2	mat_Blade_halo_1のさらに周囲を覆っているパーツ。mat_Blade_halo_1と同様に描画される

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
