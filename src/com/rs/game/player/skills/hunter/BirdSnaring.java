package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.game.player.OwnedObjectManager;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.game.player.Skills;
import com.rs.game.player.Player;
import com.rs.game.World;

public class BirdSnaring extends Action {

	public enum Birds {

		CRIMSON_SWIFT(5073, new Item[] { new Item(10088), new Item(526, 1), new Item(9978, 1) }, 1, 34, 19180, 19174, new Animation(6775), new Animation(6774)),
		GOLDEN_WARBLER(5075, new Item[] { new Item(1583), new Item(526, 1), new Item(9978, 1) }, 5, 48, 19184, 19174, new Animation(6775), new Animation(6774)),
		COPPER_LONGTAIL(5076, new Item[] { new Item(10091), new Item(526, 1), new Item(9978, 1) }, 9, 61, 19186, 19174, new Animation(6775), new Animation(6774)),
		CERULEAN_TWITCH(5074, new Item[] { new Item(10089), new Item(526, 1), new Item(9978, 1) }, 11, 64.67, 19182, 19174, new Animation(6775), new Animation(6774)),
		TROPICAL_WAGTAIL(5072, new Item[] { new Item(10087), new Item(526, 1), new Item(9978, 1) }, 19, 95.2, 19178, 19174, new Animation(6775), new Animation(6774)),
		WIMPY_BIRD(7031, new Item[] { new Item(11525, 1), new Item(526, 1), new Item(9978, 1) }, 39, 167, 28930, 19174, new Animation(6775), new Animation(6774));

		public static Birds forObjectId(int id) {
			return object.get(id);
		}

		private int npcId, level, successfulTransformObjectId, failedTransformObjectId;
		private Item[] item;
		private double xp;
		private Animation successCatchAnim, failCatchAnim;
		static final Map<Integer, Birds> npc = new HashMap<Integer, Birds>();
		static final Map<Integer, Birds> object = new HashMap<Integer, Birds>();

		static {
			for (Birds npcs : Birds.values())
				npc.put(npcs.npcId, npcs);
			for (Birds objets : Birds.values())
				object.put(objets.successfulTransformObjectId, objets);
		}

		public static Birds forId(int id) {
			return npc.get(id);
		}

		private Birds(int npcId, Item[] item, int level, double xp, int successfulTransformObjectId, int failedTransformObjectId, Animation successCatchAnim, Animation failCatchAnim) {
			this.npcId = npcId;
			this.item = item;
			this.level = level;
			this.xp = xp;
			this.successfulTransformObjectId = successfulTransformObjectId;
			this.failedTransformObjectId = failedTransformObjectId;
			this.successCatchAnim = successCatchAnim;
			this.failCatchAnim = failCatchAnim;
		}

		public Animation getFailCatchAnim() {
			return failCatchAnim;
		}

		public int getFailedTransformObjectId() {
			return failedTransformObjectId;
		}

		public Item[] getItems() {
			return item;
		}

		public int getLevel() {
			return level;
		}

		public int getNpcId() {
			return npcId;
		}

		public Animation getSuccessCatchAnim() {
			return successCatchAnim;
		}

		public int getSuccessfulTransformObjectId() {
			return successfulTransformObjectId;
		}

		public double getXp() {
			return xp;
		}
	}

	public int getTrapAmount(Player player) {
		int level = 20;
		int trapAmount = 2;
		for (int i = 0; i < 3; i++) {
			if (player.getSkills().getLevel(Skills.HUNTER) >= level) {
				trapAmount++;
				level += 20;
			}
		}
		return trapAmount;
	}

	@Override
	public boolean start(Player player) {
		int trapAmt = getTrapAmount(player);
		if (OwnedObjectManager.getObjectsforValue(player, 19175) == trapAmt) {
			player.getPackets().sendGameMessage("You can't setup more than " + trapAmt + " traps.");
			return false;
		}
		List<WorldObject> objects = World.getRegion(player.getRegionId()).getSpawnedObjects();
		if (objects != null) {
			for (WorldObject object : objects) {
				if (object.getX() == player.getX() && object.getY() == player.getY() && object.getPlane() == player.getPlane()) {
					player.sendMessage("You can't setup your trap here.");
					return false;
				}
			}
		}
		player.sendMessage("You start setting up the trap..");
		player.setNextAnimation(new Animation(5208));
		player.lock(3);
		setActionDelay(player, 2);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		player.getInventory().deleteItem(10006, 1);
		new OwnedObjectManager(player, new WorldObject[] { new WorldObject(19175, 10, 0, player.getX(), player.getY(), player.getPlane()) }, 600000);
		return -1;
	}

	@Override
	public void stop(Player player) {
	}

}
