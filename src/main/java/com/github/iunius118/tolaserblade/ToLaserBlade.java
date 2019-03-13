package com.github.iunius118.tolaserblade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.iunius118.tolaserblade.client.ClientEventHandler;
import com.github.iunius118.tolaserblade.item.ItemEventHandler;
import com.github.iunius118.tolaserblade.item.ItemLasarBlade;
import com.github.iunius118.tolaserblade.item.ItemLaserBlade;
import com.github.iunius118.tolaserblade.item.crafting.RecipeLaserBladeClass1;
import com.github.iunius118.tolaserblade.item.crafting.RecipeLaserBladeClass2;
import com.github.iunius118.tolaserblade.item.crafting.RecipeLaserBladeClass3;
import com.github.iunius118.tolaserblade.item.crafting.RecipeLaserBladeDyeing;
import com.github.iunius118.tolaserblade.network.NetworkHandler;
import com.github.iunius118.tolaserblade.network.ServerConfigMessage;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.ObjectHolder;

@Mod(ToLaserBlade.MOD_ID)
public class ToLaserBlade {
	public static final String MOD_ID = "tolaserblade";
	public static final String MOD_NAME = "ToLaserBlade";
	public static final String MOD_UPDATE_JSON_URL = "https://raw.githubusercontent.com/Iunius118/ToLaserBlade/master/update.json";

	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static final String NAME_ITEM_LASAR_BLADE = "lasar_blade";
	public static final ModelResourceLocation MRL_ITEM_LASAR_BLADE = new ModelResourceLocation(MOD_ID + ":" + NAME_ITEM_LASAR_BLADE, "inventory");

	public static final String NAME_ITEM_LASER_BLADE = "laser_blade";
	public static final ModelResourceLocation MRL_ITEM_LASER_BLADE = new ModelResourceLocation(MOD_ID + ":" + NAME_ITEM_LASER_BLADE, "inventory");
	public static final ResourceLocation RL_OBJ_ITEM_LASER_BLADE = new ResourceLocation(MOD_ID, "item/laser_blade.obj");
	public static final ResourceLocation RL_TEXTURE_ITEM_LASER_BLADE = new ResourceLocation(MOD_ID, "item/laser_blade");

	// public static boolean hasShownUpdate = false;

	// Recipe Serializers
	public static final IRecipeSerializer<ShapelessRecipe> CRAFTING_LASER_BLADE_DYEING = RecipeSerializers.register(new RecipeLaserBladeDyeing.Serializer());
	public static final IRecipeSerializer<ShapedRecipe> CRAFTING_LASER_BLADE_CLASS_1 = RecipeSerializers.register(new RecipeLaserBladeClass1.Serializer());
	public static final IRecipeSerializer<ShapedRecipe> CRAFTING_LASER_BLADE_CLASS_2 = RecipeSerializers.register(new RecipeLaserBladeClass2.Serializer());
	public static final IRecipeSerializer<ShapedRecipe> CRAFTING_LASER_BLADE_CLASS_3 = RecipeSerializers.register(new RecipeLaserBladeClass3.Serializer());

	// Init network channels
	public static final NetworkHandler NETWORK_HANDLER = new NetworkHandler();

	public ToLaserBlade() {
		// Register lifecycle events
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::preInit);
		modEventBus.addListener(this::initServer);
		modEventBus.addListener(this::initClient);
		modEventBus.addListener(this::postInit);

		// Register config
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ToLaserBladeConfig.commonSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ToLaserBladeConfig.clientSpec);

		// Register events
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
	}


	public void preInit(final FMLCommonSetupEvent event) {

	}

	private void initServer(final FMLDedicatedServerSetupEvent event) {

	}

	private void initClient(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		OBJLoader.INSTANCE.addDomain(MOD_ID);
	}

	public void postInit(InterModProcessEvent event) {

	}

	@ObjectHolder(MOD_ID)
	public static class Items {
		public static final Item lasar_blade = null;
		public static final Item laser_blade = null;
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			if (FMLLoader.getDist().isClient()) {
				ClientEventHandler.setTEISR();
			}

			event.getRegistry().registerAll(
					new ItemLasarBlade().setRegistryName(NAME_ITEM_LASAR_BLADE),
					new ItemLaserBlade().setRegistryName(NAME_ITEM_LASER_BLADE)
					);
		}
	}

	@SubscribeEvent
	public static void remapItems(RegistryEvent.MissingMappings<Item> mappings) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : mappings.getAllMappings()) {
			if (!mapping.key.getNamespace().equals(MOD_ID)) {
				continue;
			}

			String name = mapping.key.getPath();
			if (name.equals(MOD_ID + "." + NAME_ITEM_LASER_BLADE)) {
				// Replace item ID "tolaserblade:tolaserblade.laser_blade" (-1.11.2) with "tolaserblade:laser_blade" (1.12-)
				mapping.remap(ToLaserBlade.Items.laser_blade);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn (PlayerLoggedInEvent event) {
		NETWORK_HANDLER.getConfigChannel().sendTo(
				new ServerConfigMessage(ToLaserBladeConfig.COMMON.isEnabledBlockingWithLaserBlade.get()),
				((EntityPlayerMP) event.getPlayer()).connection.getNetworkManager(),
				NetworkDirection.PLAY_TO_CLIENT);
	}

	/*

	// For damage test
	/*
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event)
	{
	float dmg = CombatRules.getDamageAfterAbsorb(event.getAmount(), (float)event.getEntityLiving().getTotalArmorValue(), (float)event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
	String str = event.getSource().getDamageType() + " caused " + dmg + " point damage to " + event.getEntityLiving().getName() + "!";
	Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.SYSTEM, new TextComponentString(str));
	}
	// */

	/*
		@SubscribeEvent
		public void onConnectedToServer(ClientConnectedToServerEvent event) {
			if (!hasShownUpdate) {
				// Check update and Notify client
				CheckResult result = ForgeVersion.getResult(Loader.instance().activeModContainer());
				Status status = result.status;

				if (status == Status.PENDING) {
					// Failed to get update information
					return;
				}

				if (status == Status.OUTDATED || status == Status.BETA_OUTDATED) {
					ITextComponent modNameHighlighted = new TextComponentString(MOD_NAME);
					modNameHighlighted.getStyle().setColor(TextFormatting.YELLOW);

					ITextComponent newVersionHighlighted = new TextComponentString(result.target.toString());
					newVersionHighlighted.getStyle().setColor(TextFormatting.YELLOW);

					ITextComponent message = new TextComponentTranslation("tolaserblade.update.newversion", modNameHighlighted).appendText(": ")
							.appendSibling(newVersionHighlighted);
					message.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, result.url));

					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);

					hasShownUpdate = true;
				}

			}
		}

	}

	// */

}
