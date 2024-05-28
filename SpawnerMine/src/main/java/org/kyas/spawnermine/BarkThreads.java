package org.kyas.spawnermine;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarkThreads implements Listener {
    List<Material> axes = new ArrayList<>(Arrays.asList( //всего 6
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
    ));
    List<Material> woods = new ArrayList<>(Arrays.asList( //всего 10
            Material.OAK_LOG, // Дуб
            Material.DARK_OAK_LOG, // Тёмный дуб
            Material.BIRCH_LOG, // Берёза
            Material.SPRUCE_LOG, // Ель
            Material.JUNGLE_LOG, // Тропическое дерево
            Material.ACACIA_LOG, // Акация
            Material.WARPED_STEM, // Искаженный стебель
            Material.CRIMSON_STEM, // Багровый стебель
            Material.MANGROVE_LOG, // Мангровое дерево
            Material.CHERRY_LOG // Вишнёвое дерево
    ));

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        Material itemMaterial = item.getType();
        Block block = e.getClickedBlock();
        if (axes.contains(itemMaterial)) {
            if (block != null && woods.contains(block.getType())) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.STRING));
            }
        }
    }
}
