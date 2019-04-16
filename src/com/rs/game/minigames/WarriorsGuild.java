package com.rs.game.minigames;

import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.WorldTile;
import com.rs.game.player.controlers.Controler;
import com.rs.game.player.Skills;
import com.rs.game.WorldObject;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.map.doors.DoubleDoors;
import com.rs.game.npc.wguild.AnimatedArmour;
import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;

public class WarriorsGuild extends Controler {
	
	/**
	 * Cylopes Activity
	 */
	private static final int[] DEFENDERS = { 15287, 8850, 8849, 8848, 8847, 8846, 8845, 8844 };
	public static final WorldTile CYCLOPS_LOBBY = new WorldTile(2843, 3535, 2);
	
	/**
	 * Animation Activity (Combat)
	 */
	public static final int[] ARMOR_POINTS = { 5, 10, 15, 20, 50, 60, 80 };
	public static final int[][] ARMOUR_SETS = { { 1155, 1117, 1075 }, { 1153, 1115, 1067 }, { 1157, 1119, 1069 }, { 1165, 1125, 1077 }, { 1159, 1121, 1071 }, { 1161, 1123, 1073 }, { 1163, 1127, 1079 } };
	private static final String[] ARMOUR_TYPE = { "Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Rune" };

	public boolean inCyclopse;
	private int cyclopseOption;
	public transient static int killedCyclopses;
	//boolean inLobby;
	
	@Override
	public void start() {
		int attack = player.getSkills().getLevelForXp(Skills.ATTACK);
		int strength = player.getSkills().getLevelForXp(Skills.STRENGTH);
		if (attack >= 99 || strength >= 99 || attack + strength >= 130) {
			handleEntrance();
		} else {
			player.sendMessage("You need at least level 99 in Attack or Strength, or a combination of");
			player.sendMessage("of both to add up to at least 130 total.");
			this.removeControler();
		}
	}
	
	@Override
	public void magicTeleported(int type) {
		player.getControlerManager().forceStop();
	}
	
	@Override
	public boolean login() {
		if (player.getPlane() == 2) {
			inCyclopse = false;
			player.setNextWorldTile(new WorldTile(2845, 3540, 2));
		}
		return false;
	}

	@Override
	public boolean logout() {
		//resetKegBalance();
		//amountOfPlayers--;
		this.setArguments(new Object[] { this.inCyclopse, this.cyclopseOption });
		return false;
	}
	
	@Override
	public boolean handleItemOnObject(final WorldObject object, Item item) {
		if (object.getId() == 15621) {
			if (player.getTemporaryAttributtes().get("animator_spawned") != null) {
				player.getPackets().sendGameMessage("You are already in combat with an animation.");
				return false;
			}
			int realIndex = getIndex(item.getId());
			if (realIndex == -1)
				return false;
			player.lock();
			final int finalIndex = realIndex;
			final boolean running = player.getRun();
			WorldTasksManager.schedule(new WorldTask() {
				int ticks;

				@Override
				public void run() {
					ticks++;
					if (ticks == 0)
						player.faceObject(object);
					else if (ticks == 1) {
						player.setRunHidden(false);
						for (int armor : ARMOUR_SETS[realIndex]) {
						player.getInventory().deleteItem(armor, 1);
						}
						player.setNextAnimation(new Animation(827));
						player.getDialogueManager().startDialogue("WGuildAnimator");
					} else if (ticks == 2) {
						player.addWalkSteps(player.getX(), player.getY() + 3);
					} else if (ticks == 3) {
						player.faceObject(object);
						player.getDialogueManager().finishDialogue();
						player.setRunHidden(running);
					} else if (ticks == 5) {
						AnimatedArmour npc = new AnimatedArmour(player, 4278 + finalIndex, object, -1, true);
						npc.setRun(false);
						npc.setNextForceTalk(new ForceTalk("IM ALIVE!"));
						npc.setNextAnimation(new Animation(4166));
						npc.getCombat().setTarget(player);
						npc.addWalkSteps(player.getX(), player.getY() + 2);
						player.getHintIconsManager().addHintIcon(npc, 1, -1, false);
						player.getTemporaryAttributtes().put("animator_spawned", true);
					} else if (ticks == 6) {
						player.unlock();
						stop();
						return;
					}
				}
			}, 1, 1);
			return false;
		}
		return true;
	}
	
