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

import java.math.BigDecimal;
import java.util.UUID;

/**
 * MobEntry
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class MobEntry {

  private final UUID player;
  private String type;
  private String weather;
  private boolean isBaby;
  private String weaponMaterial;
  private String aggressionType;
  private long time;
  private String timeOfDay;
  private boolean isOwned;
  private boolean isPlayerOwner;
  private BigDecimal reward;
  private String currency;

  public MobEntry(final String type, final UUID player) {
    this(type, player, "Clear", false, "Fist", "Passive", 900, "Morning", false, false);
  }

  public MobEntry(String type, final UUID player, String weather, boolean isBaby, String weaponMaterial, String aggressionType, long time, String timeOfDay, boolean isOwned, boolean isPlayerOwner) {
    this.weather = weather;
    this.type = type;
    this.player = player;
    this.isBaby = isBaby;
    this.weaponMaterial = weaponMaterial;
    this.aggressionType = aggressionType;
    this.time = time;
    this.timeOfDay = timeOfDay;
    this.isOwned = isOwned;
    this.isPlayerOwner = isPlayerOwner;
    this.reward = BigDecimal.ZERO;
    this.currency = "Default";
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public UUID getPlayer() {
    return player;
  }

  public String getWeather() {
    return weather;
  }

  public void setWeather(String weather) {
    this.weather = weather;
  }

  public boolean isBaby() {
    return isBaby;
  }

  public void setBaby(boolean baby) {
    isBaby = baby;
  }

  public String getWeaponMaterial() {
    return weaponMaterial;
  }

  public void setWeaponMaterial(String weaponMaterial) {
    this.weaponMaterial = weaponMaterial;
  }

  public String getAggressionType() {
    return aggressionType;
  }

  public void setAggressionType(String aggressionType) {
    this.aggressionType = aggressionType;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(String timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  public boolean isOwned() {
    return isOwned;
  }

  public void setOwned(boolean owned) {
    isOwned = owned;
  }

  public boolean isPlayerOwner() {
    return isPlayerOwner;
  }

  public void setPlayerOwner(boolean playerOwner) {
    isPlayerOwner = playerOwner;
  }

  public BigDecimal getReward() {
    return reward;
  }

  public void setReward(BigDecimal reward) {
    this.reward = reward;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}