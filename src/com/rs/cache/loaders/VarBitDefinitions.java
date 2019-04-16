package com.rs.cache.loaders;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.io.InputStream;

public final class VarBitDefinitions {

	private static final ConcurrentHashMap<Integer, VarBitDefinitions> varpbitDefs = new ConcurrentHashMap<Integer, VarBitDefinitions>();

	public int varpBitId;
	public int varpId;
	public int tillBitshift;
	public int fromBitshift;

	public static final VarBitDefinitions getClientVarpBitDefinitions(int id) {
		VarBitDefinitions script = varpbitDefs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.getCacheFileManagers()[22].getFileData(id >>> 1416501898, id & 0x3ff);
		script = new VarBitDefinitions();
		script.varpBitId = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		varpbitDefs.put(id, script);
		return script;

	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			varpId = stream.readUnsignedShort();
			fromBitshift = stream.readUnsignedByte();
			tillBitshift = stream.readUnsignedByte();
		}
	}

	private VarBitDefinitions() {

	}
}