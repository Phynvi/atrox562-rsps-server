package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.WorldTile;
import com.rs.game.player.OwnedObjectManager;

/**
 * Handles all possible net trapping methods.
 * 
 * @author Kris
 */
public class NetTrapping extends Action {

	public enum NetPreys {

		SWAMP_LIZARD(5117, 29, 19675, 19674, new Item(10149, 1), 152),
		//SQUIRREL(),
		ORANGE_SALAMANDER(5114, 47, 19654, 19655, new Item(10146, 1), 224),
		//PENGUIN(),
		RED_SALAMANDER(5115, 59, 19659, 19658, new Item(10147, 1), 272),
		BLACK_SALAMANDER(5116, 67, 19667, 19666, new Item(10148, 1), 304);

		private Item loot;
		private int experience, npcId, successfulObjectId, level, lootableObject;

		static final Map<Integer, NetPreys> npc = new HashMap<Integer, NetPreys>();
		static final Map<Integer, NetPreys> object = new HashMap<Integer, NetPreys>();

		static {
			for (NetPreys npcs : NetPreys.values())
				npc.put(npcs.npcId, npcs);
			for (NetPreys objects : NetPreys.values())
				object.put(objects.lootableObject, objects);
		}

		public static NetPreys forObjectId(int id) {
			return object.get(id);
		}

		public static NetPreys forId(int id) {
			return npc.get(id);
		}

		private NetPreys(int npcId, int level, int lootableObject, int successfulObjectId, Item loot, int experience) {
			this.npcId = npcId;
			this.level = level;
			this.lootableObject = lootableObject;
			this.successfulObjectId = successfulObjectId;
			this.loot = loot;
			this.experience = experience;
		}

		public Item getLoot() {
			return loot;
		}

		public int getExperience() {
			return experience;
		}

		public int getSuccessfulObjectId() {
			return successfulObjectId;
		}

		public int getLootableObjectId() {
			return lootableObject;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevel() {
			return level;
		}
	}

	private WorldObject object;

	public NetTrapping(WorldObject object) {
		this.object = object;
	}

	@Override
	public boolean start(Player player) {
		int trapAmt = HunterCore.getTrapAmount(player);
		if (OwnedObjectManager.getObjectsforValue(player, object.getId()) == trapAmt) {
			player.getPackets().sendGameMessage("You can't setup more than " + trapAmt + " traps.");
			return false;
		}
		if (!player.getInventory().containsItem(303, 1) || !player.getInventory().containsItem(954, 1)) {
			player.sendMessage("You need a small fishing net and a rope to set up a net trap.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.HUNTER) < 29) {
			player.sendMessage("You need at least level 29 Hunter to setup a net trap.");
			return false;
		}
		player.sendMessage("You start setting up the trap..");
		player.setNextAnimation(new Animation(5215));
		player.getInventory().deleteItem(303, 1);
		player.getInventory().deleteItem(954, 1);
		player.lock(2);
		setActionDelay(player, 2);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}
	
	private WorldTile getLocation() {
		switch(object.getRotation()) {
		case 0:
			return new WorldTile(object.getX(), object.getY() + 1, object.getPlane());
		case 1:
			return new WorldTile(object.getX() + 1, object.getY(), object.getPlane());
		case 2:
			return new WorldTile(object.getX(), object.getY() - 1, object.getPlane());
		default:
			return new WorldTile(object.getX() - 1, object.getY(), object.getPlane());
		}
	}

	@Override
	public int processWithDelay(Player player) {
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		WorldTile tile = getLocation();
		new OwnedObjectManager(player, new WorldObject[] { new WorldObject(19670, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()) }, 600000);
		new OwnedObjectManager(player, new WorldObject[] { new WorldObject(19673, object.getType(), object.getRotation(), tile.getX(), tile.getY(), tile.getPlane()) }, 600000);
		return -1;
	}

	@Override
	public void stop(final Player player) {}

}