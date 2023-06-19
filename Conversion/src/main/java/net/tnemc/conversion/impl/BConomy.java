package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.currency.Currency;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BConomy extends Converter {
  private final File configFile = new File(TNECore.directory(), "../BConomy/config.yml");
  private final YamlFile config = YamlFile.loadConfiguration(configFile, true);

  private final String table = "accounts";

  public BConomy() throws IOException {
  }

  @Override
  public String name() {
    return "BConomy";
  }

  @Override
  public String type() {
    return (config.getBoolean("Database.Mysql.Enabled"))? "MySQL" : "sqlite";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../BConomy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("Database.Mysql.Host"),
        config.getInt("Database.Mysql.Port"), config.getString("Database.Mysql.Database"),
        config.getString("Database.Mysql.User"), config.getString("Database.Mysql.Pass"),
        config.getString("Database.Mysql.Table"), "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("userid"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("Database.Mysql.Host"),
        config.getInt("Database.Mysql.Port"), config.getString("Database.Mysql.Database"),
        config.getString("Database.Mysql.User"), config.getString("Database.Mysql.Pass"),
        config.getString("Database.Mysql.Table"), "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("userid"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }
}