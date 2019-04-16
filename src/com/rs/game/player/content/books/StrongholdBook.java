package com.rs.game.player.content.books;

import com.rs.game.player.Player;

public class StrongholdBook {
	
	public static void openBook(Player player) {
		player.getInterfaceManager().sendInterface(26);
		for (int i = 0; i < 102; i++) {
		player.getPackets().sendIComponentText(26, i, "");
		}
		player.getPackets().sendIComponentText(26, 101, "Stronghold of Security - Notes");
		player.getPackets().sendIComponentText(26, 97, "<col=0000FF>Description");
		player.getPackets().sendIComponentText(26, 68, "This stronghold was");
		player.getPackets().sendIComponentText(26, 69, "unearthed by a miner");
		player.getPackets().sendIComponentText(26, 70, "prospecting for new ores");
		player.getPackets().sendIComponentText(26, 71, "around the Barbarian Village.");
		player.getPackets().sendIComponentText(26, 72, "After gathering some");
		player.getPackets().sendIComponentText(26, 73, "equipment he ventured into");
		player.getPackets().sendIComponentText(26, 74, "the maze of tunnels and was");
		player.getPackets().sendIComponentText(26, 75, "missing for a long time. He");
		player.getPackets().sendIComponentText(26, 76, "finally emerged along with");
		player.getPackets().sendIComponentText(26, 77, "copious notes regarding the");
		player.getPackets().sendIComponentText(26, 78, "new beasts and strange");
		player.getPackets().sendIComponentText(26, 79, "experiences which had befallen");
		player.getPackets().sendIComponentText(26, 80, "him. He also mentioned that");
		player.getPackets().sendIComponentText(26, 81, "there was treasure to be had,");
		player.getPackets().sendIComponentText(26, 82, "but no one has been able to");
		player.getPackets().sendIComponentText(26, 83, "wring a word from him about");
		player.getPackets().sendIComponentText(26, 84, "this, he simply flapped his");
		player.getPackets().sendIComponentText(26, 85, "arms and slapped his head.");
		player.getPackets().sendIComponentText(26, 86, "This book details his notes");
		player.getPackets().sendIComponentText(26, 87, "and my diary of exploration.");
		player.getPackets().sendIComponentText(26, 88, "I am exploring to see if I");
		player.getPackets().sendIComponentText(26, 89, "can find out more...");
		player.getPackets().sendIComponentText(26, 66, "2");
	}

}
