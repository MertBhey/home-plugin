package me.mertbhey.HomeSystem.spigot.commands;

import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.mertbhey.HomeSystem.API.HomeAPI;
import me.mertbhey.HomeSystem.common.structures.Home;
import me.mertbhey.HomeSystem.spigot.managers.HomeManager;
import me.mertbhey.HomeSystem.spigot.managers.i18n;

public class DeleteHomeCommand implements CommandExecutor, TabCompleter {
  @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!label.equalsIgnoreCase("delhome")) {
      return false;
    }

    if(!(sender instanceof Player)) {
      sender.sendMessage(i18n.getMessage("player_only"));
      return true;
    }

    Player player = (Player) sender;

    if(!player.hasPermission("homesystem.delhome")) {
      sender.sendMessage(i18n.getMessage("no_permission"));
      return true;
    }

    if(args.length != 1) {
      sender.sendMessage(i18n.getMessage("invalid_usage"));
      return true;
    }

    Optional<Home> cacheValue = HomeManager.getHome(player.getUniqueId(), args[0]);
    if(!cacheValue.isPresent()) {
      sender.sendMessage(i18n.getMessage("home_not_found", args[0]));
      return true;
    }

    boolean isDeleted = HomeAPI.deleteHome(player.getUniqueId(), cacheValue.get().getId());
    if(!isDeleted) {
      sender.sendMessage(i18n.getMessage("unexpected_error"));
      return true;
    }

    HomeManager.updateCache(player.getUniqueId());
    sender.sendMessage(i18n.getMessage("home_deleted", args[0]));
    return true;
  }

  @Override public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if(!(sender instanceof Player)) {
      return null;
    }

    Player player = (Player) sender;

    if(args.length == 1) {
      return HomeManager.getHomes(player.getUniqueId())
        .stream()
        .map((home) -> home.getName())
        .filter((name) -> name.toLowerCase().startsWith(args[0].toLowerCase()))
        .toList();
    }

    return List.of();
  }
}
