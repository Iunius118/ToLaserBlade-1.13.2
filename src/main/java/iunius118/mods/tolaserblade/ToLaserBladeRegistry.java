package iunius118.mods.tolaserblade;

import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import iunius118.mods.tolaserblade.client.renderer.RenderItemLaserBlade;
import iunius118.mods.tolaserblade.tileentity.TileEntityRenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.google.common.base.Function;

public class ToLaserBladeRegistry {

	public static void registerItems() {
		GameRegistry.register(ToLaserBlade.Items.itemLaserBlade);
		MinecraftForge.EVENT_BUS.register(ToLaserBlade.Items.itemLaserBlade);

		registerItemRecipes();
	}

	public static void registerItemRecipes() {
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						new ItemStack(ToLaserBlade.Items.itemLaserBlade),
						" ID",
						"IGI",
						"RI ",
						'I', "ingotIron",
						'D', "gemDiamond",
						'G', "dustGlowstone",
						'R', "dustRedstone")
				);

		ItemStack smiteBlade = new ItemStack(ToLaserBlade.Items.itemLaserBlade);
		smiteBlade.addEnchantment(Enchantment.getEnchantmentByLocation("smite"), 10);
		GameRegistry.addRecipe(
				new ShapedOreRecipe(
						smiteBlade,
						" ID",
						"IGI",
						"RI ",
						'I', "ingotIron",
						'D', "blockDiamond",
						'G', "glowstone",
						'R', "blockRedstone")
				);


		GameRegistry.addRecipe(new ItemStack(ToLaserBlade.Items.itemLaserBlade),
				"L",
				'L', ToLaserBlade.Items.itemLaserBlade);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		ModelLoader.setCustomModelResourceLocation(ToLaserBlade.Items.itemLaserBlade, 0, ToLaserBlade.ModelLocations.mrlItemLaserBlade);

		ForgeHooksClient.registerTESRItemStack(ToLaserBlade.Items.itemLaserBlade, 0, TileEntityRenderItem.class);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRenderItem.class, new RenderItemLaserBlade());
	}

	@SideOnly(Side.CLIENT)
	public static void registerBakedModels(ModelBakeEvent event) {
		ModelLaserBlade modelLaserBlade = new ModelLaserBlade(
				bakeModel(ToLaserBlade.ModelLocations.rlOBJItemLaserBlade),
				event.getModelRegistry().getObject(ToLaserBlade.ModelLocations.mrlItemLaserBlade));

		event.getModelRegistry().putObject(ToLaserBlade.ModelLocations.mrlItemLaserBlade, modelLaserBlade);
	}


	@SideOnly(Side.CLIENT)
	public static IBakedModel bakeModel(ResourceLocation location) {
		try {
			IModel model = ModelLoaderRegistry.getModel(location);
			IBakedModel bakedModel = model.bake(model.getDefaultState(),
					DefaultVertexFormats.ITEM,
					new Function<ResourceLocation, TextureAtlasSprite>() {

						@Override
						public TextureAtlasSprite apply(ResourceLocation location) {
							Minecraft mc = Minecraft.getMinecraft();
							return mc.getTextureMapBlocks().getAtlasSprite(location.toString());
						}

					});

			return bakedModel;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
