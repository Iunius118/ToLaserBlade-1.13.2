package iunius118.mods.tolaserblade;

import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToLaserBladeRegistry {

	public static void registerItems() {
		GameRegistry.register(ToLaserBlade.Items.itemLaserBlade);
	}

	public static void registerRecipes() {
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
				event.getModelRegistry().getObject(ToLaserBlade.ModelLocations.mrlItemLaserBlade));

		event.getModelRegistry().putObject(ToLaserBlade.ModelLocations.mrlItemLaserBlade, modelLaserBlade);
	}

}
