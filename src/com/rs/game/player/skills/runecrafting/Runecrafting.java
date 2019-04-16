package com.rs.game.player.skills.runecrafting;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.item.Item;
import com.rs.game.player.Skills;
import com.rs.game.Animation;
import com.rs.utils.Utils;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.cities.achievements.CityAchievements;
import com.rs.game.cities.achievements.AchievementDiary;

import java.util.HashMap;

public class Runecrafting {

	/**
	 * @author Kris Do not change the order within the enum, using ordinary()
	 *         method;
	 */
	public enum Runes {
		AIR_RUNE(556, 1, 5, 11, 1438, false, new WorldTile(2841, 4828, 0), new WorldTile(2985, 3294, 0)),
		MIND_RUNE(558, 2, 5.5, 14, 1448, false, new WorldTile(2793, 4827, 0), new WorldTile(2980, 3514, 0)),
		WATER_RUNE(555, 5, 6, 19, 1444, false, new WorldTile(3495, 4832, 0), new WorldTile(3183, 3165, 0)),
		EARTH_RUNE(557, 9, 6.5, 26, 1440, false, new WorldTile(2655, 4829, 0), new WorldTile(3304, 3474, 0)),
		FIRE_RUNE(554, 14, 7, 35, 1442, false, new WorldTile(2576, 4846, 0), new WorldTile(3311, 3255, 0)),
		BODY_RUNE(559, 20, 7.5, 46, 1446, false, new WorldTile(2521, 4833, 0), new WorldTile(3055, 3446, 0)),
		COSMIC_RUNE(564, 27, 8, 59, 1454, true, new WorldTile(2163, 4833, 0), new WorldTile(2407, 4379, 0)),
		LAW_RUNE(563, 54, 9.5, -1, 1458, true, new WorldTile(2464, 4817, 0), new WorldTile(2857, 3379, 0)),
		NATURE_RUNE(561, 44, 9, 91, 1462, true, new WorldTile(2473, 2400, 0), new WorldTile(2869, 3017, 0)),
		CHAOS_RUNE(562, 35, 8.5, 74, 1452, true, new WorldTile(2282, 4837, 0), new WorldTile(3059, 3589, 0)),
		DEATH_RUNE(560, 65, 10, -1, 1456, true, new WorldTile(2208, 4829, 0), new WorldTile(1863, 4639, 0)),
		BLOOD_RUNE(565, 77, 10.5, -1, 1450, true, new WorldTile(2468, 4888, 1), new WorldTile(3561, 9779, 0)),
		ASTRAL_RUNE(9075, 40, 8.7, 82, -1, true, null, null);

		private int runeId, requiredLevel, doubleRunes, talismanId;
		private double experience;
		private boolean pureEssenceOnly;
		private WorldTile portalCoords, ruinsCoords;

		private static final HashMap<Integer, Runes> RUNES = new HashMap<Integer, Runes>();

		static {
			for (Runes r : values())
				RUNES.put(r.ordinal(), r);
		}

		public static Runes getRune(int id) {
			return RUNES.get(id);
		}

		Runes(int runeId, int requiredLevel, double experience, int doubleRunes, int talismanId, boolean pureEssenceOnly, WorldTile portalCoords, WorldTile ruinsCoords) {
			this.runeId = runeId;
			this.requiredLevel = requiredLevel;
			this.experience = experience;
			this.doubleRunes = doubleRunes;
			this.talismanId = talismanId;
			this.pureEssenceOnly = pureEssenceOnly;
			this.portalCoords = portalCoords;
			this.ruinsCoords = ruinsCoords;
		}

		public int getRuneId() {
			return runeId;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public int getDoubleRunesLevel() {
			return doubleRunes;
		}

		public boolean isPureEssenceOnly() {
			return pureEssenceOnly;
		}

		public double getExperience() {
			return experience;
		}

		public WorldTile getPortalCoordinates() {
			return portalCoords;
		}

		public WorldTile getRuinsCoordinates() {
			return ruinsCoords;
		}

		public int getTalismanId() {
			return talismanId;
		}
	}

	public enum CombinationRunes {
		MIST_RUNE_AIR(4695, 555, 6, 8, 2478, 1444),
		MIST_RUNE_WATER(4695, 556, 6, 8.5, 2480, 1438),
		DUST_RUNE_AIR(4696, 557, 10, 8.3, 2478, 1440),
		DUST_RUNE_EARTH(4696, 556, 10, 9, 2481, 1438),
		MUD_RUNE_WATER(4698, 557, 13, 9.3, 2480, 1440),
		MUD_RUNE_EARTH(4698, 555, 13, 9.5, 2481, 1444),
		SMOKE_RUNE_AIR(4697, 554, 15, 8.5, 2478, 1442),
		SMOKE_RUNE_FIRE(4697, 556, 15, 9, 2482, 1438),
		STEAM_RUNE_WATER(4694, 554, 19, 9.5, 2480, 1442),
		STEAM_RUNE_FIRE(4694, 555, 19, 10, 2482, 1444),
		LAVA_RUNE_EARTH(4699, 554, 23, 10, 2481, 1442),
		LAVA_RUNE_FIRE(4699, 557, 23, 10.5, 2482, 1440);

