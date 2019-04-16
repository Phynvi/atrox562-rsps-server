package com.rs.game.cities;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.npc.NPC;

public class AlKharid {
	
	public static boolean processNPCOption1(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 2824:
            player.getDialogueManager().startDialogue("TannerD", npc.getId());
            return true;
            default:
                return false;
		}
	}
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 3828:
			if (object.getX() == 3227 && object.getY() == 3108) {
				player.sendMessage("You climb down the hole..");
				player.useStairs(827, new WorldTile(3484, 9510, 2), 1, 1);
				return true;
			}
			return false;
		case 3829:
			if (object.getX() == 3483 && object.getY() == 9510) {
				player.useStairs(828, new WorldTile(3226, 3108, 0), 1, 1);
				return true;
			}
			return false;
		default:
			return false;
		}
	}
	
	public static boolean handleObjectClick5(WorldObject object, Player player) {
		WorldObject openedDoor = new WorldObject(35549, object.getType(), object.getRotation() + 1,
									3268 , 3228, object.getPlane());
		WorldObject openedDoor1 = new WorldObject(35551, object.getType(), object.getRotation() - 1,
									3268 , 3227, object.getPlane());
		switch (object.getId()) {
		case 35549:
		if (player.getInventory().containsItem(995, 10)) {
					player.getInventory().deleteItem(995, 10);
					World.spawnTemporaryObject(openedDoor, 1200, false);
					World.spawnTemporaryObject(openedDoor1, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(player.getX() >= object.getX() ? object.getX() - 1 : object.getX(), 3228 , -1, false);
						player.getPackets().sendGameMessage("You paid 10 coins and pass through the gate.");
				} 
		case 35551:
		if (player.getInventory().containsItem(995, 10)) {
					player.getInventory().deleteItem(995, 10);
					World.spawnTemporaryObject(openedDoor, 1200, false);
					World.spawnTemporaryObject(openedDoor1, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(player.getX() >= object.getX() ? object.getX() - 1 : object.getX(), 3228 , -1, false);
						player.getPackets().sendGameMessage("You paid 10 coins and pass through the gate.");
				}
			return false;
		default:
			return false;
		}
	}
}
