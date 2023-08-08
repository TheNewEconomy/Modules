package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
public class GemsEconomy extends Converter {

  public GemsEconomy() {
    super("../GemsEconomy/config.yml");
  }

  @Override
  public String name() {
    return "GemsEconomy";
  }

  @Override
  public String type() {
    return config.getString("storage").toLowerCase();
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../GemsEconomy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    /*initialize(new TNEDataManager(type(), config.getString("mysql.host"),
        config.getInt("mysql.port"), config.getString("mysql.database"),
        config.getString("mysql.username"), config.getString("mysql.password"),
        config.getString("mysql.tableprefix"), "accounts.db",
        false, false, 60, false));
    String table = config.getString("mysql.tableprefix") + "_balances";

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      while(results.next()) {
        Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
        final Optional<Currency> curOptional = TNECore.eco().currency().findCurrency(results.getString("currency_id"));
        if(curOptional.isPresent()) {
          cur = curOptional.get();
        }
        ConversionModule.convertedAdd(results.getString("account_id"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getUid(),
            BigDecimal.valueOf(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();*/
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    final File dataFile = new File(TNECore.directory(), "../GemsEconomy/data.yml");
    try {
      final YamlFile dataConfiguration = YamlFile.loadConfiguration(dataFile);

      final ConfigurationSection accountSection = dataConfiguration.getConfigurationSection("accounts");
      if(accountSection != null) {
        final Set<String> accounts = accountSection.getKeys(false);
        for(String uuid : accounts) {
          final ConfigurationSection balanceSection = accountSection.getConfigurationSection(uuid + ".balances");
          if(balanceSection != null) {
            final Set<String> currencies = balanceSection.getKeys(false);
            for(String currency : currencies) {
              Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
              final Optional<Currency> curOptional = TNECore.eco().currency().findCurrency(currency);
              if(curOptional.isPresent()) {
                cur = curOptional.get();
              }

              BigDecimal value = BigDecimal.ZERO;
              try {
                value = new BigDecimal(dataConfiguration.getDouble("accounts." + uuid + ".balances." + currency));
              } catch(Exception ignore) {
                System.out.println("Couldn't parse balance value for node: " + "accounts." + uuid + ".balances." + currency + ". This balance will have to be manually converted using /money give");
              }

              ConversionModule.convertedAdd(UUID.fromString(uuid), "", TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getUid(), value);
            }
          }
        }
      }
    } catch (IOException e) {
      TNECore.log().error("Failed to convert GemsEconomy.", e, DebugLevel.STANDARD);
    }
  }
}
