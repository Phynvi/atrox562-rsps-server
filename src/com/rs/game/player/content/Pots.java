package com.rs.game.player.content;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
//import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.controlers.CrucibleControler;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public final class Pots {

	public static enum Pot {

		ATTACK_POTION(new int[] { 2428, 121, 123, 125 }, Effects.ATTACK_POTION),

		STRENGTH_POTION(new int[] { 113, 115, 117, 119 }, Effects.STRENGTH_POTION),

		DEFENCE_POTION(new int[] { 2432, 133, 135, 137 }, Effects.DEFENCE_POTION),

		RANGE_POTION(new int[] { 2444, 169, 171, 173 }, Effects.RANGE_POTION),

		MAGIC_POTION(new int[] { 3040, 3042, 3044, 3046 }, Effects.MAGIC_POTION),

		ANTI_POISION(new int[] { 2446, 175, 177, 179 }, Effects.ANTIPOISON),

		PRAYER_POTION(new int[] { 2434, 139, 141, 143 }, Effects.PRAYER_POTION),

		SUPER_ATT_POTION(new int[] { 2436, 145, 147, 149 }, Effects.SUPER_ATT_POTION),

		SUPER_STR_POTION(new int[] { 2440, 157, 159, 161 }, Effects.SUPER_STR_POTION),

		SUPER_DEF_POTION(new int[] { 2442, 163, 165, 167 }, Effects.SUPER_DEF_POTION),

		ENERGY_POTION(new int[] { 3008, 3010, 3012, 3014 }, Effects.ENERGY_POTION),

		SUPER_ENERGY(new int[] { 3016, 3018, 3020, 3022 }, Effects.SUPER_ENERGY),

		SUPER_RESTORE_POTION(new int[] { 3024, 3026, 3028, 3030 }, Effects.SUPER_RESTORE),

		SARADOMIN_BREW(new int[] { 6685, 6687, 6689, 6691 }, Effects.SARADOMIN_BREW),

		ZAMORAK_BREW(new int[] { 2450, 189, 191, 193 }, Effects.ZAMORAK_BREW),

		ANTI_FIRE(new int[] { 2452, 2454, 2456, 2458 }, Effects.ANTI_FIRE),

		SUMMONING_POTION(new int[] { 12140, 12142, 12144, 12146 }, Effects.SUMMONING_POT),

		SANFEW_SERUM(new int[] { 10925, 10927, 10929, 10931 }, Effects.SANFEW_SERUM);

		private int[] id;
		private Effects effect;

		private Pot(int[] id, Effects effect) {
			this.id = id;
			this.effect = effect;
		}
		
		public boolean isPotion() {
		    return getMaxDoses() == 4;
		}

		public int getMaxDoses() {
		    return id.length;
		}

		public int getIdForDoses(int doses) {
		    return id[getMaxDoses() - doses];
		}
	}

	private enum Effects {
		ATTACK_POTION(Skills.ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		FISHING_POTION(Skills.FISHING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (level + 3);
			}
		},
		ZAMORAK_BREW(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE) {

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				if (skillId == Skills.ATTACK) {
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return (int) (level + 2 + (realLevel * 0.2));
				} else if (skillId == Skills.STRENGTH) {
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return (int) (level + 2 + (realLevel * 0.12));
				} else {
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return (int) (level + 2 + (realLevel * 0.1));
				}
			}

			@Override
			public void extra(Player player) {
				int hitpointsModification = (int) (player.getMaxHitpoints() * 0.1) + 2;
				player.heal((-1) * hitpointsModification, 0);
			}

		},
		SANFEW_SERUM(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGE, Skills.AGILITY,
				Skills.COOKING, Skills.CRAFTING, Skills.FARMING, Skills.FIREMAKING, Skills.FISHING, Skills.FLETCHING,
				Skills.HERBLORE, Skills.MINING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.SMITHING, Skills.THIEVING,
				Skills.WOODCUTTING, Skills.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel)
					return actualLevel;
				if (actualLevel + boost > realLevel)
					return realLevel;
				return actualLevel + boost;
			}

			@Override
			public void extra(Player player) {
				player.getSkills().restorePrayer((int) (player.getSkills().getLevelForXp(Skills.PRAYER) * 0.33));

				player.addPoisonImmune(180000);
				// TODO DISEASE HEALING
			}

		},
		SUMMONING_POT(Skills.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int restore = (int) (Math.floor(player.getSkills().getLevelForXp(Skills.SUMMONING) * 0.25) + 7);
				if (actualLevel + restore > realLevel)
					return realLevel;
				return actualLevel + restore;
			}

			@Override
			public void extra(Player player) {
			}
		},
		ANTIPOISON() {
			@Override
			public void extra(Player player) {
				player.addPoisonImmune(180000);
				player.getPackets().sendGameMessage("You are now immune to poison.");
			}
		},
		SUPER_ANTIPOISON() {
			@Override
			public void extra(Player player) {
				player.addPoisonImmune(360000);
				player.getPackets().sendGameMessage("You are now immune to poison.");
			}
		},
		ENERGY_POTION() {
			@Override
			public void extra(Player player) {
				int restoredEnergy = player.getRunEnergy() + 20;
				player.setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
			}
		},
		SUPER_ENERGY() {
			@Override
			public void extra(Player player) {
				int restoredEnergy = player.getRunEnergy() + 40;
				player.setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
			}
		},
		ANTI_FIRE() {
			@Override
			public void extra(final Player player) {
				player.addFireImmune(360000);
				final long current = player.getFireImmune();
				player.getPackets().sendGameMessage("You are now immune to dragonfire.");
				WorldTasksManager.schedule(new WorldTask() {
					boolean stop = false;

					@Override
					public void run() {
						if (current != player.getFireImmune()) {
							stop();
							return;
						}
						if (!stop) {
							player.getPackets()
									.sendGameMessage("<col=480000>Your antifire potion is about to run out...</col>");
							stop = true;
						} else {
							stop();
							player.getPackets()
									.sendGameMessage("<col=480000>Your antifire potion has ran out...</col>");
						}
					}
				}, 500, 100);
			}
		},
		STRENGTH_POTION(Skills.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		DEFENCE_POTION(Skills.DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		RANGE_POTION(Skills.RANGE) {

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.1));
			}
		},
		MAGIC_POTION(Skills.MAGIC) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 5;
			}
		},
		PRAYER_POTION() {
			@Override
			public void extra(Player player) {
				player.getSkills()
						.restorePrayer(((int) (Math.floor(player.getSkills().getLevelForXp(Skills.PRAYER) / 4) + 7)));
			}
		},
		SUPER_STR_POTION(Skills.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_DEF_POTION(Skills.DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_ATT_POTION(Skills.ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SARADOMIN_BREW("You drink some of the foul liquid.", Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH,
				Skills.MAGIC, Skills.RANGE) {

			@Override
			public boolean canDrink(Player player) {
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				if (skillId == Skills.DEFENCE) {
					int boost = (int) (realLevel * 0.25);
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return level + boost;
				} else {
					return (int) (actualLevel * 0.90);
				}
			}

			@Override
			public void extra(Player player) {
				int hitpointsModification = (int) (player.getMaxHitpoints() * 0.15);
				player.heal(hitpointsModification + 20, hitpointsModification);
			}
		},
		SUPER_RESTORE(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGE, Skills.AGILITY,
				Skills.COOKING, Skills.CRAFTING, Skills.FARMING, Skills.FIREMAKING, Skills.FISHING, Skills.FLETCHING,
				Skills.HERBLORE, Skills.MINING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.SMITHING, Skills.THIEVING,
				Skills.WOODCUTTING, Skills.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel)
					return actualLevel;
				if (actualLevel + boost > realLevel)
					return realLevel;
				return actualLevel + boost;
			}

			@Override
			public void extra(Player player) {
				player.getSkills().restorePrayer((int) (player.getSkills().getLevelForXp(Skills.PRAYER) * 0.33));
			}

		},
		RESTORE_POTION(Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE, Skills.PRAYER) {

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel)
					return actualLevel;
				if (actualLevel + boost > realLevel)
					return realLevel;
				return actualLevel + boost;
			}
		};

		private int[] affectedSkills;
		private String drinkMessage;

		private Effects(int... affectedSkills) {
			this(null, affectedSkills);
		}

		private Effects(String drinkMessage, int... affectedSkills) {
			this.drinkMessage = drinkMessage;
			this.affectedSkills = affectedSkills;
		}

		public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
			return actualLevel;
		}

		public boolean canDrink(Player player) {
			return true;
		}

		public void extra(Player player) {
		}
	}

	public static Pot getPot(int id) {
		for (Pot pot : Pot.values())
			for (int potionId : pot.id) {
				if (id == potionId)
					return pot;
			}
		return null;
	}
	
	public static int getDoses(Pot pot, Item item) {
		for (int i = pot.id.length - 1; i >= 0; i--) {
			if (pot.id[i] == item.getId())
				return pot.id.length - i;
		}
		return 0;
	}


	public static boolean pot(Player player, Item item, int slot) {
		Pot pot = getPot(item.getId());
		if (pot == null)
			return false;
		if (player.getPotDelay() > Utils.currentTimeMillis())
			return true;
		if (!player.getControlerManager().canPot(pot))
			return true;
		if (!pot.effect.canDrink(player))
			return true;
		player.addPotDelay(1075);
		String name = item.getDefinitions().getName();
		int index = name.indexOf("(");
		int dosesLeft = 0;
		if (index != -1)
			dosesLeft = Integer.parseInt(name.substring(index).replace("(", "").replace(")", "")) - 1;
		int toPot = pot.id.length - dosesLeft;
		player.getInventory().getItems().set(slot,
				new Item(dosesLeft > 0 && toPot < pot.id.length ? pot.id[toPot] : 229, 1));
		player.getInventory().refresh(slot);
		for (int skillId : pot.effect.affectedSkills)
			player.getSkills().set(skillId, pot.effect.getAffectedSkill(player, skillId,
					player.getSkills().getLevel(skillId), player.getSkills().getLevelForXp(skillId)));
		pot.effect.extra(player);
		player.setNextAnimation(new Animation(829));
		player.getPackets().sendSound(4580, 0);
		player.getPackets().sendGameMessage(
				pot.effect.drinkMessage != null ? pot.effect.drinkMessage
						: "You drink some of your " + name.toLowerCase().replace(" (1)", "").replace(" (2)", "")
								.replace(" (3)", "").replace(" (4)", "").replace(" (5)", "").replace(" (6)", "") + ".",
				true);
		player.getPackets().sendGameMessage(
				dosesLeft == 0 ? "You have finished your potion." : "You have " + dosesLeft + " dose of potion left.",
				true);
		return true;
	}

	public static void lunarPotRestoreShare(Player player, Item item) {
		Pot pot = getPot(item.getId());
		if (pot == null)
			return;
		if (player.getPotDelay() > Utils.currentTimeMillis())
			return;
		if (!player.getControlerManager().canPot(pot))
			return;
		if (!pot.effect.canDrink(player))
			return;
		player.addPotDelay(1075);
		for (int skillId : pot.effect.affectedSkills)
			player.getSkills().set(skillId, pot.effect.getAffectedSkill(player, skillId,
					player.getSkills().getLevel(skillId), player.getSkills().getLevelForXp(skillId)));
		pot.effect.extra(player);
		return;
	}

	public static int VIAL = 229;
    public static boolean mixPot(Player player, Item fromItem, Item toItem,  int fromSlot, int toSlot) {
    	if (fromItem.getId() == VIAL || toItem.getId() == VIAL) {
    	    Pot pot = getPot(fromItem.getId() == VIAL ? toItem.getId() : fromItem.getId());
    	    if (pot == null)
    	    	return false;
    	    if (fromSlot == toSlot)
    			return false;
    	    int doses = getDoses(pot, fromItem.getId() == VIAL ? toItem : fromItem);
    	    if (doses == 1) {
    	    	player.getInventory().switchItem(fromSlot, toSlot);
    	    	player.getPackets().sendGameMessage("You pour from one container into the other.", true);
    	    	return true;
    	    }
    	    int vialDoses = doses / 2;
    	    doses -= vialDoses;
    	    player.getInventory().getItems().set(fromItem.getId() == VIAL ? toSlot : fromSlot, new Item(pot.getIdForDoses(doses), 1));
    	    player.getInventory().getItems().set(fromItem.getId() == VIAL ? fromSlot : toSlot, new Item(pot.getIdForDoses(vialDoses), 1));
    	    player.getInventory().refresh(fromSlot);
    	    player.getInventory().refresh(toSlot);
    	    player.getPackets().sendGameMessage("You split the potion between the two vials.", true);
    	    return true;
    	}
    	Pot pot = getPot(fromItem.getId());
    	if (pot == null)
    	    return false;
        if (fromSlot == toSlot)
    		return false;
    	int doses2 = getDoses(pot, toItem);
    	if (doses2 == 0 || doses2 == pot.getMaxDoses())
    	    return false;
    	int doses1 = getDoses(pot, fromItem);
    	doses2 += doses1;
    	doses1 = doses2 > pot.getMaxDoses() ? doses2 - pot.getMaxDoses() : 0;
    	doses2 -= doses1;
    	if (doses1 == 0) {
    		player.getInventory().getItems().set(fromSlot, new Item(229, 1));
    		player.getInventory().refresh(fromSlot);
    	} else {
    	    player.getInventory().getItems().set(fromSlot, new Item(doses1 > 0 ? pot.getIdForDoses(doses1) : VIAL, 1));
    	    player.getInventory().refresh(fromSlot);
    	}
    	player.getInventory().getItems().set(toSlot, new Item(pot.getIdForDoses(doses2), 1));
    	player.getInventory().refresh(toSlot);
    	player.getPackets().sendGameMessage("You pour from one container into the other.");
    	return true;
        }

	private Pots() {

	}
}
