package com.rs.game.player;

import java.io.Serializable;

import com.rs.Settings;
import com.rs.game.Hit;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils;
import com.rs.game.World;
import com.rs.game.player.dialogues.LevelUp;
import com.rs.game.minigames.bountyhunter.BountyHunter;

public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;
	
	public static final double MAXIMUM_EXP = 200000000;
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5,
			MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18,
			FARMING = 19, RUNECRAFTING = 20, HUNTER = 21, CONSTRUCTION = 22, SUMMONING = 23;
	
	public static final String[] SKILL_NAME = { "Attack", "Defence",
		"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
		"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting", "Hunter", "Construction", "Summoning" };
	
	public short level[];
	public double xp[];
	//public int level[] = new int[SKILL_COUNT];
	public static final int SKILL_COUNT = 24;
	
	private transient Player player;
	
	
	public Skills() {
		level = new short[24];
		xp = new double[24];
		for(int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
	}
	
	public void restoreSkills() {
		for(int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}
	
	/*public int getTotalLevel() {
		int totallevel = 0;
		for (int i = 0; i <= 24; i++) {
			totallevel += getLevelForXp(i);
		}
		return totallevel;
	}*/
	
	public long getTotalXp() {
		long totalxp = 0;
		for (double xp : getXp()) {
			totalxp += xp;
		}
		return totalxp;
	}
	
	public void drainPrayer(int amount) {
		int level = getLevel(PRAYER);
		set(PRAYER, level - amount > 0 ? level - amount : 0);
	}
	
	
	public void restorePrayer(int amount) {
		int level = getLevel(PRAYER);
		int realLevel = getLevelForXp(PRAYER);
		set(PRAYER, level + amount >= realLevel ? realLevel : level + amount);
		//player.getPackets().sendSkillLevels();
		player.getAppearence().generateAppearenceData();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public int getLevel(int skill) {
		return level[skill];
	}
	
	public double getXp(int skill) {
		return xp[skill];
	}
	
	public double[] getXp() {
		return xp;
	}
	
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1; 
		double melee = (attack + strength) * 0.325; 
		double ranger = Math.floor(ranged * 1.5) * 0.325; 
		double mage = Math.floor(magic * 1.5) * 0.325; 
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}
	
	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}
	
	public int getCombatLevelWithSummoning() {
		return getCombatLevel()+getSummoningCombatLevel();
	}
	
	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}
	
    public static int getXPForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
       }
       return 0;
    }
    

	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}
	public void init() {
		for(int skill = 0; skill < level.length; skill++)
			refresh(skill);
	}
    
	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
	}
	
	public int getTotalLevel() {
		int totallevel = 0;
		for (int i = 0; i <= 23; i++)
			totallevel += getLevelForXp(i);
		return totallevel;
	}
	
	private static int[] milestones = { 50, 100, 150, 250, 500, 750, 1000, 1250, 1500, 1750, 2000, 2250, 2376 };
	
	public void addXp(int skill, double exp) {
		player.getControlerManager().trackXP(skill, (int) exp);
		exp *= 15;
		int oldLevel = getLevelForXp(skill);
		int oldXP = (int) xp[skill];
		int oldCombat = getCombatLevelWithSummoning();
		xp[skill] += exp;
		if(xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if(newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if(skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC))
				player.getAppearence().generateAppearenceData();
			if (oldCombat < getCombatLevelWithSummoning())
					player.sendMessage("<col=ff0000>You've just advanced a Combat level. You have reached level "
							+ getCombatLevelWithSummoning() + ".");
		for (int achievements : milestones) {
				if (getTotalLevel() == achievements)
					World.sendWorldMessage("<col=ff6600><img=4>Milestone: "+ player.getDisplayName() +" has achieved Total lvl: "+ achievements +".", false);
		}
		}
		if (oldXP < 104273167 && xp[skill] > 104273167){
			LevelUp.send104m(player, skill);
			if (skill == WOODCUTTING)
				player.getBank().addItem(15286, 120, true);
			else if (skill == THIEVING)
				player.getBank().addItem(15285, 120, true);
			else if (skill == SUMMONING)
				player.getBank().addItem(15284, 120, true);
			else if (skill == STRENGTH)
				player.getBank().addItem(15283, 120, true);
			else if (skill == SMITHING)
				player.getBank().addItem(15282, 120, true);
			else if (skill == SLAYER)
				player.getBank().addItem(15281, 120, true);
			else if (skill == RUNECRAFTING)
				player.getBank().addItem(15280, 120, true);
			else if (skill == RANGE)
				player.getBank().addItem(15279, 120, true);
			else if (skill == PRAYER)
				player.getBank().addItem(15278, 120, true);
			else if (skill == MINING)
				player.getBank().addItem(15277, 120, true);
			else if (skill == MAGIC)
				player.getBank().addItem(15276, 120, true);
			else if (skill == HUNTER)
				player.getBank().addItem(15275, 120, true);
			else if (skill == HITPOINTS)
				player.getBank().addItem(15274, 120, true);
			else if (skill == HERBLORE)
				player.getBank().addItem(15273, 120, true);
			else if (skill == FLETCHING)
				player.getBank().addItem(15272, 120, true);
			else if (skill == FISHING)
				player.getBank().addItem(15271, 120, true);
			else if (skill == FIREMAKING)
				player.getBank().addItem(15270, 120, true);
			else if (skill == FARMING)
				player.getBank().addItem(15269, 120, true);
			else if (skill == DEFENCE)
				player.getBank().addItem(15268, 120, true);
			else if (skill == CRAFTING)
				player.getBank().addItem(15267, 120, true);
			else if (skill == COOKING)
				player.getBank().addItem(15266, 120, true);
			else if (skill == CONSTRUCTION)
				player.getBank().addItem(15265, 120, true);
			else if (skill == ATTACK)
				player.getBank().addItem(15264, 120, true);
			else if (skill == AGILITY)
				player.getBank().addItem(15263, 120, true);
		}
		refresh(skill);
	}


	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

	public void resetSkillNoRefresh(int skill) {
		xp[skill] = 0;
		level[skill] = 1;
	}

}
