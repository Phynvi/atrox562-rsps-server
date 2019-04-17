package com.rs.net.decoders.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.Settings;
import com.rs.game.player.dialogues.LevelUp;
import com.rs.game.item.Item;
import com.rs.game.minigames.PuroPuro;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.soulwars.SoulWarsRewards;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.EmotesManager;
import com.rs.game.player.Equipment;
import com.rs.game.player.LendingManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.FightPitsViewingOrb;
import com.rs.game.player.skills.crafting.JewelleryCrafting;
import com.rs.game.player.actions.Rest;
import com.rs.game.player.skills.smithing.Smelting;
import com.rs.game.player.content.Canoes;
import com.rs.game.player.content.CarrierTravel;
import com.rs.game.player.content.CarrierTravel.Carrier;
import com.rs.game.player.content.CharacterDesign;
import com.rs.game.player.content.FairyRings;
import com.rs.game.player.content.GraveStoneSelection;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.Lend;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.content.PlayerLook;
//import com.rs.game.player.content.Runecrafting;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.SpiritTree;
import com.rs.game.player.content.Teleports;
import com.rs.game.player.content.custom.SlayerTeleports;
import com.rs.game.player.content.clans.Clan.ClanRanks;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.quests.CooksAssistant;
import com.rs.game.player.content.quests.DoricsQuest;
import com.rs.game.player.content.quests.RuneMysteries;
import com.rs.game.player.content.quests.QuestNotAdded;
import com.rs.game.player.content.quests.WolfWhistle;
import com.rs.game.player.controlers.pestcontrol.PestControlRewards;
//import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.game.player.skills.construction.House;
import com.rs.game.player.skills.construction.House.RoomReference;
import com.rs.game.player.skills.construction.HouseConstants.Builds;
import com.rs.game.player.skills.construction.HouseControler;
import com.rs.game.player.skills.crafting.Tanning;
import com.rs.game.player.skills.magic.Enchanting;
import com.rs.game.player.skills.smithing.Smithing.ForgingInterface;
import com.rs.game.player.skills.summoning.Summoning;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.utils.ShopsHandler;
import com.rs.utils.WeightManager;
import com.rs.utils.Utils;

public class ButtonHandler {
	
