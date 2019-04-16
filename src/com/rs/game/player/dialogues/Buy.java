package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Skills;

/**
 * Setting a skill level.
 * 
 * @author Raghav
 * 
 */
public class Buy extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Hello there, I can sell you any item you wish!"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_5_OPTIONS) {
			if(stage == -1) {
				stage = 0;
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "OH! okay thanks." },
						IS_PLAYER, player.getIndex(), 9827);
			} else if (stage == 0) {
				if (componentId == 1) {
					int itemid = 0;
					player.getTemporaryAttributtes().put("itemId",
							player.getInventory().addItem(itemid, 1));
					player.getPackets().sendRunScript(108,
							new Object[] { "Enter item ID:" });
				} 
			}
		} else
			end();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
