package com.rs.game.player.controlers;

import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Pots;
import com.rs.game.player.controlers.Controler;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.utils.Misc;
import com.rs.utils.Utils;

public class Wilderness extends Controler {

	private boolean showingSkull;

	@Override
	public void start() {
		checkBoosts(player);
	}

	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getLevelForXp(Skills.ATTACK);
		int maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.ATTACK)) {
			player.getSkills().set(Skills.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.STRENGTH);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.STRENGTH)) {
			player.getSkills().set(Skills.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.DEFENCE);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.DEFENCE)) {
			player.getSkills().set(Skills.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.RANGE);
		maxLevel = (int) (level + 5 + (level * 0.1));
		if (maxLevel < player.getSkills().getLevel(Skills.RANGE)) {
			player.getSkills().set(Skills.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.MAGIC);
		maxLevel = level + 7;
		if (maxLevel < player.getSkills().getLevel(Skills.MAGIC)) {
			player.getSkills().set(Skills.MAGIC, maxLevel);
			changed = true;
		}
		if (changed)
			player.getPackets().sendGameMessage(
					"Your extreme potion bonus has been reduced.");
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	@Override
	public boolean keepCombating(Entity target) {
		if (target instanceof NPC)
			return true;
		if (!canAttack(target))
			return false;
		if (target.getAttackedBy() != player
				&& player.getAttackedBy() != target)
			player.setWildernessSkull();
		if (player.getCombatDefinitions().getSpellId() <= 0
				&& Utils.inCircle(new WorldTile(3105, 3933, 0), target, 23)) {
			player.getPackets().sendGameMessage(
					"You can only use magic in the arena.");
			return false;
		}
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.isCanPvp() && !p2.isCanPvp()) {
				player.getPackets().sendGameMessage(
						"That player is not in the wilderness.");
				return false;
			}
			if (Math.abs(player.getSkills().getCombatLevel()
					- p2.getSkills().getCombatLevel()) > getWildLevel(player)) {
				player.getPackets().sendGameMessage(
						"Your level diffrence is too great!");
				player.getPackets().sendGameMessage(
						"You need to move deeper into the Wilderness.");
				return false;
			}
			if (Math.abs(player.getSkills().getCombatLevel()
					- p2.getSkills().getCombatLevel()) > getWildLevel(p2)) {
				player.getPackets().sendGameMessage(
						"Your level diffrence is too great!");
				player.getPackets().sendGameMessage(
						"You need to move deeper into the Wilderness.");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		if (player.getCombatDefinitions().getSpellId() <= 0 && Utils.inCircle(new WorldTile(3105, 3933, 0), target, 23)) {
			player.getPackets().sendGameMessage("You can only use magic in the arena.");
			return false;
		}
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.isCanPvp() && !p2.isCanPvp()) {
				player.getPackets().sendGameMessage(
						"That player is not in the wilderness.");
				return false;
			}
			if (Math.abs(player.getSkills().getCombatLevel()
					- p2.getSkills().getCombatLevel()) > getWildLevel(player)) {
				player.getPackets().sendGameMessage(
						"The difference between your combat level and the combat level of "
								+ p2.getDisplayName() + " is too great.");
				player.getPackets()
						.sendGameMessage(
								"You needs to move deeper into the Wilderness before you can attack him.");
				return false;
			}
			if (Math.abs(player.getSkills().getCombatLevel()
					- p2.getSkills().getCombatLevel()) > getWildLevel(p2)) {
				player.getPackets().sendGameMessage(
						"The difference between your combat level and the combat level of "
								+ p2.getDisplayName() + " is too great.");
				player.getPackets()
						.sendGameMessage(
								"He needs to move deeper into the Wilderness before you can attack him.");
				return false;
			}
			if (canHit(target))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Entity target) {
		if (target instanceof NPC)
			return true;
		if (player.getCombatDefinitions().getSpellId() <= 0
				&& Utils.inCircle(new WorldTile(3105, 3933, 0), target, 23)) {
			player.getPackets().sendGameMessage(
					"You can only use magic in the arena.");
			return false;
		}
		Player p2 = (Player) target;
		if (Math.abs(player.getSkills().getCombatLevel()
				- p2.getSkills().getCombatLevel()) > getWildLevel(player))
			return false;
		if (Math.abs(player.getSkills().getCombatLevel()
				- p2.getSkills().getCombatLevel()) > getWildLevel(p2))
			return false;
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (getWildLevel(player) > 21) {
			player.getPackets().sendGameMessage(
					"You can't teleport above level 20 wilderness.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;

	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		if (getWildLevel(player) > 21) {
			player.getPackets().sendGameMessage(
					"You can't teleport above level 20 wilderness.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You are teleblocked for another "
							+ player.getTeleBlockTimeleft() + ".");
			return false;
		}
		return true;
	}

	@Override
	public boolean processJewerlyTeleport(WorldTile toTile) {
		if (getWildLevel(player) > 31) {
			player.getPackets().sendGameMessage(
					"You can't teleport above level 30 wilderness.");
			return false;
		}
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You are teleblocked for another "
							+ player.getTeleBlockTimeleft() + ".");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		if (player.getTeleBlockDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You are teleblocked for another "
							+ player.getTeleBlockTimeleft() + ".");
			return false;
		}
		return true;
	}

	public void showSkull() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 5 : 17, 381);	
	}

	public static boolean isDitch(int id) {
		return id >= 23271 && id <= 23275 || id >= 65076 && id <= 65087;
		//return id == 23271;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (isDitch(object.getId())) {
			player.lock();
			player.setNextAnimation(new Animation(6132));
			final WorldTile toTile = new WorldTile(object.getRotation() == 1
					|| object.getRotation() == 3 ? object.getX() + 2
					: player.getX(), object.getRotation() == 0
					|| object.getRotation() == 2 ? object.getY() - 1
					: player.getY(), object.getPlane());

			player.setNextForceMovement(new ForceMovement(
					new WorldTile(player),
					1,
					toTile,
					2,
					object.getRotation() == 0 || object.getRotation() == 2 ? ForceMovement.SOUTH
							: ForceMovement.EAST));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(toTile);
					player.faceObject(object);
					removeIcon();
					removeControler();
					player.resetReceivedDamage();
					player.unlock();
				}
			}, 2);
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		return true;
	}

	@Override
	public void sendInterfaces() {
		if (isAtWild(player))
			showSkull();
	}

	@Override
	public boolean sendDeath() {
		final Player instance = player;
		player.resetWalkSteps();
		player.lock(7);
		player.setNextAnimation(new Animation(836));
		if (player.getFamiliar() != null)
			player.getFamiliar().sendDeath(player);
	        final NPC index = new NPC(2862, new WorldTile(player.getX() + 1, player.getY() + 1, 0), -1, false);
			index.setNextAnimation(new Animation(380));
			index.setNextFaceEntity(player);
			index.setNextForceTalk(new ForceTalk(randomDeath(player)));
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 2) {
					Player killer = player.getMostDamageReceivedSourcePlayer();
					if (killer != null) {
						player.sendItemsOnDeath(killer, true);
						killer.PKP += 1;
						killer.getPackets().sendGameMessage("You now have "+ killer.PKP +" Pk Points.");
						killer.increaseKillCount(player);
						System.out.println("Wilderness: "
								+ killer.getUsername() + " "
								+ player.getUsername() + "");
						System.out.println("Wilderness: " + killer == null);
					} else
						player.sendItemsOnDeath(instance, true);
					player.getEquipment().init();
					player.getInventory().init();
					player.reset();
					player.setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
					player.setNextAnimation(new Animation(-1));
					removeIcon();
					removeControler();
				} else if (loop == 3) {
					player.getPackets().sendMusic(90);
	                index.setFinished(true);
	                World.removeNPC(index);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	public static String randomDeath(Player p) {
		switch (Misc.random(8)) {
		case 0:
			return "There is no escape, " + p.getDisplayName()
					+ "...";
		case 1:
			return "Muahahahaha!";
		case 2:
			return "You belong to me!";
		case 3:
			return "Beware mortals, " + p.getDisplayName()
					+ " travels with me!";
		case 4:
			return "Your time here is over, " + p.getDisplayName()
					+ "!";
		case 5:
			return "Now is the time you die, " + p.getDisplayName()
					+ "!";
		case 6:
			return "I claim " + p.getDisplayName() + " as my own!";
		case 7:
			return "" + p.getDisplayName() + " is mine!";
		case 8:
			return "Let me escort you to Edgeville, "
					+ p.getDisplayName() + "!";
		case 9:
			return "I have come for you, " + p.getDisplayName()
					+ "!";
		}
		return "";
	}
	
	@Override
	public void moved() {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe(player);
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setCanPvp(true);
			showSkull();
			player.getAppearence().generateAppearenceData();
			checkBoosts(player);
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon();
		} else if (!isAtWildSafe && !isAtWild) {
			player.setCanPvp(false);
			removeIcon();
			removeControler();
		}
	}

	public void removeIcon() {
		if (showingSkull) {
			showingSkull = false;
			player.setCanPvp(false);
			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 5 : 17);
			player.getAppearence().generateAppearenceData();
			player.getEquipment().refresh(null);
		}
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public void forceClose() {
		removeIcon();
	}

	public static final boolean isAtWild(WorldTile tile) {
		return (tile.getX() >= 3011 && tile.getX() <= 3132
				&& tile.getY() >= 10052 && tile.getY() <= 10175)
				|| (tile.getX() >= 2940 && tile.getX() <= 3395
						&& tile.getY() >= 3525 && tile.getY() <= 4000)
				|| (tile.getX() >= 3264 && tile.getX() <= 3279
						&& tile.getY() >= 3279 && tile.getY() <= 3672)
				|| (tile.getX() >= 2756 && tile.getX() <= 2875
						&& tile.getY() >= 5512 && tile.getY() <= 5627)
				|| (tile.getX() >= 3158 && tile.getX() <= 3181
						&& tile.getY() >= 3679 && tile.getY() <= 3697)
				|| (tile.getX() >= 3280 && tile.getX() <= 3183
						&& tile.getY() >= 3885 && tile.getY() <= 3888)
				|| (tile.getX() >= 3012 && tile.getX() <= 3059
						&& tile.getY() >= 10303 && tile.getY() <= 10351)
				|| (tile.getX() >= 3060 && tile.getX() <= 3072
						&& tile.getY() >= 10251 && tile.getY() <= 10263);
	}

	public static boolean isAtWildSafe(Player player) {
		return (player.getX() >= 2940 && player.getX() <= 3395
				&& player.getY() <= 3524 && player.getY() >= 3523
				|| player.getX() >= 2327 && player.getX() <= 2332
				&& player.getY() >= 3686 && player.getY() <= 3693
				|| player.getX() >= 2994 && player.getX() <= 3030
				&& player.getY() >= 3526 && player.getY() <= 3533
				|| player.getX() >= 3005 && player.getX() <= 3025
				&& player.getY() >= 3534 && player.getY() <= 3543 || player
				.getX() >= 3001
				&& player.getX() <= 3004
				&& player.getY() >= 3534 && player.getY() <= 3538);
	}

	public static int getWildLevel(Player player) {
		if ((player.getX() >= 3060 && player.getX() <= 3072
				&& player.getY() >= 10251 && player.getY() <= 10263))
			return 42;
		if (player.getY() > 9900)
			return (player.getY() - 9912) / 8 + 1;
		return (player.getY() - 3520) / 8 + 1;
	}
}
