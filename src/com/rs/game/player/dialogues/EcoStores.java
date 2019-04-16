package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class EcoStores extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS, "Pick a Store",
				"Melee", "Ranger",
				"Mage", "Supplies");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			end();
			ShopsHandler.openShop(player, 179);
		} else if (componentId == 2) {
			end();
			ShopsHandler.openShop(player, 180);
		} else if (componentId == 3) {
			end();
			ShopsHandler.openShop(player, 181);
		} else if (componentId == 4) {
			end();
			ShopsHandler.openShop(player, 182);
		}
	}

	@Override
	public void finish() {

	}

}
