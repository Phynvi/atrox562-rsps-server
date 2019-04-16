package com.rs.game.map.signposts;

import com.rs.game.WorldTile;

public enum SignpostsData {

	LUMBRIDGE(new WorldTile(3235, 3228, 0), "Head north towards Fred's farm and the windmill.", "Cross the bridge and head east to Al-Kharid or north to Varrock.", "South to the swamps of Lumbridge", "West to the Lumbridge Castle and Draynor Village. Beware of the goblins!"),
	DRAYNOR(new WorldTile(3107, 3296, 0), "North to Draynor Manor", "East to Lumbridge", "South to Draynor Village and the Wizards' Tower.", "West to Port Sarim, Falador and Rimmington."),
	LUMBRIDGE_BRIDGE(new WorldTile(3261, 3260, 2), "North to farms and Varrock.", "East to Al-Kharid - toll gate; bring some money.", "The River Lum lies to the South.", "West to Lumbridge."),
	VARROCK_RAT(new WorldTile(3268, 3332, 2), "Sheep lay this way.", "East to Al-Kharid mine and follow the path North to Varrock east gate.", "South through farms to Al-Kharid and Lumbridge.", "West to Champions' Guild and Varrock South gate."),
	VARROCK(new WorldTile(3223, 3427, 0), "North to Varrock mine and Varrock East gate.", "Follow the path East to the Digsite.", "South to large Mining area and Al-Kharid.", "West to Champion's Guild and Varrock South gate."),
	RIMMINGTON(new WorldTile(2983, 3278, 0), "North to the glorious White Knights city of Falador.", "Follow the path East to Port Sarim and Draynor Village.", "Follow the path South to Rimmington.", "Follow the path West to the Crafting Guild.");
	
	private final WorldTile worldtile;
	private final String north, east, south, west;
	
	private SignpostsData(final WorldTile worldtile, final String north, final String east, final String south, final String west) {
		this.worldtile = worldtile;
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}
	
	public final WorldTile getWorldTile() {
		return worldtile;
	}
	
	public final String getNorthText() {
		return north;
	}
	
	public final String getEastText() {
		return east;
	}
	
	public final String getSouthText() {
		return south;
	}
	
	public final String getWestText() {
		return west;
	}
}
