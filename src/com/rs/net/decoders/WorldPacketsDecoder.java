package com.rs.net.decoders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;
import java.util.TimerTask;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.skills.herblore.Herblore;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.skills.cooking.Cooking;
import com.rs.game.player.content.Lend;
import com.rs.game.player.skills.crafting.LeatherCrafting;
import com.rs.game.player.skills.crafting.LeatherCrafting.LeatherData;
import com.rs.game.player.LendingManager;
import com.rs.cores.CoresManager;
import com.rs.game.player.skills.construction.HouseControler;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.content.CarrierTravel;
import com.rs.game.player.content.CarrierTravel.Carrier;
import com.rs.game.player.skills.crafting.GlassBlowing;
import com.rs.game.player.skills.crafting.GlassBlowing.GlassData;
import com.rs.game.player.skills.hunter.BoxTrapping;
import com.rs.game.player.skills.hunter.BirdSnaring;
import com.rs.game.player.skills.hunter.ButterflyNetting;
import com.rs.game.player.skills.hunter.ButterflyNetting.Entities;
import com.rs.game.player.skills.hunter.WhiteRabbit;
import com.rs.game.Graphics;
import com.rs.utils.ItemExamines;
import com.rs.utils.GlobalItems;
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
import com.rs.game.player.skills.smithing.Smelting.Bars;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.FightPits;
import com.rs.game.player.skills.thieving.Thieving;
import com.rs.game.minigames.creations.StealingCreation;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.pest.Lander;
import com.rs.game.npc.NPC;
import com.rs.game.ForceTalk;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.others.LivingRock;
import com.rs.game.player.skills.fletching.Fletching;
import com.rs.game.player.skills.fletching.Fletching.Fletch;
import com.rs.game.player.ClueScrolls;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.EmotesManager;
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
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.FightPitsViewingOrb;
import com.rs.game.player.skills.herblore.HerbCleaning;
import com.rs.game.player.actions.Listen;
import com.rs.game.player.skills.summoning.Summoning.Pouches;
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
import com.rs.game.player.skills.thieving.PyramidPlunderControler;
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
import com.rs.game.player.skills.smithing.Smithing.ForgingInterface;
import com.rs.io.InputStream;
import com.rs.io.OutputStream;
import com.rs.net.Session;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.decoders.handlers.NPCHandler;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.game.player.actions.Rest;
import com.rs.game.player.skills.construction.House;
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
import com.rs.game.player.content.books.StrongholdBook;
import com.rs.game.player.content.quests.WolfWhistle;
import com.rs.game.player.skills.crafting.BattlestaffCrafting;
import com.rs.game.player.skills.crafting.BattlestaffCrafting.Battlestaff;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[256];
	
	private final static int IDK = 176;
	private final static int ITEM_ON_PLAYER = 138;
	private final static int JOIN_CLAN_CHAT_PACKET = 36;
	private final static int INTERFACE_BUTTON_ON_ITEM_PACKET = 78;
	private final static int NPC_EXAMINE_PACKET = 49;
	private final static int OBJECT_EXAMINE_PACKET = 142;
	private final static int WALKING_PACKET = 119;
	private final static int WORLD_MAP_CLICKING_PACKET = 148;
	private final static int MINI_WALKING_PACKET = 163;
	private final static int WEIRD_WALKING_PACKET = 160;
	private final static int AFK_PACKET = 244;
	public final static int ACTION_BUTTON1_PACKET = 216;
	public final static int ACTION_BUTTON2_PACKET = 205;
	private final static int WEAR_ITEM_PACKET = 229;
	private final static int ENTER_LONGSTRING_PACKET = 197;
	public final static int ACTION_BUTTON3_PACKET = 3;
	public final static int ACTION_BUTTON4_PACKET = 19;
	public final static int ACTION_BUTTON5_PACKET = 193;
	public final static int ACTION_BUTTON6_PACKET = 76;
	public final static int ACTION_BUTTON7_PACKET = 173;
	public final static int ACTION_BUTTON8_PACKET = 89;
	public final static int ACTION_BUTTON9_PACKET = 221;
	private final static int SPELL_ON_OBJECT = 159;
	private final static int PLAYER_TRADE_OPTION_PACKET = 141;
	private final static int PLAYER_OPTION4_PACKET = 114;
	private final static int MOVE_CAMERA_PACKET = 235;
	private final static int CLICK_PACKET = 87;
	private final static int CLAN_UNKNOWN_PACKET = 103;
	private final static int CLAN_UNKNOWN_PACKET2 = 115;
	private final static int CLAN_UNKNOWN_PACKET3 = 82;
	private final static int CLOSE_INTERFACE_PACKET = 91;
	private final static int COMMANDS_PACKET = 171;
	private final static int IN_OUT_SCREEN_PACKET = 4;
	private final static int INTER_PACKET_COUNT_CHECKER_PACKET = 5;
	private final static int SWITCH_DETAIL = 155;
	private final static int DONE_LOADING_REGION = 116;
	private final static int PING_PACKET = 255;
	private final static int SCREEN_PACKET = 170;
	private final static int INTERFACE_ON_GROUND_ITEM_PACKET = 54;
	private final static int PUBLIC_CHAT_PACKET = 182;
	private final static int QUICK_CHAT_PACKET = 68;
	private final static int ADD_FRIEND_PACKET = 226;
	private final static int REMOVE_FRIEND_PACKET = 92;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 123;
	private final static int OBJECT_CLICK1_PACKET = 45;
	private final static int OBJECT_CLICK2_PACKET = 190;//190
	private final static int OBJECT_CLICK3_PACKET = 26;//26
	private final static int OBJECT_CLICK4_PACKET = 143;
	private final static int OBJECT_CLICK5_PACKET = 181;
	private final static int NPC_CLICK1_PACKET = 217;
	private final static int NPC_CLICK2_PACKET = 254;
	private final static int NPC_CLICK3_PACKET = 38;
	private final static int NPC_CLICK4_PACKET = 95;
	private final static int ATTACK_NPC = 207;//207
	private final static int ATTACK_PLAYER = 152;
	private final static int PLAYER_OPTION_2_PACKET = 140;
	private final static int ITEM_DROP_PACKET = 248;
	private final static int ITEM_ON_ITEM_PACKET = 117;
	private final static int ITEM_SELECT_PACKET = 66;
	private final static int ITEM_ON_OBJECT_PACKET = 202;
	private final static int ITEM_OPERATE_PACKET = 29;//189
	private final static int ITEM_OPERATE2_PACKET = 69;
	private final static int ITEM_OPERATE1_PACKET = 189;//189
	private final static int ITEM_ON_NPC_PACKET = 136;//189
	private final static int ITEM_TAKE_PACKET = 194;
	private final static int DIALOGUE_CONTINUE_PACKET = 147;
	private final static int ENTER_INTEGER_PACKET = 206;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 253;
	private final static int SWITCH_BANK_ITEMS = 112;
	private final static int ITEM_OPTION1_PACKET = 234;
	private final static int INTERFACE_ON_PLAYER = 79;
	private final static int INTERFACE_ON_NPC = 84;
	private final static int ENTER_NAME_PACKET = 172;
	private final static int GRAND_EXCHANGE_ITEM_SELECT_PACKET = 139;
	private final static int ITEM_EXAMINE_PACKET = 124;
	private final static int REPORT_ABUSE = 118;
	private final static int UNKNOWN2_PACKET = 41;
	private final static int MUSIC_VOLUME_PACKET = 183;
	private final static int GROUND_ITEM_CLICK_2 = 151;
	private final static int CLAN_INFO_PACKET = 105;
	private final static int CLAN_CHAT_SETUP = 47;
	private final static int LEAVE_CLAN_PACKET = 77;
	static {
		loadPacketSizes();
	}
	
	public static void loadPacketSizes() {
		for(int id = 0; id < 256; id++)
			PACKET_SIZES[id] = -4;
		PACKET_SIZES[IDK] = 4;
		PACKET_SIZES[GRAND_EXCHANGE_ITEM_SELECT_PACKET] = 2;
		PACKET_SIZES[ITEM_ON_PLAYER] = 11;
		PACKET_SIZES[ENTER_NAME_PACKET] = 6;
		PACKET_SIZES[AFK_PACKET] = 0;
		PACKET_SIZES[OBJECT_EXAMINE_PACKET] = 2;
		PACKET_SIZES[ACTION_BUTTON1_PACKET] = 6;
		PACKET_SIZES[WORLD_MAP_CLICKING_PACKET] = 4;
		PACKET_SIZES[WEIRD_WALKING_PACKET] = 4;
		PACKET_SIZES[ACTION_BUTTON2_PACKET] = 4;
		PACKET_SIZES[WEAR_ITEM_PACKET] = 8;
		PACKET_SIZES[ACTION_BUTTON3_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON4_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON5_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON6_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON7_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON8_PACKET] = 6;
		PACKET_SIZES[ACTION_BUTTON9_PACKET] = 6;
		PACKET_SIZES[INTERFACE_BUTTON_ON_ITEM_PACKET] = 14;
		PACKET_SIZES[CLICK_PACKET] = 6;
		PACKET_SIZES[CLOSE_INTERFACE_PACKET] = 0;
		PACKET_SIZES[COMMANDS_PACKET] = -1;
		PACKET_SIZES[ENTER_LONGSTRING_PACKET] = 6;
		PACKET_SIZES[IN_OUT_SCREEN_PACKET] = 1;
		PACKET_SIZES[INTER_PACKET_COUNT_CHECKER_PACKET] = 2;
		PACKET_SIZES[PING_PACKET] = 0;
		PACKET_SIZES[MINI_WALKING_PACKET] = -1;
		PACKET_SIZES[WALKING_PACKET] = -1;
		PACKET_SIZES[SCREEN_PACKET] = 6;
		PACKET_SIZES[SWITCH_DETAIL] = 4;
		PACKET_SIZES[DONE_LOADING_REGION] = 0;
		PACKET_SIZES[PUBLIC_CHAT_PACKET] = -1;
		PACKET_SIZES[CLAN_UNKNOWN_PACKET] = 10;
		PACKET_SIZES[CLAN_UNKNOWN_PACKET2] = 2;
		PACKET_SIZES[CLAN_UNKNOWN_PACKET3] = 7;
		PACKET_SIZES[QUICK_CHAT_PACKET] = -1;
		PACKET_SIZES[ADD_FRIEND_PACKET] = -1;
		PACKET_SIZES[REMOVE_FRIEND_PACKET] = -1;
		PACKET_SIZES[CLAN_INFO_PACKET] = 2;
		PACKET_SIZES[INTERFACE_ON_GROUND_ITEM_PACKET] = 20;
		PACKET_SIZES[SEND_FRIEND_MESSAGE_PACKET] = -1;
		PACKET_SIZES[SPELL_ON_OBJECT] = 13;
		PACKET_SIZES[OBJECT_CLICK1_PACKET] = 7;
		PACKET_SIZES[OBJECT_CLICK2_PACKET] = 7;
		PACKET_SIZES[OBJECT_CLICK3_PACKET] = 7;//6
		PACKET_SIZES[OBJECT_CLICK4_PACKET] = 7;
		PACKET_SIZES[OBJECT_CLICK5_PACKET] = 7;
		PACKET_SIZES[ITEM_DROP_PACKET] = 8;
		PACKET_SIZES[ITEM_ON_ITEM_PACKET] = 16;
		PACKET_SIZES[ITEM_SELECT_PACKET] = 8;
		PACKET_SIZES[ITEM_OPERATE_PACKET] = 8;
		PACKET_SIZES[ITEM_OPERATE1_PACKET] = 8;
		PACKET_SIZES[ITEM_OPERATE2_PACKET] = 8;
		PACKET_SIZES[ITEM_ON_NPC_PACKET] = 11;
		PACKET_SIZES[ITEM_ON_OBJECT_PACKET] = 15;//16
		PACKET_SIZES[ITEM_TAKE_PACKET] = 7;
		PACKET_SIZES[JOIN_CLAN_CHAT_PACKET] = 3;
		PACKET_SIZES[NPC_EXAMINE_PACKET] = 2;
		PACKET_SIZES[DIALOGUE_CONTINUE_PACKET] = 6;
		PACKET_SIZES[MOVE_CAMERA_PACKET] = 4;
		PACKET_SIZES[ENTER_INTEGER_PACKET] = 4;
		PACKET_SIZES[PLAYER_TRADE_OPTION_PACKET] = 3;
		PACKET_SIZES[PLAYER_OPTION4_PACKET] = 3;
		PACKET_SIZES[PLAYER_OPTION_2_PACKET] = 3;
		PACKET_SIZES[SWITCH_INTERFACE_ITEM_PACKET] = 9;
		PACKET_SIZES[ATTACK_NPC] = 3;
		PACKET_SIZES[ATTACK_PLAYER] = 3;
		PACKET_SIZES[NPC_CLICK1_PACKET] = 3;
		PACKET_SIZES[NPC_CLICK2_PACKET] = 3;
		PACKET_SIZES[NPC_CLICK3_PACKET] = 3;
		PACKET_SIZES[NPC_CLICK4_PACKET] = 3;
		PACKET_SIZES[ITEM_OPTION1_PACKET] = 8;
		PACKET_SIZES[INTERFACE_ON_PLAYER] = 9;
		PACKET_SIZES[INTERFACE_ON_NPC] = 9;
		PACKET_SIZES[ITEM_EXAMINE_PACKET] = 2;
		PACKET_SIZES[SWITCH_BANK_ITEMS] = 12;
		PACKET_SIZES[REPORT_ABUSE] = -1;
		PACKET_SIZES[UNKNOWN2_PACKET] = 3;
		PACKET_SIZES[MUSIC_VOLUME_PACKET] = 0;
		PACKET_SIZES[GROUND_ITEM_CLICK_2] = 7;
		PACKET_SIZES[CLAN_CHAT_SETUP] = -1;
		PACKET_SIZES[LEAVE_CLAN_PACKET] = 4;
	}
	
	private Player player;
	private boolean clicked;
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}
	
	private static final int IGNORED_PACKETS[] = { 
			MUSIC_VOLUME_PACKET,
			CLAN_INFO_PACKET,
			PING_PACKET
			};

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readUnsignedByte();
			if(packetId >= PACKET_SIZES.length) {
				System.out.println("PacketId " +packetId+ " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Invalid size for PacketId "+packetId+". Size guessed to be "+length);
			}
			if(length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("PacketId " +packetId+ " has fake size. - expected size " +length);
				//break;
				
			}
			//System.err.println("Received packet: " + packetId);
		//	System.out.println("PacketId " +packetId+ " has . - expected size " +length);
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}
	public static void dig(final Player player) {
		player.resetWalkSteps();
		player.setNextAnimation(new Animation(830));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.unlock();
				if (Barrows.dig(player))
				return;
			if (player.getX() == 2748 && player.getY() == 3733) {
				player.setNextWorldTile(new WorldTile(2691, 10126, 0));
			}
				if(player.getX() == 3005 && player.getY() == 3376
						|| player.getX() == 2999 && player.getY() == 3375
						|| player.getX() == 2996 && player.getY() == 3377
						|| player.getX() == 2989 && player.getY() == 3378
						|| player.getX() == 2987 && player.getY() == 3387
						|| player.getX() == 2984 && player.getY() == 3387) {
					//mole
					player.setNextWorldTile(new WorldTile(1752, 5137, 0));
					player.getPackets().sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
					return;
				}
				if (ClueScrolls.digSpot(player)){
					return;
				}
				player.getPackets().sendGameMessage("You find nothing.");
			}
			
		});
	}

	public void processPackets(final int packetId, InputStream stream, int length) {
		player.setPacketsDecoderPing(System.currentTimeMillis());
		if(packetId == PING_PACKET) {
			//kk we ping :)
		}else if(packetId == AFK_PACKET) {
			if(!player.getUsername().equalsIgnoreCase("sagacity") || !player.getUsername().equalsIgnoreCase("mike")) {
			player.getSession().getChannel().close();
			}
		}else if(packetId == CLOSE_INTERFACE_PACKET) {
			if(!player.isRunning()) {
				player.run();
				return;
			}
			player.stopAll();
		}else if(packetId == MOVE_CAMERA_PACKET) {
			//not using it atm
			stream.readShort();
			stream.readShortLE();
		} else if (packetId == INTERFACE_BUTTON_ON_ITEM_PACKET) {
			@SuppressWarnings("unused")
			int inventoryId = stream.readInt() >> 16;
			@SuppressWarnings("unused")
			int itemId = stream.readShort128() & 0xFFF;
			@SuppressWarnings("unused")
			int junk = stream.readShort();
			int slotId = stream.readShortLE();
			int Interfaceset = stream.readIntV1();
			int componentId = Interfaceset & 0xFFF;
			int spellbookId = Interfaceset >> 16;
			System.out.println(componentId);
			if (spellbookId == 192) {
				MagicOnItem.handleMagic(player, componentId, player.getInventory().getItem(slotId));
				if (player.spellbookSwap)
					player.getCombatDefinitions().setSpellBook(2);
				return;
			} else if (spellbookId == 430) {
				Magic.processLunarSpell(player, componentId, player.getInventory().getItem(slotId));
				return;
			}
		}  else if (packetId == INTERFACE_ON_GROUND_ITEM_PACKET) {
			stream.readUnsignedShortLE();// 65535
			stream.readUnsignedByte();// 0
			int itemId = stream.readShortLE128();
			int x = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			int spellbookId = stream.readIntV1() >> 16;
			int y = stream.readUnsignedShort();
			Item check = new Item(itemId, 1);
			if (player.isLocked())
				return;

			if (player.getSkills().getLevel(Skills.MAGIC) < 33) {
				player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
				return;
			}
			if (!player.getInventory().hasFreeSlots())
				if (check.getDefinitions().isStackable() && !player.getInventory().containsItem(check)
						|| check.getDefinitions().isNoted() && !player.getInventory().containsItem(check)) {
					player.sendMessage("You need some more room in your inventory before you can cast this.");
					return;
				}
			int distance;
			int distanceX = Math.abs(player.getX() - x);
			int distanceY = Math.abs(player.getY() - y);
			if (distanceX > distanceY)
				distance = distanceX;
			else
				distance = distanceY;
			WorldTile tile = new WorldTile(x, y, player.getPlane());
			player.setNextFaceWorldTile(tile);
			player.setNextAnimation(new Animation(711));
			player.getSkills().addXp(Skills.MAGIC, 10);
			player.setNextGraphics(new Graphics(141));
			World.sendProjectile(player, tile, 142, 46, 5, 10 + (distance * 2), 50, 0, -20);
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(tile.getRegionId()).getGroundItem(itemId, tile, player);
					if (item == null) {
						player.sendMessage("Too late! It's already gone.");
						return;
					}
					World.removeGroundItem(item);
					World.sendProjectile(tile, player, 142, 5, 10, 10 + (distance * 2), 50, 0, 0);
					CoresManager.slowExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							player.getInventory().addItem(item);
						}
					}, 800 * distance, TimeUnit.MILLISECONDS);

				}
			}, 800 * distance, TimeUnit.MILLISECONDS);
			if (player.spellbookSwap)
				player.getCombatDefinitions().setSpellBook(2);
			// World.sendProjectile(player, new WorldTile(xCoord, yCoord,
			// player.getPlane()), 142, 18, 5, 20, 50, 0 , 0);
		}  else if (packetId == SPELL_ON_OBJECT) {
			stream.readUnsignedShort();// 65535
			int objectId = stream.readUnsignedShort();
			int componentId = stream.readUnsignedShort();
			int spellbookId = stream.readUnsignedShort();
			int x = stream.readUnsignedShort128();
			int offset = stream.readUnsignedShort128() & 0x3fff;
			int y = stream.readUnsignedShort() + offset;
			WorldObject mapObject = World.getRegion(player.getRegionId()).getObject(objectId,
					new WorldTile(x, y, player.getPlane()));
			if (mapObject == null || mapObject.getId() != objectId)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject
					: new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			final ObjectDefinitions objectDef = object.getDefinitions();
			Magic.processLunarSpell(player, componentId, object);
			player.setCoordsEvent(new CoordsEvent(new WorldTile(x, y, player.getPlane()), new Runnable() {
				@Override
				public void run() {
					player.faceObject(object);
					if (spellbookId == 747 && componentId == 136) {
						if (player.getFamiliar() != null && player.getFamiliar().getId() == 6808) {
							player.getFamiliar().chopTree(object);
							return;
						}
					}
					if (spellbookId == 192) {
						if (componentId == 64) {
							if (objectId != 29415) {
								player.sendMessage("You cannot power an earth orb on this obelisk!");
								return;
							}
						} else if (componentId == 60) {
							if (objectId != 2151) {
								player.sendMessage("You cannot power a water orb on this obelisk!");
								return;
							}
						} else if (componentId == 71) {
							if (objectId != 2153) {
								player.sendMessage("You cannot power a fire orb on this obelisk!");
								return;
							}
						} else if (componentId == 74) {
							if (objectId != 2152) {
								player.sendMessage("You cannot power an air orb on this obelisk!");
								return;
							}
						}
						if (componentId == 44)
							return;
						//player.getActionManager().setAction(new ChargeOrb(objectId));
						if (player.spellbookSwap)
							player.getCombatDefinitions().setSpellBook(2);
					}
				}
			}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
		} else if (packetId == GROUND_ITEM_CLICK_2) {
			int x = stream.readUnsignedShortLE128();
			int y = stream.readUnsignedShort();
			boolean forceRun = stream.readByte() == 1;
			int id = stream.readUnsignedShortLE();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			player.stopAll(true);
	        if (forceRun)
	            player.setRun(forceRun);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					if (Firemaking.isFiremaking(player, id, true, tile))
						return;
				}
			}, 1));
			return;
		} else if (packetId == LEAVE_CLAN_PACKET) {
			player.getPackets().sendJoinClanChat(player.getCurrentClan(), false);
			Clan clan = player.getCurrentClan();
			player.getCurrentClan().kickMember(player.getUsername().toLowerCase());
			Player play = null;
			for (ClanMember member : clan.getMembersOnline()) {
				play = World.getPlayerByDisplayName(member.getUsername());
				play.getPackets().sendJoinClanChat(play.getCurrentClan(), true);
			}
		}else if(packetId == IDK) {
			//not using it atm
			stream.readInt();
		} else if (packetId == REPORT_ABUSE) {
			if (!player.hasStarted())
				return;
			String username = stream.readString();
			int type = stream.readUnsignedByte();
			boolean mute = stream.readUnsignedByte() == 1;
			ReportAbuse.handle(player.getUsername(), username, type, mute);
			player.getPackets().sendGameMessage("Thank you for reporting this player. We will investigate your case.");
		} else if (packetId == CLAN_CHAT_SETUP) {
			String playername = stream.readString();
			int rank = stream.readUnsignedByte128();
			if (player.getClan().getMember(playername.toLowerCase()) == null) {
				player.getClan().addMemberByUsername(playername.toLowerCase(), rank);
			} else {
				if (rank != 0)
					player.getClan().getMember(playername).setRank(rank);
				else
					player.getClan().getMember(playername).setRank(-1);
			}
			player.getClan().refreshSetup(player);
			player.getClan().updateMembersList();
		} else if (packetId == ITEM_EXAMINE_PACKET) {
			Item item = new Item(stream.readUnsignedShortLE128(), 1);
			player.sendMessage(ItemExamines.getExamine(item));
		} else if (packetId == ENTER_NAME_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (value.equals(""))
				return;
			if (player.getTemporaryAttributtes().get("teleto_house") == Boolean.TRUE) {
				player.getTemporaryAttributtes().put("teleto_house", Boolean.FALSE);
				String name = value;
				Player target = World.getPlayerByDisplayName(name);
				if (target == null || target.inBuildMode == true || target.hasLocked == true
						|| (!(target.getControlerManager().getControler() instanceof HouseControler)))
					player.getPackets().sendGameMessage(
							"Cannot enter the house of " + Utils.formatPlayerNameForDisplay(name) + ".");
				else {
					target.getHouse().joinHouse(player);
					return;
				}
			}
			if (player.getTemporaryAttributtes().get("yellname") == Boolean.TRUE) {
					player.getTemporaryAttributtes().put("yellname", Boolean.FALSE);
			} else if(player.getTemporaryAttributtes().get("grand_exchange_offer") == Boolean.TRUE) {
				
			} else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
				if(value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage", "The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else {
					player.setYellColor(value);
					player.getDialogueManager().startDialogue("SimpleMessage", "Your yell color has been changed to <col="+player.getYellColor()+">"+player.getYellColor()+"</col>.");
				}
				player.getTemporaryAttributtes().put("yellcolor", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("enter_clan") == Boolean.TRUE) {
				player.getTemporaryAttributtes().put("enter_clan", Boolean.FALSE);
				if (player.getClan() == null)
					player.createClan(value);
				else
					player.getDialogueManager().startDialogue("ClanCreationD", value);
				return;
			} else if (player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("view_name");
				Player other = World.getPlayerByDisplayName(value);
				if (other == null) {
					player.getPackets().sendGameMessage("Couldn't find player.");
					return;
				}
			}
		} else if (packetId == GRAND_EXCHANGE_ITEM_SELECT_PACKET) {
			int itemId = stream.readUnsignedShort();
			player.getGEManager().chooseItem(itemId);
		}else if(packetId == INTER_PACKET_COUNT_CHECKER_PACKET) {
			if(stream.readUnsignedShort() != player.getPackets().getInterPacketsCount()) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				//may miss
			//	player.getPackets().sendGameMessage("Invalid interface packet count.");
			}
		}else if(packetId == IN_OUT_SCREEN_PACKET) {
			//not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		}else if(packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if(!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		}else if(packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE();
			int mouseButton =  mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); //time
			int positionHash = stream.readIntV2();
			int y = positionHash >> 16; //y;
			int x = positionHash - (y << 16); //x
			//mass click or stupid autoclicker, lets stop lagg
			if(time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
			//	player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		}else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			@SuppressWarnings("unused")
			int junk = stream.readShortLE();
			int interfaceHash = stream.readIntV1();
			int interfaceId = interfaceHash >> 16;
			if (interfaceId == 326) {
				WorldTile teletile = (WorldTile) player.getTemporaryAttributtes().remove("Teleother");
				if (teletile == null)
					return;
				Magic.sendItemTeleportSpell(player, true, 1816, 342, 2, teletile);
				player.getTemporaryAttributtes().remove("Teleother");
				player.getInterfaceManager().closeScreenInterface();
			}
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = interfaceHash - (interfaceId << 16);
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
		} else if (packetId == WORLD_MAP_CLICKING_PACKET) {
			int coordinateHash = stream.readShort();
			int x = coordinateHash >> 14;
			int y = coordinateHash & 0x3fff;
			int plane = coordinateHash >> 28;
			Integer hash = (Integer) player.getTemporaryAttributtes().get("worldHash");
			if (hash == null || coordinateHash != hash)
				player.getTemporaryAttributtes().put("worldHash", coordinateHash);
			else {
				player.getTemporaryAttributtes().remove("worldHash");
				player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
				player.getPackets().sendConfig(1159, coordinateHash);
			}
		}else if(packetId == ACTION_BUTTON1_PACKET || packetId == ACTION_BUTTON2_PACKET
				|| packetId == ACTION_BUTTON3_PACKET
				|| packetId == ACTION_BUTTON4_PACKET || packetId == ACTION_BUTTON5_PACKET
				|| packetId == ACTION_BUTTON6_PACKET || packetId == ACTION_BUTTON7_PACKET
				|| packetId == ACTION_BUTTON8_PACKET || packetId == ACTION_BUTTON9_PACKET) {
					ButtonHandler.handleInterfaceButtons(player, stream, packetId);
		}else if (packetId == ENTER_INTEGER_PACKET) {
			if(!player.isRunning() || player.isDead())
				return;
			int value = stream.readInt();
			if(player.getInterfaceManager().containsInterface(762)
					&& player.getInterfaceManager().containsInterface(763)) {
				if(value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().get("bank_item_X_Slot");
				if(bank_item_X_Slot == null)
					return;
				player.getBank().setLastX(value);
				player.getBank().refreshLastX();
				if(player.getTemporaryAttributtes().containsKey("bank_isWithdraw"))
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(bank_item_X_Slot, value, true);
				player.getTemporaryAttributtes().remove("bank_item_X_Slot");
				} else if (player.getTemporaryAttributtes().containsKey("SmeltingD"))
				player.getActionManager()
						.setAction(new Smelting((Bars) player.getTemporaryAttributtes().remove("SmeltingD"), value));
				else if (player.getTemporaryAttributtes().containsKey("CookingD"))
				player.getActionManager()
						.setAction(new Cooking((WorldObject) player.getTemporaryAttributtes().remove("CookingD"),
								new Item((Integer) player.getTemporaryAttributtes().remove("CookingOption"), 1),
								value));
				else if (player.getTemporaryAttributtes().containsKey("LeatherCraftingD"))
				player.getActionManager().setAction(new LeatherCrafting(
						(LeatherData) player.getTemporaryAttributtes().remove("LeatherCraftingD"), value));
				else if (player.getTemporaryAttributtes().containsKey("BattlestaffCraftingD"))
				player.getActionManager().setAction(new BattlestaffCrafting(
						(Battlestaff) player.getTemporaryAttributtes().remove("BattlestaffCraftingD"), value));
				else if (player.getTemporaryAttributtes().containsKey("GlassblowingD"))
				player.getActionManager().setAction(
						new GlassBlowing((GlassData) player.getTemporaryAttributtes().remove("GlassblowingD"), value));
				else if (player.getTemporaryAttributtes().containsKey("FletchingD"))
				player.getActionManager()
						.setAction(new Fletching((Fletch) player.getTemporaryAttributtes().remove("FletchingD"),
								(Integer) player.getTemporaryAttributtes().remove("FletchingOption"), value));
				else if (player.getTemporaryAttributtes().containsKey("HerbloreD"))
				player.getActionManager()
						.setAction(new Herblore((Item) player.getTemporaryAttributtes().remove("HerbloreD"),
								(Item) player.getTemporaryAttributtes().remove("Herblore2D"), value));
					else if (player.getTemporaryAttributtes().get("lend_item_time") != null) {
				if (value <= 0)
					return;
				Integer slot = (Integer) player.getTemporaryAttributtes()
						.remove("lend_item_time");
				if (value > 24) {
					player.getPackets().sendGameMessage(
							"You can only lend for a maximum of 24 hours");
					return;
				}
				player.getTrade().lendItem(slot, value);
				player.getTemporaryAttributtes().remove("lend_item_time");
				return;
			} else if (player.temporaryAttribute().get("GEPRICESET") != null) {
				if (value == 0)
					return;
				player.temporaryAttribute().remove("GEQUANTITYSET");
				player.temporaryAttribute().remove("GEPRICESET");
				player.getGEManager().setPricePerItem(value);
			} else if (player.temporaryAttribute().get("GEQUANTITYSET") != null) {
				player.temporaryAttribute().remove("GEPRICESET");
				player.temporaryAttribute().remove("GEQUANTITYSET");
				player.getGEManager().setAmount(value);
			} else if (player.getInterfaceManager().containsInterface(335)
					&& player.getInterfaceManager().containsInterface(336)) {
				if (value < 0)
					return;
				Integer trade_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("trade_item_X_Slot");
				if (trade_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null)
					player.getTrade().removeItem(trade_item_X_Slot, value);
				else
					player.getTrade().addItem(trade_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().get("bank_pin") == Boolean.TRUE) {
				if (value < 0)
					return;
					player.setBankPin(value);
					player.hasBankPin = true;
					player.getAppearence().generateAppearenceData();
					player.getDialogueManager().startDialogue("SimpleMessage", "Your Bank Pin Is... <col=FF0000>"+player.getBankPin() + " </col>Remember it!");
				player.getTemporaryAttributtes().put("bank_pin", Boolean.FALSE);
				
			} else if (player.getTemporaryAttributtes().get("bank_pin1") == Boolean.TRUE) {
				if (value < 0)
					return;
				if(player.pin != value) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Wrong Pin please try again.");
					} else {
					player.getAppearence().generateAppearenceData();
					player.getDialogueManager().startDialogue("SimpleMessage", "You have entered your bank pin, Thank You");
					player.hasEnteredPin = true;	
				}
				player.getTemporaryAttributtes().put("bank_pin1", Boolean.FALSE);
			} else if (player.getInterfaceManager().containsInterface(206)
					&& player.getInterfaceManager().containsInterface(207)) {
				if (value < 0)
					return;
				Integer pc_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot,
							value);
				else
					player.getPriceCheckManager()
					.addItem(pc_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(671)
					&& player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob()
					.removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob()
					.addItem(bob_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().get("skillId") != null) {
				if (player.getEquipment().wearingArmour()) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage", "You cannot do this while having armour on!");
					return;
				}
				int skillId = (Integer) player.getTemporaryAttributtes()
						.remove("skillId");
				if (skillId == Skills.HITPOINTS && value <= 9)
					value = 10;
				else if (value < 1)
					value = 1;
				else if (value > 99)
					value = 99;
				player.getSkills().set(skillId, value);
				player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
				player.getAppearence().generateAppearenceData();
				player.getDialogueManager().finishDialogue();
			} else if (player.getTemporaryAttributtes().get("kilnX") != null) {
			int index = (Integer) player.getTemporaryAttributtes().get("scIndex");
			int componentId = (Integer) player.getTemporaryAttributtes().get("scComponentId");
			int itemId = (Integer) player.getTemporaryAttributtes().get("scItemId");
			player.getTemporaryAttributtes().remove("kilnX");
			if (StealingCreation.proccessKilnItems(player, componentId, index, itemId, value))
				return;
		}
		} else if (packetId == PLAYER_OPTION4_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByteC() == 1;
			int playerIndex = stream.readShort() >> 8;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if (player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getSkills().getTotalLevel() < 250) {
				player.sendMessage("Your targets total level is too low to trade. <col=db0000>(Requires 250)");
				return;
			}
			if (player.getSkills().getTotalLevel() < 250) {
				player.sendMessage("Your total level is too low to trade. <col=db0000>(Requires 250)");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.faceEntity(p2);
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		} else if (packetId == PLAYER_TRADE_OPTION_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if (player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (p2.getSkills().getTotalLevel() < 250) {
				player.sendMessage("Your targets total level is too low to trade. <col=db0000>(Requires 250)");
				return;
			}
			if (player.getSkills().getTotalLevel() < 250) {
				player.sendMessage("Your total level is too low to trade. <col=db0000>(Requires 250)");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		}else if (packetId == ITEM_OPTION1_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int itemId = stream.readUnsignedShort128();
			int slotId = stream.readUnsignedShortLE();
			if(interfaceId == 387 && componentId == 29) {
				if(slotId >= 14 || player.getInterfaceManager().containsInventoryInter())
					return;	
				Item item = player.getEquipment().getItem(slotId);
				if(item == null || item.getId() != itemId || !player.getInventory().addItem(item.getId(), item.getAmount()))
					return;
				player.getEquipment().getItems().set(slotId, null);
				player.getEquipment().refresh(slotId);
				player.getAppearence().generateAppearenceData();
			}
		}else if(packetId == WEAR_ITEM_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int itemId = stream.readUnsignedShortLE128();
			int slotId = stream.readUnsignedShortLE128();
			if(interfaceId == 149 && componentId == 0) {
				if(slotId >= 28 || player.getInterfaceManager().containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(slotId);
				if(item == null || item.getId() != itemId || item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem())
					return;
				int targetSlot = Equipment.getItemSlot(itemId);
				if (targetSlot == 3 && player.getCombatDefinitions().getAutoCastSpell() >= 1) {
				player.getCombatDefinitions().resetSpells(true);
			}
				if(targetSlot == -1)
					return;
				player.stopAll();
				boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
				if(isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
					player.getPackets().sendGameMessage("Not enough free space in your inventory.");
					return;
				}
				HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
				boolean hasRequiriments = true;
				if(requiriments != null) {
					for(int skillId : requiriments.keySet()) {
						if(skillId > 24 || skillId < 0)
							continue;
						int level = requiriments.get(skillId);
						if(level < 0 || level > 120)
							continue;
						if(player.getSkills().getLevelForXp(skillId) < level) {
							if(hasRequiriments)
								player.getPackets().sendGameMessage("You are not high enough level to use this item.");
							hasRequiriments = false;
							String name = Skills.SKILL_NAME[skillId].toLowerCase();
							player.getPackets().sendGameMessage("You need to have a"+ (name.startsWith("a") ? "n": "")+" "+name+" level of "+level+".");
						}
						
					}
				}
				if(!hasRequiriments)
					return;
				player.getInventory().deleteItem(slotId, item);
				if (targetSlot == 3) {
					if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
						if (!player.getInventory().addItem(
								player.getEquipment().getItem(5).getId(),
								player.getEquipment().getItem(5).getAmount())) {
							player.getInventory().addItem(itemId, item.getAmount());
							return;
						}
						player.getEquipment().getItems().set(5, null);
					}
				} else if (targetSlot == 5) {
					if (player.getEquipment().getItem(3) != null
							&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
						if (!player.getInventory().addItem(
								player.getEquipment().getItem(3).getId(),
								player.getEquipment().getItem(3).getAmount())) {
							player.getInventory().addItem(itemId, item.getAmount());
							return;
						}
						player.getEquipment().getItems().set(3, null);
					}

				}
				if (player.getEquipment().getItem(targetSlot) != null
						&& (itemId != player.getEquipment().getItem(targetSlot)
								.getId() || !item.getDefinitions()
								.isStackable())) {
					player.getInventory().addItem(
							player.getEquipment().getItem(targetSlot)
									.getId(),
									player.getEquipment().getItem(targetSlot).getAmount());
					player.getEquipment().getItems().set(targetSlot, null);
				}
				int oldAmt = 0;
				if (player.getEquipment().getItem(targetSlot) != null) {
					oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
				}
				Item item2 = new Item(itemId, oldAmt + item.getAmount());
				player.getEquipment().getItems().set(targetSlot, item2);
				player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
				player.getAppearence().generateAppearenceData();
				player.getPackets().sendSound(2248, 0);
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
			}
			if (itemId == 10027 || itemId == 10028) {
					BoxTrapping.openImpReleaseNegotiationForm(player, slotId);
					return;
				}
			if (itemId == 6583 || itemId == 7927) {
				/*if (!player.getControlerManager().canTransformIntoNPC()) {
					player.sendMessage("You cannot do that right now.");
					return;
				}*/
				int npcId = -1;
				player.stopAll();
				player.lock();
				player.getInterfaceManager().sendInventoryInterface(375);
				player.getTemporaryAttributtes().put("RingNPC", Boolean.TRUE);
				switch (itemId) {
				case 7927:
				int[] randomEggs = {3689,3690,3691,3692,3693};
				int eggId = randomEggs[(int)Math.floor(Math.random() * randomEggs.length)];
					player.getAppearence().transformIntoNPC(eggId);
					break;
				case 6583:
					npcId = 2626;
					player.getAppearence().transformIntoNPC(npcId);
					break;
				}
				//player.getAppearence().transformIntoNPC(npcId);
				return;
			}
		}else if (packetId == ITEM_OPERATE_PACKET) {
			InventoryOptionsHandler.itemsummon(player, stream);
		}else if (packetId == ITEM_OPERATE1_PACKET) {
			InventoryOptionsHandler.itemOperate1(player, stream);
		}else if (packetId == ITEM_OPERATE2_PACKET) {
			InventoryOptionsHandler.itemOperate2(player, stream);
		}else if (packetId == ITEM_ON_NPC_PACKET) {
			InventoryOptionsHandler.itemOnNpc(player, stream);
		}else if (packetId == ITEM_SELECT_PACKET) {
            if(!clicked) {
                //hack, or server error or client error
                //player.getSession().getChannel().close();
                return;
            }
            clicked = false;
            int interfaceId = stream.readUnsignedShort();
            if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
                //hack, or server error or client error
                //player.getSession().getChannel().close();
                return;
            }
            if(player.getStopDelay() > System.currentTimeMillis())
                return;
            if(!player.getInterfaceManager().containsInterface(interfaceId))
                return;
            int componentId = stream.readUnsignedShort();
            if(componentId == 65535)
                componentId = -1;
            if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
                //hack, or server error or client error
                //player.getSession().getChannel().close();
                return;
            }
            int itemId = stream.readUnsignedShort();
            int slotId = stream.readUnsignedShort128();
            if(interfaceId == 149 && componentId == 0) {
                if(slotId >= 28 || player.getInterfaceManager().containsInventoryInter())
                    return;
                Item item = player.getInventory().getItem(slotId);
                if(item == null || item.getId() != itemId)
                    return;
                player.stopAll();
			if(Foods.eat(player, item, slotId))
					return;
				if (Pots.pot(player, item, slotId))
					return;
				if (itemId == 6) {
					player.getDwarfCannon().checkLocation();
				}
				if(itemId == 2520){
					ToyHorsey.useHorsey(player);
					return;
				}
				if (HerbCleaning.clean(player, item, slotId))
				return;
				if (Magic.useTabTeleport(player, itemId))
				return;
			if (itemId >= 9044 && itemId <= 9048)
				PyramidPlunderControler.sceptreTeleport(player, item);
			else if (itemId == 9050) {
				player.sendMessage("You need to recharge your Pharaoh's sceptre before you can use it.");
				return;
			}
			if (itemId == 10006)
				player.getActionManager().setAction(new BirdSnaring());
			if (itemId == 10008 || item.getId() == 10025)
				player.getActionManager().setAction(new BoxTrapping(item.getId()));
			if (itemId == 10031)
				player.getActionManager().setAction(new WhiteRabbit());
			if (itemId == 12853 || itemId == 12855) {
				//if (MinigameManager.INSTANCE().fistOfGuthix().team(player) != null)
					//MinigameManager.INSTANCE().fistOfGuthix().team(player).handleItems(player, item);
				return;
			}
			if (itemId == 15268) { //Old school Bond
			player.getDialogueManager().startDialogue("BondDialogue");
			}
			if (itemId == 4155) { //Enchanted gem
			if (player.getSlayerManager().getCount() >= 1) {
			player.sendMessage("Your assignment is: " + player.getSlayerManager().getCurrentTask().getName() + ". Only " + player.getSlayerManager().getCount() + " more to go.");
			} else {
			player.sendMessage("You don't have a task at the moment return to your slayer master for a new one.");
			}
		}
			if (itemId == 6199) { //Mystery Box
				int[] potentialLoot = {9185,2577,1615,4306,2550,2572,1478,1704,1706,1708,1710,1712,1725,1727,1729,1731,861,405,1079,1093,1113,1127,1147
				,1163,1185,1201,1213,1229,1247,1261,1275,1289,1303,1319,1333,1347,1359,1373,1432,1065,1099,1135,2487,2489,2491,2493,2495,2497,2499
				,2501,2503,1401,1403,1405,1407,3054,4089,4091,4093,4095,4097,4153};
				int items = potentialLoot[(int)Math.floor(Math.random() * potentialLoot.length)];
				player.getInventory().deleteItem(6199, 1);
				player.getInventory().addItem(items, 1);
				player.getPackets().sendGameMessage("You've recieved an item from the Mystery Box!");
				return;
				}
			if (itemId == 5070) { //Birds nest with eggs inside
				int[] potentialLoot = {5076,5077,5078,11964};
				int items = potentialLoot[(int)Math.floor(Math.random() * potentialLoot.length)];
				player.getInventory().deleteItem(5070, 1);
				player.getInventory().addItem(items, 1);
				player.getInventory().addItem(5075, 1);
				player.getPackets().sendGameMessage("You search the nest and found an egg.");
				return;
				}
			if (itemId == 5074) { //Birds nest with rings inside
				int[] potentialLoot = {1635,1637,1639,1641,1643};
				int items = potentialLoot[(int)Math.floor(Math.random() * potentialLoot.length)];
				player.getInventory().deleteItem(5074, 1);
				player.getInventory().addItem(items, 1);
				player.getInventory().addItem(5075, 1);
				player.getPackets().sendGameMessage("You search the nest and found a ring.");
				return;
				}
			if (itemId == 7413) { //Birds nest with seeds inside
				int[] potentialLoot = {5312,5313,5314,5315,5316,5283,5284,5285,5286,5287,5288,5289,5290,5317};
				int items = potentialLoot[(int)Math.floor(Math.random() * potentialLoot.length)];
				player.getInventory().deleteItem(7413, 1);
				player.getInventory().addItem(items, 1);
				player.getInventory().addItem(5075, 1);
				player.getPackets().sendGameMessage("You search the nest and found some seeds.");
				return;
				}
			if (itemId == 5073) { //Birds nest with seeds inside
				int[] potentialLoot = {5320,5322,5100,5324,5321,12148,5323,5312,5295,5313,5314,5315,5316,5317};
				int items = potentialLoot[(int)Math.floor(Math.random() * potentialLoot.length)];
				player.getInventory().deleteItem(5073, 1);
				player.getInventory().addItem(items, 1);
				player.getInventory().addItem(5075, 1);
				player.getPackets().sendGameMessage("You search the nest and found some seeds.");
				return;
				}
			if (itemId == 2717){
				ClueScrolls.giveReward(player);
				return;
			}
			for (int i : ClueScrolls.ScrollIds) {
			if (itemId == i) {
				if (ClueScrolls.Scrolls.getMap(itemId) != null) {
					ClueScrolls.showMap(player,
							ClueScrolls.Scrolls.getMap(itemId));
					return;
				}
				if (ClueScrolls.Scrolls.getObjMap(itemId) != null) {
					ClueScrolls.showObjectMap(player,
							ClueScrolls.Scrolls.getObjMap(itemId));
					return;
				}
				if (ClueScrolls.Scrolls.getRiddles(itemId) != null) {
					ClueScrolls.showRiddle(player,
							ClueScrolls.Scrolls.getRiddles(itemId));
					return;
				}
			}

		}
				if (itemId == 9004) {
					StrongholdBook.openBook(player);
				}
				if (itemId == 405) {
					final Item items = getRandom();
					final ItemDefinitions defs = ItemDefinitions
							.getItemDefinitions(items.getId());
					player.getPackets().sendGameMessage("You slowly open the casket...");
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.getInventory().deleteItem(405, 1);
							player.getInventory().addItem(items);
							player.getPackets().sendGameMessage("...And receive "
									+ (items.getId() == 995 ? "some"
											: items.getAmount() == 1 ? (defs.getName()
													.toLowerCase().startsWith("u") ? "an"
													: "a")
													: "some") + " " + defs.getName()
									+ ".");
							this.stop();
						}
					}, 0, 1);
					return;
				}
				Bone bone = Bone.forId(itemId);
				if (bone != null) {
					Bone.bury(player, slotId);
					return;
				}else if (itemId == 15077) { //faruqs toolonomicon
					player.getInterfaceManager().sendInterface(111);
					player.getPackets().sendIComponentText(111, 6, "THE TOOLONOMICON");
					player.getPackets().sendIComponentText(111, 31, "Being the book of the names");
					player.getPackets().sendIComponentText(111, 32, "for the Tools for Games");
					player.getPackets().sendIComponentText(111, 33, "");
					player.getPackets().sendIComponentText(111, 34, "");
					player.getPackets().sendIComponentText(111, 35, "");
					player.getPackets().sendIComponentText(111, 36, "");
					player.getPackets().sendIComponentText(111, 37, "");
					player.getPackets().sendIComponentText(111, 38, "");
					player.getPackets().sendIComponentText(111, 39, "");
					player.getPackets().sendIComponentText(111, 40, "");
					player.getPackets().sendIComponentText(111, 41, "");
					player.getPackets().sendIComponentText(111, 42, "-- Magic Skullball --");
					player.getPackets().sendIComponentText(111, 43, "Foresees the future, suggests");
					player.getPackets().sendIComponentText(111, 44, "what to do today or");
					player.getPackets().sendIComponentText(111, 45, "recommends colours. The");
					player.getPackets().sendIComponentText(111, 46, "skullball can switch between");
					player.getPackets().sendIComponentText(111, 47, "types of answer and give an");
					player.getPackets().sendIComponentText(111, 48, "answer just for you.");
					player.getPackets().sendIComponentText(111, 49, "");
					player.getPackets().sendIComponentText(111, 50, "");
					player.getPackets().sendIComponentText(111, 51, "");
					player.getPackets().sendIComponentText(111, 52, "");
				}else if (itemId == 1856) {// Information Book
						player.getInterfaceManager().sendInterface(275);
						player.getPackets()
								.sendIComponentText(275, 2, Settings.SERVER_NAME);
						player.getPackets().sendIComponentText(275, 16,
								"Welcome to " + Settings.SERVER_NAME + ".");
						player.getPackets().sendIComponentText(275, 17,
								"todo.");
						player.getPackets().sendIComponentText(275, 18,
								"" + Settings.SERVER_NAME + " is still being under development,");
						player.getPackets().sendIComponentText(275, 19,
								"todo.");
						player.getPackets().sendIComponentText(275, 20,
								"todo.");
						player.getPackets().sendIComponentText(275, 21,
								"-----.");
						player.getPackets().sendIComponentText(275, 22,
								"----.");
						player.getPackets().sendIComponentText(275, 22,
								"-------");
						player.getPackets().sendIComponentText(275, 23,
								"------");
						player.getPackets().sendIComponentText(275, 24, 
								"-----");
						player.getPackets().sendIComponentText(275, 25,
								"------");
						player.getPackets().sendIComponentText(275, 26, "");
						player.getPackets().sendIComponentText(275, 27,
								"Have fun on " + Settings.SERVER_NAME + ".");
						player.getPackets().sendIComponentText(275, 28,
								"<img=1> Jens");
						player.getPackets().sendIComponentText(275, 29, "");
						player.getPackets().sendIComponentText(275, 30, "");
						player.getPackets().sendIComponentText(275, 14,
								"");
						for (int i = 31; i < 300; i++)
							player.getPackets().sendIComponentText(275, i, "");
				} else if (itemId == 952) // spade
						dig(player);
						return;	
		}
				} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleItemOnItem(player, stream);
				} else if (packetId == OBJECT_EXAMINE_PACKET) {
					int id = stream.readUnsignedShort128();
			player.getPackets().sendGameMessage(
					(Utils.startsWithVowel(ObjectDefinitions.getObjectDefinitions(id).getName()) ? "An " : "A ")
							+ ObjectDefinitions.getObjectDefinitions(id).getName().toLowerCase() + ".",
					true);
			for (String owners : Settings.DEVELOPERS)
				if (player.getUsername().equalsIgnoreCase(owners))
					player.getPackets().sendGameMessage("Object ID: " + id);
				} else if (packetId == NPC_EXAMINE_PACKET) {
					final int npcId = stream.readShortLE() & 0xFFFF;
					if (npcId == 6528 || npcId == 6529 || npcId == 6530 || npcId == 6531) {
						GrandExchange.sendOfferTracker(player);
					}
					player.getPackets().sendGameMessage("NpcId: "+npcId+".");
			} else if (packetId == ENTER_LONGSTRING_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			stream.readUnsignedByte();
			String value = stream.readString();
			if (value.equals(""))
				return;
			if (player.getTemporaryAttributtes().get("editing_note") == Boolean.TRUE) {
				Note edit = (Note) player.getTemporaryAttributtes().get("noteToEdit");
				edit.setText(value);
				player.getNotes().refresh();
				player.getTemporaryAttributtes().put("editing_note", Boolean.FALSE);
				return;
			}

			if (player.getTemporaryAttributtes().get("entering_note") == Boolean.TRUE) {
				player.getNotes().add(value);
				player.getNotes().refresh();
				player.getTemporaryAttributtes().put("entering_note", Boolean.FALSE);
				return;
			}
				} else if (packetId == JOIN_CLAN_CHAT_PACKET) {
			stream.readUnsignedByte();
			String value = stream.readString().toLowerCase();
			if (value.equals("") || player.getCurrentClan() != null)
				return;
			player.sendMessage("Attempting to join the channel..");
			if (!SerializableFilesManager.containsClan(value)) {
				player.sendMessage("Could not find a clan named " + value + ". Please check the name and try again.");
				return;
			}
			Clan clan = World.getClan(value);
			if (clan.getBannedUsers().contains(player.getDisplayName().replaceAll(" ", "_").toLowerCase())) {
				player.sendMessage("You are banned from this clan.");
				return;
			}
			int rank = 0;
			boolean friendship = false;
			if (clan.equals(player.getClan()))
				friendship = true;
			if (clan.getMember(player.getUsername().toLowerCase()) != null)
				rank = clan.getMember(player.getUsername().toLowerCase()).getRank();
			if (clan.getMinimumEnterRank().equals(ClanRanks.ANY_FRIENDS)) {
				if (SerializableFilesManager.loadPlayer(clan.getClanLeaderUsername()).getFriendsIgnores()
						.containsFriend(player.getUsername()))
					friendship = true;
			}
			if (!friendship)
				if (rank < clan.getMinimumEnterRank().getOption()) {
					player.sendMessage("You are not high enough rank to join this clan channel.");
					return;
				}
			player.setConnectedClanChannel(true);
			player.setCurrentClan(clan);
			if (player.getCurrentClan().getMember(player.getUsername().toLowerCase()) == null)
				player.getCurrentClan().addMember(player, 0);
			player.getCurrentClan().updateMembersList();
			player.sendMessage("Successfully joined the channel.");
			return;
				} else if (packetId == ITEM_ON_OBJECT_PACKET) {
			InventoryOptionsHandler.handleItemObject(player, stream);
		}else if (packetId == ITEM_DROP_PACKET) {
			if(!clicked) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			clicked = false;
			int interfaceId = stream.readUnsignedShort();
			
			if(player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = stream.readUnsignedShort();
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
				//hack, or server error or client error
				//player.getSession().getChannel().close();
				return;
			}
			int slotId = stream.readUnsignedShort128();
			int itemId = stream.readUnsignedShortLE();
			if(interfaceId == 149 && componentId == 0) {
				if(slotId >= 28 || player.getInterfaceManager().containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(slotId);
				if(item == null || item.getId() != itemId)
					return;
				if (player.getPetManager().spawnPet(itemId, true))
					return;
				if(item.getDefinitions().isLended()) {
					Lend lend;
					if ((lend = LendingManager.getLend(player)) != null) {
					player.getDialogueManager().startDialogue("DiscardItemOption", lend);
					return;
				}
			}
			if (player.getSkills().getTotalLevel() < 250) {
				player.sendMessage("You must have a total level of 250 to drop!");
				return;
			}
				if(item.getDefinitions().isDestroyItem()) {
					player.getDialogueManager().startDialogue("DestroyItemOption", slotId, item);
					return;
				}
				player.getInventory().deleteItem(slotId, item);
				World.addGroundItem(item, new WorldTile(player), player, false, 180, true);
			}
		} else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = System.currentTimeMillis();
			if (player.getStopDelay() > currentTime || player.getFreezeDelay() >= currentTime)
				return;
			int y = stream.readShortLE128();
			final int id = stream.readShort128();
			int x = stream.readShortLE128();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			// if(forceRun)
			// player.setRun(forceRun);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
					if (item == null)
						return;
					if (item.isSpawned()) {
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								GlobalItems.loadItemSpawns(item.getTile().getRegionId());
								this.stop();
							}
						}, item.getTick());
					}
					player.setNextFaceWorldTile(tile);
					player.addWalkSteps(tile.getX(), tile.getY(), 1);
					World.removeGroundItem(player, item);
					player.getPackets().sendSound(2582, 0);
				}
			}, 1, 1));
		} else if (packetId == SWITCH_BANK_ITEMS) {
			final int interfaceHash = stream.readInt();
			
			final int fromInterfaceId = interfaceHash >> 16;
			final int fromComponentId = interfaceHash & 0xFFFF;
			
			final int toSlot = stream.readShort();
			final int fromSlot = stream.readShort128();
			final int toInterfaceHash = stream.readIntV1();
			
			final int toInterfaceId = toInterfaceHash >> 16;
			final int toComponentId = toInterfaceHash & 0xFFFF;
			
			if (fromInterfaceId == 762)
				player.getBank().switchItem(fromSlot, toSlot < 0 ? 65535 : toSlot, fromComponentId, toComponentId);
		} else if (packetId == SWITCH_BANK_ITEMS || packetId == SWITCH_INTERFACE_ITEM_PACKET) {	
			if (!clicked)
				return;
			clicked = false;
			final int interfaceSet = stream.readInt();
			stream.skip(1);
			final int toSlot = stream.readShort128();
			final int fromSlot = stream.readShort128();
			final int interfaceId = interfaceSet >> 16;
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (interfaceId == 149) {
				if (toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (interfaceId == 762)
				player.getBank().switchItem(fromSlot, toSlot < 0 ? 65535 : toSlot, 0, 0);
		}else if(packetId == SWITCH_DETAIL) {
			int hash = stream.readInt();
			if(hash != 1057001181) {
				//hack, or server error or client error
				player.getSession().getChannel().close();
				return;
			}
			//done loading region when switch detail or mapregion
		}else if (packetId == DONE_LOADING_REGION) {
			if(!player.clientHasLoadedMapRegion()) {
				//load objects and items here
				player.setClientHasLoadedMapRegion();
			}
			GlobalItems.loadItemSpawns(player.getRegionId());
			player.refreshSpawnedObjects();
			player.refreshSpawnedItems();
		} else if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || player.isLocked())
				return;
			long currentTime = System.currentTimeMillis();
			if (player.getStopDelay() > currentTime)
				return;
			if (player.getFreezeDelay() >= currentTime) {
				player.getPackets().sendGameMessage("A magical force stops you from moving.");
				return;
			}
			if (player.getInterfaceManager().containsChatBoxInter())
				player.getInterfaceManager().closeChatBoxInterface();
			if (packetId == MINI_WALKING_PACKET)
				length -= 18;
			int coordY = stream.readUnsignedShortLE();
			stream.read128Byte();
			int coordX = stream.readUnsignedShort();
			int steps = (length - 5) / 2;

			if (steps > 25)
				steps = 25;
			player.stopAll(true);
			if (player.isResting()) {
				player.setNextAnimation(new Animation(5748));
				player.setResting(false);
				return;
			}
			/*if (player.isListeningToMusician()) {
				player.setNextAnimation(new Animation(5748));
				player.setListeningToMusician(false);
				return;
			}*/
			if (player.addWalkSteps(coordX, coordY))
				for (int step = 0; step < steps; step++)
					if (!player.addWalkSteps(coordX + stream.readByte(), coordY + stream.read128Byte()))
						break;
		}else if (packetId == WEIRD_WALKING_PACKET) {
			/*if(!player.hasStarted() || player.hasFinished())
				return;
			int coordX = stream.readShort();
			int coordY = stream.readShort();
			player.resetWalkSteps();
			player.addWalkSteps(coordX, coordY);*/
		}else if (packetId == PLAYER_OPTION_2_PACKET) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
		//	@SuppressWarnings("unused")
			//boolean unknown = stream.read128Byte() == 1;
			int playerIndex = stream.readShortLE();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
		}else if (packetId == ATTACK_PLAYER) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.read128Byte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis()
					|| !player.getControlerManager().canPlayerOption1(p2))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControlerManager().canAttack(p2))
				return;

			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.getPackets()
				.sendGameMessage(
						"You can only attack players in a player-vs-player area.");
				return;
			}
			if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != p2
						&& player.getAttackedByDelay() > Utils
						.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You are already in combat.");
					return;
				}
				if (p2.getAttackedBy() != player
						&& p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (p2.getAttackedBy() instanceof NPC) {
						p2.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage(
								"That player is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(p2));
		}else if (packetId == ATTACK_NPC) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			int npcIndex = stream.readUnsignedShortLE();
			@SuppressWarnings("unused")
			boolean unknown = stream.read128Byte() == 1;
			NPC npc = World.getNPCs().get(npcIndex);
			if(npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption())
				return;
			if (!player.getControlerManager().canAttack(npc)) {
				return;
			}
			if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc
							&& player.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player
							&& npc.getAttackedByDelay() > Utils
							.currentTimeMillis()) {
						player.getPackets().sendGameMessage(
								"This npc is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(npc));
			System.out.println(npc.getId());
		}else if (packetId == ITEM_ON_PLAYER) {
			final int itemId = stream.readShort128() & 0xFFFF;
			int playerIndex = stream.readUnsignedShort128();
			final Player usedOn = World.getPlayers().get(playerIndex);
			InventoryOptionsHandler.handleItemOnPlayer(player, usedOn, itemId);
		}else if (packetId == INTERFACE_ON_PLAYER) {
			if(!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if(player.getStopDelay() > System.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByteC() == 1;
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntV2();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if(Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if(!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if(componentId == 65535)
				componentId = -1;
			if(componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			int playerIndex = stream.readUnsignedShort128();
			Player p2 = World.getPlayers().get(playerIndex);
			if(p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll(false);
			switch (interfaceId) {
			case 662:
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2
								.getCoordFaceX(p2.getSize()), p2
								.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player
									&& p2.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(p2));
					}
					break;
				}
			case Inventory.INVENTORY_INTERFACE:// Item on player
			/*	if (!player.getControlerManager()
						.processItemOnPlayer(p2, junk2))
					return;*/
				InventoryOptionsHandler.handleItemOnPlayer(player, p2, junk2);
				break;
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2
								.getCoordFaceX(p2.getSize()), p2
								.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
							.sendGameMessage(
									"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								player.getPackets()
								.sendGameMessage(
										"That "
												+ (player
														.getAttackedBy() instanceof Player ? "player"
																: "npc")
																+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player
									&& p2.getAttackedByDelay() > Utils
									.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets()
									.sendGameMessage(
											"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager()
						.setAction(new PlayerCombat(p2));
					}
					break;
					case 79:
					if (player.isLocked() || p2.isLocked())
						return;
					if (!p2.acceptAid) {
						player.getPackets().sendGameMessage("That player does not accept aid.");
						return;
					}
					if (!Magic.checkSpellRequirements(player, 74, true, 566, 1, 563, 1, 557, 1))
						return;
					if (p2.getTemporaryAttributtes().get("Teleother") != null
							&& p2.getTemporaryAttributtes().get("Teleother").equals(true))
						return;
					player.faceEntity(p2);
					WorldTile location = new WorldTile(3222, 3218, 0);
					p2.getInterfaceManager().sendInterface(326);
					player.setNextAnimation(new Animation(1818));
					player.setNextGraphics(new Graphics(343));
					p2.getPackets().sendIComponentText(326, 1, player.getDisplayName());
					p2.getPackets().sendIComponentText(326, 3, "Lumbridge");
					p2.setCloseInterfacesEvent(new Runnable() {
						@Override
						public void run() {
							p2.getTemporaryAttributtes().remove("Teleother");
						}
					});
					p2.getTemporaryAttributtes().put("Teleother", location);
					if (player.spellbookSwap)
						player.getCombatDefinitions().setSpellBook(2);
					break;
				case 84:
					if (player.isLocked() || p2.isLocked())
						return;
					if (!p2.acceptAid) {
						player.getPackets().sendGameMessage("That player does not accept aid.");
						return;
					}
					if (!Magic.checkSpellRequirements(player, 82, true, 566, 1, 563, 1, 555, 1))
						return;
					if (p2.getTemporaryAttributtes().get("Teleother") != null
							&& p2.getTemporaryAttributtes().get("Teleother").equals(true))
						return;
					player.faceEntity(p2);
					WorldTile locat = new WorldTile(2962, 3381, 0);
					p2.getInterfaceManager().sendInterface(326);
					player.setNextAnimation(new Animation(1818));
					player.setNextGraphics(new Graphics(343));
					p2.getPackets().sendIComponentText(326, 1, player.getDisplayName());
					p2.getPackets().sendIComponentText(326, 3, "Falador");
					p2.setCloseInterfacesEvent(new Runnable() {
						@Override
						public void run() {
							p2.getTemporaryAttributtes().remove("Teleother");
						}
					});
					p2.getTemporaryAttributtes().put("Teleother", locat);
					if (player.spellbookSwap)
						player.getCombatDefinitions().setSpellBook(2);
					break;
				case 87:
					if (player.isLocked() || p2.isLocked())
						return;
					if (!p2.acceptAid) {
						player.getPackets().sendGameMessage("That player does not accept aid.");
						return;
					}
					if (!Magic.checkSpellRequirements(player, 90, true, 566, 2, 563, 1))
						return;
					if (p2.getTemporaryAttributtes().get("Teleother") != null
							&& p2.getTemporaryAttributtes().get("Teleother").equals(true))
						return;
					player.faceEntity(p2);
					WorldTile loc = new WorldTile(2757, 3478, 0);
					player.setNextAnimation(new Animation(1818));
					player.setNextGraphics(new Graphics(343));
					p2.getInterfaceManager().sendInterface(326);
					p2.setCloseInterfacesEvent(new Runnable() {
						@Override
						public void run() {
							p2.getTemporaryAttributtes().remove("Teleother");
						}
					});
					p2.getPackets().sendIComponentText(326, 1, player.getDisplayName());
					p2.getPackets().sendIComponentText(326, 3, "Camelot");
					p2.getTemporaryAttributtes().put("Teleother", loc);
					if (player.spellbookSwap)
						player.getCombatDefinitions().setSpellBook(2);
					break;
				}
				break;
			}
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getStopDelay() > System.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE();
			int npcIndex = stream.readUnsignedShort();
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte128() == 1;
			int interfaceHash = stream.readIntV2();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			if (npc.getId() != 6990 && !npc.getDefinitions().hasAttackOption()) {
				player.getPackets().sendGameMessage("You can't attack this npc.");
				return;
			}
			player.stopAll(false);
			Magic.processLunarOnNpc(player, componentId, npc);
			switch (interfaceId) {
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
					/*if (interfaceId == 747 || interfaceId == 662) {
						player.getPackets().sendGameMessage("componentId: " + componentId);
					}*/
					if (interfaceId == 747 && componentId == 14 || interfaceId == 662 && componentId == 65) {
						player.getFamiliar().setTarget(npc);
					}
					if(npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (npc.getId() != 6990 && !player.getFamiliar().canAttack(npc)) {
						player.getPackets()
						.sendGameMessage(
								"You can only use your familiar in a multi-zone area.");
						return;
					} else if (interfaceId == 747 && npc.getId() == 6990 && player.wolfWhistle == 5 || interfaceId == 662 && npc.getId() == 6990 && player.wolfWhistle == 5) {
						player.getInventory().deleteItem(12425, 1);
						player.getFamiliar().setNextAnimation(new Animation(8293));
						player.getFamiliar().setNextGraphics(new Graphics(1334));
						World.sendProjectile(player.getFamiliar(), npc, 1333, 34, 16, 30, 35, 16, 0);
						npc.setNextForceTalk(new ForceTalk("Whiiiine!"));
						player.wolfWhistle = 6;
						player.getPackets().sendConfig(1178, 32989);
						WolfWhistle.sendCompleted(player);
					} else if (interfaceId == 747 && componentId == 120 || interfaceId == 662 && componentId == 131 || componentId == 75) {
						player.getFamiliar().setSpecial(true);
						player.getFamiliar().setTarget(npc);
					//}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
								npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(npc));
						if (player.spellbookSwap)
							CoresManager.slowExecutor.schedule(new Runnable() {
								@Override
								public void run() {
									player.spellbookSwap = false;
									player.getCombatDefinitions().setSpellBook(2);

								}
							}, 1, TimeUnit.SECONDS);
					}
					break;
				}
				break;
			case 192:
				switch (componentId) {
				case 26:
				case 31:
				case 75:
				case 78:
				case 82:
				case 54:
				case 56:
				case 79:
				case 47:
				case 35:
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
								npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (componentId == 47)
							if (!npc.getName().contains("skeleton") && !npc.getName().contains("zombie")
									&& !npc.getName().contains("Skeleton") && !npc.getName().contains("Zombie")
									&& !npc.getName().contains("Ghost") && !npc.getName().contains("Shade")
									&& !npc.getName().contains("ghost") && !npc.getName().contains("shade")) {
								player.sendMessage("Crumble undead only affects undead monsters.");
								return;
							}

						if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(npc));
						if (player.spellbookSwap)
							CoresManager.slowExecutor.schedule(new Runnable() {
								@Override
								public void run() {
									player.spellbookSwap = false;
									player.getCombatDefinitions().setSpellBook(2);

								}
							}, 1, TimeUnit.SECONDS);

					}
					break;
				}
				break;
			}
		}else if (packetId == NPC_CLICK1_PACKET) {
			NPCHandler.handleOption1(player, stream);
		}else if (packetId == NPC_CLICK2_PACKET) {
			NPCHandler.handleOption2(player, stream);
		}else if (packetId == NPC_CLICK3_PACKET) {
			NPCHandler.handleOption3(player, stream);
		}else if (packetId == NPC_CLICK4_PACKET) {
			NPCHandler.handleOption4(player, stream);
		}else if(packetId == OBJECT_CLICK1_PACKET) {
			ObjectHandler.handleOption1(player, stream);
		}else if (packetId == OBJECT_CLICK2_PACKET) {
			ObjectHandler.handleOption2(player, stream);
		}else if (packetId == OBJECT_CLICK3_PACKET) {
			ObjectHandler.handleOption3(player, stream);
		}else if (packetId == OBJECT_CLICK4_PACKET) {
			ObjectHandler.handleOption4(player, stream);
		} else if (packetId == OBJECT_CLICK5_PACKET) {
			ObjectHandler.handleOption5(player, stream);
		}else if(packetId == ADD_FRIEND_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			player.getFriendsIgnores().addFriend(stream.readString());
		}else if(packetId == REMOVE_FRIEND_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			player.getFriendsIgnores().removeFriend(stream.readString());
		}else if(packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 30 minutes.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if(p2 == null)
				return;
			player.getFriendsIgnores().sendMessage(p2, Huffman.readEncryptedMessage(76, stream));
		}else if(packetId == PUBLIC_CHAT_PACKET) {
			if(!player.hasStarted() || player.hasFinished())
				return;
			int effects = stream.readUnsignedShort();
			String message = Huffman.readEncryptedMessage(76, stream);
			if(message.startsWith("::"))
				//if command exists and processed wont send message as public message
				if(Commands.processCommand(player, message.replace("::", ""), false, false))
					return;
			if(message.contains("0hdr2ufufl9ljlzlyla") || message.contains("0hdr"))
                return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You temporary muted. Recheck in 48 hours.");
				return;
			}
			for (String s : Settings.UNWANTED_WORDS) {
				if (message.contains(s)) {
				int length1 = s.length();
				String replace = "";
					for(int i = 0; i < length1; i++) {
						replace += "*";
					}
					message = message.replaceAll(s, replace);
				
				}
			}
			if (message.startsWith("/")) {
				if (player.isConnectedClanChannel()) {
					if (message.startsWith("/kick ")) {
						message.replace("/kick ", "");
						String[] cmd = message.toLowerCase().split(" ");
						String name = "";
						for (int i = 1; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						if (player.getCurrentClan().getMember(player.getDisplayName().toLowerCase()).getRank() < player
								.getCurrentClan().getMinimumKickRank().getOption()
								&& !player.getCurrentClan().getClanLeaderUsername()
										.equalsIgnoreCase(player.getDisplayName())) {
							player.sendMessage("You are not high enough rank to kick anyone on this clan channel.");
							return;
						}
						if (player.getCurrentClan().getClanLeaderUsername().equalsIgnoreCase(name)) {
							player.sendMessage("You cannot kick the clan leader.");
							return;
						}
						String fixedName = name.replaceAll(" ", "_").toLowerCase();
						if (player.getCurrentClan().getMember(fixedName) != null) {
							World.getPlayer(fixedName).getPackets().sendJoinClanChat(player.getCurrentClan(), false);
							World.getPlayer(fixedName).sendMessage("You have just been kicked from the clan "
									+ player.getCurrentClan().getClanName() + " by " + player.getDisplayName() + ".");
							player.getCurrentClan().kickMember(fixedName);
							Player play = null;
							for (ClanMember p : player.getCurrentClan().getMembers()) {
								play = World.getPlayer(p.getUsername());
								if (play == null || play.getCurrentClan() == null)
									continue;
								play.sendMessage("[<col=0000ff>"
										+ Utils.formatPlayerNameForDisplay(play.getCurrentClan().getClanName())
										+ "</col>] " + player.getDisplayName() + " kicked "
										+ Utils.formatPlayerNameForDisplay(name) + " from the clan.");
								play.getCurrentClan().updateMembersList();
							}
							World.getPlayer(fixedName).setCurrentClan(null);
						} else
							player.sendMessage("Could not find player " + Utils.formatPlayerNameForDisplay(name) + ".");
						return;
					} else if (message.startsWith("/ban ")) {
						message.replace("/ban ", "");
						String[] cmd = message.toLowerCase().split(" ");
						String name = "";
						for (int i = 1; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						if (!player.getCurrentClan().getClanLeaderUsername()
								.equalsIgnoreCase(player.getDisplayName())) {
							player.sendMessage("You are not high enough rank to ban anyone on this clan channel.");
							return;
						}
						if (player.getCurrentClan().getClanLeaderUsername().equalsIgnoreCase(name)) {
							player.sendMessage("You cannot ban the clan leader.");
							return;
						}
						String fixedName = name.replaceAll(" ", "_").toLowerCase();
						if (player.getCurrentClan().getMember(fixedName) != null) {
							World.getPlayer(fixedName).getPackets().sendJoinClanChat(player.getCurrentClan(), false);
							World.getPlayer(fixedName).sendMessage("You have just been banned from the clan "
									+ player.getCurrentClan().getClanName() + " by " + player.getDisplayName() + ".");
							player.getCurrentClan().kickMember(fixedName);
							player.getCurrentClan().ban(fixedName);
							Player play = null;
							for (ClanMember p : player.getCurrentClan().getMembers()) {
								play = World.getPlayer(p.getUsername());
								if (play == null || play.getCurrentClan() == null)
									continue;
								play.sendMessage("[<col=0000ff>"
										+ Utils.formatPlayerNameForDisplay(play.getCurrentClan().getClanName())
										+ "</col>] " + player.getDisplayName() + " banned "
										+ Utils.formatPlayerNameForDisplay(name) + " from the clan.");
								play.getCurrentClan().updateMembersList();
							}
							World.getPlayer(fixedName).setCurrentClan(null);
						} else
							player.sendMessage("Could not find player " + Utils.formatPlayerNameForDisplay(name) + ".");
						return;
					} else if (message.startsWith("/unban ")) {
						message.replace("/unban ", "");
						String[] cmd = message.toLowerCase().split(" ");
						String name = "";
						for (int i = 1; i < cmd.length; i++)
							name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
						if (!player.getCurrentClan().getClanLeaderUsername()
								.equalsIgnoreCase(player.getDisplayName())) {
							player.sendMessage("You are not high enough rank to unban anyone on this clan channel.");
							return;
						}
						String fixedName = name.replaceAll(" ", "_").toLowerCase();
						if (player.getCurrentClan().unban(fixedName)) {
							Player play = null;
							for (ClanMember p : player.getCurrentClan().getMembers()) {
								play = World.getPlayer(p.getUsername());
								if (play == null || play.getCurrentClan() == null)
									continue;
								play.sendMessage("[<col=0000ff>"
										+ Utils.formatPlayerNameForDisplay(play.getCurrentClan().getClanName())
										+ "</col>] " + player.getDisplayName() + " unbanned "
										+ Utils.formatPlayerNameForDisplay(name) + " from the clan.");
								play.getCurrentClan().updateMembersList();
							}
						} else
							player.sendMessage(name + " is not banned from this clan.");
						return;
					} else if (message.startsWith("/info")) {
						player.sendMessage("Current clan: "
								+ Utils.formatPlayerNameForDisplay(player.getCurrentClan().getClanName()) + ".");
						if (player.getCurrentClan().getMinimumEnterRank().equals(ClanRanks.ANYONE))
							player.sendMessage("Anyone can enter this clan.");
						else if (player.getCurrentClan().getMinimumEnterRank().equals(ClanRanks.ANY_FRIENDS))
							player.sendMessage("Only the friends of "
									+ Utils.formatPlayerNameForDisplay(player.getCurrentClan().getClanLeaderUsername())
									+ " can enter this clan.");
						else if (player.getCurrentClan().getMinimumEnterRank().equals(ClanRanks.ONLY_ME))
							player.sendMessage("Only " + player.getCurrentClan().getClanLeaderUsername()
									+ " can enter this clan.");
						else
							player.sendMessage(
									"Only " + player.getCurrentClan().getMinimumEnterRank().toString().toLowerCase()
											+ "+ can enter this clan.");
						if (player.getCurrentClan().getMinimumTalkRank().equals(ClanRanks.ANYONE))
							player.sendMessage("Anyone can talk on this clan.");
						else if (player.getCurrentClan().getMinimumTalkRank().equals(ClanRanks.ANY_FRIENDS))
							player.sendMessage("Only the friends of "
									+ Utils.formatPlayerNameForDisplay(player.getCurrentClan().getClanLeaderUsername())
									+ " can talk on this clan.");
						else if (player.getCurrentClan().getMinimumTalkRank().equals(ClanRanks.ONLY_ME))
							player.sendMessage("Only " + player.getCurrentClan().getClanLeaderUsername()
									+ " can talk on this clan.");
						else
							player.sendMessage(
									"Only " + player.getCurrentClan().getMinimumTalkRank().toString().toLowerCase()
											+ "+ can talk on this clan.");
						if (player.getCurrentClan().getMinimumKickRank().equals(ClanRanks.ONLY_ME))
							player.sendMessage("Only " + player.getCurrentClan().getClanLeaderUsername()
									+ " can kick on this clan.");
						else
							player.sendMessage(
									"Only " + player.getCurrentClan().getMinimumKickRank().toString().toLowerCase()
											+ "+ can kick on this clan.");

						if (player.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANYONE))
							player.sendMessage("No-one can share loot on this clan.");
						else if (player.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANY_FRIENDS))
							player.sendMessage("Only the friends of "
									+ Utils.formatPlayerNameForDisplay(player.getCurrentClan().getClanLeaderUsername())
									+ " can share loot on this clan.");
						else
							player.sendMessage(
									"Only " + player.getCurrentClan().getMinimumLootshareRank().toString().toLowerCase()
											+ "+ can share loot on this clan.");
						player.sendMessage(
								"Lootshare is currently " + (player.getCurrentClan().isLootsharing() ? "on." : "off."));
						player.sendMessage(
								"Coinshare is currently " + (player.getCurrentClan().isCoinsharing() ? "on." : "off."));
						if (player.getCurrentClan().getMembersOnlineAmount() > 1
								|| player.getCurrentClan().getMembersOnlineAmount() == 0)
							player.sendMessage("There are currently " + player.getCurrentClan().getMembersOnlineAmount()
									+ " members online on this clan.");
						else
							player.sendMessage("There is currently one member online on this clan.");
						return;
					}
					boolean friendship = false;
					if (player.getCurrentClan().getMinimumTalkRank().equals(ClanRanks.ANY_FRIENDS)) {
						if (World.getPlayer(player.getCurrentClan().getClanLeaderUsername()).getFriendsIgnores()
								.containsFriend(player.getUsername()))
							friendship = true;
					}
					if (!friendship)
						if (player.getCurrentClan().getMember(player.getUsername()).getRank() < player.getCurrentClan()
								.getMinimumTalkRank().getOption()) {
							player.sendMessage("You are not high enough rank to chat in this clan channel.");
							return;
						}
					String msg = message.replaceFirst("/", "");
					Player play = null;
					for (ClanMember member : player.getCurrentClan().getMembersOnline()) {
						play = World.getPlayerByDisplayName(member.getUsername().toLowerCase());
						if (play != null)
							play.getPackets().sendClanMessage(player, play.getCurrentClan().getClanName(), msg);
					}
				} else
					player.sendMessage("You are currently not in a clan channel.");
				return;
			}
			player.setNextPublicChatMessage(new PublicChatMessage(message, effects));
		}else if (packetId == QUICK_CHAT_PACKET) {
			//just tells you which client script created packet
			@SuppressWarnings("unused")
			boolean secondClientScript = stream.readByte() == 1;//script 5059 or 5061
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if(length > 3) {
				data = new byte[length-3];
				stream.readBytes(data);
			}
			if(fileId == 1)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.AGILITY)};
			else if (fileId == 8)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.ATTACK)};
			else if (fileId == 13)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.CONSTRUCTION)};
			else if (fileId == 16)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.COOKING)};
			else if (fileId == 23)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.CRAFTING)};
			else if (fileId == 30)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.DEFENCE)};
			else if (fileId == 34)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FARMING)};
			else if (fileId == 41)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FIREMAKING)};
			else if (fileId == 47)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FISHING)};
			else if (fileId == 55)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.FLETCHING)};
			else if (fileId == 62)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.HERBLORE)};
			else if (fileId == 70)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.HITPOINTS)};
			else if (fileId == 74)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.HUNTER)};
			else if (fileId == 135)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.MAGIC)};
			else if (fileId == 127)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.MINING)};
			else if (fileId == 120)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.PRAYER)};
			else if (fileId == 116)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.RANGE)};
			else if (fileId == 111)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.RUNECRAFTING)};
			else if (fileId == 103)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.SLAYER)};
			else if (fileId == 96)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.SMITHING)};
			else if (fileId == 92)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.STRENGTH)};
			else if (fileId == 85)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.SUMMONING)};
			else if (fileId == 79)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.THIEVING)};
			else if (fileId == 142)
				data = new byte[] {(byte) player.getSkills().getLevelForXp(Skills.WOODCUTTING)};
			player.setNextPublicChatMessage(new QuickChatMessage(fileId, data));
		}else if(packetId == COMMANDS_PACKET) {
			if(!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			Commands.processCommand(player, command, true, clientCommand);
			for (int IP : IGNORED_PACKETS) {
				if (packetId == IP)
					return;
			}
		}else if (player.getRights() == 2) {
			Logger.log(this, "Missing packet "+packetId+", expected size: "+length+", actual size: "+PACKET_SIZES[packetId]);
			player.sendMessage("Missing packet "+packetId+", expected size: "+length+", actual size: "+PACKET_SIZES[packetId]);
		}
	}

	public static boolean handleDoor(Player player, WorldObject object, long timer) {
		if (World.isSpawnedObject(object))
			return false;
		WorldObject openedDoor = new WorldObject(object.getId(),
				object.getType(), object.getRotation() + 1, object.getX(),
				object.getY(), object.getPlane());
		if (object.getRotation() == 0)
			openedDoor.moveLocation(-1, 0, 0);
		else if (object.getRotation() == 1)
			openedDoor.moveLocation(0, 1, 0);
		else if (object.getRotation() == 2)
			openedDoor.moveLocation(1, 0, 0);
		else if (object.getRotation() == 3)
			openedDoor.moveLocation(0, -1, 0);
		if (World.removeTemporaryObject(object, timer, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, timer, true);
			return true;
		}
		return false;
	}

	private static boolean handleDoor(Player player, WorldObject object) {
		return handleDoor(player, object, 60000);//60000
	}
	
	public Player getPlayer() {
		return player;
	}

	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		//player.stopAll(false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null
				|| !player.getInventory().addItem(item.getId(),
						item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
	/*	if (Runecrafting.isTiara(item.getId()))
			player.getPackets().sendConfig(491, 0);*/
		if (slotId == 3)
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
	}
	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendIComponentText(667, 28,
				"Stab: +" + player.getCombatDefinitions().getBonuses()[0]);
		player.getPackets().sendIComponentText(667, 29,
				"Slash: +" + player.getCombatDefinitions().getBonuses()[1]);
		player.getPackets().sendIComponentText(667, 30,
				"Crush: +" + player.getCombatDefinitions().getBonuses()[2]);
		player.getPackets().sendIComponentText(667, 31,
				"Magic: +" + player.getCombatDefinitions().getBonuses()[3]);
		player.getPackets().sendIComponentText(667, 32,
				"Range: +" + player.getCombatDefinitions().getBonuses()[4]);
		player.getPackets().sendIComponentText(667, 33,
				"Stab: +" + player.getCombatDefinitions().getBonuses()[5]);
		player.getPackets().sendIComponentText(667, 34,
				"Slash: +" + player.getCombatDefinitions().getBonuses()[6]);
		player.getPackets().sendIComponentText(667, 35,
				"Crush: +" + player.getCombatDefinitions().getBonuses()[7]);
		player.getPackets().sendIComponentText(667, 36,
				"Magic: +" + player.getCombatDefinitions().getBonuses()[8]);
		player.getPackets().sendIComponentText(667, 37,
				"Range: +" + player.getCombatDefinitions().getBonuses()[9]);
		player.getPackets().sendIComponentText(667, 38,
				"Summoning: +" + player.getCombatDefinitions().getBonuses()[10]);
		player.getPackets().sendIComponentText(667, 39, 
				"Absorb Melee: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 40,
				"Absorb Magic: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 41,
				"Absorb Ranged: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS]+ "%");
		player.getPackets().sendIComponentText(667, 42,
				"Strength: " + player.getCombatDefinitions().getBonuses()[14]);
		player.getPackets().sendIComponentText(667, 43,
				"Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
		player.getPackets().sendIComponentText(667, 44,
				"Prayer: +" + player.getCombatDefinitions().getBonuses()[16]);
		player.getPackets().sendIComponentText(667,45,"Magic Damage: +" + player.getCombatDefinitions().getBonuses()[17] + "%");
	}
	static Item[] COMMON = { new Item(995, 3000), new Item(995, 1000),
		new Item(995, 459), new Item(995, 354), new Item(995, 1500),
		new Item(995, 100), new Item(995, 1), new Item(995, 900),
		new Item(995, 2445), new Item(995, 2350), new Item(1454),
		new Item(1623) };
static Item[] UNCOMMON = { new Item(1621), new Item(1619), new Item(1452) };
static Item[] RARE = { new Item(1617), new Item(985), new Item(987),
		new Item(1462) };

private static Item getCommon() {
	return COMMON[(int) (Math.random() * COMMON.length)];
}

private static Item getUnCommon() {
	return UNCOMMON[(int) (Math.random() * UNCOMMON.length)];
}

private static Item getRare() {
	return RARE[(int) (Math.random() * RARE.length)];
}

private static Item getRandom() {
	int common = Utils.random(5, 7);
	int uncommon = Utils.random(5, 50);
	int rare = Utils.random(0, 100);
	if (common == 5 || common == 7) {
		return getCommon();
	} else if (common != 5 && common != 7 && uncommon == 5
			|| uncommon == 50) {
		return getUnCommon();
	} else if (common != 5 && common != 7 && uncommon != 50
			&& uncommon != 5 && rare == 0 || rare == 100) {
		return getRare();
	} else {
		return getCommon();
	}
}


public static void openItemsKeptOnDeath(Player player) {
	player.getInterfaceManager().sendInterface(102);
	sendItemsKeptOnDeath(player, player.isAtWild() ? true : false);
}

/*public static boolean sendWear(Player player, int slotId, int itemId) {
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
		// player.getCharges().wear(targetSlot);
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
	}*/

public static Integer[][] getItemSlotsKeptOnDeath(final Player player,
		boolean atWilderness, boolean skulled, boolean protectPrayer) {
	ArrayList<Integer> droppedItems = new ArrayList<Integer>();
	ArrayList<Integer> protectedItems = new ArrayList<Integer>();
	ArrayList<Integer> lostItems = new ArrayList<Integer>();
	//boolean inRiskArea = FfaZone.inRiskArea(player);
	for (int i = 1; i < 44; i++) {
		Item item = i >= 16 ? player.getInventory().getItem(i - 16)
				: player.getEquipment().getItem(i - 1);
		if (item == null)
			continue;
		int stageOnDeath = item.getDefinitions().getStageOnDeath();
		if (ItemConstants.keptOnDeath(item) && atWilderness)
			protectedItems.add(i);
		else if (!atWilderness && stageOnDeath == 1)
			protectedItems.add(i);
		else if (stageOnDeath == -1)
			lostItems.add(i);
		else
			droppedItems.add(i);
	}
	int keptAmount = (player.hasSkull() /*|| inRiskArea*/) ? 0 : 3;
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

public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
	boolean skulled = player.hasSkull();
//	boolean inFfa = FfaZone.inArea(player);
	@SuppressWarnings("unused")
	// TODO
	//boolean inRiskArea = FfaZone.inRiskArea(player);
	Integer[][] slots = getItemSlotsKeptOnDeath(player, wilderness,
			skulled, player.getPrayer().usingPrayer(0, 10)
					|| player.getPrayer().usingPrayer(1, 0));
	Item[][] items = getItemsKeptOnDeath(player, slots);
	long riskedWealth = 0;
	long carriedWealth = 0;
	for (Item item : items[1]) {
		if (item == null)
			continue;
		carriedWealth = riskedWealth += GrandExchange
				.getPrice(item.getId()) * item.getAmount();
	}
	for (Item item : items[0]) {
		if (item == null)
			continue;
		carriedWealth += GrandExchange.getPrice(item.getId())
				* item.getAmount();
	}
	/*if (slots[0].length > 0) {
		for (int i = 0; i < slots[0].length; i++)
			player.getVarsManager().sendVarBit(9222 + i, slots[0][i]);
		player.getVarsManager().sendVarBit(9227, slots[0].length);
	} else {
		player.getVarsManager().sendVarBit(9222, -1);
		player.getVarsManager().sendVarBit(9227, 1);
	}
	player.getVarsManager().sendVarBit(9226, (wilderness || inFfa) ? 1 : 0);
	if (!inFfa)
		player.getVarsManager().sendVarBit(9229, player.hasSkull() ? 1 : 0);*/
	StringBuffer text = new StringBuffer();
	text.append("Items kept on death:").append("<br><br>");
	for (Item item : items[0]) {
		text.append(item.getName()).append("<br>").append("<br>");
	}
	text.append("<br>")
			.append("<br>")
			.append("Carried wealth:")
			.append("<br>")
			.append(carriedWealth > Integer.MAX_VALUE ? "Too high!" : Utils
					.getFormattedNumber((int) carriedWealth, ','))
			.append("<br>")
			.append("<br>")
			.append("Risked wealth:")
			.append("<br>")
			.append(riskedWealth > Integer.MAX_VALUE ? "Too high!" : Utils
					.getFormattedNumber((int) riskedWealth, ','))
			.append("<br>").append("<br>");
	text.append("Respawn point:").append("<br>").append("Edgeville bank.");
	player.getPackets().sendGlobalString(352, text.toString());
}

}
