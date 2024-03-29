package com.github.iunius118.tolaserblade.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLasarBlade extends ItemSword {

    private final IItemTier tier = new ItemLasarBlade.ItemTier();
    private final float attackDamage;
    private final float attackSpeed;

    public ItemLasarBlade() {
        super(new ItemLasarBlade.ItemTier(), 3, -1.2F, (new Item.Properties()).group(ItemGroup.TOOLS));

        attackDamage = 3.0F + tier.getAttackDamage();
        attackSpeed = -1.2F;
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        EntityPlayer player = context.getPlayer();
        ItemStack itemstack = context.getItem();
        BlockPos pos = context.getPos();
        EnumFacing facing = context.getFace();

        if (!(itemstack.getItem() instanceof ItemLasarBlade)) {
            return EnumActionResult.PASS;
        }

        IBlockState blockstate = world.getBlockState(pos);
        Block block = blockstate.getBlock();
        int costDamage = tier.getMaxUses() / 2 + 1;

        // Redstone Torch -> Repairing/Collecting

        if ((block == Blocks.REDSTONE_TORCH || block == Blocks.REDSTONE_WALL_TORCH) && player.canPlayerEdit(pos, facing, itemstack)) {
            int itemDamage = itemstack.getDamage();
            if (itemDamage >= costDamage || player.abilities.isCreativeMode) {
                // Repair this
                itemstack.setDamage(itemDamage - costDamage);
            } else {
                // Collect a Redstone Torch
                if (!player.inventory.addItemStackToInventory(new ItemStack(Blocks.REDSTONE_TORCH))) {
                    // Cannot collect because player's inventory is full
                    return EnumActionResult.PASS;
                }
            }

            // Destroy the Redstone Torch block
            if (!world.isRemote) {
                world.destroyBlock(pos, false);
            }

            return EnumActionResult.SUCCESS;
        }

        // Damage -> Redstone Torch

        if (!player.abilities.isCreativeMode && itemstack.getDamage() >= costDamage) {
            // This is too damaged to place Redstone Torch
            return EnumActionResult.PASS;
        }

        // Place Redstone Torch and Damage this
        if (player.isSneaking() && Blocks.REDSTONE_TORCH.asItem().onItemUse(context) == EnumActionResult.SUCCESS) {
            itemstack.setCount(1);
            itemstack.damageItem(costDamage, player);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage, 0));
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed, 0));
        }

        return multimap;
    }

    public static class ItemTier implements IItemTier {
        @Override
        public int getHarvestLevel() {
            return 3;
        }

        @Override
        public int getMaxUses() {
            return 255;
        }

        @Override
        public float getEfficiency() {
            return 12.0F;
        }

        @Override
        public float getAttackDamage() {
            return 1.0F;
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(Blocks.REDSTONE_TORCH);
        }
    }
}
