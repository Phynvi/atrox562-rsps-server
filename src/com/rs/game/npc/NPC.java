package com.rs.game.npc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.ForceTalk;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.player.ClueScrolls;
import com.rs.game.Graphics;
import com.rs.game.player.content.clans.Clan.ClanRanks;
import com.rs.game.player.content.clans.ClanMember;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.utils.SerializableFilesManager;
import com.rs.game.minigames.soulwars.SoulWars;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.content.RingOfWealth;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.CastleWars.PlayingGame;
import com.rs.utils.Misc;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;
import com.rs.game.player.skills.slayer.Slayer;
import com.rs.game.player.skills.slayer.SlayerManager;
import com.rs.game.player.skills.slayer.Slayer.SlayerTask;
import com.rs.utils.NPCCharms;
import com.rs.utils.NPCCharmDrops;

public class NPC extends Entity implements Serializable {

	private static final long serialVersionUID = -4794678936277614443L;

	private int id;
	private WorldTile respawnTile;
	private int mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private boolean randomwalk;
	private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab
	// def, blahblah till 9
	private boolean spawned;
	private transient NPCCombat combat;
	public WorldTile forceWalk;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private int forceAgressiveDistance;
	private boolean forceMultiAttacked;
	private boolean noDistanceCheck;
	private boolean intelligentRouteFinder;
	// npc masks
	private transient Transformation nextTransformation;
	//name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;
	private transient boolean locked;
	
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.setSpawned(spawned);
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk((getDefinitions().walkMask & 0x2) != 0
				|| forceRandomWalk(id));
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
		if (id == 1526) {
	           cwNPCTalk();
	    } else if (id == 6138) {
	           TownCrier();
	    }
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextTransformation != null || changedCombatLevel || changedName;
	}

	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}

	public void setNPC(int id) {
		this.id = id;
		bonuses = NPCBonuses.getBonuses(id);
	}
	
	public boolean calcFollow(WorldTile target, boolean inteligent) {
		return calcFollow(target, -1, true, inteligent);
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public void setCanBeAttackFromOutOfArea(boolean b) {
		canBeAttackFromOutOfArea = b;
	}
	
	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}

	public int getId() {
		return id;
	}

	public void processNPC() {
		if (isDead() || locked)
			return;
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Utils.currentTimeMillis()) {
							if (((hasRandomWalk()) && World.getRotation(
									getPlane(), getX(), getY()) == 0) // temporary
									// fix
									&& Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								int moveY = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								resetWalkSteps();
								if (getMapAreaNameHash() != -1) {
									if (!MapAreas.isAtArea(getMapAreaNameHash(), this)) {
										forceWalkRespawnTile();
										return;
									}
									addWalkSteps(getX() + moveX, getY() + moveY, 5);
								}else 
									addWalkSteps(respawnTile.getX() + moveX, respawnTile.getY() + moveY, 5);
							}
						}
					}
				}
			}
		}
		 if (id == 494) {
	            setRandomWalk(false);
	        }
		 else if (id == 7448) {
			 setRandomWalk(false);
	         setName("Donator Store");
	    } else if (id == 462) {
			 setRandomWalk(false);
		} else if (id == 614) {
			 setRandomWalk(false);
	    } else if (id == 8462) {
			 setRandomWalk(false);
		} else if (id == 8461) {
			 setRandomWalk(false);
		} else if (id == 1597) {
			 setRandomWalk(false);
	    } else if (id == 8464) {
			 setRandomWalk(false);
	    }
		if (isForceWalking()) {
			if (getFreezeDelay() < Utils.currentTimeMillis()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps())
						addWalkSteps(forceWalk.getX(), forceWalk.getY(),
								getSize(), true);
					if (!hasWalkSteps()) { // failing finding route
						setNextWorldTile(new WorldTile(forceWalk)); // force
						// tele
						// to
						// the
						// forcewalk
						// place
						forceWalk = null; // so ofc reached forcewalk place
					}
				} else
					// walked till forcewalk place
					forceWalk = null;
			}
		}
	}
	
	public static void cwNPCTalk() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				String[] speak = { "", "Enter any portal more players more fun", "" };
				int i = Misc.random(1, 3);
				for (NPC n : World.getNPCs()) {
					if (!n.getName().equalsIgnoreCase("Lanthus")) {
						continue;
					}
					n.setNextForceTalk(new ForceTalk(speak[i]));
				}
			}
		}, 0, 8); //time in seconds
	}
	
	public static void TownCrier() {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				String[] speak = { "Vote for "+ Settings.SERVER_NAME +".", ""+ Settings.ANNOUNCEMENT1 +"", ""+ Settings.ANNOUNCEMENT2 +"", ""+ Settings.ANNOUNCEMENT3 +"" };
				int i = Misc.random(1, 3);
				for (NPC n : World.getNPCs()) {
					if (!n.getName().equalsIgnoreCase("Town crier")) {
						continue;
					}
					n.setNextForceTalk(new ForceTalk(speak[i]));
					n.setNextAnimation(new Animation(6865));
				}
			}
		}, 0, 8); //time in seconds
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}

	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.anInt853 << 32 != 0 && definitions.respawnDirection > 0 && definitions.respawnDirection <= 8)
			return definitions.respawnDirection -1;
		return 0;
	}
	
	/*
	 * forces npc to random walk even if cache says no, used because of fake
	 * cache information
	 */
	private static boolean forceRandomWalk(int npcId) {
		switch (npcId) {
		case 494:
			return false;
		case 3341:
		case 3342:
		case 3343:
			return true;
		default:
			return false;
			/*
			 * default: return NPCDefinitions.getNPCDefinitions(npcId).name
			 * .equals("Icy Bones");
			 */
		}
	}
	
	public void sendSoulSplit(final Hit hit, final Entity user) {
		final NPC target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				setNextGraphics(new Graphics(2264));
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0,
							0);
			}
		}, 1);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage)
			hit.setDamage(capDamage);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(1, 18)) 
					sendSoulSplit(hit, p2);
				if (hit.getDamage() == 0)
					return;
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							p2.getPrayer().setBoostedLeech(true);
							return;
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
							// att
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Attack from the enemy, boosting your Attack.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2215, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Attack from the enemy, boosting your Attack.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2231, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Utils.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.getPackets()
										.sendGameMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.getPackets()
										.sendGameMessage(
												"Your curse drains Strength from the enemy, boosting your Strength.",
												true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									World.sendProjectile(p2, this, 2248, 35,
											35, 20, 5, 0, 0);
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
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2218, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Range from the enemy, boosting your Range.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2236, 35, 35,
										20, 5, 0, 0);
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
					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Utils.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your sap curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.",
											true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2221, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Utils.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.getPackets()
									.sendGameMessage(
											"Your opponent has been weakened so much that your leech curse has no effect.",
											true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.getPackets()
									.sendGameMessage(
											"Your curse drains Magic from the enemy, boosting your Magic.",
											true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								World.sendProjectile(p2, this, 2240, 35, 35,
										20, 5, 0, 0);
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

					// overall

					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Utils.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.getPackets()
								.sendGameMessage(
										"Your opponent has been weakened so much that your leech curse has no effect.",
										true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.getPackets()
								.sendGameMessage(
										"Your curse drains Defence from the enemy, boosting your Defence.",
										true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							World.sendProjectile(p2, this, 2244, 35, 35, 20, 5,
									0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		bonuses = NPCBonuses.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		setFinished(true);
		World.updateEntityRegion(this);
		World.removeNPC(this);
	}

	public void setRespawnTask() {
		if (!hasFinished()) {
			reset();
			setLocation(respawnTile);
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, getCombatDefinitions().getRespawnDelay() * 600,
		TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null)
			combat = new NPCCombat(this);
		spawn();
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	public NPCCombat getCombat() {
		return combat;
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					drop();
					Player killer = getMostDamageReceivedSourcePlayer();
					//npcDied(killer, id);
					reset();
					setLocation(respawnTile);
					finish();
					if (!isSpawned())
						setRespawnTask();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	public void drop() {
		try {
			NPCCharms charms = NPCCharmDrops.getCharm(getName());
			Player killer1 = getMostDamageReceivedSourcePlayer();
			if (charms != null) {
				double currentChance = 0;
				int charm = -1;
				double roll = Utils.getRandomDouble(100);
				for (int i = 0; i < 4; i++) {
					if (i == 0 && roll <= charms.getBlueCharmRate()) {
						charm = NPCCharms.BLUE_CHARM;
						break;
					} else if (i == 1 && roll <= charms.getCrimsonCharmRate() + currentChance) {
						charm = NPCCharms.CRIMSON_CHARM;
						break;
					} else if (i == 2 && roll <= charms.getGreenCharmRate() + currentChance) {
						charm = NPCCharms.GREEN_CHARM;
						break;
					} else if (i == 3 && roll <= charms.getGoldCharmRate() + currentChance) {
						charm = NPCCharms.GOLD_CHARM;
						break;
					} 
					currentChance += (i == 0 ? charms.getBlueCharmRate() : i == 1 ? charms.getCrimsonCharmRate() : i == 2 ? charms.getGreenCharmRate() : charms.getGoldCharmRate());
				}
				if (charm != -1 && killer1 != null)
					sendDrop(killer1, new Drop(charm, 100, charms.getCharmsAmount(), charms.getCharmsAmount(), false));
			}
			Drop[] drops = NPCDrops.getDrops(id);
			if (drops == null)
				return;
			int scrollId = ClueScrolls.ScrollIds[Utils.random(ClueScrolls.ScrollIds.length - 1)];
			if (Misc.random(300) == 1) {
				sendDrop(killer1, new Drop(scrollId, 100, 1, 1, false));
			}
			Player killer = getMostDamageReceivedSourcePlayer();
			if (killer == null)
				return;
			SlayerManager manager = killer.getSlayerManager();
			if (manager.isValidTask(getName())) {
			    Slayer.killedTask(killer, this);
			}
			if (killer.getSlayerManager().getCurrentTask() == SlayerTask.VAMPYRE && this.getId() == 6214) {
			    Slayer.killedTask(killer, this);
			}
			/*if (killer.inArea(3066, 9644, 3158, 9714)) {
				if (this.getId() == 75 || this.getId() == 74 || getId() == 76 || getId() == 5305) {
					AchievementDiary diary = killer.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
					if (!diary.isComplete(0, 5)*//* && CAVE_BORDER.insideBorder(p)*//*) {
						diary.updateTask(killer, 0, 5, true);
					}
				}
			}
			if (this.getId() == 1832) {
				AchievementDiary diary = killer.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
				if (!diary.isComplete(0, 0)) {
					diary.updateTask(killer, 0, 0, true);
				}
			}*/
			

			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100)
					sendDrop(killer, drop);
				else {
					if ((Utils.getRandomDouble(100)) <= drop.getRate())
						possibleDrops[possibleDropsCount++] = drop;
				}
			}

			if (possibleDropsCount > 0)
				sendDrop(killer, possibleDrops[Utils.getRandom(possibleDropsCount - 1)]);
			
			if (getId() == 1619 || getId() == 49 || getId() == 6219 || getId() == 6203 || getId() == 6208 || getId() == 6206
			 || getId() == 6204 || getId() == 6215 || getId() == 6214 || getId() == 6212) {
				killer.setZamorakKC(killer.getZamorakKC() + 1);
				killer.getPackets().sendIComponentText(601, 9, ""+killer.getZamorakKC());
			}
			if (getId() == 6247 || getId() == 6248 || getId() == 6250 || getId() == 6252 || getId() == 6255 || getId() == 6254
			 || getId() == 6256) {
				killer.setSaradominKC(killer.getSaradominKC() + 1);
				killer.getPackets().sendIComponentText(601, 8, ""+killer.getSaradominKC());
			}
			if (getId() == 6260 || getId() == 6261 || getId() == 6263 || getId() == 6265 || getId() == 6277 || getId() == 6275
			 || getId() == 6270) {
				killer.setBandosKC(killer.getBandosKC() + 1);
				killer.getPackets().sendIComponentText(601, 7, ""+killer.getBandosKC());
			}
			if (getId() == 6229 || getId() == 6222 || getId() == 6223 || getId() == 6225 || getId() == 6227 || getId() == 6232
			 || getId() == 6229 || getId() == 6242) {
				killer.setArmadylKC(killer.getArmadylKC() + 1);
				killer.getPackets().sendIComponentText(601, 6, ""+killer.getArmadylKC());
			}
			int petRoll = Utils.getRandom(7500);
			if (getId() == 3200 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15304, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15304, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Chaos Elemental Jr.!", false);
			}
			if (getId() == 50 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15301, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15301, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Prince Black Dragon!", false);
			}
			if (getId() == 2881 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15297, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15297, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Dagannoth Supreme Jr.!", false);
			}
			if (getId() == 2882 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15298, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15298, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Dagannoth Prime Jr.!", false);
			}
			if (getId() == 2883 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15299, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15299, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Dagannoth Rex Jr.!", false);
			}
			if (getId() == 1160 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15300, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15300, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Kaplhite princess!", false);
			}
			if (getId() == 8614 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15302, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15302, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Kraken pet!", false);
			}
			if (getId() == 5247 && petRoll == 1) {
				if (killer.getPet() != null || killer.getFamiliar() != null) {
					killer.getBank().addItem(15303, 1, 0, true);
					killer.getPackets().sendGameMessage("You already have a follower, your new pet has been sent to bank.");
				} else {
					killer.getPetManager().spawnPet(15303, true);
				}
				World.sendWorldMessage("<img=4><col=FF6600><shad=00FFFF>[News] " + killer.getDisplayName() + " just got Penance pet!", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}
	
	private void sendDrop(Player player, Drop drop) {
		Player luckyPlayer = null;
		Player play = null;
		List<Player> winner = new ArrayList<Player>();
		HashMap<Integer, Player> percentages = new HashMap<Integer, Player>();
		int totalPotential = 1;
		int size = getSize();
		Item item = ItemDefinitions.getItemDefinitions(drop.getItemId()).isStackable()
				? new Item(drop.getItemId(), (drop.getMinAmount()) + Utils.getRandom(drop.getExtraAmount()))
				: new Item(drop.getItemId(), drop.getMinAmount() + Utils.getRandom(drop.getExtraAmount()));
				if (player.getCurrentClan() != null && player.isAtMultiArea()) {
			if (player.getCurrentClan().isLootsharing()
					&& !player.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANYONE)
					&& !(player.getCurrentClan().isCoinsharing() && item.getDefinitions().getValue() > 50000)) {
				for (ClanMember member : player.getCurrentClan().getMembers()) {
					play = World.getPlayer(member.getUsername());
					if (play == null)
						continue;

					if (!play.getCurrentClan().getClanLeaderUsername().equalsIgnoreCase(play.getDisplayName())) {
						if (!play.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANY_FRIENDS) && play
								.getCurrentClan().getMember(play.getDisplayName().replaceAll(" ", "_").toLowerCase())
								.getRank() < player.getCurrentClan().getMinimumLootshareRank().getOption())
							continue;
						else if (play.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANY_FRIENDS)) {
							if (!SerializableFilesManager.loadPlayer(play.getCurrentClan().getClanLeaderUsername())
									.getFriendsIgnores()
									.containsFriend(play.getDisplayName().toLowerCase().replaceAll(" ", "_")))
								continue;
						}
					}
					if (player.withinDistance(play, 16))
						winner.add(play);
				}
				for (Player p : winner) {
					if (p == null)
						continue;
					totalPotential += p.getCurrentClan().getMember(p.getUsername().toLowerCase().replaceAll(" ", " "))
							.getPotential();
				}

				for (Player p : winner) {
					int currentAmount = 0;
					for (int i = -1; i < (p.getCurrentClan()
							.getMember(p.getUsername().toLowerCase().replaceAll(" ", " ")).getPotential()) * 100
							/ totalPotential; i++) {
						percentages.put(currentAmount, p);
						currentAmount++;
					}
				}
				int luckyOne = Utils.random(percentages.size());
				luckyPlayer = percentages.get(luckyOne);
				for (Player p : winner) {
					if (p == null)
						continue;
					if (p == luckyPlayer) {
						p.sendMessage(
								"<col=115b0d>You received: " + item.getAmount() + " " + item.getName() + ".</col>");
						p.getCurrentClan().getMember(p.getDisplayName().replaceAll(" ", "_").toLowerCase())
								.setPotential((p.getCurrentClan()
										.getMember(p.getDisplayName().replaceAll(" ", "_").toLowerCase()).getPotential()
										- item.getDefinitions().getTipitPrice()) < 0 ? 0
												: (p.getCurrentClan()
														.getMember(
																p.getDisplayName().replaceAll(" ", "_").toLowerCase())
														.getPotential() - item.getDefinitions().getTipitPrice()));
					} else {
						p.sendMessage(luckyPlayer.getDisplayName() + " received: " + item.getAmount() + " "
								+ item.getName() + ".");
						p.sendMessage("Your chance of receiving loot has improved.");
						p.getCurrentClan().getMember(p.getDisplayName().replaceAll(" ", "_").toLowerCase())
								.setPotential(p.getCurrentClan()
										.getMember(p.getDisplayName().replaceAll(" ", "_").toLowerCase()).getPotential()
										+ Math.round(item.getDefinitions().getTipitPrice() / winner.size()));
					}
				}
			} else if (!player.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANYONE)) {
				for (ClanMember member : player.getCurrentClan().getMembers()) {
					play = World.getPlayer(member.getUsername());
					if (play == null)
						continue;
					if (!play.getCurrentClan().getClanLeaderUsername().equalsIgnoreCase(play.getDisplayName())) {
						if (!play.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANY_FRIENDS) && play
								.getCurrentClan().getMember(play.getDisplayName().replaceAll(" ", "_").toLowerCase())
								.getRank() < player.getCurrentClan().getMinimumLootshareRank().getOption())
							continue;
						else if (play.getCurrentClan().getMinimumLootshareRank().equals(ClanRanks.ANY_FRIENDS)) {
							if (!SerializableFilesManager.loadPlayer(play.getCurrentClan().getClanLeaderUsername())
									.getFriendsIgnores()
									.containsFriend(play.getDisplayName().toLowerCase().replaceAll(" ", "_")))
								continue;
						}
					}
					if (player.withinDistance(play, 16))
						winner.add(play);
					int amt = 0;
					if (winner.size() > 0)
						amt = Math.round(item.getDefinitions().getTipitPrice() / winner.size());
					else
						amt = item.getDefinitions().getTipitPrice();
					for (Player p : winner) {
						if (p == null)
							continue;
						p.sendMessage("<col=115b0d>You received " + amt + " gold as your split of this drop: "
								+ item.getAmount() + " x " + item.getName() + ".");
						if (p.getInventory().getNumberOf(995) > 1 && !(p.getInventory().getNumberOf(995) + amt < 0)
								|| player.getInventory().getNumberOf(995) == 0 && player.getInventory().hasFreeSlots())
							p.getInventory().addItem(995, amt);
						else {
							World.addGroundItem(new Item(995, amt),
									new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()), p, false, 180,
									true);
							p.sendMessage(
									"<col=ff0000>Note: </col>Due to lack of inventory space, your split has been dropped on the ground.");
						}
					}
					return;
				}
			}
		}
		percentages.clear();
		World.addGroundItem(item, new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane()),
				luckyPlayer == null ? player : luckyPlayer, false, 180, true);
		if (item.getDefinitions().getValue() > 200000) {
			World.sendWorldMessage("<col=46A4FF>"+ player.getUsername() +" just received " + item.getName() + " for killing: " + this.getDefinitions().getName() +".", false);
		}
	}

	@Override
	public int getSize() {
		return getDefinitions().size;
	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}
	
	

	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0;
	}

	public WorldTile getRespawnTile() {
		return respawnTile;
	}

	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget()
				&& !(combat.getTarget() instanceof Familiar))
			lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking())
			return;
		combat.setTarget(entity);
		lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null)
			return;
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}
	
	public ArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		int size = getSize();
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		int attackStyle = getCombatDefinitions().getAttackStyle();
		for (int regionId : getMapRegionsIds()) {
			if (checkPlayers) {
				List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
				if (playerIndexes != null) {
					for (int playerIndex : playerIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (player == null || player.isDead() || player.hasFinished() || !player.isRunning()
								//|| player.getAppearence().isHidden()
								|| !Utils.isOnRange(getX(), getY(), size, player.getX(), player.getY(),
										player.getSize(),
										forceAgressiveDistance != 0 ? forceAgressiveDistance
												: (attackStyle == NPCCombatDefinitions.MELEE
														|| attackStyle == NPCCombatDefinitions.SPECIAL2) ? 1 : 4)
								|| (!forceMultiAttacked && (!isAtMultiArea() || !player.isAtMultiArea())
										&& (player.getAttackedBy() != this
												&& (player.getAttackedByDelay() > Utils.currentTimeMillis())))
								|| !clipedProjectile(player, false)
								|| (!forceAgressive && !Wilderness.isAtWild(this)
										&& player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2))
							continue;
						possibleTarget.add(player);
					}
				}
			}
			if (checkNPCs) {
				List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
				if (npcsIndexes != null) {
					for (int npcIndex : npcsIndexes) {
						NPC npc = World.getNPCs().get(npcIndex);
						if (npc == null || npc == this || npc.isDead() || npc.hasFinished()
								|| !Utils.isOnRange(getX(), getY(), size, npc.getX(), npc.getY(), npc.getSize(),
										forceAgressiveDistance > 0 ? forceAgressiveDistance : getSize())
								|| !npc.getDefinitions().hasAttackOption()
								|| ((!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis())
								|| !clipedProjectile(npc, false))
							continue;
						possibleTarget.add(npc);
					}
				}
			}
		}
		return possibleTarget;
	}

