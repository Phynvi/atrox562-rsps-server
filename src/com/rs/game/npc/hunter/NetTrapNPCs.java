package com.rs.game.npc.hunter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.game.player.OwnedObjectManager;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.cores.CoresManager;
import com.rs.game.WorldObject;
import com.rs.game.player.skills.hunter.NetTrapping.NetPreys;
import com.rs.game.player.Skills;
import com.rs.game.npc.NPC;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.game.Animation;

/**
 * @author Kris
 */
public class NetTrapNPCs extends NPC {

	private static final long serialVersionUID = -4386620351809564335L;

	public NetTrapNPCs(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	private void failedAttempt(WorldObject object) {
		WorldTile tile = getLocation(object);
		setNextAnimation(new Animation(5275));
		WorldObject failedObject = new WorldObject(19676, object.getType(), object.getRotation(), tile.getX(), tile.getY(), tile.getPlane());
		if (OwnedObjectManager.convertIntoObject(World.getObject(object, 10), failedObject)) {
			OwnedObjectManager.removeObject(OwnedObjectManager.getOwner(object), object);
		}
		resetObject(failedObject);
	}

	private void resetObject(WorldObject object) {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				OwnedObjectManager.removeObject(OwnedObjectManager.getOwner(object), object);
				World.spawnObject(new WorldObject(19677, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), true);
			}
		}, 500, TimeUnit.MILLISECONDS);

	}
	
	private WorldTile getLocation(WorldObject object) {
		switch(object.getRotation()) {
		case 0:
			return new WorldTile(object.getX(), object.getY() - 1, object.getPlane());
		case 1:
			return new WorldTile(object.getX() - 1, object.getY(), object.getPlane());
		case 2:
			return new WorldTile(object.getX(), object.getY() + 1, object.getPlane());
		default:
			return new WorldTile(object.getX() + 1, object.getY(), object.getPlane());
		}
	}

	@Override
	public void processMovement() {
		super.processMovement();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final NetPreys info = NetPreys.forId(getId());
			for (final WorldObject object : objects) {
				if (object.getId() == 19673 && OwnedObjectManager.getOwner(object).getSkills().getLevel(Skills.HUNTER) >= NetPreys.forId(this.getId()).getLevel()) {
					if (object.getX() == this.getX() && object.getY() == this.getY() && object.getPlane() == this.getPlane()) {
						if (HunterCore.calculateSuccess(OwnedObjectManager.getOwner(object), info.getLevel())) {
							WorldTile location = getLocation(object);
							WorldObject transformedTrap = new WorldObject(info.getSuccessfulObjectId(), object.getType(), object.getRotation(), location.getX(), location.getY(), location.getPlane());
							if (OwnedObjectManager.convertIntoObject(World.getObject(location, 10), transformedTrap)) {
								reset();
								setLocation(getRespawnTile());
								finish();
								setRespawnTask();
								transformObjectAgain(info, transformedTrap);
								OwnedObjectManager.removeObject(OwnedObjectManager.getOwner(object), object);
							}
						} else
							failedAttempt(object);
					}
				}
			}
		}
	}

	private void transformObjectAgain(NetPreys info, WorldObject object) {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
					if (OwnedObjectManager.convertIntoObject(object, new WorldObject(info.getLootableObjectId(), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane())));
			}
		}, 500, TimeUnit.MILLISECONDS);

	}

}