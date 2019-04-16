package com.rs.game.player.dialogues;

import com.rs.game.World;

public class BondDialogue extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Claim bond?",
				"Yes, i would like to claim my rank.", "No, I would like to save it.");
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1 && !player.isDonator()) {
			player.getInventory().deleteItem(15268, 1);
			player.setDonator(true);
			World.sendWorldMessage("<col=ff6600><img=4>News: "+ player.getDisplayName() +" has just become a Donator.", false);
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
