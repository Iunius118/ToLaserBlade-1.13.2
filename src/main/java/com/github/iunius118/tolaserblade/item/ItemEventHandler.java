package com.github.iunius118.tolaserblade.item;

import com.github.iunius118.tolaserblade.ToLaserBlade;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemEventHandler {
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        // When player interact with entity
        ItemStack itemStack = event.getItemStack();

        if (itemStack.getItem() == ToLaserBlade.Items.LASER_BLADE) {
            // For stopping duplicate of Laser Blade when player interact with Item Frame
            event.setCanceled(true);
            EntityPlayer player = event.getEntityPlayer();
            ItemStack itemStack1 = itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy();

            if (event.getTarget().processInitialInteract(event.getEntityPlayer(), event.getHand())) {
                if (player.abilities.isCreativeMode && itemStack == event.getItemStack() && itemStack.getCount() < itemStack1.getCount()) {
                    itemStack.setCount(itemStack1.getCount());
                }

                event.setCancellationResult(EnumActionResult.SUCCESS);
                return;
            }

            event.setCancellationResult(EnumActionResult.PASS);
            return;
        }
    }

    @SubscribeEvent
    public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
        // When item destroyed by damage
        EntityPlayer player = event.getEntityPlayer();

        if (!player.getEntityWorld().isRemote) {
            ItemStack original = event.getOriginal();

            if (original.getItem() == ToLaserBlade.Items.LASER_BLADE) {
                LaserBlade laserBlade = LaserBlade.create(original);
                ItemStack core = laserBlade.saveTagsToItemStack(new ItemStack(ToLaserBlade.Items.LASER_BLADE_CORE));

                // Drop Core
                EntityItem entityitem = new EntityItem(player.world, player.posX, player.posY + 0.5, player.posZ, core);
                player.world.spawnEntity(entityitem);
            }
        }
    }

    @SubscribeEvent
    public void onCriticalHit(CriticalHitEvent event) {
        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();

        if (stack.getItem() instanceof ItemLaserBlade) {
            ((ItemLaserBlade) stack.getItem()).onCriticalHit(event);
        }
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        ItemStack stackOut = event.getCrafting();

        if (stackOut.getItem() instanceof ItemLaserBlade) {
            ((ItemLaserBlade) stackOut.getItem()).onCrafting(event);
        }
    }

    @SubscribeEvent
    public void onAnvilRepair(AnvilRepairEvent event) {
        ItemStack left = event.getItemInput();

        if (left.getItem() instanceof ItemLaserBlade) {
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
