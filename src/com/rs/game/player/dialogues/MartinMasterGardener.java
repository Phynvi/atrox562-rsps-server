package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;

public class MartinMasterGardener extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Skillcape of Farming"
					,"Quest.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			if(componentId == 1) {
				stage = 0;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What is that cape that you're wearing?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 100;
				player.getPackets().sendGameMessage("This quest is not added.");
			}
		} else if (stage == 0) {
			stage = 100;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This is a Skillcape of Farming, isn't it incredible? It's a",
					"symbol of my ability as the finest farmer in the land!"
					}, IS_NPC, npcId, 9827);
		} else if(stage == 100) {
                	end();
		}
	}

	@Override
	public void finish() {
		
	}

}
