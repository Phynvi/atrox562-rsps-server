package com.rs.game.player;

import java.io.Serializable;
import java.util.HashMap;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.grandexchange.Offer;
import com.rs.game.player.content.grandexchange.OfferHistory;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.ItemExamines;
import com.rs.utils.Utils;

public class GrandExchangeManager implements Serializable {

    private static final long serialVersionUID = -866326987352331696L;

    private transient Player player;

    private long[] offerUIds;
    private OfferHistory[] history;

    public GrandExchangeManager() {
	offerUIds = new long[6];
	history = new OfferHistory[5];
    }

    public void setPlayer(Player player) {
	this.player = player;
    }

    public void init() {
	GrandExchange.linkOffers(player);
    }

    public void stop() {
	GrandExchange.unlinkOffers(player);
    }

    public long[] getOfferUIds() {
	return offerUIds;
    }

    public boolean isSlotFree(int slot) {
	return offerUIds[slot] == 0;
    }

    public void addOfferHistory(OfferHistory o) {
	OfferHistory[] dest = new OfferHistory[history.length];
	dest[0] = o;
	System.arraycopy(history, 0, dest, 1, history.length - 1);
	history = dest;
    }

    public void openHistory() {
	player.getInterfaceManager().sendInterface(643);
	for (int i = 0; i < history.length; i++) {
	    OfferHistory o = history[i];
	    player.getPackets().sendIComponentText(643, 25 + i, o == null ? "" : o.isBought() ? "You bought" : "You sold");
	    player.getPackets().sendIComponentText(643, 35 + i, o == null ? "" : ItemDefinitions.getItemDefinitions(o.getId()).getName());
	    player.getPackets().sendIComponentText(643, 30 + i, o == null ? "" : Utils.getFormattedNumber(o.getQuantity()));
	    player.getPackets().sendIComponentText(643, 40 + i, o == null ? "" : Utils.getFormattedNumber(o.getPrice()));
	}
    }
	
    public void openGrandExchange() {
	player.getInterfaceManager().sendInterface(105);
	player.getPackets().sendConfig2(563, 4194304);
	player.getPackets().sendConfig(1112, -1);	
	player.getPackets().sendConfig(1113, -1);
	player.getPackets().sendConfig(1109, -1);
	player.getPackets().sendConfig(1110, 0);
	player.getPackets().sendUnlockIComponentOptionSlots(105, 209, -1, 0, 0, 1);
	player.getPackets().sendUnlockIComponentOptionSlots(105, 211, -1, 0, 0, 1);
	cancelOffer();
	player.getPackets().sendHideIComponent(105, 193, true);
	hideMemberSlots();
	/*player.setCloseInterfacesEvent(new Runnable() {
	    @Override
	    public void run() {
		if (getType() == 0)
		    player.getPackets().sendRunScript(571);
		player.getInterfaceManager().removeInterface(752, 7);
	    }
	});*/
    }
    
	/**
	 * Hides the member slots.
	 */
	public void hideMemberSlots() {
		player.getPackets().sendHideIComponent(105, 98, true);
		player.getPackets().sendHideIComponent(105, 99, true);
		player.getPackets().sendHideIComponent(105, 100, true);
		player.getPackets().sendHideIComponent(105, 101, true);
		player.getPackets().sendHideIComponent(105, 105, true);
		player.getPackets().sendHideIComponent(105, 104, false);
		player.getPackets().sendHideIComponent(105, 117, true);
		player.getPackets().sendHideIComponent(105, 118, true);
		player.getPackets().sendHideIComponent(105, 119, true);
		player.getPackets().sendHideIComponent(105, 120, true);
		player.getPackets().sendHideIComponent(105, 124, true);
		player.getPackets().sendHideIComponent(105, 123, false);
	}
	/*
	 * Hidden child:20
Hidden child:36
Hidden child:52
Hidden child:67
Hidden child:71
Hidden child:86
Hidden child:90
Hidden child:105
Hidden child:109
Hidden child:124
Hidden child:127
Hidden child:154
Hidden child:188
Hidden child:193
Hidden child:197
	 */
    public void openCollectionBox() {
	player.getInterfaceManager().sendInterface(109);
	player.getPackets().sendUnlockIComponentOptionSlots(109, 18, 0, 2, 0, 1);
	player.getPackets().sendUnlockIComponentOptionSlots(109, 23, 0, 2, 0, 1);
	player.getPackets().sendUnlockIComponentOptionSlots(109, 28, 0, 2, 0, 1);
	player.getPackets().sendUnlockIComponentOptionSlots(109, 36, 0, 2, 0, 1);
	player.getPackets().sendUnlockIComponentOptionSlots(109, 44, 0, 2, 0, 1);
	player.getPackets().sendUnlockIComponentOptionSlots(109, 52, 0, 2, 0, 1);
    }

