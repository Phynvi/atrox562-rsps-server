package com.rs.game.player;

import java.io.Serializable;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.utils.Utils;

public class Prayer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2082861520556582824L;
	
	private final static int[][] prayerLvls = {
		// normal prayer book
				{ 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 35,
						37, 40, 43, 44, 45, 46, 49, 52, 60, 70 /*,65, 77*/},
				// ancient prayer book
				{ 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82,
						84, 86, 89, 92, 95 } };
	
	private final static int[][][] closePrayers = { { // normal prayer book
		{ 0, 5, 13 }, // Skin prayers 0
				{ 1, 6, 14 }, // Strength prayers 1
				{ 2, 7, 15 }, // Attack prayers 2
				{ 3, 11, 20 }, // Range prayers 3
				{ 4, 12, 21, 28 }, // Magic prayers 4
				{ 8, 9, 26 }, // Restore prayers 5
				{ 10 }, // Protect item prayers 6
				{ 17, 18, 19 }, // Protect prayers 7
				{ 16 }, // Other protect prayers 8
				{ 22, 23, 24 }, // Other special prayers 9
				{ 25, 26 } // Other prayers 10
		}, { // ancient prayer book
		{ 0 }, // Protect item prayers 0
				{ 1, 2, 3, 4 }, // sap prayers 1
				{ 5 }, // other prayers 2
				{ 7, 8, 9, 17, 18 }, // protect prayers 3
				{ 6 }, // other protect prayers 4
				{ 10, 11, 12, 13, 14, 15, 16 }, // leech prayers 5
				{ 19 }, // other prayers
		} };
	
	private final static int[] prayerSlotValues = { 1, 2, 4, 262144, 524288, 8,
		16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216,
		4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072,
		33554432, /*134217728,*/ 67108864 /*268435456*/ };
		
	public boolean isProtectingItem() {
		return usingPrayer(0, 10);
	}
	
	private transient Player player;
	private transient boolean[][] onPrayers;
	private transient boolean usingQuickPrayer;
	private transient int onPrayersCount;
	private int prayerpoints;
	private boolean[][] quickPrayers;
	private transient int[] leechBonuses;
	private boolean ancientcurses;
	private transient int drainDelay;
	private transient boolean boostedLeech;
	public double getMageMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		
		//normal
		if (usingPrayer(0, 4))
			value += 0.05;
		else if (usingPrayer(0, 12))
			value += 0.10;
		else if (usingPrayer(0, 21))
			value += 0.15;
		else if (usingPrayer(1,3))
			value += leechBonuses[2]/100;
		else if (usingPrayer(1,12))
			value += (5+leechBonuses[5])/100;
		return value;
	}
	
	public double getRangeMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		
		//normal
		if (usingPrayer(0, 3))
			value += 0.05;
		else if (usingPrayer(0, 11))
			value += 0.10;
		else if (usingPrayer(0, 20))
			value += 0.15;
		else if (usingPrayer(1,2))
			value += leechBonuses[1]/100;
		else if (usingPrayer(1,11))
			value += (5+leechBonuses[4])/100;
		return value;
	}
	
	public double getAttackMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		
		//normal
		if (usingPrayer(0, 2))
			value += 0.05;
		else if (usingPrayer(0, 7))
			value += 0.10;
		else if (usingPrayer(0, 15))
			value += 0.15;
		else if (usingPrayer(0, 25))
			value += 0.15;
		else if (usingPrayer(0, 26))
			value += 0.20;
		else if (usingPrayer(1,1))
			value += leechBonuses[0]/100;
		else if (usingPrayer(1,10))
			value += (5+leechBonuses[3])/100;
		else if (usingPrayer(1,19))
			value += (15+leechBonuses[8])/100;
		return value;
	}
	public double getStrengthMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		
		//normal
		if (usingPrayer(0, 1))
			value += 0.05;
		else if (usingPrayer(0, 6))
			value += 0.10;
		else if (usingPrayer(0, 14))
			value += 0.15;
		else if (usingPrayer(0, 25))
			value += 0.18;
		else if (usingPrayer(0, 26))
			value += 0.23;
		else if (usingPrayer(1,1))
			value += leechBonuses[0]/100;
		else if (usingPrayer(1,14))
			value += (5+leechBonuses[7])/100;
		else if (usingPrayer(1,19))
			value += (23+leechBonuses[10])/100;
		return value;
	}
	public double getDefenceMultiplier() {
		if(onPrayersCount == 0)
			return 1.0;
		double value = 1.0;
		
		//normal
		if (usingPrayer(0, 0))
			value += 0.05;
		else if (usingPrayer(0, 5))
			value += 0.10;
		else if (usingPrayer(0, 13))
			value += 0.15;
		else if (usingPrayer(0, 25))
			value += 0.20;
		else if (usingPrayer(0, 26))
			value += 0.25;
		else if (usingPrayer(1,1))
			value += leechBonuses[0]/100;
		else if (usingPrayer(1,13))
			value += (5+leechBonuses[6])/100;
		else if (usingPrayer(1,19))
			value += (15+leechBonuses[9])/100;
		return value;
	}
	
	
	
	public boolean reachedMax(int bonus) {
		if(bonus != 8 && bonus != 9 && bonus != 10)
				return leechBonuses[bonus] >= 20;
			else
				return false;
	}
	
	public void increaseLeechBonus(int bonus) {
			leechBonuses[bonus]++;
	}
	
	public void increaseTurmoilBonus(Player p2) {
		leechBonuses[8] = (int) ((100 * Math.floor(0.15 * p2.getSkills().getLevelForXp(Skills.ATTACK))) / p2.getSkills().getLevelForXp(Skills.ATTACK));
		leechBonuses[9] = (int) ((100 * Math.floor(0.15 * p2.getSkills().getLevelForXp(Skills.DEFENCE))) / p2.getSkills().getLevelForXp(Skills.DEFENCE));
		leechBonuses[10] = (int) ((100 * Math.floor(0.1 * p2.getSkills().getLevelForXp(Skills.STRENGTH))) / p2.getSkills().getLevelForXp(Skills.STRENGTH));
	}
	
	
	
	public void closePrayers(int prayerId) {
		if(ancientcurses) {
			if(prayerId == 1) {
				if(leechBonuses[0] > 0)
					player.getPackets().sendGameMessage("Your Attack is now unaffected by sap and leech curses.", true);
				leechBonuses[0] = 0;
			} else if(prayerId == 2) {
				if(leechBonuses[1] > 0)
					player.getPackets().sendGameMessage("Your Range is now unaffected by sap and leech curses.", true);
				leechBonuses[1] = 0;
			} else if(prayerId == 3) {
				if(leechBonuses[2] > 0)
					player.getPackets().sendGameMessage("Your Magic is now unaffected by sap and leech curses.", true);
				leechBonuses[2] = 0;
			} else if(prayerId == 10) {
				if(leechBonuses[3] > 0)
					player.getPackets().sendGameMessage("Your Attack is now unaffected by sap and leech curses.", true);
				leechBonuses[3] = 0;
			}else if(prayerId == 11) {
				if(leechBonuses[4] > 0)
					player.getPackets().sendGameMessage("Your Ranged is now unaffected by sap and leech curses.", true);
				leechBonuses[4] = 0;
			}else if(prayerId == 12) {
				if(leechBonuses[5] > 0)
					player.getPackets().sendGameMessage("Your Magic is now unaffected by sap and leech curses.", true);
				leechBonuses[5] = 0;
			}else if(prayerId == 13) {
				if(leechBonuses[6] > 0)
					player.getPackets().sendGameMessage("Your Defence is now unaffected by sap and leech curses.", true);
				leechBonuses[6] = 0;
			}else if(prayerId == 14) {
				if(leechBonuses[7] > 0)
					player.getPackets().sendGameMessage("Your Strength is now unaffected by sap and leech curses.", true);
				leechBonuses[7] = 0;
			}else if(prayerId == 19) {
				leechBonuses[8] = 0;
				leechBonuses[9] = 0;
				leechBonuses[10] = 0;
			}
		}
	}
	
	public int getPrayerHeadIcon() {
		if(onPrayersCount == 0)
			return -1;
		int value = -1;
		if (usingPrayer(0, 16))
			value += 8;
		if (usingPrayer(0, 17))
			value += 3;
		else if (usingPrayer(0, 18))
			value += 2;
		else if (usingPrayer(0, 19))
			value += 1;
		else if (usingPrayer(0, 22))
			value += 4;
		else if (usingPrayer(0, 23))
			value += 6;
		else if (usingPrayer(0, 24))
			value += 5;
		else if (usingPrayer(1, 6)) {
			value += 16;
			if (usingPrayer(1, 8))
				value += 2;
			else if (usingPrayer(1, 7))
				value += 3;
			else if (usingPrayer(1, 9))
				value += 1;
		}else if (usingPrayer(1, 7))
			value += 14;
		else if (usingPrayer(1, 8))
			value += 15;
		else if (usingPrayer(1, 9))
			value += 13;
		else if (usingPrayer(1, 17))
			value += 20;
		else if (usingPrayer(1, 18))
			value += 21;
		return value;
	}
	
	public void switchSettingQuickPrayer() {
		usingQuickPrayer = !usingQuickPrayer;
		player.getPackets().sendButtonConfig(181, usingQuickPrayer ? 1 : 0);//activates quick choose
	    player.getPackets().sendUnlockIComponentOptionSlots(271, usingQuickPrayer ? 7 : 6, 0, 26, 0);	
		if (usingQuickPrayer) //switchs tab to prayer
			player.getPackets().sendButtonConfig(168, 6);
	}
	
	public void switchQuickPrayers() {
		if (!checkPrayer())
			return;
		if (hasPrayersOn())
			closeAllPrayers();
		else {
			boolean hasOn = false;
			int index = 0;
			for (boolean prayer : quickPrayers[getPrayerBook()]) {
				if (prayer) {
					if(usePrayer(index))
						hasOn = true;
				}
				index++;
			}
			if(hasOn)  {
				player.getPackets().sendButtonConfig(182, 1);
				recalculatePrayer();
			}
		}
	}

	
	private void closePrayers(int[]... prayers) {
		for (int[] prayer : prayers)
			for (int prayerId : prayer)
				if (usingQuickPrayer)
					quickPrayers[getPrayerBook()][prayerId] = false;
				else {
					if(onPrayers[getPrayerBook()][prayerId])
						onPrayersCount--;
					onPrayers[getPrayerBook()][prayerId] = false;
					closePrayers(prayerId);
					
				}
	}
	
	public void switchPrayer(int prayerId) {
		if (!usingQuickPrayer)
			if (!checkPrayer())
				return;
		usePrayer(prayerId);
		recalculatePrayer();
	}
	
	private boolean usePrayer(int prayerId) {
		if (prayerId < 0 || prayerId >= prayerLvls[getPrayerBook()].length)
			return false;
		if (player.getSkills().getLevelForXp(5) < prayerLvls[this
				.getPrayerBook()][prayerId]) {
			player.getPackets().sendGameMessage("You need a prayer level of at least " + prayerLvls[getPrayerBook()][prayerId]+ " to use this prayer.");
			return false;
		}
		if (!usingQuickPrayer) {
			if (onPrayers[getPrayerBook()][prayerId]) {
				onPrayers[getPrayerBook()][prayerId] = false;
				closePrayers(prayerId);
				onPrayersCount--;
				player.getAppearence().generateAppearenceData();
				return true;
			}
		} else {
			if (quickPrayers[getPrayerBook()][prayerId]) {
				quickPrayers[getPrayerBook()][prayerId] = false;
				return true;
			}
		}
		boolean needAppearenceGenerate = false;
		if (getPrayerBook() == 0) {
			switch (prayerId) {
			case 0:
			case 5:
			case 13:
				closePrayers(closePrayers[getPrayerBook()][0],
						closePrayers[getPrayerBook()][10]);
				break;
			case 1:
			case 6:
			case 14:
				closePrayers(closePrayers[getPrayerBook()][1],
						closePrayers[getPrayerBook()][3],
						closePrayers[getPrayerBook()][4],
						closePrayers[getPrayerBook()][10]);
				break;
			case 2:
			case 7:
			case 15:
				closePrayers(closePrayers[getPrayerBook()][2],
						closePrayers[getPrayerBook()][3],
						closePrayers[getPrayerBook()][4],
						closePrayers[getPrayerBook()][10]);
				break;
			case 3:
			case 11:
			case 20:
				closePrayers(closePrayers[getPrayerBook()][1],
						closePrayers[getPrayerBook()][2],
						closePrayers[getPrayerBook()][3],
						closePrayers[getPrayerBook()][10]);
				break;
			case 4:
			case 12:
			case 21:
				closePrayers(closePrayers[getPrayerBook()][1],
						closePrayers[getPrayerBook()][2],
						closePrayers[getPrayerBook()][4],
						closePrayers[getPrayerBook()][10]);
				break;
			case 8:
			case 9:
			case 27:
				closePrayers(closePrayers[getPrayerBook()][5]);
				break;
			case 10:
				closePrayers(closePrayers[getPrayerBook()][6]);
				break;
			case 17:
			case 18:
			case 19:
				closePrayers(closePrayers[getPrayerBook()][7],
						closePrayers[getPrayerBook()][9]);
				needAppearenceGenerate = true;
				break;
			case 16:
				closePrayers(closePrayers[getPrayerBook()][8],
						closePrayers[getPrayerBook()][9]);
				needAppearenceGenerate = true;
				break;
			case 22:
			case 23:
			case 24:
				closePrayers(closePrayers[getPrayerBook()][7],
						closePrayers[getPrayerBook()][8],
						closePrayers[getPrayerBook()][9]);
				needAppearenceGenerate = true;
				break;
			case 25:
			case 26:
				closePrayers(closePrayers[getPrayerBook()][0],
						closePrayers[getPrayerBook()][1],
						closePrayers[getPrayerBook()][2],
						closePrayers[getPrayerBook()][3],
						closePrayers[getPrayerBook()][4],
						closePrayers[getPrayerBook()][10]);
				break;
			case 28:
				closePrayers(closePrayers[getPrayerBook()][0],
						closePrayers[getPrayerBook()][1],
						closePrayers[getPrayerBook()][2],
						closePrayers[getPrayerBook()][4],
						closePrayers[getPrayerBook()][10]);
				break;
			default:
				return false;
			}
		} else {
			switch (prayerId) {
			case 0:
				player.setNextAnimation(new Animation(12567));
				player.setNextGraphics(new Graphics(2213));
				closePrayers(closePrayers[getPrayerBook()][0]);
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				closePrayers(closePrayers[getPrayerBook()][5],
						closePrayers[getPrayerBook()][6]);
				break;
			case 5:
				player.setNextAnimation(new Animation(12589));
				player.setNextGraphics(new Graphics(2266));
				closePrayers(closePrayers[getPrayerBook()][2]);
				break;
			case 7:
			case 8:
			case 9:
			case 17:
			case 18:
				closePrayers(closePrayers[getPrayerBook()][3]);
				needAppearenceGenerate = true;
				break;
			case 6:
				closePrayers(closePrayers[getPrayerBook()][4]);
				needAppearenceGenerate = true;
				break;
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
				closePrayers(closePrayers[getPrayerBook()][1],
						closePrayers[getPrayerBook()][6]);
				break;
			case 19:
				player.setNextAnimation(new Animation(12565));
				player.setNextGraphics(new Graphics(2226));
				closePrayers(closePrayers[getPrayerBook()][1], closePrayers[getPrayerBook()][5],
						closePrayers[getPrayerBook()][6]);
			break;
			default:
				return false;
			}
		}
		if (!usingQuickPrayer) {
			onPrayers[getPrayerBook()][prayerId] = true;
			onPrayersCount++;
			if(needAppearenceGenerate)
				player.getAppearence().generateAppearenceData();
		} else
			quickPrayers[getPrayerBook()][prayerId] = true;
		return true;
	}

	
	public void processPrayer() {
		if(!hasPrayersOn())
			return;
		boostedLeech = false;
		if(drainDelay <= 0) {
			player.getSkills().drainPrayer(1);
			drainDelay = 5;
		}
		drainDelay--;
		
		if(!checkPrayer())
			closeAllPrayers();
	}
	public int getOnPrayersCount() {
		return onPrayersCount;
	}
	
	public void closeAllPrayers() {
		onPrayers = new boolean[][] { new boolean[29], new boolean[20] };
		onPrayersCount = 0;
		player.getPackets().sendButtonConfig(182, 0);
		player.getPackets().sendConfig(/*ancientcurses ? 1582 : */1395, 0);
		player.getAppearence().generateAppearenceData();
	}
	
	public boolean hasPrayersOn() {
		/*for (boolean prayer : onPrayers[getPrayerBook()])
			if (prayer)
				return true;
		return false;*/
		return onPrayersCount > 0;
	}
	
	private boolean checkPrayer() {
		if (player.getSkills().getLevel(5) <= 0) {
			player.getPackets().sendGameMessage("Please recharge your prayer at the Lumbridge Church.");
			return false;
		}
		return true;
	}
	
	private int getPrayerBook() {
		return ancientcurses == false ? 0 : 1;
	}
	
	private void recalculatePrayer() {
		int value = 0;
		int index = 0;
		for (boolean prayer : (!usingQuickPrayer ? onPrayers[getPrayerBook()] : quickPrayers[getPrayerBook()])) {
			if (prayer)
				value += /*ancientcurses ? Math.pow(2, index) : */prayerSlotValues[index];
			index++;
		}
		player.getPackets().sendConfig(/*ancientcurses ? (usingQuickPrayer ? 1584 : 1582) : */(usingQuickPrayer ? 1397 : 1395), value);
	}
	
	public void init() {
		player.getPackets().sendButtonConfig(181, usingQuickPrayer ? 1 : 0);
		player.getPackets().sendConfig(1086, ancientcurses ? 1 : 0);
		player.getPackets().sendConfig(1584, ancientcurses ? 1 : 0);
		player.getPackets().sendUnlockIComponentOptionSlots(271, usingQuickPrayer ? 7 : 6, 0, 26, 0);
	}
	
	public void setPrayerBook(boolean ancientcurses) {
		closeAllPrayers();
		this.ancientcurses = ancientcurses;
		player.getInterfaceManager().sendPrayerBook();
		init();
	}
	
	public Prayer() {
		quickPrayers = new boolean[][] { new boolean[29], new boolean[20] };
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		onPrayers = new boolean[][] { new boolean[29], new boolean[20] };
		leechBonuses = new int[11];
		drainDelay = 5;
	}
	
	public boolean isAncientCurses() {
		return ancientcurses;
	}
	public boolean usingPrayer(int book, int prayerId) {
		return onPrayers[book][prayerId];
	}
	
	public boolean isUsingQuickPrayer() {
		return usingQuickPrayer;
	}

	public boolean isBoostedLeech() {
		return boostedLeech;
	}

	public void setBoostedLeech(boolean boostedLeech) {
		this.boostedLeech = boostedLeech;
	}

	public int getPrayerpoints() {
		return prayerpoints;
	}

	public void setPrayerpoints(int prayerpoints) {
		this.prayerpoints = prayerpoints;
	}

	public void refreshPrayerPoints() {
		player.getPackets().sendConfig(2382, prayerpoints);
	}

	public void drainPrayerOnHalf() {
		if (prayerpoints > 0) {
			prayerpoints = prayerpoints / 2;
			refreshPrayerPoints();
		}
	}
	
	
	public boolean hasFullPrayerpoints() {
		return getPrayerpoints() >= player.getSkills().getLevelForXp(Skills.PRAYER);
	}

	/*public void drainPrayer(int amount) {
		if ((prayerpoints - amount) >= 0)
			prayerpoints -= amount;
		else
			prayerpoints = 0;
		refreshPrayerPoints();
	}*/

	public void restorePrayer(int amount) {
		int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER);
		if ((prayerpoints + amount) <= maxPrayer)
		prayerpoints += amount;
		else
		prayerpoints = maxPrayer;
		refreshPrayerPoints();
	}

	public void reset() {
		closeAllPrayers();
		prayerpoints = player.getSkills().getLevelForXp(Skills.PRAYER);
		refreshPrayerPoints();
	}
	
	public boolean isMageProtecting() {
		return ancientcurses ? usingPrayer(1, 7) : usingPrayer(0, 17);
	}

	public boolean isRangeProtecting() {
		return ancientcurses ? usingPrayer(1, 8) : usingPrayer(0, 18);
	}

	public boolean isMeleeProtecting() {
		return ancientcurses ? usingPrayer(1, 9) : usingPrayer(0, 19);
	}
	
	private static final double[] prayerData = { 1.2, 1.2, 1.2, 1.2, 1.2, 1.2,
		0.6, 0.6, 0.6, 3.6, 1.8, 0.6, 0.6, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3,
		0.3, 0.3, 0.3, 1.2, 0.6, 0.18, 0.24, 0.15, 0.2, 0.18 };

	private static final double[] ancientDrainRate = { 1.8, 0.24, 0.24, 0.24,
		0.24, 1.8, 0.3, 0.3, 0.3, 0.3, 0.36, 0.36, 0.36, 0.36, 0.36, 0.36,
		0.36, 1.2, 0.2, 0.2, };

	public void drainPrayer(int amount) {
		double drainRate = 0;
		if (getPrayerBook() == 0) {
			for(int i = 0; i < prayerData.length; i++) {
				if (onPrayers[getPrayerBook()][i]) {
					drainRate += prayerData[i];
				}
			}
		} else if (getPrayerBook() == 1) {
			for (int i = 0; i < ancientDrainRate.length; i++) {
				if (onPrayers[getPrayerBook()][i]) 
					drainRate += prayerData[i];
			}
		}
		if (drainRate != 0) 
			drainRate = drainRate * (1 + 0.035);
		if (prayerpoints - drainRate < 0) 
			prayerpoints = 0;
		else
			prayerpoints -= drainRate;
		refreshPrayerPoints();
	}
	
}
