package com.rs.game.player.skills;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.DungeonBoss;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.skills.Dungeonnering.DungeonStructure;
import com.rs.game.player.skills.Dungeonnering.RoomStructure;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

public final class Dungeon {

	private int floor;
	private int complexity;
	private CopyOnWriteArrayList<Player> team;
	private DungeonStructure structure;
	private RoomStructure bossRoom;
	private boolean[] loadedRoom;
	private int[] roomMusics;
	private int baseRegionX;
	private int baseRegionY;
	private boolean loadedDungeon;
	private boolean destroyed;
	private int startPartyTotalCombatLevel;
	
	public Dungeon(int floor, int complexity,  final DungeonStructure structure, Player[] teamArray) {
		this.floor = floor;
		this.complexity = complexity;
		this.team = new CopyOnWriteArrayList<Player>();
		this.structure = structure;
		loadedRoom = new boolean[structure.getRoomsCount()];
		roomMusics = new int[structure.getRoomsCount()];
		for(Player player : teamArray) {
			team.add(player);
			player.getControlerManager().startControler("DungControler", this);
		}
		startPartyTotalCombatLevel = getPartyTotalCombatLevel();
		//slow executor loads dungeon as it may take up to few secs
		CoresManager.slowExecutor.submit(new Runnable() {
			@Override
			public void run() {
				int dungeonSize = structure.getDungeonSize();
				int[] emptyMapBaseCoords;
				if(dungeonSize == Dungeonnering.SMALL_ROOM) {
					emptyMapBaseCoords = RegionBuilder.findEmptyMap(16, 16);
					RegionBuilder.cutMap(emptyMapBaseCoords[0], emptyMapBaseCoords[1], 16, 16, 0);
					// 32 ry 240
				}else if (dungeonSize == Dungeonnering.MED_ROOM) {
					emptyMapBaseCoords = RegionBuilder.findEmptyMap(16, 32);
					RegionBuilder.cutMap(emptyMapBaseCoords[0], emptyMapBaseCoords[1], 16, 32, 0);
				}else if (dungeonSize == Dungeonnering.BIG_ROOM) {
					emptyMapBaseCoords = RegionBuilder.findEmptyMap(32, 32);
					RegionBuilder.cutMap(emptyMapBaseCoords[0], emptyMapBaseCoords[1], 32, 32, 0);
				}else
					throw new RuntimeException("Invalid dungeon size.");
				baseRegionX = emptyMapBaseCoords[0];
				baseRegionY = emptyMapBaseCoords[1];
				Object[] bossInform = Dungeonnering.getRandomBossRoom(structure.getDungeonType());
				bossRoom = (RoomStructure) bossInform[0];
				roomMusics[0] = Dungeonnering.getDungeonStartRoomMusic(structure.getDungeonType());  //start music
				for(int index = 1; index < structure.getRoomsCount()-1; index++)
					roomMusics[index] = Dungeonnering.getRandomDungeonMusic(structure.getDungeonType());
				roomMusics[structure.getRoomsCount()-1] = (Integer) bossInform[1]; //boss music
				loadRoom(0); //loads start room
				startDungeon();
			}
			
		});
		
	}
	
	public boolean hasLoaded() {
		return loadedDungeon;
	}
	
	public void remove(Player player) {
		team.remove(player);
		if(loadedDungeon) {
			if(team.isEmpty())
				destroyDungeon();
		}
	}
	
	public void openStairs() {
		WorldObject object;
		int roomId = structure.getRoomsCount()-1;
		switch(structure.getDungeonType()) {
		case 0:
		default:
			object = new WorldObject(3784, 10, 3, ((baseRegionX << 3) + structure.getRoomX()[roomId] * 16)+7
					, ((baseRegionY << 3) + structure.getRoomY()[roomId] * 16)+15
					, 0);
			break;
		}
		World.spawnObject(object, false);
		for(Player player : team)
			player.setMusicId(770);
	}
	
	public void startDungeon() {
		
		if(team.isEmpty()) {
			destroyDungeon();
			return;
		}
		for(Player player : team) {
			player.stopAll();
			player.setMapSize(3); //biggest map size so less reloading when walking from a part of dungeon to another
			teleHome(player);
			player.getCombatDefinitions().setSpellBook(3); //sets dungeon spellbook
			player.setMusicId(roomMusics[0]);
			player.getPackets().sendGameMessage("");
			player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
			player.getPackets().sendGameMessage("Floor "+floor+" Complexity "+complexity+" (Full)");
			int dungeonSize = structure.getDungeonSize();
			player.getPackets().sendGameMessage("Dungeon Size: "+ 
				(dungeonSize == Dungeonnering.SMALL_ROOM ? "Small" : dungeonSize == Dungeonnering.MED_ROOM ? "Medium" : "Large"));
			player.getPackets().sendGameMessage("Party Size:Dificulty "+team.size()+":"+team.size());
			player.getPackets().sendGameMessage("");
		}
		loadedDungeon = true;
	}
	
	public void teleHome(Player player) {
		player.setNextWorldTile(
				new WorldTile(((baseRegionX << 3) + structure.getRoomX()[0] * 16)+8
						, ((baseRegionY << 3) + structure.getRoomY()[0] * 16)+8
						, 0));
	}
	
	
	public int getRoomIndex(int roomX, int roomY) {
		for(int index = 0; index < structure.getRoomsCount(); index++)
			if(structure.getRoomX()[index] == roomX && structure.getRoomY()[index] == roomY)
				return index;
		return -1;
	}
	
