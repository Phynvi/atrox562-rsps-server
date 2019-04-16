package com.rs.game.player.skills.crafting;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class Tanning {

	public enum TanData {
		SOFT_LEATHER(1, new Item(1739, 1), new Item(1741, 1)), HARD_LEATHER(3, new Item(1739, 1), new Item(1743,
				1)), SNAKESKIN_TAI_BWO_WANNAI(15, new Item(6287, 1), new Item(6289, 1)), SNAKESKIN_TEMPLE_TREKKING(20,
						new Item(7801, 1),
						new Item(6289, 1)), GREEN_DRAGONHIDE(20, new Item(1753, 1), new Item(1745, 1)), BLUE_DRAGONHIDE(
								20, new Item(1751, 1), new Item(2505, 1)), RED_DRAGONHIDE(20, new Item(1749, 1),
										new Item(2507, 1)), BLACK_DRAGONHIDE(20, new Item(1747, 1), new Item(2509, 1));

		int cost;
		Item leather, tannedLeather;

		private TanData(int cost, Item leather, Item tannedLeather) {
			this.cost = cost;
			this.leather = leather;
			this.tannedLeather = tannedLeather;
		}

		public int getCost() {
			return cost;
		}

		public Item getLeather() {
			return leather;
		}

		public Item getTannedLeather() {
			return tannedLeather;
		}
	}

	public static void handleInterface(Player player, int interfaceId, int componentId, int packetId) {
		if (interfaceId == 324) {
			switch (componentId) {
			case 1:
				tanLeather(player, TanData.SOFT_LEATHER, getAmountByPacket(packetId));
				break;
			case 2:
				tanLeather(player, TanData.HARD_LEATHER, getAmountByPacket(packetId));
				break;
			case 3:
				tanLeather(player, TanData.SNAKESKIN_TAI_BWO_WANNAI, getAmountByPacket(packetId));
				break;
			case 4:
				tanLeather(player, TanData.SNAKESKIN_TEMPLE_TREKKING, getAmountByPacket(packetId));
				break;
			case 5:
				tanLeather(player, TanData.GREEN_DRAGONHIDE, getAmountByPacket(packetId));
				break;
			case 6:
				tanLeather(player, TanData.BLUE_DRAGONHIDE, getAmountByPacket(packetId));
				break;
			case 7:
				tanLeather(player, TanData.RED_DRAGONHIDE, getAmountByPacket(packetId));
				break;
			case 8:
				tanLeather(player, TanData.BLACK_DRAGONHIDE, getAmountByPacket(packetId));
				break;
			}
		}
	}

	private static int getAmountByPacket(int packetId) {
		switch (packetId) {
		case 216:
			return 1;
		case 19:
			return 5;
		case 193:
			return 10;
		case 76:
			return -1;
		default:
			return 27;
		}
	}

	public static void tanLeather(Player player, TanData leather, int amount) {
		if (getRealTanAmount(player, leather, amount) == 0) {
			player.sendMessage("You need some hides and cash to tan this.");
			return;
		}
		if (amount == -1) {
			player.getTemporaryAttributtes().put("TanningL", leather);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount of leather you'd like to tan: " });
			return;
		}
		int realAmount = getRealTanAmount(player, leather, amount);
		player.getInventory().deleteItem(leather.getLeather().getId(), realAmount);
		player.getInventory().deleteItem(new Item(995, leather.getCost() * realAmount));
		player.getInventory().addItem(leather.getTannedLeather().getId(), realAmount);
		player.sendMessage(
				"The tanner happily tans " + (realAmount < 11 ? Utils.getNumberInWords(realAmount) : realAmount)
						+ " of your " + leather.getLeather().getName().toLowerCase() + "s.");
		player.getInterfaceManager().closeScreenInterface();
	}

	private static int getRealTanAmount(Player player, TanData leather, int amount) {
		if (player.getInventory().getNumberOf(995) >= (amount * leather.getCost())
				&& player.getInventory().containsItem(leather.getLeather().getId(), amount))
			return amount;
		else if (player.getInventory().getNumberOf(995) <= (amount * leather.getCost())
				&& player.getInventory().containsItem(leather.getLeather().getId(), amount))
			return Math.round(player.getInventory().getNumberOf(995) / leather.getCost());
		else if (player.getInventory().getNumberOf(995) <= (amount * leather.getCost())
				&& !player.getInventory().containsItem(leather.getLeather().getId(), amount))
			if (Math.round(player.getInventory().getNumberOf(995) / leather.getCost()) < player.getInventory()
					.getNumberOf(leather.getLeather().getId()))
				return Math.round(player.getInventory().getNumberOf(995) / leather.getCost());
			else
				return player.getInventory().getNumberOf(leather.getLeather().getId());
		else
			return player.getInventory().getNumberOf(leather.getLeather().getId());
	}

}
