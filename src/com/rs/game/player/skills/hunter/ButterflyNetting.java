package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.ForceTalk;
import com.rs.game.ForceMovement;
import com.rs.game.cities.achievements.CityAchievements;
import com.rs.game.cities.achievements.AchievementDiary;
import com.rs.game.minigames.PuroPuro;
import com.rs.game.player.Skills;
import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.Animation;
import com.rs.game.tasks.WorldTask;
import com.rs.utils.Utils;
import com.rs.game.tasks.WorldTasksManager;

public class ButterflyNetting extends Action {

	/**
	 * @author Kris Handles all entities caught using this very method.
	 */

	public enum Entities {

		RUBY_HARVEST(5085, 15, 10020, -1, null, null, null, null),
		SAPPHIRE_GLACIALIS(5084, 25, 10018, -1, null, null, null, null),
		SNOWY_KNIGHT(5083, 35, 10016, -1, null, null, null, null),
		BLACK_WARLOCK(5082, 45, 10014, -1, null, null, null, null),

		BABY_IMPLING(1028, 17, 11238, 90,
				new Item[] { new Item(1755, 1), new Item(1734, 1), new Item(1733, 1), new Item(946, 1),
						new Item(1985, 1), new Item(2347, 1), new Item(1759, 1) },
				new Item[] { new Item(1927, 1), new Item(319, 1), new Item(2007, 1), new Item(1779, 1),
						new Item(7170, 1), new Item(401, 1), new Item(1438, 1) },
				new Item[] { new Item(2355, 1), new Item(1607, 1), new Item(1743, 1), new Item(379, 1),
						new Item(1761, 1) }, // TODO: Add clue scroll (easy)
				null),

		YOUNG_IMPLING(1029, 22, 11240, 70,
				new Item[] { new Item(361, 1), new Item(1902, 1), new Item(1539, 5), new Item(1523, 1),
						new Item(7936, 1) },
				new Item[] { new Item(855, 1), new Item(1353, 1), new Item(2293, 1), new Item(7178, 1),
						new Item(247, 1), new Item(453, 1), new Item(1777, 1), new Item(231, 1), new Item(1761, 1) },
				new Item[] { new Item(10997, 1), new Item(1157, 1), new Item(8778, 1), new Item(133, 1),
						new Item(2359, 1) }, // TODO: Add clue scroll (easy)
				null),

		GOURMET_IMPLING(1030, 28, 11242, 55,
				new Item[] { new Item(365, 1), new Item(361, 1), new Item(2011, 1), new Item(2327, 1),
						new Item(1897, 1), new Item(2293, 1), new Item(5004, 1) },
				new Item[] { new Item(1883, 1), new Item(247, 1), new Item(380, 4), new Item(386, 3), new Item(7170, 1),
						new Item(7178, 1), new Item(7188, 1) },
				new Item[] { new Item(5755, 1), new Item(10137, 5), new Item(7179, 6), new Item(374, 3),
						new Item(10136, 1), new Item(5406, 1) },
				new Item[] { new Item(7218, 1) }),

		EARTH_IMPLING(1031, 36, 11244, 45,
				new Item[] { new Item(1442, 1), new Item(1440, 1), new Item(5535, 1), new Item(557, 32),
						new Item(447, Utils.random(1, 3)), new Item(237, 1), new Item(2353, 1), new Item(1273, 1),
						new Item(5311, 2), new Item(5104, 2), new Item(6033, 6) },
				new Item[] { new Item(6035, 2), new Item(1784, 4), new Item(5294, 2), new Item(454, 2) },
				new Item[] { new Item(444, 1), new Item(1622, 2), new Item(1606, 2), new Item(1603, 1) }, // TODO:
																											// Add
																											// clue
																											// scroll
																											// (medium)
				null),

		ESSENCE_IMPLING(1032, 42, 11246, 38,
				new Item[] { new Item(7937, 20), new Item(555, 30), new Item(556, 30), new Item(558, 25),
						new Item(559, 28), new Item(562, 4), new Item(1448, 1) },
				new Item[] { new Item(564, 1) },
				new Item[] { new Item(563, 13), new Item(565, 7), new Item(566, 11), new Item(561, 13) }, null),

