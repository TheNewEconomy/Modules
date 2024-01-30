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

import net.tnemc.conversion.command.platform.BukkitConvert;
import net.tnemc.conversion.command.platform.SpongeConvert;
import net.tnemc.conversion.command.resolvers.ConverterResolver;
import net.tnemc.conversion.command.resolvers.ConverterSuggestion;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.io.storage.StorageManager;
import net.tnemc.core.module.Module;
import net.tnemc.core.module.ModuleInfo;
import net.tnemc.libs.lamp.commands.CommandHandler;
import net.tnemc.libs.lamp.commands.orphan.OrphanCommand;
import net.tnemc.libs.lamp.commands.orphan.Orphans;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    version = "1.2.0"
)
//https://tnemc.net/files/module-version.xml
public class ConversionModule implements Module {

  private final ConverterManager manager;

  private static ConversionModule instance;

  public ConversionModule() {
    this.manager = new ConverterManager();
    instance = this;
  }

  @Override
  public void enable(TNECore core) {
    TNECore.log().inform("Enabled conversion module!", DebugLevel.OFF);
  }

  @Override
  public void disable(TNECore core) {

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
  public void registerCommands(CommandHandler command) {
    command.getAutoCompleter().registerParameterSuggestions(Converter.class, new ConverterSuggestion());
    command.registerValueResolver(Converter.class, new ConverterResolver());
  }

  @Override
  public List<OrphanCommand> registerMoneySub() {
    return new ArrayList<>();
  }

  @Override
  public List<OrphanCommand> registerTransactionSub() {
    return new ArrayList<>();
  }

  @Override
  public List<OrphanCommand> registerAdminSub() {

    switch (TNECore.server().name().toLowerCase()) {
      case "sponge" -> TNECore.instance().command().register(Orphans.path("tne").handler(new SpongeConvert()));
      default -> TNECore.instance().command().register(Orphans.path("tne").handler(new BukkitConvert()));
    }

    return new ArrayList<>();
  }

  public static void convertedAdd(final UUID id, String name, String world, UUID currency, BigDecimal amount) {
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

    final String newID = name.replaceAll("\\.", "!").replaceAll("\\-", "@").replaceAll("\\_", "%");

    if(!conversion.contains("Accounts." + newID + ".id")) {
      conversion.set("Accounts." + newID + ".id", id.toString());
    }

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

  public static ConversionModule instance() {
    return instance;
  }

  public ConverterManager getManager() {
    return manager;
  }
}