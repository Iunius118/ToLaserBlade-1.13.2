package iunius118.mods.tolaserblade.item;

import iunius118.mods.tolaserblade.ToLaserBlade;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import com.google.common.collect.Multimap;

public class ItemLaserBlade extends ItemSword {

	private final Item.ToolMaterial material;
	private final float attackDamage;
	private final float attackSpeed;
	public final int[] colors = {0xFFFF0000, 0xFFD0A000, 0xFF00E000, 0xFF0080FF, 0xFF0000FF, 0xFFA000FF, 0xFFC0C0C0, 0xFF020202};

	public static final String KEY_COLOR_CORE = "colorC";
	public static final String KEY_COLOR_HALO = "colorH";

	public ItemLaserBlade() {
		super(ToLaserBlade.ToolMaterials.LASER);
		material = ToLaserBlade.ToolMaterials.LASER;
		attackDamage = 3.0F + material.getDamageVsEntity();
		attackSpeed = 0.0F;
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		ItemStack stack = event.crafting;

		if (stack == null || stack.getItem() != ToLaserBlade.Items.itemLaserBlade) {
			return;
		}

		int invSize = event.craftMatrix.getSizeInventory();

		for (int i = 0; i < invSize; i++) {
			ItemStack itemSlot = event.craftMatrix.getStackInSlot(i);

			if (itemSlot == null) {
				continue;

			} else if (itemSlot.getItem() == ToLaserBlade.Items.itemLaserBlade) {
				NBTTagCompound nbtNew;
				NBTTagCompound nbtOld = itemSlot.getTagCompound();

				if (nbtOld == null) {
					nbtNew = new NBTTagCompound();
				} else {
					nbtNew = (NBTTagCompound)nbtOld.copy();
				}

				BlockPos pos = event.player.getPosition();
				Biome biome = event.player.worldObj.getBiomeForCoordsBody(pos);
				int colorCore = 0xFFFFFFFF;
				int colorHalo = colors[0];


				if (biome instanceof BiomeHell) {
					colorHalo = colors[6];
				} else if (biome instanceof BiomeEnd) {
					colorCore = colors[7];
					colorHalo = colors[7];
				} else {
					float temp = biome.getTemperature();

					if (1.0 > temp && temp >= 0.9) {
						colorHalo = colors[1];
					} else if (0.5 > temp && temp >= 0.2) {
						colorHalo = colors[2];
					} else if (0.2 > temp && temp >= 0.0) {
						colorHalo = colors[3];
					} else if (0.0 > temp) {
						colorHalo = colors[4];
					} else if (temp >= 1.0) {
						colorHalo = colors[5];
					}
				}

				nbtNew.setInteger(KEY_COLOR_CORE, colorCore);
				nbtNew.setInteger(KEY_COLOR_HALO, colorHalo);
				stack.setTagCompound(nbtNew);
				break;
			}
		}
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
