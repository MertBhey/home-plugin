package me.mertbhey.HomeSystem.spigot;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.mertbhey.HomeSystem.common.data.HomeDatabase;
import me.mertbhey.HomeSystem.spigot.commands.CreateHomeCommand;
import me.mertbhey.HomeSystem.spigot.commands.DeleteHomeCommand;
import me.mertbhey.HomeSystem.spigot.commands.HomeCommand;
import me.mertbhey.HomeSystem.spigot.commands.HomeGUICommand;
import me.mertbhey.HomeSystem.spigot.events.onInventoryClick;

public class HomePlugin extends JavaPlugin {
  @Getter private static HomePlugin instance;

  @Override public void onEnable() {
    instance = this;

    Configuration.init();
    // Initializing it one time is enough. After that we can just use HomeDatabase#getInstance() to get the instance.
    new HomeDatabase(
      Configuration.getConfig().getDatabase().getHost(),
      Configuration.getConfig().getDatabase().getPort(),
      Configuration.getConfig().getDatabase().getDatabase(),
      Configuration.getConfig().getDatabase().getTable(),

      Configuration.getConfig().getDatabase().getUsername(),
      Configuration.getConfig().getDatabase().getPassword()
    );

    {
      CreateHomeCommand createHomeCommand = new CreateHomeCommand();
      getCommand("sethome").setExecutor(createHomeCommand);
    }

    {
      DeleteHomeCommand deleteHomeCommand = new DeleteHomeCommand();
      getCommand("delhome").setExecutor(deleteHomeCommand);
      getCommand("delhome").setTabCompleter(deleteHomeCommand);
    }

    {
      HomeCommand homeCommand = new HomeCommand();
      getCommand("home").setExecutor(homeCommand);
      getCommand("home").setTabCompleter(homeCommand);
    }

    {
      HomeGUICommand homeCommand = new HomeGUICommand();
      getCommand("homes").setExecutor(homeCommand);
    }

    {
      getServer()
        .getPluginManager()
        .registerEvents(new onInventoryClick(), this);
    }
  }
}
