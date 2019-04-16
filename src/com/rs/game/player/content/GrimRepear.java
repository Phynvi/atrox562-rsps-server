package com.rs.game.player.content;

import java.util.Calendar;

import java.util.GregorianCalendar;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Misc;


//GrimReaper.handleDeathsActions(p);
public class GrimRepear {
	static Player player;


	/**
	 * String contains the data for the random messages that the grim reaper
	 * says when he spawns.
	 */
	
	public static final String[] DIALOGUE = {
			"There is no escape, " + player.getDisplayName()
			+ "...", "You belong to me!", 
			"I claim " + player.getDisplayName() + " as my own!",
			"I have come for you, " + player.getDisplayName()
			+ "!"
			};

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

	/**
	 * This void contains the data so that death will only spawn in October
	 * after the day of the 25.
	 */

	public static void handleDeathsActions(Player p) {
		Calendar cal = new GregorianCalendar();
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (month == 9 && day > 25) {
				spawnDeath(p);
			}
		
	}

	/**
	 * This void contains the data so that when death spawns he will say
	 * something from the string above.
	 */

	public static void handleChat(NPC npc) {
		final Player instance = player;
	npc.setNextForceTalk(new ForceTalk(randomDeath(player)));
	
			}
	

	/**
	 * This void contains the data so that death will spawn only when you die.
	 */

	public static void spawnDeath(Player p) {
       final NPC index = new NPC(2862, new WorldTile(
                p.getX() + 1, p.getY() + 1, 0), -1, false);
		index.setNextAnimation(new Animation(405));
		index.setNextFaceEntity(p);
		//handleChat(index);
	/*	WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
                index.setFinished(true);
                World.removeNPC(index);
                stop();
			}
		}, 1200);*/
	}
}