package com.rs.utils;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;

public final class EconomyPrices {

	public static int getPrice(int itemId) {
		Item item = new Item(itemId);
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		
		if (defs.isNoted())
			itemId = defs.getCertId();
		if (!ItemConstants.isTradeable(new Item(itemId, 1)))
			return 0;
		if (itemId == 995) // TODO after here
			return 1;
			switch(itemId){
			case 1115:
				return 560;
			
			
			}
		return defs.getValue(); // TODO get price from real item from saved
									
	}

	private EconomyPrices() {

	}
}
