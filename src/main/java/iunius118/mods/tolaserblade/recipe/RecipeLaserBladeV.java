package iunius118.mods.tolaserblade.recipe;

import com.google.gson.JsonObject;

import iunius118.mods.tolaserblade.ToLaserBlade;
import iunius118.mods.tolaserblade.item.ItemLaserBlade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

//Recipe Laser Blade - Enchant V
public class RecipeLaserBladeV extends ShapedOreRecipe
{

    public RecipeLaserBladeV(ResourceLocation group, ItemStack result, ShapedPrimer primer)
    {
        super(group, result, primer);
    }

    // tolaserblade:laser_blade_v
    public static class Factory implements IRecipeFactory
    {

        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);
            // Enchant V
            ItemStack output = recipe.getRecipeOutput();
            output.addEnchantment(Enchantment.getEnchantmentByLocation("smite"), 5);
            NBTTagCompound nbt = output.getTagCompound();
            nbt.setFloat(ItemLaserBlade.KEY_ATK, ItemLaserBlade.MOD_ATK_V);
            nbt.setFloat(ItemLaserBlade.KEY_SPD, ItemLaserBlade.MOD_SPD_V);

            ShapedPrimer primer = new ShapedPrimer();
            primer.width = recipe.getWidth();
            primer.height = recipe.getHeight();
            primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
            primer.input = recipe.getIngredients();

            return new RecipeLaserBladeV(new ResourceLocation(ToLaserBlade.MOD_ID, "laser_blade_v"), output, primer);
        }

    }

}
