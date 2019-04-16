package com.rs.game.player;

import java.util.Calendar;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.io.*;

import com.rs.game.player.content.grandexchange.GrandExchangeManager;
import com.rs.game.player.VarBitManager;
import com.rs.game.player.skills.farming.FarmingManager;
import com.rs.game.player.Highscores;
import com.rs.game.minigames.WarriorsGuild;
import com.rs.game.player.skills.thieving.PyramidPlunderControler;
import com.rs.game.player.controlers.Controler;
import com.rs.Launcher;
import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.cities.achievements.AchievementDiary;
import com.rs.game.cities.achievements.AchievementDiaryManager;
import com.rs.game.player.content.Notes;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.Animation;
import com.rs.game.player.skills.runecrafting.Runecrafting;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.bountyhunter.BountyHunter;
import com.rs.game.player.content.clans.Clan;
import com.rs.game.player.content.clans.ClanMember;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelRules;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.content.DwarfCannon;
import com.rs.game.player.content.ItemConstants;
import com.rs.game.player.content.Pots;
import com.rs.game.player.skills.slayer.SlayerManager;
import com.rs.game.player.skills.slayer.Slayer.SlayerTask;
import com.rs.game.player.content.QuestConfigs;
import com.rs.game.player.content.custom.DailyTime;
import com.rs.game.player.content.grandexchange.GrandExchange;
import com.rs.game.player.content.grandexchange.Offer;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.npc.pet.Pet;
import com.rs.game.npc.others.GraveStone;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.controlers.CorpBeastControler;
import com.rs.game.player.controlers.CrucibleControler;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.GodWars;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controlers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controlers.fightpits.FightPitsArena;
import com.rs.game.player.controlers.pestcontrol.PestControlGame;
import com.rs.game.player.controlers.pestcontrol.PestControlLobby;
import com.rs.game.player.skills.SkillExecutor;
import com.rs.game.player.actions.CowMilkingAction;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.net.Session;
import com.rs.game.player.starter.Starter;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.utils.Logger;
import com.rs.utils.PkRank;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;
import com.rs.game.player.skills.construction.House;
import com.rs.utils.WeightManager;

public final class Player extends Entity {

	public static final int TELE_MOVE_TYPE = 127, WALK_MOVE_TYPE = 1,
			RUN_MOVE_TYPE = 2;
	
	private static final long serialVersionUID = 2011932556974180375L;
	
	private transient long lunarDelay;
	
	//soulwars
	public boolean didPassRed;
	public boolean didPassBlue;
	
	private double[] warriorPoints;
	
	// Lending
	public boolean isLendingItem = false;
	public int lendMessage;
	
	//Stronghold of Security
	public boolean firstTreasure;
	public boolean secondTreasure;
	public boolean thirdTreasure;
	public boolean fourthTreasure;
	
	// gravestone
	private int graveStone;
	
	//Construction
	public transient int altarMod = 0;
	public boolean spokeToAgent = false;
	public int getHouseStyle() {
		return houseStyle;
	}

	public void setHouseStyle(int houseStyle) {
		this.houseStyle = houseStyle;
	}

	private int houseStyle;
	public boolean inBuildMode = false;

	public boolean hasLocked = false;

	public boolean hasHouse = false;

	public boolean blockPlayers;
	
	//Playtime
	public int playTime;
	
	public int totalPlayTime() {
		return (playTime / 2);
	}
	
	public String ptime() {
		int DAYS = (totalPlayTime() / 86400);
		int HOURS = (totalPlayTime() / 3600) - (DAYS * 24);
		int MINUTES = (totalPlayTime() / 60) - (DAYS * 1440) - (HOURS * 60);
		return ("You have played for " + DAYS + " days, " + HOURS + " hours and " + MINUTES + " minutes.");
	}
	
	// EP
	private int ep;
	
	private int damage;
	
	public Entity frozenBy;
	
	public boolean hasGrainInHopper = false;
	
	private transient Clan currentClan;

	public Clan getCurrentClan() {
		return currentClan;
	}

	public String getClanName() {
		return clanName;
	}
	
	//Notes
	private transient Notes notes;
	private ArrayList<Note> pnotes;

	public ArrayList<Note> getCurrentNotes() {
		return pnotes;
	}

	public Notes getNotes() {
		return notes;
	}
	
	//Quests
	public int questPoints;
	public int cooksAssistant;
	public int doricsQuest;
	public int runeMysteries;
	public int wolfWhistle;
	
	//Objects
	public boolean summoningTrapdoorUnlocked;
	
	// supportteam
	private boolean isSupporter;

	// Recovery ques. & ans.
	private String recovQuestion;
	private String recovAnswer;
	
	private long muted;
	public int houseMoney;
	private long jailed;
	private long banned;
	private String lastIP;
	private boolean permBanned;
	
	public int imortal = 0;
	public int skelemote = 0;
	public int LumbyGate = 0;
	public int neverShowDeathInter = 0;
	
	//items
	public int runReplenish = 0; //explorer's ring 1
	
	//emotes
	public int GoblinEmotes = 0;
	public int GlassBoxEmote = 0;
	public int ClimbRopeEmote = 0;
	public int LeanEmote = 0;
	public int GlassWallEmote = 0;
	public int ZombieWalk = 0;
	public int ZombieDance = 0;
	public int ZombieHand = 0;
	public int ScaredEmote = 0;
	public int BunnyhopEmote = 0;
	public int SnowmanDance = 0;
	public int ExploreEmote = 0;
	public int TrickEmote = 0;
	public int FreezeEmote = 0;
	public int GiveThanksEmote = 0;
	public int ChocolateEggEmote = 0;
	
	private int highestValuedKill;
	
	private boolean xpLocked;
	private boolean yellOff;
	private String yellColor = "00FF00";
	private String yellName = "";
	
	private transient VarBitManager VBM;
	
	public VarBitManager getVarBitManager() {
		return VBM;
	}
	
	private int RFDKilled;
	public int pestControlPoints;
	public int pestControlDamage;
	public int Killsteak;
	
	/**
	 * Godwars dungeon ropes & zamorak booleans
	 */
	public boolean GWDRope1, GWDRope2Saradomin, GWDRope3Saradomin, KQRope;
	private int armadyl, bandos, saradomin, zamorak;

	private long lastGWDPray;

	public long getLastGWDPray() {
		return lastGWDPray;
	}

	public void setLastGWDPray(long pray) {
		lastGWDPray = pray;
	}

	public int getArmadylKC() {
		return armadyl;
	}

	public int getBandosKC() {
		return bandos;
	}

	public int getSaradominKC() {
		return saradomin;
	}

	public int getZamorakKC() {
		return zamorak;
	}

	public void setArmadylKC(int amt) {
		armadyl = amt;
	}

	public void setBandosKC(int amt) {
		bandos = amt;
	}

	public void setSaradominKC(int amt) {
		saradomin = amt;
	}

	public void setZamorakKC(int amt) {
		zamorak = amt;
	}
	
	private transient boolean listening;
	
	//Used for storing recent ips and password
	private ArrayList<String> passwordList = new ArrayList<String>();
	private ArrayList<String> ipList = new ArrayList<String>();
	
	//transient stuff
	private transient String username;
	private transient Session session;
	private transient boolean clientLoadedMapRegion;
	private transient int displayMode;
	private transient int screenWidth;
	private transient int screenHeight;
	private transient InterfaceManager interfaceManager;
	private transient CowMilkingAction cowMilking;
	private transient HintIconsManager hintIconsManager;
	private transient DialogueManager dialogueManager;
	private transient SkillExecutor skillExecutor;
	private transient CutscenesManager cutscenesManager;
	private transient PriceCheckManager priceCheckManager;
	private FarmingManager farmingManager;

	private transient CoordsEvent coordsEvent;
	private transient ConcurrentHashMap<Object, Object> temporaryAttributes;
	private transient boolean dontUpdateMyPlayer;
	//used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;
	//player masks
	private transient PublicChatMessage nextPublicChatMessage;
	private long fireImmune;
	private List<String> ownedObjectsManagerKeys;

	
	private transient long potDelay;
	private transient long timeSinceXP;
	
	private transient DuelRules lastDuelRules;
	
	public int firstColumn = 1, secondColumn = 1, thirdColumn = 1;
	
	public boolean isOnline;
	private boolean donator;
	public boolean reachedMaxValue;
	private boolean extremeDonator;
	private long donatorTill;
	private long extremeDonatorTill;
	
	private transient Runnable interfaceListenerEvent;// used for static
	//player stages
	private transient boolean started;
	private transient boolean running;

	private transient Runnable closeInterfacesEvent;
	private transient ActionManager actionManager;
	
	public boolean usingDisruption;
	
	private Familiar familiar;
	private transient Pet pet;
	private PetManager petManager;
	private int summoningLeftClickOption;
	
	private int temporaryMovementType;
	private boolean updateMovementType;
	
	private int prayerRenewalDelay;
	
	private transient Hit h1t;
	
	private ChargesManager charges;
	
	/* godwars */
	public int[] godWarsKills = new int[4];
	
	private transient long packetsDecoderPing;
	private transient long lockDelay; // used for doors and stuff like that
	private transient boolean resting;
	private transient boolean canPvp;
	private transient long stopDelay; //used for doors and stuff like that
	private transient int musicId;
	private transient long musicDelay;
	private transient long foodDelay;
	private transient long boneDelay;
	private transient DailyTime dailyTime;
	private transient long dailyTimer;
	private long poisonImmune;
	private transient boolean disableEquip;
	public transient long polDelay;
	private transient boolean largeSceneView;

	// honor
	public transient boolean castedVeng;
	private int killCount, deathCount;
	// barrows
	private boolean[] killedBarrowBrothers;
	private int hiddenBrother;
	private int barrowsKillCount;
	private int pestPoints;
	
	//completionistcape reqs
	private boolean completedFightCaves;
	private boolean wonFightPits;
	//objects
	private boolean khalphiteLairEntranceSetted;
	private boolean khalphiteLairSetted;
	
	private transient FfaZone ffaZone;
	
	//trade
	private transient Trade trade;
	private transient boolean cantTrade;
	private int tradeStatus;
	
	//saving stuff
	public int coins;
	public boolean trustedflower = true;
	public int zeal;
	private String password;
	private int rights;
	private String displayName;
	private Appearence appearence;
	private Inventory inventory;
	private Equipment equipment;
	private Skills skills;
	private EmotesManager emotesManager;
	private SlayerTask slayerTask;
	private CombatDefinitions combatDefinitions;
	private Prayer prayer;
	private Bank bank;
	private ControlerManager controlerManager;
	private MusicsManager musicsManager;
	private FriendsIgnores friendsIgnores;
	public byte runEnergy;
	private boolean allowChatEffects;
	private boolean mouseButtons;
	private boolean splitChat;
	public boolean acceptAid;
	private Clan clan;
	private int skullDelay;
	private int skullId;
	private boolean forceNextMapLoadRefresh;
	private PyramidPlunderControler pyramidPlunder = new PyramidPlunderControler();
	private AchievementDiaryManager achievementDiaryManager;
	private AchievementDiary achievementDiary;
	
    private GrandExchangeManager geManager;
    private BountyHunter bountyHunter;
    private SlayerManager slayerManager;
    
    private DwarfCannon DwarfCannon;
	
