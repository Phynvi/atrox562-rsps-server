package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;

public class KaramjaTrip extends Dialogue {
	
	private int npcId;
	public static final WorldTile KARAMJA = new WorldTile(2954, 3158, 0);

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Do you want to go on a trip to Karamja?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"The trip will cost you 30 coins." }, IS_NPC, npcId, 9827);
			stage = 0;
		} else if (stage == 0) {
			sendDialogue(SEND_2_OPTIONS, "Would you like to go?",
					"Yes please.", "No thank you.");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == 1) {
				if (player.getInventory().getNumerOf(995) >= 30){
				doTrip();
				} else {
				stage = 3;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Looks like you can't afford a trip to Karamja." }, IS_NPC, npcId, 9827);
				}
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "No thank you." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 3;
			}
		} else if (stage == 3) {
			end();
		}
	}

	@Override
	public void finish() {
		
	}
	
	public void doTrip() {
		stage = 3;
		player.getInventory().deleteItem(995, 30);
		player.setNextWorldTile(KARAMJA);
		sendDialogue(SEND_1_TEXT_INFO, "The ship arrived at Karamja.");
	}

}
