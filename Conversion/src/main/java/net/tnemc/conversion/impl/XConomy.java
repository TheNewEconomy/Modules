package net.tnemc.conversion.impl;

import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataCon;
import net.tnemc.conversion.ConfigurableSQLConnector;
import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.storage.SQLEngine;
import net.tnemc.core.io.storage.connect.SQLConnector;
import net.tnemc.core.io.storage.engine.sql.MySQL;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
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
public class XConomy extends Converter {

  public XConomy() {
    super("../XConomy/database.yml");
  }

  @Override
  public String name() {
    return "XConomy";
  }

  @Override
  public String type() {
    final String type = config.getString("Settings.storage-type").toLowerCase();
    if(type.equalsIgnoreCase("maria")) return "mysql";
    return type;
  }

  @Override
  public File dataFolder() {
    return new File(TNECore.directory(), "../XConomy/");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    final Currency cur = TNECore.eco().currency().getDefaultCurrency(TNECore.server().defaultRegion(TNECore.eco().region().getMode()));

    final SQLEngine engine = new MySQL();
    final SQLConnector connector = new ConfigurableSQLConnector(engine, "TNEConvert", new File(TNECore.directory(), "../XConomy/" + config.getString("SQLite.path")),
            config.getString("MySQL.host"), config.getInt("MySQL.port"), config.getString("MySQL.database"), config.getString("MySQL.user"), config.getString("MySQL.pass"));

    connector.initialize();
    try(ResultSet results = connector.executeQuery("SELECT UID,player,balance FROM xconomy;", new Object[]{})) {

      while(results.next()) {
        ConversionModule.convertedAdd(UUID.fromString(results.getString("UID")), results.getString("player"), TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getUid(), new BigDecimal(results.getString("balance")));
      }

    } catch(Exception ignore) {

    }

    try(ResultSet results = connector.executeQuery("SELECT account,UUID,balance FROM xconomynon;", new Object[]{})) {

      while(results.next()) {

        final String name = results.getString("account");

        ConversionModule.convertedAdd(UUID.nameUUIDFromBytes(("NonPlayer:" + name).getBytes(StandardCharsets.UTF_8)), name, TNECore.server().defaultRegion(TNECore.eco().region().getMode()), cur.getUid(), new BigDecimal(results.getString("balance")));
      }

    } catch(Exception ignore) {

    }
  }
}
