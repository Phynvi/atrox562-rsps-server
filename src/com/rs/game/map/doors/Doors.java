package com.rs.game.map.doors;

import java.util.ArrayList;

import com.rs.game.WorldObject;
import com.rs.game.World;

/**
 * Manages all the doors in the game.
 * 
 * @author Kris
 *
 */
public class Doors {

	/**
	 * Constructs a new door.
	 * 
	 * @param door
	 *            The door we're managing.
	 */
	private Doors(WorldObject door) {
		this.object = door;
		this.originalId = object.getId();
		this.currentId = object.getId();
		this.originalX = object.getX();
		this.originalY = object.getY();
		this.currentX = originalX;
		this.currentY = originalY;
		this.originalRotation = object.getRotation();
		this.currentRotation = originalRotation;
		this.open = object.getDefinitions().getOption(1).equalsIgnoreCase("close");
	}

	/**
	 * The door we're managing.
	 */
	private WorldObject object;

	/**
	 * The doors original object id.
	 */
	private int originalId;

	/**
	 * The doors current object id.
	 */
	private int currentId;

	/**
	 * The doors original x coordinate.
	 */
	private int originalX;

	/**
	 * The doors original y coordinate.
	 */
	private int originalY;

	/**
	 * The doors current x coordinate.
	 */
	private int currentX;

	/**
	 * The doors current y coordinate.
	 */
	private int currentY;

	/**
	 * The doors original rotation.
	 */
	private int originalRotation;

	/**
	 * The doors current rotation.
	 */
	private int currentRotation;

	/**
	 * Is the door open?
	 */
	private boolean open;

	/**
	 * An array of all the doors.
	 */
	private static ArrayList<Doors> doors = new ArrayList<Doors>();

	/**
	 * An array list of special doors (these doors don't unlock at all).
	 */
	private static final int[] SpecialDoors = { 2069, 3014 };

	/**
	 * Gets the door to manage.
	 * 
	 * @param door
	 *            The door to manage.
	 * @return The <code>Doors</code>.
	 */
	private static Object[] getDoor(WorldObject door) {
		for (Doors d : doors) {
			if (d != null)
				if (d.currentId == door.getId() && d.currentX == door.getX() && d.currentY == door.getY())
					return new Object[] { d, true };
		}
		Doors d = new Doors(door);
		doors.add(d);
		return new Object[] { d, false };
	}

	/**
	 * Manages a door.
	 * 
	 * @param door
	 *            The door we're managing.
	 */
	public static void manageDoor(WorldObject door) {
		Object[] o = getDoor(door);
		Doors d = (Doors) o[0];
		boolean existing = (boolean) o[1];
		if (d == null)
			return;
		int xAdjustment = 0, yAdjustment = 0;
		for (int i = 0; i < SpecialDoors.length; i++) {
			if (d.originalId == SpecialDoors[i])
				return;
		}
		if (d.object.getType() == 0) {
			if (!d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					xAdjustment = -1;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					yAdjustment = 1;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					xAdjustment = 1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					yAdjustment = -1;
				}
			} else if (d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					yAdjustment = 1;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					xAdjustment = 1;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					yAdjustment = -1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					xAdjustment = -1;
				}
			}
		} else if (d.object.getType() == 9) {
			if (!d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					xAdjustment = 1;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					xAdjustment = 1;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					xAdjustment = -1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					xAdjustment = -1;
				}
			} else if (d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					xAdjustment = 1;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					xAdjustment = 1;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					xAdjustment = -1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					xAdjustment = -1;
				}
			}
		}
		if (d.originalX == d.currentX && d.originalY == d.currentY) {
			d.currentX += xAdjustment;
			d.currentY += yAdjustment;
		} else {
			d.currentX = d.originalX;
			d.currentY = d.originalY;
		}
		if (d.currentId == d.originalId) {
			if (!d.open) {
				d.currentId += 1;
			} else if (d.open) {
				d.currentId -= 1;
			}
		} else if (d.currentId != d.originalId) {
			if (!d.open) {
				d.currentId -= 1;
			} else if (d.open) {
				d.currentId += 1;
			}
		}
		if (!existing)
			d.currentId = GameEntrances.getDoorId(door);
		else {
			d.currentId = d.originalId;
			doors.remove(d);
		}
		if (d.object.getType() == 0) {
			if (!d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					d.currentRotation = 1;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					d.currentRotation = 2;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					d.currentRotation = 3;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					d.currentRotation = 0;
				} else if (d.originalRotation != d.currentRotation) {
					d.currentRotation = d.originalRotation;
				}
			} else if (d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					d.currentRotation = 3;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					d.currentRotation = 0;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					d.currentRotation = 1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					d.currentRotation = 2;
				} else if (d.originalRotation != d.currentRotation) {
					d.currentRotation = d.originalRotation;
				}
			}
		} else if (d.object.getType() == 9) {
			if (!d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					d.currentRotation = 3;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					d.currentRotation = 2;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					d.currentRotation = 1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					d.currentRotation = 0;
				} else if (d.originalRotation != d.currentRotation) {
					d.currentRotation = d.originalRotation;
				}
			} else if (d.open) {
				if (d.originalRotation == 0 && d.currentRotation == 0) {
					d.currentRotation = 3;
				} else if (d.originalRotation == 1 && d.currentRotation == 1) {
					d.currentRotation = 0;
				} else if (d.originalRotation == 2 && d.currentRotation == 2) {
					d.currentRotation = 1;
				} else if (d.originalRotation == 3 && d.currentRotation == 3) {
					d.currentRotation = 2;
				} else if (d.originalRotation != d.currentRotation) {
					d.currentRotation = d.originalRotation;
				}
			}
		}
		World.getRegion(door.getRegionId()).removeObject(door);
		World.removeObject(door, true);
		World.spawnObject(new WorldObject(d.currentId, d.object.getType(), d.currentRotation, d.currentX, d.currentY, d.object.getPlane()), true);
	}

}