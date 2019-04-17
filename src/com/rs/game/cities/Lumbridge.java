package com.rs.game.cities;

import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.game.Hit;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.game.npc.NPC;
import com.rs.game.World;

public class Lumbridge {
	
	public static boolean processNPCOption1(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 300:
            player.getDialogueManager().startDialogue("Sedridor", npc.getId());
            return true;
			case 456:
            player.getDialogueManager().startDialogue("FatherAereck", npc.getId());
            return true;
			case 741:
            player.getDialogueManager().startDialogue("DukeHoracio", npc.getId());
            return true;
			case 7969:
            player.getDialogueManager().startDialogue("ExplorerJackDialogue", npc.getId());
            return true;
            default:
                return false;
		}
	}
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 2147:
		if (object.getX() == 3104 && object.getY() == 3162) {
            player.useStairs(828, new WorldTile(3104, 9576, 0), 1, 1);
		}
        return true;
		case 5493:
                player.useStairs(828, new WorldTile(3165, 3251, 0), 1, 1);
                return true;
		case 5947:
                player.useStairs(827, new WorldTile(3168, 9572, 0), 1, 1);
                return true;
        case 5946:
                if (object.getX() == 3169 && object.getY() == 9572)
                    player.useStairs(828, new WorldTile(3169, 3173, 0), 1, 1);
                return true;
		case 6434:
		WorldObject openedHole = new WorldObject(object.getId()+1,
						object.getType(), object.getRotation(), object.getX(),
						object.getY(), object.getPlane());
						World.spawnTemporaryObject(openedHole, 60000, true);
				return true;
		case 6435:
			if (object.getX() == 3084 && object.getY() == 3272) {
				player.useStairs(827, new WorldTile(3085, 9672, 0), 1, 1);
				return true;
			} else if (object.getX() == 3118 && object.getY() == 3244) {
				player.useStairs(827, new WorldTile(3118, 9643, 0), 1, 1);
				return true;
			}
			return false;
		case 15566:
                sendString(player, "<col=8A0808>~-~-~ WARNING ~-~-~", 220, 5);
                sendString(player,"<col=8A0808>Noxious gases vent into this cave.", 220, 7);
                sendString(player,"<col=8A0808>Naked flames may cause an explosion!", 220, 8);
                sendString(player,"<col=8A0808>Beware of vicious head-grabbing beasts!", 220, 10);
                sendString(player,"<col=8A0808>Contact a Slayer master for protective headgear.", 220, 11);
                player.getInterfaceManager().sendInterface(220);
                return true;
		case 32015:
		if (object.getX() == 3103 && object.getY() == 9576) {
            player.useStairs(828, new WorldTile(3105, 3162, 0), 1, 1);
		}
        return true;
		case 36773:
                if (object.getX() == 3204 && object.getY() == 3207) {
                    player.setNextWorldTile(new WorldTile(3205, 3209, 1));
                    return true;
                }
                return false;
		case 36776:
                if (object.getX() == 3204 && object.getY() == 3229) {
                    player.setNextWorldTile(new WorldTile(3205, 3228, 1));
                    return true;
                }
                return false;
		case 36774:
		case 36796:
				player.getDialogueManager().startDialogue("ClimbStairsD");
				return true;
		default:
			return false;
		}
	}
	
	public static boolean handleObjectClick4(WorldObject object, Player player) {
        switch (object.getId()) {
            case 5492:
                if (player.getSkills().getLevel(Skills.THIEVING) >= 28) {
                    int success = Utils.random(8);
                    if (success == 1) {
                        player.lock(2);
                        player.stopAll();
                        player.addWalkSteps(3041, player.getY() >= object.getY() ? object.getY() - 1 : object.getY(), -1, false);
                        player.sendMessage("You successfully pick-locked the door.");
                        player.useStairs(827, new WorldTile(3149, 9652, 0), 1, 2);
                    } else if (success >= 2 && success <= 5) {
                        player.setNextAnimation(new Animation(2244));
                        player.lock(5);
                        player.stopAll();
                        player.sendMessage("You fail to pick the lock and hurt yourself.");
                        int damage = Utils.random(1, 2);
                        player.applyHit(new Hit(player, damage, Hit.HitLook.REGULAR_DAMAGE));
                    } else {
                        player.setNextAnimation(new Animation(2244));
                        player.lock(5);
                        player.stopAll();
                        player.sendMessage("You fail to pick the lock.");
                    }
                } else {
                    player.sendMessage("You must have a thieving level of at least 28 to attempt to pick this lock.");
                }
                return true;
            default:
                return false;
        }
    }
	
	private static void sendString(Player player, String string, int interfaceId, int child) {
        player.getPackets().sendIComponentText(interfaceId, child, string);
    }
}
