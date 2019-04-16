package com.rs.game.player.skills.hunter;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.npc.NPC;
import com.rs.game.WorldTile;
import com.rs.game.player.actions.Action;
import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.game.Animation;
import com.rs.game.player.Skills;

public class Falconry extends Action {

	public enum Kebbits {

		SPOTTED_KEBBIT(5098, 43, 104, new Item(10125, 1)), DARK_KEBBIT(5099, 57, 132,
				new Item(10115, 1)), DASHING_KEBBIT(5100, 69, 156, new Item(10127, 1));

		private int npcId, level, experience;
		private Item loot;

		private static final HashMap<Integer, Kebbits> KEBBITS = new HashMap<>();

		static {
			for (Kebbits k : values()) {
				KEBBITS.put(k.ordinal(), k);
			}
		}

		public static Kebbits getKebbit(int id) {
			return KEBBITS.get(5100 - id);
		}

		Kebbits(int npcId, int level, int experience, Item loot) {
			this.npcId = npcId;
			this.level = level;
			this.experience = experience;
			this.loot = loot;
		}

		public int getNPCID() {
			return npcId;
		}

		public int getLevel() {
			return level;
		}

		public int getExperience() {
			return experience;
		}

		public Item getLoot() {
			return loot;
		}

	}

	private NPC npc;
	private Kebbits prey;
	private WorldTile location;

	public Falconry(NPC npc) {
		this.npc = npc;
		this.prey = Kebbits.getKebbit(npc.getId());
	}

	@Override
	public boolean start(Player player) {
		if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished())
			return false;
		if (player.getEquipment().getWeaponId() == 10023) {
			player.sendMessage("You've already sent your Falcon out to catch the prey.");
			return false;
		} else if (player.getEquipment().getWeaponId() != 10024) {
			player.sendMessage("You need to be wielding a Falconer's glove to catch the kebbits.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.HUNTER) < prey.getLevel()) {
			player.sendMessage("You need at least level " + prey.getLevel() + " Hunter to catch this Kebbit.");
			return false;
		}
		this.location = new WorldTile(npc.getX(), npc.getY(), npc.getPlane());
		return true;
	}

	public static void handleFalcons(NPC npc, Player player) {
		if (!npc.getName().equalsIgnoreCase(player.getDisplayName())) {
			player.sendMessage("This is not your falcon.");
			return;
		}
		player.setNextAnimation(new Animation(7270));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				Kebbits kebbit = Kebbits.getKebbit(npc.getId() + 4);
				player.getSkills().addXp(Skills.HUNTER, kebbit.getExperience());
				player.getHintIconsManager().removeAll();
				player.sendMessage("You retrieve the falcon as well as the fur of the dead kebbit.");
				npc.finish();
				player.getEquipment().resetFalconry();
				if (player.getInventory().getFreeSlots() >= 2) {
					player.getInventory().addItem(kebbit.getLoot());
					player.getInventory().addItem(new Item(526, 1));
				} else {
					if (player.getInventory().hasFreeSlots())
						player.getInventory().addItem(kebbit.getLoot());
					else
						World.addGroundItem(kebbit.getLoot(), new WorldTile(player));
					World.addGroundItem(new Item(526), new WorldTile(player));
				}
			}
		}, 1);

		return;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		int distance;
		int distanceX = Math.abs(player.getX() - npc.getX());
		int distanceY = Math.abs(player.getY() - npc.getY());
		if (distanceX > distanceY)
			distance = distanceX;
		else
			distance = distanceY;
		player.getEquipment().setFalconry();
		World.sendProjectile(player, player, npc, 922, 50, 20, 30, 10, 0, 0);

		CoresManager.slowExecutor.schedule(new Runnable() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					boolean success = false;
					if (location.getX() == npc.getX() && location.getY() == npc.getY())
						success = true;
					if (!success) {
						World.sendProjectile(player, location, player, 922, 50, 20, 30, 10, 0, 0);
						secondHalf(player, distance);
						player.sendMessage("The falcon swoops down on the kebbit, but just misses catching it.");
					} else {
						NPC gyr = new NPC(5094 + prey.ordinal(), npc, -1, false);
						npc.setRespawnTask();
						npc.finish();
						gyr.setLocked(true);
						player.sendMessage("The falcon successfully swoops down and captures the kebbit.");
						gyr.setName(player.getDisplayName());
						player.getHintIconsManager().addHintIcon(gyr, 1, -1, false);
						startRetrievalTask(player, gyr);
					}
				}
			}
		}, (distance) * 200, TimeUnit.MILLISECONDS);
		return -1;
	}

	private void startRetrievalTask(Player player, NPC gyr) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (gyr == null || gyr.isDead() || gyr.hasFinished())
					this.stop();
				if (loop == 30) {
					if (player != null) {
						player.getHintIconsManager().removeUnsavedHintIcon();
						player.sendMessage(
								"Your falcon has left its prey. You see it heading back toward the falconer.");
						if (player.getEquipment().getWeaponId() == 10024
								|| player.getEquipment().getWeaponId() == 10023)
							player.getEquipment().removeFalcon();
					}
					gyr.finish();
				}
				loop++;
			}
		}, 0, 1);
	}

	private void secondHalf(Player player, int distance) {
		CoresManager.slowExecutor.schedule(() -> player.getEquipment().resetFalconry(), (distance) * 180, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop(Player player) {

	}

}
