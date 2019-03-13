package com.github.iunius118.tolaserblade.item;

import com.github.iunius118.tolaserblade.ToLaserBlade;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemEventHandler {
	@SubscribeEvent
	public void onCriticalHit(CriticalHitEvent event) {

	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		ItemStack stackOut = event.getCrafting();

		if (stackOut.getItem() == ToLaserBlade.Items.laser_blade) {
			((ItemLaserBlade) stackOut.getItem()).onCrafting(event);
		}
	}

	@SubscribeEvent
	public void onAnvilRepair(AnvilRepairEvent event) {
		ItemStack left = event.getItemInput();

		if (left.getItem() == ToLaserBlade.Items.laser_blade) {
			((ItemLaserBlade) left.getItem()).onAnvilRepair(event);
		}
	}

	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();

		if (left.getItem() == ToLaserBlade.Items.laser_blade) {
			((ItemLaserBlade) left.getItem()).onAnvilUpdate(event);
		}
	}
}
