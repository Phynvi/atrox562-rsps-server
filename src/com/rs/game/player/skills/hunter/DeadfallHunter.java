package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.WorldObject;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Skills;

/**
 * Handles all possible deadfall methods.
 * 
 * @author Kris
 */
public class DeadfallHunter extends Action {

	public enum DeadfallPreys {

		WILD_KEBBIT(5089, 23, 19215, 19208, new Item(10113, 1), 128, new Animation(5208), new Animation(5208)),
		BARB_TAILED_KEBBIT(5088, 33, 19218, 19212, new Item(10129, 1), 168, new Animation(5208), new Animation(5208)),
		PRICKLY_KEBBIT(5086, 37, 19217, 19214, new Item(10105, 1), 204, new Animation(5208), new Animation(5208)),
		DISEASED_KEBBIT(7039, 44, 28942, 28940, new Item(12567, 1), 200, new Animation(5208), new Animation(5208)),
		SABRE_TOOTHED_KEBBIT(5087, 51, 19216, 19209, new Item(10109, 1), 200, new Animation(5208), new Animation(5208)),
		PENGUIN(5428, 51, 43467, 43469, null, 200, new Animation(5208), new Animation(5208));

		private Item loot;
		private int experience, npcId, successfulObjectId, level, lootableObject;
		private Animation placingTrap;
		private Animation dismantlingTrap;

		static final Map<Integer, DeadfallPreys> npc = new HashMap<Integer, DeadfallPreys>();
		static final Map<Integer, DeadfallPreys> object = new HashMap<Integer, DeadfallPreys>();

		static {
			for (DeadfallPreys npcs : DeadfallPreys.values())
				npc.put(npcs.npcId, npcs);
			for (DeadfallPreys objects : DeadfallPreys.values())
				object.put(objects.lootableObject, objects);
		}

		public static DeadfallPreys forObjectId(int id) {
			return object.get(id);
		}

		public static DeadfallPreys forId(int id) {
			return npc.get(id);
		}

		private DeadfallPreys(int npcId, int level, int lootableObject, int successfulObjectId, Item loot, int experience, Animation placingTrap, Animation dismantlingTrap) {
			this.npcId = npcId;
			this.level = level;
			this.lootableObject = lootableObject;
			this.successfulObjectId = successfulObjectId;
			this.loot = loot;
			this.experience = experience;
			this.placingTrap = placingTrap;
			this.dismantlingTrap = dismantlingTrap;
		}

		public Item getLoot() {
			return loot;
		}

		public int getExperience() {
			return experience;
		}

		public Animation getPlacingAnimation() {
			return placingTrap;
		}

		public Animation getDismantlingAnimation() {
			return dismantlingTrap;
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

	public DeadfallHunter(WorldObject object) {
		this.object = object;
	}

	@Override
	public boolean start(Player player) {
		int trapAmt = HunterCore.getTrapAmount(player);
		if (OwnedObjectManager.getObjectsforValue(player, 19205) == trapAmt) {
			player.getPackets().sendGameMessage("You can't setup more than " + trapAmt + " traps.");
			return false;
		}
		if (!player.getInventory().containsItem(946, 1) || !player.getInventory().containsItem(HunterCore.getLogsId(player), 1)) {
			player.sendMessage("You need a knife and some logs to set up a deadfall trap.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.HUNTER) < 23) {
			player.sendMessage("You need at least level 23 Hunter to setup a deadfall trap.");
			return false;
		}
		player.sendMessage("You start setting up the trap..");
		player.setNextAnimation(new Animation(5208));
		player.getInventory().deleteItem(HunterCore.getLogsId(player), 1);
		player.lock(5);
		setActionDelay(player, 5);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		new OwnedObjectManager(player, new WorldObject[] { new WorldObject(19206, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()) }, 600000);
		return -1;
	}

	@Override
	public void stop(final Player player) {
	}

}