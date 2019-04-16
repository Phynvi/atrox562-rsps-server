package com.rs.game.player.dialogues;

import com.rs.utils.Utils;
import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;

public class RangedTutorClaim extends Dialogue {

	int npcId;
	private long tutorDelayTime;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.getTutorDelay() > 1) {
			stage = 0;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I work with the Magic Combat tutor to give out",
					"consumable items that you may need for combat such",
					"as arrows and runes. However we have had some",
					"cheeky people try to take both!"
					}, IS_NPC, npcId, 9827);
	} else if (player.getTutorDelay() < 1) {
			stage = 1;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Nemarti gives you a Training shortbow and 25 arrows.",
							"They can only be used together"
							}, IS_ITEM, 9705, 9827);
			player.setTutorDelay(tutorDelayTime + 1800000); // 30mins
			player.getInventory().addItem(9705, 1);
			player.getInventory().addItem(9706, 25);
	}
}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I work with the Magic Combat tutor to give out",
					"consumable items that you may need for combat such",
					"as arrows and runes. However we have had some",
					"cheeky people try to take both!"
					}, IS_NPC, npcId, 9827); 
		} else if(stage == 0) {
                	stage = 1;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"So, every half an hour, you may come back and claim",
					"either arrows OR runes, but not both. Come back in a",
					"while for runes, or simply make your own."
					}, IS_NPC, npcId, 9827); 
		} else if(stage == 1) {
                	end();
		}
	}

	@Override
	public void finish() {
		
	}

}
