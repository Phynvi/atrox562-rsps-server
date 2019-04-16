package com.rs.game.player.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.player.Skills;
import com.rs.game.Graphics;

public class BattlestaffCrafting extends Action {

	/**
	 * @author Kris Handles the battlestaff creation process.
	 */
	public enum Battlestaff {

		AIR_BATTLESTAFF(573, 1397, 137.5, 66), WATER_BATTLESTAFF(571, 1395, 100, 54), EARTH_BATTLESTAFF(575, 1399,
				112.5, 58), FIRE_BATTLESTAFF(569, 1393, 125, 62);

		private double experience;
		private int levelRequired;
		private int orb, battlestaff;

		private static Map<Integer, Battlestaff> battlestaffs = new HashMap<Integer, Battlestaff>();

		public static Battlestaff forId(int id) {
			return battlestaffs.get(id);
		}

		static {
			for (Battlestaff BATTLESTAFF : Battlestaff.values()) {
				battlestaffs.put(BATTLESTAFF.getOrb(), BATTLESTAFF);
			}
		}

		private Battlestaff(int orb, int battlestaff, double experience, int levelRequired) {
			this.orb = orb;
			this.battlestaff = battlestaff;
			this.experience = experience;
			this.levelRequired = levelRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public int getOrb() {
			return orb;
		}

		public int getBattlestaff() {
			return battlestaff;
		}

	}

	public static boolean isOrb(int itemUsed) {
		switch (itemUsed) {
		case 569:
		case 575:
		case 573:
		case 571:
			return true;
		default:
			return false;
		}
	}

	public static void attach(Player player, Battlestaff battlestaff) {
		if (player.getInventory().getItems().getNumberOf(battlestaff.getOrb()) <= 1
				|| player.getInventory().getItems().getNumberOf(1391) <= 1)
			player.getActionManager().setAction(new BattlestaffCrafting(battlestaff, 1));
		else
			player.getDialogueManager().startDialogue("BattlestaffCraftingD", battlestaff);
	}

	private Battlestaff battlestaff;
	private int quantity;

	public BattlestaffCrafting(Battlestaff battlestaff, int quantity) {
		this.battlestaff = battlestaff;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < battlestaff.getLevelRequired()) {
			player.sendMessage(
					"You need a crafting level of " + battlestaff.getLevelRequired() + " to craft that battlestaff.");
			return false;
		}
		if (!player.getInventory().containsOneItem(battlestaff.getOrb())) {
			player.sendMessage("You don't have any "
					+ ItemDefinitions.getItemDefinitions(battlestaff.getOrb()).getName().toLowerCase() + " to attach.");
			return false;
		}
		if (!player.getInventory().containsOneItem(1391)) {
			player.sendMessage("You don't have any battlestaves to attach the orbs on.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (battlestaff.getOrb() == 575) {
			player.setNextGraphics(new Graphics(151));
		} else if (battlestaff.getOrb() == 569) {
			player.setNextGraphics(new Graphics(152));
		} else if (battlestaff.getOrb() == 571) {
			player.setNextGraphics(new Graphics(149));
		} else if (battlestaff.getOrb() == 573) {
			player.setNextGraphics(new Graphics(150));
		}
		player.getInventory().deleteItem(battlestaff.getOrb(), 1);
		player.getInventory().deleteItem(1391, 1);
		player.getInventory().addItem(battlestaff.getBattlestaff(), 1);
		player.getSkills().addXp(Skills.CRAFTING, battlestaff.getExperience() / 5);
		player.getPackets()
				.sendGameMessage("You attach the "
						+ ItemDefinitions.getItemDefinitions(battlestaff.getOrb()).getName().toLowerCase()
						+ " onto the battlestaff.", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		return 2;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}
