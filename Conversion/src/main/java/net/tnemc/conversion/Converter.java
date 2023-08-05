package net.tnemc.conversion;

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
 * ConversionModule
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public abstract class Converter {
  protected final File configFile;
  protected YamlFile config = null;

  //TODO: Initialize a storage manager instance for conversion.

  public Converter(@NotNull final String configLocation) {
    if(!configLocation.trim().equalsIgnoreCase("")) {
      configFile = new File(TNECore.directory(), configLocation);
      if(configFile.exists()) {
        try {
          config = YamlFile.loadConfiguration(configFile, true);
        } catch (IOException e) {
          TNECore.log().error("Error while attempting to load conversion config.", e, DebugLevel.STANDARD);
        }
      }
    } else {
      configFile = null;
    }
  }

  public Map<String, Object> hikariProperties(String format) {
    Map<String, Object> properties = new HashMap<>();

    if(format.equalsIgnoreCase("mysql")) {
      properties.put("autoReconnect", true);
      properties.put("cachePrepStmts", true);
      properties.put("prepStmtCacheSize", 250);
      properties.put("prepStmtCacheSqlLimit", 2048);
      properties.put("rewriteBatchedStatements", true);
      properties.put("useServerPrepStmts", true);
      properties.put("cacheResultSetMetadata", true);
      properties.put("cacheServerConfiguration", true);
      properties.put("useSSL", false);
    }

    return properties;
  }

  public abstract String name();

  public abstract String type();

  public abstract File dataFolder();

  public void convert() {
    try {
      new File(TNECore.directory(), "extracted.yml").createNewFile();
    } catch(Exception e) {
      TNECore.log().error("Something went wrong with conversion.", e, DebugLevel.STANDARD);
    }
    try {
      switch (type().toLowerCase()) {
        case "mysql" -> mysql();
        case "sqlite" -> sqlite();
        case "h2" -> h2();
        case "postgre" -> postgre();
        case "flatfile", "mini" -> flatfile();
        case "json" -> json();
        case "yaml" -> yaml();
        case "inventory" -> inventoryDB();
        case "experience" -> expDB();
      }
    } catch(InvalidDatabaseImport exception) {
      TNECore.log().error("Something went wrong with conversion.", exception, DebugLevel.STANDARD);
    }
  }

  public void mysql() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("MySQL", name());
  }

  public void sqlite() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("SQLite", name());
  }

  public void h2() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("H2", name());
  }

  public void postgre() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("PostgreSQL", name());
  }

  public void flatfile() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("FlatFile", name());
  }

  public void yaml() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("YAML", name());
  }

  //This is the dumbest trend ever.
  public void json() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("JSON", name());
  }

  //iConomy Specific
  public void inventoryDB() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("InventoryDB", name());
  }

  //iConomy Specific.
  public void expDB() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("ExperienceDB", name());
  }
}