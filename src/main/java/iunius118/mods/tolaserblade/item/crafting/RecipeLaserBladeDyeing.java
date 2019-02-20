package iunius118.mods.tolaserblade.item.crafting;

import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

// Recipe Laser Blade - dyeing
public class RecipeLaserBladeDyeing extends ShapelessRecipe {
	public RecipeLaserBladeDyeing(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
		super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ToLaserBlade.CRAFTING_LASER_BLADE_DYEING;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		for (int i = 0; i < inv.getHeight(); ++i) {
			for (int j = 0; j < inv.getWidth(); ++j) {
				ItemStack itemstack = inv.getStackInSlot(j + i * inv.getWidth());

				if (itemstack.getItem() == ToLaserBlade.Items.laser_blade) {
					return itemstack.copy();
				}
			}
		}

		return getRecipeOutput().copy();
	}

	@Override
	public boolean isDynamic() {
		// Hide this recipe
		return true;
	}

	// tolaserblade:laser_blade_dyeing
	public static class Serializer extends ShapelessRecipe.Serializer {
		private static final ResourceLocation NAME = new ResourceLocation("tolaserblade", "crafting_laser_blade_dyeing");

		@Override
		public ShapelessRecipe read(ResourceLocation recipeId, JsonObject json) {
			ShapelessRecipe recipe = RecipeSerializers.CRAFTING_SHAPELESS.read(recipeId, json);
			return new RecipeLaserBladeDyeing(recipe.getId(), recipe.getGroup(), recipe.getRecipeOutput(), recipe.getIngredients());
		}

		@Override
		public ResourceLocation getName() {
			return NAME;
		}
	}
}
