package com.rs.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.rs.game.item.Item;
import com.rs.game.World;
import com.rs.game.WorldTile;

public class GlobalItems {

	public static final void init() {
		if (!(new File("data/items/packedItemSpawns").exists())) {
			System.out.println("Packing item spawns");
			packItemSpawns();
		}
	}

	private static final void packItemSpawns() {
		Logger.log("GlobalItems", "Packing item spawns...");
		if (!new File("data/items/packedItemSpawns").mkdir())
			throw new RuntimeException("Couldn't create packedItemSpawns directory.");
		try {
			BufferedReader in = new BufferedReader(new FileReader("data/items/worldItemSpawns.txt"));
			while (true) {
				try {
					String line = in.readLine();
					if (line == null)
						break;
					if (line.startsWith("//"))
						continue;
					String[] splitedLine = line.split(" - ");
					if (splitedLine.length != 3)
						throw new RuntimeException("1. Invalid Object Spawn line: " + line);
					String[] splitedLine2 = splitedLine[0].split(" ");
					String[] splitedLine3 = splitedLine[1].split(" ");
					if (splitedLine2.length != 2 && splitedLine3.length != 3)
						throw new RuntimeException("2. Invalid Object Spawn line: " + line);
					int itemId = Integer.parseInt(splitedLine2[0]);
					int itemAmount = Integer.parseInt(splitedLine2[1]);
					int tick = Integer.parseInt(splitedLine[2]);
					WorldTile tile = new WorldTile(Integer.parseInt(splitedLine3[0]), Integer.parseInt(splitedLine3[1]), Integer.parseInt(splitedLine3[2]));
					addItemSpawn(itemId, itemAmount, tile.getRegionId(), tile, tick);
				} finally {
					//in.close();
				}
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void loadItemSpawns(int regionId) {
		File file = new File("data/items/packedItemSpawns/" + regionId + ".os");
		if (!file.exists())
			return;
		try {
			RandomAccessFile in = new RandomAccessFile(file, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int itemId = buffer.getShort() & 0xffff;
				int itemAmount = buffer.getShort() & 0xffff;
				int plane = buffer.get() & 0xff;
				int x = buffer.getShort() & 0xffff;
				int y = buffer.getShort() & 0xffff;
				int tick = buffer.getShort() & 0xffff;
				World.addPermanentGroundItem(new Item(itemId, itemAmount), new WorldTile(x, y, plane), tick, true);
			}
			channel.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final void addItemSpawn(int itemId, int itemAmount, int regionId, WorldTile tile, int tick) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream("data/items/packedItemSpawns/" + regionId + ".os", true));
			out.writeShort(itemId);
			out.writeShort(itemAmount);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeShort(tick);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}