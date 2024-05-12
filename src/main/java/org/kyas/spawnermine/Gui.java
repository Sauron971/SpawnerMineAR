package org.kyas.spawnermine;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;


@SuppressWarnings("deprecation")
public class Gui implements InventoryHolder, Listener {
    private MineKyas plugin;
    private Inventory inventory;
    private int index;
    private String lastEmptyMine;
    private String checkedMine;


    private final ItemStack coal = new ItemStack(Material.COAL_ORE);
    private final ItemStack copper = new ItemStack(Material.COPPER_ORE);

    private final ItemStack iron = new ItemStack(Material.IRON_ORE);
    private final ItemStack gold = new ItemStack(Material.GOLD_ORE);
    private final ItemStack redstone = new ItemStack(Material.REDSTONE_ORE);
    private final ItemStack amethyst = new ItemStack(Material.BUDDING_AMETHYST);
    private final ItemStack delete = new ItemStack(Material.BARRIER);
    private final ItemStack bell = new ItemStack(Material.BELL);

    public Gui() {
        createGui();
    }

    public Gui(MineKyas plugin) {
        this.plugin = plugin;
        this.index = plugin.getConfig().getInt("counterOfMines");
    }

    public void createGui() {
        this.inventory = Bukkit.createInventory(this, 27, ChatColor.translateAlternateColorCodes('&', "Создать точку спавна руды"));
        // Добавляем предметы в инвентарь
        ItemMeta coalMeta = coal.getItemMeta();
        ItemMeta ironMeta = iron.getItemMeta();
        ItemMeta goldMeta = gold.getItemMeta();
        ItemMeta copperMeta = copper.getItemMeta();
        ItemMeta diamondMeta = redstone.getItemMeta();
        ItemMeta amethystMeta = amethyst.getItemMeta();
        ItemMeta bellMeta = bell.getItemMeta();
        ItemMeta deleteMeta = delete.getItemMeta();
        coalMeta.setLore(Collections.singletonList(ChatColor.RESET + "Нажмите добавить в мир рудник"));
        ironMeta.setLore(Collections.singletonList(ChatColor.RESET + "Нажмите добавить в мир рудник"));
        goldMeta.setLore(Collections.singletonList(ChatColor.RESET + "Нажмите добавить в мир рудник"));
        diamondMeta.setLore(Collections.singletonList(ChatColor.RESET + "Нажмите добавить в мир рудник"));

        amethystMeta.setLore(Collections.singletonList(ChatColor.RESET + "Нажмите добавить в мир рудник"));
        copperMeta.setLore(Collections.singletonList(ChatColor.RESET + "Нажмите добавить в мир рудник"));
        coal.setItemMeta(coalMeta);
        iron.setItemMeta(ironMeta);
        gold.setItemMeta(goldMeta);
        copper.setItemMeta(copperMeta);
        redstone.setItemMeta(diamondMeta);
        amethyst.setItemMeta(amethystMeta);
        bellMeta.setLore(Collections.singletonList(ChatColor.RESET + "Заспавнить руду во всех рудниках"));
        deleteMeta.setDisplayName(ChatColor.RESET + "Удалить рудник под вами");
        deleteMeta.setLore(Collections.singletonList(ChatColor.RED + "Внимание! Нужно стоять ровно в центре координат рудника!"));
        bell.setItemMeta(bellMeta);
        delete.setItemMeta(deleteMeta);
        inventory.addItem(coal, copper, iron, gold, redstone, amethyst);
        inventory.setItem(26, bell);
        inventory.setItem(25, delete);
    }

    @Override
    public @NotNull Inventory getInventory() {
        createGui();
        return inventory;
    }

    public void openGui(Player player) {
        player.openInventory(inventory);
    }

    private boolean copyAndEquals(ItemStack from, ItemStack to) {
        ItemMeta f = from.getItemMeta();
        to.setItemMeta(f);
        return from.equals(to);
    }

    private Location roundLoc(World w, Location loc) {
        double x = (int) loc.getX();
        double y = (int) loc.getY();
        double z = (int) loc.getZ();

        return new Location(w, x, y, z);
    }

    private boolean checkMine(Location loc) {
        double locX = loc.getX();
        double locY = loc.getY();
        double locZ = loc.getZ();
        for (int i = 0; i < index; i++) {
            String mine = "mine" + i;
            double x = plugin.getConfig().getDouble(mine + ".x");
            double y = plugin.getConfig().getDouble(mine + ".y");
            double z = plugin.getConfig().getDouble(mine + ".z");
            if (locX == x && locY == y && locZ == z) {
                checkedMine = mine;
                return true;
            }
            if (plugin.getConfig().get(mine) != null && plugin.getConfig().get(mine).equals("")) {
                lastEmptyMine = mine;
            }
        }
        return false;
    }

