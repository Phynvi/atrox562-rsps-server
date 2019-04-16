package com.rs.game.player.starter;

import com.rs.Settings;
import com.rs.game.player.Player;
import com.rs.game.World;
import com.rs.game.player.content.clans.Clan;

/**
 * This class handles the giving of a starter kit.
 * 
 * @author Emperial
 * 
 */
public class Starter {

	public static final int MAX_STARTER_COUNT = 3;

	public static void appendStarter(Player player) {
		String ip = player.getSession().getIP();
		int count = StarterMap.getSingleton().getCount(ip);
		player.starter = 1;
		if (count >= MAX_STARTER_COUNT) {
			return;
		}
		player.getInterfaceManager().sendInterface(771);
		World.sendWorldMessage("<col=46A4FF>"+ player.getUsername() +" has joined "+ Settings.SERVER_NAME+" for the first time!", false);
		player.getInventory().addItem(995, 20000); // cash
		player.getInventory().addItem(330, 100); // 100 salmons
		player.getInventory().addItem(590, 1); //tinderbox
		player.getInventory().addItem(1351, 1); //bronze axe
		player.getInventory().addItem(1265, 1); //bronze pickaxe
		player.getInventory().addItem(303, 1); //fishing net
		player.getInventory().addItem(556, 25); //25 air runes
		player.getInventory().addItem(558, 25); //25 mind runes
		player.getInventory().addItem(557, 15); //15 earth runes
		player.getInventory().addItem(1375, 1); //bronze battleaxe
		player.getInventory().addItem(542, 1); //monk's robe
		player.getInventory().addItem(544, 1); //monk's robe
		player.getInventory().addItem(841, 1); //shortbow
		player.getInventory().addItem(882, 150); //150 bronze arrows
		player.getInventory().addItem(1129, 1); //leather body
		player.getInventory().addItem(1095, 1); //leather chaps
		player.getInventory().addItem(6199, 1); //mystery box
	//	player.softreset = true; 

		//player.getHintIconsManager().removeUnsavedHintIcon();
		player.getMusicsManager().reset();
		player.getCombatDefinitions().setAutoRelatie(true);
		player.getCombatDefinitions().refreshAutoRelatie();
		//player.getCutscenesManager().play(5);
		StarterMap.getSingleton().addIP(ip);
	}

}