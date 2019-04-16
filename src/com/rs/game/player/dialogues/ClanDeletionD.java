package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.World;
import com.rs.utils.SerializableFilesManager;

public class ClanDeletionD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Are you sure you'd like<br>to delete your clan", "Yes, I'm sure.",
				"No, I don't want to delete it.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId == OPTION_1) {
			if (player.getClan() != null) {
				player.getClan().killChannel();
				SerializableFilesManager.deleteClan(player.getClan());
				World.removeCachedClan(player.getClan().getClanLeaderUsername());
				player.setCurrentClan(null);
				player.setConnectedClanChannel(false);
				player.sendMessage("Your clan has successfully been deleted.");
			}
		}
		end();
	}

	@Override
	public void finish() {

	}

}
