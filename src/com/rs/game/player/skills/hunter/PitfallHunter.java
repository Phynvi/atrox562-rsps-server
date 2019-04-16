package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;
import com.rs.game.WorldObject;
import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.game.player.Player;
import com.rs.game.npc.hunter.PitfallNPCs;

/**
 * Handles all possible pitfall methods.
 * 
 * @author Kris
 */
public class PitfallHunter extends Action {

	public enum PitfallPreys {

		SABRE_TOOTHED_KYATT(5103, 55, 19264, new Item(10103, 1), 300, new Animation(5208)), SPINED_LARUPIA(5104, 31,
				19259, new Item(10095, 1), 180,
				new Animation(5208)), HORNED_GRAAHK(5105, 41, 19264, new Item(10099, 1), 240, new Animation(5208));

		private Item loot;
		private int experience, npcId, objectId, level;
		private Animation placingTrap;

		static final Map<Integer, PitfallPreys> npc = new HashMap<Integer, PitfallPreys>();
		static final Map<Integer, PitfallPreys> object = new HashMap<Integer, PitfallPreys>();

		static {
			for (PitfallPreys npcs : PitfallPreys.values())
				npc.put(npcs.npcId, npcs);
			for (PitfallPreys objects : PitfallPreys.values()) {
				for (int i = 0; i < 5; i++)
					object.put(objects.objectId + i, objects);
			}

		}

		public static PitfallPreys forObjectId(int id) {
			return object.get(id);
		}

		public static PitfallPreys forId(int id) {
			return npc.get(id);
		}

		private PitfallPreys(int npcId, int level, int objectId, Item loot, int experience, Animation placingTrap) {
			this.npcId = npcId;
			this.level = level;
			this.objectId = objectId;
			this.loot = loot;
			this.experience = experience;
			this.placingTrap = placingTrap;
		}

		public Item getLoot() {
			return loot;
		}

		public int getExperience() {
			return experience;
		}

		public Animation getPlacingAnimation() {
			return placingTrap;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevel() {
			return level;
		}
	}

	private WorldObject object;

	public PitfallHunter(WorldObject object) {
		this.object = object;
	}

	@Override
	public boolean start(Player player) {
		int trapAmt = HunterCore.getTrapAmount(player);
		int total = 0;
		if (total == trapAmt) {
			player.getPackets().sendGameMessage("You can't setup more than " + trapAmt + " traps.");
			return false;
		}
		if (!player.getInventory().containsItem(946, 1)
				|| !player.getInventory().containsItem(HunterCore.getLogsId(player), 1)) {
			player.sendMessage("You need a knife and some logs to set up a pitfall trap.");
			return false;
		}
		player.sendMessage("You start setting up the trap..");
		player.setNextAnimation(new Animation(5208));
		player.getInventory().deleteItem(HunterCore.getLogsId(player), 1);
		player.lock(5);
		setActionDelay(player, 5);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		PitfallNPCs.addPitfallTrap(object, player);
		player.getVarBitManager().sendVarbit(object.getDefinitions().varbit, 1);
		return -1;
	}

	@Override
	public void stop(final Player player) {
	}

}