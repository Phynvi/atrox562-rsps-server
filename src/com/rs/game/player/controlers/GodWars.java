package com.rs.game.player.controlers;

import java.util.concurrent.TimeUnit;

import com.rs.game.WorldObject;
import com.rs.game.Animation;
import com.rs.cores.CoresManager;
import com.rs.game.ForceMovement;
import com.rs.game.item.Item;
import com.rs.game.npc.godwars.armadyl.GodwarsArmadylFaction;
import com.rs.game.npc.godwars.bandos.GodwarsBandosFaction;
import com.rs.game.npc.godwars.zammorak.GodwarsZammorakFaction;
import com.rs.game.npc.godwars.saradomin.GodwarsSaradominFaction;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.Magic;
import com.rs.game.tasks.WorldTask;
import com.rs.utils.Utils;
import com.rs.game.World;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.WorldTile;

public class GodWars extends Controler {

	@Override
	public void start() {
		sendOverlay(601);
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean login() {
		if (player.getX() >= 2917 && player.getX() <= 2938 && player.getY() >= 5316 && player.getY() <= 5331
				&& player.getPlane() == 2)
			sendOverlay(598);
		else if (player.getX() >= 2880 && player.getX() <= 2943 && player.getY() >= 5334 && player.getY() <= 5375
				&& player.getPlane() == 2
				|| player.getX() >= 2923 && player.getX() <= 2927 && player.getY() >= 5332 && player.getY() <= 5333
						&& player.getPlane() == 2)
			sendOverlay(599);
		else
			sendOverlay(601);
		return false;
	}
	
	public static void moveBoulder(final Player player,
			final WorldObject object) {
		if (player.getSkills().getLevelForXp(Skills.STRENGTH) < 60) {
			player.sendMessage("You're too weak to move this.");
			return;
		}
		player.lock(3);
		player.setNextAnimation(new Animation(4344));
		WorldObject movedBoulder = new WorldObject(26339, object.getType(), object.getRotation(), object.getX() , object.getY(), object.getPlane());
		final WorldTile toTile = new WorldTile(player.getX(), player.getY() + 4, object.getPlane());
		final WorldTile backTile = new WorldTile(player.getX(), player.getY() - 4, object.getPlane());
		World.spawnTemporaryObject(movedBoulder, 3700, false);
		if (player.getY() <= object.getY())
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 6, ForceMovement.EAST));
		else
		player.setNextForceMovement(new ForceMovement(player, 1, backTile, 6, ForceMovement.EAST));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				if (player.getY() <= object.getY()) {
				player.setNextWorldTile(toTile);
			} else {
				player.setNextWorldTile(backTile);
			}
			}

		}, 3);
	}

	public static boolean handleObjects(Player player, WorldObject object) {
		if (object.getId() == 26338) {
			moveBoulder(player, object);
			return true;
		}
		if (object.getId() == 26342) {
			if (!player.GWDRope1) {
				if (player.getInventory().containsItem(new Item(954, 1))) {
					player.getInventory().deleteItem(new Item(954, 1));
					player.GWDRope1 = true;
					player.getPackets().sendConfigByFile(3932, 1);
					player.sendMessage("You tie a rope to the rock.");
				} else
					player.sendMessage("You need a rope to tie to the rock.");
			} else {
				CoresManager.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						player.getControlerManager().startControler("GodWars");
					}
				}, 700, TimeUnit.MILLISECONDS);
				player.sendMessage("You climb down the hole..");
				player.useStairs(827, new WorldTile(2881, 5310, 2), 1, 1);
			}
			return true;
		}
		return false;
	}

	public void sendOverlay(int id) {
		sendKC(id);
		player.getInterfaceManager().sendGWDOverlay(id,
				player.getInterfaceManager().hasRezizableScreen() ? true : false);

	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 26293) {
			player.useStairs(828, new WorldTile(2916, 3746, 0), 1, 1);
			player.getControlerManager().forceStop();
			player.sendMessage("You climb up the rope.");
			return true;
		} else if (object.getId() == 26384) {
			if (player.getX() >= 2851) {
				player.setNextFaceWorldTile(new WorldTile(2850, 5333, 2));
				if (player.getInventory().containsItem(2347, 1)
						&& player.getSkills().getLevelForXp(Skills.STRENGTH) >= 70) {
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (loop == 0) {
								player.sendMessage("You bang the door with a hammer.");
								player.setNextAnimation(new Animation(11159));
							} else if (loop == 2) {
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 1, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextForceMovement(
										new ForceMovement(new WorldTile(2850, 5333, 2), 2, ForceMovement.WEST));
								player.setNextAnimation(new Animation(6395));
							} else if (loop == 3) {
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 0, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextWorldTile(new WorldTile(2850, 5333, 2));
								player.unlock();
								this.stop();
							}
							loop++;
						}
					}, 0, 1);
				} else {
					if (!player.getInventory().containsItem(2347, 1)
							&& player.getSkills().getLevelForXp(Skills.STRENGTH) < 70)
						player.sendMessage("You need a hammer and at least level 70 Strength to bang the door.");
					else if (!player.getInventory().containsItem(2347, 1))
						player.sendMessage("You need a hammer to bang the door.");
					else
						player.sendMessage("You need at least level 70 Strength to bang the door.");
				}
			} else {
				player.lock();
				player.sendMessage("You leave the Bandos stronghold..");
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							World.spawnObject(new WorldObject(object.getId(), object.getType(), 1, object.getX(),
									object.getY(), object.getPlane()), true);
							player.setNextForceMovement(
									new ForceMovement(new WorldTile(2851, 5333, 2), 2, ForceMovement.EAST));
							player.setNextAnimation(new Animation(6395));
						} else if (loop == 1) {
							World.spawnObject(new WorldObject(object.getId(), object.getType(), 0, object.getX(),
									object.getY(), object.getPlane()), true);
							player.setNextWorldTile(new WorldTile(2851, 5333, 2));
							player.unlock();
							this.stop();
						}
						loop++;
					}
				}, 0, 1);
			}
			return true;
		} else if (object.getId() == 26439) {
			if (player.getY() < 5335) {
				player.lock();
				player.setNextAnimation(new Animation(9727));
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							player.setNextWorldTile(new WorldTile(2885, 5334, 2));
							player.setNextAnimation(new Animation(-1));
							player.getAppearence().setRenderEmote(846);
						} else if (loop == 1) {
							player.getInterfaceManager().sendGWDOverlay(115,
									player.getInterfaceManager().hasRezizableScreen() ? true : false);
						} else if (loop == 3) {
							player.getAppearence().setRenderEmote(-1);
							player.setNextWorldTile(new WorldTile(2885, 5345, 2));
						} else if (loop == 4) {
							player.getInterfaceManager().sendGWDOverlay(170,
									player.getInterfaceManager().hasRezizableScreen() ? true : false);
						} else if (loop == 5) {
							player.getSkills().drainPrayer(player.getSkills().getLevel(Skills.PRAYER));
							player.sendMessage("The extreme evil of this place leaves your Prayer drained.");
							sendOverlay(599);
							moved();
							player.unlock();
							this.stop();
						}
						loop++;
					}
				}, 0, 1);
			} else {
				player.lock();
				player.setNextAnimation(new Animation(9727));
				WorldTasksManager.schedule(new WorldTask() {
					int loop;

					@Override
					public void run() {
						if (loop == 0) {
							player.setNextWorldTile(new WorldTile(2885, 5343, 2));
							player.setNextAnimation(new Animation(-1));
							player.getAppearence().setRenderEmote(846);
						} else if (loop == 1) {
							player.getInterfaceManager().sendGWDOverlay(115,
									player.getInterfaceManager().hasRezizableScreen() ? true : false);
						} else if (loop == 3) {
							player.getAppearence().setRenderEmote(-1);
							player.setNextWorldTile(new WorldTile(2885, 5332, 2));
						} else if (loop == 4) {
							player.getInterfaceManager().sendGWDOverlay(170,
									player.getInterfaceManager().hasRezizableScreen() ? true : false);
						} else if (loop == 5) {
							player.getSkills().drainPrayer(player.getSkills().getLevel(Skills.PRAYER));
							player.sendMessage("The extreme evil of this place leaves your Prayer drained.");
							sendOverlay(601);
							moved();
							player.unlock();
							this.stop();
						}
						loop++;
					}
				}, 0, 1);
			}
			return true;
		} else if (object.getId() == 26303) {
			boolean hasCrossbow = false;
			for (Item item : player.getInventory().getItems().getItems()) {
				if (item == null)
					continue;
				if (item.getName().contains("crossbow"))
					hasCrossbow = true;
			}
			if (player.getEquipment().getItem(3).getName().contains("crossbow"))
				hasCrossbow = true;
			if (!hasCrossbow && !player.getInventory().containsItem(new Item(9418, 1))
					&& player.getSkills().getLevelForXp(Skills.RANGE) < 70) {
				player.sendMessage("You need at least level 70 ranged as well as a crossbow and a mithril grapple");
				player.sendMessage("to cross the deep chasm.");
				return true;
			} else if (!hasCrossbow && !player.getInventory().containsItem(new Item(9418, 1))) {
				player.sendMessage("You need a crossbow and a mithril grapple to cross the deep chasm.");
				return true;
			} else if (!hasCrossbow && player.getSkills().getLevelForXp(Skills.RANGE) < 70) {
				player.sendMessage("You need at least level 70 ranged as well as a crossbow to cross the deep chasm.");
				return true;
			} else if (!player.getInventory().containsItem(new Item(9418, 1))
					&& player.getSkills().getLevelForXp(Skills.RANGE) < 70) {
				player.sendMessage(
						"You need at least level 70 ranged as well as a mithril grapple to cross the deep chasm.");
				return true;
			} else if (!player.getInventory().containsItem(new Item(9418, 1))) {
				player.sendMessage("You need a mithril grapple to cross the deep chasm.");
				return true;
			} else if (!hasCrossbow) {
				player.sendMessage("You need a crossbow to cross the deep chasm.");
				return true;
			} else if (player.getSkills().getLevelForXp(Skills.RANGE) < 70) {
				player.sendMessage("You need at least level 70 Ranged to cross the deep chasm.");
				return true;
			}
			player.lock();
			player.sendMessage("You grapple the deep chasm..");
			WorldTasksManager.schedule(new WorldTask() {
				int loop;

				@Override
				public void run() {
					if (loop == 0) {
						player.getInterfaceManager().sendGWDOverlay(115,
								player.getInterfaceManager().hasRezizableScreen() ? true : false);
					} else if (loop == 2) {
						if (player.getY() == 5279)
							player.setNextWorldTile(new WorldTile(2872, 5269, 2));
						else
							player.setNextWorldTile(new WorldTile(2872, 5279, 2));
					} else if (loop == 3) {
						player.getInterfaceManager().sendGWDOverlay(170,
								player.getInterfaceManager().hasRezizableScreen() ? true : false);
					} else if (loop == 4) {
						moved();
						player.unlock();
						this.stop();
					}
					loop++;
				}
			}, 0, 1);
		} else if (object.getId() == 26426) {
			if (player.getY() <= 5295) {
				if (player.getArmadylKC() >= 15) {
					player.setArmadylKC(player.getArmadylKC() - 15);
					sendKC(601);
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (loop == 0) {
								player.sendMessage("You enter the Armadyl's Eyrie.");
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 0, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextForceMovement(
										new ForceMovement(new WorldTile(2839, 5296, 2), 2, ForceMovement.NORTH));
								player.setNextAnimation(new Animation(6395));
							} else if (loop == 1) {
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 1, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextWorldTile(new WorldTile(2839, 5296, 2));
								player.unlock();
								this.stop();
							}
							loop++;
						}
					}, 0, 1);
				} else
					player.sendMessage("You need to slay at least 15 followers of Armadyl to enter Armadyl's Eyrie.");
			} else
				player.sendMessage("The door is locked.");
			return true;
		} else if (object.getId() == 26425) {
			if (player.getX() <= 2863) {
				if (player.getBandosKC() >= 15) {
					player.setBandosKC(player.getBandosKC() - 15);
					sendKC(601);
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (loop == 0) {
								player.sendMessage("You enter the Bandos' Stronghold.");
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 1, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextForceMovement(
										new ForceMovement(new WorldTile(2864, 5354, 2), 2, ForceMovement.EAST));
								player.setNextAnimation(new Animation(6395));
							} else if (loop == 1) {
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 2, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextWorldTile(new WorldTile(2864, 5354, 2));
								player.unlock();
								this.stop();
							}
							loop++;
						}
					}, 0, 1);
				} else
					player.sendMessage("You need to slay at least 15 followers of Bandos to enter Bandos' Stronghold.");
			} else
				player.sendMessage("The door is locked.");
			return true;
		} else if (object.getId() == 26428) {
			if (player.getY() >= 5332) {
				if (player.getZamorakKC() >= 15) {
					player.setZamorakKC(player.getZamorakKC() - 15);
					sendKC(598);
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (loop == 0) {
								player.sendMessage("You enter the Zamorak's Fortress.");
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 2, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextForceMovement(
										new ForceMovement(new WorldTile(2925, 5331, 2), 2, ForceMovement.SOUTH));
								player.setNextAnimation(new Animation(6395));
							} else if (loop == 1) {
								sendOverlay(598);
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 3, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextWorldTile(new WorldTile(2925, 5331, 2));
								player.unlock();
								this.stop();
							}
							loop++;
						}
					}, 0, 1);
				} else
					player.sendMessage(
							"You need to slay at least 15 followers of Zamorak to enter Zamorak's Fortress.");
			} else
				player.sendMessage("The door is locked.");
			return true;
		} else if (object.getId() == 26427) {
			if (player.getX() >= 2908) {
				if (player.getSaradominKC() >= 15) {
					player.setSaradominKC(player.getSaradominKC() - 15);
					sendKC(601);
					player.lock();
					WorldTasksManager.schedule(new WorldTask() {
						int loop;

						@Override
						public void run() {
							if (loop == 0) {
								player.sendMessage("You enter the Saradomin's Encampment.");
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 1, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextForceMovement(
										new ForceMovement(new WorldTile(2907, 5265, 0), 2, ForceMovement.WEST));
								player.setNextAnimation(new Animation(6395));
							} else if (loop == 1) {
								World.spawnObject(new WorldObject(object.getId(), object.getType(), 0, object.getX(),
										object.getY(), object.getPlane()), true);
								player.setNextWorldTile(new WorldTile(2907, 5265, 0));
								player.unlock();
								this.stop();
							}
							loop++;
						}
					}, 0, 1);
				} else {
					player.sendMessage("You need to slay at least 15 followers of Saradomin to enter Saradomin's");
				}
			} else
				player.sendMessage("The door is locked.");
			return true;
		} else if (object.getId() == 26298) {
			if (player.getSkills().getLevelForXp(Skills.AGILITY) < 70) {
				player.sendMessage("You need at least level 70 Agility to climb up the rope.");
				return true;
			}
			player.useStairs(828, new WorldTile(2920, 5276, 1), 1, 1);
			player.sendMessage("You climb up the rope.");
			return true;
		} else if (object.getId() == 26294) {
			if (player.getSkills().getLevelForXp(Skills.AGILITY) < 70) {
				player.sendMessage("You need at least level 70 Agility to climb up the rope.");
				return true;
			}
			player.useStairs(828, new WorldTile(2912, 5300, 2), 1, 1);
			player.sendMessage("You climb up the rope.");
			return true;
		} else if (object.getId() == 26287 || object.getId() == 26286 || object.getId() == 26289
				|| object.getId() == 26288) {
			if (player.getLastGWDPray() < Utils.currentTimeMillis()) {
				if (player.getSkills().getLevel(Skills.PRAYER) >= player.getSkills().getLevelForXp(Skills.PRAYER)) {
					player.getPackets().sendGameMessage("You already have full prayer points.");
					return true;
				}
				int points = player.getSkills().getLevelForXp(Skills.PRAYER);
				if (object.getId() == 26287 && GodwarsSaradominFaction.hasGodItem(player)
						|| object.getId() == 26286 && GodwarsZammorakFaction.hasGodItem(player)
						|| object.getId() == 26289 && GodwarsBandosFaction.hasGodItem(player)
						|| object.getId() == 26288 && GodwarsArmadylFaction.hasGodItem(player))
					points += points * 0.1;
				player.getSkills().set(Skills.PRAYER, points);
				player.setLastGWDPray(Utils.currentTimeMillis() + (600 * 1000));
				player.lock(5);
				player.getPackets().sendGameMessage("You pray to the gods...", true);
				player.setNextAnimation(new Animation(645));
			} else {
				player.sendMessage("The gods forbid you from praying.");
				player.sendMessage(
						"You may use this altar again in " + Utils.getTimeLeft(player.getLastGWDPray()) + "");
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		if (object.getId() == 26444) {
			if (!player.GWDRope2Saradomin) {
				if (player.getInventory().containsItem(new Item(954, 1))) {
					player.getInventory().deleteItem(new Item(954, 1));
					player.GWDRope2Saradomin = true;
					player.sendMessage("You tie a rope to the rock.");
					player.getPackets().sendConfigByFile(3933, 1);
				} else
					player.sendMessage("You need a rope to tie to the rock.");
			} else {
				if (player.getSkills().getLevelForXp(Skills.AGILITY) < 70) {
					player.sendMessage("You need at least level 70 Agility to climb down the rope.");
					return true;
				}
				player.sendMessage("You climb down the rock.");
				player.useStairs(827, new WorldTile(2914, 5300, 1), 1, 1);
			}
			return true;
		} else if (object.getId() == 26445) {
			if (!player.GWDRope3Saradomin) {
				if (player.getInventory().containsItem(new Item(954, 1))) {
					player.getInventory().deleteItem(new Item(954, 1));
					player.GWDRope3Saradomin = true;
					player.sendMessage("You tie a rope to the rock.");
					player.getPackets().sendConfigByFile(3934, 1);
				} else
					player.sendMessage("You need a rope to tie to the rock.");
			} else {
				if (player.getSkills().getLevelForXp(Skills.AGILITY) < 70) {
					player.sendMessage("You need at least level 70 Agility to climb down the rope.");
					return true;
				}
				player.sendMessage("You climb down the rock.");
				player.useStairs(827, new WorldTile(2920, 5274, 0), 1, 1);
			}
			return true;
		} else if (object.getId() == 26287 || object.getId() == 26286 || object.getId() == 26289
				|| object.getId() == 26288) {
			Magic.sendNormalTeleportSpell(player, 1, 0, new WorldTile(2914, 3747, 0));
			player.getControlerManager().forceStop();
			return true;
		}
		return false;
	}

	@Override
	public boolean sendDeath() {
		remove();
		removeControler();
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		remove();
		removeControler();
	}

	private void sendKC(int currentInter) {
		player.getPackets().sendConfigByFile(3939, player.getArmadylKC());
		player.getPackets().sendConfigByFile(3941, player.getBandosKC());
		player.getPackets().sendConfigByFile(3938, player.getSaradominKC());
		player.getPackets().sendConfigByFile(3942, player.getZamorakKC());
	}

	@Override
	public void forceClose() {
		remove();
	}

	public void remove() {
		player.getInterfaceManager().closeGWDOverlay(player.getInterfaceManager().hasRezizableScreen() ? true : false);
		player.setArmadylKC(0);
		player.setBandosKC(0);
		player.setZamorakKC(0);
		player.setSaradominKC(0);
	}

}