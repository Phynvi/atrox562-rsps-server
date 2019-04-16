package com.rs.game.player.dialogues;

public class SecurityOfStronghold extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Pick your reward",
				"Fancy Boots.", "Fighting Boots.");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
				player.getInventory().addItem(9005, 1);
				end();
			} else if (componentId == 2) {
				player.getInventory().addItem(9006, 1);
				end();
			}
		 else
			end();
	}

	@Override
	public void finish() {

	}

}
