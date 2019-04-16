package com.rs.game.npc.hunter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.rs.game.Animation;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.cores.CoresManager;
import com.rs.game.WorldObject;
import com.rs.game.player.skills.hunter.PitfallHunter.PitfallPreys;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.player.Player;
import com.rs.game.ForceMovement;
import com.rs.game.npc.NPC;
import com.rs.game.WorldTile;

/**
 * @author Kris
 */
public class PitfallNPCs extends NPC {

	private static final long serialVersionUID = -4386620351809564335L;

	public PitfallNPCs(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	private static final HashMap<Player, WorldObject> TRAPS = new HashMap<Player, WorldObject>();

	public static void addPitfallTrap(WorldObject pitfall, Player player) {
		TRAPS.put(player, pitfall);
	}

	public static void removePitfallTrap(WorldObject pitfall, Player player) {
		TRAPS.remove(player, pitfall);
	}

	public WorldTile getWorldTile(WorldObject object) {
		if (object.getRotation() == 2) {
			if (this.getY() < object.getY())
				return new WorldTile(this.getX(), object.getY() + 2, object.getPlane());
			else
				return new WorldTile(this.getX(), object.getY() - 1, object.getPlane());
		} else if (object.getRotation() == 1) {
			if (this.getX() < object.getX())
				return new WorldTile(object.getX() + 2, this.getY(), object.getPlane());
			else
				return new WorldTile(object.getX() - 1, this.getY(), object.getPlane());
		} else if (object.getRotation() == 0) {
			if (this.getY() > object.getY())
				return new WorldTile(this.getX(), object.getY() - 2, object.getPlane());
			else
				return new WorldTile(this.getX(), object.getY() + 1, object.getPlane());
		} else {
			if (this.getX() > object.getX())
				return new WorldTile(object.getX() - 2, this.getY(), object.getPlane());
			else
				return new WorldTile(object.getX() + 1, this.getY(), object.getPlane());
		}
	}

	@Override
	public void processMovement() {
		super.processMovement();
		if (TRAPS.isEmpty())
			return;
		for (Map.Entry<Player, WorldObject> trap : TRAPS.entrySet()) {
			Player player = trap.getKey();
			WorldObject object = trap.getValue();
			final PitfallPreys info = PitfallPreys.forId(getId());
			if (withinDistance(object, 2)
					&& player.getVarBitManager().getBitValue(object.getDefinitions().varbit) == 1) {
				TRAPS.remove(player, object);
				if (HunterCore.calculateSuccess(player, info.getLevel())) {
					this.faceObject(object);
					this.setNextAnimation(new Animation(5231));
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							player.getVarBitManager().sendVarbit(object.getDefinitions().varbit, 3);
						}
					}, 1, TimeUnit.SECONDS);
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							reset();
							setLocation(getRespawnTile());
							finish();
							setRespawnTask();
						}
					}, 500, TimeUnit.MILLISECONDS);

				} else {
					this.setCantInteract(true);
					WorldTile destination = getWorldTile(object);
					NPC pitfall = this;
					WorldTasksManager.schedule(new WorldTask() {
						int ticks = 0;

						@Override
						public void run() {
							ticks++;
							if (ticks == 1) {
								pitfall.setNextAnimation(new Animation(5231));
								pitfall.setNextForceMovement(new ForceMovement(pitfall, 0, destination, 2, 1));
								player.getVarBitManager().sendVarbit(object.getDefinitions().varbit, 2);
							} else if (ticks == 2) {
								pitfall.setNextWorldTile(destination);
								player.getVarBitManager().sendVarbit(object.getDefinitions().varbit, 0);
								player.sendMessage("The pitfall trap that you constructed has collapsed.");
							} else if (ticks == 3) {
								pitfall.setTarget(player);
								setCantInteract(false);
								this.stop();
							}
						}
					}, 0, 1);
				}
			}
		}
	}
}