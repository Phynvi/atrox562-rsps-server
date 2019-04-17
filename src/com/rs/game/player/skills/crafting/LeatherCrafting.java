package com.rs.game.player.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.game.player.actions.Action;
import com.rs.utils.Utils;
import com.rs.game.player.Skills;
import com.rs.cache.loaders.ItemDefinitions;

/**
 * @author Kris Handles all types of leather crafting. NOTE: I've put coif into
 *         the hard-leather list because maximum amount of items that may be
 *         displayed on the interface is six, unfortunately.
 */
public class LeatherCrafting extends Action {

	public enum LeatherData {

		LEATHER_GLOVES(1741, 1, 1059, 1, 13.75),

		LEATHER_BOOTS(1741, 1, 1061, 7, 16.25),

		LEATHER_COWL(1741, 1, 1167, 9, 18.5),

		LEATHER_VAMBS(1741, 1, 1063, 11, 22),

		LEATHER_BODY(1741, 1, 1129, 14, 25),

		LEATHER_CHAPS(1741, 1, 1095, 18, 27),

		COIF(1743, 1, 1169, 38, 37),

		HARD_LEATHER_GLOVES(1743, 1, 1131, 28, 35),

		GREEN_D_HIDE_VAMBS(1745, 1, 1065, 57, 62),

		GREEN_D_HIDE_CHAPS(1745, 2, 1099, 60, 124),

		GREEN_D_HIDE_BODY(1745, 3, 1135, 63, 186),

		BLUE_D_HIDE_VAMBS(2505, 1, 2487, 66, 70),

		BLUE_D_HIDE_CHAPS(2505, 2, 2493, 68, 140),

		BLUE_D_HIDE_BODY(2505, 3, 2499, 71, 210),

		RED_D_HIDE_VAMBS(2507, 1, 2489, 73, 78),

		RED_D_HIDE_CHAPS(2507, 2, 2495, 75, 156),

		RED_D_HIDE_BODY(2507, 3, 2501, 77, 234),

		BLACK_D_HIDE_VAMBS(2509, 1, 2491, 79, 86),

		BLACK_D_HIDE_CHAPS(2509, 2, 2497, 82, 172),

		BLACK_D_HIDE_BODY(2509, 3, 2503, 84, 258),

		SNAKESKIN_BANDANA(6289, 5, 6326, 48, 45),

		SNAKESKIN_BODY(6289, 15, 6322, 53, 55),

		SNAKESKIN_CHAPS(6289, 12, 6324, 51, 50),

		SNAKESKIN_BOOTS(6289, 6, 6328, 45, 30),

		SNAKESKIN_VAMBRACES(6289, 8, 6330, 47, 35);

		private int leatherId, leatherAmount, finalProduct, requiredLevel;
		private double experience;
		private String name;

		private static Map<Integer, LeatherData> leatherItems = new HashMap<Integer, LeatherData>();

		static {
			for (LeatherData leather : LeatherData.values()) {
				leatherItems.put(leather.finalProduct, leather);
			}
		}

		public static LeatherData forId(int id) {
			return leatherItems.get(id);
		}

		private LeatherData(int leatherId, int leatherAmount, int finalProduct, int requiredLevel, double experience) {
			this.leatherId = leatherId;
			this.leatherAmount = leatherAmount;
			this.finalProduct = finalProduct;
			this.requiredLevel = requiredLevel;
			this.experience = experience;
			this.name = ItemDefinitions.getItemDefinitions(getFinalProduct()).getName()/*.replace("d'hide", "")*/;
		}

		public double getExperience() {
			return experience;
		}

		public int getFinalProduct() {
			return finalProduct;
		}

		public int getLeatherAmount() {
			return leatherAmount;
		}

		public int getLeatherId() {
			return leatherId;
		}

		public String getName() {
			return name;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}
	}

	private final Animation CRAFT_ANIMATION = new Animation(1249);
	public static final Item NEEDLE = new Item(1733);
	private static final Item THREAD = new Item(1734);

	private static int getIndex(Player player) {
		int leather = (Integer) player.getTemporaryAttributtes().get("leatherType");
		if (leather == LEATHER[0])
			return 0;
		if (leather == LEATHER[1])
			return 1;
		if (leather == LEATHER[2])
			return 2;
		if (leather == LEATHER[3])
			return 3;
		if (leather == LEATHER[4])
			return 4;
		if (leather == LEATHER[5])
			return 5;
		if (leather == LEATHER[6])
			return 6;
		if (leather == LEATHER[7])
			return 7;
		return -1;
	}