	private int getIndex(int checkedId) {
		for (int i = 0; i < ARMOUR_SETS.length; i++) {
			for (int j = 0; j < ARMOUR_SETS[i].length; j++) {
				if (ARMOUR_SETS[i][j] == checkedId) {
					for (int k = 0; k < 3; k++) {
						if (!player.getInventory().containsItem(ARMOUR_SETS[i][k], 1)) {
							player.getPackets().sendGameMessage("You need a full set of " + ARMOUR_TYPE[i] + " to use the animator.");
							return -1;
						}
					}
					return i;
				}
			}
		}
		return -1;
	}
	
	public static int getBestDefender(Player player) {
		for (int index = 0; index < DEFENDERS.length; index++) {
			if (player.getEquipment().getShieldId() == DEFENDERS[index] || player.getInventory().containsItem(DEFENDERS[index], 1))
				return DEFENDERS[index - 1 < 0 ? 0 : index - 1];
		}
		return DEFENDERS[7];
	}
	
	private void handleEntrance() {
		WorldObject door = World.getObject(new WorldTile(2877, 3546, 0), 0);
		WorldObject door2 = new WorldObject(15654, 0, 1, 2876, 3546, 0);
		if (door != null) {
			WorldTasksManager.schedule(new WorldTask() {
				int loop;
				@Override
				public void run() {
					if (loop == 0) {
						World.removeObject(door, true);
						World.spawnObject(door2, true);
						player.addWalkSteps(player.getX() > 2876 ? 2876 : 2877, 3546, 1, false);
					} else if (loop == 1) {
						World.removeObject(door2, true);
						World.spawnObject(door, true);
						if (player.getX() >= 2877)
							forceClose();
						stop();
					}
					loop++;
				}
			}, 0, 1);
		}
	}
	
	private void enterCyclopse() {
		WorldTasksManager.schedule(new WorldTask() {
				int loop;
				@Override
				public void run() {
					if (loop == 0) {
						inCyclopse = true;
					} else if (loop == 1) {
						if(!player.getInventory().containsItem(8851, 10)) {
						player.setNextWorldTile(new WorldTile(2845, 3540, 2));
						player.getDialogueManager().startDialogue("SimpleMessage", "You ran out of warrior guild tokens.");
						} else 
						player.getInventory().deleteItem(8851, 10);
						player.getPackets().sendGameMessage("10 of your warrior guild tokens crumble away.");
						loop = 0;
					}
					loop++;
				}
			}, 0, 120); //1 minute
		}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 38012 && object.getPlane() == 1) {
			player.getDialogueManager().startDialogue("ClimbStairsD");
		}
		if (object.getId() == 15638 && object.getPlane() == 2) {
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getPlane() - 1));
		}
		if (object.getId() == 15647 || object.getId() == 15641 && object.getPlane() == 2 || object.getId() == 15644 && object.getPlane() == 2) {
			if(!player.getInventory().containsItem(8851, 10)) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You must have 10 or more warrior guild tokens to enter.");
			return false;
			}
			if (World.isSpawnedObject(object))
				return false;
			player.lock(2);
			WorldObject opened = new WorldObject(object.getId(), object.getType(), object.getRotation() - 1, object.getX(), object.getY(), object.getPlane());
			World.spawnObjectTemporary(opened, 800);
			if (player.getX() >= object.getX()) {
			player.addWalkSteps(object.getX() - 2, object.getY(), 1, false);
			inCyclopse = false;
			} else 
			player.addWalkSteps(object.getX() + 2, object.getY(), 1, false);
			player.getInventory().deleteItem(8851, 10);
			enterCyclopse();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processObjectClick2(WorldObject object) {
		if(object.getId() == 38012) {
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), 2));
				return false;
			}
		return true;
	}
	
	@Override
	public boolean processObjectClick3(WorldObject object) {
		if(object.getId() == 38012) {
			player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), 0));
				return false;
		}
		return true;
	}
	
	/**
	 * Tokens per tile: 1.3 Energy used(standing): 9 Energy used(stepping): 8
	 * Energy used(spinning): 7 Hitpoints lost when failed: 1 Experience per
	 * tile(Standing Throw): 12.1 Experience per Tile(Stepping Throw): 11.7
	 * Experience per Tile(Spinning Throw): 11.6
	 */

}
