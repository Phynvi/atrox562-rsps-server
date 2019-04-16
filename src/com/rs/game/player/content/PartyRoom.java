package com.rs.game.player.content;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.utils.EconomyPrices;
import com.rs.utils.ItemSetsKeyGenerator;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.WorldTile;
import com.rs.game.WorldObject;
import com.rs.game.ForceTalk;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class PartyRoom {
	
	public static int PARTY_CHEST_INTERFACE = 647;
	public static int INVENTORY_INTERFACE = 336;
	private static ItemsContainer<Item> items = new ItemsContainer<Item>(100, false);
	private static final int CHEST_INTERFACE_ITEMS_KEY = ItemSetsKeyGenerator.generateKey();
	
	/**
	 * If the knight dance is commenced.
	 */
	private static boolean dancing;
	
	public static void openPartyChest(final Player player) {
		player.getTemporaryAttributtes().put("PartyRoomInventory", Boolean.TRUE);
		player.getInterfaceManager().sendInterface(PARTY_CHEST_INTERFACE);
		player.getInterfaceManager().sendInventoryInterface(INVENTORY_INTERFACE);
		sendOptions(player);
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				player.getTemporaryAttributtes().remove("PartyRoomInventory");
			}
		});
	}
	
	private static void sendOptions(final Player player) {
		player.getPackets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 0, 93, 4, 7,
				"Deposit", "Deposit-5", "Deposit-10", "Deposit-All", "Deposit-X");
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0, 27, 1278);
		player.getPackets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 30, CHEST_INTERFACE_ITEMS_KEY, 4, 7, "Value");
		player.getPackets().sendIComponentSettings(PARTY_CHEST_INTERFACE, 30, 0, 27, 1150);
		player.getPackets().sendInterSetItemsOptionsScript(PARTY_CHEST_INTERFACE, 33, CHEST_INTERFACE_ITEMS_KEY, true, 4, 7, "Examine");
		player.getPackets().sendIComponentSettings(PARTY_CHEST_INTERFACE, 33, 0, 27, 1026);
		
	}
	
	public static int getTotalCoins() {
		int price = 0;
		for(Item item : items.getItems()) {
			if(item == null)
				continue;
			price += EconomyPrices.getPrice(item.getId());
		}
		return price;
	}
	
	public static void startParty(final Player player) {
		player.getInventory().deleteItem(995, 1000);
						World.spawnObject(new WorldObject(115, 10, 0, 3049, 3381, 0), true);
                        World.spawnObject(new WorldObject(116, 10, 0, 3045, 3380, 0), true);
                        World.spawnObject(new WorldObject(117, 10, 0, 3043, 3383, 0), true);
                        World.spawnObject(new WorldObject(118, 10, 0, 3041, 3380, 0), true);
                        World.spawnObject(new WorldObject(119, 10, 0, 3039, 3378, 0), true);
                        World.spawnObject(new WorldObject(120, 10, 0, 3041, 3377, 0), true);
                        World.spawnObject(new WorldObject(121, 10, 0, 3041, 3376, 0), true);
                        World.spawnObject(new WorldObject(122, 10, 0, 3042, 3373, 0), true);
                        World.spawnObject(new WorldObject(115, 10, 0, 3045, 3376, 0), true);
                        World.spawnObject(new WorldObject(116, 10, 0, 3046, 3375, 0), true);
                        World.spawnObject(new WorldObject(117, 10, 0, 3049, 3376, 0), true);
                        World.spawnObject(new WorldObject(118, 10, 0, 3052, 3378, 0), true);
                        World.spawnObject(new WorldObject(119, 10, 0, 3050, 3379, 0), true);
                        World.spawnObject(new WorldObject(120, 10, 0, 3046, 3382, 0), true);
                        World.spawnObject(new WorldObject(121, 10, 0, 3044, 3377, 0), true);
                        World.spawnObject(new WorldObject(122, 10, 0, 3043, 3375, 0), true);
                        World.spawnObject(new WorldObject(115, 10, 0, 3047, 3374, 0), true);
                        World.spawnObject(new WorldObject(116, 10, 0, 3050, 3374, 0), true);
                        World.spawnObject(new WorldObject(117, 10, 0, 3051, 3377, 0), true);
                        World.spawnObject(new WorldObject(118, 10, 0, 3047, 3379, 0), true);
                        World.spawnObject(new WorldObject(119, 10, 0, 3048, 3382, 0), true);
                        World.spawnObject(new WorldObject(120, 10, 0, 3045, 3383, 0), true);
                        World.spawnObject(new WorldObject(121, 10, 0, 3043, 3381, 0), true);
                        World.spawnObject(new WorldObject(122, 10, 0, 3039, 3380, 0), true);
                        World.spawnObject(new WorldObject(115, 10, 0, 3041, 3378, 0), true);
                        World.spawnObject(new WorldObject(116, 10, 0, 3038, 3374, 0), true);
                        World.spawnObject(new WorldObject(117, 10, 0, 3040, 3377, 0), true);
                        World.spawnObject(new WorldObject(118, 10, 0, 3041, 3374, 0), true);
                        World.spawnObject(new WorldObject(119, 10, 0, 3047, 3377, 0), true);
                        World.spawnObject(new WorldObject(120, 10, 0, 3045, 3382, 0), true);
                        World.spawnObject(new WorldObject(121, 10, 0, 3050, 3383, 0), true);
                        World.spawnObject(new WorldObject(122, 10, 0, 3051, 3380, 0), true);
	}
	
	public static void purchase(final Player player, boolean balloons) {
		if(balloons) {
			if(player.getInventory().containsItem(995, 1000)) {
				startParty(player);
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage", "Balloon Bonanza costs 1000 coins.");
			}
		} else {
			if(player.getInventory().containsItem(995, 500)) {
				startDancingKnights();
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage", "Nightly Dance costs 500 coins.");
			}
		}
	}
	
	public static void startDancingKnights() {
		
	}
}