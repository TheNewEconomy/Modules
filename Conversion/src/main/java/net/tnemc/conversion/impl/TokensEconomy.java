package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import org.bukkit.configuration.ConfigurationSection;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
public class TokensEconomy extends Converter {
  private File configFile = new File(TNECore.directory(), "../TokensEconomy/players.yml");
  private YamlFile config = YamlFile.loadConfiguration(configFile, true);

  public TokensEconomy() throws IOException {
  }

  @Override
  public String name() {
    return "TokensEconomy";
  }

  @Override
  public String type() {
    return "yaml";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../TokensEconomy/players.yml");
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {

    final ConfigurationSection accountSection = config.getConfigurationSection("Players");
    if(accountSection != null) {
      final Set<String> accounts = accountSection.getKeys(false);
      for(String uuid : accounts) {
        final Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
        ConversionModule.convertedAdd(uuid, TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getIdentifier(), new BigDecimal(config.getString("Players." + uuid)));
      }
    }
  }
}
