package com.rs.net.decoders.handlers;

import java.util.TimerTask;

import com.rs.game.Animation;
import com.rs.game.player.CoordsEvent;
import com.rs.cores.CoresManager;
import com.rs.game.ForceTalk;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.player.content.pet.Pets;
import com.rs.utils.Utils;
import com.rs.game.WorldTile;
import com.rs.game.World;
import com.rs.game.player.content.Teleports;
import com.rs.io.InputStream;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.GraveStone;
import com.rs.game.player.skills.hunter.ButterflyNetting;
import com.rs.game.player.skills.hunter.ButterflyNetting.Entities;
import com.rs.game.player.skills.hunter.Falconry;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.game.player.actions.mining.LivingMineralMining;
import com.rs.game.player.controlers.RuneEssenceController;
import com.rs.game.minigames.PuroPuro;
import com.rs.game.player.skills.fishing.Fishing;
import com.rs.game.player.skills.fishing.Fishing.FishingSpots;
import com.rs.game.player.content.CarrierTravel;
import com.rs.game.player.content.CarrierTravel.Carrier;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.dialogues.FremennikShipmaster;
import com.rs.game.player.actions.Listen;
import com.rs.game.player.skills.thieving.PickPocketAction;
import com.rs.game.player.skills.thieving.PickPocketableNPC;
import com.rs.utils.ShopsHandler;
import com.rs.game.player.skills.slayer.Slayer.SlayerMaster;
import com.rs.game.cities.Lumbridge;
import com.rs.game.cities.Edgeville;
import com.rs.game.cities.AlKharid;
import com.rs.game.cities.SeersVillage;
import com.rs.game.cities.Falador;
import com.rs.game.cities.Catherby;
import com.rs.game.cities.Varrock;
import com.rs.game.player.controlers.pestcontrol.PestControlRewards;

public class NPCHandler {
	
