package com.rs.game.player.content.quests;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;



public class CooksAssistant {

	public static void checkProgress(Player player) {
		if (player.cooksAssistant == 0) {
			player.getPackets().sendIComponentText(275, 16, "I can start this quest by speaking to the <col=db0000>Cook</col> in the");
			player.getPackets().sendIComponentText(275, 17, "Kitchen on the ground floor of <col=db0000>Lumbridge Castle.");
		} else if (player.cooksAssistant == 1) {
			player.getPackets().sendIComponentText(275, 16, "It's the <col=db0000>Duke of Lumbridge's</col> birthday and I have to help");
			player.getPackets().sendIComponentText(275, 17, "his <col=db0000>Cook</col> make him a <col=db0000>birthday cake</col>. To do this I need to");
			player.getPackets().sendIComponentText(275, 18, "bring him the following ingredients:");
			player.getPackets().sendIComponentText(275, 19, "I need to find a <col=db0000>bucket of milk.");
			player.getPackets().sendIComponentText(275, 20, "I need to find a <col=db0000>pot of flour.");
			player.getPackets().sendIComponentText(275, 21, "I need to find an <col=db0000>egg.");
			if (player.getInventory().containsOneItem(1927))
			player.getPackets().sendIComponentText(275, 19, "<str>I need to find a <col=db0000>bucket of milk.");
			if (player.getInventory().containsOneItem(1933))
			player.getPackets().sendIComponentText(275, 20, "<str>I need to find a <col=db0000>pot of flour.");
			if (player.getInventory().containsOneItem(1944))
			player.getPackets().sendIComponentText(275, 21, "<str>I need to find an <col=db0000>egg.");
		} else if (player.cooksAssistant == 3) {
			player.getPackets().sendIComponentText(275, 16, "<str>forgotten to buy the ingredients he needed to make him a");
			player.getPackets().sendIComponentText(275, 17, "<str>cake. I brought the cook an egg, some flour and some milk");
			player.getPackets().sendIComponentText(275, 18, "<str>and the cook made a delicious looking cake with them:");
			player.getPackets().sendIComponentText(275, 20, "<str>As a reward he now lets me use his high quality range");
			player.getPackets().sendIComponentText(275, 21, "<str>which lets me burn things less whenever I wish to cook");
			player.getPackets().sendIComponentText(275, 22, "<str>there.");
			player.getPackets().sendIComponentText(275, 23, "<col=db0000>QUEST COMPLETE!");
		}
	}
	
	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 310; i++) {
		player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendIComponentText(275, 2, "Cook's Assistant");
	}
	
	public static void sendCompleted(Player player) {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 1;
		player.getPackets().sendItemOnIComponent(277, 5, 1891, 1);
		player.getPackets().sendIComponentText(277, 3, "Congratulations!");
		player.getPackets().sendIComponentText(277, 4, "You have completed the Cook's Assistant Quest!");
		player.getPackets().sendIComponentText(277, 5, "You are awarded:");
		player.getPackets().sendIComponentText(277, 6, "Quest Points:");
		player.getPackets().sendIComponentText(277, 7, ""+player.questPoints+"");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "4500 Cooking XP");
		player.getPackets().sendIComponentText(277, 12, "Access to the cook's range");
		player.getPackets().sendIComponentText(277, 13, "");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getSkills().addXp(Skills.COOKING, 300);
	}

}