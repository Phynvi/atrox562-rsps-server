package com.rs.game.map.doors;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.player.Player;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.World;

public class DoubleDoors {

	private static HashMap<WorldObject[], WorldObject[]> doubleDoors = new HashMap<WorldObject[], WorldObject[]>();

	private static WorldTile[] getTile(WorldObject object) {
		switch (object.getRotation()) {
		case 0:
			return new WorldTile[] { new WorldTile(object.getX(), object.getY() + 1, object.getPlane()),
					new WorldTile(object.getX() + 1, object.getY() + 1, object.getPlane()) };
		case 1:
			return new WorldTile[] { new WorldTile(object.getX() - 1, object.getY() + 2, object.getPlane()),
					new WorldTile(object.getX() - 1, object.getY() + 1, object.getPlane()) };
		case 2:
			return new WorldTile[] { new WorldTile(object.getX() + 1, object.getY() + 2, object.getPlane()),
					new WorldTile(object.getX() + 2, object.getY() + 2, object.getPlane()) };
		case 3:
			return new WorldTile[] { new WorldTile(object.getX() + 1, object.getY(), object.getPlane()),
					new WorldTile(object.getX(), object.getY(), object.getPlane()) };
		default:
			return new WorldTile[] { new WorldTile(object.getX(), object.getY(), object.getPlane()),
					new WorldTile(object.getX(), object.getY(), object.getPlane()) };
		}
	}

	public static boolean handleDoor(Player player, WorldObject object) {
		boolean open = false;
		for (Map.Entry<WorldObject[], WorldObject[]> entry : doubleDoors.entrySet()) {
			WorldObject[] firstSet = entry.getKey();
			WorldObject[] secondSet = entry.getValue();
			if (firstSet[0] == null || firstSet[1] == null)
				continue;
			if (firstSet[0].getId() == object.getId() && firstSet[0].getType() == object.getType()
					&& firstSet[0].getRotation() == object.getRotation() && firstSet[0].getX() == object.getX()
					&& firstSet[0].getY() == object.getY() && firstSet[0].getPlane() == object.getPlane()
					|| firstSet[1].getId() == object.getId() && firstSet[1].getType() == object.getType()
							&& firstSet[1].getRotation() == object.getRotation() && firstSet[1].getX() == object.getX()
							&& firstSet[1].getY() == object.getY() && firstSet[1].getPlane() == object.getPlane()) {
				World.removeObject(firstSet[1], true);
				World.removeObject(firstSet[0], true);
				World.spawnObject(secondSet[0], true);
				World.spawnObject(secondSet[1], true);
				doubleDoors.remove(firstSet, secondSet);
				open = true;
				return true;
			}
		}
		if (!open) {
			WorldObject primaryDoor = null, secondaryDoor = null;
			for (WorldObject obj : World.getRegion((player.getRegionId() == 12860 || player.getRegionId() == 11836)
					? player.getRegionId() + 1 : player.getRegionId()).getAllObjects()) {
				if (obj == null)
					continue;
				if (isDoubleDoor(obj)) {
					if (!player.withinDistance(obj, 5))
						continue;
					if (primaryDoor == null)
						primaryDoor = obj;
					else if (secondaryDoor == null)
						secondaryDoor = obj;
				}
			}
			if (primaryDoor == null || secondaryDoor == null)
				return false;
			World.removeObject(primaryDoor, true);
			World.removeObject(secondaryDoor, true);
			WorldObject newDoor1 = new WorldObject(primaryDoor.getId(), primaryDoor.getType(),
					primaryDoor.getRotation() - 1, getTile(primaryDoor)[0]);
			WorldObject newDoor2 = new WorldObject(secondaryDoor.getId(), secondaryDoor.getType(),
					secondaryDoor.getRotation() + 1, getTile(primaryDoor)[1]);
			doubleDoors.put(new WorldObject[] { newDoor1, newDoor2 }, new WorldObject[] { primaryDoor, secondaryDoor });
			World.spawnObject(newDoor1, true);
			World.spawnObject(newDoor2, true);
			return true;
		}
		return false;
	}

	public static boolean isDoubleDoor(WorldObject object) {
		switch (object.getId()) {
		case 1596:
		case 1597:
		case 25825:
		case 25826:
		case 25827:
		case 25828:
		case 15641:
		case 15644:
			return true;
		default:
			return false;
		}
	}

}
