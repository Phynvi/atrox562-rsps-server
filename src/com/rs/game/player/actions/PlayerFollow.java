package com.rs.game.player.actions;

import com.rs.game.player.Player;
import com.rs.game.route.RouteFinder;
import com.rs.game.route.strategy.EntityStrategy;
import com.rs.utils.Utils;


/**
 * 
 * @Improved Andreas - AvalonPK
 * 
 */

public class PlayerFollow extends Action {

	private Player target;

	public PlayerFollow(Player target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player))
			return true;
		player.setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.hasFinished()
				|| target.hasFinished() || player.getLockDelay() > Utils.currentTimeMillis())
			return false;
		if (player.getPlane() != target.getPlane())
			return false;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = player.getSize();
		int maxDistance = 16;
		if (player.getPlane() != target.getPlane()
				|| distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return false;
		if (player.getFreezeDelay() < Utils.currentTimeMillis()) {
		int lastFaceEntity = target.getLastFaceEntity();
		if (lastFaceEntity == player.getClientIndex()
				&& target.getActionManager().getAction() instanceof PlayerFollow) {
			if (target.getDirection() == 0)
				player.addWalkSteps(target.getX(), target.getY() + 1, 25, true);
			if (target.getDirection() == 2048)
				player.addWalkSteps(target.getX() + 1, target.getY() + 1, 25, true);
			if (target.getDirection() == 4096)
				player.addWalkSteps(target.getX() + 1, target.getY(), 25, true);
			if (target.getDirection() == 6144)
				player.addWalkSteps(target.getX() + 1, target.getY() - 1, 25, true);
			if (target.getDirection() == 8192)
				player.addWalkSteps(target.getX(), target.getY() - 1, 25, true);
			if (target.getDirection() == 10240)
				player.addWalkSteps(target.getX() - 1, target.getY() - 1, 25, true);
			if (target.getDirection() == 12288)
				player.addWalkSteps(target.getX() - 1, target.getY(), 25, true);
			if (target.getDirection() == 14336)
				player.addWalkSteps(target.getX() - 1, target.getY() + 1, 25, true);
		} else if (!player.clipedProjectile(target, true)
				|| !Utils.isOnRange(player.getX(), player.getY(), size,
						target.getX(), target.getY(), target.getSize(), 0)) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER,
					player.getX(), player.getY(), player.getPlane(),
					player.getSize(), new EntityStrategy(target), true);
			if (steps == -1)
				return false;

			if (steps > 0) {
				player.resetWalkSteps();

				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				for (int step = steps - 1; step >= 0; step--) {
					if (!player.addWalkSteps(bufferX[step], bufferY[step], 25,
							true))
						break;
				}
			}
			return true;
			}
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.setNextFaceEntity(null);
	}
}