package com.rs.game.player.dialogues;

import com.rs.utils.ShopsHandler;

public class PkerShops extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS, "Pick a Store",
				"Melee", "Ranger",
				"Mage", "Misc");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			end();
			ShopsHandler.openShop(player, 175);
		} else if (componentId == 2) {
			end();
			ShopsHandler.openShop(player, 176);
		} else if (componentId == 3) {
			end();
			ShopsHandler.openShop(player, 177);
		} else if (componentId == 4) {
			end();
			ShopsHandler.openShop(player, 178);
		}
	}

	@Override
	public void finish() {

	}

}
