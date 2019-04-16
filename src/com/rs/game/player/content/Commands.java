package com.rs.game.player.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Equipment;
import com.rs.game.player.LendingManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.quests.CooksAssistant;
import com.rs.game.player.content.quests.DoricsQuest;
import com.rs.game.player.skills.Dungeonnering;
import com.rs.game.player.skills.summoning.Summoning;
import com.rs.game.player.skills.summoning.Summoning.Pouches;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Encrypt;
import com.rs.utils.IPBanL;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.game.player.cutscenes.WolfWhistle1;

/*
 * doesnt let it be extended
 */
public final class Commands {

	/*
	 * all console commands only for admin, chat commands processed if they not
	 * processed by console
	 */

	// String computerName =
	// InetAddress.getByName(player.getSession().getIP()).getHostName();
	public static final int[] unspawnables = { 995, 1050, 1051 };
	public static final String[] unspawnablesNames = { "torva", "chaotic", "partyhat", "h'ween mask" };

	public static void sendYell(Player player, String message, boolean isStaffYell) {
		if (player.getMuted() > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
			return;
		}
		if (player.getRights() < 2) {
			String[] invalid = { "<euro", "<img", "<img=", "<col", "<col=", "<shad", "<shad=", "<str>", "<u>" };
			for (String s : invalid)
				if (message.contains(s)) {
					player.getPackets().sendGameMessage("You cannot add additional code to the message.");
					return;
				}
		}
		for (Player players : World.getPlayers()) {
			if (players == null || !players.isRunning())
				continue;
			if (isStaffYell) {
				if (players.getRights() > 0 || players.getUsername().equalsIgnoreCase("jens"))
					players.getPackets()
							.sendGameMessage("<col=ff0000>[Staff Yell]</col> "
									+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ": " + message + ".",
							true);
				return;
			}
			if (player.getRights() == 2 && player.getUsername().equalsIgnoreCase("jens")) {
				players.getPackets().sendGameMessage("<col=ff00db>[Developer] <img=1>" + player.getDisplayName()
						+ ": <col=ff00db>" + message + "</col>");
			} else if (player.getUsername().equalsIgnoreCase("venom")) {
				players.getPackets().sendGameMessage("<col=FF7F00><img=5>[Web Developer]" + player.getDisplayName()
						+ ": <col=FF7F00>" + message + "</col>");
			} else if (player.getRights() == 2) {
				players.getPackets().sendGameMessage("[<col=DB0000>Administrator</col>] <img=1>"
						+ player.getDisplayName() + ": <col=DB0000>" + message + "</col>");
			} else if (player.getRights() == 1) {

				players.getPackets().sendGameMessage("[<col=347235>Moderator</col>] <img=0>" + player.getDisplayName()
						+ ": <col=347235>" + message + "</col>");
			} else if (player.isSupporter()) {
				players.getPackets().sendGameMessage("[<col=347235>Supporter</col>]<img=12>" + player.getDisplayName()
						+ ": <col=347235>" + message + "</col>");
			} else if (player.isDonator()) {
				players.getPackets().sendGameMessage("[<col=db0000>Donator</col>] " + player.getDisplayName()
						+ ": <col=db0000>" + message + "</col>");
			} else if (player.isExtremeDonator()) {
				players.getPackets().sendGameMessage("[<col=20f2eb>Extreme Donator</col>] <img=11>"
						+ player.getDisplayName() + ": <col=20f2eb>" + message + "</col>");
			} else if (player.getRights() == 0) {
				players.getPackets().sendGameMessage(
						"[Player]" + player.getDisplayName() + ": <col=ff0000>" + message + "</col>");
			} else if (player.getRights() == 10) {
				players.getPackets().sendGameMessage("[<col=FF6600>Co-Owner</col>]<img=10>" + player.getDisplayName()
						+ ": <col=FF6600>" + message + "</col>");
			} else if (player.isSupporter()) {
				players.getPackets().sendGameMessage("[<col=347235>Supporter</col>]<img=12>" + player.getDisplayName()
						+ ": <col=347235>" + message + "</col>");
			} else if (player.isDonator()) {
				players.getPackets().sendGameMessage("[<col=00ff00>Donator</col>] <img=8>" + player.getDisplayName()
						+ ": <col=00ff00>" + message + "</col>");
			} else if (player.isExtremeDonator()) {
				players.getPackets().sendGameMessage("[<col=20f2eb>Extreme Donator</col>] <img=11>"
						+ player.getDisplayName() + ": <col=20f2eb>" + message + "</col>");
			}
		}
	}

