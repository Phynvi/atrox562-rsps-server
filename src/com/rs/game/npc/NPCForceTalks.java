package com.rs.game.npc;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.utils.Utils;

public class NPCForceTalks {

	static String[] ducks = { "Quack!", "Qwack!" };
	static String[] cows = { "Mooo!", "Moooooo!", "Moooo!" };
	static String[] chickens = { "Bwuk!", "Bwuk bwuk!", "Bwuk bwuk bwuk!" };
	static String[] sheep = { "Baa!", "Baaaa!", "Meh!" };

	public static final void forceTalkTask() {
		CoresManager.fastExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				for (NPC npc : World.getNPCs()) {
					if (npc.isDead() || npc == null)
						continue;
					if (npc.getName().contains("Duck") || npc.getName().equals("Duck")) {
						if (Utils.getRandom(100) > 85)
							npc.setNextForceTalk(new ForceTalk(ducks[Utils.getRandom(ducks.length - 1)]));
					} else if (npc.getName().contains("Cow") || npc.getName().equals("Cow")) {
						if (Utils.getRandom(100) > 90)
							npc.setNextForceTalk(new ForceTalk(cows[Utils.getRandom(cows.length - 1)]));
					} else if (npc.getName().equals("Chicken")) {
						if (Utils.getRandom(100) > 90)
							npc.setNextForceTalk(new ForceTalk(chickens[Utils.getRandom(chickens.length - 1)]));
					} else if (npc.getName().equals("Sheep") || npc.getName().equals("Ram")) {
						if (Utils.getRandom(100) > 85)
							npc.setNextForceTalk(new ForceTalk(sheep[Utils.getRandom(sheep.length - 1)]));
					}
				}
			}
		}, 0, 2, TimeUnit.SECONDS);
	}
}
