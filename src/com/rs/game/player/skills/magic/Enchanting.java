package com.rs.game.player.skills.magic;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.content.Magic;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.game.player.actions.Action;

public class Enchanting {

	private static final int AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, COSMIC_RUNE = 564;
	
	private static short[] BOLT_NODES = { 879, 9337, 9335, 880, 9338, 9336, 9339, 9340, 9341, 9342 };
	private static short[] BOLT_PRODCUTS = { 9236, 9240, 9237, 9238, 9241, 9239, 9242, 9243, 9244, 9245 };
	private static int[][] BOLT_RUNES = { { 564, 1, 556, 2 }, { 564, 1, 555, 1, 558, 1 }, { 564, 1, 557, 2 }, { 564, 1, 555, 2 }, { 564, 1, 556, 3, 561, 1 }, { 564, 1, 554, 2 }, { 564, 1, 554, 5, 565, 1 }, { 564, 1, 557, 10, 563, 2 }, { 564, 1, 557, 15, 566, 1 }, { 564, 1, 554, 20, 560, 1 } };
	private static double[] BOLT_EXPERIENCE = { 9, 17, 19, 29, 37, 33, 59, 67, 78, 97 };
	private static byte[] BOLT_LEVELS = { 4, 7, 14, 24, 27, 29, 49, 57, 68, 87 };

	public enum Types {
		SAPPHIRE(1, 7, 2550, 3853, 1727, 11074), EMERALD(2, 27, 2552, 5521, 1729, 11079), RUBY(3, 49, 2568, 11194, 1725,
				11088), DIAMOND(4, 57, 2570, 11090, 1731,
						11095), DRAGONSTONE(5, 68, 2572, 11105, 1712, 11118), ONYX(6, 87, 6583, 11128, 6585, 11133);

		int enchantLevel, level, ring, necklace, amulet, bracelet;

		Types(int enchantLevel, int level, int ring, int necklace, int amulet, int bracelet) {
			this.enchantLevel = enchantLevel;
			this.level = level;
			this.ring = ring;
			this.necklace = necklace;
			this.amulet = amulet;
			this.bracelet = bracelet;
		}
	}
	
	public static int getComponentIndex(int componentId) {
		if (componentId == 14)
			return 0;
		else if (componentId == 29)
			return 1;
		else if (componentId == 18)
			return 2;
		else if (componentId == 22)
			return 3;
		else if (componentId == 32)
			return 4;
		else if (componentId == 26)
			return 5;
		else if (componentId == 35)
			return 6;
		else if (componentId == 38)
			return 7;
		else if (componentId == 41)
			return 8;
		else if (componentId == 44)
			return 9;
		return -1;
	}
	
	public static void processBoltEnchantSpell(Player player, final int index, final int ticks) {
		player.lock(3);
		player.getActionManager().setAction(new Action() {

			private int cycle = ticks;

			@Override
			public boolean start(Player player) {
				if (!Magic.checkSpellLevel(player, BOLT_LEVELS[index]))
					return false;
				player.closeInterfaces();
				return true;
			}

			@Override
			public boolean process(Player player) {
				if (player.getInventory().getAmountOf(BOLT_NODES[index]) == 0)
					return false;
				return cycle > 0;
			}

			@Override
			public int processWithDelay(Player player) {
				if (!Magic.checkRunes(player, true, BOLT_RUNES[index]))
					return -1;
				cycle--;
				int preBolt = BOLT_NODES[index];
				int boltAmount = player.getInventory().getAmountOf(preBolt);
				int createdCount = boltAmount > 10 ? 10 : boltAmount;
				player.setNextGraphics(new Graphics(759));
				player.setNextAnimation(new Animation(4462));
				player.getInventory().deleteItem(preBolt, createdCount);
				player.getInventory().addItem(BOLT_PRODCUTS[index], createdCount);
				player.getSkills().addXp(Skills.MAGIC, BOLT_EXPERIENCE[index]);
				return 2;
			}

			@Override
			public void stop(Player player) {
				setActionDelay(player, 3);
			}
		});
	}

