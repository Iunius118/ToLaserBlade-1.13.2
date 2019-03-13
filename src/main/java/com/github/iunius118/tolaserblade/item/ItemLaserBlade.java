package com.github.iunius118.tolaserblade.item;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.github.iunius118.tolaserblade.ToLaserBlade;
import com.github.iunius118.tolaserblade.ToLaserBladeConfig;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.NetherBiome;
import net.minecraft.world.biome.TheEndBiome;
import net.minecraft.world.biome.TheVoidBiome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemLaserBlade extends ItemSword {
	private final IItemTier tier = new ItemLaserBlade.ItemTier();
	private final float attackDamage;
	private final float attackSpeed;
	public static Item.Properties properties = (new Item.Properties()).setNoRepair().group(ItemGroup.TOOLS);

	// Blade color table
	public final static int[] colors = { 0xFFFF0000, 0xFFD0A000, 0xFF00E000, 0xFF0080FF, 0xFF0000FF, 0xFFA000FF, 0xFFFFFFFF, 0xFF020202, 0xFFA00080 };

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

	private static final IItemPropertyGetter BLOCKING_GETTER = (stack, world, entity) -> {
		return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
	};

	public ItemLaserBlade() {
		super(new ItemLaserBlade.ItemTier(), 3, -1.2F, properties);

		attackDamage = 3.0F + tier.getAttackDamage();
		attackSpeed = -1.2F;

		addPropertyOverride(new ResourceLocation("blocking"), BLOCKING_GETTER);
	}

	/* Handle events */

	public void onCrafting(ItemCraftedEvent event) {
		if (event.getPlayer().world.isRemote) {
			return;
		}

		ItemStack stackOut = event.getCrafting();
		NBTTagCompound nbt = stackOut.getTag();

		if (nbt == null) {
			nbt = ItemLaserBlade.setColors(stackOut, 0xFFFFFFFF, ItemLaserBlade.colors[0], false, false);
		}

		if (nbt.hasKey(ItemLaserBlade.KEY_IS_CRAFTING)) {
			// Crafting on crafting table
			nbt.removeTag(ItemLaserBlade.KEY_IS_CRAFTING);
			return;
		}

		ItemLaserBlade.changeBladeColorByBiome(nbt, event.getPlayer());
	}

	public void onAnvilRepair(AnvilRepairEvent event) {
		ItemStack left = event.getItemInput();

		if (!left.isEnchanted() && event.getIngredientInput().isEmpty()) {
			ItemStack output = event.getItemResult();
			String name = output.getDisplayName().getString();

			// Use GIFT code
			if ("GIFT".equals(name) || "おたから".equals(name)) {
				ItemLaserBlade.setPerformanceClass3(output, ItemLaserBlade.colors[1]);
				output.clearCustomName();
			}
		}
	}

	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		Item itemRight = right.getItem();
		String name = event.getName();
		ItemStack output = left.copy();

		// Upgrade to Class 4
		if (itemRight == net.minecraft.init.Items.NETHER_STAR) {
			if (ItemLaserBlade.upgradeClass4(output)) {
				ItemLaserBlade.changeDisplayNameOnAnvil(left, output, name);

				event.setCost(ItemLaserBlade.COST_LVL_CLASS_4);
				event.setMaterialCost(ItemLaserBlade.COST_ITEM_CLASS_4);
				event.setOutput(output);
			}

			return;

		} else if (ItemTags.getCollection().getOrCreate(new ResourceLocation(ToLaserBlade.MOD_ID + ":mob_head")).contains(itemRight)) {
			// Increase Attack point
			NBTTagCompound nbt = output.getTag();
			if (nbt != null) {
				// Only Class 4 blade
				float atk = nbt.getFloat(ItemLaserBlade.KEY_ATK);
				if (atk >= ItemLaserBlade.MOD_ATK_CLASS_4 && atk < 2041) {
					ItemLaserBlade.changeDisplayNameOnAnvil(left, output, name);

					if (ItemLaserBlade.getSkullOwnerName(right).equals("Iunius")) {
						// For debug
						nbt.setFloat(ItemLaserBlade.KEY_ATK, 2040.0f);
					} else {
						nbt.setFloat(ItemLaserBlade.KEY_ATK, atk + 1.0f);
					}

					event.setCost((int) atk / 100 + 10);
					event.setMaterialCost(1);
					event.setOutput(output);

					return;
				}
			}
		}
		// Change blade colors
		else if (ItemLaserBlade.changeBladeColorByItem(output.getTag(), right)) {
			ItemLaserBlade.changeDisplayNameOnAnvil(left, output, name);

			event.setCost(1);
			event.setMaterialCost(1);
			event.setOutput(output);

			return;
		}
	}

	/* Set color and performance */

	public static NBTTagCompound setPerformance(ItemStack stack, float modSpeed, float modAttack) {
		NBTTagCompound nbt = stack.getOrCreateTag();

		nbt.setFloat(KEY_SPD, modSpeed);
		nbt.setFloat(KEY_ATK, modAttack);

		return nbt;
	}

	public static NBTTagCompound setColors(ItemStack stack, int colorCore, int colorHalo, boolean isSubColorCore, boolean isSubColorHalo) {
		NBTTagCompound nbt = stack.getOrCreateTag();

		nbt.setInt(KEY_COLOR_CORE, colorCore);
		nbt.setInt(KEY_COLOR_HALO, colorHalo);
		nbt.setBoolean(KEY_IS_SUB_COLOR_CORE, isSubColorCore);
		nbt.setBoolean(KEY_IS_SUB_COLOR_HALO, isSubColorHalo);

		return nbt;
	}

	public static boolean checkColors(ItemStack stack, int colorCore, int colorHalo, boolean isSubColorCore, boolean isSubColorHalo) {
		NBTTagCompound nbt = stack.getOrCreateTag();

		if (!nbt.contains(KEY_COLOR_CORE, NBT.TAG_INT)) {
			nbt.setInt(KEY_COLOR_CORE, 0xFFFFFFFF);
		}

		if (colorCore != nbt.getInt(KEY_COLOR_CORE)) {
			return false;
		}

		if (!nbt.contains(KEY_COLOR_HALO, NBT.TAG_INT)) {
			nbt.setInt(KEY_COLOR_HALO, colors[0]);
		}

		if (colorHalo != nbt.getInt(KEY_COLOR_HALO)) {
			return false;
		}

		if (!nbt.hasKey(KEY_IS_SUB_COLOR_CORE)) {
			nbt.setBoolean(KEY_IS_SUB_COLOR_CORE, false);
		}

		if (isSubColorCore != nbt.getBoolean(KEY_IS_SUB_COLOR_CORE)) {
			return false;
		}

		if (!nbt.hasKey(KEY_IS_SUB_COLOR_HALO)) {
			nbt.setBoolean(KEY_IS_SUB_COLOR_HALO, false);
		}

		if (isSubColorHalo != nbt.getBoolean(KEY_IS_SUB_COLOR_HALO)) {
			return false;
		}

		return true;
	}

	public static NBTTagCompound setPerformanceClass1(ItemStack stack, int colorHalo) {
		setPerformance(stack, 0, MOD_ATK_CLASS_1);
		return setColors(stack, 0xFFFFFFFF, colorHalo, false, false);
	}

	public static NBTTagCompound setPerformanceClass2(ItemStack stack) {
		setPerformance(stack, 0, 0);
		return setColors(stack, 0xFFFFFFFF, colors[0], false, false);
	}

	public static NBTTagCompound setPerformanceClass3(ItemStack stack, int colorHalo) {
		stack.addEnchantment(Enchantments.SMITE, 5);
		NBTTagCompound nbt = setPerformance(stack, MOD_SPD_CLASS_3, MOD_ATK_CLASS_3);

		if (checkColors(stack, 0xFFFFFFFF, colors[0], false, false)) {
			// Color the blade yellow, if the blade color is default
			return setColors(stack, 0xFFFFFFFF, colorHalo, false, false);
		}

		return nbt;
	}

	public static boolean changeBladeColorByItem(NBTTagCompound nbt, ItemStack stack) {
		if (nbt == null) {
			return false;
		}

		Item item = stack.getItem();

		if (item instanceof ItemDye) {
			int color = ((ItemDye) stack.getItem()).getDyeColor().getMapColor().colorValue | 0xFF000000;

			if (nbt.contains(KEY_COLOR_CORE, NBT.TAG_INT) && nbt.getInt(KEY_COLOR_CORE) == color) {
				return false;
			}

			nbt.setInt(KEY_COLOR_CORE, color);

			return true;
		} else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockStainedGlass) {
			int color = ((BlockStainedGlass)((ItemBlock) item).getBlock()).getColor().getMapColor().colorValue | 0xFF000000;

			if (nbt.contains(KEY_COLOR_HALO, NBT.TAG_INT) && nbt.getInt(KEY_COLOR_HALO) == color) {
				return false;
			}

			nbt.setInt(KEY_COLOR_HALO, color);

			return true;
		}

		return false;
	}

	public static void changeBladeColorByBiome(NBTTagCompound nbt, EntityPlayer player) {
		World world = player.world;
		Biome biome = world.getBiome(player.getPosition());

		// Dyeing by Biome type or Biome temperature
		if (world.getDimension().getType() == DimensionType.NETHER || biome instanceof NetherBiome) {
			// Nether
			boolean isSubColorCore = nbt.getBoolean(KEY_IS_SUB_COLOR_CORE);
			nbt.setBoolean(KEY_IS_SUB_COLOR_CORE, !isSubColorCore);
		} else if (world.getDimension().getType() == DimensionType.THE_END || biome instanceof TheEndBiome) {
			// The End
			boolean isSubColorHalo = nbt.getBoolean(KEY_IS_SUB_COLOR_HALO);
			nbt.setBoolean(KEY_IS_SUB_COLOR_HALO, !isSubColorHalo);
		} else if (biome instanceof TheVoidBiome) {
			// The Void
			nbt.setInt(KEY_COLOR_CORE, colors[7]);
			nbt.setInt(KEY_COLOR_HALO, colors[7]);
		} else {
			// Biomes on Overworld or the other dimensions
			float temp = biome.getDefaultTemperature();

			if (temp > 1.5F) {
				// t > 1.5
				nbt.setInt(KEY_COLOR_HALO, colors[5]);
			} else if (temp > 1.0F) {
				// 1.5 >= t > 1.0
				nbt.setInt(KEY_COLOR_HALO, colors[8]);
			} else if (temp > 0.8F) {
				// 1.0 >= t > 0.8
				nbt.setInt(KEY_COLOR_HALO, colors[1]);
			} else if (temp >= 0.5F) {
				// 0.8 >= t >= 0.5
				nbt.setInt(KEY_COLOR_HALO, colors[0]);
			} else if (temp >= 0.2F) {
				// 0.5 > t >= 0.2
				nbt.setInt(KEY_COLOR_HALO, colors[2]);
			} else if (temp >= -0.25F) {
				// 0.2 > t >= -0.25
				nbt.setInt(KEY_COLOR_HALO, colors[3]);
			} else {
				// -0.25 > t
				nbt.setInt(KEY_COLOR_HALO, colors[4]);
			}
		}
	}

	public static void changeDisplayNameOnAnvil(ItemStack left, ItemStack output, String name) {
		// Set or Clear display name
		if (StringUtils.isBlank(name)) {
			if (left.hasDisplayName()) {
				output.clearCustomName();
			}
		} else {
			if (!name.equals(left.getDisplayName().getString())) {
				output.setDisplayName(new TextComponentString(name));
			}
		}
	}

	public static String getSkullOwnerName(ItemStack stack) {
		if (stack.getItem() == Items.PLAYER_HEAD && stack.hasTag()) {
			String name = "";
			NBTTagCompound tag = stack.getTag();

			if (tag.contains("SkullOwner", NBT.TAG_STRING)) {
				name = tag.getString("SkullOwner");
			} else if (tag.contains("SkullOwner", NBT.TAG_COMPOUND)) {
				NBTTagCompound tagOwner = tag.getCompound("SkullOwner");

				if (tagOwner.contains("Name", NBT.TAG_STRING)) {
					name = tagOwner.getString("Name");
				}
			}

			if (name != null) {
				return name;
			}
		}

		return "";
	}

	public static boolean enchant(@Nonnull ItemStack stack, @Nonnull Enchantment ench, int level, boolean canOverrideLavel) {
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		int oldLevel = map.getOrDefault(Enchantments.SMITE, 0);
		int newLevel = level;

		if (oldLevel == 0) {
			Map<Enchantment, Integer> mapNew = new HashMap<>();

			for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
				Enchantment key = entry.getKey();

				if (key.isCompatibleWith(ench)) {
					mapNew.put(key, entry.getValue());

				} else if (canOverrideLavel) {
					int keyLevel = map.get(key);

					if (keyLevel > newLevel) {
						newLevel = keyLevel;
					}
				}
			}

			mapNew.put(ench, newLevel);
			EnchantmentHelper.setEnchantments(mapNew, stack);
			return true;

		} else if (oldLevel < newLevel) {
			map.put(ench, newLevel);
			EnchantmentHelper.setEnchantments(map, stack);
			return true;

		} else {

		}

		return false;
	}

	public static boolean upgradeClass4(ItemStack stack) {
		NBTTagCompound nbt = stack.getTag();

		if (nbt == null) {
			// Upgrade all to Class 4
			stack.addEnchantment(Enchantments.SMITE, LVL_SMITE_CLASS_4);
			stack.addEnchantment(Enchantments.SWEEPING, LVL_SWEEPING_CLASS_4);
			nbt = stack.getTag();
			nbt.setFloat(KEY_ATK, MOD_ATK_CLASS_4);
			nbt.setFloat(KEY_SPD, MOD_SPD_CLASS_3);

			return true;
		}

		boolean isAtkClass4 = false;
		boolean isSpdClass4 = false;
		boolean isSmiteClass4 = false;
		boolean isSweepingClass4 = false;

		// Upgrade Attack to Class 4
		if (nbt.getFloat(KEY_ATK) < MOD_ATK_CLASS_4) {
			nbt.setFloat(KEY_ATK, MOD_ATK_CLASS_4);
		} else {
			isAtkClass4 = true;
		}

		// Upgrade Speed to Class 4
		if (nbt.getFloat(KEY_SPD) < MOD_SPD_CLASS_3) {
			nbt.setFloat(KEY_SPD, MOD_SPD_CLASS_3);
		} else {
			isSpdClass4 = true;
		}

		// Upgrade Enchantment to Class 4
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		Integer lvlSmite = map.get(Enchantments.SMITE);
		Integer lvlSweeping = map.get(Enchantments.SWEEPING);

		// Upgrade Smite to Class 4
		if (lvlSmite == null) {
			Map<Enchantment, Integer> mapNew = new HashMap<>();

			for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
				Enchantment key = entry.getKey();

				if (key.isCompatibleWith(Enchantments.SMITE)) {
					mapNew.put(key, entry.getValue());
				}
			}

			map = mapNew;
			map.put(Enchantments.SMITE, LVL_SMITE_CLASS_4);
		} else if (lvlSmite < LVL_SMITE_CLASS_4) {
			map.put(Enchantments.SMITE, LVL_SMITE_CLASS_4);
		} else {
			isSmiteClass4 = true;
		}

		// Upgrade Sweeping to Class 4
		if (lvlSweeping == null || lvlSweeping < LVL_SWEEPING_CLASS_4) {
			map.put(Enchantments.SWEEPING, LVL_SWEEPING_CLASS_4);
		} else {
			isSweepingClass4 = true;
		}

		if (isAtkClass4 && isSpdClass4 && isSmiteClass4 && isSweepingClass4) {
			return false; // Already Class 4
		}

		EnchantmentHelper.setEnchantments(map, stack);

		return true;
	}

	/* Shield function */

	@Override
	public EnumAction getUseAction(ItemStack stack) {
		if (ToLaserBladeConfig.COMMON.isEnabledBlockingWithLaserBladeInServer.get()) {
			return EnumAction.BLOCK;
		} else {
			return EnumAction.NONE;
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		if (ToLaserBladeConfig.COMMON.isEnabledBlockingWithLaserBladeInServer.get()) {
			return 72000;
		} else {
			return 0;
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (ToLaserBladeConfig.COMMON.isEnabledBlockingWithLaserBladeInServer.get()) {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		} else {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		}
	}

	/* Characterizing */

	@Override
	public float getAttackDamage() {
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
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return tier.getEfficiency();
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, ToolType tool, EntityPlayer player, IBlockState blockState) {
		return tier.getHarvestLevel();
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			float modDamage = 0;
			float modSpeed = 0;

			NBTTagCompound nbt = stack.getTag();
			if (nbt != null) {
				// Fix attack speed for old version
				if (!nbt.hasKey(KEY_SPD)) {
					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack) >= LVL_SMITE_CLASS_4) {
						nbt.setFloat(KEY_SPD, MOD_SPD_CLASS_3);
					} else {
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
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage + modDamage, 0));
			multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed + modSpeed, 0));
		}

		return multimap;
	}

	@OnlyIn(Dist.CLIENT)
	public static class ColorHandler implements IItemColor {
		@Override
		public int getColor(ItemStack stack, int tintIndex) {
			if (ToLaserBladeConfig.CLIENT.isEnabledLaserBlade3DModel.get()) {
				return 0xFFFFFFFF;
			}

			switch (tintIndex) {
			case 1:
				return getColorFronNBT(stack, KEY_COLOR_HALO, KEY_IS_SUB_COLOR_HALO, colors[0]);

			case 2:
				return getColorFronNBT(stack, KEY_COLOR_CORE, KEY_IS_SUB_COLOR_CORE, 0xFFFFFFFF);

			default:
				return 0xFFFFFFFF;
			}
		}

		private int getColorFronNBT(ItemStack stack, String keyColor, String keyIsSubColor, int defaultColor) {
			NBTTagCompound nbt = stack.getTag();

			if (nbt != null && nbt.contains(keyColor, NBT.TAG_INT)) {
				int color = nbt.getInt(keyColor);

				if (nbt.getBoolean(keyIsSubColor)) {
					color = ~color | 0xFF000000;
				}

				return color;
			}

			return defaultColor;
		}
	}

	public static class ItemTier implements IItemTier {
		@Override
		public int getHarvestLevel() {
			return 3;
		}

		@Override
		public int getMaxUses() {
			return 32767;
		}

		@Override
		public float getEfficiency() {
			return 12.0F;
		}

		@Override
		public float getAttackDamage() {
			return 3.0F;
		}

		@Override
		public int getEnchantability() {
			return 22;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.fromItems(Items.REDSTONE);
		}
	}
}
