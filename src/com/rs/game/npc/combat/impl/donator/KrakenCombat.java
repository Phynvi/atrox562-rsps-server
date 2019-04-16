package com.rs.game.npc.combat.impl.donator;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class KrakenCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] {8614};
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if(npc.getHitpoints() < npc.getMaxHitpoints()/2 && Utils.random(5) == 0)  //if lower than 50% hp, 1/5 prob of healing
			npc.heal(100);
		
		int attackStyle = Utils.random(2);
		if(target instanceof Player) {
			Player targetPlayer = (Player) target;
		}
		switch(attackStyle) {
		case 0: //magic
			int damage = getRandomMaxHit(npc, defs.getMaxHit(),
					NPCCombatDefinitions.MAGE, target);
			delayHit(npc, 2, target, getMagicHit(npc, damage));
			World.sendProjectile(npc, target, 1354, 34, 16, 30, 35, 16, 0);
			if(damage > 0) {
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						target.setNextGraphics(new Graphics(1300, 0, 100));
						if(Utils.random(5) == 0) { //1/5 prob poisoning while maging
							target.setNextGraphics(new Graphics(1300));
							target.getPoison().makePoisoned(16);
						}
					}

				}, 2);
			}
			npc.setNextAnimation(new Animation(11000));
		break;
		case 1: //range
			delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, defs.getMaxHit(),
					NPCCombatDefinitions.RANGE, target)));
			World.sendProjectile(npc, target, 1278, 34, 16, 30, 35, 16, 0);
			//npc.setNextAnimation(new Animation(getRangeAnimation(npc)));
		break;
		}
		return defs.getAttackDelay();
	}
}