	public static boolean startEnchant(Player player, Item item, int enchantLevel) {
		Base base = getBase(enchantLevel);
		if (!canBeEnchanted(base, item.getId())) {
			player.sendMessage("This item can't be enchanted.");
			return false;
		}
		for (Types t : Types.values()) {
			if (enchantLevel == t.enchantLevel) {
				if (player.getSkills().getLevel(Skills.MAGIC) < t.level) {
					player.sendMessage(
							"You need at least level " + t.level + " Magic to enchant " + t.name().toLowerCase() + ".");
					return false;
				}
				if (item.getId() == base.baseRing)
					return enchant(player, t.level, base.baseRing, t.ring, enchantLevel);
				if (item.getId() == base.baseNecklace)
					return enchant(player, t.level, base.baseNecklace, t.necklace, enchantLevel);
				if (item.getId() == base.baseAmulet)
					return enchant(player, t.level, base.baseAmulet, t.amulet, enchantLevel);
				if (item.getId() == base.baseBracelet)
					return enchant(player, t.level, base.baseBracelet, t.bracelet, enchantLevel);
			}
		}
		return false;
	}

	public static boolean enchant(Player player, int reqLevel, int toEnchant, int toMake, int enchantLevel) {
		String name = ItemDefinitions.getItemDefinitions(toEnchant).getName();
		String makeName = ItemDefinitions.getItemDefinitions(toMake).getName();

		if (!checkRunes(player, reqLevel, enchantLevel))
			return false;

		if (enchantLevel <= 2) {
			player.setNextAnimation(new Animation(719));
			player.setNextGraphics(new Graphics(114, 0, 100));
		} else if (enchantLevel > 2 && enchantLevel <= 4) {
			player.setNextAnimation(new Animation(720));
			player.setNextGraphics(new Graphics(115, 0, 100));
		} else if (enchantLevel == 5) {
			player.setNextAnimation(new Animation(721));
			player.setNextGraphics(new Graphics(116, 0, 100));
		} else if (enchantLevel == 6) {
			player.setNextAnimation(new Animation(721));
			player.setNextGraphics(new Graphics(452, 0, 100));
		}

		player.getInventory().deleteItem(toEnchant, 1);
		player.getInventory().addItem(toMake, 1);
		player.getSkills().addXp(Skills.MAGIC, (enchantLevel * 10));
		player.sendMessage("You enchant the " + name.toLowerCase() + " into "
				+ (Utils.startsWithVowel(makeName) ? "an " : "a ") + makeName.toLowerCase().replace("(4)", "") + ".");
		player.getPackets().sendButtonConfig(168, 7);
		return false;
	}

	public static boolean checkRunes(Player player, int level, int enchantLevel) {
		if (enchantLevel == 1)
			return Magic.checkSpellRequirements(player, level, true, COSMIC_RUNE, 1, WATER_RUNE, 1);
		else if (enchantLevel == 2)
			return Magic.checkSpellRequirements(player, level, true, COSMIC_RUNE, 1, AIR_RUNE, 3);
		else if (enchantLevel == 3)
			return Magic.checkSpellRequirements(player, level, true, COSMIC_RUNE, 1, FIRE_RUNE, 3);
		else if (enchantLevel == 4)
			return Magic.checkSpellRequirements(player, level, true, COSMIC_RUNE, 1, EARTH_RUNE, 10);
		else if (enchantLevel == 5)
			return Magic.checkSpellRequirements(player, level, true, COSMIC_RUNE, 1, EARTH_RUNE, 15, WATER_RUNE, 15);
		else if (enchantLevel == 6)
			return Magic.checkSpellRequirements(player, level, true, COSMIC_RUNE, 1, FIRE_RUNE, 20, EARTH_RUNE, 20);
		return false;
	}

	public enum Base {

		SAPPHIRE(1, 1637, 1656, 1694, 11072), EMERALD(2, 1639, 1658, 1696, 11076), RUBY(3, 1641, 1660, 1698,
				11085), DIAMOND(4, 1643, 1662, 1700,
						11092), DRAGONSTONE(5, 1645, 1664, 1702, 11115), ONYX(6, 6575, 6577, 6581, 11130);

		int level, baseRing, baseNecklace, baseAmulet, baseBracelet;

		Base(int level, int baseRing, int baseNecklace, int baseAmulet, int baseBracelet) {
			this.level = level;
			this.baseRing = baseRing;
			this.baseNecklace = baseNecklace;
			this.baseAmulet = baseAmulet;
			this.baseBracelet = baseBracelet;
		}

	}

	public static boolean canBeEnchanted(Base b, int itemId) {
		return b.baseAmulet == itemId || b.baseNecklace == itemId || b.baseRing == itemId || b.baseBracelet == itemId;
	}

	public static Base getBase(int type) {
		for (Base t : Base.values()) {
			if (type == t.level) {
				return t;
			}
		}
		return null;
	}

}
