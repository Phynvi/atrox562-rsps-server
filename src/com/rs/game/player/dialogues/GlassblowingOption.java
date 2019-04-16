package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.skills.crafting.GlassBlowing;
import com.rs.game.player.skills.crafting.GlassBlowing.GlassData;

public class GlassblowingOption extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select an Option", "Low-level glass", "High-level glass");
		stage = 0;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 0) {
			if (componentId == OPTION_1) {
				end();
				player.getDialogueManager().startDialogue("GlassBlowingD", GlassData.forId(GlassBlowing.PRODUCTS[0]),
						GlassData.forId(GlassBlowing.PRODUCTS[1]), GlassData.forId(GlassBlowing.PRODUCTS[2]),
						GlassData.forId(GlassBlowing.PRODUCTS[3]));
			} else {
				end();
				player.getDialogueManager().startDialogue("GlassBlowingD", GlassData.forId(GlassBlowing.PRODUCTS[4]),
						GlassData.forId(GlassBlowing.PRODUCTS[5]), GlassData.forId(GlassBlowing.PRODUCTS[6]),
						GlassData.forId(GlassBlowing.PRODUCTS[7]));
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
