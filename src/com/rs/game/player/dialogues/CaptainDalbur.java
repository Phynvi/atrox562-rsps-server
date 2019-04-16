package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;

public class CaptainDalbur extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Where can you take me?" },
					IS_PLAYER, player.getIndex(), 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
                	stage = 1;
                	sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Glough has ordered that I only take gnomes on Gnome",
							"Air."
							}, IS_NPC, npcId, 9827);
		}else if (stage == 1) {
			stage = 2;
			sendDialogue(SEND_3_OPTIONS,
					"Select an Option",
					"What's Gnome Air?",
					"How much for one-way to Varrock?"
					,"I'll leave you to it.");
		} else if (stage == 2) {
			if(componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What's Gnome Air?"
						}, IS_PLAYER, player.getIndex(), 9827); 
						stage = 6;
			}else if (componentId == 3) {
				stage = 3;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"How much for one-way to Varrock?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 4) {
				stage = 100;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I'll leave you to it."
						}, IS_PLAYER, player.getIndex(), 9827); 
		} else if (stage == 100) {
				end();
			}
				} else if (stage == 3) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I can't take you anywhere."
							}, IS_NPC, npcId, 9827);
				} else if (stage == 4) {
				stage = 5;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"How come?"
						}, IS_PLAYER, player.getIndex(), 9827); 
				} else if (stage == 5) {
				stage = 1;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I would, but Glough has told me not to take humans on",
							"Gnome Air."
							}, IS_NPC, npcId, 9827);
				} else if (stage == 6) {
				stage = 7;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Gnome Air is the finest airline in "+ Settings.SERVER_NAME +"!"
							}, IS_NPC, npcId, 9827);
				} else if (stage == 7) {
				stage = 8;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Well...it's the only real airline in "+ Settings.SERVER_NAME +"."
							}, IS_NPC, npcId, 9827);
				} else if (stage == 8) {
				stage = 9;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ha!"
							}, IS_NPC, npcId, 9827);
				} else if (stage == 9) {
				stage = 10;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What do you mean by real airline?"
						}, IS_PLAYER, player.getIndex(), 9827); 
				} else if (stage == 10) {
				stage = 1;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Well, there's a dodgy magic carpet operation in the",
							"desert, I doubt it will ever take off... Ha!"
							}, IS_NPC, npcId, 9827);
				}
		}

	@Override
	public void finish() {
		
	}

}
