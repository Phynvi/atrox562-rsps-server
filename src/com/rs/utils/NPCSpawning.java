package com.rs.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Utils.EntityDirection;
import com.rs.game.player.Player;
import com.rs.game.Region;

public class NPCSpawning {

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		
		//Faladors bankers
		World.spawnNPC(494, new WorldTile(2945, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2946, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2947, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2948, 3366, 0), -1, false, "NORTH");
		World.spawnNPC(494, new WorldTile(2949, 3366, 0), -1, false, "NORTH");

		//Edgeville
		World.spawnNPC(44, new WorldTile(3096, 3493, 0), -1, false, "WEST");
		World.spawnNPC(45, new WorldTile(3096, 3491, 0), -1, false, "WEST");
		World.spawnNPC(44, new WorldTile(3096, 3489, 0), -1, false, "WEST");
		World.spawnNPC(45, new WorldTile(3097, 3494, 0), -1, false, "NORTH");
		World.spawnObject(new WorldObject(24343, 10, 1, 3085, 3509, 0), true);
		
		//Grand Exchange
		World.spawnNPC(614, new WorldTile(3161, 3480, 0), -1, false, "NORTH");
		World.spawnNPC(496, new WorldTile(3166, 3490, 0), -1, false, "EAST");
		World.spawnNPC(497, new WorldTile(3166, 3489, 0), -1, false, "EAST");
		World.spawnNPC(495, new WorldTile(3163, 3489, 0), -1, false, "WEST");
		World.spawnNPC(6200, new WorldTile(3163, 3490, 0), -1, false, "WEST");
		World.spawnNPC(6528, new WorldTile(3165, 3488, 0), -1, false, "SOUTH");
		World.spawnNPC(6531, new WorldTile(3164, 3488, 0), -1, false, "SOUTH");
		World.spawnNPC(6529, new WorldTile(3164, 3491, 0), -1, false, "NORTH");
		World.spawnNPC(6530, new WorldTile(3165, 3491, 0), -1, false, "NORTH");
		World.spawnNPC(462, new WorldTile(3163, 3477, 0), -1, false, "NORTH");
		World.spawnObject(new WorldObject(10251, 10, 2, 3164, 3476, 0), true);

        //Varrocks banker
		World.spawnNPC(44, new WorldTile(3191, 3435, 0), -1, false, "WEST");
		World.spawnNPC(45, new WorldTile(3191, 3437, 0), -1, false, "WEST");
		World.spawnNPC(44, new WorldTile(3191, 3439, 0), -1, false, "WEST");
		World.spawnNPC(45, new WorldTile(3191, 3441, 0), -1, false, "WEST");
		World.spawnNPC(44, new WorldTile(3191, 3443, 0), -1, false, "WEST");
		
		World.spawnNPC(44, new WorldTile(3180, 3436, 0), -1, false, "EAST");
		World.spawnNPC(45, new WorldTile(3180, 3438, 0), -1, false, "EAST");
		World.spawnNPC(44, new WorldTile(3180, 3440, 0), -1, false, "EAST");
		World.spawnNPC(45, new WorldTile(3180, 3442, 0), -1, false, "EAST");
		World.spawnNPC(44, new WorldTile(3180, 3444, 0), -1, false, "EAST");
		
		//Al kharid banker
		World.spawnNPC(496, new WorldTile(3267, 3164, 0), -1, false, "EAST");
		World.spawnNPC(497, new WorldTile(3267, 3165, 0), -1, false, "EAST");
		World.spawnNPC(496, new WorldTile(3267, 3166, 0), -1, false, "EAST");
		World.spawnNPC(497, new WorldTile(3267, 3167, 0), -1, false, "EAST");
		World.spawnNPC(496, new WorldTile(3267, 3168, 0), -1, false, "EAST");
		World.spawnNPC(497, new WorldTile(3267, 3169, 0), -1, false, "EAST");
		World.spawnNPC(496, new WorldTile(3267, 3170, 0), -1, false, "EAST");
		
		//Lumbridge guards
		World.spawnNPC(926, new WorldTile(3269, 3229, 0), -1, false, "EAST");
		World.spawnNPC(926, new WorldTile(3268, 3226, 0), -1, false, "EAST");
		World.spawnNPC(925, new WorldTile(3267, 3226, 0), -1, false, "WEST");
		World.spawnNPC(925, new WorldTile(3266, 3229, 0), -1, false, "WEST");
		
		//Donator zone
		World.spawnNPC(7448, new WorldTile(2204, 3808, 0), -1, false, "NORTH");
		World.spawnObject(new WorldObject(42192, 10, 2, 2203, 3808, 0), true);
		World.spawnObject(new WorldObject(24343, 10, 2, 2202, 3807, 0), true);
		World.spawnObject(new WorldObject(2469, 10, 2, 2205, 3811, 0), true);
		
		//Home area
		/*World.spawnObject(new WorldObject(24343, 10, 2, 2147, 5091, 1), true);
		//Slayer Portal TODO: Make it open interface
		World.spawnObject(new WorldObject(10251, 10, 2, 2144, 5091, 1), true);
		World.spawnNPC(44, new WorldTile(2152, 5102, 1), -1, false, "SOUTH");
		World.spawnNPC(6528, new WorldTile(2150, 5102, 1), -1, false, "SOUTH");
		World.spawnNPC(462, new WorldTile(2143, 5099, 1), -1, false, "EAST");
		World.spawnNPC(970, new WorldTile(2152, 5093, 1), -1, false, "NORTH");
		World.spawnNPC(8462, new WorldTile(2154, 5106, 1), -1, false, "SOUTH");
		World.spawnNPC(8461, new WorldTile(2156, 5106, 1), -1, false, "SOUTH");
		World.spawnNPC(8464, new WorldTile(2158, 5106, 1), -1, false, "SOUTH");
		World.spawnNPC(1597, new WorldTile(2160, 5106, 1), -1, false, "SOUTH");
		World.spawnObject(new WorldObject(1317, 10, 2, 2155, 5102, 1), true);
		World.spawnObject(new WorldObject(14082, 10, 2, 2157, 5100, 1), true);*/
		
		//Kraken in temporary cave
		//World.spawnNPC(8614, new WorldTile(2614, 10281, 0), -1, false, "SOUTH");
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/npcspawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new WorldTile(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					WorldTile spawnLoc = new WorldTile(x, y, z);
					npc.setLocation(spawnLoc);
					World.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}