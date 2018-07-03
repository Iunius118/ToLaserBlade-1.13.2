package iunius118.mods.tolaserblade.item;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Multimap;

import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.biome.BiomeVoid;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemLaserBlade extends ItemSword
{

    private final Item.ToolMaterial material;
    private final float attackDamage;
    private final float attackSpeed;
    // Blade color table
    public final static int[] colors = { 0xFFFF0000, 0xFFD0A000, 0xFF00E000, 0xFF0080FF, 0xFF0000FF, 0xFFA000FF, 0xFFFFFFFF, 0xFF020202, 0xFFA00080 };

    public final Enchantment enchSmite;
    public final Enchantment enchSweeping;

    public static final String KEY_ATK = "ATK";
    public static final String KEY_SPD = "SPD";
    public static final String KEY_COLOR_CORE = "colorC";
    public static final String KEY_COLOR_HALO = "colorH";
    public static final String KEY_IS_SUB_COLOR_CORE = "isSubC";
    public static final String KEY_IS_SUB_COLOR_HALO = "isSubH";
    public static final String KEY_IS_CRAFTING = "isCrafting";

    public static final float MOD_SPD_CLASS_3 = 1.2F;
    public static final float MOD_ATK_CLASS_1 = -1.0F;
    public static final float MOD_ATK_CLASS_3 = 3.0F;
    public static final float MOD_ATK_CLASS_4 = 7.0F;

    public static final int LVL_SMITE_CLASS_3 = 5;
    public static final int LVL_SMITE_CLASS_4 = 10;
    public static final int LVL_SWEEPING_CLASS_4 = 3;

    public static final int COST_LVL_CLASS_4 = 20;
    public static final int COST_ITEM_CLASS_4 = 1;

    public ItemLaserBlade()
    {
        super(ToLaserBlade.MATERIAL_LASER);

        setCreativeTab(CreativeTabs.TOOLS);
        setNoRepair();
        material = ToLaserBlade.MATERIAL_LASER;
        attackDamage = 3.0F + material.getAttackDamage();
        attackSpeed = -1.2F;

        enchSmite = Enchantment.getEnchantmentByLocation("smite");
        enchSweeping = Enchantment.getEnchantmentByLocation("sweeping");
    }

    public static NBTTagCompound setPerformance(ItemStack stack, float modSpeed, float modAttack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }

        nbt.setFloat(KEY_SPD, modSpeed);
        nbt.setFloat(KEY_ATK, modAttack);

        return nbt;
    }

    public static NBTTagCompound setColors(ItemStack stack, int colorCore, int colorHalo, boolean isSubColorCore, boolean isSubColorHalo)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }

        nbt.setInteger(KEY_COLOR_CORE, colorCore);
        nbt.setInteger(KEY_COLOR_HALO, colorHalo);
        nbt.setBoolean(KEY_IS_SUB_COLOR_CORE, isSubColorCore);
        nbt.setBoolean(KEY_IS_SUB_COLOR_HALO, isSubColorHalo);

        return nbt;
    }

    public static boolean checkColors(ItemStack stack, int colorCore, int colorHalo, boolean isSubColorCore, boolean isSubColorHalo)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }

        if (!nbt.hasKey(KEY_COLOR_CORE, NBT.TAG_INT))
        {
            nbt.setInteger(KEY_COLOR_CORE, 0xFFFFFFFF);
        }

        if (colorCore != nbt.getInteger(KEY_COLOR_CORE))
        {
            return false;
        }

        if (!nbt.hasKey(KEY_COLOR_HALO, NBT.TAG_INT))
        {
            nbt.setInteger(KEY_COLOR_HALO, colors[0]);
        }

        if (colorHalo != nbt.getInteger(KEY_COLOR_HALO))
        {
            return false;
        }

        if (!nbt.hasKey(KEY_IS_SUB_COLOR_CORE))
        {
            nbt.setBoolean(KEY_IS_SUB_COLOR_CORE, false);
        }

        if (isSubColorCore != nbt.getBoolean(KEY_IS_SUB_COLOR_CORE))
        {
            return false;
        }

        if (!nbt.hasKey(KEY_IS_SUB_COLOR_HALO))
        {
            nbt.setBoolean(KEY_IS_SUB_COLOR_HALO, false);
        }

        if (isSubColorHalo != nbt.getBoolean(KEY_IS_SUB_COLOR_HALO))
        {
            return false;
        }

        return true;
    }

    public static NBTTagCompound setPerformanceClass1(ItemStack stack, int colorHalo)
    {
        setPerformance(stack, 0, MOD_ATK_CLASS_1);
        return setColors(stack, 0xFFFFFFFF, colorHalo, false, false);
    }

    public static NBTTagCompound setPerformanceClass2(ItemStack stack)
    {
        setPerformance(stack, 0, 0);
        return setColors(stack, 0xFFFFFFFF, colors[0], false, false);
    }

    public static NBTTagCompound setPerformanceClass3(ItemStack stack, int colorHalo)
    {
        stack.addEnchantment(Enchantment.getEnchantmentByLocation("smite"), 5);
        NBTTagCompound nbt = setPerformance(stack, MOD_SPD_CLASS_3, MOD_ATK_CLASS_3);

        if (checkColors(stack, 0xFFFFFFFF, colors[0], false, false))
        {
            return setColors(stack, 0xFFFFFFFF, colorHalo, false, false);
        }

        return nbt;
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event)
    {
        if (event.player.world.isRemote)
        {
            return;
        }

        ItemStack stackOut = event.crafting;

        if (stackOut.getItem() != this)
        {
            return;
        }

        // Get NBT
        NBTTagCompound nbt = stackOut.getTagCompound();

        if (nbt == null)
        {
            nbt = setColors(stackOut, 0xFFFFFFFF, colors[0], false, false);
        }

        if (nbt.hasKey(KEY_IS_CRAFTING))
        {
            // Crafting on crafting table
            nbt.removeTag(KEY_IS_CRAFTING);
            return;
        }

        changeBladeColorByBiome(nbt, event.player);
    }

    public static boolean changeBladeColorByItem(NBTTagCompound nbt, ItemStack stack)
    {
        if (nbt == null)
        {
            return false;
        }

        Item item = stack.getItem();

        if (item instanceof ItemDye)
        {
            int color = EnumDyeColor.byDyeDamage(stack.getItemDamage()).getColorValue() | 0xFF000000;

            if (nbt.hasKey(KEY_COLOR_CORE, NBT.TAG_INT) && nbt.getInteger(KEY_COLOR_CORE) == color)
            {
                return false;
            }

            nbt.setInteger(KEY_COLOR_CORE, color);

            return true;
        }
        else if (item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof BlockStainedGlass)
        {
            int color = EnumDyeColor.byMetadata(stack.getMetadata()).getColorValue() | 0xFF000000;

            if (nbt.hasKey(KEY_COLOR_HALO, NBT.TAG_INT) && nbt.getInteger(KEY_COLOR_HALO) == color)
            {
                return false;
            }

            nbt.setInteger(KEY_COLOR_HALO, color);

            return true;
        }

        return false;
    }

    public static void changeBladeColorByBiome(NBTTagCompound nbt, EntityPlayer player)
    {
        World world = player.world;
        Biome biome = world.getBiomeForCoordsBody(player.getPosition());

        // Dyeing by Biome type or Biome temperature
        if (world.provider.getDimension() == -1 || biome instanceof BiomeHell)
        {
            // Nether
            boolean isSubColorCore = nbt.getBoolean(KEY_IS_SUB_COLOR_CORE);
            nbt.setBoolean(KEY_IS_SUB_COLOR_CORE, !isSubColorCore);
        }
        else if (world.provider.getDimension() == 1 || biome instanceof BiomeEnd)
        {
            // End
            boolean isSubColorHalo = nbt.getBoolean(KEY_IS_SUB_COLOR_HALO);
            nbt.setBoolean(KEY_IS_SUB_COLOR_HALO, !isSubColorHalo);
        }
        else if (biome instanceof BiomeVoid)
        {
            // Void
            nbt.setInteger(KEY_COLOR_CORE, colors[7]);
            nbt.setInteger(KEY_COLOR_HALO, colors[7]);
        }
        else
        {
            // Biomes on Overworld or the other dimensions
            float temp = biome.getDefaultTemperature();

            if (temp > 1.5F)
            {
                // t > 1.5
                nbt.setInteger(KEY_COLOR_HALO, colors[5]);
            }
            else if (temp > 1.0F)
            {
                // 1.5 >= t > 1.0
                nbt.setInteger(KEY_COLOR_HALO, colors[8]);
            }
            else if (temp > 0.8F)
            {
                // 1.0 >= t > 0.8
                nbt.setInteger(KEY_COLOR_HALO, colors[1]);
            }
            else if (temp >= 0.5F)
            {
                // 0.8 >= t >= 0.5
                nbt.setInteger(KEY_COLOR_HALO, colors[0]);
            }
            else if (temp >= 0.2F)
            {
                // 0.5 > t >= 0.2
                nbt.setInteger(KEY_COLOR_HALO, colors[2]);
            }
            else if (temp >= -0.25F)
            {
                // 0.2 > t >= -0.25
                nbt.setInteger(KEY_COLOR_HALO, colors[3]);
            }
            else
            {
                // -0.25 > t
                nbt.setInteger(KEY_COLOR_HALO, colors[4]);
            }
        }
    }

    @SubscribeEvent
    public void onAnvilRepair(AnvilRepairEvent event)
    {
        ItemStack left = event.getItemInput();

        if (left.getItem() == this && !left.isItemEnchanted() && event.getIngredientInput().isEmpty())
        {
            ItemStack output = event.getItemResult();
            String name = output.getDisplayName();

            // GIFT code
            if ("GIFT".equals(name) || "おたから".equals(name))
            {
                setPerformanceClass3(output, colors[1]);
                output.clearCustomName();
            }
        }
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        String name = event.getName();

        if (left.getItem() != this)
        {
            return;
        }

        ItemStack output = left.copy();

        // Upgrade to Class 4
        if (right.getItem() == Items.NETHER_STAR)
        {
            if (upgradeClass4(output))
            {
                if (StringUtils.isBlank(name))
                {
                    if (left.hasDisplayName())
                    {
                        output.clearCustomName();
                    }
                }
                else
                {
                    if (!name.equals(left.getDisplayName()))
                    {
                        output.setStackDisplayName(name);
                    }
                }

                event.setCost(COST_LVL_CLASS_4);
                event.setMaterialCost(COST_ITEM_CLASS_4);
                event.setOutput(output);
            }

            return;
        }
        // Change blade colors
        else if (changeBladeColorByItem(output.getTagCompound(), right))
        {
            if (StringUtils.isBlank(name))
            {
                if (left.hasDisplayName())
                {
                    output.clearCustomName();
                }
            }
            else
            {
                if (!name.equals(left.getDisplayName()))
                {
                    output.setStackDisplayName(name);
                }
            }

            event.setCost(1);
            event.setMaterialCost(1);
            event.setOutput(output);

            return;
        }
    }

    public boolean upgradeClass4(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            // Upgrade all to Class 4
            stack.addEnchantment(enchSmite, LVL_SMITE_CLASS_4);
            stack.addEnchantment(enchSweeping, LVL_SWEEPING_CLASS_4);
            nbt = stack.getTagCompound();
            nbt.setFloat(KEY_ATK, MOD_ATK_CLASS_4);
            nbt.setFloat(KEY_SPD, MOD_SPD_CLASS_3);

            return true;
        }

        boolean isAtkX = false;
        boolean isSpdX = false;
        boolean isSmiteX = false;
        boolean isSweepingX = false;

        // Upgrade Attack to Class 4
        if (nbt.getFloat(KEY_ATK) < MOD_ATK_CLASS_4)
        {
            nbt.setFloat(KEY_ATK, MOD_ATK_CLASS_4);
        }
        else
        {
            isAtkX = true;
        }

        // Upgrade Speed to Class 4
        if (nbt.getFloat(KEY_SPD) < MOD_SPD_CLASS_3)
        {
            nbt.setFloat(KEY_SPD, MOD_SPD_CLASS_3);
        }
        else
        {
            isSpdX = true;
        }

        // Upgrade Enchantment to Class 4
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        Integer lvlSmite = map.get(enchSmite);
        Integer lvlSweeping = map.get(enchSweeping);

        // Upgrade Smite to Class 4
        if (lvlSmite == null)
        {
            Map<Enchantment, Integer> mapNew = new HashMap();

            for (Map.Entry<Enchantment, Integer>entry : map.entrySet())
            {
                Enchantment key = entry.getKey();

                if (key.isCompatibleWith(enchSmite))
                {
                    mapNew.put(key, entry.getValue());
                }
            }

            map = mapNew;
            map.put(enchSmite, LVL_SMITE_CLASS_4);
        }
        else if (lvlSmite < LVL_SMITE_CLASS_4)
        {
            map.put(enchSmite, LVL_SMITE_CLASS_4);
        }
        else
        {
            isSmiteX = true;
        }

        // Upgrade Sweeping to Class 4
        if (lvlSweeping == null || lvlSweeping < LVL_SWEEPING_CLASS_4)
        {
            map.put(enchSweeping, LVL_SWEEPING_CLASS_4);
        }
        else
        {
            isSweepingX = true;
        }

        if (isAtkX && isSpdX && isSmiteX && isSweepingX)
        {
            return false; // Already Class 4
        }

        EnchantmentHelper.setEnchantments(map, stack);

        return true;
    }

    @Override
    public float getAttackDamage()
    {
        return attackDamage;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return material.getEfficiency();
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return true;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState)
    {
        return material.getHarvestLevel();
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            float modDamage = 0;
            float modSpeed = 0;

            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt != null)
            {
                // Fix attack speed for old version
                if (!nbt.hasKey(KEY_SPD))
                {
                    if (EnchantmentHelper.getEnchantmentLevel(enchSmite, stack) >= LVL_SMITE_CLASS_4)
                    {
                        nbt.setFloat(KEY_SPD, MOD_SPD_CLASS_3);
                    }
                    else
                    {
                        nbt.setFloat(KEY_SPD, 0);
                    }

                    checkColors(stack, 0xFFFFFFFF, colors[0], false, false);
                }

                // Get attack modifiers from NBT
                modDamage = nbt.getFloat(KEY_ATK);
                modSpeed = nbt.getFloat(KEY_SPD);
            } else {
                setPerformanceClass2(stack);
            }

            multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage + modDamage, 0));
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed + modSpeed, 0));
        }

        return multimap;
    }

    public static class ColorHandler implements IItemColor
    {

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex)
        {

            switch (tintIndex)
            {
            case 1:
                return getColorFronNBT(stack, KEY_COLOR_HALO, KEY_IS_SUB_COLOR_HALO, colors[0]);

            case 2:
                return getColorFronNBT(stack, KEY_COLOR_CORE, KEY_IS_SUB_COLOR_CORE, 0xFFFFFFFF);

            default:
                return 0xFFFFFFFF;
            }
        }

        public int getColorFronNBT(ItemStack stack, String keyColor, String keyIsSubColor, int defaultColor)
        {
            NBTTagCompound nbt = stack.getTagCompound();

            if (nbt != null && nbt.hasKey(keyColor, NBT.TAG_INT))
            {
                int color = nbt.getInteger(keyColor);

                if (nbt.getBoolean(keyIsSubColor))
                {
                    color = ~color | 0xFF000000;
                }

                return color;
            }

            return defaultColor;
        }

    }

}
