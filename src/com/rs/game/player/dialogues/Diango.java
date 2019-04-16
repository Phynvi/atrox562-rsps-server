package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import java.util.logging.Logger;
import com.rs.game.player.content.Magic;
import com.rs.game.ForceTalk;

public class Diango extends Dialogue {
	
	private int npcId;

	@Override
	public void start() {
		sendDialogue(SEND_2_TEXT_CHAT,
				
					"Diango", "This store is just temporary untill the items", "become obtainable.");
                
	}

	@Override
	public void run(int interfaceId, int componentId) {
if (stage == -1) {
			end();
		}
	}
        
   @Override
	public void finish() {
		
	}
       
	
	public void doTrip() {
		
	}

}
