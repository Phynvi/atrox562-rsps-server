package com.rs.game.player.skills.magic;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.item.Item;
import com.rs.game.player.content.Magic;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public class MagicOnItem {

	private static final int LOW_ALCHEMY = 38;
	private static final int HIGH_ALCHEMY = 59;
	private static final int SUPER_HEAT = 50;

	private static final int LV1_ENCHANT = 29;
	private static final int LV2_ENCHANT = 41;
	private static final int LV3_ENCHANT = 53;
	private static final int LV4_ENCHANT = 61;
	private static final int LV5_ENCHANT = 76;
	private static final int LV6_ENCHANT = 86;

	private enum SuperHeat {
		BRONZE_BAR(new Item(2349, 1), new Item(438, 1), new Item(436, 1), 1, 6.2), IRON_BAR(new Item(2351, 1),
				new Item(440, 1), null, 15, 12.5), STEEL_BAR(new Item(2353, 1), new Item(440, 1), new Item(453, 2), 30,
						17.5), SILVER_BAR(new Item(2355, 1), new Item(442, 1), null, 20, 13.7), GOLD_BAR(
								new Item(2357, 1), new Item(444, 1), null, 40, 22.5), MITHRIL_BAR(new Item(2359, 1),
										new Item(447, 1), new Item(453, 4), 50, 30), ADAMANTITE_BAR(new Item(2361, 1),
												new Item(449, 1), new Item(453, 6), 70, 37.5), RUNITE_BAR(
														new Item(2363, 1), new Item(451, 1), new Item(453, 8), 85, 50);

		Item bar, primaryOre, secondaryOre;
		int level;
		double experience;

		private SuperHeat(Item bar, Item primaryOre, Item secondaryOre, int level, double experience) {
			this.bar = bar;
			this.primaryOre = primaryOre;
			this.secondaryOre = secondaryOre;
			this.level = level;
			this.experience = experience;
		}

		static final Map<Integer, SuperHeat> ore = new HashMap<Integer, SuperHeat>();

		public static SuperHeat forId(int id) {
			return ore.get(id);
		}

		static {
			for (SuperHeat ores : SuperHeat.values())
				ore.put((int) ores.primaryOre.getId(), ores);
		}

		public Item getBar() {
			return bar;
		}

		public Item getPrimaryOre() {
			return primaryOre;
		}

		public Item getSecondaryOre() {
			return secondaryOre;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}

	public static void handleMagic(Player player, int magicId, Item item) {
		switch (magicId) {
		case LOW_ALCHEMY:
			processAlchemy(item, player, true);
			break;
		case HIGH_ALCHEMY:
			processAlchemy(item, player, false);
			break;
		case SUPER_HEAT:
			if (item.getId() == 453) {
				player.sendMessage("You cannot cast this spell on coal. You must choose the primary ore of the bar.");
				return;
			}
			SuperHeat chosenItem = SuperHeat.forId(item.getId());
			if (chosenItem == null) {
				if (item.getId() == 436)
					chosenItem = SuperHeat.BRONZE_BAR;
			}
			if (chosenItem != null) {
				/**
				 * If meets the requirements, they will automatically superheat
				 * the bar into steel.
				 */
				if (item.getId() == 440)
					if (player.getSkills().getLevel(Skills.SMITHING) >= 30
							&& player.getInventory().containsItem(new Item(453, 2)))
						chosenItem = SuperHeat.STEEL_BAR;
				if (chosenItem.getSecondaryOre() != null
						&& !player.getInventory().containsItem(chosenItem.getSecondaryOre())) {
					player.sendMessage("You need at least " + chosenItem.getSecondaryOre().getAmount() + " "
							+ chosenItem.getSecondaryOre().getName().toLowerCase() + " to superheat a bar of "
							+ chosenItem.getBar().getName().toLowerCase().replace(" bar", "."));
					return;
				}
				if (player.getSkills().getLevel(Skills.SMITHING) < chosenItem.getLevel()) {
					player.sendMessage(
							"You need at least level " + chosenItem.getLevel() + " Smithing to superheat this bar.");
					return;
				}
				if (!Magic.checkSpellRequirements(player, 43, true, 554, 4, 561, 1))
					return;
				player.getSkills().addXp(Skills.MAGIC, 53);
				player.getSkills().addXp(Skills.SMITHING, chosenItem.getExperience());
				player.setNextAnimation(new Animation(725));
				player.setNextGraphics(new Graphics(148));
				player.getInventory().deleteItem(chosenItem.getPrimaryOre());
				if (chosenItem.getSecondaryOre() != null)
					player.getInventory().deleteItem(chosenItem.getSecondaryOre());
				player.getInventory().addItem(chosenItem.getBar());
				player.sendMessage(
						"You superheat a bar of " + chosenItem.getBar().getName().toLowerCase().replace(" bar", "."));
				player.getPackets().sendButtonConfig(168, 7);
			} else
				player.sendMessage("You cannot superheat that!");
			break;
		case LV1_ENCHANT:
			Enchanting.startEnchant(player, item, 1);
			break;
		case LV2_ENCHANT:
			Enchanting.startEnchant(player, item, 2);
			break;
		case LV3_ENCHANT:
			Enchanting.startEnchant(player, item, 3);
			break;
		case LV4_ENCHANT:
			Enchanting.startEnchant(player, item, 4);
			break;
		case LV5_ENCHANT:
			Enchanting.startEnchant(player, item, 5);
			break;
		case LV6_ENCHANT:
			Enchanting.startEnchant(player, item, 6);
			break;
		default:
			break;
		}
	}

	private static void processAlchemy(Item item, Player player, boolean lowAlch) {
		if (player.isLocked())
			return;
		if (player.getSkills().getLevel(Skills.MAGIC) < (lowAlch == true ? 21 : 55)) {
			player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
			return;
		}
		if (!player.getInventory().containsItem(item.getId(), 1))
			return;
		if (item.getId() == 995) {
			player.getPackets().sendGameMessage("You can't " + (lowAlch == true ? "low" : "high") + " alch this!");
			return;
		}
		if (hasFireStaff(player) && !player.getInventory().containsItem(561, 1)) {
				player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
				return;
			} else {
			if (!hasFireStaff(player) && !player.getInventory().containsItem(554, (lowAlch == true ? 3 : 5))) {
				player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
				return;
			}
		}

		if (!hasFireStaff(player))
			player.getInventory().deleteItem(554, (lowAlch ? 3 : 5));
		player.setNextAnimation(getAnim(hasFireStaff(player), lowAlch));
		player.setNextGraphics(getGfx(hasFireStaff(player), lowAlch));
		player.getInventory().deleteItem(561, 1);
		int value = (int) (item.getDefinitions().getTipitPrice() * (lowAlch ? 0.85 : 0.95));
		player.getInventory().deleteItem(item.getId(), 1);
		player.getInventory().addItem(995, value);
		player.lock(1);
		player.getSkills().addXp(Skills.MAGIC, (lowAlch ? 31 : 65));
		player.getPackets().sendButtonConfig(168, 7);
	}

	private static Animation getAnim(boolean hasStaff, boolean lowAlch) {
		if (hasStaff && lowAlch == true)
			return new Animation(9625);
		if (hasStaff && lowAlch == false)
			return new Animation(9633);
		if (!hasStaff && lowAlch == true)
			return new Animation(712);
		if (!hasStaff && lowAlch == false)
			return new Animation(713);
		return null;
	}

	private static Graphics getGfx(boolean hasStaff, boolean lowAlch) {
		if (hasStaff && lowAlch == true)
			return new Graphics(1692);
		if (hasStaff && lowAlch == false)
			return new Graphics(1693);
		if (!hasStaff && lowAlch == true)
			return new Graphics(112);
		if (!hasStaff && lowAlch == false)
			return new Graphics(113);
		return null;
	}

	private static boolean hasFireStaff(Player player) {
		return player.getEquipment().getWeaponId() == 1387;
	}

}
