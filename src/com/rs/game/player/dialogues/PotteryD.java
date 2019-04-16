package com.rs.game.player.dialogues;

import com.rs.game.player.skills.crafting.Pottery;
import com.rs.game.player.skills.crafting.Pottery.PotteryData;
import com.rs.game.player.content.SkillsDialogue;

public class PotteryD extends Dialogue {

	@Override
	public void start() {
		int[] items = new int[parameters.length];
		for (int i = 0; i < items.length; i++) {
			if (player.getTemporaryAttributtes().get("Pottery").equals(true))
				items[i] = ((PotteryData) parameters[i]).getProduct().getId();
			else
				items[i] = ((PotteryData) parameters[i]).getFinishedProduct().getId();
		}
		SkillsDialogue.sendSkillsDialogue(player, "Choose how many you wish to make", items);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > parameters.length) {
			end();
			return;
		}
		PotteryData data = (PotteryData) parameters[option];
		if (componentId >= 21 && componentId <= 26) {
			end();
			player.getTemporaryAttributtes().put("PotteryD", data);
			player.getTemporaryAttributtes().put("PotteryOption", option);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		int invQuantity = 0;
		int quantity = SkillsDialogue.getQuantity(componentId, player);
		if (player.getTemporaryAttributtes().get("Pottery").equals(true))
			invQuantity = player.getInventory().getItems().getNumberOf(1761);
		else
			invQuantity = player.getInventory().getItems().getNumberOf(data.getProduct().getId());
		if (quantity > invQuantity)
			quantity = invQuantity;
		player.getActionManager().setAction(new Pottery(data, quantity));
		end();
	}

	@Override
	public void finish() {

	}

}
