package com.rs.net.decoders.handlers;

import com.rs.utils.Logger;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.game.minigames.CastleWars;

import java.util.Arrays;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.player.content.FairyRings;
import com.rs.game.player.controlers.GodWars;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.skills.hunter.HunterCore;
import com.rs.game.player.skills.thieving.PyramidPlunderControler;
import com.rs.utils.ItemExamines;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.player.content.agility.Shortcuts;
import com.rs.game.player.actions.Spinning;
import com.rs.game.player.actions.Spinning.SpinningItem;
import com.rs.game.player.skills.crafting.Pottery;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.player.actions.CowMilkingAction;
import com.rs.game.player.skills.smithing.Smelting;
import com.rs.game.player.content.GodswordCreating;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.FightPits;
import com.rs.game.player.content.custom.SlayerTeleports;
import com.rs.game.minigames.PuroPuro;
import com.rs.game.minigames.bountyhunter.BountyHunter;
import com.rs.game.player.skills.thieving.Thieving;
import com.rs.game.minigames.creations.StealingCreation;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.pest.Lander;
import com.rs.game.npc.NPC;
import com.rs.game.ForceTalk;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.player.ClueScrolls;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.EmotesManager;
import com.rs.game.player.cutscenes.WolfWhistle;
import com.rs.game.player.cutscenes.WolfWhistle1;
import com.rs.game.player.Equipment;
import com.rs.game.ForceMovement;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.content.quests.CooksAssistant;
import com.rs.game.player.content.quests.DoricsQuest;
import com.rs.game.player.content.Pickables;
import com.rs.game.player.content.agility.Agility;
import com.rs.game.player.content.quests.QuestNotAdded;
import com.rs.game.player.Inventory;
import com.rs.game.player.OwnedObjectManager;
import com.rs.utils.SerializableFilesManager;
import com.rs.game.player.content.clans.Clan;
import com.rs.game.player.content.clans.ClanMember;
import com.rs.game.player.content.clans.Clan.ClanRanks;
import com.rs.game.player.Player;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.FightPitsViewingOrb;
import com.rs.game.player.skills.herblore.HerbCleaning;
import com.rs.game.player.skills.runecrafting.Runecrafting;
import com.rs.game.player.actions.Listen;
import com.rs.game.player.skills.summoning.Summoning;
import com.rs.game.player.actions.WhirlPool;
import com.rs.game.player.content.agility.BarbarianOutpostAgility;
import com.rs.game.player.content.agility.GnomeAgility;
import com.rs.game.player.content.agility.WildernessAgility;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.objects.Clipedobject;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.game.player.skills.thieving.PickPocketAction;
import com.rs.game.player.skills.thieving.PickPocketableNPC;
import com.rs.game.player.content.Burying.Bone;
import com.rs.game.player.content.CharacterDesign;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.Hunter;
import com.rs.game.player.content.ReportAbuse;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.ShootingStars;
import com.rs.game.player.content.SpiritTree;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.content.ToyHorsey;
import com.rs.game.player.content.Shop;
import com.rs.game.player.skills.firemaking.Firemaking;
import com.rs.game.player.dialogues.FremennikShipmaster;
import com.rs.game.player.skills.woodcutting.Woodcutting;
import com.rs.game.player.skills.woodcutting.Woodcutting.TreeDefinitions;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.io.OutputStream;
import com.rs.net.Session;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.game.player.actions.Rest;
import com.rs.utils.Logger;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;
import com.rs.game.player.content.Foods;
import com.rs.game.player.controlers.Barrows;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.actions.mining.EssenceMining;
import com.rs.game.player.actions.mining.EssenceMining.EssenceDefinitions;
import com.rs.game.player.actions.mining.Mining;
import com.rs.game.player.skills.crafting.JewelleryCrafting;
import com.rs.game.player.actions.mining.Mining.RockDefinitions;
import com.rs.game.player.actions.mining.MiningBase;
import com.rs.game.player.actions.mining.LivingMineralMining;
import com.rs.game.player.skills.magic.MagicOnItem;
import com.rs.game.map.signposts.Signposts;
import com.rs.game.player.content.Canoes;
import com.rs.game.cities.AlKharid;
import com.rs.game.cities.DorgeshKaan;
import com.rs.game.cities.Lumbridge;
import com.rs.game.cities.MosleHarmless;
import com.rs.game.cities.Varrock;
import com.rs.game.cities.Falador;
import com.rs.game.cities.FeldipHills;
import com.rs.game.cities.FremennikProvince;
import com.rs.game.map.doors.Doors;
import com.rs.game.map.doors.Gates;
import com.rs.game.map.doors.DoubleDoors;

/**
 * Simplifies the event which occurs when a certain object is clicked
 * (OBJECT_ClICK_1 packet) so that the end-user of the source will not need to
 * deal with the hassle of adding ifs and other code in WorldPacketsDecoder. <br />
 * I haven't taken the time to copy all the implemented IDs from
 * WorldPacketsDecoder, however. (Besides Mining)
 * 
 * @author Cyber Sheep
 */

public class ObjectHandler {


