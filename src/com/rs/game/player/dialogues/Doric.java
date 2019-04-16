package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.content.quests.DoricsQuest;

public class Doric extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (player.doricsQuest == 0) {
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hello traveller, what brings you to my humble smithy?"
				}, IS_NPC, npcId, 9827);
		} else if (player.doricsQuest == 1) {
			stage = 16;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Have you got my materials yet, traveller?"
				}, IS_NPC, npcId, 9827);
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_5_OPTIONS,
					"What would you like to say?",
					"I wanted to use your anvils.",
					"I want to use your whetstone.",
					"Mind your own business, shortstuff.",
					"I was just checking out the landscape.",
					"What do you make here?");
			} else if(stage == 0) {
                	if(componentId == 1) {
						stage = 1;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I wanted to use your anvils."
						}, IS_PLAYER, player.getIndex(), 9827); 
			} else if (componentId == 2) {
						stage = 5;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I wanted to use your whetstone."
						}, IS_PLAYER, player.getIndex(), 9827); 
			} else if (componentId == 3) {
						stage = 7;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Mind your own business, shortstuff."
						}, IS_PLAYER, player.getIndex(), 9827); 
			} else if (componentId == 4) {
						stage = 8;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I was just checking out the landscape."
						}, IS_PLAYER, player.getIndex(), 9827); 
			} else if (componentId == 5) {
						stage = 12;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What do you make here?"
						}, IS_PLAYER, player.getIndex(), 9827); 
			}
		} else if(stage == 1) {
                	stage = 2;
					sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"My anvils get enough work with my own use. I make",
					"pickaxes, and it takes alot of hard work. If you could",
					"get me some more materials, then I could let you use",
					"them." }, IS_NPC, npcId, 9827);
		}else if (stage == 2) {
			stage = 3;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Yes, I will get you the materials.",
					"No, hitting rocks is for the boring people, sorry.");
		} else if (stage == 3) {
			if(componentId == 1) {
				stage = 15;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes, I will get you the materials."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No, hitting rocks is for the boring people, sorry."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}
		} else if (stage == 4) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"That is your choice. Nice to meet you anyway." }, IS_NPC, npcId, 9827);
			} else if (stage == 5) {
				stage = 6;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"The whetstone is for more advanced smithing, but I",
					"could let you use it as well as my anvils if you could",
					"get me some more materials." }, IS_NPC, npcId, 9827);
			} else if (stage == 6) {
			if(componentId == 1) {
						stage = 50;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Not working."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 4;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No, hitting rocks is for the boring people, sorry."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}
		}else if (stage == 7) {
				stage = 50;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"How nice to meet someone with such pleasant manners.",
					"Do come again when you need to shout at someone",
					"smaller than you!" }, IS_NPC, npcId, 9827);
				}else if (stage == 8) {
				stage = 9;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hope you like it. I do enjoy the solitude of my little",
					"home. If you get time, please say hi to my friends in",
					"the Dwarven Mine." }, IS_NPC, npcId, 9827);
				}else if (stage == 9) {
				stage = 10;
				sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Dwarven Mine?",
					"Will do!");
				} else if (stage == 10) {
				if(componentId == 1) {
						stage = 11;
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Dwarven Mine?"
						}, IS_PLAYER, player.getIndex(), 9827); 
				}else if (componentId == 2) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Will do!"
						}, IS_PLAYER, player.getIndex(), 9827);
				}
				}else if (stage == 11) {
				stage = 50;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Yep, the entrance is in the side of Ice Mountain just to",
					"the east of here. They're a friendly bunch. Stop in at",
					"Nurmof's store and buy one of my pickaxes!" }, IS_NPC, npcId, 9827);
				}else if (stage == 12) {
				stage = 13;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I make pickaxes. I am the best maker of pickaxes in the ",
					"whole of "+ Settings.SERVER_NAME +"." }, IS_NPC, npcId, 9827);	
				}else if (stage == 13) {
				stage = 14;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Do you have any to sell?"
						}, IS_PLAYER, player.getIndex(), 9827);	
				}else if (stage == 14) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Sorry, but I've got a running order with Nurmof." }, IS_NPC, npcId, 9827);	
				} else if (stage == 15) {
				stage = 50;
				sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Clay is what I use more than anything, to make casts.",
					"Could you get me 6 clay, 4 copper ore, and 2 iron ore,",
					"please? I could pay a little, and let you use my anvils.",
					"Take this pickaxe with you just in case you need it." }, IS_NPC, npcId, 9827);	
					player.doricsQuest = 1;	
					player.getPackets().sendConfig(31, 10);	
					player.getInventory().addItem(1265, 1);	
				}else if (stage == 16) {
				if (!player.getInventory().containsItem(434, 6) || !player.getInventory().containsItem(436, 4) || !player.getInventory().containsItem(440, 2)) {
				stage = 50;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Sorry i don't have them all yet."
						}, IS_PLAYER, player.getIndex(), 9827);	
				} if (player.getInventory().containsItem(434, 6) && player.getInventory().containsItem(436, 4) && player.getInventory().containsItem(440, 2)) {
				stage = 17;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I have everything you need."
						}, IS_PLAYER, player.getIndex(), 9827);	
					}						
				} else if (stage == 17) {
				stage = 18;
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Many thanks. Pass them here, please, I can spare you",
					"some coins for your trouble, and please use my anvils",
					"any time you want." }, IS_NPC, npcId, 9827);
				}else if (stage == 18) {
				stage = 19;			
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You hand the clay, copper, and iron to Doric."
							}, IS_ITEM, 436, 9827);					
				}else if (stage == 19) {
					end();
					DoricsQuest.sendCompleted(player);
				} else if (stage == 50) {
				end();
				}else
				end();
	}

	@Override
	public void finish() {
		
	}

}
