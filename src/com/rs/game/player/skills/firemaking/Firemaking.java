package com.rs.game.player.skills.firemaking;

import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.utils.Utils;

public class Firemaking extends Action {

	private static enum Fire {
		LOGS(1511, 1, 30, 5249, 40),
		RED_LOGS(7404, 1, 30, 11404, 50),
		GREEN_LOGS(7405, 1, 30, 11405, 50),
		BLUE_LOGS(7406, 1, 30, 11406, 50),
		PURPLE_LOGS(10329, 1, 30, 20001, 50),
		WHITE_LOGS(10328, 1, 30, 20000, 50),
		ACHEY_LOGS(2862, 1, 30, 5249, 40),
		OAK_LOGS(1521, 15, 45, 5249, 60),
		WILLOW_LOGS(1519, 30, 45, 5249, 90),
		TEAK_LOGS(6333, 35, 45, 5249, 105),
		ARCTIC_PINE_LOGS(10810, 42, 50, 5249, 125),
		MAPLE_LOGS(1517, 45, 50, 5249, 135.5),
		MAHOGANY_LOGS(6332, 50, 70, 5249, 157.5),
		EUCALYPTUS_LOGS(12581, 58, 70, 5249, 193.5),
		YEW_LOGS(1515, 60, 80, 5249, 202.5),
		MAGIC_LOGS(1513, 75, 90, 5249, 303),
		CURSED_MAGIC(13567, 82, 100, 5249, 303);

		private int logId;
		private int level;
		private int life;
		private int fireId;
		private double xp;

		Fire(int logId, int level, int life, int fireId, double xp) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.fireId = fireId;
			this.xp = xp;
		}

		public double getExperience() {
			return xp;
		}

		public int getFireId() {
			return fireId;
		}

		public int getLevel() {
			return level;
		}

		public int getLife() {
			return (life * 1700);
			//return (life * 600);
		}

		public int getLogId() {
			return logId;
		}
	}

	private static double increasedExperience(Player player, double totalXp) {
		if (player.getEquipment().getGlovesId() == 13660 && player.getEquipment().getRingId() == 13659)
			totalXp *= 1.05;
		else if (player.getEquipment().getGlovesId() == 13660)
			totalXp *= 1.02;
		else if (player.getEquipment().getRingId() == 13659)
			totalXp *= 1.02;
		return totalXp;
	}

	public static boolean isFiremaking(Player player, int logId, boolean ground, WorldTile groundCoords) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getActionManager().setAction(new Firemaking(fire, ground, groundCoords));
				return true;
			}
		}
		return false;

	}

	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = InventoryOptionsHandler.contains(590, item1, item2);
		if (log == null)
			return false;
		return isFiremaking(player, log.getId(), false, new WorldTile(player));
	}

	private Fire fire;
	/**
	 * These bottom ones are for determining whether the logs already
	 * pre-existed on the ground and were lit by "light" on ground item or not.
	 */
	private WorldTile groundCoords;
	private boolean ground;

	private Firemaking(Fire fire, boolean ground, WorldTile groundCoords) {
		this.fire = fire;
		this.ground = ground;
		this.groundCoords = groundCoords;
	}

	private boolean checkAll(Player player) {
		if (player.getInterfaceManager().containsScreenInter()
				|| player.getInterfaceManager().containsInventoryInter()) {
			player.getPackets().sendGameMessage("Please finish what you're doing before doing this action.");
			return false;
		}
		if (!player.getInventory().containsItem(590, 1)) {
			player.sendMessage("You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.sendMessage("You do not have the required level to light this.");
			return false;
		}
		if (!World.canMoveNPC(player.getPlane(), player.getX(), player.getY(), 1)
				|| World.getRegion(player.getRegionId()).getSpawnedObject(player) != null) {
			player.sendMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		final WorldTile tile = groundCoords;
		if (tile.getX() == player.getX() && tile.getY() == player.getY())
			if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
					if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
						player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		player.getPackets().sendGameMessage("The fire catches and the logs begin to burn.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final FloorItem item = World.getRegion(tile.getRegionId()).getGroundItem(fire.getLogId(), tile, player);
				if (item == null)
					return;
				if (!World.removeGroundItem(player, item, false))
					return;
				World.spawnTempGroundObject(new WorldObject(fire.getFireId(), 10, 0, tile.getX(), tile.getY(), tile.getPlane()), 592, fire.getLife());
				player.getSkills().addXp(Skills.FIREMAKING, increasedExperience(player, fire.getExperience()));
				player.setNextFaceWorldTile(tile);
				/*if (player.inArea(3192, 3195, 3260, 3253) || player.inArea(3066, 3222, 3146, 3312)) {
					if (fire == Fire.OAK_LOGS) {
						AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
						if (!diary.isComplete(0, 4)) {
							diary.updateTask(player, 0, 4, true);
						}
					}
				}*/
			}
		}, 1);
		player.getTemporaryAttributtes().put("Fire", Utils.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public boolean start(Player player) {
		player.resetWalkSteps();
		if (!checkAll(player))
			return false;
		player.getPackets().sendSound(2594, 0);
		player.getPackets().sendGameMessage("You attempt to light the logs.", true);
		if (!ground) {
			player.getInventory().deleteItem(fire.getLogId(), 1);
			World.addGroundItem(new Item(fire.getLogId(), 1), new WorldTile(player), player, false, 180, true);
		}
		Long time = (Long) player.getTemporaryAttributtes().remove("Fire");
		boolean quickFire = time != null && time > Utils.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : 2);
		if (!quickFire)
			player.setNextAnimation(new Animation(733));
		return true;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}