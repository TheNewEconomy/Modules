package net.tnemc.conversion;

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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.tnemc.plugincore.core.io.storage.SQLEngine;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;

import java.io.File;
import java.util.Map;

/**
 * ConfigurableSQLConnector
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class ConfigurableSQLConnector extends SQLConnector {

  protected final SQLEngine engine;
  protected final String poolName;
  protected final File dbFile;
  protected final String host;
  protected final int port;
  protected final String db;
  protected final String user;
  protected final String password;

  public ConfigurableSQLConnector(SQLEngine engine, String poolName, File dbFile, String host, int port, String db, String user, String password) {
    super();

    this.engine = engine;
    this.poolName = poolName;
    this.dbFile = dbFile;
    this.host = host;
    this.port = port;
    this.db = db;
    this.user = user;
    this.password = password;
  }

  @Override
  public void initialize() {
    findDriverSource(engine);

    final HikariConfig config = new HikariConfig();

    if(sourceClass != null) {
      config.setDataSourceClassName(sourceClass);
    } else {
      config.setDriverClassName(driverClass);
    }

    final String url = engine.url(dbFile.getAbsolutePath(), host, port, db);

    //String file, String host, int port, String database
    config.addDataSourceProperty("url", url);
    config.setJdbcUrl(url);
    config.setUsername(user);
    config.setPassword(password);

    config.setPoolName(poolName);
    config.setConnectionTestQuery("SELECT 1");

    for(Map.Entry<String, Object> entry : engine.properties().entrySet()) {
      config.addDataSourceProperty(entry.getKey(), entry.getValue());
    }

    this.source = new HikariDataSource(config);
  }

  @Override
  public boolean checkVersion() {
    return super.checkVersion();
  }
}