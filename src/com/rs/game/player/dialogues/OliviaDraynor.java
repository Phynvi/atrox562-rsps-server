package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.ShopsHandler;

public class OliviaDraynor extends Dialogue {

	int npcId;
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
				NPCDefinitions.getNPCDefinitions(npcId).name,
				"Would you like to trade in seeds?"
				}, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(stage == -1) {
			stage = 0;
			sendDialogue(SEND_3_OPTIONS,
					"Select an Option",
					"Yes"
					,"No"
					,"Where do I get rarer seeds from?");
		}else if(stage == 0) {
			if(componentId == 2) {
				end();
				ShopsHandler.openShop(player, 165);
			}else if (componentId == 3) {
				stage = 100;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"No, thanks."
						}, IS_PLAYER, player.getIndex(), 9827);
			}else if (componentId == 4) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Where do I get rarer seeds from?"
						}, IS_PLAYER, player.getIndex(), 9827);
			}else
				end();
		}else if (stage == 1) {
			stage = 100;
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"The Master Farmers usually carry a few rare seeds",
					"around with them, although I don't know if they'd want",
					"to part with them for any price to be honest."
					}, IS_NPC, npcId, 9827);
		}else if (stage == 100) {
			end();
		}else
			end();
	}

	@Override
	public void finish() {
		
	}

}
