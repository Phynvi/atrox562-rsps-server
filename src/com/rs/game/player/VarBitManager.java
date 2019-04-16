package com.rs.game.player;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.Cache;
import com.rs.cache.loaders.VarBitDefinitions;

public class VarBitManager {
	
	private Map<Integer, Integer> VarpValues = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> VarbitValues = new HashMap<Integer, Integer>();
	private Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public VarBitManager(Player player) {
		this.player = player;
	}

	public void sendVar(int id, int value) {
		player.getPackets().sendConfig(id, value);
		VarpValues.put(id, value);
	}

	public void sendVarbit(int id, int value) {
		player.getPackets().sendConfigByFile(id, value);
		VarbitValues.put(id, value);
	}

	public Integer getBitValue(int id) {
		if (VarbitValues.containsKey(id))
			return VarbitValues.get(id);
		else
			return 0;
	}

	public Integer getValue(int id) {
		return VarpValues.get(id);
	}

}