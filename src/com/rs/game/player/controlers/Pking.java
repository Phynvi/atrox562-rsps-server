package com.rs.game.player.controlers;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.area.PKWorld;
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
public final class Pking extends Controler {

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
        setRisk(true);
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
        return false;
    }

    public boolean isRisk() {
        return risk;
    }

    public void setRisk(boolean risk) {
        this.risk = risk;
    }
}