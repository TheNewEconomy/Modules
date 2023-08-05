package net.tnemc.conversion.command;/*
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

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.utils.Extractor;

import java.util.Optional;

/**
 * ConvertCommand
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class ConvertCommand {

    public static void handle(CmdSource<?> sender, Converter converter) {

        if(converter == null) {
            sender.message(new MessageData( "Invalid converter specified!"));
            return;
        }

        TNECore.server().scheduler().createDelayedTask(()-> {
            converter.convert();
            sender.message(new MessageData( "Conversion has completed. Running restoration command."));
            Extractor.restore(0);
        }, new ChoreTime(0), ChoreExecution.SECONDARY);
        sender.message(new MessageData("Messages.Admin.Restoration"));
        sender.message(new MessageData( "Conversion is now in progress."));
    }
}