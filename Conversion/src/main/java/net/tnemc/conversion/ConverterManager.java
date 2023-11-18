package net.tnemc.conversion;/*
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

import net.tnemc.conversion.impl.CMI;
import net.tnemc.conversion.impl.Essentials;
import net.tnemc.conversion.impl.GemsEconomy;
import net.tnemc.conversion.impl.XConomy;
import net.tnemc.core.module.ModuleInfo;

import java.util.*;

/**
 * ConverterManager
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class ConverterManager {


    private final Map<String, Converter> converters = new HashMap<>();

    public ConverterManager() {
        add(new CMI());
        add(new Essentials());
        add(new GemsEconomy());
        add(new XConomy());
    }

    public Map<String, Converter> getConverters() {
        return converters;
    }

    public void add(Converter converter) {
        converters.put(converter.name().toLowerCase(), converter);
    }

    public List<String> search() {
        List<String> found = new ArrayList<>();

        Iterator<Map.Entry<String, Converter>> it = converters.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, Converter> entry = it.next();

            if(entry.getValue().dataFolder() != null) {
                if(entry.getValue().dataFolder().exists()) found.add(entry.getKey());
            }
        }
        return found;
    }

    public Optional<Converter> find(String name) {
        return Optional.of(converters.get(name.toLowerCase()));
    }
}