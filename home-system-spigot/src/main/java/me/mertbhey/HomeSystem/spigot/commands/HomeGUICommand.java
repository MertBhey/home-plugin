package me.mertbhey.HomeSystem.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mertbhey.HomeSystem.spigot.managers.GUIBuilder;
import me.mertbhey.HomeSystem.spigot.managers.HomeManager;
import me.mertbhey.HomeSystem.spigot.managers.i18n;

public class HomeGUICommand implements CommandExecutor {
  @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!label.equalsIgnoreCase("homes")) {
      return false;
    }

    if(!(sender instanceof Player)) {
      sender.sendMessage(i18n.getMessage("player_only"));
      return true;
    }

    Player player = (Player) sender;

    if(!player.hasPermission("homesystem.homes")) {
      sender.sendMessage(i18n.getMessage("no_permission"));
      return true;
    }

    Inventory inventory = new GUIBuilder(54)
      .putDefaultHomeItems()
      .setTitle(i18n.getMessageWithoutPrefix("home_gui_title"))
      .setPlayer(player)
      .addHomes(HomeManager.getHomes(player.getUniqueId()))
      .build();

    player.openInventory(inventory);
    return true;
  }
}
