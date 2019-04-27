package com.rs.game.area;

import com.rs.game.player.Player;

public class PKWorld {

    public static boolean checkPlayerTiles(Player player) {
        if (isAtSafeZone(player)) {
            player.setCanPvp(false);
            showInterface(player, false);
            player.sendMessage("You just entered at a safezone!");
            return true;
        } else {
            player.setCanPvp(true);
            showInterface(player, true);
            player.getControlerManager().startControler("Pking");
            player.sendMessage("Danger zone");
            player.sendMessage("ljkj");
            return false;
        }
    }

    public static final boolean isAtSafeZone(Player player) {
        return (AreaManager.get(player) != null && (AreaManager.get(player)
                .name().equalsIgnoreCase("GESafeZone")));

    }

    public static void showInterface(Player player, boolean enabled) {
        if (enabled) {
            player.getPackets().closeInterface(893);
            player.getPackets().sendIComponentText(892, 1, "<col=dbb81f>PvP</col>");
            player.getInterfaceManager()
                    .sendTab(
                            player.getInterfaceManager().hasRezizableScreen() ? 5
                                    : 1, 892);
        } else {
            player.getPackets().closeInterface(892);
            player.getPackets().sendIComponentText(893, 1, "<col=1fdb22>Safe</col>");
            player.getInterfaceManager()
                    .sendTab(
                            player.getInterfaceManager().hasRezizableScreen() ? 5
                                    : 1, 893);
        }
    }

    //edge
    public static boolean edgeBank(Player player) {
        return (player.getX() > 3090 && player.getY() < 3500
                && player.getX() < 3099 && player.getY() > 3487);
    }

    //grand exchange
    public static boolean grandExchange(Player player) {
        return (player.getX() > 3143 && player.getY() < 3513
                && player.getX() < 3186 && player.getY() > 3471);
    }

    //varrock west bank
    public static boolean varrockBank(Player player) {
        return (player.getX() > 3178 && player.getY() < 3447
                && player.getX() < 3195 && player.getY() > 3431);
    }

    //varrock east bank
    public static boolean varrockEastBank(Player player) {
        return (player.getX() > 3249 && player.getY() < 3426
                && player.getX() < 3259 && player.getY() > 3413);
    }

    //camelot
    public static boolean camelot(Player player) {
        return (player.getX() > 2718 && player.getY() < 3498
                && player.getX() < 2731 && player.getY() > 3486);
    }

    //cammy2
    public static boolean camelot2(Player player) {
        return (player.getX() > 2805 && player.getY() < 3446
                && player.getX() < 2813 && player.getY() > 3437);
    }

    //lummy
    public static boolean lumbridge(Player player) {
        return (player.getX() > 3201 && player.getY() < 3237
                && player.getX() < 3229 && player.getY() > 3200);
    }

    //fally
    public static boolean falador(Player player) {
        return (player.getX() > 2942 && player.getY() < 3374
                && player.getX() < 2950 && player.getY() > 3367);
    }

    //fally2
    public static boolean faladorEast(Player player) {
        return (player.getX() > 3008 && player.getY() < 3359
                && player.getX() < 3020 && player.getY() > 3352);
    }

    //ardy
    public static boolean ardy(Player player) {
        return (player.getX() > 2611 && player.getY() < 3336
                && player.getX() < 2622 && player.getY() > 3329);
    }

    //ardy2
    public static boolean ardy2(Player player) {
        return (player.getX() > 2648 && player.getY() < 3288
                && player.getX() < 2659 && player.getY() > 3279);
    }
}