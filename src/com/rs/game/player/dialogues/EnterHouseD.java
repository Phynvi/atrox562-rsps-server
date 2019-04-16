package com.rs.game.player.dialogues;

import com.rs.Settings;

public class EnterHouseD extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS,"What would you like to do?", "Go to your house.", "Go to your house (Building mode).",
				"Go to a friend's house.", "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
		case 1:
			if (player.hasHouse == true) {
				player.getHouse().setBuildMode(false);
				player.getHouse().enterMyHouse();
				end();
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You must purchase a house in order to do this, You can buy a property from any "
								+ Settings.SERVER_NAME + " Estate Agent.");
			}
			break;
		case 2:
			if (player.hasHouse == true) {
				player.getHouse().setBuildMode(true);
				player.getHouse().enterMyHouse();
				end();
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You must purchase a house in order to do this, You can buy a property from any "
								+ Settings.SERVER_NAME + " Estate Agent.");
			}
			break;
		case 3:
			end();
			player.getTemporaryAttributtes().put("teleto_house", true);
			player.getPackets().sendRunScript(109, "Enter Friends Name:");
			break;
		case 4:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}