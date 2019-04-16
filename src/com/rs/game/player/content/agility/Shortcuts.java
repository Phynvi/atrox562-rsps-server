package com.rs.game.player.content.agility;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.WorldTile;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.WorldObject;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public final class Shortcuts {

	public static boolean Use(Player player, WorldObject object) {
		
		/**
		 * Slayer tower
		 */
		if (object.getId() == 9319) {
			 if (player.getSkills().getLevel(Skills.AGILITY) < 61) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 61 Agility to climb Spikey chain.");
				return false;
			}
		player.useStairs(828, new WorldTile(player.getX(), player.getY(), 1), 2, 1, "You climb up the spikey chain.");
		}
		
		if (object.getId() == 9320) {
			 if (player.getSkills().getLevel(Skills.AGILITY) < 61) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 61 Agility to climb Spikey chain.");
				return false;
			}
		player.useStairs(828, new WorldTile(player.getX(), player.getY(), 0), 2, 1, "You climb down the spikey chain.");
		}
		
		/**
		 * Edgeville dungeon
		 */
		 
		 if (object.getId() == 29370) {
			 final boolean running = player.getRun();
			 if (player.getSkills().getLevel(Skills.AGILITY) < 51) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 51 Agility to Squeeze through.");
				return false;
			}
			player.lock();
			if (player.getX() > 3152) {
				player.setRunHidden(false);
				player.getAppearence().setRenderEmote(295);
				player.setNextForceMovement(new ForceMovement(player, 0, new WorldTile(3149, 9906, 0), 2, ForceMovement.WEST));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
						player.getAppearence().setRenderEmote(-1);
						player.setRunHidden(running);
						player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
						player.setNextWorldTile(new WorldTile(3149, 9906, 0));
					}
				}, 1);
			} else if (player.getX() < 3152) {
				if (player.getSkills().getLevel(Skills.AGILITY) < 51) {
				player.getDialogueManager().startDialogue("SimpleMessage", "You need at least level 51 Agility to Squeeze through.");
				return false;
			}
				player.setRunHidden(false);
				player.getAppearence().setRenderEmote(295);
				player.setNextForceMovement(new ForceMovement(player, 0, new WorldTile(3155, 9906, 0), 2, ForceMovement.EAST));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
						player.getAppearence().setRenderEmote(-1);
						player.setRunHidden(running);
						player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
						player.setNextWorldTile(new WorldTile(3155, 9906, 0));
					}
				}, 1);
			}
			return true;
		}

		/**
		 * Lumbridge Stile @Fred's farm
		 */
		if (object.getId() == 33842) {
			player.lock();
			if (player.getY() < 3277) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(3197, 3278, 0), 2, ForceMovement.NORTH));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
						player.sendMessage("You climb over the stile.");
						player.setNextWorldTile(new WorldTile(3197, 3278, 0));
					}
				}, 1);
			} else if (player.getY() > 3277) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(3197, 3275, 0), 2, ForceMovement.SOUTH));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
						player.sendMessage("You climb over the stile.");
						player.setNextWorldTile(new WorldTile(3197, 3275, 0));
					}
				}, 1);
			}
			return true;
		}
		/**
		 * Falconry stile
		 */
		if (object.getId() == 19222) {
			player.lock();
			if (player.getY() < 3621) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(2371, 3622, 0), 2, ForceMovement.NORTH));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
						player.sendMessage("You climb over the stile.");
						player.setNextWorldTile(new WorldTile(2371, 3622, 0));
						if (player.getControlerManager().getControler() != null)
							player.getControlerManager().getControler().forceClose();
					}
				}, 1);
			} else if (player.getY() >= 3621) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(2371, 3619, 0), 2, ForceMovement.SOUTH));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.unlock();
						player.sendMessage("You climb over the stile.");
						player.setNextWorldTile(new WorldTile(2371, 3619, 0));
						player.getControlerManager().startControler("FalconryControler");
					}
				}, 1);
			}
			return true;
		}
		/**
		 * Hunter pitfall trap
		 */
		if (object.getId() >= 19259 && object.getId() <= 19268 || object.getId() >= 19253 && object.getId() <= 19255) {
			player.faceObject(object);
			if (player.getX() > object.getX() && object.getId() != 19260 && object.getId() != 19264
					&& object.getId() != 19254 && object.getId() != 19255) {
				player.setNextAnimation(new Animation(3067));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(player.getX() - 3, player.getY(), 0), 2, 3));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {

						player.lock(1);
						player.setNextWorldTile(new WorldTile(player.getX() - 3, player.getY(), 0));
					}
				}, 1);
			} else if (player.getX() < object.getX()) {
				player.setNextAnimation(new Animation(3067));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(player.getX() + 3, player.getY(), 0), 2, 1));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.setNextWorldTile(new WorldTile(player.getX() + 3, player.getY(), 0));
					}
				}, 1);
			} else if (player.getY() < object.getY()) {
				player.setNextAnimation(new Animation(3067));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(player.getX(), player.getY() + 3, 0), 2, 0));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 3, 0));
					}
				}, 1);
			} else if (player.getY() > object.getY()) {
				player.setNextAnimation(new Animation(3067));
				player.setNextForceMovement(
						new ForceMovement(player, 0, new WorldTile(player.getX(), player.getY() - 3, 0), 2, 2));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.setNextWorldTile(new WorldTile(player.getX(), player.getY() - 3, 0));
					}
				}, 1);
			}
			return true;
		}
		/**
		 * Falador crumbling wall
		 */
		else if (object.getId() == 11844) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 5) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need at least level 5 Agility to climb over this crumbling wall.");
				return false;
			}
			if (player.getX() == 2936) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(new ForceMovement(player, 0, new WorldTile(2935, 3355, 0), 2, 3));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.sendMessage("You climb over the crumbling wall.");
						player.setNextWorldTile(new WorldTile(2935, 3355, 0));
					}
				}, 1);
			} else if (player.getX() == 2934) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(new ForceMovement(player, 0, new WorldTile(2936, 3355, 0), 2, 1));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.sendMessage("You climb over the crumbling wall.");
						player.setNextWorldTile(new WorldTile(2936, 3355, 0));
					}
				}, 1);
			}
			return true;
		}

		/**
		 * Falador Underwall Tunnel
		 */
		if (object.getId() == 9310 || object.getId() == 9309) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 26) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need at least level 26 Agility to climb under this underwall tunnel");
				return false;
			}
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				int id = object.getId();

				@Override
				public void run() {
					if (ticks == 0)
						player.setNextWorldTile(new WorldTile(object.getX(), object.getY(), object.getPlane()));
					boolean inFalador = id == 9310;
					WorldTile tile = inFalador ? new WorldTile(2948, 3310, 0) : new WorldTile(2948, 3312, 0);
					player.lock();
					ticks++;
					if (ticks == 1) {
						player.setNextAnimation(new Animation(2589));
						player.setNextForceMovement(
								new ForceMovement(object, 1, inFalador ? ForceMovement.SOUTH : ForceMovement.NORTH));
					} else if (ticks == 3) {
						player.setNextWorldTile(new WorldTile(2948, 3311, 0));
						player.setNextAnimation(new Animation(2590));
					} else if (ticks == 5) {
						player.setNextAnimation(new Animation(2591));
						player.setNextWorldTile(tile);
					} else if (ticks == 6) {
						player.setNextWorldTile(
								new WorldTile(tile.getX() + (inFalador ? -1 : 1), tile.getY(), tile.getPlane()));
						player.unlock();
						stop();
					}
				}
			}, 0, 0);
			return true;
		}

		/**
		 * River lum grapple
		 */

		/**
		 * Moss giant island rope shortcut
		 */

		/**
		 * Scale Falador north wall grapple
		 */

		/**
		 * Karamja dungeon stepping stones
		 */

		/**
		 * Varrock south fence jump
		 */
		else if (object.getId() == 9300) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 13) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need at least level 13 Agility to climb over this crumbling wall.");
				return false;
			}
			if (player.getY() == 3331) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(new ForceMovement(player, 0, new WorldTile(3230, 3330, 0), 2, 2));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.sendMessage("You climb over the fence.");
						player.setNextWorldTile(new WorldTile(3230, 3330, 0));
					}
				}, 1);
			} else if (player.getY() == 3330) {
				player.setNextAnimation(new Animation(839));
				player.setNextForceMovement(new ForceMovement(player, 0, new WorldTile(3230, 3331, 0), 2, 0));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.lock(1);
						player.sendMessage("You climb over the fence.");
						player.setNextWorldTile(new WorldTile(3230, 3331, 0));
					}
				}, 1);
			}
			return true;
		}
		/**
		 * Edgeville dungeon monkey bar shortcut
		 */
		else if (object.getId() == 29375) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 15) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need at least level 15 Agility to jump across these monkey bars.");
				return false;
			}
			if (player.getX() < 3120)
				player.setNextWorldTile(new WorldTile(3120, player.getY(), player.getPlane()));
			else if (player.getX() > 3121)
				player.setNextWorldTile(new WorldTile(3121, player.getY(), player.getPlane()));
			player.sendMessage("You climb the monkey bars.");
			player.setNextAnimation(new Animation(742));
			final WorldTile toTile;
			if (object.getY() == 9964)
				toTile = new WorldTile(player.getX(), player.getY() + 1, player.getPlane());
			else
				toTile = new WorldTile(player.getX(), player.getY() - 1, player.getPlane());
			if (object.getY() == 9964)
				player.setNextForceMovement(new ForceMovement(player, 1,
						new WorldTile(player.getX(), player.getY() + 1, player.getPlane()), 0, ForceMovement.NORTH));
			else
				player.setNextForceMovement(new ForceMovement(player, 1,
						new WorldTile(player.getX(), player.getY() - 1, player.getPlane()), 0, ForceMovement.SOUTH));
			player.setNextWorldTile(toTile);
			WorldTasksManager.schedule(new WorldTask() {
				int x;

				@Override
				public void run() {
					final WorldTile toTile;
					if (object.getY() == 9964)
						toTile = new WorldTile(player.getX(), player.getY() + 1, player.getPlane());
					else
						toTile = new WorldTile(player.getX(), player.getY() - 1, player.getPlane());
					if (x++ == 6) {
						stop();
						player.getSkills().addXp(Skills.AGILITY, 20);
						player.setNextAnimation(new Animation(743));
						player.setNextWorldTile(new WorldTile(player.getX(), (object.getY() == 9964 ? 993 : 9964), 0));
						player.getAppearence().setRenderEmote(-1);
						player.sendMessage("..and make it across safely.");
						return;
					}
					if (x > 0) {
						player.setNextAnimation(new Animation(744));
						player.getAppearence().setRenderEmote(662);
						player.setNextForceMovement(new ForceMovement(player, 1, toTile, 6,
								(object.getY() == 9964 ? ForceMovement.NORTH : ForceMovement.SOUTH)));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								player.setNextWorldTile(toTile);

							}
						}, 0);
					}
				}

			}, 1, 1);
			return true;
		}
		/**
		 * Yanille agility shortcut
		 */
		if (object.getId() == 9301 || object.getId() == 9302) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 16) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need at least level 16 Agility to climb under this underwall tunnel");
				return false;
			}
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				int id = object.getId();

				@Override
				public void run() {
					if (ticks == 0)
						player.setNextWorldTile(new WorldTile(object.getX(), object.getY(), object.getPlane()));
					boolean inYanille = id == 9301;
					WorldTile tile = inYanille ? new WorldTile(2575, 3111, 0) : new WorldTile(2575, 3108, 0);
					player.lock();
					ticks++;
					if (ticks == 1) {
						player.setNextAnimation(new Animation(2589));
						player.setNextForceMovement(
								new ForceMovement(object, 1, inYanille ? ForceMovement.SOUTH : ForceMovement.NORTH));
					} else if (ticks == 3) {
						player.setNextWorldTile(new WorldTile(2575, 3109, 0));
						player.setNextAnimation(new Animation(2590));
					} else if (ticks == 5) {
						player.setNextAnimation(new Animation(2591));
						player.setNextWorldTile(tile);
					} else if (ticks == 6) {
						player.setNextWorldTile(
								new WorldTile(tile.getX() + (inYanille ? -1 : 1), tile.getY(), tile.getPlane()));
						player.unlock();
						stop();
					}
				}
			}, 0, 0);
			return true;
		}

		/**
		 * GE agility shortcut
		 */

		if (object.getId() == 9311 || object.getId() == 9312) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 21) {
				player.getDialogueManager().startDialogue("SimpleMessage",
						"You need at least level 21 Agility to use this shortcut.");
				return false;
			}
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				int id = object.getId();

				@Override
				public void run() {
					if (ticks == 0)
						player.setNextWorldTile(new WorldTile(object.getX(), object.getY(), object.getPlane()));
					boolean withinGE = id == 9312;
					WorldTile tile = withinGE ? new WorldTile(3139, 3516, 0) : new WorldTile(3143, 3514, 0);
					player.lock();
					ticks++;
					if (ticks == 1) {
						player.setNextAnimation(new Animation(2589));
						player.setNextForceMovement(
								new ForceMovement(object, 1, withinGE ? ForceMovement.WEST : ForceMovement.EAST));
					} else if (ticks == 3) {
						player.setNextWorldTile(new WorldTile(3141, 3515, 0));
						player.setNextAnimation(new Animation(2590));
					} else if (ticks == 5) {
						player.setNextAnimation(new Animation(2591));
						player.setNextWorldTile(tile);
					} else if (ticks == 6) {
						player.setNextWorldTile(
								new WorldTile(tile.getX() + (withinGE ? -1 : 1), tile.getY(), tile.getPlane()));
						player.unlock();
						stop();
					}
				}
			}, 0, 0);
			return true;
		}

		/**
		 * Frem
		 */
		if (object.getId() == 44339) {
			if (player.getSkills().getLevelForXp(Skills.AGILITY) >= 81) {
				if (player.getX() >= 2775)
					player.setNextWorldTile(new WorldTile(player.getX() - 7, player.getY(), player.getPlane()));
				else
					player.setNextWorldTile(new WorldTile(player.getX() + 7, player.getY(), player.getPlane()));
			} else
				player.sendMessage("You need at least a level of 81 agility to use this shortcut.");
			return false;
		}
		if (object.getId() == 77052) {
			if (player.getSkills().getLevelForXp(Skills.AGILITY) >= 62) {
				if (player.getX() <= 2730)
					player.setNextWorldTile(new WorldTile(player.getX() + 5, player.getY(), player.getPlane()));
				else
					player.setNextWorldTile(new WorldTile(player.getX() - 5, player.getY(), player.getPlane()));
			} else
				player.sendMessage("You need at least a level of 62 agility to use this shortcut.");
			return false;
		}

		/*
		 * Agility Shortcuts Author @Silvernova
		 */

		if (object.getId() == 47237) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 90) {
				player.getPackets().sendGameMessage("You need 90 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 1641 && player.getY() == 5260 || player.getX() == 1641 && player.getY() == 5259
					|| player.getX() == 1640 && player.getY() == 5259) {
				player.setNextWorldTile(new WorldTile(1641, 5268, 0));
			} else {
				player.setNextWorldTile(new WorldTile(1641, 5260, 0));
			}
		}
		if (object.getId() == 47233) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 86) {
				player.getPackets().sendGameMessage("You need 86 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 1633 && player.getY() == 5292 || player.getX() == 1633 && player.getY() == 5291) {
				player.setNextWorldTile(new WorldTile(1633, 5294, 0));
			} else {
				player.setNextWorldTile(new WorldTile(1633, 5292, 0));
			}
		}
		if (object.getId() == 10536) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 74) {
				player.getPackets().sendGameMessage("You need 74 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2860 && player.getY() == 2971 || player.getX() == 2861 && player.getY() == 2971
					|| player.getX() == 2859 && player.getY() == 2971) {
				player.setNextWorldTile(new WorldTile(2860, 2977, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2860, 2971, 0));
			}
		}
		if (object.getId() == 3803) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 43) {
				player.getPackets().sendGameMessage("You need 43 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2879 && player.getY() == 3673 || player.getX() == 2879 && player.getY() == 3672
					|| player.getX() == 2879 && player.getY() == 3671) {
				player.setNextWorldTile(new WorldTile(2875, 3672, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2879, 3673, 0));
			}
		}
		if (object.getId() == 9304) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 43) {
				player.getPackets().sendGameMessage("You need 43 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2874 && player.getY() == 3663 || player.getX() == 2875 && player.getY() == 3663
					|| player.getX() == 2876 && player.getY() == 3663) {
				player.setNextWorldTile(new WorldTile(2874, 3659, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2874, 3663, 0));
			}
		}
		if (object.getId() == 9303) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 41) {
				player.getPackets().sendGameMessage("You need 41 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2858 && player.getY() == 3663 || player.getX() == 2858 && player.getY() == 3664) {
				player.setNextWorldTile(new WorldTile(2854, 3665, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2858, 3663, 0));
			}
		}
		if (object.getId() == 9306) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 41) {
				player.getPackets().sendGameMessage("You need 41 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2908 && player.getY() == 3687 || player.getX() == 2908 && player.getY() == 3686
					|| player.getX() == 2908 && player.getY() == 3685) {
				player.setNextWorldTile(new WorldTile(2912, 3687, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2908, 3687, 0));
			}
		}
		if (object.getId() == 9306) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 13) {
				player.getPackets().sendGameMessage("You need 13 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2920 && player.getY() == 3676) {
				player.setNextWorldTile(new WorldTile(2920, 3674, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2920, 3676, 0));
			}
		}
		if (object.getId() == 34889) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 64) {
				player.getPackets().sendGameMessage("You need 64 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2930 && player.getY() == 3682 || player.getX() == 2930 && player.getY() == 3681
					|| player.getX() == 2930 && player.getY() == 3680) {
				player.setNextWorldTile(new WorldTile(2934, 3681, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2930, 3682, 0));
			}
		}
		if (object.getId() == 34878) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 64) {
				player.getPackets().sendGameMessage("You need 64 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2950 && player.getY() == 3683 || player.getX() == 2950 && player.getY() == 3682) {
				player.setNextWorldTile(new WorldTile(2954, 3682, 0));
			} else {
				player.setNextWorldTile(new WorldTile(2950, 3683, 0));
			}
		}
		if (object.getId() == 26327) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 60) {
				player.getPackets().sendGameMessage("You need 60 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2950 && player.getY() == 3767 || player.getX() == 2950 && player.getY() == 3768) {
				player.setNextWorldTile(new WorldTile(2942, 3768, 0));
			}
		}
		if (object.getId() == 26328) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 60) {
				player.getPackets().sendGameMessage("You need 60 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2942 && player.getY() == 3768) {
				player.setNextWorldTile(new WorldTile(2950, 3767, 0));
			}
		}
		if (object.getId() == 29099) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 29) {
				player.getPackets().sendGameMessage("You need 29 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 2596 && player.getY() == 2869) {
				player.setNextAnimation(new Animation(844));
				player.setNextWorldTile(new WorldTile(2596, 2871, 0));
			} else {
				player.setNextAnimation(new Animation(844));
				player.setNextWorldTile(new WorldTile(2596, 2869, 0));
			}
		}

		if (object.getId() == 37703) {
			if (player.getSkills().getLevel(Skills.AGILITY) < 28) {
				player.getPackets().sendGameMessage("You need 28 agility to use this shortcut.");
				return false;
			}
			if (player.getX() == 3083 && player.getY() == 3353) {
				player.setNextAnimation(new Animation(2240));
				player.setNextWorldTile(new WorldTile(3084, 3353, 0));
			} else {
				player.setNextAnimation(new Animation(2240));
				player.setNextWorldTile(new WorldTile(3083, 3353, 0));
			}
		}

		return false;
	}

}

