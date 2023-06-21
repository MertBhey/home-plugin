package me.mertbhey.HomeSystem.spigot.managers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.mertbhey.HomeSystem.API.HomeAPI;
import me.mertbhey.HomeSystem.common.structures.Home;
import me.mertbhey.HomeSystem.spigot.HomePlugin;
import net.jodah.expiringmap.ExpiringMap;

public class HomeManager {
  private static Map<UUID, List<Home>> homes = ExpiringMap.builder()
    .expiration(15, TimeUnit.SECONDS)
    .build();

  /**
   * Returns the cached homes for the given UUID. If the cache is not exists, it will be updated.
   */
  public static List<Home> getHomes(UUID uuid) {
    if(!homes.containsKey(uuid)) {
      HomeManager.updateCache(uuid);
    }

    return homes.get(uuid);
  }

  /**
   * Find a home by its name
   */
  public static Optional<Home> getHome(UUID id, String name) {
    return HomeManager
      .getHomes(id)
      .stream()
      .filter((home) -> home.getName().equalsIgnoreCase(name))
      .findFirst();
  }

  /**
   * Deletes the cache for the given UUID
   */
  public static void deleteCache(UUID uuid) {
    HomeManager.homes.remove(uuid);
  }

  /**
   * Force-update the cache for the given UUID
   */
  public static void updateCache(UUID uuid) {
    HomeManager.homes.put(uuid, HomeAPI.getHomes(uuid));
  }

  /**
   * Teleports the player to the given home.
   * Player is expected to be online.
   */
  public static void teleportPlayerTo(Home home) {
    Player player = Bukkit.getPlayer(home.getUuid());
    if(player == null) {
      HomePlugin
        .getInstance()
        .getLogger()
        .warning("Player %s is not online, cannot teleport to home %s".formatted(home.getUuid(), home.getName()));
      return;
    }

    World world = Bukkit.getWorld(home.getWorld());
    if(world == null) {
      player.sendMessage(i18n.getMessage("world_not_found", home.getWorld()));
      return;
    }

    Location loc = new Location(
      world,
      home.getX(),
      home.getY(),
      home.getZ(),
      (float) home.getYaw(),
      (float) home.getPitch()
    );

    if(!loc.getChunk().isLoaded()) {
      loc.getChunk().load();
    }

    player.teleport(loc);
    player.sendMessage(i18n.getMessage("teleported", home.getName()));
  }
}