		private int runeId, requiredRuneId, levelRequired, objectId, talismanId;
		double experience;

		CombinationRunes(int runeId, int requiredRuneId, int requiredLevel, double experience, int objectId, int talismanId) {
			this.runeId = runeId;
			this.requiredRuneId = requiredRuneId;
			this.levelRequired = requiredLevel;
			this.experience = experience;
			this.objectId = objectId;
			this.talismanId = talismanId;
		}

		public int getRuneId() {
			return runeId;
		}

		public int getRequiredRuneId() {
			return requiredRuneId;
		}

		public int getRequiredLevel() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getObjectId() {
			return objectId;
		}

		public int getTalismanId() {
			return talismanId;
		}

	}

	public static void craftRunes(Runes rune, Player player) {
		if (player.getSkills().getLevel(Skills.RUNECRAFTING) < rune.getRequiredLevel()) {
			player.sendMessage("You need at least " + rune.getRequiredLevel() + " Runecrafting " + "to runecraft " + rune.toString().toLowerCase() + "s.");
			return;
		}
		if (rune.isPureEssenceOnly() && !player.getInventory().containsItem(new Item(7936, 1))) {
			player.sendMessage("You need some pure essence to runecraft " + rune.toString().toLowerCase().replace("_", " ") + "s.");
			return;
		} else if (!rune.isPureEssenceOnly() && !player.getInventory().containsItem(new Item(1436, 1)) && !player.getInventory().containsItem(new Item(7936, 1))) {
			player.sendMessage("You need some " + (!rune.isPureEssenceOnly() ? "rune or " : "") + "pure essence to runecraft " + rune.toString().toLowerCase().replace("_", " ") + "s.");
			return;
		}
		if (player.inArea(2581, 4834, 2589, 4842)) {
			if (rune == Runes.FIRE_RUNE) {
				if (!player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE).isComplete(1, 9)) {
					player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE).updateTask(player, 1, 9, true);
				}
			}
		}
		int runes = rune.isPureEssenceOnly() ? player.getInventory().getNumberOf(7936) : (player.getInventory().getNumberOf(7936) + player.getInventory().getNumberOf(1436));
		if (!rune.isPureEssenceOnly())
			player.getInventory().deleteItem(new Item(1436, runes));
		player.getInventory().deleteItem(new Item(7936, runes));
		int multiplier = (int) Math.floor(player.getSkills().getLevel(Skills.RUNECRAFTING) / rune.getDoubleRunesLevel());
		multiplier += 1;
		player.setNextAnimation(new Animation(791));
		player.getSkills().addXp(Skills.RUNECRAFTING, rune.getExperience() * runes);
		player.getInventory().addItem(new Item(rune.getRuneId(), runes * multiplier));
		player.sendMessage("You bind the Temple's power into " + rune.toString().replace("_", " ").toLowerCase() + ".");
		if (rune == Runes.WATER_RUNE) {
			AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
			if (!diary.isComplete(0, 2)) {
				diary.updateTask(player, 0, 2, true);
			}
		}
		if (rune == Runes.COSMIC_RUNE && (runes * multiplier) == 56) {
			AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
			if (!diary.isComplete(2, 1)) {
				diary.updateTask(player, 2, 1, true);
			}
		}
	}

	public static boolean craftCombinationRunes(Player player, Item item, WorldObject object) {
		for (CombinationRunes cRune : CombinationRunes.values()) {
			if (cRune.getObjectId() == object.getId() && item.getId() == cRune.getTalismanId()) {
				if (player.getSkills().getLevel(Skills.RUNECRAFTING) < cRune.getRequiredLevel()) {
					player.sendMessage("You need at least level " + cRune.getRequiredLevel() + " Runecrafting to combine these runes.");
					return true;
				}
				if (!player.getInventory().containsItem(new Item(7936, 1))) {
					player.sendMessage("You need some pure essence in order to combine these runes.");
					return true;
				}
				if (!player.getInventory().containsItem(new Item(cRune.getRequiredRuneId(), 1))) {
					player.sendMessage("You need some " + ItemDefinitions.getItemDefinitions(cRune.getRequiredRuneId()).getName() + "s to combine these runes.");
					return true;
				}
				int runes = player.getInventory().getNumberOf(7936) < player.getInventory().getNumberOf(cRune.getRequiredRuneId()) ? player.getInventory().getNumberOf(7936) : player.getInventory().getNumberOf(cRune.getRequiredRuneId());
				boolean success = Utils.getRandom(100) > 50;
				if (player.getTemporaryAttributtes().get("MAGIC_IMBUE") == null)
					player.getInventory().deleteItem(cRune.getTalismanId(), 1);
				player.getInventory().deleteItem(7936, runes);
				player.getInventory().deleteItem(cRune.getRequiredRuneId(), runes);
				player.setNextAnimation(new Animation(791));
				if (success) {
					player.getInventory().addItem(new Item(cRune.getRuneId(), runes));
					player.sendMessage("You bind the Temple's power into " + cRune.toString().replace("_", " ").toLowerCase().replace(" fire", "").replace(" air", "").replace(" earth", "").replace(" water", "") + "s.");
				} else
					player.sendMessage("You fail to bind the Temple's power into " + cRune.toString().replace("_", " ").toLowerCase().replace(" fire", "").replace(" air", "").replace(" earth", "").replace(" water", "") + "s.");
				player.getSkills().addXp(Skills.RUNECRAFTING, cRune.getExperience() * runes);
				return true;
			}
		}
		return false;
	}

	public static boolean handleAltars(Player player, WorldObject object) {
		if (object.getId() >= 2478 && object.getId() <= 2488) {
			craftRunes(Runes.getRune(object.getId() - 2478), player);
			return true;
		} else if (object.getId() >= 2465 && object.getId() <= 2468 || object.getId() >= 2470 && object.getId() <= 2475 || object.getId() == 2469 && object.getX() == 2576) {
			player.setNextWorldTile(Runes.getRune(object.getId() - 2465).getRuinsCoordinates());
			return true;
		} else if (object.getId() == 30624) {
			craftRunes(Runes.BLOOD_RUNE, player);
			return true;
		} else if (object.getId() == 2477) {
			player.setNextWorldTile(new WorldTile(2468, 4888, 1));
			return true;
		} else if (object.getId() >= 2452 && object.getId() <= 2462) {
			player.setNextWorldTile(Runes.getRune(object.getId() - 2452).getPortalCoordinates());
			return true;
		} else if (object.getId() == 2464) {
			if (player.getEquipment().getHatId() != 5549 && player.getEquipment().getWeaponId() != 13641 && !player.getInventory().containsItem(1450, 1)) {
				player.sendMessage("A mystical force stops you from entering the ruins.");
				return true;
			}
			if (player.getEquipment().getHatId() != 5549 && player.getEquipment().getWeaponId() != 13641 && player.getInventory().containsItem(1450, 1))
				player.getInventory().deleteItem(new Item(1450, 1));
			player.setNextWorldTile(Runes.getRune(object.getId() - 2452).getPortalCoordinates());
		}
		return false;
	}

	public static boolean handleTalismanOnAltar(Player player, WorldObject object, Item item) {
		if (object.getId() >= 2452 && object.getId() <= 2462 && item.getId() >= 1438 && item.getId() <= 1456) {
			if (item.getId() == Runes.getRune(object.getId() - 2452).getTalismanId()) {
				//player.getInventory().deleteItem(item);
				player.setNextWorldTile(Runes.getRune(object.getId() - 2452).getPortalCoordinates());
				return true;
			}
		} else if (object.getId() == 2464 && item.getId() == 1450) {
			player.getInventory().deleteItem(item);
			player.setNextWorldTile(Runes.BLOOD_RUNE.getPortalCoordinates());
			return true;
		} else if (craftCombinationRunes(player, item, object))
			return true;
		return false;
	}

	public static void handleRuinsConfigs(Player player) {
		for (int i = 0; i < 11; i++)
			player.getPackets().sendConfigByFile(607 + i, 0);
		if (player.getEquipment().getWeaponId() >= 13630 && player.getEquipment().getWeaponId() <= 13641)
			player.getPackets().sendConfigByFile(607 + (player.getEquipment().getWeaponId() - 13630), 1);
		switch (player.getEquipment().getHatId()) {
		case 5527:
			player.getPackets().sendConfigByFile(607, 1);
			break;
		case 5529:
			player.getPackets().sendConfigByFile(608, 1);
			break;
		case 5531:
			player.getPackets().sendConfigByFile(609, 1);
			break;
		case 5533:
			player.getPackets().sendConfigByFile(612, 1);
			break;
		case 5535:
			player.getPackets().sendConfigByFile(610, 1);
			break;
		case 5537:
			player.getPackets().sendConfigByFile(611, 1);
			break;
		case 5539:
			player.getPackets().sendConfigByFile(613, 1);
			break;
		case 5541:
			player.getPackets().sendConfigByFile(615, 1);
			break;
		case 5543:
			player.getPackets().sendConfigByFile(614, 1);
			break;
		case 5545:
			player.getPackets().sendConfigByFile(616, 1);
			break;
		case 5547:
			player.getPackets().sendConfigByFile(617, 1);
			break;
		case 5549:
			player.getPackets().sendConfigByFile(618, 1);
			break;
		}
	}

}
