package com.rs.game.player;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.game.player.content.ItemConstants;
import com.rs.utils.EconomyPrices;
import com.rs.game.player.content.Lend;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.cache.loaders.ItemDefinitions;

import java.util.concurrent.CopyOnWriteArrayList;

public class Trade {

	private Player player, target;
	private ItemsContainer<Item> items;
	private boolean tradeModified;
	private boolean accepted;
	private Integer wealthValue = 0;
	private Item lendedItem;
	private int lendTime;
	public CopyOnWriteArrayList<Item> offeredItems = new CopyOnWriteArrayList<Item>();

	public Trade(Player player) {
		this.player = player;
		items = new ItemsContainer<Item>(28, false);
	}
	
	public Item getLendedItem() {
		return lendedItem;
	}

	public int getLendedTime() {
		return lendTime;
	}

	public void setLendedItem(Item item) {
		this.lendedItem = item;
	}

	public void setLendedTime(int lendTime) {
		this.lendTime = lendTime;
	}
	
	public void lendItem(int slot, int hours) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = player.getInventory().getItem(slot);
				if (item == null)
					return;
				if (item.getDefinitions().getLendId() == -1) {
					player.getPackets().sendGameMessage("You can't lend that!");
					return;
				}
				if (player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage(
							"You're too busy to lend an item.");
					return;
				}
				if (target.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage(
							"They are too busy to lend an item.");
					return;
				}
				if (LendingManager.hasLendedItem(target)) {
					player.getPackets().sendGameMessage(
							"Your target already has a lended item.");
					return;
				}
				setLendedItem(item);
				setLendedTime(hours);
				Item[] itemsBefore = items.getItemsCopy();
				player.getInventory().deleteItem(slot, item);
				refreshItems(itemsBefore);
				cancelAccepted();
			}
		}
	}

	public void openTrade(Player target) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				this.target = target;
				wealthValue = 0;
				player.getPackets().sendIComponentText(335, 15, "Trading With: " + target.getDisplayName());
				sendInterItems();
				sendOptions();
				sendTradeModified();
				refreshFreeInventorySlots();
				refreshStageMessage(true);
				player.getInterfaceManager().sendInterface(335);
				player.getInterfaceManager().sendInventoryInterface(336);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						closeTrade(CloseTradeStage.CANCEL);
					}
				});
			}
		}
	}

	public void removeItem(final int slot, int amount) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = items.get(slot);
				if (item == null)
					return;
				Item[] itemsBefore = items.getItemsCopy();
				int maxAmount = items.getNumberOf(item);
				if (amount < maxAmount)
					item = new Item(item.getId(), amount);
				else
					item = new Item(item.getId(), maxAmount);
				items.remove(slot, item);
				player.getTrade().wealthValue -= item.getDefinitions().getTipitPrice() * item.getAmount();
				player.getInventory().addItem(item);
				refreshItems(itemsBefore);
				cancelAccepted();
				setTradeModified(true);
			}
		}
	}

	private void sendFlash(int slot) {
		player.getPackets().sendInterFlashScript(335, 33, 4, 7, slot);
		target.getPackets().sendInterFlashScript(335, 36, 4, 7, slot);
	}

	private void cancelAccepted() {
		boolean canceled = false;
		if (accepted) {
			accepted = false;
			canceled = true;
		}
		if (target.getTrade().accepted) {
			target.getTrade().accepted = false;
			canceled = true;
		}
		if (canceled)
			refreshBothStageMessage(canceled);
	}

	public void addItem(int slot, int amount) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				Item item = player.getInventory().getItem(slot);
				if (item == null)
					return;
				if (!ItemConstants.isTradeable(item)) {
					player.getPackets().sendGameMessage("That item isn't tradeable.");
					return;
				}
				Item[] itemsBefore = items.getItemsCopy();
				int maxAmount = player.getInventory().getItems().getNumberOf(item);
				if (amount < maxAmount)
					item = new Item(item.getId(), amount);
				else
					item = new Item(item.getId(), maxAmount);
				items.add(item);
				player.getTrade().wealthValue += item.getDefinitions().getTipitPrice() * item.getAmount();
				player.getInventory().deleteItem(slot, item);
				refreshItems(itemsBefore);
				cancelAccepted();
			}
		}
	}

	private void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = items.getItems()[index];
			if (itemsBefore[index] != item) {
				if (itemsBefore[index] != null && (item == null || item.getId() != itemsBefore[index].getId()
						|| item.getAmount() < itemsBefore[index].getAmount()))
					sendFlash(index);
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		player.getPackets().sendIComponentText(335, 42,
				"Wealth transfer: "
						+ ((target.getTrade().wealthValue - player.getTrade().wealthValue) < 0 ? "<col=ff0000>" : "+")
						+ (target.getTrade().wealthValue - player.getTrade().wealthValue) + " gp");
		target.getPackets().sendIComponentText(335, 42,
				"Wealth transfer: "
						+ ((player.getTrade().wealthValue - target.getTrade().wealthValue) < 0 ? "<col=ff0000>" : "+")
						+ (player.getTrade().wealthValue - target.getTrade().wealthValue) + " gp");
		refresh(finalChangedSlots);
		refreshFreeInventorySlots();
	}

	private void sendOptions() {
		player.getPackets().sendInterSetItemsOptionsScript(335, 30, 90, 4, 7, "Remove", "Remove-5", "Remove-10",
				"Remove-All", "Remove-X", "Value");
		player.getPackets().sendIComponentSettings(335, 30, 0, 27, 1150);
		Object[] tparams3 = new Object[] { "", "", "", "", "", "", "", "", "Value<col=FF9040>", -1, 0, 7, 4, 90,
				21954593 };
		player.getPackets().sendRunScript(695, tparams3);
		player.getPackets().sendIComponentSettings(335, 33, 0, 27, 1026);
		player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4, 7, "Offer", "Offer-5", "Offer-10",
				"Offer-All", "Offer-X", "Value<col=FF9040>", "Lend");
		player.getPackets().sendIComponentSettings(335, 87, -1, -1, 1026);
		player.getPackets().sendIComponentSettings(335, 88, -1, -1, 1030);
		player.getPackets().sendIComponentSettings(335, 83, -1, -1, 1024);
		player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
		player.getPackets().sendHideIComponent(335, 74, false);
		player.getPackets().sendHideIComponent(335, 75, false);

	}

	public boolean isTrading() {
		return target != null;
	}

	public void setTradeModified(boolean modified) {
		if (modified == tradeModified)
			return;
		tradeModified = modified;
		sendTradeModified();
	}

	private void sendInterItems() {
		player.getPackets().sendItems(-1, 0, 90, items);
		target.getPackets().sendItems(-2, 0, 90, items);
	}
	private void refresh(int... slots) {
		player.getPackets().sendUpdateItems(-1, 0, 90, items, slots);
		target.getPackets().sendUpdateItems(-2, 0, 90, items, slots);
		if (target.getTrade().getLendedItem() != null) {
			player.getPackets().sendItemOnIComponent(335, 83,
					target.getTrade().getLendedItem().getId(), 1);
			player.getPackets().sendIComponentText(
					335,
					84,
					target.getTrade().getLendedTime() + " hour"
							+ (getLendedTime() > 1 ? "s" : ""));
			target.getPackets().sendItemOnIComponent(335, 87,
					target.getTrade().getLendedItem().getId(), 1);
			target.getPackets().sendIComponentText(
					335,
					88,
					target.getTrade().getLendedTime() + " hour"
							+ (getLendedTime() > 1 ? "s" : ""));
		}
		if (getLendedItem() != null) {
			target.getPackets().sendItemOnIComponent(335, 83,
					getLendedItem().getId(), 1);
			target.getPackets().sendIComponentText(
					335,
					84,
					getLendedTime() + " hour"
							+ (getLendedTime() > 1 ? "s" : ""));
			player.getPackets().sendItemOnIComponent(335, 87,
					getLendedItem().getId(), 1);
			player.getPackets().sendIComponentText(
					335,
					88,
					getLendedTime() + " hour"
							+ (getLendedTime() > 1 ? "s" : ""));
		}

	}

	public void accept(boolean firstStage) {
		synchronized (this) {
			if (!isTrading())
				return;
			synchronized (target.getTrade()) {
				if (target.getTrade().accepted) {
					if (firstStage) {
						if (nextStage())
							target.getTrade().nextStage();
					} else {
						player.setCloseInterfacesEvent(null);
						player.closeInterfaces();
						closeTrade(CloseTradeStage.DONE);
					}
					return;
				}
				accepted = true;
				refreshBothStageMessage(firstStage);
			}
		}
	}

	public void sendValue(int slot, boolean traders) {
		if (!isTrading())
			return;
		Item item = traders ? target.getTrade().items.get(slot) : items.get(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		int price = item.getDefinitions().getTipitPrice();
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": market price is " + price + " coins.");
	}

	public void sendValue(int slot) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage("That item isn't tradeable.");
			return;
		}
		int price = item.getDefinitions().getTipitPrice();
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": market price is " + price + " coins.");
	}

	public void sendExamine(int slot, boolean traders) {
		if (!isTrading())
			return;
		Item item = traders ? target.getTrade().items.get(slot) : items.get(slot);
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	private boolean nextStage() {
		if (!isTrading())
			return false;
		if (player.getInventory().getItems().getUsedSlots() + target.getTrade().items.getUsedSlots() > 28) {
			player.getPackets().sendGameMessage("You don't have enough space in your inventory for this trade.");
			target.getPackets()
					.sendGameMessage("Other player doesn't have enough space in their inventory for this trade.");
			return false;
		}
		accepted = false;
		player.getInterfaceManager().sendInterface(334);
		player.getInterfaceManager().closeInventoryInterface();
		writeOffers();
		target.getPackets().sendIComponentText(334, 45, "Trading with:<br>" + player.getDisplayName());
		player.getPackets().sendIComponentText(334, 45, "Trading with:<br>" + target.getDisplayName());
		refreshBothStageMessage(false);
		return true;
	}

	private void refreshBothStageMessage(boolean firstStage) {
		refreshStageMessage(firstStage);
		target.getTrade().refreshStageMessage(firstStage);
	}

	private void refreshStageMessage(boolean firstStage) {
		player.getPackets().sendIComponentText(firstStage ? 335 : 334, firstStage ? 37 : 33,
				getAcceptMessage(firstStage));
	}

	private String getAcceptMessage(boolean firstStage) {
		if (accepted)
			return "Waiting for other player...";
		if (target.getTrade().accepted)
			return "Other player has accepted.";
		return firstStage ? "" : "Are you sure you want to make this trade?";
	}

	private void sendTradeModified() {
		player.getPackets().sendConfig(1042, tradeModified ? 1 : 0);
		target.getPackets().sendConfig(1043, tradeModified ? 1 : 0);
	}

	private void refreshFreeInventorySlots() {
		int freeSlots = player.getInventory().getFreeSlots();
		target.getPackets().sendIComponentText(335, 21, player.getDisplayName() + " has "
				+ (freeSlots == 0 ? "no" : freeSlots) + " free" + "<br>inventory slots");
	}

	private void writeOffers() {
		ItemsContainer<Item> offer = items;
		ItemsContainer<Item> other = offer;
		player.getPackets().sendHideIComponent(334, 37, false);
		player.getPackets().sendHideIComponent(334, 41, false);
		player.getPackets().sendHideIComponent(334, 36, false);
		target.getPackets().sendHideIComponent(334, 37, false);
		target.getPackets().sendHideIComponent(334, 41, false);
		target.getPackets().sendHideIComponent(334, 36, false);
		player.getPackets().sendIComponentText(334, 45, "Trading with: <br>" + target.getDisplayName());
		target.getPackets().sendIComponentText(334, 41, buildString(other));
		player.getPackets().sendIComponentText(334, 37, buildString(offer));

	}

	public String buildString(ItemsContainer<Item> offer) {
		String a = "";
		if (offer.getUsedSlots() > 0) {
			for (int i = 0; i < offer.getSize(); i++) {
				if (offer.get(i) == null)
					continue;

				a = a + "<col=FF9040>" + offer.get(i).getDefinitions().getName();
				if (offer.get(i).getAmount() > 1) {
					a = a + "<col=FFFFFF> x ";
					a = a + "<col=FFFFFF>" + Integer.toString(offer.get(i).getAmount());
				} else {
					a = a + "<br>";
				}
			}
		} else {
			a = "<col=FFFFFF>Absolutely nothing!";
		}
		return a;
	}

	private static enum CloseTradeStage {
		CANCEL, NO_SPACE, DONE
	}

	private void closeTrade(CloseTradeStage stage) {
		synchronized (this) {
			synchronized (target.getTrade()) {
				Player oldTarget = target;
				target = null;
				Item lendItem = getLendedItem();
				int time = getLendedTime();
				tradeModified = false;
				accepted = false;
				if (CloseTradeStage.DONE != stage) {
					player.getInventory().getItems().addAll(items);
					if (lendItem != null) {
						player.getInventory().addItem(lendItem);
					}
					player.getInventory().init();
					items.clear();
				} else {
					player.getPackets().sendGameMessage("Accepted trade.");
					Logger.printTradeLog(player, oldTarget, oldTarget.getTrade().items.toArray());
					player.getInventory().getItems().addAll(oldTarget.getTrade().items);
					player.getInventory().init();
					oldTarget.getTrade().items.clear();
					if (lendItem != null) {
						Lend lend = new Lend(player.getUsername(),
								oldTarget.getUsername(), lendItem,
								Utils.currentTimeMillis()
										+ (time * 60 * 60 * 1000));
						LendingManager.lend(lend);
						oldTarget.getInventory().addItem(
								lendItem.getDefinitions().getLendId(), 1);
					}
					player.getTrade().setLendedItem(null);
				}
				if (oldTarget.getTrade().isTrading()) {
					oldTarget.setCloseInterfacesEvent(null);
					oldTarget.closeInterfaces();
					oldTarget.getTrade().closeTrade(stage);
					player.getTrade().setLendedItem(null);
					oldTarget.getTrade().setLendedItem(null);
					if (CloseTradeStage.CANCEL == stage) {
						player.getTrade().setLendedItem(null);
						oldTarget.getTrade().setLendedItem(null);
						oldTarget.getPackets().sendGameMessage("Other player declined the trade!");
					} else if (CloseTradeStage.NO_SPACE == stage) {
						player.getPackets()
								.sendGameMessage("You don't have enough space in your inventory for this trade.");
						oldTarget.getPackets().sendGameMessage(
								"Other player doesn't have enough space in their inventory for this trade.");
					}
				}
			}
		}
	}

}
