package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.ShopsHandler;

public class Aubury extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.runeMysteries == 3) {
		sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"My gratitude to you adventurer for bringing me these",
				"research notes. I notice that you brought the head",
				"wizard a special talisman that was the key to our finally",
				"unlocking the puzzle."
				}, IS_NPC, npcId, 9827);
		stage = 22;
		}
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Do you want to buy some runes?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1 && player.runeMysteries == 2) {
			stage = 0;
			sendDialogue(SEND_3_OPTIONS,
					"What would you like to say?",
					"Yes please!",
					"Oh, it's a rune shop. No thank you, then.",
					"I have been sent here with a package for you.");
		}else if(stage == 0) {
			if(componentId == 2) {
				ShopsHandler.openShop(player, 6);
				end();
			}else if (componentId == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Oh, it's a rune shop. No thank you, then."
						}, IS_PLAYER, player.getIndex(), 9827); 
				stage = 1;
			}else if (componentId == 4) {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I have been sent here with a package for you. It's from",
						"the head wizard at the Wizards' Tower."
						}, IS_PLAYER, player.getIndex(), 9827); 
				stage = 2;
			}else
				end();
		}else if (stage == 1) {
			stage = 20;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, if you find someone who does want runes, please",
					"send them my way."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Really? But... surely he can't have..? Please, let me",
					"have it, it must be extremely important for him to have",
					"sent a stranger."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 3) {
			sendDialogue(SEND_1_TEXT_CHAT, "", "You hand Aubury the research package." );
			stage = 4;
		}else if (stage == 4) {
			stage = 21;
			player.getInventory().deleteItem(290, 1);
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This... this is incredible. Please, give me a few moments",
					"to quickly look over this, and then talk to me again."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 20) {
			end();
		}else if(stage == 21) {
			end();
			player.runeMysteries = 3;
		}else if(stage == 22) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Combined with the information I had already collated",
					"regarding the Rune Essence, I think we have finally",
					"unlocked the power to"
					}, IS_NPC, npcId, 9827); 
			stage = 23;
		}else if(stage == 23) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"...no. I am getting ahead of myself. Please take this",
					"summary of my research back to the head wizard at",
					"the Wizards' Tower. I trust his judgement on whether",
					"to let you in on our little secret or not."
					}, IS_NPC, npcId, 9827); 
			stage = 24;
		}else if (stage == 24) {
			player.getInventory().addItem(291, 1);
			sendDialogue(SEND_1_TEXT_CHAT, "", "Aubury gives you his research notes." );
			stage = 25;
		}else if (stage == 25) {
			end();
			player.runeMysteries = 4;
	}
}

	@Override
	public void finish() {
		
	}

}
