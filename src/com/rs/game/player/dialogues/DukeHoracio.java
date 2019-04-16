package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.LendingManager;

public class DukeHoracio extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Greetings. Welcome to my castle."
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_OPTIONS,
					"What would you like to say?",
					"Have you any quests for me?",
					"Where can I find money?");
		}else if(stage == 0) {
			if(componentId == 1) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Have you any quests for me?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 9;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Where can I find money?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else
				end();
		}else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well it's not really a quest but I recently discovered",
					"this strange talisman."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"It seems to be mystical and I have never seen anything",
					"like it before. Would you take it to the head wizard at"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 3) {
				stage = 4;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"the Wizard's Tower for me? It's just south-west of here",
					"and should not take you very long at all. I would be",
					"awfully grateful."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 4) {
			stage = 5;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Sure, no problem.",
					"Not right now.");
		}else if(stage == 5) {
			if(componentId == 1) {
				stage = 6;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Sure, no problem."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Not right now."
						}, IS_PLAYER, player.getIndex(), 9827); 
				end();
			}else
			end();
		}else if (stage == 6) {
				stage = 7;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Thank you very much, stranger. I am sure the head",
					"wizard will reward you for such an interesting find."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 7) {
			stage = 8;
			sendDialogue(SEND_1_TEXT_CHAT, "", "The Duke hands you an <col=8A0808>air talisman." );
		}else if (stage == 8) {
			end();
			player.getPackets().sendConfig(63, 1);
			player.getInventory().addItem(1438, 1);
			player.runeMysteries = 1;
		}else if (stage == 9) {
				stage = 10;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I've heard that the blacksmiths are prosperous amongst",
					"the peasantry. Maybe you could try your hand at",
					"that?"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 10) {
				end();
		}
}

	@Override
	public void finish() {
		
	}

}
