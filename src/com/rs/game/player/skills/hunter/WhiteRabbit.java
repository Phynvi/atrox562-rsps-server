package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.cores.CoresManager;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;
import com.rs.game.player.Skills;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.WorldObject;
import com.rs.game.World;

public class WhiteRabbit extends Action {
	
	public enum Paths {
		NORTH_ONE(new WorldTile[] { new WorldTile(2329, 3536, 0), new WorldTile(2329, 3533, 0), new WorldTile(2331, 3533, 0), new WorldTile(2331, 3534, 0), new WorldTile(2332, 3534, 0), new WorldTile(2332, 3534, 0), new WorldTile(2336, 3535, 0) }),
		NORTH_TWO(new WorldTile[]  { new WorldTile(2329, 3536, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2338, 3530, 0) }),
		NORTH_THREE(new WorldTile[]  { new WorldTile(2329, 3536, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2336, 3530, 0), new WorldTile(2336, 3526, 0) }),
		NORTH_FOUR(new WorldTile[] { new WorldTile(2329, 3536, 0), new WorldTile(2328, 3530, 0), new WorldTile(2328, 3525, 0) }),
		NORTH_FIVE(new WorldTile[] { new WorldTile(2329, 3536, 0), new WorldTile(2329, 3533, 0), new WorldTile(2324, 3533, 0) }),
		NORTH_EAST_ONE(new WorldTile[] { new WorldTile(2336, 3535, 0), new WorldTile(2333, 3535, 0), new WorldTile(2332, 3534, 0), new WorldTile(2331, 3534, 0), new WorldTile(2331, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3536, 0) }),
		NORTH_EAST_TWO(new WorldTile[] { new WorldTile(2336, 3535, 0), new WorldTile(2333, 3535, 0), new WorldTile(2332, 3534, 0), new WorldTile(2331, 3534, 0), new WorldTile(2331, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2338, 3530, 0) }),
		NORTH_EAST_THREE(new WorldTile[] { new WorldTile(2336, 3535, 0), new WorldTile(2333, 3535, 0), new WorldTile(2332, 3534, 0), new WorldTile(2331, 3534, 0), new WorldTile(2331, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2336, 3530, 0), new WorldTile(2336, 3526, 0) }),
		NORTH_EAST_FOUR(new WorldTile[] { new WorldTile(2336, 3535, 0), new WorldTile(2333, 3535, 0), new WorldTile(2332, 3534, 0), new WorldTile(2331, 3534, 0), new WorldTile(2331, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3531, 0), new WorldTile(2328, 3530, 0), new WorldTile(2328, 3525, 0) }),
		NORTH_EAST_FIVE(new WorldTile[] { new WorldTile(2336, 3535, 0), new WorldTile(2333, 3535, 0), new WorldTile(2332, 3534, 0), new WorldTile(2331, 3534, 0), new WorldTile(2331, 3533, 0), new WorldTile(2324, 2533, 0) }),
		EAST_ONE(new WorldTile[] { new WorldTile(2338, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3536, 0) }),
		EAST_TWO(new WorldTile[] { new WorldTile(2338, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3533, 0), new WorldTile(2330, 3533, 0), new WorldTile(2331, 3534, 0), new WorldTile(2332, 3534, 0), new WorldTile(2333, 3535, 0), new WorldTile(2336, 3535, 0) }),
		EAST_THREE(new WorldTile[] { new WorldTile(2338, 3530, 0), new WorldTile(2336, 3530, 0), new WorldTile(2336, 3526, 0) }),
		EAST_FOUR(new WorldTile[] { new WorldTile(2338, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2328, 3530, 0), new WorldTile(2328, 3525, 0) }),
		EAST_FIVE(new WorldTile[] { new WorldTile(2338, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3533, 0), new WorldTile(2324, 3533, 0) }),
		SOUTH_EAST_ONE(new WorldTile[] { new WorldTile(2336, 3526, 0), new WorldTile(2336, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3536, 0) }),
		SOUTH_EAST_TWO(new WorldTile[] { new WorldTile(2336, 3526, 0), new WorldTile(2336, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3533, 0), new WorldTile(2330, 3533, 0), new WorldTile(2331, 3534, 0), new WorldTile(2332, 3534, 0), new WorldTile(2333, 3535, 0), new WorldTile(2336, 3535, 0) }),
		SOUTH_EAST_THREE(new WorldTile[] { new WorldTile(2336, 3526, 0), new WorldTile(2336, 3530, 0), new WorldTile(2338, 3530, 0) }),
		SOUTH_EAST_FOUR(new WorldTile[] { new WorldTile(2336, 3526, 0), new WorldTile(2336, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2328, 3530, 0), new WorldTile(2328, 3525, 0) }),
		SOUTH_EAST_FIVE(new WorldTile[] { new WorldTile(2336, 3526, 0), new WorldTile(2336, 3530, 0), new WorldTile(2334, 3530, 0), new WorldTile(2333, 3531, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3533, 0), new WorldTile(2324, 3533, 0) }),
		SOUTH_ONE(new WorldTile[] { new WorldTile(2328, 3525, 0), new WorldTile(2328, 3530, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3536, 0) }),
		SOUTH_TWO(new WorldTile[] { new WorldTile(2328, 3525, 0), new WorldTile(2328, 3530, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3533, 0), new WorldTile(2330, 3533, 0), new WorldTile(2331, 3534, 0), new WorldTile(2332, 3534, 0), new WorldTile(2333, 3535, 0), new WorldTile(2336, 3535, 0) }),
		SOUTH_THREE(new WorldTile[] { new WorldTile(2328, 3525, 0), new WorldTile(2328, 3530, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2338, 3530, 0) }),
		SOUTH_FOUR(new WorldTile[] { new WorldTile(2328, 3525, 0), new WorldTile(2328, 3530, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2336, 3530, 0), new WorldTile(2336, 3526, 0) }),
		SOUTH_FIVE(new WorldTile[] { new WorldTile(2328, 3525, 0), new WorldTile(2328, 3530, 0), new WorldTile(2329, 3531, 0), new WorldTile(2329, 3533, 0), new WorldTile(2324, 3533, 0) }),
		WEST_ONE(new WorldTile[] { new WorldTile(2324, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3536, 0) }),
		WEST_TWO(new WorldTile[] { new WorldTile(2324, 3533, 0), new WorldTile(2330, 3533, 0), new WorldTile(2331, 3534, 0), new WorldTile(2334, 3534, 0), new WorldTile(2333, 3535, 0), new WorldTile(2336, 3535, 0) }),
		WEST_THREE(new WorldTile[] { new WorldTile(2324, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2338, 3530, 0) }),
		WEST_FOUR(new WorldTile[] { new WorldTile(2324, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3531, 0), new WorldTile(2333, 3531, 0), new WorldTile(2334, 3530, 0), new WorldTile(2336, 3530, 0), new WorldTile(2336, 3526, 0) }),
		WEST_FIVE(new WorldTile[] { new WorldTile(2324, 3533, 0), new WorldTile(2329, 3533, 0), new WorldTile(2329, 3531, 0), new WorldTile(2328, 3530, 0), new WorldTile(2328, 3525, 0) });
		
