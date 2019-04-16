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
import com.rs.game.player.Skills;

public class KrilTsutsaroth extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6203 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.getRandom(1);
		switch (attackStyle) {
		case 0:
			npc.setNextAnimation(new Animation(6947));
			npc.setNextGraphics(new Graphics(1210));

			for (Entity t : npc.getPossibleTargets()) {
				delayHit(npc, 1, t, getMagicHit(npc, getRandomMaxHit(npc, 30, NPCCombatDefinitions.MAGE, t)));
				World.sendProjectile(npc, t, 1211, 41, 16, 41, 35, 16, 0);
				if (Utils.getRandom(4) == 0)
					t.getPoison().makePoisoned(16);
			}
			break;
		case 1:
			int damage = 47;
			if (target instanceof Player && ((Player) target).getPrayer().usingPrayer(0, 19)
					&& Utils.getRandom(10) > 7) {
				Player player = (Player) target;
				damage = 49;
				npc.setNextForceTalk(new ForceTalk("YARRRRRRR!"));
				player.getSkills().drainPrayer(player.getSkills().getLevel(Skills.PRAYER) / 2);
				player.getPrayer().refreshPrayerPoints();
				player.setPrayerDelay(Utils.getRandom(5) + 5);
				player.getPackets().sendGameMessage(
						"K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
			}
			if (Utils.getRandom(4) == 0)
				randomForcetalk(npc);
			for (Entity e : npc.getPossibleTargets()) {
				if (Utils.getRandom(4) == 0)
					e.getPoison().makePoisoned(16);

				npc.setNextAnimation(new Animation(damage == 47 ? 6945 : 6947));
				delayHit(npc, 0, e, getMeleeHit(npc, getRandomMaxHit(npc, damage, NPCCombatDefinitions.MELEE, e)));
			}
			break;
		}
		return defs.getAttackDelay();
	}

	private void randomForcetalk(NPC npc) {
		switch (Utils.random(0, 8)) {
		case 0:
			npc.setNextForceTalk(new ForceTalk("Attack them, you dogs!"));
			break;
		case 1:
			npc.setNextForceTalk(new ForceTalk("Forward!"));
			break;
		case 2:
			npc.setNextForceTalk(new ForceTalk("Death to Saradomin's dogs!"));
			break;
		case 3:
			npc.setNextForceTalk(new ForceTalk("Kill them, you cowards!"));
			break;
		case 4:
			npc.setNextForceTalk(new ForceTalk("The Dark One will have their souls!"));
			break;
		case 5:
			npc.setNextForceTalk(new ForceTalk("Zamorak curse them!"));
			break;
		case 6:
			npc.setNextForceTalk(new ForceTalk("Rend them limb from limb!"));
			break;
		case 7:
			npc.setNextForceTalk(new ForceTalk("No retreat!"));
			break;
		case 8:
			npc.setNextForceTalk(new ForceTalk("Flay them all!"));
			break;
		default:
			break;
		}
	}
}