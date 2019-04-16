package com.rs.game.player.skills;

import com.rs.game.player.Player;
import com.rs.utils.Utils;

public final class Dungeonnering {
	
	public static final int SMALL_ROOM = 0;
	public static final int MED_ROOM = 1;
	public static final int BIG_ROOM = 2;
	public static final int ICE_DUNDEON = 0;
	public static final int[] DUNGEON_DOORS = new int[] {50342};
	public static final int[] DUNGEON_EXITS = new int[] {51156};
	/*
	 * type, size, dungs
	 */
	private static final DungeonStructure[][][] dungeonStructures = {

		//ice dungeons
		new DungeonStructure[][] {
				//small dungeons
				new DungeonStructure[] {
						DungeonStructure.ICE_DUNGEON_SMALL_1
				},
				//med dungeons
				new DungeonStructure[] {
						
				},
				//big dungeons
				new DungeonStructure[] {
						
				}
				
		}
	};
	
	public static DungeonStructure getRandomDungeonStructure(int dungeonType, int dungeonSize) {
		DungeonStructure[] dungs = dungeonStructures[dungeonType][dungeonSize];
		return dungs[Utils.getRandom(dungs.length-1)];
	}
	
	public static enum DungeonStructure {
		ICE_DUNGEON_SMALL_1(ICE_DUNDEON, SMALL_ROOM,
				new Object[] {
				//start room
				new Object[] {
						RoomStructure.ICE_ROOM_START_NWSE_DOOR, 2, 2
				},
				new Object[] {
						RoomStructure.ICE_ROOM_NWSE_DOOR, 3, 2
				},
				new Object[] {
						RoomStructure.ICE_ROOM_S_DOOR, 3, 3
				},
				//boss room
				new Object[] {
						//the room is null cuz its random choosed, based on minimun party guy dung lvl and dung type
						null, 2, 3
				}
		});
		
		//the first room on array has to be start room
		//the last room on array has to be boss room
		
		private RoomStructure[] roomStructures;
		//4x4 rooms dungs usualy for small
		private int[] roomX; 
		private int[] roomY; 
		private int dungeonType;
		private int dungeonSize;
		private DungeonStructure(int dungeonType, int dungeonSize, Object... objects) {
			this.dungeonType = dungeonType;
			this.dungeonSize = dungeonSize;
			roomStructures = new RoomStructure[objects.length];
			roomX = new int[objects.length];
			roomY = new int[objects.length];
			for(int index = 0; index < objects.length; index++) {
				Object[] objectArray2 = (Object[]) objects[index];
				roomStructures[index] = (RoomStructure) objectArray2[0];
				roomX[index] = (Integer) objectArray2[1];
				roomY[index] = (Integer) objectArray2[2];
			}
		}
		
		public int getDungeonSize() {
			return dungeonSize;
		}
		
		public int getRoomsCount() {
			return roomStructures.length;
		}
		
		public RoomStructure[] getRoomStructures() {
			return roomStructures;
		}
		
		public int[] getRoomX() {
			return roomX;
		}
		
		public int[] getRoomY() {
			return roomY;
		}
		
		public int getDungeonType() {
			return dungeonType;
		}
	}
	
	
	public static int getDungeonStartRoomMusic(int dungeonType) {
		return dungeonMusics[dungeonType][0];
	}
	
	public static int getRandomDungeonMusic(int dungeonType) {
		int[] rooms = dungeonMusics[dungeonType];
		return rooms[Utils.getRandom(rooms.length-2)+1];
	}
	
	private static final int[][] dungeonMusics = {
		//ice dungmusics, the first music id is the start room
		new int[] {
			822, 811, 812, 813
		}
	};
	
	public static Object[] getRandomBossRoom(int dungeonType) {
		Object[][] rooms = bossesRooms[dungeonType];
		return rooms[Utils.getRandom(rooms.length-1)];
	}
	
	private static final Object[][][] bossesRooms = {
		//ice bossesrooms
		new Object[][] {
				//boss room, musicid
				new Object[] {RoomStructure.ICY_BONES_BOSS_ROOM, 820}
		}
	};
	
	public static final int NORMAL_NPC = 0;
	public static final int MARK_NPC = 1;
	public static final int BOSS_NPC = 2;
	
	public static enum RoomStructure {
		ICE_ROOM_START_NWSE_DOOR(14,632
				, "Smuggler", NORMAL_NPC, 6, 6),
		ICE_ROOM_S_DOOR(10,240),
		ICE_ROOM_NWSE_DOOR(8,248
				,"Mysterious shade", MARK_NPC, 5, 5
				,"Ice giant", MARK_NPC, 8, 8
				,"Ice elemental", MARK_NPC, 12, 12),
		ICY_BONES_BOSS_ROOM(24,626
				,"Icy Bones", BOSS_NPC, 7, 7);
		private int regionX;
		private int regionY;
		private String[] npcName;
		private int[] npcType;
		private int[] npcDungeonX; //up to 16
		private int[] npcDungeonY; //up to 16
		private RoomStructure(int regionX, int regionY, Object... npcs) {
			this.regionX = regionX;
			this.regionY = regionY;
			if(npcs != null) {
				npcName = new String[npcs.length/4];
				npcType = new int[npcs.length/4];
				npcDungeonX = new int[npcs.length/4];
				npcDungeonY = new int[npcs.length/4];
				for(int index = 0; index < npcName.length; index++) {
					npcName[index] = (String) npcs[index*4];
					npcType[index] = (Integer) npcs[index*4+1];
					npcDungeonX[index] = (Integer) npcs[index*4+2];
					npcDungeonY[index] = (Integer) npcs[index*4+3];
				}
			}
		}
		
		public int getRegionX() {
			return regionX;
		}
		
		public int getRegionY() {
			return regionY;
		}
		
		public String[] getNpcName() {
			return npcName;
		}
		
		public int[] getNpcType() {
			return npcType;
		}
		
		public int[] getNpcDungeonX() {
			return npcDungeonX;
		}
		
		public int[] getNpcDungeonY() {
			return npcDungeonY;
		}
	}
	

	public static int getDungeonType(int floor) {
		return ICE_DUNDEON; //atm all dungs are ice
	}
	
	public static void startDungeon(int size, int floor, int complexity, Player... team) {
		new Dungeon(floor, complexity, getRandomDungeonStructure(getDungeonType(floor), size), team);
	}
	
	private Dungeonnering() {
		
	}
}