    public void setSlot(int slot) {
    	player.getPackets().sendConfig(1112, slot);
    }

    public void setMarketPrice(int price) {
    	player.getPackets().sendConfig(1114, price);
    }

    public void setPricePerItem(int price) {
    	player.getPackets().sendConfig(1111, price);
    }

    public int getPricePerItem() {
	return player.getVarBitManager().getValue(1111);
    }

    public int getCurrentSlot() {
	return player.getVarBitManager().getValue(1112);
    }

    public void setItemId(int id) {
    	player.getPackets().sendConfig(1109, id);
    }

    public int getItemId() {
	return player.getVarBitManager().getValue(1109);
    }

    public void setAmount(int amount) {
    	player.getPackets().sendConfig(1110, amount);
    }

    public int getAmount() {
	return player.getVarBitManager().getValue(1110);
    }

    public void setType(int amount) {
    	player.getPackets().sendConfig(1113, amount);
    }

    public int getType() {
	return player.getVarBitManager().getValue(1113);
    }

    public void handleButtons(int interfaceId, int componentId, int slotId, int packetId) {
	if (interfaceId == 105) {
	    switch (componentId) {
		case 19:
		    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			viewOffer(0);
		    else
			abortOffer(0);
		    break;
		case 35:
		    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			viewOffer(1);
		    else
			abortOffer(1);
		    break;
		case 51:
		    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			viewOffer(2);
		    else
			abortOffer(2);
		    break;
		case 70:
		    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			viewOffer(3);
		    else
			abortOffer(3);
		    break;
		case 89:
		    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			viewOffer(4);
		    else
			abortOffer(4);
		    break;
		case 108:
		    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			viewOffer(5);
		    else
			abortOffer(5);
		    break;
		case 200:
		    abortCurrentOffer();
		    break;
		case 30:
		    makeOffer(0, false);
		    break;
		case 31:
		    makeOffer(0, true);
		    break;
		case 46:
		    makeOffer(1, false);
		    break;
		case 47:
		    makeOffer(1, true);
		    break;
		case 62:
		    makeOffer(2, false);
		    break;
		case 63:
		    makeOffer(2, true);
		    break;
		case 81:
		    makeOffer(3, false);
		    break;
		case 82:
		    makeOffer(3, true);
		    break;
		case 100:
		    makeOffer(4, false);
		    break;
		case 101:
		    makeOffer(4, true);
		    break;
		case 119:
		    makeOffer(5, false);
		    break;
		case 120:
		    makeOffer(5, true);
		    break;
		case 128:
		    cancelOffer();
		    break;
		case 155:
		    modifyAmount(getAmount() - 1);
		    break;
		case 157:
		    modifyAmount(getAmount() + 1);
		    break;
		case 160:
		    modifyAmount(getAmount() + 1);
		    break;
		case 162:
		    modifyAmount(getAmount() + 10);
		    break;
		case 164:
		    modifyAmount(getAmount() + 100);
		    break;
		case 166:
		    modifyAmount(getType() == 0 ? getAmount() + 1000 : getItemAmount(new Item(getItemId())));
		    break;
		case 168:
		    editAmount();
		    break;
		case 169:
		    modifyPricePerItem(getPricePerItem() - 1);
		    break;
		case 171:
		    modifyPricePerItem(getPricePerItem() + 1);
		    break;
		case 175:
		    modifyPricePerItem(GrandExchange.getPrice(getItemId()));
		    break;
		case 177:
		    editPrice();
		    break;
		case 179:
		    modifyPricePerItem((int) (Math.ceil(getPricePerItem() * 1.05)));
		    break;
		case 181:
		    modifyPricePerItem((int) (getPricePerItem() * 0.95));
		    break;
		case 186:
		    confirmOffer();
		    break;
		case 190:
		    chooseItem();
		    break;
		case 206:
		    collectItems(getCurrentSlot(), 0, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
		case 208:
		    collectItems(getCurrentSlot(), 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
	    }
	} else if (interfaceId == 107 && componentId == 18)
	    offer(slotId);
	else if (interfaceId == 449 && componentId == 1)
	    player.getInterfaceManager().closeInventoryInterface();
	else if (interfaceId == 109) {
	    switch (componentId) {
		case 19:
		    collectItems(0, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
		case 23:
		    collectItems(1, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
		case 27:
		    collectItems(2, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
		case 32:
		    collectItems(3, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
		case 37:
		    collectItems(4, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
		case 42:
		    collectItems(5, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
		    break;
	    }
	}
    }

    /*
     * public static final int ITEMID_CONFIG = 1109, AMOUNT_CONFIG = 1110,
     * PRICE_PER_CONFIG = 1111, SLOT_CONFIG = 1112, TYPE_CONFIG = 1113;
     */

    public void cancelOffer() {
	/*setItemId(-1);
	setAmount(0);
	setPricePerItem(1);
	setMarketPrice(0);
	setSlot(-1);*/
	player.getPackets().sendConfig(1109, -1);
	player.getPackets().sendConfig(1110, 0);	
	player.getPackets().sendConfig(1111, 0);
	player.getPackets().sendConfig(1112, -1);	
	player.getPackets().sendConfig(1113, 0);
	//player.removeTemporaryAttribute("box");
	//player.removeTemporaryAttribute("geItem");
	//player.removeTemporaryAttribute("price");
	//player.removeTemporaryAttribute("geAmount");	
	/*if (getType() == 0)
	    player.getPackets().sendRunScript(571);
	player.getInterfaceManager().closeInventoryInterface();
	player.getInterfaceManager().removeInterface(752, 7);
	setType(-1);*/
    }

    public void editAmount() {
	if (getType() == -1)
	    return;
	player.getTemporaryAttributtes().put("GEQUANTITYSET", Boolean.TRUE);
	player.getPackets().sendInputIntegerScript(true, "Enter the quantity you wish to " + (getType() == 0 ? "purchase" : "sell") + ":");
    }

    public void editPrice() {
	if (getType() == -1)
	    return;
	if (getItemId() == -1) {
	    player.getPackets().sendGameMessage("You must choose an item first.");
	    return;
	}
	player.getTemporaryAttributtes().put("GEPRICESET", Boolean.TRUE);
	player.getPackets().sendInputIntegerScript(true, "Enter the price you wish to to " + (getType() == 0 ? "buy" : "sell") + " for:");
    }

    public void modifyPricePerItem(int value) {
	if (getType() == -1)
	    return;
	if (getItemId() == -1) {
	    player.getPackets().sendGameMessage("You must choose an item first.");
	    return;
	}
	if (value < 1)
	    value = 1;
	setPricePerItem(value);
	player.getPackets().sendSound(4045, 0);
    }

    public void modifyAmount(int value) {
	if (getType() == -1)
	    return;
	if (value < 0)
	    value = 0;
	setAmount(value);
	player.getPackets().sendSound(4041, 0);
    }

    public void abortCurrentOffer() {
	int slot = getCurrentSlot();
	if (slot == -1)
	    return;
	abortOffer(slot);
    }

    public void abortOffer(int slot) {
	if (isSlotFree(slot))
	    return;
	GrandExchange.abortOffer(player, slot);
	player.getPackets().sendGameMessage("Abort request acknowledged. Please be aware that your offer may have already been completed.");
    player.getPackets().sendSound(4044, 0);
    }

    public void collectItems(int slot, int invSlot, int option) {
	if (slot == -1 || isSlotFree(slot))
	    return;
	GrandExchange.collectItems(player, slot, invSlot, option);
	player.getPackets().sendSound(4042, 0);
    }

    public void viewOffer(int slot) {
	if (isSlotFree(slot) || getCurrentSlot() != -1) {
	    return;
	}
	Offer offer = GrandExchange.getOffer(player, slot);
	if (offer == null)
	    return;
	setSlot(slot);
	setExtraDetails(offer.getId());
    }

    /*
     * includes noted
     */
    public int getItemAmount(Item item) {
	int notedId = item.getDefinitions().certId;
	return player.getInventory().getNumerOf(item.getId()) + player.getInventory().getNumerOf(notedId);
    }

    public void chooseItem(int id) {
	if (!player.getInterfaceManager().containsInterface(105))
	    return;
	setItem(new Item(id), false);
    }

    public void sendInfo(Item item) {
	player.getInterfaceManager().sendInventoryInterface(449);
	player.getPackets().sendConfig(741, item.getId());
	player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
	player.getPackets().sendGlobalString(34, ""); // quest id for some items
	int[] bonuses = new int[18];
	ItemDefinitions defs = item.getDefinitions();
	bonuses[CombatDefinitions.STAB_ATTACK] += defs.getStabAttack();
	bonuses[CombatDefinitions.SLASH_ATTACK] += defs.getSlashAttack();
	bonuses[CombatDefinitions.CRUSH_ATTACK] += defs.getCrushAttack();
	bonuses[CombatDefinitions.MAGIC_ATTACK] += defs.getMagicAttack();
	bonuses[CombatDefinitions.RANGE_ATTACK] += defs.getRangeAttack();
	bonuses[CombatDefinitions.STAB_DEF] += defs.getStabDef();
	bonuses[CombatDefinitions.SLASH_DEF] += defs.getSlashDef();
	bonuses[CombatDefinitions.CRUSH_DEF] += defs.getCrushDef();
	bonuses[CombatDefinitions.MAGIC_DEF] += defs.getMagicDef();
	bonuses[CombatDefinitions.RANGE_DEF] += defs.getRangeDef();
	bonuses[CombatDefinitions.SUMMONING_DEF] += defs.getSummoningDef();
	bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS] += defs.getAbsorveMeleeBonus();
	bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS] += defs.getAbsorveMageBonus();
	bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS] += defs.getAbsorveRangeBonus();
	bonuses[CombatDefinitions.STRENGTH_BONUS] += defs.getStrengthBonus();
	bonuses[CombatDefinitions.RANGED_STR_BONUS] += defs.getRangedStrBonus();
	bonuses[CombatDefinitions.PRAYER_BONUS] += defs.getPrayerBonus();
	bonuses[CombatDefinitions.MAGIC_DAMAGE] += defs.getMagicDamage();
	boolean hasBonus = false;
	for (int bonus : bonuses)
	    if (bonus != 0) {
		hasBonus = true;
		break;
	    }
	if (hasBonus) {
	    HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
	    if (requiriments != null && !requiriments.isEmpty()) {
		String reqsText = "";
		for (int skillId : requiriments.keySet()) {
		    if (skillId > 24 || skillId < 0)
			continue;
		    int level = requiriments.get(skillId);
		    if (level < 0 || level > 120)
			continue;
		    boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
		    reqsText += "<br>" + (hasReq ? "<col=00ff00>" : "<col=ff0000>") + "Level " + level + " " + Skills.SKILL_NAME[skillId];
		}
		player.getPackets().sendGlobalString(26, "<br>Worn on yourself, requiring: " + reqsText);
	    } else
		player.getPackets().sendGlobalString(26, "<br>Worn on yourself");
	    player.getPackets().sendGlobalString(35, "<br>Attack<br><col=ffff00>+" + bonuses[CombatDefinitions.STAB_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SLASH_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.CRUSH_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.MAGIC_ATTACK] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.RANGE_ATTACK] + "<br><col=ffff00>---" + "<br>Strength" + "<br>Ranged Strength" + "<br>Magic Damage" + "<br>Absorve Melee" + "<br>Absorve Magic" + "<br>Absorve Ranged" + "<br>Prayer Bonus");
	    player.getPackets().sendGlobalString(36, "<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
	    player.getPackets().sendGlobalString(52, "<<br>Defence<br><col=ffff00>+" + bonuses[CombatDefinitions.STAB_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SLASH_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.CRUSH_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.MAGIC_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.RANGE_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SUMMONING_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.STRENGTH_BONUS] + "<br><col=ffff00>" + bonuses[CombatDefinitions.RANGED_STR_BONUS] + "<br><col=ffff00>" + bonuses[CombatDefinitions.MAGIC_DAMAGE] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS] + "%<br><col=ffff00>" + bonuses[CombatDefinitions.PRAYER_BONUS]);
	} else {
	    player.getPackets().sendGlobalString(26, "");
	    player.getPackets().sendGlobalString(35, "");
	    player.getPackets().sendGlobalString(36, "");
	    player.getPackets().sendGlobalString(52, "");
	}

    }

    public void chooseItem() {
	if (getType() != 0)
	    return;
	player.getInterfaceManager().setInterface(true, 752, 7, 389);
	player.getPackets().sendRunScript(570, "Grand Exchange Item Search");
    }

    public void offer(int slot) {
	Item item = player.getInventory().getItem(slot);
	if (item == null)
	    return;
	setItem(item, true);
	player.getPackets().sendSound(4043, 0);
    }

    public void setExtraDetails(int id) {
	setItemId(id);
	setMarketPrice(GrandExchange.getPrice(id));
	player.getPackets().sendIComponentText(105, 143, ItemExamines.getExamine(new Item(id)));
    }

    public void setItem(Item item, boolean sell) {
	if (item.getId() == Shop.COINS || !ItemConstants.isTradeable(item)) {
		player.getPackets().sendSound(3654, 0);
	    player.getPackets().sendGameMessage("This item cannot be sold on the Grand Exchange.");
	    return;
	}
	if (item.getDefinitions().isNoted() && item.getDefinitions().getCertId() != -1)
	    item = new Item(item.getDefinitions().getCertId(), item.getAmount());
	int price = GrandExchange.getPrice(item.getId());
	setPricePerItem(price);
	setAmount(item.getAmount());
	setExtraDetails(item.getId());
	if (!sell)
	    sendInfo(item);
    }

    public void confirmOffer() {
	int type = getType();
	if (type == -1)
	    return;
	int slot = getCurrentSlot();
	if (slot == -1 || !isSlotFree(slot))
	    return;
	boolean buy = type == 0;
	int itemId = getItemId();
	if (itemId == -1) {
		player.getPackets().sendSound(3654, 0);
	    player.getPackets().sendGameMessage("You must choose an item to " + (buy ? "buy" : "sell") + "!");
	    return;
	}
	int amount = getAmount();
	if (amount == 0) {
		player.getPackets().sendSound(3654, 0);
	    player.getPackets().sendGameMessage("You must choose the quantity you wish to " + (buy ? "buy" : "sell") + "!");
	    return;
	}
	int pricePerItem = getPricePerItem();
	if (pricePerItem != 0) {
	    if (amount > 2147483647 / pricePerItem) { // TOO HIGH
	    	player.getPackets().sendSound(3654, 0);
		player.getPackets().sendGameMessage("You do not have enough coins to cover the offer.");
		return;
	    }
	}
	if (buy) {
	    int price = pricePerItem * amount;
	    if (player.getInventory().getCoinsAmount() < price) {
	    	player.getPackets().sendSound(3654, 0);
		player.getPackets().sendGameMessage("You do not have enough coins to cover the offer.");
		return;
	    }
	  //  player.getInventory().removeItemMoneyPouch(new Item(995, price));
	} else {
	    int inventoryAmount = getItemAmount(new Item(itemId));
	    if (amount > inventoryAmount) {
	    player.getPackets().sendSound(3654, 0);
		player.getPackets().sendGameMessage("You do not have enough of this item in your inventory to cover the offer.");
		return;
	    }
	    int notedId = ItemDefinitions.getItemDefinitions(itemId).certId; 
	    int notedAmount = player.getInventory().getNumerOf(notedId);
	    if (notedAmount < amount) {
		player.getInventory().deleteItem(notedId, notedAmount);
		player.getInventory().deleteItem(itemId, amount - notedAmount);
	    } else
		player.getInventory().deleteItem(notedId, amount);
	}
	GrandExchange.sendOffer(player, slot, itemId, amount, pricePerItem, buy);
	cancelOffer();
    }

    public void makeOffer(int slot, boolean sell) {
	if (!isSlotFree(slot) || getCurrentSlot() != -1) {
	    return;
	}
	setType(sell ? 1 : 0);
	setSlot(slot);
	if (sell) {
		/*
		 * 		player.setTemporaryAttribute("buying", Boolean.FALSE);
		player.getActionSender().sendConfig(1113, 1);
		player.getActionSender().sendInventoryInterface(107);
		player.getActionSender().sendRunScript(149, GrandExchangeConstants.OFFER, "IviiiIsssss");
		player.getActionSender().sendAccessMask(0, 27, 107, 18, 0, 1026);
		player.getActionSender().sendItems(-1, -1327, 93, player.getInventory().getContainer());
		player.getActionSender().setInterfaceConfig(107, 0, false);
		 */
	    player.getPackets().sendHideIComponent(105, 196, true);
	    player.getInterfaceManager().sendInventoryInterface(107);
	    player.getPackets().sendUnlockIComponentOptionSlots(107, 18, 0, 27, 0, 1026);
	    player.getPackets().sendInterSetItemsOptionsScript(107, 18, 93, 4, 7, "Offer");
	} else {
		player.getPackets().sendInterface(false, 752, 7, 389);
		player.getPackets().sendRunScript(149, new Object[] { "Grand Exchange Item Search" },"IviiiIsssss" );
		}
    }

}

