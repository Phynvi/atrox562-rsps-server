package com.rs;

import com.rs.game.WorldTile;

public final class Settings {

	//client/server settings
	public static final String SERVER_NAME = "Atrox";
	public static final int PORT_ID = 43594;
	public static final String CACHE_PATH = "data/562cache";
	public static final int RECEIVE_DATA_LIMIT = 5000;
	public static final int PACKET_SIZE_LIMIT = 5000;
	public static final int CLIENT_REVISION = 562;
	public static final int CUSTOM_CACHE_REVISION = 1;
	//welcome screen
	public static final String WeekTitle = "Latest Update";
	public static final String WeekMessage = "Dialogues & Bank improvements.";
	//world settings
	public static final int START_PLAYER_HITPOINTS = 10;
	public static final WorldTile START_PLAYER_LOCATION = new WorldTile(3164, 3484, 0);
	public static final WorldTile RESPAWN_PLAYER_LOCATION = new WorldTile(3164, 3484, 0);
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000; //30 seconds in ms
	public static final int WORLD_CYCLE_TIME = 600; //the speed of world in ms
	public static final int XP_RATE = 1;
	public static int DROP_RATE = 1;
	//mem settings
	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	public static final int LOCAL_NPCS_LIMIT = 250;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000; //30mb
	//game constants
	public static final int [] MAP_SIZES = {104, 120, 136, 168};
	//Town crier messages
	public static final String ANNOUNCEMENT1 = "2018-04-26: Released BETA!";
	public static final String ANNOUNCEMENT2 = "Donate to help us stay alive and get some extra benefits.";
	public static final String ANNOUNCEMENT3 = "Remember to vote to help us grow and claim your reward.";
	
	/**
	 * Quest, Music & Emote settings
	 */
	public static final int AIR_GUITAR_MUSICS_COUNT = 500;
	public static final int MAX_QUESTPOINTS = 4;
	
	/**
	 * Launching settings
	 */
	public static boolean DEBUG;
	public static boolean HOSTED;
	public static boolean ECONOMY;
	
	/**
	 * Donator settings
	 */
	public static String[] DONATOR_ITEMS = {  };
	
	public static String[] EXTREME_DONATOR_ITEMS = { };

	/**
	 * Item settings
	 */
	public static String[] EARNED_ITEMS = { "tokkul", "castle wars ticket", "(class",
			"sacred clay", "dominion", "sled", "magic carpet"};
	
	public static String[] REMOVING_ITEMS = { };
	
	public static String[] VOTE_REQUIRED_ITEMS = { };

	public static final String[] UNWANTED_WORDS = { };
	
	public static final String[] DEVELOPERS = {"sagacity", "mike"};
	public static String[] UNDEAD_NPCS = {"ghost", "zombie", "revenant", "skeleton", "abberant spectre", "banshee", "ghoul", "vampire", "skeletal"};
	
	private Settings() {
		
	}

}
