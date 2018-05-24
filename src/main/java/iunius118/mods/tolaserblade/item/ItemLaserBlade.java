package iunius118.mods.tolaserblade.item;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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
    public final static int[] colors = { 0xFFFF0000, 0xFFD0A000, 0xFF00E000, 0xFF0080FF, 0xFF0000FF, 0xFFA000FF, 0xFFFFFFFF, 0xFF020202 };

    public final Enchantment enchSmite;
    public final Enchantment enchSweeping;

    public static final String KEY_ATK = "ATK";
    public static final String KEY_SPD = "SPD";
    public static final String KEY_COLOR_CORE = "colorC";
    public static final String KEY_COLOR_HALO = "colorH";
    public static final String KEY_IS_SUB_COLOR = "isSubC";

    public static final float MOD_SPD_V = 1.2F;
    public static final float MOD_ATK_O = -1.0F;
    public static final float MOD_ATK_V = 3.0F;
    public static final float MOD_ATK_X = 7.0F;

    public static final int LVL_SMITE_V = 5;
    public static final int LVL_SMITE_X = 10;
    public static final int LVL_SWEEPING_X = 3;

    public static final int COST_LVL_X = 20;
    public static final int COST_ITEM_X = 1;

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

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event)
    {
        if (event.player.world.isRemote)
        {
            return;
        }

        ItemStack stack = event.crafting;

        if (stack.getItem() != this)
        {
            return;
        }

        // Get NBT
        NBTTagCompound nbt = stack.getTagCompound();

        BlockPos pos = event.player.getPosition();
        Biome biome = event.player.world.getBiomeForCoordsBody(pos);
        int colorCore = 0xFFFFFFFF;
        int colorHalo = colors[0];
        boolean isSubColor = false;

        boolean canTreasureCodeDyeing = false;
        boolean canBiomeDyeing = false;

        if (nbt == null)
        {
            // Got from Creative Inventory
            nbt = new NBTTagCompound();
            nbt.setInteger(ItemLaserBlade.KEY_COLOR_HALO, ItemLaserBlade.colors[0]);
            stack.setTagCompound(nbt);
        }

        if (!nbt.hasKey(KEY_COLOR_HALO, NBT.TAG_INT))
        {
            // Not dye (created from materials)
        }
        // Dyeing by Biome type or Biome temperature
        else if (biome instanceof BiomeHell)
        {
            colorHalo = colors[6];
        }
        else if (biome instanceof BiomeEnd)
        {
            colorHalo = colors[6];
            isSubColor = true;
        }
        else if (biome instanceof BiomeVoid)
        {
            colorCore = colors[7];
            colorHalo = colors[7];
        }
        else
        {
            float temp = biome.getTemperature(pos);
            // System.out.println(biome.toString() + " " + temp); // For debugging

            if (0.95 >= temp && temp >= 0.85   )
            {
                colorHalo = colors[1];
            }
            else if (0.4 > temp && temp >= 0.1)
            {
                colorHalo = colors[2];
            }
            else if (0.1 > temp && temp >= -0.1)
            {
                colorHalo = colors[3];
            }
            else if (-0.1 > temp)
            {
                colorHalo = colors[4];
            }
            else if (temp > 0.95)
            {
                colorHalo = colors[5];
            }
        }

        // Set NBT
        nbt.setInteger(KEY_COLOR_CORE, colorCore);
        nbt.setInteger(KEY_COLOR_HALO, colorHalo);
        nbt.setBoolean(KEY_IS_SUB_COLOR, isSubColor);
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
                output.addEnchantment(enchSmite, LVL_SMITE_V);
                NBTTagCompound nbt = output.getTagCompound();
                nbt.setFloat(KEY_ATK, MOD_ATK_V);
                nbt.setFloat(KEY_SPD, MOD_SPD_V);
                nbt.setInteger(KEY_COLOR_CORE, 0xFFFFFFFF);
                nbt.setInteger(KEY_COLOR_HALO, colors[1]);
                nbt.setBoolean(KEY_IS_SUB_COLOR, false);
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

        // Upgrade to X
        if (left.getItem() == this && right.getItem() == Items.NETHER_STAR)
        {
            ItemStack output = left.copy();
            NBTTagCompound nbt = output.getTagCompound();

            if (nbt == null)
            {
                // Upgrade all to X
                output.addEnchantment(enchSmite, LVL_SMITE_X);
                output.addEnchantment(enchSweeping, LVL_SWEEPING_X);
                nbt = output.getTagCompound();
                nbt.setFloat(KEY_ATK, MOD_ATK_X);
                nbt.setFloat(KEY_SPD, MOD_SPD_V);

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

                event.setCost(COST_LVL_X);
                event.setMaterialCost(COST_ITEM_X);
                event.setOutput(output);
                return;
            }

            boolean isAtkX = false;
            boolean isSpdX = false;
            boolean isSmiteX = false;
            boolean isSweepingX = false;

            // Upgrade Attack to X
            if (nbt.getFloat(KEY_ATK) < MOD_ATK_X)
            {
                nbt.setFloat(KEY_ATK, MOD_ATK_X);
            }
            else
            {
                isAtkX = true;
            }

            // Upgrade Speed to X
            if (nbt.getFloat(KEY_SPD) < MOD_SPD_V)
            {
                nbt.setFloat(KEY_SPD, MOD_SPD_V);
            }
            else
            {
                isSpdX = true;
            }

            // Upgrade Enchantment to X
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(left);
            Integer lvlSmite = map.get(enchSmite);
            Integer lvlSweeping = map.get(enchSweeping);

            // Upgrade Smite to X
            if (lvlSmite == null)
            {
                Map<Enchantment, Integer> mapNew = Maps.<Enchantment, Integer>newLinkedHashMap();

                for (Map.Entry<Enchantment, Integer>entry : map.entrySet())
                {
                    Enchantment key = entry.getKey();

                    if (key.isCompatibleWith(enchSmite))
                    {
                        mapNew.put(key, entry.getValue());
                    }
                }

                map = mapNew;
                map.put(enchSmite, LVL_SMITE_X);
            }
            else if (lvlSmite < LVL_SMITE_X)
            {
                map.put(enchSmite, LVL_SMITE_X);
            }
            else
            {
                isSmiteX = true;
            }

            // Upgrade Sweeping to X
            if (lvlSweeping == null || lvlSweeping < LVL_SWEEPING_X)
            {
                map.put(enchSweeping, LVL_SWEEPING_X);
            }
            else
            {
                isSweepingX = true;
            }

            if (isAtkX && isSpdX && isSmiteX && isSweepingX)
            {
                return; // Already X
            }

            EnchantmentHelper.setEnchantments(map, output);

            // Rename
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

            event.setCost(COST_LVL_X);
            event.setMaterialCost(COST_ITEM_X);
            event.setOutput(output);
            return;
        }
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
                    if (EnchantmentHelper.getEnchantmentLevel(enchSmite, stack) >= LVL_SMITE_X)
                    {
                        nbt.setFloat(KEY_SPD, MOD_SPD_V);
                    }
                    else
                    {
                        nbt.setFloat(KEY_SPD, 0);
                    }
                }

                // Get attack mods from NBT
                modDamage = nbt.getFloat(KEY_ATK);
                modSpeed = nbt.getFloat(KEY_SPD);
            }

            multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage + modDamage, 0));
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed + modSpeed, 0));
        }

        return multimap;
    }

}
