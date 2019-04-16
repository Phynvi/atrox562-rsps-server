package com.rs.game.player;

import java.io.Serializable;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.utils.ItemExamines;
import com.rs.game.player.skills.hunter.BoxTrapping;
import com.rs.utils.Utils;

public final class Inventory implements Serializable {

	private static final long serialVersionUID = 8842800123753277093L;

		public static final int INVENTORY_INTERFACE = 149;//679
		
	
	private ItemsContainer<Item> items;
	
	private transient Player player;
	
	public Inventory() {
		items = new ItemsContainer<Item>(28, false);
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void init() {
		player.getPackets().sendItems(149, 0, 93, items);
	}
	
	public void reset() {
		items.reset();
		init(); //as all slots reseted better just send all again
	}
	
	public void replaceItem(int id, int amount, int slot) {
		Item item = items.get(slot);
		if (item == null)
			return;
		item.setId(id);
		item.setAmount(amount);
		refresh(slot);
	}
	
	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(149, 0, 93, items, slots);
	}
	
	public int getAmountOf(int itemId) {
	return items.getNumberOf(itemId);
    }
	
	public boolean addItem(int itemId, int amount, int charges) {
		if(itemId < 0 || amount < 0 || itemId >= Utils.getItemDefinitionsSize())
			return false;
		Item[] itemsBefore = items.getItemsCopy();
		if(!items.add(new Item(itemId, amount, charges))) {
			items.add(new Item(itemId, items.getFreeSlots(), charges));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}
	
	public void addItem(int itemId, int amount, boolean dropIfInvFull) {
		if (itemId < 0 || amount < 0 || !player.getControlerManager().canAddInventoryItem(itemId, amount))
			return;
		final Item[] itemsBefore = items.getItemsCopy();
		int numberToDrop;
		if (!items.add(new Item(itemId, amount))) {
			numberToDrop = amount - items.getFreeSlots();
			items.add(new Item(itemId, items.getFreeSlots()));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			World.addGroundItem(new Item(itemId, numberToDrop), player, player, false, 60, true);
			refreshItems(itemsBefore);
			return;
		}
	}
	public boolean addItem(int itemId, int amount) {
		if(itemId < 0 || amount < 0 || itemId >= Utils.getItemDefinitionsSize())
			return false;
		Item[] itemsBefore = items.getItemsCopy();
		if(!items.add(new Item(itemId, amount))) {
			items.add(new Item(itemId, items.getFreeSlots()));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}/*
	
	public void deleteItem(int slot, Item item) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
	}
	
	public void deleteItem(int itemId, int ammount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, ammount));
		refreshItems(itemsBefore);
	}*/
	
	/*
	 * No refresh needed its client to who does it :p
	 */
	public void switchItem(int fromSlot, int toSlot) {
		Item fromItem = items.get(fromSlot);
		Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		refresh(fromSlot, toSlot);
		BoxTrapping.handleItemSwitching(player, fromSlot, toSlot);
	}
	
	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for(int index = 0; index < itemsBefore.length; index++) {
			if(itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}
	
	public ItemsContainer<Item> getItems() {
		return items;
	}
	
	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}
	
	public int getFreeSlots() {
		return items.getFreeSlots();
	}
	
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	public int getNumerOf(int itemId) {
		return items.getNumberOf(itemId);
	}
	
	public int getItemsContainerSize() {
		return items.getSize();
	}
	
	public boolean containsItem(int itemId, int ammount) {
		return items.contains(new Item(itemId, ammount));
	}
	public boolean containsItem(Item item) {
    	return items.contains(item);
    }
	public boolean containsOneItem(int... itemIds) {
		for(int itemId : itemIds) {
			if(items.containsOne(new Item(itemId, 1)))
				return true;
		}
		return false;
	}

	public boolean addItem(Item item) {
		if (item.getId() < 0
				|| item.getAmount() < 0
				|| item.getId() >= Utils.getItemDefinitionsSize())
			return false;
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(item)) {
			items.add(new Item(item.getId(), items.getFreeSlots()));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}
	public void deleteItem(int slot, Item item) {
		if (!player.getControlerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
	}

	public boolean removeItems(Item... list) {
		for (Item item : list)  {
			if(item == null)
				continue;
				deleteItem(item);
		}
		return true;
	}

	public void deleteItem(int itemId, int amount) {
		if (!player.getControlerManager().canDeleteInventoryItem(itemId, amount))
			return;
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void deleteItem(Item item) {
		if (!player.getControlerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(item);
		refreshItems(itemsBefore);
	}

	public Item lookup(int id) {
		return items.lookup(id);
	}
	
	public int lookupSlot(int id) {
		try {
		return items.lookupSlot(id);
		} catch(Exception e) {
		return 0;
		}
	}

    public void sendExamine(int slotId) {
        if (slotId >= getItemsContainerSize())
                return;
        Item item = items.get(slotId);
        if (item == null)
                return;
		if (item.getDefinitions().getValue() <= 1) {
			player.getPackets().sendGameMessage("If you think the item " + item.getDefinitions().getName()
					+ " isn't priced right, please report it!");
		}
        player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
}

    public int numberOf(int id) {
		return items.getNumberOf(new Item(id, 1));
	}
	
	public int getNumberOf(int itemId) {
		return items.getNumberOf(itemId);
	}

	public Item get(int slot) {
		return items.get(slot);
	}

	public void unlockInventoryOptions() {
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0,
				27, 4554126);
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 28,
				55, 2097152);
	}

	public int getCoinsAmount() {
		int coins = items.getNumberOf(995);
		return coins < 0 ? Integer.MAX_VALUE : coins;
	}

	public boolean containsItems(Item[] item) {
		for (int i = 0; i < item.length; i++)
			if (!items.contains(item[i]))
				return false;
		return true;
	}

	public boolean addItemDrop(final int itemId, final int amount,
			final WorldTile tile) {
		if (itemId < 0
				|| amount < 0
				//|| !Utils.itemExists(itemId)
				|| !player.getControlerManager().canAddInventoryItem(itemId,
						amount))
			return false;
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(new Item(itemId, amount)))
			World.updateGroundItem(new Item(itemId, amount), tile, player, 60,
					0);
		else
			refreshItems(itemsBefore);
		return true;
	}

	public boolean addItemDrop(int itemId, int amount) {
		return addItemDrop(itemId, amount, new WorldTile(player));
	}

	
}
