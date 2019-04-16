package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.item.Item;
import com.rs.game.player.skills.herblore.Herblore;
import com.rs.game.player.content.SkillsDialogue;

public class HerbloreD extends Dialogue {

	private int items;
	private Item first;
	private Item second;

	@Override
	public void start() {
		items = (Integer) parameters[0];
		first = (Item) parameters[1];
		second = (Item) parameters[2];
		int amount;
		if (first.getId() == Herblore.PESTLE_AND_MORTAR)
			amount = player.getInventory().getItems().getNumberOf(second.getId());
		else if (second.getId() == Herblore.PESTLE_AND_MORTAR)
			amount = player.getInventory().getItems().getNumberOf(first.getId());
		else {
			amount = player.getInventory().getItems().getNumberOf(first.getId());
			if (amount > player.getInventory().getItems().getNumberOf(second.getId()))
				amount = player.getInventory().getItems().getNumberOf(second.getId());
		}
		SkillsDialogue.sendSkillsDialogue(player, "Choose how many you wish to make", new int[] { items });

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId >= 21 && componentId <= 26) {
			end();
			player.getTemporaryAttributtes().put("HerbloreD", first);
			player.getTemporaryAttributtes().put("Herblore2D", second);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		player.getActionManager()
				.setAction(new Herblore(first, second, SkillsDialogue.getQuantity(componentId, player)));
		end();
	}

	@Override
	public void finish() {
	}
}
