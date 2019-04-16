package com.rs.game.player.content.quests;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;



public class DoricsQuest {

	public static void checkProgress(Player player) {
		if (player.doricsQuest == 0) {
			player.getPackets().sendIComponentText(275, 16, "I can start this quest by speaking to <col=db0000>Doric</col> who is <col=db0000>North of");
			player.getPackets().sendIComponentText(275, 17, "<col=db0000>Falador");
			player.getPackets().sendIComponentText(275, 19, "There aren't any requirement but <col=db0000>Level 15 Mining</col> will help.");
		} else if (player.doricsQuest == 1) {
			player.getPackets().sendIComponentText(275, 16, "<str>I have spoken to <col=db0000>Doric");
			player.getPackets().sendIComponentText(275, 18, "I need to collect some items and bring them to <col=db0000>Doric");
			player.getPackets().sendIComponentText(275, 19, "<col=db0000>6 Clay");
			player.getPackets().sendIComponentText(275, 20, "<col=db0000>4 Copper Ore");
			player.getPackets().sendIComponentText(275, 21, "<col=db0000>2 Iron Ore");
			if (player.getInventory().containsItem(434, 6))
			player.getPackets().sendIComponentText(275, 19, "<str><col=db0000>6 Clay");
			if (player.getInventory().containsItem(436, 4))
			player.getPackets().sendIComponentText(275, 20, "<str><col=db0000>4 Copper Ore");
			if (player.getInventory().containsItem(440, 2))
			player.getPackets().sendIComponentText(275, 21, "<str><col=db0000>2 Iron Ore");
		} else if (player.doricsQuest == 2) {
			player.getPackets().sendIComponentText(275, 16, "<str>I have spoken to <col=db0000>Doric");
			player.getPackets().sendIComponentText(275, 18, "<str>I have collected some Clay, Copper Ore and Iron Ore");
			player.getPackets().sendIComponentText(275, 20, "<str>Doric rewarded me for all my hard work");
			player.getPackets().sendIComponentText(275, 21, "<str>I can now use Doric's Anvils whenever I want");
			player.getPackets().sendIComponentText(275, 22, "<col=db0000>QUEST COMPLETE!");
			}
		}
	
	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 310; i++) {
		player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendIComponentText(275, 2, "Doric's Quest");
	}
	
	public static void sendCompleted(Player player) {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 1;
		player.doricsQuest = 2;
		player.getPackets().sendItemOnIComponent(277, 5, 1269, 1);
		player.getPackets().sendIComponentText(277, 3, "Congratulations!");
		player.getPackets().sendIComponentText(277, 4, "You have completed Doric's Quest!");
		player.getPackets().sendIComponentText(277, 5, "You are awarded:");
		player.getPackets().sendIComponentText(277, 6, "Quest Points:");
		player.getPackets().sendIComponentText(277, 7, ""+player.questPoints+"");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "4500 Mining XP");
		player.getPackets().sendIComponentText(277, 12, "900 Coins");
		player.getPackets().sendIComponentText(277, 13, "Use of Doric's Anvils");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getSkills().addXp(Skills.MINING, 300);
		player.getInventory().deleteItem(434, 6);
		player.getInventory().deleteItem(436, 4);
		player.getInventory().deleteItem(440, 2);
		player.getInventory().addItem(995, 900);
		player.getPackets().sendConfig(31, 100);
	}

}