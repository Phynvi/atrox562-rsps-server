package com.rs.game.map.signposts;

import com.rs.game.player.Player;
import com.rs.game.WorldObject;

public final class Signposts {

	private static final void sendSignpost(final Player player, final SignpostsData data) {
		player.getPackets().sendIComponentText(135, 3, data.getNorthText());
		player.getPackets().sendIComponentText(135, 8, data.getEastText());
		player.getPackets().sendIComponentText(135, 9, data.getSouthText());
		player.getPackets().sendIComponentText(135, 12, data.getWestText());
		player.getInterfaceManager().sendInterface(135);
	}
	
	public static final boolean handleSignpost(final Player player, final WorldObject object) {
		if (object.getDefinitions().getName().equals("Signpost") || object.getDefinitions().getName().equals("signpost")) {
			for (SignpostsData data : SignpostsData.values()) {
				if (data.getWorldTile().getTileHash() == object.getTileHash()) {
					sendSignpost(player, data);
					return true;
				}
			}
		}
		return false;
	}
}
