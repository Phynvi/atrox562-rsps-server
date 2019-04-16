package com.rs.game.cities;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;

public class FeldipHills {
	

	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 26685://Poison Waste Dungeon Entrance
			player.setNextWorldTile(new WorldTile(1985, 4173, 0));
			/**if (player.getSkills().getLevel(Skills.SLAYER) >= 56) {
			player.setNextWorldTile(new WorldTile(1985, 4173, 0));
			player.sendMessage("You have entered the Poison Waste Slayer Dungeon.");
			 } else {
                 player.sendMessage("You must have a slayer level of at least 56 to enter the Poison Waste Slayer Dungeon.");**/
			return true;
		case 26570://Exits
		case 26573:
			player.setNextWorldTile(new WorldTile(2321, 3100, 0));
			return true;
		case 26518://Ladder Up
		if (object.getX() == 1991) {
			player.useStairs(828, new WorldTile(player.getX(), player.getY() + 1, 1), 2, 1, "You climb up the ladder.");
			return true;
		} else if (object.getX() == 2041) {
			player.useStairs(828, new WorldTile(player.getX() + 1, player.getY(), 1), 2, 1, "You climb up the ladder.");
			return true;
		}
		case 26519://Ladder Down
		if (object.getX() == 2042) {
			player.useStairs(828, new WorldTile(2041, 4172, 0), 2, 1, "You climb down the ladder.");
			return true;
		} else if (object.getX() == 1991) {
			player.useStairs(828, new WorldTile(1991, 4175, 0), 2, 1, "You climb down the ladder.");
			return true;
		}
			
		default:
			return false;
		}
	}
}
