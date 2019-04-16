package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.skills.crafting.BattlestaffCrafting;
import com.rs.game.player.skills.crafting.BattlestaffCrafting.Battlestaff;

public class BattlestaffCraftingD extends Dialogue {

	private Battlestaff battlestaff;

	@Override
	public void start() {
		this.battlestaff = (Battlestaff) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, "Choose how many you wish to attach",
				new int[] { battlestaff.getOrb() });

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId >= 21 && componentId <= 26) {
			end();
			player.getTemporaryAttributtes().put("BattlestaffCraftingD", battlestaff);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		player.getActionManager()
				.setAction(new BattlestaffCrafting(battlestaff, SkillsDialogue.getQuantity(componentId, player)));
		end();
	}

	@Override
	public void finish() {

	}

}
