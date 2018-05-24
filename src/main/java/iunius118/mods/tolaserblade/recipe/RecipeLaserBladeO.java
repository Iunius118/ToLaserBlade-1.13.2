package iunius118.mods.tolaserblade.recipe;

import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.ToLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

//Recipe Laser Blade - Enchant O
public class RecipeLaserBladeO extends ShapedOreRecipe
{

    public RecipeLaserBladeO(ResourceLocation group, ItemStack result, ShapedPrimer primer)
    {
        super(group, result, primer);
    }

    // tolaserblade:laser_blade_o
    public static class Factory implements IRecipeFactory
    {

        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);
            // Set Attack O
            ItemStack output = recipe.getRecipeOutput();
            NBTTagCompound nbt = output.getTagCompound();

            if (nbt == null)
            {
                nbt = new NBTTagCompound();
                output.setTagCompound(nbt);
            }

            nbt.setFloat(ItemLaserBlade.KEY_ATK, ItemLaserBlade.MOD_ATK_O);

            ShapedPrimer primer = new ShapedPrimer();
            primer.width = recipe.getWidth();
            primer.height = recipe.getHeight();
            primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
            primer.input = recipe.getIngredients();

            return new RecipeLaserBladeO(new ResourceLocation(ToLaserBlade.MOD_ID, "laser_blade_o"), output, primer);
        }

    }

}
