package com.rs.game.cities;

import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;

public class MosleHarmless {
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 15767:
		player.setNextWorldTile(new WorldTile(3748, 9374, 0));
		case 15811:
		player.setNextWorldTile(new WorldTile(3749, 2973, 0));
		default:
			return false;
		}
	}
}
