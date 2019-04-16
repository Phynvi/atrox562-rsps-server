package com.rs.game.minigames.bountyhunter;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import com.rs.cores.CoresManager;
import com.rs.game.player.Player;
import com.rs.game.WorldTile;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.net.decoders.handlers.ButtonHandler;

public class BountyHunter implements Serializable {
	
	private static final long serialVersionUID = 2011932556974180375L;
	
	public static boolean inBounty = false;
	public static long lootPenalty;
	private static int likelihood = 0;
	static final int TARGET_LIKELIHOOD_INCREASE = 10;
	private static int playerHunting;
	
	private transient Player player;
    private static transient Player target;
	
	static final int TARGET_COOLDOWN = 5 * 60; //5 minutes
	private long lastTarget = 0;
	
	private static CopyOnWriteArrayList<Player> handledPlayers = new CopyOnWriteArrayList<>();
	
	//TODO
	/*
	pickup penalty , 180 seconds after first loot
	*/
	
	public void setPlayer(Player player) {
        this.player = player;
    }
	
	/**
     * Does this player have a target?
     *
     * @return target == null
     */
    boolean hasTarget() {
        return target != null;
    }
	
	/**
     * Handle the player logging out
     */
    public void logout() {
        if (target != null) {
            target.getBountyHunter().removeTarget(true);
            removeTarget(false);
        }
        lastTarget = System.currentTimeMillis();
        likelihood = 0;
        BountyHunter.removeHandledPlayer(player);
    }
	
	/**
     * Remove a player from the handled hunters
     *
     * @param player the player
     */
    public static void removeHandledPlayer(Player player) {
        handledPlayers.remove(player);
        player.getBountyHunter().leaveBounty(player);
    }
	
	/**
     * Remove the players target
     *
     * @param loggedOut did the target logout?
     */
    public void removeTarget(boolean loggedOut) {
        if (loggedOut) {
            player.sendMessage("Your target has logged out. You will be assigned a new one shortly.");
            lastTarget = System.currentTimeMillis() - (long) (BountyHunter.TARGET_COOLDOWN * 0.3
                                                              * 1000); // 30% of the target delay
            likelihood = 50;
        } else {
            lastTarget = System.currentTimeMillis() - BountyHunter.TARGET_COOLDOWN * 1000;
            likelihood = 30;
        }
        sendInter(player);
        player.getHintIconsManager().removeUnsavedHintIcon();
        target = null;
    }
	
	/**
     * Check if the killed player was the target. You can add the rogue rewards here as well if you want.
     *
     * @param killed the killed player
     */
    public void checkKill(Player killed) {
        if (target == null) return;
        //I just put it to give 20 pkp for testing you can do whatever you want with it
        //You probably want to get the artifact ids and give some of those
        if (killed.getUsername().equalsIgnoreCase(target.getUsername())) {
            player.sendMessage("You have killed your target.");
            //player.setPvpPoints(player.getPvpPoints() + 20);
            target.getBountyHunter().removeTarget(false);
            player.getBountyHunter().removeTarget(false);
        }
    }

    /**
     * Set a target for this player
     *
     * @param target the target
     */
    void assignTarget(Player target) {
        this.target = target;
        player.getHintIconsManager().addHintIcon(target, 0, -1, false);
        sendInter(player);
    }
	
	private static void increaseLikelihood() {
        likelihood += TARGET_LIKELIHOOD_INCREASE;
    }
	
	/**
     * A player can only get a target every TARGET_COOLDOWN seconds.
     */
    boolean isOnTargetCooldown() {
        return (System.currentTimeMillis() - lastTarget) < TARGET_COOLDOWN * 1000;
    }
	
	/**
     * Process a player playing bounty hunter, (add targets, etc.)
     * Should get ran about once a minute
     *
     * @param player
     */
    private static void handleHunter(Player player) {
        if (player.getBountyHunter().inBounty == true) {
            //player.getBountyHunter().increaseLikelihood();
            if (!player.getBountyHunter().hasTarget() && !player.getBountyHunter().isOnTargetCooldown()) {
                findTarget(player);
            }
        } else {
            if (!player.getBountyHunter().hasTarget()) handledPlayers.remove(player);
        }
        player.getBountyHunter().sendInter(player);
    }
	
	/**
     * Are we handling this player? Used for adding the player to handled players on login etc.
     *
     * @param player the player
     * @return whether the player is being handled as a bounty hunter
     */
    public static boolean handlingPlayer(Player player) {
        return handledPlayers.contains(player);
    }
	
	/**
     * Attempt to locate a target for the player
     *
     * @param player the player
     */
    private static void findTarget(Player player) {
        for (Player player2 : handledPlayers) {
            if (player.getBountyHunter().inBounty == true && player2.getBountyHunter().inBounty == true) {
                player2.getBountyHunter().assignTarget(player);
                player.getBountyHunter().assignTarget(player2);
                return;
            }
        }
	}
	
	public static void addHandledPlayer(Player player) {
        handledPlayers.add(player);
    }
	
	public static void calculateWealth(Player player) {
		int riskedWealth = 0;
		int carriedWealth = 0;
		Integer[][] slots = ButtonHandler.getItemSlotsKeptOnDeath(player, true,
				true, player.getPrayer().usingPrayer(0, 10)
						|| player.getPrayer().usingPrayer(1, 0));
		Item[][] items = ButtonHandler.getItemsKeptOnDeath(player, slots);
		for (Item item : items[1])
			riskedWealth += item.getDefinitions().getTipitPrice() * item.getAmount();
		for (Item item : items[0])
			carriedWealth += item.getDefinitions().getTipitPrice() * item.getAmount();
		carriedWealth = carriedWealth + riskedWealth;
		if (carriedWealth >= 100000 && carriedWealth <= 499999) {
		player.setSkullInfiniteDelay(5);
		} else if (carriedWealth >= 500000 && carriedWealth <= 1100000) {
		player.setSkullInfiniteDelay(4);
		} else if (carriedWealth >= 1100000 && carriedWealth <= 2500000) {
		player.setSkullInfiniteDelay(3);
		} else if (carriedWealth > 2500000) {
		player.setSkullInfiniteDelay(2);
		}
	}

	public static void enterBounty(Player player) {
		inBounty = true;
		playerHunting += 1;
		addHandledPlayer(player);
		player.setNextWorldTile(new WorldTile(3113, 3685, 0));
		sendInter(player);
		player.setCanPvp(true);
		player.setAtMultiArea(true);
		player.setSkullInfiniteDelay(6);
		calculateWealth(player);
		if (playerHunting >= 2) {
                findTarget(player);
        }
		WorldTasksManager.schedule(new WorldTask() {

			int count = 180;

			@Override
			public void run() {
				if (count == 0) {
					increaseLikelihood();
					sendInter(player);
					count += 180;
				}
				count--;
			}
		}, 0, 2);
	}
	
	public static void leaveBounty(Player player) {
		inBounty = false;
		player.setNextWorldTile(new WorldTile(3164, 3685, 0));
		playerHunting -= 1;
		player.setCanPvp(false);
		player.closeInterfaces();
		player.setAtMultiArea(false);
		player.removeSkull();
	}
	
	public static void sendInter(Player player) {
		player.getInterfaceManager().sendOverlay(591);
		if (player.getBountyHunter().hasTarget()) {
		player.getPackets().sendIComponentText(591, 8, ""+ target.getUsername());
		} else {
		player.getPackets().sendIComponentText(591, 8, "None");
		}
		player.getPackets().sendConfig(1410, likelihood > 60 ? 60 : likelihood);
	}
}
