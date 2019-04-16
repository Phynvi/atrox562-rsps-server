package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.WorldObject;
import com.rs.game.World;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class BoxTrapping extends Action {

	public enum BoxEntities {

		FERRET(5081, new Item[] { new Item(10092, 1) }, 27, 1, 115, 28920, new Animation(5191), new Animation(5189)),
		BROWN_GECKO(7289, new Item[] { new Item(12738, 1) }, 27, 10, 100, 28921, new Animation(8361), new Animation(8362)),
		GREEN_GECKO(7290, new Item[] { new Item(12739, 1) }, 27, 10, 100, 28922, new Animation(8361), new Animation(8362)),
		RED_GECKO(7291, new Item[] { new Item(12740, 1) }, 27, 10, 100, 28923, new Animation(8361), new Animation(8362)),
		BLUE_GECKO(7292, new Item[] { new Item(12741, 1) }, 27, 10, 100, 28924, new Animation(8361), new Animation(8362)),
		GREY_RACCOON(6997, new Item[] { new Item(12486, 1) }, 27, 80, 100, 28925, new Animation(7722), new Animation(7720)),
		RED_RACCOON(7276, new Item[] { new Item(12736, 1) }, 27, 80, 100, 28926, new Animation(7722), new Animation(7720)),
		YELLOW_RACCOON(7275, new Item[] { new Item(12734, 1) }, 27, 80, 100, 28927, new Animation(7722), new Animation(7720)),
		MONKEY_ONE(7228, new Item[] { new Item(12682, 1) }, 27, 95, 100, 28928, new Animation(8349), new Animation(8350)),
		MONKEY_TWO(7229, new Item[] { new Item(12684, 1) }, 27, 95, 100, 28929, new Animation(8349), new Animation(8350)),
		MONKEY_THREE(7230, new Item[] { new Item(12686, 1) }, 27, 95, 100, 19189, new Animation(8349), new Animation(8350)),
		MONKEY_FOUR(7231, new Item[] { new Item(12688, 1) }, 27, 95, 100, 19190, new Animation(8349), new Animation(8350)),
		MONKEY_FIVE(7232, new Item[] { new Item(12690, 1) }, 27, 95, 100, 19191, new Animation(8349), new Animation(8350)),
		MONKEY_SIX(7233, new Item[] { new Item(12692, 1) }, 27, 95, 100, 28557, new Animation(8349), new Animation(8350)),
		MONKEY_SEVEN(7234, new Item[] { new Item(12694, 1) }, 27, 95, 100, 28558, new Animation(8349), new Animation(8350)),
		MONKEY_EIGHT(7235, new Item[] { new Item(12696, 1) }, 27, 95, 100, 28567, new Animation(8349), new Animation(8350)),
		MONKEY_NINE(7236, new Item[] { new Item(12698, 1) }, 27, 95, 100, 28906, new Animation(8349), new Animation(8350)),
		MONKEY_TEN(6944, new Item[] { new Item(12496, 1) }, 27, 95, 100, 28913, new Animation(8349), new Animation(8350)),
		BROWN_PLATYPUS(7024, new Item[] { new Item(12551, 1) }, 48, 10, 205, 29887, new Animation(8668), new Animation(8669)),
		GREY_PLATYPUS(7025, new Item[] { new Item(12552, 1) }, 48, 10, 205, 29888, new Animation(8668), new Animation(8669)),
		TAN_PLATYPUS(7026, new Item[] { new Item(12553, 1) }, 48, 10, 205, 29897, new Animation(8668), new Animation(8669)),
		CHINCHOMPA(5079, new Item[] { new Item(10033, 1) }, 53, 1, 198.5, 29898, new Animation(5184), new Animation(5185)),
		PENGUIN(8690, new Item[] { new Item(Utils.random(1) == 0 ? 14832 : 14833, 1) }, 56, 1, 150, 29899, new Animation(5671), new Animation(5669)),
		RED_CHINCHOMPA(5080, new Item[] { new Item(10034, 1) }, 63, 1, 265, 29900, new Animation(5671), new Animation(5669)),
		PAWYA(7012, new Item[] { new Item(12535, 1), new Item(526, 1), Utils.random(1) == 0 ? new Item(5288, 1) : null }, 66, 1, 400, 29901, new Animation(8615), new Animation(8611)),
		IMP(708, null, 71, 1, 450, 19226, new Animation(175), new Animation(173)),
		GRENWALL(7010, new Item[] { new Item(12539, Utils.random(18, 21)), new Item(526, 1) }, 77, 1, 1100, 29902, new Animation(8602), new Animation(8603));

		private int npcId, level, shakingBox, summoningLevel;
		private Item[] item;
		private double xp;
		private Animation successCatchAnim, failCatchAnim;
		static final Map<Integer, BoxEntities> npc = new HashMap<Integer, BoxEntities>();
		static final Map<Integer, BoxEntities> object = new HashMap<Integer, BoxEntities>();

		static {
			for (BoxEntities npcs : BoxEntities.values())
				npc.put(npcs.npcId, npcs);
			for (BoxEntities objets : BoxEntities.values())
				object.put(objets.shakingBox, objets);
		}

		public static BoxEntities forId(int id) {
			return npc.get(id);
		}

		public static BoxEntities forObjectId(int id) {
			return object.get(id);
		}

		public static Item getGrenwallLoot() {
			Item[] loot = new Item[] { new Item(12535, 1), new Item(215, 1), new Item(207, 1), new Item(2485, 1), new Item(209, 1), new Item(213, 1), new Item(3049, 1), new Item(211, 1), new Item(217) };
			return loot[Utils.getRandom(loot.length - 1)];
		}

		private BoxEntities(int npcId, Item[] item, int level, int summoningLevel, double xp, int shakingBox, Animation successAnim, Animation failAnim) {
			this.npcId = npcId;
			this.item = item;
			this.level = level;
			this.summoningLevel = summoningLevel;
			this.xp = xp;
			this.shakingBox = shakingBox;
			this.successCatchAnim = successAnim;
			this.failCatchAnim = failAnim;
		}

		public Animation getFailCatchAnim() {
			return failCatchAnim;
		}

		public Item[] getItems() {
			return item;
		}

		public int getLevel() {
			return level;
		}

		public int getSummoningRequirement() {
			return summoningLevel;
		}

		public int getNpcId() {
			return npcId;
		}

		public Animation getSuccessCatchAnim() {
			return successCatchAnim;
		}

		public int getSuccessfulTransformObjectId() {
			return shakingBox;
		}

		public double getXp() {
			return xp;
		}
	}

	public int getTrapAmount(Player player) {
		int level = 20;
		int trapAmount = 2;
		for (int i = 0; i < 3; i++) {
			if (player.getSkills().getLevel(Skills.HUNTER) >= level) {
				trapAmount++;
				level += 20;
			}
		}
		return trapAmount;
	}
	
	int itemId = -1;
	
	public BoxTrapping(int itemId) {
		this.itemId = itemId;
	}

	@Override
	public boolean start(Player player) {
		int trapAmt = getTrapAmount(player);
		if (OwnedObjectManager.getObjectsforValue(player, itemId == 10025 ? 19223 : 19187) == trapAmt) {
			player.getPackets().sendGameMessage("You can't setup more than " + trapAmt + " traps.");
			return false;
		}
		List<WorldObject> objects = World.getRegion(player.getRegionId()).getSpawnedObjects();
		if (objects != null) {
			for (WorldObject object : objects) {
				if (object.getX() == player.getX() && object.getY() == player.getY() && object.getPlane() == player.getPlane()) {
					player.sendMessage("You can't setup your trap here.");
					return false;
				}
			}
		}
		player.sendMessage("You start setting up the trap..");
		player.setNextAnimation(new Animation(5208));
		player.lock(3);
		setActionDelay(player, 2);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1))
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1))
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1))
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
		player.getInventory().deleteItem(itemId, 1);
		new OwnedObjectManager(player, new WorldObject[] { new WorldObject(itemId == 10025 ? 19223 : 19187, 10, 0, player.getX(), player.getY(), player.getPlane()) }, 600000);
		return -1;
	}

	@Override
	public void stop(Player player) {
	}
	
	public static void openImpReleaseNegotiationForm(Player player, int slot) {
		player.getPackets().sendUnlockIComponentOptionSlots(478, 14, 0, 27, 0, 1);
		player.getTemporaryAttributtes().put("ImpRelease", slot);
		if (player.getInventory().get(slot).getId() == 10028) {
				player.getPackets().sendIComponentText(478, 13, "Select an item or stack of items to deposit.<br>You can deposit one more item or stack.");
		}
		player.setCloseInterfacesEvent(new Runnable() {
			@Override
			public void run() {
				player.getTemporaryAttributtes().remove("ImpRelease");
			}
		});
		player.getInterfaceManager().sendInterface(478);
	}
	
	public static boolean sendDirectly(Player player, int impSlot, int usedWith) {
		if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness) {
			if (Wilderness.getWildLevel(player) > 30 && !Wilderness.isAtWildSafe(player)) {
				player.sendMessage("The imp refuses to carry items this far in Wilderness.");
			return false;
			}
		}
		Item imp = player.getInventory().getItem(impSlot);
		if (imp.getId() != 10027 && imp.getId() != 10028)
			return false;
		Item target = player.getInventory().getItem(usedWith);
		imp.setId(imp.getId() == 10027 ? 10028 : 10025);
		player.getInventory().deleteItem(target);
		player.getBank().addItem(target.getId(), target.getAmount(), true);
		for (int i = 0; i < 27; i++)
			player.getInventory().refresh(i);
		if (imp.getId() == 10025)
			player.sendMessage("You release the imp after it sends the " + target.getDefinitions().getName() + (target.getAmount() > 1 && !target.getName().endsWith("s") ? "s" : "") + " to your bank.");
		else
			player.sendMessage("The imp sends the " + target.getDefinitions().getName() + (target.getAmount() > 1 && !target.getName().endsWith("s") ? "s" : "") + " to your bank.");
		return true;
	}
	
	public static void handleImpReleaseNegotiationForm(Player player, int slotId) {
		if (player.getControlerManager().getControler() != null && player.getControlerManager().getControler() instanceof Wilderness) {
			if (Wilderness.getWildLevel(player) > 30 && !Wilderness.isAtWildSafe(player)) {
				player.sendMessage("The imp refuses to carry items this far in Wilderness.");
			return;
			}
		}
		int impSlot = player.getTemporaryAttributtes().get("ImpRelease") == null ? -1 : (int) player.getTemporaryAttributtes().get("ImpRelease");
		if (impSlot == -1)
			player.getInterfaceManager().closeScreenInterface();
		Item imp = player.getInventory().get(impSlot);
		player.getInventory().get(impSlot).setId(player.getInventory().get(impSlot).getId() == 10027 ? 10028 : 10025);
		Item toRemove = player.getInventory().get(slotId);
		player.getInventory().deleteItem(toRemove);
		player.getBank().addItem(toRemove.getId(), toRemove.getAmount(), true);
		for (int i = 0; i < 27; i++)
			player.getInventory().refresh(i);
		if (imp.getId() == 10025) {
			player.getInterfaceManager().closeScreenInterface();
			player.sendMessage("You release the imp after it sends the " + toRemove.getDefinitions().getName() + (toRemove.getAmount() > 1 && !toRemove.getName().endsWith("s") ? "s" : "") + " to your bank.");
		} else {
			player.getPackets().sendIComponentText(478, 13, "Select an item or stack of items to deposit.<br>You can deposit one more item or stack.");
			player.sendMessage("The imp sends the " + toRemove.getDefinitions().getName() + (toRemove.getAmount() > 1 && !toRemove.getName().endsWith("s") ? "s" : "") + " to your bank.");
		}
		if (player.getInventory().get(impSlot) == null)
			player.getInterfaceManager().closeScreenInterface();
	}
	
	public static void handleItemSwitching(Player player, int fromSlot, int toSlot) {
		int impSlot = player.getTemporaryAttributtes().get("ImpRelease") == null ? -1 : (int) player.getTemporaryAttributtes().get("ImpRelease");
		if (impSlot == -1)
			return;
		else if (fromSlot == impSlot)
			player.getTemporaryAttributtes().put("ImpRelease", toSlot);
		else if (toSlot == impSlot)
			player.getTemporaryAttributtes().put("ImpRelease", fromSlot);
	}

}
