package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.Skills;
import com.rs.cache.loaders.NPCDefinitions;

public class Matthias extends Dialogue {

	private int npcId;
	private int startingStage;

	@Override
	public void start() {
		this.npcId = (int) parameters[0];
		this.startingStage = (int) parameters[1];
		if (startingStage == 0)
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Hello, Matthias." }, IS_PLAYER, player.getIndex(), 9827);
		else if (startingStage == 1) {
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Could I rent your gyr falcon, please?" }, IS_PLAYER, player.getIndex(), 9827);
			stage = 20;
		} else if (startingStage == 2) {
			sendNPCDialogue(npcId, 9827, "Hello, there.", "Are you done hunting?");
			stage = 50;
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			sendNPCDialogue(npcId, 9827, "Hello, traveller.", "What brings you here on this beautiful day?");
			break;
		case 0:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "I'd like to ask a few questions about this place." }, IS_PLAYER, player.getIndex(), 9827);
			break;
		case 1:
			sendNPCDialogue(npcId, 9827, "Sure, what'd you like to know?");
			break;
		case 2:
			sendOptionsDialogue("Select an Option", "What is this place?", "Could I rent your gyr falcon?",
					"Nothing, bye.");
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "What is this place?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 9;
				break;
			case OPTION_2:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Could I rent your gyr falcon?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 19;
				break;
			case OPTION_3:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Nothing, bye." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
				break;
			}
			break;
		case 10:
			sendNPCDialogue(npcId, 9827, "This is my home. I've grown all the",
					"animals you see around here on my own. Players come",
					"here to train their Hunting skills on my fields using Falcons.");
			break;
		case 11:
			sendNPCDialogue(npcId, 9827, "Would you be interested in training Hunter",
					"on my fields for a small fee of 500 gold pieces?");
			break;
		case 12:
			sendOptionsDialogue("Select an Option", "Sure, I'd love that.", "No thanks, I'd rather not.");
			break;
		case 13:
			switch (componentId) {
			case OPTION_1:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Sure, I'd love that." }, IS_PLAYER, player.getIndex(), 9827);
				break;
			case OPTION_2:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "No thanks, I'd rather not." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
				break;
			}
			break;
		case 14:
			if (player.getSkills().getLevelForXp(Skills.HUNTER) < 43) {
				sendNPCDialogue(npcId, 9827, "On a second thought though, I cannot let you.",
						"You seem to not have enough experience in Hunting yet.");
				stage = 100;
			} else if (player.getInventory().getNumberOf(995) < 500) {
				sendNPCDialogue(npcId, 9827, "You don't seem to have enough gold with you.",
						"Come back when you've got at least 500 gold pieces on you.");
				stage = 100;
			} else if (player.getEquipment().getWeaponId() != -1 || player.getEquipment().getGlovesId() != -1
					|| player.getEquipment().getShieldId() != -1) {
				sendNPCDialogue(npcId, 9827, "You need to free your hands before I can rent my",
						"gyr falcon out to you.. Come back when you've got nothing on", "your hands.");
				stage = 100;
			} else {
				player.getInventory().deleteItem(995, 500);
				player.getEquipment().addFalcon();
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Matthias hands you a gyr Falcon."
							}, IS_ITEM, 10024, 9827);
			}
			break;
		case 15:
			sendNPCDialogue(npcId, 9827, "There you go. Good luck hunting!");
			break;
		case 20:
			sendNPCDialogue(npcId, 9827, "Of course you can. How else would I", "keep my business going otherwise?");
			stage = 13;
			break;
		case 50:
			sendOptionsDialogue("Select an Option", "Yes, I'd like to hand in my falcon.", "No, not yet.");
			break;
		case 51:
			switch (componentId) {
			case OPTION_1:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Yes, I'd like to hand in my falcon." }, IS_PLAYER, player.getIndex(), 9827);
				break;
			case OPTION_2:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "No, not yet." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
				break;
			}
			break;
		case 52:
			player.getEquipment().removeFalcon();
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You hand over your falcon to Matthias."
							}, IS_ITEM, 10024, 9827);
			break;
		default:
			end();
			break;
		}
		stage++;
	}

	@Override
	public void finish() {

	}

}
