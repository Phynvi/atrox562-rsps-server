package com.rs.game.player.dialogues;

public class SimpleMessage extends Dialogue {

	@Override
	public void start() {
		String messages = (String) parameters[0];
		sendDialogue(messages);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {

	}

}
