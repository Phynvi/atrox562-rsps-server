package com.rs.game.player.controlers.pestcontrol;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class PestControlRewards {
	
	private static int totalXp;

	public static void openRewardsInterface(Player player) {
		player.getPackets().sendIComponentText(267, 93, "Void Knights' Reward Options");
		player.getPackets().sendIComponentText(267, 101, "Your points:");
		player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
		player.getInterfaceManager().sendInterface(267);
		totalXp(player);
	}
	
	public static void totalXp(Player player) {
		if (player.getSkills().getCombatLevel() >= 3 && player.getSkills().getCombatLevel() <= 34) {
			totalXp = 18;
		} else if (player.getSkills().getCombatLevel() >= 35 && player.getSkills().getCombatLevel() <= 42) {
			totalXp = 36;
		} else if (player.getSkills().getCombatLevel() >= 43 && player.getSkills().getCombatLevel() <= 48) {
			totalXp = 54;
		} else if (player.getSkills().getCombatLevel() >= 49 && player.getSkills().getCombatLevel() <= 54) {
			totalXp = 72;
		} else if (player.getSkills().getCombatLevel() >= 55 && player.getSkills().getCombatLevel() <= 59) {
			totalXp = 90;
		} else if (player.getSkills().getCombatLevel() >= 60 && player.getSkills().getCombatLevel() <= 64) {
			totalXp = 108;
		} else if (player.getSkills().getCombatLevel() >= 65 && player.getSkills().getCombatLevel() <= 69) {
			totalXp = 126;
		} else if (player.getSkills().getCombatLevel() >= 70 && player.getSkills().getCombatLevel() <= 73) {
			totalXp = 144;
		} else if (player.getSkills().getCombatLevel() >= 74 && player.getSkills().getCombatLevel() <= 77) {
			totalXp = 162;
		} else if (player.getSkills().getCombatLevel() >= 78 && player.getSkills().getCombatLevel() <= 81) {
			totalXp = 180;
		} else if (player.getSkills().getCombatLevel() >= 82 && player.getSkills().getCombatLevel() <= 84) {
			totalXp = 198;
		} else if (player.getSkills().getCombatLevel() >= 85 && player.getSkills().getCombatLevel() <= 90) {
			totalXp = 216;
		} else if (player.getSkills().getCombatLevel() >= 91 && player.getSkills().getCombatLevel() <= 105) {
			totalXp = 234;
		} else if (player.getSkills().getCombatLevel() >= 106 && player.getSkills().getCombatLevel() <= 138) {
			totalXp = 270;
		}
	}

	public static void handleMainButtons(Player player, int componentId, int slotId) {
		int randomAmount = Utils.getRandom(50);
		switch (componentId) {
		case 34:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.ATTACK, totalXp);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Attack XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.getPestPoints());
			break;
		case 49:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.ATTACK, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Attack XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 56:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.ATTACK, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Attack XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 35:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.STRENGTH, totalXp * 1);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Strength XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 50:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.STRENGTH, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Strength XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 57:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.STRENGTH, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Strength XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 36:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.DEFENCE, totalXp * 1);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Defence XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 51:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.DEFENCE, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Defence XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 58:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.DEFENCE, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Defence XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 37:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.RANGE, totalXp * 1);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Ranged XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 52:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.RANGE, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Ranged XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 59:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.RANGE, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Ranged XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 38:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.MAGIC, totalXp * 1);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Magic XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 53:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.MAGIC, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Magic XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 60:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.MAGIC, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Magic XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 39:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.HITPOINTS, totalXp * 1);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Hitpoints XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 54:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.HITPOINTS, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Hitpoints XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 61:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.HITPOINTS, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Hitpoints XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 40:
			if (player.pestControlPoints < 1) {
				player.getPackets().sendGameMessage("You need atleast 1 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 1) {
				player.setPestPoints(player.pestControlPoints -= 1);
				player.getSkills().addXp(Skills.PRAYER, totalXp * 1);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Prayer XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 55:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getSkills().addXp(Skills.PRAYER, totalXp * 10);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Prayer XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 62:
			if (player.pestControlPoints < 100) {
				player.getPackets().sendGameMessage("You need atleast 100 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 100) {
				player.setPestPoints(player.pestControlPoints -= 100);
				player.getSkills().addXp(Skills.PRAYER, totalXp * 100);
				player.getPackets().sendGameMessage("You purchased "+ totalXp * 5 +" Prayer XP.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 45:
			if (player.pestControlPoints < 30) {
				player.getPackets().sendGameMessage("You need atleast 30 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 30) {
				player.setPestPoints(player.pestControlPoints -= 30);
				player.getInventory().addItem(14701, 1);
				player.getPackets().sendGameMessage("You purchased a Herb Pack.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 46:
			if (player.pestControlPoints < 15) {
				player.getPackets().sendGameMessage("You need atleast 15 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 15) {
				player.setPestPoints(player.pestControlPoints -= 15);
				player.getInventory().addItem(454, randomAmount);
				player.getInventory().addItem(441, randomAmount);
				player.getPackets().sendGameMessage("You purchased a Mineral Pack.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 48:
			if (player.pestControlPoints < 15) {
				player.getPackets().sendGameMessage("You need atleast 15 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 15) {
				player.setPestPoints(player.pestControlPoints -= 15);
				player.getInventory().addItem(15246, 1);
				player.getPackets().sendGameMessage("You purchased a Seed Pack.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 41:
			if (player.pestControlPoints < 250) {
				player.getPackets().sendGameMessage("You need atleast 250 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 250) {
				player.setPestPoints(player.pestControlPoints -= 250);
				player.getInventory().addItem(8841, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Mace.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 42:
			if (player.pestControlPoints < 250) {
				player.getPackets().sendGameMessage("You need atleast 250 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 250) {
				player.setPestPoints(player.pestControlPoints -= 250);
				player.getInventory().addItem(8839, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Top.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 43:
			if (player.pestControlPoints < 250) {
				player.getPackets().sendGameMessage("You need atleast 250 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 250) {
				player.setPestPoints(player.pestControlPoints -= 250);
				player.getInventory().addItem(8840, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Robes.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 44:
			if (player.pestControlPoints < 150) {
				player.getPackets().sendGameMessage("You need atleast 150 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 150) {
				player.setPestPoints(player.pestControlPoints -= 150);
				player.getInventory().addItem(8842, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Gloves.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 67:
			if (player.pestControlPoints < 200) {
				player.getPackets().sendGameMessage("You need atleast 200 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 200) {
				player.setPestPoints(player.pestControlPoints -= 200);
				player.getInventory().addItem(11663, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Mage Helm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 68:
			if (player.pestControlPoints < 200) {
				player.getPackets().sendGameMessage("You need atleast 200 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 200) {
				player.setPestPoints(player.pestControlPoints -= 200);
				player.getInventory().addItem(11664, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Ranger Helm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 69:
			if (player.pestControlPoints < 200) {
				player.getPackets().sendGameMessage("You need atleast 200 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 200) {
				player.setPestPoints(player.pestControlPoints -= 200);
				player.getInventory().addItem(11665, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Melee Helm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 70:
			if (player.pestControlPoints < 10) {
				player.getPackets().sendGameMessage("You need atleast 10 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 10) {
				player.setPestPoints(player.pestControlPoints -= 10);
				player.getInventory().addItem(11666, 1);
				player.getPackets().sendGameMessage("You purchased a Void Knight Zeal.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 75:
			if (player.pestControlPoints < 2) {
				player.getPackets().sendGameMessage("You need atleast 2 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 2) {
				player.setPestPoints(player.pestControlPoints -= 2);
				player.getInventory().addItem(12166, 1);
				player.getPackets().sendGameMessage("You purchased a Spinner charm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 81:
			if (player.pestControlPoints < 2) {
				player.getPackets().sendGameMessage("You need atleast 2 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 2) {
				player.setPestPoints(player.pestControlPoints -= 2);
				player.getInventory().addItem(12164, 1);
				player.getPackets().sendGameMessage("You purchased a Ravager charm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 78:
			if (player.pestControlPoints < 2) {
				player.getPackets().sendGameMessage("You need atleast 2 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 2) {
				player.setPestPoints(player.pestControlPoints -= 2);
				player.getInventory().addItem(12167, 1);
				player.getPackets().sendGameMessage("You purchased a Torcher charm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		case 84:
			if (player.pestControlPoints < 2) {
				player.getPackets().sendGameMessage("You need atleast 2 Pest Control points to buy this.");
				return;
			} else if (player.pestControlPoints >= 2) {
				player.setPestPoints(player.pestControlPoints -= 2);
				player.getInventory().addItem(12165, 1);
				player.getPackets().sendGameMessage("You purchased a Shifter charm.");
			}
			player.getPackets().sendIComponentText(267, 104, "" + player.pestControlPoints);
			break;
		}
	}

}
