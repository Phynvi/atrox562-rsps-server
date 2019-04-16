package com.rs.game.player.content.custom;

import com.rs.game.player.Player;
import com.rs.game.WorldTile;

public class SlayerTeleports {
	
	private static int destination;
	
	public static void sendInter(Player player) {
		player.getInterfaceManager().sendInterface(625);
		player.getPackets().sendIComponentText(625, 45, "Please select your destination on the options below.");
		//player.getPackets().sendNPCOnIComponent(625, 39, 1);
		player.getPackets().sendItemOnIComponent(625, 39, 4151, 1);
		player.getPackets().sendIComponentText(625, 67, "Slayer Locations");
		player.getPackets().sendIComponentText(625, 68, "Slayer Tower");
		player.getPackets().sendIComponentText(625, 71, "Fremennik");
		player.getPackets().sendIComponentText(625, 74, "Ancient Cavern");
		player.getPackets().sendIComponentText(625, 77, "Poison Waste");
		player.getPackets().sendIComponentText(625, 80, "Dorgesh-Kaan");
		player.getPackets().sendIComponentText(625, 83, "----");
	}
	
	public static void handleButtonInterface(Player player, int componentId) {
		if (componentId == 70) {
			destination = 1;
		} else if (componentId == 73) {
			destination = 2;
		} else if (componentId == 76) {
			destination = 3;
		} else if (componentId == 79) {
			destination = 4;
		} else if (componentId == 82) {
			destination = 5;
		} else if (componentId == 85) {
			destination = 6;
		} else if (componentId == 106 && destination == 1) {
			destination = 0;
			player.setNextWorldTile(new WorldTile(3429, 3532, 0)); //slayer tower
			player.closeInterfaces();
		} else if (componentId == 106 && destination == 2) {
			destination = 0;
			player.setNextWorldTile(new WorldTile(2794, 3615, 0)); //fremmy dungeon
			player.closeInterfaces();
		} else if (componentId == 106 && destination == 3) {
			destination = 0;
			player.setNextWorldTile(new WorldTile(2511, 3518, 0)); //ancient cavern
			player.closeInterfaces();
		} else if (componentId == 106 && destination == 4) {
			destination = 0;
			player.setNextWorldTile(new WorldTile(2317, 3092, 0)); //poison waste
			player.closeInterfaces();
		} else if (componentId == 106 && destination == 5) {
			destination = 0;
			player.setNextWorldTile(new WorldTile(2718, 5339, 0)); //Dorgesh-Kaan
			player.closeInterfaces();
		} else if (componentId == 106 && destination == 6) {
			destination = 0;
		}
	}
	
}