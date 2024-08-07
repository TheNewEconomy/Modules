package net.tnemc.conversion.impl;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

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

public class Essentials extends Converter {

  private final File dataDirectory = new File(PluginCore.directory(), "../Essentials/userdata");
  final File mysqlStorageFile = new File(PluginCore.directory(), "../EssentialsMysqlStorage/config.yml");

  public Essentials() {
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
    return new File(PluginCore.directory(), "../Essentials/userdata");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {

    if(!mysqlStorageFile.exists()) return;

    /*try {
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

        final Currency currency = TNECore.eco().currency().getDefaultCurrency(PluginCore.server().defaultRegion(TNECore.eco().region().getMode()));
        while(results.next()) {
          ConversionModule.convertedAdd(results.getString("player_uuid"),
                  PluginCore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getUid(),
                  BigDecimal.valueOf(results.getDouble("money")));
          ConversionModule.convertedAdd(results.getString("player_uuid"),
                  PluginCore.server().defaultRegion(TNECore.eco().region().getMode()), currency.getUid(),
                  BigDecimal.valueOf(results.getDouble("offline_money")));
        }
      } catch(SQLException ignore) {}
      close();

    } catch (IOException e) {
      TNECore.log().error("Failed to convert EssentialsMysqlStorage Data!", DebugLevel.OFF);
    }*/

  }

  @Override
  public void yaml() throws InvalidDatabaseImport {

    if(!dataDirectory.isDirectory() || dataDirectory.listFiles() == null ) return;

    for(File accountFile : dataDirectory.listFiles()) {

      try {
        final YamlDocument acc = YamlDocument.create(accountFile);

        if(!acc.contains("last-account-name")) {
          PluginCore.log().inform("Skipping account of file: " + accountFile.getName().substring(0, accountFile.getName().lastIndexOf(".")) + ". Invalid format.");
          continue;
        }

        //because essentials is essentials
        final String name = acc.getString("last-account-name").replaceAll("town_", "town-").replaceAll("nation_", "nation-");

        final BigDecimal money = acc.contains("money")? new BigDecimal(acc.getString("money")) : BigDecimal.ZERO;
        final Currency currency = TNECore.eco().currency().getDefaultCurrency(TNECore.eco().region().defaultRegion());

        ConversionModule.convertedAdd(UUID.fromString(accountFile.getName().substring(0, accountFile.getName().lastIndexOf("."))), name, TNECore.eco().region().defaultRegion(), currency.getUid(), money);

      } catch (IOException e) {
        PluginCore.log().error("Failed to convert Essentials Account: " + accountFile.getName() + ".", DebugLevel.OFF);
      }
    }
  }
}