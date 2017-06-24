package iunius118.mods.tolaserblade.recipe;

import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

//Recipe Laser Blade - Smite X
public class RecipeLaserBladeX extends ShapedOreRecipe
{

    public RecipeLaserBladeX(ResourceLocation group, ItemStack result, ShapedPrimer primer)
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

    // shaped_ore_enchant_smite_x
    public static class Factory implements IRecipeFactory
    {

        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);
            ShapedPrimer primer = new ShapedPrimer();
            primer.width = recipe.getWidth();
            primer.height = recipe.getHeight();
            primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
            primer.input = recipe.getIngredients();
            return new RecipeLaserBladeX(new ResourceLocation(ToLaserBlade.MOD_ID, "laser_blade_x"), recipe.getRecipeOutput(), primer);
        }

    }

}
