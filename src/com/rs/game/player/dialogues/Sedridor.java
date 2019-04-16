package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.content.quests.RuneMysteries;

public class Sedridor extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Welcome adventurer, to the world renowned",
				"Wizards' Tower. How may I help you?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1 && player.runeMysteries == 1) {
			stage = 0;
			sendDialogue(SEND_3_OPTIONS,
					"Select an Option",
					"Nothing thanks, I'm just looking around.",
					"What are you doing down here?",
					"I'm looking for the head wizard.");
		}else if(stage == -1 && player.runeMysteries == 4) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Ah, "+ player.getDisplayName() +". How goes your quest? Have you delivered",
				"the research notes to my friend Aubury yet?"
				}, IS_NPC, npcId, 9827);
			stage = 29;
		}else if(stage == 0) {
			if(componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Nothing thanks, I'm just looking around."
						}, IS_PLAYER, player.getIndex(), 9827); 
			stage = 1;
			}else if (componentId == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What are you doing down here?"
						}, IS_PLAYER, player.getIndex(), 9827); 
				stage = 2;
			}else if (componentId == 4) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I'm looking for the head wizard."
						}, IS_PLAYER, player.getIndex(), 9827); 
				stage = 7;
			}else
				end();
		}else if (stage == 1) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, take care adventurer. You stand on the",
					"ruins of the old destroyed Wizards' Tower.",
					"Strange and powerful magicks lurk here."
					}, IS_NPC, npcId, 9827);
			stage = 20;
		}else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"That is indeed a good question. Here in the cellar",
					"of the Wizards' Tower you find the remains of",
					"the old Wizards' Tower, destroyed by fire"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 3) {
			stage = 4;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"many years past by the treachery of the Zamorakians.",
					"Many mysteries were lost, which we try to find once",
					"more. By building this Tower on the remains of the old,"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 4) {
			stage = 5;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"we sought to show the world of our dedication to",
					"learning the mysteries of Magic. I am here searching",
					"through these fragments for knowledge from",
					"the artefacts from our past."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"And have you found anything useful?"
						}, IS_PLAYER, player.getIndex(), 9827); 
		}else if (stage == 6) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Aaaah... that would be telling adventurer. Anything I",
					"have found I cannot speak freely of, for fear the",
					"treachery of the past might be repeated."
					}, IS_NPC, npcId, 9827); 
			stage = 20;
		}else if (stage == 7) {
			stage = 8;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Oh, you are, are you?",
					"And just why would you be doing that?"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 8) {
			stage = 9;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"The Duke of Lumbridge sent me to find him. I have",
						"this weird talisman he found. He said the head wizard",
						"would be very interested in it."
						}, IS_PLAYER, player.getIndex(), 9827); 
		}else if (stage == 9) {
			stage = 10;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Did he now? HmmmMMMMMmmmmm.",
					"Well that IS interesting. Hand it over then adventurer,",
					"let me see what all the hubbub about it is.",
					"Just some amulet I'll wager."
					}, IS_NPC, npcId, 9827); 
		}else if(stage == 10) {
			stage = 11;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Ok, here you are.",
					"No, I'll only give it to the head wizard.");
		}else if(stage == 11) {
			if(componentId == 1) {
				stage = 12;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Ok, here you are."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 20;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No, I'll only give it to the head wizard."
						}, IS_PLAYER, player.getIndex(), 9827); 
			} else
				end();
		}else if (stage == 12) {
			stage = 13;
			sendDialogue(SEND_1_TEXT_CHAT, "", "You hand the Talisman to the wizard." );
		}else if (stage == 13) {
			stage = 14;
			player.getInventory().deleteItem(1438, 1);
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Wow! This is... incredible!"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 14) {
			stage = 15;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Th-this talisman you brought me...! It is the last piece",
					"of the puzzle, I think! Finally! The legacy of our",
					"ancestors... it will return to us once more!"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 15) {
			stage = 16;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I need time to study this, " + player.getUsername() + ". Can you please",
					"do me this task while I study this talisman you have",
					"brought me? In the mighty town of Varrock, which"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 16) {
			stage = 17;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"is located North East of here, there is a certain shop",
					"that sells magical runes. I have in this package all of the",
					"research I have done relating to the Rune Stones, and"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 17) {
			stage = 18;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"require somebody to take them to the shopkeeper so that",
					"he may share my research and offer me his insights.",
					"Do this thing for me, and bring back what he gives you,"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 18) {
			stage = 19;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"and if my suspicions are correct, I will let you into the",
					"knowledge of one of the greatest secrets this world has",
					"ever known! A secret so powerful that it destroyed the"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 19) {
			stage = 21;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"original Wizards' Tower all of those centuries",
					"ago! My reasearch, combined with this mysterious",
					"talisman... I cannot believe the answer to",
					"the mysteries is so close now!"
					}, IS_NPC, npcId, 9827); 
		}else if(stage == 20) {
			end();
		}else if (stage == 21) {
			stage = 22;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Do this thing for me " + player.getUsername() + ". Be rewarded in a",
					"way you can never imagine."
					}, IS_NPC, npcId, 9827); 
		}else if(stage == 22) {
			stage = 23;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Yes, certainly.",
					"No, I'm busy.");
		}else if(stage == 23) {
			if(componentId == 1) {
				stage = 24;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes, certainly."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}else if (componentId == 2) {
				stage = 20;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No, I'm busy."
						}, IS_PLAYER, player.getIndex(), 9827);
			} else
				end();
		}else if (stage == 24) {
			stage = 25;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Take this package, and head directly North",
					"from here, through Draynor Village, until you reach",
					"the Barbarian Village. Then head East from there",
					"until you reach Varrock."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 25) {
			stage = 26;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Once in Varrock, take this package to the owner of the",
					"rune shop. His name is Aubury. You may find it",
					"helpful to ask one of Varrock's citizens for directions,"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 26) {
			stage = 27;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"as Varrock can be a confusing place for the first time",
					"visitor. He will give you a special item - bring it back to",
					"me, and I shall show you the mystery of the runes..."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 27) {
			stage = 28;
			sendDialogue(SEND_1_TEXT_CHAT, "", "The head wizard gives you a package" );
		}else if (stage == 28) {
			stage = 20;
			player.getInventory().addItem(290, 1);
			player.runeMysteries = 2;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Best of luck with your quest, "+ player.getDisplayName() +"."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 29) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes, I have. He gave me some research notes",
						"to pass on to you."
						}, IS_PLAYER, player.getIndex(), 9827);
			stage = 30;
		}else if (stage == 30) {
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"May I have his notes then?"
					}, IS_NPC, npcId, 9827); 
			stage = 31;
		}else if (stage == 31) {
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Sure. I have them here."
						}, IS_PLAYER, player.getIndex(), 9827);
			stage = 32;
		}else if (stage == 32) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, before you hand them over to me, as you",
					"have been nothing but truthful with me to this point,",
					"and I admire that in an adventurer, I will let you",
					"into the secret of our research."
					}, IS_NPC, npcId, 9827); 
			stage = 33;
		}else if (stage == 33) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Now as you may or may not know, many",
					"centuries ago, the wizards at this Tower",
					"learnt the secret of creating Rune Stones, which",
					"allowed us to cast Magic very easily."
					}, IS_NPC, npcId, 9827); 
			stage = 34;
		}else if (stage == 34) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"When this Tower was burnt down the secret of",
					"creating runes was lost to us for all time... except it",
					"wasn't. Some months ago, while searching these ruins",
					"for information from the old days,"
					}, IS_NPC, npcId, 9827); 
			stage = 35;
		}else if (stage == 35) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I came upon a scroll, almost destroyed, that detailed a",
					"magical rock deep in the icefields of the North, closed",
					"of from access by anything other than magical means."
					}, IS_NPC, npcId, 9827); 
			stage = 36;
		}else if (stage == 36) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This rock was called the 'Rune Essence' by the",
					"magicians who studied its powers. Apparently, by simply",
					"breaking a chunk from it, a Rune Stone could be",
					"fashioned very quickly and easily at certain"
					}, IS_NPC, npcId, 9827); 
			stage = 37;
		}else if (stage == 37) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"elemental altars that were scattered across the land",
					"back then. Now, this is an interesting little piece of",
					"history, but not much use to us as modern wizards",
					"without access to the Rune Essence,"
					}, IS_NPC, npcId, 9827); 
			stage = 38;
		}else if (stage == 38) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"or these elemental altars. This is where you and",
					"Aubury come into this story. A few weeks back,",
					"Aubury discovered in a standard delivery of runes",
					"to his store, a parachment detailing a"
					}, IS_NPC, npcId, 9827); 
			stage = 39;
		}else if (stage == 39) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"teleportation spell that he had never come across",
					"before. To his shock, when cast it took him to a",
					"strange rock he had never encountered before...",
					"yet that felt strangely familiar..."
					}, IS_NPC, npcId, 9827); 
			stage = 40;
		}else if (stage == 40) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"As i'm sure you have now guessed, he had discovered a",
					"portal leading to the mythical Rune Essence. As soon as",
					"he told me of this spell, I saw the importance of his find,"
					}, IS_NPC, npcId, 9827); 
			stage = 41;
		}else if (stage == 41) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"for if we could but find the elemental altars spoken",
					"of in the ancient texts, we would once more be able",
					"to create runes as our ancestors had done! It would",
					"be the saviour of the wizards' art!"
					}, IS_NPC, npcId, 9827); 
			stage = 42;
		}else if (stage == 42) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					player.getDisplayName(),
					"I'm still not sure how I fit into",
					"this little story of yours..."
					}, IS_PLAYER, player.getIndex(), 9827);
			stage = 43;
		}else if (stage == 43) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You haven't guessed? This talisman you brought me...",
					"it is the key to the elemental altar of air! When",
					"you hold it next, it will direct you towards"
					}, IS_NPC, npcId, 9827); 
			stage = 44;
		}else if (stage == 44) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"the entrance to the long forgotten Air Altar! By",
					"bringing pieces of the Rune Essence to the Air Temple,",
					"you will be able to fashion your own Air Runes!"
					}, IS_NPC, npcId, 9827); 
			stage = 45;
		}else if (stage == 45) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"And this is not all! By finding other talismans similar",
					"to this one, you will eventually be able to craft every",
					"rune that is avaible on this world! Just"
					}, IS_NPC, npcId, 9827); 
			stage = 46;
		}else if (stage == 46) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"as our ancestors did! I cannot stress enough what a",
					"find this is! Now, due to the risks involved of letting",
					"this mighty power fall into the wrong hands"
					}, IS_NPC, npcId, 9827); 
			stage = 47;
		}else if (stage == 47) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I will keep the teleport skill to the Rune Essence",
					"a closely guarded secret, shared only by myself",
					"and those Magic users around the world",
					"whom I trust enough to keep it."
					}, IS_NPC, npcId, 9827); 
			stage = 48;
		}else if (stage == 48) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This means that if any evil power should discover",
					"the talismans required to enter the elemental",
					"temples, we will be able to prevent their access",
					"to the Rune Essence and prevent"
					}, IS_NPC, npcId, 9827); 
			stage = 49;
		}else if (stage == 49) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"tragedy befalling this world. I know not where the",
					"temples are located, nor do I know where the talismans",
					"have been scattered to in this land, but I now"
					}, IS_NPC, npcId, 9827); 
			stage = 50;
		}else if (stage == 50) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"return your Air Talisman to you. Find the Air",
					"Temple, and you will be able to charge your Rune",
					"Essences to become Air Runes at will. Any time"
					}, IS_NPC, npcId, 9827); 
			stage = 51;
		}else if (stage == 51) {
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"you wish to visit the Rune Essence, speak to me",
					"or Aubury and we will open a portal to that",
					"mystical place for you to visit."
					}, IS_NPC, npcId, 9827); 
			stage = 52;
		}else if (stage == 52) {
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					player.getDisplayName(),
					"So only you and Aubury know the teleport",
					"spell to the Rune Essence?"
					}, IS_PLAYER, player.getIndex(), 9827);
			stage = 54;
		}else if (stage == 54) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"No... there are others... whom I will tell of your",
					"authorisation to visit that place. When you speak",
					"to them, they will know you, and grant you",
					"access to that place when asked."
					}, IS_NPC, npcId, 9827); 
			stage = 55;
		}else if (stage == 55) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Use the Air Talisman to locate the air temple,",
					"and use any further talismans you find to locate",
					"the other missing elemental temples.",
					"Now... my research notes please?"
					}, IS_NPC, npcId, 9827); 
			stage = 56;
		}else if (stage == 56) {
			stage = 57;
			sendDialogue(SEND_2_TEXT_CHAT, "", "You hand the head wizard the research notes.", "He hands you back the Air Talisman." );
		}else if (stage == 57) {
			end();
			RuneMysteries.sendCompleted(player);
	}
}

	@Override
	public void finish() {
		
	}

}
