package me.mertbhey.HomeSystem.spigot.stores;

import org.bukkit.NamespacedKey;

import lombok.Getter;

/**
 * GuiKeyStore, used to retrieve gui item keys easily.
 */
public enum GuiKeyStore {
  DUMMY("dummy"),
  HOME("home"),
  DELETE("delete"),

  CONFIRM("deletion_confirm"),
  CANCEL("deletion_cancel");

  @Getter private String key;

  private GuiKeyStore(String key) {
    this.key = key;
  }

  public static NamespacedKey getNamespacedKey() {
    return new NamespacedKey("homesystem", "type");
  }
}
