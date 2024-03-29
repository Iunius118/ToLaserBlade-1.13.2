package com.github.iunius118.tolaserblade.item.crafting;

import com.github.iunius118.tolaserblade.ToLaserBlade;
import com.github.iunius118.tolaserblade.item.LaserBlade;
import com.google.gson.JsonObject;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

//Recipe Laser Blade - Class 1
public class RecipeLaserBladeClass1 extends ShapedRecipe {
    public RecipeLaserBladeClass1(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn) {
        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ToLaserBlade.CRAFTING_LASER_BLADE_CLASS_1;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        LaserBlade result = LaserBlade.create(getRecipeOutput().copy());

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (result.changeColorByItem(stack).getCost() > 0) {
                return result.getItemStack();
            }
        }

        return result.getItemStack();
    }

    // tolaserblade:crafting_laser_blade_class_1
    public static class Serializer extends ShapedRecipe.Serializer {
        private static final ResourceLocation NAME = new ResourceLocation("tolaserblade", "crafting_laser_blade_class_1");

        @Override
        public ShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json);

            ItemStack output = LaserBlade.create(recipe.getRecipeOutput())
                    .setAttack(LaserBlade.MOD_ATK_CLASS_1)
                    .enchant(Enchantments.SMITE, LaserBlade.LVL_SMITE_CLASS_1)
                    .setCraftingTag()
                    .getItemStack();

            return new RecipeLaserBladeClass1(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), output);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
