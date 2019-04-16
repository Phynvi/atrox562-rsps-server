package com.rs.game.player.skills.firemaking;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;

public class GnomishFirelighters {

	private static final int LOGS = 1511;
	private static final int RED_FIRELIGHTER = 7329;
	private static final int GREEN_FIRELIGHTER = 7330;
	private static final int BLUE_FIRELIGHTER = 7331;
	private static final int PURPLE_FIRELIGHTER = 10326;
	private static final int WHITE_FIRELIGHTER = 10327;
	private static final int RED_LOGS = 7404;
	private static final int GREEN_LOGS = 7405;
	private static final int BLUE_LOGS = 7406;
	private static final int PURPLE_LOGS = 10329;
	private static final int WHITE_LOGS = 10328;

	public enum Colourables {

		RED_GNOMISH_LOGS(RED_FIRELIGHTER, RED_LOGS), GREEN_GNOMISH_LOGS(GREEN_FIRELIGHTER,
				GREEN_LOGS), BLUE_GNOMISH_LOGS(BLUE_FIRELIGHTER, BLUE_LOGS), PURPLE_GNOMISH_LOGS(PURPLE_FIRELIGHTER,
						PURPLE_LOGS), WHITE_GNOMISH_LOGS(WHITE_FIRELIGHTER, WHITE_LOGS);

		private int dye, product;

		/**
		 * Used to obtain the value based off of original item and dye
		 */
		public static Colourables forItem(int item, int dye) {
			for (Colourables product : Colourables.values()) {
				if (item == LOGS && product.dye == dye || item == product.dye && dye == LOGS) {
					return product;
				}
			}
			return null;
		}

		/**
		 * Used to obtain the value based off of the product
		 */
		public static Colourables forProduct(int item) {
			for (Colourables product : Colourables.values()) {
				if (product.product == item) {
					return product;
				}
			}
			return null;
		}

		public int getDye() {
			return dye;
		}

		public int getProduct() {
			return product;
		}

		private Colourables(int dye, int product) {
			this.dye = dye;
			this.product = product;
		}
	}

	public static boolean ApplyDyeToItems(Player player, int used, int usedWith) {
		if (!player.getInventory().containsItem(used, 1) || !player.getInventory().containsItem(usedWith, 1)) {
			return false;
		}
		if (usedWith == LOGS) {
			player.getInventory().deleteItem(used, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItem(Colourables.forItem(used, usedWith).getProduct(), 1);
		} else {
			player.getInventory().deleteItem(used, 1);
			player.getInventory().deleteItem(usedWith, 1);
			player.getInventory().addItem(Colourables.forItem(used, usedWith).getProduct(), 1);
		}
		if (Colourables.forItem(used, usedWith) != null)
			player.getPackets()
					.sendGameMessage("You carefully colour the logs with " + (ItemDefinitions
							.getItemDefinitions(Colourables.forItem(used, usedWith).getDye()).getName().toLowerCase())
							+ ".", true);
		return true;
	}

}