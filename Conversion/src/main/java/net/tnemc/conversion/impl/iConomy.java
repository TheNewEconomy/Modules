package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.TNECore;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.currency.Currency;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

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
public class iConomy extends Converter {
  private File configFile = new File(TNECore.directory(), "../iConomy/Config.yml");
  private YamlFile config = YamlFile.loadConfiguration(configFile, true);
  private String table = config.getString("System.Database.Settings.Table");

  @Override
  public String name() {
    return "iConomy";
  }

  @Override
  public String type() {
    return config.getString("System.Database.Type");
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../iConomy/Config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("System.Database.Settings.MySQL.Hostname"),
        config.getInt("System.Database.Settings.MySQL.Port"), config.getString("System.Database.Settings.Name"),
        config.getString("System.Database.Settings.MySQL.Username"), config.getString("System.Database.Settings.MySQL.Password"),
        config.getString("System.Database.Settings.Table"), "accounts.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT * FROM " + table + ";")) {

      final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));
      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("username"),
            TNECore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getIdentifier(),
            BigDecimal.valueOf(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    super.h2();
  }

  @Override
  public void postgre() throws InvalidDatabaseImport {
    super.postgre();
  }

  @Override
  public void flatfile() throws InvalidDatabaseImport {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File("plugins/iConomy/accounts.mini")));

      String line;
      while((line = reader.readLine()) != null) {
        String[] split = line.split(" ");
        Double money = Double.parseDouble(split[1].split(":")[1]);
        ConversionModule.convertedAdd(split[0].trim(), TNECore.server().defaultRegion(TNECore.eco().region().getMode()), TNE.manager().currencyManager().get(TNECore.server().defaultRegion(TNECore.eco().region().getMode())).name(), new BigDecimal(money));
      }
    } catch(Exception e) {
      TNE.instance().getLogger().log(Level.WARNING, "Unable to load iConomy Data.");
    }
  }

  @Override
  public void inventoryDB() throws InvalidDatabaseImport {
    super.inventoryDB();
  }

  @Override
  public void expDB() throws InvalidDatabaseImport {
    super.expDB();
  }
}