	public static boolean handleItemOnItem(Player player, Item itemUsed, Item usedWith) {
		for (int i = 0; i < LEATHER.length; i++) {
			if (itemUsed.getId() == LEATHER[i] || usedWith.getId() == LEATHER[i]) {
				player.getTemporaryAttributtes().put("leatherType", LEATHER[i]);
				int index = getIndex(player);
				if (index == -1)
					return true;
				if (itemUsed.getId() == 6289 || usedWith.getId() == 6289)
					player.getDialogueManager().startDialogue("LeatherCraftingD", LeatherData.forId(PRODUCTS[index][0]), LeatherData.forId(PRODUCTS[index][1]), LeatherData.forId(PRODUCTS[index][2]), LeatherData.forId(PRODUCTS[index][3]), LeatherData.forId(PRODUCTS[index][4]));
				else if (itemUsed.getId() == 1745 || itemUsed.getId() == 2505 || itemUsed.getId() == 2507 || itemUsed.getId() == 2509 || usedWith.getId() == 1745 || usedWith.getId() == 2505 || usedWith.getId() == 2509 || usedWith.getId() == 2507) {
					player.getDialogueManager().startDialogue("LeatherCraftingD", LeatherData.forId(PRODUCTS[index][0]), LeatherData.forId(PRODUCTS[index][1]), LeatherData.forId(PRODUCTS[index][2]));
				} else if (itemUsed.getId() == 1741 || usedWith.getId() == 1741) {
					player.getDialogueManager().startDialogue("LeatherCraftingD", LeatherData.forId(PRODUCTS[index][0]), LeatherData.forId(PRODUCTS[index][1]), LeatherData.forId(PRODUCTS[index][2]), LeatherData.forId(PRODUCTS[index][3]), LeatherData.forId(PRODUCTS[index][4]), LeatherData.forId(PRODUCTS[index][5]));

				} else if (itemUsed.getId() == 1743 || usedWith.getId() == 1743) {
					player.getDialogueManager().startDialogue("LeatherCraftingD", LeatherData.forId(PRODUCTS[index][0]), LeatherData.forId(PRODUCTS[index][1]));
				} else
					player.getDialogueManager().startDialogue("LeatherCraftingD", LeatherData.forId(PRODUCTS[index][0]), LeatherData.forId(PRODUCTS[index][1]), LeatherData.forId(PRODUCTS[index][2]));
				return true;
			}
		}
		return false;
	}

	private int quantity;

	private LeatherData data;

	private int removeThread = 5;

	private static final int LEATHER[] = { 1741, 1743, 1745, 2505, 2507, 2509, 6289, 24374 };

	private static final int PRODUCTS[][] = { { 1059, 1061, 1167, 1063, 1129, 1095 }, { 1169, 1131 }, { 1065, 1099, 1135, 25794 }, { 2487, 2493, 2499, 25796 }, { 2489, 2495, 2501, 25798 }, { 2491, 2497, 2503, 25800 }, { 6326, 6322, 6324, 6328, 6330 }, { 24376, 24379, 24382 } };

	public LeatherCrafting(LeatherData data, int quantity) {
		this.data = data;
		this.quantity = quantity;
	}

	private boolean checkAll(Player player) {
		if (player.getInterfaceManager().containsScreenInter() || player.getInterfaceManager().containsInventoryInter()) {
			player.sendMessage("Please finish what you're doing before attempting this action.");
			return false;
		}
		if (data.getRequiredLevel() > player.getSkills().getLevel(Skills.CRAFTING)) {
			player.sendMessage("You need a crafting level of " + data.getRequiredLevel() + " to craft this hide.");
			return false;
		}
		if (player.getInventory().getItems().getNumberOf(data.getLeatherId()) < data.getLeatherAmount()) {
			player.getPackets().sendGameMessage("You have insufficient materials in your inventory.");
			return false;
		}
		if (!player.getInventory().getItems().containsOne(THREAD)) {
			player.sendMessage("You need some thread to do this.");
			return false;
		}
		if (!player.getInventory().getItems().containsOne(NEEDLE)) {
			player.sendMessage("You need a needle to craft leather.");
			return false;
		}
		if (!player.getInventory().containsOneItem(data.getLeatherId())) {
			player.sendMessage("You've ran out of " + ItemDefinitions.getItemDefinitions(data.getLeatherId()).getName().toLowerCase() + ".");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(data.getLeatherId(), data.getLeatherAmount());
		player.getInventory().addItem(data.getFinalProduct(), 1);
		player.getSkills().addXp(Skills.CRAFTING, data.getExperience());
		player.sendMessage("You make a pair of " + data.getName().toLowerCase() + ".");
		quantity--;
		removeThread--;
		if (removeThread == 0) {
			removeThread = Utils.random(2, 6);
			player.getInventory().removeItems(THREAD);
			player.sendMessage("You use up one of your reels of thread.");
		}
		/*if (player.inArea(3240, 3255, 3266, 3300)) {
			if (data == LeatherData.COIF) {
				if (!player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE).isComplete(1, 4)) {
					player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE).updateTask(player, 1, 4, true);
				}
			}
		}*/
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(CRAFT_ANIMATION);
		return 0;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, 1);
		player.setNextAnimation(CRAFT_ANIMATION);
		return true;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}