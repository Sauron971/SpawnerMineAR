package org.kyas.spawnermine;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpawnOres implements Runnable {
    private final MineKyas plugin;
    private final Random rand = new Random();
    private ArrayList<Material> coal = new ArrayList<>();
    private ArrayList<Material> copper = new ArrayList<>();
    private ArrayList<Material> iron = new ArrayList<>();
    private ArrayList<Material> gold = new ArrayList<>();
    private ArrayList<Material> redstone = new ArrayList<>();
    private ArrayList<Material> amethyst = new ArrayList<>();

    public SpawnOres(MineKyas plugin) {
        this.plugin = plugin;

        coal.add(Material.COAL_ORE);
        coal.add(Material.DEEPSLATE_COAL_ORE);
        copper.add(Material.COPPER_ORE);
        copper.add(Material.DEEPSLATE_COPPER_ORE);
        iron.add(Material.IRON_ORE);
        iron.add(Material.DEEPSLATE_IRON_ORE);
        gold.add(Material.GOLD_ORE);
        gold.add(Material.DEEPSLATE_GOLD_ORE);
        amethyst.add(Material.AMETHYST_SHARD);
        redstone.add(Material.REDSTONE_ORE);
        redstone.add(Material.DEEPSLATE_REDSTONE_ORE);

        coal.add(Material.COAL_BLOCK);
        copper.add(Material.RAW_COPPER_BLOCK);
        iron.add(Material.RAW_IRON_BLOCK);
        gold.add(Material.RAW_GOLD_BLOCK);
        amethyst.add(Material.AMETHYST_BLOCK);
    }

    private List<List<Location>> getLocationArray(Location center) {
        List<List<Location>> array = new ArrayList<>();
        int offsetX = 0;
        int offsetZ = 0;
        if (center.getX() < 0 && !(center.getZ() < 0)) {
            offsetX = -1;
        }
        if (center.getZ() < 0 && center.getX() < 0) {
            offsetX = -1;
            offsetZ = -1;
        }
        if (center.getZ() < 0 && !(center.getX() < 0)) {
            offsetZ = -1;
        }

        for (int i = 0; i < 5; i++) {
            List<Location> listTemp = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Location locTemp = new Location(center.getWorld(), center.getX() - 2 + j + offsetX, center.getY(), center.getZ() + 2 - i + offsetZ);
                listTemp.add(locTemp);
            }
            array.add(listTemp);
        }
        return array;
    }

    private void spawnInWorld(List<List<Location>> arrayLocation, String material) {
        for (List<Location> innerList : arrayLocation) {
            for (Location location : innerList) {
                if (rand.nextInt(2) == 1) {
                    switch (material) {
                        case "COAL_ORE":
                            location.getBlock().setType(coal.get(rand.nextInt(2)));
                            break;
                        case "COPPER_ORE":
                            location.getBlock().setType(copper.get(rand.nextInt(2)));
                            break;
                        case "IRON_ORE":
                            location.getBlock().setType(iron.get(rand.nextInt(2)));
                            break;
                        case "GOLD_ORE":
                            location.getBlock().setType(gold.get(rand.nextInt(2)));
                            break;
                        case "REDSTONE_ORE":
                            location.getBlock().setType(redstone.get(rand.nextInt(2)));
                            break;
                        case "AMETHYST_SHARD":
                            location.getBlock().setType(amethyst.get(0));
                            break;
                        default:
                            plugin.getLogger().info("Не удалось заспавнить руду в руднике!" + material);
                    }
                } else {
                    location.getBlock().setType(Material.STONE);
                }
            }
        }
        if (rand.nextInt(100) == 2) {
            arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(Material.DIAMOND_ORE);
        }
        if (rand.nextInt(100) == 1) {
            arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(Material.EMERALD_ORE);
        }
        if (rand.nextInt(100) <= 10) {
            switch (material) {
                case "COAL_ORE":
                    arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(coal.get(2));
                    break;
                case "COPPER_ORE":
                    arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(copper.get(2));
                    break;
                case "IRON_ORE":
                    arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(iron.get(2));
                    break;
                case "GOLD_ORE":
                    arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(gold.get(2));
                    break;
                case "AMETHYST_SHARD":
                    arrayLocation.get(rand.nextInt(5)).get(rand.nextInt(5)).getBlock().setType(amethyst.get(1));
                    break;
            }
        }
    }

    public void spawn() {
        for (int i = 0; i < plugin.locations.size() - 1; i += 2) {
            Location locCenter = (Location) plugin.locations.get(i);
            String material = (String) plugin.locations.get(i + 1);
            spawnInWorld(getLocationArray(locCenter), material);
//            Objects.requireNonNull(plugin.getServer().getPlayer("_Kyas17")).sendMessage("We did this! We spawn!");
        }
    }

    @Override
    public void run() {
        spawn();
    }
}
