package com.rs.game.player.content.quests;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;



public class WolfWhistle {

	public static void checkProgress(Player player) {
		if (player.wolfWhistle == 0) {
			player.getPackets().sendIComponentText(275, 16, "I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<col=db0000>Taverley</col>.");
		} else if (player.wolfWhistle == 1) {
			player.getPackets().sendIComponentText(275, 16, "<str>I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<str><col=db0000>Taverley</col>.");
			player.getPackets().sendIComponentText(275, 18, "I should go upstairs to find out who is making all the noice.");
		} else if (player.wolfWhistle == 2) {
			player.getPackets().sendIComponentText(275, 16, "<str>I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<str><col=db0000>Taverley</col>.");
			player.getPackets().sendIComponentText(275, 18, "<str>I should go upstairs to find out who is making all the noice.");
			player.getPackets().sendIComponentText(275, 19, "I should talk to <col=db0000>Pikkupstix</col> and tell him");
			player.getPackets().sendIComponentText(275, 20, "what I've seen.");
		} else if (player.wolfWhistle == 3) {
			player.getPackets().sendIComponentText(275, 16, "<str>I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<str><col=db0000>Taverley</col>.");
			player.getPackets().sendIComponentText(275, 18, "<str>I should go upstairs to find out who is making all the noice.");
			player.getPackets().sendIComponentText(275, 19, "<str>I should talk to <col=db0000>Pikkupstix</col> and tell him");
			player.getPackets().sendIComponentText(275, 20, "<str>what I've seen.");
			player.getPackets().sendIComponentText(275, 21, "I need to bring 2 <col=db0000>Wolf bones</col> to <col=db0000>Pikkupstix</col>.");
		} else if (player.wolfWhistle == 4) {
			player.getPackets().sendIComponentText(275, 16, "<str>I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<str><col=db0000>Taverley</col>.");
			player.getPackets().sendIComponentText(275, 18, "<str>I should go upstairs to find out who is making all the noice.");
			player.getPackets().sendIComponentText(275, 19, "<str>I should talk to <col=db0000>Pikkupstix</col> and tell him");
			player.getPackets().sendIComponentText(275, 20, "<str>what I've seen.");
			player.getPackets().sendIComponentText(275, 21, "<str>I need to bring 2 <col=db0000>Wolf bones</col> to <col=db0000>Pikkupstix</col>.");
			player.getPackets().sendIComponentText(275, 22, "I should go down to the cellar and make a <col=db0000>spirit wolf pouch</col> and");
			player.getPackets().sendIComponentText(275, 23, "some <col=db0000>Howl scrolls.");
		} else if (player.wolfWhistle == 5) {
			player.getPackets().sendIComponentText(275, 16, "<str>I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<str><col=db0000>Taverley</col>.");
			player.getPackets().sendIComponentText(275, 18, "<str>I should go upstairs to find out who is making all the noice.");
			player.getPackets().sendIComponentText(275, 19, "<str>I should talk to <col=db0000>Pikkupstix</col> and tell him");
			player.getPackets().sendIComponentText(275, 20, "<str>what I've seen.");
			player.getPackets().sendIComponentText(275, 21, "<str>I need to bring 2 <col=db0000>Wolf bones</col> to <col=db0000>Pikkupstix</col>.");
			player.getPackets().sendIComponentText(275, 22, "<str>I should go down to the cellar and make a <col=db0000>spirit wolf pouch</col> and");
			player.getPackets().sendIComponentText(275, 23, "<str>some <col=db0000>Howl scrolls.");
			player.getPackets().sendIComponentText(275, 24, "Go upstairs and scare away the <col=db0000>giant wolpertinger");
			player.getPackets().sendIComponentText(275, 25, "using your familiars special attack <col=db0000>Howl.");
		} else if (player.wolfWhistle == 6) {
			player.getPackets().sendIComponentText(275, 16, "<str>I can begin this quest by talking to <col=db0000>Pikkupstix</col>, who lives in");
			player.getPackets().sendIComponentText(275, 17, "<str><col=db0000>Taverley</col>.");
			player.getPackets().sendIComponentText(275, 18, "<str>I should go upstairs to find out who is making all the noice.");
			player.getPackets().sendIComponentText(275, 19, "<str>I should talk to <col=db0000>Pikkupstix</col> and tell him");
			player.getPackets().sendIComponentText(275, 20, "<str>what I've seen.");
			player.getPackets().sendIComponentText(275, 21, "<str>I need to bring 2 <col=db0000>Wolf bones</col> to <col=db0000>Pikkupstix</col>.");
			player.getPackets().sendIComponentText(275, 22, "<str>I should go down to the cellar and make a <col=db0000>spirit wolf pouch</col> and");
			player.getPackets().sendIComponentText(275, 23, "<str>some <col=db0000>Howl scrolls.");
			player.getPackets().sendIComponentText(275, 24, "<str>Go upstairs and scare away the <col=db0000>giant wolpertinger");
			player.getPackets().sendIComponentText(275, 25, "<str>using your familiars special attack <col=db0000>Howl.");
			player.getPackets().sendIComponentText(275, 26, "<col=db0000>QUEST COMPLETE!");
		}
	}
	
	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 310; i++) {
		player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendIComponentText(275, 2, "Wolf Whistle");
	}
	
	public static void sendCompleted(Player player) {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 1;
		player.getPackets().sendItemOnIComponent(277, 5, 12047, 1);
		player.getPackets().sendIComponentText(277, 3, "Congratulations!");
		player.getPackets().sendIComponentText(277, 4, "You have completed Wolf Whistle!");
		player.getPackets().sendIComponentText(277, 5, "You are awarded:");
		player.getPackets().sendIComponentText(277, 6, "Quest Points:");
		player.getPackets().sendIComponentText(277, 7, ""+player.questPoints+"");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "4140 Summoning XP");
		player.getPackets().sendIComponentText(277, 12, "275 Gold charms");
		player.getPackets().sendIComponentText(277, 13, "Ability to train Summoning");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getSkills().addXp(Skills.SUMMONING, 276);
		player.getInventory().addItem(12158, 275);
	}

}