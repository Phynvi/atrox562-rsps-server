package com.rs.game.player.actions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;

import com.rs.game.npc.pest.PestPortal;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Combat;
import com.rs.game.player.content.Magic;
import com.rs.game.player.skills.slayer.Slayer;
import com.rs.game.player.skills.slayer.Slayer.SlayerTask;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.pestcontrol.PestControlGame;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.Utils;

public class PlayerCombat extends Action {

	private Entity target;
	public int max_hit; // temporary constant
	private double base_mage_xp; // temporary constant
	private int mage_hit_gfx; // temporary constant
	private int magic_sound; // temporary constant
	private int max_poison_hit; // temporary constant
	private int freeze_time; // temporary constant
	@SuppressWarnings("unused")
	private boolean reduceAttack; // temporary constant
	private boolean blood_spell; // temporary constant
	private boolean block_tele;
	private int spellcasterGloves;
	private int spell_type = -1;

	private static final int AIR_SPELL = 0, FIRE_SPELL = 3, SMOKE_SPELL = 4, SHADOW_SPELL = 5, BLOOD_SPELL = 6,
			ICE_SPELL = 7;

	public PlayerCombat(Entity target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		if (player == null) {
			return false;
		}
		player.setNextFaceEntity(target);
		if (checkAll(player)) {
			return true;
		}
		player.setNextFaceEntity(null);
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	private boolean forceCheckClipAsRange(Entity target) {
		return target instanceof PestPortal;
	}

	@Override
	public int processWithDelay(Player player) {
		int isRanging = isRanging(player);
		int spellId = player.getCombatDefinitions().getSpellId();
		int maxDistance = isRanging != 0 ? getAttackDistance(player) : spellId > 0 || hasPolyporeStaff(player) ? 8 : 0;
		if (spellId < 1 && hasPolyporeStaff(player)) {
			spellId = 65535;
		}
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		double multiplier = 1.0;
		double magicMultiplier = 1.0;
		if (target.isDead())
			return 0;
		if (target.hasWalkSteps() && player.hasWalkSteps() && player.getFreezeDelay() < Utils.currentTimeMillis())
			maxDistance += target.hasWalkSteps() ? 2 : 1;
		if (player.temporaryAttribute().get("miasmic_effect") == Boolean.TRUE)
			multiplier = 1.5;
		if (player.temporaryAttribute().get("bakriminel_effect") == Boolean.TRUE)
			magicMultiplier = 1.5;
		int size = target.getSize();
		if (!player.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target)))
			return 0;
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return 0;
		if (!player.getControlerManager().keepCombating(target))
			return -1;
		if (spellId > 0) {
			boolean manualCast = spellId != 65535 && spellId >= 256;
			Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
			spellcasterGloves = gloves != null && gloves.getDefinitions().getName().contains("Spellcaster glove")
					&& player.getEquipment().getWeaponId() == -1 && new Random().nextInt(30) == 0 ? spellId : -1;
			int delay = mageAttack(player, manualCast ? spellId - 256 : spellId, !manualCast);
			if (player.getNextAnimation() != null && spellcasterGloves > 0) {
				player.setNextAnimation(new Animation(14339));
				spellcasterGloves = -1;
			}
			return (int) (delay * magicMultiplier);
		} else {
			if (isRanging == 0) {
				return (int) (meleeAttack(player) * multiplier);
			} else if (isRanging == 1) {
				player.getPackets().sendGameMessage("This ammo is not very effective with this weapon.");
				return -1;
			} else if (isRanging == 3) {
				player.getPackets().sendGameMessage("You dont have any ammo in your backpack.");
				return -1;
			} else {
				return (int) (rangeAttack(player) * multiplier);
			}
		}
	}

	private void addAttackedByDelay(Entity player) {
		target.setAttackedBy(player);
		target.setAttackedByDelay(Utils.currentTimeMillis() + 5000); // 8seconds
	}

	private int getRangeCombatDelay(Player player, int weaponId, int attackStyle) {
		int delay = 6;
		if (weaponId != -1) {
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("shortbow") || weaponId == 4734 || weaponId == 4934 || weaponId == 4935
					|| weaponId == 4936 || weaponId == 4937 || weaponId == 10156)
				delay = 3;
			else if (weaponName.contains("crystal") || weaponName.contains("crossbow"))
				delay = 5;
			else if (weaponName.contains("chinchompa"))
				delay = 2;
			else if (weaponId == 6522)
				delay = 2;
			else if (weaponName.contains("knife") || weaponName.contains("sling"))
				delay = 2;
			else if (weaponName.contains("dart")) {
				delay = 2;
			} else {
				switch (weaponId) {
				case 15241:
					delay = 8;
					break;
				case 11235: // dark bows
				case 15701:
				case 15702:
				case 15703:
				case 15704:
					delay = 9;
					break;
				default:
					delay = 6;
					break;
				}
			}
		}
		final String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if (attackStyle == 1) {
			delay--;
		} else if (attackStyle == 2) {
			if (player.getEquipment().getAmmoId() == 24116 && weaponName.contains("crossbow"))
				delay--;
			else
				delay++;
		}
		if (player.getEquipment().getAmmoId() == 24116 && weaponName.contains("crossbow"))
			return delay--;
		else
			return delay;
	}

	public Entity[] getMultiAttackTargets(Player player) {
		return getMultiAttackTargets(player, 1, 9);
	}

	public Entity[] getMultiAttackTargets(Player player, int maxDistance, int maxAmtTargets) {
		List<Entity> possibleTargets = new ArrayList<Entity>();
		possibleTargets.add(target);
		if (target.isAtMultiArea()) {
			y: for (int regionId : target.getMapRegionsIds()) {
				Region region = World.getRegion(regionId);
				if (target instanceof Player) {
					List<Integer> playerIndexes = region.getPlayerIndexes();
					if (playerIndexes == null)
						continue;
					for (int playerIndex : playerIndexes) {
						Player p2 = World.getPlayers().get(playerIndex);
						if (p2 == null || p2 == player || p2 == target || p2.isDead() || !p2.hasStarted()
								|| p2.hasFinished() || !p2.isCanPvp() || !p2.isAtMultiArea()
								|| !p2.withinDistance(target, maxDistance) || !player.getControlerManager().canHit(p2))
							continue;
						possibleTargets.add(p2);
						if (possibleTargets.size() == maxAmtTargets)
							break y;
					}
				} else {
					List<Integer> npcIndexes = region.getNPCsIndexes();
					if (npcIndexes == null)
						continue;
					for (int npcIndex : npcIndexes) {
						NPC n = World.getNPCs().get(npcIndex);
						if (n == null || n == target || n == player.getFamiliar() || n.isDead() || n.hasFinished()
								|| !n.isAtMultiArea() || !n.withinDistance(target, maxDistance)
								|| !n.getDefinitions().hasAttackOption() || !player.getControlerManager().canHit(n))
							continue;
						possibleTargets.add(n);
						if (possibleTargets.size() == maxAmtTargets)
							break y;
					}
				}
			}
		}
		return possibleTargets.toArray(new Entity[possibleTargets.size()]);
	}

	private long teleBlockTime;

	public int mageAttack(final Player player, int spellId, boolean autocast) {
		if (!autocast) {
			player.getCombatDefinitions().resetSpells(false);
			player.getActionManager().forceStop();
		}
		if (!Magic.checkCombatSpell(player, spellId, -1, true)) {
			if (autocast)
				player.getCombatDefinitions().resetSpells(true);
			return -1;
		}
		if (spellId == 65535) {
			player.setNextFaceEntity(target);
			player.setNextGraphics(new Graphics(2034));
			player.setNextAnimation(new Animation(15448));
			mage_hit_gfx = 2036;
			delayMagicHit(Utils.getDistance(player, target) > 3 ? 2 : 1, getMagicHit(player,
					getRandomMagicMaxHit(player, (5 * player.getSkills().getLevel(Skills.MAGIC)) - 180)));
			World.sendElementalProjectile(player, target, 2035);
			return 4;
		}
		if (player.getCombatDefinitions().getSpellBook() == 192) {
			switch (spellId) {
			case 54:// iban blast
				player.setNextGraphics(new Graphics(87));
				player.setNextAnimation(new Animation(708));
				mage_hit_gfx = 89;
				base_mage_xp = 30;
				int baseDamage = 250;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 88);
				return 4;
			case 56:// slayer dart
				player.setNextGraphics(new Graphics(219));
				player.setNextAnimation(new Animation(1576));
				mage_hit_gfx = 331;
				base_mage_xp = 30;
				baseDamage = player.getSkills().getLevel(Skills.MAGIC) / 6 + 130;
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 330);
				return 4;
			case 25:// wind strike
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 92;
				base_mage_xp = 5.5;
				baseDamage = 20;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 90;
				}
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 91);
				return 4;
			case 28:// water strike
				player.setNextGraphics(new Graphics(93, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 95;
				base_mage_xp = 7.5;
				baseDamage = 40;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 100;
				}
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 94);
				return 4;
			case 36:
				if (target.getFreezeDelay() < Utils.currentTimeMillis()) {
					player.setNextGraphics(new Graphics(177, 0, 100));
					player.setNextAnimation(new Animation(710));
					mage_hit_gfx = 181;
					base_mage_xp = 60.5;
					Hit bindHit = getMagicHit(player, getRandomMagicMaxHit(player, 20));
					delayMagicHit(getMageDelay(player, target), bindHit);
					World.sendFastBowProjectile(player, target, 178);
					if (bindHit.getDamage() > 0) {
						target.addFreezeDelay(5000, true);
						World.sendElementalProjectile(player, target, 178);
					}
				} else {
					player.getPackets().sendGameMessage("This player is already effected by this spell.", true);
					return -1;
				}
				return 4;
			case 55:
				if (target.getFreezeDelay() < Utils.currentTimeMillis()) {
					player.setNextGraphics(new Graphics(177, 0, 100));
					player.setNextAnimation(new Animation(710));
					mage_hit_gfx = 180;
					base_mage_xp = 91.1;
					Hit snareHit = getMagicHit(player, getRandomMagicMaxHit(player, 30));
					delayMagicHit(getMageDelay(player, target), snareHit);
					if (snareHit.getDamage() > 0) {
						target.addFreezeDelay(10000, true);
						World.sendElementalProjectile(player, target, 178);
					}
				} else {
					player.getPackets().sendGameMessage("This player is already effected by this spell.", true);
					return -1;
				}
				return 4;
			case 81:// entangle
				if (target.getFreezeDelay() < Utils.currentTimeMillis()) {
					player.setNextGraphics(new Graphics(177, 0, 100));
					player.setNextAnimation(new Animation(710));
					mage_hit_gfx = 179;
					base_mage_xp = 91.1;
					Hit entangleHit = getMagicHit(player, getRandomMagicMaxHit(player, 50));
					delayMagicHit(getMageDelay(player, target), entangleHit);
					if (entangleHit.getDamage() > 0) {
						target.addFreezeDelay(15000, true);
						World.sendElementalProjectile(player, target, 178);
					}
				} else {
					player.getPackets().sendGameMessage("This player is already effected by this spell.", true);
					return -1;
				}
				return 4;
			case 30://earth strike
				player.setNextGraphics(new Graphics(96));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 98;
				base_mage_xp = 9.5;
				baseDamage = 60;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 110;
				}
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 97);
				return 4;
			case 32: //fire strike
				player.setNextGraphics(new Graphics(99, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 101;
				base_mage_xp = 11.5;
				spell_type = FIRE_SPELL;
				baseDamage = 80;
				if (player.getEquipment().getGlovesId() == 205) {
					baseDamage = 120;
				}
				int damage = getRandomMagicMaxHit(player, baseDamage);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463)
						damage *= 2;
				}
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
				World.sendElementalProjectile(player, target, 100);
				return 4;
			case 34: //wind bolt
				player.setNextGraphics(new Graphics(117, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 119;
				base_mage_xp = 13.5;
				baseDamage = 90;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 120;
				}
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 118);
				return 4;
			case 39://water bolt
				player.setNextGraphics(new Graphics(120, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 122;
				base_mage_xp = 16.5;
				baseDamage = 100;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 130;
				}
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 121);
				return 4;
			case 42://earth bolt
				player.setNextGraphics(new Graphics(123, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 125;
				base_mage_xp = 19.5;
				baseDamage = 110;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 140;
				}
				delayMagicHit(getMageDelay(player, target),
						getMagicHit(player, getRandomMagicMaxHit(player, baseDamage)));
				World.sendElementalProjectile(player, target, 124);
				return 4;
			case 45://fire bolt
				player.setNextGraphics(new Graphics(126, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 128;
				base_mage_xp = 22.5;
				spell_type = FIRE_SPELL;
				baseDamage = 120;
				if (player.getEquipment().getGlovesId() == 777) {
					baseDamage = 150;
				}
				damage = getRandomMagicMaxHit(player, baseDamage);
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
				World.sendElementalProjectile(player, target, 127);
				return 4;
			case 49://wind blast
				player.setNextGraphics(new Graphics(132, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 134;
				base_mage_xp = 25.5;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 130)));
				World.sendElementalProjectile(player, target, 133);
				return 4;
			case 52://water blast
				player.setNextGraphics(new Graphics(135, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 137;
				base_mage_xp = 31.5;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 140)));
				World.sendElementalProjectile(player, target, 136);
				return 4;
			case 58://earth blast
				player.setNextGraphics(new Graphics(138, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 140;
				base_mage_xp = 31.5;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 150)));
				World.sendElementalProjectile(player, target, 139);
				return 4;
			case 63://fire blast
				player.setNextGraphics(new Graphics(129, 0, 100));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 131;
				base_mage_xp = 34.5;
				spell_type = FIRE_SPELL;
				damage = getRandomMagicMaxHit(player, 160);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463)
						damage *= 2;
				}
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
				World.sendProjectile(player, target, 130, 46, 31, 50, 50, 0, 0);
				return 4;
			case 66:
				player.setNextAnimation(new Animation(811));
				mage_hit_gfx = 76;
				base_mage_xp = 34.5;
				damage = getRandomMagicMaxHit(player, 200);
				delayMagicHit(Utils.getDistance(player, target) > 3 ? 2 : 1, getMagicHit(player, damage));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					if (damage > 0)
						p2.getPrayer().drainPrayer(10);
				}
				return 4;
			case 67:
				player.setNextAnimation(new Animation(811));
				mage_hit_gfx = 77;
				base_mage_xp = 34.5;
				damage = getRandomMagicMaxHit(player, 200);
				delayMagicHit(Utils.getDistance(player, target) > 3 ? 2 : 1, getMagicHit(player, damage));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					if (damage > 0)
						p2.getSkills().drainLevel(Skills.DEFENCE,
								(int) (p2.getSkills().getLevel(Skills.DEFENCE) * 0.05));
				}
				return 4;
			case 68:
				player.setNextAnimation(new Animation(811));
				mage_hit_gfx = 78;
				base_mage_xp = 34.5;
				damage = getRandomMagicMaxHit(player, 200);
				delayMagicHit(Utils.getDistance(player, target) > 3 ? 2 : 1, getMagicHit(player, damage));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					if (damage > 0)
						p2.getSkills().drainLevel(Skills.MAGIC, (int) (p2.getSkills().getLevel(Skills.MAGIC) * 0.05));
				}
				return 4;
			case 70://wind wave
				player.setNextGraphics(new Graphics(158));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 160;
				base_mage_xp = 36;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 170)));
				World.sendElementalProjectile(player, target, 159);
				return 4;
			case 73://water wave
				player.setNextGraphics(new Graphics(161));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 163;
				base_mage_xp = 37.5;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 180)));
				World.sendElementalProjectile(player, target, 162);
				return 4;
			case 77://earth wave
				player.setNextGraphics(new Graphics(164));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 166;
				base_mage_xp = 42.5;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 190)));
				World.sendElementalProjectile(player, target, 165);
				return 4;
			case 80://fire wave
				player.setNextGraphics(new Graphics(155));
				player.setNextAnimation(new Animation(711));
				mage_hit_gfx = 157;
				base_mage_xp = 42.5;
				spell_type = FIRE_SPELL;
				damage = getRandomMagicMaxHit(player, 200);
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					if (n.getId() == 9463)
						damage *= 2;
				}
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
				World.sendElementalProjectile(player, target, 156);
				return 4;
			case 86:
				if (target instanceof Player && ((Player) target).getTeleBlockDelay() <= Utils.currentTimeMillis()
						&& ((Player) target).getTeleBlockImmune() <= Utils.currentTimeMillis()) {
					player.setNextGraphics(new Graphics(1841));
					player.setNextAnimation(new Animation(10503));
					mage_hit_gfx = 1843;
					base_mage_xp = 80;
					block_tele = true;
					if (target instanceof Player) {
						Player targetPlayer = (Player) target;
						teleBlockTime = (targetPlayer.getPrayer().usingPrayer(0, 17)
								|| targetPlayer.getPrayer().usingPrayer(1, 7) ? 150000 : 300000);
					}
					Hit hit = getMagicHit(player, getRandomMagicMaxHit(player, 30));
					delayMagicHit(2, hit);
					World.sendElementalProjectile(player, target, 1842);
				} else {
					player.getPackets().sendGameMessage("This player is already effected by this spell.", true);
				}
				return 4;
			case 84:
				player.setNextGraphics(new Graphics(457));
				player.setNextAnimation(new Animation(10546));
				mage_hit_gfx = 2700;
				base_mage_xp = 80;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 220)));
				World.sendElementalProjectile(player, target, 462);
				return 4;
			case 87:
				player.setNextGraphics(new Graphics(2701));
				player.setNextAnimation(new Animation(10542));
				mage_hit_gfx = 2712;
				base_mage_xp = 80;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 240)));
				World.sendElementalProjectile(player, target, 2707);
				return 4;
			case 89:
				player.setNextGraphics(new Graphics(2717));
				player.setNextAnimation(new Animation(14209));
				mage_hit_gfx = 2727;
				base_mage_xp = 80;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 260)));
				World.sendElementalProjectile(player, target, 2722);
				return 4;
			case 91:
				player.setNextGraphics(new Graphics(2728));
				player.setNextAnimation(new Animation(2791));
				mage_hit_gfx = 2741;
				base_mage_xp = 80;
				spell_type = FIRE_SPELL;
				damage = getRandomMagicMaxHit(player, 280);
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
				World.sendElementalProjectile(player, target, 2735);
				World.sendElementalProjectile(player, target, 2736);
				World.sendElementalProjectile(player, target, 2736);
				return 4;
			case 99:
				player.setNextGraphics(new Graphics(457));
				player.setNextAnimation(new Animation(10546));
				mage_hit_gfx = 1019;
				base_mage_xp = 70;
				spell_type = AIR_SPELL;
				int minDamage = 160 + (player.getSkills().getLevelForXp(Skills.MAGIC) - 77) * 5;
				int boost = (player.getSkills().getLevelForXp(Skills.MAGIC) - 77) * 5;
				int hit = getRandomMagicMaxHit(player, minDamage + boost);
				if (hit > 0 && hit < boost)
					hit += boost;
				delayMagicHit(Utils.getDistance(player, target) > 3 ? 2 : 1, getMagicHit(player, hit));
				World.sendSOAProjectile(player, target, 1019);
				return player.getEquipment().getWeaponId() == 21777 ? 3 : 5;
			}
		} else if (player.getCombatDefinitions().getSpellBook() == 193) {
			switch (spellId) {
			case 28:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 385;
				base_mage_xp = 30;
				max_poison_hit = 20;
				spell_type = SMOKE_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 150)));
				World.sendProjectile(player, target, 386, 28, 18, 50, 50, 0);
				return 4;
			case 32:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 379;
				base_mage_xp = 31;
				reduceAttack = true;
				spell_type = SHADOW_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 160)));
				World.sendProjectile(player, target, 380, 28, 18, 50, 50, 0);
				return 4;
			case 36:
				player.setNextAnimation(new Animation(10513));
				player.setNextGraphics(new Graphics(1845));
				mage_hit_gfx = 1847;
				base_mage_xp = 35;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 200)));
				World.sendProjectile(player, target, 1846, 43, 22, 51, 50, 16);
				if (target.temporaryAttribute().get("miasmic_immunity") == Boolean.TRUE) {
					return 4;
				}
				if (target instanceof Player) {
					((Player) target).getPackets().sendGameMessage("You feel slowed down.");
				}
				target.temporaryAttribute().put("miasmic_immunity", Boolean.TRUE);
				target.temporaryAttribute().put("miasmic_effect", Boolean.TRUE);
				final Entity t = target;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						t.temporaryAttribute().remove("miasmic_effect");
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								t.temporaryAttribute().remove("miasmic_immunity");
								stop();
							}
						}, 15);
						stop();
					}
				}, 20);
				return 4;
			case 37:
				player.setNextAnimation(new Animation(10524));
				player.setNextGraphics(new Graphics(1850));
				mage_hit_gfx = 1851;
				base_mage_xp = 48;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 280)));
				World.sendProjectile(player, target, 1852, 43, 22, 51, 50, 16);
				if (target.temporaryAttribute().get("miasmic_immunity") == Boolean.TRUE) {
					return 4;
				}
				if (target instanceof Player) {
					((Player) target).getPackets().sendGameMessage("You feel slowed down.");
				}
				target.temporaryAttribute().put("miasmic_immunity", Boolean.TRUE);
				target.temporaryAttribute().put("miasmic_effect", Boolean.TRUE);
				final Entity t0 = target;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						t0.temporaryAttribute().remove("miasmic_effect");
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								t0.temporaryAttribute().remove("miasmic_immunity");
								stop();
							}
						}, 15);
						stop();
					}
				}, 60);
				return 4;
			case 38:
				player.setNextAnimation(new Animation(10516));
				player.setNextGraphics(new Graphics(1848));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {
					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 1849;
						base_mage_xp = 42;
						int damage = getRandomMagicMaxHit(player, 240);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						if (target.temporaryAttribute().get("miasmic_immunity") != Boolean.TRUE) {
							if (target instanceof Player) {
								((Player) target).getPackets().sendGameMessage("You feel slowed down.");
							}
							target.temporaryAttribute().put("miasmic_immunity", Boolean.TRUE);
							target.temporaryAttribute().put("miasmic_effect", Boolean.TRUE);
							final Entity t = target;
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									t.temporaryAttribute().remove("miasmic_effect");
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											if (player == null || t == null || player.isDead() || player.hasFinished()
													|| t.isDead() || t.hasFinished())
												return;
											t.temporaryAttribute().remove("miasmic_immunity");
											stop();
										}
									}, 15);
									stop();
								}
							}, 40);
						}
						if (!nextTarget) {
							if (damage == -1) {
								return false;
							}
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 39:
				player.setNextAnimation(new Animation(10518));
				player.setNextGraphics(new Graphics(1853));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {
					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 1854;
						base_mage_xp = 54;
						int damage = getRandomMagicMaxHit(player, 320);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						if (target.temporaryAttribute().get("miasmic_immunity") != Boolean.TRUE) {
							if (target instanceof Player) {
								((Player) target).getPackets().sendGameMessage("You feel slowed down.");
							}
							target.temporaryAttribute().put("miasmic_immunity", Boolean.TRUE);
							target.temporaryAttribute().put("miasmic_effect", Boolean.TRUE);
							final Entity t = target;
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									t.temporaryAttribute().remove("miasmic_effect");
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											if (player == null || t == null || player.isDead() || player.hasFinished()
													|| t.isDead() || t.hasFinished())
												return;
											t.temporaryAttribute().remove("miasmic_immunity");
											stop();
										}
									}, 15);
									stop();
								}
							}, 80);
						}
						if (!nextTarget) {
							if (damage == -1) {
								return false;
							}
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 24:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 373;
				base_mage_xp = 33;
				blood_spell = true;
				spell_type = BLOOD_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 170)));
				World.sendProjectile(player, target, 374, 18, 18, 50, 50, 0);
				return 4;
			case 20:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 361;
				base_mage_xp = 34;
				freeze_time = 5000;
				spell_type = ICE_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 180)));
				World.sendProjectile(player, target, 362, 18, 18, 50, 50, 0);
				return 4;

			case 30:
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 389;
						base_mage_xp = 36;
						max_poison_hit = 20;
						spell_type = SMOKE_SPELL;
						int damage = getRandomMagicMaxHit(player, 190);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						World.sendProjectile(player, target, 388, 18, 18, 50, 50, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 34:
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 382;
						base_mage_xp = 37;
						reduceAttack = true;
						spell_type = SHADOW_SPELL;
						int damage = getRandomMagicMaxHit(player, 200);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 26:
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 376;
						base_mage_xp = 39;
						blood_spell = true;
						spell_type = BLOOD_SPELL;
						int damage = getRandomMagicMaxHit(player, 210);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 22:
				player.setNextGraphics(new Graphics(362));
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						spell_type = ICE_SPELL;
						long currentTime = Utils.currentTimeMillis();
						if (target.getSize() >= 3 || target.getFreezeDelay() >= currentTime
								|| target.getFrozenBlockedDelay() >= currentTime) {
							mage_hit_gfx = 363;
						} else {
							mage_hit_gfx = 363;
							freeze_time = 10000;
						}
						base_mage_xp = 46;
						int damage = getRandomMagicMaxHit(player, 220);
						magic_sound = 169;
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						World.sendProjectile(player, target, 366, 43, 0, 120, 0, 50);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 29:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 387;
				base_mage_xp = 42;
				max_poison_hit = 40;
				spell_type = SMOKE_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 230)));
				World.sendProjectile(player, target, 386, 18, 18, 50, 50, 0);
				return 4;
			case 33:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 381;
				base_mage_xp = 43;
				reduceAttack = true;
				spell_type = SHADOW_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 240)));
				World.sendProjectile(player, target, 380, 18, 18, 50, 50, 0);
				return 4;
			case 25:
				player.setNextAnimation(new Animation(1978));
				mage_hit_gfx = 375;
				base_mage_xp = 45;
				blood_spell = true;
				spell_type = BLOOD_SPELL;
				delayMagicHit(getMageDelay(player, target), getMagicHit(player, getRandomMagicMaxHit(player, 250)));
				World.sendProjectile(player, target, 374, 18, 18, 50, 50, 0);
				return 4;

			case 21:
				player.setNextAnimation(new Animation(1978));
				playSound(171, player, target);
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						magic_sound = 169;
						spell_type = ICE_SPELL;
						long currentTime1 = Utils.currentTimeMillis();
						if (target.getSize() >= 3 || target.getFreezeDelay() >= currentTime1
								|| target.getFrozenBlockedDelay() >= currentTime1) {
							mage_hit_gfx = 367;
						} else {
							mage_hit_gfx = 367;
							freeze_time = 15000;
						}
						base_mage_xp = 46;
						int damage = getRandomMagicMaxHit(player, 260);
						Hit hit = getMagicHit(player, damage);
						delayMagicHit(getMageDelay(player, target), hit);
						World.sendProjectile(player, target, 368, 18, 18, 50, 50, 0);
						return nextTarget;

					}
				});
				return 4;

			case 31:
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 391;
						base_mage_xp = 48;
						max_poison_hit = 40;
						spell_type = SMOKE_SPELL;
						int damage = getRandomMagicMaxHit(player, 270);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						World.sendProjectile(player, target, 390, 18, 18, 50, 50, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 35:
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 383;
						base_mage_xp = 49;
						reduceAttack = true;
						spell_type = SHADOW_SPELL;
						int damage = getRandomMagicMaxHit(player, 280);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			case 27:
				player.setNextAnimation(new Animation(1979));
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						mage_hit_gfx = 377;
						base_mage_xp = 51;
						blood_spell = true;
						spell_type = BLOOD_SPELL;
						int damage = getRandomMagicMaxHit(player, 290);
						delayMagicHit(getMageDelay(player, target), getMagicHit(player, damage));
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;

			case 23:
				player.setNextAnimation(new Animation(1979));
				playSound(171, player, target);
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						magic_sound = 168;
						base_mage_xp = 52;
						spell_type = ICE_SPELL;
						long currentTime2 = Utils.currentTimeMillis();
						if (target.getSize() >= 3 || target.getFreezeDelay() >= currentTime2
								|| target.getFrozenBlockedDelay() >= currentTime2) {
							mage_hit_gfx = 1677;
						} else {
							mage_hit_gfx = 369;
							freeze_time = 20000;
						}
						int divide = (int) 1.5;
						int damage = getRandomMagicMaxHit(player, 300 / divide);
						Hit hit = getMagicHit(player, damage);
						delayMagicHit(getMageDelay(player, target), hit);
						World.sendProjectile(player, target, 368, 60, 32, 50, 50, 0);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return 4;
			}

		}
		return -1;
	}

	public interface MultiAttack {

		public boolean attack();

	}

	public int getMageDelay(Player player, Entity target) {
		return Utils.getDistance(player, target) == 0 ? 1
				: Utils.getDistance(player, target) > 3 ? 4 : Utils.getDistance(player, target);
	}

	public void attackTarget(Entity[] targets, MultiAttack perform) {
		Entity realTarget = target;
		for (Entity t : targets) {
			target = t;
			if (!perform.attack())
				break;
		}
		target = realTarget;
	}

	public int getRandomMagicMaxHit(Player player, int baseDamage) {
		int current = getMagicMaxHit(player, baseDamage);
		if (current <= 0)
			return -1;
		int hit = Utils.random(current + 1);
		if (hit > 0) {
			if (target instanceof NPC) {
				NPC n = (NPC) target;
				if (n.getId() == 14301 && (spell_type == FIRE_SPELL || spell_type == BLOOD_SPELL)) {
					int half = (int) (hit / 2);
					hit += half;
				} else if (n.getId() == 14301 && hasFireCape(player) && (spell_type != ICE_SPELL)) {
					hit += 20;
				} else if (n.getId() == 14301 && (spell_type == ICE_SPELL)) {
					hit = 0;
					player.sendMessage("Your spell seems to have no effect on the elementals icy armor.");
				}
				if (n.getId() == 9463 && (spell_type == FIRE_SPELL)) {
					if (hasFireCape(player))
						hit *= 2;
					else
						hit *= 1.5;
				}
			}
		}
		return hit/10;
	}

	public int getMagicMaxHit(Player player, int baseDamage) {
		double attack;
		double mageLevel = player.getSkills().getLevel(Skills.MAGIC);
		double A = 0;
		double AttackBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK];
		attack = mageLevel;
		attack *= player.getPrayer().getMageMultiplier();
		attack = Math.round(attack);
		attack += 8;
		if (fullVoidEquipped(player, 11663, 11674))
			attack *= 1.3;
		if (target instanceof NPC) {
			NPC n = (NPC) target;
		}
		attack = attack * (1 + AttackBonus / 64);
		A = Math.round(attack);
		double defence;
		double D = 0;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			double DefenceBonus = p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF];
			defence = (p2.getSkills().getLevel(Skills.MAGIC) * 0.7) + (p2.getSkills().getLevel(Skills.DEFENCE) * 0.3);
			defence = Math.round(defence);
			defence += 8;
			defence = defence * (1 + DefenceBonus / 64);
			D = Math.round(defence);
		} else {
			NPC n = (NPC) target;
			defence = (1 + (n.getBonuses() == null ? 0 : n.getBonuses()[CombatDefinitions.MAGIC_DEF]));
			D = Math.round(defence);
		}
		double prob = A / D;
		double random = Math.random();
		if (A <= D) {
			prob = (A - 1) / (D * 2);
		} else if (A > D) {
			prob = 1 - (D + 1) / (A * 2);
		}
		if (prob > 0.90)
			prob = 0.90;
		else if (prob < 0.05)
			prob = 0.05;
		if (prob < random) {
			return 0;
		}
		max_hit = baseDamage;
		double boost = 1
				+ ((player.getSkills().getLevel(Skills.MAGIC) - player.getSkills().getLevelForXp(Skills.MAGIC)) * 0.03);
		if (boost > 1)
			max_hit *= boost;
		if (target instanceof NPC) {
			NPC n = (NPC) target;
		}
		double magicPerc = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE];
		if (player.getCombatDefinitions().getSpellId() >= 66 && player.getCombatDefinitions().getSpellId() <= 68) {
			if ((player.getEquipment().getWeaponId() == 2415 && player.getEquipment().getCapeId() == 2412)
					|| (player.getEquipment().getWeaponId() == 2416 && player.getEquipment().getCapeId() == 2413)
					|| (player.getEquipment().getWeaponId() == 2417 && player.getEquipment().getCapeId() == 2414)) {
			}
		}
		if (spellcasterGloves > 0) {
			if (baseDamage > 60 || spellcasterGloves == 28 || spellcasterGloves == 25) {
				magicPerc += 17;
				if (target instanceof Player) {
					Player p = (Player) target;
					p.getSkills().drainLevel(0, p.getSkills().getLevel(0) / 10);
					p.getSkills().drainLevel(1, p.getSkills().getLevel(1) / 10);
					p.getSkills().drainLevel(2, p.getSkills().getLevel(2) / 10);
					p.getPackets().sendGameMessage("Your melee skills have been drained.");
					player.getPackets().sendGameMessage("Your spell weakened your enemy.");
				}
				player.getPackets().sendGameMessage("Your magic surged with extra power.");
			}
		}
		boost = magicPerc / 100 + 1;
		max_hit *= boost;
		return max_hit;
	}

	private int rangeAttack(final Player player) {
		final int weaponId = player.getEquipment().getWeaponId();
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int combatDelay = getRangeCombatDelay(player, weaponId, attackStyle);
		int soundId = getSoundId(weaponId, attackStyle);
		if (player.getCombatDefinitions().isUsingSpecialAttack()) {
			int specAmt = getSpecialAmount(weaponId);
			if (specAmt == 0) {
				player.getPackets().sendGameMessage(
						"This weapon has no special Attack, if you still see special bar please relogin.");
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return combatDelay;
			}
			if (player.getCombatDefinitions().hasRingOfVigour())
				specAmt *= 0.9;
			if (player.getInventory().containsItem(773, 1))
				specAmt *= 0.0;
			if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
				player.getPackets().sendGameMessage("You don't have enough power left.");
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return combatDelay;
			}
			player.getCombatDefinitions().desecreaseSpecialAttack(specAmt);
			switch (weaponId) {
			case 19149:// zamorak bow
			case 19151:
				player.setNextAnimation(new Animation(426));
				player.setNextGraphics(new Graphics(97));
				World.sendFastBowProjectile(player, target, 100);
				delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
			case 19146:
			case 19148:// guthix bow
				player.setNextAnimation(new Animation(426));
				player.setNextGraphics(new Graphics(95));
				World.sendFastBowProjectile(player, target, 98);
				delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
			case 19143:// saradomin bow
			case 19145:
				player.setNextAnimation(new Animation(426));
				player.setNextGraphics(new Graphics(96));
				// World.sendProjectile(player, target, 99, 41, 16, 25, 35, 16);
				World.sendFastBowProjectile(player, target, 99);
				delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
			case 10034:
			case 10033:
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is first player
												// on array

					@Override
					public boolean attack() {
						int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true,
								weaponId == 10034 ? 1.2 : 1.0, true);
						player.setNextAnimation(new Animation(2779));
						World.sendProjectile(player, target, weaponId == 10034 ? 909 : 908, 41, 16, 31, 35, 16);
						delayHit(1, weaponId, attackStyle, getRangeHit(player, damage));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								if (player == null || target == null || player.isDead() || player.hasFinished()
										|| target.isDead() || target.hasFinished())
									return;
								target.setNextGraphics(new Graphics(2739, 0, 96 << 16));
								dropAmmo(player, 1);
							}
						}, 2);
						if (!nextTarget) {
							if (damage == -1)
								return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				break;
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
				player.getPackets().sendSound(2545, 0);
				player.setNextAnimation(new Animation(1074));
				World.sendMSBProjectile(player, target, 249);
				World.sendMSBProjectile2(player, target, 249);
				delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				delayHit(Utils.getDistance(player, target) > 3 ? 3 : 1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true)));
				dropAmmo(player, 2);
				break;
			case 15241: // Hand cannon
				player.setNextAnimation(new Animation(12175));
				WorldTasksManager.schedule(new WorldTask() {
					int loop = 0;

					@Override
					public void run() {
						if ((target.isDead() || player.isDead() || loop > 6)) {
							stop();
							return;
						}
						if (loop == 2) {
							if (player.getEquipment().getWeaponId() == 15241) {
								player.setNextGraphics(new Graphics(2138));
								player.setNextAnimation(new Animation(12153));
								World.sendCannonProjectile(player, target, 2143);
								dropAmmo(player, 1);
								delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
										getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
								player.getActionManager().setActionDelay(1);
							} else {
								player.getCombatDefinitions().restoreSpecialAttack(50);
							}
						} else if (loop == 3) {
							stop();
						}
						loop++;
					}
				}, 0, (int) 0.25);
				combatDelay = 9;
				break;
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				int ammoId = player.getEquipment().getAmmoId();
				player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
				player.setNextGraphics(new Graphics(getStartArrowProjectileId(weaponId, ammoId), 0, 100));
				if (ItemDefinitions.getItemDefinitions(player.getEquipment().getAmmoId()).getName().toLowerCase()
						.contains("dragon arrow")) {
					int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.270, true);
					int damage2 = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.270, true);
					if (damage < 8) {
						damage = 8;
					} else
						damage += 8;
					if (damage2 < 8) {
						damage2 = 8;
					} else
						damage2 += 8;
					player.getPackets().sendSound(3731, 0);
					World.sendSlowBowProjectile(player, target, 1099);
					World.sendSlowBow2Projectile(player, target, 1099);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, damage));
					delayHit(Utils.getDistance(player, target) > 3 ? 3 : 2, weaponId, attackStyle,
							getRangeHit(player, damage2));
					checkSwiftGlovesEffect(player, attackStyle, weaponId, damage + damage + damage,
							getArrowProjectileId(weaponId, ammoId), 2);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							if (player == null || target == null || player.isDead() || player.hasFinished()
									|| target.isDead() || target.hasFinished())
								return;
							target.setNextGraphics(new Graphics(1100, 0, 100));
							target.setNextGraphics(new Graphics(1100, 0, 100));
						}
					}, 2);
				} else {
					int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.270, true);
					int damage2 = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.270, true);
					if (damage < 50) {
						damage = 50;
					} else
						damage += 50;
					if (damage2 < 50) {
						damage2 = 50;
					} else
						damage2 += 50;
					World.sendSlowBowProjectile(player, target, 1103);
					World.sendSlowBow2Projectile(player, target, 1103);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, damage));
					delayHit(Utils.getDistance(player, target) > 3 ? 3 : 2, weaponId, attackStyle,
							getRangeHit(player, damage2));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							if (player == null || target == null || player.isDead() || player.hasFinished()
									|| target.isDead() || target.hasFinished())
								return;
							target.setNextGraphics(new Graphics(1103, 0, 100));
							target.setNextGraphics(new Graphics(1103, 0, 100));
						}
					}, 2);
				}
				dropAmmo(player, 2);
				break;
			case 14684: // zanik cbow
				player.setNextAnimation(new Animation(11359));
				player.setNextGraphics(new Graphics(1714));
				WorldTasksManager.schedule(new WorldTask() {
					int loop = 0;

					@Override
					public void run() {
						if ((target.isDead() || player.isDead() || loop > 6 || !player.getWalkSteps().isEmpty())) {
							stop();
							return;
						}
						if (loop == 1) {
							ItemDefinitions defs = ItemDefinitions
									.getItemDefinitions(player.getEquipment().getWeaponId());
							World.sendCBOWProjectile(player, target, 2001);
							delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
									getRangeHit(player,
											getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true) + 30
													+ Utils.getRandom(120)));
							dropAmmo(player, 1);
							player.getActionManager().setActionDelay(defs.getAttackSpeed());
						} else if (loop == 3)
							stop();
						loop++;
					}
				}, 0, (int) 0.25);
				break;
			case 13954:// morrigan javelin
			case 12955:
			case 13956:
			case 13879:
			case 13880:
			case 13881:
			case 13882:
				player.setNextGraphics(new Graphics(1836));
				player.setNextAnimation(new Animation(10501));
				World.sendThrowProjectile(player, target, 1837);
				final int hit = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true);
				delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
						getRangeHit(player, hit));
				final Entity finalTarget = target;
				processMorriganJavelins(player, hit);
				dropAmmo(player, -1);
				break;

			case 13883:
			case 13957:// morigan thrown axe
				player.setNextGraphics(new Graphics(1838));
				player.setNextAnimation(new Animation(10504));
				World.sendThrowProjectile(player, target, 1839);
				// World.sendProjectile(player, target, 1839, 41, 41, 41, 35,
				// 0);
				delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
						getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.2, true)));
				dropAmmo(player, -1);
				break;
			default:
				Logger.log(this, "Unhandled Special Attack from " + weaponId);
				player.getPackets().sendGameMessage(
						"This weapon has no special Attack, if you still see special bar please relogin.");
				return combatDelay;
			}
		} else {
			if (weaponId != -1) {
				String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
				if (weaponName.toLowerCase().contains("throwing axe") || weaponName.toLowerCase().contains("knife")
						|| weaponName.toLowerCase().contains("dart") || weaponName.toLowerCase().contains("saga")
						|| weaponName.toLowerCase().contains("javelin") || weaponId == 6522
						|| weaponName.toLowerCase().contains("thrownaxe")) {
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					player.setNextGraphics(new Graphics(getStartThrowProjectileId(weaponId), 0, 100));
					World.sendThrowProjectile(player, target, getThrowProjectileId(weaponId));
					checkSwiftGlovesEffect(player, attackStyle, weaponId, hit, getThrowProjectileId(weaponId), 4);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, hit));
					dropAmmo(player, -1);
					player.getAppearence().generateAppearenceData();
				} else if (weaponName.contains("chinchompa") || weaponName.contains("Chinchompa")) {
					Item weapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
					player.getEquipment().getItem(Equipment.SLOT_WEAPON)
							.setAmount(player.getEquipment().getItem(Equipment.SLOT_WEAPON).getAmount() - 1);
					World.sendProjectile(player, target, weaponId == 10034 ? 909 : 908, 41, 16, 31, 35, 16, 0);
					attackTarget(getMultiAttackTargets(player), new MultiAttack() {

						private boolean nextTarget;

						@Override
						public boolean attack() {
							int damage = getRandomMaxHit(player, weaponId, attackStyle, true, true,
									weaponId == 10034 ? 1.2 : 1.0, true);
							player.setNextAnimation(new Animation(2779));
							player.getEquipment().refresh(Equipment.SLOT_WEAPON);

							delayHit(1, weaponId, attackStyle, getRangeHit(player, damage));
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									target.setNextGraphics(new Graphics(157, 0, 96 << 16));
								}
							}, 1);
							if (!nextTarget) {
								if (damage == -1)
									return false;
								nextTarget = true;
							}
							return nextTarget;
						}
					});
					if (player.getEquipment().getItem(Equipment.SLOT_WEAPON).getAmount() == 0) {
						player.sendMessage("You used up your last Chinchompa.");
						player.getEquipment().deleteItem(weaponId, weapon.getAmount());
						player.getAppearence().generateAppearenceData();
						return -1;
					}
					return combatDelay;
				} else if (weaponName.toLowerCase().contains("bow")/* || weaponName.toLowerCase().contains(" crossbow")*/) {
					int damage = 0;
					int ammoId = player.getEquipment().getAmmoId();
					if (ammoId != -1 && Utils.getRandom(8) == 0) {
						switch (ammoId) {
						case 883: //bronze arrow (p)
						case 885: //iron arrow (p)
						case 887: //steel arrow (p)
						case 889: //mithril arrow (p)
						case 891: //adamant arrow (p)
						case 893: //rune arrow (p)
							target.getPoison().makePoisoned(4);
							break;
						case 9241:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
							target.setNextGraphics(new Graphics(752));
							target.getPoison().makePoisoned(4);
							soundId = 2914;
							break;
						case 9237:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
							target.setNextGraphics(new Graphics(755));
							if (target instanceof Player) {
								Player p2 = (Player) target;
								p2.stopAll();
							} else {
								NPC n = (NPC) target;
								n.setTarget(null);
							}
							soundId = 2914;
							break;
						case 9242:
							max_hit = Short.MAX_VALUE;
							damage = (int) (target.getHitpoints() * 0.2);
							target.setNextGraphics(new Graphics(754));
							player.applyHit(new Hit(target,
									player.getHitpoints() > 20 ? (int) (player.getHitpoints() * 0.1) : 1,
									HitLook.MISSED));
							soundId = 2912;
							break;
						case 9243:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.15, true);
							target.setNextGraphics(new Graphics(758));
							soundId = 2913;
							if (target instanceof Player) {
								Player targetPlayer = ((Player) target);
								int amountLeft;
								if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.DEFENCE,
										damage / 20)) > 0) {
									if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.STRENGTH,
											amountLeft)) > 0) {
										if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.PRAYER,
												amountLeft)) > 0) {
											if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.ATTACK,
													amountLeft)) > 0) {
												if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.MAGIC,
														amountLeft)) > 0) {
													if (targetPlayer.getSkills().drainLevel(Skills.RANGE,
															amountLeft) > 0) {
														break;
													}
												}
											}
										}
									}
								}
							}
							break;
						case 9244:
							if (Combat.hasAntiDragProtection(target))
								break;
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.55, true);
							if (damage > 0) {
								target.setNextGraphics(new Graphics(756));
								soundId = 2915;
							}
							break;
						case 9245:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.15, true);
							target.setNextGraphics(new Graphics(753));
							player.heal((int) (damage * 0.25));
							soundId = 2917;
							break;
						case 24116:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true);
							if (attackStyle == 0) {
								target.setNextGraphics(new Graphics(3025));
								if (target.temporaryAttribute().get("bakriminel_immunity") != Boolean.TRUE) {
									if (target instanceof Player) {
										((Player) target).getPackets().sendGameMessage("You feel slowed down.");
									}
									target.temporaryAttribute().put("bakriminel_immunity", Boolean.TRUE);
									target.temporaryAttribute().put("bakriminel_effect", Boolean.TRUE);
									final Entity t = target;
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											t.temporaryAttribute().remove("bakriminel_effect");
											WorldTasksManager.schedule(new WorldTask() {
												@Override
												public void run() {
													t.temporaryAttribute().remove("bakriminel_immunity");
													stop();
												}
											}, 15);
											stop();
										}
									}, 30);
								}
							}

							if (attackStyle == 1) {
								target.setNextGraphics(new Graphics(3024));
								final Entity finalTarget = target;
								WorldTasksManager.schedule(new WorldTask() {
									int damage1 = 8;

									@Override
									public void run() {
										if (finalTarget.isDead() || finalTarget.hasFinished()) {
											stop();
											return;
										}
										if (damage1 > 0) {
											damage1 -= 1;
											finalTarget.applyHit(
													new Hit(player, Utils.random(15), HitLook.REGULAR_DAMAGE));
										} else {
											finalTarget.applyHit(new Hit(player, damage1, HitLook.REGULAR_DAMAGE));
											stop();
										}
									}
								}, 4, 2);
							}
							if (attackStyle == 2) {
								target.setNextGraphics(new Graphics(3025));
								int random = new Random().nextInt(100);
								if (random > 50) {
									if (target instanceof Player) {
										Player p2 = (Player) target;
										p2.setRunEnergy(p2.getRunEnergy() > 15 ? p2.getRunEnergy() - 15 : 0);
									}
								} else if (random < 50) {
									target.addFreezeDelay(5000, true);
								}
							}
							break;
						default:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						}
					} else {
						damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						checkSwiftGlovesEffect(player, attackStyle, weaponId, damage, getBoltGfxId(weaponId, ammoId),
								3);
					}
					player.getPackets().sendSound(2695, 0);
					World.sendCBOWProjectile(player, target, getBoltGfxId(weaponId, ammoId));
					delayHit(Utils.getDistance(player, target) > 4 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, damage));
					dropAmmo(player);
				} else if (weaponId == 18357) {
					int damage = 0;
					int ammoId = player.getEquipment().getAmmoId();
					if (ammoId != -1 && Utils.getRandom(7) == 0) {
						switch (ammoId) {
						case 9241:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
							target.setNextGraphics(new Graphics(752));
							target.getPoison().makePoisoned(4);
							soundId = 2914;
							break;
						case 9237:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
							target.setNextGraphics(new Graphics(755));
							if (target instanceof Player) {
								Player p2 = (Player) target;
								p2.stopAll();
							} else {
								NPC n = (NPC) target;
								n.setTarget(null);
							}
							soundId = 2914;
							break;
						case 9242:
							max_hit = Short.MAX_VALUE;
							damage = (int) (target.getHitpoints() * 0.2);
							target.setNextGraphics(new Graphics(754));
							player.applyHit(new Hit(target,
									player.getHitpoints() > 20 ? (int) (player.getHitpoints() * 0.1) : 1,
									HitLook.MISSED));
							soundId = 2912;
							break;
						case 9243:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.15, true);
							target.setNextGraphics(new Graphics(758));
							soundId = 2913;
							if (target instanceof Player) {
								Player targetPlayer = ((Player) target);
								int amountLeft;
								if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.DEFENCE,
										damage / 20)) > 0) {
									if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.STRENGTH,
											amountLeft)) > 0) {
										if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.PRAYER,
												amountLeft)) > 0) {
											if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.ATTACK,
													amountLeft)) > 0) {
												if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.MAGIC,
														amountLeft)) > 0) {
													if (targetPlayer.getSkills().drainLevel(Skills.RANGE,
															amountLeft) > 0) {
														break;
													}
												}
											}
										}
									}
								}
							}
							break;
						case 9244:
							if (Combat.hasAntiDragProtection(target))
								break;
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.55, true);
							if (damage > 0) {
								target.setNextGraphics(new Graphics(756));
								soundId = 2915;
							}
							break;
						case 9245:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.25, true);
							target.setNextGraphics(new Graphics(753));
							player.heal((int) (damage * 0.25));
							soundId = 2917;
							break;
						case 24116:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true, true, 1.0, true);
							if (attackStyle == 0) {
								target.setNextGraphics(new Graphics(3025));
								if (target.temporaryAttribute().get("bakriminel_immunity") != Boolean.TRUE) {
									if (target instanceof Player) {
										((Player) target).getPackets().sendGameMessage("You feel slowed down.");
									}
									target.temporaryAttribute().put("bakriminel_immunity", Boolean.TRUE);
									target.temporaryAttribute().put("bakriminel_effect", Boolean.TRUE);
									final Entity t = target;
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											t.temporaryAttribute().remove("bakriminel_effect");
											WorldTasksManager.schedule(new WorldTask() {
												@Override
												public void run() {
													t.temporaryAttribute().remove("bakriminel_immunity");
													stop();
												}
											}, 15);
											stop();
										}
									}, 30);
								}
							}
							if (attackStyle == 1) {
								target.setNextGraphics(new Graphics(3024));
								final Entity finalTarget = target;
								WorldTasksManager.schedule(new WorldTask() {
									int damage1 = 8;

									@Override
									public void run() {
										if (finalTarget.isDead() || finalTarget.hasFinished()) {
											stop();
											return;
										}
										if (damage1 > 0) {
											damage1 -= 1;
											finalTarget.applyHit(
													new Hit(player, Utils.random(15), HitLook.REGULAR_DAMAGE));
										} else {
											finalTarget.applyHit(new Hit(player, damage1, HitLook.REGULAR_DAMAGE));
											stop();
										}
									}
								}, 4, 2);
							}
							if (attackStyle == 2) {
								target.setNextGraphics(new Graphics(3025));
								int random = new Random().nextInt(100);
								if (random > 50) {
									if (target instanceof Player) {
										Player p2 = (Player) target;
										p2.setRunEnergy(p2.getRunEnergy() > 15 ? p2.getRunEnergy() - 15 : 0);
									}
								} else if (random < 50) {
									target.addFreezeDelay(5000, true);
								}
							}
							break;
						default:
							damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						}
					} else {
						damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						checkSwiftGlovesEffect(player, attackStyle, weaponId, damage, getBoltGfxId(weaponId, ammoId),
								3);
					}
					player.getPackets().sendSound(2695, 0);
					World.sendCBOWProjectile(player, target, getBoltGfxId(weaponId, ammoId));
					delayHit(Utils.getDistance(player, target) > 4 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, damage));
					if (weaponId != 4740)
						dropAmmo(player);
					else
						player.getEquipment().removeAmmo(ammoId, 1);
				} else if (weaponId == 15241) {// handcannon
					if (Utils.getRandom(player.getSkills().getLevel(Skills.FIREMAKING) << 1) == 0) {
						// explode
						player.setNextGraphics(new Graphics(2140));
						player.getEquipment().getItems().set(3, null);
						player.getEquipment().refresh(3);
						player.getAppearence().generateAppearenceData();
						player.applyHit(new Hit(player, Utils.getRandom(150) + 10, HitLook.REGULAR_DAMAGE));
						player.setNextAnimation(new Animation(12175));
						return combatDelay;
					} else {
						player.setNextGraphics(new Graphics(2138));
						World.sendCannonProjectile(player, target, 2143);
						delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
								getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
						dropAmmo(player, -2);
					}
				} else if (weaponName.toLowerCase().contains("crystal")) {
					player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
					player.setNextGraphics(new Graphics(250, 0, 100));
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					World.sendFastBowProjectile(player, target, 249);
					checkSwiftGlovesEffect(player, attackStyle, weaponId, hit, 249, 1);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, hit));
				} else if (weaponName.toLowerCase().contains("zaryte")) {
					player.setNextAnimation(new Animation(getWeaponAttackEmote(weaponId, attackStyle)));
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					World.sendFastBowProjectile(player, target, 1066);
					checkSwiftGlovesEffect(player, attackStyle, weaponId, hit, 1066, 1);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, hit));
				} else if (weaponId == 21364) {// sagaie
					player.getEquipment().removeAmmo(weaponId, -1);
					player.getAppearence().generateAppearenceData();
					player.setNextAnimation(new Animation(3236));
					World.sendFastBowProjectile(player, target, getThrowProjectileId(weaponId));
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
				} else if (weaponId == 11235 || weaponId == 15701 || weaponId == 15702 || weaponId == 15703
						|| weaponId == 15704) { // dbows
					final int ammoId = player.getEquipment().getAmmoId();
					player.setNextGraphics(new Graphics(getStartArrowProjectileId(weaponId, ammoId), 0, 100));
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					player.getPackets().sendSound(3731, 0);
					World.sendSlowBowProjectile(player, target, getArrowProjectileId(weaponId, ammoId));
					World.sendSlowBow2Projectile(player, target, getArrowProjectileId(weaponId, ammoId));
					checkSwiftGlovesEffect(player, attackStyle, weaponId, hit, getArrowProjectileId(weaponId, ammoId),
							2);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
					delayHit(Utils.getDistance(player, target) > 3 ? 3 : 2, weaponId, attackStyle,
							getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
					dropAmmo(player, 2);
				} else if (weaponId == 21365) { // Bolas
					dropAmmo(player, -3);
					player.setNextAnimation(new Animation(3128));
					World.sendFastBowProjectile(player, target, getThrowProjectileId(weaponId));
					int delay = 15000;
					if (target instanceof Player) {
						Player p = (Player) target;
						Item weapon = p.getEquipment().getItem(3);
						boolean slashBased = weapon != null;
						if (weapon != null) {
							int slash = p.getCombatDefinitions().getBonuses()[CombatDefinitions.SLASH_ATTACK];
							for (int i = 0; i < 5; i++) {
								if (p.getCombatDefinitions().getBonuses()[i] > slash) {
									slashBased = false;
									break;
								}
							}
						}
						if (p.getInventory().containsItem(946, 1) || slashBased) {
							delay /= 2;
						}
						if (p.getPrayer().usingPrayer(0, 18) || p.getPrayer().usingPrayer(1, 8)) {
							delay /= 2;
						}
						if (delay < 5000) {
							delay = 5000;
						}
					}
					long currentTime = Utils.currentTimeMillis();
					if (getRandomMaxHit(player, weaponId, attackStyle, true) > 0
							&& target.getFrozenBlockedDelay() < currentTime) {
						target.addFreezeDelay(delay, true);
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								if (player == null || target == null || player.isDead() || player.hasFinished()
										|| target.isDead() || target.hasFinished())
									return;
								target.setNextGraphics(new Graphics(469, 0, 96));
							}
						}, 2);
					}
					playSound(soundId, player, target);
					return combatDelay;
				} else { // bow/default
					final int ammoId = player.getEquipment().getAmmoId();
					player.getPackets().sendSound(2700, 0);
					player.setNextGraphics(new Graphics(getStartArrowProjectileId(weaponId, ammoId), 0, 100));
					int hit = getRandomMaxHit(player, weaponId, attackStyle, true);
					World.sendFastBowProjectile(player, target, getArrowProjectileId(weaponId, ammoId));
					checkSwiftGlovesEffect(player, attackStyle, weaponId, hit, getArrowProjectileId(weaponId, ammoId),
							1);
					delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
							getRangeHit(player, hit));
					if (weaponId != -1) {
						if (!weaponName.toLowerCase().contains("zaryte")
								&& !weaponName.toLowerCase().contains("crystal")
								&& !weaponName.toLowerCase().contains("sling")
								&& !weaponName.toLowerCase().contains("karil")) {
							dropAmmo(player);
						}
					}
				}

				player.setNextAnimationNoPriority(new Animation(getWeaponAttackEmote(weaponId, attackStyle)), target);
			}
		}
		playSound(soundId, player, target);
		return combatDelay;
	}

	/**
	 * Handles the swift gloves effect.
	 * 
	 * @param player
	 *            The player.
	 * @param hitDelay
	 *            The delay before hitting the target.
	 * @param attackStyle
	 *            The attack style used.
	 * @param weaponId
	 *            The weapon id.
	 * @param hit
	 *            The hit done.
	 * @param gfxId
	 *            The gfx id.
	 * @param startHeight
	 *            The start height of the original projectile.
	 * @param endHeight
	 *            The end height of the original projectile.
	 * @param speed
	 *            The speed of the original projectile.
	 * @param delay
	 *            The delay of the original projectile.
	 * @param curve
	 *            The curve of the original projectile.
	 * @param startDistanceOffset
	 *            The start distance offset of the original projectile.
	 */
	private void checkSwiftGlovesEffect(Player player, int attackStyle, int weaponId, int hit, int gfxId, int bow) {
		// int bow;
		// 1 = shortbow/fast projectile
		// 2 = dbow/slow projectile
		// 3 = crossbow/bolt projectile
		// 4 = dart/knife projectile
		Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
		if (gloves == null || !gloves.getDefinitions().getName().contains("Swift glove")) {
			return;
		}
		if (hit != 0 && hit < ((max_hit / 3) * 2) || new Random().nextInt(3) != 0) {
			return;
		}
		player.getPackets().sendGameMessage("You fired an extra shot.");
		if (bow == 1)
			World.sendFastBowSwiftProjectile(player, target, gfxId);
		if (bow == 2)
			World.sendSlowBowSwiftProjectile(player, target, gfxId);
		if (bow == 3)
			World.sendCBOWSwiftProjectile(player, target, gfxId);
		if (bow == 4)
			World.sendThrowSwiftProjectile(player, target, gfxId);
		delayHit(Utils.getDistance(player, target) > 3 ? 2 : 1, weaponId, attackStyle,
				getRangeHit(player, getRandomMaxHit(player, weaponId, attackStyle, true)));
		if (hit > (max_hit - 10)) {
			target.addFreezeDelay(10000, false);
			target.setNextGraphics(new Graphics(181, 0, 96));
		}

	}

	public void dropAmmo(Player player, int quantity) {
		if (quantity == -2) {
			final int ammoId = player.getEquipment().getAmmoId();
			player.getEquipment().removeAmmo(ammoId, 1);
		} else if (quantity == -1 || quantity == -3) {
			final int weaponId = player.getEquipment().getWeaponId();
			if (weaponId != -1) {
				if ((quantity == -3 && Utils.getRandom(10) < 2) || (quantity != -3 && Utils.getRandom(3) > 0)) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId != -1 && ItemDefinitions.getItemDefinitions(capeId).getName().contains("Ava's"))
						return; // nothing happens
				} else {
					player.getEquipment().removeAmmo(weaponId, quantity);
					return;
				}
				player.getEquipment().removeAmmo(weaponId, quantity);
				World.updateGroundItem(new Item(weaponId, quantity),
						new WorldTile(target.getCoordFaceX(target.getSize()), target.getCoordFaceY(target.getSize()),
								target.getPlane()),
						player);
			}
		} else {
			final int ammoId = player.getEquipment().getAmmoId();
			if (Utils.getRandom(3) > 0) {
				int capeId = player.getEquipment().getCapeId();
				if (capeId != -1 && ItemDefinitions.getItemDefinitions(capeId).getName().contains("Ava's"))
					return; // nothing happens
			} else {
				player.getEquipment().removeAmmo(ammoId, quantity);
				return;
			}
			if (ammoId != -1) {
				player.getEquipment().removeAmmo(ammoId, quantity);
				World.updateGroundItem(new Item(ammoId, quantity), new WorldTile(target.getCoordFaceX(target.getSize()),
						target.getCoordFaceY(target.getSize()), target.getPlane()), player);
			}
		}
	}

	public void dropAmmo(Player player) {
		dropAmmo(player, 1);
	}

	public int getBoltGfxId(int weaponId, int ammoId) {
		if (ammoId == 24116) {
			return 3023;
		}
		if (ammoId == 8882) {
			return 740;
		}
		return 27;
	}

	private int getAttackDistance(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if (name.contains("polypore"))
			return attackStyle != 1 ? 8 : 10;
		if (name.contains("dart"))
			return attackStyle != 2 ? 3 : 5;
		if (name.contains("knife") || name.contains("throwaxe") || name.contains("sling"))
			return attackStyle != 2 ? 4 : 6;
		if (name.contains("javelin"))
			return attackStyle != 2 ? 5 : 7;
		if (name.contains("dorgeshuun"))
			return attackStyle != 2 ? 6 : 8;
		if (name.contains("longbow") || name.contains("dark") || name.contains("chinchompa"))
			return attackStyle != 2 ? 9 : 10;
		if (name.contains("zaryte") || name.contains("crystal"))
			return 10;
		return attackStyle != 2 ? 7 : 9;
	}

	public int getStartArrowProjectileId(int weaponId, int arrowId) {
		String name = ItemDefinitions.getItemDefinitions(arrowId).getName().toLowerCase();
		if (name.contains("bronze arrow")) {
			return 19;
		}
		if (name.contains("iron arrow")) {
			return 18;
		}
		if (name.contains("steel arrow")) {
			return 20;
		}
		if (name.contains("mithril arrow")) {
			return 21;
		}
		if (name.contains("adamant arrow")) {
			return 22;
		}
		if (name.contains("rune arrow")) {
			return 24;
		}
		if (arrowId == 19152) {
			return 96;
		}
		if (arrowId == 19157) {
			return 95;
		}
		if (arrowId == 19162) {
			return 97;
		}
		if (name.contains("dragon arrow")) {
			return 1116;
		}
		return -1; // bronze default
	}

	public int getArrowProjectileId(int weaponId, int arrowId) {
		String name = ItemDefinitions.getItemDefinitions(arrowId).getName().toLowerCase();
		if (name.contains("bronze arrow")) {
			return 10;
		}
		if (name.contains("iron arrow")) {
			return 11;
		}
		if (name.contains("steel arrow")) {
			return 12;
		}
		if (name.contains("mithril arrow")) {
			return 13;
		}
		if (name.contains("adamant arrow")) {
			return 14;
		}
		if (name.contains("rune arrow")) {
			return 15;
		}
		if (arrowId == 19152) {
			return 99;
		}
		if (arrowId == 19157) {
			return 98;
		}
		if (arrowId == 19162) {
			return 100;
		}
		if (name.contains("dragon arrow")) {
			return 1120;
		}
		return -1;// bronze default
	}

	public static int getStartThrowProjectileId(int weaponId) {
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if (name.contains("bronze dart")) {
			return 232;
		} else if (name.contains("iron dart")) {
			return 233;
		} else if (name.contains("steel dart")) {
			return 234;
		} else if (name.contains("black dart")) {
			return 235;
		} else if (name.contains("mithril dart")) {
			return 235;
		} else if (name.contains("adamant dart")) {
			return 236;
		} else if (name.contains("rune dart")) {
			return 237;
		} else if (name.contains("dragon dart")) {
			return 1123;
		}
		return -1;
	}

	public static int getThrowProjectileId(int weaponId) {
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		if (name.contains("bronze knife")) {
			return 212;
		} else if (name.contains("iron knife")) {
			return 213;
		} else if (name.contains("steel knife")) {
			return 214;
		} else if (name.contains("black knife")) {
			return 215;
		} else if (name.contains("mithril knife")) {
			return 216;
		} else if (name.contains("adamant knife")) {
			return 217;
		} else if (name.contains("rune knife")) {
			return 218;
			// darts
		} else if (name.contains("bronze dart")) {
			return 226;
		} else if (name.contains("iron dart")) {
			return 227;
		} else if (name.contains("steel dart")) {
			return 228;
		} else if (name.contains("black dart")) {
			return 229;
		} else if (name.contains("mithril dart")) {
			return 229;
		} else if (name.contains("adamant dart")) {
			return 230;
		} else if (name.contains("rune dart")) {
			return 231;
		} else if (name.contains("dragon dart")) {
			return 1122;
		} else if (name.contains("sagaie")) {
			return 466;
		} else if (name.contains("bolas")) {
			return 468;
		} else if (name.contains("morrigan's javelin")) {
			return 1837;
		} else if (name.contains("morrigan's throwing")) {
			return 1839;
		} else if (name.contains("toktz-xil-ul")) {
			return 442;
		}
		return -1;
	}

	@SuppressWarnings("unused")
	private int getRangeHitDelay(Player player) {
		return Utils.getDistance(player.getX(), player.getY(), target.getX(), target.getY()) >= 1 ? 1 : 1;
	}

	private int meleeAttack(final Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int combatDelay = getMeleeCombatDelay(player, weaponId);
		int soundId = getSoundId(weaponId, attackStyle);
		if (weaponId == -1) {
			Item gloves = player.getEquipment().getItem(Equipment.SLOT_HANDS);
			if (gloves != null && gloves.getDefinitions().getName().contains("Goliath gloves")) {
				weaponId = -2;
			}
		}
		if (player.getCombatDefinitions().isUsingSpecialAttack()) {
			if (!specialExecute(player))
				return combatDelay;
			switch (weaponId) {
			case 15442:// whip start
			case 15443:
			case 15444:
			case 15441:
			case 4151:
			case 23691:
				player.setNextAnimation(new Animation(11971));
				player.getPackets().sendSound(2713, 0);
				target.setNextGraphics(new Graphics(2108, 0, 100));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					p2.setRunEnergy(p2.getRunEnergy() > 25 ? p2.getRunEnergy() - 25 : 0);
				}
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true)));
				break;
			case 21371: // Abby Vine Whip
				player.getPackets().sendSound(2713, 0);
				processVineCall(player);
				break;

			case 11730: // sara sword
			case 15290: //sara blessed sword
				player.setNextAnimation(new Animation(11993));
				player.getPackets().sendSound(3853, 1);
				target.setNextGraphics(new Graphics(1194));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)),
						getMagicHit(player, 5 + Utils.getRandom(10)));
				soundId = 3853;
				break;
			case 1249:// d spear
			case 1263:
			case 3176:
			case 5716:
			case 5730:
			case 13770:
			case 13772:
			case 13774:
			case 13776:
			case 11716:
				player.setNextAnimation(new Animation(12017));
				player.stopAll();
				target.setNextGraphics(new Graphics(80, 5, 60));
				if (target instanceof Player) {
					final Player other = (Player) target;
					other.lock(5);
					other.stopAll();
					other.addFoodDelay(3000);
					other.setAttackedBy(player);
					other.setAttackedByDelay(10000);
					if (!target.addWalkSteps(target.getX() - player.getX() + target.getX(),
							target.getY() - player.getY() + target.getY(), 1))
						player.setNextFaceEntity(target);
					target.setDirection(player.getDirection());
				} else {
					NPC n = (NPC) target;
					n.setFreezeDelay(3000);
					n.resetCombat();
				}
				break;
			case 11698: // sgs
			case 24514:
			case 23681:
				player.setNextAnimation(new Animation(12019));
				player.setNextGraphics(new Graphics(2109));
				int sgsdamage = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true);
				player.heal(sgsdamage / 2);
				player.getPrayer().restorePrayer((sgsdamage / 4) * 10);
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, sgsdamage));
				break;
			case 11696: // bgs
			case 24512:
			case 23680:
				player.setNextAnimation(new Animation(11991));
				player.setNextGraphics(new Graphics(2114));
				player.getPackets().sendSound(3834, 0);
				int damage = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.15, true);
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, damage));
				if (target instanceof Player) {
					Player targetPlayer = ((Player) target);
					int amountLeft;
					if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.DEFENCE, damage / 10)) > 0) {
						if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.STRENGTH, amountLeft)) > 0) {
							if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.PRAYER, amountLeft)) > 0) {
								if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.ATTACK, amountLeft)) > 0) {
									if ((amountLeft = targetPlayer.getSkills().drainLevel(Skills.MAGIC,
											amountLeft)) > 0) {
										if (targetPlayer.getSkills().drainLevel(Skills.RANGE, amountLeft) > 0) {
											break;
										}
									}
								}
							}
						}
					}
				}
				break;
			case 11694: // ags
			case 24510:
			case 23679:
				player.setNextAnimationNoPriority(new Animation(11989), player);
				player.setNextGraphics(new Graphics(2113));
				player.getPackets().sendSound(3865, 0);
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.375, true)));
				break;

			case 13899: // vls
			case 13901:
				player.setNextAnimation(new Animation(10502));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.25, true)));
				break;
			case 13902: // statius hammer
			case 13904:
				player.setNextAnimation(new Animation(10505));
				player.setNextGraphics(new Graphics(1840));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.25, true)));
				break;
			case 13905: // vesta spear
			case 13907:
				player.setNextAnimation(new Animation(10499));
				player.setNextGraphics(new Graphics(1835));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.25, true)));
				break;

			case 19780:
			case 19784: // korasi sword
				player.setNextAnimation(new Animation(14788));
				player.setNextGraphics(new Graphics(1729));
				final double multiplier = 0.5 + Math.random();
				attackTarget(getMultiAttackTargets(player), new MultiAttack() {

					private boolean nextTarget; // real target is firsts
					final int weaponId = player.getEquipment().getWeaponId();

					// player on array

					@Override
					public boolean attack() {
						final Entity[] targets = getMultiAttackTargets(player);
						//int korasiDamage = getMaxHit(player, weaponId, attackStyle, false, true, 1);
						//korasiDamage *= multiplier;
						//max_hit = (int) (korasiDamage * 1.5);
						//delayHit(0, weaponId, attackStyle, getMagicHit(player, korasiDamage / targets.length + 1));

						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								for (Entity e : targets)
									e.setNextGraphics(new Graphics(2795, 0, 100));
								target.setNextGraphics(new Graphics(2795, 0, 100));
							}
						});
						if (!nextTarget) {
							//if (korasiDamage == -1)
							//	return false;
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return combatDelay;
			/*
			 * case 19784: // korasi sword player.setNextAnimation(new
			 * Animation(14788)); player.setNextGraphics(new Graphics(1729));
			 * int korasiDamage = getMaxHit(player, weaponId, attackStyle,
			 * false, true, 1); double multiplier = 0.5 + Math.random(); max_hit
			 * = (int) (korasiDamage * 1.5); korasiDamage *= multiplier; Hit h =
			 * getMagicHit(player, korasiDamage); //h.setCriticalMark();
			 * delayNormalHit(weaponId, attackStyle, h); break;
			 */
			case 11700:
			case 24516://ZGS
				int zgsdamage = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true);
				player.setNextAnimation(new Animation(7070));
				player.setNextGraphics(new Graphics(1221));
				player.getPackets().sendSound(3834, 0);
				if (zgsdamage != 0 && target.getSize() == 1) {
					target.setNextGraphics(new Graphics(2104));
					target.addFreezeDelay(18000);
				}
				delayNormalHit(weaponId, attackStyle, getMeleeHit(player, zgsdamage));
				break;
			case 14484: // d claws
			case 23695:
				player.setNextAnimationNoPriority(new Animation(10961), player);
				player.setNextGraphics(new Graphics(1950));
				int[] hits = new int[] { 0, 1 };
				int hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
				if (hit > 10) {
					hits = new int[] { hit, hit / 2, (hit / 2) / 2, (hit / 2) - ((hit / 2) / 2) };
				} else {
					hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
					if (hit > 0) {
						hits = new int[] { 0, hit, hit / 2, hit - (hit / 2) };
					} else {
						hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
						if (hit > 0) {
							hits = new int[] { 0, 0, hit / 2, (hit / 2) + 10 };
						} else {
							hit = getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true);
							if (hit > 0) {
								hits = new int[] { 0, 0, 0, (int) (hit * 1.0) };
							} else {
								int[] miss = { Utils.random(10), Utils.random(10) };
								for (int i = 0; i < miss.length; i++) {
									delayHit(0, weaponId, attackStyle, getMeleeHit(player, miss[i]));
								}
							}
						}
					}
				}
				for (int i = 0; i < hits.length; i++) {
					if (i > 1) {
						delayHit(1, weaponId, attackStyle, getMeleeHit(player, hits[i]));
					} else {
						delayNormalHit(weaponId, attackStyle, getMeleeHit(player, hits[i]));
					}
				}
				break;
			case 10887: // anchor
				player.setNextAnimation(new Animation(5870));
				player.setNextGraphics(new Graphics(1027));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true)));
				break;
			case 1305: // dragon long
				player.setNextAnimation(new Animation(12033));
				player.setNextGraphics(new Graphics(2117));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.25, true)));
				break;
			case 3204: // d hally
				player.setNextAnimation(new Animation(1203));
				player.setNextGraphics(new Graphics(282, 0, 96));
				player.getPackets().sendSound(2529, 0);
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				if (target.getSize() > 1)
					delayHit(0, weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				break;
			case 4587: // dragon sci
				player.setNextAnimation(new Animation(12031));
				player.setNextGraphics(new Graphics(2118));
				Hit hit1 = getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					if (hit1.getDamage() > 0)
						p2.setPrayerDelay(5000);// 5 seconds
				}
				delayNormalHit(weaponId, attackStyle, hit1);
				soundId = 2540;
				break;
			case 1215: // dragon dagger
			case 1231: //dragon dagger (p)
			case 5680: //dragon dagger (p+)
			case 5698: // dds
				player.setNextAnimationNoPriority(new Animation(1062), player);
				player.setNextGraphics(new Graphics(252, 0, 100));
				if (target instanceof Player) {
					delayNormalHit(weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.15, true)),

					getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.15, true)));
				} else {
					delayNormalHit(weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
					delayHit(1, weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				}
				soundId = 2537;
				break;
			case 1434: // dragon mace
				player.setNextAnimation(new Animation(1060));
				player.setNextGraphics(new Graphics(251, 0, 100));
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.45, true)));
				soundId = 2541;
				break;
			case 4153:
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.1, true)));
				player.setNextAnimation(new Animation(1667));
				player.setNextGraphics(new Graphics(340, 0, 96 << 16));
				break;
			case 14679:
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true)));
				player.setNextAnimation(new Animation(10505));
				player.setNextGraphics(new Graphics(1840, 0, 96 << 16));
				break;
			default:
				Logger.log(this, "Unhandled Special Attack from " + weaponId);
				player.getPackets().sendGameMessage(
						"This weapon has no special Attack, if you still see special bar please relogin.");
				return combatDelay;
			}
		} else {
			if (weaponId == -2 && new Random().nextInt(15) == 0) {
				player.setNextAnimation(new Animation(14417));
				final int attack = attackStyle;
				attackTarget(getMultiAttackTargets(player, 5, Integer.MAX_VALUE), new MultiAttack() {

					private boolean nextTarget;

					@Override
					public boolean attack() {
						target.addFreezeDelay(10000, true);
						target.setNextGraphics(new Graphics(181, 0, 96));
						final Entity t = target;
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								final int damage = getRandomMaxHit(player, -2, attack, false, true, 1.0, false);
								t.setNextGraphics(new Graphics(181, 0, 96));
								t.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));

								stop();
							}
						}, 1);
						if (target instanceof Player) {
							Player p = (Player) target;
							for (int i = 0; i < 7; i++) {
								if (i != 3 && i != 5) {
									p.getSkills().drainLevel(i, 7);
								}
							}
							p.getPackets().sendGameMessage("Your stats have been drained!");
						}
						if (!nextTarget) {
							nextTarget = true;
						}
						return nextTarget;

					}
				});
				return combatDelay;
			}
			MeleeHitDelay(player);
			player.setNextAnimationNoPriority(new Animation(getWeaponAttackEmote(weaponId, attackStyle)), player);
		}
		playSound(soundId, player, target);
		return combatDelay;
	}

	public void MeleeHitDelay(final Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		delayNormalHit(weaponId, attackStyle,
				getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false)));
	}

	public void playSound(int soundId, Player player, Entity target) {
		if (soundId == -1)
			return;
		player.getPackets().sendSound(soundId, 0);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			p2.getPackets().sendSound(soundId, 0);
		}
	}

	public static int getSpecialAmount(int weaponId) {
		switch (weaponId) {
		case 4587: // dragon sci
		case 859: // magic longbow
		case 861: // magic shortbow
		case 10284: // Magic composite bow
		case 18332: // Magic longbow (sighted)
			return 55;
		case 11235: // dark bows
		case 15701:
		case 15702:
		case 15703:
		case 15704:
			return 65;
		case 13899: // vls
		case 13901:
		case 1305: // dragon long
		case 1215: // dragon dagger
		case 1231: //dds (p)
		case 5680: //dds(p+)
		case 5698: // dds
		case 1434: // dragon mace
		case 1249:// d spear
		case 1263:
		case 11716:
		case 3176:
		case 5716:
		case 5730:
		case 13770:
		case 13772:
		case 13774:
		case 13776:
			return 25;
		case 3204: // d hally
			return 30;
		case 13902:
		case 13904:
			return 35;
		case 15442:// whip start
		case 15443:
		case 15444:
		case 15441:
		case 21371:
		case 4151:
		case 23691:
		case 11698: // sgs
		case 24514:
		case 23681:
		case 11694: // ags
		case 24510:
		case 23679:
		case 13905: // vesta spear
		case 13907:
		case 14484: // d claws
		case 23695:
		case 10887: // anchor
		case 4153: // granite maul
		case 14679:
		case 14684: // zanik cbow
		case 15241: // hand cannon
		case 13908:
		case 13954:// morrigan javelin
		case 13955:
		case 13956:
		case 13879:
		case 13880:
		case 13881:
		case 13882:
		case 13883:// morigan thrown axe
		case 13957:
			return 50;
		case 15290: // ss blessed sword
			return 65;
		case 11730: // ss
		case 23690:
		case 11696: // bgs
		case 24512:
		case 23680:
		case 23682:
		case 35:// Excalibur
		case 8280:
		case 14632:
		case 1377:// dragon battle axe
		case 13472:
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		case 15486:// staff of lights
		case 11736:
			return 100;
		case 19784: // korasi sword
		case 11700:
		case 24516:
			return 60;
		default:
			return 0;
		}
	}

	public int getRandomMaxHit(Player player, int weaponId, int attackStyle, boolean ranging) {
		return getRandomMaxHit(player, weaponId, attackStyle, ranging, true, 1.0D, false);
	}

	private int getStyleAttackBonus(Player player) {
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		if (attackStyle == CombatDefinitions.SHARED)
			return 1;
		if (attackStyle == Skills.ATTACK)
			return 3;
		return 0;
	}

	private int getStyleDefenceBonus(Player player) {
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		if (attackStyle == CombatDefinitions.SHARED)
			return 1;
		if (attackStyle == Skills.DEFENCE)
			return 3;
		return 0;
	}

	private int getStyleStrengthBonus(Player player) {
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		if (attackStyle == CombatDefinitions.SHARED)
			return 1;
		if (attackStyle == Skills.STRENGTH)
			return 3;
		return 0;
	}

	private boolean hasSpecialWeakness(Player player, int id, int attackStyle) {
		int weaponId = player.getEquipment().getWeaponId();
		boolean hasSpear = player.getEquipment().getWeaponId() == 11716 || player.getEquipment().getWeaponId() == 1249;
		switch (id) {
		case 8133:// corp
			if (hasSpear && attackStyle == CombatDefinitions.STAB_ATTACK)
				return true;
			break;
		}
		return false;
	}

	public int hitchance(Player player, int weaponId, int attackStyle, boolean ranging, boolean defenceAffects,
			double specMultiplier, boolean usingSpec) {
		double accuracy = 0;
		int attackBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.getMeleeBonusStyle(weaponId,
				attackStyle)];
		int attackLevel = player.getSkills().getLevel(Skills.ATTACK);
		accuracy = attackLevel + (2.5 * attackBonus);
		double blockchance = 0;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			int defenceBonus = p2.getCombatDefinitions().getBonuses()[CombatDefinitions
					.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))];
			int defenceLevel = p2.getSkills().getLevel(Skills.DEFENCE);
			blockchance = defenceBonus + defenceLevel;
		}
		double prob = accuracy / blockchance;
		double random = Math.random();
		if (prob > 0.90)
			prob = 0.90;
		else if (prob < 0.05)
			prob = 0.05;
		else if (prob < random)
			return 0;
		int hit = Utils.random(max_hit + 1);
		return hit/10;

	}

	public int getRandomMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean defenceAffects,
			double specMultiplier, boolean usingSpec) {
		max_hit = getMaxHit(player, weaponId, attackStyle, ranging, usingSpec, specMultiplier);
		if (defenceAffects) {
			double dhBoost = 1;
			/*
			 * if (fullDharokEquipped(player)) { double hp =
			 * player.getHitpoints(); double maxhp = player.getMaxHitpoints();
			 * double d = hp / maxhp; dhBoost = 2 - d; }
			 */
			double attack = 0;
			double A = 0;
			double attackBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions
					.getMeleeBonusStyle(weaponId, attackStyle)];
			double range = 0;
			double R = 0;
			double rangeBonus = player.getCombatDefinitions().getBonuses()[4];
			if (!ranging) {
				attack += player.getSkills().getLevel(0);
				attack *= player.getPrayer().getAttackMultiplier();
				attack = Math.round(attack);
				attack += 8;
				if (fullVoidEquipped(player, (new int[] { 11665, 11676 })))
					attack *= 1.3;
				if (usingSpec) {
					double multiplier = getSpecialAccuracyModifier(player);
					attack *= multiplier;
				}
				if (target instanceof NPC) {
					NPC n = (NPC) target;
					for (String string : Settings.UNDEAD_NPCS) {
						if (n.getDefinitions().getName().toLowerCase().contains(string)) {
							if (SalveAmmyE(player))
								attack *= 1.20;
							else if (SalveAmmy(player))
								attack *= 1.15;
						}
					}
				}
				if (fullBerserkerMaul(player) || fullBerserkerMace(player) || fullBerserkerSword(player)
						|| fullBerserkerDagger(player))
					attack *= 1.20;
				attack = attack * (1 + attackBonus / 64);
				A = Math.round(attack);
			} else {
				range = player.getSkills().getLevel(Skills.RANGE);
				range *= player.getPrayer().getRangeMultiplier();
				range = Math.round(range);
				range += 8;
				if (fullVoidEquipped(player, (new int[] { 11664, 11675 })))
					range *= 1.3;
				if (usingSpec) {
					double multiplier = getSpecialAccuracyModifier(player);
					range *= multiplier;
				}
				if (target instanceof NPC) {
					NPC n = (NPC) target;
				}
				range = range * (1 + rangeBonus / 64);
				R = Math.round(range);
			}
			double defence = 0;
			double D = 0;
			double rangedefence = 0;
			double RD = 0;
			if (target instanceof Player) {
				Player p2 = (Player) target;
				if (!ranging) {
					double defenceBonus = p2.getCombatDefinitions().getBonuses()[CombatDefinitions
							.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))];
					defence = p2.getSkills().getLevel(Skills.DEFENCE);
					defence *= p2.getPrayer().getDefenceMultiplier();
					defence = Math.round(defence);
					defence += 8;
					defence = defence * (1 + defenceBonus / 64);
					D = Math.round(defence);
				} else {
					double rangeDefenceBonus = p2.getCombatDefinitions().getBonuses()[9];
					rangedefence = p2.getSkills().getLevel(Skills.DEFENCE);
					rangedefence *= p2.getPrayer().getDefenceMultiplier();
					rangedefence = Math.round(rangedefence);
					rangedefence += 8;
					rangedefence = rangedefence * (1 + rangeDefenceBonus / 64);
					RD = Math.round(rangedefence);
				}
			} else {
				if (ranging) {
					NPC n = (NPC) target;
					rangedefence = (1 + (n.getBonuses() != null ? n.getBonuses()[9] : 0));
					RD = Math.round(rangedefence);
				} else {
					NPC n = (NPC) target;
					defence = (1 + (n.getBonuses() != null
							? n.getBonuses()[CombatDefinitions
									.getMeleeDefenceBonus(CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle))]
							: 0));
					if (hasSpecialWeakness(player, n.getId(),
							CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle)))
						defence *= 0.3;
					D = Math.round(defence);
				}
			}
			if (ranging) {
				double prob = R / RD;
				double random = Math.random();
				if (R <= RD) {
					prob = (R - 1) / (RD * 2);
				} else if (R > RD) {
					prob = 1 - (RD + 1) / (R * 2);
				}
				/*
				 * if (player.getUsername().equalsIgnoreCase("andreas"))
				 * player.getPackets().sendGameMessage( "Attack: " + new
				 * DecimalFormat((R < 1.0 ? "#.##" :
				 * ".##")).format(R).replace(',', '.') + ", Defence: " + new
				 * DecimalFormat((RD < 1.0 ? "#.##" :
				 * ".##")).format(RD).replace(',', '.') + ", Hit chance: " + new
				 * DecimalFormat((prob < 1.0 ? "#.##" :
				 * ".##")).format(prob).replace(',', '.') + "%");
				 */
				if (prob > 0.90)
					prob = 0.90;
				else if (prob < 0.05)
					prob = 0.05;
				if (prob < random) {
					return 0;
				}
			} else {
				double prob = A / D;
				double random = Math.random();
				if (A <= D) {
					prob = (A - 1) / (D * 2);
				} else if (A > D) {
					prob = 1 - (D + 1) / (A * 2);
				}
				/*
				 * if (player.getUsername().equalsIgnoreCase("andreas"))
				 * player.getPackets().sendGameMessage( "Attack: " + new
				 * DecimalFormat((A < 1.0 ? "#.##" :
				 * ".##")).format(A).replace(',', '.') + ", Defence: " + new
				 * DecimalFormat((D < 1.0 ? "#.##" :
				 * ".##")).format(D).replace(',', '.') + ", Hit chance: " + new
				 * DecimalFormat((prob < 1.0 ? "#.##" :
				 * ".##")).format(prob).replace(',', '.') + "%");
				 */
				if (prob > 0.90)
					prob = 0.90;
				else if (prob < 0.05)
					prob = 0.05;
				if (prob < random) {
					return 0;
				}
			}

		}
		int hit = Utils.random(max_hit + 1);
		if (player.getInventory().containsItem(773, 1) && player.getRights() > 1) {
			int MaxHit = (int) (max_hit);
			hit -= MaxHit;
			max_hit -= MaxHit;
			if (hit < 0)
				hit = MaxHit;
			if (hit < MaxHit)
				hit += MaxHit;
		}
		return hit/10;
	}

	private final int getMaxHit1(Player player, int weaponId,
			int attackStyle, boolean ranging, boolean usingSpec,
			double specMultiplier) {
		if (!ranging) {

			/*//whip hiting 450 no pot no pray? lmao nty
			int strLvl = player.getSkills().getLevel(Skills.STRENGTH);
			int strBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS];
			double strMult = player.getPrayer().getStrengthMultiplier();
			double xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
			double style = xpStyle == Skills.STRENGTH ? 3 : xpStyle == CombatDefinitions.SHARED ? 1 : 0;
			int dhp = 0;
			double dharokMod = 1.0;
			double hitMultiplier = 1.0;
			if (fullDharokEquipped(player)) {
				dhp = player.getMaxHitpoints() - player.getHitpoints();
				dharokMod = (dhp * 0.001) + 1;
			}
			if (fullVoidEquipped(player, 11665, 11676)) {
				hitMultiplier *= 1.1;
			}
			hitMultiplier *= specMultiplier;
			double cumulativeStr = (strLvl * strMult + style) * dharokMod;
			return (int) ((14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * hitMultiplier);*/

			double strengthLvl = player.getSkills().getLevel(Skills.STRENGTH);
			int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
			double styleBonus = xpStyle == Skills.STRENGTH ? 3 : xpStyle == CombatDefinitions.SHARED ? 1 : 0;
			double otherBonus = 1;
			if (fullDharokEquipped(player)) {
				double hp = player.getHitpoints();
				double maxhp = player.getMaxHitpoints();
				double d = hp / maxhp;
				otherBonus = 2 - d;
			}
			double effectiveStrength = 8 + Math.floor((strengthLvl * player.getPrayer().getStrengthMultiplier()) + styleBonus);
			if (fullVoidEquipped(player, 11665, 11676)) 
				effectiveStrength = Math.floor(effectiveStrength*1.1);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS];
			if (weaponId == -2) {
				strengthBonus += 82;
			}
			double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
			return (int) Math.floor(baseDamage * specMultiplier * otherBonus);
		} else {
			if(weaponId == 24338 && target instanceof Player) {
				player.getPackets().sendGameMessage("The royal crossbow feels weak and unresponsive against other players.");
				return 60;
			}
			double rangedLvl = player.getSkills().getLevel(Skills.RANGE);
			double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
			double effectiveStrenght = Math.floor(rangedLvl	* player.getPrayer().getRangeMultiplier()) + styleBonus;

			if (fullVoidEquipped(player, 11664, 11675))
				effectiveStrenght += Math.floor((player.getSkills().getLevelForXp(
						Skills.RANGE) / 5) + 1.6);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS];
			double baseDamage = 5 + (((effectiveStrenght + 8) * (strengthBonus + 64)) / 64);
			return (int) Math.floor(baseDamage * specMultiplier);
		}
	}
	
	private final int getMaxHit(Player player, int weaponId, int attackStyle, boolean ranging, boolean usingSpec,
			double specMultiplier) {
		if (!ranging) {
			double strengthLvl = player.getSkills().getLevel(Skills.STRENGTH);
			double otherBonus = 1;
			if (fullDharokEquipped(player)) {
				double hp = player.getHitpoints();
				double maxhp = player.getMaxHitpoints();
				double d = hp / maxhp;
				otherBonus = 2 - d;
			}
			double effectiveStrength = 8 + Math
					.floor((strengthLvl * player.getPrayer().getStrengthMultiplier()) + getStyleStrengthBonus(player));
			if (fullVoidEquipped(player, 11665, 11676))
				effectiveStrength = Math.floor(effectiveStrength * 1.1);
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS];
			if (usingGoliathGloves(player)) {
				strengthBonus += 82;
			}
			if (target instanceof NPC) {
				NPC n = (NPC) target;
				for (String string : Settings.UNDEAD_NPCS) {
					if (n.getDefinitions().getName().toLowerCase().contains(string)) {
						if (SalveAmmyE(player)) {
							effectiveStrength = Math.floor(effectiveStrength * 1.20);
						}
					}
				}
			}
			if (target instanceof NPC) {
				NPC n = (NPC) target;
			}
			if (target instanceof NPC) {
				NPC n = (NPC) target;
				for (String string : Settings.UNDEAD_NPCS) {
					if (n.getDefinitions().getName().toLowerCase().contains(string)) {
						if (SalveAmmy(player)) {
							effectiveStrength = Math.floor(effectiveStrength * 1.15);
						}
					}
				}
			}
			if (fullBerserkerMaul(player) || fullBerserkerMace(player) || fullBerserkerSword(player)
					|| fullBerserkerDagger(player)) {
				effectiveStrength = Math.floor(effectiveStrength * 1.20);
			}
			double baseDamage = 5 + effectiveStrength * (1 + (strengthBonus / 64));
			return (int) Math.floor(baseDamage * specMultiplier * otherBonus);
		} else {
			if (weaponId == 24338 && target instanceof Player) {
				player.getPackets()
						.sendGameMessage("The royal crossbow feels weak and unresponsive against other players.");
				return 60;
			}
			double rangedLvl = player.getSkills().getLevel(Skills.RANGE);
			double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
			double effectiveStrenght = Math.round(rangedLvl * player.getPrayer().getRangeMultiplier()) + styleBonus;
			if (target instanceof NPC) {
				NPC n = (NPC) target;
			}
			if (fullVoidEquipped(player, 11664, 11675))
				effectiveStrenght += Math.floor((player.getSkills().getLevelForXp(Skills.RANGE) / 5) + 1.6);
			if (player.getEquipment().getWeaponId() == 20173
					&& !(player.getControlerManager().getControler() instanceof Wilderness)) {
				effectiveStrenght += Math.floor((player.getSkills().getLevelForXp(Skills.RANGE) / 4) + 4.0);
			}
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS];
			double baseDamage = 5 + (((effectiveStrenght + 8) * (strengthBonus + 64)) / 64);
			return (int) Math.round(baseDamage * specMultiplier);
		}
	}

	private double getSpecialAccuracyModifier(Player player) {
		Item weapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
		if (weapon == null)
			return 1;
		String name = weapon.getDefinitions().getName().toLowerCase();
		if (name.contains("whip") || name.contains("dragon scimitar") || name.contains("dragon dagger")
				|| name.contains("dragon spear") || name.contains("zamorakian spear") || name.contains("dragon halberd")
				|| name.contains("anchor") || name.contains("magic longbow") || name.contains("magic shortbow")
				|| name.contains("dragon longsword"))
			return 1.25;
		if (name.contains("dragon mace"))
			return 1.1;
		if (name.contains("claws") || name.contains("korasi") || name.contains("dragon claws")
				|| name.contains("godsword"))
			return 1.6;
		if (name.contains("granite maul") || name.contains("granite mace"))
			return 1.3;
		if (name.contains("dark bow") || name.contains("zanik"))
			return 1.5;
		if (name.contains("morrigan's javel"))
			return 1.4;
		if (name.contains("vesta's spear") || name.contains("statius' warhammer")
				|| name.contains("statius' warhammer (deg)") || name.contains("morrigan's throw")
				|| name.contains("hand cannon"))
			return 1.7;
		if (name.contains("vesta's longsword") || name.contains("vesta's longsword (deg)"))
			return 2.5;
		return 1;
	}

	public boolean hasFireCape(Player player) {
		int capeId = player.getEquipment().getCapeId();
		return capeId == 6570 || capeId == 23659 || capeId == 23660 || capeId == 20769 || capeId == 20771;
	}

	public static final boolean fullVanguardEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		int bootsId = player.getEquipment().getBootsId();
		int glovesId = player.getEquipment().getGlovesId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1 || bootsId == -1 || glovesId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(bootsId).getName().contains("Vanguard")
				&& ItemDefinitions.getItemDefinitions(glovesId).getName().contains("Vanguard");
	}

	public static final boolean usingGoliathGloves(Player player) {
		String name = player.getEquipment().getItem(Equipment.SLOT_SHIELD) != null
				? player.getEquipment().getItem(Equipment.SLOT_SHIELD).getDefinitions().getName().toLowerCase() : "";
		if (player.getEquipment().getItem((Equipment.SLOT_HANDS)) != null) {
			if (player.getEquipment().getItem(Equipment.SLOT_HANDS).getDefinitions().getName().toLowerCase()
					.contains("goliath") && player.getEquipment().getWeaponId() == -1) {
				if (name.contains("defender") && name.contains("dragonfire shield"))
					return true;
				return true;
			}
		}
		return false;
	}

	public static final boolean fullVeracsEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Verac's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Verac's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Verac's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Verac's");
	}

	public static final boolean fullGuthansEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Guthan's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Guthan's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Guthan's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Guthan's");
	}

	public static final boolean fullAkrisaeEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Akrisae's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Akrisae's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Akrisae's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Akrisae's");
	}

	public static final boolean fullDharokEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Dharok's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Dharok's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Dharok's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Dharok's");
	}

	public static final boolean fullBerserkerMaul(Player player) {
		int amuletId = player.getEquipment().getAmuletId();
		int weaponId = player.getEquipment().getWeaponId();
		if (amuletId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(amuletId).getName().contains("Berserker neck")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Tzhaar-ket-om");
	}

	public static final boolean fullBerserkerMace(Player player) {
		int amuletId = player.getEquipment().getAmuletId();
		int weaponId = player.getEquipment().getWeaponId();
		if (amuletId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(amuletId).getName().contains("Berserker neck")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Tzhaar-ket-em");
	}

	public static final boolean fullBerserkerSword(Player player) {
		int amuletId = player.getEquipment().getAmuletId();
		int weaponId = player.getEquipment().getWeaponId();
		if (amuletId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(amuletId).getName().contains("Berserker neck")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Toktz-xil-ak");
	}

	public static final boolean fullBerserkerDagger(Player player) {
		int amuletId = player.getEquipment().getAmuletId();
		int weaponId = player.getEquipment().getWeaponId();
		if (amuletId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(amuletId).getName().contains("Berserker neck")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Toktz-xil-ek");
	}

	public static final boolean BlackMask(Player player) {
		int hatId = player.getEquipment().getHatId();
		if (hatId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(hatId).getName().contains("Black mask");
	}

	public static final boolean Hexcrest(Player player) {
		int hatId = player.getEquipment().getHatId();
		if (hatId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(hatId).getName().contains("Hexcrest");
	}

	public static final boolean FocusSight(Player player) {
		int hatId = player.getEquipment().getHatId();
		if (hatId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(hatId).getName().contains("Focus sight");
	}

	public static final boolean SlayerHelmet(Player player) {
		int hatId = player.getEquipment().getHatId();
		if (hatId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(hatId).getName().contains("Slayer helmet");
	}

	public static final boolean FullSlayerHelmet(Player player) {
		int hatId = player.getEquipment().getHatId();
		if (hatId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(hatId).getName().contains("Full slayer helmet");
	}

	public static final boolean SalveAmmy(Player player) {
		int amuletId = player.getEquipment().getAmuletId();
		if (amuletId == -1)
			return false;
		return player.getEquipment().getAmuletId() == 4081;
	}

	public static final boolean SalveAmmyE(Player player) {
		int amuletId = player.getEquipment().getAmuletId();
		if (amuletId == -1)
			return false;
		return player.getEquipment().getAmuletId() == 10588;
	}

	public static final boolean fullVoidEquipped(Player player, int... helmid) {
		boolean hasDeflector = player.getEquipment().getShieldId() == 19712;
		if (player.getEquipment().getGlovesId() != 8842) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		int legsId = player.getEquipment().getLegsId();
		boolean hasLegs = legsId != -1 && (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
		if (!hasLegs) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		int torsoId = player.getEquipment().getChestId();
		boolean hasTorso = torsoId != -1
				&& (torsoId == 8839 || torsoId == 10611 || torsoId == 19785 || torsoId == 19787 || torsoId == 19789);
		if (!hasTorso) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		if (hasDeflector)
			return true;
		int helmId = player.getEquipment().getHatId();
		if (helmId == -1)
			return false;
		boolean hasHelm = false;
		for (int id : helmid) {
			if (helmId == id) {
				hasHelm = true;
				break;
			}
		}
		if (!hasHelm)
			return false;
		return true;
	}


	public void delayNormalHit(int weaponId, int attackStyle, Hit... hits) {
		delayHit(0, weaponId, attackStyle, hits);
	}

	public static Hit getMeleeHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MELEE_DAMAGE);
	}

	public static Hit getRangeHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.RANGE_DAMAGE);
	}

	public static Hit getMagicHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MAGIC_DAMAGE);
	}

	public static Hit getRegularHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.REGULAR_DAMAGE);
	}

	public static Hit getReflectedHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MISSED);
	}

	private void delayMagicHit(int delay, final Hit... hits) {
		delayHit(delay, -1, -1, hits);
	}

	public void resetVariables() {
		base_mage_xp = 0;
		mage_hit_gfx = 0;
		magic_sound = 0;
		max_poison_hit = 0;
		freeze_time = 0;
		reduceAttack = false;
		blood_spell = false;
		block_tele = false;
	}



	public void sendGuthanEffect(final Hit hit, final Player player, final Entity target) {
		int damage = hit.getDamage();
		if (target instanceof Player) {
			Player p2 = (Player) target;
			player.heal(damage);
			p2.setNextGraphics(new Graphics(398));
		} else {
			player.heal(damage);
			target.setNextGraphics(new Graphics(398));
		}
	}

	public void sendAkrisaeEffect(final Hit hit, final Player player, final Entity target) {
		int damage = hit.getDamage();
		if (target instanceof Player) {
			Player p2 = (Player) target;
			player.getPrayer().restorePrayer(damage / 3);
			p2.getPrayer().drainPrayer(damage / 5);
		} else {
			player.getPrayer().restorePrayer(damage / 3);
		}
	}

	public void handleDivine(Player player, Hit hit) {
		int damage = hit.getDamage();
		if (target instanceof Player) {
			Player p2 = (Player) target;
			int shieldId = p2.getEquipment().getShieldId();
			if (damage > 0) {
				if (shieldId == 13740 || shieldId == 23698) {
					int drain = (int) (Math.ceil(damage * 0.3) / 2);
					if (p2.getPrayer().getPrayerpoints() >= drain) {
						hit.setDamage((int) (damage * 0.70));
						p2.getPrayer().drainPrayer(drain);
					}
				}
			}
		}
	}

	public void handleElysian(Player player, Hit hit) {
		int damage = hit.getDamage();
		if (target instanceof Player) {
			Player p2 = (Player) target;
			int shieldId = p2.getEquipment().getShieldId();
			if (shieldId == 13742) {
				if (Utils.getRandom(10) <= 7 && damage > 0)
					hit.setDamage((int) (damage * 0.75));
			}
		}
	}

	public void handleSOL(Player player, Hit hit) {
		int damage = hit.getDamage();
		if (target instanceof Player) {
			Player p2 = (Player) target;
			int weaponId = p2.getEquipment().getWeaponId();
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (p2.polDelay > Utils.currentTimeMillis()) {
					if (weaponId != 15486 && weaponId != 11736) {
						p2.setPolDelay(0);
						p2.getPackets().sendGameMessage("The power of the " + (weaponId == 15486 ? "light" : "dead")
								+ "fades. Your resistance to melee attacks return to normal.");
					} else {
						if (damage > 0) {
							if (weaponId == 15486)
								p2.setNextGraphics(new Graphics(2320, 0, 0));
							if (weaponId == 11736)
								p2.setNextGraphics(new Graphics(3059, 0, 0));
							hit.setDamage((int) (damage * 0.5));
						}
					}
				}
			}
		}
	}

	public void handleAbsorbRing(Player player, Hit hit) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			int ringId = p2.getEquipment().getRingId();
			if (hit.getLook() == HitLook.MELEE_DAMAGE) {
				if (ringId == 773) {
					if (hit.getDamage() > 0)
						hit.setDamage((int) (hit.getDamage()));
				}
			}
		}
	}

	public void handleDisruption(Player player, Hit hit) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (p2.isUsingDisruption()) {
				if (hit.getLook() == HitLook.MELEE_DAMAGE || hit.getLook() == HitLook.RANGE_DAMAGE
						|| hit.getLook() == HitLook.MAGIC_DAMAGE) {
					p2.usingDisruption = false;
					p2.disDelay = 0;
					p2.getPackets().sendGameMessage("Your Disruption Shield blocked your victim's damage.");
					player.getPackets().sendGameMessage("Your victim's Disruption Shield blocked your damage.");
					hit.setDamage(0);
				}
			}
		}
	}

	public void handleRingOfRecoil(Player player, Hit hit) {
		int damage = (int) (hit.getDamage() * 0.1);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (p2.getEquipment().getRingId() == 2550) {
				if (hit.getLook() == HitLook.MELEE_DAMAGE || hit.getLook() == HitLook.RANGE_DAMAGE
						|| hit.getLook() == HitLook.MAGIC_DAMAGE) {
					if (hit.getDamage() > 9) {
						if (p2.recoilHits <= 0) {
							player.applyHit(new Hit(p2, damage > 60 ? 60 : damage, HitLook.REGULAR_DAMAGE));
							p2.recoilHits = 400;
							p2.recoilHits -= damage;
							p2.getPackets().sendGameMessage("Your ring of recoil has degraded.");
						} else if (p2.recoilHits >= damage) {
							player.applyHit(new Hit(p2, damage > 60 ? 60 : damage, HitLook.REGULAR_DAMAGE));
							p2.recoilHits -= damage;
						} else if (p2.recoilHits < damage) {
							p2.getEquipment().deleteItem(2550, 1);
							p2.getAppearence().generateAppearenceData();
							player.applyHit(new Hit(p2, damage > 60 ? 60 : damage, HitLook.REGULAR_DAMAGE));
							p2.recoilHits = 400;
							p2.recoilHits -= damage;
							p2.getPackets().sendGameMessage("Your ring of recoil has shattered.");
						}
					}
				}
			}
		}
	}

	private void handleVengHit(Player player, Hit hit) {
		int damage = hit.getDamage() > target.getHitpoints() ? target.getHitpoints() : hit.getDamage();
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (p2.castedVeng && damage >= 4) {
				p2.castedVeng = false;
				p2.setNextForceTalk(new ForceTalk("Taste vengeance!"));
				player.applyHit(new Hit(p2, (int) (damage * 0.75), HitLook.REGULAR_DAMAGE));
			}
		}
	}

	public void handleAbsorb(Player player, Hit hit) {
		int damage = hit.getDamage();
		Player p2 = (Player) target;
		if (hit.getLook() == HitLook.MELEE_DAMAGE) {
			int reducedDamage = (damage - 200)
					* p2.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] / 100;
			if (damage - reducedDamage > 200 && p2.getHitpoints() > 200) {
				if (reducedDamage > 0) {
					hit.setDamage(damage - reducedDamage);
					hit.setSoaking(new Hit(player, reducedDamage, HitLook.MISSED));
				}
			}
		}
		if (hit.getLook() == HitLook.RANGE_DAMAGE) {
			int reducedDamage = (damage - 200)
					* p2.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] / 100;
			if (damage - reducedDamage > 200 && p2.getHitpoints() > 200) {
				if (reducedDamage > 0) {
					hit.setDamage(damage - reducedDamage);
					hit.setSoaking(new Hit(player, reducedDamage, HitLook.MISSED));
				}
			}
		}
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			int reducedDamage = (damage - 200)
					* p2.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] / 100;
			if (damage - reducedDamage > 200 && p2.getHitpoints() > 200) {
				if (reducedDamage > 0) {
					hit.setDamage(damage - reducedDamage);
					hit.setSoaking(new Hit(player, reducedDamage, HitLook.MISSED));
				}
			}
		}
	}

	public void handleAutoRetaliate(Player player, Entity target) {
		Player p2 = (Player) target;
		if (p2.getCombatDefinitions().isAutoRelatie() && !p2.getActionManager().hasSkillWorking()
				&& !p2.hasWalkSteps()) {
			p2.closeInterfaces();
			p2.getActionManager().setAction(new PlayerCombat(player));
		}
	}

	public void checkPID(Hit hit, Player player, Entity target) {
		Player p2 = (Player) target;
		if (player.getControlerManager().getControler() instanceof DuelArena) {
			if (player.getDamage() >= p2.getHitpoints() && p2.getDamage() >= player.getHitpoints()) {
				if (player.getIndex() > p2.getIndex()) {
					hit.setDamage(-1);
				}
			}
		}
	}

	public void handleKerisEffect(Hit hit, Player player) {
		if (Utils.getRandom(9) == 0) {
			hit.setDamage(hit.getDamage() * 3);
			player.getPackets().sendGameMessage(
					"You slip your dagger through a chink in the creature's chitin, landing a vicious blow.");
		} else {
			hit.setDamage(hit.getDamage() * 2);
		}
	}

	public void handleSagaie(Hit hit, Player player) {
		if (isRanging(player) > 0) {
			if (player.getEquipment().getWeaponId() == 21364) {
				int distance = Utils.getDistance(player, target);
				if (distance > 4)
					distance = 4;
				if (hit.getDamage() > 20)
					hit.setDamage(hit.getDamage() + (20 * distance));
			}
		}
	}

	private int epTimer;

	private void delayHit(int delay, final int weaponId, final int attackStyle, final Hit... hits) {
		addAttackedByDelay(hits[0].getSource());

		final Entity target = this.target;
		final double base_mage_xp = this.base_mage_xp;
		final int freeze_time = this.freeze_time;
		final int max_hit = this.max_hit;
		final int mage_hit_gfx = this.mage_hit_gfx;
		final int magic_sound = this.magic_sound;
		final int max_poison_hit = this.max_poison_hit;
		final boolean blood_spell = this.blood_spell;
		final boolean block_tele = this.block_tele;
		resetVariables();

		for (Hit hit : hits) {
			final Player player = (Player) hit.getSource();
			if (target instanceof Player) {
				Player p2 = (Player) target;
				p2.handleProtectPrayers(hit);
				addAttackedByDelay(p2);
			}
			handleDivine(player, hit);
			handleElysian(player, hit);
			handleSOL(player, hit);
			handleSagaie(hit, player);
			if (target instanceof Player)
				handleAbsorb(player, hit);
			int damage = hit.getDamage() > target.getHitpoints() ? target.getHitpoints() : hit.getDamage();

			if (blood_spell)
				player.heal(damage / 4);
			if (fullGuthansEquipped(player) && Utils.random(3) == 0)
				sendGuthanEffect(hit, player, target);
			if (fullAkrisaeEquipped(player) && Utils.random(10) <= 3)
				sendAkrisaeEffect(hit, player, target);
			if (player.isAtWild() && player.getEp() < 100) {
				if (Utils.getRandom(2) == 0)
					if (player.getEp() >= 97 && player.getEp() <= 99)
						player.setEp(100);
					else
						player.setEp(player.getEp() + Utils.random(1, 3));
			}
			if (target instanceof NPC) {
				NPC n = (NPC) target;
				if (player.getEquipment().getWeaponId() == 10581
						&& n.getDefinitions().getName().toLowerCase().contains("kalphite"))
					handleKerisEffect(hit, player);
				if (n.getCapDamage() != -1 && hit.getDamage() > n.getCapDamage()) {
					if (player.getEquipment().getRingId() == 773)
						hit.setDamage(n.getMaxHitpoints());
					else
						hit.setDamage(n.getCapDamage());
				}
			}
			addAttackedByDelay(player);
			if (target instanceof Player || target instanceof NPC) {
				if (hit.getLook() == HitLook.RANGE_DAMAGE) {
					double rangeXP = damage * 4;
					if (attackStyle == 2) {
						player.getSkills().addXp(Skills.RANGE, (damage * 0.2));
						player.getSkills().addXp(Skills.DEFENCE, (damage * 0.2));
					} else {
						player.getSkills().addXp(Skills.RANGE, rangeXP);
					}
					double hpXP = (damage * 0.133) * 5; //15 exp per hit *0.133 orginal
					if (hpXP > 0)
						player.getSkills().addXp(Skills.HITPOINTS, hpXP * 2);
				} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
					double meleeXP = damage * 4;
					if (meleeXP > 0) {
						int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
						if (xpStyle != CombatDefinitions.SHARED) {
							player.getSkills().addXp(xpStyle, meleeXP);
						} else {
							player.getSkills().addXp(Skills.ATTACK, (damage / 3));
							player.getSkills().addXp(Skills.STRENGTH, (damage / 3));
							player.getSkills().addXp(Skills.DEFENCE, (damage / 3));
						}
					}
					double hpXP = damage * (1 + 1 / 3);
					if (hpXP > 0)
						player.getSkills().addXp(Skills.HITPOINTS, hpXP);
				} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
					double magicXP = base_mage_xp * 1 + (damage / 5);
					//double magicXP = base_mage_xp + (damage > 0 ? damage * 0.3 : 0);
					if (mage_hit_gfx != 0 && damage > 0) {
						if (freeze_time > 0) {
							player.setFrozenBy(target);
							target.addFreezeDelay(freeze_time, freeze_time == 0);
							if (freeze_time > 0)
								if (target instanceof Player) {
									((Player) target).stopAll(false);
								}
							target.addFrozenBlockedDelay(freeze_time + (4 * 1000));
						}
					}
					if (magicXP > 0) {
						if (player.getCombatDefinitions().isDefensiveCasting()
								|| (hasPolyporeStaff(player) && player.getCombatDefinitions().getAttackStyle() == 1)) {
							player.getSkills().addXp(Skills.DEFENCE, (damage * 0.1));
							player.getSkills().addXp(Skills.MAGIC, (damage * 0.133));
						} else {
							player.getSkills().addXp(Skills.MAGIC, magicXP);
						}
						double hpXP = (damage * 0.133);
						if (hpXP > 0)
							player.getSkills().addXp(Skills.HITPOINTS, hpXP);
					}
				}
			}
			if (target instanceof Player) {
				Player p2 = (Player) target;
				if (p2.teleportDelay > Utils.currentTimeMillis())
					return;
			}
		}

		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				for (Hit hit : hits) {
					boolean splash = false;
					Player player = (Player) hit.getSource();
					if (player.isDead() || player.hasFinished() || target.isDead() || target.hasFinished())
						return;
					if (hit.getDamage() > -1) {
						target.applyHit(hit); // also reduces damage if needed,
												// pray
						// and special items affect here
					} else {
						splash = true;
						hit.setDamage(0);
					}
					doDefenceEmote(player);
					int damage = hit.getDamage() > target.getHitpoints() ? target.getHitpoints() : hit.getDamage();
					if ((damage >= max_hit * 0.90) && (hit.getLook() == HitLook.MAGIC_DAMAGE
							|| hit.getLook() == HitLook.RANGE_DAMAGE || hit.getLook() == HitLook.MELEE_DAMAGE))
						hit.setCriticalMark();
					if (hit.getLook() == HitLook.RANGE_DAMAGE || hit.getLook() == HitLook.MELEE_DAMAGE) {
						double combatXp = damage / 2.5;
						if (combatXp > 0) {
							if (hit.getLook() == HitLook.RANGE_DAMAGE) {
								if (weaponId != -1) {
									String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
									if (name.contains("(p++)")) {
										if (Utils.getRandom(8) == 0)
											target.getPoison().makePoisoned(5);
									} else if (name.contains("(p+)")) {
										if (Utils.getRandom(8) == 0)
											target.getPoison().makePoisoned(4);
									} else if (name.contains("(p)")) {
										if (Utils.getRandom(8) == 0)
											target.getPoison().makePoisoned(3);
									}
								}
							} else {
								if (weaponId != -1) {
									String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
									if (name.contains("(p++)")) {
										if (Utils.getRandom(8) == 0)
											target.getPoison().makePoisoned(6);
									} else if (name.contains("(p+)")) {
										if (Utils.getRandom(8) == 0)
											target.getPoison().makePoisoned(4);
									} else if (name.contains("(p)")) {
										if (Utils.getRandom(8) == 0)
											target.getPoison().makePoisoned(2);
									}
									if (weaponId >= 21371 && weaponId <= 21375) {
										if (Utils.getRandom(8) == 0) {
											target.getPoison().makePoisoned(6);
										}
									}
								}
							}
						}
					} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (splash || hit.getDamage() == 0) {
							target.setNextGraphics(new Graphics(85, 0, 96));
							playSound(227, player, target);
						} else {
							if (mage_hit_gfx != 0) {
								target.setNextGraphics(new Graphics(mage_hit_gfx, 0,
										mage_hit_gfx == 76 || mage_hit_gfx == 78 || mage_hit_gfx == 77
												|| mage_hit_gfx == 369 || mage_hit_gfx == 1843
												|| (mage_hit_gfx > 1844 && mage_hit_gfx < 1855) ? 0 : 96));
								if (block_tele) {
									if (target instanceof Player) {
										Player targetPlayer = (Player) target;
										targetPlayer.setTeleBlockDelay(teleBlockTime);
										targetPlayer.setTeleBlockImmune(teleBlockTime + 10000);
										targetPlayer.getPackets().sendGameMessage(
												"A teleportblock spell have been cast on you, you can't teleport for another "
														+ targetPlayer.getTeleBlockTimeleft() + ".",
												true);
									}
								}
							}
							if (magic_sound > 0)
								playSound(magic_sound, player, target);
						}
					}
					if (max_poison_hit > 0 && Utils.getRandom(10) == 0) {
						if (!target.getPoison().isPoisoned())
							target.getPoison().makePoisoned(max_poison_hit);
					}
					if (target instanceof Player) {
						Player p2 = (Player) target;
						handleAutoRetaliate(player, p2);
					} else {
						NPC n = (NPC) target;
						if (!n.isUnderCombat() || n.canBeAttackedByAutoRelatie())
							n.setTarget(player);
					}

				}
			}
		}, delay);
	}

	private int getSoundId(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName.contains("dart") || weaponName.contains("knife"))
				return 2707;
		}
		return -1;
	}

	public static int getWeaponAttackEmote(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			if (weaponId == -2) {
				// punch/block:14393 kick:14307 spec:14417
				switch (attackStyle) {
				case 1:
					return 14307;
				default:
					return 14393;
				}
			}
			if (weaponId == 4726) {
				switch (attackStyle) {
				case 1:// str
					return 2081;
				case 2:// controlled
					return 2083;
				default:// attack & def
					return 2080;
				}
			}
			if (weaponId == 11736) {
				switch (attackStyle) {
				case 1:// str
					return 413;
				case 2:// controlled
					return 414;
				default:// attack & def
					return 413;
				}
			}
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
			if (weaponName != null && !weaponName.equals("null")) {
				if (weaponName.contains("crossbow"))
					return weaponName.contains("karil") ? 2075 : 4230;
				if (weaponName.contains("bow"))
					return 426;
				if (weaponName.contains("chinchompa"))
					return 2779;
				if (weaponName.contains("staff of light")) {
					switch (attackStyle) {
					case 0:
						return 15072;
					case 1:
						return 15071;
					case 2:
						return 414;
					}
				}
				if (weaponName.contains("staff") || weaponName.contains("wand")) {
					switch (attackStyle) {
					case 2:
						return 419;
					default:
						return 393;
					}
				}
				if (weaponName.contains("chaotic staff"))
					return 401;
				if (weaponName.contains("dart"))
					return 6600;
				if (weaponName.contains("knife"))
					return 6600;
				if (weaponName.contains("scimitar") || weaponName.contains("korasi's sword") || weaponName.contains("longsword")) {
					switch (attackStyle) {
					case 2:
						return 12028;
					default:
						return 12029;
					}
				}
				if (weaponName.contains("granite mace"))
					return 400;
				if (weaponName.contains("mace")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 401;
					}
				}
				if (weaponName.contains("hatchet")) {
					switch (attackStyle) {
					case 2:
						return 401;
					default:
						return 395;
					}
				}
				if (weaponName.contains("warhammer")) {
					switch (attackStyle) {
					default:
						return 401;
					}
				}
				if (weaponName.contains("claws")) {
					switch (attackStyle) {
					case 2:
						return 1067;
					default:
						return 393;
					}
				}
				if (weaponName.contains("whip") || weaponName.contains("tentacle")) {
					switch (attackStyle) {
					case 1:
						return 11969;
					case 2:
						return 11970;
					default:
						return 11968;
					}
				}
				if (weaponName.contains("anchor")) {
					switch (attackStyle) {
					case 2:
						return 5865;
					default:
						return 5865;
					}
				}
				if (weaponName.contains("tzhaar-ket-em")) {
					switch (attackStyle) {
					default:
						return 401;
					}
				}
				if (weaponName.contains("toktz-xil-ek")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 12311;
					}
				}
				if (weaponName.contains("toktz-xil-ak")) {
					switch (attackStyle) {
					case 2:
						return 12311;
					default:
						return 400;
					}
				}
				if (weaponName.contains("tzhaar-ket-om")) {
					switch (attackStyle) {
					default:
						return 2661;
					}
				}
				if (weaponName.contains("halberd")) {
					switch (attackStyle) {
					case 1:
						return 440;
					default:
						return 428;
					}
				}
				if (weaponName.contains("zamorakian spear")) {
					switch (attackStyle) {
					case 1:// str
						return 12005;
					case 2:// controlled
						return 12009;
					default:// attack & def
						return 12006;
					}
				}
				if (weaponName.contains("spear")) {
					switch (attackStyle) {
					case 1:
						return 440;
					case 2:
						return 429;
					default:
						return 428;
					}
				}
				if (weaponName.contains("flail")) {
					return 2062;
				}
				if (weaponName.contains("javelin")) {
					return 10501;
				}
				if (weaponName.contains("morrigan's throwing axe"))
					return 10504;
				if (weaponName.contains("agai"))
					return 3236;
				if (weaponName.contains("pickaxe")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 401;
					}
				}
				if (weaponName.contains("dragon dagger")) {
					switch (attackStyle) {
					case 2:
						return 377;
					default:
						return 376;
					}
				}
				if (weaponName.contains("dagger")) {
					switch (attackStyle) {
					case 2:
						return 390;
					default:
						return 386;
					}
				}
				if (weaponName.contains("Toktz-xil-ak")) {
					switch (attackStyle) {
					case 2:
						return 12311;
					default:
						return 400;
					}
				}
				if (weaponName.contains("2h sword") || weaponName.equals("dominion sword")
						|| weaponName.equals("thok's sword") || weaponName.equals("saradomin sword") || weaponName.equals("saradomin's blessed sword")) {
					switch (attackStyle) {
					case 2:
						return 7048;
					case 3:
						return 7049;
					default:
						return 7041;
					}
				}
				if (weaponName.contains("laded sword")) {
					switch (attackStyle) {
					case 2:
						return 13048;
					default:
						return 13049;
					}
				}
				if (weaponName.contains(" sword") || weaponName.contains("saber") || weaponName.contains("longsword")
						|| weaponName.contains("light") || weaponName.contains("excalibur")) {
					switch (attackStyle) {
					case 2:
						return 12310;
					default:
						return 12311;
					}
				}
				if (weaponName.contains("rapier") || weaponName.contains("brackish")) {
					switch (attackStyle) {
					case 2:
						return 13048;
					default:
						return 13049;
					}
				}
				if (weaponName.contains("katana")) {
					switch (attackStyle) {
					case 2:
						return 1882;
					default:
						return 1884;
					}
				}
				if (weaponName.contains("godsword")) {
					switch (attackStyle) {
					case 2:
						return 11980;
					case 3:
						return 11981;
					default:
						return 11979;
					}
				}
				if (weaponName.contains("greataxe") || weaponName.contains("balmung")) {
					switch (attackStyle) {
					case 2:
						return 12003;
					default:
						return 12002;
					}
				}
				if (weaponName.contains("granite maul")) {
					switch (attackStyle) {
					default:
						return 1665;
					}
				}
				if (weaponName.contains("battleaxe")) {
					switch (attackStyle) {
					default:
						return 395;
					}
				}

			}
		}
		switch (weaponId) {
		case 6522:
			return 2614;
		case 800: //bronze thrownaxe
		case 801: //iron thrownaxe
		case 802: //steel thrownaxe
		case 803: //mithril thrownaxe
		case 804: //addy thrownaxe
		case 805: //rune thrownaxe
			return 929;
		case 13883: // morrigan thrown axe
			return 10504;
		case 15241:
			return 12174;
		case 4675:
		case 4710:
		case 4862:
		case 4170:
			return 393;
		default:
			switch (attackStyle) {
			case 1:
				return 423;
			default:
				return 422; // todo default emote
			}
		}
	}

	private void doDefenceEmote(Player player) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (p2.getCombatDefinitions().getAttackStyle() > Utils.currentTimeMillis()) {
				return;
			}
			if (player.isTeleporting)
				return;
			if (p2.getLockDelay() > Utils.currentTimeMillis()) {
				return;
			}
			if (player.getLockDelay() > Utils.currentTimeMillis()) {
				return;
			}
		}
		target.setNextAnimationNoPriority(new Animation(Combat.getDefenceEmote(target)), target);
	}

	private void doDefenceEmote(NPC npc) {
		if (target instanceof NPC) {
			NPC n = (NPC) target;
			if (n.getCombatDefinitions().getAttackStyle() > Utils.currentTimeMillis()) {
				return;
			}
		}
		npc.setNextAnimationNoPriority(new Animation(Combat.getDefenceEmote(target)), npc);
	}

	public static int getMeleeCombatDelay(Player player, int weaponId) {
		final ItemDefinitions defs = ItemDefinitions.getItemDefinitions(weaponId);
		if (defs == null || weaponId == -1)
			return weaponId == -1 ? 3 : 4;
		return defs.getAttackSpeed() - 1;
	}

	@Override
	public void stop(final Player player) {
		player.setNextFaceEntity(null);
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.hasFinished() || target.isDead() || target.hasFinished()
				|| player.getLockDelay() > Utils.currentTimeMillis()) {
			return false;
		}
		player.resetWalkSteps();
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = player.getSize();
		int maxDistance = 16;
		if (player.getPlane() != target.getPlane() || distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
			player.freezeDelay = 0;
			return false;
		}
		if (player.getCombatDefinitions().getSpellId() <= 0
				&& Utils.inCircle(new WorldTile(3105, 3933, 0), target, 23)) {
			player.getPackets().sendGameMessage("You can only use magic in the arena.");
			return false;
		}
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (!player.isCanPvp() || !p2.isCanPvp())
				return false;
		} else {
			NPC n = (NPC) target;
			if (n.isCantInteract()) {
				return false;
			}
			if (n instanceof Familiar) {
				Familiar familiar = (Familiar) n;
				if (!familiar.canAttack(target))
					return false;
			} else {
				if (!n.canBeAttackFromOutOfArea() && !MapAreas.isAtArea(n.getMapAreaNameHash(), player)) {
					return false;
				}
				if (n.getId() == 879) {
					if (player.getEquipment().getWeaponId() != 2402
							&& player.getCombatDefinitions().getAutoCastSpell() <= 0 && !hasPolyporeStaff(player)) {
						player.getPackets().sendGameMessage("I'd better wield Silverlight first.");
						return false;
					}
				} else if (n.getId() >= 14084 && n.getId() <= 14139) {
					int weaponId = player.getEquipment().getWeaponId();
					if (!((weaponId >= 13117 && weaponId <= 13146) || (weaponId >= 21580 && weaponId <= 21582))
							&& player.getCombatDefinitions().getAutoCastSpell() <= 0 && !hasPolyporeStaff(player)) {
						player.getPackets().sendGameMessage("I'd better wield a silver weapon first.");
						return false;
					}
				} else {
					int slayerLevel = Combat.getSlayerLevelForNPC(n.getId());
					if (slayerLevel > player.getSkills().getLevel(Skills.SLAYER)) {
						player.getPackets().sendGameMessage(
								"You need at least a slayer level of " + slayerLevel + " to fight this.");
						return false;
					}
				} if (n.getId() == 6222 || n.getId() == 6223 || n.getId() == 6225 || n.getId() == 6227
						|| (n.getId() >= 6232 && n.getId() <= 6246)) {
					if (isRanging(player) == 0) {
						player.getPackets()
								.sendGameMessage("The Aviansie is flying too high for you to attack using melee.");
						return false;
					}
				}
			}
		}
		if (!(target instanceof NPC && ((NPC) target).isForceMultiAttacked())) {
			if (player.isAtMultiArea() && !target.isAtMultiArea()) {
				if (target.getAttackedBy() != player && target.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("That "
							+ (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
					return false;
				}
			}
			if (!target.isAtMultiArea() && !player.isAtMultiArea()) {
				if (player.getAttackedBy() != target && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You are already in combat.");
					return false;
				}
				if (target.getAttackedBy() != player && target.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("That "
							+ (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
					return false;
				}
			}
		}
		int isRanging = isRanging(player);
		int targetSize = target.getSize();
		if (player.getFreezeDelay() >= Utils.currentTimeMillis()) {
			if (isRanging(player) == 0 && player.getCombatDefinitions().getSpellId() <= 0 && size == 1)
				if (target.getX() == player.getX() + 1 && target.getY() == player.getY() + 1
						|| target.getX() == player.getX() - 1 && target.getY() == player.getY() - 1
						|| target.getX() == player.getX() - 1 && target.getY() == player.getY() + 1
						|| target.getX() == player.getX() + 1 && target.getY() == player.getY() - 1)
					return false;

			if (player.withinDistance(target, 0))
				return false;
			if (player.getCombatDefinitions().isUsingSpecialAttack()
					&& player.hasInstantSpecial(player.getEquipment().getWeaponId())
					&& player.withinDistance(target, 1)) {
			} else
				return true;
		}
		if (distanceX < size && distanceX > -1 && distanceY < size && distanceY > -1 && !target.hasWalkSteps()) {
			player.resetWalkSteps();
			if (!player.addWalkSteps(target.getX() + size, target.getY())) {
				player.resetWalkSteps();
				if (!player.addWalkSteps(target.getX() - 1, target.getY())) {
					player.resetWalkSteps();
					if (!player.addWalkSteps(target.getX(), target.getY() + size)) {
						player.resetWalkSteps();
						if (!player.addWalkSteps(target.getX(), target.getY() - 1)) {
							return false;
						}
					}
				}
			}
			return true;
		} else if (isRanging == 0 && target.getSize() == 1 && player.getCombatDefinitions().getSpellId() <= 0
				&& !hasPolyporeStaff(player) && Math.abs(player.getX() - target.getX()) == 1
				&& Math.abs(player.getY() - target.getY()) == 1 && !target.hasWalkSteps()) {
			if (!player.addWalkSteps(target.getX(), player.getY(), 1))
				player.addWalkSteps(player.getX(), target.getY(), 1);
			return true;
		}
		int spellId = player.getCombatDefinitions().getSpellId();
		maxDistance = isRanging != 0 ? getAttackDistance(player) : spellId > 0 || hasPolyporeStaff(player) ? 7 : 0;
		boolean needCalc = !player.hasWalkSteps() || target.hasWalkSteps();
		if ((!player.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target)))
				|| !Utils.isOnRange(player.getX(), player.getY(), size, target.getX(), target.getY(), target.getSize(),
						maxDistance)) {
			// if (!player.hasWalkSteps()) {
			if (needCalc) {
				player.resetWalkSteps();
				player.calcFollow(target, player.getRun() ? 2 : 1, true, true);
			}
			// }
			return true;
		} else {
			player.resetWalkSteps();
		}
		if (player.getPolDelay() >= Utils.currentTimeMillis() && !(player.getEquipment().getWeaponId() == 15486
				|| player.getEquipment().getWeaponId() == 22207 || player.getEquipment().getWeaponId() == 22209
				|| player.getEquipment().getWeaponId() == 11736 || player.getEquipment().getWeaponId() == 22211
				|| player.getEquipment().getWeaponId() == 22213))
			player.setPolDelay(0);
		player.temporaryAttribute().put("last_target", target);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			player.setTargetName(p2);
			p2.setTargetName(player);
		}
		target.temporaryAttribute().put("last_attacker", player);
		if (player.getCombatDefinitions().isInstantAttack()) {
			player.getCombatDefinitions().setInstantAttack(false);
			if (player.getCombatDefinitions().getAutoCastSpell() > 0)
				return true;
			if (player.getCombatDefinitions().isUsingSpecialAttack()) {
				if (!specialExecute(player))
					return true;
				int weaponId = player.getEquipment().getWeaponId();
				int attackStyle = player.getCombatDefinitions().getAttackStyle();
				switch (weaponId) {
				case 4153:
					player.faceEntity(target);
					player.setNextAnimationNoPriority(new Animation(1667), player);
					player.setNextGraphics(new Graphics(340, 0, 96 << 16));
					delayNormalHit(weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true)));
					player.getActionManager().setActionDelay(player.getActionManager().getActionDelay());
					break;
				case 14679:
					player.setNextAnimationNoPriority(new Animation(10505), player);
					player.setNextGraphics(new Graphics(1840));
					player.faceEntity(target);
					delayNormalHit(weaponId, attackStyle, getMeleeHit(player,
							getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true)));
					player.getActionManager().setActionDelay(2);
					break;
				}
			}
			return true;
		}
		return true;
	}

	public static boolean specialExecute(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		player.getCombatDefinitions().switchUsingSpecialAttack();
		int specAmt = getSpecialAmount(weaponId);
		if (specAmt == 0) {
			player.getPackets()
					.sendGameMessage("This weapon has no special Attack, if you still see special bar please relogin.");
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
			return false;
		}
		if (player.getCombatDefinitions().hasRingOfVigour())
			specAmt *= 0.9;
		// if (player.getInventory().containsItem(773, 1))
		// specAmt *= 0.0;
		if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
			player.getPackets().sendGameMessage("You don't have enough power left.");
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
			return false;
		}
		player.getCombatDefinitions().desecreaseSpecialAttack(specAmt);
		return true;
	}

	/*
	 * 0 not ranging, 1 invalid ammo so stops att, 2 can range, 3 no ammo
	 */

	public static final int isRanging(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return 0;
		if (weaponId >= 13719 && weaponId <= 13721)
			return 0;
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
		if (name != null) {
			if (name.toLowerCase().contains("bronze knife") || name.toLowerCase().contains("iron knife")
					|| name.toLowerCase().contains("steel knife") || name.toLowerCase().contains("b򡣬 knife")
					|| name.toLowerCase().contains("mthiril knife") || name.toLowerCase().contains("adamant knife")
					|| name.toLowerCase().contains("rune knife") || name.toLowerCase().contains("bronze dart")
					|| name.toLowerCase().contains("iron dart") || name.toLowerCase().contains("steel dart")
					|| name.toLowerCase().contains("black dart") || name.toLowerCase().contains("mithril dart")
					|| name.toLowerCase().contains("adamant dart") || name.toLowerCase().contains("dragon dart")
					|| name.toLowerCase().contains("rune dart") || name.toLowerCase().contains("javelin")
					|| name.contains("thrownaxe") || name.toLowerCase().contains("throwing axe")
					|| name.toLowerCase().contains("crystal bow") || name.toLowerCase().contains("sling")
					|| name.toLowerCase().contains("zaryte") || name.toLowerCase().contains("chinchompa")
					|| weaponId == 6522)
				return 2;
		}
		int ammoId = player.getEquipment().getAmmoId();
		switch (weaponId) {
		case 15241: // Hand cannon
			switch (ammoId) {
			case -1:
				return 3;
			case 15243: // Hand cannon shot
				return 2;
			default:
				return 1;
			}
		case 9705: //training bow
		switch (ammoId) {
		case 9706: //training arrows
		return 2;
		}
		case 839: // longbow
		case 841: // shortbow
			switch (ammoId) {
			case -1:
				return 3;
			case 883: // bronze p
			case 5616: // bronze p+
			case 5622: // bronze p++
			case 885: // iron p
			case 5617: // iron p+
			case 5623: // iron p++
			case 882: // bronze arrow
			case 884: // iron arrow
				return 2;
			default:
				return 1;
			}
		case 843: // oak longbow
		case 845: // oak shortbow
			switch (ammoId) {
			case -1:
				return 3;
			case 883: // bronze p
			case 5616: // bronze p+
			case 5622: // bronze p++
			case 885: // iron p
			case 5617: // iron p+
			case 5623: // iron p++
			case 887: // steel p
			case 5618: // steel p
			case 5624: // steel p++
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
				return 2;
			default:
				return 1;
			}
		case 847: // willow longbow
		case 849: // willow shortbow
		case 13541: // Willow composite bow
			switch (ammoId) {
			case -1:
				return 3;
			case 883: // bronze p
			case 5616: // bronze p+
			case 5622: // bronze p++
			case 885: // iron p
			case 5617: // iron p+
			case 5623: // iron p++
			case 887: // steel p
			case 5618: // steel p
			case 5624: // steel p++
			case 889: // mithril p
			case 5619: // mithril p+
			case 5625: // mithril p++
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
				return 2;
			default:
				return 1;
			}
		case 851: // maple longbow
		case 853: // maple shortbow
		case 18331: // Maple longbow (sighted)
			switch (ammoId) {
			case -1:
				return 3;
			case 883: // bronze p
			case 5616: // bronze p+
			case 5622: // bronze p++
			case 885: // iron p
			case 5617: // iron p+
			case 5623: // iron p++
			case 887: // steel p
			case 5618: // steel p
			case 5624: // steel p++
			case 889: // mithril p
			case 5619: // mithril p+
			case 5625: // mithril p++
			case 891: // adamant p
			case 5620: // adamant p+
			case 5626: // adamant p++
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
				return 2;
			default:
				return 1;
			}
		case 2883:// ogre bow
			switch (ammoId) {
			case -1:
				return 3;
			case 2866: // ogre arrow
				return 2;
			default:
				return 1;
			}
		case 4827:// Comp ogre bow
			switch (ammoId) {
			case -1:
				return 3;
			case 2866: // ogre arrow
			case 4773: // bronze brutal
			case 4778: // iron brutal
			case 4783: // steel brutal
			case 4788: // black brutal
			case 4793: // mithril brutal
			case 4798: // adamant brutal
			case 4803: // rune brutal
				return 2;
			default:
				return 1;
			}
		case 855: // yew longbow
		case 857: // yew shortbow
		case 10281: // Yew composite bow
		case 14121: // Sacred clay bow
		case 859: // magic longbow
		case 861: // magic shortbow
		case 10284: // Magic composite bow
		case 18332: // Magic longbow (sighted)
		case 6724: // seercull
			switch (ammoId) {
			case -1:
				return 3;
			case 883: // bronze p
			case 5616: // bronze p+
			case 5622: // bronze p++
			case 885: // iron p
			case 5617: // iron p+
			case 5623: // ron p++
			case 887: // steel p
			case 5618: // steel p
			case 5624: // steel p++
			case 889: // mithril p
			case 5619: // mithril p+
			case 5625: // mithril p++
			case 891: // adamant p
			case 5620: // adamant p+
			case 5626: // adamant p++
			case 893: // rune arrow p
			case 5621: // rune arrow p+
			case 5627: // rune arrow p++
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
			case 892: // rune arrow
				return 2;
			case 19157: // guthix arrows
			case 19152: // saradomin arrows
			case 19162: // zamorak arrows
				return 2;
			default:
				return 1;
			}
		case 11235: // dark bows
		case 15293: //3rd age bow
			switch (ammoId) {
			case -1:
				return 3;
			case 883: // bronze p
			case 5616: // bronze p+
			case 5622: // bronze p++
			case 885: // iron p
			case 5617: // iron p+
			case 5623: // ron p++
			case 887: // steel p
			case 5618: // steel p
			case 5624: // steel p++
			case 889: // mithril p
			case 5619: // mithril p+
			case 5625: // mithril p++
			case 891: // adamant p
			case 5620: // adamant p+
			case 5626: // adamant p++
			case 893: // rune arrow p
			case 5621: // rune arrow p+
			case 5627: // rune arrow p++
			case 11227: // dragon arrow p
			case 11228: // dragon arrow p+
			case 11229: // dragon arrow p++
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
			case 892: // rune arrow
			case 11212: // dragon arrow
				return 2;
			default:
				return 1;
			}
		case 19143: // saradomin bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19152: // saradomin arrow
				return 2;
			default:
				return 1;
			}
		case 19146: // guthix bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19157: // guthix arrow
				return 2;
			default:
				return 1;
			}
		case 19149: // zamorak bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19162: // zamorak arrow
				return 2;
			default:
				return 1;
			}
		case 24338: // Royal crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 24336: // Coral bolts
				return 2;
			default:
				return 1;
			}
		case 24303: // Coral crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 24304: // Coral bolts
				return 2;
			default:
				return 1;
			}
		case 4734: // karil crossbow
		case 4934:
		case 4935:
		case 4936:
		case 4937:
			switch (ammoId) {
			case -1:
				return 3;
			case 4740: // bolt rack
				return 2;
			default:
				return 1;
			}
		case 10156: // hunters crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 10158: // Kebbit bolts
			case 10159: // Long kebbit bolts
				return 2;
			default:
				return 1;
			}
		case 8880: // Dorgeshuun c'bow
			switch (ammoId) {
			case -1:
				return 3;
			case 8882: // bone bolts
				return 2;
			default:
				return 1;
			}
		case 14684: // zanik crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9144: // rune bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
			case 8882: // bone bolts
				return 2;
			default:
				return 1;
			}
		case 767: // phoenix crossbow
		case 837: // crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
				return 2;
			default:
				return 1;
			}
		case 9174: // bronze crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9236: // Opal bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9176: // blurite crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9139: // Blurite bolts
			case 9237: // Jade bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9177: // iron crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9179: // steel crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
				return 2;
			default:
				return 1;
			}
		case 13081: // black crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9181: // Mith crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9145: // silver bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9183: // adam c bow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9185: // rune c bow
		case 15383: // armadyl crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9144: // rune bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
			case 9244: // Dragon bolts (e)
			case 9245: // Onyx bolts (e)
			case 24116: // Bakriminel bolts
			case 13280:
				return 2;
			default:
				return 1;
			}
		default:
			return 0;
		}
	}

	/**
	 * Checks if the player is wielding polypore staff.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True} if so.
	 */
	private static boolean hasPolyporeStaff(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		return weaponId == 22494 || weaponId == 22496;
	}

	private int morriganHits;

	public void processMorriganJavelins(final Player player, int hit) {
		final Entity finalTarget = target;
		if (morriganHits > 0) {
			morriganHits += hit;
			return;
		}
		morriganHits += hit;
		if (target instanceof Player) {
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					if (finalTarget.isDead() || finalTarget.hasFinished()) {
						stop();
						return;
					}
					if (morriganHits > 50) {
						morriganHits -= 50;
						finalTarget.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
					} else {
						finalTarget.applyHit(new Hit(player, morriganHits, HitLook.REGULAR_DAMAGE));
						stop();
					}
				}
			}, 4, 2);
		} else {
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					if (finalTarget.isDead() || finalTarget.hasFinished()) {
						stop();
						return;
					}
					if (morriganHits > 50) {
						morriganHits -= 50;
						finalTarget.applyHit(new Hit(player, 50, HitLook.REGULAR_DAMAGE));
					} else {
						finalTarget.applyHit(new Hit(player, morriganHits, HitLook.REGULAR_DAMAGE));
						stop();
					}
				}
			}, 4, 2);
		}
	}

	private int vineHits;

	public void processVineCall(final Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final Entity finalTarget = target;
		player.setNextAnimation(new Animation(11971));
		if (Utils.random(1) == 0 && !target.getPoison().isPoisoned()) {
			target.getPoison().makePoisoned(4);
		}
		delayNormalHit(weaponId, attackStyle,
				getMeleeHit(player, getRandomMaxHit(player, weaponId, attackStyle, false, true, 1.0, true)));
		if (vineHits > 0) {
			vineHits += 10;
			return;
		}
		vineHits += 10;
		if (target instanceof Player) {
			final Player p2 = (Player) target;
			WorldTasksManager.schedule(new WorldTask() {
				final int getX = target.getX();
				final int getY = target.getY();
				final int plane = target.getPlane();

				@Override
				public void run() {
					World.sendGraphics(null, new Graphics(478), new WorldTile(getX, getY, plane));

					if (finalTarget.isDead() || finalTarget.hasFinished()) {
						stop();
						return;
					}
					if (vineHits > 1) {
						vineHits--;
						if (target.getX() == getX && target.getY() == getY && target.getPlane() == plane) {
							finalTarget
									.applyHit(new Hit(player,
											(int) (Utils.random(1,
													p2.getSkills().getLevelForXp(Skills.STRENGTH) * 1.30)),
									HitLook.REGULAR_DAMAGE));
						}

					} else {
						if (target.getX() == getX && target.getY() == getY && target.getPlane() == plane) {
							finalTarget
									.applyHit(new Hit(player,
											(int) (Utils.random(1,
													p2.getSkills().getLevelForXp(Skills.STRENGTH) * 1.30)),
									HitLook.REGULAR_DAMAGE));
						}
						stop();
					}
				}
			}, 4, 2);
		} else {
			final int damage = (Utils.random(80, 180));
			WorldTasksManager.schedule(new WorldTask() {
				final int getX = target.getX();
				final int getY = target.getY();
				final int plane = target.getPlane();

				@Override
				public void run() {
					World.sendGraphics(null, new Graphics(478), new WorldTile(getX, getY, plane));

					if (finalTarget.isDead() || finalTarget.hasFinished()) {
						stop();
						return;
					}
					if (vineHits > 1) {
						vineHits -= 1;
						if (target.getX() == getX && target.getY() == getY && target.getPlane() == plane) {
							finalTarget.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
						}

					} else {
						if (target.getX() == getX && target.getY() == getY && target.getPlane() == plane) {
							finalTarget.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
						}

						stop();
					}
				}
			}, 4, 2);
		}
	}

	public Entity getTarget() {
		return target;
	}
}