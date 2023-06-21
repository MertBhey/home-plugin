package me.mertbhey.HomeSystem.API;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.mertbhey.HomeSystem.common.data.HomeDatabase;
import me.mertbhey.HomeSystem.common.structures.Home;

public class HomeAPI {
  static {
    if(HomeDatabase.getInstance() == null) {
      throw new Error("Home plugin is not active.");
    }
  }

  /**
   * Creates a new home.
   * @param home Home to be created.
   * @return True if home is created successfully, false otherwise.
   */
  public static boolean createHome(Home home) {
    try {
      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("INSERT INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      statement.setString(1, home.getUuid().toString());
      statement.setString(2, home.getName());
      statement.setString(3, home.getWorld());
      statement.setDouble(4, home.getX());
      statement.setDouble(5, home.getY());
      statement.setDouble(6, home.getZ());
      statement.setDouble(7, home.getYaw());
      statement.setDouble(8, home.getPitch());
      statement.executeUpdate();

      return true;
    } catch(SQLException error) {
      error.printStackTrace();
      return false;
    }
  }

  /**
   * Returns a list of homes of a player.
   * @param uuid UUID of the player.
   * @return List of homes of the player.
   */
  public static List<Home> getHomes(UUID uuid) {
    try {
      List<Home> homes = new ArrayList<>();

      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("SELECT * FROM homes WHERE uuid = ?");
      statement.setString(1, uuid.toString());
      statement.executeQuery();

      while(statement.getResultSet().next()) {
        homes.add(new Home(statement.getResultSet()));
      }

      return homes;
    } catch(SQLException error) {
      error.printStackTrace();
      return null;
    }
  }

  /**
   * Returns a home of a player by home name.
   * @param id UUID of the player.
   * @param name Name of the home.
   * @return Home object or null if home does not exist.
   */
  public static Home getHome(UUID id, String name) {
    try {
      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("SELECT * FROM homes WHERE uuid = ? AND name = ?");
      statement.setString(1, id.toString());
      statement.setString(2, name);
      statement.executeQuery();

      if(statement.getResultSet().next()) {
        return new Home(statement.getResultSet());
      }

      return null;
    } catch(SQLException error) {
      error.printStackTrace();
      return null;
    }
  }

  /**
   * Finds a home of a player by home id.
   * @param uuid UUID of the player.
   * @param id Id of the home.
   * @return Home object or null if home does not exist.
   */
  public static Home getHome(UUID uuid, int id) {
    try {
      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("SELECT * FROM homes WHERE uuid = ? AND id = ?");
      statement.setString(1, uuid.toString());
      statement.setInt(2, id);
      statement.executeQuery();

      if(statement.getResultSet().next()) {
        return new Home(statement.getResultSet());
      }

      return null;
    } catch(SQLException error) {
      error.printStackTrace();
      return null;
    }
  }

  /**
   * Deletes a home.
   * @param id UUID of the player.
   * @param name Name of the home.
   * @return True if homes are deleted successfully, false otherwise.
   */
  public static boolean deleteHome(UUID id, String name) {
    try {
      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("DELETE FROM homes WHERE uuid = ? AND name = ?");
      statement.setString(1, id.toString());
      statement.setString(2, name);
      statement.executeUpdate();

      return true;
    } catch(SQLException error) {
      error.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes a home.
   * @param id UUID of the player.
   * @param name Name of the home.
   * @return True if homes are deleted successfully, false otherwise.
   */
  public static boolean deleteHome(UUID uuid, int id) {
    try {
      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("DELETE FROM homes WHERE uuid = ? AND id = ?");
      statement.setString(1, uuid.toString());
      statement.setInt(2, id);
      statement.executeUpdate();

      return true;
    } catch(SQLException error) {
      error.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes all homes of a player.
   * @param uuid UUID of the player.
   * @return True if homes are deleted successfully, false otherwise.
   */
  public static boolean deleteHomes(UUID uuid) {
    try {
      PreparedStatement statement = HomeDatabase.getInstance().getConnection().prepareStatement("DELETE FROM homes WHERE uuid = ?");
      statement.setString(1, uuid.toString());
      statement.executeUpdate();

      return true;
    } catch(SQLException error) {
      error.printStackTrace();
      return false;
    }
  }
}
