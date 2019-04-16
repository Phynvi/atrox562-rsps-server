package com.rs.game.player.skills.thieving;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.WorldTile;
import com.rs.utils.Utils;
import com.rs.game.tasks.WorldTask;
import com.rs.game.WorldObject;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.player.controlers.Controler;
import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.player.content.Magic;
import com.rs.game.ForceTalk;
import com.rs.game.npc.others.Mummy;
import com.rs.game.Animation;
import com.rs.utils.Logger;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.cores.CoresManager;
import com.rs.game.player.Skills;
import com.rs.game.Graphics;

public final class PyramidPlunderControler extends Controler implements Serializable {

	/**
	 * Generated serializable UID
	 */
	private static final long serialVersionUID = 69229330866454358L;
	private static final int PICK_LOCK = 881;
	private static final int PASS_TRAP = 2246;
	private static final int URNSEARCH_START = 4340;
	private static final int URNSEARCH_SUCCESS = 4342;
	private static final int URNSEARCH_FAIL = 4341;
	private static final int CHARM_START = 1877;
	private static final int CHARM_CONTINUE = 1878;
	private static final int SEARCH_CHEST = 4238;
	private static final int DEAD_END = 16460;

	private static int[] Doors = { 16540, 16541, 16542, 16539 };
	private static int[] Entrance = { 16543, 16544, 16545, 16546 };
	private static List<Integer> Exits = new ArrayList<Integer>();
	public static List<Integer> Entrances = new ArrayList<Integer>();
	private List<WorldTile> spawnedObjects = new ArrayList<WorldTile>();
	private List<Integer> charmedUrns = new ArrayList<Integer>();

	private boolean passingTrap;
	private Mummy mummy;
	private Player player;

	@Override
	public boolean login() {
		player.setNextWorldTile(new WorldTile(3288, 2801, 0));
		player.getInterfaceManager().closeOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
		removeControler();
		return false;
	}

	@Override
	public boolean logout() {
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		player.getInterfaceManager().closeOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
		removeControler();
	}

