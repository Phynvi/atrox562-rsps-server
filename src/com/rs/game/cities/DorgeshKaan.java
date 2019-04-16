package com.rs.game.cities;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

public class DorgeshKaan {
	

	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		/** 
		 * Stairs 
		 * **/
		case 22939:
			player.useStairs(-1, new WorldTile(player.getX(), player.getY() + 3, 1), 2, 1, "You up the staircase.");
			return true;
		case 22940:
			player.useStairs(-1, new WorldTile(player.getX(), player.getY() - 3, 0), 2, 1, "You down the staircase.");
			return true;
		case 22938:
			player.useStairs(-1, new WorldTile(player.getX() - 4, player.getY(), 0), 2, 1, "You down the staircase.");
			return true;
		case 22937:
			player.useStairs(-1, new WorldTile(player.getX() + 4, player.getY(), 1), 2, 1, "You up the staircase.");
			return true;			
		default:
			return false;
		}
	}
}
