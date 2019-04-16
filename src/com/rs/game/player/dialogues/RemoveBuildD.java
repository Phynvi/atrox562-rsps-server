package com.rs.game.player.dialogues;

import com.rs.game.WorldObject;

public class RemoveBuildD extends Dialogue {

	WorldObject object;

	@Override
	public void start() {
		this.object = (WorldObject) parameters[0];
		sendDialogue(SEND_2_OPTIONS,"Really remove it?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == 1) {
			player.getHouse().removeBuild(object);
		}
		end();
	}

	@Override
	public void finish() {

	}

}
