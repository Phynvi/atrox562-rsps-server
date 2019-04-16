package com.rs.game.player.dialogues;

import com.rs.game.item.Item;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.skills.fletching.Fletching;
import com.rs.game.player.skills.fletching.Fletching.Fletch;
import com.rs.game.player.content.SkillsDialogue;

public class FletchingD extends Dialogue {

	private Fletch items;

	@Override
	public void start() {
		items = (Fletch) parameters[0];
		SkillsDialogue.sendSkillsDialogue(player, message(items), items.getProduct());
	}

	public String message(Fletch item) {
		String name = item.name().toLowerCase();
		if (item.getId() == 2864)
			return "Choose how many sets of 4 arrows you wish to make";
		if (item.getId() >= 1601 && item.getId() <= 1615 || item.getId() == 6573)
			return "Choose how many sets of " + (item.getId() == 6573 ? "24" : "12") + " bolt tips you wish to make";
		if (name.contains("bolt"))
			return "Choose how many sets of 10 bolts you wish to make";
		if (name.contains("arrow"))
			return "Choose how many sets of 15 arrows you wish to make";
		if (name.contains("dart"))
			return "Choose how many sets of 10 darts you wish to make";
		return "Choose how many you wish to make";
	}

	@Override
	public void run(int interfaceId, int componentId) {
		boolean maxQuantityTen = Fletching.maxMakeQuantityTen(new Item(items.getSelected()));
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > items.getProduct().length) {
			end();
			return;
		}
		if (componentId >= 21 && componentId <= 26) {
			end();
			player.getTemporaryAttributtes().put("FletchingD", items);
			player.getTemporaryAttributtes().put("FletchingOption", option);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		int quantity = maxQuantityTen ? 10 : SkillsDialogue.getQuantity(componentId, player);
		int invQuantity = player.getInventory().getItems().getNumberOf(items.getId());
		if (!maxQuantityTen) {
			if (quantity > invQuantity)
				quantity = invQuantity;
		}
		player.getActionManager().setAction(new Fletching(items, option, quantity));
		end();
	}

	@Override
	public void finish() {
	}

}
