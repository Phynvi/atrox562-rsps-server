package com.rs.game.minigames.clanwars;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.Pots;
import com.rs.game.player.controlers.Controler;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Misc;

/**
 * Handles the FFA Clan Wars zone.
 * 
 * @author Emperor
 * @Improved Andreas - AvalonPK
 * 
 */
public final class FfaZone extends Controler {

	/**
	 * If the FFA zone is the risk zone.
	 */
	private boolean risk;

	/**
	 * If the player was in the ffa pvp area.
	 */
	private transient boolean wasInArea = false;

	private boolean showingSkull;

	@Override
	public void start() {
		if (getArguments() == null || getArguments().length < 1) {
			this.setRisk(player.getX() >= 2948 && player.getY() >= 5508
					&& player.getX() <= 3071 && player.getY() <= 5631);
		} else {
			this.setRisk((Boolean) getArguments()[0]);
		}
		moved();
	}
	
	public static String randomDeath(Player p) {
		switch (Misc.random(8)) {
		case 0:
			return "There is no escape, " + p.getDisplayName()
					+ "...";
		case 1:
			return "Muahahahaha!";
		case 2:
			return "You belong to me!";
		case 3:
			return "Beware mortals, " + p.getDisplayName()
					+ " travels with me!";
		case 4:
			return "Your time here is over, " + p.getDisplayName()
					+ "!";
		case 5:
			return "Now is the time you die, " + p.getDisplayName()
					+ "!";
		case 6:
			return "I claim " + p.getDisplayName() + " as my own!";
		case 7:
			return "" + p.getDisplayName() + " is mine!";
		case 8:
			return "Let me escort you to Edgeville, "
					+ p.getDisplayName() + "!";
		case 9:
			return "I have come for you, " + p.getDisplayName()
					+ "!";
		}
		return "";
	}

	@Override
	public boolean sendDeath() {
		player.setNextAnimation(new Animation(836));
		player.resetWalkSteps();
		player.lock(7);
		player.stopAll();
	       final NPC index = new NPC(2862, new WorldTile(
	                player.getX() + 1, player.getY() + 1, 0), -1, false);
			index.setNextAnimation(new Animation(380));
			index.setNextFaceEntity(player);
			index.setNextForceTalk(new ForceTalk(randomDeath(player)));
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 2) {
	                index.setFinished(true);
	                World.removeNPC(index);
					if (isRisk()) {
						Player killer = player
								.getMostDamageReceivedSourcePlayer();
						if (killer != null) {
							player.sendItemsOnDeath(killer, true);
							killer.increaseKillCount(player);
							System.out.println("FfaZone: "
									+ killer.getUsername() + " "
									+ player.getUsername() + "");
							System.out.println("FfaZone: " + killer == null);
						}
						player.getEquipment().init();
						player.getInventory().init();
					}
					player.setNextWorldTile(new WorldTile(3270, 3687, 0));
					player.getControlerManager().startControler(
							"clan_wars_request");
					player.reset();
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 3) {
					player.getPackets().sendMusic(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	}

	@Override
	public void sendInterfaces() {
		if (inPvpArea(player))
			showInterface();
	}

	public void showInterface() {
		player.getInterfaceManager()
				.sendTab(
						player.getInterfaceManager().hasRezizableScreen() ? 5
								: 1, 789);
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 38700:
			player.setNextWorldTile(new WorldTile(3270, 3687, 0));
			player.getControlerManager().startControler("clan_wars_request");
			return false;
		}
		return true;
	}

	@Override
	public void moved() {
		boolean inPvpArea = inPvpArea(player);
		boolean inSafeArea = inSafeArea(player);
		if (inPvpArea) {
			player.setCanPvp(true);
			if (!player.getInterfaceManager().containsInterface(789))
				showInterface();
			if (isRisk())
				Wilderness.checkBoosts(player);
		} else if (inSafeArea) {
			player.setCanPvp(false);
		} else if (!inSafeArea && !inPvpArea) {
			player.setCanPvp(false);
			removeIcon();
			removeControler();
		}
	}

	@Override
	public boolean keepCombating(Entity victim) {
		if (!(victim instanceof Player)) {
			return true;
		}
		return player.isCanPvp() && ((Player) victim).isCanPvp();
	}

	public void removeIcon() {
		player.setCanPvp(false);
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 11 : 0);
		player.getAppearence().generateAppearenceData();
		player.getEquipment().refresh(null);
	}

	@Override
	public void forceClose() {
		player.setCanPvp(false);
		player.getPackets().sendPlayerOption("null", 1, true);
		boolean resized = player.getInterfaceManager().hasRezizableScreen();
		player.getPackets().closeInterface(resized ? 746 : 548,
				resized ? 11 : 27);
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	/**
	 * Checks if the location is in a ffa pvp zone.
	 * 
	 * @param t
	 *            The world tile.
	 * @return {@code True} if so.
	 */
	public static boolean inPvpArea(Player player) {
		return (player.getX() >= 2948 && player.getX() <= 3071
				&& player.getY() >= 5512 && player.getY() <= 5631) // Risk area.
				|| (player.getX() >= 2756 && player.getX() <= 2879
						&& player.getY() >= 5512 && player.getY() <= 5631); // Safe
																			// area.
	}

	public static boolean inSafeArea(Player player) {
		return (player.getX() >= 2755 && player.getX() <= 2875 && player.getY() >= 5507 && player
				.getY() <= 5511) // Safe area
				|| (player.getX() >= 2947 && player.getX() <= 3069 && player.getY() >= 5507 && player
						.getY() <= 5511); // Risk area.
	}

	public static boolean inRiskArea(Player player) {
		if (player.getX() >= 2948 && player.getY() >= 5508
				&& player.getX() <= 3071 && player.getY() <= 5631)
			return true;
		return false;
	}

	/**
	 * Checks if the location is in a ffa zone.
	 * 
	 * @param t
	 *            The world tile.
	 * @return {@code True} if so.
	 */
	public static boolean inArea(WorldTile t) {
		return (t.getX() >= 2948 && t.getY() >= 5508 && t.getX() <= 3071 && t
				.getY() <= 5631) // Risk area.
				|| (t.getX() >= 2756 && t.getY() >= 5508 && t.getX() <= 2879 && t
						.getY() <= 5631); // Safe area.
	}

	/**
	 * Checks if a player's overload effect is changed (due to being in the risk
	 * ffa zone, in pvp)
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True} if so.
	 */
	public static boolean isOverloadChanged(Player player) {
		if (!(player.getControlerManager().getControler() instanceof FfaZone)) {
			return false;
		}
		return player.isCanPvp()
				&& ((FfaZone) player.getControlerManager().getControler())
						.isRisk();
	}

	public boolean isRisk() {
		return risk;
	}

	public void setRisk(boolean risk) {
		this.risk = risk;
	}
}