	/*
	 * returns if command was processed
	 */
	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0) // if they used ::(nothing) theres no command
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (player.getRights() >= 2 && processAdminCommand(player, cmd, console, clientCommand)) {
			archiveLogs(player, cmd);
			return true;
		} if (player.getRights() >= 1 && processModCommand(player, cmd, console, clientCommand)) {
			archiveLogs(player, cmd);
			return true;
		} if (Settings.ECONOMY) {
			player.getPackets().sendGameMessage("Commands are set to off");
			return true;
		}
		return processNormalCommand(player, cmd, console, clientCommand);
	}

	/*
	 * extra parameters if you want to check them
	 */
	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console,
			boolean clientCommand) {
		if (clientCommand) {
			switch (cmd[0]) {
			case "tryshake":
				player.getPackets().sendCameraShake(3, 12, 25, 12, 25);
				return true;
			case "tele":
				try {
					cmd = cmd[1].split(",");
					int plane = Integer.valueOf(cmd[0]);
					int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
					int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
					if (player.getDisplayName().equalsIgnoreCase("sagacity") || player.getDisplayName().equalsIgnoreCase("mike"))
						player.setNextWorldTile(new WorldTile(x, y, plane));
				} catch (Exception e) {

				}
				return true;
			}
		} else {
			if (cmd[0].equals("update") && (player.getUsername().equalsIgnoreCase("sagacity"))) {
				int delay = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPainelBoxMessage("Use: ::update secondsDelay(IntegerValue)");
						return true;
					}
				}
				LendingManager.process();
				GrandExchange.save();
				World.safeShutdown(false, delay);
			} else if (cmd[0].equals("ww1")) {
				//player.useStairs(828, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), 1, 2);
				World.spawnNPC(6990, new WorldTile(2921, 3444, 1), -1, false, "SOUTH");
				player.getCutscenesManager().play(new WolfWhistle1());
				return true;
			} else if (cmd[0].equals("mapfiles")) {
				int regionId = player.getRegionId();
				int regionX = (regionId >> 8) * 64;
				int regionY = (regionId & 0xff) * 64;
				int mapArchiveId = Cache.getCacheFileManagers()[5].getContainerId("m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
				int landscapeArchiveId = Cache.getCacheFileManagers()[5].getContainerId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
				
                System.out.println("RegionId: "+cmd[1]);
        		System.out.println("landArchive: "+landscapeArchiveId);
        		System.out.println("mapArchive: "+mapArchiveId);
       			 return false;
			} else if (cmd[0].equals("restart")) {
				int delay = 60;
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPainelBoxMessage("Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
					;
				}
				LendingManager.process();
				GrandExchange.save();
				World.safeShutdown(true, delay);
			} else if (cmd[0].equals("npc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::npc npcId");
					return true;
				}
				try {
					World.spawnNPC(Integer.valueOf(cmd[1]), new WorldTile(player), -1, true);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::npc npcId");
					return true;
				}

			} else if (cmd[0].equals("dialogue")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::spawnnpc npcId");
					return true;
				}
				try {
					player.getDialogueManager().startDialogue("Integer.valueOf(cmd[1])");
					// World.spawnNPC(Integer.valueOf(cmd[1]), new
					// WorldTile(player), -1, true);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::spawnnpc npcId");
					return true;
				}
			} else if (cmd[0].equals("getid")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::hairc npcId");
					return true;
				}
				try {
					String itemName = "";
					for (int i = 1; i < cmd.length; i++)
						itemName += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					ItemDefinitions.getItemID(player, itemName);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::hairc npcId");
					return true;
				}
			} else if (cmd[0].equals("removecannon")) {
				player.setHasDwarfCannon(false);
			} else if (cmd[0].equals("cookstest")) {
				CooksAssistant.sendCompleted(player);
			} else if (cmd[0].equals("doricstest")) {
				DoricsQuest.sendCompleted(player);
			} else if (cmd[0].equals("grim")) {
				// GrimRepear.spawnDeath(player);
				final NPC index = new NPC(2862, new WorldTile(player.getX() + 1, player.getY() + 1, 0), -1, false);
				return true;
			} else if (cmd[0].equals("hairc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::hairc npcId");
					return true;
				}
				try {
					player.getAppearence().setHairColor(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::hairc npcId");
					return true;
				}
			} else if (cmd[0].equals("beardc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::hairc npcId");
					return true;
				}
				try {//
					player.getAppearence().setFacialHair(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::hairc npcId");
					return true;
				}
			} else if (cmd[0].equals("summon")) {
				return true;
			} else if (cmd[0].equals("gehistory")) {
				player.getGEManager().openHistory();
				return true;

			} else if (cmd[0].equals("pouch")) {
				Summoning.spawnFamiliar(player, Pouches.PACK_YAK);
				return true;
			} else if (cmd[0].equals("work")) {
				// player.getPackets().sendConfig(168, 8);// tab id
				player.getInterfaceManager().sendInventoryInterface(662);
				return true;
			} else if (cmd[0].equals("testpvp")) {
				player.getPackets().sendHideIComponent(745, 6, true);
				player.getPackets().sendHideIComponent(745, 3, false);
				// player.getPackets().sendHideIComponent(745, 4, true);
				// player.getPackets().sendHideIComponent(745, 2, true);
				// player.getPackets().sendHideIComponent(745, 1, false);
				return true;
			} else if (cmd[0].equals("checkip")) {
				if (cmd.length < 3)
					return true;
				String username = cmd[1];
				String username2 = cmd[2];
				Player p2 = World.getPlayerByDisplayName(username);
				Player p3 = World.getPlayerByDisplayName(username2);
				boolean same = false;
				if (p3.getSession().getIP().equalsIgnoreCase(p2.getSession().getIP())) {
					same = true;
				} else {
					same = false;
				}
				player.getPackets().sendGameMessage("They have the same IP : " + same);
				return true;
			} else if (cmd[0].equals("bosses")) {
				player.getDialogueManager().startDialogue("TeleportBosses");
				 //Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3114,5528, 0));
			} else if (cmd[0].equals("bork")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3114, 5528, 0));
			} else if (cmd[0].equals("karamja")) {
				player.getDialogueManager().startDialogue("KaramjaTrip",
						Utils.getRandom(1) == 0 ? 11701 : (Utils.getRandom(1) == 0 ? 11702 : 11703));
				return true;
			} else if (cmd[0].equals("minigames")) {
				player.getDialogueManager().startDialogue("TeleportMinigame");
			} else if (cmd[0].equals("getip")) {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p = World.getPlayerByDisplayName(name);
				if (p == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else
					player.getPackets()
							.sendGameMessage("" + p.getDisplayName() + "'s IP is " + p.getSession().getIP() + ".");
				return true;
			} else if (cmd[0].equals("getip")) {
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p = World.getPlayerByDisplayName(name);
				if (p == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				} else
					player.getPackets()
							.sendGameMessage("" + p.getDisplayName() + "'s IP is " + p.getSession().getIP() + ".");
				return true;
			} else if (cmd[0].equals("spawnplayer") && (player.getUsername().equalsIgnoreCase("sagacity"))) {
				Player other = new Player("troll");
				other.init(player.getSession(), "fagit", 0, 0, 0);
				other.setNextWorldTile(player);
				// other.getControlerManager().startControler("Wilderness");
				// other.setNextWorldTile(new WorldTile(2751, 4927, 1));
				return true;
			} else if (cmd[0].equals("wildon")) {
				player.getControlerManager().startControler("Wilderness");
			} else if (cmd[0].equals("newhome")) {
				player.getControlerManager().startControler("NewHomeControler");
			} else if (cmd[0].equals("setlevel")) {
				if (cmd.length < 3) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage("Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, Skills.getXPForLevel(level));
					player.getAppearence().generateAppearenceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
			} else if (cmd[0].equals("topc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::topc npcId");
					return true;
				}
				try {
					player.getAppearence().setTopColor(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::topc npcId");
					return true;
				}
			} else if (cmd[0].equals("legsc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::legsc npcId");
					return true;
				}
				try {
					player.getAppearence().setLegsColor(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::legsc npcId");
					return true;
				}
			} else if (cmd[0].equals("beard")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				try {
					player.getAppearence().setBeardStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
			} else if (cmd[0].equals("tops")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				try {
					player.getAppearence().setTopStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				} // ShopsHandler.openShop(player, 12);
			} else if (cmd[0].equals("shop")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::shop Id");
					return true;
				}
				try {
					ShopsHandler.openShop(player, Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::shop Id");
					return true;
				}
				if (cmd[0].equalsIgnoreCase("permdonator")) {
					String name = "";
					for (int i = 1; i < cmd.length; i++)
						name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					Player target = World.getPlayerByDisplayName(name);
					boolean loggedIn = true;
					if (target == null) {
						target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
						if (target != null)
							target.setUsername(Utils.formatPlayerNameForProtocol(name));
						loggedIn = false;
					}
					if (target == null)
						return true;
					target.getInventory().addItem(15098, 1);
					target.setDonator(true);

					SerializableFilesManager.savePlayer(target);

					if (loggedIn)
						target.getPackets().sendGameMessage(
								"You have been given donator by " + Utils.formatPlayerNameForDisplay(player.getUsername()),
								true);
					player.getPackets().sendGameMessage(
							"You gave donator to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
					return true;
				}
				if (cmd[0].equalsIgnoreCase("permdonator")) {
					String name = "";
					for (int i = 1; i < cmd.length; i++)
						name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					Player target = World.getPlayerByDisplayName(name);
					boolean loggedIn = true;
					if (target == null) {
						target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
						if (target != null)
							target.setUsername(Utils.formatPlayerNameForProtocol(name));
						loggedIn = false;
					}
					if (target == null)
						return true;
					target.getInventory().addItem(15098, 1);
					target.setDonator(true);

					SerializableFilesManager.savePlayer(target);

					if (loggedIn)
						target.getPackets().sendGameMessage(
								"You have been given donator by " + Utils.formatPlayerNameForDisplay(player.getUsername()),
								true);
					player.getPackets().sendGameMessage(
							"You gave donator to " + Utils.formatPlayerNameForDisplay(target.getUsername()), true);
					return true;
				}
			} else if (cmd[0].equals("mute")) {
				if (player.getUsername().equalsIgnoreCase("sagacity") || player.getUsername().equalsIgnoreCase("venom")) {
				String name;
				Player target;
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					target.getPackets()
							.sendGameMessage("You've been muted for "
									+ (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
									+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
									+ target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setMuted(Utils.currentTimeMillis()
							+ (player.getRights() >= 1 ? (48 * 60 * 60 * 1000) : (1 * 60 * 60 * 1000)));
					player.getPackets().sendGameMessage(
							"You have muted " + (player.getRights() >= 1 ? " 48 hours by " : "1 hour by ")
									+ target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				}
				}
			} else if (cmd[0].equals("unmuteall")) {
				for (Player targets : World.getPlayers()) {
					if (player == null)
						continue;
					targets.setMuted(0);
				}
			} else if (cmd[0].equalsIgnoreCase("obtest")) {
				// player.getCutscenesManager().play(new HomeCutScene());
				player.getPackets().sendObjectAnimation(new WorldObject(45078, 0, 3, 3651, 5123, 0),
						new Animation(12220));
			} else if (cmd[0].equals("unpermban")) {
				String name;
				Player target;
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				target.setPermBanned(false);
				target.setBanned(0);
				player.getPackets().sendGameMessage(
						"You've unbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (cmd[0].equals("permban")) {
				if (!player.getUsername().equalsIgnoreCase("sagacity")) {
					player.getPackets().sendGameMessage("Only sagacity can permban");
					return true;
				}
				String name;
				Player target;
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					target.setPermBanned(true);
					target.getPackets().sendGameMessage("You've been perm banned by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have perm banned: " + target.getDisplayName() + ".");
					target.getSession().getChannel().close();
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					if (target.getRights() == 2)
						return true;
					target.setPermBanned(true);
					player.getPackets()
							.sendGameMessage("You have perm banned: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc11);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if (cmd[0].equals("ipban")) {
				String name;
				Player target;
				if (!player.getUsername().equalsIgnoreCase("sagacity")) {
					player.getPackets().sendGameMessage("Only sagacity can ipban");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				boolean loggedIn11111 = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name));
					loggedIn11111 = false;
				}
				if (target != null) {
					if (target.getRights() == 2)
						return true;
					IPBanL.ban(target, loggedIn11111);
					player.getPackets().sendGameMessage(
							"You've permanently ipbanned " + (loggedIn11111 ? target.getDisplayName() : name) + ".");
				} else {
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				}
			} else if (cmd[0].equals("unipban")) {
				String name;
				Player target;
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				File acc11 = new File("data/characters/" + name.replace(" ", "_") + ".p");
				target = null;
				if (target == null) {
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc11);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				IPBanL.unban(target);
				player.getPackets().sendGameMessage(
						"You've unipbanned " + Utils.formatPlayerNameForDisplay(target.getUsername()) + ".");
				try {
					SerializableFilesManager.storeSerializableClass(target, acc11);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (cmd[0].equals("shoptest")) {
				ShopsHandler.openShop(player, 12);
				return true;
			} else if (cmd[0].equals("pray")) {
				final int maxPrayer = player.getSkills().getLevelForXp(Skills.PRAYER);
				player.getSkills().restorePrayer(maxPrayer);
				player.getPackets().sendGameMessage("Prayer restored.");
				return true;
			} else if (cmd[0].equals("legss")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				try {
					player.getAppearence().setLegsStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
			} else if (cmd[0].equals("hairs")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				try {
					player.getAppearence().setHairStyle(Integer.valueOf(cmd[1]));
					player.getAppearence().generateAppearenceData();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
			} else if (cmd[0].equals("hit")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::Beard npcId");
					return true;
				}
				try {
					/*
					 * player.setNextForceTalk(new ForceTalk(
					 * "<col=ff0000>I'VE TAKEN " + Integer.valueOf(cmd[1]) +
					 * " OUT OF MY HP." ));
					 */
					player.applyHit(new Hit(player, Integer.valueOf(cmd[1]), HitLook.REGULAR_DAMAGE));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::hit amount");
					return true;
				}
			} else if (cmd[0].equals("spec")) {
				for (int i = 0; i < 10; i++)
					player.getCombatDefinitions().restoreSpecialAttack();
			} else if (cmd[0].equals("internpc")) {
				if (cmd.length < 4) {
					player.getPackets().sendPainelBoxMessage("Use: ::internpc interfaceId componentId npcId");
					return true;
				}
				try {
					int interfaceId = Integer.valueOf(cmd[1]);
					int componentId = Integer.valueOf(cmd[2]);
					int npcId = Integer.valueOf(cmd[3]);
					player.getPackets().sendNPCOnIComponent(interfaceId, componentId, npcId);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::internpc interfaceId componentId npcId");
				}
			} else if (cmd[0].equals("inter")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
				}
			} else if (cmd[0].equals("inters")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
				}
			} else if (cmd[0].equals("interh")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						// player.getPackets().sendIComponentModel(interId,
						// componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
				}
			} else if (cmd[0].equals("hidec")) {
				if (cmd.length < 4) {
					player.getPackets().sendPainelBoxMessage("Use: ::hidec interfaceid componentId hidden");
					return true;
				}
				try {
					player.getPackets().sendHideIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							Boolean.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::hidec interfaceid componentId hidden");
				}
			} else if (cmd[0].equals("music")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::music musicid");
					return true;
				}
				try {
					player.getPackets().sendMusic(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::music musicid");
				}
				} else if (cmd[0].equals("sound")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::sound soundid");
					return true;
				}
				try {
					player.getPackets().sendSound(Integer.valueOf(cmd[1]), 3);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::sound soundid");
				}
			} else if (cmd[0].equals("godwars")) {
				player.getControlerManager().startControler("GodWars");
				// ExchangeHandler.mainInterface(player)
				return true;
			} else if (cmd[0].equals("duel")) {
				player.getControlerManager().startControler("DuelControler");
				return true;
			} else if (cmd[0].equals("ancients")) {
				player.getDialogueManager().startDialogue("AncientAltar");
				return true;
			} else if (cmd[0].equals("homeg")) {
				player.getDialogueManager().startDialogue("NewHomeGuide");
				return true;
			} else if (cmd[0].equals("lunar")) {
				player.getDialogueManager().startDialogue("LunarAltar");
				return true;
			} else if (cmd[0].equals("house")) {
				player.getControlerManager().startControler("HouseControler");
				return true;
			} else if (cmd[0].equals("dungalone")) {
				Dungeonnering.startDungeon(Dungeonnering.SMALL_ROOM, 1, 1, player);
			} else if (cmd[0].equals("har")) {
				player.getInterfaceManager().sendInterface(204);
				player.getAppearence().setSkinColor(4);
			} else if (cmd[0].equals("music2")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::music id category");
					return true;
				}
				try {
					player.getPackets().sendMusic2(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::music id category");
				}
			} else if (cmd[0].equals("tab")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::tab tabid interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendTab(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::tab tabid interfaceId");
				}
			} else if (cmd[0].equals("tele")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]),
							cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::tele coordX coordY plane");
				}
			} else if (cmd[0].equals("tonpc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
			} else if (cmd[0].equals("spawnobject")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::spawnobject id");
					return true;
				}
				try {
					int objectId = Integer.valueOf(cmd[1]);
					for (int regionId : player.getMapRegionsIds()) {
						CopyOnWriteArrayList<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes == null)
							continue;
						for (Integer playerIndex : playersIndexes) {
							Player p2 = World.getPlayers().get(playerIndex);
							if (p2 == null || !p2.hasStarted() || p2.hasFinished() || !p2.withinDistance(p2))
								continue;
							p2.getPackets().sendSpawnedObject(
									new WorldObject(objectId, 10, 0, player.getX(), player.getY(), player.getPlane()));
						}
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::spawnobject id");
				}

				player.getAppearence().setRenderEmote(-1);
			} else if (cmd[0].equals("remote")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::remote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::remote id");
				}
			} else if (cmd[0].equals("trade")) {
				String name;
				Player target;
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					player.getTrade().openTrade(target);
					target.getTrade().openTrade(player);
				}
				return true;
			} else if (cmd[0].equals("teletome")) {
				String name;
				Player target;
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else
					target.setNextWorldTile(player);
				return true;
			} else if (cmd[0].equals("gfxtile")) {
				if (cmd.length < 5) {
					player.getPackets().sendPainelBoxMessage("Use: ::gfxtile id x y plane");
					return true;
				}
				try {
					player.getPackets().sendGraphics(new Graphics(Integer.valueOf(cmd[1])),
							new WorldTile(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]), Integer.valueOf(cmd[4])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::gfxtile id x y plane");
				}
			} else if (cmd[0].equals("spellbook")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::spellbook id");
					return true;
				}
				try {
					player.getCombatDefinitions().setSpellBook(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::spellbook id");
				}
			} else if (cmd[0].equals("item")) {
				if (player.getUsername().equalsIgnoreCase("sagacity") || player.getUsername().equalsIgnoreCase("mike")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::item id (optional:amount)");
					return true;
				}
				try {
					player.getInventory().addItem(Integer.valueOf(cmd[1]),
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::item id (optional:amount)");
				}
				if (cmd[0].equalsIgnoreCase("copy")) {

					String username = "";
					for (int i = 1; i < cmd.length; i++)
						username += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					Player p2 = World.getPlayerByDisplayName(username);
					if (p2 == null) {
						player.getPackets().sendGameMessage("Couldn't find player " + username + ".");
						return true;
					}
					if (!player.getEquipment().wearingArmour()) {
						player.getPackets().sendGameMessage("Please remove your armour first.");
						return true;
					}
					Item[] items = p2.getEquipment().getItems().getItemsCopy();
					for (int i = 0; i < items.length; i++) {
						if (items[i] == null)
							continue;
						HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
						boolean hasRequiriments = true;
						if (requiriments != null) {
							for (int skillId : requiriments.keySet()) {
								if (skillId > 24 || skillId < 0)
									continue;
								int level = requiriments.get(skillId);
								if (level < 0 || level > 120)
									continue;
								if (player.getSkills().getLevelForXp(skillId) < level) {
									if (hasRequiriments)
										player.getPackets()
												.sendGameMessage("You are not high enough level to use this item.");
									hasRequiriments = false;
									String name = Skills.SKILL_NAME[skillId].toLowerCase();
									player.getPackets().sendGameMessage("You need to have a"
											+ (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
								}

							}
						}
						if (!hasRequiriments)
							return true;
						player.getEquipment().getItems().set(i, items[i]);
						player.getEquipment().refresh(i);
					}
					player.getAppearence().generateAppearenceData();
					return true;
				}
				
			} else if (cmd[0].equals("itemn")) {
				if (!player.getUsername().equalsIgnoreCase("sagacity") || !player.getUsername().equalsIgnoreCase("mike")) {
					if (!player.canSpawn()) {
						player.getPackets().sendPainelBoxMessage("You cannot spawn items by name.");
						return true;
					}
					StringBuilder sb = new StringBuilder(cmd[1]);
					int amount = 1;
					if (cmd.length > 2) {
						for (int i = 2; i < cmd.length; i++) {
							if (cmd[i].startsWith("+")) {
								amount = Integer.parseInt(cmd[i].replace("+", ""));
							} else {
								sb.append(" ").append(cmd[i]);
							}
						}
					}
					String name = sb.toString().toLowerCase().replace("[", "(").replace("]", ")").replaceAll(",", "'");
					for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
						ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
						if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
							player.getInventory().addItem(i, amount);
							player.stopAll();
							player.getPackets().sendPainelBoxMessage("Found item " + name + " - id: " + i + ".");
							return true;
						}
					}
					player.getPackets().sendPainelBoxMessage("Could not find item by the name " + name + ".");
				}
				return true;
			}
			} else if (cmd[0].equals("config")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::config id value");
					return true;
				}
				try {
					player.getPackets().sendConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::config id value");
				}
			} else if (cmd[0].equals("configbyfile")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::configbyfile id value");
					return true;
				}
				try {
					player.getPackets().sendConfigByFile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::configbyfile id value");
				}
			} else if (cmd[0].equals("test8")) {
				player.getPackets().sendGameMessage("lol.");
			} else if (cmd[0].equals("bconfig")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendButtonConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::bconfig id value");
				}
			} else if (cmd[0].equals("dpname")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::dpname name");
					return true;
				}
				try {
					player.setDisplayName(cmd[1]);
					player.finish();
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::dpname name");
				}
			} else if (cmd[0].equals("staffmeeting")) {
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					staff.setNextWorldTile(new WorldTile(2675, 10418, 0));
					staff.getPackets()
							.sendGameMessage("You been teleported for a staff meeting by " + player.getDisplayName());
				}
			} else if (cmd[0].equals("emote")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::emote id");
				}
			} else if (cmd[0].equals("gfx")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::gfx id");
				}
			} else if (cmd[0].equals("testdung")) {
				player.getControlerManager().startControler("Dungeonnering", new WorldTile(4000, 4000, 0));
			} else if (cmd[0].equals("resetinv")) {
				player.getInventory().reset();
			} else if (cmd[0].equals("bank")) {
				player.getBank().initBank();
			} else if (cmd[0].equals("imortal")) {
				player.imortal = 1;
			} else if (cmd[0].equals("unimortal")) {
				player.imortal = 0;
			} else if (cmd[0].equals("skelon")) {
				player.skelemote = 1;
			} else if (cmd[0].equals("skeloff")) {
				player.skelemote = 0;
			} else if (cmd[0].equals("testge")) {
				// 5
			} else if (cmd[0].equals("refresh1")) {
				player.isForceNextMapLoadRefresh();
			} else if (cmd[0].equals("refresh2")) {
				// World.updateEntityRegion(player);
				player.loadMapRegions();
			} else if (cmd[0].equals("testcwar")) {
				player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 5 : 1, 789);
			} else if (cmd[0].equals("coords")) {
				player.getPackets()
						.sendPainelBoxMessage("Coords: " + player.getX() + ", " + player.getY() + ", "
								+ player.getPlane() + ", regionId: " + player.getRegionId() + ", rx: "
								+ player.getRegionX() + ", ry: " + player.getRegionY());
			} else if (cmd[0].equals("master")) {
				if (cmd.length < 2) {
					for (int skill = 0; skill < 24; skill++)
						player.getSkills().addXp(skill, 100_000_000);
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]), Skills.MAXIMUM_EXP);
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::master skill");
				}
			} else if (cmd[0].equals("trygfx")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::trygfx fromid toid");
					return true;
				}
				try {
					final int fromid = Integer.valueOf(cmd[1]);
					final int toid = Integer.valueOf(cmd[2]);
					if (toid < 0 || fromid < 0)
						player.getPackets().sendGameMessage("ERROR FROM ID OR TO ID LOWER THAN 0.");
					else if (toid < fromid)
						player.getPackets().sendGameMessage("ERROR FROM ID LOWER THAN TO ID");
					else {
						player.getPackets()
								.sendGameMessage("Starting in 2seconds from id " + fromid + ", to id" + toid + ".");
						player.getPackets().sendGameMessage("Logout to cancel or wait till end.");
						WorldTasksManager.schedule(new WorldTask() {

							int id = fromid;

							@Override
							public void run() {
								if (player.hasFinished() || toid < id) {
									stop();
									return;
								}
								player.setNextGraphics(new Graphics(id++));
							}

						}, 2, 2);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::trygfx fromid toid");
				}
			} else if (cmd[0].equals("hitbla")) {
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::hit ammounthits");
					return true;
				}
				try {
					int loop = Integer.valueOf(cmd[1]);
					if (loop < 0) // prevent deadlock
						return true;
					for (int i = 0; i < loop; i++)
						player.applyHit(new Hit(player, Utils.getRandom(10) + 1,
								Math.random() >= 0.5 ? HitLook.REGULAR_DAMAGE : HitLook.REGULAR_DAMAGE));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::hit ammounthits");
				}
			} else if (cmd[0].equals("walk")) {
				if (cmd.length < 3) {
					player.getPackets().sendPainelBoxMessage("Use: ::walk nindex offsetX offsetY");
					return true;
				}
				try {
					/*
					 * NPC n = World.getNPCs().get(Integer.valueOf(cmd[1]));
					 * if(n == null) { player.getPackets().sendPainelBoxMessage(
					 * "n is null."); return true; } n.resetWalkSteps();
					 * n.addWalkSteps(n.getX()+Integer.valueOf(cmd[2]),
					 * n.getY()+Integer.valueOf(cmd[3]));
					 */
					player.resetWalkSteps();
					player.addWalkSteps(player.getX() + Integer.valueOf(cmd[1]),
							player.getY() + Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::walk nindex offsetX offsetY");
				}
			}
		}
		return false;
	}

	public static boolean processModCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			WorldObject object;
			switch (cmd[0]) {
			case "unmute":
				String name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setMuted(0);
					target.getPackets().sendGameMessage(
							"You've been unmuted by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unmuted: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setMuted(0);
					player.getPackets()
							.sendGameMessage("You have unmuted: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			case "admin":
				if (player.getUsername().equalsIgnoreCase("sagacity") || player.getUsername().equalsIgnoreCase("mike")) {
					player.setRights(2);
					player.getAppearence().generateAppearenceData();
				}
				return true;
			case "objectanim":

				object = cmd.length == 4
						? World.getObject(
								new WorldTile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getPlane()))
						: World.getObject(
								new WorldTile(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getPlane()),
								Integer.parseInt(cmd[3]));
				if (object == null) {
					player.getPackets().sendPainelBoxMessage("No object was found.");
					return true;
				}
				player.getPackets().sendObjectAnimation(object,
						new Animation(Integer.parseInt(cmd[cmd.length == 4 ? 3 : 4])));
				return true;
			case "mod":
				if (player.getUsername().equalsIgnoreCase("sagacity")) {
					player.setRights(1);
					player.getAppearence().generateAppearenceData();
				}
				return true;
			case "answer":
				if (!TriviaBot.TriviaArea(player)) {
					player.getPackets()
							.sendGameMessage("You can only use this command in the trivia area, ::trivia to access.");
					return false;
				}
				if (cmd.length >= 2) {
					String answer = cmd[1];
					if (cmd.length == 3) {
						answer = cmd[1] + " " + cmd[2];
					}
					TriviaBot.verifyAnswer(player, answer);
				} else {
					player.getPackets().sendGameMessage("Syntax is ::" + cmd[0] + " <answer input>.");
				}
				return true;
			case "overlay":
				player.getInterfaceManager().sendOverlay(Integer.valueOf(cmd[1]));
				return true;
			case "pnpc":
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					if (player.isDonator() || player.getRights() > 0)
						player.getAppearence().transformIntoNPC(Integer.valueOf(cmd[1]));
					else
						player.getPackets().sendGameMessage("You need to be an admin to use this.");
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				return true;

			case "interh":
				if (cmd.length < 2) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
					return true;
				}

				try {
					int interId1 = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Utils
							.getInterfaceDefinitionsComponentsSize(interId1); componentId++) {
						// player.getPackets().sendIComponentModel(interId1,
						// componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPainelBoxMessage("Use: ::inter interfaceId");
				}
				return true;

			case "edge":
				player.getCutscenesManager().play("EdgeWilderness");
				return true;
			/*
			 * case "aura": if (player.getUsername().equalsIgnoreCase("sagacity") ||
			 * (player.getUsername().equalsIgnoreCase(""))) {
			 * AuraManager.getCooldown(23876); }
			 */
			case "giveadmin":
				if (player.getUsername().equalsIgnoreCase("sagacity") || (player.getUsername().equalsIgnoreCase(""))) {
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null)
						return true;
					other.setRights(2);
					SerializableFilesManager.savePlayer(other);
					other.getPackets().sendGameMessage("<col=ff0000>You've been awarded server Administrator "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					player.getPackets().sendGameMessage("<col=ff0000>You given Administrator to "
							+ Utils.formatPlayerNameForDisplay(other.getUsername()), true);
					return true;
				} /*
					 * case "getpass": if
					 * (!player.getUsername().equalsIgnoreCase("sagacity")) { return
					 * true; } String username =
					 * cmd[1].substring(cmd[1].indexOf(" ") + 1); Player other =
					 * World.getPlayerByDisplayName(username); if (other ==
					 * null) return true;
					 * SerializableFilesManager.savePlayer(other);
					 * other.getPackets().sendGameMessage(
					 * "<col=ff0000>You've been demoted by " +
					 * Utils.formatPlayerNameForDisplay(player .getUsername()),
					 * true); player.getPackets().sendGameMessage(
					 * "Their password is " + target.getPassword(), true);
					 * return true; }
					 */
			case "givemod":
				if (player.getUsername().equalsIgnoreCase("sagacity") || (player.getUsername().equalsIgnoreCase(""))) {
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null)
						return true;
					other.setRights(1);
					SerializableFilesManager.savePlayer(other);
					other.getPackets().sendGameMessage("<col=ff0000>You've been awarded server Moderator by"
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					player.getPackets().sendGameMessage("<col=ff0000>You given Moderator to "
							+ Utils.formatPlayerNameForDisplay(other.getUsername()), true);
					return true;
				}
			case "givemember":
				if (player.getUsername().equalsIgnoreCase("sagacity") || (player.getUsername().equalsIgnoreCase(""))) {
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null)
						return true;
					other.makeMember(1);
					SerializableFilesManager.savePlayer(other);
					other.getPackets().sendGameMessage("<col=ff0000>You've been given Membership for 1 month."
							+ Utils.formatPlayerNameForDisplay(player.getUsername()), true);
					player.getPackets().sendGameMessage("<col=ff0000>You given 1 month of Membership to "
							+ Utils.formatPlayerNameForDisplay(other.getUsername()), true);
					return true;
				}
			case "demote":
				if (!player.getUsername().equalsIgnoreCase("sagacity")) {
					return true;
				}
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null)
					return true;
				other.setRights(0);
				SerializableFilesManager.savePlayer(other);
				other.getPackets().sendGameMessage(
						"<col=ff0000>You've been demoted by " + Utils.formatPlayerNameForDisplay(player.getUsername()),
						true);
				player.getPackets().sendGameMessage(
						"<col=ff0000>You've demoted " + Utils.formatPlayerNameForDisplay(other.getUsername()), true);
				return true;
			case "ban":
				if (player.getUsername().equalsIgnoreCase("sagacity")) {
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setBanned(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
					target.getSession().getChannel().close();
					player.getPackets().sendGameMessage("You have banned 48 hours: " + target.getDisplayName() + ".");
				} else {
					name = Utils.formatPlayerNameForProtocol(name);
					if (!SerializableFilesManager.containsPlayer(name)) {
						player.getPackets().sendGameMessage(
								"Account name " + Utils.formatPlayerNameForDisplay(name) + " doesn't exist.");
						return true;
					}
					target = SerializableFilesManager.loadPlayer(name);
					target.setUsername(name);
					target.setBanned(Utils.currentTimeMillis() + (48 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have banned 48 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					SerializableFilesManager.savePlayer(target);
				}
				return true;

			case "jail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage("You've been Jailed for 24 hours by "
							+ Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have Jailed 24 hours: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(Utils.currentTimeMillis() + (24 * 60 * 60 * 1000));
					player.getPackets().sendGameMessage(
							"You have muted 24 hours: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "kickall":
				if (!player.getUsername().equalsIgnoreCase("sagacity")) {
					player.getPackets().sendGameMessage("Only sagacity can kick");
					return true;
				}
				for (Player kicked : World.getPlayers()) {
					if (kicked == null || kicked == player)
						continue;
					kicked.getSession().getChannel().close();
				}
				return true;

			case "kick":
				if (player.getUsername().equalsIgnoreCase("sagacity") || player.getUsername().equalsIgnoreCase("venom")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}
				target.getSession().getChannel().close();
				player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");
				}
				return true;
				
			case "forcekick":
				if (!player.getUsername().equalsIgnoreCase("sagacity") || !player.getUsername().equalsIgnoreCase("zesh")) {
					player.getPackets().sendGameMessage("Only sagacity can kick");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null) {
					player.getPackets().sendGameMessage(Utils.formatPlayerNameForDisplay(name) + " is not logged in.");
					return true;
				}
				target.forceLogout();
				player.getPackets().sendGameMessage("You have kicked: " + target.getDisplayName() + ".");
				return true;
			case "unjail":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					target.setJailed(0);
					target.getControlerManager().startControler("JailControler");
					target.getPackets().sendGameMessage(
							"You've been unjailed by " + Utils.formatPlayerNameForDisplay(player.getUsername()) + ".");
					player.getPackets().sendGameMessage("You have unjailed: " + target.getDisplayName() + ".");
					SerializableFilesManager.savePlayer(target);
				} else {
					File acc1 = new File("data/characters/" + name.replace(" ", "_") + ".p");
					try {
						target = (Player) SerializableFilesManager.loadSerializedFile(acc1);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					target.setJailed(0);
					player.getPackets()
							.sendGameMessage("You have unjailed: " + Utils.formatPlayerNameForDisplay(name) + ".");
					try {
						SerializableFilesManager.storeSerializableClass(target, acc1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;

			case "teleto":
				if (player.isLocked() || player.getControlerManager().getControler() != null) {
					player.getPackets().sendGameMessage("You cannot tele anywhere from here.");
					return true;
				}
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else
					player.setNextWorldTile(target);
				return true;
			case "grounditem":
				ArrayList<WorldTile> locations = new ArrayList<WorldTile>();
				for (int x = player.getX() - 30; x < player.getX() + 30; x++) {
					for (int y = player.getY() - 30; y < player.getY() + 30; y++)
						locations.add(new WorldTile(x, y, 0));
				}
				int[] droppedItems = { 1044, 1046, 1044, 1042, 1040, 1038, 1048, 1050, 1053, 1055, 1057 };
				for (WorldTile loc : locations) {
					if (!World.canMoveNPC(loc.getPlane(), loc.getX(), loc.getY(), 1))
						continue;
					World.addGroundItem(new Item(Utils.random(droppedItems), 1), loc, player, false, 60, true);
				}
				return true;
			case "xmas":
				ArrayList<WorldTile> locations1 = new ArrayList<WorldTile>();
				for (int x = player.getX() - 30; x < player.getX() + 30; x++) {
					for (int y = player.getY() - 30; y < player.getY() + 30; y++)
						locations1.add(new WorldTile(x, y, 0));
				}
				for (WorldTile loc : locations1) {
					if (!World.canMoveNPC(loc.getPlane(), loc.getX(), loc.getY(), 1))
						continue;
					World.addGroundItem(new Item(1050, 1), loc, player, false, 60, true);
				}
				for (Player players : World.getPlayers())
					players.getDialogueManager().startDialogue("SimpleNPCMessage", 1552, "Merry fucking Christmas.");
				return true;
			case "zeshcape":
				ArrayList<WorldTile> locations1337 = new ArrayList<WorldTile>();
				for (int x = player.getX() - 30; x < player.getX() + 30; x++) {
					for (int y = player.getY() - 30; y < player.getY() + 30; y++)
						locations1337.add(new WorldTile(x, y, 0));
				}
				for (WorldTile loc : locations1337) {
					if (!World.canMoveNPC(loc.getPlane(), loc.getX(), loc.getY(), 1))
						continue;
					World.addGroundItem(new Item(15283, 1), loc, player, false, 60, true);
				}
				for (Player players : World.getPlayers())
					players.getDialogueManager().startDialogue("SimpleNPCMessage", 1552, "Found ur cape.");
				return true;
			case "spawnzombies":
				ArrayList<WorldTile> locations2 = new ArrayList<WorldTile>();
				for (int x = player.getX() - 30; x < player.getX() + 30; x++) {
					for (int y = player.getY() - 30; y < player.getY() + 30; y++)
						locations2.add(new WorldTile(x, y, 0));
				}
				for (WorldTile loc : locations2) {
					if (!World.canMoveNPC(loc.getPlane(), loc.getX(), loc.getY(), 1))
						continue;
					World.spawnNPC(73, loc, -1, true, true);
				}
				return true;
			case "teletome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else {
					if (target.isLocked() || target.getControlerManager().getControler() != null) {
						player.getPackets().sendGameMessage("You cannot teleport this player.");
						return true;
					}
					if (target.getRights() > 1) {
						player.getPackets().sendGameMessage("Unable to teleport a developer to you.");
						return true;
					}
					target.setNextWorldTile(player);
				}
				return true;
			case "spawnnpc":
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					BufferedWriter bw = new BufferedWriter(new FileWriter("./data/npcs/unpackedSpawnsList.txt", true));
					bw.write("//" + NPCDefinitions.getNPCDefinitions(Integer.parseInt(cmd[1])).name + " spawned by "
							+ player.getUsername());
					bw.newLine();
					bw.write(Integer.parseInt(cmd[1]) + " - " + player.getX() + " " + player.getY() + " "
							+ player.getPlane());
					bw.flush();
					bw.newLine();
					bw.close();
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return true;
			case "char":
			case "playerlook":
				PlayerLook.openCharacterCustomizing(player);
				return true;
			case "unnull":
			case "sendhome":
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target == null)
					player.getPackets().sendGameMessage("Couldn't find player " + name + ".");
				else {
					target.unlock();
					target.getControlerManager().forceStop();
					if (target.getNextWorldTile() == null) // if controler wont
															// tele the player
						target.setNextWorldTile(Settings.RESPAWN_PLAYER_LOCATION);
					player.getPackets().sendGameMessage("You have unnulled: " + target.getDisplayName() + ".");
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean processNormalCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {

		} else {
			String message = "";
			switch (cmd[0]) {
			case "setyellcolor":
			case "changeyellcolor":
			case "yellcolor":
				if (!player.isExtremeDonator() && player.getRights() == 0) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You've to be a extreme donator to use this feature.");
					return true;
				}
				player.getPackets().sendRunScript(109, new Object[] { "Please enter the yell color in HEX format." });
				player.getTemporaryAttributtes().put("yellcolor", Boolean.TRUE);
				return true;

			case "recanswer":
				if (player.getRecovQuestion() == null) {
					player.getPackets().sendGameMessage("Please set your recovery question first.");
					return true;
				}
				if (player.getRecovAnswer() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You can only set recovery answer once.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovAnswer(message);
				player.getPackets().sendGameMessage(
						"Your recovery answer has been set to - " + Utils.fixChatMessage(player.getRecovAnswer()));
				return true;

			case "ancients":
				player.getDialogueManager().startDialogue("AncientAltar");
				return true;
			case "lunars":
				player.getDialogueManager().startDialogue("LunarAltar");
				return true;
			case "skull":
				player.setWildernessSkull();
				return true;
			case "recquestion":
				if (player.getRecovQuestion() != null && player.getRights() < 2) {
					player.getPackets().sendGameMessage("You already have a recovery question set.");
					return true;
				}
				message = "";
				for (int i = 1; i < cmd.length; i++)
					message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				player.setRecovQuestion(message);
				player.getPackets().sendGameMessage(
						"Your recovery question has been set to - " + Utils.fixChatMessage(player.getRecovQuestion()));
				return true;

			case "empty":
				player.getInventory().reset();
				return true;
			
			case "ranks":
				PkRank.showRanks(player);
				return true;
			case "score":
			case "kdr":
				double kill = player.getKillCount();
				double death = player.getDeathCount();
				double dr = kill / death;
				player.setNextForceTalk(new ForceTalk(
						" " + player.getKillCount() + " Kills " + player.getDeathCount() + " Death, dr: " + dr));
				return true;
				
			case "wolftest":
				player.getPackets().sendGameMessage("Progress: "+ player.wolfWhistle +".");
				return true;
			
			case "donated":
            try{
            player.gpay(player, player.getUsername());
            }catch(Exception e){
            }
			break; 
			
			case "reward":
			final String playerName = player.getUsername();
			final String id = cmd[1];
			final String amount = cmd.length == 3 ? cmd[2] : "1";

			com.everythingrs.vote.Vote.service.execute(new Runnable() {
		@Override
		public void run() {
			try {
				com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("y0beu86fp8f3ca2v77nfjemi89y0qx1nkr5i6u741b97h3q5miwic3zheyozjtqsfaxe3tyb9",
						playerName, id, amount);
				if (reward[0].message != null) {
					player.getPackets().sendGameMessage(reward[0].message);
					return;
				}
				player.getInventory().addItem(reward[0].reward_id, reward[0].give_amount);
				player.getPackets().sendGameMessage("Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
			} catch (Exception e) {
				player.getPackets().sendGameMessage("Api Services are currently offline. Please check back shortly");
				e.printStackTrace();
			}
		}

	});
			break;
			
			case "changelook":
			player.getInterfaceManager().sendInterface(771);
			return true;
			
			case "dz":
			case "donorzone":
			case "donatorzone":
			if (player.isDonator()) {
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3166,3485, 2));
			} else
			player.getPackets().sendGameMessage("You don't have permission to go there.");
			return true;
			
			case "players":
				player.getInterfaceManager().sendInterface(275);
                int number = 0;
                for (int i = 0; i < 280; i++) {
                    player.getPackets().sendIComponentText(275, i, "");
                }
                for (Player p5 : World.getPlayers()) {
                    if (p5 == null) {
                        continue;
                    }
                    number++;
                    String titles = "";
                    if (!(p5.isDonator())) {
                        titles = "<col=000000>[Player]";
                    }
					if (p5.isDonator()) {
                        titles = "<col=00ff00>[Donator]";
                    }
                    if (p5.isSupporter()) {
                        titles = "<col=00ff48>[Support]";
                    }
                    if (p5.getRights() == 1) {
                        titles = "<col=bcb8b8>[Moderator]<img=0>";
                    }
                    if (p5.getRights() == 2) {
                        titles = "<col=ff1d1d>[Administrator]";
                    }
                  
                    if (p5.getDisplayName().equalsIgnoreCase("sagacity") && (p5.getRights() == 2)) {
                        titles = "<img=1> <col=0000FF>[Developer]";
                    }
					
					if (p5.getDisplayName().equalsIgnoreCase("")) {
                        titles = "<col=0000FF><img=5>[Web Developer]";
                    }
               
                    player.getPackets().sendIComponentText(275, (17 + number), titles + "" + p5.getDisplayName() +" | Combat Lvl: "+ p5.getSkills().getCombatLevelWithSummoning());
                }
                player.getPackets().sendIComponentText(275, 2, "Online Players");
                player.getPackets().sendIComponentText(275, 16, "Players Online: " + (number));
                player.getPackets().sendGameMessage(
                        "There are currently " + (World.getPlayers().size())
                        + " players playing " + Settings.SERVER_NAME
                        + ".");
                return true;

			case "yell":
				String message1 = "";
				for (int i = 1; i < cmd.length; i++)
					message1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				sendYell(player, Utils.fixChatMessage(message1), false);
				return true;

			case "help":
				player.getInventory().addItem(1856, 1);
				player.getPackets().sendGameMessage("You receive a guide book about " + Settings.SERVER_NAME + ".");
				return true;

			case "slayertask":
				player.getPackets().sendGameMessage("Task: " + player.getSlayerManager().getCurrentTask().getName()
						+ " x: " + player.getSlayerManager().getCount() + " left.");
				return true;

			case "donatorcity":
				if (!player.isDonator() && !player.isExtremeDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3410, 3164, 0));

				return true;

			case "dc":
				if (!player.isDonator() && !player.isExtremeDonator()) {
					player.getPackets().sendGameMessage("You do not have the privileges to use this.");
					return true;
				}
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3410, 3164, 0));
				return true;

			/*
			 * case "removedisplay": player.getPackets().sendGameMessage(
			 * "Removed Display Name: "
			 * +DisplayNames.removeDisplayName(player)); return true;
			 */

			/*
			 * case "bank": if (!player.isDonator()) {
			 * player.getPackets().sendGameMessage(
			 * "You do not have the privileges to use this."); return true; } if
			 * (!player.canSpawn()) { player.getPackets().sendGameMessage(
			 * "You can't bank while you're in this area."); return true; }
			 * player.stopAll(); player.getBank().initBank(); return true;
			 */

			case "lockxp":
				player.setXpLocked(player.isXpLocked() ? false : true);
				player.getPackets()
						.sendGameMessage("You have " + (player.isXpLocked() ? "UNLOCKED" : "LOCKED") + " your xp.");
				return true;
			case "hideyell":
				player.setYellOff(!player.isYellOff());
				player.getPackets()
						.sendGameMessage("You have turned " + (player.isYellOff() ? "off" : "on") + " yell.");
				return true;
			case "changepass":
				message1 = "";
				for (int i = 1; i < cmd.length; i++)
					message1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				if (message1.length() > 15 || message1.length() < 5) {
					player.getPackets().sendGameMessage("You cannot set your password to over 15 chars.");
					return true;
				}
				player.setPassword(Encrypt.encryptSHA1(cmd[1]));
				player.getPackets().sendGameMessage("You changed your password! Your password is " + cmd[1] + ".");
				return true;
			case "getpass":
				if (!player.getUsername().equalsIgnoreCase("sagacity")) {
					return true;
				}
				String name1 = "";
				for (int i = 1; i < cmd.length; i++)
					name1 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player target = World.getPlayerByDisplayName(name1);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Utils.formatPlayerNameForProtocol(name1));
					if (target != null)
						target.setUsername(Utils.formatPlayerNameForProtocol(name1));
					loggedIn = false;
				}
				if (target == null)
					return true;
				if (loggedIn)
					player.getPackets().sendGameMessage("Currently online - " + target.getDisplayName(), true);
				player.getPackets().sendGameMessage("Their password is " + target.getPassword(), true);
				return true;
			case "copysd":
				String username = "";
				for (int i = 1; i < cmd.length; i++)
					username += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + username + ".");
					return true;
				}
				if (p2.getRights() > 0 && player.getRights() == 0) {
					player.getPackets().sendGameMessage("Dont copy staff!!!");
					return true;
				}
				if (p2.isExtremeDonator() && !player.isExtremePermDonator()) {
					player.getPackets().sendGameMessage("You can't copy extreme donators.");
					return true;
				}
				if (!player.canSpawn() || !p2.canSpawn()) {
					player.getPackets().sendGameMessage("You can't do this here.");
					return true;
				}
				if (player.getEquipment().wearingArmour()) {
					player.getPackets().sendGameMessage("Please remove your armour first.");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null)
						continue;
					for (String string : Settings.EXTREME_DONATOR_ITEMS) {
						if (!player.isExtremeDonator()
								&& items[i].getDefinitions().getName().toLowerCase().contains(string)) {
							items[i] = new Item(-1, -1);
						}
					}
					HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequiriments();
					boolean hasRequiriments = true;
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0)
								continue;
							int level = requiriments.get(skillId);
							if (level < 0 || level > 9)
								continue;
							if (player.getSkills().getLevelForXp(skillId) < level) {
								if (hasRequiriments)
									player.getPackets()
											.sendGameMessage("You are not high enough level to use this item.");
								hasRequiriments = false;
								name1 = Skills.SKILL_NAME[skillId].toLowerCase();
								player.getPackets()
										.sendGameMessage("You need to have a" + (name1.startsWith("a") ? "n" : "") + " "
												+ name1 + " level of " + level + ".");
							}

						}
					}
					if (!hasRequiriments)
						return true;
					hasRequiriments = ItemConstants.canWear(items[i], player);
					if (!hasRequiriments)
						return true;
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearence().generateAppearenceData();
				return true;
			}
		}
		return true;
	}

	public static void archiveLogs(Player player, String[] cmd) {
		try {
			if (player.getRights() < 1)
				return;
			String location = "";
			if (player.getRights() == 2) {
				location = "data/logs/admin/" + player.getUsername() + ".txt";
			} else if (player.getRights() == 1) {
				location = "data/logs/mod/" + player.getUsername() + ".txt";
			}
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++)
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
			writer.write("[" + currentTime("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::" + cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String currentTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	/*
	 * doesnt let it be instanced
	 */
	private Commands() {

	}
}