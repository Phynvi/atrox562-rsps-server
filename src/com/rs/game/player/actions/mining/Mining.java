package com.rs.game.player.actions.mining;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.cores.CoresManager;

import java.util.concurrent.TimeUnit;

public final class Mining extends MiningBase {

	public static enum RockDefinitions {

		CLAY(1, 5, 434, 10, 5, 5,
				new int[] { 31062, 31063, 31064, 11189, 11190, 11191, 9711, 9712, 9713, 15503, 15504, 15505, 14904,
						14905 },
				new int[] { 11555, 11556, 11157, 11552, 11553, 11554, 9723, 9724, 9725, 11555, 11556, 11557, 14892,
						14893 }),

		COPPER(1, 17.5, 436, 15, 10, 15,
				new int[] { 31080, 31081, 31082, 11960, 11961, 11962, 9708, 9709, 9710, 11936, 11937, 11938, 14906,
						14907 },
				new int[] { 11555, 11556, 11557, 11555, 11556, 11557, 9723, 9724, 9725, 11552, 11553, 11554, 14892,
						14893 }),

		TIN(1, 17.5, 438, 15, 10, 15,
				new int[] { 31077, 31078, 31079, 11957, 11958, 11959, 11933, 11934, 11935, 14902, 14903 },
				new int[] { 11552, 11553, 11554, 11555, 11556, 11557, 11552, 11553, 11554, 14892, 14893 }),

		// LIMESTONE(10, 26.5, 3211, 25, 10, 10000, new int[] { }, new int [] {
		// }),

		BLURITE(10, 17.5, 668, 15, 12, 30, new int[] { 33220, 33221 }, new int[] { 33222, 33223 }),

		IRON(15, 35, 440, 17, 15, 32,
				new int[] { 31071, 31072, 31073, 11954, 11955, 11956, 9717, 9718, 9719, 37307, 37308, 37309, 14913,
						14914, 14856, 14857 },
				new int[] { 11552, 11553, 11554, 11555, 11556, 11557, 9723, 9724, 9725, 11552, 11553, 11554, 14892,
						14893, 14832, 14833 }),

		SILVER(20, 40, 442, 22, 18, 40,
				new int[] { 11186, 11187, 11188, 9714, 9715, 9716, 37370, 37304, 37305, 37306, 11948, 11949, 11950 },
				new int[] { 11552, 11553, 11554, 9723, 9724, 9725, 11552, 11552, 11553, 11554, 11555, 11556, 11557 }),

		COAL(30, 50, 453, 30, 25, 42,
				new int[] { 31068, 31069, 31070, 11930, 11931, 11932, 11963, 11964, 14850, 14851, 14852 },
				new int[] { 11552, 11553, 11554, 11552, 11553, 11554, 11555, 11556, 14832, 14833, 14834 }),

		// SANDSTONE(35, 30, 6971, 30, 20, 10, new int[] { }, new int [] { }),

		GEM(40, 65, -1, 75, 30, 200, new int[] { 11364, 11194, 11195 }, new int[] { 11366, 11365, 11366 }),

		GOLD(40, 65, 444, 50, 30, 45,
				new int[] { 31065, 31066, 31067, 11183, 11184, 11185, 9720, 9721, 9722, 37310, 37311, 37312 },
				new int[] { 11552, 11553, 11554, 11552, 11553, 11554, 9723, 9724, 9725, 11552, 11553, 11554 }),

		// GRANITE(45, 50, 6979, 50, 32, 50, new int[] { }, new int [] { }),

		MITHRIL(55, 80, 447, 55, 35, 55, new int[] { 31086, 31087, 31088, 11942, 11943, 11944, 14853, 14854, 14855 },
				new int[] { 11552, 11553, 11554, 11552, 11553, 11554, 14832, 14833, 14834 }),

		ADAMANTITE(70, 95, 449, 65, 40, 70, new int[] { 31083, 31084, 31085, 11939, 11940, 11941, 14862, 14863, 14864 },
				new int[] { 11552, 11553, 11554, 11552, 11553, 11554, 14832, 14833, 14834 }),

		RUNITE(85, 125, 451, 85, 60, 100, new int[] { 14859, 14960 }, new int[] { 14832, 14833 }),
		
