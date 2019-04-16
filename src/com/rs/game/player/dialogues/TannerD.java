package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;

/**
 * Handles Tanner usual dialogue.
 * 
 * @author Kris
 */
public class TannerD extends Dialogue {

	int npcId = 2824;

	@Override
	public void start() {
		sendNPCDialogue(npcId, 9827, "Good afternoon.");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case 0:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Hello there." }, IS_PLAYER, player.getIndex(), 9827);
			break;
		case 1:
			sendOptionsDialogue("What would you like to ask?", "Could you tan my hides, please?", "How are you?",
					"Do you sell anything?", "Nothing, bye.");
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Could you tan my hides, please?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 9;
				break;
			case OPTION_2:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "How are you?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 19;
				break;
			case OPTION_3:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Do you sell anything?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 29;
				break;
			case OPTION_4:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Nothing, bye." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 49;
				break;
			}
			break;
		case 10:
			sendNPCDialogue(npcId, 9827, "Most certainly!", "It'll be my pleasure..");
			break;
		case 11:
			end();
			player.getInterfaceManager().sendInterface(324);
			break;
		case 20:
			sendNPCDialogue(npcId, 9827, "I'm good.", "Business has been blooming lately!");
			break;
		case 21:
			sendNPCDialogue(npcId, 9827, "Speaking of which, if you ever need a tanner", "you know who to visit!");
			break;
		case 22:
			sendOptionsDialogue("What would you like to ask?", "Could you tan my hides, please?",
					"Do you sell anything?", "Nothing, bye.");
			break;
		case 23:
			switch (componentId) {
			case OPTION_1:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Could you tan my hides, please?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 9;
				break;
			case OPTION_2:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Do you sell anything?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 29;
				break;
			case OPTION_3:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Nothing, bye." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 49;
				break;
			}
			break;
		case 30:
			sendNPCDialogue(npcId, 9827, "Nothing but my services, which include", "tanning various hides!");
			break;
		case 31:
			sendOptionsDialogue("What would you like to ask?", "Could you tan my hides, please?", "How are you?",
					"Nothing, bye.");
			break;
		case 32:
			switch (componentId) {
			case OPTION_1:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Could you tan my hides, please?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 9;
				break;
			case OPTION_2:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "How are you?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 19;
				break;
			case OPTION_3:
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Nothing, bye." }, IS_PLAYER, player.getIndex(), 9827);
				stage = 49;
				break;
			}
			break;
		case 50:
			end();
			break;
		default:
			break;
		}
		stage++;
	}

	@Override
	public void finish() {
	}

}