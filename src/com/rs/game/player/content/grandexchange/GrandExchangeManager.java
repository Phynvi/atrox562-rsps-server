package com.rs.game.player.content.grandexchange;
 
import java.io.Serializable;
import java.util.HashMap;
 
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.Shop;
import com.rs.utils.ItemBonuses;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.game.player.content.grandexchange.Offer;
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
            player.getPackets().sendIComponentText(643, 25 + i,
                    o == null ? "" : o.isBought() ? "You bought" : "You sold");
            player.getPackets().sendIComponentText(
                    643,
                    35 + i,
                    o == null ? "" : ItemDefinitions.getItemDefinitions(
                            o.getId()).getName());
            player.getPackets().sendIComponentText(
                    643,
                    30 + i,
                    o == null ? "" : ""
                            + Utils.getFormattedNumber(o.getQuantity(), ','));
            player.getPackets().sendIComponentText(
                    643,
                    40 + i,
                    o == null ? "" : ""
                            + Utils.getFormattedNumber(o.getPrice(), ','));
        }
    }
 
    public void openGrandExchange() {
        player.getInterfaceManager().sendInterface(105);
		if (!player.isDonator()) {
			player.getPackets().sendHideIComponent(105, 79, true);
			player.getPackets().sendHideIComponent(105, 80, true);
			player.getPackets().sendHideIComponent(105, 81, true);
			player.getPackets().sendHideIComponent(105, 82, true);
			player.getPackets().sendHideIComponent(105, 98, true);
			player.getPackets().sendHideIComponent(105, 99, true);
			player.getPackets().sendHideIComponent(105, 100, true);
			player.getPackets().sendHideIComponent(105, 101, true);
			player.getPackets().sendHideIComponent(105, 117, true);
			player.getPackets().sendHideIComponent(105, 118, true);
			player.getPackets().sendHideIComponent(105, 119, true);
			player.getPackets().sendHideIComponent(105, 120, true);
		}
		player.getPackets().sendUnlockIComponentOptionSlots(105, 209, -1, 0, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(105, 211, -1, 0, 0, 1);
        cancelOffer();
        player.setCloseInterfacesEvent(new Runnable() {
            @Override
            public void run() {
                player.getPackets().sendRunScript(571);
                player.getInterfaceManager().removeInterface(752, 7);
            }
        });
    }
 
    public void openCollectionBox() {
        player.getInterfaceManager().sendInterface(109);
        player.getPackets()
                .sendUnlockIComponentOptionSlots(109, 18, 0, 2, 0, 1);
        player.getPackets()
                .sendUnlockIComponentOptionSlots(109, 22, 0, 2, 0, 1);
        player.getPackets()
                .sendUnlockIComponentOptionSlots(109, 26, 0, 2, 0, 1);
        player.getPackets()
                .sendUnlockIComponentOptionSlots(109, 31, 0, 2, 0, 1);
        player.getPackets()
                .sendUnlockIComponentOptionSlots(109, 36, 0, 2, 0, 1);
        player.getPackets()
                .sendUnlockIComponentOptionSlots(109, 41, 0, 2, 0, 1);
    }
	
	public void openSets() {
        player.getInterfaceManager().sendInterface(645);
		player.getPackets().sendGESetOption();
    }
 
    public void setSlot(int slot) {
        player.getVarBitManager().sendVar(1112, slot);
    }
 
    public void setMarketPrice(int price) {
        player.getVarBitManager().sendVar(1114, price);
    }
 
    public void setPricePerItem(int price) {
		if (price == 0) {
			setPricePerItem(1);
		}
        player.getVarBitManager().sendVar(1111, price);
    }
 
    public int getPricePerItem() {
        return player.getVarBitManager().getValue(1111);
    }
 
    public int getCurrentSlot() {
        return player.getVarBitManager().getValue(1112);
    }
   
    public void setItemId(int id) {
        player.getVarBitManager().sendVar(1109, id);
    }
 
    public int getItemId() {
        return player.getVarBitManager().getValue(1109);
    }
   
    public void setAmount(int amount) {
        player.getVarBitManager().sendVar(1110, amount);
    }
 
    public int getAmount() {
        return player.getVarBitManager().getValue(1110);
    }
 
    public void setType(int amount) {
        player.getVarBitManager().sendVar(1113, amount);
    }
 
    public int getType() {
        return player.getVarBitManager().getValue(1113);
    }
 
    public void handleButtons(int interfaceId, int componentId, int slotId, int packetId) {
    if (interfaceId == 105) {
        switch (componentId) {
        case 18:
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
            viewOffer(0);
            else
            abortOffer(0);
            break;
        case 34:
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
            viewOffer(1);
            else
            abortOffer(1);
            break;
        case 50:
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
            viewOffer(2);
            else
            abortOffer(2);
            break;
        case 69:
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
            viewOffer(3);
            else
            abortOffer(3);
            break;
        case 88:
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
            viewOffer(4);
            else
            abortOffer(4);
            break;
        case 107:
            if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
            viewOffer(5);
            else
            abortOffer(5);
            break;
        case 203:
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
        case 127:
            cancelOffer();
            break;
        case 157:
            modifyAmount(getAmount() - 1);
            player.getPackets().sendSound(4045, 0);
            break;
        case 159:
            modifyAmount(getAmount() + 1);
            player.getPackets().sendSound(4041, 0);
            break;
        case 162:
            modifyAmount(getAmount() + 1);
            player.getPackets().sendSound(4041, 0);
            break;
        case 164:
            modifyAmount(getAmount() + 10);
            player.getPackets().sendSound(4041, 0);
            break;
        case 166:
            modifyAmount(getAmount() + 100);
            player.getPackets().sendSound(4041, 0);
            break;
        case 168:
            modifyAmount(getType() == 0 ? getAmount() + 1000 : getItemAmount(new Item(getItemId())));
            player.getPackets().sendSound(4041, 0);
            break;
        case 170:
            editAmount();
            break;
        case 171:
            modifyPricePerItem(getPricePerItem() - 1);
            player.getPackets().sendSound(4045, 0);
            break;
        case 173:
            modifyPricePerItem(getPricePerItem() + 1);
            player.getPackets().sendSound(4041, 0);
            break;
        case 180:
            modifyPricePerItem(GrandExchange.getPrice(getItemId()));
            player.getPackets().sendSound(4045, 0);
            break;
        case 185:
            editPrice();
            player.getPackets().sendSound(4041, 0);
            break;
        case 183:
            modifyPricePerItem((int) (Math.ceil(getPricePerItem() * 1.05)));
            player.getPackets().sendSound(4041, 0);
            break;
        case 177:
            modifyPricePerItem((int) (getPricePerItem() * 0.95));
            player.getPackets().sendSound(4041, 0);
            break;
        case 186:
            confirmOffer();
            player.getPackets().sendSound(4043, 0);
            break;
        case 190:
        confirmOffer();
        player.getPackets().sendSound(4043, 0);
            break;
		case 194:
		openSearch();
			break;
        case 209:
                collectItems(
                        getCurrentSlot(),
                        0,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            case 211:
                collectItems(
                        getCurrentSlot(),
                        1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
        }
        } else if (interfaceId == 107 && componentId == 18)
            offer(slotId);
        else if (interfaceId == 449 && componentId == 1)
            player.getInterfaceManager().closeInventoryInterface();
        else if (interfaceId == 109) {
            switch (componentId) {
            case 18:
                collectItems(
                        0,
                        slotId == 0 ? 0 : 1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            case 22:
                collectItems(
                        1,
                        slotId == 0 ? 0 : 1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            case 26:
                collectItems(
                        2,
                        slotId == 0 ? 0 : 1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            case 31:
                collectItems(
                        3,
                        slotId == 0 ? 0 : 1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            case 36:
                collectItems(
                        4,
                        slotId == 0 ? 0 : 1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            case 41:
                collectItems(
                        5,
                        slotId == 0 ? 0 : 1,
                        packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0
                                : 1);
                player.getPackets().sendSound(4042, 0);
                break;
            }
        }
    }
 
    /*
     * public static final int ITEMID_CONFIG = 1109, AMOUNT_CONFIG = 1110,
     * PRICE_PER_CONFIG = 1111, SLOT_CONFIG = 1112, TYPE_CONFIG = 1113;
     */
 
    public void cancelOffer() {
		setItemId(-1);
		setAmount(0);
		setPricePerItem(1);
		setMarketPrice(0);
		setSlot(-1);
		setType(-1);
		player.getInterfaceManager().closeInventoryInterface();
    }
 
    public void editAmount() {
        if (getType() == -1)
            return;
        if (getItemId() == -1) {
            player.getPackets().sendGameMessage(
                    "You must choose an item first.");
            return;
        }
        player.temporaryAttribute().put("GEQUANTITYSET", true);
        player.getPackets().sendRunScript(
                108,
                "Enter the quantity you wish to "
                        + (getType() == 0 ? "purchase" : "sell") + ":");
    }
 
    public void editPrice() {
        if (getType() == -1)
            return;
        if (getItemId() == -1) {
            player.getPackets().sendGameMessage(
                    "You must choose an item first.");
            return;
        }
        player.temporaryAttribute().put("GEPRICESET", true);
        player.getPackets().sendRunScript(
                108,
                "Enter the price you wish to to "
                        + (getType() == 0 ? "buy" : "sell") + " for:");
    }
 
    public void modifyPricePerItem(int value) {
        if (getType() == -1)
            return;
        if (getItemId() == -1) {
            player.getPackets().sendGameMessage(
                    "You must choose an item first.");
            player.getPackets().sendSound(3654, 0);
            return;
        }
        if (value < 1)
            value = 1;
        setPricePerItem(value);
    }
 
    public void modifyAmount(int value) {
        if (getType() == -1)
            return;
        if (value < 0)
            value = 0;
        setAmount(value);
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
    }
 
    public void collectItems(int slot, int invSlot, int option) {
        if (slot == -1 || isSlotFree(slot))
            return;
        GrandExchange.collectItems(player, slot, invSlot, option);
    }
 
    public void viewOffer(int slot) {
        if (isSlotFree(slot) || getCurrentSlot() != -1) {
			player.getPackets().sendGameMessage("viewOffer error: slotFree / current slot -1");
            return;
        }
        Offer offer = GrandExchange.getOffer(player, slot);
        if (offer == null) {
			player.getPackets().sendGameMessage("viewOffer error: offer == null");
            return;
		}
        setSlot(slot);
        setExtraDetails(offer.getId());
    }
 
    /*
     * includes noted
     */
    public int getItemAmount(Item item) {
        int notedId = item.getDefinitions().certId;
        return player.getInventory().getAmountOf(item.getId())
                + player.getInventory().getAmountOf(notedId);
    }
 
    public void chooseItem(int id) {
        if (!player.getInterfaceManager().containsInterface(105))
            return;
        setItem(new Item(id), false);
    }
	
	public void sendInfo(Item item) {
		if (item == null)
			return;
		player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": current costs " + getPricePerItem() + " "
				+ ItemDefinitions.getItemDefinitions(995).getName().toLowerCase() + ".");
		player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendHideIComponent(449, 21, true);
		player.getPackets().sendButtonConfig(741, item.getId());
		player.getPackets().sendButtonConfig(745, 0);
		player.getPackets().sendButtonConfig(746, -1);
		player.getPackets().sendButtonConfig(168, 98);
		player.getPackets().sendItemOnIComponent(449, 13, item.getId(), 1);
		player.getPackets().sendIComponentText(449, 14, item.getName() + "");
		player.getPackets().sendIComponentText(449, 25,
				"You currently have: "
						+ (player.getInventory().getNumberOf(995) >= getPricePerItem() ? player.getInventory().getNumberOf(995)
								: "<col=ff0000>" + player.getInventory().getNumberOf(995))
						+ " " + ItemDefinitions.getItemDefinitions(995).getName().toLowerCase() + ".");

		int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
		if (bonuses != null) {
			boolean noReqs = false;
			HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
			if (requiriments != null && !requiriments.isEmpty()) {

				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 99)
						continue;
					boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
					reqsText += "<br>" + (hasReq ? "<col=00ff00>" : "<col=ff0000>") + "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}

				// furthermore

				player.getPackets().sendGlobalString(26, reqsText);
				noReqs = reqsText == "" ? true : false;
			}
			player.getPackets().sendGlobalString(34,
					noReqs ? "<br>Worn on yourself, requiring: " : "<br>Worn on yourself");
			if (!noReqs)
				player.getPackets().sendGlobalString(26, "");
			player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
			player.getPackets().sendGlobalString(35,
					"<br>Attack<br><col=ffff00>+" + bonuses[CombatDefinitions.STAB_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK] + "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage" + "<br>Prayer Bonus");
			player.getPackets().sendGlobalString(36, "<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.getPackets().sendGlobalString(52, "<<br>Defence<br><col=ffff00>+"
					+ bonuses[CombatDefinitions.STAB_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SLASH_DEF]
					+ "<br><col=ffff00>+" + bonuses[CombatDefinitions.CRUSH_DEF] + "<br><col=ffff00>+"
					+ bonuses[CombatDefinitions.MAGIC_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.RANGE_DEF]
					+ "<br><col=ffff00>+" + bonuses[CombatDefinitions.SUMMONING_DEF] + "<br><col=ffff00>+"
					+ bonuses[CombatDefinitions.STRENGTH_BONUS] + "<br><col=ffff00>"
					+ bonuses[CombatDefinitions.RANGED_STR_BONUS] + "<br><col=ffff00>"
					+ bonuses[CombatDefinitions.MAGIC_DAMAGE] + "%<br><col=ffff00>"
					+ bonuses[CombatDefinitions.PRAYER_BONUS]);

		} else {
			player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
			player.getPackets().sendGlobalString(34, "");
		}
		/*
		 * } else player.getPackets().sendGlobalString(26, "");
		 */

	}
 
    /*public void sendInfo(Item item) {
        player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendItemOnIComponent(449, 13, item.getId(), 1);
		player.getPackets().sendIComponentText(449, 14, item.getName() + "");
		player.getPackets().sendIComponentText(449, 25,
				"You currently have: "
						+ (player.getInventory().getNumberOf(995) >= getPricePerItem() ? player.getInventory().getNumberOf(995)
								: "<col=ff0000>" + player.getInventory().getNumberOf(995))
						+ " " + ItemDefinitions.getItemDefinitions(995).getName().toLowerCase() + ".");
        player.getPackets().sendConfig(741, item.getId());
        player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
        player.getPackets().sendGlobalString(34, ""); // quest id for some items
        int[] bonuses = new int[18];
        item.getDefinitions();
        for (int bonus : bonuses)
            if (bonus != 0) {
                break;
            }
            HashMap<Integer, Integer> requiriments = item.getDefinitions()
                    .getWearingSkillRequiriments();
            if (requiriments != null && !requiriments.isEmpty()) {
                String reqsText = "";
                for (int skillId : requiriments.keySet()) {
                    if (skillId > 24 || skillId < 0)
                        continue;
                    int level = requiriments.get(skillId);
                    if (level < 0 || level > 120)
                        continue;
                    boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
                    reqsText += "<br>"
                            + (hasReq ? "<col=00ff00>" : "<col=ff0000>")
                            + "Level " + level + " "
                            + Skills.SKILL_NAME[skillId];
                }
                player.getPackets().sendGlobalString(26, "<br>Worn on yourself, requiring: " + reqsText);
            } else
                player.getPackets().sendGlobalString(26, "<br>Worn on yourself");
			player.getPackets().sendGlobalString(
                    35,
                    "<br>Attack<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.STAB_ATTACK]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.SLASH_ATTACK]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.CRUSH_ATTACK]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.MAGIC_ATTACK]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.RANGE_ATTACK]
                            + "<br><col=ffff00>---" + "<br>Strength"
                            + "<br>Ranged Strength" + "<br>Magic Damage"
                            + "<br>Absorve Melee" + "<br>Absorve Magic"
                            + "<br>Absorve Ranged" + "<br>Prayer Bonus");
            player.getPackets()
                    .sendGlobalString(36,
                            "<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
            player.getPackets().sendGlobalString(
                    52,
                    "<<br>Defence<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.STAB_DEF]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.SLASH_DEF]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.CRUSH_DEF]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.MAGIC_DEF]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.RANGE_DEF]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.SUMMONING_DEF]
                            + "<br><col=ffff00>+"
                            + bonuses[CombatDefinitions.STRENGTH_BONUS]
                            + "<br><col=ffff00>"
                            + bonuses[CombatDefinitions.RANGED_STR_BONUS]
                            + "<br><col=ffff00>"
                            + bonuses[CombatDefinitions.MAGIC_DAMAGE]
                            + "%<br><col=ffff00>"
                            + bonuses[CombatDefinitions.ABSORVE_MELEE_BONUS]
                            + "%<br><col=ffff00>"
                            + bonuses[CombatDefinitions.ABSORVE_MAGE_BONUS]
                            + "%<br><col=ffff00>"
                            + bonuses[CombatDefinitions.ABSORVE_RANGE_BONUS]
                            + "%<br><col=ffff00>"
                            + bonuses[CombatDefinitions.PRAYER_BONUS]);
            player.getPackets().sendGlobalString(26, "");
            player.getPackets().sendGlobalString(35, "");
            player.getPackets().sendGlobalString(36, "");
            player.getPackets().sendGlobalString(52, "");
    }*/
 
    public void chooseItem() {
        if (getType() != 0)
            return;
        setItemId(-1);
        setAmount(0);
        setPricePerItem(1);
        setMarketPrice(0);
        player.getInterfaceManager().closeInventoryInterface();
        player.getPackets().sendConfig(1109, -1);
        player.getPackets().sendConfig(1112, 0);
        player.getPackets().sendConfig(1113, 0);
        player.getPackets().sendConfig1(1241, 16750848);
        player.getPackets().sendConfig1(1242, 15439903);
        player.getPackets().sendConfig1(741, -1);
        player.getPackets().sendConfig1(743, -1);
        player.getPackets().sendConfig1(744, 0);
        player.getPackets().sendInterface(true, 752, 7, 389);
        player.getPackets().sendRunScript(570,
                new Object[] { "Grand Exchange Item Search" });
    }
 
    public void offer(int slot) {
        Item item = player.getInventory().getItem(slot);
        if (item == null)
            return;
        setItem(item, true);
    }
 
    public void setExtraDetails(int id) {
        setItemId(id);
        setMarketPrice(GrandExchange.getPrice(id));
        int totalsellamount = GrandExchange.getTotalSellQuantity(id);
        int totalbuyamount = GrandExchange.getTotalBuyQuantity(id);
        int sellprice = GrandExchange.getCheapestSellPrice(id);
        int sellamount = GrandExchange.getSellQuantity(id);
        int buyprice = GrandExchange.getBestBuyPrice(id);
        int buyamount = GrandExchange.getBuyQuantity(id);
        if (getType() == 1) {
            if (GrandExchange.getPrice(id) >= 1 && !UnlimitedGEReader.itemIsLimited(id) || LimitedGEReader.itemIsLimited(id)) {
                if (totalbuyamount > 0) {
                    player.getPackets().sendIComponentText(105, 142, ""
                        + ItemExamines.getExamine(new Item(id))
                        + "<br><br>Best sell price: " + Utils.getFormattedNumber(buyprice, ',')
                        + "<br>Quantity: "
                        + Utils.getFormattedNumber(buyamount, ','));
                    player.getPackets().sendFilteredGameMessage(true, "There are %s %s currently being bought on the Grand Exchange.",
                            Utils.getFormattedNumber(totalbuyamount, ','), ItemDefinitions.getItemDefinitions(id).getName() + "s");
                } else
                    player.getPackets().sendIComponentText(105, 142, ""
                        + ItemExamines.getExamine(new Item(id))
                        + "<br> <br>There are currently no buy offers for this item.");
            } else {
                player.getPackets()
                .sendIComponentText(
                        105,
                        142,
                        ItemExamines.getExamine(new Item(id))
                                + "<br> <br>The Grand Exchange will automatically buy this <br>"
                                + "item for 5% less than its guide price.");
            }
        }
        if (getType() == 0) {
            if ((GrandExchange.getPrice(id) != 1
                    && GrandExchange.getPrice(id) < 5000
                    && !LimitedGEReader.itemIsLimited(id)
                    && GrandExchange.getPrice(id) != 1) || UnlimitedGEReader.itemIsLimited(id)) {
                player.getPackets().sendIComponentText(
                        105,
                        142,
                        ItemExamines.getExamine(new Item(id))
                                + "<br> <br>Quantity: Unlimited.");
            } else {
                if (totalsellamount > 0) {
                    player.getPackets().sendIComponentText(105, 142, ""
                        + ItemExamines.getExamine(new Item(id))
                        + "<br><br>Lowest price: " + Utils.getFormattedNumber(sellprice, ',')
                        + "<br>Quantity: "
                        + Utils.getFormattedNumber(sellamount, ','));
                    player.getPackets().sendFilteredGameMessage(true, "There are %s %s currently being sold on the Grand Exchange.",
                            Utils.getFormattedNumber(totalsellamount, ','), ItemDefinitions.getItemDefinitions(id).getName() + "s");
                } else
                    player.getPackets().sendIComponentText(105, 142, ""
                        + ItemExamines.getExamine(new Item(id))
                        + "<br> <br>There are currently no sell offers for this item.");
 
            }
        }
    }
 
    public void setItem(Item item, boolean sell) {
        if (item.getId() == Shop.COINS || !ItemConstants.isTradeable(item)) {
            player.getPackets().sendGameMessage(
                    "This item cannot be sold on the Grand Exchange.");
            return;
        }
        if (item.getDefinitions().isNoted()
                && item.getDefinitions().getCertId() != -1)
            item = new Item(item.getDefinitions().getCertId(), item.getAmount());
        int price = GrandExchange.getPrice(item.getId());
        setPricePerItem(price);
        if (!sell)
            setAmount(1);
        else
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
        player.getPackets().sendGameMessage("You must choose an item to " + (buy ? "buy" : "sell") + "!");
        return;
    }
    int amount = getAmount();
    if (amount == 0) {
        player.getPackets().sendGameMessage("You must choose the quantity you wish to " + (buy ? "buy" : "sell") + "!");
        return;
    }
    int pricePerItem = getPricePerItem();
    if (pricePerItem != 0) {
        if (amount > 2147483647 / pricePerItem) { // TOO HIGH
        player.getPackets().sendGameMessage("You do not have enough coins to cover the offer.");
        return;
        }
    }
    if (buy) {
        int price = pricePerItem * amount;
        if (player.getInventory().getCoinsAmount() < price) {
        player.getPackets().sendGameMessage("You do not have enough coins to cover the offer.");
        return;
        } else
		player.getInventory().deleteItem(995, price);
    } else {
        int inventoryAmount = getItemAmount(new Item(itemId));
        if (amount > inventoryAmount) {
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
	GrandExchange.save();
    cancelOffer();
    }
	
	public void openSearch() {
		player.getPackets().sendConfig1(1109, -1);
        player.getPackets().sendConfig1(1112, 0);
        player.getPackets().sendConfig1(1113, 0);
        player.getPackets().sendInterface(true, 752, 6, 389);
        player.getPackets().sendRunScript(570, new Object[] { "Grand Exchange Item Search" });
	}
	
	public void makeOffer(int slot, boolean sell) {
		if (!isSlotFree(slot) || getCurrentSlot() != -1) {
			return;
		}
		setType(sell ? 1 : 0);
		setSlot(slot);
		if (sell) {
			player.getPackets().sendHideIComponent(105, 196, true);
			player.getInterfaceManager().sendInventoryInterface(107);
			player.getPackets().sendUnlockIComponentOptionSlots(107, 18, 0, 27,
					0);
			player.getPackets().sendInterSetItemsOptionsScript(107, 18, 93, 4,
					7, "Offer");
		} else {
			player.getPackets().sendHideIComponent(105, 196, true);
			player.getPackets().sendConfig(1109, -1);
			player.getPackets().sendConfig1(741, -1);
			player.getPackets().sendConfig1(743, -1);
			player.getPackets().sendConfig1(744, 0);
			player.getPackets().sendInterface(true, 752, 6, 389);
			player.getPackets().sendRunScript(570,
					new Object[] { "Grand Exchange Item Search" });
		}
	}
 
    /*public void makeOffer(int slot, boolean sell) {
        if (!isSlotFree(slot)) {
            return;
        }
        setType(sell ? 1 : 0);
        setSlot(slot);
        if (sell) {
            player.getPackets().sendHideIComponent(105, 196, true);
            player.getInterfaceManager().sendInventoryInterface(107);
            player.getPackets().sendUnlockIComponentOptionSlots(107, 18, 0, 27,
                    0);
            player.getPackets().sendInterSetItemsOptionsScript(107, 18, 93, 4,
                    7, "Offer");
        } else {
        player.getPackets().sendConfig(1109, -1);//sendConfig1
        player.getPackets().sendConfig(1112, 0);//sendConfig1
        player.getPackets().sendConfig(1113, 0);//sendConfig1
        player.getPackets().sendInterface(true, 752, 6, 389);
        player.getPackets().sendRunScript(570, new Object[] { "Grand Exchange Item Search" });
        }
    }*/
}