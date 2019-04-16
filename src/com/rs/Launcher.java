package com.rs;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Region;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.player.LendingManager;
import com.rs.game.area.AreaManager;
import com.rs.game.minigames.soulwars.SoulWars;
import com.rs.utils.GlobalItems;
import com.rs.game.npc.combat.CombatScriptsHandler;
import com.rs.game.player.Player;
import com.rs.game.player.content.FishingSpotsHandler;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.controlers.ControlerHandler;
import com.rs.game.player.cutscenes.CutscenesHandler;
import com.rs.game.player.dialogues.DialogueHandler;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.IPBanL;
import com.rs.utils.ItemBonuses;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.MapContainersXteas;
import com.rs.utils.MusicHints;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.NPCSpawning;
import com.rs.utils.NPCSpawns;
import com.rs.utils.ObjectSpawns;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.ShopsHandler;
import com.rs.utils.WorldList;
import com.rs.utils.NPCCharmDrops;
import com.rs.utils.huffman.Huffman;
import com.rs.utils.WeightManager;

public final class Launcher {
	
	public static final void main(String[] args) throws Exception {
		long currentTime = System.currentTimeMillis();
		Logger.log("Launcher", "Initing Cache..."); 
		Cache.init();
		/*try {
			ObjectDefinitions.load();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		ItemEquipIds.init();
		Huffman.init();
		Logger.log("Launcher", "Initing Data Files..."); 
		MapContainersXteas.init();
		MapAreas.init();
		NPCCharmDrops.init();
		ObjectSpawns.init();
		IPBanL.init();
		PkRank.init();
		ItemExamines.init();
		AreaManager.init();
		NPCSpawns.init();
		SoulWars.init();
		WorldList. getWorldList();
		GlobalItems.init();
		NPCBonuses.init();
		NPCDrops.init();
		GrandExchange.init();
		MusicHints.init();
		WeightManager.init();
		ShopsHandler.init();
		NPCCombatDefinitionsL.init();
		ItemBonuses.init();
		Logger.log("Launcher", "Initing Fishing Spots...");
		FishingSpotsHandler.init();
		Logger.log("Launcher", "Initing Lent Items...");
		LendingManager.init();
		Logger.log("Launcher", "Initing Dialogues..."); 
		DialogueHandler.init();
		Logger.log("Launcher", "Initing Controlers..."); 
		ControlerHandler.init();
		Logger.log("Launcher", "Initing Cutscenes...");
		CutscenesHandler.init();
		Logger.log("Launcher", "Initing Cores Manager...");
		CoresManager.init();
		Logger.log("Launcher", "Initing World...");
		World.init();
		Logger.log("Launcher", "Initing NPC Combat Scripts...");
		CombatScriptsHandler.init();
		Logger.log("Launcher", "Initing Region Builder..."); 
		RegionBuilder.init();
		//Logger.log("Launcher", "Starting Control Panel");
		//Panel frame = new Panel();
		//frame.setVisible(true);
		Logger.log("Launcher", "Initing Server Channel Handler...");
		try {
			ServerChannelHandler.init();
			NPCSpawning.spawnNPCS();
		}catch(ChannelException e) {
			e.printStackTrace();
			Logger.log("Launcher", "Failed initing Server Channel Handler. Shutting down...");
			shutdown();
			return;
		}
		Logger.log("Launcher", "Server took "+(System.currentTimeMillis()-currentTime)+" milli seconds to launch.");
		addCleanMemoryTask();
		addAccountsSavingTask();
	}
	
	private static final void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				if(Runtime.getRuntime().freeMemory() < Settings.MIN_FREE_MEM_ALLOWED) 
					cleanMemory();
			}
		}, 0, 10, TimeUnit.MINUTES);
	}
	
	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SerializableFilesManager.savePlayer(player);
		}
		IPBanL.save();
		GrandExchange.save();
		PkRank.save();
	}

	
	private static final void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveFiles();
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 1, 1, TimeUnit.MINUTES);
	}
	
	/*public static final void saveAccounts() {
		for(Player player : World.getPlayers()) {
			if(player == null || !player.hasStarted() || player.hasFinished())
				continue;
			SerializableFilesManager.savePlayer(player);
		}
	}*/
	
	/**
	 * Alright so, launch teh server and let's make sure everything functions.
	 */
	
	
	public static final void cleanMemory() {
		ItemDefinitions.clearItemsDefinitions();
		NPCDefinitions.clearNPCDefinitions();
		//ObjectDefinitions.clearObjectDefinitions();
		for(Region region : World.getRegions().values())
			region.removeMapFromMemory();
		System.gc();
	}
	
	public static final void shutdown() {
		try {
			ServerChannelHandler.shutdown();
			CoresManager.shutdown();
		} finally {
			System.exit(0);
		}
	}
	
	public static final void restart() {
		System.gc();
		try {
            Runtime.getRuntime().exec("java -Xms512m -cp bin;data/libs/netty-3.6.3.Final.jar com.rs.Launcher");
			System.exit(0);
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	private Launcher() {
		
	}
	
	
}
