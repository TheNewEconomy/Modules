package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.TNECore;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
public class Essentials extends Converter {
  private final File dataDirectory = new File(TNECore.directory(), "../Essentials/userdata");
  final File mysqlStorageFile = new File(TNECore.directory(), "../EssentialsMysqlStorage/config.yml");

  public Essentials() throws IOException {
    super("");
  }

  @Override
  public String name() {
    return "Essentials";
  }

  @Override
  public String type() {
    return (mysqlStorageFile.exists())? "mysql" : "yaml";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../Essentials/userdata");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {

    if(!mysqlStorageFile.exists()) return;

    try {
      final YamlFile config = YamlFile.loadConfiguration(mysqlStorageFile);

      final String table = config.getString("Database.Mysql.TableName");

      initialize(new TNEDataManager(type(), config.getString("Database.Mysql.Host"),
              config.getInt("Database.Mysql.Port"), config.getString("Database.Mysql.DatabaseName"),
              config.getString("Database.Mysql.User"), config.getString("Database.Mysql.Password"),
              table, "accounts.db",
              false, false, 60, false));
      open();

      try(Connection connection = db.getConnection();
          Statement statement = connection.createStatement();
          ResultSet results = statement.executeQuery("SELECT player_uuid, money, offline_money FROM " + table + ";")) {

        final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
        while(results.next()) {
          ConversionModule.convertedAdd(results.getString("player_uuid"),
                  TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getUid(),
                  BigDecimal.valueOf(results.getDouble("money")));
          ConversionModule.convertedAdd(results.getString("player_uuid"),
                  TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getUid(),
                  BigDecimal.valueOf(results.getDouble("offline_money")));
        }
      } catch(SQLException ignore) {}
      close();

    } catch (IOException e) {
      TNECore.log().error("Failed to convert EssentialsMysqlStorage Data!", DebugLevel.OFF);
    }

  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    if(!dataDirectory.isDirectory() || dataDirectory.listFiles() == null ) return;

    for(File accountFile : dataDirectory.listFiles()) {

      try {
        final YamlFile acc = YamlFile.loadConfiguration(accountFile);

        final String name = (acc.contains("lastAccountName"))? acc.getString("lastAccountName")
                : accountFile.getName().substring(0, accountFile.getName().lastIndexOf("."));

        final BigDecimal money = acc.contains("money")? new BigDecimal(acc.getString("money")) : BigDecimal.ZERO;
        final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));

        ConversionModule.convertedAdd(name, TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getUid(), money);

      } catch (IOException e) {
        TNECore.log().error("Failed to convert Essentials Account: " + accountFile.getName() + ".", DebugLevel.OFF);
      }
    }
  }
}