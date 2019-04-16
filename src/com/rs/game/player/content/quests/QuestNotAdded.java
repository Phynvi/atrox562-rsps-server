package com.rs.game.player.content.quests;

import com.rs.Settings;
import com.rs.game.player.Player;



public class QuestNotAdded {

	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 310; i++) {
		player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendIComponentText(275, 18, "This quest has not been added to "+ Settings.SERVER_NAME+" yet,");
		player.getPackets().sendIComponentText(275, 19, "but if you like this quest you can make a suggestion at");
		player.getPackets().sendIComponentText(275, 20, "our ::forums and we might start making it sooner.");
		player.getPackets().sendIComponentText(275, 2, "Not added.");
	}

}