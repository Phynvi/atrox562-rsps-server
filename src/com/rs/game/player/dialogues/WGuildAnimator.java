package com.rs.game.player.dialogues;

public class WGuildAnimator extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_NO_CONTINUE_2_TEXT_CHAT, "The animator hums, something appears to be working.", "You stand back..." );
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {

	}

}
