package com.rs.game.player.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.rs.game.item.Item;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.utils.NPCExamines;
import com.rs.game.WorldObject;
import com.rs.Settings;
import com.rs.game.player.skills.farming.FarmingManager.FarmingSpot;
import com.rs.game.player.skills.farming.FarmingManager.SpotInfo;
import com.rs.game.minigames.bountyhunter.BountyHunter;
import com.rs.game.npc.NPC;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.controlers.Kalaboss;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.actions.HomeTeleport;

/*
 * content package used for static stuff
 */
public class Magic {

	public static final int MAGIC_TELEPORT = 0, ITEM_TELEPORT = 1, OBJECT_TELEPORT = 2;

	@SuppressWarnings("unused")
	private static final int AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
			NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560, BLOOD_RUNE = 565, SOUL_RUNE = 566,
			ASTRAL_RUNE = 9075, LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695, DUST_RUNE = 4696,
			SMOKE_RUNE = 4697, MUD_RUNE = 4698, LAVA_RUNE = 4699, BODY_RUNE = 559, COSMIC_RUNE = 564;

	private static final boolean hasInfiniteRunes(int runeId, int weaponId, int shieldId) {
		if (runeId == AIR_RUNE) {
			if (weaponId == 1381 || weaponId == 21777) // air staff
				return true;
		} else if (runeId == WATER_RUNE) {
			if (weaponId == 1383 || shieldId == 18346) // water staff
				return true;
		} else if (runeId == EARTH_RUNE) {
			if (weaponId == 1385) // earth staff
				return true;
		} else if (runeId == FIRE_RUNE) {
			if (weaponId == 1387) // fire staff
				return true;
		}
		return false;
	}

