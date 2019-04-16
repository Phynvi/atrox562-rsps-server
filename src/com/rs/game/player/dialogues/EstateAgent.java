package com.rs.game.player.dialogues;

import com.rs.game.item.Item;
import com.rs.cache.loaders.NPCDefinitions;

public class EstateAgent extends Dialogue {

	private int npcId = 4247;

	@Override
	public void start() {
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Good day, " + player.getDisplayName() + ", I'm the Estate Agent. My job is to",
					"sell properties, May I interest you in a property?"
					}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			if (!player.hasHouse) {
				sendDialogue(SEND_2_OPTIONS,"Would you like to buy<br>a property?", "Yes please", "No thank you.");
				stage = 1;
			} else {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Can I interest you in a change of scenery?",
					"It'll cost you a bit for the move, but it's well worth it."
					}, IS_NPC, npcId, 9827);
				stage = 8;
			}
			break;
		case 1:
			switch (componentId) {
			case 1:
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I can sell you a lovely house, which you'd",
					"be able to access from any house portal around the",
					"world for a cheap price!"
					}, IS_NPC, npcId, 9827);
				stage = 2;
				break;
			default:
				end();
				break;
			}
			break;
		case 2:
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"It sounds amazing!",
						"How much exactly does it cost?"
						}, IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
			break;
		case 3:
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This house only costs 100,000 gold coins.",
					"It's a bargain if you ask me!"
					}, IS_NPC, npcId, 9827);
			stage = 4;
			break;
		case 4:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"So would you like to buy it now or later?"
					}, IS_NPC, npcId, 9827);
			stage = 5;
			break;
		case 5:
			sendDialogue(SEND_2_OPTIONS,"Buy a property for 100k?", "I'd like to buy it now please.",
					"I'll think about it and come back later.");
			stage = 6;
			break;
		case 6:
			switch (componentId) {
			case 1:
				if (player.getInventory().getNumberOf(995) >= 100000) {
					sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"It's been a pleasure doing business Sir!",
					"To access your house, simply find a house portal around the world."
					}, IS_NPC, npcId, 9827);
					player.getInventory().deleteItem(new Item(995, 100000));
					player.hasHouse = true;
					player.spokeToAgent = true;
					player.setHouseStyle(0);
					stage = 7;
				} else {
					sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I'm sorry, it looks like you don't have the funds",
					"in your inventory, please come back when you do."
					}, IS_NPC, npcId, 9827);
					stage = 7;
				}
				break;
			default:
				end();
				break;
			}
			break;
		case 7:
			end();
			break;
		case 8:
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"That sounds amazing!",
						"How much would a 'change in scenery' cost?"
						}, IS_PLAYER, player.getIndex(), 9827);
			stage = 11;
			break;
		case 11:
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"That depends very on the type of environment you desire.",
					"Which of the following draws your interest?"
					}, IS_NPC, npcId, 9827);
			stage = 12;
			break;
		case 12:
			sendDialogue(SEND_3_OPTIONS,"Which theme would you like?", "Basic timber", "Basic stone", "White washed stone");
			stage = 13;
			break;
		case 13:
			switch (componentId) {
			case 2:
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This is the default environment, chances are, you already",
					"have this selected. It'll cost 50,000 gold coins to purchase."
					}, IS_NPC, npcId, 9827);
				player.getTemporaryAttributtes().put("HouseEnvironment", 0);
				player.getTemporaryAttributtes().put("HouseCost", 50000);
				stage = 15;
				break;
			case 3:
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This environment is slightly more expensive to purchase,",
					"it'll cost you 200,000 gold coins."
					}, IS_NPC, npcId, 9827);
				player.getTemporaryAttributtes().put("HouseEnvironment", 1);
				player.getTemporaryAttributtes().put("HouseCost", 200000);
				stage = 15;
				break;
			case 4:
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This environment is set in the desert.",
					"It'll cost you 350,000 gold coins."
					}, IS_NPC, npcId, 9827);
				player.getTemporaryAttributtes().put("HouseEnvironment", 2);
				player.getTemporaryAttributtes().put("HouseCost", 350000);
				stage = 15;
				break;
			}
			break;
		case 15:
			sendDialogue(SEND_2_OPTIONS,"Are you sure?", "Yes.", "Maybe another time.");
			stage = 16;
			break;
		case 16:
			switch (componentId) {
			case 1:
				final Integer selection = (Integer) player.getTemporaryAttributtes().remove("HouseEnvironment");
				final Integer value = (Integer) player.getTemporaryAttributtes().remove("HouseCost");
				if (selection == null || value == null) {
					end();
					return;
				}
				if (player.getInventory().getNumberOf(995) >= value) {
					player.getInventory().deleteItem(new Item(995, value));
					player.setHouseStyle(selection);
					player.getHouse().changeLook(player);
				} else
					player.sendMessage("You don't have enough coins in your inventory for this purchase.");
				end();
				break;
			default:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}
}