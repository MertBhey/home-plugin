package me.mertbhey.HomeSystem.common.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import lombok.Getter;

public class HomeDatabase {
  @Getter private static HomeDatabase instance;
  @Getter private Connection connection;

  public HomeDatabase(String host, int port, String database, String tableName, String username, String password) {
    instance = this;

    try {
      connection = DriverManager.getConnection(
          "jdbc:mysql://%s:%s/%s?autoReconnect=true".formatted(host, port, database),
          username,
          password);
      this.createTable(tableName);
    } catch (SQLException error) {
      Logger.getLogger("Database").warning("Error while connecting to database: %s".formatted(error.getMessage()));
    }
  }

  /**
   ** Indexing UUID to make it faster to find the homes of a player, if database fills up.
   */
  private boolean createTable(String tableName) {
    try {
      PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `%s` (`id` INT AUTO_INCREMENT PRIMARY KEY, `uuid` VARCHAR(255) NOT NULL, `name` VARCHAR(255) NOT NULL, `world` VARCHAR(255) NOT NULL, `x` DOUBLE NOT NULL, `y` DOUBLE NOT NULL, `z` DOUBLE NOT NULL, `yaw` DOUBLE NOT NULL, `pitch` DOUBLE NOT NULL, INDEX `idx_uuid` (`uuid`))".formatted(tableName));
      statement.executeUpdate();
      statement.close();

      return true;
    } catch (SQLException error) {
      Logger.getLogger("Database").warning("Error while creating table: %s".formatted(error.getMessage()));
      return false;
    }
  }

  /**
   * Disconnect from the database.
   * @return true if disconnected (now or before), false if not.
   */
  public boolean disconnected() {
    try {
      if(connection == null || connection.isClosed()) return true;

      connection.close();
      return true;
    } catch (SQLException error) {
      Logger.getLogger("Database").warning("Error while disconnecting from database: %s".formatted(error.getMessage()));
      return false;
    }
  }

}
