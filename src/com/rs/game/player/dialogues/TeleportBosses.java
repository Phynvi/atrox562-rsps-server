package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

public class TeleportBosses extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_5_OPTIONS, "Boss Teleports",
				"Corporeal Beast", "Tormented Demons", "God Wars", "King black dragon",
				"Page 2");
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
		if (componentId == 1) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2966, 4383, 0));
		} else if (componentId == 2) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2562, 5739, 0));
		} else if (componentId == 3) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2898, 3714, 0));
		} else if (componentId == 4) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3013, 3847, 0));
		} else if (componentId == 5) {
		stage = 2;
		sendDialogue(SEND_5_OPTIONS, "Boss Teleports",
				"Chaos elemental", "Dagannoth kings", "----", "----",
				"----");
		}
		} if (stage == 2) {
		if (componentId == 1) {
		end();
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3269, 3919, 0));
		} else if (componentId == 2) {
		end();
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2900, 4447, 0));
		} else if (componentId == 3) {
		end();
		} else if (componentId == 4) {
		end();
		} else if (componentId == 5) {
		end();
		}
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}