	public static void handleInterfaceButtons(Player player, InputStream stream, int packetId) {
			int interfaceId = stream.readUnsignedShort();
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int slotId = packetId != WorldPacketsDecoder.ACTION_BUTTON2_PACKET ? stream.readUnsignedShort() : -1;
			//int buttonId = stream.readShort() & 0xFFFF;
			int buttonId2 = -1;
			Lend lend = null;
			if (!player.getControlerManager().processButtonClick(interfaceId,
					componentId, slotId, packetId))
				return;
			if (stream.getLength() >= 6) {
				buttonId2 = stream.readShort() & 0xFFFF;
			}
			if (buttonId2 == 65535) {
				buttonId2 = 0;
			}
			if(slotId == 65535)
				slotId = -1;
			if (interfaceId == 153) { //never show this again on death interface
			player.closeInterfaces();
			player.neverShowDeathInter = 1;
			}
			if (interfaceId == 190) {
				if (slotId == 1) {
			CooksAssistant.sendInterface(player);
			CooksAssistant.checkProgress(player);
			} else if (slotId == 3) {
			DoricsQuest.sendInterface(player);
			DoricsQuest.checkProgress(player);
			} else if (slotId == 13) {
			RuneMysteries.sendInterface(player);
			RuneMysteries.checkProgress(player);
			} else if (slotId == 131) {
			WolfWhistle.sendInterface(player);
			WolfWhistle.checkProgress(player);
				} else {
			QuestNotAdded.sendInterface(player);
			}
		} else if (interfaceId == 449) {
			if (componentId == 1) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				//if (shop == null)
					//return;
				player.getInterfaceManager().sendInventory();
				//shop.sendInventory(player);
			} else if (componentId == 21) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null)
					return;
				Integer slot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
				if (slot == null)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					shop.buy(player, slot, 1);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					shop.buy(player, slot, 5);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
					shop.buy(player, slot, 10);
				else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
					shop.buy(player, slot, 50);
					}
		}
		if (interfaceId == 161 || interfaceId == 163 || interfaceId == 164) {
			player.getSlayerManager().handleSlayerInterfaceButtons(interfaceId, componentId);
		} else if (interfaceId == 589) {
			if (componentId == 9) {
				if (player.getClan() != null && player.getClan().getClanName() != null)
					player.getClan().refreshSetup(player);
				player.getInterfaceManager().sendInterface(590);
			} else if (componentId == 14) {
				if (player.getCurrentClan().getClanLeaderUsername().equalsIgnoreCase(player.getUsername())) {
					player.getCurrentClan().setLootshare(player.getCurrentClan().isLootsharing() ? false : true);
				} else
					player.sendMessage("Only the clan owner can switch lootshare settings.");
			}
		} else if (interfaceId == 432) {
			final int index = Enchanting.getComponentIndex(componentId);
			if (index == -1)
				return;
			Enchanting.processBoltEnchantSpell(player, index, packetId == 14 ? 1 : packetId == 67 ? 5 : 10);
		} if (interfaceId == 729) {
			PlayerLook.handleThessaliasMakeOverButtons(player, componentId, slotId);
		}
		if (interfaceId == 267) {
			PestControlRewards.handleMainButtons(player, componentId, slotId);
		}
		if (interfaceId == 324) {
			Tanning.handleInterface(player, interfaceId, componentId, packetId);
			return;
		}
		if (interfaceId == 590) {
			if (componentId == 22) {
				if (packetId == 216) {
					/**
					 * Creates the clan; Modifies if one exists by default.
					 */
					player.getTemporaryAttributtes().put("enter_clan", true);
					player.getPackets().sendRunScript(109, "Choose a clan name:");
				} else if (packetId == 19) {
					/**
					 * Deletes the clan.
					 */
					player.getDialogueManager().startDialogue("ClanDeletionD");
					return;
				}
			} else if (player.getClan() == null) {
				player.sendMessage("You must create a clan first before you can modify the settings.");
				return;
			} else if (componentId == 23)
				player.getClan().setMinimumEnterRank(ClanRanks.getRank(packetId));
			else if (componentId == 24)
				player.getClan().setMinimumTalkRank(ClanRanks.getRank(packetId));
			else if (componentId == 25)
				player.getClan().setMinimumKickRank(ClanRanks.getRank(packetId));
			else if (componentId == 26) {
				player.getClan().setMinimumLootshareRank(ClanRanks.getRank(packetId));
			} else if (componentId == 33) {
				if (!player.getClan().isLootsharing()) {
					player.sendMessage("You need to enable lootshare before you can toggle coinshare.");
					return;
				}
				player.getClan().setCoinshare(player.getClan().isCoinsharing() ? false : true);
			}
			/**
			 * Updates all the data
			 */
			if (player.getClan() != null)
				player.getClan().refreshSetup(player);
		}
			if (interfaceId == 311) {
			Smelting.handleInterface(player, interfaceId, componentId);
			}
			if (interfaceId == 403) {
			Teleports.handleButtonInterface(player, componentId);
			}
			if (interfaceId == 625) {
			SlayerTeleports.handleButtonInterface(player, componentId);
			}
			if (interfaceId == 846) {
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
			}
			if (interfaceId == 300) {
			ForgingInterface.handleIComponents(player, componentId);
			return;
		}
			if (interfaceId == 675) {
			JewelleryCrafting.handleButtonClick(player, componentId, packetId == 216 ? 1 : packetId == 19 ? 5 : packetId == 193 ? 10 : 28);
			return;
		}
		if (interfaceId == 734) {
			if (componentId == 21)
				FairyRings.confirmRingHash(player);
			else
				FairyRings.handleDialButtons(player, componentId);
		} else if (interfaceId == 735) {
			if (componentId >= 14 && componentId <= 14 + 64)
				FairyRings.sendRingTeleport(player, componentId - 14);
		}
		if (interfaceId == 52) {
			if (componentId >= 30 && componentId <= 34) {
				player.getTemporaryAttributtes().put("selected_canoe", componentId - 30);
				Canoes.createShapedCanoe(player);
			}
		} else if (interfaceId == 53) {
			int selectedArea = -1;
			if (componentId == 47)
				selectedArea = 0;
			else if (componentId == 48)
				selectedArea = 1;
			else if (componentId == 3)
				selectedArea = 2;
			else if (componentId == 6)
				selectedArea = 3;
			else if (componentId == 49)
				selectedArea = 4;
			if (selectedArea != -1)
				Canoes.deportCanoeStation(player, selectedArea);
		}
		if (interfaceId == 138) {
			if (componentId == 15)
				CarrierTravel.sendCarrier(player, Carrier.Gandius_Glider, false);
			else if (componentId == 17)
				CarrierTravel.sendCarrier(player, Carrier.Sindrpos_Glider, false);
			else if (componentId == 18)
				CarrierTravel.sendCarrier(player, Carrier.LemantoAndra_Glider, false);
			else if (componentId == 19)
				CarrierTravel.sendCarrier(player, Carrier.KarHewo_Glider, false);
			else if (componentId == 20)
				CarrierTravel.sendCarrier(player, Carrier.Lemantolly_Undri_Glider, false);
	}
		if (interfaceId == 652) {
			if (componentId == 31)
				GraveStoneSelection.handleSelectionInterface(player, slotId / 6);
			else if (componentId == 34)
				GraveStoneSelection.confirmSelection(player);
		} if (interfaceId == 751) {
			if (componentId == 28) {
				player.getInterfaceManager().sendInterface(594);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						player.getPackets().sendInterface(true, 752, 8, 137);
					}
				});
				return;
			}
		}
		if (interfaceId == 522) {
			if (componentId == 6)
				player.getInterfaceManager().sendMagicBook();
		}
		if (interfaceId == 34) {
			if (packetId == 76) {
				if (componentId == 9) {
					player.getNotes().remove(slotId);
				}
			} else if (packetId == 216) {
				if (componentId == 3) {
					player.getTemporaryAttributtes().put("entering_note", Boolean.TRUE);
					player.getPackets().sendRunScript(110, "Enter a new note:");
				} else if (componentId == 8) {
					if (player.getNotes().getSelectedNote() == -1)
						player.sendMessage("Select the note you wish to delete first.");
					else
						player.getNotes().remove(player.getNotes().getSelectedNote());
				} else if (componentId == 9) {
					player.getNotes().selectNote(slotId);
				} else if (componentId >= 35 && componentId <= 41)
					player.getNotes().changeColour(componentId);
			} else if (packetId == 19) {
				if (componentId == 8) {
					player.getNotes().deleteAllNotes();
					return;
				}
				Note note = player.getCurrentNotes().get(slotId);
				player.getTemporaryAttributtes().put("editing_note", Boolean.TRUE);
				player.getTemporaryAttributtes().put("noteToEdit", note);
				player.getPackets().sendRunScript(110, "Enter a new note:");
			} else if (packetId == 193) {
				if (componentId == 9) {
					player.getPackets().sendHideIComponent(34, 16, false);
					player.getTemporaryAttributtes().put("selectednote", slotId);
				}
			}
			return;
		}
			if (interfaceId == 513) {
			player.stopAll(false, true, true);
			}
			if (interfaceId == 394 || interfaceId == 396) {
			if (componentId == 11) {
				if (player.getHouse() == null
						|| !(player.getControlerManager().getControler() instanceof HouseControler))
					return;
				if (player.getTemporaryAttributtes().get("GetSlotBuild") != null) {
					player.getHouse().buildDungeonStairs(slotId,
							(Builds) player.getTemporaryAttributtes().get("GetSlotBuild"),
							(RoomReference) player.getTemporaryAttributtes().get("GetNewRoom"));
				}
				player.getHouse().build(slotId);
			}
		}
		if (interfaceId == 398) {
			if (componentId == 19)
				player.getInterfaceManager().sendSettings();
			else if (componentId == 15 || componentId == 1) {
				if (player.getHouse() == null)
					return;
				player.getHouse().setBuildMode(componentId == 15);
			} else if (componentId == 25 || componentId == 26) {
				if (player.getHouse() == null)
					return;
				player.getHouse().setArriveInPortal(componentId == 25);
			} else if (componentId == 27) {
				if (player.getHouse() == null
						|| !(player.getControlerManager().getControler() instanceof HouseControler))
					return;
				player.getHouse().expelGuests();
			} else if (componentId == 29) {
				if (player.getHouse() == null
						|| !(player.getControlerManager().getControler() instanceof HouseControler))
					return;
				House.leaveHouse(player);
			}
		} else if (interfaceId == 402) {
			if (componentId >= 87 && componentId <= 108)
				player.getHouse().createRoom(componentId - 87);

		}
			if (interfaceId == 182) {
				if(player.getInterfaceManager().containsInventoryInter())
					return;
				if(componentId == 6) 
					if(!player.hasFinished())
						player.logout();
			} else if ((interfaceId == 590 && componentId == 8) || interfaceId == 464) {
				player.getEmotesManager().useBookEmote(interfaceId == 464 ? componentId : EmotesManager.getId(slotId, packetId));
			} else if (interfaceId == 548 || interfaceId == 746) {
					if ((interfaceId == 548 && componentId == 129)
							|| (interfaceId == 746 && componentId == 168)) {
						if (player.getInterfaceManager().containsScreenInter()
								|| player.getInterfaceManager()
								.containsInventoryInter()) {
							// TODO cant open sound
							player.getPackets()
							.sendGameMessage(
									"Please finish what you're doing before opening the world map.");
							return;
						}
						// world map open
						/*player.getPackets().sendWindowsPane(755, 0);
						int posHash = player.getX() << 14 | player.getY();
						player.getPackets().sendConfig(622, posHash); // map open
						// center
						// pos
						player.getPackets().sendConfig(674, posHash); // player*/
						// position
						player.getPackets().sendWindowsPane(755, 0);
						final int posHash = player.getX() << 14 | player.getY();
						player.getPackets().sendButtonConfig(622, posHash);
						player.getPackets().sendButtonConfig(674, posHash);
					}
			} else if (interfaceId == 755) {
				if (componentId == 46)
					player.getPackets().sendWindowsPane(
							player.getInterfaceManager().hasRezizableScreen() ? 746
									: 548, 2);
				else if (componentId == 42) {
					player.getHintIconsManager().removeAll();//TODO find hintIcon index
					player.getPackets().sendConfig(1159, 1);
				}
			}else if (interfaceId == 187) {
				if (componentId == 1) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getMusicsManager().playAnotherMusic(slotId / 2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						player.getMusicsManager().sendHint(slotId / 2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getMusicsManager().addToPlayList(slotId / 2);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getMusicsManager().removeFromPlayList(slotId / 2);
				} else if (componentId == 4)
					player.getMusicsManager().addPlayingMusicToPlayList();
				else if (componentId == 10)
					player.getMusicsManager().switchPlayListOn();
				else if (componentId == 11)
					player.getMusicsManager().clearPlayList();
				else if (componentId == 13)
					player.getMusicsManager().switchShuffleOn();
			} else if (interfaceId == 192) {
				if (componentId == 2)
					player.getCombatDefinitions().switchDefensiveCasting();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 9)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId == 11)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId == 13)
					player.getCombatDefinitions().switchShowSkillSpells();
				else if (componentId >= 15 & componentId <= 17)
					player.getCombatDefinitions()
					.setSortSpellBook(componentId - 15);
				else
					Magic.processNormalSpell(player, componentId, packetId);
			/*} else if (interfaceId == 430) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId == 9)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId >= 11 & componentId <= 13)
					player.getCombatDefinitions()
					.setSortSpellBook(componentId - 11);
				else if (componentId == 20)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processLunarSpell(player, componentId, packetId);*/
			}else if (interfaceId == 105 || interfaceId == 107 || interfaceId == 109 || interfaceId == 449) {
					player.getGEManager().handleButtons(interfaceId, componentId, slotId, packetId);
			}else if (interfaceId == 864) {
				if (componentId == 7)
					SpiritTree.handleButtons(player, slotId);
			} else if (interfaceId == 673 || interfaceId == 669) {
			boolean pouch = interfaceId == 669;
			if (componentId == 15) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					if (pouch)
						Summoning.createPouch(player, slotId, 1);
					else
						Summoning.transformScrolls(player, slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					if (pouch)
						Summoning.createPouch(player, slotId, 5);
					else
						Summoning.transformScrolls(player, slotId, 6);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					if (pouch)
						Summoning.createPouch(player, slotId, 10);
					else
						Summoning.transformScrolls(player, slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					if (pouch)
						Summoning.createPouch(player, slotId, 28);
					else
						Summoning.transformScrolls(player, slotId, 28);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					if (pouch) {
						player.getTemporaryAttributtes().put("creatingPouches", slotId);
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter the amount of pouches you'd like to create: " });
					} else {
						player.getTemporaryAttributtes().put("creatingScrolls", slotId);
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter the amount of scrolls you'd like to create: " });
					}
				}
			} else if (componentId == 18 && pouch)
				Summoning.sendScrollInterface(player);
			else if (interfaceId == 276)
				SoulWarsRewards.handleRewards(player, componentId, packetId);
			else if (componentId == 17 && !pouch)
				Summoning.sendPouchInterface(player);
		} else if (interfaceId == 880) {
			if (componentId >= 7 && componentId <= 20)
				Familiar.setLeftclickOption(player, (componentId - 7) / 2);
			else if (componentId == 21)
				Familiar.confirmLeftOption(player);
			else if (componentId == 25)
				Familiar.setLeftclickOption(player, 7);
		} else if (interfaceId == 747) {
			if (componentId == 7) {
				Familiar.selectLeftOption(player);
			} else if (player.getPet() != null) {
				if (componentId == 10) {
					player.getPet().call();
				} else if (componentId == 12 || componentId == 21) {
					player.getDialogueManager().startDialogue("DismissD");
				} else if (componentId == 10 || componentId == 19) {
					player.getPet().sendFollowerDetails();
				}
			} else if (player.getFamiliar() != null) {
				if (componentId == 10 || componentId == 17)
					player.getFamiliar().call();
				else if (componentId == 11 || componentId == 21)
					player.getDialogueManager().startDialogue("DismissD");
				else if (componentId == 19)
					player.getFamiliar().takeBob();
				else if (componentId == 13 || componentId == 23)
					player.getFamiliar().renewFamiliar();
				else if (componentId == 16 || componentId == 9)
					player.getFamiliar().sendFollowerDetails();
				else if (componentId == 93 || componentId == 139) {
					if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
						player.getFamiliar().setSpecial(true);
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(player);
				}
			}
			} else if (interfaceId == 662) {
				if (componentId == 93 || componentId == 173 || componentId == 185) {
					if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK)
						player.getFamiliar().setSpecial(true);
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(player);
				}
			}
	else if (interfaceId == 540) {
		if (componentId == 69)
			PuroPuro.confirmPuroSelection(player);
		else if (componentId == 71)
			ShopsHandler.openShop(player, 999);//TODO: Open Puro Puro Shop.
		else
			PuroPuro.handlePuroInterface(player, componentId);
			} else if (interfaceId == 430) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId == 9)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId >= 9 & componentId <= 11)
					player.getCombatDefinitions()
					.setSortSpellBook(componentId - 9);
				else if (componentId == 20)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processLunarSpell(player, componentId, packetId);
		/*	}else if(interfaceId == 193) {
				if(componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if(componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if(componentId == 9)
					player.getCombatDefinitions().switchShowMiscallaneousSpells();
				else if (componentId >= 11 && componentId <= 13)
					player.getCombatDefinitions().setSortSpellBook(componentId-1);*/
			/*} else if (interfaceId == 193) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId >= 11 && componentId <= 13)
					player.getCombatDefinitions().setSortSpellBook(componentId - 9);
				else if (componentId == 18)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processAncientSpell(player, componentId, packetId);*/
			} else if (interfaceId == 206) {
			if (packetId == 216)
				player.getPriceCheckManager().removeItem(slotId, 1);
			else if (packetId == 19)
				player.getPriceCheckManager().removeItem(slotId, 5);
			else if (packetId == 193)
				player.getPriceCheckManager().removeItem(slotId, 10);
			else if (packetId == 76)
				player.getPriceCheckManager().removeItem(slotId, Integer.MAX_VALUE);
			else if (packetId == 173) {
				player.getTemporaryAttributtes().put("removingItemFromPriceChecker", slotId);
				player.getPackets().sendRunScript(108, new Object[] { "Enter the amount you'd like to remove: " });
			}
			} else if (interfaceId == 207) {
				if (componentId == 0) {
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getPriceCheckManager().addItem(slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getPriceCheckManager().addItem(slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						player.getPriceCheckManager().addItem(slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getPriceCheckManager().addItem(slotId,
								Integer.MAX_VALUE);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
						player.getTemporaryAttributtes().put("pc_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().remove("pc_isRemove");
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getInventory().sendExamine(slotId);
				}
			} else if (interfaceId == 193) {
				if (componentId == 5)
					player.getCombatDefinitions().switchShowCombatSpells();
				else if (componentId == 7)
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				else if (componentId >= 11 && componentId <= 13)
					player.getCombatDefinitions().setSortSpellBook(componentId - 1);
				else if (componentId == 18)
					player.getCombatDefinitions().switchDefensiveCasting();
				else
					Magic.processAncientSpell(player, componentId, packetId);
			}else if(interfaceId == 261) {
				if(player.getInterfaceManager().containsInventoryInter())
					return;
				if(componentId == 3) {
					player.toogleRun(true);
				}else if(componentId == 8) {
					if (player.getControlerManager().getControler() instanceof HouseControler) {
					player.getInterfaceManager().sendTab(145, 398);
					} else {
						player.getPackets().sendGameMessage("You must be in a House to do this action.");
						return;
					}
					//398 build settings
				}else if(componentId == 16) {
					if(player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please close the interface you have open before setting your graphic options.");
						return;
					}
					player.stopAll();
					player.getInterfaceManager().sendInterface(742);
				}else if (componentId == 4)
					player.switchAllowChatEffects();
				else if (componentId == 5)
					player.switchAllowSplitChat();
				else if (componentId == 6)
					player.switchMouseButtons();
				else if (componentId == 7)
					player.switchAllowAcceptAid();
				else if(componentId == 18) {
					if(player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please close the interface you have open before setting your audio options.");
						return;
					}
					player.stopAll();
					player.getInterfaceManager().sendInterface(743);
				}
				
			}else if(interfaceId == 271) {
				if(componentId == 7 || componentId == 6)
					player.getPrayer().switchPrayer(slotId);
				else if (componentId == 8 && player.getPrayer().isUsingQuickPrayer())
					player.getPrayer().switchSettingQuickPrayer();
			} else if (interfaceId == 320) {
			player.stopAll();
			boolean lvlup = false;
			int skillMenu = -1;
			switch (componentId) {
			case 125: // Attack
				skillMenu = 1;
				if (player.getTemporaryAttributtes().get("leveledUp[0]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 1);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 10);
					LevelUp.switchFlash(player, 0, false);
				}
				break;
			case 126: // Strength
				skillMenu = 2;
				if (player.getTemporaryAttributtes().get("leveledUp[2]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 2);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 20);
					LevelUp.switchFlash(player, 2, false);
				}
				break;
			case 127: // Defence
				skillMenu = 5;
				if (player.getTemporaryAttributtes().get("leveledUp[1]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 5);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 40);
					LevelUp.switchFlash(player, 1, false);
				}
				break;
			case 128: // Ranged
				skillMenu = 3;
				if (player.getTemporaryAttributtes().get("leveledUp[4]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 3);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 30);
					LevelUp.switchFlash(player, 4, false);
				}
				break;
			case 129: // Prayer
				if (player.getTemporaryAttributtes().get("leveledUp[5]") != Boolean.TRUE) {
					skillMenu = 7;
					player.getPackets().sendConfig(965, 7);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 60);
					LevelUp.switchFlash(player, 5, false);
				}
				break;
			case 130: // Magic
				if (player.getTemporaryAttributtes().get("leveledUp[6]") != Boolean.TRUE) {
					skillMenu = 4;
					player.getPackets().sendConfig(965, 4);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 33);
					LevelUp.switchFlash(player, 6, false);
				}
				break;
			case 131: // Runecrafting
				if (player.getTemporaryAttributtes().get("leveledUp[20]") != Boolean.TRUE) {
					skillMenu = 12;
					player.getPackets().sendConfig(965, 12);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 100);
					LevelUp.switchFlash(player, 20, false);
				}
				break;
			case 140: // Construction
				skillMenu = 23;
				if (player.getTemporaryAttributtes().get("leveledUp[21]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 23);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 698);
					LevelUp.switchFlash(player, 21, false);
				}
				break;
			case 133: // Hitpoints
				skillMenu = 6;
				if (player.getTemporaryAttributtes().get("leveledUp[3]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 6);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 50);
					LevelUp.switchFlash(player, 3, false);
				}
				break;
			case 134: // Agility
				skillMenu = 8;
				if (player.getTemporaryAttributtes().get("leveledUp[16]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 8);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 65);
					LevelUp.switchFlash(player, 16, false);
				}
				break;
			case 135: // Herblore
				skillMenu = 9;
				if (player.getTemporaryAttributtes().get("leveledUp[15]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 9);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 75);
					LevelUp.switchFlash(player, 15, false);
				}
				break;
			case 136: // Thieving
				skillMenu = 10;
				if (player.getTemporaryAttributtes().get("leveledUp[17]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 10);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 80);
					LevelUp.switchFlash(player, 17, false);
				}
				break;
			case 137: // Crafting
				skillMenu = 11;
				if (player.getTemporaryAttributtes().get("leveledUp[12]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 11);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 90);
					LevelUp.switchFlash(player, 12, false);
				}
				break;
			case 138: // Fletching
				skillMenu = 19;
				if (player.getTemporaryAttributtes().get("leveledUp[9]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 19);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 665);
					LevelUp.switchFlash(player, 9, false);
				}
				break;
			case 139: // Slayer
				skillMenu = 20;
				if (player.getTemporaryAttributtes().get("leveledUp[18]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 20);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 673);
					LevelUp.switchFlash(player, 18, false);
				}
				break;
			case 132: // Hunter
				skillMenu = 22;
				if (player.getTemporaryAttributtes().get("leveledUp[22]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 22);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 689);
					LevelUp.switchFlash(player, 22, false);
				}
				break;
			case 141: // Mining
				skillMenu = 13;
				if (player.getTemporaryAttributtes().get("leveledUp[14]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 13);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 110);
					LevelUp.switchFlash(player, 14, false);
				}
				break;
			case 142: // Smithing
				skillMenu = 14;
				if (player.getTemporaryAttributtes().get("leveledUp[13]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 14);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 115);
					LevelUp.switchFlash(player, 13, false);
				}
				break;
			case 143: // Fishing
				skillMenu = 15;
				if (player.getTemporaryAttributtes().get("leveledUp[10]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 15);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 120);
					LevelUp.switchFlash(player, 10, false);
				}
				break;
			case 144: // Cooking
				skillMenu = 16;
				if (player.getTemporaryAttributtes().get("leveledUp[7]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 16);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 641);
					LevelUp.switchFlash(player, 17, false);
				}
				break;
			case 145: // Firemaking
				skillMenu = 17;
				if (player.getTemporaryAttributtes().get("leveledUp[11]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 17);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 649);
					LevelUp.switchFlash(player, 11, false);
				}
				break;
			case 146: // Woodcutting
				skillMenu = 18;
				if (player.getTemporaryAttributtes().get("leveledUp[8]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 18);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 660);
					LevelUp.switchFlash(player, 8, false);
				}
				break;
			case 147: // Farming
				skillMenu = 21;
				if (player.getTemporaryAttributtes().get("leveledUp[19]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 21);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 681);
					LevelUp.switchFlash(player, 19, false);
				}
				break;
			case 148: // Summoning
				skillMenu = 24;
				if (player.getTemporaryAttributtes().get("leveledUp[23]") != Boolean.TRUE) {
					player.getPackets().sendConfig(965, 24);
				} else {
					lvlup = true;
					player.getPackets().sendConfig(1230, 705);
					LevelUp.switchFlash(player, 23, false);
				}
				break;
			}
			player.getInterfaceManager().sendInterface(lvlup ? 741 : 499);
			if (skillMenu != -1)
				player.getTemporaryAttributtes().put("skillMenu", skillMenu);
			}else if(interfaceId == 378) {
				if(componentId == 152 && !player.isRunning()) //click here to play
					player.run();
			}else if(interfaceId == 499) {
				int skillMenu = -1;
				if(player.getTemporaryAttributtes().get("skillMenu") != null)
					skillMenu = (Integer) player.getTemporaryAttributtes().get("skillMenu");
				switch(componentId) {
				case 10:
					player.getPackets().sendConfig(965, skillMenu);
					break;
				case 11:
					player.getPackets().sendConfig(965, 1024 + skillMenu);
					break;
				case 12:
					player.getPackets().sendConfig(965, 2048 + skillMenu);
					break;
				case 13:
					player.getPackets().sendConfig(965, 3072 + skillMenu);
					break;
				case 14:
					player.getPackets().sendConfig(965, 4096 + skillMenu);
					break;
				case 15:
					player.getPackets().sendConfig(965, 5120 + skillMenu);
					break;
				case 16:
					player.getPackets().sendConfig(965, 6144 + skillMenu);
					break;
				case 17:
					player.getPackets().sendConfig(965, 7168 + skillMenu);
					break;
				case 18:
					player.getPackets().sendConfig(965, 8192 + skillMenu);
					break;
				case 19:
					player.getPackets().sendConfig(965, 9216 + skillMenu);
					break;
				case 20:
					player.getPackets().sendConfig(965, 10240 + skillMenu);
					break;
				case 21:
					player.getPackets().sendConfig(965, 11264 + skillMenu);
					break;
				case 22:
					player.getPackets().sendConfig(965, 12288 + skillMenu);
					break;
				case 23:
					player.getPackets().sendConfig(965, 13312 + skillMenu);
					break;
				case 29: //close inter
					player.stopAll();
					break;
				}
		/*	} else if (interfaceId == 449) {
				if (componentId == 1) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					shop.sendInventory(player);
				} else if (componentId == 21) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					Integer slot = (Integer) player.getTemporaryAttributtes().get(
							"ShopSelectedSlot");
					if (slot == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.buy(player, slot, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						shop.buy(player, slot, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.buy(player, slot, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slot, 50);

				}
			} else if (interfaceId == 620) {
				if (componentId == 25) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.sendInfo(player, slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						shop.buy(player, slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.buy(player, slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						shop.buy(player, slotId, 50);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						shop.buy(player, slotId, 500);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						shop.sendExamine(player, slotId);
				}
			} else if (interfaceId == 621) {
				if (componentId == 0) {

					if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						player.getInventory().sendExamine(slotId);
					else {
						Shop shop = (Shop) player.getTemporaryAttributtes().get(
								"Shop");
						if (shop == null)
							return;
						if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
							shop.sendValue(player, slotId);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
							shop.sell(player, slotId, 1);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
							shop.sell(player, slotId, 5);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
							shop.sell(player, slotId, 10);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
							shop.sell(player, slotId, 50);
					}
					if (player.getTemporaryAttributtes().get("RingNPC") == Boolean.TRUE) {
				player.unlock();
				player.getAppearence().transformIntoNPC(-1);
				player.getTemporaryAttributtes().remove("RingNPC");
			}
				}*/
			} else if (interfaceId == 375) {
					player.getInterfaceManager().closeInventoryInterface();
					player.unlock();
					player.getAppearence().transformIntoNPC(-1);
		            player.getTemporaryAttributtes().remove("RingNPC");
					return;
			}else if (interfaceId == 374) {
					if (componentId >= 11 && componentId <= 15)
						player.setNextWorldTile(new WorldTile(
								FightPitsViewingOrb.ORB_TELEPORTS[componentId - 5]));
					else if (componentId == 5)
						player.stopAll();
			} else if (interfaceId == 449) {
				if (componentId == 1) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					shop.sendInventory(player);
				} else if (componentId == 21) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					Integer slot = (Integer) player.getTemporaryAttributtes().get(
							"ShopSelectedSlot");
					if (slot == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.buy(player, slot, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
						shop.buy(player, slot, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.buy(player, slot, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slot, 50);

				}
			} else if (interfaceId == 620) {
				if (componentId == 25) {//2518
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						shop.sendInfo(player, slotId);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						shop.buy(player, slotId, 1);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						shop.buy(player, slotId, 5);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						shop.buy(player, slotId, 10);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
						shop.buy(player, slotId, 50);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						shop.buy(player, slotId, 500);
					else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						shop.sendExamine(player, slotId);
				}
			} else if (interfaceId == 621) {
				if (componentId == 0) {

					if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getInventory().sendExamine(slotId);
					else {
						Shop shop = (Shop) player.getTemporaryAttributtes().get(
								"Shop");
						if (shop == null)
							return;
						if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
							shop.sendValue(player, slotId);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
							shop.sell(player, slotId, 1);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
							shop.sell(player, slotId, 5);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
							shop.sell(player, slotId, 10);
						else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET)
							shop.sell(player, slotId, 50);
					}
				}
			}else if (interfaceId == 771) {
				CharacterDesign.handle(player, componentId);
			}else if (interfaceId == 382) {
				if(componentId == 18) {
					player.stopAll();
					final WorldObject ditch = (WorldObject) player.getTemporaryAttributtes().get("wildernessditch");
					if(ditch == null)
						return;
					player.addStopDelay(4);
					player.setNextAnimation(new Animation(6132));
					final WorldTile toTile = new WorldTile(player.getX(), ditch.getY()+2, ditch.getPlane());
					player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2, 0));
					final ObjectDefinitions objectDef = ditch.getDefinitions();
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.setNextWorldTile(toTile, false);
							player.setNextFaceWorldTile(new WorldTile(ditch.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), ditch.getRotation())
									, ditch.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), ditch.getRotation())
									, ditch.getPlane()));
							player.getControlerManager().startControler("Wilderness");
						}
					}, 2);
				}
			} else if (interfaceId == 670) { // 670
			if (componentId == 0) {
				if (slotId >= player.getInventory().getItemsContainerSize())
					return;
				Item item = player.getInventory().getItem(slotId);
				if (item == null)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					if (sendWear(player, slotId, item.getId()))
						player.getPackets().sendIComponentText(667, 36,
								"Stab: " + ((player.getCombatDefinitions().getBonuses()[0]) > 0 ? "+" : "")
										+ player.getCombatDefinitions().getBonuses()[0]);
					player.getPackets().sendIComponentText(667, 37,
							"Slash: " + ((player.getCombatDefinitions().getBonuses()[1]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[1]);
					player.getPackets().sendIComponentText(667, 38,
							"Crush: " + ((player.getCombatDefinitions().getBonuses()[2]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[2]);
					player.getPackets().sendIComponentText(667, 39,
							"Magic: " + ((player.getCombatDefinitions().getBonuses()[3]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[3]);
					player.getPackets().sendIComponentText(667, 40,
							"Range: " + ((player.getCombatDefinitions().getBonuses()[4]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[4]);
					player.getPackets().sendIComponentText(667, 41,
							"Stab: " + ((player.getCombatDefinitions().getBonuses()[5]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[5]);
					player.getPackets().sendIComponentText(667, 42,
							"Slash: " + ((player.getCombatDefinitions().getBonuses()[6]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[6]);
					player.getPackets().sendIComponentText(667, 43,
							"Crush: " + ((player.getCombatDefinitions().getBonuses()[7]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[7]);
					player.getPackets().sendIComponentText(667, 44,
							"Magic: " + ((player.getCombatDefinitions().getBonuses()[8]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[8]);
					player.getPackets().sendIComponentText(667, 45,
							"Range: " + ((player.getCombatDefinitions().getBonuses()[9]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[9]);
					player.getPackets().sendIComponentText(667, 46,
							"Summoning: " + ((player.getCombatDefinitions().getBonuses()[10]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[10]);
					player.getPackets().sendIComponentText(667, 48,
							"Strength: " + ((player.getCombatDefinitions().getBonuses()[14]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[14]);
					player.getPackets().sendIComponentText(667, 49,
							"Ranged Str: " + ((player.getCombatDefinitions().getBonuses()[15]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[15]);
					player.getPackets().sendIComponentText(667, 50,
							"Prayer: " + ((player.getCombatDefinitions().getBonuses()[16]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[16]);
					player.getPackets().sendIComponentText(667, 51,
							"Magic Damage: " + ((player.getCombatDefinitions().getBonuses()[17]) > 0 ? "+" : "")
									+ player.getCombatDefinitions().getBonuses()[17] + "%");
					player.getTemporaryAttributtes().put("weight", WeightManager.calculateWeight(player));
					if (player.getTemporaryAttributtes().get("weight") != null)
						player.getPackets().sendIComponentText(667, 32,
								(double) player.getTemporaryAttributtes().get("weight") + "kg");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
					player.getInventory().sendExamine(slotId);
			}
			}  else if (interfaceId == 667) {
			if (componentId == 9) {
				if (packetId == 14) {
					sendRemove(player, slotId);
				} else if (packetId == 32) {
					//player.getEquipment().sendExamine(slotId);
				}
			} else if (componentId == 14) {
				if (slotId >= 14)
					return;
				Item item = player.getEquipment().getItem(slotId);
				if (item == null)
					return;
				if (packetId == 3)
					player.getInventory().sendExamine(slotId);
				else if (packetId == 216) {
					sendRemove(player, slotId);
					player.getPackets().sendIComponentText(667,36, "Stab: " + player.getCombatDefinitions().getBonuses()[0]);
					player.getPackets().sendIComponentText(667,37, "Slash: " + player.getCombatDefinitions().getBonuses()[1]);
					player.getPackets().sendIComponentText(667,38, "Crush: " + player.getCombatDefinitions().getBonuses()[2]);
					player.getPackets().sendIComponentText(667,39, "Magic: " + player.getCombatDefinitions().getBonuses()[3]);
					player.getPackets().sendIComponentText(667,40, "Range: " + player.getCombatDefinitions().getBonuses()[4]);
					player.getPackets().sendIComponentText(667,41, "Stab: " + player.getCombatDefinitions().getBonuses()[5]);
					player.getPackets().sendIComponentText(667,42, "Slash: " + player.getCombatDefinitions().getBonuses()[6]);
					player.getPackets().sendIComponentText(667,43, "Crush: " + player.getCombatDefinitions().getBonuses()[7]);
					player.getPackets().sendIComponentText(667,44, "Magic: " + player.getCombatDefinitions().getBonuses()[8]);
					player.getPackets().sendIComponentText(667,45, "Range: " + player.getCombatDefinitions().getBonuses()[9]);
					player.getPackets().sendIComponentText(667,46, "Summoning: " + player.getCombatDefinitions().getBonuses()[10]);
					player.getPackets().sendIComponentText(667,48, "Strength: " + player.getCombatDefinitions().getBonuses()[14]);
					player.getPackets().sendIComponentText(667,49, "Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
					player.getPackets().sendIComponentText(667,50, "Prayer: " + player.getCombatDefinitions().getBonuses()[16]);
					player.getPackets().sendIComponentText(667,51, "Magic Damage: " + player.getCombatDefinitions().getBonuses()[17]+"%");
					player.getTemporaryAttributtes().put("weight", WeightManager.calculateWeight(player));
					if (player.getTemporaryAttributtes().get("weight") != null)
						player.getPackets().sendIComponentText(667, 32,
								(double) player.getTemporaryAttributtes().get("weight") + "kg");
				}
			}
			} else if(interfaceId == 387) {
				System.out.println("Component: "+componentId+"");
				if(componentId == 53) {
					player.stopAll();
					ButtonHandler.openItemsKeptOnDeath(player);
				}else if(componentId == 56) {
					player.stopAll();
					player.getInterfaceManager().sendInterface(667);
					player.getInterfaceManager().sendInventoryInterface(670);
					player.getPackets().sendIComponentText(667,36, "Stab: " + player.getCombatDefinitions().getBonuses()[0]);
					player.getPackets().sendIComponentText(667,37, "Slash: " + player.getCombatDefinitions().getBonuses()[1]);
					player.getPackets().sendIComponentText(667,38, "Crush: " + player.getCombatDefinitions().getBonuses()[2]);
					player.getPackets().sendIComponentText(667,39, "Magic: " + player.getCombatDefinitions().getBonuses()[3]);
					player.getPackets().sendIComponentText(667,40, "Range: " + player.getCombatDefinitions().getBonuses()[4]);
					player.getPackets().sendIComponentText(667,41, "Stab: " + player.getCombatDefinitions().getBonuses()[5]);
					player.getPackets().sendIComponentText(667,42, "Slash: " + player.getCombatDefinitions().getBonuses()[6]);
					player.getPackets().sendIComponentText(667,43, "Crush: " + player.getCombatDefinitions().getBonuses()[7]);
					player.getPackets().sendIComponentText(667,44, "Magic: " + player.getCombatDefinitions().getBonuses()[8]);
					player.getPackets().sendIComponentText(667,45, "Range: " + player.getCombatDefinitions().getBonuses()[9]);
					player.getPackets().sendIComponentText(667,46, "Summoning: " + player.getCombatDefinitions().getBonuses()[10]);
					player.getPackets().sendIComponentText(667,48, "Strength: " + player.getCombatDefinitions().getBonuses()[14]);
					player.getPackets().sendIComponentText(667,49, "Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
					player.getPackets().sendIComponentText(667,50, "Prayer: " + player.getCombatDefinitions().getBonuses()[16]);
					player.getPackets().sendIComponentText(667,51, "Magic Damage: " + player.getCombatDefinitions().getBonuses()[17]+"%");
					player.getTemporaryAttributtes().put("weight", WeightManager.calculateWeight(player));
					if (player.getTemporaryAttributtes().get("weight") != null)
						player.getPackets().sendIComponentText(667, 32,
								(double) player.getTemporaryAttributtes().get("weight") + "kg");
					player.getPackets().sendRunScript(787, 1);
					player.getPackets().sendInterSetItemsOptionsScript(670, 0, 93, 4, 7, "Equip", "Compare", "Stats", "Examine");
					player.getPackets().sendUnlockIComponentOptionSlots(670, 0, 0, 27, 0, 1, 2, 3);
					player.getPackets().sendIComponentSettings(667, 14, 0, 13, 1030);
					player.getPackets().sendUnlockIComponentOptionSlots(667, 9, 0, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
				}else if(componentId == 63) {
					if(player.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendGameMessage("Please finish what you're doing before opening the price checker.");
						return;
					}
					player.stopAll();
					player.getPriceCheckManager().openPriceCheck();
				}
				}else if(interfaceId == 742) {
				if(componentId == 46) //close
					player.stopAll();
			}else if(interfaceId == 743) {
				if(componentId == 20) //close
					player.stopAll();
			}else if(interfaceId == 741) {
				if(componentId == 9) //close
					player.stopAll();
			}else if(interfaceId == 749) {
				if(componentId == 1) {
					if(packetId == 216) //activate
						player.getPrayer().switchQuickPrayers();
					else if(packetId == 19) //switch
						player.getPrayer().switchSettingQuickPrayer();
				}
			}else if (interfaceId == 750) {
				if(componentId == 1) {
					/*if(player.isResting()) {
						player.toogleRun(true);
						player.stopAll();
						return;
					}*/
				
			
					if(packetId == 216)
						//player.toogleRun(true);
						player.stopAll();
						if (player.getLockDelay() >= Utils.currentTimeMillis()) {
							player.getPackets()
									.sendGameMessage(
											"You can't toggle run while perfoming an action.");
							player.sendRunButtonConfig();
							return;
						}
						player.toogleRun(player.isResting() ? false : true);
						if (player.isResting())
							player.stopAll();
					else if (packetId == 19) {
					/*	player.stopAll();
						player.setResting(true);
						player.setNextAnimation(new Animation(5713));*/
						if (player.isResting()) {
							player.stopAll();
							return;
						}
						long currentTime = Utils.currentTimeMillis();
						if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
							player.getPackets().sendGameMessage(
									"You can't rest while perfoming an emote.");
							return;
						}
						if (player.getLockDelay() >= currentTime) {
							player.getPackets().sendGameMessage(
									"You can't rest while perfoming an action.");
							return;
						}
						player.stopAll();
						player.getActionManager().setAction(new Rest());
					}
				}			
			} else if (interfaceId == 334) {
				if(componentId == 21)
					player.closeInterfaces();
				else if (componentId == 20)
					player.getTrade().accept(false);
			} else if (interfaceId == 335) {
				if(componentId == 16)//18
				//	if(packetId == 216) 
					player.getTrade().accept(true);
				else if(componentId == 18)//20 
					player.closeInterfaces();
				else if(componentId == 30) {
					if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().removeItem(slotId, 1);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getTrade().removeItem(slotId, 5);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						player.getTrade().removeItem(slotId, 10);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getTrade().removeItem(slotId, Integer.MAX_VALUE);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
						player.getTemporaryAttributtes().put("trade_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().put("trade_isRemove", Boolean.TRUE);
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					}else if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().sendValue(slotId, false);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getTrade().sendExamine(slotId, false);
				}else if(componentId == 33) {
					if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().sendValue(slotId, true);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getTrade().sendExamine(slotId, true);
				}
			} else if (interfaceId == 336) {
				if(componentId == 0) {
					if(packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
						player.getTrade().addItem(slotId, 1);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getTrade().addItem(slotId, 5);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
						player.getTrade().addItem(slotId, 10);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
						player.getTrade().addItem(slotId, Integer.MAX_VALUE);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
						player.getTemporaryAttributtes().put("trade_item_X_Slot", slotId);
						player.getTemporaryAttributtes().remove("trade_isRemove");
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					}else if(packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET)
						player.getTrade().sendValue(slotId);
					else if(packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
						player.getInventory().sendExamine(slotId);
					if (packetId == 221)
						LendingManager.handleButtons(player, slotId);
				}
			} else if (interfaceId == 11) {
				if (componentId == 19)
					player.getBank().depositAllInventory(true);
				else if (componentId == 21)
					player.getBank().depositAllEquipment(true);
				else if (componentId == 16)
				if (packetId == 216)
					player.getBank().depositItem(slotId, 1, true);
				if (packetId == 19)
					player.getBank().depositItem(slotId, 5, true);
				if (packetId == 193)
					player.getBank().depositItem(slotId, 10, true);
				if (packetId == 76)
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
			} else if (interfaceId == 762) {
				if (componentId == 14)
					player.getBank().switchInsertItems();
				else if (componentId == 18)
					player.getBank().switchWithdrawNotes();
				else if (componentId == 20)
					player.getBank().depositAllInventory(true);
				else if (componentId == 22)
					player.getBank().depositAllEquipment(true);
			    else if (componentId >= 33 && componentId <= 49) {
					int tabId = 9 - ((componentId - 33) / 2);//9 - ((componentId - 33) / 2
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
						player.getBank().setCurrentTab(tabId);
					//long bankValue = player.getBank().getBankValue();
					//player.getPackets().sendIComponentText(762, 32, "Bank of "+Settings.SERVER_NAME+" <col=00ff00>(Bank Wealth: "+ Utils.formatDoubledAmount(bankValue)+ ")");
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
						player.getBank().collapse(tabId - 1);
			    } else if (componentId == 81) {
			    	if(packetId == 216) 
						player.getBank().withdrawItem(slotId, 1);
					else if (packetId == 19)
						player.getBank().withdrawItem(slotId, 5);
					else if (packetId == 193)
						player.getBank().withdrawItem(slotId, 10);
					else if (packetId == 76)
						player.getBank().withdrawLastAmount(slotId);
					else if (packetId == 173) {
						player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
						player.getTemporaryAttributtes().put("bank_isWithdraw", Boolean.TRUE);
						player.getPackets().sendRunScript(108, new Object[]{"Enter Amount:"});
					}
					else if (packetId == 89)
						player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
					else if (packetId == 221)
						player.getBank().withdrawItemButOne(slotId);
				}
			} else if (interfaceId == 640) {
				if (componentId == 18 || componentId == 22) {
					player.getTemporaryAttributtes().put("WillDuelFriendly", true);
					player.getPackets().sendConfig(283, 67108864);
				} else if (componentId == 19 || componentId == 21) {
					player.getTemporaryAttributtes().put("WillDuelFriendly", false);
					player.getPackets().sendConfig(283, 134217728);
				} else if (componentId == 20) {
					DuelControler.challenge(player);
				}
			} else if (interfaceId == 665) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (componentId == 0) {
					if (packetId == 216)
						player.getFamiliar().getBob().addItem(slotId, 1);
					else if (packetId == 19)
						player.getFamiliar().getBob().addItem(slotId, 5);
					else if (packetId == 193)
						player.getFamiliar().getBob().addItem(slotId, 10);
					else if (packetId == 76)
						player.getFamiliar().getBob()
						.addItem(slotId, Integer.MAX_VALUE);
					else if (packetId == 173) {
						player.getTemporaryAttributtes().put("bob_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().remove("bob_isRemove");
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						player.getInventory().sendExamine(slotId);
				}
			} else if (interfaceId == 671) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (componentId == 27) {
					if (packetId == 216)
						player.getFamiliar().getBob().removeItem(slotId, 1);
					else if (packetId == 19)
						player.getFamiliar().getBob().removeItem(slotId, 5);
					else if (packetId == 193)
						player.getFamiliar().getBob().removeItem(slotId, 10);
					else if (packetId == 76)
						player.getFamiliar().getBob()
						.removeItem(slotId, Integer.MAX_VALUE);
					else if (packetId == 173) {
						player.getTemporaryAttributtes().put("bob_item_X_Slot",
								slotId);
						player.getTemporaryAttributtes().remove("bob_isRemove");
						player.getPackets().sendRunScript(108,
								new Object[] { "Enter Amount:" });
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET)
						player.getInventory().sendExamine(slotId);
				}
			}else if(interfaceId == 763) {
				if(componentId == 0) {
					if(packetId == 216) 
						player.getBank().depositItem(slotId, 1, true);
					else if(packetId == 19) 
						player.getBank().depositItem(slotId, 5, true);
					else if(packetId == 193) 
						player.getBank().depositItem(slotId, 10, true);
					else if (packetId == 76)
						player.getBank().depositLastAmount(slotId);
					else if (packetId == 173) {
						player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
						player.getTemporaryAttributtes().remove("bank_isWithdraw");
						player.getPackets().sendRunScript(108, new Object[]{"Enter Amount:"});
					}else if (packetId == 89)
						player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
				}
			}else if (interfaceId == 884) {
					if (componentId == 4) {
				int weaponId = player.getEquipment().getWeaponId();
				if (player.hasInstantSpecial(weaponId)) {
					player.performInstantSpecial(weaponId);
					return;
				}
					player.getCombatDefinitions().switchUsingSpecialAttack();
			} else if (componentId >= 11 && componentId <= 14)
					player.getCombatDefinitions().setAttackStyle(componentId-11);
				else if (componentId == 15)
					player.getCombatDefinitions().switchAutoRelatie();
			}
			if (player.getUsername().equalsIgnoreCase("sagacity") || player.getUsername().equalsIgnoreCase("mike")) {
			player.getPackets().sendGameMessage("InterfaceId "+interfaceId+", componentId "+componentId+", slotId "+slotId+", PacketId: "+packetId);
			}
			System.out.println("InterfaceId "+interfaceId+", componentId "+componentId+", slotId "+slotId+", PacketId: "+packetId);
	}

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		player.stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null
				|| !player.getInventory().addItem(item.getId(),
						item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
		/*if (Runecrafting.isTiara(item.getId()))
			player.getPackets().sendConfig(491, 0);*/
		if (slotId == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		

}
	
	public static void openItemsKeptOnDeath(Player player) {
		player.getInterfaceManager().sendInterface(102);
		sendItemsKeptOnDeath(player, player.isAtWild() ? true : false);
	}
	
	public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
		/**
		 * Whether the player is skulled or not.
		 */
		boolean skulled = player.hasSkull();
		/**
		 * Will generate all of the items kept upon death.
		 */
		Integer[][] slots = getItemSlotsKeptOnDeath(player, wilderness,
				skulled, player.getPrayer().usingPrayer(0, 10)
						|| player.getPrayer().usingPrayer(1, 0));
		Item[][] items = getItemsKeptOnDeath(player, slots);
		/**
		 * Calculates risked and carried wealth based on the values in
		 * data/game/items/tipitdump.txt
		 */
		int riskedWealth = 0;
		int carriedWealth = 0;
		for (Item item : items[1])
			riskedWealth += item.getDefinitions().getTipitPrice() * item.getAmount();
		for (Item item : items[0])
			carriedWealth += item.getDefinitions().getTipitPrice() * item.getAmount();
		carriedWealth = carriedWealth + riskedWealth;
		/**
		 * Safe area - Defines whether the player is in a completely safe area
		 * such as a minigame or not - 1 = safe;
		 */
		Integer safeArea = 0;
		/**
		 * Defines the four (max) items kept upon death in the correct order,
		 * based on their value.
		 */
		Integer item1 = -1, item2 = -1, item3 = -1, item4 = -1;
		if (items[0].length > 0)
			item1 = (int) items[0][0].getId();
		if (items[0].length > 1)
			item2 = (int) items[0][1].getId();
		if (items[0].length > 2)
			item3 = (int) items[0][2].getId();
		if (items[0].length > 3)
			item4 = (int) items[0][3].getId();
		/**
		 * Defines the amount of items the player will keep upon death, includes
		 * prayer and wilderness affection.
		 */
		Integer amountKept = 4;
		if (skulled && player.getPrayer().isProtectingItem())
			amountKept = 1;
		else if (skulled && !player.getPrayer().isProtectingItem())
			amountKept = 0;
		else if (!skulled && !player.getPrayer().isProtectingItem())
			amountKept = 3;
		/**
		 * Sends the parameters as a runscript to the client, which will then
		 * generate the displayed items and information based on the player's
		 * currently held items.
		 */
		Object[] params = new Object[] { riskedWealth, carriedWealth, "",
				(player.getFamiliar() != null && player.getFamiliar().getBOBSize() > 0 ? 1 : 0), 0, item4, item3, item2,
				item1, amountKept, safeArea };
		player.getPackets().sendRunScript(118, params);
	}

	public static Item[][] getItemsKeptOnDeath(Player player, Integer[][] slots) {
		ArrayList<Item> droppedItems = new ArrayList<Item>();
		ArrayList<Item> keptItems = new ArrayList<Item>();
		for (int i : slots[0]) { // items kept on death
			Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null) // shouldnt
				continue;
			if (item.getAmount() > 1) {
				droppedItems.add(new Item(item.getId(), item.getAmount() - 1));
				item.setAmount(1);
			}
			keptItems.add(item);
		}
		for (int i : slots[1]) { // items droped on death
			Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null) // shouldnt
				continue;
			droppedItems.add(item);
		}
		for (int i : slots[2]) { // items protected by default
			Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null) // shouldnt
				continue;
			keptItems.add(item);
		}
		return new Item[][] { keptItems.toArray(new Item[keptItems.size()]),
				droppedItems.toArray(new Item[droppedItems.size()]) };

	}
	
	public static boolean sendWear(Player player, int slotId, int itemId) {
		if (player.hasFinished() || player.isDead())
			return false;
		player.stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		@SuppressWarnings("unused")
		String itemName = item.getDefinitions() == null ? "" : item
				.getDefinitions().getName().toLowerCase();
		if (item == null || item.getId() != itemId)
			return false;
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1 || item.getDefinitions().isNoted()) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		if (!ItemConstants.canWear(item, player))
			return true;
		boolean isTwoHandedWeapon = targetSlot == 3
				&& Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots()
				&& player.getEquipment().getWeaponId() != -1
				&& player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage(
					"Not enough free space in your inventory.");
			return false;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions()
				.getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequiriments) {
						player.getPackets()
								.sendGameMessage(
										"You are not high enough level to use this item.");
					}
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage(
							"You need to have a"
									+ (name.startsWith("a") ? "n" : "") + " "
									+ name + " level of " + level + ".");
				}

			}
		}
		if (!hasRequiriments)
			return true;
		if (!player.getControlerManager().canEquip(targetSlot, itemId))
			return false;
		player.stopAll(false, false);
		player.getInventory().deleteItem(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().addItem(
						player.getEquipment().getItem(5).getId(),
						player.getEquipment().getItem(5).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment()
							.getItem(3))) {
				if (!player.getInventory().addItem(
						player.getEquipment().getItem(3).getId(),
						player.getEquipment().getItem(3).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId() || !item
						.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory()
						.getItems()
						.set(slotId,
								new Item(player.getEquipment()
										.getItem(targetSlot).getId(), player
										.getEquipment().getItem(targetSlot)
										.getAmount()));
				player.getInventory().refresh(slotId);
			} else
				player.getInventory().addItem(
						new Item(player.getEquipment().getItem(targetSlot)
								.getId(), player.getEquipment()
								.getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot,
				targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		player.getAppearence().generateAppearenceData();
		//player.getCharges().wear(targetSlot);
		if (player.getHitpoints() > (player.getMaxHitpoints() * 1.15)) {
			player.setHitpoints(player.getMaxHitpoints());
			player.refreshHitPoints();
		}
		if (targetSlot == Equipment.SLOT_WEAPON && itemId != 15486) {
			if (player.polDelay > Utils.currentTimeMillis()) {
				player.setPolDelay(0);
				player.getPackets()
						.sendGameMessage(
								"The power of the light fades. Your resistance to melee attacks return to normal.");
			}
		}
		return true;
	}

	public static Integer[][] getItemSlotsKeptOnDeath(final Player player,
			boolean atWilderness, boolean skulled, boolean protectPrayer) {
		ArrayList<Integer> droppedItems = new ArrayList<Integer>();
		ArrayList<Integer> protectedItems = new ArrayList<Integer>();
		ArrayList<Integer> lostItems = new ArrayList<Integer>();
		boolean inRiskArea = FfaZone.inRiskArea(player);
		for (int i = 1; i < 44; i++) {
			Item item = i >= 16 ? player.getInventory().getItem(i - 16)
					: player.getEquipment().getItem(i - 1);
			if (item == null)
				continue;
			int stageOnDeath = item.getDefinitions().getStageOnDeath();
			/*if (ItemConstants.keptOnDeath(item) && atWilderness)
				protectedItems.add(i);
			else */if (!atWilderness && stageOnDeath == 1)
				protectedItems.add(i);
			else if (stageOnDeath == -1)
				lostItems.add(i);
			else
				droppedItems.add(i);
		}
		int keptAmount = (player.hasSkull() || inRiskArea) ? 0 : 3;
		if (protectPrayer)
			keptAmount++;
		if (droppedItems.size() < keptAmount)
			keptAmount = droppedItems.size();
		Collections.sort(droppedItems, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				Item i1 = o1 >= 16 ? player.getInventory().getItem(o1 - 16)
						: player.getEquipment().getItem(o1 - 1);
				Item i2 = o2 >= 16 ? player.getInventory().getItem(o2 - 16)
						: player.getEquipment().getItem(o2 - 1);
				int price1 = i1 == null ? 0
						: GrandExchange.getPrice(i1.getId());
				int price2 = i2 == null ? 0
						: GrandExchange.getPrice(i2.getId());
				if (price1 > price2)
					return -1;
				if (price1 < price2)
					return 1;
				return 0;
			}

		});
		Integer[] keptItems = new Integer[keptAmount];
		for (int i = 0; i < keptAmount; i++) {
			keptItems[i] = droppedItems.remove(0);
		}
		return new Integer[][] {
				keptItems,
				droppedItems.toArray(new Integer[droppedItems.size()]),
				protectedItems.toArray(new Integer[protectedItems.size()]),
				atWilderness ? new Integer[0] : lostItems
						.toArray(new Integer[lostItems.size()]) };

	}
}
