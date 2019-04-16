package com.rs.game.player.content;

import com.rs.game.player.Player;

public class Teleports {
	
	public static void sendInter(Player player) {
		player.getInterfaceManager().sendInterface(403);
		player.getPackets().sendIComponentText(403, 11, "Where would you like to go?");
		player.getPackets().sendItemOnIComponent(403, 16, 1303, 1); //train
		player.getPackets().sendItemOnIComponent(403, 17, 11694, 1); //boss
		player.getPackets().sendItemOnIComponent(403, 18, 2417, 1); //pvp
		player.getPackets().sendItemOnIComponent(403, 19, 10551, 1); //minigame
		player.getPackets().sendIComponentText(403, 20, "Training");
		player.getPackets().sendIComponentText(403, 21, "Bosses");
		player.getPackets().sendIComponentText(403, 22, "Pking");
		player.getPackets().sendIComponentText(403, 23, "Minigames");
	}
	
	public static void handleButtonInterface(Player player, int componentId) {
		if (componentId == 12) { //train
			player.getDialogueManager().startDialogue("TeleportTraining");
		} else if (componentId == 13) { //boss
			player.getDialogueManager().startDialogue("TeleportBosses");
		} else if (componentId == 14) { //pvp
			player.getDialogueManager().startDialogue("TeleportPking");
		} else if (componentId == 15) { //minigames
			player.getDialogueManager().startDialogue("TeleportMinigame");
		}
	}
	
}