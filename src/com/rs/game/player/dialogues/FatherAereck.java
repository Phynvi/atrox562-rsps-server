package com.rs.game.player.dialogues;

import com.rs.game.player.content.GraveStoneSelection;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.utils.Utils;

public class FatherAereck extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, 9827, "Hello there brother " + Utils.formatPlayerNameForDisplay(player.getDisplayName()) + ". How may I help you today?");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue("Select an Option", "Can I have a different gravestone?", "Can you restore my prayer?", "Nothing, nevermind.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {player.getDisplayName(), "Can I have a different gravestone?" }, IS_PLAYER, player.getIndex(), 9827);
			} else if (componentId == OPTION_2) {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {player.getDisplayName(), "Can you restore my prayer?" }, IS_PLAYER, player.getIndex(), 9827);
			} else {
				stage = 3;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {player.getDisplayName(), "Nothing, nevermind." }, IS_PLAYER, player.getIndex(), 9827);
			}
		} else if (stage == 1) {
			stage = 4;
			sendNPCDialogue(npcId, 9827, "Of course you can. Have a look at this selection of gravestones.");
		} else if (stage == 2) {
			stage = 3;
			sendNPCDialogue(npcId, 9827, "I think the Gods prefer it if you pray<br>to them at an altar dedicated to their name.");
		} else if (stage == 4) {
			end();
			GraveStoneSelection.openSelectionInterface(player);
		} else if (stage == 3) {
			end();
		}
	}

	@Override
	public void finish() {

	}
}
