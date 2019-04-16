package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;
import com.rs.cache.loaders.NPCDefinitions;

public class TeleportTraining extends Dialogue {

	@Override
	public void start() {
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(462).name,
						"Hello, I can teleport you all around "+ Settings.SERVER_NAME +",",
						" would you like to?" }, IS_NPC, 462, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_5_OPTIONS, "Where would you like to go?", "Chickens <col=db0000>(cb: 1)",
					"Rock crabs <col=db0000>(cb: 13)", "Yaks <col=db0000>(cb: 22)", "Kalphites <col=db0000>(cb: 28)", "More Options");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3234, 3293, 0));
			else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2673, 3713, 0));
			else if (componentId == 3)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2325, 3795, 0));
			else if (componentId == 4)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3231, 3108, 0));
			else if (componentId == 5) {
				stage = 3;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Tzhaar <col=db0000>(cb: 103, 149)", "Cave horror <col=db0000>(cb: 80)", "----",
						"---", "More Options");
			}
		} else if (stage == 3) {
			if (componentId == 1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2480, 5168, 0));
			} else if (componentId == 2)
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3745, 9374, 0));
			else if (componentId == 3)
				end();
			else if (componentId == 4)
				end();
			else if (componentId == 5) {
				stage = 4;
				sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
						"Magic Bank.", "Multi Area. (PvP)", "Fight Pits.",
						"Wests(PvP)", "More Options");
			}
	}
}

	@Override
	public void finish() {

	}
}
