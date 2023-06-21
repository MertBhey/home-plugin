package me.mertbhey.HomeSystem.spigot.commands;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mertbhey.HomeSystem.API.HomeAPI;
import me.mertbhey.HomeSystem.common.structures.Home;
import me.mertbhey.HomeSystem.spigot.managers.HomeManager;
import me.mertbhey.HomeSystem.spigot.managers.i18n;

public class CreateHomeCommand implements CommandExecutor {
  @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!label.equalsIgnoreCase("sethome")) {
      return false;
    }

    if(!(sender instanceof Player)) {
      sender.sendMessage(i18n.getMessage("player_only"));
      return true;
    }

    Player player = (Player) sender;

    if(!player.hasPermission("homesystem.sethome")) {
      sender.sendMessage(i18n.getMessage("no_permission"));
      return true;
    }

    if(args.length != 1) {
      sender.sendMessage(i18n.getMessage("invalid_usage"));
      return true;
    }

    if(HomeManager.getHomes(player.getUniqueId()).size() >= 45) {
      sender.sendMessage(i18n.getMessage("home_limit_reached", 45));
      return true;
    }

    Optional<Home> cacheValue = HomeManager.getHome(player.getUniqueId(), args[0]);
    if(cacheValue.isPresent()) {
      sender.sendMessage(i18n.getMessage("home_already_exists", args[0]));
      return true;
    }

    boolean isCreated = HomeAPI.createHome(new Home(
      player.getUniqueId(),
      args[0],
      player.getWorld().getName(),
      player.getLocation().getX(),
      player.getLocation().getY(),
      player.getLocation().getZ(),
      player.getLocation().getYaw(),
      player.getLocation().getPitch()
    ));
    if(!isCreated) {
      sender.sendMessage(i18n.getMessage("unexpected_error"));
      return true;
    }

    HomeManager.updateCache(player.getUniqueId());
    sender.sendMessage(i18n.getMessage("home_created", args[0]));
    return true;
  }
}
