package com.rs.game.player.skills.thieving;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.npc.NPC;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.utils.Utils;

/**
 * Handles the pick pocketing.
 */
public class PickPocketAction extends Action {

	/**
	 * Pick pocketing npc.
	 */
	private NPC npc;

	/**
	 * Data of an npc.
	 */
	private PickPocketableNPC npcData;

	/**
	 * The npc stun animation.
	 */
	private static final Animation STUN_ANIMATION = new Animation(422),

			/**
			 * The pick pocketing animation.
			 */
			PICKPOCKETING_ANIMATION = new Animation(881);

	/**
	 * The index to use in the levels required arrays.
	 */
	private int index;

	/**
	 * Constructs a new {@code PickpocketAction} {@code Object}.
	 * 
	 * @param npc
	 *            The npc to whom the player is pickpocketing.
	 * @param npcData
	 *            Data of an npc.
	 */
	public PickPocketAction(NPC npc, PickPocketableNPC npcData) {
		this.npc = npc;
		this.npcData = npcData;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
			int agilityLevel = player.getSkills().getLevel(Skills.AGILITY);
			if (Utils.getRandom(50) < 5) {
				for (int i = 0; i < 4; i++) {
					if (npcData.getThievingLevels()[i] <= thievingLevel
							&& npcData.getAgilityLevels()[i] <= agilityLevel)
						index = i;
				}
			}
			player.faceEntity(npc);
			player.setNextAnimation(PICKPOCKETING_ANIMATION);
			player.getPackets().sendGameMessage(
					"You attempt to pick the " + npc.getDefinitions().name.toLowerCase() + "'s pocket...");
			setActionDelay(player, 3);
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (!isSuccesful(player)) {
			player.getPackets()
					.sendGameMessage("You fail to pick the " + npc.getDefinitions().name.toLowerCase() + "'s pocket.");
			npc.setNextAnimation(null);
			npc.setNextAnimation(STUN_ANIMATION);
			npc.faceEntity(player);
			player.setNextAnimation(new Animation(424));
			player.setNextGraphics(new Graphics(80, 5, 60));
			player.getPackets().sendGameMessage("You've been stunned.");
			player.applyHit(new Hit(player, npcData.getStunDamage(), HitLook.REGULAR_DAMAGE));
			if (npcData.equals(PickPocketableNPC.MASTER_FARMER) || npcData.equals(PickPocketableNPC.FARMER)) {
				npc.setNextForceTalk(new ForceTalk("Cor blimey mate, what are ye doing in me pockets?"));
				player.lock(npcData.getStunTime());
				stop(player);
			} else
			npc.setNextForceTalk(new ForceTalk("What do you think you're doing?"));
			player.lock(npcData.getStunTime());
			stop(player);
		} else {
			player.getPackets().sendGameMessage(
					"You succesfully pick the " + npc.getDefinitions().name.toLowerCase() + "'s pocket.");
			double totalXp = npcData.getExperience();
			player.getSkills().addXp(Skills.THIEVING, (index > 0 ? totalXp * index : totalXp));
			for (int i = 0; i <= index; i++) {
				Item item = npcData.getLoot()[Utils.random(npcData.getLoot().length)];
				if (item != null) {
					if (item.getId() == 995)
						player.getInventory().addItem(item.getId(), item.getAmount());
					else
						player.getInventory().addItem(item.getId(), item.getAmount());
				}
			}
			/*if (player.inArea(3192, 3195, 3260, 3253)) {
				if (npcData.equals(PickPocketableNPC.MAN)) {
					AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
					if (!diary.isComplete(0, 3)) {
						diary.updateTask(player, 0, 3, true);
					}
				}
			}*/
			/*if (player.inArea(3066, 3237, 3089, 3271)) {
				if (npcData.equals(PickPocketableNPC.MASTER_FARMER)) {
					if (!player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE).isComplete(1, 6)) {
						player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE).updateTask(player, 1, 6, true);
					}
				}
			}*/
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		npc.setNextFaceEntity(null);
		setActionDelay(player, 3);
	}

	/**
	 * Checks if the player is succesfull to thiev or not.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True} if succesfull, {@code false} if not.
	 */
	private boolean isSuccesful(Player player) {
		int baseLevel = player.getSkills().getLevel(Skills.THIEVING);
		int boost = getIncreasedChance(player) + 5;
		int random = Utils.getRandom(baseLevel + boost);
		double required = npcData.getThievingLevels()[0];
		double ratio = random / (Utils.random(npcData.getThievingLevels()[0] + 6) + 1);
		double result = ratio * baseLevel;
		return (result >= required);
	}

	/**
	 * Gets the increased chance for succesfully pickpocketing.
	 * 
	 * @param player
	 *            The player.
	 * @return The amount of increased chance.
	 */
	private int getIncreasedChance(Player player) {
		int chance = 0;
		int handId = player.getEquipment().getGlovesId();
		int capeId = player.getEquipment().getCapeId();
		if (handId == 10075)
			chance += 12;
		if (capeId == 20771 || capeId == 20769 || capeId == 29996 || capeId == 15345 || capeId == 15347
				|| capeId == 15349 || capeId == 19748 || capeId == 28301 || capeId == 28302)
			chance += 15;
		if (npc.getDefinitions().name.contains("H.A.M")) {
			for (Item item : player.getEquipment().getItems().getItems()) {
				if (item != null && item.getDefinitions().getName().contains("H.A.M")) {
					chance += 3;
				}
			}
		}
		return chance;
	}

	/**
	 * Checks everything before starting.
	 * 
	 * @param player
	 *            The player.
	 * @return
	 */
	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.THIEVING) < npcData.getThievingLevels()[0]) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a thieving level of " + npcData.getThievingLevels()[0] + " to steal from this npc.");
			return false;
		}
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets().sendGameMessage("You don't have enough space in your inventory.");
			return false;
		}
		if (player.getAttackedBy() != null && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You can't do this while you're under combat.");
			return false;
		}
		if (npc.getAttackedBy() != null && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("The npc is under combat.");
			return false;
		}
		if (npc.isDead()) {
			player.getPackets().sendGameMessage("Too late, the npc is dead.");
			return false;
		}
		if (player.getLockDelay() > Utils.currentTimeMillis() || npc == null || player == null || npcData == null
				|| NPCDefinitions.getNPCDefinitions(npc.getId()) == null)
			return false;
		return true;

	}

}
