package com.rs.game.player.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.actions.Action;
import com.rs.game.item.Item;
import com.rs.game.player.Skills;
import com.rs.game.Animation;
import com.rs.game.player.Player;

/**
 * @Author Kris Handles the glassblowing
 **/

public class GlassBlowing extends Action {

	private final Animation CRAFT_ANIMATION = new Animation(884);
	public static final Item PIPE = new Item(1785);
	private int quantity;
	private GlassData data;

	private static final int GLASS = 1775;

	public static final int PRODUCTS[] = { 1919, 4527, 4522, 229, 6667, 567, 4542, 10973 };

	public enum GlassData {

		BEER_GLASS(1775, 1, 1919, 1, 17.5), CANDLE_LANTERN(1775, 1, 4527, 4, 19), OIL_LAMP(1775, 1, 4522, 12, 25), VIAL(
				1775, 1, 229, 33, 35), FISHBOWL(1775, 1, 6667, 42, 42.5), UNPOWDERED_ORD(1775, 1, 567, 46,
						52.5), LANTERN_LENS(1775, 1, 4542, 49, 55), LIGHT_ORB(1775, 1, 10973, 87, 70);

		private int glassId, glassAmount, finalProduct, requiredLevel;
		private double experience;
		private String name;

		private static Map<Integer, GlassData> glassItems = new HashMap<Integer, GlassData>();

		public static GlassData forId(int id) {
			return glassItems.get(id);
		}

		static {
			for (GlassData glass : GlassData.values()) {
				glassItems.put(glass.finalProduct, glass);
			}
		}

		private GlassData(int glassId, int glassAmount, int finalProduct, int requiredLevel, double experience) {
			this.glassId = glassId;
			this.glassAmount = glassAmount;
			this.finalProduct = finalProduct;
			this.requiredLevel = requiredLevel;
			this.experience = experience;
			this.name = ItemDefinitions.getItemDefinitions(getFinalProduct()).getName().replace("vial", "glass");
		}

		public int getglassId() {
			return glassId;
		}

		public int getglassAmount() {
			return glassAmount;
		}

		public int getFinalProduct() {
			return finalProduct;
		}

		public int getRequiredLevel() {
			return requiredLevel;
		}

		public double getExperience() {
			return experience;
		}

		public String getName() {
			return name;
		}
	}

	public static boolean handleItemOnItem(Player player, Item itemUsed, Item usedWith) {
		if (itemUsed.getId() == GLASS || usedWith.getId() == GLASS) {
			player.getTemporaryAttributtes().put("glassType", GLASS);
			int index = getIndex(player);
			if (index == -1)
				return true;
			int glass = (Integer) player.getTemporaryAttributtes().get("glassType");
			if (glass == GLASS) {
				player.getDialogueManager().startDialogue("GlassblowingOption");
				return true;
			}
		}
		return false;
	}

	private static int getIndex(Player player) {
		int glass = (Integer) player.getTemporaryAttributtes().get("glassType");
		if (glass == GLASS)
			return 0;
		return -1;
	}

	public GlassBlowing(GlassData data, int quantity) {
		this.data = data;
		this.quantity = quantity;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, 3);
		player.setNextAnimation(CRAFT_ANIMATION);
		return true;
	}

	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.CRAFTING) < data.getRequiredLevel()) {
			player.getPackets()
					.sendGameMessage("You need a crafting level of " + data.getRequiredLevel() + " to craft this.");
			return false;
		}
		if (player.getInventory().getItems().getNumberOf(data.getglassId()) < data.getglassAmount()) {
			player.getPackets()
					.sendGameMessage("You don't have enough "
							+ ItemDefinitions.getItemDefinitions(data.getglassId()).getName().toLowerCase()
							+ " in your inventory to do this.");
			return false;
		}
		if (!player.getInventory().containsOneItem(data.getglassId())) {
			player.getPackets().sendGameMessage("You've ran out of "
					+ ItemDefinitions.getItemDefinitions(data.getglassId()).getName().toLowerCase() + ".");
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
		player.getInventory().deleteItem(data.getglassId(), data.getglassAmount());
		player.getInventory().addItem(data.getFinalProduct(), 1);
		player.getSkills().addXp(Skills.CRAFTING, data.getExperience());
		player.getPackets().sendGameMessage("You make a " + data.getName().toLowerCase() + ".", true);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(CRAFT_ANIMATION);
		stop(player);
		return 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 1);
	}

}