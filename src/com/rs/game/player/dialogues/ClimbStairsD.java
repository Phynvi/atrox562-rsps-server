package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;

public class ClimbStairsD extends Dialogue {

	private WorldTile up, down;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
		if (componentId == 2) {
		player.useStairs(-1, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 2, 1, "You climb up the stairs.");
		} else if (componentId == 3) {
		player.useStairs(-1, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 2, 1, "You climb down the stairs.");
		} else if (componentId == 4) {
			end();
		}

	}

	@Override
	public void start() {
		sendDialogue(SEND_3_OPTIONS,"Select an Option",
				"Climb up",
				"Climb down",
				"Cancel");

	}

}
