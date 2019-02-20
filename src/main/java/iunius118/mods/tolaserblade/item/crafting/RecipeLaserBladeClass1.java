package iunius118.mods.tolaserblade.item.crafting;

import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.ToLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.NBTTagCompound;
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
		ItemStack result = getRecipeOutput().copy();
		NBTTagCompound nbt = result.getTag();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);

			if (ItemLaserBlade.changeBladeColorByItem(nbt, stack)) {
				return result;
			}
		}

		return result;
	}

	// tolaserblade:laser_blade_class_1
	public static class Serializer extends ShapedRecipe.Serializer {
		private static final ResourceLocation NAME = new ResourceLocation("tolaserblade", "crafting_laser_blade_class_1");

		@Override
		public ShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
			ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json);

			ItemStack output = recipe.getRecipeOutput();
			NBTTagCompound nbt = ItemLaserBlade.setPerformanceClass1(output, ItemLaserBlade.colors[0]);
			nbt.setBoolean(ItemLaserBlade.KEY_IS_CRAFTING, true);

			return new RecipeLaserBladeClass1(recipe.getId(), recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), output);
		}

		@Override
		public ResourceLocation getName() {
			return NAME;
		}
	}
}
