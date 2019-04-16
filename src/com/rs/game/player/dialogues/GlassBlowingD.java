package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.content.SkillsDialogue;
import com.rs.game.player.skills.crafting.GlassBlowing;
import com.rs.game.player.skills.crafting.GlassBlowing.GlassData;

public class GlassBlowingD extends Dialogue {
	@Override
	public void start() {
		int[] items = new int[parameters.length];
		for (int i = 0; i < items.length; i++)
			items[i] = ((GlassData) parameters[i]).getFinalProduct();

		SkillsDialogue.sendSkillsDialogue(player, "Choose how many you wish to make", items);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > parameters.length) {
			end();
			return;
		}
		GlassData data = (GlassData) parameters[option];
		if (componentId >= 21 && componentId <= 26) {
			end();
			player.getTemporaryAttributtes().put("GlassblowingD", data);
			player.getTemporaryAttributtes().put("GlassblowingOption", option);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		int quantity = SkillsDialogue.getQuantity(componentId, player);
		int invQuantity = player.getInventory().getItems().getNumberOf(data.getglassId());
		if (quantity > invQuantity)
			quantity = invQuantity;
		player.getActionManager().setAction(new GlassBlowing(data, quantity));
		end();
	}

	@Override
	public void finish() {

	}

}