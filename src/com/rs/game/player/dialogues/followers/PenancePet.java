package com.rs.game.player.dialogues.followers;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;

public class PenancePet extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { player.getDisplayName(), "Of all the high gamble rewards I could have won, ", "I won you..." }, IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Keep trying, human. You'll never win that ", "Dragon Chainbody." }, IS_NPC, npcId, 9827);
		} else if(stage == 0) {
            end();
		}
	}

	@Override
	public void finish() {
		
	}

}
