package com.rs.game.cities;

import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.utils.ShopsHandler;
import com.rs.game.npc.NPC;

public class SeersVillage {
	public static boolean processNPCOption1(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 4946:
            	player.getDialogueManager().startDialogue("FiremakingMaster", npc.getId());
            	return true;
            default:
                return false;
		}
	}
	
	public static boolean processNPCOption2(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 4946:
            	ShopsHandler.openShop(player, 169);
            	return true;
            default:
                return false;
		}
	}
	
}
