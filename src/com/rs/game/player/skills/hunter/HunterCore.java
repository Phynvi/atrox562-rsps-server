package com.rs.game.player.skills.hunter;

import java.util.concurrent.TimeUnit;

import com.rs.game.player.Skills;
import com.rs.game.player.skills.hunter.BirdSnaring.Birds;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.cores.CoresManager;
import com.rs.game.WorldObject;
import com.rs.utils.Utils;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.skills.hunter.BoxTrapping.BoxEntities;
import com.rs.game.player.skills.hunter.DeadfallHunter.DeadfallPreys;
import com.rs.game.player.skills.hunter.PitfallHunter.PitfallPreys;
import com.rs.game.npc.hunter.PitfallNPCs;
import com.rs.game.World;
import com.rs.game.player.skills.hunter.NetTrapping.NetPreys;
import com.rs.utils.pathfinder.PathFinder;

public class HunterCore {
	public static boolean isAtPiscatoryWildKebbits(Player player) {
		if (player.getX() >= 2284 && player.getX() <= 2406 && player.getY() >= 3488 && player.getY() <= 3590)
			return true;
		return false;
	}

	public static boolean isAtPenguins(Player player) {
		if (player.getX() >= 2683 && player.getX() <= 2758 && player.getY() >= 3984 && player.getY() <= 4038)
			return true;
		return false;
	}

	public static boolean isAtPiscatoryPricklyKebbits(Player player) {
		if (player.getX() >= 2284 && player.getX() <= 2406 && player.getY() >= 3591 && player.getY() <= 3700)
			return true;
		return false;
	}

	public static boolean isAtFeldipHillsBarbTailedKebbits(Player player) {
		if (player.getX() >= 2429 && player.getX() <= 2650 && player.getY() >= 2848 && player.getY() <= 3003)
			return true;
		return false;
	}

	public static boolean isAtNorthernRelleka(Player player) {
		if (player.getX() >= 2691 && player.getX() <= 2753 && player.getY() >= 3753 && player.getY() <= 3800)
			return true;
		return false;
	}

	public static boolean isAtFeldipHillsDiseasedKebbits(Player player) {
		if (player.getX() >= 2429 && player.getX() <= 2650 && player.getY() >= 2799 && player.getY() <= 2847)
			return true;
		return false;
	}