	public void targetFinishedWithoutDie() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getPlunder().mummy = null;
	}

	public void targetDied() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		player.getPlunder().mummy = null;

	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 4476) {
			player.getDialogueManager().startDialogue("PyramidPlunderD", npc.getId());
			return true;
		}
		return false;
	}

	@Override
	public boolean processObjectClick3(WorldObject object) {
		if (object.getId() == 16501 || object.getId() == 16502 || object.getId() >= 16518 && object.getId() <= 16532) {
			if (player.getVarBitManager().getBitValue(getVarbit(object)) == 2) {
				player.lock();
				player.setNextAnimation(new Animation(CHARM_START));
				player.getPackets().sendGameMessage("You begin charming the snake..");
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop < 5) {
							player.setNextAnimationNoPriority(new Animation(CHARM_CONTINUE));
						} else if (loop == 5) {
							player.getPackets().sendGameMessage(
									"The snake finally becomes calm. You may now loot the urn safely.");
							player.unlock();
							player.getPlunder().charmedUrns.add(object.getId());
							sendVarbit(object, 3);
							stop();
						}
						loop++;
					}

				}, 0, 1);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {

		if (object.getId() == 16501 || object.getId() == 16502 || object.getId() >= 16518 && object.getId() <= 16532) {
			if (player.getPlunder().charmedUrns.contains(object.getId())) {
				player.sendMessage("You've already made sure this urn doesn't contain any snakes.");
				return false;
			}
			if (player.getVarBitManager().getBitValue(getVarbit(object)) == 0) {
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							player.lock();
							player.setNextAnimation(new Animation(URNSEARCH_START));
							player.getPackets().sendGameMessage("You search the urn for snakes...");
						} else if (loop == 1) {
							if (calculateSuccess()) {
								player.getPlunder().charmedUrns.add(object.getId());
								player.sendMessage("The urn doesn't contain any snakes");
							} else {
								sendVarbit(object, 2);
								player.sendMessage("The urn contains a vicious snake.");
							}
							player.unlock();
							stop();
						}
						loop++;
					}

				}, 0, 1);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getDefinitions().name.equals("Tomb Door"))
		player.setNextWorldTile(new WorldTile(3288, 2801, 0));	
		//player.getDialogueManager().startDialogue("LeavePyramidPlunder");
		else if (object.getId() == 16547)
			if (player.getVarBitManager().getBitValue(getVarbit(object)) == 0)
				searchSarcophagus(object);
			else
				player.sendMessage("You've already searched this sarcophagus.");
		else if (object.getId() == 59795)
			if (player.getVarBitManager().getBitValue(getVarbit(object)) == 0)
				searchLastSarcophagus(object);
			else
				player.sendMessage("You've already searched this sarcophagus.");
		else if (object.getId() == 16517)
			passTrap(object);
		else if (object.getId() == 16501 || object.getId() == 16502
				|| object.getId() >= 16518 && object.getId() <= 16532)
			if (player.getVarBitManager().getBitValue(getVarbit(object)) == 0
					|| player.getVarBitManager().getBitValue(getVarbit(object)) == 2
					|| player.getVarBitManager().getBitValue(getVarbit(object)) == 3)
				searchUrn(object);
			else
				player.sendMessage("You've already searched this urn.");
		else if (object.getId() == 16537)
			searchChest(object);
		//else if (object.getId() == 16459)
			//player.setNextWorldTile(new WorldTile(3288, 2801, 0));
		else if (object.getId() >= 16539 && object.getId() <= 16542)
			openDoor(object);
		return false;
	}

	/**
	 * Activates a door attempt.<br>
	 * <br>
	 * If the attempt fails, the door disappears. If the attempt succeeds,
	 * {@code changeFloor(player)} is called.
	 * 
	 * @param player
	 * @param door
	 *            {@code instanceof WorldObject}
	 */
	private void openDoor(WorldObject door) {
		if (getCurrentRoomNumber() == 8) {
			player.sendMessage("You're already on the highest floor, there's no going further here.");
			return;
		} else if (player.getSkills().getLevel(Skills.THIEVING) < (player.getVarBitManager().getBitValue(2376) + 10)) {
			player.sendMessage("You need at least level " + (player.getVarBitManager().getBitValue(2376) + 10)
					+ " Thieving to pick this lock.");
			return;
		}
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(PICK_LOCK));
					player.getPackets().sendGameMessage("You attempt to pick the lock...");
				} else if (loop == 2) {
					if (door.getId() != Exits.get(getCurrentRoomNumber())) {
						player.getPackets().sendGameMessage("...and you realize that it leads to a dead end.");
						WorldObject dead = new WorldObject(DEAD_END, 0, door.getRotation(), door.getX(), door.getY(),
								door.getPlane());
						player.getPackets().sendSpawnedObject(dead);
						player.getPlunder();
						player.getPlunder().spawnedObjects.add(new WorldTile(dead.getX(), dead.getY(), dead.getPlane()));
					} else
						sendNextRoom();
					player.unlock();
					stop();
				}
				loop++;
			}

		}, 0, 1);
	}

	/**
	 * Searches a chest for loot and updates the map textures accordingly.
	 * 
	 * @param player
	 * @param chest
	 */
	private void searchChest(WorldObject chest) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;

			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(SEARCH_CHEST));
					player.getPackets().sendGameMessage("You search the chest for treasures...");
					sendVarbit(chest, 1);
				} else if (loop == 1) {
					if (calculateSuccess()) {
						player.getSkills().addXp(Skills.THIEVING, getXp("Gold chest"));
						player.getPackets().sendGameMessage("You find some treasure in the chest!");
						giveReward(ChestRewards.values());
						if (Utils.random(100) > 80) {
							sendTarget(4500, player);
							player.sendMessage("A large swarm of scarabs appears from the chest.");
						}
					} else {
						if (Utils.random(100) > 80) {
							sendTarget(4500, player);
							player.sendMessage("A large swarm of scarabs appears from the chest.");
						}
						player.getPackets().sendGameMessage("...but you find nothing valuable in the chest");
					}
					player.unlock();
					stop();
				}
				loop++;
			}

		}, 0, 1);
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof Mummy && target != player.getPlunder().mummy) {
			player.getPackets().sendGameMessage("This isn't your target.");
			return false;
		}
		return true;
	}

	private void sendTarget(int id, WorldTile tile) {
		if (player.getPlunder().mummy != null)
			player.getPlunder().mummy.disapear();

		player.getPlunder().mummy = new Mummy(id, tile, this);
		if (id == 4500)
			player.getPlunder().mummy.setNextAnimation(new Animation(1949));
		if (id != 4477)
			player.getPlunder().mummy.setTarget(player);
		if (id == 6753)
			player.getPlunder().mummy.setNextForceTalk(new ForceTalk("Youu... Death!"));
		if (id != 4477)
			player.getHintIconsManager().addHintIcon(player.getPlunder().mummy, 1, -1, false);
	}

	/**
	 * Searches a sarcophagus for loot and updates the map textures accordingly.
	 * 
	 * @param player
	 * @param sarcophagus
	 */
	private void searchSarcophagus(WorldObject sarcophagus) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;

			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(7321));
					player.getPackets().sendGameMessage("You search the chest for treasures...");

				} else if (loop == 3) {
					sendVarbit(sarcophagus, 1);
					player.getSkills().addXp(Skills.STRENGTH, getXp("Sarcophagus"));
					player.getPackets().sendGameMessage("You find some treasure in the sarcophagus!");
					giveReward(SarcophagusRewards.values());
					if (Utils.random(100) > 50) {
						sendTarget(6753, player);
						player.sendMessage("A mummy appears from the sarcophagus!");
					}
					player.unlock();
					stop();
				}
				loop++;
			}

		}, 0, 1);
	}

	/**
	 * Searches the sarcophagus for loot and updates the map textures
	 * accordingly.
	 * 
	 * @param player
	 * @param sarcophagus
	 */
	private void searchLastSarcophagus(WorldObject sarcophagus) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;

			@Override
			public void run() {
				if (loop == 0) {
					player.lock();
					player.setNextAnimation(new Animation(PICK_LOCK));
					player.getPackets().sendGameMessage("You open the sarcophagus and search for treasures...");

				} else if (loop == 1) {
					sendVarbit(sarcophagus, 1);
					player.getPackets().sendGameMessage("You find some treasure in the sarcophagus!");
					giveReward(SarcophagusRewards.values());
					if (Utils.random(100) > 30) {
						sendTarget(6753, player);
						player.sendMessage("A mummy appears from the sarcophagus!");
					}
					player.unlock();
					stop();
				}
				loop++;
			}

		}, 0, 1);
	}

	/**
	 * Searches an urn for loot and updates the map textures accordingly.
	 * 
	 * @param player
	 * @param urn
	 */
	private void searchUrn(WorldObject urn) {
		player.lock();
		player.setNextAnimation(new Animation(URNSEARCH_START));
		player.getPackets().sendGameMessage("You search the urn for treasures...", true);
		WorldTasksManager.schedule(new WorldTask() {

			int loop = 0;
			boolean success = false;

			@Override
			public void run() {
				if (loop == 0) {
					if (!player.getPlunder().charmedUrns.contains(urn.getId())) {
						if (calculateSuccess()) {
							success = true;
							player.setNextAnimation(new Animation(URNSEARCH_SUCCESS));
							player.getPackets().sendGameMessage("You find some treasure!", true);
							player.getSkills().addXp(Skills.THIEVING, getXp("Urn"));
						} else {
							player.setNextAnimation(new Animation(URNSEARCH_FAIL));
							player.getPackets().sendGameMessage("..and you get bitten by a snake!", true);
							player.getPoison().makePoisoned(Utils.random(5, 10));
						}
					} else {
						success = true;
						player.setNextAnimation(new Animation(URNSEARCH_SUCCESS));
						player.getPackets().sendGameMessage("You find some treasure!", true);
					}
				} else if (loop == 1) {
					if (success) {
						giveReward(UrnRewards.values());
						sendVarbit(urn, 1);
					}
					player.unlock();
					stop();
				}
				loop++;
			}

		}, 0, 2);
	}

	/**
	 * Returns experience given dependant on factors.
	 */
	private int getXp(String object) {
		int room = getCurrentRoomNumber();
		if (object.equals("Urn") || object.equals("Sarcophagus")) {
			switch (room) {
			case 1:
				return 20;
			case 2:
				return 30;
			case 3:
				return 50;
			case 4:
				return 70;
			case 5:
				return 100;
			case 6:
				return 150;
			case 7:
				return 225;
			default:
				return 275;
			}
		} else if (object.equals("Gold chest")) {
			switch (room) {
			case 1:
				return 40;
			case 2:
				return 60;
			case 3:
				return 100;
			case 4:
				return 140;
			case 5:
				return 200;
			case 6:
				return 300;
			case 7:
				return 450;
			default:
				return 550;
			}
		}
		return 0;
	}

	/**
	 * Locates the room player's in right now
	 */
	private int getCurrentRoomNumber() {
		int destX = player.getX();
		int destY = player.getY();
		if (destX >= 1920 && destX <= 1935 && destY >= 4462 && destY <= 4483)
			return 1;
		else if (destX >= 1944 && destX <= 1964 && destY >= 4461 && destY <= 4483)
			return 2;
		else if (destX >= 1966 && destX <= 1982 && destY >= 4448 && destY <= 4475)
			return 3;
		else if (destX >= 1921 && destX <= 1947 && destY >= 4444 && destY <= 4461)
			return 4;
		else if (destX >= 1947 && destX <= 1968 && destY >= 4440 && destY <= 4457)
			return 5;
		else if (destX >= 1920 && destX <= 1933 && destY >= 4421 && destY <= 4443)
			return 6;
		else if (destX >= 1937 && destX <= 1958 && destY >= 4416 && destY <= 4437)
			return 7;
		return 8;
	}

	/**
	 * Sending varbit upon opening an object
	 */
	private void sendVarbit(WorldObject object, int value) {
		if (object.getId() >= 16518 && object.getId() <= 16532)
			player.getVarBitManager().sendVarbit(2346 + (object.getId() - 16518), value);
		else if (object.getId() == 16547)
			player.getVarBitManager().sendVarbit(2362, value);
		else if (object.getId() == 59795)
			player.getVarBitManager().sendVarbit(3422, value);
		else if (object.getId() == 16537)
			player.getVarBitManager().sendVarbit(2363, value);
	}

	/**
	 * Gets the varbit associated with the object
	 */

	private int getVarbit(WorldObject object) {
		if (object.getId() >= 16518 && object.getId() <= 16532)
			return 2346 + (object.getId() - 16518);
		if (object.getId() == 16547)
			return 2362;
		else if (object.getId() == 59795)
			return 3422;
		else if (object.getId() == 16537)
			return 2363;
		return 0;
	}

	/**
	 * Determines whether or not a player will succeed in looting an object.
	 * 
	 * @param player
	 * @return whether or not the loot is successful
	 */
	private boolean calculateSuccess() {
		double chance = 0.4;
		int levelReq = 21;
		int expertise = player.getSkills().getLevel(Skills.THIEVING) - levelReq;
		if (expertise <= 3 && expertise > 0)
			chance += 0.1;
		if (expertise <= 6 && expertise > 3)
			chance += 0.1;
		if (expertise <= 9 && expertise > 6)
			chance += 0.1;
		if (expertise > 9)
			chance += 0.2;
		if (Math.random() < chance)
			return true;
		return false;
	}

	/**
	 * Handles the player passing through a spear trap.
	 * 
	 * @param player
	 * @param trap
	 *            {@code instanceof WorldObject}
	 */
	private void passTrap(WorldObject trap) {
		WorldTasksManager.schedule(new WorldTask() {

			int loop = 0;
			boolean success = false;

			@Override
			public void run() {

				if (loop == 0) {
					if (calculateSuccess())
						success = true;
					if (player.isLocked())
						success = false;
					player.lock();
					player.getPackets().sendGameMessage("You attempt to sneak past the trap...");
					player.setNextAnimation(new Animation(PASS_TRAP));
				} else if (loop == 1) {
					if (success) {
						player.getPlunder().passingTrap = true;
						if (trap.getX() == player.getX()) {
							if (trap.getY() > player.getY()) {
								player.addWalkSteps(player.getX(), player.getY() + 3, 3, false);
							} else {
								player.addWalkSteps(player.getX(), player.getY() - 3, 3, false);
							}
						} else if (trap.getY() == player.getY()) {
							if (trap.getX() > player.getX()) {
								player.addWalkSteps(player.getX() + 3, player.getY(), 3, false);
							} else {
								player.addWalkSteps(player.getX() - 3, player.getY(), 3, false);
							}
						} else if (trap.getX() > player.getX() && trap.getY() < player.getY()) {
							player.addWalkSteps(player.getX() + 3, player.getY(), 3, false);
						} else if (trap.getX() < player.getX() && trap.getY() < player.getY()) {
							if (trap.getRotation() == 2)
								player.addWalkSteps(player.getX(), player.getY() - 3, 3, false);
							else
								player.addWalkSteps(player.getX() - 3, player.getY(), 3, false);
						} else if (player.getX() > trap.getX() && trap.getY() > player.getY()) {
							player.addWalkSteps(player.getX(), player.getY() + 3, 3, false);
						}
						player.getPackets().sendGameMessage("... and you manage to squeeze by undeteced.");
					} else {
						player.unlock();
						if (!success)
							World.sendObjectAnimation(trap, new Animation(459));
						player.getPackets().sendGameMessage("You fail to get past the trap!");
						player.applyHit(new Hit(player, Utils.random(5, 10), HitLook.REGULAR_DAMAGE));
					}
				} else if (loop == 2) {
					player.unlock();
					player.getPlunder().passingTrap = false;
					stop();
				}
				loop++;
			}

		}, 0, 2);
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getInterfaceManager().closeOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
		removeControler();
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getInterfaceManager().closeOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
		removeControler();
		return true;
	}

	private void startTimer() {
		WorldTasksManager.schedule(new WorldTask() {
			int time = 0;

			@Override
			public void run() {
				if (player.getControlerManager().getControler() == null) {
					player.getInterfaceManager()
							.closeOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
					stop();
				}
				if (time < 500) {
					player.getVarBitManager().sendVarbit(2375, time);
				} else if (time == 500) {
					removeControler();
					player.getInterfaceManager()
							.closeOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
					player.sendMessage("Your time has ran out!");
					player.setNextWorldTile(new WorldTile(3288, 2801, 0));
					stop();
				}
				time++;
			}
		}, 1, 1);
	}

	@Override
	public void start() {
		//resetAllConfigs();
		player.setNextWorldTile(new WorldTile(1927, 4477, 0));
		player.getInterfaceManager().sendOverlay(428);
		player.getPackets().sendConfig(2377, 1);
		player.getVarBitManager().sendVarbit(2376, 21);
		startTimer();
		if (player.getPlunder().spawnedObjects.size() != 0)
			for (int i = 0; i < player.getPlunder().spawnedObjects.size(); i++)
				player.getPackets().sendSpawnedObject(World.getObject(player.getPlunder().spawnedObjects.get(i), 0));
	}

	@Override
	public void moved() {
		/**
		 * Handles all PP traps. When walked infront of, they will release upon
		 * the player
		 */
		int x = player.getX();
		int y = player.getY();
		/**
		 * First room traps
		 */
		if (x == 1927 && y == 4473)
			releaseTrap(new WorldTile(1926, 4473, 0), new WorldTile(1927, 4474, 0));
		else if (x == 1928 && y == 4473)
			releaseTrap(new WorldTile(1928, 4473, 0), new WorldTile(1928, 4474, 0));
		else if (x == 1928 && y == 4472)
			releaseTrap(new WorldTile(1928, 4472, 0), new WorldTile(1928, 4471, 0));
		else if (x == 1927 && y == 4472)
			releaseTrap(new WorldTile(1926, 4472, 0), new WorldTile(1927, 4471, 0));
		/**
		 * Second room traps
		 */
		else if (x == 1955 && y == 4474)
			releaseTrap(new WorldTile(1955, 4474, 0), new WorldTile(1955, 4475, 0));
		else if (x == 1954 && y == 4474)
			releaseTrap(new WorldTile(1953, 4474, 0), new WorldTile(1954, 4475, 0));
		else if (x == 1954 && y == 4473)
			releaseTrap(new WorldTile(1953, 4473, 0), new WorldTile(1954, 4472, 0));
		else if (x == 1955 && y == 4473)
			releaseTrap(new WorldTile(1955, 4473, 0), new WorldTile(1955, 4472, 0));
		/**
		 * Third room traps
		 */
		else if (x == 1977 && y == 4466)
			releaseTrap(new WorldTile(1977, 4466, 0), new WorldTile(1977, 4467, 0));
		else if (x == 1977 && y == 4465)
			releaseTrap(new WorldTile(1977, 4465, 0), new WorldTile(1977, 4464, 0));
		else if (x == 1976 && y == 4465)
			releaseTrap(new WorldTile(1975, 4465, 0), new WorldTile(1976, 4464, 0));
		else if (x == 1976 && y == 4466)
			releaseTrap(new WorldTile(1975, 4466, 0), new WorldTile(1976, 4467, 0));
		/**
		 * Fourth room traps
		 */
		else if (x == 1930 && y == 4452)
			releaseTrap(new WorldTile(1930, 4451, 0), new WorldTile(1929, 4452, 0));
		else if (x == 1931 && y == 4452)
			releaseTrap(new WorldTile(1931, 4451, 0), new WorldTile(1932, 4452, 0));
		else if (x == 1931 && y == 4453)
			releaseTrap(new WorldTile(1931, 4453, 0), new WorldTile(1932, 4453, 0));
		else if (x == 1930 && y == 4453)
			releaseTrap(new WorldTile(1930, 4453, 0), new WorldTile(1929, 4453, 0));
		/**
		 * Fifth room traps
		 */
		else if (x == 1962 && y == 4445)
			releaseTrap(new WorldTile(1962, 4445, 0), new WorldTile(1963, 4445, 0));
		else if (x == 1961 && y == 4445)
			releaseTrap(new WorldTile(1961, 4445, 0), new WorldTile(1960, 4445, 0));
		else if (x == 1961 && y == 4444)
			releaseTrap(new WorldTile(1961, 4443, 0), new WorldTile(1960, 4444, 0));
		else if (x == 1962 && y == 4444)
			releaseTrap(new WorldTile(1962, 4443, 0), new WorldTile(1963, 4444, 0));
		/**
		 * Sixth room traps
		 */
		else if (x == 1926 && y == 4427)
			releaseTrap(new WorldTile(1925, 4427, 0), new WorldTile(1926, 4426, 0));
		else if (x == 1927 && y == 4427)
			releaseTrap(new WorldTile(1927, 4427, 0), new WorldTile(1927, 4426, 0));
		else if (x == 1927 && y == 4428)
			releaseTrap(new WorldTile(1927, 4428, 0), new WorldTile(1927, 4429, 0));
		else if (x == 1926 && y == 4428)
			releaseTrap(new WorldTile(1925, 4428, 0), new WorldTile(1926, 4429, 0));
		/**
		 * Seventh room traps
		 */
		else if (x == 1944 && y == 4424)
			releaseTrap(new WorldTile(1943, 4424, 0), new WorldTile(1944, 4423, 0));
		else if (x == 1945 && y == 4424)
			releaseTrap(new WorldTile(1945, 4424, 0), new WorldTile(1945, 4423, 0));
		else if (x == 1945 && y == 4425)
			releaseTrap(new WorldTile(1945, 4425, 0), new WorldTile(1945, 4426, 0));
		else if (x == 1944 && y == 4425)
			releaseTrap(new WorldTile(1943, 4425, 0), new WorldTile(1944, 4426, 0));
		/**
		 * Eighth room traps
		 */
		else if (x == 1974 && y == 4423)
			releaseTrap(new WorldTile(1973, 4423, 0), new WorldTile(1974, 4422, 0));
		else if (x == 1975 && y == 4423)
			releaseTrap(new WorldTile(1975, 4423, 0), new WorldTile(1975, 4422, 0));
		else if (x == 1975 && y == 4424)
			releaseTrap(new WorldTile(1975, 4424, 0), new WorldTile(1975, 4425, 0));
		else if (x == 1974 && y == 4424)
			releaseTrap(new WorldTile(1973, 4424, 0), new WorldTile(1974, 4425, 0));
	}

	private void releaseTrap(WorldTile TrapTile, WorldTile moveTile) {
		if (player.getPlunder().passingTrap)
			return;
		player.applyHit(new Hit(player, Utils.random(5, 20), HitLook.REGULAR_DAMAGE));
		World.sendObjectAnimation(new WorldObject(16517, 10, 2, TrapTile), new Animation(459));
		player.addWalkSteps(moveTile.getX(), moveTile.getY());
		player.sendMessage("The trap releases on you.");

	}

	private void sendNextRoom() {
		player.sendMessage("You find a way to the next room..");
		//resetAllConfigs();
		player.getVarBitManager().sendVarbit(2377, player.getVarBitManager().getBitValue(2377) + 1);
		player.getVarBitManager().sendVarbit(2376, player.getVarBitManager().getBitValue(2376) + 10);
		if (player.getVarBitManager().getBitValue(2377) == 2)
			player.setNextWorldTile(new WorldTile(1954, 4477, 0));
		else if (player.getVarBitManager().getBitValue(2377) == 3)
			player.setNextWorldTile(new WorldTile(1977, 4471, 0));
		else if (player.getVarBitManager().getBitValue(2377) == 4)
			player.setNextWorldTile(new WorldTile(1927, 4453, 0));
		else if (player.getVarBitManager().getBitValue(2377) == 5)
			player.setNextWorldTile(new WorldTile(1965, 4444, 0));
		else if (player.getVarBitManager().getBitValue(2377) == 6)
			player.setNextWorldTile(new WorldTile(1927, 4424, 0));
		else if (player.getVarBitManager().getBitValue(2377) == 7)
			player.setNextWorldTile(new WorldTile(1943, 4421, 0));
		else if (player.getVarBitManager().getBitValue(2377) == 8)
			player.setNextWorldTile(new WorldTile(1974, 4420, 0));
	}

	private void resetAllConfigs() {
		for (int i = 2346; i < 2375; i++)
			player.getVarBitManager().sendVar(i, 0);
		player.getPackets().sendConfig(3422, 0);
		if (player.getPlunder().charmedUrns != null)
			player.getPlunder().charmedUrns.clear();
		if (player.getPlunder().mummy != null)
			player.getPlunder().mummy.disapear();
	}

	public static void PyramidPlunderTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					Entrances.clear();
					Exits.clear();
					for (int i = 0; i < 8; i++)
						Exits.add(Doors[Utils.random(Doors.length)]);
					Entrances.add(Entrance[Utils.random(Entrance.length)]);
					for (Player players : World.getPlayers()) {
						if (players.getControlerManager().getControler() instanceof PyramidPlunderControler)
							players.sendMessage("The pyramid moves. All of the doors have been relocated.");
						if (players.getPlunder().spawnedObjects.size() != 0
								&& players.getControlerManager().getControler() instanceof PyramidPlunderControler)
							for (int i = 0; i < players.getPlunder().spawnedObjects.size(); i++)
								players.getPackets().sendSpawnedObject(
										World.getObject(players.getPlunder().spawnedObjects.get(i), 0));
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 15, TimeUnit.MINUTES);
	}

	public static void sceptreTeleport(Player player, Item item) {
		int sceptre = item.getId() + 2;
		player.lock();
		player.setNextGraphics(new Graphics(715));
		player.setNextAnimation(new Animation(9596));
		player.getInventory().deleteItem(item.getId(), 1);
		player.getInventory().addItem(sceptre, 1);
		player.sendMessage("You use up one of the sceptre's charges and teleport to Pyramid Plunder.");

		WorldTasksManager.schedule(new WorldTask() {
			int loop = 0;

			@Override
			public void run() {
				if (loop == 1) {
					player.setNextWorldTile(new WorldTile(1934, 4458, 2));
					player.unlock();
					if (sceptre == 9050)
						player.sendMessage("That was your last charge.");
					stop();
					return;
				}
				loop++;
			}
		}, 1, 1);
		return;
	}

	private interface Rewards {
		Item getItem();

		int getWeight();
	}

	private enum UrnRewards implements Rewards {
		IVORY_COMB(new Item(9026, 1), 100), STONE_SEAL(new Item(9042, 1), 50), GOLD_SEAL(new Item(9040, 1),
				15), POTTERY_SCARAB(new Item(9032, 1), 75), GOLDEN_SCARAB(new Item(9028, 1), 15), POTTERY_STATUETTE(
						new Item(9036, 1),
						75), STONE_STATUETTE(new Item(9038, 1), 50), GOLDEN_STATUETTE(new Item(9034, 1),
								15), JEWELLED_GOLDEN_STATUETTE(new Item(20661, 1), 3), BLACK_IBIS_MASK(
										new Item(21534, 1), 1), BLACK_IBIS_BODY(new Item(21532, 1), 1), BLACK_IBIS_LEGS(
												new Item(21533, 1), 1), BLACK_IBIS_BOOTS(new Item(21535, 1), 1);

		Item item;
		int weight;

		UrnRewards(Item item, int weight) {
			this.item = item;
			this.weight = weight;
		}

		@Override
		public Item getItem() {
			return item;
		}

		@Override
		public int getWeight() {
			return weight;
		}

	}

	private enum ChestRewards implements Rewards {
		IVORY_COMB(new Item(9026, 1), 100), STONE_SEAL(new Item(9042, 1), 50), GOLD_SEAL(new Item(9040, 1),
				15), POTTERY_SCARAB(new Item(9032, 1), 75), GOLDEN_SCARAB(new Item(9028, 1), 15), POTTERY_STATUETTE(
						new Item(9036, 1),
						75), STONE_STATUETTE(new Item(9038, 1), 50), GOLDEN_STATUETTE(new Item(9034, 1),
								15), JEWELLED_GOLDEN_STATUETTE(new Item(20661, 1), 3), JEWELED_DIAMOND_STATUETTE(
										new Item(21570, 1), 1), SCEPTRE(new Item(9050, 1), 1);

		Item item;
		int weight;

		ChestRewards(Item item, int weight) {
			this.item = item;
			this.weight = weight;
		}

		@Override
		public Item getItem() {
			return item;
		}

		@Override
		public int getWeight() {
			return weight;
		}

	}

	private enum SarcophagusRewards implements Rewards {
		IVORY_COMB(new Item(9026, 1), 100), STONE_SEAL(new Item(9042, 1), 50), GOLD_SEAL(new Item(9040, 1),
				20), POTTERY_SCARAB(new Item(9032, 1), 75), GOLDEN_SCARAB(new Item(9028, 1), 20), POTTERY_STATUETTE(
						new Item(9036, 1), 75), STONE_STATUETTE(new Item(9038, 1), 50), GOLDEN_STATUETTE(
								new Item(9034, 1), 20), JEWELLED_GOLDEN_STATUETTE(new Item(20661, 1),
										5), JEWELED_DIAMOND_STATUETTE(new Item(21570, 1), 3), SCEPTRE(new Item(9050, 1),
												2), BLACK_IBIS_MASK(new Item(21534, 1), 1), BLACK_IBIS_BODY(
														new Item(21532, 1), 1), BLACK_IBIS_LEGS(new Item(21533, 1),
																1), BLACK_IBIS_BOOTS(new Item(21535, 1), 1);

		Item item;
		int weight;

		SarcophagusRewards(Item item, int weight) {
			this.item = item;
			this.weight = weight;
		}

		@Override
		public Item getItem() {
			return item;
		}

		@Override
		public int getWeight() {
			return weight;
		}

	}

	/**
	 * Selects a reward from a weighted list of rewards and gives it to the
	 * player. <br>
	 * <br>
	 * Also sends a world message to all players if the reward is of high
	 * rarity.
	 * 
	 * @param player
	 * @param values
	 */
	private void giveReward(Rewards[] values) {
		List<Item> filler = new ArrayList<>();

		Item reward = null;

		for (Rewards _reward : values) {
			for (int i = 0; i < _reward.getWeight(); i++)
				filler.add(_reward.getItem());
		}

		Collections.shuffle(filler);

		reward = filler.get(Utils.random(0, filler.size() - 1));
		if (reward.getId() != 9050) {
			if (player.getInventory().hasFreeSlots())
				player.getInventory().addItem(reward);
			else
				World.addGroundItem(reward, player, player, false, 60, true);
		}

		if (reward.getId() == ChestRewards.SCEPTRE.getItem().getId()
				|| reward.getId() == SarcophagusRewards.SCEPTRE.getItem().getId()) {
			World.sendWorldMessage("<col=ff0000><img=4>News:</col> " + player.getDisplayName()
					+ " just looted the Pharaoh's Sceptre from Pyramid Plunder!", false);
			player.lock();
			WorldTasksManager.schedule(new WorldTask() {
				int loop = 0;

				@Override
				public void run() {
					if (loop == 0) {
						sendTarget(4477, player);
						player.getPlunder().mummy.setNextForceTalk(new ForceTalk("You! Get out of my pyramid!"));
						Magic.sendNormalTeleportSpell(player, 1, 0, new WorldTile(3288, 2801, 0), null);
						return;
					} else if (loop == 2) {
						if (player.getInventory().hasFreeSlots())
							player.getInventory().addItem(9050, 1);
						else
							World.addGroundItem(new Item(9050, 1), player, player, false, 60, true);
						if (player.getPlunder().mummy != null)
							player.getPlunder().mummy.disapear();
						stop();
					}
					loop++;
				}
			}, 1, 1);
		}
	}

}