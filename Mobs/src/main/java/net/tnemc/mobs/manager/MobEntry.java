package net.tnemc.mobs.manager;

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
 * MobEntry
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class MobEntry {

  private String type = "";

  private boolean tamed = false;
  private boolean baby = false;
  private String owner = "";


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isTamed() {
    return tamed;
  }

  public void setTamed(boolean tamed) {
    this.tamed = tamed;
  }

  public boolean isBaby() {
    return baby;
  }

  public void setBaby(boolean baby) {
    this.baby = baby;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}