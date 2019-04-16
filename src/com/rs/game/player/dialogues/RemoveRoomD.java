package com.rs.game.player.dialogues;

import com.rs.utils.Utils;
import com.rs.game.player.skills.construction.House.RoomReference;

public class RemoveRoomD extends Dialogue {

	private RoomReference room;

	@Override
	public void start() {
		this.room = (RoomReference) parameters[0];
		sendDialogue(SEND_2_OPTIONS,"Remove the " + Utils.formatPlayerNameForDisplay(room.getRoom().toString()) + "?", "Yes.",
				"No.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == 1)
			player.getHouse().removeRoom(room);
		end();
	}

	@Override
	public void finish() {
	}

}
