package com.rs.game.player.dialogues.followers;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;

public class ChaosElePet extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Is it true a level 3 skiller caught one of your siblings?" }, IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Yes, they killed my mummy, kidnapped my brother, smiled about", "it and went to sleep." }, IS_NPC, npcId, 9827);
		} else if(stage == 0) {
			stage = 1;
            sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { player.getDisplayName(), "Aww, well you have me now! I shall call you Squishy and you shall be mine and you shall be my", "Squishy" }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 1) {
            stage = 2;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Come on, Squishy come on, little Squishy!" }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 2) {
            end();
		}
	}

	@Override
	public void finish() {
		
	}

}
