package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Skills;

public class LumbyGaurds extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hey there adventurer, How may I help you?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "I would like to pass through the gate please." },
					IS_PLAYER, player.getIndex(), 9827);
		}else if(stage == 0) {
                	stage = 1;
                	sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ofcourse you may, that will be 10gp, but if you", 
							"complete the quest Prince Ali Rescue you could",
							"pass for free, Would you like to pay 10gp to pass?."
							}, IS_NPC, npcId, 9827);
		}else if(stage == 100) {
end();
		}else if (stage == 1) {
			stage = 2;
			sendDialogue(SEND_2_OPTIONS,
					"What would you like to say?",
					"Yes please, I'll pay 10gp."
					,"No thanks.");
		} else if (stage == 2) {
			stage = 3;
			if(componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes please, I'll pay 10gp."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No thanks."
						}, IS_PLAYER, player.getIndex(), 9827); 
			} else if (stage == 4) {
				end();
			} else if(player.LumbyGate == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"You don't need to pay again."
						}, IS_NPC, npcId, 9827);
				}
				else{
                	sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Execellent, now you have unlimited access to the gate,",
							"here is a cape for your purchase. Thank you."
							}, IS_NPC, npcId, 9827);
					player.getInventory().deleteItem(995, 100000);
				player.getInventory().addItem(4413,1);
				player.LumbyGate = 2;
				}
			}else if (stage == 3) {
				stage = 100;
				if(!player.getInventory().containsItem(995, 10)) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Looks like you do not have 10 coins."
						}, IS_NPC, npcId, 9827);
				}
				else if(player.LumbyGate == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"You have just payed 10 coins."
						}, IS_NPC, npcId, 9827);
				}
				else if(player.LumbyGate == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"You don't need to pay again."
						}, IS_NPC, npcId, 9827);
				}
				else{
                	sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Thank you, please click on the gate."
							}, IS_NPC, npcId, 9827);
					player.getInventory().deleteItem(995, 10);
				player.LumbyGate = 1;
				}
				}else
				end();
		
	}

	@Override
	public void finish() {
		
	}

}