	public static void handleOption1(Player player, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			final int  x = stream.readUnsignedShort();
			@SuppressWarnings("unused")
			int junk = stream.readUnsignedByte128();
			final int id = stream.readUnsignedShortLE();
			final int  y = stream.readUnsignedShortLE();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			int destX = player.getX();
			int destY = player.getY();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.stopAll();
					player.faceObject(object);
					/*player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));*/
					if(!player.getControlerManager().processObjectClick1(object))
						return;
					if (CastleWars.handleObjects(player, id))
						return;
					else if (Shortcuts.Use(player, object))
					return;
					else if (Signposts.handleSignpost(player, object))
					return;
					else if (player.getFarmingManager().isFarming(id, null, 1))
					return;
					else if (Runecrafting.handleAltars(player, object))
					return;
					else if (Pottery.handleObjectClick(object, player))
					return;
					else if (HunterCore.handleHunterObjects1(object, player))
					return;
					else if (AlKharid.handleObjectClick1(object, player))
					return;
					else if (FeldipHills.handleObjectClick1(object, player))
					return;
					else if (DorgeshKaan.handleObjectClick1(object, player))
					return;
					else if (Lumbridge.handleObjectClick1(object, player))
					return;
					else if (Varrock.handleObjectClick1(object, player))
					return;
					else if (Falador.handleObjectClick1(object, player))
					return;
					else if (MosleHarmless.handleObjectClick1(object, player))
					return;
					else if (FremennikProvince.handleObjectClick1(object, player))
					return;
					else if (GodWars.handleObjects(player, object))
					return;
					else if (Mining.mineRocks(object, player))
					return;
					else if (Woodcutting.canChop(player, object))
					return;
					else if (DoubleDoors.isDoubleDoor(object)) {
					DoubleDoors.handleDoor(player, object);
					return;
				} else if (object.getId() >= 16543 && object.getId() <= 16546) {
					player.sendMessage("You search the mysterious looking door..");
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							if (object.getId() != PyramidPlunderControler.Entrances.get(0)) {
								player.sendMessage("..and quickly realize it leads to nowhere.");
							} else {
								player.sendMessage("..and quickly realize it leads into the pyramid.");
								//player.setNextWorldTile(new WorldTile(1934, 4450, 2));
								//player.getControlerManager().startControler("PyramidPlunderControler");
							}
							player.unlock();
						}

					}, 1);
				} else if (id == 10251) { // Slayer portal
					SlayerTeleports.sendInter(player);	
					//TODO: Replace Terribly Hardcoded stalls
				} else if (id == 4874) {
					player.getInventory().addItem(1891, 1);
					player.setNextAnimation(new Animation(881));
					player.addFreezeDelay(1L);
					player.getSkills().addXp(17, 16D);
					Thieving.checkGuards(player);
					} else if (id == 4875) {
					if (player.getSkills().getLevel(Skills.THIEVING) <= 15) {
					player.getPackets().sendGameMessage("You need an thieving level of 15 to steal from this stall",
					true);
					return;
					} else if (player.getInventory().getFreeSlots() <= 0) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.", true);
					return;
					} else if (player.getSkills().getLevel(Skills.THIEVING) >= 15) {
					player.getInventory().addItem(950, 1);
					player.setNextAnimation(new Animation(881));
					player.addFreezeDelay(1L);
					player.getSkills().addXp(17, 26D);
					Thieving.checkGuards(player);
					}
					} else if (id == 4876) {
					if (player.getSkills().getLevel(Skills.THIEVING) <= 35) {
					player.getPackets().sendGameMessage("You need an thieving level of 35 to steal from this stall",
					true);
					return;
					} else if (player.getInventory().getFreeSlots() <= 0) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.", true);
					return;
					} else if (player.getSkills().getLevel(Skills.THIEVING) >= 35) {
					player.getInventory().addItem(1635, 1);
					player.setNextAnimation(new Animation(881));
					player.addFreezeDelay(1L);
					player.getSkills().addXp(17, 32D);
					Thieving.checkGuards(player);
					}
					} else if (id == 4877) {
					if (player.getSkills().getLevel(Skills.THIEVING) <= 45) {
					player.getPackets().sendGameMessage("You need an thieving level of 45 to steal from this stall",
					true);
					return;
					} else if (player.getInventory().getFreeSlots() <= 0) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.", true);
					return;
					} else if (player.getSkills().getLevel(Skills.THIEVING) > 45) {
					player.getInventory().addItem(7650, 1);
					player.setNextAnimation(new Animation(881));
					player.addFreezeDelay(1L);
					player.getSkills().addXp(17, 40D);
					Thieving.checkGuards(player);
					}
					} else if (id == 4878) {
					if (player.getSkills().getLevel(Skills.THIEVING) <= 80) {
					player.getPackets().sendGameMessage("You need an thieving level of 80 to steal from this stall",
					true);
					return;
					} else if (player.getInventory().getFreeSlots() <= 0) {
					player.getPackets().sendGameMessage("Not enough space in your inventory.", true);
					return;
					} else if (player.getSkills().getLevel(Skills.THIEVING) > 80) {
					player.getInventory().addItem(1331, 1);
					player.setNextAnimation(new Animation(881));
					player.addFreezeDelay(1L);
					player.getSkills().addXp(17, 50D);
					Thieving.checkGuards(player);
					}
				} else if (id == 25014) { // PuroPuro
					player.getControlerManager().forceStop();
					Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new WorldTile(2427, 4446, 0), 9, false, Magic.OBJECT_TELEPORT);					
				} else if (id == 24991) {
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.getControlerManager().startControler("PuroPuro");
						}
					}, 10);
					Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new WorldTile(2591, 4320, 0), 9, false, Magic.OBJECT_TELEPORT);
				} else if (object.getId() == 15653) {
					player.getControlerManager().startControler("WarriorsGuild");
					return;
				} else if (id == 2469 && object.getX() == 2205 && object.getY() == 3811) {
					player.setNextWorldTile(new WorldTile(3804, 3548, 0));
				} else if (id == 16944 || id == 14097 || id == 14064 || id == 14079 || id == 14058 || id == 23047 || id == 14073 || id == 14148 || id == 14067 || id == 14133 || id == 14142 || id == 14151 || id == 14154 || id == 68234 || id == 14145 || id == 14109 || id == 14112 || id == 14130 || id == 14121 || id == 52679 || id == 14118 || id == 52613 || id == 14082 || id == 14100 || id == 14106 || id == 16184 || id == 61498 || id == 14103 || id == 14091 || id == 14061 || id == 14076 || id == 14136 || id == 14115) {
					FairyRings.openRingInterface(player, object, false);
				} else if (id == 12128) {
					FairyRings.openRingInterface(player, object, true);
				} else if (id == 4499) {
					player.setNextWorldTile(new WorldTile(2808, 10002, 0));
				} else if (id == 4500) {
					player.setNextWorldTile(new WorldTile(2796, 3615, 0));
				} else if (id == 5008) {
					player.setNextWorldTile(new WorldTile(2780, 10161, 0));
				} else if (id == 10137 && object.getX() == 2149 && object.getY() == 5088) {
					player.setNextWorldTile(new WorldTile(2152, 5088, 0));
				} else if (id == 10137 && object.getX() == 2152 && object.getY() == 5109) {
					player.setNextWorldTile(new WorldTile(2150, 5109, 0));
				} else if (id == 1738 || id == 2641) {
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY(), player.getPlane() + 1));
				} else if (id == 5973) {
					player.setNextWorldTile(new WorldTile(2730, 3713, 0));
				 } else if (object.getId() == 15478 || object.getId() == 15482) {
					player.getDialogueManager().startDialogue("EnterHouseD");
					return;
				} else if (id == 115 || id == 117 || id == 118 || id == 121 || id == 122) {
					player.addWalkSteps(object.getX(), object.getY() , -1, false);
					player.setNextAnimation(new Animation(794));
                    player.getPackets().sendSound(279, 1);
				} else if (id == 10167) {
						player.useStairs(833, new WorldTile(player.getX(), player.getY(), 2), 1, 2);
				} else if (id == 10168) {
						player.useStairs(833, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
				 } else if (id == 26934) {
						player.useStairs(833, new WorldTile(3097, 9868, 0), 1, 2);
				 } else if (object.getId() == 28676) {
						player.useStairs(827, new WorldTile(2207, 5346, 0), 1, 2);
						return;
				} else if (object.getId() == 24360) {
						player.useStairs(-1, new WorldTile(3190, 9834, 0), 1, 2);
						return;
				} else if (object.getId() == 24365) {
						player.useStairs(-1, new WorldTile(3188, 3433, 0), 1, 2);
						return;
				 } else if (object.getId() == 26933) { //Edge trapdoor
					WorldObject openedHole = new WorldObject(object.getId()+1,
					object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
					World.spawnTemporaryObject(openedHole, 60000, true);
					return;
				} else if (object.getId() == 30571) { //trapdoor
					player.useStairs(827, new WorldTile(3424, 3484, 0), 1, 2);
					return;
				} else if (id == 30572) {//climb down to lumby basement
					player.useStairs(828, new WorldTile(3209, 9617, 0), 2, 1, "You climb down the trapdoor.");
					return;
					} else if (id == 36687) {//climb down to lumby basement
					player.useStairs(828, new WorldTile(3209, 9617, 0), 2, 1, "You climb down the trapdoor.");
					return;
					}
					else if (id == 29355 && object.getX() == 3209 && object.getY() == 9616) {//climb up from lumby basement
					player.useStairs(828, new WorldTile(3210, 3216, 0), 2, 1, "You climb up the ladder.");
					return;
					}
					else if (id == 4493) {
					player.useStairs(-1, new WorldTile(3433, 3538, 1), 2, 1, "You climb up the Staircase.");
					return;
					}
					else if (id == 41425) {
					player.useStairs(-1, new WorldTile(2720, 9775, 0), 2, 1, "You climb down the Staircase.");
					return;
					}
					else if (id == 32048) {
					player.useStairs(-1, new WorldTile(2723, 3375, 0), 2, 1, "You climb up the Staircase.");
					return;
					}
					else if (id == 41435) {
					player.useStairs(-1, new WorldTile(player.getX(), player.getY() + 4, 1), 2, 1, "You climb up the Staircase.");
					return;
					}
					else if (id == 41436) {
					player.useStairs(-1, new WorldTile(player.getX(), player.getY() - 4, 0), 2, 1, "You climb down the Staircase.");
					return;
					}
					else if (id == 4494) {
					player.useStairs(-1, new WorldTile(3438, 3538, 0), 2, 1, "You climb down the Staircase.");
					return;
					}
					else if (id == 4495) {
					player.useStairs(-1, new WorldTile(3417, 3541, 2), 2, 1, "You climb up the Staircase.");
					return;
					}
					else if (id == 4496) {
					player.useStairs(-1, new WorldTile(3412, 3541, 1), 2, 1, "You climb down the Staircase.");
					return;
					}
					else if (id == 36795) {//climb up lumby windmill
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 2, 1, "You climb up the ladder.");
					return;
					}
					else if (id == 36797) {//climb down lumby windmill
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 2, 1, "You climb down the ladder.");
					return;
					}
					else if (id == 3205) {//climb down barbarian agility ladder
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 2, 1, "You climb down the ladder.");
					return;
					}
					else if (id == 2797) {//climb down from watchtower top floor
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
					}
					else if (id == 17122) {//climb down from watchtower second floor
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
					}
					else if (id == 2833) {//climb up to watchtower second floor
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
					}
					else if (id == 2796) {//climb up to watchtower top floor
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
					}
					else if (id == 36775) {
					player.useStairs(-1, new WorldTile(player.getX(), player.getY(), 1), 2, 1, "You climb down the staircase.");
					return;
					}
					else if (id == 32015) {
					player.useStairs(828, new WorldTile(2884, 3398, 1), 2, 1, "You climb up the ladder.");
					return;
					}
					else if (id == 20987) {
					player.useStairs(828, new WorldTile(2884, 9798, 1), 2, 1, "You climb down the ladder.");
					return;
					}
					else if (id == 5097) { //climb up brimhaven dungeons stairs
					player.useStairs(-1, new WorldTile(2636, 9510, 2), 2, 1, "You climb up the stairs.");
					return;
					}
					else if (id == 5098) { //climb down brimhaven dungeons stairs
					player.useStairs(-1, new WorldTile(2637, 9517, 0), 2, 1, "You climb down the stairs.");
					return;
					}
					else if (id == 36778) {
					player.useStairs(-1, new WorldTile(player.getX(), player.getY(), 1), 2, 1, "You climb down the staircase.");
					return;
					}
					else if (id == 22945) { //bone door to Dorgesh kaan dungeon
					player.useStairs(-1, new WorldTile(3318, 9602, 0), 2, 1, "");
					return;
					} else if (id == 32952) { //bone door to Dorgesh kaan
					player.useStairs(-1, new WorldTile(2747, 5374, 0), 2, 1, "");
					return;
					} else if (id == 5110 && object.getX() == 2649
						&& object.getY() == 9561) { // Steps brimhaven
					player.lock(16);
					WorldTasksManager.schedule(new WorldTask() {
						int x;

						@Override
						public void run() {
							if (x++ == 7) {
								stop();
								return;
							}
							if (x == 1 || x == 2) {
								final WorldTile toTile = new WorldTile(player
										.getX(), player.getY() - 1, player
										.getPlane());
								player.setNextForceMovement(new ForceMovement(
										toTile, 1, ForceMovement.SOUTH));
								player.setNextWorldTile(toTile);
							}
							if (x == 3 || x == 4) {
								final WorldTile toTile = new WorldTile(player
										.getX() - 1, player.getY(), player
										.getPlane());
								player.setNextForceMovement(new ForceMovement(
										toTile, 1, ForceMovement.WEST));
								player.setNextWorldTile(toTile);
							}
							if (x > 4) {
								final WorldTile toTile = new WorldTile(player
										.getX(), player.getY() - 1, player
										.getPlane());
								player.setNextForceMovement(new ForceMovement(
										toTile, 1, ForceMovement.SOUTH));
								player.setNextWorldTile(toTile);
							}
							player.setNextAnimation(new Animation(741));
						}
					}, 0, 1);
						}  else if (id == 5111 && object.getX() == 2647
						&& object.getY() == 9558) { // Steps brimhaven
					player.lock(16);
					WorldTasksManager.schedule(new WorldTask() {
						int y;

						@Override
						public void run() {
							if (y++ == 7) {
								stop();
								return;
							}
							if (y < 4) {
								final WorldTile toTile = new WorldTile(player
										.getX(), player.getY() + 1, player
										.getPlane());
								player.setNextForceMovement(new ForceMovement(
										toTile, 1, ForceMovement.NORTH));
								player.setNextWorldTile(toTile);
							}
							if (y > 3 && y < 7) {
								final WorldTile toTile = new WorldTile(player
										.getX() + 1, player.getY(), player
										.getPlane());
								player.setNextForceMovement(new ForceMovement(
										toTile, 1, ForceMovement.EAST));
								player.setNextWorldTile(toTile);
							}
							if (y > 5) {
								final WorldTile toTile = new WorldTile(player
										.getX(), player.getY() + 1, player
										.getPlane());
								player.setNextForceMovement(new ForceMovement(
										toTile, 1, ForceMovement.NORTH));
								player.setNextWorldTile(toTile);
							}
							player.setNextAnimation(new Animation(741));
						}
					}, 0, 1);
				} else if (id == 28121) {
					BountyHunter.enterBounty(player);
				} else if (id == 28119) {
					BountyHunter.leaveBounty(player);
				} else if (id >= 35542 && id <= 35544 && player.getY() == 3117 && player.getInventory().containsItem(1854, 1)) { // go through shantay pass
					player.getInventory().deleteItem(1854, 1);
					player.lock(2);
					player.stopAll();
					player.addWalkSteps(player.getX() >= object.getX() ? object.getX() - 1 : object.getX(), 3115 , -1, false);
					player.getPackets().sendGameMessage("The guard takes your Shantay Pass as you go through the gate.");
					return;
					} else if (id >= 35542 && id <= 35544 && player.getY() == 3117 && !player.getInventory().containsItem(1854, 1)) {
					player.getPackets().sendGameMessage("You need a pass to go through here..");
					return;	
					} else if (id >= 35542 && id <= 35544 && player.getY() == 3115) {
					player.lock(2);
					player.stopAll();
					player.addWalkSteps(player.getX() >= object.getX() ? object.getX() - 1 : object.getX(), 3117 , -1, false);
					return;
					}						
				 if (id == 2718) {
					if (!player.getInventory().containsItem(1931, 1) && player.hasGrainInHopper == true) {
						player.getPackets().sendGameMessage("You need a pot to fill the flour with.");
						return;
					} else if (player.getInventory().containsItem(1931, 1) && player.hasGrainInHopper == true) {
						final Animation PLACE_ANIMATION = new Animation(832);
						//player.getPackets().sendObjectAnimation(new WorldObject(2718, 10, 0, 3166, 3305, 2), new Animation(466));
						player.getPackets().sendGameMessage("You operate the controls and fill your pot with flour.");
						player.getInventory().deleteItem(1931, 1);
						player.getInventory().addItem(1933, 1);
						player.hasGrainInHopper = false;
						player.setNextAnimation(PLACE_ANIMATION);
						return;
					} else {
						player.getPackets().sendGameMessage("You see no reason why you need to operate the controls.");
						return;
					}
				 }
					if (object.getId() == 1805) {
					if (object.getX() == 3191 && object.getY() == 3363 && player.questPoints < Settings.MAX_QUESTPOINTS || player.questPoints < 32) {
					player.getPackets().sendGameMessage("Entering Champions' Guild requires 32 quest points.");
					return;
					}
				}
					if (object.getDefinitions().getName().toLowerCase().contains("door") || object.getDefinitions().getName().equalsIgnoreCase("door") || id == 2623) {
					//if (object.getDefinitions().getName().equalsIgnoreCase("door") || id == 2623) {
					Doors.manageDoor(object);
					return;
					} else if (object.getDefinitions().getName().contains("Gate")) {
						Gates.handleGate(player, object);
					return;
					} else if (id == 8689) { //milk cow
					player.getActionManager().setAction(new CowMilkingAction());
					} else if (ClueScrolls.objectSpot(player, object)){
						return;
					}
					if (object.getDefinitions().getName().equalsIgnoreCase("Bank booth") || object.getId() == 4483 || object.getId() == 42192 || object.getId() == 2693) {
					player.getBank().initBank();
					return;
				}
					//whirlpool
					if (object.getId() == 25274) {
						player.getActionManager().setAction(
								new WhirlPool(WhirlPool.WHIRLPOOL_LOC));
						return;
					}
				 else if (id == 14315) {
					if (Lander.canEnter(player, 0))
						return;
				 } else if (id == 14314) {
					Lander.getLanders()[0].exitLander(player);
						return;
				} else if (id == 25631) {
					if (Lander.canEnter(player, 1))
						return;
				} else if (id == 25629) {
					Lander.getLanders()[1].exitLander(player);
						return;
				} else if (id == 25632) {
					if (Lander.canEnter(player, 2))
						return;
				} else if (id == 25630) {
					Lander.getLanders()[2].exitLander(player);
						return;
				}
					else if (id == 5492) {
						player.getPackets().sendGameMessage("It's locked.");
						return;
				}
					else if (id == 6) {
						player.getDwarfCannon().preRotationSetup(object);
						return;
					}

					if (object.getId() == 25216) {
						player.setNextWorldTile(new WorldTile(
								Settings.RESPAWN_PLAYER_LOCATION));
					}

					if (object.getId() == 25336 && object.getX() == 1770
							&& object.getY() == 5365) {
						player.setNextWorldTile(new WorldTile(1768, 5366, 1));
						return;
					}

					if (object.getId() == 25338 && object.getX() == 1769
							&& object.getY() == 5365) {
						player.setNextWorldTile(new WorldTile(1772, 5366, 0));
						return;
					}

					if (object.getId() == 25337 && object.getX() == 1744
							&& object.getY() == 5323) {
						player.setNextWorldTile(new WorldTile(1744, 5321, 1));
						return;
					}

					if (object.getId() == 39468 && object.getX() == 1744
							&& object.getY() == 5322) {
						player.setNextWorldTile(new WorldTile(1745, 5325, 0));
						return;
					}

					if (object.getId() == 47232) {
						player.setNextWorldTile(new WorldTile(1661, 5257, 0));
						return;
					}

					if (object.getId() == 47231) {
						player.setNextWorldTile(new WorldTile(1735, 5313, 1));
						return;
					}
					
					if (object.getId() == 28572) {
						if (player.wolfWhistle == 1) {
						player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
						player.getCutscenesManager().play(new WolfWhistle());
						return;
						} else if (player.wolfWhistle == 5) {
						player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
						World.spawnNPC(6990, new WorldTile(2921, 3444, 1), -1, false, "SOUTH");
						player.getCutscenesManager().play(new WolfWhistle1());
						} else {
							player.getPackets().sendGameMessage("You do not have permission to go upstairs.");
					}
					}
					
					if (object.getId() == 28653) {
						player.useStairs(827, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 1, 2);
						return;
					}
					
					if (object.getId() == 28714) {
						player.useStairs(828, new WorldTile(2926, 3444, player.getPlane()), 1, 2);
						return;
					}
					
					else if (object.getId() == 28716) {
						Summoning.sendPouchInterface(player);
					}
					else if (object.getId() == 4039) { //Taverly trapdoor to summoning
					if (player.summoningTrapdoorUnlocked == true) {
					WorldObject openedHole = new WorldObject(28676,
									object.getType(), object.getRotation(), object.getX(),
									object.getY(), object.getPlane());
						World.spawnTemporaryObject(openedHole, 60000, true);
						return;
					} else {
							player.getPackets().sendGameMessage("It's locked.");
							return;
					}
				}
				else if (object.getDefinitions().getName().equalsIgnoreCase("bank deposit box")) {
					player.getBank().openDepositBox();
				} else if (object.getDefinitions().getName().equalsIgnoreCase("small obelisk")) {
                 	if (!objectDef.containsOption(0, "Renew-points"))
                 		return;
                 	int summonLevel = player.getSkills().getLevelForXp(Skills.SUMMONING);
                 	if (player.getSkills().getLevel(Skills.SUMMONING) >= summonLevel) {
                         player.getPackets().sendGameMessage("You already have full Summoning points.");
                 		return;
                 	}
                     player.lock(3);
                     player.setNextAnimation(new Animation(8502));
                     player.getSkills().set(Skills.SUMMONING, summonLevel);
                     player.getPackets().sendGameMessage("You have recharged your Summoning points.", true);
                     return;
					} if (id == 1804 && object.getX() == 3115 && object.getY() == 3450) {
						if(!player.getInventory().containsItem(275, 1)) {
							player.getPackets().sendGameMessage("you need a Key to enter.");
						} else{
							WorldObject openedDoor = new WorldObject(object.getId(),
									object.getType(), object.getRotation() - 1,
									object.getX() , object.getY(), object.getPlane());
							if (World.removeTemporaryObject(object, 1200, false)) {
								World.spawnTemporaryObject(openedDoor, 1200, false);
								player.lock(2);
								player.stopAll();
								player.addWalkSteps(
										 3115, player.getY() >= object.getY() ? object.getY() - 1
													: object.getY() , -1, false);
							player.getPackets().sendGameMessage("You pass through the door.");
							}
					}
					}
					// Wilderness course start
					else if (id == 2297)
						WildernessAgility.walkAcrossLogBalance( player, object );
					else if (id == 37704)
						WildernessAgility.jumpSteppingStones( player, object );
					else if (id == 2288)
						WildernessAgility.enterWildernessPipe( player, object.getX( ), object.getY( ) );
					else if (id == 65734)
						WildernessAgility.climbUpWall( player, object );
					else if (id == 2283)
						WildernessAgility.swingOnRopeSwing( player, object );
					else if (id == 65365)
						WildernessAgility.enterWildernessCourse( player );
					else if (id == 65367)
						WildernessAgility.exitWildernessCourse( player );
					else if (id == 5906) {
						if (player.getSkills().getLevel(Skills.AGILITY) < 42) {
							player.getPackets().sendGameMessage("You need an agility level of 42 to use this obstacle.");
							return;
						}
						player.lock();
						WorldTasksManager.schedule(new WorldTask() {
							int count = 0;

							@Override
							public void run() {
								if(count == 0) {
									player.setNextAnimation(new Animation(2594));
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
								}else if (count == 2) {
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -2 : +2), object.getY(), 0);
									player.setNextWorldTile(tile);
								}else if (count == 5) {
									player.setNextAnimation(new Animation(2590));
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
								}else if (count == 7) {
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -5 : +5), object.getY(), 0);
									player.setNextWorldTile(tile);
								}else if (count == 10) {
									player.setNextAnimation(new Animation(2595));
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6), object.getY(), 0);
									player.setNextForceMovement(new ForceMovement(tile, 4, Utils.getMoveDirection(tile.getX() - player.getX(), tile.getY() - player.getY())));
								}else if (count == 12) {						 
									WorldTile tile = new WorldTile(object.getX() + (object.getRotation() == 2 ? -6 : +6), object.getY(), 0);
									player.setNextWorldTile(tile);
								}else if (count == 14) {
									stop();
									player.unlock();
								}
								count++;
							}

						}, 0, 0);
					}
					
					else if (id == 9369)
						FightPits.enterLobby(player, false);
				 else if (id == 29602 || id == 6483
						 || id == 28716//Obelisk
						 || id == 28719//Obelisk
						 || id == 28722//Obelisk
						 || id == 28725//Obelisk
						 || id == 28728//Obelisk
						 || id == 28731//Obelisk
						 || id == 28734//Obelisk
						 || id == 6484//Obelisk
						 || id == 6486//Obelisk
						 || id == 6487//Obelisk
						 || id == 6489//Obelisk
						 || id == 6490//Obelisk
						 || id == 6492//Obelisk
						 || id == 6493//Obelisk
						 || id == 14825//Obelisk
						 || id == 14826//Obelisk
		 	     		 || id == 14827//Obelisk								
		 	     		 || id == 14828//Obelisk
						 || id == 14829//Obelisk
						 || id == 14830//Obelisk
						 || id == 14831//Obelisk
				       	 || id == 16482//Obelisk
						/* && object.getY() > 3527*/)
					player.getControlerManager().startControler("ObeliskControler", object);
				 else if (id == 9356)					 
					FightCaves.enterFightCaves(player);
				 else if (id == 12163 || id == 12164 || id == 12165 || id == 12166) {
					if (player.getTemporaryAttributtes().get("canoe_shaped") != null && (boolean) player.getTemporaryAttributtes().get("canoe_shaped"))
						Canoes.openTravelInterface(player, id - 12163);
					else if (player.getTemporaryAttributtes().get("canoe_chopped") != null && (boolean) player.getTemporaryAttributtes().get("canoe_chopped"))
						Canoes.openSelectionInterface(player);
					else
						Canoes.chopCanoeTree(player, id - 12163);
				}
				 else if (id == 36972 || id == 24343 || id == 26288 || id == 26289 || id == 26286 || id == 26287) { //altar
						final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER);
					player.lock(5);
					player.getPackets().sendGameMessage(
							"You pray to the gods...", true);

					player.setNextAnimation(new Animation(645));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getSkills().restorePrayer(
									maxPrayer);
							player.getPackets()
							.sendGameMessage(
									"...and recharged your prayer.",
									true);
						}
					}, 2);
				//}
				} else if (id == 1987) 
					player.setNextWorldTile(new WorldTile(2512, 3478, 0));
				 else if (id == 2020) 
					player.setNextWorldTile(new WorldTile(2511, 3463, 0));
				 else if (id == 37247) 
					player.setNextWorldTile(new WorldTile(2575, 9875, 0));
				else if (id == 43528)
					GnomeAgility.climbUpGnomeTreeBranch2(player);
				else if (id == 43539)
					GnomeAgility.JumpDown(player, object);
				else if (id == 43581)
					GnomeAgility.RunGnomeBoard(player, object);
				 //else if (id == 10283) 
					 //GnomeAgility.swimriver(player);
				 //karamja
				 else if (id == 492)
				 player.setNextWorldTile(new WorldTile(2855, 9570, 0)); //climb down rope
				 else if (id == 1764)
				 player.setNextWorldTile(new WorldTile(2858, 3168, 0));//climb up rope
				 else if (id == 31284)
				 player.setNextWorldTile(new WorldTile(2480, 5175, 0));//enter cave to members cave
				 else if (id == 9359)
				 player.setNextWorldTile(new WorldTile(2866, 9572, 0));//back to lesser cave
				//strongHold of safety
				 else if (id == 29603)
				 player.setNextWorldTile(new WorldTile(3082, 4229, 0));
				 else if (id == 29602)
				 player.setNextWorldTile(new WorldTile(3074, 3456, 0));
				 else if (id == 29589)
				 player.setNextWorldTile(new WorldTile(3083, 3452, 0));
				 else if (id == 29592)
				 player.setNextWorldTile(new WorldTile(3086, 4247, 0));
				 else if (id == 29623)
				 player.setNextWorldTile(new WorldTile(3080, 4235, 0));
				 else if (id == 29660)
					 player.setNextWorldTile(new WorldTile(3149, 4251, 2));
					 else if (id == 29659)
					 player.setNextWorldTile(new WorldTile(3146, 4249, 1));
					 else if (id == 29656)
					 player.setNextWorldTile(new WorldTile(3149, 4244, 2));
					 else if (id == 29655)
					 player.setNextWorldTile(new WorldTile(3146, 4246, 1));
					 else if (id == 29664)
					 player.setNextWorldTile(new WorldTile(3157, 4244, 2));
					 else if (id == 29663)
					 player.setNextWorldTile(new WorldTile(3160, 4246, 1));
					 else if (id == 29668)
					 player.setNextWorldTile(new WorldTile(3157, 4251, 2));
					 else if (id == 29667)
					 player.setNextWorldTile(new WorldTile(3160, 4249, 1));
					 else if (id == 29672)
					 player.setNextWorldTile(new WorldTile(3171, 4271, 3));
					 else if (id == 29671)
					 player.setNextWorldTile(new WorldTile(3174, 4273, 2));
					 else if (id == 29728)
					 player.setNextWorldTile(new WorldTile(3159, 4279, 3));
					 else if (id == 29729)
                     player.setNextWorldTile(new WorldTile(3077, 3462, 0));

					//start of varrock dungeon
					else if (id == 29355 && object.getX() == 3230 && object.getY() == 9904) //varrock dungeon climb to bear
						player.useStairs(828, new WorldTile(3229, 3503, 0), 1, 2);
					else if (id == 24264)
						player.useStairs(833, new WorldTile(3229, 9904, 0), 1, 2);
					else if (id == 24366)
						player.useStairs(828, new WorldTile(3237, 3459, 0), 1, 2);
					else if (id == 882 && object.getX() == 3237 && object.getY() == 3458) 
						player.useStairs(833, new WorldTile(3237, 9858, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3097 && object.getY() == 9867) //edge dungeon climb
						player.useStairs(828, new WorldTile(3096, 3468, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3088 && object.getY() == 9971)
						player.useStairs(828, new WorldTile(3087, 3571, 0), 1, 2);
					else if (id == 65453)
						player.useStairs(833, new WorldTile(3089, 9971, 0), 1, 2);
					else if (id == 12389 && object.getX() == 3116 && object.getY() == 3452)
						player.useStairs(833, new WorldTile(3117, 9852, 0), 1, 2);
					else if (id == 29355 && object.getX() == 3116 && object.getY() == 9852)
						player.useStairs(833, new WorldTile(3115, 3452, 0), 1, 2);
				 else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (object.getDefinitions().getName().equalsIgnoreCase("Obelisk") && object.getY() > 3525) {
					//Who the fuck removed the controler class and the code from SONIC!!!!!!!!!!
					//That was an hour of collecting coords :fp: Now ima kill myself.
				}else if (Wilderness.isDitch(id)) {// wild ditch
					//	  player.getDialogueManager().startDialogue(
							//	  "WildernessDitch", object);
							player.getTemporaryAttributtes().put("wildernessditch", object);
							player.getInterfaceManager().sendInterface(382);
				/*}else if (id == 23271) {// wild ditch
					player.getTemporaryAttributtes().put("wildernessditch", object);
					player.getInterfaceManager().sendInterface(382);*/
				} else if (id == 28779) {
                    player.getDialogueManager().startDialogue("BorkEnter");
				}else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage(
							"You enter the portal...");
					player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3,
							"..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);
				} else if (id == 12202) {// mole entrance
					if(!player.getInventory().containsItem(952, 1)) {
						player.getPackets().sendGameMessage("You need a spade to dig this.");
						return;
					}
					if(player.getX() != object.getX() || player.getY() != object.getY()) {
						player.lock();
						player.addWalkSteps(object.getX(), object.getY());
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								InventoryOptionsHandler.dig(player);
							}

						}, 1);
					}else
						InventoryOptionsHandler.dig(player);
				} else if (id == 12230 && object.getX() == 1752 && object.getY() == 5136) {// mole exit 
					player.setNextWorldTile(new WorldTile(2986, 3316, 0));
				}
				else if (id == 38811 || id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382)
						player.getInterfaceManager().sendInterface(650);
					
					else if (object.getX() == 2918 && object.getY() == 4382) 
						player.stopAll();
						player.setNextWorldTile(new WorldTile(
								player.getX() == 2921 ? 2917 : 2921, player
										.getY(), player.getPlane()));
		                   /*
	                     * Clan Wars Free For all (safe) portals
	                     * 
	                     * Game Made by Kiyomi
	                     */
	                /*} else if (id == 38698) {
	                	if (player.getSkills().getCombatLevel() < 30) {
	                		player.sendMessage("You need a combat level of at least 30 to enter this portal.");
	                		}else {
	                		player.setNextWorldTile(new WorldTile(2815, 5511, 0));
	                		player.sendMessage("You can fight other players here, but your items are SAFE here.");
	                		player.getControlerManager().startControler("FFA");
	                		}*/
	                } else if (id == 38698) {
	        			player.setNextWorldTile(new WorldTile(2815, 5511, 0));
	        			player.getControlerManager().startControler("clan_wars_ffa", false);
	                } else if (id == 38699) {
	        			player.setNextWorldTile(new WorldTile(3007, 5511, 0));
	        			player.getControlerManager().startControler("clan_wars_ffa", true);
	             /*   } else if (id == 38700) {
	                	if (player.getSkills().getCombatLevel() < 30) {
	                		player.sendMessage("You need a combat level of at least 30 to leave the game.");
	                		}else {
	                		player.setNextWorldTile(new WorldTile(3270, 3687, 0));
	                		player.sendMessage("You have left the Clan Wars Free-For-All (Safe).");
	                		player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 5 : 1, 789);
	                		}*/
	                } else if (id == 42219) {
	    					player.getControlerManager().startControler("Soulwars");
				} else if(id == 3832) {
					if(object.getX() == 3508 && object.getY() == 9494) {
						player.useStairs(828, new WorldTile(3509, 9496, 2), 1, 2);
					}
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				}/* else if (id == 54019 || id == 54020 || id == 55301)
					PkRank.showRanks(player);*/
				else if (id == 1817 && object.getX() == 2273
						&& object.getY() == 4680) { // kbd lever
					Magic.pushLeverTeleport(player, new WorldTile(3067, 10254,
							0));
				} else if (id == 14315) {
					player.getControlerManager().startControler("PestControlLobby", 1);
				} else if (id == 1816 && object.getX() == 3067
						&& object.getY() == 10252) { // kbd out lever
					Magic.pushLeverTeleport(player,
							new WorldTile(2273, 4681, 0));
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				} else if (id == 32015 && object.getX() == 3069
						&& object.getY() == 10256) { // kbd stairs
					player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 1765 && object.getX() == 3017
						&& object.getY() == 3849) { // kbd out stairs
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3069, 10255, 0));
					player.getControlerManager().forceStop();
				} else if (id == 14315) {
					player.getControlerManager().startControler("PestControlLobby", 1);
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3089, 3957, 0));
				} else if (id == 1814) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3155, 3923, 0));
				} else if (id == 1815) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2561, 3311, 0));
				}
				else if (id == 36970)
					player.getInterfaceManager().sendInterface(459);
                    //player.getDialogueManager().startDialogue("SpinningD");
					
					//
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9740) 
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9738) 
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3018 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3020 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
					else if (id == 2295)
						GnomeAgility.walkGnomeLog(player);
					else if (id == 2285)
						GnomeAgility.climbGnomeObstacleNet(player);
					else if (id == 35970)
						GnomeAgility.climbUpGnomeTreeBranch(player);
					else if (id == 2314 || id == 2315)
						GnomeAgility.climbDownGnomeTreeBranch(player);
					else if (id == 2312)
						GnomeAgility.walkGnomeRope(player);
					else if (id == 4059)
						GnomeAgility.walkBackGnomeRope(player);
					else if (id == 2286)
						GnomeAgility.climbGnomeObstacleNet2(player);
					else if (id == 43543 || id == 43544)
						GnomeAgility.enterGnomePipe(player, object.getX(), object.getY());
					//BarbarianOutpostAgility start
				else if (id == 20210) 
					BarbarianOutpostAgility.enterObstaclePipe(player, object);
				else if (id == 43526)
					BarbarianOutpostAgility.swingOnRopeSwing(player, object);
				else if (id == 43595 && x == 2550 && y == 3546)
					BarbarianOutpostAgility.walkAcrossLogBalance(player, object);
				else if (id == 20211 && x == 2538 && y == 3545)
					BarbarianOutpostAgility.climbObstacleNet(player, object);
				else if (id == 2302 && x == 2535 && y == 3547)
					BarbarianOutpostAgility.walkAcrossBalancingLedge(player, object);
				else if (id == 1948)
					BarbarianOutpostAgility.climbOverCrumblingWall(player, object);
				else if (id == 43533)
					BarbarianOutpostAgility.runUpWall(player, object);
				else if (id == 43597) 
					BarbarianOutpostAgility.climbUpWall(player, object);
				else if (id == 43587)
					BarbarianOutpostAgility.fireSpringDevice(player, object);
				else if (id == 43527)
					BarbarianOutpostAgility.crossBalanceBeam(player, object);
				else if (id == 43531)
					BarbarianOutpostAgility.jumpOverGap(player, object);
				else if (id == 43532)
					BarbarianOutpostAgility.slideDownRoof(player, object);
				else if (id == 2491)
					player.getActionManager()
					.setAction(
							new EssenceMining(
									object,
									player.getSkills().getLevel(
											Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
													: EssenceDefinitions.Pure_Essence));
					//start falador mininig
				else if (id == 30942 && object.getX() == 3019 && object.getY() == 3450) 
					player.useStairs(828, new WorldTile(3020, 9850, 0), 1, 2);
				else if (id == 6226 && object.getX() == 3019 && object.getY() == 9850) 
					player.useStairs(833, new WorldTile(3018, 3450, 0), 1, 2);
				else if (id == 31002 ) 
					player.useStairs(833, new WorldTile(2998, 3452, 0), 1, 2);
				else if (id == 31012) 
					player.useStairs(828, new WorldTile(2996, 9845, 0), 1, 2);
				else if (id == 30943 && object.getX() == 3059 && object.getY() == 9776) 
					player.useStairs(-1, new WorldTile(3061, 3376, 0), 0, 1);
				else if (id == 30944 && object.getX() == 3059 && object.getY() == 3376) 
					player.useStairs(-1, new WorldTile(3058, 9776, 0), 0, 1);
				else if (id == 2112 && object.getX() == 3046 && object.getY() == 9756) {
					if(player.getSkills().getLevelForXp(Skills.MINING) < 60) {
					//	player.getDialogueManager().startDialogue("SimpleNPCMessage", MiningGuildDwarf.getClosestDwarfID(player),"Sorry, but you need level 60 Mining to go in there.");
						return;
					}
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY() + 1, object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								3046, player.getY() > object.getY() ? object.getY()
										: object.getY() + 1 , -1, false);
					}
				}else if (id == 6226  && object.getX() == 3019 && object.getY() == 9740) 
					player.useStairs(828, new WorldTile(3019, 3341, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3019 && object.getY() == 9738) 
					player.useStairs(828, new WorldTile(3019, 3337, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3018 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3017, 3339, 0), 1, 2);
				else if (id == 6226  && object.getX() == 3020 && object.getY() == 9739) 
					player.useStairs(828, new WorldTile(3021, 3339, 0), 1, 2);
				else if (id == 26425 && player.getX() == 2863 || player.getX() == 2863 && player.getY() == 5354) {
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								 player.getX() >= object.getX() ? object.getX() + 1
											: object.getX(), 5354 , -1, false);
						player.getPackets().sendGameMessage("You pass through the gate.");
						}
				}
				else if (id == 26425 && player.getX() == 2864 && player.getY() == 5354) {
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								 player.getX() >= object.getX() ? object.getX() - 2
											: object.getX(), 5354 , -1, false);
						player.getPackets().sendGameMessage("You pass through the gate.");
						}
				}
				else if (id == 35549 && object.getX() == 3268 && object.getY() == 3227) {
					if(player.LumbyGate == 0) {
						player.getDialogueManager().startDialogue("LumbyGaurds", 925);
					}else if(player.LumbyGate == 2){
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() + 1,
							object.getX() , object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								 player.getX() >= object.getX() ? object.getX() - 1
											: object.getX(), 3227 , -1, false);
						player.getPackets().sendGameMessage("You pass through the gate.");
						}
					}else if(player.LumbyGate == 1){
					WorldObject openedDoor = new WorldObject(object.getId(),
							object.getType(), object.getRotation() - 1,
							object.getX() , object.getY(), object.getPlane());
					if (World.removeTemporaryObject(object, 1200, false)) {
						World.spawnTemporaryObject(openedDoor, 1200, false);
						player.lock(2);
						player.stopAll();
						player.addWalkSteps(
								 player.getX() >= object.getX() ? object.getX() - 1
											: object.getX(), 3227 , -1, false);
						player.LumbyGate = 0;
						player.getPackets().sendGameMessage("You pass through the gate.");
						}
				}
				}
					//champion guild
					else if (id == 24357 && object.getX() == 3188 && object.getY() == 3355) 
						player.useStairs(-1, new WorldTile(3189, 3354, 1), 0, 1);
					else if (id == 24359 && object.getX() == 3188 && object.getY() == 3355) 
						player.useStairs(-1, new WorldTile(3189, 3358, 0), 0, 1);
					else if (id == 1805 && object.getX() == 3191 && object.getY() == 3363) {
						WorldObject openedDoor = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX() , object.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false)) {
							World.spawnTemporaryObject(openedDoor, 1200, false);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									3191, player.getY() >= object.getY() ? object.getY() - 1
											: object.getY() , -1, false);
							if(player.getY() >= object.getY())
								player.getDialogueManager().startDialogue("SimpleNPCMessage", 198, "Greetings bolt adventurer. Welcome to the guild of", "Champions.");
						}
					}
					else if (id == 2350 && (object.getX() == 3352 && object.getY() == 3417 && object.getPlane() == 0))
						player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
					else if (id == 2353 && (object.getX() == 3177 && object.getY() == 5730 && object.getPlane() == 0))
						player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
					else if (id == 47120) { //zaros altar
						
						//recharge if needed
						if(player.getSkills().getLevel(Skills.PRAYER) < player.getSkills().getLevelForXp(Skills.PRAYER)) {
							player.addStopDelay(12);
							player.setNextAnimation(new Animation(12563));
							player.getSkills().set(Skills.PRAYER, (int) (player.getSkills().getLevelForXp(Skills.PRAYER)*1.15));
						}
						player.getDialogueManager().startDialogue("ZarosAltar");
					} else if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
						player.getDialogueManager().startDialogue("ClimbNoEmoteStairs", new WorldTile(3205, 3228, 2), new WorldTile(3205, 3228, 0));
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
						player.getDialogueManager().startDialogue("ClimbNoEmoteStairs", new WorldTile(3205, 3209, 2), new WorldTile(3205, 3209, 0));
					else if (id == 36786)
						player.getDialogueManager().startDialogue("Banker", 4907); 
					else if (id == 42425 && object.getX() == 3220 && object.getY() == 3222) { //zaros portal
						player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5, "And you find yourself into a digsite.");
						player.addWalkSteps(3222, 3223, -1, false);
						player.getPackets().sendGameMessage("You examine portal and it aborves you...");
					}else if (id == 46500 && object.getX() == 3351 && object.getY() == 3415) { //zaros portal
						player.useStairs(-1, new WorldTile(Settings.RESPAWN_PLAYER_LOCATION.getX(), Settings.RESPAWN_PLAYER_LOCATION.getY(), Settings.RESPAWN_PLAYER_LOCATION.getPlane()), 2, 3, "You found your way back to home.");
						player.addWalkSteps(3351, 3415, -1, false);
				/*	}else if (id == 23271) {//wild ditch
						player.getTemporaryAttributtes().put("wildernessditch", object);
						player.getInterfaceManager().sendInterface(382);*/
					}
					else if (id == 67053)
						player.useStairs(-1, new WorldTile(3120, 3519, 0), 0, 1);
					else {
						switch (object.getDefinitions().getName()) {
							//object.getDefinitions().getName().equalsIgnoreCase("Gate")
						case "closed chest":
							if (objectDef.containsOption(0, "Open")) {
								player.setNextAnimation(new Animation(536));
								player.lock(2);
								WorldObject openedChest = new WorldObject(object.getId()+1,
										object.getType(), object.getRotation(), object.getX(),
										object.getY(), object.getPlane());
								//if (World.removeTemporaryObject(object, 60000, true)) {
								player.faceObject(openedChest);
								World.spawnTemporaryObject(openedChest, 60000, true);
								//}
							}
							break;
						case "open chest":
							if (objectDef.containsOption(0, "Search")) 
								player.getPackets().sendGameMessage("You search the chest but find nothing.");
							break;
						case "spiderweb":
							if(object.getRotation() == 2) {
								player.lock(2);
								if (Utils.getRandom(1) == 0) {
									player.addWalkSteps(player.getX(), player.getY() < y ? object.getY()+2 : object.getY() - 1, -1, false);
									player.getPackets().sendGameMessage("You squeeze though the web.");
								} else
									player.getPackets().sendGameMessage(
											"You fail to squeeze though the web; perhaps you should try again.");
							}
							break;
						case "web":
							if (objectDef.containsOption(0, "Slash")) {
								player.setNextAnimation(new Animation(PlayerCombat
										.getWeaponAttackEmote(player.getEquipment()
												.getWeaponId(), player
												.getCombatDefinitions()
												.getAttackStyle())));
								slashWeb(player, object);
							}
							break;
						case "bank deposit box":
							if (objectDef.containsOption(0, "Deposit"))
								//player.getBank().initDepositBox();
							break;
						case "bank":
						case "bank chest":
						case "bank booth":
						case "counter":
							if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use"))
								player.getBank().initBank();
							break;
						case "ladder":
							handleLadder(player, object, 1);
							break;
						case "staircase":
							handleStaircases(player, object, 1);
							break;
						case "altar":
							if (objectDef.containsOption(0, "Pray") || objectDef.containsOption(0, "Pray-at")) {
								final int maxPrayer = player.getSkills()
										.getLevelForXp(Skills.PRAYER);
								if (player.getPrayer().getPrayerpoints() < maxPrayer) {
									player.lock(5);
									player.getPackets().sendGameMessage(
											"You pray to the gods...", true);
									player.setNextAnimation(new Animation(645));
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											player.getSkills().restorePrayer(
													maxPrayer);
											player.getPackets()
											.sendGameMessage(
													"...and recharged your prayer.",
													true);
										}
									}, 2);
								} else 
									player.getPackets().sendGameMessage(
											"You already have full prayer.");
								if (id == 6552)
									player.getDialogueManager().startDialogue(
											"AncientAltar");
							}
							break;
						default:
							player.getPackets().sendGameMessage(
									"Nothing interesting happens.");
							break;
						}
					}
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					player.getPackets().sendGameMessage("object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
					System.out.println("cliked 1 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
				
			},objectDef.getSizeX(), Wilderness.isDitch(id) ? 4 : objectDef
					.getSizeY(), object.getRotation())); 
			//objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
	
	public static void handleOption2(Player player, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			@SuppressWarnings("unused")
			int junk = stream.readUnsigned128Byte();
			int y = stream.readUnsignedShort128();
			final int id = stream.readUnsignedShort();
			//final int itemId = stream.readShort128() & 0xFFFF;
			int x = stream.readUnsignedShort();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));
					if(!player.getControlerManager().processObjectClick2(object))
						return;
					if (Pickables.handlePickable(player, object))
                    return;
					if (Smelting.handleObjects(player, object))
						return;
					else if (Mining.examineRocks(object, player))
					return;
					else if (player.getFarmingManager().isFarming(id, null, 2))
					return;
					else if (id == 14011 || id == 7053 || id == 34383 || id == 34384 || id == 34382 || id == 34385
					 || id == 34387 || id == 34386 || id == 635)
					Thieving.handleStalls(player, object);
					if (id == 6) {
					player.getDwarfCannon().pickUpDwarfCannon(0, object);
					return;
				}
				else if (id == 1317 || id == 1294 || id == 1293) //spirit tree
						SpiritTree.sendSpiritTreeInterface(player);
				 else if (id == 25014) { // PuroPuro
					player.getControlerManager().forceStop();
					Magic.sendTeleportSpell(player, 6601, -1, 1118, -1, 0, 0, new WorldTile(2427, 4446, 0), 9, false, Magic.OBJECT_TELEPORT);	
				}
					else if (id == 36796) {//climb up lumby windmill
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 2, 1, "You climb up the ladder.");
					return;
					}
					else if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
					player.useStairs(-1, new WorldTile(3205, 3228, 2), 0, 1);
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
					player.useStairs(-1, new WorldTile(player.getX(), player.getY(), 2), 0, 1);
					else if (id == 35551 && object.getX() == 3268 && object.getY() == 3228) {
						if(!player.getInventory().containsItem(995, 10)) {
							player.getPackets().sendGameMessage("you need to pay 10gp.");
						}else{
						WorldObject openedDoor = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX() , object.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false)) {
							World.spawnTemporaryObject(openedDoor, 1200, false);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									 player.getX() >= object.getX() ? object.getX() - 1
												: object.getX(), 3228 , -1, false);
							/*player.addWalkSteps(
									3268, player.getY() >= object.getY() ? object.getY() - 1
											: object.getY() , -1, false);*/
							player.getInventory().deleteItem(995, 10);
							}
					}
					}
					else if (id == 35549 && object.getX() == 3268 && object.getY() == 3227) {
						if(!player.getInventory().containsItem(995, 10)) {
							player.getPackets().sendGameMessage("you need to pay 10gp.");
						}else{
						WorldObject openedDoor = new WorldObject(object.getId(),
								object.getType(), object.getRotation() - 1,
								object.getX() , object.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false)) {
							World.spawnTemporaryObject(openedDoor, 1200, false);
							player.lock(2);
							player.stopAll();
							player.addWalkSteps(
									 player.getX() >= object.getX() ? object.getX() - 1
												: object.getX(), 3227 , -1, false);
							/*player.addWalkSteps(
									3268, player.getY() >= object.getY() ? object.getY() - 1
											: object.getY() , -1, false);*/
							player.getInventory().deleteItem(995, 10);
							}
					}
					}
					else if (id == 36786)
						player.getBank().initBank();
					else if (Clipedobject.BankBooth(id))
						player.getBank().initBank();
					else if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
						player.useStairs(-1, new WorldTile(3205, 3228, 2), 0, 1);
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
						player.useStairs(-1, new WorldTile(3205, 3209, 2), 0, 1);
					else
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					System.out.println("cliked 2 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
	
	public static void handleOption3(Player player, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			int x = stream.readUnsignedShort();
			final int id = stream.readUnsignedShortLE128();
			int y = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			int junk = stream.readUnsignedByte();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));
					if(!player.getControlerManager().processObjectClick3(object))
						return;
					else if (player.getFarmingManager().isFarming(id, null, 3))
					return;
					player.setNextFaceWorldTile(tile);
					if (object.getDefinitions().getName().equalsIgnoreCase("Bank booth")) {
					player.getGEManager().openCollectionBox();
					return;
				}
					 if (id == 36777 && (object.getX() == 3204 && object.getY() == 3229 && object.getPlane() == 1))
							player.useStairs(-1, new WorldTile(3205, 3228, 0), 0, 1);
					else if (id == 36796) {//climb down lumby windmill
					player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), 2, 1, "You climb down the ladder.");
					return;
					}
					else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
							player.useStairs(-1, new WorldTile(player.getX(), player.getY(), 0), 0, 1);
						else if (id == 36774 && (object.getX() == 3204 && object.getY() == 3207 && object.getPlane() == 1))
							player.useStairs(-1, new WorldTile(3205, 3209, 0), 0, 1);
					else
						player.getPackets().sendGameMessage("Nothing interesting happens.");
					System.out.println("cliked 3 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
	
	public static void handleOption4(Player player, InputStream stream) {
		if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime =  System.currentTimeMillis();
			if(player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			final int id = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			int junk = stream.readByte();
			final int x = stream.readUnsignedShortLE128();
			final int y = stream.readUnsignedShortLE128();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if(!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
			if(mapObject == null)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			player.stopAll(false);
			final ObjectDefinitions objectDef = object.getDefinitions();
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation())
							, object.getCoordFaceY(objectDef.getSizeX(),objectDef.getSizeY(), object.getRotation())
							, object.getPlane()));
					if(!player.getControlerManager().processObjectClick4(object))
						return;
					else if (Lumbridge.handleObjectClick4(object, player))
					return;
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					switch (objectDef.name.toLowerCase()) {
					case "magical wheat":
						PuroPuro.pushThrough(player, object);
						break;
					default:
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						break;
					}
					System.out.println("cliked 4 at object id : "+id+", "+object.getX()+", "+object.getY()+", "+object.getPlane());
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}
	
	public static void handleOption5(Player player, InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
			return;
		long currentTime = System.currentTimeMillis();
		if (player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
			return;
		int x = stream.readUnsignedShort128();
		int y = stream.readUnsignedShortLE128();
		int id = stream.readUnsignedShort();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id) {
				return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
		player.stopAll(false);
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.faceObject(object);
				player.sendMessage("Clicked object ID: " + id + ", X: " + x + ", Y: " + y + ", Rotation: "
						+ object.getRotation() + ", Type: " + object.getType() + ", Option: 5");
				if (!player.getControlerManager().processObjectClick5(object))
					return;
				else if (player.getFarmingManager().isFarming(id, null, 3))
					return;
				else if (AlKharid.handleObjectClick5(object, player))
					return;
				switch (objectDef.name.toLowerCase()) {
				case "magical wheat":
					PuroPuro.pushThrough(player, object);
					break;
				default:
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					break;
				}
				
			}
		}, objectDef.getSizeX(), Wilderness.isDitch(object.getId()) ? 4 : objectDef.getSizeY(), object.getRotation()));
	}
	
	public static void handleOptionExamine(final Player player, final WorldObject object) {
		/*if(player.getUsername().equalsIgnoreCase("jens")) {
			int offsetX = object.getX() - player.getX();
			int offsetY = object.getY() - player.getY();
			System.out.println("Offsets"+offsetX+ " , "+offsetY);
		}*/
		player.getPackets().sendGameMessage("It's an " + object.getDefinitions().getName() + ".");
				
				Logger.log(
						"ObjectHandler",
						"examined object id : " + object.getId() + ", "
								+ object.getX() + ", " + object.getY()
								+ ", " + object.getPlane() + ", "
								+ object.getType() + ", "
								+ object.getRotation() + ", "
								+ object.getDefinitions().getName());
	}
	
	private static boolean handleStaircases(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbNoEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
							new WorldTile(player.getX(), player.getY(), player
									.getPlane() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}
	
	private static boolean handleLadder(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
							new WorldTile(player.getX(), player.getY(), player
									.getPlane() - 1), "Climb up the ladder.",
									"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}
	
	private static void slashWeb(Player player, WorldObject object) {

		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1,
					object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getPlane()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage(
					"You fail to cut through the web.");
	}
}