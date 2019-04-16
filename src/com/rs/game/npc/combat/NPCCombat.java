package com.rs.game.npc.combat;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceMovement;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.fightcaves.FightCavesNPC;
import com.rs.game.npc.others.Hybrid;
import com.rs.game.npc.pest.PestPortal;
import com.rs.game.player.Player;
import com.rs.game.player.content.Combat;
import com.rs.utils.MapAreas;
import com.rs.utils.Utils;

/**
 * 
 * @Improved Andreas - AvalonPK
 * 
 */

public final class NPCCombat {

	private NPC npc;
	private int combatDelay;
	private Entity target;

	public NPCCombat(NPC npc) {
		this.npc = npc;
	}

	public int getCombatDelay() {
		return combatDelay;
	}

	/*
	 * returns if under combat
	 */
	public boolean process() {
		if (combatDelay > 0)
			combatDelay--;
		if (target != null) {
			if (!checkAll()) {
				removeTarget();
				return false;
			}
			if (combatDelay <= 0)
				combatDelay = combatAttack();
			return true;
		}
		return false;
	}

	/*
	 * return combatDelay
	 */
	private int combatAttack() {
		Entity target = this.target;
		if (target == null)
			return 0;
		if (npc.isDead() || npc.hasFinished() || npc.isForceWalking() || target.isDead() || target.hasFinished()
				|| npc.getPlane() != target.getPlane())
			return 0;
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = defs.getAttackStyle();
		if (target instanceof Familiar) {
			Familiar familiar = (Familiar) target;
			Player player = familiar.getOwner();
			if (player != null) {
				target = player;
				npc.setTarget(target);
			}
			if (target == familiar.getOwner()) {
				npc.setTarget(target);
			}

		}
		int maxDistance = attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.SPECIAL2 ? 0
				: npc instanceof FightCavesNPC && attackStyle == NPCCombatDefinitions.SPECIAL ? 12 : 7;
		int size = npc.getSize();
		int targetSize = target.getSize();
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance) {
			return 0;
		}
		//addAttackedByDelay(target);
		return CombatScriptsHandler.specialAttack(npc, target);
	}

	protected void doDefenceEmote(Entity target) {
		target.setNextAnimationNoPriority(new Animation(Combat.getDefenceEmote(target)), target);
	}

	public Entity getTarget() {
		return target;
	}

	public void addAttackedByDelay(Entity target) {
		target.setAttackedBy(npc);
		target.setAttackedByDelay(Utils.currentTimeMillis() + 5000); // 8seconds
	}

	public void setTarget(Entity target) {
		this.target = target;
		npc.setNextFaceEntity(target);
		if (!checkAll()) {
			removeTarget();
			return;
		}
	}

	// maxDistance = npc.getForceAgressiveDistance() > 0 ? npc
	// .getForceAgressiveDistance() : 4;

	// maxDistance = npc.getForceTargetDistance() > 0 ? npc
	// .getForceTargetDistance() * 2 : 8;

	public boolean checkAll() {
		Entity target = this.target; // prevents multithread issues
		if (target == null)
			return false;
		if (npc.isDead() || npc.hasFinished() || npc.isForceWalking() || target.isDead() || target.hasFinished()
				|| npc.getPlane() != target.getPlane())
			return false;
		int distanceX = npc.getX() - npc.getRespawnTile().getX();
		int distanceY = npc.getY() - npc.getRespawnTile().getY();
		int targetdistanceX = target.getX() - npc.getRespawnTile().getX();
		int targetdistanceY = target.getY() - npc.getRespawnTile().getY();
		int size = npc.getSize();
		int maxDistance;
		int attackStyle = npc.getCombatDefinitions().getAttackStyle();
		if (npc.getFreezeDelay() >= Utils.currentTimeMillis()) {
			if ((attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.SPECIAL2)
					&& size == 1)
				if (npc.getX() == target.getX() + 1 && npc.getY() == target.getY() + 1
						|| npc.getX() == target.getX() - 1 && npc.getY() == target.getY() - 1
						|| npc.getX() == target.getX() - 1 && npc.getY() == target.getY() + 1
						|| npc.getX() == target.getX() + 1 && npc.getY() == target.getY() - 1) {
					combatDelay = 3;
					return true;
				}
			if (npc.withinDistance(target, 0))
				return false;
		}
		if (!npc.isNoDistanceCheck() && !npc.isCantFollowUnderCombat()) {
			maxDistance = npc instanceof Familiar ? 32
					: npc.getForceTargetDistance() != 0 ? npc.getForceTargetDistance() * 2 : 12;
			if (!(npc instanceof Familiar)) {
				if (npc.getMapAreaNameHash() != -1) {
					// if out his area
					if (!MapAreas.isAtArea(npc.getMapAreaNameHash(), npc) || (!npc.canBeAttackFromOutOfArea()
							&& !MapAreas.isAtArea(npc.getMapAreaNameHash(), target))) {
						npc.forceWalkRespawnTile();
						return false;
					}
				} else if (targetdistanceX > size + maxDistance || targetdistanceX < -1 - maxDistance
						|| targetdistanceY > size + maxDistance || targetdistanceY < -1 - maxDistance) {
					npc.forceWalkRespawnTile();
					return false;
				} else if ((distanceX > size + maxDistance || distanceX < -1 - maxDistance
						|| distanceY > size + maxDistance || distanceY < -1 - maxDistance)) {
					return false;
				}
			}
			maxDistance = npc instanceof Familiar ? 16
					: npc.getForceAgressiveDistance() != 0 ? npc.getForceAgressiveDistance() : 8;
			if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance
					|| distanceY < -1 - maxDistance)
				return false;
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();

		} else {
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();
		}
		// checks for no multi area :)
		if (npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			if (!familiar.canAttack(target))
				return false;
		} else {
			if (!npc.isForceMultiAttacked()) {
				if (!target.isAtMultiArea() || !npc.isAtMultiArea()) {
					if (npc.getAttackedBy() != target && npc.getAttackedByDelay() > Utils.currentTimeMillis())
						return false;
					if (target.getAttackedBy() != npc && target.getAttackedByDelay() > Utils.currentTimeMillis())
						return false;
				}
			}
		}
		if (!npc.isCantFollowUnderCombat()) {
			// if is under
			int targetSize = target.getSize();
			if (distanceX < size && distanceX > -targetSize && distanceY < size && distanceY > -targetSize
					&& !target.hasWalkSteps()) {
				npc.resetWalkSteps();
				if (!npc.addWalkSteps(target.getX() + 1, npc.getY())) {
					npc.resetWalkSteps();
					if (!npc.addWalkSteps(target.getX() - size, npc.getY())) {
						npc.resetWalkSteps();
						if (!npc.addWalkSteps(npc.getX(), target.getY() + 1)) {
							npc.resetWalkSteps();
							if (!npc.addWalkSteps(npc.getX(), target.getY() - size)) {
								return true;
							}
						}
					}
				}
				return true;
			}
			/*if (npc instanceof Nex) {
				Nex nex = (Nex) npc;
				maxDistance = nex.isForceFollowClose() ? 0 : 7;
				if ((!npc.clipedProjectile(target, maxDistance == 0 && !forceCheckClipAsRange(target)))
						|| !Utils.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize,
								maxDistance)) {
					npc.resetWalkSteps();
					if (!Utils.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize, 10)) {
						int[][] dirs = Utils.getCoordOffsetsNear(size);
						for (int dir = 0; dir < dirs[0].length; dir++) {
							final WorldTile tile = new WorldTile(new WorldTile(target.getX() + dirs[0][dir],
									target.getY() + dirs[1][dir], target.getPlane()));
							if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), size)) {
								npc.setNextForceMovement(new ForceMovement(new WorldTile(npc), 0, tile, 1,
										Utils.getMoveDirection(tile.getX() - npc.getX(), tile.getY() - npc.getY())));
								npc.setNextAnimation(new Animation(17408));
								npc.setNextWorldTile(tile);
								return true;
							}
						}
					} else
						npc.calcFollow(target, 2, true, npc.isIntelligentRouteFinder());
					return true;
				} else
					npc.resetWalkSteps();
			}*/ else {
				maxDistance = npc.isForceFollowClose() ? 0
						: (attackStyle == NPCCombatDefinitions.MELEE || attackStyle == NPCCombatDefinitions.SPECIAL2)
								? 0
								: npc instanceof FightCavesNPC && attackStyle == NPCCombatDefinitions.SPECIAL
												? 12 : 7;
				npc.resetWalkSteps();
				// is far from target, moves to it till can attack
				if ((!npc.clipedProjectile(target, maxDistance == 0)) || distanceX > size + maxDistance
						|| distanceX < -1 - maxDistance || distanceY > size + maxDistance
						|| distanceY < -1 - maxDistance) {
					if (npc.isIntelligentRouteFinder()) {
						if (!npc.calcFollow(target, npc.getRun() ? 2 : 1, true, npc.isIntelligentRouteFinder())
								&& combatDelay < 3)
							combatDelay = 3;
						return true;
					} else {
						if (!npc.addWalkStepsInteract(target.getX(), target.getY(), 1, size, true) && combatDelay < 3)
							combatDelay = 3;
						return true;
					}
				}
				// if under target, moves

			}
		}
		return true;
	}

	private boolean forceCheckClipAsRange(Entity target) {
		return target instanceof PestPortal;
	}

	public void addCombatDelay(int delay) {
		combatDelay += delay;
	}

	public void setCombatDelay(int delay) {
		combatDelay = delay;
	}

	public boolean underCombat() {
		return target != null;
	}

	public void removeTarget() {
		this.target = null;
		npc.setNextFaceEntity(null);
	}

	public void reset() {
		combatDelay = 0;
		target = null;
	}

}