package net.tnemc.mobs;/*
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
import net.tnemc.libs.lamp.commands.CommandHandler;
import net.tnemc.libs.lamp.commands.orphan.OrphanCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MobsModule
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
@ModuleInfo(
        name = "Mobs",
        author = "creatorfromhell",
        version = "1.2.0"
)
//https://tnemc.net/files/module-version.xml
public class MobsModule implements Module {

  public static final List<String> passive = new ArrayList<>();
  public static final List<String> neutral = new ArrayList<>();
  public static final List<String> hostile = new ArrayList<>();

  public void enableListeners() {
    switch (TNECore.server().name().toLowerCase()) {
      case "sponge":
        break;
      default:
        break;
    }
  }

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
    return new ArrayList<>();
  }

  @Override
  public List<OrphanCommand> registerTransactionSub() {
    return new ArrayList<>();
  }

  @Override
  public List<OrphanCommand> registerAdminSub() {
    return new ArrayList<>();
  }
}