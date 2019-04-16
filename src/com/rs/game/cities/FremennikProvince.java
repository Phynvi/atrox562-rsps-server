package com.rs.game.cities;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.npc.NPC;
import com.rs.utils.ShopsHandler;

public class FremennikProvince {
	
	public static boolean processNPCOption2(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 123123:
            	ShopsHandler.openShop(player, 60);
            	return true;
			case 12313:
            	ShopsHandler.openShop(player, 59);
            	return true;
            default:
                return false;
		}
	}
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 25339:
			player.setNextWorldTile(new WorldTile(1778, 5343, 1));
			return true;
		case 25340:
			player.setNextWorldTile(new WorldTile(1778, 5346, 0));
			return true;
		default:
			return false;
		}
	}
}