		S1_Star(10, 14, 13727, 17, 15, -1, new int[] { 38660 }, new int[] { -1 }),
		
		S2_Star(20, 25, 13727, 22, 30, -1, new int[] { 38661 }, new int[] { 38660 }),
		
		S3_Star(30, 29, 13727, 30, 40, -1, new int[] { 38662 }, new int[] { 38661 }),
		
		S4_Star(40, 32, 13727, 50, 50, -1, new int[] { 38663 }, new int[] { 38662 }),
		
		S5_Star(50, 47, 13727, 57, 60, -1, new int[] { 38664 }, new int[] { 38663 }),
		
		S6_Star(60, 71, 13727, 64, 70, -1, new int[] { 38665 }, new int[] { 38664 }),
		
		S7_Star(70, 114, 13727, 70, 80, -1, new int[] { 38666 }, new int[] { 38665 }),
		
		S8_Star(80, 145, 13727, 80, 90, -1, new int[] { 38667 }, new int[] { 38666 }),
		
		S9_Star(90, 210, 13727, 85, 100, -1, new int[] { 38668 }, new int[] { 38667 });

		private int level;
		private int[] rockId;
		private int[] depletedRocksId;
		private double xp;
		private int oreId;
		private int oreBaseTime;
		private int oreRandomTime;
		private int respawnDelay;

		private RockDefinitions(int level, double xp, int oreId, int oreBaseTime, int oreRandomTime, int respawnDelay,
				int[] rockId, int[] depletedRocksId) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
			this.respawnDelay = respawnDelay;
			this.rockId = rockId;
			this.depletedRocksId = depletedRocksId;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

		public int[] getDepletedRockId() {
			return depletedRocksId;
		}

		public int[] getRockId() {
			return rockId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}
	}

	private WorldObject rock;
	private RockDefinitions definitions;

	public Mining(WorldObject rock, RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You swing your pickaxe at the rock.", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null) {
			if (player.getFamiliar().getId() == 7342 || player.getFamiliar().getId() == 7342) {
				summoningBonus += 10;
			} else if (player.getFamiliar().getId() == 6832 || player.getFamiliar().getId() == 6831) {
				summoningBonus += 1;
			} else if (player.getFamiliar().getId() == 7370 || player.getFamiliar().getId() == 7371) {
				summoningBonus += 1;
			} else if (player.getFamiliar().getId() == 7345 || player.getFamiliar().getId() == 7346) {
				summoningBonus += 7;
			}
		}

