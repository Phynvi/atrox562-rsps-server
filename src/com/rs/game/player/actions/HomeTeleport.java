package com.rs.game.player.actions;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.utils.Utils;

public class HomeTeleport extends Action {

        protected static final int HOME_ANIMATION = 1722;
        protected static final int HOME_GRAPHIC = 3017;
        protected static final int DONE_ANIMATION = 1725; 

        private int currentTime;
        private WorldTile tile;

        @Override
        public boolean start(final Player player) {
                tile = Settings.RESPAWN_PLAYER_LOCATION;
                if (!player.getControlerManager().processMagicTeleport(tile))
                        return false;
                return process(player);
        }

        @Override
        public int processWithDelay(Player player) {
                player.getWalkSteps().clear();
                if (currentTime++ == 0) {
        			player.setNextAnimation(new Animation(1722));
        			player.setNextGraphics(new Graphics(775));
        			player.getPackets().sendSound(193, 0);
                }else if (currentTime == 1) {
                    player.setNextAnimation(new Animation(1723));
                    player.setNextGraphics(new Graphics(800));
                }else if (currentTime == 2) {
                    player.setNextAnimation(new Animation(1724));
                    player.setNextGraphics(new Graphics(801));
                }else if (currentTime == 3) {
                    player.setNextAnimation(new Animation(1725));
                    player.setNextGraphics(new Graphics(802));
                }else if (currentTime == 4) {
                    player.setNextAnimation(new Animation(2798));
                    player.setNextGraphics(new Graphics(803));
                }else if (currentTime == 5) {
                    player.setNextAnimation(new Animation(2799));
                    player.setNextGraphics(new Graphics(804));
                    player.getPackets().sendSound(194, 0);
                }else if (currentTime == 6) {
                    player.setNextAnimation(new Animation(2800));
                    player.setNextGraphics(new Graphics(1703));
                }else if (currentTime == 7) {
                    player.setNextAnimation(new Animation(3195));
                    player.setNextGraphics(new Graphics(1704));
                }else if (currentTime == 8) {
                    player.setNextAnimation(new Animation(4643));
                    player.setNextGraphics(new Graphics(1705));
                }else if (currentTime == 9) {
                    player.setNextAnimation(new Animation(4645));
                    player.setNextGraphics(new Graphics(1706));
                }else if (currentTime == 10) {
                    player.setNextAnimation(new Animation(4646));
                    player.setNextGraphics(new Graphics(1707));
                }else if (currentTime == 11) {
                    player.setNextAnimation(new Animation(4847));
                    player.setNextGraphics(new Graphics(1708));
                }else if (currentTime == 12) {
                    player.setNextAnimation(new Animation(4848));
                    player.setNextGraphics(new Graphics(1709));
                }else if (currentTime == 13) {
                    player.setNextAnimation(new Animation(4849));
                    player.setNextGraphics(new Graphics(1710));
                }else if (currentTime == 14) {
                    player.setNextAnimation(new Animation(4850));
                    player.setNextGraphics(new Graphics(1711));
                }else if (currentTime == 15) {
                    player.setNextAnimation(new Animation(4851));
                    player.setNextGraphics(new Graphics(1712));
                    player.getPackets().sendSound(196, 0);
                }else if (currentTime == 16) {
                    player.setNextAnimation(new Animation(4852));
                    player.setNextGraphics(new Graphics(1713));
                } else if (currentTime == 17) {
                    player.setNextWorldTile(tile.transform(0, 1, 0));
                    player.getControlerManager().magicTeleported(Magic.MAGIC_TELEPORT);
                    if (player.getControlerManager().getControler() == null)
                        Magic.teleControlersCheck(player, tile);
                    player.setNextFaceWorldTile(new WorldTile(tile.getX(), tile.getY(),
                            tile.getPlane()));
                    player.setDirection(6);
                } else if (currentTime == 18) {
                    player.setNextAnimation(new Animation(-1));
                } else if (currentTime == 24)
                    return -1;
                return 0;
            }

        @Override
        public boolean process(Player player) {
                if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
                        player.getPackets()
                                        .sendGameMessage(
                                                        "You can't home teleport until 10 seconds after the end of combat.");
                        return false;
                }
                return true;
        }

        @Override
        public void stop(Player player) {
        }

}