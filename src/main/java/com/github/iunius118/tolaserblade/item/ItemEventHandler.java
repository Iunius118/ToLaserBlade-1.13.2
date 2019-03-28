package com.github.iunius118.tolaserblade.item;

import com.github.iunius118.tolaserblade.ToLaserBlade;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemEventHandler {
	@SubscribeEvent
	public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
		ItemStack original = event.getOriginal();

		if (original.getItem() == ToLaserBlade.Items.LASER_BLADE && original.getDamage() >= LaserBlade.MAX_USES - 1) {
			LaserBlade laserBlade = LaserBlade.create(original);
			ItemStack core = laserBlade.saveTagsToItemStack(new ItemStack(ToLaserBlade.Items.LASER_BLADE_CORE));
			event.getEntityPlayer().setHeldItem(event.getHand(), core);
		}
	}

	@SubscribeEvent
	public void onCriticalHit(CriticalHitEvent event) {
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();

		if (stack.getItem() == ToLaserBlade.Items.LASER_BLADE) {
			((ItemLaserBlade) stack.getItem()).onCriticalHit(event);
		}
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		ItemStack stackOut = event.getCrafting();

		if (stackOut.getItem() == ToLaserBlade.Items.LASER_BLADE) {
			((ItemLaserBlade) stackOut.getItem()).onCrafting(event);
		}
	}

	@SubscribeEvent
	public void onAnvilRepair(AnvilRepairEvent event) {
		ItemStack left = event.getItemInput();

		if (left.getItem() == ToLaserBlade.Items.LASER_BLADE) {
			((ItemLaserBlade) left.getItem()).onAnvilRepair(event);
			event.setBreakChance(0.075F);
		} else if (left.getItem() == ToLaserBlade.Items.LASER_BLADE_CORE) {
			event.setBreakChance(0.075F);
		}
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();

		if (left.getItem() == ToLaserBlade.Items.LASER_BLADE || left.getItem() == ToLaserBlade.Items.LASER_BLADE_CORE) {
			((ItemLaserBlade) ToLaserBlade.Items.LASER_BLADE).onAnvilUpdate(event);
		}
	}
}
