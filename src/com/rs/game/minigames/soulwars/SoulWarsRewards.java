package com.rs.game.minigames.soulwars;

import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.game.player.Player;

public class SoulWarsRewards {
	
		
		public static void handleRewards(Player player, int componentId, int packetId) {
			switch (componentId) {

			case 21:

			case 5:
				break;

			case 27:// Gold Charm
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Gold Charm is worth 2 Zeal Points for 5 Charms.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 2) {
						player.zeal -= 2;
						player.getInventory().addItem(12158, 5);
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					if (player.zeal <= 1) {
						player.sendMessage("You don't have enough zeal for this..");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					break;
				}
				break;

			case 26:// Green Charm
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Green Charm is worth 2 Zeal Points for 5 Charms.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 2) {
						player.zeal -= 2;
						player.getInventory().addItem(12159, 5);
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					if (player.zeal <= 1) {
						player.sendMessage("You don't have enough zeal for this..");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					break;
				}
				break;

			case 25:// Red Charm
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Red Charm is worth 2 Zeal Points for 5 Charms.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 2) {
						player.zeal -= 2;
						player.getInventory().addItem(12160, 5);
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					if (player.zeal <= 1) {
						player.sendMessage("You don't have enough zeal for this..");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					break;
				}
				break;

			case 24:// Blue Charm
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Blue Charm is worth 2 Zeal Points for 5 Charms.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 2) {
						player.zeal -= 2;
						player.getInventory().addItem(12163, 5);
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					}
					if (player.zeal <= 1) {
						player.sendMessage("You don't have enough zeal for this..");
					}
					break;
				}
				break;

			case 6:// Creeping Hand
				player.sendMessage("Unavailable Soul Wars Reward.");
				if (player.getRights() == 3) {
					player.sendMessage("B: " + componentId + " ][ P: " + packetId);
				}
				break;
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:// Minitrice
				player.sendMessage("Unavailable Soul Wars Reward.");
				if (player.getRights() == 3) {
					player.sendMessage("B: " + componentId + " ][ P: " + packetId);
				}
				break;
			case 10:// Baby Basilisk
				player.sendMessage("Unavailable Soul Wars Reward.");
				if (player.getRights() == 3) {
					player.sendMessage("B: " + componentId + " ][ P: " + packetId);
				}
				break;
			case 12:// Baby Kurask
				player.sendMessage("Unavailable Soul Wars Reward.");
				if (player.getRights() == 3) {
					player.sendMessage("B: " + componentId + " ][ P: " + packetId);
				}
				break;
			case 16:// Abyssal Minion
				player.sendMessage("Unavailable Soul Wars Reward.");
				if (player.getRights() == 3) {
					player.sendMessage("B: " + componentId + " ][ P: " + packetId);
				}
				break;
			case 8:// GAMBLE!
				player.sendMessage("Unavailable Soul Wars Reward.");
				if (player.getRights() == 3) {
					player.sendMessage("B: " + componentId + " ][ P: " + packetId);
				}
				break;

			case 37:// Attack..
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Attack Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Attack XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(0, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Attack XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(0, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Attack XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(0, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 32:// Slayer Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Slayer Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Slayer XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(18, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Slayer XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(18, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Slayer XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(18, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 38:// Prayer Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Prayer Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Prayer XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(5, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Prayer XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(5, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Prayer XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(5, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 39:// Mage Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Magic Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Magic XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(6, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Magic XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(6, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Magic XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(6, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 36:// Range Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Ranged Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Ranged XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(4, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Ranged XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(4, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Ranged XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(4, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 33:// Hitpoints Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Hitpoints Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Hitpoints XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(3, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Hitpoints XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(3, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Hitpoints XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(3, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 34:// Defence Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Defence Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Defence XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(1, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Defence XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(1, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Defence XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(1, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

			case 35:// Strength Reward
				switch (packetId) {
				case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
					player.sendMessage("Strength Reward: 1 Zeal is 5,000 XP.");
					player.getPackets().sendIComponentText(276, 59,
							"Zeal: " + player.zeal);
					break;
				case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
					if (player.zeal >= 1) {
						player.sendMessage("You spend 1 Zeal on 5,000 Strength XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(2, 5000);
						player.zeal -= 1;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
					if (player.zeal >= 10) {
						player.sendMessage("You spend 10 Zeal on 50,000 Strength XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(2, 50000);
						player.zeal -= 10;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
					if (player.zeal >= 100) {
						player.sendMessage("You spend 100 Zeal on 500,000 Strength XP.");
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
						player.getSkills().addXp(2, 500000);
						player.zeal -= 100;
						player.getPackets().sendIComponentText(276, 59,
								"Zeal: " + player.zeal);
					} else {
						player.getPackets().sendGameMessage("You do not have enough zeal.");
					}
					break;
				}
				break;

				/*
				 * //case 37://Attack Reward 130K XP each
				 * player.sendMessage("Unavailable Soul Wars Reward.");
				 * if(player.getRights() == 3) { player.sendMessage("B: "+componentId+
				 * " ][ P: "+packet); } break;
				 */
			default:
				if (player.getRights() == 2 || player.getRights() == 4) {
					player.sendMessage("Button ID: " + componentId
							+ "  Button2ID: " + packetId);
				}
				break;
			}
		}
	}