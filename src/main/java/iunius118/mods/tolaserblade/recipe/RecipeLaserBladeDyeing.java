package iunius118.mods.tolaserblade.recipe;

import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.ToLaserBlade;
import iunius118.mods.tolaserblade.ToLaserBlade.ITEMS;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

// Recipe Laser Blade - dyeing
public class RecipeLaserBladeDyeing extends ShapelessOreRecipe
{

    public RecipeLaserBladeDyeing(ResourceLocation group, NonNullList<Ingredient> input, ItemStack result)
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

    @Override
    public boolean isHidden() {
        // Hide this recipe
        return true;
    }

    // shapeless_ore_laser_blade_dyeing
    public static class Factory implements IRecipeFactory
    {

        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);
            return new RecipeLaserBladeDyeing(new ResourceLocation(ToLaserBlade.MOD_ID, "laser_blade_dyeing"), recipe.getIngredients(), recipe.getRecipeOutput());
        }

    }

}
