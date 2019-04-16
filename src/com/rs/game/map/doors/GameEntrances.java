package com.rs.game.map.doors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import com.rs.utils.Utils;
import com.rs.utils.Logger;
import com.rs.game.WorldObject;
import com.rs.cache.loaders.ObjectDefinitions;

public class GameEntrances {

	private static final List<GameEntrances> DOORS = new ArrayList<GameEntrances>();
	private static final List<GameEntrances> GATES = new ArrayList<GameEntrances>();
	
	public static final int getGateId(WorldObject object) {
		for (GameEntrances gates : GATES) {
			if (gates.getOpenId() == object.getId())
				return gates.getClosedId();
			else if (gates.getClosedId() == object.getId())
				return gates.getOpenId();
		}
		return object.getId();
	}
	
	public static final int getDoorId(WorldObject object) {
		for (GameEntrances doors : DOORS) {
			if (doors.getOpenId() == object.getId())
				return doors.getClosedId();
			else if (doors.getClosedId() == object.getId())
				return doors.getOpenId();
		}
		return object.getId();
	}

	public static final void init() {
		if (!(new File("data/world/objects/packedEntrances.e").exists()))
			packEntrances();
		else
			loadEntrances();
	}

	private static final void loadEntrances() {
		try {
			Logger.log(GameEntrances.class, "Loading game entrance objects (doors, gates)..");
			RandomAccessFile in = new RandomAccessFile("data/world/objects/packedEntrances.e", "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				int type = buffer.get();
				int openId = buffer.getInt();
				int closedId = buffer.getInt();
				if (type == 0)
					DOORS.add(new GameEntrances(openId, closedId));
				else
					GATES.add(new GameEntrances(openId, closedId));
			}
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static final void packEntrances() {
		File file = new File("data/world/objects/unpackedEntrances.txt");
		if (!(file.exists())) {
			Logger.log(GameEntrances.class, "Writing game object entrances list..");
			createList();
		}
		Logger.log(GameEntrances.class, "Packing game object entrances (doors, gates)..");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s;
			while ((s = br.readLine()) != null) {
				if (s.isEmpty())
					continue;
				String[] args = s.split(" ");
				int openId = Integer.valueOf(args[1].replace(",", ""));
				int closedId = Integer.valueOf(args[2]);
				if (args[0].equals("Door:"))
					DOORS.add(new GameEntrances(openId, closedId));
				else
					GATES.add(new GameEntrances(openId, closedId));
			}
			br.close();
			RandomAccessFile out = new RandomAccessFile("data/world/objects/packedEntrances.e", "rw");
			for (GameEntrances doors : DOORS) {
				out.writeByte(0);
				out.writeInt(doors.openId);
				out.writeInt(doors.closedId);
			}
			for (GameEntrances gates : GATES) {
				out.writeByte(1);
				out.writeInt(gates.openId);
				out.writeInt(gates.closedId);
			}
			out.getChannel().close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void createList() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/world/objects/unpackedEntrances.txt"));
			primary: for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
				ObjectDefinitions defs = ObjectDefinitions.getObjectDefinitions(i);
				if (defs.models == null)
					continue primary;
				if (defs.containsOption("Open") || defs.containsOption("Close")) {
					boolean open = defs.containsOption("Open");
					loop: for (int x = 0; x < Utils.getObjectDefinitionsSize(); x++) {
						ObjectDefinitions d = ObjectDefinitions.getObjectDefinitions(x);
						if (d.models == null)
							continue loop;
						if ((open ? d.containsOption("Close") : d.containsOption("Open")) && defs.models[0] == d.models[0]) {
							if (defs.modelColour == null) {
								writer.flush();
								writer.newLine();
								writer.write((defs.name.toLowerCase().contains("door") ? "Door: " : "Gate: ") + i + ", " + x);
								continue primary;
							} else {
								if (d.modelColour != null && defs.modelColour[0] == d.modelColour[0]) {
									writer.flush();
									writer.newLine();
									writer.write((defs.name.toLowerCase().contains("door") ? "Door: " : "Gate: ") + i + ", " + x);
									continue primary;
								}
							}
						}
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final int openId;
	private final int closedId;

	public GameEntrances(final int openId, final int closedId) {
		this.openId = openId;
		this.closedId = closedId;
	}

	public int getOpenId() {
		return openId;
	}

	public int getClosedId() {
		return closedId;
	}
}
