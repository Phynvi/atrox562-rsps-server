package com.rs.game.cities;

import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.game.player.dialogues.MiningGuildDwarf;
import com.rs.game.player.content.PartyRoom;

public class Falador {
	
	boolean balloons;
	
	public static boolean processNPCOption1(Player player, NPC npc) {
		player.faceEntity(npc);
        npc.faceEntity(player);
        switch (npc.getId()) {
			case 3295:
            	player.getDialogueManager().startDialogue("MiningMaster", npc.getId());
            	return true;
            default:
                return false;
		}
	}
	
	public static boolean handleObjectClick1(WorldObject object, Player player) {
		switch (object.getId()) {
		case 26193:
		PartyRoom.openPartyChest(player);
		return true;
		case 26194:
		player.getDialogueManager().startDialogue("PartyRoomLever");
		return true;
		case 30944:
		if (object.getX() == 3059 && object.getY() == 3376) {
		if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 60 Mining to enter the guild.");
			return true;
		}
			player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
			return true;
		}
		case 30941:
				player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 1);
				return true;
		case 2113:
		if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
				player.getDialogueManager().startDialogue("SimpleNPCMessage", MiningGuildDwarf.getClosestDwarfID(player), "Sorry, but you need level 60 Mining to go in there.");
				return true;
					}
				player.useStairs(828, new WorldTile(3021, 9739, 0), 0, 1);
		default:
			return false;
		}
	}
}
