package com.rs.game.player.dialogues;

import com.rs.game.player.Player;
import com.rs.game.item.Item;
import com.rs.game.player.skills.construction.House;
import com.rs.cache.loaders.ItemDefinitions;

public class StorePet extends Dialogue {

	public static final int ITEM = 0, OBJECT = 1;

	private House house;
	private int type;

	private String getName(int id) {
		return ItemDefinitions.getItemDefinitions(id).getName();
	}

	private int item;

	@Override
	public void start() {
		house = (House) parameters[0];
		type = (Integer) parameters[1];// need pet check for item
		item = (int) parameters[2];
		if (player.getHouse() == null || house != player.getHouse()) {
			end();
			player.getPackets().sendGameMessage("You need to be in your own house to do this.");
			return;
		}
		if (type == OBJECT)
			/*sendOptionsDialogue("Which pet would you like to remove?",
					(house.storedPets[0] != 0 ? getName(house.storedPets[0]) : "None"),
					(house.storedPets[1] != 0 ? getName(house.storedPets[1]) : "None"),
					(house.storedPets[2] != 0 ? getName(house.storedPets[2]) : "None"),
					(house.storedPets[3] != 0 ? getName(house.storedPets[3]) : "None"));*/
			sendDialogue(SEND_4_OPTIONS,
					"Which pet would you like to remove?",
					(house.storedPets[0] != 0 ? getName(house.storedPets[0]) : "None")
					,(house.storedPets[1] != 0 ? getName(house.storedPets[1]) : "None")
					,(house.storedPets[2] != 0 ? getName(house.storedPets[2]) : "None")
					,(house.storedPets[3] != 0 ? getName(house.storedPets[3]) : "None"));
		else if (type == ITEM)
		sendDialogue(SEND_2_OPTIONS,
					"Are you sure you'd like to store this pet?",
					"Yes"
					,"No");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (componentId) {
		case 1:
			switch (type) {
			case OBJECT:
				removePet(player, 0, house.storedPets[0]);
				break;
			case ITEM:
				if (!player.getInventory().containsItem(item, 1)) {
					end();
					return;
				}
				int count = 0;
				for (int i = 0; i < house.storedPets.length; i++) {
					if (house.storedPets[i] != 0) {
						count++;
						if (count >= house.storedPets.length) {
							player.sendMessage("You have no storage left in your enclosure.");
							end();
							break;
						}
						continue;
					}
					house.storedPets[i] = item;
					player.getInventory().deleteItem(item, 1);
					player.getPackets()
							.sendGameMessage("You are now storing your " + getName(item) + " in slot " + (++i) + ".");
					end();
					break;
				}
				break;
			}
			break;
		case 2:
			switch (type) {
			case OBJECT:
				removePet(player, 1, house.storedPets[1]);
				break;
			case ITEM:
				end();
				break;
			}
			break;
		case 3:
			removePet(player, 2, house.storedPets[2]);
			break;
		case 4:
			removePet(player, 3, house.storedPets[3]);
			break;
		default:
			end();
			break;
		}
	}

	private void removePet(Player player, int slot, int pet) {
		if (house == null || house != player.getHouse()) {
			end();
			return;
		}
		if (player.getHouse().storedPets[slot] == 0) {
			end();
			player.getPackets().sendGameMessage("You have no pet stored in that slot.");
			return;
		}
		if (player.getInventory().getFreeSlots() < 1) {
			end();
			player.getPackets().sendGameMessage("You need atleast one free spot in your inventory to do this.");
			return;
		}
		player.getInventory().addItem(new Item(house.storedPets[slot], 1));
		house.storedPets[slot] = 0;
		end();
		player.getPackets().sendGameMessage("You have removed your " + getName(pet) + " from slot " + (++slot) + ".");
	}

	@Override
	public void finish() {
	}

}