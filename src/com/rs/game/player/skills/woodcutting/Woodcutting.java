package com.rs.game.player.skills.woodcutting;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.actions.Action;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.game.WorldTile;

public final class Woodcutting extends Action {

	public enum HatchetDefinitions {

		BRONZE(1351, 1, 1, 879),
		IRON(1349, 5, 2, 877),
		STEEL(1353, 5, 3, 875),
		BLACK(1361, 11, 4, 873),
		MITHRIL(1355, 21, 5, 871),
		ADAMANT(1357, 31, 7, 869),
		RUNE(1359, 41, 10, 867),
		DRAGON(6739, 61, 13, 2846),
		INFERNO(13661, 61, 13, 10251);

		private int itemId, levelRequried, axeTime, emoteId;

		private HatchetDefinitions(int itemId, int levelRequried, int axeTime, int emoteId) {
			this.itemId = itemId;
			this.levelRequried = levelRequried;
			this.axeTime = axeTime;
			this.emoteId = emoteId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getLevelRequried() {
			return levelRequried;
		}

		public int getAxeTime() {
			return axeTime;
		}

		public int getEmoteId() {
			return emoteId;
		}
	}

	public enum TreeDefinitions {

		TREE(1, 25, 1511, 20, 5, 0, new int[] { 1278, 1276, 1289 }, new int[] { 1342, 1342, 1351 }),
		FRUIT_TREE(1, 25, -1, 20, 8, 0, new int[] {}, new int[] {}),
		EVERGREEN(1, 25, 1511, 20, 8, 1, new int[] {}, new int[] {}),
		JUNGLE_TREE(1, 25, 1511, 20, 8, 1, new int[] {}, new int[] {}),
		ACHEY_TREE(1, 25, 2862, 25, 9, 0, new int[] {}, new int[] {}),
		OAK(15, 37.5, 1521, 30, 12, 15, new int[] {}, new int[] {}),
		WILLOW_TREE(30, 67.5, 1519, 40, 15, 15, new int[] {}, new int[] {}),
		TEAK_TREE(35, 85, 6333, 45, 17, 15, new int[] {}, new int[] {}),
		MAPLE_TREE(45, 100, 1517, 55, 20, 15, new int[] { 1307 }, new int[] { 1343 }),
		HOLLOW_TREE(45, 82.5, -1, 55, 20, 15, new int[] {}, new int[] {}),
		MAHOGANY_TREE(50, 125, 6332, 60, 22, 15, new int[] {}, new int[] {}),
		ARCTIC_PINE(54, 140.2, 10810, 64, 24, 15, new int[] {}, new int[] {}),
		EUCALYPTUS_TREE(58, 165, 12581, 68, 25, 15, new int[] {}, new int[] {}),
		YEW_TREE(60, 175, 1515, 72, 27, 15, new int[] {}, new int[] {}),
		MAGIC_TREE(75, 250, 1513, 85, 35, 15, new int[] {}, new int[] {}),
		CURSED_MAGIC_TREE(85, 275, 13567, 85, 35, 15, new int[] {}, new int[] {}),
		BRIM_VINES(10, 0, -1, 22, 3, 0, new int[] {}, new int[] { -1 });

		private int level;
		private double xp;
		private int logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int respawnDelay;
		private int randomLifeProbability;
		private int[] treeId, stumpId;

		private TreeDefinitions(int level, double xp, int logsId, int logBaseTime, int respawnDelay, int randomLifeProbability, int[] treeId, int[] stumpId) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
			this.treeId = treeId;
			this.stumpId = stumpId;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getLogsId() {
			return logsId;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}

		public int[] getTreeId() {
			return treeId;
		}

		public int[] getStumpId() {
			return stumpId;
		}
	}

	private WorldObject tree;
	private TreeDefinitions definitions;

	private int emoteId;
	private boolean usingBeaver = false;
	private int axeTime;

	public Woodcutting(WorldObject tree, TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage(usingBeaver ? "Your beaver uses its strong teeth to chop down the tree..." : "You swing your hatchet at the tree" + "...", true);
		setActionDelay(player, getWoodcuttingDelay(player));// 3 because otherwise person may get logs
									// within the split second they click the
									// tree. All too random, this evens it out.
		return true;
	}

	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = player.getFamiliar() != null ? (player.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807) ? 10 : 0 : 0;
		int wcTimer = (definitions.getLogBaseTime() * 2) - (player.getSkills().getLevel(8) + summoningBonus) - (Utils.getRandom(axeTime) * 5);
		if (wcTimer < 1 + definitions.getLogRandomTime())
			wcTimer = 1 + Utils.getRandom(definitions.getLogRandomTime());
		return wcTimer;
	}