		int mineTimer = definitions.getOreBaseTime() - (player.getSkills().getLevel(Skills.MINING) + summoningBonus)
				- Utils.getRandom(pickaxeTime);
		if (mineTimer < 1 + definitions.getOreRandomTime()) {
			mineTimer = 1 + Utils.getRandom(definitions.getOreRandomTime());
		}
		return mineTimer + Utils.random(3);
	}

	private int sandstoneMined;

	private boolean checkAll(Player player) {

		if (!hasPickaxe(player)) {
			player.sendMessage("You need a pickaxe to mine this rock.");
			return false;
		}

		if (!setPickaxe(player)) {
			player.sendMessage("You do not have the required level to use this pickaxe.");
			return false;
		}

		if (!hasMiningLevel(player)) {
			return false;
		}

		if (!player.getInventory().hasFreeSlots()) {
			player.sendMessage("Not enough space in your inventory.");
			return false;
		}

		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.sendMessage("You need a mining level of " + definitions.getLevel() + " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(emoteId));
		return checkRock(player);
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		int chance = 100 - Math.round(player.getSkills().getLevel(Skills.MINING) / 15);
		int ammy = player.getEquipment().getAmuletId();
		if (ammy == 1706 || ammy == 1708 || ammy == 1710 || ammy == 1712 || ammy == 10360 || ammy == 10358
				|| ammy == 10356 || ammy == 10354) {
			chance = -10;
		}
		if (player.getEquipment().getRingId() == 2572) {
			chance = -10;
		}
		if (Utils.random(chance) == 1) {
			int[] gems = { 1623, 1623, 1623, 1623, 1623, 1621, 1621, 1621, 1621, 1619, 1619, 1619, 1617, 1617 };
			int i = Utils.random(gems.length - 1);
			String gemName = ItemDefinitions.getItemDefinitions(gems[i]).getName();
			if (player.getInventory().hasFreeSlots()) {
				player.getInventory().addItem(gems[i], 1);
				player.getPackets().sendGameMessage("You find an " + gemName + "!", true);
			} else {
				World.addGroundItem(new Item(gems[i]), new WorldTile(player.getX(), player.getY(), player.getPlane()),
						player, true, 180, true);
				player.getPackets().sendGameMessage(
						"You do not have enough space in your inventory, so you drop the gem on the floor.");
			}
		}
		if (!player.getInventory().hasFreeSlots() && definitions.getOreId() != -1) {
			player.setNextAnimation(new Animation(-1));
			player.getDialogueManager().startDialogue("SimpleMessage", "Not enough space in your inventory.");
			return -1;
		}
		if (definitions.getDepletedRockId()[0] != -1) {
			int emptyId = 0;
			for (int i = 0; i < definitions.getRockId().length; i++)
				if (definitions.getRockId()[i] == rock.getId())
					emptyId = definitions.getDepletedRockId()[i];
			World.spawnTemporaryObject(new WorldObject(emptyId, rock.getType(), rock.getRotation(), rock.getX(),
					rock.getY(), rock.getPlane()), definitions.respawnDelay * 600, false);
			player.setNextAnimation(new Animation(-1));
			return -1;
		}

		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		double xpBoost = 1.00;
		int idSome = 0;

		/*
		 * if (definitions == RockDefinitions.SANDSTONE) { int[] sandstones = {
		 * 6971, 6971, 6971, 6971, 6971, 6971, 6973, 6973, 6973, 6973, 6975,
		 * 6975, 6975, 6977, 6977}; int i = Utils.random(sandstones.length - 1);
		 * player.getInventory().addItem(sandstones[i], 1); String sandstoneName
		 * = ItemDefinitions.getItemDefinitions(sandstones[i]).getName().
		 * toLowerCase(); player.getPackets().sendGameMessage("You mine some " +
		 * sandstoneName + ".", true); int xp = 0; if (i <= 10 && i >= 6) { xp =
		 * 10; } else if (i <= 13 && i >= 11) { xp = 20; } else if (i <= 15 && i
		 * >= 14) { xp = 30; } player.getSkills().addXp(Skills.MINING, xp); }
		 */
		/*
		 * if (definitions == RockDefinitions.GRANITE) { int[] granites = {
		 * 6979, 6979, 6979, 6979, 6981, 6981, 6981, 6983, 6983}; int i =
		 * Utils.random(granites.length - 1);
		 * player.getInventory().addItem(granites[i], 1); String graniteName =
		 * ItemDefinitions.getItemDefinitions(granites[i]).getName().toLowerCase
		 * (); player.getPackets().sendGameMessage("You mine some " +
		 * graniteName + ".", true); int xp = 0; if (i <= 6 && i >= 4) { xp =
		 * 10; } else if (i > 6) { xp = 15; }
		 * player.getSkills().addXp(Skills.MINING, xp); } else
		 */ if (definitions == RockDefinitions.GEM) {
			int[] gems = { 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625,
					1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1625, 1627, 1627, 1627, 1627, 1627, 1627,
					1627, 1627, 1627, 1627, 1627, 1627, 1629, 1629, 1629, 1629, 1629, 1629, 1623, 1623, 1623, 1623,
					1623, 1621, 1621, 1621, 1621, 1619, 1619, 1619, 1617, 1617 };
			int i = Utils.random(gems.length - 1);
			player.getInventory().addItem(gems[i], 1);
			String gemName = ItemDefinitions.getItemDefinitions(gems[i]).getName().toLowerCase();
			player.getPackets().sendGameMessage("You mine some " + gemName + ".", true);
		}

		double totalXp = definitions.getXp() * xpBoost;
		player.getSkills().addXp(Skills.MINING, totalXp);

		if (definitions.getOreId() != -1
				&& definitions != RockDefinitions.GEM/*
														 * definitions !=
														 * RockDefinitions.
														 * SANDSTONE &&
														 * definitions !=
														 * RockDefinitions.
														 * GRANITE
														 */) {
			player.getInventory().addItem(definitions.getOreId() + idSome, 1);
			String oreName = ItemDefinitions.getItemDefinitions(definitions.getOreId() + idSome).getName()
					.toLowerCase();
			player.getPackets().sendGameMessage("You mine some " + oreName + ".", true);
			if (player.inArea(3283, 3271, 3321, 3325)) {
				/*if (definitions == RockDefinitions.IRON) {
					AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
					if (!diary.isComplete(0, 8)) {
						diary.updateTask(player, 0, 8, true);
					}
				}*/
			}
		}
	}

	private boolean checkRock(Player player) {
		if (sandstoneMined == 10) {
			return false;
		} else if (!player.getInventory().hasFreeSlots() && definitions.getOreId() != -1) {
			return false;
		} else
			return World.getRegion(rock.getRegionId()).containsObject(rock.getId(), rock);
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

	public static boolean mineRocks(WorldObject object, Player player) {
		switch (object.getId()) {
		case 31062:
		case 31063:
		case 31064:
		case 11189:
		case 11190:
		case 11191:
		case 9711:
		case 9712:
		case 9713:
		case 15503:
		case 15504:
		case 15505:
		case 14904:
		case 14905:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.CLAY));
			return true;
		case 31080:
		case 31081:
		case 31082:
		case 11960:
		case 11961:
		case 11962:
		case 9708:
		case 9709:
		case 9710:
		case 11936:
		case 11937:
		case 11938:
		case 14906:
		case 14907:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.COPPER));
			return true;
		case 31077:
		case 31078:
		case 31079:
		case 11957:
		case 11958:
		case 11959:
		case 11933:
		case 11934:
		case 11935:
		case 14902:
		case 14903:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.TIN));
			return true;
		case 33220:
		case 33221:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.BLURITE));
			return true;
		case 31071:
		case 31072:
		case 31073:
		case 11954:
		case 11955:
		case 11956:
		case 9717:
		case 9718:
		case 9719:
		case 37307:
		case 37308:
		case 37309:
		case 14913:
		case 14914:
		case 14856:
		case 14857:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.IRON));
			return true;
		case 11186:
		case 11187:
		case 11188:
		case 9714:
		case 9715:
		case 9716:
		case 37370:
		case 37304:
		case 37305:
		case 37306:
		case 11948:
		case 11949:
		case 11950:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.SILVER));
			return true;
		case 31068:
		case 31069:
		case 31070:
		case 11930:
		case 11931:
		case 11932:
		case 11963:
		case 11964:
		case 14850:
		case 14851:
		case 14852:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.COAL));
			return true;
		case 11364:
		case 11194:
		case 11195:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.GEM));
			return true;
		case 31065:
		case 31066:
		case 31067:
		case 11183:
		case 11184:
		case 11185:
		case 9720:
		case 9721:
		case 9722:
		case 37310:
		case 37311:
		case 37312:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.GOLD));
			return true;
		case 31086:
		case 31087:
		case 31088:
		case 11942:
		case 11943:
		case 11944:
		case 14853:
		case 14854:
		case 14855:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.MITHRIL));
			return true;
		case 31083:
		case 31084:
		case 31085:
		case 11939:
		case 11940:
		case 11941:
		case 14862:
		case 14863:
		case 14864:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.ADAMANTITE));
			return true;
		case 14859:
		case 14960:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.RUNITE));
			return true;
		case 38660:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S1_Star));
			return true;
		case 38661:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S2_Star));
			return true;
		case 38662:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S3_Star));
			return true;
		case 38663:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S4_Star));
			return true;
		case 38664:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S5_Star));
			return true;
		case 38665:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S6_Star));
			return true;
		case 38666:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S7_Star));
			return true;
		case 38667:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S8_Star));
			return true;
		case 38668:
			player.getActionManager().setAction(new Mining(object, RockDefinitions.S9_Star));
			return true;
		case 14832:
		case 14833:
		case 11152:
		case 11153:
		case 11154:
		case 14834:
		case 9723:
		case 9724:
		case 9725:
		case 11555:
		case 11556:
		case 11557:
		case 33222:
		case 33223:
		case 14892:
		case 14893:
			player.sendMessage("The rock is currently empty.");
			return true;
		}
		return false;
	}

	private static void sendExamineMessage(Player player, String message) {
		player.sendMessage("You prospect the rocks..");
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if (message == "empty.")
					player.sendMessage("..the rocks is currently empty.");
				else
					player.sendMessage("..the rocks contain some " + message);
			}
		}, 1500, TimeUnit.MILLISECONDS);
	}

	public static boolean examineRocks(WorldObject object, Player player) {
		switch (object.getId()) {
		case 31062:
		case 31063:
		case 31064:
		case 11189:
		case 11190:
		case 11191:
		case 9711:
		case 9712:
		case 9713:
		case 15503:
		case 15504:
		case 15505:
		case 14904:
		case 14905:
			sendExamineMessage(player, "clay.");
			return true;
		case 31080:
		case 31081:
		case 31082:
		case 11960:
		case 11961:
		case 11962:
		case 9708:
		case 9709:
		case 9710:
		case 11936:
		case 11937:
		case 11938:
		case 14906:
		case 14907:
			sendExamineMessage(player, "copper ore.");
			return true;
		case 31077:
		case 31078:
		case 31079:
		case 11957:
		case 11958:
		case 11959:
		case 11933:
		case 11934:
		case 11935:
		case 14902:
		case 14903:
			sendExamineMessage(player, "tin ore.");
			return true;
		case 33220:
		case 33221:
			sendExamineMessage(player, "blurite ore.");
			return true;
		case 31071:
		case 31072:
		case 31073:
		case 11954:
		case 11955:
		case 11956:
		case 9717:
		case 9718:
		case 9719:
		case 37307:
		case 37308:
		case 37309:
		case 14913:
		case 14914:
		case 14856:
		case 14857:
			sendExamineMessage(player, "iron ore.");
			return true;
		case 11186:
		case 11187:
		case 11188:
		case 9714:
		case 9715:
		case 9716:
		case 37370:
		case 37304:
		case 37305:
		case 37306:
		case 11948:
		case 11949:
		case 11950:
			sendExamineMessage(player, "silver ore.");
			return true;
		case 31068:
		case 31069:
		case 31070:
		case 11930:
		case 11931:
		case 11932:
		case 11963:
		case 11964:
		case 14850:
		case 14851:
		case 14852:
			sendExamineMessage(player, "coal.");
			return true;
		case 11364:
		case 11194:
		case 11195:
			sendExamineMessage(player, "gems.");
			return true;
		case 31065:
		case 31066:
		case 31067:
		case 11183:
		case 11184:
		case 11185:
		case 9720:
		case 9721:
		case 9722:
		case 37310:
		case 37311:
		case 37312:
			sendExamineMessage(player, "gold ore.");
			return true;
		case 31086:
		case 31087:
		case 31088:
		case 11942:
		case 11943:
		case 11944:
		case 14853:
		case 14854:
		case 14855:
			sendExamineMessage(player, "mithril ore.");
			return true;
		case 31083:
		case 31084:
		case 31085:
		case 11939:
		case 11940:
		case 11941:
		case 14862:
		case 14863:
		case 14864:
			sendExamineMessage(player, "adamantite ore.");
			return true;
		case 14859:
		case 14960:
			sendExamineMessage(player, "runite ore.");
			return true;
		case 38660:
			sendExamineMessage(player, "star level 1");
			return true;
		case 38661:
			sendExamineMessage(player, "star level 2");
			return true;
		case 38662:
			sendExamineMessage(player, "star level 3");
			return true;
		case 38663:
			sendExamineMessage(player, "star level 4");
			return true;
		case 38664:
			sendExamineMessage(player, "star level 5");
			return true;
		case 38665:
			sendExamineMessage(player, "star level 6");
			return true;
		case 38666:
			sendExamineMessage(player, "star level 7");
			return true;
		case 38667:
			sendExamineMessage(player, "star level 8");
			return true;
		case 38668:
			sendExamineMessage(player, "star level 9");
			return true;
		case 14832:
		case 14833:
		case 11152:
		case 11153:
		case 11154:
		case 14834:
		case 9723:
		case 9724:
		case 9725:
		case 11555:
		case 11556:
		case 11557:
		case 33222:
		case 33223:
		case 14892:
		case 14893:
			sendExamineMessage(player, "empty.");
			return true;
		}
		return false;
	}

}
