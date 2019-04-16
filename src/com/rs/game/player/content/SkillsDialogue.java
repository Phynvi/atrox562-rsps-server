package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.item.Item;

public final class SkillsDialogue {

	/**
	 * @author Kris
	 * @param player
	 *            - Entity given.
	 * @param explanation
	 *            - Text ontop of the interface describing the current action,
	 *            sent by the action.
	 * @param items
	 *            - Objects displayed on the interface.
	 */
	public static void sendSkillsDialogue(Player player, String explanation, int[] items) {
		player.getPackets().sendButtonConfig(754, items.length);
		player.getPackets().sendHideIComponent(513, 21, false);
		//player.getPackets().sendIComponentText(explanation, 2, 513);
		player.getPackets().sendGlobalString(131, explanation);
		for (int i = 0; i < items.length; i++) {
			Item item = new Item(items[i], 1);
			player.getPackets().sendButtonConfig(755 + i, items[i]);
			//player.getPackets().sendIComponentText(item.getName(), 15 + i, 513);
			player.getPackets().sendGlobalString(132 + i, item.getName());
		}
		player.getInterfaceManager().sendChatBoxInterface(513);
	}

	private SkillsDialogue() {

	}

	/**
	 * 
	 * @param componentId
	 *            - Component clicked
	 * @return Picks a slot out of the six. (ComponentID 33 is the first slot,
	 *         next being ++ etc);
	 */
	public static int getItemSlot(int componentId) {
		System.out.println(componentId);
		return (componentId >= 39 && componentId <= 44) ? componentId - 39
				: (componentId >= 33 && componentId <= 38) ? componentId - 33
						: (componentId >= 27 && componentId <= 32) ? componentId - 27 : componentId - 21;
	}

	/**
	 * 
	 * @param componentId
	 *            - Component clicked
	 * @return Returns the amount chosen based off of the componentID. Nested.
	 */
	public static int getQuantity(int componentId, Player player) {
		System.out.println(componentId);
		return (componentId >= 39 && componentId <= 44) ? 1
				: (componentId >= 33 && componentId <= 38) ? 5 : (componentId >= 27 && componentId <= 32) ? 10 : 0;
	}
}