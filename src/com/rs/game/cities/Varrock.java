package com.rs.game.cities;

import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.game.player.content.PlayerLook;
import com.rs.game.player.controlers.RuneEssenceController;

public class Varrock {
	
	public static boolean processNPCOption1(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 547:
            player.getDialogueManager().startDialogue("Baraek", npc.getId());
            return true;
			case 553:
            player.getDialogueManager().startDialogue("Aubury", npc.getId());
            return true;
			case 847:
            player.getDialogueManager().startDialogue("CookingMaster", npc.getId());
            return true;
            default:
                return false;
		}
	}
	
	public static boolean processNPCOption3(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 553:
			RuneEssenceController.teleport(player, npc);
			return true;
			case 548:
            PlayerLook.openThessaliasMakeOver(player);
            return true;
            default:
                return false;
		}
	}
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 2712:
			if (player.getEquipment().getCapeId() == 9801 || player.getSkills().getLevel(Skills.COOKING) >= 32 && player.getEquipment().getHatId() == 1949) {
			return false;
			} else {
			player.getPackets().sendGameMessage("You need atleast 32 cooking and/or wear a chef's hat or cooking cape.");
			return true;
				}
		case 16047:
		if (player.fourthTreasure == false) {
			player.fourthTreasure = true;
			player.getPackets().sendConfig(802, 8);
			player.getEmotesManager().unlockEmote(31);
			player.getDialogueManager().startDialogue("SecurityOfStronghold");
				return true;
		} else if (player.fourthTreasure == true && !player.getInventory().containsOneItem(9005, 9006)) {
			player.getDialogueManager().startDialogue("SecurityOfStronghold");
			return true;
		}
		return false;
		case 16077:
		if (player.secondTreasure == false) {
			player.secondTreasure = true;
			player.getInventory().addItem(995, 3000);
			player.getPackets().sendConfig(802, 2);
			player.getEmotesManager().unlockEmote(30);
				return true;
		}
			return false;
		case 16081:
			if (object.getX() == 2026 && object.getY() == 5218) {
			player.useStairs(827, new WorldTile(2123, 5252, 0), 1, 1);
				return true;
		}
			return false;
		case 16115:
			if (object.getX() == 2148 && object.getY() == 5284) {
			player.useStairs(827, new WorldTile(2309, 5239, 0), 1, 1);
				return true;
		}
			return false;
		case 16043:
		case 16044:
		case 16065:
		case 16066:
		case 16089:
		case 16090:
		case 16124:
		case 16123:
		if (object.getRotation() == 3) {
						if (player.getY() < object.getY())
							player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 2, player.getPlane()));
						if (player.getY() == object.getY())
							player.setNextWorldTile(new WorldTile(player.getX(), player.getY() - 1, player.getPlane()));
					} else if (object.getRotation() == 1) {
						if (player.getY() > object.getY())
							player.setNextWorldTile(new WorldTile(player.getX(), player.getY() - 2, player.getPlane()));
						if (player.getY() == object.getY())
							player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 1, player.getPlane()));
					} else if (object.getRotation() == 2) {
						if (player.getX() > object.getX())
							player.setNextWorldTile(new WorldTile(player.getX() - 2, player.getY(), player.getPlane()));
						if (player.getX() == object.getX())
							player.setNextWorldTile(new WorldTile(player.getX() + 1, player.getY(), player.getPlane()));
					} else if (object.getRotation() == 0) {
						if (player.getX() < object.getX())
							player.setNextWorldTile(new WorldTile(player.getX() + 2, player.getY(), player.getPlane()));
						if (player.getX() == object.getX())
							player.setNextWorldTile(new WorldTile(player.getX() - 1, player.getY(), player.getPlane()));
					}
					return false;
		case 16118:
		if (player.thirdTreasure == false) {
			player.thirdTreasure = true;
			player.getInventory().addItem(995, 5000);
			player.getPackets().sendConfig(802, 4);
			player.getEmotesManager().unlockEmote(33);
			player.getSkills().restoreSkills();
				return true;
		}
		return false;
		case 16135:
		if (player.firstTreasure == false) {
			player.firstTreasure = true;
			player.getInventory().addItem(995, 2000);
			player.getPackets().sendConfig(802, 1);
			player.getEmotesManager().unlockEmote(32);
				return true;
		}
		return false;
		case 16149:
			if (object.getX() == 1902 && object.getY() == 5222) {
			player.useStairs(827, new WorldTile(2042, 5245, 0), 1, 1);
				return true;
		}
		return false;
		case 16154:
			if (object.getX() == 3081 && object.getY() == 3420) {
				player.useStairs(827, new WorldTile(1858, 5244, 0), 1, 1);
				player.getDialogueManager().startDialogue("SimpleMessage", "You squeeze through the hole and find a ladder a few feet down<br>leading into the Stronghold of Security.");
				return true;
			}
		return false;		
		case 13971:
		case 13969:
		case 13967:
		case 13975:
		case 13978: //Entrance to Tolna's Rift
			player.setNextWorldTile(new WorldTile(3297, 9822, 0));
			return true;
		case 13999: //Return to the Surface From Tolna's Rift
			player.setNextWorldTile(new WorldTile(3309, 3449, 0));
			return true;
		case 16048:
		case 16049:
		case 16078:
		case 16112:
		case 16146:
		case 16148:
				player.useStairs(828, new WorldTile(3081, 3421, 0), 1, 1);
				return true;
		case 16152:
		if (!player.getInventory().containsItem(9004, 1)) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You rummage around in the dead explorer's bag..<br>and find a book of hand written notes.");
				player.getInventory().addItem(9004, 1);
				return true;
		}
		case 28089:
		if (object.getX() == 3164 || object.getX() == 3165) {
		player.getGEManager().openGrandExchange();
		return true;
		} else if (object.getY() == 3489 || object.getY() == 3490) {
		player.getBank().initBank();
		return true;
		}
		default:
			return false;
		}
	}
}
