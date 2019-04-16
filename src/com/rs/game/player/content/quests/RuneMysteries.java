package com.rs.game.player.content.quests;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;



public class RuneMysteries {

	public static void checkProgress(Player player) {
		if (player.runeMysteries == 0) {
			player.getPackets().sendIComponentText(275, 16, "I can start this quest by speaking to <col=db0000>Duke Horacio of");
			player.getPackets().sendIComponentText(275, 17, "<col=db0000>Lumbridge</col>, upstairs in <col=db0000>Lumbridge Castle.");
		} else if (player.runeMysteries == 1) {
			player.getPackets().sendIComponentText(275, 16, "<str>I spoke to Duke Horacio and he showed me a strange");
			player.getPackets().sendIComponentText(275, 17, "<str>talisman that had been found by one of his subjects.");
			player.getPackets().sendIComponentText(275, 18, "<str>I agreed to take it to the Wizard's Tower, South West of");
			player.getPackets().sendIComponentText(275, 19, "<str>Lumbridge for further examination by the wizards.");
			player.getPackets().sendIComponentText(275, 20, "I need to find the <col=db0000>Head Wizard</col> and give him the <col=db0000>Talisman");
		} else if (player.runeMysteries == 2) {
			player.getPackets().sendIComponentText(275, 16, "<str>I spoke to Duke Horacio and he showed me a strange");
			player.getPackets().sendIComponentText(275, 17, "<str>talisman that had been found by one of his subjects");
			player.getPackets().sendIComponentText(275, 18, "<str>I agreed to take it to the Wizards' Tower, South West of");
			player.getPackets().sendIComponentText(275, 19, "<str>Lumbridge for further examination by the wizards.");
			player.getPackets().sendIComponentText(275, 20, "<str>I gave the Talisman to the Head Wizard of the Tower and");
			player.getPackets().sendIComponentText(275, 21, "<str>agreed to help him with his research into rune stones.");
			player.getPackets().sendIComponentText(275, 22, "I should take this <col=db0000>Research Package</col> to <col=db0000>Aubury</col> in <col=db0000>Varrock");
		} else if (player.runeMysteries == 3) {
			player.getPackets().sendIComponentText(275, 16, "<str>I spoke to Duke Horacio and he showed me a strange");
			player.getPackets().sendIComponentText(275, 17, "<str>talisman that had been found by one of his subjects");
			player.getPackets().sendIComponentText(275, 18, "<str>I agreed to take it to the Wizards' Tower, South West of");
			player.getPackets().sendIComponentText(275, 19, "<str>Lumbridge for further examination by the wizards.");
			player.getPackets().sendIComponentText(275, 20, "<str>I gave the Talisman to the Head Wizard of the Tower and");
			player.getPackets().sendIComponentText(275, 21, "<str>agreed to help him with his research into rune stones.");
			player.getPackets().sendIComponentText(275, 22, "<str>I took the research package to Varrock and delivered it.");
			player.getPackets().sendIComponentText(275, 23, "I should speak to <col=db0000>Aubury</col> again when he has finished");
			player.getPackets().sendIComponentText(275, 24, "examining the <col=db0000>research package</col> I have delivered him.");
		} else if (player.runeMysteries == 4) {
			player.getPackets().sendIComponentText(275, 16, "<str>I spoke to Duke Horacio and he showed me a strange");
			player.getPackets().sendIComponentText(275, 17, "<str>talisman that had been found by one of his subjects");
			player.getPackets().sendIComponentText(275, 18, "<str>I agreed to take it to the Wizards' Tower, South West of");
			player.getPackets().sendIComponentText(275, 19, "<str>Lumbridge for further examination by the wizards.");
			player.getPackets().sendIComponentText(275, 20, "<str>I gave the Talisman to the Head Wizard of the Tower and");
			player.getPackets().sendIComponentText(275, 21, "<str>agreed to help him with his research into rune stones.");
			player.getPackets().sendIComponentText(275, 22, "<str>I took the research package to Varrock and delivered it.");
			player.getPackets().sendIComponentText(275, 23, "<str>Aubury was interested in the research package and gave");
			player.getPackets().sendIComponentText(275, 24, "<str>me his own research notes to deliver to Sedridor.");
			player.getPackets().sendIComponentText(275, 25, "I should take the <col=db0000>notes</col> to <col=db0000>Sedridor</col> and see what he says.");
		} else if (player.runeMysteries == 5) {
			player.getPackets().sendIComponentText(275, 16, "<str>I spoke to Duke Horacio and he showed me a strange");
			player.getPackets().sendIComponentText(275, 17, "<str>talisman that had been found by one of his subjects");
			player.getPackets().sendIComponentText(275, 18, "<str>I agreed to take it to the Wizards' Tower, South West of");
			player.getPackets().sendIComponentText(275, 19, "<str>Lumbridge for further examination by the wizards.");
			player.getPackets().sendIComponentText(275, 20, "<str>I gave the Talisman to the Head Wizard of the Tower and");
			player.getPackets().sendIComponentText(275, 21, "<str>agreed to help him with his research into rune stones.");
			player.getPackets().sendIComponentText(275, 22, "<str>I took the research package to Varrock and delivered it.");
			player.getPackets().sendIComponentText(275, 23, "<str>Aubury was interested in the research package and gave");
			player.getPackets().sendIComponentText(275, 24, "<str>me his own research notes to deliver to Sedridor.");
			player.getPackets().sendIComponentText(275, 25, "<str>I brought Sedridor the research notes that Aubury had");
			player.getPackets().sendIComponentText(275, 26, "<str>compiled so that he could compare their research. They");
			player.getPackets().sendIComponentText(275, 27, "<str>discovered that it was now possible to create new rune");
			player.getPackets().sendIComponentText(275, 28, "<str>stones, a skill that had been thought last forever.");
			player.getPackets().sendIComponentText(275, 29, "<str>In return for all of my help they thought me how to do this,");
			player.getPackets().sendIComponentText(275, 30, "<str>and will teleport me to mine blank runes anytime.");
			player.getPackets().sendIComponentText(275, 31, "			<col=db0000>QUEST COMPLETE!");
			}
		}
	
	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendInterface(275);
		for (int i = 0; i < 310; i++) {
		player.getPackets().sendIComponentText(275, i, "");
		}
		player.getPackets().sendIComponentText(275, 2, "Rune Mysteries");
	}
	
	public static void sendCompleted(Player player) {
		player.getInterfaceManager().sendInterface(277);
		player.questPoints += 1;
		player.runeMysteries = 5;
		player.getPackets().sendItemOnIComponent(277, 5, 1438, 1);
		player.getPackets().sendIComponentText(277, 3, "Congratulations!");
		player.getPackets().sendIComponentText(277, 4, "You have completed the Rune Mysteries Quest!");
		player.getPackets().sendIComponentText(277, 5, "You are awarded:");
		player.getPackets().sendIComponentText(277, 6, "Quest Points:");
		player.getPackets().sendIComponentText(277, 7, ""+player.questPoints+"");
		player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
		player.getPackets().sendIComponentText(277, 11, "Runecrafting skill");
		player.getPackets().sendIComponentText(277, 12, "Air talisman");
		player.getPackets().sendIComponentText(277, 13, "");
		player.getPackets().sendIComponentText(277, 14, "");
		player.getPackets().sendIComponentText(277, 15, "");
		player.getPackets().sendIComponentText(277, 16, "");
		player.getPackets().sendIComponentText(277, 17, "");
		player.getInventory().deleteItem(291, 1);
		player.getInventory().addItem(1438, 1);
		player.getPackets().sendConfig(63, 6);
	}

}