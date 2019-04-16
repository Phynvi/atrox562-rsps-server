package com.rs.game.player.skills.smithing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.rs.game.Animation;
import com.rs.game.player.actions.Action;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.content.skills.SmeltingInterface;
import com.rs.game.player.skills.crafting.JewelleryCrafting;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.cores.CoresManager;

public class Smelting extends Action {

	/**
	 * @author Kris Handles smelting all possible bars outside of quests.
	 */

	public enum Bars {
		BRONZE_BAR(new Item(2349, 1), new Item(438, 1), new Item(436, 1), 1, 6.2), BLURITE_BAR(new Item(9467, 1),
				new Item(668, 1), null, 8, 8), IRON_BAR(new Item(2351, 1), new Item(440, 1), null, 15, 12.5), SILVER_BAR(new Item(2355, 1),
								new Item(442, 1), null, 20, 13.7),  STEEL_BAR(
						new Item(2353, 1), new Item(440, 1), new Item(453, 2), 30, 17.5), GOLD_BAR(new Item(2357, 1), new Item(444, 1), null,
										40, 22.5), MITHRIL_BAR(new Item(2359, 1), new Item(447, 1), new Item(453, 4),
												50, 30), ADAMANTITE_BAR(new Item(2361, 1), new Item(449, 1),
														new Item(453, 6), 70, 37.5), RUNITE_BAR(new Item(2363, 1),
																new Item(451, 1), new Item(453, 8), 85, 50),
																CANNONBALL(new Item(2, 4), new Item(2353, 1), null, 15, 25.6);

		private Item bar, primaryOre, secondaryOre;
		private int level;
		private double experience;

		private static final HashMap<Integer, Bars> BARS = new HashMap<Integer, Bars>();

		static {
			for (Bars b : values()) {
				BARS.put(b.ordinal(), b);
			}
		}

		public static Bars getBar(int id) {
			return BARS.get(id);
		}

