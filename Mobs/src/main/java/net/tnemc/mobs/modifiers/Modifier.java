package net.tnemc.mobs.modifiers;

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

import net.tnemc.mobs.manager.MobEntry;

import java.math.BigDecimal;

/**
 * Modifier represents a modifier for an entity kill that may increase, or decrease the value of the kill.
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public interface Modifier {

  /**
   * Used to apply the modifier
   * @param entry The {@link MobEntry entry} that we're using to apply this modifier.
   * @return The modifier amount that should be applied to this entry.
   */
  BigDecimal apply(final MobEntry entry);
}