    private boolean saveToConfig(Location loc, String mat) {
        if (checkMine(loc)) {
            return false;
        }
        if (lastEmptyMine != null) {
            double locX = loc.getX();
            double locY = loc.getY();
            double locZ = loc.getZ();
            plugin.getConfig().set(lastEmptyMine + ".material", mat);
            plugin.getConfig().set(lastEmptyMine + ".x", locX);
            plugin.getConfig().set(lastEmptyMine + ".y", locY);
            plugin.getConfig().set(lastEmptyMine + ".z", locZ);
            plugin.saveConfig();
            plugin.createArrayMines();
            lastEmptyMine = null;
            return true;
        }
        double locX = loc.getX();
        double locY = loc.getY();
        double locZ = loc.getZ();
        String path = "mine" + index;
        index++;
        plugin.getConfig().set(path + ".material", mat);
        plugin.getConfig().set(path + ".x", locX);
        plugin.getConfig().set(path + ".y", locY);
        plugin.getConfig().set(path + ".z", locZ);
        plugin.getConfig().set("counterOfMines", index);
        plugin.saveConfig();
        plugin.createArrayMines();
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        World w = p.getWorld();
        Location loc = p.getLocation();
        // Отменяем возможность вытаскивания предметов из GUI
        if (event.getInventory().getHolder() instanceof Gui) {
            event.setCancelled(true);
        }
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && event.getInventory().getHolder() instanceof Gui) {
            if (copyAndEquals(currentItem, coal)) {
                Location locCoal = roundLoc(w, loc);
                p.closeInventory();
                if (saveToConfig(locCoal, coal.getType().toString())) {
                    p.sendMessage("Рудник создан успешно!");
                } else {
                    p.sendMessage("Рудник в этой точке уже существует!");
                }

            } else if (copyAndEquals(currentItem, copper)) {
                Location locCopper = roundLoc(w, loc);
                p.closeInventory();
                if (saveToConfig(locCopper, copper.getType().toString())) {
                    p.sendMessage("Рудник создан успешно!");

                } else {
                    p.sendMessage("Рудник в этой точке уже существует!");
                }

            } else if (copyAndEquals(currentItem, iron)) {
                Location locIron = roundLoc(w, loc);
                p.closeInventory();
                if (saveToConfig(locIron, iron.getType().toString())) {
                    p.sendMessage("Рудник создан успешно!");
                } else {
                    p.sendMessage("Рудник в этой точке уже существует!");
                }

            } else if (copyAndEquals(currentItem, gold)) {
                Location locGold = roundLoc(w, loc);
                p.closeInventory();
                if (saveToConfig(locGold, gold.getType().toString())) {
                    p.sendMessage("Рудник создан успешно!");
                } else {
                    p.sendMessage("Рудник в этой точке уже существует!");
                }

            } else if (copyAndEquals(currentItem, redstone)) {
                Location locRedstone = roundLoc(w, loc);
                p.closeInventory();
                if (saveToConfig(locRedstone, redstone.getType().toString())) {
                    p.sendMessage("Рудник создан успешно!");
                } else {
                    p.sendMessage("Рудник в этой точке уже существует!");
                }

            } else if (copyAndEquals(currentItem, amethyst)) {
                Location locAmethyst = roundLoc(w, loc);
                p.closeInventory();
                if (saveToConfig(locAmethyst, "AMETHYST_SHARD")) {
                    p.sendMessage("Рудник создан успешно!");
                } else {
                    p.sendMessage("Рудник в этой точке уже существует!");
                }
            } else if (copyAndEquals(currentItem, bell)) {
                new SpawnOres(plugin).spawn();
                p.playSound(loc, Sound.ENTITY_CAT_PURREOW, 100, 50);
            } else if (copyAndEquals(currentItem, delete)) {
                Location locPlayer = p.getLocation();
                locPlayer = roundLoc(w, locPlayer);
                if (checkMine(locPlayer)) {
                    plugin.getConfig().set(checkedMine, "");
                    plugin.saveConfig();
                    lastEmptyMine = null;
                    p.closeInventory();
                    p.playSound(loc, Sound.ENCHANT_THORNS_HIT, 100, 50);
                } else {
                    p.sendMessage("Под вами нет рудника!");
                }
            }

        }
    }
}
