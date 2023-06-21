package me.mertbhey.HomeSystem.spigot.managers;

import java.util.regex.Pattern;

import com.google.gson.JsonObject;

import me.mertbhey.HomeSystem.spigot.Configuration;
import net.md_5.bungee.api.ChatColor;

public class i18n {
  private static JsonObject messages = Configuration.getMessages();
  private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

  /**
   * Get a message from the messages.json file, add the prefix and translate color codes.
   */
  public static String getMessage(String key, Object... args) {
    String prefix = messages.get("prefix").getAsString();
    String message = messages.get(key).getAsString().formatted(args);

    return ChatColor.translateAlternateColorCodes('&', prefix + message);
  }

  /**
   * Get a message from the messages.json file and translate color codes.
   */
  public static String getMessageWithoutPrefix(String key, Object... args) {
    String message = messages.get(key).getAsString().formatted(args);

    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static String stripColor(String message) {
    return COLOR_CODE_PATTERN.matcher(message).replaceAll("");
  }
}