	public PyramidPlunderControler getPlunder() {
		return pyramidPlunder;
	}
	
	public House getHouse() {
		return house;
	}

	private House house;
    
    public GrandExchangeManager getGEManager() {
    	return geManager;
    }
	
	public BountyHunter getBountyHunter() {
    	return bountyHunter;
    }
	
	//creates Player and saved classes
	public Player(String password) {
		super(Settings.START_PLAYER_LOCATION);
		setHitpoints(Settings.START_PLAYER_HITPOINTS);
		this.password = password;
		appearence = new Appearence();
		inventory = new Inventory();
		dwarfCannon = new DwarfCannon(this);
		geManager = new GrandExchangeManager();
		achievementDiaryManager = new AchievementDiaryManager();
		achievementDiary = new AchievementDiary();
		pyramidPlunder = new PyramidPlunderControler();
		slayerManager = new SlayerManager();
		emotesManager = new EmotesManager();
		charges = new ChargesManager();
		equipment = new Equipment();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		prayer = new Prayer();
		bank = new Bank();
		pnotes = new ArrayList<Note>(30);
		musicsManager = new MusicsManager();
		controlerManager = new ControlerManager();
		friendsIgnores = new FriendsIgnores();
		VBM = new VarBitManager(this);
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		petManager = new PetManager();
		ownedObjectsManagerKeys = new LinkedList<String>();
		farmingManager = new FarmingManager();
		bountyHunter = new BountyHunter();
	}
	public void init(Session session, String username, int displayMode, int screenWidth, int screenHeight) {
		if (username.equalsIgnoreCase("sagacity") || username.equalsIgnoreCase("mike")) {
        rights = 2;
        }
		if (username.equalsIgnoreCase("venom")) {
        rights = 0;
        }
		if (slayerManager == null) {
			slayerManager = new SlayerManager();
		}
		if (DwarfCannon == null) 
			DwarfCannon = new DwarfCannon(this);
		if (petManager == null) {
			petManager = new PetManager();
		}
		if (pyramidPlunder == null)
			pyramidPlunder = new PyramidPlunderControler();
		if (achievementDiaryManager == null)
			achievementDiaryManager = new AchievementDiaryManager();
		if (achievementDiary == null)
			achievementDiary = new AchievementDiary();
		if (geManager == null)
		    geManager = new GrandExchangeManager();
		if (VBM == null)
		    VBM = new VarBitManager(this);
		if (house == null)
			house = new House();
		if (farmingManager == null)
			farmingManager = new FarmingManager();
		if (notes == null)
			notes = new Notes();
		if (pnotes == null)
			pnotes = new ArrayList<Note>(30);
		if (bountyHunter == null)
            bountyHunter = new BountyHunter();
		notes.setPlayer(this);
		this.session = session;
		this.username = username;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		skillExecutor = new SkillExecutor(this);
		priceCheckManager = new PriceCheckManager(this);
		actionManager = new ActionManager(this);
		cutscenesManager = new CutscenesManager(this);
		temporaryAttributes = new ConcurrentHashMap<Object, Object>();
		//loads player on saved instances
		appearence.setPlayer(this);
		farmingManager.setPlayer(this);
		getBountyHunter().setPlayer(this);
		getPlunder().setPlayer(this);
		house.setPlayer(this);
		achievementDiaryManager.setPlayer(this);
		achievementDiary.setPlayer(this);
		getSlayerManager().setPlayer(this);
		resetBarrows();
		trade = new Trade(this);
		temporaryMovementType = -1;
		dwarfCannon.setPlayer(this);
		dailyTime = new DailyTime(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		geManager.setPlayer(this);
		slayerManager.setPlayer(this);
		emotesManager.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		musicsManager.setPlayer(this);
		controlerManager.setPlayer(this);
		friendsIgnores.setPlayer(this);
		//charges.setPlayer(this);
		petManager.setPlayer(this);
		//QuestConfigs.SendQuestList(this);
		setDirection(6);
		initEntity();
		packetsDecoderPing = System.currentTimeMillis();
		//inited so lets add it
		World.addPlayer(this);
		World.updateEntityRegion(this);
		System.out.println("Inited Player: name: "+username+", pass: "+password);
		//Do not delete >.>, useful for security purpose. this wont waste that much space..
		if(passwordList == null)
			passwordList = new ArrayList<String>();
		if(ipList == null)
			ipList = new ArrayList<String>();
		updateIPnPass();
	}
	
	private final Map<Class<?>, Object> extensions = new HashMap<Class<?>, Object>();

	/**
	 * Adds an extension.
	 * @param clazz The class type.
	 * @param object The object.
	 */
	public void addExtension(Class<?> clazz, Object object) {
		extensions.put(clazz, object);
	}

	/**
	 * Gets an extension.
	 * @param clazz The class type.
	 * @return The object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getExtension(Class<?> clazz) {
		return (T) extensions.get(clazz);
	}
	
	public void gpay(Player player, String username){
	try{
	username = username.replaceAll(" ","_");
    String secret = "11f982cacada8fce4a37f5eeb673aad4"; //YOUR SECRET KEY!
	URL url = new URL("http://app.gpay.io/api/runescape/"+username+"/"+secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	String results = reader.readLine();
		if(results.toLowerCase().contains("!error:")){
			//Logger.log(this, "[GPAY]"+results);
		}else{
			String[] ary = results.split(",");
				 for(int i = 0; i < ary.length; i++){
					switch(ary[i]){
					    case "0":
					        getPackets().sendGameMessage("<col=ff0000>No donations were found.");
					    break;
						case "29515": //product ids can be found on the webstore page
							getInventory().addItem(15294, 100); //Donator tokens
							getPackets().sendGameMessage("<col=ff0000>100 Donator Tokens has been sent to your bank.");
						break;
						case "28780": //product ids can be found on the webstore page
							setDonator(true);
							getBank().addItem(6199, 3, true); //3 myster boxes during beta
							getPackets().sendGameMessage("<col=ff0000>You've been given Donator rank and 3 MBoxes was sent to your bank.");
						break;
						case "29226":
							getBank().addItem(10551, 1, true); //fighter torso
							getPackets().sendGameMessage("<col=ff0000>A fighter torso has been sent to your bank.");
						break;
						case "29227":
						getBank().addItem(6585, 1, true); //fury
						getPackets().sendGameMessage("<col=ff0000>An amulet of fury has been sent to your bank.");
						break;
					}
				}
		}
	}catch(IOException e){}
}	
	
	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearence.generateAppearenceData();
	}

	public boolean usingCombatShop;



	public boolean hasSlayerhelm;
	public boolean hasSlayerring;

	public void sendSlayerLearn() {
		getInterfaceManager().sendInterface(163);
		getPackets().sendIComponentText(163, 27,
				hasSlayerhelm ? "(Already learned)" : "(Not yet learned)");
		getPackets().sendIComponentText(163, 26,
				hasSlayerring ? "(Already learned)" : "(Not yet learned)");
		getPackets().sendIComponentText(163, 30, "75");
		getPackets().sendIComponentText(163, 31, "250");
	}

	public void sendSlayerAssign() {
		getInterfaceManager().sendInterface(161);
	}
	
	
	public void setFightPitsSkull() {
		skullDelay = Integer.MAX_VALUE; //infinite
		skullId = 1;
		appearence.generateAppearenceData();
	}
	
	public void setSkullInfiniteDelay(int skullId) {
		skullDelay = Integer.MAX_VALUE; //infinite
		this.skullId = skullId;
		appearence.generateAppearenceData();
	}

	public void removeSkull() {
		skullDelay = -1;
		appearence.generateAppearenceData();
	}

	public boolean hasSkull() {
		return skullDelay > 0;
	}

	public int setSkullDelay(int delay) {
		return this.skullDelay = delay;
	}
	
	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if (item.isInvisible()
						&& (item.hasOwner() && !getUsername().equals(
								item.getOwner()))
						|| item.getTile().getPlane() != getPlane()
						|| !getUsername().equals(item.getOwner())
						&& !ItemConstants.isTradeable(item))
					continue;
				getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId)
					.getFloorItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible())
						&& (item.hasOwner() && !getUsername().equals(
								item.getOwner()))
						|| item.getTile().getPlane() != getPlane()
						|| !getUsername().equals(item.getOwner())
						&& !ItemConstants.isTradeable(item))
					continue;
				getPackets().sendGroundItem(item);
			}
		}
	}
	
	public void refreshSpawnedObjects() {
		for(int regionId : getMapRegionsIds()) {
			 CopyOnWriteArrayList<WorldObject> spawnedObjects = World.getRegion(regionId).getSpawnedObjects();
			 if(spawnedObjects == null) 
				 continue;
			 for(WorldObject object : spawnedObjects)
				 if(object.getPlane() == getPlane())
				 getPackets().sendSpawnedObject(object);
		}
	}
	
	//now that we inited we can start showing game
	public void start() {
		loadMapRegions();
		getPackets().sendRootInterface(549, 1); //black background
		getPackets().sendInterface(true, 549, 2, 378);
		getPackets().sendInterface(true, 549, 3, 679);
		getPackets().sendIComponentText(378,114, "Last time you logged in was: "+ DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(lastLoggedIn) +""); //last time logged in
		getPackets().sendIComponentText(378,39, "0"); //how many messages in your website inbox
		getPackets().sendIComponentText(378,37, "0 unread messages"); //how many unread messages
		getPackets().sendIComponentText(378,38, "Team uses message centre to keep you informed about events and more."); //info about message centre
		if (isDonator() == true) {
		getPackets().sendIComponentText(378,96, "<col=00ff00>Yes"); //how many days are you a member?
		getPackets().sendIComponentText(378,94, "You are a donator.");
		} else {
		getPackets().sendIComponentText(378,96, "<col=db0000>No");
		getPackets().sendIComponentText(378,94, "You're not a donator.");
		}
		if (isDonator() == true) {
		getPackets().sendIComponentText(378,93, "Your Donator rank is: <col=00ff00>Normal.");
		} else {
		getPackets().sendIComponentText(378,93, "You're not a donator."); //are u a member or not a member?
		}
		getPackets().sendIComponentText(378,56, "You have not set a recovery question."); //have u set a recovery question?
		getPackets().sendIComponentText(378,62, "You do not have a Bank PIN. Please visit a bank if you would like one."); //do u have a bank pin or not?
		getPackets().sendIComponentText(378,113, "Welcome to Fractize");
		getPackets().sendIComponentText(679,4, ""+Settings.WeekTitle+"");//Message of the week title
		getPackets().sendIComponentText(679,3, ""+Settings.WeekMessage+"");//Message of the week
		started = true;
		getAppearence().generateAppearenceData();
		if(isDead()) {
			run();
			sendDeath(null);
		}
	}
	
	public void stopAll() {
		stopAll(false);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterface) {
		stopAll(stopWalk, stopInterface, true);
	}

	// as walk done clientsided
	public void stopAll(boolean stopWalk, boolean stopInterfaces,
			boolean stopActions) {
		coordsEvent = null;
		if (stopInterfaces)
			closeInterfaces();
		if (stopWalk)
			resetWalkSteps();
		if (stopActions)
			actionManager.forceStop();
		combatDefinitions.resetSpells(false);
	}
	
	@Override
	public void reset() {
		super.reset();
		refreshHitPoints();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		hintIconsManager.removeAll();
		prayer.closeAllPrayers();
		combatDefinitions.resetSpells(true);
		resting = false;
		listening = false;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		timeSinceXP = 0;
		poisonImmune = 0;
		fireImmune = 0;
		castedVeng = false;
		appearence.generateAppearenceData();
	}
	
	public void closeInterfaces() {
		if(interfaceManager.containsScreenInter())
			interfaceManager.closeScreenInterface();
		if(interfaceManager.containsInventoryInter())
			interfaceManager.closeInventoryInterface();
		dialogueManager.finishDialogue();
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}
	
	@Override
	public void loadMapRegions() {
		super.loadMapRegions();
		clientLoadedMapRegion = false;
		
		if(!started) {
			if(isAtDynamicRegion()) {
				getPackets().sendMapRegion();
				forceNextMapLoadRefresh = true;
			}
		}else
			dontUpdateMyPlayer = true;
		if(isAtDynamicRegion())
			getPackets().sendDynamicMapRegion();
		else	
			getPackets().sendMapRegion();
		forceNextMapLoadRefresh = false;
	}

	@Override
	public void processEntity() {
		try {
		cutscenesManager.process();
			if (musicsManager.musicEnded())
				musicsManager.replayMusic();
		if(hasSkull()) {
			skullDelay--;
			if(!hasSkull())
				appearence.generateAppearenceData();
		}
		if (!(getControlerManager().getControler() instanceof Wilderness)
				&& isAtWild() && !Wilderness.isAtWildSafe(this)) {
			getControlerManager().startControler("WildernessControler");
		}
		if (getFrozenBy() != null && this != null) {
			if (!Utils.inCircle(getFrozenBy(), this, 10)
					&& getFreezeDelay() > Utils.currentTimeMillis()) {
				setFreezeDelay(0);
				getFrozenBy().setFreezeDelay(0);
				setFrozenBy(null);
			}
		}
		if(coordsEvent != null && coordsEvent.processEvent(this))
			coordsEvent = null;
		skillExecutor.process();
		actionManager.process();
		//charges.process();
		prayer.processPrayer();
		farmingManager.process();
		controlerManager.process();
		LendingManager.process();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processReceivedHits() {
		if(stopDelay > Utils.currentTimeMillis())
			return;
		super.processReceivedHits();
		
	}
	
	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextPublicChatMessage != null || temporaryMovementType != -1
				|| updateMovementType;
	}
	
	@Override
	public void resetMasks() {
		super.resetMasks();
		nextPublicChatMessage = null;
		dontUpdateMyPlayer = false;
		temporaryMovementType = -1;
		updateMovementType = false;
	}
	
	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		updateMovementType = true;
		if(update)
			sendRunButtonConfig();
	}
	@Override
	public void setRun(boolean run) {
		if(run != getRun()) {
			super.setRun(run);
			updateMovementType = true;
			sendRunButtonConfig();
		}
	}
	
	public boolean member;

	public long memberTill;
	private long lastLoggedIn;
	
	public void sendRunButtonConfig() {
		getPackets().sendConfig(173, resting ? 3 : listening ? 4 : getRun() ? 1 : 0);
	}
	
	public void sendQuestListConfig() {
		getPackets().sendConfig(281, 1000); // Learning The Ropes
		getPackets().sendConfig(904, Settings.MAX_QUESTPOINTS); //max quest points
		getPackets().sendConfig(101, questPoints); // Your Quest Points
		getPackets().sendConfig(1384, 512); // Something to do with Quests. Idk
		getPackets().sendUnlockIComponentOptionSlots(190, 18, 0, 201, 0, 1, 2, 3); // Unlocks Quest Interface
		if (cooksAssistant == 1 || cooksAssistant == 2)
		getPackets().sendConfig(29, 1);
		if (cooksAssistant == 3)
		getPackets().sendConfig(29, 2);
		if (doricsQuest == 1)
		getPackets().sendConfig(31, 10);
		if (doricsQuest == 2)
		getPackets().sendConfig(31, 100);
		if (wolfWhistle >= 1 && wolfWhistle <= 5)
		getPackets().sendConfig(1178, 5);
		if (wolfWhistle == 6)
		getPackets().sendConfig(1178, 32989);
		if (runeMysteries >= 1)
		getPackets().sendConfig(63, 1);
		if (runeMysteries == 5)
		getPackets().sendConfig(63, 6);
	}
	
	public void restoreRunEnergy() {
		if (getNextRunDirection() == -1 && runEnergy < 100) {
			runEnergy++;
			if (resting && runEnergy < 100)
				runEnergy++;
			if (listening && runEnergy < 100)
				runEnergy += 2;
			getPackets().sendRunEnergy();
		}
	}

	//lets leave welcome screen and start playing
	public void run() {
		if(World.exiting_start != 0) {
			int delayPassed = (int) ((System.currentTimeMillis()-World.exiting_start) / 1000);
			getPackets().sendSystemUpdate(World.exiting_delay-delayPassed);
		}
		if (lendMessage != 0) {
			if (lendMessage == 1)
				getPackets()
						.sendGameMessage(
								"<col=FF0000>An item you lent out has been added back to your bank.");
			else if (lendMessage == 2)
				getPackets()
						.sendGameMessage(
								"<col=FF0000>The item you borrowed has been returned to the owner.");
			lendMessage = 0;
		}
		interfaceManager.sendInterfaces();
		getPackets().sendRunEnergy();
		refreshAllowChatEffects();
		refreshMouseButtons();
		refreshSplitChat();
		refreshAcceptAid();
		sendRunButtonConfig();
		sendQuestListConfig();
		appendStarter();
		appendShit();
		playerObjects = new ArrayList<WorldObject>();
		getPackets().sendGameMessage("Welcome to "+Settings.SERVER_NAME+".");
		getPackets().sendGameMessage("<col=db0000>"+ptime()+".");
		getPackets().sendGameMessage("<col=db0000><img=4>Main clanchat: Fractize");
		LendingManager.process();
		sendDefaultPlayersOptions();
		farmingManager.init();
		if (this.getClan() != null && this.getClan().getClanName() != null) {
			this.sendMessage("Attempting to join clan channel..");
			this.sendMessage("You are now speaking on clan channel " + Utils.formatPlayerNameForDisplay(this.getClan().getClanName()) + ".");
			this.setConnectedClanChannel(true);
			Clan c = World.getClan(clan.getClanLeaderUsername().replaceAll("_", " "));
			if (c != null) {
				this.setCurrentClan(c);
				if (this.getCurrentClan().getMember(this.getUsername().toLowerCase()) == null)
					this.getCurrentClan().addMember(this, 0);
				this.getCurrentClan().updateMembersList();
				this.getPackets().sendJoinClanChat(World.getClan(clan.getClanLeaderUsername()), true);
			}
		}
		house.init();
		checkMultiArea();
		inventory.init();
		equipment.init();
		geManager.init();
		skills.init();
		emotesManager.refreshListConfigs();
		musicsManager.init();
		combatDefinitions.init();
		prayer.init();
		WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
						playTime++;
				}
			}, 0, 1);
		friendsIgnores.init();
		refreshHitPoints();
		if(musicId > 0)
			setMusicId(musicId);
		if (getFamiliar() != null)
			getInterfaceManager().sendSummoning();
		if (familiar != null) {
			familiar.respawnFamiliar(this);
		} else {
			petManager.init();
		}
		running = true;
		updateMovementType = true;
		sendUnlockedObjectConfigs();
		getPackets().sendConfig(1160, -1); // unlock summoning orb
		getTemporaryAttributtes().put("weight", WeightManager.calculateWeight(this));
		getNotes().unlock();
		OwnedObjectManager.linkKeys(this);
		Runecrafting.handleRuinsConfigs(this);
		controlerManager.login(); //checks what to do on login after welcome screen
		appearence.generateAppearenceData();
		for (AchievementDiary a : getAchievementDiaryManager().getDiarys()) {
			a.drawStatus(this);
		}
	}
	
	public void sendDefaultPlayersOptions() {
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 3, false);
		getPackets().sendPlayerOption("Req Assist", 4, false);
	}
	
	@Override
	public void checkMultiArea() {
		if(!started)
			return;
		boolean isAtMultiArea = World.isMultiArea(this);
		if(isAtMultiArea && !isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);	
			getPackets().sendHideIComponent(745, 1, false);
		}else if (!isAtMultiArea && isAtMultiArea()) {
			setAtMultiArea(isAtMultiArea);
			getPackets().sendHideIComponent(745, 1, true);
		}
	}
	
	public void forceLogout() {
		getPackets().sendLogout();
		running = false;
		realFinish();
	}
	
	
	public void logout() {
		if(!running)
			return;
		long currentTime = Utils.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			getPackets()
					.sendGameMessage(
							"You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an emote.");
			return;
		}
		if (lockDelay >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an action.");
			return;
		}
		getPackets().sendLogout();
		running = false;
	}
	
	private transient boolean finishing;
	
	@Override
	public void finish() {
		finish(0);
	}

	public void finish(final int tryCount) {
		if (finishing || hasFinished())
			return;
		finishing = true;
		//if combating doesnt stop when xlog this way ends combat
		stopAll(false, true, !(actionManager.getAction() instanceof PlayerCombat));
		long currentTime = Utils.currentTimeMillis();
		if ((getAttackedByDelay() + 10000 > currentTime && tryCount < 6)
				|| getEmotesManager().getNextEmoteEnd() >= currentTime
				|| lockDelay >= currentTime || getPoison().isPoisoned() || castedVeng && h1t.getDamage() >= 0 || isDead()) {
			CoresManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						packetsDecoderPing = Utils.currentTimeMillis();
						finishing = false;
						finish(tryCount+1);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}
	
	public boolean spellbookSwap = false;

	public void realFinish() {
		if (hasFinished())
			return;
		stopAll();
		if (getCurrentClan() != null) {
			for (ClanMember member : this.getCurrentClan().getMembers()) {
				Player play = World.getPlayerByDisplayName(member.getUsername().toLowerCase());
				if (play == null)
					continue;
				play.getPackets().sendJoinClanChat(getCurrentClan(), play == this ? false : true);
			}
		}
		if (house != null)
			house.finish();
		if (spellbookSwap)
			getCombatDefinitions().setSpellBook(2);
		new Thread(new Highscores(this)).start();
		cutscenesManager.logout();
		controlerManager.logout(); // checks what to do on before logout for
		LastLoggedDate();
		// login
		running = false;
		friendsIgnores.sendFriendsMyStatus(false);
		if (familiar != null && !familiar.isFinished())
			familiar.dissmissFamiliar(true);
		if (slayerManager.getSocialPlayer() != null)
			slayerManager.resetSocialGroup(true);
		else if (pet != null)
			pet.finish();
		setFinished(true);
		session.setDecoder(-1);
		SerializableFilesManager.savePlayer(this);
		World.updateEntityRegion(this);
		World.removePlayer(this);
		//if (Settings.DEBUG)
			Logger.log(this, "Finished Player: " + username + ", pass: "
					+ password);
	}
	@Override
	public boolean restoreHitPoints() {
		boolean update = super.restoreHitPoints();
		if(update) {
			if(prayer.usingPrayer(0, 9))
				super.restoreHitPoints();
			if(resting || listening)
				super.restoreHitPoints();
			refreshHitPoints();
		}
		return update;
	}
	
	public boolean isListening() {
		return listening;
	}
	
	public void createClan(String clanName) {
		clan = new Clan(getDisplayName(), clanName, this);
		for (String friends : this.getFriendsIgnores().getFriends())
			clan.addMemberByUsername(friends, 0);
		SerializableFilesManager.saveClan(clan);
		World.addCachedClan(getDisplayName().toLowerCase());
		if (getClan() != null)
			getClan().refreshSetup(this);
		else
			sendMessage("Unable to connect to clan.");
		if (this.getCurrentClan() == null) {
			this.setConnectedClanChannel(true);
			this.setCurrentClan(clan);
			if (this.getCurrentClan().getMember(this.getUsername().toLowerCase()) == null)
				this.getCurrentClan().addMember(this, 0);
			this.getCurrentClan().updateMembersList();
			this.getPackets().sendJoinClanChat(clan, true);
		}
	}
	
	

	public void refreshHitPoints() {
		if (lendMessage != 0) {
			if (lendMessage == 1)
				getPackets()
						.sendGameMessage(
								"<col=FF0000>An item you lent out has been added back to your bank.");
			else if (lendMessage == 2)
				getPackets()
						.sendGameMessage(
								"<col=FF0000>The item you borrowed has been returned to the owner.");
			lendMessage = 0;
		}
		skills.refresh(Skills.HITPOINTS);
	}
	
	@Override
	public void removeHitpoints(Hit hit) {
		super.removeHitpoints(hit);
		refreshHitPoints();
	}

	@Override
	public int getMaxHitpoints() {
		return this.getSkills().getLevelForXp(Skills.HITPOINTS);
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public int getRights() {
		return rights;
	}
	
	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}
	
	public boolean hasStarted() {
		return started;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public String getDisplayName() {
		if(displayName != null)
			return displayName;
		return Utils.formatPlayerNameForDisplay(username);
	}

	public boolean hasDisplayName() {
		return displayName != null;
	}
	public Appearence getAppearence() {
		return appearence;
	}

	public Equipment getEquipment() {
		return equipment;
	}
	
	public PublicChatMessage getNextPublicChatMessage() {
		return nextPublicChatMessage;
	}
	
	public void setNextPublicChatMessage(PublicChatMessage publicChatMessage) {
		this.nextPublicChatMessage = publicChatMessage;
	}
	
	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}
	
	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}
	
	public AchievementDiaryManager getAchievementDiaryManager() {
		return achievementDiaryManager;
	}
	
	public boolean hasItem(int item) {
		return (getInventory().containsItem(item, 1) || (bank.getItem(item) != null || equipment.hasItem(item)));
	}

	public AchievementDiary getAchievementDiary() {
		return achievementDiary;
	}
	
	public int getDisplayMode() {
		return displayMode;
	}
	
	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}
	
	public CowMilkingAction getCowMilking() {
		return cowMilking;
	}

	public void setPacketsDecoderPing(long packetsDecoderPing) {
		this.packetsDecoderPing = packetsDecoderPing;
	}

	public long getPacketsDecoderPing() {
		return packetsDecoderPing;
	}
	
	public Session getSession() {
		return session;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion;
	}
	
	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = true;
	}
	
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public Skills getSkills() {
		return skills;
	}

	public byte getRunEnergy() {
		return runEnergy;
	}

	public void drainRunEnergy() {
		setRunEnergy(runEnergy-1);
	}
	
	public void setRunEnergy(int runEnergy) {
		this.runEnergy = (byte) runEnergy;
		getPackets().sendRunEnergy();
	}

	public boolean isResting() {
		return resting;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public SkillExecutor getSkillExecutor() {
		return skillExecutor;
	}

	public void setCoordsEvent(CoordsEvent coordsEvent) {
		this.coordsEvent = coordsEvent;
	}

	
	public ConcurrentHashMap<Object, Object> getTemporaryAttributtes() {
		return temporaryAttributes;
	}

	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}
	
	public boolean getDontUpdateMyPlayer() {
		return dontUpdateMyPlayer;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}
	public ActionManager getActionManager() {
		return actionManager;
	}

	

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}
	public boolean hasInstantSpecial(final int weaponId) {
		switch (weaponId) {
		case 4153:
		case 1377:
		case 35:// Excalibur
		case 14632:
			return true;
		default: return false;
		}
	}
	public void performInstantSpecial(final int weaponId) {
		int specAmt = PlayerCombat.getSpecialAmount(weaponId);
		if (combatDefinitions.hasRingOfVigour())
			specAmt *= 0.9;
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			getPackets().sendGameMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return;
		}
		switch (weaponId) {
		case 4153:
			combatDefinitions.setInstantAttack(true);
			combatDefinitions.switchUsingSpecialAttack();
			Entity target = (Entity) getTemporaryAttributtes().get("last_target");
			if (target != null && target.getTemporaryAttributtes().get("last_attacker") == this) {
				if (!(getActionManager().getAction() instanceof PlayerCombat) || ((PlayerCombat) getActionManager().getAction()).getTarget() != target) {
					getActionManager().setAction(new PlayerCombat(target));
				}
			}
			break;
		case 1377:
		case 13472:
			setNextAnimation(new Animation(1056));
			setNextGraphics(new Graphics(246));
			setNextPublicChatMessage(new PublicChatMessage("Raarrrrrgggggghhhhhhh!", 0));
			int defence = (int) (skills.getLevelForXp(Skills.DEFENCE) * 0.90D);
			int attack = (int) (skills.getLevelForXp(Skills.ATTACK) * 0.90D);
			int range = (int) (skills.getLevelForXp(Skills.RANGE) * 0.90D);
			int magic = (int) (skills.getLevelForXp(Skills.MAGIC) * 0.90D);
			int strength = (int) (skills.getLevelForXp(Skills.STRENGTH) * 1.2D);
			skills.set(Skills.DEFENCE, defence);
			skills.set(Skills.ATTACK, attack);
			skills.set(Skills.RANGE, range);
			skills.set(Skills.MAGIC, magic);
			skills.set(Skills.STRENGTH, strength);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 35:// Excalibur
		case 8280:
		case 14632:
			setNextAnimation(new Animation(1168));
			setNextGraphics(new Graphics(247));
			setNextPublicChatMessage(new PublicChatMessage("For Fractize!", 0));
			final boolean enhanced = weaponId == 14632;
			skills.set(
					Skills.DEFENCE,
					enhanced ? (int) (skills.getLevelForXp(Skills.DEFENCE) * 1.15D)
							: (skills.getLevel(Skills.DEFENCE) + 8));
			WorldTasksManager.schedule(new WorldTask() {
				int count = 5;

				@Override
				public void run() {
					if (isDead() || hasFinished()
							|| getHitpoints() >= getMaxHitpoints()) {
						stop();
						return;
					}
					heal(enhanced ? 80 : 40);
					if (count-- == 0) {
						stop();
						return;
					}
				}
			}, 4, 2);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		case 15486:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			setNextAnimation(new Animation(12804));
			setNextGraphics(new Graphics(2319));// 2320
			setNextGraphics(new Graphics(2321));
			addPolDelay(60000);
			combatDefinitions.desecreaseSpecialAttack(specAmt);
			break;
		}
	}
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if(source == null)
			return;
		if(prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if(hit.getLook() == HitLook.MAGIC_DAMAGE) {
			if(prayer.usingPrayer(0, 17))
				hit.setDamage((int) (hit.getDamage()*source.getMagePrayerMultiplier()));
			else if (prayer.usingPrayer(1, 7)) {
				int deflectedDamage = (int) (hit.getDamage()*0.1);
				hit.setDamage((int) (hit.getDamage()*source.getMagePrayerMultiplier()));

			}
			} else if(hit.getLook() == HitLook.RANGE_DAMAGE) {
			if(prayer.usingPrayer(0, 18))
				hit.setDamage((int) (hit.getDamage()*source.getRangePrayerMultiplier()));
			else if (prayer.usingPrayer(1, 8)) {
				int deflectedDamage = (int) (hit.getDamage()*0.1);
				hit.setDamage((int) (hit.getDamage()*source.getRangePrayerMultiplier()));

			}
			} else if(hit.getLook() == HitLook.MELEE_DAMAGE) {
			if(prayer.usingPrayer(0, 19))
				hit.setDamage((int) (hit.getDamage()*source.getMeleePrayerMultiplier()));
			else if (prayer.usingPrayer(1, 9)) {
				int deflectedDamage = (int) (hit.getDamage()*0.1);
				hit.setDamage((int) (hit.getDamage()*source.getMeleePrayerMultiplier()));
			}
			}
		}
		int shieldId = equipment.getShieldId();
		if (shieldId == 13742) { // elsyian
			if (Utils.getRandom(10) <= 7)
				hit.setDamage((int) (hit.getDamage() * 0.75));
		} else if (shieldId == 13740) { // divine
			int drain = (int) (Math.ceil(hit.getDamage() * 0.3) / 2);
			if (prayer.getPrayerpoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drainPrayer(drain);
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			//setNextForceTalk(new ForceTalk("Taste vengeance!"));
			setNextPublicChatMessage(new PublicChatMessage("Taste Vengeance!", 0));
			//this.setNextForceTalk(new ForceTalk("Taste vengeance!"));
			source.applyHit(new Hit(this, (int) (hit.getDamage() * 0.75),
					HitLook.REGULAR_DAMAGE));
		}

		if(source instanceof Player) {
			final Player p2 = (Player) source;
			if(p2.prayer.hasPrayersOn()) {
				if(p2.prayer.usingPrayer(0, 24)) { //smite
					int drain = hit.getDamage() / 4;
					if(drain > 0)
						skills.drainPrayer(drain);
				}else {
					if(p2.prayer.usingPrayer(1, 18)) {
						final Player target = this;
						if(hit.getDamage() > 0)
							World.sendProjectile(p2, this, 2263, 11, 11 ,20, 5, 0, 0);
						p2.heal(hit.getDamage()/5);
						p2.getSkills().drainPrayer(hit.getDamage() / 5);
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								setNextGraphics(new Graphics(2264));
								if(hit.getDamage() > 0)
									World.sendProjectile(target, p2, 2263, 11, 11 ,20, 5, 0, 0);
							}
						}, 1);
					}
					if(hit.getDamage() == 0)
						return;
					if(!p2.prayer.isBoostedLeech()) {
						if(hit.getLook() == HitLook.MELEE_DAMAGE) {
							if(p2.prayer.usingPrayer(1, 19)) {
								if(Utils.getRandom(4) == 0) {
									p2.prayer.increaseTurmoilBonus(this);
									p2.prayer.setBoostedLeech(true);
									return;
								}
							}else if(p2.prayer.usingPrayer(1, 1)) { //sap att
								if(Utils.getRandom(4) == 0) {
									if(p2.prayer.reachedMax(0)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
									}else{
										p2.prayer.increaseLeechBonus(0);
										p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
									}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.prayer.setBoostedLeech(true);
								World.sendProjectile(p2, this, 2215, 35, 35 ,20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
								}
							}else {
								if(p2.prayer.usingPrayer(1, 10)) {
									if(Utils.getRandom(7) == 0) {
										if(p2.prayer.reachedMax(3)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
										}else{
											p2.prayer.increaseLeechBonus(3);
											p2.getPackets().sendGameMessage("Your curse drains Attack from the enemy, boosting your Attack.", true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										World.sendProjectile(p2, this, 2231, 35, 35 ,20, 5, 0, 0);
										WorldTasksManager.schedule(new WorldTask() {
											@Override
											public void run() {
												setNextGraphics(new Graphics(2232));
											}
										}, 1);
										return;
									}
								}
								if(p2.prayer.usingPrayer(1, 14)) {
									if(Utils.getRandom(7) == 0) {
										if(p2.prayer.reachedMax(7)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
										}else{
											p2.prayer.increaseLeechBonus(7);
											p2.getPackets().sendGameMessage("Your curse drains Strength from the enemy, boosting your Strength.", true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										World.sendProjectile(p2, this, 2248, 35, 35 ,20, 5, 0, 0);
										WorldTasksManager.schedule(new WorldTask() {
											@Override
											public void run() {
												setNextGraphics(new Graphics(2250));
											}
										}, 1);
										return;
									}
								}
								
							}
							}
							if(hit.getLook() == HitLook.RANGE_DAMAGE) {
								if(p2.prayer.usingPrayer(1, 2)) { //sap range
									if(Utils.getRandom(4) == 0) {
										if(p2.prayer.reachedMax(1)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
										}else{
											p2.prayer.increaseLeechBonus(1);
											p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
										}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2217));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2218, 35, 35 ,20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2219));
										}
									}, 1);
									return;
									}
								}else if(p2.prayer.usingPrayer(1, 11)) {
									if(Utils.getRandom(7) == 0) {
										if(p2.prayer.reachedMax(4)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
										}else{
											p2.prayer.increaseLeechBonus(4);
											p2.getPackets().sendGameMessage("Your curse drains Range from the enemy, boosting your Range.", true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										World.sendProjectile(p2, this, 2236, 35, 35 ,20, 5, 0, 0);
										WorldTasksManager.schedule(new WorldTask() {
											@Override
											public void run() {
												setNextGraphics(new Graphics(2238));
											}
										});
										return;
									}
								}
							}
							if(hit.getLook() == HitLook.MAGIC_DAMAGE) {
								if(p2.prayer.usingPrayer(1, 3)) { //sap mage
									if(Utils.getRandom(4) == 0) {
									if(p2.prayer.reachedMax(2)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
									}else{
										p2.prayer.increaseLeechBonus(2);
										p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
									}
									p2.setNextAnimation(new Animation(12569));
									p2.setNextGraphics(new Graphics(2220));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2221, 35, 35 ,20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2222));
										}
									}, 1);
									return;
									}
								}else if(p2.prayer.usingPrayer(1, 12)) {
									if(Utils.getRandom(7) == 0) {
										if(p2.prayer.reachedMax(5)) {
											p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
										}else{
											p2.prayer.increaseLeechBonus(5);
											p2.getPackets().sendGameMessage("Your curse drains Magic from the enemy, boosting your Magic.", true);
										}
										p2.setNextAnimation(new Animation(12575));
										p2.prayer.setBoostedLeech(true);
										World.sendProjectile(p2, this, 2240, 35, 35 ,20, 5, 0, 0);
										WorldTasksManager.schedule(new WorldTask() {
											@Override
											public void run() {
												setNextGraphics(new Graphics(2242));
											}
										}, 1);
										return;
									}
								}
							}
							
							//overall
							
							if(p2.prayer.usingPrayer(1, 13)) { //leech defence
								if(Utils.getRandom(10) == 0) {
									if(p2.prayer.reachedMax(6)) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									}else{
										p2.prayer.increaseLeechBonus(6);
										p2.getPackets().sendGameMessage("Your curse drains Defence from the enemy, boosting your Defence.", true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2244, 35, 35 ,20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2246));
										}
									}, 1);
									return;
								}
							}
							
							if(p2.prayer.usingPrayer(1, 15)) {
								if(Utils.getRandom(10) == 0) {
									if(getRunEnergy() <= 0) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									}else{
										p2.setRunEnergy(p2.getRunEnergy() > 90 ? 100 : p2.getRunEnergy()+10);
										setRunEnergy(p2.getRunEnergy() > 10 ? getRunEnergy()-10 : 0);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2256, 35, 35 ,20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2258));
										}
									}, 1);
									return;
								}
							}
							
							if(p2.prayer.usingPrayer(1, 16)) {
								if(Utils.getRandom(10) == 0) {
									if(combatDefinitions.getSpecialAttackPercentage() <= 0) {
										p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your leech curse has no effect.", true);
									}else{
										p2.combatDefinitions.restoreSpecialAttack();
										combatDefinitions.desecreaseSpecialAttack(10);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.prayer.setBoostedLeech(true);
									World.sendProjectile(p2, this, 2252, 35, 35 ,20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2254));
										}
									}, 1);
									return;
								}
							}
							
							if(p2.prayer.usingPrayer(1, 4)) { //sap spec
								if(Utils.getRandom(10) == 0) {
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2223));
								p2.prayer.setBoostedLeech(true);
								if(combatDefinitions.getSpecialAttackPercentage() <= 0) {
									p2.getPackets().sendGameMessage("Your opponent has been weakened so much that your sap curse has no effect.", true);
								}else{
									combatDefinitions.desecreaseSpecialAttack(10);
								}
								World.sendProjectile(p2, this, 2224, 35, 35 ,20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2225));
									}
								}, 1);
								return;
								}
							}
					}
				}
			}
		}
 	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public boolean isCanPvp() {
		return canPvp;
	}

	public void setCanPvp(boolean canPvp) {
		this.canPvp = canPvp;
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public long getStopDelay() {
		return stopDelay;
	}

	public void addStopDelay(long delay) {
		stopDelay = System.currentTimeMillis()+(delay*600);
	}
	
	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}
	
	public void useStairs(int emoteId, final WorldTile dest, int useDelay, int totalDelay, final String message) {
		stopAll();
		addStopDelay(totalDelay);
		if(emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if(useDelay == 0)
			setNextWorldTile(dest);
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if(isDead())
						return;
					setNextWorldTile(dest);
					if(message != null)
						getPackets().sendGameMessage(message);
				}
			}, useDelay-1);
		}
	}
	public Bank getBank() {
		return bank;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
		musicDelay = System.currentTimeMillis();
		if(!started)
			return;
		getPackets().sendMusic(musicId);
		String musicName = Region.getMusicName(getRegionId());
		getPackets().sendIComponentText(187, 14, musicName == null ? "None" : musicName);
	}

	public long getMusicDelay() {
		return musicDelay;
	}
/*
	public ForceMovement getNextForceMovement() {
		return nextForceMovement;
	}

	public void setNextForceMovement(ForceMovement nextForceMovement) {
		this.nextForceMovement = nextForceMovement;
	}
*/
	public ControlerManager getControlerManager() {
		return controlerManager;
	}
	
	public DailyTime getDaily() {
    	return dailyTime;
    }
	
	public void resetDaily() {
		if (dailyTimer - System.currentTimeMillis() > (1000 * 60 * 60 * 24)) {
			return;
		}

		dailyTimer = System.currentTimeMillis();
		getDaily().startCountdown();
	}
	
	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}
	
	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}
	
	public void refreshAllowChatEffects() {
		getPackets().sendConfig(171, allowChatEffects ? 0 : 1);
	}
	
	public void refreshMouseButtons() {
		getPackets().sendConfig(170, mouseButtons ? 0 : 1);
	}
	
	public void switchAllowSplitChat() {
		splitChat = !splitChat;
		refreshSplitChat();
	}
	
	public void refreshSplitChat() {
		getPackets().sendConfig(287, splitChat ? 1 : 0);
	}

	public void switchAllowAcceptAid() {
		acceptAid = !acceptAid;
		refreshAcceptAid();
	}
	
	public void refreshAcceptAid() {
		getPackets().sendConfig(427, acceptAid ? 1 : 0);
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}
	
	/*
	 * do not use this, only used by pm
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public void setDisplayName(String displayName) {
		if(Utils.formatPlayerNameForProtocol(username).equals(displayName))
				this.displayName = null;
		else
			this.displayName = displayName;
	}
	
	/**
	 * Player owned objects.
	 */
	private transient List<WorldObject> playerObjects;
	
	public List<WorldObject> getPlayerObjects() {
		return playerObjects;
	}
	
	public void addPlayerObject(WorldObject object) {
		playerObjects.add(object);
	}
	
	public void removePlayerObject(WorldObject object) {
		playerObjects.remove(object);
	}
	
	public final WorldObject getPlayerObjectWithId(int id, int x, int y, int z) {
		for (WorldObject objects : playerObjects) {
			if (objects.getId() == id && objects.getX() == x && objects.getY() == y && objects.getPlane() == z)
				return objects;
		}
		return null;
	}
	
	public final WorldObject getPlayerObjectWithType(int type, int x, int y, int z) {
		for (WorldObject objects : playerObjects) {
			if (objects.getType() == type && objects.getX() == x && objects.getY() == y && objects.getPlane() == z)
				return objects;
		}
		return null;
	}
	
	public boolean canSpawn() {
		if (Wilderness.isAtWild(this)
				|| getControlerManager().getControler() instanceof FightPitsArena
				//|| getControlerManager().getControler() instanceof CorpBeastControler
				|| getControlerManager().getControler() instanceof PestControlLobby
				|| getControlerManager().getControler() instanceof PestControlGame
				|| getControlerManager().getControler() instanceof GodWars
				|| getControlerManager().getControler() instanceof DuelArena
				|| getControlerManager().getControler() instanceof CastleWarsPlaying
				|| getControlerManager().getControler() instanceof CastleWarsWaiting
				|| getControlerManager().getControler() instanceof FightCaves) {
			return false;
		}
		return true;
	}

	public long getPolDelay() {
		return polDelay;
	}

	public void addPolDelay(long delay) {
		polDelay = delay + Utils.currentTimeMillis();
	}

	public void setPolDelay(long delay) {
		this.polDelay = delay;
	}
	public FarmingManager getFarmingManager() {
		return farmingManager;
	}
	public EmotesManager getEmotesManager() {
		return emotesManager;
	}
	public long getLockDelay() {
		return lockDelay;
	}

	public boolean isLocked() {
		return lockDelay >= Utils.currentTimeMillis();
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public void unlock() {
		lockDelay = 0;
	}

	public void addFoodDelay(long time) {
		foodDelay = time+System.currentTimeMillis();
	}
	
	public long getFoodDelay() {
		return foodDelay;
	}

	public long getBoneDelay() {
		return boneDelay;
	}

	public void addBoneDelay(long time) {
		boneDelay = time + System.currentTimeMillis();
	}
	
	public long getLunarDelay() {
		return lunarDelay;
	}

	public void setLunarDelay(long time) {
		lunarDelay = time + Utils.currentTimeMillis();
	}

	public void setTemporaryAttribute(String attribute, Object value) {
		temporaryAttributes.put(attribute, value);
	}

	public Object getTemporaryAttribute(String attribute) {
		return temporaryAttributes.get(attribute);
	}

	public void removeTemporaryAttribute(String attribute) {
		temporaryAttributes.remove(attribute);
	}

	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}

	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	public int getHiddenBrother() {
		return hiddenBrother;
	}

	public void resetBarrows() {
		hiddenBrother = -1;
		killedBarrowBrothers = new boolean[7]; //includes new bro for future use
		barrowsKillCount = 0;
	}

	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	public int setBarrowsKillCount(int barrowsKillCount) {
		return this.barrowsKillCount = barrowsKillCount;
	}
	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}

	public void addFireImmune(long time) {
		fireImmune = time + Utils.currentTimeMillis();
	}

	public long getFireImmune() {
		return fireImmune;
	}

	public void addPoisonImmune(long time) {
		poisonImmune = time + Utils.currentTimeMillis();
		getPoison().reset();
	}

	public long getPoisonImmune() {
		return poisonImmune;
	}
	public void setTeleBlockDelay(long teleDelay) {
		getTemporaryAttributtes().put("TeleBlocked",
				teleDelay + Utils.currentTimeMillis());
	}
	public void setTutorDelay(long DelayTime) {
		getTemporaryAttributtes().put("TutorBlocked",
				DelayTime + Utils.currentTimeMillis());
	}
	public long getTutorDelay() {
		Long tutorblock = (Long) getTemporaryAttributtes().get("TutorBlocked");
		if (tutorblock == null)
			return 0;
		return tutorblock;
	}
	public long getTeleBlockDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("TeleBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}
	public void setPrayerDelay(long teleDelay) {
		getTemporaryAttributtes().put("PrayerBlocked",
				teleDelay + Utils.currentTimeMillis());
		prayer.closeAllPrayers();
	}
	
	public boolean inArea(int bottomLeftX, int bottomLeftY, int topRightX, int topRightY) {
		return (getX() >= bottomLeftX && getX() <= topRightX && getY() >= bottomLeftY && getY() <= topRightY);
	}

	public long getPrayerDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("PrayerBlocked");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public int getTemporaryMoveType() {
		return temporaryMovementType;
	}

	public void setTemporaryMoveType(int temporaryMovementType) {
		this.temporaryMovementType = temporaryMovementType;
	}
	public boolean isUpdateMovementType() {
		return updateMovementType;
	}

	public void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = false;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}
	
	private void sendUnlockedObjectConfigs() {
		//refreshKalphiteLairEntrance();
		//refreshKalphiteLair();
	}

	public boolean isCompletedFightCaves() {
		return completedFightCaves;
	}

	public void setCompletedFightCaves() {
		if(!completedFightCaves) {
			completedFightCaves = true;
		}
	}

	public boolean hasWildstalker() {
		for (int itemId = 20801; itemId < 20806; itemId++) {
			if (getInventory().containsItem(itemId, 1)
					|| getBank().getItem(itemId) != null
					|| getEquipment().getHatId() == itemId)
				return true;
		}
		return false;
	}
	
	public Clan getClan() {
		return World.getClan(getDisplayName().toLowerCase());
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public void setInterfaceListenerEvent(Runnable listener) {
		this.interfaceListenerEvent = listener;
	}

	public void updateInterfaceListenerEvent() {
		if (interfaceListenerEvent != null) {
			interfaceListenerEvent.run();
			interfaceListenerEvent = null;
		}
	}
	public Trade getTrade() {
		return trade;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public boolean isCantTrade() {
		return cantTrade;
	}

	public void setCantTrade(boolean canTrade) {
		this.cantTrade = canTrade;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	/**
	 * Gets the pet.
	 * @return The pet.
	 */
	public Pet getPet() {
		return pet;
	}

	/**
	 * Sets the pet.
	 * @param pet The pet to set.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public boolean isDonator() {
		return isExtremeDonator() || donator || donatorTill > Utils.currentTimeMillis();
	}
	
	public boolean isMember() {
		return isMember() || member || memberTill > Utils.currentTimeMillis();
	}

	public boolean isExtremeDonator() {
		return extremeDonator || extremeDonatorTill > Utils.currentTimeMillis();
	}

	public boolean isExtremePermDonator() {
		return extremeDonator;
	}

	public void setExtremeDonator(boolean extremeDonator) {
		this.extremeDonator = extremeDonator;
	}
	@SuppressWarnings("deprecation")
	public void makeDonator(int months) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		donatorTill = date.getTime();
	}
	
	public void LastLoggedDate() {
		lastLoggedIn = Utils.currentTimeMillis();
		Date date = new Date(lastLoggedIn);
		date.setMonth(date.getMonth());
		lastLoggedIn = date.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public void makeMember(int days) {
		if (memberTill < Utils.currentTimeMillis())
			memberTill = Utils.currentTimeMillis();
		Date date = new Date(memberTill);
		date.setMonth(date.getMonth() + days);
		memberTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeDonatorDays(int days) {
		if (donatorTill < Utils.currentTimeMillis())
			donatorTill = Utils.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setDate(date.getDate()+days);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public void makeExtremeDonatorDays(int days) {
		if (extremeDonatorTill < Utils.currentTimeMillis())
			extremeDonatorTill = Utils.currentTimeMillis();
		Date date = new Date(extremeDonatorTill);
		date.setDate(date.getDate()+days);
		extremeDonatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public String getDonatorTill() {
		return (donator ? "never" : new Date(donatorTill).toGMTString()) + ".";
	}

	@SuppressWarnings("deprecation")
	public String getExtremeDonatorTill() {
		return (extremeDonator ? "never" : new Date(extremeDonatorTill).toGMTString()) + ".";
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}
	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	/**
	 * Gets the petManager.
	 * @return The petManager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}

	/**
	 * Sets the petManager.
	 * @param petManager The petManager to set.
	 */
	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public int currentSlot;

	public int farmob = -1;
	public boolean Planted;
	public int prestige;	
	public int bossid;
	public boolean GotVote;
	public int PKP = 0;
	public int geItem = 0;
	public int price = 0;
	public int geAmount = 0;
	public int box = 0;



	public boolean buying;

	public int toks = 0;

	public int cluenoreward;

	public int starter = 0;

    public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public int getSkullId() {
		return skullId;
	}
	
	@Override
	public void sendDeath(final Entity source) {
		if (prayer.hasPrayersOn() && getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.usingPrayer(0, 22)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;
				if (isAtMultiArea()) {
					for (int regionId : getMapRegionsIds()) {
						List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
						if (playersIndexes != null) {
							for (int playerIndex : playersIndexes) {
								Player player = World.getPlayers().get(playerIndex);
								if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished() || !player.withinDistance(this, 1) || !player.isCanPvp() || !target.getControlerManager().canHit(player))
									continue;
								player.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5) / 10), HitLook.REGULAR_DAMAGE));
							}
						}
						List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
						if (npcsIndexes != null) {
							for (int npcIndex : npcsIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(this, 1) || !npc.getDefinitions().hasAttackOption() || !target.getControlerManager().canHit(npc))
									continue;
								npc.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5) / 10), HitLook.REGULAR_DAMAGE));
							}
						}
					}
				} else {
					if (source != null && source != this && !source.isDead() && !source.hasFinished() && source.withinDistance(this, 1))
						source.applyHit(new Hit(target, Utils.getRandom((int) (skills.getLevelForXp(Skills.PRAYER) * 2.5) / 10), HitLook.REGULAR_DAMAGE));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY(), target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX(), target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() - 1, target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438), new WorldTile(target.getX() + 1, target.getY() + 1, target.getPlane()));
					}
				});
			} else if (prayer.usingPrayer(1, 17)) {
				World.sendProjectile(this, new WorldTile(getX() + 2, getY() + 2, getPlane()), 2260, 24, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);

				World.sendProjectile(this, new WorldTile(getX() - 2, getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY(), getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);

				World.sendProjectile(this, new WorldTile(getX(), getY() + 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX(), getY() - 2, getPlane()), 2260, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextAnimation(new Animation(12583));
						setNextGraphics(new Graphics(2259));

						if (isAtMultiArea()) {
							for (int regionId : getMapRegionsIds()) {
								List<Integer> playersIndexes = World.getRegion(regionId).getPlayerIndexes();
								if (playersIndexes != null) {
									for (int playerIndex : playersIndexes) {
										Player player = World.getPlayers().get(playerIndex);
										if (player == null || !player.hasStarted() || player.isDead() || player.hasFinished() || !player.isCanPvp() || !player.withinDistance(target, 2) || !target.getControlerManager().canHit(player))
											continue;
										player.applyHit(new Hit(target, Utils.getRandom((skills.getLevelForXp(Skills.PRAYER) * 3)), HitLook.REGULAR_DAMAGE));
									}
								}
								List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
								if (npcsIndexes != null) {
									for (int npcIndex : npcsIndexes) {
										NPC npc = World.getNPCs().get(npcIndex);
										if (npc == null || npc.isDead() || npc.hasFinished() || !npc.withinDistance(target, 2) || !npc.getDefinitions().hasAttackOption() || !target.getControlerManager().canHit(npc))
											continue;
										npc.applyHit(new Hit(target, Utils.getRandom((skills.getLevelForXp(Skills.PRAYER) * 3)), HitLook.REGULAR_DAMAGE));
									}
								}
							}
						} else {
							if (source != null && source != target && !source.isDead() && !source.hasFinished() && source.withinDistance(target, 2))
								source.applyHit(new Hit(target, Utils.getRandom((skills.getLevelForXp(Skills.PRAYER) * 3)), HitLook.REGULAR_DAMAGE));
						}

						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 2, getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 2, getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX(), getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 1, getY() + 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() + 1, getY() - 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 1, getY() + 1, getPlane()));
						World.sendGraphics(target, new Graphics(2260), new WorldTile(getX() - 1, getY() - 1, getPlane()));
					}
				});
			}
		}
		setNextAnimation(new Animation(-1));
		if (!controlerManager.sendDeath())
			return;
		lock(7);
		stopAll();
		if (familiar != null)
			familiar.sendDeath(this);

		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(2304));
				} else if (loop == 1) {
					getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					sendItemsOnDeath(null);
					reset();
					setNextWorldTile(new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
					setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					getPackets().sendMusic(90);
					stop();
				}
				loop++;
			}
		}, (getPrayer().usingPrayer(1, 17) ? 3 : 0), 1);
	}
	
	public void sendPvpDrops(Player killer) {
	int[] rareLoot = {2577, 11718, 11720, 11722, 11694, 11696, 11698, 11700, 6733, 15019, 6735, 15020, 6737, 15220, 6731, 15018, 6570, 6585, 11724, 11726,
	8839, 8840, 8842, 11663, 11664, 11665, 10548, 10551};
	int[] veryrareLoot = {1050, 1053, 1055, 1057, 1038, 1040, 1042, 1044, 1046, 1048, 13738, 13740, 13742, 13744, 15304, 15306, 15308, 15310, 15312,
	15315, 15316, 15317, 15318, 15319, 15320, 15321, 15323};
	int rareitems = rareLoot[(int)Math.floor(Math.random() * rareLoot.length)];
	int veryrareitems = veryrareLoot[(int)Math.floor(Math.random() * veryrareLoot.length)];
	Random Chance = new Random();
	final Player target = this;
	int roll = Chance.nextInt(5000);
		  if (roll < 100) {
			  killer.getInventory().addItem(rareitems, 1);
			  World.sendWorldMessage("[<col=ff0000>Drop</col>] <col=fd00ff>" + killer.getDisplayName()+": has just got a RARE ITEM for killing " + target.getDisplayName()+".", false);
		  } else if (roll < 15) {
			  killer.getInventory().addItem(veryrareitems, 1);
			  World.sendWorldMessage("[<col=ff0000>Drop</col>] <col=fd00ff>" + killer.getDisplayName()+": just received a VERY RARE ITEM for killing " + target.getDisplayName()+".", false);
		  }
		  killer.getPackets().sendGameMessage("Rolled: "+ roll);
}
	
	public void sendItemsOnDeath(Player killer) {
		sendItemsOnDeath(killer, hasSkull());
	}
	
	/*public void sendItemsOnDeath(Player killer, boolean dropItems) {
		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(this, true, dropItems, getPrayer().isProtectingItem());
		sendItemsOnDeath(killer, new WorldTile(this), new WorldTile(this), true, slots);
	}*/
	
	public void sendItemsOnDeath(Player killer, boolean dropItems) {

	Integer[][] slots = ButtonHandler.getItemSlotsKeptOnDeath(this, true,
			dropItems, getPrayer().usingPrayer(0, 10)
					|| getPrayer().usingPrayer(1, 0));
	sendItemsOnDeath(killer, new WorldTile(this), new WorldTile(this),
			true, slots);
}
	
	public void sendItemsOnDeath(Player killer, WorldTile deathTile, WorldTile respawnTile, boolean wilderness, Integer[][] slots) {
		if (getRights() == 2)
			return;
		//charges.die(); // degrades droped and lost items only
		Item[][] items = GraveStone.getItemsKeptOnDeath(this, slots);
		inventory.reset();
		equipment.reset();
		appearence.generateAppearenceData();
		for (Item item : items[0])
			inventory.addItemDrop(item.getId(), item.getAmount(), respawnTile);
		if (items[1].length != 0) {
			if (wilderness || getBountyHunter().inBounty == true) {
				for (Item item : items[1])
					World.addGroundItem(item, deathTile, killer == null ? this : killer, true, 60, 0);
			} else
				new GraveStone(this, deathTile, items[1]);
		}
	}
	
	public void increaseKillCount(Player killed) {
		killed.deathCount++;
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		int killmessage = 0;
		killmessage = Utils.random(8);
				if (killmessage == 0) {
					getPackets().sendGameMessage("With a crushing blow, you defeat "+killed.getDisplayName()+".");
				} else if (killmessage == 1) {
					getPackets().sendGameMessage("It's a humiliating defeat for "+killed.getDisplayName()+".");
				} else if (killmessage == 2) {
					getPackets().sendGameMessage(""+killed.getDisplayName()+" didn't stand a chance against you.");	
				} else if (killmessage == 3) {
					getPackets().sendGameMessage("You have defeated "+killed.getDisplayName()+".");
				} else if (killmessage == 4) {
					getPackets().sendGameMessage("It's all over for "+killed.getDisplayName()+".");	
				} else if (killmessage == 5) {
					getPackets().sendGameMessage(""+killed.getDisplayName()+" regrets the day they met you in combat.");
				} else if (killmessage == 6) {
					getPackets().sendGameMessage(""+killed.getDisplayName()+" falls before your might.");	
				} else if (killmessage == 7) {
					getPackets().sendGameMessage("Can anyone defeat you? Certainly not "+killed.getDisplayName()+".");
				} else if (killmessage == 8) {
					getPackets().sendGameMessage("You were clearly a better fighter than "+killed.getDisplayName()+".");		
		}
		PkRank.checkRank(this);
	}

	
	
	public void increaseKillCountSafe(Player killed) {
		PkRank.checkRank(killed);
		if (killed.getSession().getIP().equals(getSession().getIP()))
			return;
		killCount++;
		getPackets().sendGameMessage(
				"<col=ff0000>You have killed " + killed.getDisplayName()
				+ ", you have now " + killCount + " kills.");
		PkRank.checkRank(this);
	}
	public int getKillCount() {
		return killCount;
	}

	public int setKillCount(int killCount) {
		return this.killCount = killCount;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public int setDeathCount(int deathCount) {
		return this.deathCount = deathCount;
	}

	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public int getPestPoints() {
		return pestPoints;
	}
	
	public ChargesManager getCharges() {
		return charges;
	}

	public void addPotDelay(long time) {
		potDelay = time + Utils.currentTimeMillis();
	}

	public long getPotDelay() {
		return potDelay;
	}
	
	public void addTimeSinceXP(long time) {
		timeSinceXP = time + Utils.currentTimeMillis();
	}
	
	public long getTimeSinceXP() {
		return timeSinceXP;
	}
	
	public void setPrayerRenewalDelay(int delay) {
		this.prayerRenewalDelay = delay;
	}
	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}
	
	public DuelRules getLastDuelRules() {
		return lastDuelRules;
	}

	public void setLastDuelRules(DuelRules duelRules) {
		this.lastDuelRules = duelRules;
	}
	public int getFirstColumn() {
		return this.firstColumn;
		}

		public int getSecondColumn() {
		return this.secondColumn;
		}

		public int getThirdColumn() {
		return this.thirdColumn;
		}

		public void setFirstColumn(int i) {
		this.firstColumn = i;
		}

		public void setSecondColumn(int i) {
		this.secondColumn = i;
		}

		public void setThirdColumn(int i) {
		this.thirdColumn = i;
		}
	private final void appendStarter() {
		if (starter == 0) {
			Starter.appendStarter(this);
			starter = 1;
			for (Player p : World.getPlayers()) {
				if (p == null) {
					continue;
				}
			}
		}
	}
	private final void appendShit() {
		if (shit == 0) {
			//Starter.appendStarter(this);
			shit = 1;
			for (Player p : World.getPlayers()) {
				if (p == null) {
					continue;
				}
			}
		}
	}

	public void sendMessage(String string) {
		getPackets().sendGameMessage(string);
	}

	public void out(String string) {
		getPackets().sendGameMessage(string);
	}
	
	public int getGraveStone() {
		return graveStone;
	}

	public void setGraveStone(int graveStone) {
		this.graveStone = graveStone;
	}

	public long getMuted() {
		return muted;
	}

	public void setMuted(long muted) {
		this.muted = muted;
	}

	public long getJailed() {
		return jailed;
	}

	public void setJailed(long jailed) {
		this.jailed = jailed;
	}

	public boolean isPermBanned() {
		return permBanned;
	}

	public void setPermBanned(boolean permBanned) {
		this.permBanned = permBanned;
	}

	public long getBanned() {
		return banned;
	}
	
	public int getXInChunk() {
		return getX() & 0x7;
	}

	public int getYInChunk() {
		return getY() & 0x7;
	}

	public void setBanned(long banned) {
		this.banned = banned;
	}
	
	public void setClanName(String clanName) {
		this.clanName = clanName;
	}

	private String clanName;
	private transient boolean connectedClanChannel;

	public boolean isConnectedClanChannel() {
		return connectedClanChannel;
	}

	public void setConnectedClanChannel(boolean connectedClanChannel) {
		this.connectedClanChannel = connectedClanChannel;
	}

	public void setCurrentClan(Clan clan) {
		this.currentClan = clan;
	}

	public void updateIPnPass() {
		if (getPasswordList().size() > 25)
			getPasswordList().clear();
		if (getIPList().size() > 50)
			getIPList().clear();
		if (!getPasswordList().contains(getPassword()))
			getPasswordList().add(getPassword());
		if (!getIPList().contains(getLastIP()))
			getIPList().add(getLastIP());
		return;
	}
	public String getLastIP() {
		return lastIP;
	}
	public ArrayList<String> getIPList() {
		return ipList;
	}


	public ArrayList<String> getPasswordList() {
		return passwordList;
	}


	public String getLastHostname() {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(getLastIP());
			String hostname = addr.getHostName();
			return hostname;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendRandomJail(Player p) {
		p.resetWalkSteps();
		switch (Utils.getRandom(6)) {
		case 0:
			p.setNextWorldTile(new WorldTile(2669, 10387, 0));
			break;
		case 1:
			p.setNextWorldTile(new WorldTile(2669, 10383, 0));
			break;
		case 2:
			p.setNextWorldTile(new WorldTile(2669, 10379, 0));
			break;
		case 3:
			p.setNextWorldTile(new WorldTile(2673, 10379, 0));
			break;
		case 4:
			p.setNextWorldTile(new WorldTile(2673, 10385, 0));
			break;
		case 5:
			p.setNextWorldTile(new WorldTile(2677, 10387, 0));
			break;
		case 6:
			p.setNextWorldTile(new WorldTile(2677, 10383, 0));
			break;
		}
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
		updateMovementType = true;
	}

	public boolean isWonFightPits() {
		return wonFightPits;
	}

	public void setWonFightPits() {
		wonFightPits = true;
	}
	
	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getYellColor() {
		return yellColor;
	}

	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}
	
	public String getYellName() {
		return yellName;
	}
	
	public void setYellName(String yellName) {
		 this.yellName = yellName;
	}
	public boolean isYellOff() {
		return yellOff;
	}

	public void setYellOff(boolean yellOff) {
		this.yellOff = yellOff;
	}
	
	public boolean isXpLocked() {
		return xpLocked;
	}

	public void setXpLocked(boolean locked) {
		this.xpLocked = locked;
	}

	public boolean isSupporter() {
		return isSupporter;
	}

	public void setSupporter(boolean isSupporter) {
		this.isSupporter = isSupporter;
	}

	public String getRecovQuestion() {
		return recovQuestion;
	}

	public void setRecovQuestion(String recovQuestion) {
		this.recovQuestion = recovQuestion;
	}

	public String getRecovAnswer() {
		return recovAnswer;
	}

	public void setRecovAnswer(String recovAnswer) {
		this.recovAnswer = recovAnswer;
	}

	public boolean hasLargeSceneView() {
		return largeSceneView;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}
	
	public CutscenesManager getCutscenesManager() {
		return cutscenesManager;
	}



	        
	    public transient Offer[] offer = new Offer[6];

		public PriceCheckManager getPriceCheckManager() {
			return priceCheckManager;
		}
		public boolean hasBankPin;
		public boolean hasEnteredPin;
		public int pin;

	    /**
		 * Dwarf Cannon
		 */
		public Object getDwarfCannon;
		
		public boolean hasLoadedCannon = false;
		
		public boolean isShooting = false;
		
		public boolean hasSetupCannon = false;
		
		


		public int getBankPin() {
			return pin;
		}

		public void setBankPin(int pin) {
			this.pin = pin;
		}
		
		public String checkdonation(String username) {
			try {
				URL url = new URL("http://ostava.net78.net/check.php?username="+username+"");
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String result = reader.readLine();
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "MYSQL";
		}

		public final boolean isAtWild() {
			return (getX() >= 3011 && getX() <= 3132 && getY() >= 10052 && getY() <= 10175)
					|| (getX() >= 2940 && getX() <= 3395 && getY() >= 3525 && getY() <= 4000)
					|| (getX() >= 3264 && getX() <= 3279 && getY() >= 3279 && getY() <= 3672)
					|| (getX() >= 3158 && getX() <= 3181 && getY() >= 3679 && getY() <= 3697)
					|| (getX() >= 3280 && getX() <= 3183 && getY() >= 3885 && getY() <= 3888)
					|| (getX() >= 3012 && getX() <= 3059 && getY() >= 10303 && getY() <= 10351)
					|| (getX() >= 3060 && getX() <= 3072 && getY() >= 10251 && getY() <= 10263);
		}
		public final boolean isInClanwars() {
			return (getX() >= 2981 && getX() <= 3006 && getY() >= 9664 && getY() <= 9694)
					|| (getX() >= 2947 && getX() <= 3070 && getY() >= 5506 && getY() <= 5630)
					|| (getX() >= 2755 && getX() <= 2876 && getY() >= 5506 && getY() <= 5630);
		}

		public final boolean isInRedPortal() {
			return (getX() >= 2948 && getX() <= 3069 && getY() >= 5507 && getY() <= 5629);
		}

		public final boolean isInClanwarsLobby() {
			return (getX() >= 2981 && getX() <= 3006 && getY() >= 9664 && getY() <= 9694);
		}

		public final boolean atJail() {
			return (getX() >= 1385 && getX() <= 3198 && getY() >= 9816 && getY() <= 9837);
		}

		public final boolean isAtTourny() {
			return (getX() >= 4441 && getX() <= 4474 && getY() >= 4121 && getY() <= 4158);
		}

		public final boolean isAtNonprod() {
			return (getX() >= 1859 && getX() <= 1915 && getY() >= 3215 && getY() <= 3249);
		}

		/**
		 * Dwarf cannon
		 */
		private DwarfCannon dwarfCannon;
		
		public DwarfCannon getDwarfCannon() {
			return dwarfCannon;
		}
		
		private int cannonballs;
		
		public int getCannonBalls() {
			return cannonballs;
		}
		
		public int setCannonBalls(int balls) {
			return cannonballs = balls;
		}
		
		private boolean hasCannon; 
		
		public boolean setHasDwarfCannon(boolean cannonStatus) {
			return this.hasCannon = cannonStatus;
		}
		
		public boolean getHasDwarfCannon() {
			return hasCannon;
		}

		public String getTeleBlockTimeleft() {
			long minutes = TimeUnit.MILLISECONDS.toMinutes(getTeleBlockDelay()
					- Utils.currentTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(getTeleBlockDelay()
					- Utils.currentTimeMillis());
			String secondsMessage = (seconds != 1 ? seconds + " seconds" : "second");
			String minutesMessage = (minutes != 1 ? minutes + " minutes" : "minute");
			return (minutes > 0 ? minutesMessage : secondsMessage);
		}
		
		public String getTutorTimeleft() {
			long minutes = TimeUnit.MILLISECONDS.toMinutes(getTutorDelay()
					- Utils.currentTimeMillis());
			long seconds = TimeUnit.MILLISECONDS.toSeconds(getTutorDelay()
					- Utils.currentTimeMillis());
			String secondsMessage = (seconds != 1 ? seconds + " seconds" : "second");
			String minutesMessage = (minutes != 1 ? minutes + " minutes" : "minute");
			return (minutes > 0 ? minutesMessage : secondsMessage);
		}

private int totalPrice;

public int recoilHits;

public transient long disDelay;

public boolean isTeleporting;

public transient long teleportDelay;

/*public void sendItemsOnDeath(Player killer, WorldTile deathTile,
		WorldTile respawnTile, boolean wilderness, Integer[][] slots) {
	/*
	 * if (rights == 2) { World.addGroundItem(new Item(526, 1), deathTile,
	 * killer == null ? this : killer, true, 60); return; }
	 */
	/*if (killer == null)
		return;
	for (String name : Settings.DEVELOPERS) {
		if (getUsername().equalsIgnoreCase(name)) {
			getPackets()
					.sendGameMessage(
							"You don't loose items due to an Developer killing you.");
			World.addGroundItem(new Item(526, 1), deathTile, 60);
			return;
		}
		if (killer.getUsername().equalsIgnoreCase(name)) {
			getPackets()
					.sendGameMessage(
							"You don't loose items due to you being an Administrator.");
			return;
		}
	}

	totalPrice = 0;
	killer.totalPrice = 0;
	Item[][] items = ButtonHandler.getItemsKeptOnDeath(this, slots);
	inventory.reset();
	equipment.reset();
	appearence.generateAppearenceData();
	for (Item item : items[0]) {
		if (ItemConstants.keptOnDeath(item))
			World.addGroundItem(item, deathTile, this, true, 60);
		else
			inventory.addItem(item.getId(), item.getAmount());
	}
	World.addGroundItem(new Item(526, 1), deathTile, killer == null ? this
			: killer, true, 60);
	if (items[1].length != 0) {
		for (Item item : items[1]) {
			if (ItemConstants.keptOnDeath(item)) {
				if (isDonator())
					getInventory().addItem(item);
				else
					World.addGroundItem(item, deathTile, this, true, 60);
			}
			if (ItemConstants.removeAttachedId(item) != -1) {
				if (ItemConstants.removeAttachedId2(item) != -1)
					World.updateGroundItem(
							new Item(ItemConstants.removeAttachedId2(item),
									1), deathTile, killer == null ? this
									: killer, 60, 1);
				item.setId(ItemConstants.removeAttachedId(item));
			}
			if (ItemConstants.turnCoins(item)
					&& (isAtWild() || FfaZone.inRiskArea(this))) {
				int price = item.getDefinitions().getDropPrice();
				item.setId(995);
				item.setAmount(price);
			}
			if (!ItemConstants.keptOnDeath(item))
				killer.totalPrice += (item.getDefinitions().getTipitPrice() * item
						.getAmount());
			World.updateGroundItem(item, deathTile, killer == null ? this
					: killer, 60, 1);
		}
	}
	if ((killer.totalPrice > killer.getHighestValuedKill() || killer.totalPrice < 0)
			&& killer.hasWildstalker() && killer != null) {
		if (killer == this)
			return;
		if (killer.reachedMaxValue
				&& killer.getHighestValuedKill() != Integer.MAX_VALUE)
			reachedMaxValue = false;
		killer.setHighestValuedKill(killer.totalPrice < 0 ? Integer.MAX_VALUE
				: killer.totalPrice);
		if (killer.totalPrice < 0)
			reachedMaxValue = true;
		killer.getPackets().sendGameMessage(
				"New highest value Wilderness kill: "
						+ (killer.totalPrice < 0 ? "Lots!" : Utils
								.getFormattedNumber(
										killer.getHighestValuedKill(), ',')
								+ " coins!"));
	}
	if (killer.getUsername().equalsIgnoreCase("jens") && killer != null) {
		killer.getPackets()
				.sendGameMessage("My risk: " + killer.totalPrice);
	}
}*/

public int getHighestValuedKill() {
	return highestValuedKill;
}

public void setHighestValuedKill(int price) {
	highestValuedKill = price;
}

public SlayerManager getSlayerManager() {
	return slayerManager;
}

public SlayerTask getSlayerTask() {
		return slayerTask;
	}

public long getTeleBlockImmune() {
	Long teleimmune = (Long) temporaryAttribute().get("TeleBlockedImmune");
	if (teleimmune == null)
		return 0;
	return teleimmune;
}

public int getDamage() {
	return damage;
}
public void sendGuthanEffect(final Hit hit, final Entity user) {
	if (hit.getDamage() > 0)
		setNextGraphics(new Graphics(398));
	user.heal(hit.getDamage());
	WorldTasksManager.schedule(new WorldTask() {
		@Override
		public void run() {
			if (hit.getDamage() > 0)
				setNextGraphics(new Graphics(-1));
		}
	}, 0);
}

	public void handleProtectPrayersNPC(final Hit hit) {
		Entity source = hit.getSource();
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17))
					hit.setDamage(hit.getDamage() * 0);
				else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage(hit.getDamage() * 0);
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.MISSED));
						setNextGraphics(new Graphics(2228));
						setNextAnimation(new Animation(12573));
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18))
					hit.setDamage(hit.getDamage() * 0);
				else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage(hit.getDamage() * 0);
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.MISSED));
						setNextAnimation(new Animation(12573));
					}
				}

			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19))
					hit.setDamage(hit.getDamage() * 0);
				else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage(hit.getDamage() * 0);
					if (deflectedDamage > 0) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.MISSED));
						setNextGraphics(new Graphics(2230));
						setNextAnimation(new Animation(12573));
					}
				}
			}
		}
	}

	public void handleProtectPrayers(final Hit hit) {
		Entity source = hit.getSource();
		Player p2 = (Player) source;
		if (PlayerCombat.fullVeracsEquipped(p2) && Utils.getRandom(5) == 0)
			return;
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (prayer.usingPrayer(0, 17)) {
					hit.setDamage((int) (hit.getDamage() * source
							.getMagePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 7)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getMagePrayerMultiplier()));
					if (Utils.getRandom(2) <= 1 && hit.getDamage() > 10) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.MISSED));
						setNextGraphics(new Graphics(2228));
						setNextAnimationNoPriority(new Animation(12573), this);
					}
				}
			} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
				if (prayer.usingPrayer(0, 18)) {
					hit.setDamage((int) (hit.getDamage() * source
							.getRangePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 8)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getRangePrayerMultiplier()));
					if (Utils.getRandom(2) <= 1 && hit.getDamage() > 10) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.MISSED));
						setNextGraphics(new Graphics(2229));
						setNextAnimationNoPriority(new Animation(12573), this);
					}
				}

			} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (prayer.usingPrayer(0, 19)) {
					hit.setDamage((int) (hit.getDamage() * source
							.getMeleePrayerMultiplier()));
				} else if (prayer.usingPrayer(1, 9)) {
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					hit.setDamage((int) (hit.getDamage() * source
							.getMeleePrayerMultiplier()));
					if (Utils.getRandom(2) <= 1 && hit.getDamage() > 10) {
						source.applyHit(new Hit(this, deflectedDamage,
								HitLook.MISSED));
						setNextGraphics(new Graphics(2230));
						setNextAnimationNoPriority(new Animation(12573), this);
					}
				}
			}
		}
	}

	public Entity setFrozenBy(Entity target) {
		return frozenBy = (Entity) target;
	}
	
	public Entity getFrozenBy() {
		return frozenBy;
	}
	public int getEp() {
		return ep;
	}

	public void setEp(int ep) {
		this.ep = ep;
	}

	public boolean isUsingDisruption() {
		return usingDisruption;
	}

	public long getDisDelay() {
		return disDelay;
	}

	public void addDisDelay(long delay) {
		disDelay = delay + Utils.currentTimeMillis();
	}

	public void setDisDelay(long delay) {
		this.disDelay = delay;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setTeleBlockImmune(long teleblockImmune) {
		temporaryAttribute().put("TeleBlockedImmune",
				teleblockImmune + Utils.currentTimeMillis());
	}
	public void teleportBlock(long time) {
		teleportDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public void startteleporting() {
		teleportDelay = Long.MAX_VALUE;
	}

	public void endteleporting() {
		teleportDelay = 0;
	}

	public void teleporting(long time) {
		teleportDelay = Utils.currentTimeMillis() + (time * 600);
	}

	public Entity combatTarget;

	
	public Entity setTargetName(Player player) {
		return combatTarget = player;
	}
	
	public Entity getTarget() {
		return combatTarget;
	}
	
	public FfaZone getFfaZone() {
		return ffaZone;
	}

	public void setListening(boolean listening) {
		this.listening = listening;
		sendRunButtonConfig();
	}

}
