package me.mertbhey.HomeSystem.common.structures;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import lombok.Getter;

public class Home {
  @Getter private int id;
  @Getter private UUID uuid;
  @Getter private String name;
  @Getter private String world;
  @Getter private double x;
  @Getter private double y;
  @Getter private double z;
  @Getter private double yaw;
  @Getter private double pitch;

  public Home(ResultSet result) throws SQLException {
    this.id = result.getInt("id");
    this.uuid = UUID.fromString(result.getString("uuid"));
    this.name = result.getString("name");
    this.world = result.getString("world");
    this.x = result.getDouble("x");
    this.y = result.getDouble("y");
    this.z = result.getDouble("z");
    this.yaw = result.getDouble("yaw");
    this.pitch = result.getDouble("pitch");
  }

  public Home(UUID uuid, String name, String world, double x, double y, double z, double yaw, double pitch) {
    this.uuid = uuid;
    this.name = name;
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }
}