	/*
	 * returns loaded, if didnt need load, player walks
	 */
	public boolean loadRoom(Player player, int roomX, int roomY, boolean playMusic) {
		int roomIndex = getRoomIndex(roomX, roomY);
		if(roomIndex == -1)
			return true;
		boolean loadedRoom = loadRoom(roomIndex);
		if(!loadedRoom && playMusic && player.getMusicId() != roomMusics[roomIndex])
			player.setMusicId(roomMusics[roomIndex]);
		return loadedRoom;
	}
	
	public int[] getCurrentRoomPos(WorldTile tile) {
		return new int[] {(tile.getRegionX()-baseRegionX)/2, (tile.getRegionY()-baseRegionY)/2};
	}
	/*
	 * unuused atm
	 */
	public boolean isAtBossRoom(WorldTile tile) {
		int[] roomCoords = getCurrentRoomPos(tile);
		return getRoomIndex(roomCoords[0], roomCoords[1]) == structure.getRoomsCount()-1;
	}
	
	public int getPartyTotalCombatLevel() {
		int combatLevel = 0;
		for(Player player : team)
			combatLevel += player.getSkills().getCombatLevelWithSummoning();
		return combatLevel;	
	}
	
	private boolean loadRoom(int index) {
		if(loadedRoom[index])
			return false;
		loadedRoom[index] = true;
		final RoomStructure room = index == structure.getRoomsCount()-1 ? bossRoom : structure.getRoomStructures()[index];
		final int roomRegionX = baseRegionX + (structure.getRoomX()[index]*2);
		final int roomRegionY = baseRegionY + (structure.getRoomY()[index]*2);
		RegionBuilder.copyMap(room.getRegionX(), room.getRegionY(),
				roomRegionX,
				roomRegionY,
				2, new int[1], new int[1]);
		int regionId = (((roomRegionX / 8) << 8) + (roomRegionY / 8));
		for(Player player : team) {
			if(!player.getMapRegionsIds().contains(regionId)) //if player is to far... no need to reload, he will reload when walk to that room
				continue;
			player.setForceNextMapLoadRefresh(true);
			player.loadMapRegions();
		}
		//slow executor loads dungeon as it may take up to few secs
		final Dungeon dungeon = this;
			CoresManager.slowExecutor.submit(new Runnable() {

				@Override
				public void run() {
					String[] npcNames = room.getNpcName();
					if(npcNames != null) {
						int[] loadType = room.getNpcType();
						int recommendedLevel = startPartyTotalCombatLevel/npcNames.length;
						for(int i = 0; i < npcNames.length; i++) {
							if(destroyed)
								break;
							int npcId = -1;
							if(loadType[i] == Dungeonnering.NORMAL_NPC) {
								for(int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
									if(destroyed)
										break;
									NPCDefinitions npcDef = NPCDefinitions.getNPCDefinitions(id);
									if(npcDef.name != null && npcDef.name.equals(npcNames[i])) {
										npcId = id;
										break;
									}
								}
							}else if (loadType[i] == Dungeonnering.MARK_NPC) {
								if(destroyed)
									break;
								int lastCombatLevelDifference = -1;
								for(int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
									NPCDefinitions npcDef = NPCDefinitions.getNPCDefinitions(id);
									if(npcDef.name != null && npcDef.name.equals(npcNames[i]) && npcDef.hasMarkOption()) {
										int difference = npcDef.combatLevel-recommendedLevel;
										if(difference < 0)
											difference = -difference;
										if(lastCombatLevelDifference == -1 || difference < lastCombatLevelDifference
												|| (difference == lastCombatLevelDifference &&  NPCDefinitions.getNPCDefinitions(npcId).combatLevel < npcDef.combatLevel)) {
											lastCombatLevelDifference = difference;
											npcId = id;
										}
									}
								}
							}else if (loadType[i] == Dungeonnering.BOSS_NPC) {
								if(destroyed)
									break;
								int lastCombatLevelDifference = -1;
								for(int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
									NPCDefinitions npcDef = NPCDefinitions.getNPCDefinitions(id);
									if(npcDef.name != null && npcDef.name.equals(npcNames[i])) {
										int difference = npcDef.combatLevel-recommendedLevel;
										if(difference < 0)
											difference = -difference;
										if(lastCombatLevelDifference == -1 || (difference < lastCombatLevelDifference)) {
											lastCombatLevelDifference = difference;
											npcId = id;
										}
									}
								}
							}
							if(destroyed)
								break;
							if(npcId == -1)
								continue;
							if (loadType[i] == Dungeonnering.BOSS_NPC) {
								new DungeonBoss(npcId, new WorldTile((roomRegionX << 3)+room.getNpcDungeonX()[i]
										, (roomRegionY << 3)+room.getNpcDungeonY()[i]
										, 0), dungeon);
							}else {
								new NPC(npcId, new WorldTile((roomRegionX << 3)+room.getNpcDungeonX()[i]
										, (roomRegionY << 3)+room.getNpcDungeonY()[i]
										, 0), -1, true, true);
							}
							
						}
					}
				}	
			});
			
		return true;
	}

	public void destroyDungeon() {
		if(destroyed)
			return;
		destroyed = true;
		WorldTasksManager.schedule(new WorldTask()  {
			@Override
			public void run() {
				int dungeonSize = structure.getDungeonSize();
				if(dungeonSize == Dungeonnering.SMALL_ROOM)
					RegionBuilder.destroyMap(baseRegionX, baseRegionY, 16, 16);
				else if (dungeonSize == Dungeonnering.MED_ROOM)
					RegionBuilder.destroyMap(baseRegionX, baseRegionY, 16, 32);
				else if (dungeonSize == Dungeonnering.BIG_ROOM)
					RegionBuilder.destroyMap(baseRegionX, baseRegionY, 32, 32);
				else
					throw new RuntimeException("Invalid dungeon size.");
			}
		}, 1);
	}
	


	public boolean isDestroyed() {
		return destroyed;
	}
	
}
