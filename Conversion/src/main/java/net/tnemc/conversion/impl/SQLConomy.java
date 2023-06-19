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
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SQLConomy extends Converter {
  private File configFile = new File(TNECore.directory(), "../SQLConomy/config.yml");
  private YamlFile config = YamlFile.loadConfiguration(configFile, true);

  public SQLConomy() throws IOException {
  }

  @Override
  public String name() {
    return "SQLConomy";
  }

  @Override
  public String type() {
    return "mysql";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../SQLConomy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("sql_server").split("\\:")[0],
        3306, config.getString("sql_database"),
        config.getString("sql_user"), config.getString("sql_password"),
        "balances", "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT uuid, balance FROM balances;")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("uuid"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }
}