package com.rs.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class NPCCharmDrops {

	private final static HashMap<String, NPCCharms> NPCCHARMS = new HashMap<String, NPCCharms>();
	private final static String UNPACKED_PATH = "data/npcs/Charm rate.txt";

	public static final NPCCharms getCharm(String npc) {
		NPCCharms charms = NPCCHARMS.get(npc);
		if (charms != null)
			return charms;
		return null;
	}

	public static void init() {
		Logger.log("NPC Charms", "Reading NPC charms data..");
		try {
			BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				String[] splitedLine = line.split(" - ", 2);
				String npcName = splitedLine[0];
				String[] rates = splitedLine[1].split(" ");
				int amount = Integer.valueOf(rates[0]);
				double goldRate = Double.valueOf(rates[1]);
				double greenRate = Double.valueOf(rates[2]);
				double crimsonRate = Double.valueOf(rates[3]);
				double blueRate = Double.valueOf(rates[4]);
				NPCCHARMS.put(npcName, new NPCCharms(amount, goldRate, greenRate, crimsonRate, blueRate));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}