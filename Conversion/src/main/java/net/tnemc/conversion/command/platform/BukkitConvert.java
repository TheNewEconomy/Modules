package net.tnemc.conversion.command.platform;/*
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

import net.tnemc.bukkit.impl.BukkitCMDSource;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.command.ConvertCommand;
import net.tnemc.libs.lamp.commands.annotation.Description;
import net.tnemc.libs.lamp.commands.annotation.Subcommand;
import net.tnemc.libs.lamp.commands.annotation.Usage;
import net.tnemc.libs.lamp.commands.bukkit.BukkitCommandActor;
import net.tnemc.libs.lamp.commands.bukkit.annotation.CommandPermission;
import net.tnemc.libs.lamp.commands.orphan.OrphanCommand;

/**
 * Bukkit
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class BukkitConvert implements OrphanCommand {

    @Subcommand({"convert"})
    @Usage("/tne convert")
    @Description("Used to convert from other economy plugins.")
    @CommandPermission("tne.admin.convert")
    public void convert(BukkitCommandActor sender, Converter converter) {
        ConvertCommand.handle(new BukkitCMDSource(sender), converter);
    }
}