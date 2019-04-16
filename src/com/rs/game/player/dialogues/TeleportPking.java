package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class TeleportPking extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "PvP Teleports",
				"Green dragons", "Demonic ruins", "", "",
				"");
	}

	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
		player.getControlerManager().startControler("Wilderness");
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2977, 3600, 0));
		} else if (componentId == 2) {
		player.getControlerManager().startControler("Wilderness");
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3288, 3886, 0));
		} else if (componentId == 3) {
		player.getControlerManager().startControler("Wilderness");
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2898, 3714, 0));
		} else if (componentId == 4) {
		player.getControlerManager().startControler("Wilderness");
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3007, 3848, 0));
		} else if (componentId == 5) {
		player.getControlerManager().startControler("Wilderness");
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3269, 3919, 0));
		} else {
			end();
	}
	}

	@Override
	public void finish() {

	}
}
