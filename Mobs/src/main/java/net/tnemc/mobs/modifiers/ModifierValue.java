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

import net.tnemc.core.account.holdings.modify.HoldingsOperation;

import java.math.BigDecimal;

/**
 * ModifierValue represents an entry value for a modifier. This could be a range, or a flat value.
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class ModifierValue {

  private final double min;
  private final double max;
  private final double value;

  private final HoldingsOperation operation;

  public ModifierValue(final String value) {

    String working = value;
    if(value.startsWith("-=")) {

      operation = HoldingsOperation.SUBTRACT;
      working = working.replaceAll("-=", "");

    } else if(value.startsWith("\\=")) {

      operation = HoldingsOperation.DIVIDE;
      working = working.replaceAll("\\=", "");

    } else if(value.startsWith("+=")) {

      operation = HoldingsOperation.ADD;
      working = working.replaceAll("\\+=", "");

    } else {

      operation = HoldingsOperation.MULTIPLY;

    }

    if(working.contains(",")) {

      final String[] rangeSplit = working.split(",");
      if(rangeSplit.length >= 2) {

        this.min = Double.parseDouble(rangeSplit[0]);
        this.max = Double.parseDouble(rangeSplit[1]);
        this.value = 0;
      } else {

        this.value = Double.parseDouble(rangeSplit[0]);
        this.min = 0;
        this.max = 0;
      }
    } else {

      this.value = Double.parseDouble(value);
      this.min = 0;
      this.max = 0;
    }
  }

  public double value() {
    if(value == 0.0) {
      //TODO: Random range.
    }
    return value;
  }

  public BigDecimal modify(final BigDecimal amount) {
    return operation.perform(amount, BigDecimal.valueOf(value()));
  }
}