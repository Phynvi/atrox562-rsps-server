package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.Skills;
import com.rs.game.item.Item;
import com.rs.game.player.skills.smithing.Smelting;
import com.rs.game.player.skills.smithing.Smelting.Bars;

public class CannonballsD extends Dialogue {
	
	int amount;

	@Override
	public void start() {
		if (player.getSkills().getLevel(Skills.SMITHING) < 35) {
			player.sendMessage("You need at least level 35 Smithing to make cannonballs.");
			return;
		}
		SkillsDialogue.sendSkillsDialogue(player, "How many bars would you like to use?",
				new int[] { 2 });

	}

	@Override
	public void run(int interfaceId, int componentId) {
		Bars bar = Bars.getBar(9);
		player.sendMessage("componentId = "+ componentId +"");
		player.getActionManager().setAction(new Smelting(bar, Smelting.getAmountBasedOnComponent(player, componentId, bar)));
		end();
	}

	@Override
	public void finish() {

	}

}
