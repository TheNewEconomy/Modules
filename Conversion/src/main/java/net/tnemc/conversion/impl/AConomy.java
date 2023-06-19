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
import java.util.Set;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Converter
 *
 * @author creatorfromhell
 * @since 1.2.0.0
 */
public class AConomy extends Converter {
  private File configFile = new File(TNECore.directory(), "../AConomy/config.yml");
  private YamlFile config = YamlFile.loadConfiguration(configFile, true);
  private File dataFile = new File(TNECore.directory(), "../AConomy/playerdata.yml");
  private YamlFile data = YamlFile.loadConfiguration(dataFile, true);

  public AConomy() throws IOException {
  }

  @Override
  public String name() {
    return "AConomy";
  }

  @Override
  public String type() {
    return (config.getBoolean("Mysql.Enabled"))? "mysql" : "yaml";
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../AConomy/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("Mysql.Host"),
        config.getInt("Mysql.Port"), config.getString("Mysql.Database"),
        config.getString("Mysql.Username"), config.getString("Mysql.Password"),
        "", "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT `Name`, `Balance`  FROM `Economy`;")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("Name"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("Balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void yaml() throws InvalidDatabaseImport {
    Set<String> keys = data.getKeys(false);

    final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));

    for(String id : keys) {
      if(id.toLowerCase().contains("aconomy")) continue;


      ConversionModule.convertedAdd(id,
          TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
          BigDecimal.valueOf(data.getDouble(id)));
    }
  }
}