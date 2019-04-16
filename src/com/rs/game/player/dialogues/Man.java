package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;

public class Man extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Hello, how's it going?" },
					IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Not to bad thanks."
							}, IS_NPC, npcId, 9827);
		} else if(stage == 0) {
                	end();
		}
	}

	@Override
	public void finish() {
		
	}

}
