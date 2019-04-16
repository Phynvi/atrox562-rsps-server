package com.rs.game.player.dialogues;

import com.rs.game.player.skills.construction.House.RoomReference;

public class CreateRoomD extends Dialogue {

	private RoomReference room;

	@Override
	public void finish() {
		player.getHouse().previewRoom(room, true, false);
		player.getHouse().reload(room);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == 4) {
			player.getHouse().reload(room);
			end();
			return;
		}
		if (componentId == 3) {
			end();
			player.getHouse().createRoom(room);
			return;
		}
		player.getHouse().previewRoom(room, true, false);
		room.setRotation((room.getRotation() + (componentId == 1 ? 1 : -1)) & 0x3);
		sendPreview();
	}

	public void sendPreview() {
		sendDialogue(SEND_4_OPTIONS,"Select an Option", "Rotate clockwise", "Rotate anticlockwise.", "Build.", "Cancel");
		player.getHouse().previewRoom(room, false, false);
	}

	@Override
	public void start() {
		this.room = (RoomReference) parameters[0];
		if (room == null)
			return;
		sendDialogue(SEND_4_OPTIONS,"Select an Option", "Rotate clockwise", "Rotate anticlockwise.", "Build.", "Cancel");
		player.getHouse().previewRoom(room, false, true);
	}

}