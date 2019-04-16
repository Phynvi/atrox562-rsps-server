package com.rs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

public final class NPCSpawns {

	private static final Object lock = new Object();
	public static boolean addSpawn(String username, int id, WorldTile tile) throws Throwable {
		synchronized(lock) {
			File file = new File("data/npcs/spawns.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("// "+NPCDefinitions.getNPCDefinitions(id).name+", "+NPCDefinitions.getNPCDefinitions(id).combatLevel+", added by: "+username);
			writer.newLine();
			writer.flush();
			writer.write(id+" - "+tile.getX()+" "+tile.getY()+" "+tile.getPlane());
			writer.newLine();
			writer.flush();
			World.spawnNPC(id, tile, -1, true);
			return true;
		}

	}

	public static boolean removeSpawn(NPC npc) throws Throwable {
		synchronized(lock) {
			List<String> page = new ArrayList<>();
			File file = new File("data/npcs/spawns.txt");
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			boolean removed = false;
			int id =  npc.getId();
			WorldTile tile = npc.getRespawnTile();
			while((line = in.readLine()) != null)  {
				if(line.equals(id+" - "+tile.getX()+" "+tile.getY()+" "+tile.getPlane())) {
					page.remove(page.get(page.size()-1)); //description
					removed = true;
					continue;
				}
				page.add(line);
			}
			if(!removed)
				return false;
			file.delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(String l : page) {
				writer.write(l);
				writer.newLine();
				writer.flush();
			}
			npc.finish();
			return true;
		}
	}

	public static final void init() {
		if (!new File("data/npcs/packedSpawns").exists())
			packNPCSpawns();
	}

	private static final void packNPCSpawns() {
		Logger.log("NPCSpawns", "Packing npc spawns...");
		if (!new File("data/npcs/packedSpawns").mkdir())
			throw new RuntimeException(
					"Couldn't create packedSpawns directory.");
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"data/npcs/unpackedSpawnsList.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid NPC Spawn line: "
							+ line);
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 5);
				if (splitedLine2.length != 3 && splitedLine2.length != 5)
					throw new RuntimeException("Invalid NPC Spawn line: "
							+ line);
				WorldTile tile = new WorldTile(
						Integer.parseInt(splitedLine2[0]),
						Integer.parseInt(splitedLine2[1]),
						Integer.parseInt(splitedLine2[2]));
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				if (splitedLine2.length == 5) {
					mapAreaNameHash = Utils.getNameHash(splitedLine2[3]);
					canBeAttackFromOutOfArea = Boolean
							.parseBoolean(splitedLine2[4]);
				}
				addNPCSpawn(npcId, tile.getRegionId(), tile, mapAreaNameHash,
						canBeAttackFromOutOfArea);
			}
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadNPCSpawns(int regionId) {
		File file = new File("data/npcs/packedSpawns/" + regionId + ".ns");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				int npcId = buffer.getShort() & 0xffff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				boolean hashExtraInformation = buffer.get() == 1;
				int mapAreaNameHash = -1;
				boolean canBeAttackFromOutOfArea = true;
				if (hashExtraInformation) {
					mapAreaNameHash = buffer.getInt();
					canBeAttackFromOutOfArea = buffer.get() == 1;
				}
				World.spawnNPC(npcId, new WorldTile(x, y, plane),
						mapAreaNameHash, canBeAttackFromOutOfArea);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static final void addNPCSpawn(int npcId, int regionId,
			WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"data/npcs/packedSpawns/" + regionId + ".ns", true));
			out.writeShort(npcId);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(mapAreaNameHash != -1);
			if (mapAreaNameHash != -1) {
				out.writeInt(mapAreaNameHash);
				out.writeBoolean(canBeAttackFromOutOfArea);
			}
			out.flush();
			out.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	private static WorldTile[] worldtile = new WorldTile[] { 
			//Starting world impling spawns..
			new WorldTile(3162, 3163, 0),
			new WorldTile(3233, 3173, 0),
			new WorldTile(3169, 3277, 0),
			new WorldTile(3097, 3289, 0),
			new WorldTile(3060, 3268, 0),
			new WorldTile(2986, 3216, 0),
			new WorldTile(2940, 3203, 0),
			new WorldTile(2932, 3264, 0),
			new WorldTile(2954, 3276, 0),
			new WorldTile(2927, 3332, 0),
			new WorldTile(2893, 3349, 0),
			new WorldTile(2900, 3371, 0),
			new WorldTile(2896, 3403, 0),
			new WorldTile(2879, 3410, 0),
			new WorldTile(2867, 3455, 0),
			new WorldTile(2884, 3470, 0),
			new WorldTile(2914, 3509, 0),
			new WorldTile(2943, 3500, 0),
			new WorldTile(2983, 3499, 0),
			new WorldTile(3013, 3504, 0),
			new WorldTile(3047, 3515, 0),
			new WorldTile(3101, 3491, 0),
			new WorldTile(3116, 3472, 0),
			new WorldTile(3132, 3454, 0),
			new WorldTile(3170, 3402, 0),
			new WorldTile(3177, 3382, 0),
			new WorldTile(3189, 3372, 0),
			new WorldTile(3206, 3363, 0),
			new WorldTile(3242, 3367, 0),
			new WorldTile(3266, 3367, 0),
			new WorldTile(3305, 3378, 0),
			new WorldTile(3286, 3386, 0),
			new WorldTile(3274, 3436, 0),
			new WorldTile(3287, 3460, 0),
			new WorldTile(3265, 3515, 0),
			new WorldTile(3245, 3504, 0),
			new WorldTile(3176, 3512, 0),
			new WorldTile(3173, 3382, 0),
			new WorldTile(3181, 3342, 0),
			new WorldTile(3192, 3318, 0),
			new WorldTile(3241, 3274, 0),
			new WorldTile(3252, 3305, 0),
			new WorldTile(3275, 3319, 0),
			new WorldTile(3305, 3241, 0),
			new WorldTile(3326, 3169, 0),
			new WorldTile(3300, 3096, 0),
			new WorldTile(3333, 3063, 0),
			new WorldTile(3377, 3019, 0),
			new WorldTile(3303, 2974, 0),
			new WorldTile(3257, 2945, 0),
			new WorldTile(3202, 2918, 0),
			new WorldTile(3152, 2920, 0),
			new WorldTile(3149, 2971, 0),
			new WorldTile(3360, 2784, 0),
			new WorldTile(3334, 2760, 0),
			new WorldTile(3418, 2772, 0),
			new WorldTile(3074, 3395, 0),
			new WorldTile(3072, 3356, 0),
			new WorldTile(3124, 3315, 0),
			new WorldTile(2904, 3381, 0),
			new WorldTile(2906, 3425, 0),
			new WorldTile(2882, 3466, 0),
			new WorldTile(2929, 3506, 0),
			new WorldTile(2927, 3541, 0),
			new WorldTile(2878, 3566, 0),
			new WorldTile(2836, 3594, 0),
			new WorldTile(2838, 3594, 0),
			new WorldTile(2810, 3509, 0),
			new WorldTile(2816, 3478, 0),
			new WorldTile(2789, 3429, 0),
			new WorldTile(2749, 3448, 0),
			new WorldTile(2727, 3464, 0),
			new WorldTile(2717, 3495, 0),
			new WorldTile(2682, 3502, 0),
			new WorldTile(2625, 3513, 0),
			new WorldTile(2644, 3405, 0),
			new WorldTile(2636, 3356, 0),
			new WorldTile(2606, 3346, 0),
			new WorldTile(2573, 3354, 0),
			new WorldTile(2536, 3382, 0),
			new WorldTile(2502, 3399, 0),
			new WorldTile(2459, 3404, 0),
			new WorldTile(2449, 3443, 0),
			new WorldTile(2397, 3457, 0),
			new WorldTile(2377, 3420, 0),
			new WorldTile(2368, 3419, 0),
			new WorldTile(2343, 3573, 0),
			new WorldTile(2347, 3620, 0),
			new WorldTile(2332, 3648, 0),
			new WorldTile(2412, 2851, 0),
			new WorldTile(2460, 2866, 0),
			new WorldTile(2460, 2892, 0),
			new WorldTile(2498, 2916, 0),
			new WorldTile(2515, 2923, 0),
			new WorldTile(2508, 2968, 0),
			new WorldTile(2517, 2996, 0),
			new WorldTile(2546, 3004, 0),
			new WorldTile(2577, 3022, 0),
			new WorldTile(2613, 2993, 0),
			new WorldTile(2579, 3093, 0),
			new WorldTile(2536, 3219, 0),
			new WorldTile(2528, 3237, 0),
			new WorldTile(2541, 3263, 0),
			new WorldTile(2482, 3216, 0),
			new WorldTile(2465, 3183, 0),
			new WorldTile(2419, 3147, 0),
			new WorldTile(2377, 3071, 0),
			new WorldTile(2337, 3069, 0),
			new WorldTile(2435, 3239, 0),
			new WorldTile(2504, 3274, 0)
	};
	
	private NPCSpawns() {
	}
}
