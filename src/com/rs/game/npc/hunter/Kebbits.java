package com.rs.game.npc.hunter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.game.player.OwnedObjectManager;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.cores.CoresManager;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.game.player.Skills;
import com.rs.game.npc.NPC;
import com.rs.game.player.skills.hunter.DeadfallHunter.DeadfallPreys;

/**
 * @author Kris
 */
public class Kebbits extends NPC {

	private static final long serialVersionUID = -4386620351809564335L;

	public Kebbits(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	private void failedAttempt(WorldObject object) {
		setNextAnimation(new Animation(5275));
		WorldObject failedObject = new WorldObject(19219, object.getType(), object.getRotation(), object.getX(),
				object.getY(), object.getPlane());
		if (OwnedObjectManager.convertIntoObject(object, failedObject))
			;
		resetObject(failedObject);
	}

	private void resetObject(WorldObject object) {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				OwnedObjectManager.removeObject(OwnedObjectManager.getOwner(object), object);
				World.spawnObject(new WorldObject(19205, object.getType(), object.getRotation(), object.getX(),
						object.getY(), object.getPlane()), true);
			}
		}, 500, TimeUnit.MILLISECONDS);

	}

	private int getObject(WorldObject object, DeadfallPreys info) {
		if (object.getRotation() == 0)
			return this.getY() < object.getY() ? info.getSuccessfulObjectId() - 1 : info.getSuccessfulObjectId();
		else if (object.getRotation() == 1)
			return this.getX() < object.getX() ? info.getSuccessfulObjectId() - 1 : info.getSuccessfulObjectId();
		else if (object.getRotation() == 2)
			return this.getX() > object.getX() ? info.getSuccessfulObjectId() - 1 : info.getSuccessfulObjectId();
		else
			return this.getY() > object.getY() ? info.getSuccessfulObjectId() - 1 : info.getSuccessfulObjectId();
	}

	private int getPenguinObject(WorldObject object, DeadfallPreys info) {
		if (object.getRotation() == 0)
			return this.getY() < object.getY() ? info.getLootableObjectId() - 1 : info.getLootableObjectId();
		else if (object.getRotation() == 1)
			return this.getX() < object.getX() ? info.getLootableObjectId() - 1 : info.getLootableObjectId();
		else if (object.getRotation() == 2)
			return this.getX() > object.getX() ? info.getLootableObjectId() - 1 : info.getLootableObjectId();
		else
			return this.getY() > object.getY() ? info.getLootableObjectId() - 1 : info.getLootableObjectId();
	}

	@Override
	public void processMovement() {
		super.processMovement();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final DeadfallPreys info = DeadfallPreys.forId(getId());
			for (final WorldObject object : objects) {
				if (object.getId() == 19206 && OwnedObjectManager.getOwner(object).getSkills()
						.getLevel(Skills.HUNTER) >= DeadfallPreys.forId(this.getId()).getLevel()) {
					if (withinDistance(object, 1)) {
						if (HunterCore.calculateSuccess(OwnedObjectManager.getOwner(object), info.getLevel())) {
							this.faceObject(object);
							int id = getObject(object, info);
							WorldObject transformedTrap = new WorldObject(id, object.getType(), object.getRotation(),
									object.getX(), object.getY(), object.getPlane());
							if (OwnedObjectManager.convertIntoObject(object, transformedTrap)) {
								reset();
								setLocation(getRespawnTile());
								finish();
								setRespawnTask();
								transformObjectAgain(info, transformedTrap);
							}
						} else
							failedAttempt(object);
					}
				}
			}
		}
	}

	private void transformObjectAgain(DeadfallPreys info, WorldObject object) {
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if (info.getNpcId() != 5428)
					if (OwnedObjectManager.convertIntoObject(object, new WorldObject(info.getLootableObjectId(),
							object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane())))
						;
					else if (OwnedObjectManager.convertIntoObject(object, new WorldObject(getPenguinObject(object, info),
							object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane())))
						;
			}
		}, 500, TimeUnit.MILLISECONDS);

	}

}