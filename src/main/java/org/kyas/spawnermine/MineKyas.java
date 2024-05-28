package org.kyas.spawnermine;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MineKyas extends JavaPlugin {
    public List<Object> locations = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.STRIKETHROUGH + "[MineKyas] А это ты... Давно не виделись. Как дела? Я была так занята, пока была мертва. Ну, после того как ты убила меня.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[MineKyas] is enable!");
        this.getCommand("createDotMine").setExecutor(new CmdOpenGui(this));
        getServer().getPluginManager().registerEvents(new Gui(this), this);
        getServer().getPluginManager().registerEvents(new BarkThreads(), this);
        this.saveDefaultConfig();
        int time = this.getConfig().getInt("per_min_spawn") * 1200;
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SpawnOres(this), 0L, time);
        createArrayMines();
    }

    @Override
    public void onDisable() {
        // Plugin disabling logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.STRIKETHROUGH + "[MineKyas] Прощай, Кэролайн. Ты победила. Уходи. Было весело. Не возвращайся.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[MineKyas] is disable!");
    }
    // Создаем массив всех центров и материалов находящихся в конфиге
    // 1 локация 2 материал на этой локации
    public void createArrayMines() {
        locations.clear();
        int times = getConfig().getInt("counterOfMines");
        for (int i = 0; i < times; i++) {
            if (getConfig().get("mine" + i).equals("")) {
                continue;
            }
            double x = getConfig().getDouble("mine" + i + ".x");
            double z = getConfig().getDouble("mine" + i + ".z");
            double y = getConfig().getDouble("mine" + i + ".y");
            String mat = Objects.requireNonNull(getConfig().get("mine" + i + ".material")).toString();
            World w = getServer().getWorld((String) Objects.requireNonNull(getConfig().get("world")));
            Location loc = new Location(w, x, y, z);
            locations.add(loc);
            locations.add(mat);
        }
    }
}
