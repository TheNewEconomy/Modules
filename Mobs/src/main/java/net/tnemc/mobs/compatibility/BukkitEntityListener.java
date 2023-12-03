package net.tnemc.mobs.compatibility;

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

import net.tnemc.mobs.MobsModule;
import net.tnemc.mobs.manager.MobEntry;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

/**
 * BukkitEntityListener
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class BukkitEntityListener implements Listener {

  @EventHandler
  public void onMobKill(EntityDeathEvent event) {
    LivingEntity mob = event.getEntity();
    Player player = mob.getKiller();

    if(player == null) {
      return;
    }

    final MobEntry entry = new MobEntry();

    final String weather = (mob.getWorld().isClearWeather())? "Clear" : "Storm";
    final String mobType = mob.getType().name().toLowerCase().replace("_", "");
    final boolean isBaby = mob instanceof Ageable && !((Ageable) mob).isAdult();
    final String weaponMaterial = getWeaponMaterial(player);
    final String aggressionType = getAggressionType(mobType);
    final String timeOfDay = getTimeOfDay(mob.getWorld().getTime());
    final boolean isOwned = mob instanceof Tameable && ((Tameable) mob).isTamed();
    final boolean isPlayerOwner = isOwned && player.getUniqueId().equals(((Tameable) mob).getOwner().getUniqueId());
  }

  private String getWeaponMaterial(Player player) {
    if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
      return "FIST";
    } else {
      return player.getInventory().getItemInMainHand().getType().name();
    }
  }

  private String getAggressionType(final String name) {
    if(MobsModule.passive.contains(name)) {
      return "Passive";
    } else if(MobsModule.hostile.contains(name)) {
      return "Hostile";
    } else {
      return "Neutral";
    }
  }

  private String getTimeOfDay(long time) {
    if(time >= 0 && time < 3000) {
      return "Night";
    } else if(time < 6000) {
      return "Sunrise";
    } else if(time < 12000) {
      return "Morning";
    } else if(time < 13000) {
      return "Midday";
    } else if(time < 18000) {
      return "Afternoon";
    } else if(time < 19000) {
      return "Sunset";
    } else {
      return "Evening";
    }
  }
}