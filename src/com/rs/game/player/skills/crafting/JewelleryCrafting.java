package com.rs.game.player.skills.crafting;

import com.rs.game.Animation;
import com.rs.game.player.actions.Action;
import com.rs.game.player.Player;
import com.rs.game.item.Item;
import com.rs.game.player.Skills;
import com.rs.game.Graphics;

public class JewelleryCrafting {

	public static final int INTERFACE = 675;
	private static final int[] MOLDS = { 1592, 1597, 1595, 11065 };
	private static final int[] GEMS = { 2357, 1607, 1605, 1603, 1601, 1615, 6573 };
	private static final int[][] ITEMS = { { 1635, 1637, 1639, 1641, 1643, 1645, 6575 },
			{ 1654, 1656, 1658, 1660, 1662, 1664, 6577 }, { 1673, 1675, 1677, 1679, 1681, 1683, 6579 },
			{ 11069, 11072, 11076, 11085, 11092, 11115, 11130 } };
	private static final int[] COMPONENTS_BASE = { 20, 39, 58, 77 };
	private static final double[][] EXPERIENCE = { { 15, 40, 55, 70, 85, 100, 115 }, { 20, 55, 60, 75, 90, 105, 120 },
			{ 25, 60, 65, 80, 95, 110, 125 }, { 30, 65, 70, 85, 100, 150, 165 } };
	private static final byte[][] LEVEL = { { 5, 20, 27, 34, 43, 55, 67 }, { 6, 22, 29, 40, 56, 72, 82 },
			{ 7, 23, 30, 42, 58, 74, 84 }, { 8, 24, 31, 50, 70, 80, 90 } };
	private static final int[] ONYX = { 6575, 6577, 6579, 11130 };

	private static boolean fam;

	public static void openInterface(Player player, boolean familiar) {
		fam = familiar;
		player.getInterfaceManager().sendInterface(INTERFACE);
		callCS2(player);
		for (int primaryIndex = 0; primaryIndex < MOLDS.length; primaryIndex++) {
			player.getPackets().sendIComponentText(INTERFACE, 16 + (primaryIndex * 19), "");
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				player.getPackets().sendItems(-1, 0, (299 + (primaryIndex * 14) + secondaryIndex),
						new Item[] { new Item(ITEMS[primaryIndex][secondaryIndex]) });
			}
		}
	}

	private static void callCS2(Player player) {
		for (int primaryIndex = 0; primaryIndex < COMPONENTS_BASE.length; primaryIndex++) {
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				player.getPackets().sendInterSetItemsOptionsScript(INTERFACE,
						(COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2),
						(299 + (primaryIndex * 14) + secondaryIndex), false, 6, 4, "Make 1", "Make 5", "Make 10",
						"Make All");
				player.getPackets().sendUnlockIComponentOptionSlots(INTERFACE,
						(COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2), 0, 28, 0, 1, 2, 3);
			}
		}
	}

	public static void handleButtonClick(Player player, int componentId, final int tick) {
		for (int primaryIndex = 0; primaryIndex < COMPONENTS_BASE.length; primaryIndex++) {
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				if (componentId == (COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2)) {
					final int actionPrimaryIndex = primaryIndex, actionSecondaryIndex = secondaryIndex;
					player.closeInterfaces();
					player.getActionManager().setAction(new Action() {

						int ticks;

						@Override
						public boolean start(Player player) {
							this.ticks = tick;
							if (actionSecondaryIndex != 0) {
								Item product = new Item(actionSecondaryIndex == 6 ? ONYX[actionPrimaryIndex]
										: ITEMS[actionPrimaryIndex][actionSecondaryIndex]);
								if (product.getName().contains("ring")
										&& !player.getInventory().containsItem(1592, 1)) {
									player.sendMessage("You need a ring mould to do this.");
									return false;
								} else if (product.getName().contains("necklace")
										&& !player.getInventory().containsItem(1597, 1)) {
									player.sendMessage("You need a necklace mould to do this.");
									return false;
								} else if (product.getName().contains("bracelet")
										&& !player.getInventory().containsItem(11065, 1)) {
									player.sendMessage("You need a bracelet mould to do this.");
									return false;
								} else if (product.getName().contains("amulet")
										&& !player.getInventory().containsItem(1595, 1)) {
									player.sendMessage("You need an amulet mould to do this.");
									return false;
								}
							}
							return process(player);
						}

						@Override
						public boolean process(Player player) {
							if (ticks <= 0)
								return false;
							int level = LEVEL[actionPrimaryIndex][actionSecondaryIndex];
							if (player.getSkills().getLevel(Skills.CRAFTING) < level) {
								player.getPackets().sendGameMessage("You need a Crafting level of " + level + ".");
								return false;
							} else if (!player.getInventory().containsItem(2357, 1)) {
								player.getPackets()
										.sendGameMessage("You need a gold bar in order to create jewellery.");
								return false;
							} else if (actionSecondaryIndex != 0
									&& !player.getInventory().containsItem(GEMS[actionSecondaryIndex], 1)) {
								player.getPackets().sendGameMessage(
										"You are missing required the required items in order to create this type of jewellery.");
								return false;
							}
							return true;
						}

						@Override
						public int processWithDelay(Player player) {
							ticks--;
							if (!fam)
								player.setNextAnimation(new Animation(3243));
							else {
								player.setNextGraphics(new Graphics(1316));
								player.setNextAnimation(new Animation(7660));
							}
							player.getInventory().deleteItem(2357, 1);
							player.getInventory().deleteItem(GEMS[actionSecondaryIndex], 1);
							player.getInventory().addItem(actionSecondaryIndex == 6 ? ONYX[actionPrimaryIndex]
									: ITEMS[actionPrimaryIndex][actionSecondaryIndex], 1);
							player.getSkills().addXp(Skills.CRAFTING,
									EXPERIENCE[actionPrimaryIndex][actionSecondaryIndex]);
							if (fam)
								return -1;// stops
							return 2;
						}

						@Override
						public void stop(Player player) {
							setActionDelay(player, 3);
						}
					});
				}
			}
		}
	}
}