package com.rs.game.npc.hunter;

import java.util.List;

import com.rs.game.player.OwnedObjectManager;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.game.WorldObject;
import com.rs.game.World;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.game.WorldTile;
import com.rs.game.player.skills.hunter.BoxTrapping.BoxEntities;

@SuppressWarnings("serial")
public class BoxHunterNPCs extends NPC {

	public BoxHunterNPCs(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void processMovement() {
		super.processMovement();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final BoxEntities info = BoxEntities.forId(getId());
			for (final WorldObject object : objects) {
				if (object.getId() == (getId() == 708 ? 19223 : 19187)
						&& OwnedObjectManager.getOwner(object).getSkills().getLevel(Skills.HUNTER) >= BoxEntities
								.forId(this.getId()).getLevel()
						&& OwnedObjectManager.getOwner(object).getSkills().getLevel(Skills.SUMMONING) >= BoxEntities
								.forId(this.getId()).getSummoningRequirement()) {
					// WorldObject animatedObject = new WorldObject(28916, 10,
					// 0, this.getX(), this.getY(), this.getPlane());
					if (this.getX() == object.getX() && this.getY() == object.getY()) {
						if (HunterCore.calculateSuccess(OwnedObjectManager.getOwner(object), info.getLevel())) {
							if (OwnedObjectManager.convertIntoObject(object, new WorldObject(
									info.getSuccessfulTransformObjectId(), 10, 0, getX(), getY(), getPlane()))) {
								reset();
								setLocation(getRespawnTile());
								finish();
								setRespawnTask();
								/*
								 * CoresManager.slowExecutor.schedule(new
								 * Runnable() {
								 * 
								 * @Override public void run() { if
								 * (OwnedObjectManager.convertIntoObject(
								 * animatedObject, new WorldObject(info.
								 * getSuccessfulTransformObjectId(), 10, 0,
								 * getX(), getY(), getPlane()))); } }, 500,
								 * TimeUnit.MILLISECONDS);
								 */
							}
						} else {
							setNextAnimation(BoxEntities.forId(this.getId()).getFailCatchAnim());
							if (OwnedObjectManager.convertIntoObject(object,
									new WorldObject((getId() == 708 ? 19224 : 19192), 10, 0, getX(), getY(), getPlane()))) {
								/*
								 * CoresManager.slowExecutor.schedule(new
								 * Runnable() {
								 * 
								 * @Override public void run() { if
								 * (OwnedObjectManager.convertIntoObject(
								 * animatedObject, new WorldObject(19192, 10, 0,
								 * getX(), getY(), getPlane()))); } }, 500,
								 * TimeUnit.MILLISECONDS);
								 */
							}
						}
					}
				}
			}
		}
	}

}