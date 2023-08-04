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

import net.tnemc.core.TNECore;
import net.tnemc.core.io.storage.StorageManager;
import net.tnemc.core.module.Module;
import net.tnemc.core.module.ModuleInfo;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.implementation.snakeyaml.lib.Yaml;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.orphan.OrphanCommand;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * ConversionModule
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
@ModuleInfo(
    name = "Conversion",
    author = "creatorfromhell",
    version = "1.2.0",
    updateURL = "https://tnemc.net/files/module-version.xml"
)
public class ConversionModule implements Module {

  @Override
  public void enable(TNECore tneCore) {

  }

  @Override
  public void disable(TNECore tneCore) {

  }

  @Override
  public void initConfigurations(File file) {

  }

  @Override
  public void backup(StorageManager storageManager) {

  }

  @Override
  public void reset(StorageManager storageManager) {

  }

  @Override
  public void enableSave(StorageManager storageManager) {

  }

  @Override
  public void registerCommands(CommandHandler commandHandler) {

  }

  @Override
  public List<OrphanCommand> registerMoneySub() {
    return null;
  }

  @Override
  public List<OrphanCommand> registerTransactionSub() {
    return null;
  }

  @Override
  public List<OrphanCommand> registerAdminSub() {
    return null;
  }

  public static void convertedAdd(String identifier, String world, UUID currency, BigDecimal amount) {
    final File conversionFile = new File(TNECore.directory(), "extracted.yml");
    final YamlFile conversion;
    try {
      conversion = YamlFile.loadConfiguration(conversionFile);
    } catch(IOException e) {
      TNECore.log().error("Error attempting to load extracted.yml during conversion");
      return;
    }

    if(!conversion.contains("Accounts")) {

      conversion.createSection("Accounts");
      try {
        conversion.save(conversionFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }


    BigDecimal starting = BigDecimal.ZERO;

    final String newID = identifier.replaceAll("\\.", "!").replaceAll("\\-", "@").replaceAll("\\_", "%");

    if(conversion.contains("Accounts." + newID + ".Balances." + world + "." + currency)) {
      starting = new BigDecimal(conversion.getString("Accounts." + newID + ".Balances." + world + "." + currency));
    }

    conversion.set("Accounts." + newID + ".Balances." + world + "." + currency, starting.add(amount).toPlainString());
    try {
      conversion.save();
    } catch (IOException e) {
      TNECore.log().error("Error attempting to save extracted.yml during conversion");
    }
  }
}