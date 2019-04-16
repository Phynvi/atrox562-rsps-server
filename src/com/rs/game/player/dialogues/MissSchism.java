package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;

public class MissSchism extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Oooh, my dear, have you heard the news?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendDialogue(SEND_3_OPTIONS,
					"What would you like to say?",
					"Ok, tell me about the news."
					,"Who are you?"
					,"I'm not talking to you, you horrible woman.");
		}else if(stage == 0) {
			if(componentId == 2) {
				stage = 7;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Ok, tell me about the news."
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 3) {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Who are you?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 4) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I'm not talking to you, you horrible woman."
						}, IS_PLAYER, player.getIndex(), 9827);
			}else
				end();
		}else if (stage == 1) {
			stage = 100;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Oooh."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 2) {
			stage = 3;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I, my dear, am a concerned citizen of Draynor Village.",
					"Ever since the Council allowed those farmers to set up",
					"their stalls here, we've had a constant flow of thieves and",
					"murderers through our fair village, and I decided that"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 3) {
			stage = 4;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"someone HAD to stand up and",
					"keep an eye on the situation."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 4) {
			stage = 5;
			sendEntityDialogue(SEND_4_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"I also do voluntary work for the Draynor Manor",
					"Restoration Fund. We're campagning to have",
					"Draynor manor turned into a museum before the wet-ro",
					"destroys it completely."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 5) {
			stage = 6;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Well, now that I've cleared the vampire out of the manor,",
						"I guess you won't have to much trouble turning it into a",
						"museum."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 6) {
			stage = 100;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"That's all very well dear, but no vampire was ever going",
					"to stop me making it a museum."
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 7) {
			stage = 8;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Well, there's just to much to tell at once! What would",
					"you like to hear first: the vampire or the bank?"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 8) {
			stage = 9;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Tell me about the vampire.",
					"Tell me about the bank.");
		}else if(stage == 9) {
			if(componentId == 1) {
				stage = 10;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Tell me about the vampire."
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 2) {
				stage = 12;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"What about the bank?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else
				end();
		}else if (stage == 10) {
			stage = 11;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"There is an evil Vampire terrorizing the city!"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 11) {
			stage = 100;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Oh, that's not good."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 12) {
			stage = 13;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"It's terrible, absolutely terrible! Those poor people!"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 13) {
			stage = 14;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Ok, yeah."
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 14) {
			stage = 15;
			sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"And who'd have ever thought such a sweet old gentleman",
					"would do such a thing?"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 15) {
			stage = 16;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Are we talking about the bank robbery?"
						}, IS_PLAYER, player.getIndex(), 9827);
		}else if (stage == 16) {
			stage = 100;
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Oh yes, my dear. It was terrible! TERRIBLE!"
					}, IS_NPC, npcId, 9827); 
		}else if (stage == 100) {
			end();
		}else
			end();
	}

	@Override
	public void finish() {
		
	}

}
