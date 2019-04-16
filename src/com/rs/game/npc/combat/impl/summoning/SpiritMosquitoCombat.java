package com.rs.game.npc.combat.impl.summoning;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;

public class SpiritMosquitoCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7331, 7332 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			npc.setNextAnimation(new Animation(8036));
			if (target instanceof NPC) {
				if (!(((NPC) target).getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.SPECIAL))
					target.setAttackedByDelay(3000);// three seconds
				else
					familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot attack that monster.");
			} else if (target instanceof Player)
				familiar.getOwner().getPackets().sendGameMessage("Your familiar cannot attack a player here.");
		} else {
			npc.setNextAnimation(new Animation(8032));
			delayHit(npc, 1, target, getMagicHit(npc, getRandomMaxHit(npc, 230, NPCCombatDefinitions.MELEE, target)));
		}
		return defs.getAttackDelay();
	}

}

