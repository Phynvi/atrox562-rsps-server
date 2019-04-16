package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;

public class CreatePortal extends Dialogue {

	// int stage = 0;

	@Override
	public void start() {
		sendOptionsDialogue("Select the portal you would like to change.", "Left Portal.", "Center Portal",
				"Right Portal", "Never mind.");
		stage = 2;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 2) {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
