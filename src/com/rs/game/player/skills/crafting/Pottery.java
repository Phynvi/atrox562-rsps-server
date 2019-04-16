package com.rs.game.player.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.player.actions.Action;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.game.WorldObject;

/**
 * @author Kris Handles the pottery stuff.
 */

public class Pottery extends Action {

	public static final Animation MOULDING_ANIMATION = new Animation(883);
	public static final Animation FIRING_ANIMATION = new Animation(899);

	public enum PotteryData {
		POT(1, new Item(1787, 1), 6, new Item(1931, 1), 5), PIE_DISH(7, new Item(1789, 1), 15, new Item(2313, 1),
				10), BOWL(8, new Item(1791, 1), 18, new Item(1923, 1), 12.5), PLANT_POT(19, new Item(5352, 1), 20,
						new Item(5350, 1), 17.5), POT_LID(25, new Item(4438, 1), 20, new Item(4440, 1), 20);

		private int level;
		private double experience, finishedExperience;
		private Item product, finishedProduct;

		private static Map<Integer, PotteryData> PotteryItems = new HashMap<Integer, PotteryData>();

		public static PotteryData forId(int id) {
			return PotteryItems.get(id);
		}

		static {
			for (PotteryData POT : PotteryData.values()) {
				PotteryItems.put((int) POT.getProduct().getId(), POT);
			}
		}

		private static Map<Integer, PotteryData> firedItems = new HashMap<Integer, PotteryData>();

		public static PotteryData forFiredId(int id) {
			return firedItems.get(id);
		}

		static {
			for (PotteryData POT : PotteryData.values()) {
				firedItems.put((int) POT.getFinishedProduct().getId(), POT);
			}
		}

		private PotteryData(int level, Item product, double experience, Item finishedProduct,
				double finishedExperience) {
			this.level = level;
			this.product = product;
			this.experience = experience;
			this.finishedProduct = finishedProduct;
			this.finishedExperience = finishedExperience;
		}

		public int getLevel() {
			return level;
		}

		public Item getProduct() {
			return product;
		}

		public double getExperience() {
			return experience;
		}

		public Item getFinishedProduct() {
			return finishedProduct;
		}

		public double getFinishedExperience() {
			return finishedExperience;
		}
	}

	private PotteryData data;
	private int quantity;

	public Pottery(PotteryData data, int quantity) {
		this.data = data;
		this.quantity = quantity;
	}

	public static boolean handleObjectClick(WorldObject object, Player player) {
		/**
		 * Can't be fucked with indexes and shit, it's only four items that I'll
		 * never be changing again.
		 */
		if (object.getId() == 11601) {
			player.getTemporaryAttributtes().put("Pottery", false);
			player.getDialogueManager().startDialogue("PotteryD", Pottery.PotteryData.forFiredId(1931),
					Pottery.PotteryData.forFiredId(2313), Pottery.PotteryData.forFiredId(1923),
					Pottery.PotteryData.forFiredId(5350), Pottery.PotteryData.forFiredId(4440));
			return true;
		}
		return false;
	}

	public static boolean handleItemOnObject(WorldObject object, Player player, Item item) {
		if (item.getId() == 1761 && object.getId() == 2642) {
			player.getTemporaryAttributtes().put("Pottery", true);
			player.getDialogueManager().startDialogue("PotteryD", Pottery.PotteryData.forId(1787),
					Pottery.PotteryData.forId(1789), Pottery.PotteryData.forId(1791), Pottery.PotteryData.forId(5352),
					Pottery.PotteryData.forId(4438));
			return true;
		}
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
			if (player.getTemporaryAttributtes().get("Pottery").equals(true))
				player.sendMessage("You need at least level " + data.getLevel() + " Crafting to mould "
						+ (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getProduct().getName().toLowerCase() + ".");
			else
				player.sendMessage("You need at least level " + data.getLevel() + " Crafting to fire "
						+ (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getProduct().getName().toLowerCase() + ".");
			return false;
		}
		if (player.getTemporaryAttributtes().get("Pottery").equals(true)) {

			if (!player.getInventory().containsItem(new Item(1761, 1))) {
				player.sendMessage("You need at least one soft clay to mould "
						+ (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getProduct().getName());
				return false;
			}
			return true;
		} else {
			if (!player.getInventory().containsItem(data.getProduct())) {
				player.sendMessage("You need at least one " + data.getProduct().getName().toLowerCase() + " to fire "
						+ (Utils.startsWithVowel(data.getProduct().getName().replace("Unfired ", "")) ? "an " : "a ")
						+ data.getFinishedProduct().getName().toLowerCase().replace("Unfired ", "") + ".");
				return false;
			}
			return true;
		}
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (quantity <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.getLevel()) {
			if (player.getTemporaryAttributtes().get("Pottery").equals(true))
				player.sendMessage("You need at least level " + data.getLevel() + " Crafting to mould "
						+ (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getProduct().getName().toLowerCase() + ".");
			else
				player.sendMessage("You need at least level " + data.getLevel() + " Crafting to fire "
						+ (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getProduct().getName().toLowerCase() + ".");
			return false;
		}
		if (player.getTemporaryAttributtes().get("Pottery").equals(true)) {
			if (!player.getInventory().containsItem(new Item(1761, 1))) {
				player.sendMessage("You need at least one soft clay to mould "
						+ (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getProduct().getName());
				return false;
			}
		} else {
			if (!player.getInventory().containsItem(data.getFinishedProduct())) {
				player.sendMessage("You need at least one " + data.getFinishedProduct().getName().toLowerCase()
						+ " to fire " + (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
						+ data.getFinishedProduct().getName().toLowerCase().replace("Unfired ", ""));
				return false;
			}
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		quantity--;
		if (player.getTemporaryAttributtes().get("Pottery").equals(true)) {
			player.setNextAnimation(MOULDING_ANIMATION);
			player.getInventory().deleteItem(new Item(1761, 1));
			player.getInventory().addItem(data.getProduct());
			player.getSkills().addXp(Skills.CRAFTING, data.getExperience());
			player.sendMessage("You mould " + (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
					+ data.getProduct().getName().toLowerCase() + ".");
		} else {
			player.setNextAnimation(MOULDING_ANIMATION);
			player.getInventory().deleteItem(data.getProduct());
			player.getInventory().addItem(data.getFinishedProduct());
			player.getSkills().addXp(Skills.CRAFTING, data.getFinishedExperience());
			player.sendMessage("You fire " + (Utils.startsWithVowel(data.getProduct().getName()) ? "an " : "a ")
					+ data.getProduct().getName().toLowerCase() + ".");
		}
		return 2;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 1);
		return;
	}
}