		ECLECTIC_IMPLING(1033, 50, 11248, 30,
				new Item[] { new Item(1273, 1), new Item(5970, 1), new Item(231, 1),
						new Item(556, Utils.random(30, 60)), new Item(8779, 4), new Item(2358, 5) },
				new Item[] { new Item(1199, 1), new Item(444, 1), new Item(4527, 1), new Item(237, 1),
						new Item(7936, Utils.random(20, 35)) }, // TODO: Add
																// clue scroll
																// (medium)
				new Item[] { new Item(2493, 1), new Item(10083, 1), new Item(1213, 1), new Item(1391, 1),
						new Item(7208, 1), new Item(5321, 3), new Item(450, 10), new Item(5760, 2), new Item(1601, 1) },
				null),

		NATURE_IMPLING(1034, 58, 11250, 25,
				new Item[] { new Item(5100, 1), new Item(5104, 1), new Item(5281, 1), new Item(5294, 1),
						new Item(6016, 1), new Item(1513, 1), new Item(254, 4) },
				new Item[] { new Item(5286, 1), new Item(5285, 1), new Item(3000, 1), new Item(5974, 1),
						new Item(5297, 1), new Item(5299, 1), new Item(5298, 5) },
				new Item[] { new Item(5313, 1), new Item(5304, 1), new Item(5295, 1) }, // TODO:
																						// Add
																						// clue
																						// scroll(hard)
				new Item[] { new Item(270, 2), new Item(5303, 1) }),

		MAGPIE_IMPLING(1035, 65, 11252, 15,
				new Item[] { new Item(1682, 3), new Item(1732, 3), new Item(2569, 3), new Item(4097, 1),
						new Item(5541, 1), new Item(1748, 6) },
				new Item[] { new Item(4095, 1), new Item(1347, 1), new Item(2571, 4), new Item(2364, 2) }, 
				new Item[] { new Item(1215, 1), new Item(1185, 1), new Item(1601, 4), new Item(5287, 1),
						new Item(987, 1), new Item(985, 1), new Item(993, 1) },
				new Item[] { new Item(3391, 1), new Item(5300) }),

		NINJA_IMPLING(6053, 74, 11254, 8,
				new Item[] { new Item(6328, 1), new Item(3391, 1), new Item(4097, 1), new Item(6313, 1),
						new Item(3101, 1), new Item(892, 70), new Item(811, 70), new Item(868, 40), new Item(805, 50),
						new Item(1748, 10), new Item(1748, 16) },
				new Item[] { new Item(3385, 1), new Item(1113, 1), new Item(1333, 1), new Item(1343, 1),
						new Item(5680, 1), new Item(9342, 2), new Item(5938, 4), new Item(6155, 3) }, 
				new Item[] { new Item(9194, 4), new Item(2364, 4) }, null),

		PIRATE_IMPLING(7845, 76, 13337, 5,
				new Item[] { new Item(13370, 1), new Item(13372, 1), new Item(13374, 1), new Item(8924, 1),
						new Item(8925, 1), new Item(8926, 1), new Item(8927, 1), new Item(7112, 1), new Item(7124, 1),
						new Item(7130, 1), new Item(7136, 1), new Item(7116, 1), new Item(7126, 1), new Item(7132, 1),
						new Item(7138, 1), new Item(13364, 1), new Item(13366, 1), new Item(13368, 1),
						new Item(7110, 1), new Item(7122, 1), new Item(7128, 1), new Item(7134, 1), new Item(13358, 1),
						new Item(13360, 1), new Item(13362, 1), new Item(2358, 15) },
				new Item[] { new Item(13355, 1), new Item(7114, 1) }, new Item[] { new Item(8951, Utils.random(1, 5)) },
				null),

		DRAGON_IMPLING(6064, 83, 11256, 3,
				new Item[] { new Item(11212, Utils.random(100, 500)), new Item(9341, Utils.random(3, 40)),
						new Item(1305, 1), new Item(11232, Utils.random(105, 350)),
						new Item(11237, Utils.random(90, 500)), new Item(9193, Utils.random(10, 49)),
						new Item(535, Utils.random(103, 300)) },
				new Item[] { new Item(4093, 1), new Item(1705, Utils.random(2, 3)), new Item(1703, Utils.random(2, 3)),
						new Item(5698, 3), new Item(11230, Utils.random(101, 350)), new Item(5316, 1),
						new Item(537, Utils.random(50, 100)), new Item(1616, Utils.random(3, 6)) }, // TODO:
																									// Add
																									// clue
																									// scroll
																									// (hard)
				new Item[] { new Item(5300, 6), new Item(7219, 15) }, null);

