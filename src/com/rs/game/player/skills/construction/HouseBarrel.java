package com.rs.game.player.skills.construction;

import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.WorldObject;

/**
 * 
 * @author Hc747 - www.hyperion-projectxps.co.uk
 */

public class HouseBarrel extends Action {

	public static final int BEER_GLASS = 1919;
	public final Animation EMPTY_ANIMATION = new Animation(3660);
	public final Animation BEER_ANIMATION = new Animation(3661);
	public final Animation CIDER_ANIMATION = new Animation(3666);
	public final Animation ASGARNIAN_ANIMATION = new Animation(3662);
	public final Animation GREENMAN_ANIMATION = new Animation(3663);
	public final Animation DRAGON_ANIMATION = new Animation(3664);
	public final Animation CHEF_ANIMATION = new Animation(3665);
	public static final int BEER_BARREL = HouseConstants.HObject.BEER_BARREL.getId();
	public static final int CIDER_BARREL = HouseConstants.HObject.CIDER_BARREL.getId();
	public static final int ASGARNIAN_BARREL = HouseConstants.HObject.ASGARNIAN_ALE.getId();
	public static final int GREENMAN_BARREL = HouseConstants.HObject.GREENMAN_ALE.getId();
	public static final int DRAGON_BARREL = HouseConstants.HObject.DRAGON_BITTER_ALE.getId();
	public static final int CHEF_BARREL = HouseConstants.HObject.CHEFS_DELIGHT.getId();
	private int quantity;

	public static boolean handleItemOnObject(Player player, Item itemUsed, WorldObject object) {
		if (itemUsed.getId() == BEER_GLASS && object.getId() >= BEER_BARREL && object.getId() <= CHEF_BARREL) {
			if (object.getId() == BEER_BARREL) {
				player.getTemporaryAttributtes().put("BeerType", 1);
			} else if (object.getId() == CIDER_BARREL) {
				player.getTemporaryAttributtes().put("BeerType", 2);
			} else if (object.getId() == ASGARNIAN_BARREL) {
				player.getTemporaryAttributtes().put("BeerType", 3);
			} else if (object.getId() == GREENMAN_BARREL) {
				player.getTemporaryAttributtes().put("BeerType", 4);
			} else if (object.getId() == DRAGON_BARREL) {
				player.getTemporaryAttributtes().put("BeerType", 5);
			} else if (object.getId() == CHEF_BARREL) {
				player.getTemporaryAttributtes().put("BeerType", 6);
			}
			player.getActionManager().setAction(new HouseBarrel(player.getInventory().getNumberOf(BEER_GLASS)));
			return true;
		}
		return false;
	}

	public HouseBarrel(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		setActionDelay(player, 1);
		return true;
	}

	private boolean checkAll(Player player) {
		if (player.getInventory().getNumberOf(BEER_GLASS) < 1) {
			player.sendMessage("You need an empty glass to do this.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		if (quantity <= 0)
			return -1;
		final int beer = (Integer) player.getTemporaryAttributtes().get("BeerType");
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(EMPTY_ANIMATION);
				} else if (loop == 1) {
					player.getInventory().deleteItem(BEER_GLASS, 1);
					player.setNextAnimation(new Animation(-1));
					switch (beer) {
					case 1:
						player.setNextAnimation(BEER_ANIMATION);
						player.getInventory().addItem(1917, 1);
						player.sendMessage("You pour the beer into the cup.");
						break;
					case 2:
						player.setNextAnimation(CIDER_ANIMATION);
						player.getInventory().addItem(5763, 1);
						player.sendMessage("You pour the cider into the cup.");
						break;
					case 3:
						player.setNextAnimation(ASGARNIAN_ANIMATION);
						player.getInventory().addItem(1905, 1);
						player.sendMessage("You pour the asgarnian ale into the cup.");
						break;
					case 4:
						player.setNextAnimation(GREENMAN_ANIMATION);
						player.getInventory().addItem(1909, 1);
						player.sendMessage("You pour the greenman's ale into the cup.");
						break;
					case 5:
						player.setNextAnimation(DRAGON_ANIMATION);
						player.getInventory().addItem(1911, 1);
						player.sendMessage("You pour the dragon bitter into the cup.");
						break;
					case 6:
						player.setNextAnimation(CHEF_ANIMATION);
						player.getInventory().addItem(5755, 1);
						player.sendMessage("You pour the chef's delight into the cup.");
						break;
					}
				}
				loop++;
			}
		}, 0, 1);
		quantity--;
		if (quantity <= 0)
			return -1;
		return 3;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}

}
