package com.rs.game.npc.hunter;

import java.util.List;

import com.rs.game.npc.NPC;
import com.rs.game.player.skills.hunter.BirdSnaring.Birds;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.Animation;
import com.rs.game.player.Skills;
import com.rs.game.World;

@SuppressWarnings("serial")
public class BirdSnaringNPCs extends NPC {

	public BirdSnaringNPCs(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	private void failedAttempt(WorldObject object) {
		setNextAnimation(new Animation(6774));
		if (OwnedObjectManager.convertIntoObject(object, new WorldObject(19174, 10, 0, this.getX(), this.getY(), this.getPlane())));
	}

	@Override
	public void processMovement() {
		super.processMovement();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final Birds info = Birds.forId(getId());
			for (final WorldObject object : objects) {
				if (object.getId() == 19175 && OwnedObjectManager.getOwner(object).getSkills()
						.getLevel(Skills.HUNTER) >= Birds.forId(this.getId()).getLevel()) {
					if (this.getX() == object.getX() && this.getY() == object.getY()) {
						if (HunterCore.calculateSuccess(OwnedObjectManager.getOwner(object), info.getLevel())) {
							if (OwnedObjectManager.convertIntoObject(object,
									new WorldObject(info.getSuccessfulTransformObjectId(), 10, 0, this.getX(),
											this.getY(), this.getPlane()))) {
								reset();
								setLocation(getRespawnTile());
								finish();
								setRespawnTask();
							}
						} else
							failedAttempt(object);
					}
				}
			}
		}
	}
}