package iunius118.mods.tolaserblade;

import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
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
	public static final String MOD_VERSION = "0.0.3[WIP]";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.9.4-12.17.0.1921,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.9.4]";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		OBJLoader.INSTANCE.addDomain(MOD_ID);
		ToLaserBladeRegistry.registerItems();

		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(this);
			ToLaserBladeRegistry.registerItemModels();
		}
	}

	public static class Items {
		public static final String NAME_ITEM_LASER_BLADE = "tolaserblade.laser_blade";
		public static Item itemLaserBlade = new ItemLaserBlade().setRegistryName(ToLaserBlade.Items.NAME_ITEM_LASER_BLADE).setUnlocalizedName(ToLaserBlade.Items.NAME_ITEM_LASER_BLADE);
	}

	public static class ToolMaterials {
		public static final ToolMaterial LASER = EnumHelper.addToolMaterial("LASER", 3, 32767, 12.0F, 10.0F, 22).setRepairItem(new ItemStack(net.minecraft.init.Items.REDSTONE));
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static ModelResourceLocation mrlItemLaserBlade = new ModelResourceLocation(MOD_ID + ":laser_blade", "inventory");

		public static ResourceLocation rlOBJItemLaserBlade = new ResourceLocation(MOD_ID + ":item/laser_blade.obj");
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		ToLaserBladeRegistry.registerBakedModels(event);
	}

}
