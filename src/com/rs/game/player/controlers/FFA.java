package com.rs.game.player.controlers;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Misc;

public class FFA extends Controler {


	@Override
	public void start() {
		player.setCanPvp(true);
		sendInterfaces();
	}
	
	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 5 : 1,
				789);
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		sendInterfaces();
		return false; // so doesnt remove script
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
		player.addStopDelay(7);
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
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
	                index.setFinished(true);
	                World.removeNPC(index);
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setCanPvp(false);
					teleportPlayer(player);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusic(90);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}


	@Override
	public void forceClose() {
		player.setCanPvp(false);
	}

	@Override
	public void moved() {
			if (!isInClanSafe(player)) {
				player.setCanPvp(false);
			} else 
				player.setCanPvp(true);
	}
	
	private void teleportPlayer(Player player) {
			player.setNextWorldTile(new WorldTile(3000, 9676, 0));
	}

	public static boolean isInClanSafe(Player player) {
		return player.getX() >= 2756 && player.getY() >= 5512
				&& player.getX() <= 2878 && player.getY() <= 5630;
	}
}