	public static void handleOption1(final Player player, InputStream stream) {
			stream.read128Byte();
			int npcIndex = stream.readUnsignedShort128();
			final NPC npc = World.getNPCs().get(npcIndex);
			if(npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
			player.stopAll(false);
			player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
				@Override
				public void run() {
					npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize())
							, player.getCoordFaceY(player.getSize())
							, player.getPlane()));
					player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize())
							, npc.getCoordFaceY(npc.getSize())
							, npc.getPlane()));
				if(npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
				//if(player.withinDistance(npc, 2)) {
					npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize())
							, player.getCoordFaceY(player.getSize())
							, player.getPlane()));
					player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize())
							, npc.getCoordFaceY(npc.getSize())
							, npc.getPlane()));
					player.getDialogueManager().startDialogue("Banker", npc.getId());
				//}
				return;
			}
					FishingSpots spot = FishingSpots.forId(npc.getId());
                if (spot != null) {
                    player.getActionManager().setAction(new Fishing(spot, npc, 0));
                    return;	
				} Entities impling = Entities.forNPCId(npc.getId());
                if (impling != null) {
                    player.getActionManager().setAction(new ButterflyNetting(impling, npc));
                    return;
				}
				else if (Lumbridge.processNPCOption1(player, npc))
                    return;
				else if (Falador.processNPCOption1(player, npc))
                    return;
				else if (AlKharid.processNPCOption1(player, npc))
                    return;
				else if (Varrock.processNPCOption1(player, npc))
                    return;
				else if (SeersVillage.processNPCOption1(player, npc))
                    return;
				else if (npc.getId() >= 5103 && npc.getId() <= 5105) {
                    HunterCore.tease(npc, player);
                    return;
				} else if (Pets.talkTo(player, npc))
                    return;
				else if (npc.getId() >= 5098 && npc.getId() <= 5100) {
            	player.stopAll(true);
            	player.faceEntity(npc);
            	player.getActionManager().setAction(new Falconry(npc));
            		return;
				} else if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					player.getActionManager().setAction(new LivingMineralMining((LivingRock) npc));
					return;
				} else if (npc instanceof GraveStone) {
					GraveStone grave = (GraveStone) npc;
					grave.sendGraveInscription(player);
					return;
				} else if (npc.getId() == 7448 && npc.getX() == 2204 && npc.getY() == 3808) {
						ShopsHandler.openShop(player, 174);
						return;
				} else if (SlayerMaster.startInteractionForId(player, npc.getId(), 1))
                    return;
					if (npc.getDefinitions().name.contains("Grand")|| npc.getDefinitions().name.contains("Grand Exchange Clerk") || npc.getDefinitions().name.contains("Grand_Exchange_Clerk"))  { 
						//ExchangeHandler.mainInterface(player);
				} else if (npc.getId() >= 5094 && npc.getId() <= 5096) {
                    Falconry.handleFalcons(npc, player);
                    return;
				}
					if (npc.getId() == 3793)
						PestControlRewards.openRewardsInterface(player);
					if (npc.getId() == 462)
						Teleports.sendInter(player);
					if (npc.getId() == 6971 && player.getSkills().getLevel(Skills.SUMMONING) < 99)
						player.getDialogueManager().startDialogue("Pikkupstix", npc.getId());
					if (npc.getId() == 6971 && player.getSkills().getLevel(Skills.SUMMONING) == 99)
						player.getDialogueManager().startDialogue("SummoningMaster", npc.getId());
					if (npc.getId() == 284)
						player.getDialogueManager().startDialogue("Doric", npc.getId());
					if (npc.getId() == 278)
						player.getDialogueManager().startDialogue("LumbyCook", npc.getId());
					if (npc.getId() == 3299)
						player.getDialogueManager().startDialogue("MartinMasterGardener", npc.getId());
					if (npc.getId() == 4288)
						player.getDialogueManager().startDialogue("Ajjat", npc.getId());
					if (npc.getId() == 4297)
						player.getDialogueManager().startDialogue("Sloane", npc.getId());
					if (npc.getId() == 682)
						player.getDialogueManager().startDialogue("RangingMaster", npc.getId());
					if (npc.getId() == 802)
						player.getDialogueManager().startDialogue("PrayerMaster", npc.getId());
					if (npc.getId() == 3809)
						player.getDialogueManager().startDialogue("CaptainDalbur", npc.getId());
					if (npc.getId() == 614)
						player.getDialogueManager().startDialogue("EcoStores", npc.getId());
					if (npc.getId() == 945)
						player.getDialogueManager().startDialogue("SelectMode", npc.getId());
					if (npc.getId() == 520 || npc.getId() == 521)
						player.getDialogueManager().startDialogue("GeneralShop", npc.getId());
					if (npc.getId() == 7875 || npc.getId() == 7876 || npc.getId() == 7879 || npc.getId() == 7882
					|| npc.getId() == 7873)
						player.getDialogueManager().startDialogue("Man", npc.getId());
					if (npc.getId() == 2634) {
						player.getDialogueManager().startDialogue("MissSchism", npc.getId());
						return;
					}
					if (npc.getId() == 2233) {
						player.getDialogueManager().startDialogue("OliviaDraynor", npc.getId());
					return;
					}
					if (npc.getId() == 4247)
						player.getDialogueManager().startDialogue("EstateAgent", npc.getId());
					if (npc.getId() == 548)
						player.getDialogueManager().startDialogue("Buy", npc.getId());
					if (npc.getId() == 705)
						player.getDialogueManager().startDialogue("MeleeTutor", npc.getId());
					if (npc.getId() == 970)
						player.getDialogueManager().startDialogue("Diango", npc.getId());
					if (npc.getId() == 4906)
						player.getDialogueManager().startDialogue("WoodcuttingMaster", npc.getId());
					if (npc.getId() == 925)
						player.getDialogueManager().startDialogue("LumbyGaurds", npc.getId());
					if (npc.getId() == 377 || npc.getId() == 378 || npc.getId() == 376)
						player.getDialogueManager().startDialogue("KaramjaTrip", npc.getId());
					if (npc.getId() == 5093) {
                    if (player.getEquipment().getWeaponId() == 10024 || player.getEquipment().getWeaponId() == 10023)
                        player.getDialogueManager().startDialogue("Matthias", npc.getId(), 2);
                    else
                        player.getDialogueManager().startDialogue("Matthias", npc.getId(), 0);
                    return;
					 } if (npc.getId() == 926)
						player.getDialogueManager().startDialogue("LumbyGaurds", npc.getId());
					if(npc.getId() == 600) 
						player.getDialogueManager().startDialogue("test", npc.getId(), true);
				 if (npc.getId() == 8273)
                        player.getDialogueManager().startDialogue("Turael", npc.getId());
               if (npc.getId() == 8274)
                        player.getDialogueManager().startDialogue("Maz", npc.getId());
                if (npc.getId() == 9085)
                        player.getDialogueManager().startDialogue("Kuradel", npc.getId());
                if (npc.getId() == 8275)
                        player.getDialogueManager().startDialogue("Duradel", npc.getId());
                if (npc.getId() == 1598)
                        player.getDialogueManager().startDialogue("Chaeldar", npc.getId());
					if (npc.getId() == 4295)
						player.getDialogueManager().startDialogue("Velio", npc.getId());
					if (npc.getId() == 4296)
						player.getDialogueManager().startDialogue("Varnis", npc.getId());
					if (npc.getId() == 4298)
						player.getDialogueManager().startDialogue("MrEx", npc.getId());
					if(npc.getId() == 599) 
						player.getDialogueManager().startDialogue("FremennikShipmaster", npc.getId(), true);
					else if(npc.getId() == 7872) 
						player.getDialogueManager().startDialogue("FremennikShipmaster", npc.getId(), false);
					else if (npc.getId() == 5157 || npc.getId() == 1765 || npc.getId() == 43 || npc.getId() == 5160
						|| npc.getId() == 5161 || npc.getId() == 5156) {
					final int npcId = npc.getId();
					if(Utils.getRandom(2) == 0) {
						npc.setNextForceTalk(new ForceTalk("Baa!"));
						npc.playSound(756, 1);
						npc.addWalkSteps(npcId, npcId, 4, true);
						npc.setRun(true);
						player.getPackets().sendGameMessage("The sheep runs away from you.");
					} else if (player.getInventory().containsItem(5603, 1) || player.getInventory().containsItem(1735, 1)) {
						player.playSound(761, 1);
						player.getInventory().addItem(1737, 1);
						player.getPackets().sendGameMessage("You shear the sheep of it's fleece.");
						player.setNextAnimation(new Animation(893));
						npc.transformIntoNPC(5149);
						CoresManager.fastExecutor1.schedule(new TimerTask() {
						    @Override
						    public void run() {
						    	npc.transformIntoNPC(npcId);
						    }
						}, 30000);
					} else
						player.getPackets().sendGameMessage("You need a pair of shears to shear the sheep.");
				}
					else if (npc.getId() == 382 || npc.getId() == 3294 || npc.getId() == 4316)
						player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), false);
					else if (npc.getId() == 3295)
						player.getDialogueManager().startDialogue("MiningGuildDwarf", npc.getId(), true);
					else
					player.getPackets().sendGameMessage("Nothing interesting happens.");
						//player.getDialogueManager().startDialogue("Talk", npc.getId());
					System.out.println("cliked 1 at npc id : "+npc.getId()+", "+npc.getX()+", "+npc.getY()+", "+npc.getPlane());
				}
			},  npc.getSize()));
	}
	
	public static void handleOption2(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			final NPC npc = World.getNPCs().get(npcIndex);
			//if(npc == null || npc.isDead() || npc.hasFinished()/* || !player.getMapRegionsIds().contains(npc.getRegionId())*/)
			//	return;
			player.stopAll(false);
			player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
				@Override
				public void run() {
					npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize())
							, player.getCoordFaceY(player.getSize())
							, player.getPlane()));
					player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize())
							, npc.getCoordFaceY(npc.getSize())
							, npc.getPlane()));
				if(npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")
				 || npc.getDefinitions().name.contains("TzHaar-Ket-Zuh")) {
					npc.setNextFaceWorldTile(new WorldTile(player.getCoordFaceX(player.getSize())
							, player.getCoordFaceY(player.getSize())
							, player.getPlane()));
					player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize())
							, npc.getCoordFaceY(npc.getSize())
							, npc.getPlane()));
					player.getBank().initBank();
				return;
			}
				FishingSpots spot = FishingSpots.forId(npc.getId());
                if (spot != null) {
                    player.getActionManager().setAction(new Fishing(spot, npc, 1));
                    return;
                }
					if (npc.getDefinitions().name.toLowerCase().equals("grand exchange clerk")) {
					    player.faceEntity(npc);
					    if (!player.withinDistance(npc, 2))
						return;
					    npc.faceEntity(player);
					    player.getGEManager().openGrandExchange();
					    return;
				}
					if (Pets.pickUp(player, npc))
                    return;
					else if (Catherby.processNPCOption2(player, npc))
                    return;
					else if (Edgeville.processNPCOption2(player, npc))
                    return;
					PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
					if (pocket != null) {
						player.getActionManager().setAction(
								new PickPocketAction(npc, pocket));
						return;
					}
					if (npc instanceof GraveStone) {
					GraveStone grave = (GraveStone) npc;
					grave.repair(player, false);
					return;
				}
					if (npc instanceof Familiar) {
						if (npc.getDefinitions().hasOption("store")) {
							if (player.getFamiliar() != npc) {
								player.getPackets().sendGameMessage(
										"That isn't your familiar.");
								return;
							}
							player.getFamiliar().store();
						} else if (npc.getDefinitions().hasOption("cure")) {
							if (player.getFamiliar() != npc) {
								player.getPackets().sendGameMessage(
										"That isn't your familiar.");
								return;
							}
							if (!player.getPoison().isPoisoned()) {
								player.getPackets().sendGameMessage(
										"Your arent poisoned or diseased.");
								return;
							} else {
								player.getFamiliar().drainSpecial(2);
								player.addPoisonImmune(120);
							}
						}
						return;
					}
					npc.faceEntity(player);
					if (!player.getControlerManager().processNPCClick2(npc))
						return;
					else if (SlayerMaster.startInteractionForId(player, npc.getId(), 2))
                    return;
					else if (npc.getDefinitions().name.contains("Musician") || npc.getId() == 3509) {
						player.stopAll();
						player.getActionManager().setAction(new Listen());
						return;
					}
					else if (npc.getId() == 4707) {
						player.getDialogueManager().startDialogue("MagicTutorClaim", npc.getId(), true);
						return;
					} else if (npc.getId() == 1861) {
						player.getDialogueManager().startDialogue("RangedTutorClaim", npc.getId(), true);
						return;
					}
					if (npc.getId() == 3809 || npc.getId() == 3812) {
						CarrierTravel.openGliderInterface(player);
						return;
					}
					else if (SeersVillage.processNPCOption2(player, npc))
                    return;
					else if (npc.getId() == 520 || npc.getId() == 521 || npc.getId() == 522 || npc.getId() == 523 || npc.getId() == 526
					|| npc.getId() == 527 || npc.getId() == 530 || npc.getId() == 531)
						ShopsHandler.openShop(player, 1);
					else if (npc.getId() == 548)
						ShopsHandler.openShop(player, 2);
					else if (npc.getId() == 6070)
						PuroPuro.openPuroInterface(player);
					else if (npc.getId() == 970)
						ShopsHandler.openShop(player, 98);
					else if (npc.getId() == 579)
						ShopsHandler.openShop(player, 170);
					else if (npc.getId() == 594)
						ShopsHandler.openShop(player, 171);
					else if (npc.getId() == 519)
						ShopsHandler.openShop(player, 58);
					else if (npc.getId() == 537)
						ShopsHandler.openShop(player, 173);
					else if (npc.getId() == 2620)
						ShopsHandler.openShop(player, 113);
					else if (npc.getId() == 2622)
						ShopsHandler.openShop(player, 112);
					else if (npc.getId() == 2623)
						ShopsHandler.openShop(player, 111);
					else if (npc.getId() == 558)
						ShopsHandler.openShop(player, 31);
					else if (npc.getId() == 569)
						ShopsHandler.openShop(player, 166);
					else if (npc.getId() == 571)
						ShopsHandler.openShop(player, 167);
					else if (npc.getId() == 836)
						ShopsHandler.openShop(player, 168);
					else if (npc.getId() == 546)
						ShopsHandler.openShop(player, 3);
					else if (npc.getId() == 551 || npc.getId() == 552)
						ShopsHandler.openShop(player, 4);
					else if (npc.getId() == 553)
						ShopsHandler.openShop(player, 6);
					else if (npc.getId() == 550)
						ShopsHandler.openShop(player, 7);
					else if (npc.getId() == 549)
						ShopsHandler.openShop(player, 8);
					else if (npc.getId() == 576)
						ShopsHandler.openShop(player, 10);
					else if (npc.getId() == 545)
						ShopsHandler.openShop(player, 13);
					else if (npc.getId() == 1658)
						ShopsHandler.openShop(player, 14);
					else if (npc.getId() == 589)
						ShopsHandler.openShop(player, 28);
					else if (npc.getId() == 590)
						ShopsHandler.openShop(player, 29);
					else if (npc.getId() == 2721)
						ShopsHandler.openShop(player, 30);
					else if (npc.getId() == 2720)
						ShopsHandler.openShop(player, 31);
					else if (npc.getId() == 2719)
						ShopsHandler.openShop(player, 32);
					else if (npc.getId() == 559)
						ShopsHandler.openShop(player, 33);
					else if (npc.getId() == 2718)
						ShopsHandler.openShop(player, 34);
					else if (npc.getId() == 540)
						ShopsHandler.openShop(player, 35);
					else if (npc.getId() == 541)
						ShopsHandler.openShop(player, 36);
					else if (npc.getId() == 542)
						ShopsHandler.openShop(player, 37);
					else if (npc.getId() == 544)
						ShopsHandler.openShop(player, 38);
					else if (npc.getId() == 1783)
						ShopsHandler.openShop(player, 39);
					else if (npc.getId() == 5555)//needname
						ShopsHandler.openShop(player, 40);
					else if (npc.getId() == 5555)//needname
						ShopsHandler.openShop(player, 41);
					else if (npc.getId() == 1917)//needname
						ShopsHandler.openShop(player, 42);
					else if (npc.getId() == 538)
						ShopsHandler.openShop(player, 43);
					else if (npc.getId() == 580)
						ShopsHandler.openShop(player, 44);
					else if (npc.getId() == 577)
						ShopsHandler.openShop(player, 45);
					else if (npc.getId() == 584)
						ShopsHandler.openShop(player, 46);
					else if (npc.getId() == 581)
						ShopsHandler.openShop(player, 47);
					else if (npc.getId() == 5555)//needname
						ShopsHandler.openShop(player, 48);
					else if (npc.getId() == 9159)//Faruq
						ShopsHandler.openShop(player, 161);
					else if (npc.getId() == 659)//Party pete
						ShopsHandler.openShop(player, 162);
					else if (npc.getId() == 570)//gem merchant
						ShopsHandler.openShop(player, 164);
					else if (npc.getId() == 2233)//Olivia
						ShopsHandler.openShop(player, 165);
					if(npc.getId() == 9707) 
						FremennikShipmaster.sail(player, true);
					else if(npc.getId() == 9708) 
						FremennikShipmaster.sail(player, false);
					if (npc.getId() == 3781)
						player.setNextWorldTile(new WorldTile(3041, 3202, 0));
					if (npc.getId() == 3801)
						player.setNextWorldTile(new WorldTile(2659, 2676, 0));
					if (npc.getId() == 8461)
						player.getDialogueManager().startDialogue("Turael", npc.getId());
					if (npc.getId() == 14057)
						player.getDialogueManager().startDialogue("Velio", npc.getId());
					if (npc.getId() == 14078)
						player.getDialogueManager().startDialogue("Varnis", npc.getId());
					if (npc.getId() == 300) {
					if (player.runeMysteries < 5) {
						player.getPackets().sendGameMessage("You need to have completed the Rune Mysteries Quest to use this feature.");
						return;
					}
                        RuneEssenceController.teleport(player, npc);
					}
					else
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					System.out.println("cliked 2 at npc id : "+npc.getId()+", "+npc.getX()+", "+npc.getY()+", "+npc.getPlane());
				}
			},  npc.getSize()));
	}
	
	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShortLE();
        boolean forceRun = stream.readByte() == 1;
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId()))
            return;
        player.stopAll(false);
        System.err.println("Clicked option 3 on NPC: " + npc.getId());
        if (forceRun)
            player.setRun(forceRun);
        player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
            @Override
            public void run() {
                npc.resetWalkSteps();
                if (!player.getControlerManager().processNPCClick3(npc))
                    return;
				if (Varrock.processNPCOption3(player, npc))
                    return;
				if(npc.getDefinitions().name.contains("Banker") || npc.getDefinitions().name.contains("banker")) {
					player.getGEManager().openCollectionBox();
					if (npc.getDefinitions().name.contains("Grand")|| npc.getDefinitions().name.contains("Grand Exchange Clerk") || npc.getDefinitions().name.contains("Grand_Exchange_Clerk"))  { 
						player.getGEManager().openHistory();
					}
				} else if (npc instanceof GraveStone) {
					GraveStone grave = (GraveStone) npc;
					grave.repair(player, true);
					return;
                } else if (npc.getId() == 6528) {
					player.getGEManager().openHistory();
				} else if (npc.getId() == 1597 || npc.getId() == 8461 || npc.getId() == 8462 || npc.getId() == 8464) {
						ShopsHandler.openShop(player, 54);
						return;
				} else if (npc.getId() == 836) {
					player.getDialogueManager().startDialogue("BuyShantayPass", npc.getId());
				}
                player.faceEntity(npc);
                npc.faceEntity(player);
            }
        }, npc.getSize()));
	}
	
	public static void handleOption4(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShortLE();
        boolean forceRun = stream.readByte() == 1;
        final NPC npc = World.getNPCs().get(npcIndex);
        if (npc == null || npc.isCantInteract() || npc.isDead() || npc.hasFinished()
                || !player.getMapRegionsIds().contains(npc.getRegionId())) {
            return;
        }
        System.err.println("Clicked option 4 on NPC: " + npc.getId());
        player.stopAll(false);
        if (forceRun) {
            player.setRun(forceRun);
        }
		if (npc instanceof GraveStone) {
					GraveStone grave = (GraveStone) npc;
					grave.demolish(player);
					return;
				}
		if (npc.getDefinitions().name.toLowerCase().equals("grand exchange clerk")) {
					    player.faceEntity(npc);
					    if (!player.withinDistance(npc, 2))
						return;
					    npc.faceEntity(player);
					    player.getGEManager().openSets();
					    return;
				}
		else if (npc.getId() == 1597) {
		player.getSlayerManager().sendSlayerInterface();
		}
        player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
            @Override
            public void run() {
                npc.resetWalkSteps();
				player.faceEntity(npc);
                npc.faceEntity(player);
                if (npc.getId() == 6528)
				player.getGEManager().openHistory();
                //layer.faceEntity(npc);
                //npc.faceEntity(player);
                return;
            }
        }, npc.getSize()));
	}

}
