package me.mertbhey.HomeSystem.spigot.events;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.mertbhey.HomeSystem.API.HomeAPI;
import me.mertbhey.HomeSystem.common.structures.Home;
import me.mertbhey.HomeSystem.spigot.managers.GUIBuilder;
import me.mertbhey.HomeSystem.spigot.managers.HomeManager;
import me.mertbhey.HomeSystem.spigot.managers.i18n;
import me.mertbhey.HomeSystem.spigot.stores.GuiKeyStore;
import me.mertbhey.HomeSystem.spigot.stores.SkullTextureStore;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class onInventoryClick implements Listener {
  @EventHandler public void onClick(InventoryClickEvent event) {
    ItemStack item = event.getCurrentItem();
    if(item == null || item.getItemMeta() == null) {
      return;
    }

    HumanEntity player = event.getWhoClicked();
    PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
    if(!container.has(GuiKeyStore.getNamespacedKey())) return;

    String type = container.get(GuiKeyStore.getNamespacedKey(), PersistentDataType.STRING);
    switch(type) {
      case "home" -> {
        event.getClickedInventory().close();
        String homeName = i18n.stripColor(PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName()));
        Optional<Home> cacheValue = HomeManager.getHome(player.getUniqueId(), homeName);
        if(!cacheValue.isPresent()) {
          player.sendMessage(i18n.getMessage("home_not_found", homeName));
          return;
        }

        Home home = cacheValue.get();
        HomeManager.teleportPlayerTo(home);
      }

      case "delete" -> {
        event.getClickedInventory().close();

        Inventory builder = new GUIBuilder(27)
          .setPlayer(player)
          .setTitle(i18n.getMessageWithoutPrefix("delete_all_gui_title"))
          .setHeadItem(12, SkullTextureStore.limeCheck, i18n.getMessageWithoutPrefix("delete_all_confirm"), List.of(), GuiKeyStore.CONFIRM)
          .setHeadItem(14, SkullTextureStore.redX, i18n.getMessageWithoutPrefix("delete_all_cancel"), List.of(), GuiKeyStore.CANCEL)
          .build();

        player.openInventory(builder);
      }

      case "deletion_confirm" -> {
        event.getClickedInventory().close();
        HomeAPI.deleteHomes(player.getUniqueId());
        HomeManager.deleteCache(player.getUniqueId());

        player.sendMessage(i18n.getMessage("delete_all_success"));
      }

      case "deletion_cancel" -> {
        event.getClickedInventory().close();
      }

      default -> {
        event.setCancelled(true);
      }
    }
  }
}
