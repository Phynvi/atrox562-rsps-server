package com.rs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;

public final class ObjectSpawns {

	private static void addObjectSpawn(int objectId, int type,
									   int rotation, int regionId, WorldTile tile, boolean cliped) {
		try {
			final DataOutputStream out = new DataOutputStream(
					new FileOutputStream("data/map/packedSpawns/" + regionId
							+ ".os", true));
			out.writeShort(objectId);
			out.writeByte(type);
			out.writeByte(rotation);
			out.writeByte(tile.getPlane());
			out.writeShort(tile.getX());
			out.writeShort(tile.getY());
			out.writeBoolean(cliped);
			out.flush();
			out.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean addUnpackedSpawn(String username, int id,
			int rotation, WorldTile tile) throws Throwable {
		addObjectSpawn(id, 10, rotation, tile.getRegionId(), tile, true);
		final File file = new File("data/map/unpackedSpawnsList.txt");
		final BufferedWriter writer = new BufferedWriter(new FileWriter(file,
				true));
		writer.write("//" + ObjectDefinitions.getObjectDefinitions(id).name
				+ " spawned by " + username);
		writer.newLine();
		writer.flush();
		writer.write(id + " 10 " + rotation + " - " + tile.getX() + " "
				+ tile.getY() + " " + tile.getPlane() + " true");
		writer.newLine();
		writer.flush();
		writer.close();
		Region region = World.getRegion(tile.getRegionId());
		WorldObject object = new WorldObject(id, 10, rotation, tile.getX(), tile.getY(), tile.getPlane());
		region.spawnObject(object, tile.getPlane(), object.getXInRegion(), object.getYInRegion(), false);
		return true;
	}

	public static void init() {
		if (!new File("data/map/packedSpawns").exists())
			packObjectSpawns();
	}

	public static void loadObjectSpawns(int regionId) {
		final File file = new File("data/map/packedSpawns/" + regionId + ".os");
		if (!file.exists())
			return;
		try {
			final RandomAccessFile in = new RandomAccessFile(file, "r");
			final FileChannel channel = in.getChannel();
			final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			Region region = World.getRegion(regionId);
			while (buffer.hasRemaining()) {
				final int objectId = buffer.getShort() & 0xffff;
				final int type = buffer.get() & 0xff;
				final int rotation = buffer.get() & 0xff;
				final int plane = buffer.get() & 0xff;
				final int x = buffer.getShort() & 0xffff;
				final int y = buffer.getShort() & 0xffff;
				@SuppressWarnings("unused")
				final boolean cliped = buffer.get() == 1;
				WorldObject object = new WorldObject(objectId, type, rotation, x, y, plane);
				region.spawnObject(object, plane, object.getXInRegion(), object.getYInRegion(), false);
			}
			channel.close();
			in.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static void packObjectSpawns() {
		Logger.log("ObjectSpawns", "Packing object spawns...");
		if (!new File("data/map/packedSpawns").mkdir())
			throw new RuntimeException(
					"Couldn't create packedSpawns directory.");
		try {
			final BufferedReader in = new BufferedReader(new FileReader(
					"data/map/unpackedSpawnsList.txt"));
			while (true) {
				final String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				final String[] splitedLine = line.split(" - ");
				if (splitedLine.length != 2)
					throw new RuntimeException("Invalid Object Spawn line: "
							+ line);
				final String[] splitedLine2 = splitedLine[0].split(" ");
				final String[] splitedLine3 = splitedLine[1].split(" ");
				if (splitedLine2.length != 3 || splitedLine3.length != 4)
					throw new RuntimeException("Invalid Object Spawn line: "
							+ line);
				final int objectId = Integer.parseInt(splitedLine2[0]);
				final int type = Integer.parseInt(splitedLine2[1]);
				final int rotation = Integer.parseInt(splitedLine2[2]);

				final WorldTile tile = new WorldTile(
						Integer.parseInt(splitedLine3[0]),
						Integer.parseInt(splitedLine3[1]),
						Integer.parseInt(splitedLine3[2]));
				addObjectSpawn(objectId, type, rotation, tile.getRegionId(),
						tile, Boolean.parseBoolean(splitedLine3[3]));
			}
			in.close();
		} catch (final Throwable e) {
			Logger.handle(e);
		}
	}

	private ObjectSpawns() {
	}

}
