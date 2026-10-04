package me.nhan.fastbucketfix;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BucketListener implements Listener {

    // Bắt trường hợp ngắm thẳng tâm vào khối nước nguồn (đứng yên múc)
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onBucketFill(PlayerBucketFillEvent event) {
        Block block = event.getBlock();
        if (block == null || block.getType() != Material.WATER) {
            block = event.getBlockClicked();
        }

        if (block != null && block.getType() == Material.WATER) {
            if (block.getBlockData() instanceof Levelled levelled && levelled.getLevel() == 0) {
                event.setCancelled(false);
                event.setItemStack(new ItemStack(Material.WATER_BUCKET));
            }
        }
    }

    // Bắt trường hợp spam click / flick tâm đâm trúng sàn bên dưới
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFloorInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BUCKET && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock == null) return;

            Block targetWater = null;
            if (clickedBlock.getType() == Material.WATER) {
                targetWater = clickedBlock;
            } else {
                Block relative = clickedBlock.getRelative(event.getBlockFace());
                if (relative.getType() == Material.WATER) {
                    targetWater = relative;
                }
            }

            if (targetWater != null && targetWater.getBlockData() instanceof Levelled levelled && levelled.getLevel() == 0) {
                event.setCancelled(true);
                targetWater.setType(Material.AIR);

                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
                }

                player.playSound(player.getLocation(), Sound.ITEM_BUCKET_FILL, 1.0f, 1.0f);
            }
        }
    }
}