	public static final boolean checkCombatSpell(Player player, int spellId, int set, boolean delete) {
		if (spellId == 65535)
			return true;
		switch (player.getCombatDefinitions().getSpellBook()) {
		case 193:
			switch (spellId) {
			case 28:
				if (!checkSpellRequirements(player, 50, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE,
						1))
					return false;
				break;
			case 32:
				if (!checkSpellRequirements(player, 52, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE,
						1))
					return false;
				break;
			case 24:
				if (!checkSpellRequirements(player, 56, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, BLOOD_RUNE, 1))
					return false;
				break;
			case 20:
				if (!checkSpellRequirements(player, 58, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, WATER_RUNE, 2))
					return false;
				break;
			case 30:
				if (!checkSpellRequirements(player, 62, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE,
						2))
					return false;
				break;
			case 34:
				if (!checkSpellRequirements(player, 64, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE,
						2))
					return false;
				break;
			case 26:
				if (!checkSpellRequirements(player, 68, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, BLOOD_RUNE, 2))
					return false;
				break;
			case 22:
				if (!checkSpellRequirements(player, 70, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, WATER_RUNE, 4))
					return false;
				break;
			case 29:
				if (!checkSpellRequirements(player, 74, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE,
						2))
					return false;
				break;
			case 33:
				if (!checkSpellRequirements(player, 76, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, AIR_RUNE, 2, SOUL_RUNE,
						2))
					return false;
				break;
			case 25:
				if (!checkSpellRequirements(player, 80, delete, DEATH_RUNE, 2, BLOOD_RUNE, 4))
					return false;
				break;
			case 21:
				if (!checkSpellRequirements(player, 82, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, WATER_RUNE, 3))
					return false;
				break;
			case 31:
				if (!checkSpellRequirements(player, 86, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, FIRE_RUNE, 4, AIR_RUNE,
						4))
					return false;
				break;
			case 35:
				if (!checkSpellRequirements(player, 88, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, AIR_RUNE, 4, SOUL_RUNE,
						3))
					return false;
				break;
			case 27:
				if (!checkSpellRequirements(player, 92, delete, DEATH_RUNE, 4, BLOOD_RUNE, 4, SOUL_RUNE, 1))
					return false;
				break;
			case 23:
				if (!checkSpellRequirements(player, 94, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, WATER_RUNE, 6))
					return false;
				break;
			case 36: // Miasmic rush.
				if (!checkSpellRequirements(player, 61, delete, CHAOS_RUNE, 2, EARTH_RUNE, 1, SOUL_RUNE, 1)) {
					return false;
				}
				int weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			case 38: // Miasmic burst.
				if (!checkSpellRequirements(player, 73, delete, CHAOS_RUNE, 4, EARTH_RUNE, 2, SOUL_RUNE, 2)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			case 37: // Miasmic blitz.
				if (!checkSpellRequirements(player, 85, delete, BLOOD_RUNE, 2, EARTH_RUNE, 3, SOUL_RUNE, 3)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			case 39: // Miasmic barrage.
				if (!checkSpellRequirements(player, 97, delete, BLOOD_RUNE, 4, EARTH_RUNE, 4, SOUL_RUNE, 4)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			default:
				return false;
			}
			break;
		case 192:
			switch (spellId) {
			case 75:
				if (!checkSpellRequirements(player, 66, delete, WATER_RUNE, 5, EARTH_RUNE, 5, SOUL_RUNE, 1))
					return false;
				break;
			case 78:
				if (!checkSpellRequirements(player, 73, delete, WATER_RUNE, 8, EARTH_RUNE, 8, SOUL_RUNE, 1))
					return false;
				break;
			case 82:
				if (!checkSpellRequirements(player, 80, delete, WATER_RUNE, 12, EARTH_RUNE, 12, SOUL_RUNE, 1))
					return false;
				break;
			case 26:
				if (!checkSpellRequirements(player, 3, delete, WATER_RUNE, 3, EARTH_RUNE, 2, BODY_RUNE, 1))
					return false;
				break;
			case 31:
				if (!checkSpellRequirements(player, 11, delete, WATER_RUNE, 3, EARTH_RUNE, 2, BODY_RUNE, 1))
					return false;
				break;
			case 35:
				if (!checkSpellRequirements(player, 19, delete, WATER_RUNE, 2, EARTH_RUNE, 3, BODY_RUNE, 1))
					return false;
				break;
			case 25:
				if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 47:
				if (!checkSpellRequirements(player, 39, delete, AIR_RUNE, 2, EARTH_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 54:
				if (player.getEquipment().getWeaponId() != 1409) {
					player.getPackets().sendGameMessage("You must be wielding an Iban's staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 50, delete, FIRE_RUNE, 5, DEATH_RUNE, 1))
					return false;
				break;
			case 56:
				if (player.getEquipment().getWeaponId() != 4170) {
					player.getPackets().sendGameMessage("You must be wielding a Slayer's staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 50, delete, MIND_RUNE, 4, DEATH_RUNE, 1))
					return false;
				break;
			case 28:
				if (!checkSpellRequirements(player, 5, delete, WATER_RUNE, 1, AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 30:
				if (!checkSpellRequirements(player, 9, delete, EARTH_RUNE, 2, AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 32:
				if (!checkSpellRequirements(player, 13, delete, FIRE_RUNE, 3, AIR_RUNE, 2, MIND_RUNE, 1))
					return false;
				break;
			case 34: // air bolt
				if (!checkSpellRequirements(player, 17, delete, AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 36:// bind
				if (!checkSpellRequirements(player, 20, delete, EARTH_RUNE, 3, WATER_RUNE, 3, NATURE_RUNE, 2))
					return false;
				break;
			case 55: // snare
				if (!checkSpellRequirements(player, 50, delete, EARTH_RUNE, 4, WATER_RUNE, 4, NATURE_RUNE, 3))
					return false;
				break;
			case 81:// entangle
				if (!checkSpellRequirements(player, 79, delete, EARTH_RUNE, 5, WATER_RUNE, 5, NATURE_RUNE, 4))
					return false;
				break;
			case 39: // water bolt
				if (!checkSpellRequirements(player, 23, delete, WATER_RUNE, 2, AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 42: // earth bolt
				if (!checkSpellRequirements(player, 29, delete, EARTH_RUNE, 3, AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 45: // fire bolt
				if (!checkSpellRequirements(player, 35, delete, FIRE_RUNE, 4, AIR_RUNE, 3, CHAOS_RUNE, 1))
					return false;
				break;
			case 49: // air blast
				if (!checkSpellRequirements(player, 41, delete, AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 52: // water blast
				if (!checkSpellRequirements(player, 47, delete, WATER_RUNE, 3, AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 58: // earth blast
				if (!checkSpellRequirements(player, 53, delete, EARTH_RUNE, 4, AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 63: // fire blast
				if (!checkSpellRequirements(player, 59, delete, FIRE_RUNE, 5, AIR_RUNE, 4, DEATH_RUNE, 1))
					return false;
				break;
			case 70: // air wave
				if (!checkSpellRequirements(player, 62, delete, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 73: // water wave
				if (!checkSpellRequirements(player, 65, delete, WATER_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 77: // earth wave
				if (!checkSpellRequirements(player, 70, delete, EARTH_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 80: // fire wave
				if (!checkSpellRequirements(player, 75, delete, FIRE_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 84:
				if (!checkSpellRequirements(player, 81, delete, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1))
					return false;
				break;
			case 87:
				if (!checkSpellRequirements(player, 85, delete, WATER_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE,
						1))
					return false;
				break;
			case 89:
				if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE,
						1))
					return false;
				break;
			case 66: // Sara Strike
				if (player.getEquipment().getWeaponId() != 2415) {
					player.getPackets().sendGameMessage("You must be wielding a Saradomin staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2))
					return false;
				break;
			case 67: // Guthix Claws
				if (player.getEquipment().getWeaponId() != 2416 && player.getEquipment().getWeaponId() != 8841) {
					player.getPackets().sendGameMessage(
							"You must be wielding a Guthix Staff or a Void knight mace to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2))
					return false;
				break;
			case 68: // Flame of Zammy
				if (player.getEquipment().getWeaponId() != 2417) {
					player.getPackets().sendGameMessage("You must be wielding a Zamorak staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 4, BLOOD_RUNE, 2))
					return false;
				break;
			case 91:
				if (!checkSpellRequirements(player, 85, delete, FIRE_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE,
						1))
					return false;
				break;
			case 86: // teleblock
				if (!checkSpellRequirements(player, 85, delete, CHAOS_RUNE, 1, LAW_RUNE, 1, DEATH_RUNE, 1))
					return false;
				break;
			default:
				return false;
			}
			break;
		default:
			return false;
		}
		if (set >= 0) {
			if (set == 0)
				player.getCombatDefinitions().setAutoCastSpell(spellId);
			else
				player.getTemporaryAttributtes().put("tempCastSpell", spellId);
		}
		return true;
	}
	
	public static boolean useTeleTab(final Player player, final WorldTile tile) {
		if (!player.getControlerManager().processItemTeleport(tile))
			return false;
		player.lock();
		player.setNextAnimation(new Animation(9597));
		player.setNextGraphics(new Graphics(1680));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.setNextAnimation(new Animation(4731));
					stage = 1;
				} else if(stage == 1){
					WorldTile teleTile = tile;
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new WorldTile(tile, 2);
						if (World.canMoveNPC(tile.getPlane(), teleTile.getX(),
								teleTile.getY(), player.getSize()))
							break;
						teleTile = tile;
					}
					player.setNextWorldTile(teleTile);
					player.getControlerManager().magicTeleported(ITEM_TELEPORT);
					if (player.getControlerManager().getControler() == null)
						teleControlersCheck(player, teleTile);
					player.setNextFaceWorldTile(new WorldTile(teleTile.getX(),
							teleTile.getY() - 1, teleTile.getPlane()));
					player.setDirection(6);
					player.setNextAnimation(new Animation(-1));
					stage = 2;
				}else if (stage == 2) {
					player.resetReceivedDamage();
					player.unlock();
					stop();
				}
				
			}
		}, 2, 1);
		return true;
	}

	private static final void setCombatSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutoCastSpell() == spellId)
			player.getCombatDefinitions().resetSpells(true);
		else
			checkCombatSpell(player, spellId, 0, false);
	}
	
	private enum Planks {
		PLANK(new Item(960, 1), new Item(1511, 1), new Item(995, 70)), OAK_PLANK(new Item(8778, 1), new Item(1521, 1),
				new Item(995, 175)), TEAK_PLANK(new Item(8780, 1), new Item(6333, 1),
						new Item(995, 350)), MAHOGANY_PLANK(new Item(8782, 1), new Item(6332, 1), new Item(995, 1050));

		Item plank, log, coins;

		private Planks(Item plank, Item log, Item coins) {
			this.plank = plank;
			this.log = log;
			this.coins = coins;
		}

		static final Map<Integer, Planks> Plank = new HashMap<Integer, Planks>();

		public static Planks forId(int id) {
			return Plank.get(id);
		}

		static {
			for (Planks planks : Planks.values())
				Plank.put((int) planks.log.getId(), planks);
		}

		public Item getPlank() {
			return plank;
		}

		public Item getLog() {
			return log;
		}

		public Item getCoins() {
			return coins;
		}
	}
	
	public static final void processLunarSpell(final Player player, int spellId, final WorldObject object) {
		final SpotInfo info;
		final FarmingSpot spot;
		switch (spellId) {
		case 24:// fertile soil
			if (player.isLocked())
				return;
			info = SpotInfo.getInfo(object.getId());
			if (info == null) {
				player.getPackets().sendGameMessage("Um...I don't want to fertilise that!");
				return;
				// prevents server from nulling if it's not a farming object,
				// lol
			} else if (info != null) {
				// checks that the object is a farming object; best to double
				// check
				spot = player.getFarmingManager().getSpot(info);
				if (spot == null || spot.productInfo == null)
					return;
				// another null check
				if (spot.isDead() || spot.productInfo != null && spot.reachedMaxStage() || spot.isDiseased()) {
					player.getPackets().sendGameMessage("Composting it isn't going to make it get any bigger.");
					return;
				} else if (spot.hasCompost()) {
					player.getPackets().sendGameMessage("The patch has already been saturated with compost.");
					return;
				}
				if (!Magic.checkSpellRequirements(player, 83, true, ASTRAL_RUNE, 3, EARTH_RUNE, 15, NATURE_RUNE, 2))
					// checks that the player has the required level and runes!
					return;
				player.setLunarDelay(5000);
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							player.lock();
							player.faceObject(object);
							World.sendGraphics(player, new Graphics(724), object);// sends
																					// the
																					// gfx
																					// to
																					// the
																					// object
							player.setNextAnimation(new Animation(4413));// correct
																			// emote
																			// id
						} else if (loop == 3) {
							player.getSkills().addXp(Skills.MAGIC, 87);
							player.getSkills().addXp(Skills.FARMING, 18);
							spot.setSuperCompost(true);
							spot.refresh();
							player.unlock();
							player.getPackets().sendGameMessage("You saturate the patch with supercompost.");
							stop();
						}
						loop++;

					}
				}, 0, 1);
			}
			break;
		case 54:// cure plant
			if (player.isLocked())
				return;
			// SpotInfo
			info = SpotInfo.getInfo(object.getId());
			if (info == null) {
				player.getPackets().sendGameMessage("Umm... this spell won't cure that!");
				return;
				// prevents server from nulling if it's not a farming object,
				// lol
			} else if (info != null) {
				// checks that the object is a farming object; best to double
				// check
				spot = player.getFarmingManager().getSpot(info);
				if (spot == null)
					return;
				// another null check
				if (spot.isDead()) {
					player.getPackets().sendGameMessage(
							"'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.");
					return;
				}
				if (spot.isDiseased()) {
					if (!Magic.checkSpellRequirements(player, 66, true, ASTRAL_RUNE, 1, EARTH_RUNE, 8))
						// checks that the player has the required level and
						// runes!
						return;
					player.setLunarDelay(6000);
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (loop == 0) {
								player.lock();
								player.faceObject(object);
								player.setNextGraphics(new Graphics(748, 0, 120));
								player.setNextAnimation(new Animation(4432));// correct
																				// emote
																				// id
							} else if (loop == 4) {
								player.getSkills().addXp(Skills.MAGIC, 91.5);
								spot.setDiseased(false);
								spot.refresh();
								player.unlock();
								player.getPackets().sendGameMessage(
										"The produce in this patch has been restored to its natural health.");
								stop();
							}
							loop++;

						}
					}, 0, 1);
				} else {
					player.getPackets().sendGameMessage("The patch is not diseased.");
				}
			}
			break;
		}
	}
	
	public static final void processLunarSpell(Player player, int spellId, Item item) {
		switch (spellId) {
		case 33:
			if (player.getLunarDelay() > Utils.currentTimeMillis())
				return;
			if (!Magic.checkSpellRequirements(player, 86, false, ASTRAL_RUNE, 2, EARTH_RUNE, 15, NATURE_RUNE, 1))
				return;
			Planks plank = Planks.forId(item.getId());
			if (plank == null) {
				player.sendMessage("You cannot turn this into planks!");
				return;
			} else {
				if (!player.getInventory().containsItem(plank.getCoins())) {
					player.sendMessage("You need at least " + plank.getCoins().getAmount() + " coins to make "
							+ plank.getLog().getName().toLowerCase() + " into "
							+ (Utils.startsWithVowel(plank.getPlank().getName()) ? "an " : "a ")
							+ plank.getPlank().getName().toLowerCase() + ".");
					return;
				}
				player.setLunarDelay(7000);
				player.getSkills().addXp(Skills.MAGIC, 90);
				player.setNextAnimation(new Animation(6298));
				player.setNextGraphics(new Graphics(1063));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getInventory().deleteItem(ASTRAL_RUNE, 2);
						player.getInventory().deleteItem(EARTH_RUNE, 15);
						player.getInventory().deleteItem(NATURE_RUNE, 1);
						player.getInventory().deleteItem(plank.getCoins());
						player.getInventory().deleteItem(plank.getLog());
						player.getInventory().addItem(plank.getPlank());
						player.sendMessage(
								"You make " + (Utils.startsWithVowel(plank.getPlank().getName()) ? "an " : "a ")
										+ plank.getPlank().getName().toLowerCase() + " out of the "
										+ plank.getLog().getName().toLowerCase() + ".");
					}
				}, 7);
			}
			break;
		case 49:// Share restore potion
			if (player.getLunarDelay() > Utils.currentTimeMillis())
				return;
			if (!Magic.checkSpellRequirements(player, 81, false, ASTRAL_RUNE, 2, EARTH_RUNE, 10, WATER_RUNE, 10))
				return;
			if (item.getName().contains("Prayer potion") || item.getName().contains("Super restore")
					|| item.getName().contains("Summoning potion")) {
				String oldItemName = item.getName();
				int[] prayer = { 2435, 139, 141, 143 };
				int[] superRestore = { 3024, 3026, 3028, 3030 };
				int[] summoning = { 12140, 12142, 12144, 12146 };
				int doses = (item.getName().contains("(4)") ? 4
						: item.getName().contains("(3)") ? 3 : item.getName().contains("(2)") ? 2 : 1);
				player.setLunarDelay(7000);
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							Magic.checkRunes(player, true, ASTRAL_RUNE, 2, EARTH_RUNE, 10, WATER_RUNE, 10);
							player.getSkills().addXp(Skills.MAGIC, 84);
							player.setNextAnimation(new Animation(4413));
						} else if (loop == 1) {
							int affected = 0;
							ArrayList<Player> players = new ArrayList<Player>();
							for (int regionId : player.getMapRegionsIds()) {
								List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
								if (playerIndexes == null)
									continue;
								for (int playerIndex : playerIndexes) {
									Player p2 = World.getPlayers().get(playerIndex);
									if (p2 == null || p2 == player || p2.isDead() || !p2.hasStarted()
											|| p2.hasFinished() || !p2.withinDistance(player, 2)
											|| !player.getControlerManager().canHit(p2))
										continue;
									else if (p2.getControlerManager().getControler() != null/*
											&& p2.getControlerManager().getControler() instanceof DuelArenaInsideController*/)
										continue;
									affected++;
									if (!players.contains(p2))
										players.add(p2);
									if (affected >= doses)
										break;
								}
							}
							if (affected <= 0) {
								this.stop();
								return;
							}

							for (Iterator<Player> it = players.iterator(); it.hasNext();) {
								Player p2 = it.next();
								if (p2 == null || p2.hasFinished() || !p2.hasStarted())
									continue;
								if (player == null || player.hasFinished() || !player.hasStarted())
									break;
								p2.setNextGraphics(new Graphics(728));
								player.setNextGraphics(new Graphics(728));
								p2.sendMessage(player.getDisplayName() + " shared a "
										+ item.getName().toLowerCase().replace(
												(doses == 4 ? "(4)" : doses == 3 ? "(3)" : doses == 2 ? "(2)" : "(1)"),
												"")
										+ " with you.");
								Pots.lunarPotRestoreShare(p2, item);
								int increment = (4 - doses);
								if (item.getName().contains("Prayer potion")) {
									if (affected != doses)
										item.setId(prayer[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Super restore")) {
									if (affected != doses)
										item.setId(superRestore[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Summoning potion")) {
									if (affected != doses)
										item.setId(summoning[affected + increment]);
									else
										item.setId(229);
								}
							}
							players.clear();

						} else if (loop == 2) {
							Pots.lunarPotRestoreShare(player, item);
							player.sendMessage(
									"You share a " + oldItemName.toLowerCase() + " with the surrounding players.");
							for (int i = 0; i < 27; i++)
								player.getInventory().refresh(i);
							this.stop();
						}
						loop++;

					}
				}, 0, 1);
			} else
				player.sendMessage("You cannot cast this spell on that.");
			break;
		case 48:// Share any nonrestore potion
			if (player.getLunarDelay() > Utils.currentTimeMillis())
				return;
			if (!Magic.checkSpellRequirements(player, 84, false, ASTRAL_RUNE, 3, EARTH_RUNE, 12, WATER_RUNE, 10))
				return;
			if (item.getName().contains("Super attack") || item.getName().contains("Super strength")
					|| item.getName().contains("Super defence") || item.getName().contains("Attack potion")
					|| item.getName().contains("Strength potion") || item.getName().contains("Defence potion")
					|| item.getName().contains("Ranging potion") || item.getName().contains("Magic potion")) {
				String oldItemName = item.getName();
				int[] superAttack = { 2436, 145, 147, 149 };
				int[] superStrength = { 2440, 157, 159, 161 };
				int[] superDefence = { 2442, 163, 165, 167 };
				int[] attack = { 2428, 121, 123, 125 };
				int[] strength = { 113, 115, 117, 119 };
				int[] defence = { 2432, 133, 135, 137 };
				int[] ranging = { 2444, 169, 171, 173 };
				int[] maging = { 3040, 3042, 3044, 3046 };
				int doses = (item.getName().contains("(4)") ? 4
						: item.getName().contains("(3)") ? 3 : item.getName().contains("(2)") ? 2 : 1);
				player.setLunarDelay(7000);
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							Magic.checkRunes(player, true, ASTRAL_RUNE, 3, EARTH_RUNE, 12, WATER_RUNE, 10);
							player.getSkills().addXp(Skills.MAGIC, 88);
							player.setNextAnimation(new Animation(4413));
						} else if (loop == 1) {
							int affected = 0;
							ArrayList<Player> players = new ArrayList<Player>();
							for (int regionId : player.getMapRegionsIds()) {
								List<Integer> playerIndexes = World.getRegion(regionId).getPlayerIndexes();
								if (playerIndexes == null)
									continue;
								for (int playerIndex : playerIndexes) {
									Player p2 = World.getPlayers().get(playerIndex);
									if (p2 == null || p2 == player || p2.isDead() || !p2.hasStarted()
											|| p2.hasFinished() || !p2.withinDistance(player, 2)
											|| !player.getControlerManager().canHit(p2))
										continue;
									else if (p2.getControlerManager().getControler() != null/*
											&& p2.getControlerManager().getControler() instanceof DuelArenaInsideController*/)
										continue;
									affected++;
									if (!players.contains(p2))
										players.add(p2);
									if (affected >= doses)
										break;
								}
							}
							if (affected <= 0) {
								this.stop();
								return;
							}

							for (Iterator<Player> it = players.iterator(); it.hasNext();) {
								Player p2 = it.next();
								if (p2 == null || p2.hasFinished() || !p2.hasStarted())
									continue;
								if (player == null || player.hasFinished() || !player.hasStarted())
									break;
								p2.setNextGraphics(new Graphics(728));
								player.setNextGraphics(new Graphics(728));
								p2.sendMessage(player.getDisplayName() + " shared a "
										+ item.getName().toLowerCase().replace(
												(doses == 4 ? "(4)" : doses == 3 ? "(3)" : doses == 2 ? "(2)" : "(1)"),
												"")
										+ " with you.");
								Pots.lunarPotRestoreShare(p2, item);
								int increment = (4 - doses);
								if (item.getName().contains("Super attack")) {
									if (affected != doses)
										item.setId(superAttack[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Super strength")) {
									if (affected != doses)
										item.setId(superStrength[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Super defence")) {
									if (affected != doses)
										item.setId(superDefence[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Attack potion")) {
									if (affected != doses)
										item.setId(attack[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Strength potion")) {
									if (affected != doses)
										item.setId(strength[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Defence potion")) {
									if (affected != doses)
										item.setId(defence[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Ranging potion")) {
									if (affected != doses)
										item.setId(ranging[affected + increment]);
									else
										item.setId(229);
								} else if (item.getName().contains("Magic potion")) {
									if (affected != doses)
										item.setId(maging[affected + increment]);
									else
										item.setId(229);
								}
							}
							players.clear();

						} else if (loop == 2) {
							Pots.lunarPotRestoreShare(player, item);
							player.sendMessage(
									"You share a " + oldItemName.toLowerCase() + " with the surrounding players.");
							for (int i = 0; i < 27; i++)
								player.getInventory().refresh(i);
							this.stop();
						}
						loop++;

					}
				}, 0, 1);
			} else
				player.sendMessage("You cannot cast this spell on that.");
			break;
		default:
			break;
		}
	}

	public static final void processLunarSpell(Player player, int spellId, int packetId) {
		player.stopAll(false);
		switch (spellId) {
		case 36:
			if (player.getSkills().getLevel(Skills.MAGIC) < 94) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			} else if (player.getSkills().getLevel(Skills.DEFENCE) < 40) {
				player.getPackets().sendGameMessage("You need a Defence level of 40 for this spell");
				return;
			}
			Long lastVeng = (Long) player.getTemporaryAttributtes().get("LAST_VENG");
			if (lastVeng != null && lastVeng + 30000 > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("Players may only cast vengeance once every 30 seconds.");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2, EARTH_RUNE, 10))
				return;
			player.setNextGraphics(new Graphics(726, 0, 100));
			player.setNextAnimation(new Animation(4410));
			player.setCastVeng(true);
			player.getTemporaryAttributtes().put("LAST_VENG", Utils.currentTimeMillis());
			player.getPackets().sendGameMessage("You cast a vengeance.");
			break;
		case 38:
			useHomeTele(player);
			break;
		case 19:
			if (player.getSkills().getLevel(Skills.MAGIC) < 93) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			} else if (player.getSkills().getLevel(Skills.DEFENCE) < 40) {
				player.getPackets().sendGameMessage("You need a Defence level of 40 for this spell");
				return;
			}
			if (!checkRunes(player, true, EARTH_RUNE, 11, DEATH_RUNE, 3, ASTRAL_RUNE, 4))
				return;
			break;
		case 74:
			Long lastVengGroup = (Long) player.getTemporaryAttributtes().get("LAST_VENGGRO UP");
			if (lastVengGroup != null && lastVengGroup + 30000 > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("Players may only cast vengeance group once every 30 seconds.");
				return;
			}
			if (!player.isAtMultiArea()) {
				player.getPackets().sendGameMessage("You can only cast vengeance group in a multi area.");
				return;
			}
			if (player.getSkills().getLevel(Skills.MAGIC) < 95) {
				player.getPackets().sendGameMessage("You need a level of 95 magic to cast vengeance group.");
				return;
			}
			if (!player.getInventory().containsItem(560, 3) && !player.getInventory().containsItem(557, 11)
					&& !player.getInventory().containsItem(9075, 4)) {
				player.getPackets().sendGameMessage("You don't have enough runes to cast vengeance group.");
				return;
			}
			int count = 0;
			for (Player other : World.getPlayers()) {
				if (other.withinDistance(player, 4)) {
					other.getPackets().sendGameMessage("Someone cast the Group Vengeance spell and you were affected!");
					other.setCastVeng(true);
					other.setNextGraphics(new Graphics(725, 0, 100));
					other.getTemporaryAttributtes().put("LAST_VENGGROU P", Utils.currentTimeMillis());
					other.getTemporaryAttributtes().put("LAST_VENG", Utils.currentTimeMillis());
					count++;
				}
			}
			player.getPackets().sendGameMessage("The spell affected " + count + " nearby people.");
			player.setNextGraphics(new Graphics(725, 0, 100)); // may not be
																// needed though
			player.setNextAnimation(new Animation(4410));
			player.setCastVeng(true);
			player.getTemporaryAttributtes().put("LAST_VENGGRO UP", Utils.currentTimeMillis());
			player.getTemporaryAttributtes().put("LAST_VENG", Utils.currentTimeMillis());
			player.getInventory().deleteItem(560, 3);
			player.getInventory().deleteItem(557, 11);
			player.getInventory().deleteItem(9075, 4);
			break;
		}
	}

	public static final void processAncientSpell(Player player, int spellId, int packetId) {
		player.stopAll(false);
		switch (spellId) {
		case 28:
		case 32:
		case 24:
		case 20:
		case 30:
		case 34:
		case 26:
		case 22:
		case 29:
		case 33:
		case 25:
		case 21:
		case 31:
		case 35:
		case 27:
		case 23:
		case 36:
		case 37:
		case 38:
		case 39:
			setCombatSpell(player, spellId);
			break;
		case 40:
			sendAncientTeleportSpell(player, 54, 64, new WorldTile(3099, 9882, 0), LAW_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE,
					1);
			break;
		case 41:
			sendAncientTeleportSpell(player, 60, 70, new WorldTile(3222, 3336, 0), LAW_RUNE, 2, SOUL_RUNE, 1);
			break;
		case 42:
			sendAncientTeleportSpell(player, 66, 76, new WorldTile(3492, 3471, 0), LAW_RUNE, 2, BLOOD_RUNE, 1);

			break;
		case 43:
			sendAncientTeleportSpell(player, 72, 82, new WorldTile(3006, 3471, 0), LAW_RUNE, 2, WATER_RUNE, 4);
			break;
		case 44:
			sendAncientTeleportSpell(player, 78, 88, new WorldTile(2990, 3696, 0), LAW_RUNE, 2, FIRE_RUNE, 3, AIR_RUNE,
					2);
			break;
		case 45:
			sendAncientTeleportSpell(player, 84, 94, new WorldTile(3217, 3677, 0), LAW_RUNE, 2, SOUL_RUNE, 2);
			break;
		case 46:
			sendAncientTeleportSpell(player, 90, 100, new WorldTile(3288, 3886, 0), LAW_RUNE, 2, BLOOD_RUNE, 2);
			break;
		case 47:
			sendAncientTeleportSpell(player, 96, 106, new WorldTile(2977, 3873, 0), LAW_RUNE, 2, WATER_RUNE, 8);
			break;
		case 48:
			useHomeTele(player);
			break;
		}
	}

	public static final void processNormalSpell(Player player, int spellId, int packetId) {
		player.stopAll(false);
		switch (spellId) {
		case 25: // air strike
		case 28: // water strike
		case 30: // earth strike
		case 32: // fire strike
		case 34: // air bolt
		case 39: // water bolt
		case 42: // earth bolt
		case 45: // fire bolt
		case 47:
		case 54: //iban blast
		case 56:
		case 49: // air blast
		case 52: // water blast
		case 58: // earth blast
		case 63: // fire blast
		case 70: // air wave
		case 73: // water wave
		case 77: // earth wave
		case 80: // fire wave
		case 99:
		case 84:
		case 87:
		case 89:
		case 91:
		case 36:
		case 55:
		case 81:
		case 66:
		case 67:
		case 68:
			setCombatSpell(player, spellId);
			break;
		case 27: // crossbow bolt enchant
			if (player.getSkills().getLevel(Skills.MAGIC) < 4) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			}
			player.stopAll();
			player.getInterfaceManager().sendInterface(432);
			player.getPackets().sendItemOnIComponent(432, 17, 879, 5);
			player.getPackets().sendItemOnIComponent(432, 31, 9337, 5);
			player.getPackets().sendItemOnIComponent(432, 21, 9335, 5);
			player.getPackets().sendItemOnIComponent(432, 25, 880, 5);
			player.getPackets().sendItemOnIComponent(432, 28, 9336, 5);
			player.getPackets().sendItemOnIComponent(432, 34, 9338, 5);
			player.getPackets().sendItemOnIComponent(432, 37, 9339, 5);
			player.getPackets().sendItemOnIComponent(432, 40, 9340, 5);
			player.getPackets().sendItemOnIComponent(432, 43, 9341, 5);
			player.getPackets().sendItemOnIComponent(432, 46, 9342, 5);
			break;
		case 24:
			useHomeTele(player);
			break;
		case 37: // mobi
			sendNormalTeleportSpell(player, 10, 19, new WorldTile(2413, 2848, 0), LAW_RUNE, 1, WATER_RUNE, 1, AIR_RUNE,
					1);
			break;
		case 40: // varrock
			sendNormalTeleportSpell(player, 25, 19, new WorldTile(3212, 3424, 0), FIRE_RUNE, 1, AIR_RUNE, 3, LAW_RUNE,
					1);
			break;
		case 43: // lumby
			/*AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
			if (!diary.isComplete(1, 2)) {
				diary.updateTask(player, 1, 2, true);
			}*/
			sendNormalTeleportSpell(player, 31, 41, new WorldTile(3222, 3218, 0), EARTH_RUNE, 1, AIR_RUNE, 3, LAW_RUNE,
					1);
			break;
		case 46: // fally
			sendNormalTeleportSpell(player, 37, 48, new WorldTile(2964, 3379, 0), WATER_RUNE, 1, AIR_RUNE, 3, LAW_RUNE,
					1);
			break;
		case 51: // camelot
			sendNormalTeleportSpell(player, 45, 55.5, new WorldTile(2757, 3478, 0), AIR_RUNE, 5, LAW_RUNE, 1);
			break;
		case 57: // ardy
			sendNormalTeleportSpell(player, 51, 61, new WorldTile(2664, 3305, 0), WATER_RUNE, 2, LAW_RUNE, 2);
			break;
		case 62: // watch
			sendNormalTeleportSpell(player, 58, 68, new WorldTile(2547, 3113, 2), EARTH_RUNE, 2, LAW_RUNE, 2);
			break;
		case 69: // troll
			sendNormalTeleportSpell(player, 61, 68, new WorldTile(2888, 3674, 0), FIRE_RUNE, 2, LAW_RUNE, 2);
			break;
		case 72: // ape
			sendNormalTeleportSpell(player, 64, 76, new WorldTile(2776, 9103, 0), FIRE_RUNE, 2, WATER_RUNE, 2, LAW_RUNE,
					2, 1963, 1);
			break;
		}
	}

	public static void useHomeTele(Player player) {
		player.stopAll();
		if (player.getInterfaceManager().containsChatBoxInter())
			player.getInterfaceManager().closeChatBoxInterface();
		player.resetWalkSteps();
		if (BountyHunter.inBounty == true) {
			return;
		} else {
		sendNormalTeleportSpell(player, 0, 0, new WorldTile(Settings.RESPAWN_PLAYER_LOCATION));
	}
}
	
	public static void pushLeverTeleport(final Player player,
			final WorldTile tile) {
		if (!player.getControlerManager().processObjectTeleport(tile))
			return;
		player.setNextAnimation(new Animation(2140));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.unlock();
				Magic.sendObjectTeleportSpell(player, false, tile);
			}
		}, 1);
	}

	public static final boolean checkSpellRequirements(Player player, int level, boolean delete, int... runes) {
		if (player.getSkills().getLevelForXp(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}

	public static final boolean checkRunes(Player player, boolean delete, int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		List<Integer> missingRunes = new ArrayList<Integer>();
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (hasInfiniteRunes(runeId, weaponId, shieldId))
				continue;
			if (!player.getInventory().containsItem(runeId, ammount))
				missingRunes.add(runeId);

		}
		if (missingRunes.size() != 0) {
			switch (missingRunes.size()) {
			case 4:
				player.getPackets().sendGameMessage("You do not have enough "
						+ ItemDefinitions.getItemDefinitions(missingRunes.get(0)).getName().replace(" rune", "") + ", "
						+ ItemDefinitions.getItemDefinitions(missingRunes.get(1)).getName().replace(" rune", "") + ", "
						+ ItemDefinitions.getItemDefinitions(missingRunes.get(2)).getName().replace(" rune", "")
						+ " and " + ItemDefinitions.getItemDefinitions(missingRunes.get(3)).getName() + "s"
						+ " to cast this spell.");
				return false;
			case 3:
				player.getPackets().sendGameMessage("You do not have enough "
						+ ItemDefinitions.getItemDefinitions(missingRunes.get(0)).getName().replace(" rune", "") + ", "
						+ ItemDefinitions.getItemDefinitions(missingRunes.get(1)).getName().replace(" rune", "")
						+ " and " + ItemDefinitions.getItemDefinitions(missingRunes.get(2)).getName() + "s"
						+ " to cast this spell.");
				return false;
			case 2:
				player.getPackets()
						.sendGameMessage("You do not have enough "
								+ ItemDefinitions.getItemDefinitions(missingRunes.get(0)).getName().replace(" rune", "")
								+ " and " + ItemDefinitions.getItemDefinitions(missingRunes.get(1)).getName() + "s"
								+ " to cast this spell.");
				return false;
			case 1:
				player.getPackets().sendGameMessage("You do not have enough "
						+ ItemDefinitions.getItemDefinitions(missingRunes.get(0)).getName() + "s to cast this spell.");
				return false;
			default:
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int ammount = runes[runesCount++];
				if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
					continue;
			}
				player.getInventory().deleteItem(runeId, ammount);
			}
		}
		return true;
	}
	
	public static boolean checkSpellLevel(Player player, int level) {
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		return true;
	}

	private static final void sendAncientTeleportSpell(Player player, int level, double xp, WorldTile tile,
			int... runes) {

		sendTeleportSpell(player, 1979, -1, 1681, -1, level, xp, tile, 5, true, MAGIC_TELEPORT, runes);
	}

	public static final void sendNormalTeleportSpell(Player player, int level, double xp, WorldTile tile, int... runes) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3, true, MAGIC_TELEPORT, runes);
	}

	public static final boolean sendItemTeleportSpell(Player player, boolean randomize, int upEmoteId, int upGraphicId,
			int delay, WorldTile tile) {
		return sendTeleportSpell(player, upEmoteId, -2, upGraphicId, -1, 0, 0, tile, delay, randomize, ITEM_TELEPORT);
	}

	public static final void sendObjectTeleportSpell(Player player, boolean randomize, WorldTile tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, 3, randomize, OBJECT_TELEPORT);
	}
	
	private final static WorldTile[] TABS = { new WorldTile(3217, 3426, 0),
			new WorldTile(3222, 3218, 0), new WorldTile(2965, 3379, 0),
			new WorldTile(2758, 3478, 0), new WorldTile(2660, 3306, 0) };
	
	public static boolean useTabTeleport(final Player player, final int itemId) {
		if (itemId < 8007 || itemId > 8007 + TABS.length - 1)
			return false;
		if (useTeleTab(player, TABS[itemId - 8007]))
			player.getInventory().deleteItem(itemId, 1);
		return true;
	}
	
	public static final void processLunarOnNpc(final Player player, int spellId, final NPC npc) {
		if (player == null || player.hasFinished() || !player.hasStarted() || npc == null || npc.isDead()
				|| npc.hasFinished())
			return;
		if (player.getLunarDelay() > Utils.currentTimeMillis() || player.isLocked())
			return;
		switch (spellId) {
		case 28:// monster examine
			player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {

				@Override
				public void run() {
					if (npc == null || npc.isDead() || npc.hasFinished() || !player.withinDistance(npc, 3))
						return;
					if (!Magic.checkSpellRequirements(player, 66, true, ASTRAL_RUNE, 1, COSMIC_RUNE, 1, MIND_RUNE, 1))
						return;
					player.setLunarDelay(3000);
					player.getSkills().addXp(Skills.MAGIC, 61);
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (npc == null || npc.isDead() || npc.hasFinished() || player == null
									|| !player.hasStarted() || player.hasFinished()) {
								this.stop();
								return;
							}
							if (loop == 0) {
								player.faceEntity(npc);
								player.setNextGraphics(new Graphics(1059));
								player.setNextAnimation(new Animation(6293));
							} else if (loop == 1) {
								npc.setNextGraphics(new Graphics(736));
								player.getInterfaceManager()
										.sendTab(player.getInterfaceManager().hasRezizableScreen() ? 36 : 157, 522);
								player.getPackets().sendIComponentText(522, 0,
										"Monster Name:<br>" + npc.getDefinitions().getName());
								player.getPackets().sendIComponentText(522, 1, "Combat Level: " + npc.getCombatLevel());
								player.getPackets().sendIComponentText(522, 2,
										"Hitpoints: " + npc.getHitpoints() + "/" + npc.getMaxHitpoints());
								player.getPackets().sendIComponentText(522, 3,
										"Creature's max hit: " + npc.getMaxHit());
								player.getPackets().sendIComponentText(522, 4,
										"" + NPCExamines.getExamine(npc.getId()));
								this.stop();// stops the loop
							}
							loop++;
						}
					}, 0, 1);
				}
			}, npc.getSize()));

			break;
		}
	}

	public static final boolean sendTeleportSpell(final Player player, int upEmoteId, final int downEmoteId,
			int upGraphicId, final int downGraphicId, int level, final double xp, final WorldTile tile, int delay,
			final boolean randomize, final int teleType, int... runes) {
		long currentTime = Utils.currentTimeMillis();
		if (player.getInterfaceManager().containsChatBoxInter())
			player.getInterfaceManager().closeChatBoxInterface();
		if (player.getLockDelay() > currentTime)
			return false;
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!checkRunes(player, false, runes))
			return false;
		if (teleType == MAGIC_TELEPORT) {
			if (!player.getControlerManager().processMagicTeleport(tile))
				return false;
		} else if (teleType == ITEM_TELEPORT) {
			if (!player.getControlerManager().processItemTeleport(tile))
				return false;
		} else if (teleType == OBJECT_TELEPORT) {
			if (!player.getControlerManager().processObjectTeleport(tile))
				return false;
		}
		checkRunes(player, true, runes);
		player.stopAll();
		if (upEmoteId != -1)
			player.setNextAnimation(new Animation(upEmoteId));
		if (upGraphicId != -1)
			player.setNextGraphics(new Graphics(upGraphicId));
		if (teleType == MAGIC_TELEPORT)
			player.getPackets().sendSound(200, 0);
		player.lock(3 + delay);
		WorldTasksManager.schedule(new WorldTask() {

			boolean removeDamage;

			@Override
			public void run() {
				if (!removeDamage) {
					WorldTile teleTile = tile;
					if (randomize) {
						// attemps to randomize tile by 4x4 area
						for (int trycount = 0; trycount < 10; trycount++) {
							teleTile = new WorldTile(tile, 2);
							if (World.canMoveNPC(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize()))
								break;
							teleTile = tile;
						}
					}
					player.setNextWorldTile(teleTile);
					player.getControlerManager().magicTeleported(teleType);
					if (player.getControlerManager().getControler() == null)
						teleControlersCheck(player, teleTile);
					if (xp != 0)
						player.getSkills().addXp(Skills.MAGIC, xp);
					if (downEmoteId != -1)
						player.setNextAnimation(new Animation(downEmoteId == -2 ? -1 : downEmoteId));
					if (downGraphicId != -1)
						player.setNextGraphics(new Graphics(downGraphicId));
					if (teleType == MAGIC_TELEPORT) {
						player.getPackets().sendSound(5524, 0);
						player.setNextFaceWorldTile(
								new WorldTile(teleTile.getX(), teleTile.getY() - 1, teleTile.getPlane()));
						player.setDirection(6);
						player.getPackets().sendSound(201, 0);
					}
					removeDamage = true;
				} else {
					player.resetReceivedDamage();
					stop();
				}
			}
		}, delay, 0);
		return true;
	}

	public static void teleControlersCheck(Player player, WorldTile teleTile) {
		if (Wilderness.isAtWild(teleTile))
			player.getControlerManager().startControler("Wilderness");
	}

	private Magic() {

	}
}