/*	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = World.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int playerIndex : playerIndexes) {
					Player player = World.getPlayers().get(playerIndex);
					if (player == null
							|| player.isDead()
							|| player.hasFinished()
							|| !player.isRunning()
							|| !player
							.withinDistance(
									this,
									forceTargetDistance > 0 ? forceTargetDistance
											: (getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
															: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 64
																	: 8))
																	|| (!forceMultiAttacked
																			&& (!isAtMultiArea() || !player
																					.isAtMultiArea())
																					&& player.getAttackedBy() != this && (player
																							.getAttackedByDelay() > Utils.
																							currentTimeMillis() || player
																							.getFindTargetDelay() > Utils
																							.currentTimeMillis()))
																							|| !clipedProjectile(player, false)
																							|| (!forceAgressive && !Wilderness.isAtWild(this) && player
																									.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2))
						continue;
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}*/

	public boolean checkAgressivity() {
		// if(!(Wilderness.isAtWild(this) &&
		// getDefinitions().hasAttackOption())) {
		if (!forceAgressive) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
				return false;
		}
		// }
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}
	
	
	public ArrayList<Entity> getPossibleTargets() {
		return getPossibleTargets(false, true);
	}

	public ArrayList<Entity> getPossibleTargetsWithNpcs() {
		return getPossibleTargets(true, true);
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			combat.reset();
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	@Override
	public String toString() {
		return getDefinitions().name + " - " + id + " - " + getX() + " "
				+ getY() + " " + getPlane();
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean hasRandomWalk() {
		return randomwalk;
	}

	public void setRandomWalk(boolean forceRandomWalk) {
		this.randomwalk = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}

	public void setName(String string) {
		this.name = getDefinitions().name.equals(string) ? null : string;
		changedName = true;
	}

	public int getCustomCombatLevel() {
		return combatLevel;
	}

	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}

	public String getName() {
		return name != null ? name : getDefinitions().name;
	}

	public void setCombatLevel(int level) {
		combatLevel  = getDefinitions().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}

	public boolean hasChangedName() {
		return changedName;
	}

	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}

	public WorldTile getMiddleWorldTile() {
		int size = getSize();
		return new WorldTile(getCoordFaceX(size),getCoordFaceY(size), getPlane());
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public boolean isNoDistanceCheck() {
		return noDistanceCheck;
	}

	public void setNoDistanceCheck(boolean noDistanceCheck) {
		this.noDistanceCheck = noDistanceCheck;
	}
	
	public boolean withinDistance(Player tile, int distance) {
		return super.withinDistance(tile, distance);
	}

	/**
	 * Gets the locked.
	 * @return The locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets the locked.
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	/*
	 * Sets the event after a npc has died.
	 */
	 public void npcDied(final Player player, int npcId) {
		 switch(npcId) {
	    	case 8599:
	
	              World.addGroundItem(new Item(14639, 2), new WorldTile(getCoordFaceX(getSize()),
                          getCoordFaceY(getSize()), getPlane()), player,
                          false, 180, true);
	    	case 1633:
	    		  World.addGroundItem(new Item(14639, 1), new WorldTile(getCoordFaceX(getSize()),
                          getCoordFaceY(getSize()), getPlane()), player,
                          false, 180, true);
	        }
		 }

		public boolean isIntelligentRouteFinder() {
			return intelligentRouteFinder;
		}

		public void setIntelligentRouteFinder(boolean intelligentRouteFinder) {
			this.intelligentRouteFinder = intelligentRouteFinder;
		}

		public int getForceAgressiveDistance() {
			return forceAgressiveDistance;
		}
		
		public void setForceAgressiveDistance(int forceAgressiveDistance) {
			this.forceAgressiveDistance = forceAgressiveDistance;
		}
		public void setNextNPCTransformation(int id) {
			if (id == 0)
				return;
			setNPC(id);
			nextTransformation = new Transformation(id);
			if (getCustomCombatLevel() != -1)
				changedCombatLevel = true;
			if (getCustomName() != null)
				changedName = true;
		}
	 }