package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConfigurableSQLConnector;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.storage.SQLEngine;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import net.tnemc.plugincore.core.io.storage.engine.sql.MySQL;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/24/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CMI extends Converter {

  public CMI() {
    super("../CMI/dataBaseInfo.yml");
  }

  @Override
  public String name() {
    return "CMI";
  }

  @Override
  public String type() {
    return config.getString("storage.method").toLowerCase();
  }

  @Override
  public File dataFolder() {
    return new File(PluginCore.directory(), "../CMI/dataBaseInfo.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {

    final Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.eco().region().defaultRegion());

    final String prefix = config.getString("mysql.tablePrefix");
    final String table = prefix + "users";
    final String[] workHost = config.getString("mysql.hostname").split(":");

    final SQLEngine engine = new MySQL();
    final SQLConnector connector = new ConfigurableSQLConnector(engine, "TNEConvert", new File(PluginCore.directory(), "../CMI/cmi.sqlite.db"),
            workHost[0], Integer.valueOf(workHost[1]), config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password"));

    connector.initialize();
    try(ResultSet results = connector.executeQuery("SELECT username, Balance FROM " + table + ";", new Object[]{})) {

      while(results.next()) {
        ConversionModule.convertedAdd(UUID.fromString(results.getString("UID")), results.getString("player"), TNECore.eco().region().defaultRegion(), cur.getUid(), new BigDecimal(results.getString("balance")));
      }

    } catch(Exception ignore) {

    }
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    try {

      Class.forName("org.sqlite.JDBC");

      try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + new File(PluginCore.directory(), "../CMI/cmi.sqlite.db").getAbsolutePath());
          Statement statement = connection.createStatement();
          ResultSet results = statement.executeQuery("SELECT username, Balance FROM users;")) {

        final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.eco().region().defaultRegion());
        while(results.next()) {
          ConversionModule.convertedAdd(UUID.randomUUID(), results.getString("username"),
              TNECore.eco().region().defaultRegion(), currency.getUid(),
              BigDecimal.valueOf(results.getDouble("Balance")));
        }
      } catch(SQLException ignore) {}

    } catch(Exception ignore) {}
  }
}
