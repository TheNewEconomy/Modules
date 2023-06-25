package net.tnemc.signs.handler;/*
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
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsHandler;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.calculations.CalculationData;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.utils.Identifier;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * SafeHandler
 *
 * @author creatorfromhell
 * @since 0.1.0.0
 */
public class SafeHandler implements HoldingsHandler {
  @Override
  public Identifier identifier() {
    return new Identifier("tne", "SAFE_HOLDINGS");
  }

  @Override
  public boolean supports(CurrencyType currencyType) {
    return currencyType.supportsItems();
  }

  @Override
  public boolean setHoldings(Account account, String region, Currency currency, CurrencyType currencyType, BigDecimal amount) {
    account.getWallet().setHoldings(new HoldingsEntry(region, currency.getUid(), amount, identifier()));

    if(account.isPlayer() && TNECore.server().online(account.getIdentifier())) {
      final CalculationData<Object> data = new CalculationData<>((ItemCurrency)currency,
                                                                 ((PlayerAccount)account).getPlayer()
                                                                     .get().inventory().getInventory(false),
                                                                 ((PlayerAccount)account).getUUID());
      TNECore.server().itemCalculations().setItems(data, amount);
      return true;
    }
    return false;
  }

  @Override
  public HoldingsEntry getHoldings(Account account, String region, Currency currency, CurrencyType currencyType) {
    if((currency instanceof ItemCurrency)) {
      if(!account.isPlayer() || !TNECore.server().online(account.getIdentifier()) ||
          TNECore.eco().account().getLoading().contains(((PlayerAccount)account).getUUID().toString())) {

        //Offline players have their balances saved to their wallet so check it.
        final Optional<HoldingsEntry> holdings = account.getWallet().getHoldings(region,
                                                                                 currency.getUid(),
                                                                                 identifier()
        );
        TNECore.log().debug("Getting holdings from Signs DB", DebugLevel.DEVELOPER);

        if(holdings.isPresent()) {
          return holdings.get();
        }
        return new HoldingsEntry(region,
                                 currency.getUid(),
                                 BigDecimal.ZERO,
                                 identifier());
      }
      TNECore.log().debug("Getting holdings from Safe Chests", DebugLevel.DEVELOPER);
      final CalculationData<Object> data = new CalculationData<>((ItemCurrency)currency,
                                                                 ((PlayerAccount)account).getPlayer()
                                                                     .get().inventory().getInventory(false),
                                                                 ((PlayerAccount)account).getUUID());

      return new HoldingsEntry(region, currency.getUid(),
                               TNECore.server().itemCalculations().calculateHoldings(data), identifier());
    }
    //not item currency? then return zero... should never happen.
    return new HoldingsEntry(region,
                             currency.getUid(),
                             BigDecimal.ZERO,
                             identifier());
  }
}
