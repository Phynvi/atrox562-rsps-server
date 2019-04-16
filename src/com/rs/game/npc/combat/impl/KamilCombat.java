package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.utils.Utils;

public class KamilCombat extends CombatScript {

	private Player player;

	@Override
	public Object[] getKeys() {
		return new Object[] { 1913 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		if (npc.getHitpoints() < npc.getMaxHitpoints() / 2
				&& Utils.random(5) == 0) { // if lower than 50% hp, 1/5 prob of
											// healing 10%
			npc.heal(30);
		}

		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.getRandom(2) == 0) { // magical attack
			npc.playSound(168, 2);
			npc.setNextAnimation(new Animation(1979));
			for (Entity t : npc.getPossibleTargets()) {
				delayHit(
						npc,
						1,
						t,
						getMagicHit(
								npc,
								getRandomMaxHit(npc, 340,
										NPCCombatDefinitions.MAGE, t)));
				World.sendProjectile(npc, t, 368, 41, 16, 41, 35, 16, 0);
				target.setFreezeDelay(300);
				target.setNextGraphics(new Graphics(369));
				npc.setNextForceTalk(new ForceTalk("You stand no chance!"));
			}
			
		} else if (Utils.getRandom(2) == 1) {
			npc.setNextAnimation(new Animation(12029));
			
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, 220,
									NPCCombatDefinitions.MELEE, target)));
		} else { // melee attack
			npc.setNextAnimation(new Animation(12029));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getRandomMaxHit(npc, 220,
									NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}

}