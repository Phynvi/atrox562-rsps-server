package com.rs.game.player.dialogues.followers;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.dialogues.Dialogue;

public class KalphitePrincess extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "What is it with your kind and potato cactus?" }, IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Truthfully?" }, IS_NPC, npcId, 9827);
		} else if(stage == 0) {
			stage = 1;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Yeah, please." }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 1) {
			stage = 2;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Soup. We make a fine soup with it." }, IS_NPC, npcId, 9827);
		} else if(stage == 2) {
			stage = 3;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Kalphites can cook?" }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 3) {
			stage = 4;
            sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Nah, we just collect it and put it there because we know fools like yourself will come ", "down looking for it then inevitably be killed by my mother." }, IS_NPC, npcId, 9827);
		} else if(stage == 4) {
			stage = 5;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Evidently not, that's how I got you!" }, IS_PLAYER, player.getIndex(), 9827);
		} else if(stage == 5) {
			stage = 6;
            sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
			NPCDefinitions.getNPCDefinitions(npcId).name, "Touché" }, IS_NPC, npcId, 9827);
		} else if(stage == 6) {
            end();
		}
	}

	@Override
	public void finish() {
		
	}

}