	private double lumberjackOutfit(Player player) {
		double boost = 1;
		if (player.getEquipment().getHatId() == 10941)
			boost += 0.01;
		if (player.getEquipment().getChestId() == 10939)
			boost += 0.01;
		if (player.getEquipment().getLegsId() == 10940)
			boost += 0.01;
		if (player.getEquipment().getBootsId() == 10933)
			boost += 0.01;
		if (player.getEquipment().getHatId() == 10941 && player.getEquipment().getChestId() == 10939 && player.getEquipment().getLegsId() == 10940 && player.getEquipment().getBootsId() == 10933)
			boost += 0.01;
		return boost;
	}

	public static boolean canChop(Player player, WorldObject object) {
		ObjectDefinitions objectDef = object.getDefinitions();
		switch (object.getDefinitions().getName()) {
		//case "Tree":
		case "Evergreen":
		case "Dead tree":
		if (objectDef.containsOption(0, "Chop down")) {
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.TREE));
			return true;
		}
		case "Achey":
		case "Achey tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.ACHEY_TREE));
			return true;
		case "Teak":
		case "Teak tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.TEAK_TREE));
			return true;
		case "Oak":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK));
			return true;
		case "Hollow":
		case "Hollow tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.HOLLOW_TREE));
			return true;
		case "Mahogany":
		case "Mahogany tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAHOGANY_TREE));
			return true;
		case "Arctic":
		case "Arctic pine":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.ARCTIC_PINE));
			return true;
		case "Eucalyptus":
		case "Eucalyptus tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.EUCALYPTUS_TREE));
			return true;
		case "Willow":
		case "Willow tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW_TREE));
			return true;
		case "Maple":
		case "Maple tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE_TREE));
			return true;
		case "Yew":
		case "Yew tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW_TREE));
			return true;
		case "Magic tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC_TREE));
			return true;
		case "Cursed magic tree":
		case "Cursed Magic tree":
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.CURSED_MAGIC_TREE));
			return true;
		}
		switch (object.getId()) {
		case 1276:
		case 1278:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.TREE));
			return true;
		case 1281:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK));
			return true;
		case 5552:
		case 1308:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW_TREE));
			return true;
		case 1307:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE_TREE));
			return true;
		case 1309:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW_TREE));
			return true;
		case 1306:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC_TREE));
			return true;
		case 5103:
		case 5104:
		case 5106:
		case 5107:
			player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.BRIM_VINES));
			return true;
		default:
			return false;
		}
	}

	private boolean checkAll(Player player) {
		// if (TreeDefinitions.LIGHT_JUNGLE != definitions &&
		// TreeDefinitions.MEDIUM_JUNGLE != definitions &&
		// TreeDefinitions.DENSE_JUNGLE != definitions)
		if (!hasAxe(player)) {
			player.getPackets().sendGameMessage("You need a hatchet to chop down this tree.");
			return false;
		}
		if (!setAxe(player)) {
			player.getPackets().sendGameMessage("You don't have the required level to use that axe.");
			return false;
		}
		if (!hasWoodcuttingLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getPackets().sendGameMessage("You need a woodcutting level of " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		return true;
	}

	private boolean setAxe(Player player) {
		int level = player.getSkills().getLevel(8);
		int weaponId = player.getEquipment().getWeaponId();
		// if (TreeDefinitions.LIGHT_JUNGLE == definitions ||
		// TreeDefinitions.MEDIUM_JUNGLE == definitions ||
		// TreeDefinitions.DENSE_JUNGLE == definitions)
		/*
		 * if (weaponId == 975 || weaponId == 6313 || weaponId == 6315 ||
		 * weaponId == 6317) { emoteId = 2270; axeTime = 1; return true; }
		 */
		if (!hasAxe(player)) {
			return true;
		}
		if (player.getInventory().containsOneItem(13661) || weaponId == 13661) {
			if (level >= 61) {
				emoteId = 10251;
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(6739) || weaponId == 6739) {
			if (level >= 61) {
				emoteId = 2846;
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1359) || weaponId == 1359) {
			if (level >= 41) {
				emoteId = 867;
				axeTime = 10;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1357) || weaponId == 1357) {
			if (level >= 31) {
				emoteId = 869;
				axeTime = 7;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1355) || weaponId == 1355) {
			if (level >= 21) {
				emoteId = 871;
				axeTime = 5;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1361) || weaponId == 1361) {
			if (level >= 11) {
				emoteId = 873;
				axeTime = 4;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1353) || weaponId == 1353) {
			if (level >= 6) {
				emoteId = 875;
				axeTime = 3;
				return true;
			}
		}
		if (player.getInventory().containsOneItem(1349) || weaponId == 1349) {
			emoteId = 877;
			axeTime = 2;
			return true;
		}
		if (player.getInventory().containsOneItem(1351) || weaponId == 1351) {
			emoteId = 879;
			axeTime = 1;
			return true;
		}
		return false;

	}

	private boolean hasAxe(Player player) {
		if (player.getInventory().containsOneItem(1351, 1349, 1353, 1355, 1357, 1361, 1359, 6739, 13661))
			return true;
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1351:// Bronze Axe
		case 1349:// Iron Axe
		case 1353:// Steel Axe
		case 1361:// Black Axe
		case 1355:// Mithril Axe
		case 1357:// Adamant Axe
		case 1359:// Rune Axe
		case 6739:// Dragon Axe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}

	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(usingBeaver ? 1 : emoteId));
		return checkTree(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (Utils.random(70) == 1) {
			World.addGroundItem(new Item((Utils.random(100) >= 95 ? (5070 + (Utils.random(2))) : (Utils.random(100) > 66 ? 5074 : (Utils.random(100) > 50 ? 7413 : (Utils.random(100) > 33 ? 5073 : 5075))))), new WorldTile(player.getX(), player.getY(), player.getPlane()), player, true, 30, true);
			player.sendMessage("<col=ff0000>A bird's nest falls out of the tree.");
		}
		addLog(player);
		if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
			long time = definitions.respawnDelay * 600;
			int stumpId = 0;
			for (int i = 0; i < definitions.getTreeId().length; i++)
				if (definitions.getTreeId()[i] == tree.getId())
					stumpId = definitions.getStumpId()[i];
			stumpId = 1344;
			World.spawnTemporaryObject(new WorldObject(stumpId, tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getPlane()), time);
			player.setNextAnimation(new Animation(-1));
			return -1;
		}
		if (player.inArea(3079, 3225, 3093, 3239)) {
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player) + Utils.getRandom(3);
	}

	private void addLog(Player player) {
		if (definitions == TreeDefinitions.BRIM_VINES && player.getX() >= tree.getX() && player.getX() == 2689) {
			player.addWalkSteps(player.getX() >= tree.getX() ? tree.getX() - 2 : tree.getX(), 9564 , -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getX() <= tree.getX() && player.getX() == 2689) {
			player.addWalkSteps(player.getX() <= tree.getX() ? tree.getX() + 2 : tree.getX(), 9564 , -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getY() <= tree.getY() && player.getX() == 2683) {
			player.addWalkSteps(2683, player.getY() + 2 , -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getY() >= tree.getY() && player.getX() == 2683) {
			player.addWalkSteps(2683, player.getY() - 2 , -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getX() <= tree.getX() && player.getY() == 9479) {
			player.addWalkSteps(player.getX() + 2, player.getY(), -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getX() >= tree.getX() && player.getY() == 9479) {
			player.addWalkSteps(player.getX() - 2, player.getY(), -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getX() <= tree.getX() && player.getY() == 9482) {
			player.addWalkSteps(player.getX() + 2, player.getY(), -1, false);
			return;
		} else if (definitions == TreeDefinitions.BRIM_VINES && player.getX() >= tree.getX() && player.getY() == 9482) {
			player.addWalkSteps(player.getX() - 2, player.getY(), -1, false);
			return;
		}
		player.getSkills().addXp(8, definitions.getXp() * lumberjackOutfit(player));
		String logName = ItemDefinitions.getItemDefinitions(definitions.getLogsId()).getName().toLowerCase();
		player.getPackets().sendGameMessage("You get some " + logName + ".", true);
		player.getInventory().addItem(definitions.getLogsId(), 1);
	}

	private boolean checkTree(Player player) {
		return World.getRegion(tree.getRegionId()).containsObject(tree.getId(), tree);
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
