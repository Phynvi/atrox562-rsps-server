package com.rs.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class WeightManager {

	private static Map<Integer, Double> itemWeight = new HashMap<>();

	private static final int[] WEIGHT_REDUCERS = { 88, 10069, 10073, 10663, 10071, 10074, 10664, 10553, 10554 };

	public static void init() {
		try (BufferedReader reader = new BufferedReader(new FileReader("./data/items/weights.txt"))) {
			while (true) {
				String file = reader.readLine();
				if (file == null)
					break;
				if (file.startsWith("//"))
					continue;
				String[] values = file.split(" - ");
				itemWeight.put(Integer.valueOf(values[0]), Double.parseDouble(values[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double calculateWeight(Player player) {
		double weight = 0;
		for (int REDUCERS : WEIGHT_REDUCERS) {
			if (player.getEquipment().getItems().contains(new Item(REDUCERS)))
				weight -= getReducersWeight(REDUCERS);
		}
		for (Item equipment : player.getEquipment().getItems().getItems()) {
			if (equipment == null)
				continue;
			weight += (getWeight(equipment.getId()) * equipment.getAmount());
		}
		for (Item inventory : player.getInventory().getItems().getItems()) {
			if (inventory == null)
				continue;
			weight += (getWeight(inventory.getId()) * inventory.getAmount());
		}
		return Utils.round(weight, 2);
	}

	private static double getWeight(int itemId) {
		if (itemId == -1 || ItemDefinitions.getItemDefinitions(itemId).isNoted())
			return 0;
		if (itemWeight.get(itemId) == null)
			return 0;
		for (int REDUCERS : WEIGHT_REDUCERS)
			if (itemId == REDUCERS)
				return 0.3;
		return itemWeight.get(itemId);
	}

	private static double getReducersWeight(int itemId) {
		if (ItemDefinitions.getItemDefinitions(itemId).isNoted())
			return 0;
		return itemWeight.get(itemId);
	}

}