package com.rs.game.player.dialogues;

import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.cache.loaders.NPCDefinitions;

public class Baraek extends Dialogue {

	int npcId;
	
	/**
	 * Represents the fur item.
	 */
	private static final Item FUR = new Item(6814);
	
	/**
	 * Represents the coins item.
	 */
	private static final Item COINS = new Item(995, 20);
	
	/**
	 * Represents the buy coins item.
	 */
	private static final Item BUY_COINS = new Item(995, 12);
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.getInventory().containsItem(FUR)) {
		stage = 0;
		sendDialogue(SEND_3_OPTIONS, "Select an Option",
					"Can you sell me some furs?",
					"Hello. I am in search of a quest.",
					"Would you like to buy my fur?");
		} else {
		stage = 11;
		sendDialogue(SEND_2_OPTIONS, "Select an Option",
					"Can you sell me some furs?",
					"Hello. I am in search of a quest.");
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == 0) {
			if(componentId == 2) {
			stage = 1;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Can you sell me some furs?" }, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 3) {
			end();
			}else if (componentId == 4) {
			stage = 5;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Would you like to buy my fur?" }, IS_PLAYER, player.getIndex(), 9827);
		
			}
		}else if(stage == 1) {
		stage = 2;
        sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "Yeah, sure. They're 20 gold coins each." }, IS_NPC, npcId, 9827);
		
		}else if(stage == 2) {
		stage = 3;
		sendDialogue(SEND_2_OPTIONS, "Select an Option",
					"Yeah, okay, here you go.",
					"20 gold coins? That's an outrade!");
		
		} else if(stage == 3) {
			if(componentId == 1) {
			stage = 4;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Yeah, okay, here you go." }, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 2) {
			stage = 100;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "20 gold coins? That's an outrade!" }, IS_PLAYER, player.getIndex(), 9827);
		
			}
		}else if(stage == 4) {
		if (player.getInventory().containsItem(995, 20)) {
			player.getInventory().deleteItem(995, 20);
				if (!player.getInventory().addItem(FUR)) {
					World.addGroundItem(FUR, player, player, true, 180, true);
				}
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {"", "Baraeck sells you a fur."}, IS_ITEM, 6814, 9827);
				stage = 100;
			} else {
				stage = 100;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Oops, I dont' seem to have enough coins." }, IS_PLAYER, player.getIndex(), 9827);
			}
			
		}else if(stage == 5) {
		stage = 6;
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "Let's have a look at it." }, IS_NPC, npcId, 9827);
		
		}else if(stage == 6) {
		stage = 7;
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {"", "You hand Baraeck your fur to look at."}, IS_ITEM, 6814, 9827);
		
		}else if(stage == 7) {
		stage = 8;
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name, "It's not in the best condition. I guess I could give you",
		"12 coins for it." }, IS_NPC, npcId, 9827);
		
		}else if(stage == 8) {
		stage = 9;
		sendDialogue(SEND_2_OPTIONS, "Select an Option",
					"Yeah, that'll do.",
					"I think I'll keep hold of it actually.");
					
		} else if(stage == 9) {
			if(componentId == 1) {
			stage = 10;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Yeah, that'll do." }, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 2) {
			stage = 100;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "I think I'll keep hold of it actually." }, IS_PLAYER, player.getIndex(), 9827);
		
			}
		
		}else if(stage == 10) {
		if (player.getInventory().containsItem(6814, 1)) {
			player.getInventory().deleteItem(6814, 1);
				if (!player.getInventory().addItem(BUY_COINS)) {
					World.addGroundItem(BUY_COINS, player, player, true, 180, true);
				}
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Thanks!" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
			}
		
		} else if(stage == 11) {
			if(componentId == 1) {
			stage = 1;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Can you sell me some furs?" }, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 2) {
			end();
			}
		
		}else if(stage == 100) {
		end();
		}
	}

	@Override
	public void finish() {
		
	}

}
