package iunius118.mods.tolaserblade.item;

import iunius118.mods.tolaserblade.ToLaserBlade;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLaserBlade extends ItemSword {

	private final Item.ToolMaterial material;
	private final float attackDamage;
	private final float attackSpeed;

	public ItemLaserBlade() {
		super(ToLaserBlade.ToolMaterials.LASER);
		material = ToLaserBlade.ToolMaterials.LASER;
		attackDamage = 3.0F + material.getDamageVsEntity();
		attackSpeed = 0.0F;
	}

	@Override
	public float getDamageVsEntity() {
		return attackDamage;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		return material.getEfficiencyOnProperMaterial();
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}



	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return material.getHarvestLevel();
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.removeAll(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName());
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage, 0));
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed, 0));
		}

		return multimap;
	}

}
