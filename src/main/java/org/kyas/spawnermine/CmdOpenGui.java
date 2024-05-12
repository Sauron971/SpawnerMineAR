package org.kyas.spawnermine;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdOpenGui implements CommandExecutor {

    MineKyas plugin;

    public CmdOpenGui(MineKyas plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length > 0 && strings[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.createArrayMines();
                player.sendMessage("§aКонфигурация плагина успешно перезагружена.");
            } else {
                Gui g = new Gui();
                g.openGui(player);
            }
        }
        return true;
    }
}
