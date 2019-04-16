package com.rs.game.cities;

import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.utils.ShopsHandler;

public class Catherby {
	
	public static boolean processNPCOption2(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 562:
            	ShopsHandler.openShop(player, 60);
            	return true;
			case 563:
            	ShopsHandler.openShop(player, 61);
            	return true;
			case 575:
            	ShopsHandler.openShop(player, 59);
            	return true;
            default:
                return false;
		}
	}
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		default:
			return false;
		}
	}
}
