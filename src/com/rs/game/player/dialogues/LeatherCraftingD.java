package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.skills.crafting.LeatherCrafting;
import com.rs.game.player.skills.crafting.LeatherCrafting.LeatherData;

public class LeatherCraftingD extends Dialogue {

	@Override
	public void start() {
		int[] items = new int[parameters.length];
		for (int i = 0; i < items.length; i++)
			items[i] = ((LeatherData) parameters[i]).getFinalProduct();

		SkillsDialogue.sendSkillsDialogue(player, "Choose how many you wish to craft", items);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > parameters.length) {
			end();
			return;
		}
		LeatherData data = (LeatherData) parameters[option];
		if (componentId >= 21 && componentId <= 26) {
			end();
			player.getTemporaryAttributtes().put("LeatherCraftingD", data);
			player.getTemporaryAttributtes().put("LeatherCraftingOption", option);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		int quantity = SkillsDialogue.getQuantity(componentId, player);
		int invQuantity = player.getInventory().getItems().getNumberOf(data.getLeatherId());
		if (quantity > invQuantity)
			quantity = invQuantity;
		player.getActionManager().setAction(new LeatherCrafting(data, quantity));
		end();
	}

	@Override
	public void finish() {
	}
}