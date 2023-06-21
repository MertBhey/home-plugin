package me.mertbhey.HomeSystem.spigot.managers;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import me.mertbhey.HomeSystem.common.structures.Home;
import me.mertbhey.HomeSystem.spigot.stores.GuiKeyStore;
import me.mertbhey.HomeSystem.spigot.stores.SkullTextureStore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;

public class GUIBuilder {
  private String title = "";
  private HumanEntity player;
  private ItemStack[] items;

  public GUIBuilder(int size) {
    items = new ItemStack[size];
  }

  /**
   * Put default home menu items to the GUI. May be used for other GUIs as well.
   */
  public GUIBuilder putDefaultHomeItems() {
    for(int i = items.length-9; i < items.length; i++) {
      items[i] = this.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", List.of(), GuiKeyStore.DUMMY);
    }

    items[items.length-5] = this.createHeadItem(SkullTextureStore.redX, i18n.getMessageWithoutPrefix("home_gui_delete_all"), List.of(), GuiKeyStore.DELETE);

    return this;
  }

  /**
   * Sets the player as the holder of the GUI.
   */
  public GUIBuilder setPlayer(HumanEntity player) {
    this.player = player;
    return this;
  }

  /**
   * Set the title of the GUI.
   */
  public GUIBuilder setTitle(String title) {
    this.title = ChatColor.translateAlternateColorCodes('&', title);
    return this;
  }

  /**
   * Adds the given homes to the GUI.
   */
  public GUIBuilder addHomes(List<Home> homes) {
    for(int i = 0; i < 45; i++) {
      if(i >= homes.size()) {
        break;
      }

      Home home = homes.get(i);

      items[i] = this.createItem(
        Material.PAPER,
        "§e%s".formatted(home.getName()),
        List.of(i18n.getMessageWithoutPrefix("home_gui_click_to_teleport")),
        GuiKeyStore.HOME
      );
    }

    return this;
  }

  /**
   * Used to create itemstacks without the pain.
   *
   * @param material Item material
   * @param name Display name
   * @param lore Item lore
   * @param data KeyStore data to be (primarily) used in inventory click events
   */
  private ItemStack createItem(Material material, String name, List<String> lore, GuiKeyStore data) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();

    meta.displayName(GUIBuilder.createComponent(name));
    meta.lore(lore.stream().map(GUIBuilder::createComponent).toList());

    if(data != null) {
      meta
        .getPersistentDataContainer()
        .set(GuiKeyStore.getNamespacedKey(), PersistentDataType.STRING, data.getKey());
    }

    item.setItemMeta(meta);
    return item;
  }

  private ItemStack createHeadItem(String texture, String name, List<String> lore, GuiKeyStore data) {
    ItemStack stack = this.createItem(Material.PLAYER_HEAD, name, lore, data);
    SkullMeta meta = (SkullMeta) stack.getItemMeta();

    PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), null);
    profile.setProperty(new ProfileProperty("textures", texture));
    meta.setPlayerProfile(profile);

    stack.setItemMeta(meta);
    return stack;
  }

  /**
   * Replace an item on the inventory
   */
  public GUIBuilder setItem(int slot, Material material, String name, List<String> lore, GuiKeyStore data) {
    items[slot] = this.createItem(material, name, lore, data);
    return this;
  }

  /**
   * Replace an item with a skull on the inventory
   */
  public GUIBuilder setHeadItem(int slot, String texture, String name, List<String> lore, GuiKeyStore data) {
    items[slot] = this.createHeadItem(texture, name, lore, data);
    return this;
  }


  /**
   * Creates a text component from the given string. Supports "&" color codes.
   */
  public static TextComponent createComponent(String content) {
    return Component.text(ChatColor.translateAlternateColorCodes('&', content));
  }

  public Inventory build() {
    Inventory inventory = Bukkit.createInventory(this.player, items.length, GUIBuilder.createComponent(this.title));
    inventory.setContents(this.items);
    return inventory;
  }
}
