package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.currency.Currency;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 5/11/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class GMoney extends Converter {
  private File saveFile = new File(TNECore.directory(), "../gMoney/money.yml");
  private FileConfiguration save = YamlConfiguration.loadConfiguration(saveFile);

  private File configFile = new File(TNECore.directory(), "../gMoney/config.yml");
  private YamlFile config = YamlFile.loadConfiguration(configFile, true);
  @Override
  public String name() {
    return "gMoney";
  }

  @Override
  public String type() {
    return (config.getBoolean("MySQL.Enable"))? "mysql" : "yaml";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../gMoney/config.yml");
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {

    final ConfigurationSection accountSection = save.getConfigurationSection("money");
    if(accountSection != null) {
      final Set<String> accounts = accountSection.getKeys(false);
      for(String uuid : accounts) {
        final Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
        ConversionModule.convertedAdd(uuid, TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getIdentifier(), new BigDecimal(save.getString("money." + uuid)));
      }
    }
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("MySQL.Ip"),
        3306, config.getString("MySQL.DataBase"),
        config.getString("MySQL.User"), config.getString("MySQL.Password"),
        config.getString("MySQL.Table"), "accounts.db",
        false, false, 60, false));

    final String table = config.getString("MySQL.Table");

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("player"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("money")));
      }
    } catch(SQLException ignore) {}
    close();
  }
}