		private int npcId, requiredLevel, jarId, chance;
		private Item[] commonLoot, uncommonLoot, rareLoot, veryRareLoot;

		static final Map<Integer, Entities> NPC = new HashMap<Integer, Entities>();
		static final Map<Integer, Entities> ITEM = new HashMap<Integer, Entities>();
		static final Map<Integer, Entities> ENTITY = new HashMap<Integer, Entities>();

		static {
			for (Entities npcs : Entities.values())
				NPC.put(npcs.npcId, npcs);
			for (Entities items : Entities.values())
				ITEM.put(items.jarId, items);
			for (Entities e : Entities.values())
				ENTITY.put(e.ordinal(), e);
		}

		public static Entities forItemId(int id) {
			return ITEM.get(id);
		}

		public static Entities forNPCId(int id) {
			return NPC.get(id);
		}

		public static Entities forEntityId(int id) {
			return ENTITY.get(id);
		}

		Entities(int npcId, int requiredLevel, int jarId, int chance, Item[] commonLoot, Item[] uncommonLoot,
				Item[] rareLoot, Item[] veryRareLoot) {
			this.npcId = npcId;
			this.requiredLevel = requiredLevel;
			this.jarId = jarId;
			this.chance = chance;
			this.commonLoot = commonLoot;
			this.uncommonLoot = uncommonLoot;
			this.rareLoot = rareLoot;
			this.veryRareLoot = veryRareLoot;
		}

		public int getNPCId() {
			return npcId;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public int getJarId() {
			return jarId;
		}

		public int getChance() {
			return chance;
		}

		public Item[] getCommonLoot() {
			return commonLoot;
		}

		public Item[] getUncommonLoot() {
			return uncommonLoot;
		}

		public Item[] getRareLoot() {
			return rareLoot;
		}

		public Item[] getVeryRareLoot() {
			return veryRareLoot;
		}

	}

	private Entities entity;
	private NPC npc;

	public ButterflyNetting(Entities entity, NPC npc) {
		this.entity = entity;
		this.npc = npc;
	}

	private boolean isAtPuroPuro(Player player) {
		if (player.getControlerManager().getControler() != null
				&& player.getControlerManager().getControler() instanceof PuroPuro)
			return true;
		return false;
	}

	@Override
	public boolean start(Player player) {
		if (npc.isCantInteract())
			return false;
		if (player.getSkills().getLevel(Skills.HUNTER) < entity.getRequiredLevel()) {
			player.sendMessage(
					"You need at least level " + entity.getRequiredLevel() + " Hunter to catch this impling.");
			return false;
		}
		if (isButterfly() && !player.getInventory().containsItem(10012, 1)) {
			player.sendMessage("You need an empty butterfly jar to catch this butterfly.");
			return false;
		} else if (isAtPuroPuro(player) && !player.getInventory().containsItem(11260, 1)) {
			player.sendMessage("You need an empty impling jar to catch this impling.");
			return false;
		}
		player.lock(2);
		player.sendMessage("You swing your net..");
		npc.setCantInteract(true);
		player.setNextAnimation(new Animation(6606));
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}
	
	private int getDistance(WorldTile targetLoc) {
		int distanceX = Math.abs(targetLoc.getX() - npc.getX());
		int distanceY = Math.abs(targetLoc.getY() - npc.getY());
		return distanceX > distanceY ? distanceX : distanceY;
	}

	@Override
	public int processWithDelay(Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (HunterCore.calculateSuccess(player, entity.getRequiredLevel())) {
					npc.setCantInteract(false);
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							World.spawnNPC(getSpawnNPC(), npc.getRespawnTile(), -1, false);
						}
					}, 1, TimeUnit.MINUTES);
					npc.reset();
					npc.finish();