	public static int getTrapAmount(Player player) {
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

	public static int[] logs = new int[] { 13567, 1513, 1515, 12581, 10810, 6332, 1517, 6333, 1519, 1521, 2862, 1511 };

	public static int getVarBit(WorldObject object) {
		if (object.getId() == 43614)
			return 4189;
		else if (object.getId() >= 19259 && object.getId() <= 19268)
			return object.getId() - 16295;
		else if (object.getId() >= 19253 && object.getId() <= 19255)
			return object.getId() - 16295;
		return 0;
	}

	public static int getLogsId(Player player) {
		int logsId = 1511;
		for (int i = 0; i < HunterCore.logs.length; i++)
			if (player.getInventory().containsItem(HunterCore.logs[i], 1))
				logsId = HunterCore.logs[i];
		return logsId;
	}

	public static void startDeadfall(Player player, WorldObject object) {
		player.getActionManager().setAction(new DeadfallHunter(object));
	}

	public static boolean success(Player player) {
		if (isAtFeldipHillsDiseasedKebbits(player))
			return calculateSuccess(player, 44);
		else if (isAtFeldipHillsBarbTailedKebbits(player))
			return calculateSuccess(player, 33);
		else if (isAtNorthernRelleka(player))
			return calculateSuccess(player, 51);
		else if (isAtPenguins(player))
			return calculateSuccess(player, 51);
		else if (isAtPiscatoryPricklyKebbits(player))
			return calculateSuccess(player, 37);
		else
			return calculateSuccess(player, 28);
	}

	public static boolean calculateSuccess(Player player, int requiredLevel) {
		int level = player.getSkills().getLevel(Skills.HUNTER);
		int basePercentage = 30 - requiredLevel + ((level + hunterSuccessBoosts(player)) / 2);
		if (basePercentage > 80)
			basePercentage = 80;
		else if (basePercentage < 30)
			basePercentage = 30;
		if (Utils.random(100) < basePercentage)
			return true;
		return false;
	}

	public static int hunterSuccessBoosts(Player player) {
		int boost = 0;
		if (isAtFeldipHillsDiseasedKebbits(player) || isAtFeldipHillsBarbTailedKebbits(player)) {
			if (player.getEquipment().getChestId() == 10057)
				boost += 5;
			if (player.getEquipment().getLegsId() == 10059)
				boost += 5;
		}
		return boost;
	}

	public static boolean isHunterNPC(NPC npc) {
		if (npc.getId() == 5104 || npc.getId() == 5105 || npc.getId() == 5103)
			return true;
		return false;
	}

	public static boolean handleHunterObjects1(WorldObject object, Player player) {
		if (object.getId() == 19671) {
			WorldTile loc = getNetLocation(object);
			player.lock();
			PathFinder.simpleWalkTo(player, loc);
			do {
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						player.unlock();
						player.faceObject(object);
						player.getActionManager().setAction(new NetTrapping(object));
					}
				}, 1, TimeUnit.SECONDS);
				break;
			} while (player.getX() == loc.getX() && player.getY() == loc.getY());
			return true;
		}
		Birds birds = Birds.forObjectId(object.getId());
		if (birds != null) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(5207));
				for (Item items : birds.getItems()) {
					if (player.getInventory().hasFreeSlots() || items.getDefinitions().isStackable() && player.getInventory().containsItem(items.getId(), 1))
						player.getInventory().addItem(items);
					else
						World.addGroundItem(items, new WorldTile(player));
				}

				player.getInventory().addItem(10006, 1);
				player.getSkills().addXp(Skills.HUNTER, birds.getXp() / 10);
			} else {
				player.sendMessage("This isn't your trap.");
			}
			return true;
		} else if (object.getId() == 19174) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(5207));
				player.getInventory().addItem(10006, 1);
			} else
				player.sendMessage("This isn't your trap.");
			return true;
		} else if (object.getId() == 19175) {
			if (player.getInventory().hasFreeSlots()) {
				player.getInventory().addItem(new Item(10006, 1));
				OwnedObjectManager.removeObject(player, object);
				player.sendMessage("You dismantle the trap.");
			} else
				player.sendMessage("You don't have enough free inventory space to dismantle this.");
			return true;
		}

		BoxEntities boxEntities = BoxEntities.forObjectId(object.getId());
		if (boxEntities != null) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(7270));
				if (boxEntities != BoxEntities.IMP) {
					for (Item items : boxEntities.getItems()) {
						if (items == null)
							continue;
						if (player.getInventory().hasFreeSlots() || items.getDefinitions().isStackable() && player.getInventory().containsItem(items.getId(), 1))
							player.getInventory().addItem(items);
						else
							World.addGroundItem(items, new WorldTile(player));
					}
				}
				if (boxEntities == BoxEntities.GRENWALL)
					if (player.getInventory().hasFreeSlots())
						player.getInventory().addItem(BoxEntities.getGrenwallLoot());
					else
						World.addGroundItem(BoxEntities.getGrenwallLoot(), new WorldTile(player));
				player.getInventory().addItem(boxEntities == BoxEntities.IMP ? 10027 : 10008, 1);
				player.getSkills().addXp(Skills.HUNTER, boxEntities.getXp() / 10);
			} else {
				player.sendMessage("This isn't your trap.");
			}
			return true;
		} else if (object.getId() == 19192 || object.getId() == 19224) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(7270));
				player.getInventory().addItem(object.getId() == 19192 ? 10008 : 10025, 1);
			} else
				player.sendMessage("This isn't your trap.");
			return true;
		} else if (object.getId() == 19187 || object.getId() == 19223) {
			if (player.getInventory().hasFreeSlots()) {
				player.getInventory().addItem(new Item(object.getId() == 19187 ? 10008 : 10025, 1));
				OwnedObjectManager.removeObject(player, object);
				player.setNextAnimation(new Animation(7270));
				player.sendMessage("You dismantle the trap.");
			} else
				player.sendMessage("You don't have enough free inventory space to dismantle this.");
			return true;
		}

		DeadfallPreys deadfall = DeadfallPreys.forObjectId(object.getId());
		if (deadfall != null) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(5208));
				if (deadfall.getLoot() != null) {
					if (player.getInventory().getFreeSlots() > 1) {
						player.getInventory().addItem(deadfall.getLoot());
						player.getInventory().addItem(new Item(526, 1));
					} else {
						if (player.getInventory().hasFreeSlots())
							player.getInventory().addItem(deadfall.getLoot());
						else
							World.addGroundItem(deadfall.getLoot(), new WorldTile(player));
						World.addGroundItem(new Item(526, 1), new WorldTile(player));
					}
				}
				World.spawnObject(new WorldObject(19205, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), true);
				player.getSkills().addXp(Skills.HUNTER, deadfall.getExperience() / 10);
			} else
				player.sendMessage("This isn't your trap.");
			return true;
		} else if (object.getId() == 19206) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(5208));
				if (player.getInventory().hasFreeSlots())
					player.getInventory().addItem(1511, 1);
				else
					World.addGroundItem(new Item(1511, 1), new WorldTile(player));
				player.sendMessage("You dismantle the trap.");
				player.lock(5);
				World.spawnObject(new WorldObject(19205, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), true);
			} else
				player.sendMessage("This isn't your trap.");
			return true;
		}

		NetPreys net = NetPreys.forObjectId(object.getId());
		if (net != null) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(5216));
				if (player.getInventory().getFreeSlots() > 1) {
					player.getInventory().addItem(new Item(954, 1));
					player.getInventory().addItem(new Item(303, 1));
				} else {
					if (player.getInventory().hasFreeSlots())
						player.getInventory().addItem(new Item(954, 1));
					else
						World.addGroundItem(new Item(954, 1), new WorldTile(player));
					World.addGroundItem(new Item(303, 1), new WorldTile(player));
				}
				if (net.getLoot() != null) {
					if (player.getInventory().hasFreeSlots())
						player.getInventory().addItem(net.getLoot());
					else
						World.addGroundItem(net.getLoot(), new WorldTile(player));
				}
				World.spawnObject(new WorldObject(19671, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), true);
				player.getSkills().addXp(Skills.HUNTER, net.getExperience() / 10);
			} else
				player.sendMessage("This isn't your trap.");
			return true;
		} else if (object.getId() == 19670 || object.getId() == 19673) {
			if (OwnedObjectManager.getOwner(object) != player) {
				player.sendMessage("This isn't your trap.");
				return true;
			} else {
				player.setNextAnimation(new Animation(7270));
				if (OwnedObjectManager.removeObject(player, object)) {
					if (player.getInventory().getFreeSlots() > 1) {
						player.getInventory().addItem(954, 1);
						player.getInventory().addItem(303, 1);
					} else if (player.getInventory().hasFreeSlots()) {
						player.getInventory().addItem(954, 1);
						World.addGroundItem(new Item(303, 1), new WorldTile(player));
					} else {
						World.addGroundItem(new Item(954, 1), new WorldTile(player));
						World.addGroundItem(new Item(303, 1), new WorldTile(player));
					}
					player.sendMessage("You dismantle the trap.");
					player.lock(3);
					if (object.getId() == 19670) {
						WorldTile netLoc = getNetLocation(object);
						System.out.println(getNetLocation(object).getX() + " " + getNetLocation(object).getY());
						World.removeObject(World.getSpawnedObject(netLoc.getX(), netLoc.getY(), netLoc.getPlane()), false);
						World.spawnObject(new WorldObject(19671, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), true);
					} else {
						WorldTile loc = getTreeLocation(object);
						if (OwnedObjectManager.removeObject(player, World.getObject(getTreeLocation(object))))
							;
						World.spawnObject(new WorldObject(19671, object.getType(), object.getRotation(), loc.getX(), loc.getY(), loc.getPlane()), true);
					}
				}
			}
			return true;
		} else if (object.getId() == 19677) {
			player.setNextAnimation(new Animation(5216));
			player.lock(3);
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					World.spawnObject(new WorldObject(19671, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), true);
					if (player.getInventory().getFreeSlots() > 1) {
						player.getInventory().addItem(new Item(954, 1));
						player.getInventory().addItem(new Item(303, 1));
					} else {
						if (player.getInventory().hasFreeSlots())
							player.getInventory().addItem(new Item(954, 1));
						else
							World.addGroundItem(new Item(954, 1), new WorldTile(player));
						World.addGroundItem(new Item(303, 1), new WorldTile(player));
					}
				}
			}, 1800, TimeUnit.MILLISECONDS);
			return true;
		}

		if (object.getId() == 19335) {
			if (OwnedObjectManager.removeObject(player, object)) {
				if (player.getInventory().hasFreeSlots())
					player.getInventory().addItem(10031, 1);
				else
					World.addGroundItem(new Item(10031, 1), new WorldTile(player));
				player.setNextAnimation(new Animation(7270));
				if (player.getInventory().getFreeSlots() > 2) {
					player.getInventory().addItem(new Item(10134, 1));
					player.getInventory().addItem(new Item(3226, 1));
					player.getInventory().addItem(new Item(526, 1));
				} else {
					int freeSlots = player.getInventory().getFreeSlots();
					if (freeSlots == 2) {
						player.getInventory().addItem(new Item(10134, 1));
						player.getInventory().addItem(new Item(3226, 1));
					} else if (freeSlots == 1)
						player.getInventory().addItem(new Item(10134, 1));
					else {
						World.addGroundItem(new Item(10134, 1), new WorldTile(player));
						World.addGroundItem(new Item(3226, 1), new WorldTile(player));
						World.addGroundItem(new Item(526, 1), new WorldTile(player));
					}
				}
				player.getSkills().addXp(Skills.HUNTER, 144 / 10);
			} else
				player.sendMessage("This isn't your trap.");
		} else if (object.getId() == 19334) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(7270));
				if (player.getInventory().hasFreeSlots())
					if (player.getInventory().hasFreeSlots())
						player.getInventory().addItem(10031, 1);
					else
						World.addGroundItem(new Item(10031, 1), new WorldTile(player));
			} else
				player.sendMessage("This isn't your trap.");
		} else if (object.getId() == 19333) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(7270));
				player.sendMessage("You dismantle the trap.");
				if (player.getInventory().hasFreeSlots())
					if (player.getInventory().hasFreeSlots())
						player.getInventory().addItem(10031, 1);
					else
						World.addGroundItem(new Item(10031, 1), new WorldTile(player));
			} else
				player.sendMessage("This isn't your trap.");
		}
		return false;
	}

	private static WorldTile getTreeLocation(WorldObject object) {
		switch (object.getRotation()) {
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

	private static WorldTile getNetLocation(WorldObject object) {
		switch (object.getRotation()) {
		case 0:
			return new WorldTile(object.getX(), object.getY() + 1, object.getPlane());
		case 1:
			return new WorldTile(object.getX() + 1, object.getY(), object.getPlane());
		case 2:
			return new WorldTile(object.getX(), object.getY() - 1, object.getPlane());
		default:
			return new WorldTile(object.getX() - 1, object.getY(), object.getPlane());
		}
	}

	public static boolean handleHunterObjects2(WorldObject object, Player player) {
		if (object.getId() == 19175) {
			if (OwnedObjectManager.removeObject(player, object)) {
				player.setNextAnimation(new Animation(5207));
				player.getInventory().addItem(10006, 1);
				player.sendMessage("You dismantle the trap..");
			} else
				player.sendMessage("This isn't your trap.");
			return true;
		} else if (object.getId() == 19333) {
			player.sendMessage("This trap is untouched.");
			return true;
		}
		return false;
	}

	public static void tease(NPC npc, Player player) {
		if (!player.getInventory().containsItem(10029, 1)) {
			player.sendMessage("You need a teasing stick to tease the " + npc.getName() + ".");
			return;
		}
		player.setNextAnimation(new Animation(5236));
		npc.setNextAnimation(new Animation(5227));
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if (Utils.random(3) < 3)
					npc.setTarget(player);
				else
					player.sendMessage("The " + npc.getName() + " is unresponsive towards your tease.");
			}
		}, 1000, TimeUnit.MILLISECONDS);
		return;
	}

	public static void handlePitfall(Player player, WorldObject object) {
		if (player.getVarBitManager().getBitValue(object.getDefinitions().varbit) == 0)
			player.getActionManager().setAction(new PitfallHunter(object));
		else if (player.getVarBitManager().getBitValue(object.getDefinitions().varbit) == 3) {
			PitfallPreys prey = PitfallPreys.forObjectId(object.getId());
			player.setNextAnimation(new Animation(5208));
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					player.getSkills().addXp(Skills.HUNTER, prey.getExperience() / 10);
					if (player.getInventory().getFreeSlots() > 1) {
						player.getInventory().addItem(prey.getLoot());
						player.getInventory().addItem(new Item(526, 1));
					} else {
						if (player.getInventory().hasFreeSlots())
							player.getInventory().addItem(prey.getLoot());
						else
							World.addGroundItem(prey.getLoot(), new WorldTile(player));
						World.addGroundItem(prey.getLoot(), new WorldTile(player));
					}
					player.getVarBitManager().sendVarbit(object.getDefinitions().varbit, 0);
					PitfallNPCs.removePitfallTrap(object, player);
				}
			}, 2, TimeUnit.SECONDS);
		} else if (player.getVarBitManager().getBitValue(object.getDefinitions().varbit) == 2) {
			if (player.getInventory().hasFreeSlots())
				player.getInventory().addItem(new Item(1511, 1));
			else
				World.addGroundItem(new Item(1511, 1), new WorldTile(player));
			PitfallNPCs.removePitfallTrap(object, player);
			player.getVarBitManager().sendVarbit(object.getDefinitions().varbit, 0);
		}
	}

}
