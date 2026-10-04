package me.nhan.fastbucketfix;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BucketListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BUCKET && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock == null) return;

            Block targetWater = clickedBlock.getRelative(event.getBlockFace());
            if (targetWater.getType() == Material.WATER) {
                if (targetWater.getBlockData() instanceof Levelled levelled && levelled.getLevel() == 0) {
                    event.setCancelled(true);
                    targetWater.setType(Material.AIR);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.WATER_BUCKET));
                    player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BUCKET_FILL, 1.0f, 1.0f);
                }
            }
        }
    }
}
