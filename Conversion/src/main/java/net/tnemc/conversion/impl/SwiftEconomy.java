package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.currency.Currency;
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
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SwiftEconomy extends Converter {
  private File configFile = new File(TNECore.directory(), "../SwiftEconomy/config.yml");
  private YamlFile config = YamlFile.loadConfiguration(configFile, true);

  public SwiftEconomy() throws IOException {
  }

  @Override
  public String name() {
    return "SwiftEconomy";
  }

  @Override
  public String type() {
    return (config.getBoolean("MySQL.Enabled"))? "mysql" : "yaml";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../SwiftEconomy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("MySQL.Ip"),
        3306, config.getString("MySQL.Database"),
        config.getString("MySQL.Username"), config.getString("MySQL.Password"),
        "SWIFTeco", "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT uuid, money FROM SWIFTeco;")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("uuid"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("money")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    File configFile = new File(TNECore.directory(), "../SwiftEconomy/players.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    final Set<String> accounts = config.getKeys(false);
    for(String uuid : accounts) {
      if(uuid.equalsIgnoreCase("mysql")) continue;
      final Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      ConversionModule.convertedAdd(uuid, TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getIdentifier(), new BigDecimal(config.getString(uuid + ".Money")));
    }
  }
}