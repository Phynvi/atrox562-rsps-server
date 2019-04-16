package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.ShopsHandler;

public class GeneralShop extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Can I help you at all?"
							}, IS_NPC, npcId, 9827);
		/*sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Hello, how's it going?" },
					IS_PLAYER, player.getIndex(), 9827);*/
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_OPTIONS,
					"Select an Option",
					"Yes please. What are you selling?"
					,"No thanks.");
		} else if (stage == 0) {
			stage = 1;
			if(componentId == 1) {
				end();
				ShopsHandler.openShop(player, 1);
			}else if (componentId == 2) {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No thanks."
						}, IS_PLAYER, player.getIndex(), 9827); 
			}
		} else if(stage == 2) {
                	end();
		}
	}

	@Override
	public void finish() {
		
	}

}