		private static final HashMap<Integer, Paths> PATHS = new HashMap<Integer, Paths>();

		static {
			for (Paths p : values()) {
				PATHS.put(p.ordinal(), p);
			}
		}

		public static Paths getPath(int id) {
			return PATHS.get(id);
		}
		
		private WorldTile[] WorldTiles;
		
		Paths(WorldTile[] WorldTiles) {
			this.WorldTiles = WorldTiles;
		}
		
		public WorldTile[] getWorldTiles() {
			return WorldTiles;
		}
	}
	
	public static void sendRandomRabbit() {
		List<WorldObject> objects = World.getRegion(9271).getSpawnedObjects();
		Paths path = Paths.getPath(Utils.random(29));
		NPC rabbit = new NPC(1530, path.getWorldTiles()[0], -1, false);
		rabbit.setCantInteract(true);
		rabbit.setRun(true);
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i < path.getWorldTiles().length; i++) {
					do {
						if (rabbit.hasFinished())
							break;
						rabbit.addWalkSteps(path.getWorldTiles()[i].getX(), path.getWorldTiles()[i].getY());
					} while (rabbit.hasFinished() || rabbit.getX() == path.getWorldTiles()[i - 1].getX() && rabbit.getY() == path.getWorldTiles()[i - 1].getY());
				}
				while (true) {
					if (!rabbit.hasFinished()) {
					if (objects != null) {
						for (final WorldObject object : objects) {
							if (object.getId() == 19333 && rabbit.getX() == object.getX() && rabbit.getY() == object.getY()) {
								Player player = OwnedObjectManager.getOwner(object);
								if (HunterCore.calculateSuccess(player, player.getSkills().getLevel(Skills.HUNTER))) {
									if (OwnedObjectManager.convertIntoObject(object, new WorldObject(19335, 10, 0, rabbit.getX(), rabbit.getY(), rabbit.getPlane()))) {
										rabbit.finish();
										break;
									}
								} else {
									if (OwnedObjectManager.convertIntoObject(object, new WorldObject(19334, 10, 0, object.getX(), object.getY(), object.getPlane())));
								}
							}
						}
					} 
				}
					if (rabbit.getX() == path.getWorldTiles()[path.getWorldTiles().length - 1].getX() && rabbit.getY() == path.getWorldTiles()[path.getWorldTiles().length - 1].getY()) {
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							rabbit.finish();
						}
					}, 300, TimeUnit.MILLISECONDS);
						break;
					}	
			}
			}
		}, 200, TimeUnit.MILLISECONDS);
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

	@Override
	public boolean start(Player player) {
		int trapAmt = getTrapAmount(player);
		if (OwnedObjectManager.getObjectsforValue(player, 19333) == trapAmt) {
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
		player.getInventory().deleteItem(10031, 1);
		new OwnedObjectManager(player, new WorldObject[] { new WorldObject(19333, 10, 0, player.getX(), player.getY(), player.getPlane()) }, 600000);
		return -1;
	}

	@Override
	public void stop(Player player) {}

}
