package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;

public class BuyShantayPass extends Dialogue {

	@Override
	public void start() {
		if (player.getInventory().containsItem(995, 5)) {
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {"", "You purchase a Shantay Pass."}, IS_ITEM, 1854, 9827);
			player.getInventory().addItem(1854, 1);
			player.getInventory().deleteItem(995, 5);
		} else {
			stage = 16;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
				"Shantay",
				"Sorry friend, the Shantay Pass is 5 gold coins. You",
				"don't seem to have enough money."
				}, IS_NPC, 836, 9827);
		}
	}

	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {

	}

}
