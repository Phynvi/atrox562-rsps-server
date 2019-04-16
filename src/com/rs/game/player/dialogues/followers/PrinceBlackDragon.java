package com.rs.game.player.dialogues.followers;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;

public class PrinceBlackDragon extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Shouldn't a prince only have two heads?" }, IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Why is that?" }, IS_NPC, npcId, 9827);
		} else if(stage == 0) {
			stage = 1;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Well, a standard Black dragon has one, the King has three so inbetween must have two?" }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 1) {
			stage = 2;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "You're overthinking this." }, IS_NPC, npcId, 9827);
		} else if(stage == 2) {
            end();
		}
	}

	@Override
	public void finish() {
		
	}

}
