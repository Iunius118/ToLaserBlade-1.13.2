package iunius118.mods.tolaserblade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import iunius118.mods.tolaserblade.client.renderer.ItemLaserBladeRenderer;
import iunius118.mods.tolaserblade.item.ItemLasarBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import iunius118.mods.tolaserblade.item.crafting.RecipeLaserBladeClass1;
import iunius118.mods.tolaserblade.item.crafting.RecipeLaserBladeClass2;
import iunius118.mods.tolaserblade.item.crafting.RecipeLaserBladeClass3;
import iunius118.mods.tolaserblade.item.crafting.RecipeLaserBladeDyeing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ObjectHolder;

@Mod(ToLaserBlade.MOD_ID)
public class ToLaserBlade {
	public static final String MOD_ID = "tolaserblade";
	public static final String MOD_NAME = "ToLaserBlade";
	public static final String MOD_UPDATE_JSON_URL = "https://raw.githubusercontent.com/Iunius118/ToLaserBlade/master/update.json";

	public static final Logger LOGGER = LogManager.getLogger();

	private static ToLaserBlade INSTANCE;

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

	public ToLaserBlade() {
		INSTANCE = this;

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::preInit);
		modEventBus.addListener(this::initServer);
		modEventBus.addListener(this::initClient);
		modEventBus.addListener(this::postInit);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(CommonEventHandler.INSTANCE);
	}

	public static ToLaserBlade getInstance() {
		return INSTANCE;
	}

	public void preInit(final FMLCommonSetupEvent event) {

	}

	private void initServer(final FMLDedicatedServerSetupEvent event) {

	}

	private void initClient(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);
		OBJLoader.INSTANCE.addDomain(MOD_ID);
	}

	public void postInit(InterModProcessEvent event) {

	}

	@ObjectHolder(MOD_ID)
	public static class Items {
		public static final Item lasar_blade = null;
		public static final Item laser_blade = null;
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

	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			if (FMLLoader.getDist().isClient()) {
				ClientEventHandler.INSTANCE.setTEISR();
			}

			event.getRegistry().registerAll(
					new ItemLasarBlade().setRegistryName(NAME_ITEM_LASAR_BLADE),
					new ItemLaserBlade().setRegistryName(NAME_ITEM_LASER_BLADE)
					);
		}
	}

	public static class CommonEventHandler {
		public static final CommonEventHandler INSTANCE = new CommonEventHandler();

		@SubscribeEvent
		public void onCrafting(ItemCraftedEvent event) {
			if (event.getPlayer().world.isRemote) {
				return;
			}

			ItemStack stackOut = event.getCrafting();

			if (stackOut.getItem() != Items.laser_blade) {
				return;
			}

			// Get Tags
			NBTTagCompound nbt = stackOut.getTag();

			if (nbt == null) {
				nbt = ItemLaserBlade.setColors(stackOut, 0xFFFFFFFF, ItemLaserBlade.colors[0], false, false);
			}

			if (nbt.hasKey(ItemLaserBlade.KEY_IS_CRAFTING)) {
				// Crafting on crafting table
				nbt.removeTag(ItemLaserBlade.KEY_IS_CRAFTING);
				return;
			}

			ItemLaserBlade.changeBladeColorByBiome(nbt, event.getPlayer());
		}

		@SubscribeEvent
		public void onAnvilRepair(AnvilRepairEvent event) {
			ItemStack left = event.getItemInput();

			if (left.getItem() == Items.laser_blade && !left.isEnchanted() && event.getIngredientInput().isEmpty()) {
				ItemStack output = event.getItemResult();
				String name = output.getDisplayName().getString();

				// Use GIFT code
				if ("GIFT".equals(name) || "おたから".equals(name)) {
					ItemLaserBlade.setPerformanceClass3(output, ItemLaserBlade.colors[1]);
					output.clearCustomName();
				}
			}
		}

		@SubscribeEvent
		public void onAnvilUpdate(AnvilUpdateEvent event) {
			ItemStack left = event.getLeft();
			ItemStack right = event.getRight();
			String name = event.getName();

			if (left.getItem() != Items.laser_blade) {
				return;
			}

			ItemStack output = left.copy();
			Item itemRight = right.getItem();

			// Upgrade to Class 4
			if (itemRight == net.minecraft.init.Items.NETHER_STAR) {
				if (ItemLaserBlade.upgradeClass4(output)) {
					ItemLaserBlade.changeDisplayNameOnAnvil(left, output, name);

					event.setCost(ItemLaserBlade.COST_LVL_CLASS_4);
					event.setMaterialCost(ItemLaserBlade.COST_ITEM_CLASS_4);
					event.setOutput(output);
				}

				return;
			}
			// Increase Attack point
			else if (ItemTags.getCollection().getOrCreate(new ResourceLocation(ToLaserBlade.MOD_ID + ":mob_head")).contains(itemRight)) {
				NBTTagCompound nbt = output.getTag();
				if (nbt != null) {
					// Only Class 4 blade
					float atk = nbt.getFloat(ItemLaserBlade.KEY_ATK);
					if (atk >= ItemLaserBlade.MOD_ATK_CLASS_4 && atk < 2041) {
						ItemLaserBlade.changeDisplayNameOnAnvil(left, output, name);

						if (ItemLaserBlade.getSkullOwnerName(right).equals("Iunius")) {
							// For debug
							nbt.setFloat(ItemLaserBlade.KEY_ATK, 2040.0f);
						} else {
							nbt.setFloat(ItemLaserBlade.KEY_ATK, atk + 1.0f);
						}

						event.setCost((int) atk / 100 + 10);
						event.setMaterialCost(1);
						event.setOutput(output);

						return;
					}
				}
			}
			// Change blade colors
			else if (ItemLaserBlade.changeBladeColorByItem(output.getTag(), right)) {
				ItemLaserBlade.changeDisplayNameOnAnvil(left, output, name);

				event.setCost(1);
				event.setMaterialCost(1);
				event.setOutput(output);

				return;
			}
		}
	}

	public static class ClientEventHandler {
		public static final ClientEventHandler INSTANCE = new ClientEventHandler();

		public void setTEISR() {
			ItemLaserBlade.properties = ItemLaserBlade.properties.setTEISR(() -> () -> new ItemLaserBladeRenderer());
		}

		// Model bakery
		@SubscribeEvent
		public void onModelBakeEvent(ModelBakeEvent event) {
			ModelLaserBlade modelLaserBlade = new ModelLaserBlade(bakeModel(RL_OBJ_ITEM_LASER_BLADE), event.getModelRegistry().get(MRL_ITEM_LASER_BLADE));
			event.getModelRegistry().put(MRL_ITEM_LASER_BLADE, modelLaserBlade);
		}

		@SubscribeEvent
		public void onItemColorHandlerEvent(ColorHandlerEvent.Item event) {
			event.getItemColors().register(new ItemLaserBlade.ColorHandler(), Items.laser_blade);
		}

		public IBakedModel bakeModel(ResourceLocation location) {
			try {
				IUnbakedModel model = ModelLoaderRegistry.getModel(location);
				// logger.info("Loaded obj model: " + model.hashCode());  // for debug
				IBakedModel bakedModel = model.bake(ModelLoader.defaultModelGetter(), ModelLoader.defaultTextureGetter(), model.getDefaultState(), false, DefaultVertexFormats.ITEM);
				return bakedModel;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@SubscribeEvent
		public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
			// Register texture for obj model
			event.getMap().registerSprite(Minecraft.getInstance().getResourceManager(), RL_TEXTURE_ITEM_LASER_BLADE);
		}
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