					player.sendMessage("..and you successfully capture the "
							+ entity.toString().toLowerCase().replaceAll("_", " ") + ".");
					if (isAtPuroPuro(player)) {
						if (entity == Entities.ESSENCE_IMPLING || entity == Entities.ECLECTIC_IMPLING) {
							AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
							if (!diary.isComplete(1, 8)) {
								diary.updateTask(player, 1, 8, true);
							}
						}
					}
					player.getInventory().deleteItem(new Item(isButterfly() ? 10012 : 11260, 1));
					if (isAtPuroPuro(player) || isButterfly())
						player.getInventory().addItem(new Item(entity.getJarId(), 1));
					else
						giveReward(player, entity);
				} else {
					npc.setNextForceTalk(new ForceTalk("Tehee, you missed me!"));
					player.sendMessage("..and you stumble and miss the " + (isButterfly() ? "butterfly." : "impling."));
					WorldTile forcedTile = forcedTile();
					int distance = getDistance(forcedTile);
					//npc.setNextGraphics(new Graphics(100));
					//player.setNextForceMovement(new ForceMovement(new WorldTile(player), 0, forcedTile, distance, Utils.getDirectionBetweenTiles(player, forcedTile)));
					npc.setNextForceMovement(new ForceMovement(new WorldTile(npc), 0, forcedTile, distance, Utils.getDirectionBetweenTiles(npc, forcedTile)));
					npc.setNextWorldTile(forcedTile);
					System.out.println(npc.getX() + " " + npc.getY());
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							npc.setCantInteract(false);
						}
					}, 100, TimeUnit.MILLISECONDS);
				}
			}
		}, 1);
		return -1;
	}

	private Integer getSpawnNPC() {
		for (int i = 14; i > 3; i--) {
			if (Utils.random(100) <= Entities.forEntityId(i).getChance())
				return Entities.forEntityId(i).getNPCId();
		}
		return Entities.BABY_IMPLING.npcId;
	}

	private boolean isButterfly() {
		if (entity == Entities.BLACK_WARLOCK || entity == Entities.RUBY_HARVEST || entity == Entities.SAPPHIRE_GLACIALIS
				|| entity == Entities.SNOWY_KNIGHT)
			return true;
		return false;
	}

	private WorldTile forcedTile() {
		WorldTile tile = new WorldTile(npc);
		for (int trycount = 0; trycount < 15; trycount++) {
			tile = new WorldTile(npc, 7);
			if (World.isTileFree(npc.getPlane(), tile.getX(), tile.getY(), 1))
				break;
		}
		return tile;
	}

	public static void giveReward(Player player, Entities entity) {
		int chance = Utils.random(100), requiredSpace = 1;
		Item loot;
		if (entity.getVeryRareLoot() != null && chance <= 3)
			loot = entity.getVeryRareLoot()[Utils.random(entity.getVeryRareLoot().length - 1)];
		else if (chance <= 10)
			loot = entity.getRareLoot()[Utils.random(entity.getRareLoot().length - 1)];
		else if (chance <= 25)
			loot = entity.getUncommonLoot()[Utils.random(entity.getUncommonLoot().length - 1)];
		else
			loot = entity.getCommonLoot()[Utils.random(entity.getCommonLoot().length - 1)];
		if (loot.getAmount() > 1 && !loot.getDefinitions().isStackable() && !loot.getDefinitions().isNoted())
			requiredSpace = loot.getAmount();
		else if (loot.getAmount() > 1 && (loot.getDefinitions().isStackable() || loot.getDefinitions().isNoted())
				&& player.getInventory().containsItem(loot.getId(), 1))
			requiredSpace = 0;
		if (player.getInventory().getFreeSlots() >= requiredSpace)
			player.getInventory().addItem(loot);
		else {
			int leftoverSpace = loot.getAmount() - player.getInventory().getFreeSlots();
			if (player.getInventory().hasFreeSlots())
				player.getInventory().addItem(loot.getId(), player.getInventory().getFreeSlots());
			else {
				World.addGroundItem(new Item(loot.getId(), leftoverSpace), new WorldTile(player));
			}
		}
		player.sendMessage("You find " + (loot.getAmount() > 1 ? "some " : "a ") + loot.getDefinitions().getName().toLowerCase()
						+ (loot.getAmount() > 1 && !loot.getDefinitions().getName().endsWith("s") ? "s" : "") + ".");
	}

	@Override
	public void stop(Player player) {
	}

}
