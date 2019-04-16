package com.rs.game.map.doors;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.player.Player;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.World;

public class Gates {

	private static final HashMap<WorldObject[], WorldObject[]> GATES = new HashMap<WorldObject[], WorldObject[]>();

	private static WorldTile[] getTile(WorldObject object) {
		switch (object.getRotation()) {
		case 0:
			return new WorldTile[] { new WorldTile(object.getX() - 1, object.getY(), object.getPlane()),
					new WorldTile(object.getX() - 2, object.getY(), object.getPlane()) };
		case 1:
			return new WorldTile[] { new WorldTile(object.getX() - 1, object.getY() + 2, object.getPlane()),
					new WorldTile(object.getX() - 1, object.getY() + 1, object.getPlane()) };
		case 2:
			return new WorldTile[] { new WorldTile(object.getX() + 1, object.getY() + 2, object.getPlane()),
					new WorldTile(object.getX() + 2, object.getY() + 2, object.getPlane()) };
		case 3:
			return new WorldTile[] { new WorldTile(object.getX(), object.getY() + 1, object.getPlane()),
					new WorldTile(object.getX(), object.getY(), object.getPlane()) };
		default:
			return new WorldTile[] { new WorldTile(object.getX(), object.getY(), object.getPlane()),
					new WorldTile(object.getX(), object.getY(), object.getPlane()) };
		}
	}

	public static boolean handleGate(Player player, WorldObject object) {
		if (object.getId() == 1596 || object.getId() == 1597)
			return false;
		boolean open = false;
		for (Map.Entry<WorldObject[], WorldObject[]> entry : GATES.entrySet()) {
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
				GATES.remove(firstSet, secondSet);
				open = true;
				return true;
			}
		}
		if (!open) {
			WorldObject primaryGate = null, secondaryGate = null;
			for (WorldObject obj : World.getRegion(player.getRegionId()).getAllObjects()) {
				if (obj == null)
					continue;
				if (obj.getDefinitions().getName().equalsIgnoreCase("Gate")) {
					if (!player.withinDistance(obj, 5))
						continue;
					if (primaryGate == null)
						primaryGate = obj;
					else if (secondaryGate == null)
						secondaryGate = obj;
				}
			}
			if (primaryGate == null || secondaryGate == null)
				return false;
			World.removeObject(primaryGate, true);
			World.removeObject(secondaryGate, true);
			WorldObject newGate1 = new WorldObject(GameEntrances.getGateId(primaryGate), primaryGate.getType(), primaryGate.getRotation() == 0 ? 3 : primaryGate.getRotation() + 1, getTile(primaryGate)[0]);
			WorldObject newGate2 = new WorldObject(GameEntrances.getGateId(secondaryGate), secondaryGate.getType(), secondaryGate.getRotation() == 0 ? 3 : secondaryGate.getRotation() + 1, getTile(primaryGate)[1]);
			GATES.put(new WorldObject[] { newGate1, newGate2 }, new WorldObject[] { primaryGate, secondaryGate });
			World.spawnObject(newGate1, true);
			World.spawnObject(newGate2, true);
			return true;
		}
		return false;
	}

}
