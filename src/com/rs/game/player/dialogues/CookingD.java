package com.rs.game.player.dialogues;

import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.player.skills.cooking.Cooking;
import com.rs.game.player.skills.cooking.Cooking.Cookables;
import com.rs.game.player.content.SkillsDialogue;

public class CookingD extends Dialogue {

	private Cookables cooking;
	private WorldObject object;

	@Override
	public void start() {
		this.cooking = (Cookables) parameters[0];
		this.object = (WorldObject) parameters[1];

		SkillsDialogue.sendSkillsDialogue(player, "Choose how many you wish to cook",
				new int[] { cooking.getProduct().getId() });
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (componentId >= 21 && componentId <= 26) {
			end();
			int item = cooking.getRawItem().getId();
			player.getTemporaryAttributtes().put("CookingD", object);
			player.getTemporaryAttributtes().put("CookingOption", item);
			player.getPackets().sendRunScript(108, new Object[] { "Enter the amount: " });
			return;
		}
		player.getActionManager()
				.setAction(new Cooking(object, cooking.getRawItem(), SkillsDialogue.getQuantity(componentId, player)));
		end();
	}

	@Override
	public void finish() {

	}

}
