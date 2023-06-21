package me.mertbhey.HomeSystem.spigot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;

public class Configuration {
  @Getter private static Config config;
  @Getter private static JsonObject messages;

  public static void init() {
    Configuration.copyFiles();

    config = Configuration.loadFile("config.json", new TypeToken<Config>() {}.getType());
    messages = Configuration.loadFile("messages.json", new TypeToken<JsonObject>() {}.getType());
  }

  private static void copyFiles() {
    HomePlugin.getInstance().saveResource("config.json", false);
    HomePlugin.getInstance().saveResource("messages.json", false);
  }

  /**
   * Load a file from the plugin's data folder and parse it with gson using the given type.
   */
  private static <T> T loadFile(String name, Type type) {
    File file = new File(HomePlugin.getInstance().getDataFolder(), name);
    if(!file.exists()) {
      HomePlugin.getInstance().getLogger().warning("File %s does not exist.".formatted(name));
      return null;
    }

    try {
      InputStreamReader reader = new InputStreamReader(new FileInputStream(file));

      return new Gson().fromJson(reader, type);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public class Config {
    @Getter private Database database;

    public class Database {
      @Getter private String host;
      @Getter private int port;
      @Getter private String database;
      @Getter private String table;
      @Getter private String username;
      @Getter private String password;
    }
  }
}
