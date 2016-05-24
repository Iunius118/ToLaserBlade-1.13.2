package iunius118.mods.tolaserblade;

import com.google.common.base.Function;

import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToLaserBladeRegistry {

	public static void registerItems() {
		GameRegistry.register(ToLaserBlade.Items.itemLaserBlade);

		registerItemRecipes();
	}

	public static void registerItemRecipes() {
		GameRegistry.addRecipe(new ItemStack(ToLaserBlade.Items.itemLaserBlade),
				" ID",
				"IGI",
				"RI ",
				'I', Items.IRON_INGOT,
				'D', Items.DIAMOND,
				'G', Items.GLOWSTONE_DUST,
				'R', Items.REDSTONE);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		ModelLoader.setCustomModelResourceLocation(ToLaserBlade.Items.itemLaserBlade, 0, ToLaserBlade.ModelLocations.mrlItemLaserBlade);
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