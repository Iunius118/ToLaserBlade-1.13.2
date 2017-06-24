package iunius118.mods.tolaserblade;

import com.google.common.base.Function;
import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.client.model.ModelLaserBlade;
import iunius118.mods.tolaserblade.client.renderer.RenderItemLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import iunius118.mods.tolaserblade.tileentity.TileEntityRenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod(modid = ToLaserBlade.MOD_ID, name = ToLaserBlade.MOD_NAME, version = ToLaserBlade.MOD_VERSION, dependencies = ToLaserBlade.MOD_DEPENDENCIES, useMetadata = true)
@EventBusSubscriber
public class ToLaserBlade
{

    public static final String MOD_ID = "tolaserblade";
    public static final String MOD_NAME = "ToLaserBlade";
    public static final String MOD_VERSION = "%MOD_VERSION%";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12-14.21.0.2359,)";

    public static final String NAME_ITEM_LASER_BLADE = "tolaserblade.laser_blade";
    public static final ToolMaterial MATERIAL_LASER = EnumHelper.addToolMaterial("LASER", 3, 32767, 12.0F, 10.0F, 22)
            .setRepairItem(new ItemStack(net.minecraft.init.Items.REDSTONE));
    public static final ModelResourceLocation MRL_ITEM_LASER_BLADE = new ModelResourceLocation(MOD_ID + ":laser_blade", "inventory");
    public static final ResourceLocation RL_OBJ_ITEM_LASER_BLADE = new ResourceLocation(MOD_ID, "item/laser_blade.obj");

    @SidedProxy
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @EventHandler
    public void Init(FMLInitializationEvent event)
    {
        proxy.Init(event);
    }

    @ObjectHolder(MOD_ID)
    public static class ITEMS
    {
        @ObjectHolder(NAME_ITEM_LASER_BLADE)
        public static final Item itemLaserBlade = null;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(new ItemLaserBlade().setRegistryName(NAME_ITEM_LASER_BLADE).setUnlocalizedName(NAME_ITEM_LASER_BLADE));
    }

    // RecipeFactories by recipes/_factories.json
    // shaped_ore_enchant_smite_x
    public static class RecipeFactoryLaserBladeX implements IRecipeFactory
    {

        // Recipe Laser Blade - Smite X
        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ShapedOreRecipe recipe = RecipesLaserBladeX.factory(context, json);
            ShapedPrimer primer = new ShapedPrimer();
            primer.width = recipe.getWidth();
            primer.height = recipe.getHeight();
            primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
            primer.input = recipe.getIngredients();
            return new RecipesLaserBladeX(new ResourceLocation(MOD_ID, "laser_blade_x"), recipe.getRecipeOutput(), primer);
        }

        public class RecipesLaserBladeX extends ShapedOreRecipe
        {

            public RecipesLaserBladeX(ResourceLocation group, ItemStack result, ShapedPrimer primer)
            {
                super(group, result, primer);
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv)
            {
                ItemStack result = getRecipeOutput().copy();
                result.addEnchantment(Enchantment.getEnchantmentByLocation("smite"), 10);
                return result;
            }

        }

    }

    // shapeless_ore_laser_blade_dyeing
    public static class RecipeFactoryLaserBladeDyeing implements IRecipeFactory
    {

        // Recipe Laser Blade - dyeing
        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);
            return new RecipeLaserBladeDye(new ResourceLocation(MOD_ID, "laser_blade_dyeing"), recipe.getIngredients(), recipe.getRecipeOutput());
        }

        public class RecipeLaserBladeDye extends ShapelessOreRecipe
        {

            public RecipeLaserBladeDye(ResourceLocation group, NonNullList<Ingredient> input, ItemStack result)
            {
                super(group, input, result);
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv)
            {
                for (int i = 0; i < inv.getHeight(); ++i)
                {
                    for (int j = 0; j < inv.getWidth(); ++j)
                    {
                        ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                        if (itemstack.getItem() == ITEMS.itemLaserBlade)
                        {
                            return itemstack.copy();
                        }
                    }
                }

                return getRecipeOutput().copy();
            }

        }

    }

    // Proxy Classes

    public static class CommonProxy
    {

        public void preInit(FMLPreInitializationEvent event)
        {

        }

        public void Init(FMLInitializationEvent event)
        {
            // register item event for LaserBlade dyeing
            MinecraftForge.EVENT_BUS.register(ITEMS.itemLaserBlade);
        }

    }

    @SideOnly(Side.SERVER)
    public static class ServerProxy extends CommonProxy
    {

    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends CommonProxy
    {

        @Override
        public void preInit(FMLPreInitializationEvent event)
        {
            super.preInit(event);

            OBJLoader.INSTANCE.addDomain(MOD_ID);
            MinecraftForge.EVENT_BUS.register(this); // register onModelBakeEvent
        }

        // Model registry and bakery
        @SubscribeEvent
        public void registerModels(ModelRegistryEvent event)
        {
            ModelLoader.setCustomModelResourceLocation(ITEMS.itemLaserBlade, 0, MRL_ITEM_LASER_BLADE);

            ForgeHooksClient.registerTESRItemStack(ITEMS.itemLaserBlade, 0, TileEntityRenderItem.class);
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRenderItem.class, new RenderItemLaserBlade());
        }

        @SubscribeEvent
        public void onModelBakeEvent(ModelBakeEvent event)
        {
            ModelLaserBlade modelLaserBlade = new ModelLaserBlade(bakeModel(RL_OBJ_ITEM_LASER_BLADE),
                    event.getModelRegistry().getObject(MRL_ITEM_LASER_BLADE));

            event.getModelRegistry().putObject(MRL_ITEM_LASER_BLADE, modelLaserBlade);
        }

        public IBakedModel bakeModel(ResourceLocation location)
        {
            Function<ResourceLocation, TextureAtlasSprite> spriteGetter = resource -> Minecraft.getMinecraft().getTextureMapBlocks()
                    .getAtlasSprite(resource.toString());

            try
            {
                IModel model = ModelLoaderRegistry.getModel(location);
                IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, spriteGetter);
                return bakedModel;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

    }

}
