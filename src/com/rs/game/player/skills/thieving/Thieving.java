package com.rs.game.player.skills.thieving;

import java.util.List;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.npc.NPC;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.Utils;

/**
 * Handles the Thieving Skill
 * 
 * @author Dragonkk
 * 
 */
public class Thieving {

	public enum Stall {
		VEGETABLE(0, 2, new int[] { 1957, 1965, 1942, 1982, 1550 }, 1, 2, 10, 34381),
		CAKE(34384, 5, new int[] { 1891, 1897, 2309 }, 1, 2.5, 16, 34381),
		CRAFTING(0, 5, new int[] { 1755, 1592, 1597 }, 1, 7, 16, 34381),
		MONKEY_FOOD(0, 5, new int[] { 1963 }, 1, 7, 16, 34381),
		MONKEY_GENERAL(0, 5, new int[] { 1931, 2347, 590 }, 1, 7, 16, 34381),
		TEA_STALL(635, 5, new int[] { 712 }, 1, 7, 16, 34381),
		SILK_STALL(34383, 20, new int[] { 950 }, 1, 8, 24, 34381),
		WINE_STALL(14011, 22, new int[] { 1937, 1993, 1987, 1935, 7919 }, 1, 16, 27, 14012),
		SEED_STALL(7053, 27, new int[] { 5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5105 }, 30, 11, 10, 14012),
		FUR_STALL(34387, 35, new int[] { 6814, 958 }, 1, 15, 36, 34381),
		FISH_STALL(0, 42, new int[] { 331, 359, 377 }, 1, 16, 42, 34381),
		CROSSBOW_STALL(0, 49, new int[] { 877, 9420, 9440 }, 1, 11, 52, 34381),
		SILVER_STALL(34382, 50, new int[] { 442 }, 1, 30, 54, 34381),
		SPICE_STALL(34386, 65, new int[] { 2007 }, 1, 80, 81, 34381),
		MAGIC_STALL(0, 65, new int[] { 556, 557, 554, 555, 563 }, 30, 80, 100, 34381),
		SCIMITAR_STALL(0, 65, new int[] { 1323 }, 1, 80, 100, 34381),
		GEM_STALL(34385, 75, new int[] { 1623, 1621, 1619, 1617 }, 1, 180, 16, 34381);

		private int[] item;
		private int level;
		private int amount;
		private int objectId;
		private int replaceObject;
		private double experience;
		private double seconds;

		Stall(int objectId, int level, int[] item, int amount, double seconds, double experience, int replaceObject) {
			this.objectId = objectId;
			this.level = level;
			this.item = item;
			this.amount = amount;
			this.seconds = seconds;
			this.experience = experience;
			this.replaceObject = replaceObject;
		}

		public int getReplaceObject() {
			return replaceObject;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getItem(int count) {
			return item[count];
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getTime() {
			return seconds;
		}

		public double getExperience() {
			return experience;
		}
	}

	public static boolean isGuard(int npcId) {
		if (npcId == 32 || npcId == 21 || npcId == 2256 || npcId == 23)
			return true;
		else
			return false;
	}

	public static void handleStalls(final Player player, final WorldObject object) {
		if (player.getAttackedBy() != null && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You can't do this while you're under combat.");
			return;
		}
		for (final Stall stall : Stall.values()) {
			if (stall.getObjectId() == object.getId()) {
				final WorldObject emptyStall = new WorldObject(stall.getReplaceObject(), 10, object.getRotation(),
						object.getX(), object.getY(), object.getPlane());
				if (player.getSkills().getLevel(Skills.THIEVING) < stall.getLevel()) {
					player.getPackets().sendGameMessage(
							"You need a thieving level of " + stall.getLevel() + " to steal from this.", true);
					return;
				}
				if (player.getInventory().getFreeSlots() <= 0) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.", true);
					return;
				}

				player.setNextAnimation(new Animation(881));
				player.lock(2);
				WorldTasksManager.schedule(new WorldTask() {
					boolean gaveItems;

					@Override
					public void run() {
						if (!World.getRegion(object.getRegionId()).containsObject(object.getId(), object)) {
							stop();
							return;
						}
						if (!gaveItems) {
							player.getInventory().addItem(stall.getItem(Utils.getRandom(stall.item.length - 1)),
									Utils.getRandom(stall.getAmount()));
							player.getSkills().addXp(Skills.THIEVING, stall.getExperience());
							gaveItems = true;
							checkGuards(player);
						} else {
							World.spawnTemporaryObject(emptyStall, (int) (1500 * stall.getTime()));
							stop();
						}
					}
				}, 0, 0);
			}
		}
	}

	public static void checkGuards(Player player) {
		NPC guard = null;
		int lastDistance = -1;
		for (int regionId : player.getMapRegionsIds()) {
			List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
			if (npcIndexes == null)
				continue;
			for (int npcIndex : npcIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null)
					continue;
				if (!isGuard(npc.getId()) || npc.isUnderCombat() || npc.isDead() || !npc.withinDistance(player, 4)
						|| !npc.clipedProjectile(player, true))
					continue;
				int distance = Utils.getDistance(npc.getX(), npc.getY(), player.getX(), player.getY());
				if (lastDistance == -1 || lastDistance > distance) {
					guard = npc;
					lastDistance = distance;
				}
			}
		}
		if (guard != null) {
			guard.setNextForceTalk(new ForceTalk("Hey, what do you think you're doing!?"));
			guard.setTarget(player);
		}
	}

}