		private Bars(Item bar, Item primaryOre, Item secondaryOre, int level, double experience) {
			this.bar = bar;
			this.primaryOre = primaryOre;
			this.secondaryOre = secondaryOre;
			this.level = level;
			this.experience = experience;
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

	private Bars bars;
	private int amount;

	public Smelting(Bars bars, int amount) {
		this.bars = bars;
		this.amount = amount;
	}

	public static int getAmountBasedOnComponent(Player player, int baseComponent, int componentId, Bars bar) {
		switch (baseComponent - componentId) {
		case 0:
			return 1;
		case 1:
			return 5;
		case 2:
			return 10;
		case 3:
			player.getTemporaryAttributtes().put("SmeltingD", bar);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount of bars you'd like to smelt: " });
			return 0;
		default:
			return 0;
		}
	}
	//Used for cannonballs
	public static int getAmountBasedOnComponent(Player player, int componentId, Bars bar) {
		switch (componentId) {
		case 39:
			return 1;
		case 33:
			return 5;
		case 27:
			return 10;
		case 21:
			player.getTemporaryAttributtes().put("SmeltingD", bar);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount of bars you'd like to smelt: " });
			return 0;
		default:
			return 0;
		}
	}

	public static boolean handleObjects(Player player, WorldObject object) {
		switch (object.getId()) {
		case 11666:
		case 36956:
		case 26814:
			if (player.getInventory().containsItem(2357, 1) && (player.getInventory().containsItem(1592, 1)
					|| player.getInventory().containsItem(1595, 1) || player.getInventory().containsItem(1597, 1)
					|| player.getInventory().containsItem(11065, 1))) {
				JewelleryCrafting.openInterface(player, false);
				return true;
			}
			if (player.getInterfaceManager().containsChatBoxInter())
				player.getInterfaceManager().closeChatBoxInterface();
			for (int i = 4; i < 13; i++)
				player.getPackets().sendItemOnIComponent(311, i, Bars.getBar(i - 4).getBar().getId(), 130);
			for (int i = 0; i < 9; i++)
				player.getPackets().sendIComponentText(311, 16 + (i * 4), "<br><br><br><br>"
						+ (player.getSkills().getLevel(Skills.SMITHING) >= Bars.getBar(i).getLevel() ? "<col=000000>"
								: "<col=ff0000>")
						+ Bars.getBar(i).getBar().getName().replace(" bar", "").replace("Adamantite", "Adamant"));
			player.getInterfaceManager().sendChatBoxInterface(311);
			return true;
		default:
			return false;
		}
	}

	public static void handleInterface(Player player, int interfaceId, int cId) {
		if (interfaceId == 311) {
			Bars bar = Bars.getBar(Math.round((cId - 13) / 4));
			player.getInterfaceManager().closeChatBoxInterface();
			player.getActionManager().setAction(
					new Smelting(bar, getAmountBasedOnComponent(player, (bar.ordinal() * 4 + 16), cId, bar)));
			return;
		}
	}

	@Override
	public boolean start(Player player) {
		if (amount <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.SMITHING) < bars.getLevel()) {
			player.sendMessage("You need at least level " + bars.getLevel() + " Smithing to smelt a bar of "
					+ bars.getBar().getName().toLowerCase().replace(" bar", "."));
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (amount <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.SMITHING) < bars.getLevel()) {
			player.sendMessage("Your Smithing level isn't high enough to smelt "
					+ (Utils.startsWithVowel(bars.getBar().getName()) ? "an " : "a ")
					+ bars.getBar().getName().toLowerCase() + ".");
			return false;
		}
		if (bars.getSecondaryOre() != null && !player.getInventory().containsItem(bars.getPrimaryOre())
				&& !player.getInventory().containsItem(bars.getSecondaryOre())) {
			/**
			 * Had to define adamantite bar seperatedly due to the length of the
			 * text exceeding maximum possible characters.
			 */
			if (bars != Bars.ADAMANTITE_BAR)
				player.sendMessage("You need at least one " + bars.getPrimaryOre().getName().toLowerCase() + " and "
						+ (bars.getSecondaryOre().getAmount() > 1 ? "at least " : "")
						+ Utils.getNumberInWords(bars.getSecondaryOre().getAmount()) + " "
						+ bars.getSecondaryOre().getName().toLowerCase() + " to smelt a bar of "
						+ bars.getBar().getName().toLowerCase().replace(" bar", "."));
			else {
				player.sendMessage("You need at least one " + bars.getPrimaryOre().getName().toLowerCase() + " and "
						+ (bars.getSecondaryOre().getAmount() > 1 ? "at least " : "")
						+ Utils.getNumberInWords(bars.getSecondaryOre().getAmount()) + " "
						+ bars.getSecondaryOre().getName().toLowerCase() + " to smelt a bar of");
				player.sendMessage(bars.getBar().getName().toLowerCase().replace(" bar", ".") + "");
			}
			return false;
		}
		if (!player.getInventory().containsItem(bars.getPrimaryOre())
				|| bars.getSecondaryOre() != null && !player.getInventory().containsItem(bars.getSecondaryOre())) {
			player.sendMessage("You need at least "
					+ (!player.getInventory().containsItem(bars.getPrimaryOre())
							? Utils.getNumberInWords(bars.getPrimaryOre().getAmount())
							: Utils.getNumberInWords(bars.getSecondaryOre().getAmount()))
					+ " "
					+ (!player.getInventory().containsItem(bars.getPrimaryOre())
							? bars.getPrimaryOre().getName().toLowerCase()
							: bars.getSecondaryOre().getName().toLowerCase())
					+ " to smelt a bar of " + bars.getBar().getName().toLowerCase().replace(" bar", "."));
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		if (Bars.CANNONBALL == bars) {
		player.sendMessage("You attempt to make some cannonballs.");
		} else {
		player.sendMessage("You attempt to smelt a bar of " + bars.getBar().getName().toLowerCase().replace(" bar", ".."));
		}
		player.getInventory().deleteItem(bars.getPrimaryOre());
		if (bars.getSecondaryOre() != null)
			player.getInventory().deleteItem(bars.getSecondaryOre());
		player.setNextAnimation(new Animation(899));
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if (Bars.IRON_BAR == bars) {
					int level = player.getSkills().getLevel(Skills.SMITHING);
					int boost = level >= 45 ? 30 : (level - 15);
					boolean success = Utils.getRandom(100) < (50 + boost);
					if (player.getEquipment().getRingId() == 2568)
						success = true;
					if (success) {
						if (Bars.CANNONBALL == bars) {
						player.sendMessage("You retrieve some cannonballs.");
						} else {
						player.sendMessage("You retrieve a bar of " + bars.getBar().getName().toLowerCase().replace(" bar", "."));
						}
						player.getInventory().addItem(bars.getBar());
						player.getSkills().addXp(Skills.SMITHING, bars.getExperience());
					} else
						player.sendMessage("The ore is too impure and you fail to refine it.");
				} else {
					if (Bars.CANNONBALL == bars) {
						player.sendMessage("You retrieve some cannonballs.");
						} else {
						player.sendMessage("You retrieve a bar of " + bars.getBar().getName().toLowerCase().replace(" bar", "."));
						}
					//player.sendMessage("You retrieve a bar of " + bars.getBar().getName().toLowerCase().replace(" bar", "."));
					player.getInventory().addItem(bars.getBar());
					player.getSkills().addXp(Skills.SMITHING, bars.getExperience());
				}
			}
		}, 2000, TimeUnit.MILLISECONDS);
		return 4;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 2);
	}

}