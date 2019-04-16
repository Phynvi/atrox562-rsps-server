package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class TeleportMinigame extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Minigame Teleports",
				"Duel Arena", "Pest Control", "Barrows", "Warrior Guild",
				"<col=db0000>Not added");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3370, 3267, 0));
		} else if (componentId == 2) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2662, 2649, 0));
		} else if (componentId == 3) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3565, 3290, 0));
		} else if (componentId == 4) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2881, 3546, 0));
		} else if (componentId == 5) {
		end();
		//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3259, 3266, 0));
		} else {
			end();
	}
	}

	@Override
	public void finish() {

	}
}
