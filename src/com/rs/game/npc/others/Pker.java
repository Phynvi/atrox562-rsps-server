package com.rs.game.npc.others;

import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class Pker extends NPC {

	public Pker(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setName("Isaiah");
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.5;
	}
	
	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (hit.getSource() != null) {
			int recoil = (int) (hit.getDamage() * 0.1);
			if (recoil > 0) {
				hit.getSource().applyHit(
						new Hit(this, recoil, HitLook.DESEASE_DAMAGE));
			}
		}
	}

}