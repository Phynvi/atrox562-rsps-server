package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;

public class test extends Dialogue {

	int npcId;
	private int shopId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		shopId = (Integer) parameters[1];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"NPC Dialougue is some nice stuff ehh?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendDialogue(SEND_4_OPTIONS,
					"HELL YEAHHHHH",
					"Options too??."
					,"Awsome Dialogue system brahh."
					,"I know right?."
					,"Jens is cool :p");
		}else if(stage == 0) {
			if(componentId == 1) {
			//	ShopsHandler.openShop(player, shopId);
				end();
			}else if (componentId == 2) {
			//	player.getBank().initSetPin();
				end();
			}else if (componentId == 3) {
				//TODO collection boss
				end();
			}else if (componentId == 4) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What is this place?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else
				end();
		}else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This is a branch of the Bank of "+Settings.SERVER_NAME+". We have",
					"branches in many towns."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 2) {
			stage = 3;
			sendDialogue(SEND_2_OPTIONS,
					"What would you like to say?"
					,"And what do you do?"
					,"Didnt you used to be called the Bank of Varrock?");
		}else if (stage == 3) {
			if(componentId == 1) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"And what do you do?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 5;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Didnt you used to be called the Bank of Varrock?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else
				end();
		}else if (stage == 4) {
			stage = -2;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"We will look after your items and money for you.",
					"Leave your valuables with us if you want to keep them",
					"safe."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 5) {
			stage = -2;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Yes we did, but people kept on coming into our",
					"signs were wrong. They acted as if we didn't know",
					"what town we were in or something."
					}, IS_NPC, npcId, 9827);
		}else
			end();
	}

	@Override
	public void finish() {
		
	}

}