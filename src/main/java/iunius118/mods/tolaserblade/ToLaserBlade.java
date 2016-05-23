package iunius118.mods.tolaserblade;

import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ToLaserBlade.MOD_ID,
	name = ToLaserBlade.MOD_NAME,
	version = ToLaserBlade.MOD_VERSION,
	dependencies = ToLaserBlade.MOD_DEPENDENCIES,
	acceptedMinecraftVersions = ToLaserBlade.MOD_ACCEPTED_MC_VERSIONS,
	useMetadata = true)
public class ToLaserBlade {

	public static final String MOD_ID = "tolaserblade";
	public static final String MOD_NAME = "ToLaserBlade";
	public static final String MOD_VERSION = "0.0.1";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.9.4-12.17.0.1910,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.9.4]";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ToLaserBladeRegistry.registerItems();
		ToLaserBladeRegistry.registerRecipes();

		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(this);
			ToLaserBladeRegistry.registerItemModels();
		}
	}

	public static class Items {
		private static final String NAME_ITEM_TO_LASER_BLADE = "tolaserblade.laser_blade";

		public static Item itemLaserBlade = new ItemLaserBlade().setRegistryName(NAME_ITEM_TO_LASER_BLADE);
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static ModelResourceLocation mrlItemLaserBlade = new ModelResourceLocation(new ResourceLocation(ToLaserBlade.Items.itemLaserBlade.getRegistryName() + ".obj"), "inventory");
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		ToLaserBladeRegistry.registerBakedModels(event);
	}

}
