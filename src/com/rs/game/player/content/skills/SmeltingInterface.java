package com.rs.game.player.content.skills;

import com.rs.game.player.Player;
import com.rs.game.player.skills.smithing.Smelting.Bars;



public class SmeltingInterface {
	
	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendChatBoxInterface(311);
		player.getPackets().sendItemOnIComponent(311, 4, 2349, 150);
		player.getPackets().sendItemOnIComponent(311, 5, 9467, 150);
		player.getPackets().sendItemOnIComponent(311, 6, 2351, 150);
		player.getPackets().sendItemOnIComponent(311, 7, 2355, 150);
		player.getPackets().sendItemOnIComponent(311, 8, 2353, 150);
		player.getPackets().sendItemOnIComponent(311, 9, 2357, 150);
		player.getPackets().sendItemOnIComponent(311, 10, 2359, 150);
		player.getPackets().sendItemOnIComponent(311, 11, 2361, 150);
		player.getPackets().sendItemOnIComponent(311, 12, 2363, 150);
	}

}