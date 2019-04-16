package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;

public class Pikkupstix extends Dialogue {
	
	int npcId = 6971;

	@Override
	public void start() {
		sendDialogue(SEND_2_OPTIONS, "Select an Option",
				"Talk about Wolf Whistle quest", "Talk about Pikkenmix");
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_2_OPTIONS && componentId == 1 && player.wolfWhistle == 0) {
			stage = 1;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You there! What are you doing here, as if I didn't have",
					"enough troubles?"
					}, IS_NPC, npcId, 9827);
		} else if (interfaceId == SEND_2_OPTIONS && componentId == 1 && player.wolfWhistle == 2) {
			stage = 36;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"The teeth!"
						}, IS_PLAYER, player.getIndex(), 9827);
		} else if (interfaceId == SEND_2_OPTIONS && componentId == 1 && player.wolfWhistle == 3 && player.getInventory().containsItem(2859, 2)) {
			stage = 56;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I have the wolf bones right here."
						}, IS_PLAYER, player.getIndex(), 9827);
		} else if (interfaceId == SEND_2_OPTIONS && componentId == 1 && player.wolfWhistle == 4 && player.getInventory().containsItem(12425, 10)
			 && player.getInventory().containsItem(12047, 1)) {
			stage = 59;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Here is the pouch and scroll."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 1) {
			stage = 2;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I'm just a passing adventurer. Who are you?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Who am I? My dear "+(player.getAppearence().isMale() ? "boy" : "girl")+", I just so happen to be one",
					"of the most important druids in this village!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 3) {
			stage = 4;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Wow, I don't meet that many celebrities"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 4) {
			stage = 5;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What do you do here?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, firstly, I."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 6) {
			stage = 7;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					"",
					"There is a loud crash from upstairs, followed by the tinkling <br>of broken glass"
					}, IS_NOTHING, npcId, 9827);
		}else if (stage == 7) {
			stage = 8;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Confound you, lapine menace!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 8) {
			stage = 9;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What was that?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 9) {
			stage = 10;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"That was the sound of my carefully arranged room",
					"being destroyed by a fluffy typhoon of wickedness!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 10) {
			stage = 11;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 11) {
			stage = 12;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Wait, I'm good with monsters. Do you want me to go",
						"kill it for you?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 12) {
			stage = 13;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, yes, but it's a little more complicated than that."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 13) {
			stage = 14;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"How complicated can it be?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 14) {
			stage = 15;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					"",
					"A resounding crash comes from upstairs"
					}, IS_NOTHING, npcId, 9827);
		}else if (stage == 15) {
			stage = 16;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Complicated enough for me to waste time explaining it",
					"to you."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 16) {
			stage = 17;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Tell me, have you ever heard of Summoning?"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 17) {
			stage = 18;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Not really."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 18) {
			stage = 19;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, some of the concepts I am about to discuss might",
					"go over your head, so I'll stick to the basics."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 19) {
			stage = 20;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					"",
					"There is a series of loud crunches from upstairs"
					}, IS_NOTHING, npcId, 9827);
		}else if (stage == 20) {
			stage = 21;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Gah! That better not be my wardrobe!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 21) {
			stage = 22;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Summoners are able to call upon animal familiars for a",
					"number of uses. These familiars can help the summoner",
					"practice their skills, fight on the battlefield, or offer any",
					"number of other benefits."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 22) {
			stage = 23;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Beneath this house is a monument - an obelisk - that",
					"allows us druids to tap into this Summoning power."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 23) {
			stage = 24;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					"",
					"A loud gnawing begins upstairs"
					}, IS_NOTHING, npcId, 9827);
		}else if (stage == 24) {
			stage = 25;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"The downside to these obelisks is that rouge familiars,",
					"like the one upstairs, get into this world an wreak",
					"havoc."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 25) {
			stage = 26;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I chased it upstairs, but I didn't have the necessary",
					"Summoning pouch to banish it."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 26) {
			stage = 27;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"If I leave this house, it will come down here, potentially",
					"even getting to the obelisk. If that happens, it could call",
					"another familiar through and multiply!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 27) {
			stage = 28;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I sent my assistant, Stikkiebrix, out to bring me what I",
					"needed, but he has not returned."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 28) {
			stage = 29;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					"",
					"The gnawing stops. There is a crash."
					}, IS_NOTHING, npcId, 9827);
		}else if (stage == 29) {
			stage = 30;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"How fast can it multiply?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 30) {
			stage = 31;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Like a rabbit, because it is."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 31) {
			stage = 32;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"A rabbit!"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 32) {
			stage = 33;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well yes, rabbit-like but."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 33) {
			stage = 34;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Oh, don't worry, I'll deal with this myself. One rabbit",
						"stew coming right up."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 34) {
			stage = 35;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Very well, young fool. You go see how well you do",
					"against it."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 35) {
			player.wolfWhistle = 1;	
			end();
			player.getPackets().sendConfig(1178, 5);
		}else if (stage == 36) {
			stage = 37;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"So, you finally saw what you're up against, eh? Not as",
					"harmless as you assumed, I take it?"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 37) {
			stage = 38;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Horns! Teeth!"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 38) {
			stage = 39;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Are you prepared to treat this situation with the gravity",
					"it deserves now?"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 39) {
			stage = 40;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What IS it?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 40) {
			stage = 41;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"A wolpertinger, and a pretty big one at that."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 41) {
			stage = 42;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"They are spirits that tend to be on the energetic and",
					"destructive side when they manifest here, but are a little",
					"less violent on the spirit plane."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 42) {
			stage = 43;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"So, what can be done about it? Can't you banish it?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 43) {
			stage = 44;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, I could, but in order to do so, I'd need a spirit",
					"wolf pouch."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 44) {
			stage = 45;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Wolpertingers are generally afraid of wolves, that's the",
					"rabbity-side of them. A spirit wolf will scare it and banish",
					"it."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 45) {
			stage = 46;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"The problem is that I don't have any of the necessary",
					"spirit wolf pouches, and the only thing keeping the giant",
					"wolpertinger upstairs is my presence. If I were to leave,",
					"it would amble downstairs to bring more of its kind"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 46) {
			stage = 47;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"through, as I said before."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 47) {
			stage = 48;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Well, what if I were to bring you the elements you",
						"needed? Would that work?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 48) {
			stage = 49;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Not really, I would need to go down to the obelisk in",
					"my cellar and use the necessary ingredients to make a",
					"spirit wolf pouch and some Howl scrolls, but it may slip",
					"out of the front door."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 49) {
			stage = 50;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You could infuse the pouch yourself, though. Would",
					"you like to learn the secrets of Summoning? I can",
					"sense the spark within you, urging you to master the",
					"art."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 50) {
			stage = 51;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Certainly!"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 51) {
			stage = 52;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What do you need me to bring?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 52) {
			stage = 53;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"That's wonderful!"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 53) {
			stage = 54;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"You need to bring two lots of wolf bones. I can provide",
					"the other items you will need."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 54) {
			stage = 55;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I'll get right on it."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 55) {
			player.wolfWhistle = 3;
			end();
		}else if (stage == 56) {
			stage = 57;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Splendid, dear "+(player.getAppearence().isMale() ? "boy" : "girl")+"."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 57) {
			stage = 58;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Here are the pouches, spirit shards and charms you will",
					"need to make the spirit wolf pouch and Howl scrolls.",
					"This key is to the trapdoor over there, which leads to",
					"the obelisk."
					}, IS_NPC, npcId, 9827);
			player.getInventory().addItem(12158, 2);
			player.getInventory().addItem(12155, 2);
			player.getInventory().addItem(12528, 1);
			player.getInventory().addItem(12183, 14);
			player.wolfWhistle = 4;
		}else if (stage == 58) {
			end();
		}else if (stage == 59) {
			stage = 60;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Wonderful! Now, all you have to do is go upstairs and",
					"summong the spirit wolf, then the Howl effect."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 60) {
			stage = 61;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"That's it?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 61) {
			stage = 62;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"It's that simple. The spirit wolf will, when you use the",
					"Howl scroll, chase away the giant wolpertinger and then",
					"disappear. Under normal circumstances, the spirit wolf",
					"would follow you around, defend you in combat and"
					}, IS_NPC, npcId, 9827);
		}else if (stage == 62) {
			stage = 63;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"often lend you its powers."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 63) {
			player.wolfWhistle = 5;
			end();
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
