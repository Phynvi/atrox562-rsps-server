package com.rs.game.player.dialogues.followers;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;

public class KrakenPet extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "What's Kraken?" }, IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Not heard that one before." }, IS_NPC, npcId, 9827);
		} else if(stage == 0) {
			stage = 1;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "How are you actually walking on land?" }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 1) {
			stage = 2;
            sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "We have another leg, just below the center of our body that",
			"we use to move across solid surfaces." }, IS_NPC, npcId, 9827);
		} else if(stage == 2) {
            stage = 3;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "That's.... interesting." }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 3) {
            end();
		}
	}

	@Override
	public void finish() {
		
	}

}
