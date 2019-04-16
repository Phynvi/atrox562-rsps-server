package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.game.player.content.quests.CooksAssistant;
import com.rs.cache.loaders.NPCDefinitions;

public class LumbyCook extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.cooksAssistant == 0) {
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"What am I to do?"
				}, IS_NPC, npcId, 9827);
	} else if (player.cooksAssistant == 1) {
		stage = 16;
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Did you find the ingredients?"
				}, IS_NPC, npcId, 9827);
	} else if (player.cooksAssistant == 2) {
		stage = 17;
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Thank you so much for doing this."
				}, IS_NPC, npcId, 9827);
	}
}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendDialogue(SEND_4_OPTIONS,
					"Select an Option",
					"What's wrong?"
					,"Can you make me a cake?"
					,"You don't look very happy."
					,"Nice hat!");
		}else if(stage == 0) {
			if(componentId == 1) {
				stage = 7;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What's wrong?"
						}, IS_PLAYER, player.getIndex(), 9827);
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
						"Nice hat!"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else
				end();
		}else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Err thank you. It's a pretty ordinary cooks hat really."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Still suits you. The trousers are pretty special too."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 3) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Its all standard cook's issue uniform..."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 4) {
			stage = 5;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"The whole hat, apron, stripey trousers ensemble - it",
						"works. It made you look like a real cook."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I am a real cook! I haven't got time to be chatting",
					"about Culinary Fashion. I am in desperate need of help!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 6) {
			stage = 7;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What's wrong?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 7) {
			stage = 8;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Oh dear, oh dear, oh dear, I'm in a terrible terrible",
					"mess! It's the Duke's birthday today, and I should be",
					"making him a lovely big birthday cake."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 8) {
			stage = 9;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I've forgotten to buy the ingredients. I'll never get",
					"them in time now. He'll sack me! What will I do? I have",
					"four children and a goat to look after. Would you help",
					"me? Please?"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 9) {
			stage = 10;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"I'm always happy to help a cook in distress.",
					"I can't right now, Maybe later.");
		}else if (stage == 10) {
			if(componentId == 1) {
				stage = 12;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes, I'll help you."
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 2) {
				stage = 11;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I can't right now, Maybe later."
						}, IS_PLAYER, player.getIndex(), 9827);
				}else
				end();
			}else if (stage == 11) {
			stage = 50;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Fine. I always knew you Adventurer types were callous",
					"beasts. Go on your merry way!"
					}, IS_NPC, npcId, 9827);
			} else if (stage == 12) {
			stage = 13;
			player.cooksAssistant = 1;
			player.getPackets().sendConfig(29, 1);
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Oh thank you, thank you. I need milk, an egg and",
					"flour. I'd be very grateful if you can get them for me."
					}, IS_NPC, npcId, 9827);
			} else if (stage == 13) {
			stage = 14;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"So where do I find these ingredients then?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (stage == 14) {
			stage = 15;
			sendDialogue(SEND_4_OPTIONS,
					"Select an Option",
					"Where do I find some flour?",
					"How about milk?",
					"And eggs? Where are they found?",
					"Actually, I know where to find this stuff.");
			}else if (stage == 15) {
			if(componentId == 1) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Where do I find some flour?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 2) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"How about milk?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 3) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"And eggs? Where are they found?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 4) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Actually, I know where to find this stuff."
						}, IS_PLAYER, player.getIndex(), 9827);
						}else
				end();
			} else if (stage == 16) {
			if (player.getInventory().containsOneItem(1927) && player.getInventory().containsOneItem(1933) && player.getInventory().containsOneItem(1944)) {
			stage = 17;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes i got them all here."
						}, IS_PLAYER, player.getIndex(), 9827);
						player.getInventory().deleteItem(1927, 1);
						player.getInventory().deleteItem(1933, 1);
						player.getInventory().deleteItem(1944, 1);
			player.cooksAssistant = 2;
			} else {
			stage = 50;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No im still searching."
						}, IS_PLAYER, player.getIndex(), 9827);
			}
			} else if (stage == 17) {
			stage = 18;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You've brought me everything I need! I am saved!",
					"Thank you!"
					}, IS_NPC, npcId, 9827);
			} else if (stage == 18) {
			stage = 19;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"So do I get to go to the Duke's Party?"
						}, IS_PLAYER, player.getIndex(), 9827);
			} else if (stage == 19) {
			stage = 20;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I'm afraid not, only the big cheeses get to dine with the",
					"Duke."
					}, IS_NPC, npcId, 9827);
			} else if (stage == 20) {
			stage = 21;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Well, maybe one day I'll be important enough to sit on",
						"the Duke's table."
						}, IS_PLAYER, player.getIndex(), 9827);
			} else if (stage == 21) {
			stage = 55;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Maybe, but I wont be holding my breath."
					}, IS_NPC, npcId, 9827);
			}else if (stage == 55) {
			end();
			player.cooksAssistant = 3;
			player.getPackets().sendGameMessage("Congratulations! Quest complete!");
			player.getPackets().sendConfig(29, 2);
			CooksAssistant.sendCompleted(player);
			}else if (stage == 50) {
			end();
			}
		}

	@Override
	public void finish() {
		
	}

}
