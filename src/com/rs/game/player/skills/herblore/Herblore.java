package com.rs.game.player.skills.herblore;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.game.item.Item;
import com.rs.game.player.Skills;

public class Herblore extends Action {

	private enum Ingredients {

		GUAM(249, new short[] { Herblore.VIAL, Herblore.SWAMP_TAR }, new short[] { 91, 10142 }, new byte[] { 0, 19 }, new double[] { 0, 30 }), 
		MARRENTILL(251, new short[] { Herblore.VIAL, Herblore.SWAMP_TAR }, new short[] { 93, 10143 }, new byte[] { 0, 31 }, new double[] { 0, 42.5 }), 
		TARROMIN(253, new short[] { Herblore.VIAL, Herblore.SWAMP_TAR }, new short[] { 95, 10144 }, new byte[] { 0, 39 }, new double[] { 0, 55 }), 
		HARRALANDER(255, new short[] { Herblore.VIAL, Herblore.SWAMP_TAR }, new short[] { 97, 10145 }, new byte[] { 0, 44 }, new double[] { 0, 72.5 }), 
		RANARR(257, new short[] { Herblore.VIAL }, new short[] { 99 }, new byte[] { 0 }, new double[] { 0 }), 
		TOADFLAX(2998, new short[] { Herblore.VIAL, Herblore.COCONUT_MILK }, new short[] { 3002, 5942 }, new byte[] { 0, 0 }, new double[] { 0, 0 }), 
		SPIRIT_WEED(12172, new short[] { Herblore.VIAL }, new short[] { 12181 }, new byte[] { 0 }, new double[] { 0 }), 
		WERGALI(14854, new short[] { Herblore.VIAL }, new short[] { 14856 }, new byte[] { 0 }, new double[] { 0 }), 
		IRIT(259, new short[] { Herblore.VIAL, Herblore.COCONUT_MILK }, new short[] { 101, 5951 }, new byte[] { 0, 0 }, new double[] { 0, 0 }), 
		AVANTOE(261, new short[] { Herblore.VIAL }, new short[] { 103 }, new byte[] { 0 }, new double[] { 0 }), 
		KWUARM(263, new short[] { Herblore.VIAL }, new short[] { 105 }, new byte[] { 0 }, new double[] { 0 }), 
		STARFLOWER(9017, new short[] { Herblore.VIAL }, new short[] { 9019 }, new byte[] { 0 }, new double[] { 0 }), 
		SNAPDRAGON(3000, new short[] { Herblore.VIAL }, new short[] { 3004 }, new byte[] { 0 }, new double[] { 0 }), 
		CADANTINE(265, new short[] { Herblore.VIAL }, new short[] { 107 }, new byte[] { 0 }, new double[] { 0 }), 
		LANTADYME(2481, new short[] { Herblore.VIAL }, new short[] { 2483 }, new byte[] { 0 }, new double[] { 0 }), 
		DWARF_WEED(267, new short[] { Herblore.VIAL }, new short[] { 109 }, new byte[] { 0 }, new double[] { 0 }), 
		CACTUS_SPINE(6016, new short[] { Herblore.COCONUT_MILK }, new short[] { 5936 }, new byte[] { 0 }, new double[] { 0 }), 
		TORSTOL(269, new short[] { Herblore.VIAL }, new short[] { 111 }, new byte[] { 0 }, new double[] { 0 }), 
		CAVE_NIGHTSHADE(2398, new short[] { Herblore.COCONUT_MILK }, new short[] { 5939 }, new byte[] { 0 }, new double[] { 0 }), 
		EYE_OF_NEWT(221,
																																																												new short[] {
																																																														91,
																																																														101 },
																																																												new short[] {
																																																														121,
																																																														145 },
																																																												new byte[] {
																																																														0,
																																																														45 },
																																																												new double[] {
																																																														25,
																																																														100 }), UNICORN_HORN_DUST(
																																																																235,
																																																																new short[] {
																																																																		93,
																																																																		101 },
																																																																new short[] {
																																																																		175,
																																																																		181 },
																																																																new byte[] {
																																																																		5,
																																																																		48 },
																																																																new double[] {
																																																																		37.5,
																																																																		106.3 }), LIMPWURT_ROOT(
																																																																				225,
																																																																				new short[] {
																																																																						95,
																																																																						105 },
																																																																				new short[] {
																																																																						115,
																																																																						157 },
																																																																				new byte[] {
																																																																						12,
																																																																						55 },
																																																																				new double[] {
																																																																						50,
																																																																						125 }), RED_SPIDER_EGGS(
																																																																								223,
																																																																								new short[] {
																																																																										97,
																																																																										3004,
																																																																										5936 },
																																																																								new short[] {
																																																																										127,
																																																																										3026,
																																																																										5937 },
																																																																								new byte[] {
																																																																										22,
																																																																										63,
																																																																										73 },
																																																																								new double[] {
																																																																										62.5,
																																																																										142.5,
																																																																										165 }), BLAMISH_SNAIL_SLIME(
																																																																												1581,
																																																																												new short[] {
																																																																														97 },
																																																																												new short[] {
																																																																														1582 },
																																																																												new byte[] {
																																																																														25 },
																																																																												new double[] {
																																																																														80 }), CHOCOLATE_DUST(
																																																																																1975,
																																																																																new short[] {
																																																																																		97 },
																																																																																new short[] {
																																																																																		3010 },
																																																																																new byte[] {
																																																																																		26 },
																																																																																new double[] {
																																																																																		67.5 }), WHITE_BERRIES(
																																																																																				239,
																																																																																				new short[] {
																																																																																						99,
																																																																																						107 },
																																																																																				new short[] {
																																																																																						133,
																																																																																						163 },
																																																																																				new byte[] {
																																																																																						30,
																																																																																						66 },
																																																																																				new double[] {
																																																																																						75,
																																																																																						150 }), RUBIUM(
																																																																																								12630,
																																																																																								new short[] {
																																																																																										91 },
																																																																																								new short[] {
																																																																																										12633 },
																																																																																								new byte[] {
																																																																																										31 },
																																																																																								new double[] {
																																																																																										55 }), TOAD_LEGS(
																																																																																												2152,
																																																																																												new short[] {
																																																																																														3002 },
																																																																																												new short[] {
																																																																																														3034 },
																																																																																												new byte[] {
																																																																																														34 },
																																																																																												new double[] {
																																																																																														80 }), GOAT_HORN_DUST(
																																																																																																9736,
																																																																																																new short[] {
																																																																																																		97 },
																																																																																																new short[] {
																																																																																																		9741 },
																																																																																																new byte[] {
																																																																																																		36 },
																																																																																																new double[] {
																																																																																																		84 }), PHARMAKOS_BERRIES(
																																																																																																				11807,
																																																																																																				new short[] {
																																																																																																						3002 },
																																																																																																				new short[] {
																																																																																																						11810 },
																																																																																																				new byte[] {
																																																																																																						37 },
																																																																																																				new double[] {
																																																																																																						85 }), SNAPE_GRASS(
																																																																																																								231,
																																																																																																								new short[] {
																																																																																																										99,
																																																																																																										103 },
																																																																																																								new short[] {
																																																																																																										139,
																																																																																																										151 },
																																																																																																								new byte[] {
																																																																																																										38,
																																																																																																										50 },
																																																																																																								new double[] {
																																																																																																										87.5,
																																																																																																										112.5 }), COCKATRICE_EGG(
																																																																																																												12109,
																																																																																																												new short[] {
																																																																																																														12181 },
																																																																																																												new short[] {
																																																																																																														12142 },
																																																																																																												new byte[] {
																																																																																																														40 },
																																																																																																												new double[] {
																																																																																																														92 }), FROGSPAWN(
																																																																																																																10961,
																																																																																																																new short[] {
																																																																																																																		14856 },
																																																																																																																new short[] {
																																																																																																																		14840 },
																																																																																																																new byte[] {
																																																																																																																		40 },
																																																																																																																new double[] {
																																																																																																																		92 }), CHOPPED_ONION(
																																																																																																																				1871,
																																																																																																																				new short[] {
																																																																																																																						101 },
																																																																																																																				new short[] {
																																																																																																																						18661 },
																																																																																																																				new byte[] {
																																																																																																																						46 },
																																																																																																																				new double[] {
																																																																																																																						0 }), MORT_MYRE_FUNGUS(
																																																																																																																								2970,
																																																																																																																								new short[] {
																																																																																																																										103 },
																																																																																																																								new short[] {
																																																																																																																										3018 },
																																																																																																																								new byte[] {
																																																																																																																										52 },
																																																																																																																								new double[] {
																																																																																																																										117.5 }), SHRUNK_OGLEROOT(
																																																																																																																												11205,
																																																																																																																												new short[] {
																																																																																																																														95 },
																																																																																																																												new short[] {
																																																																																																																														11204 },
																																																																																																																												new byte[] {
																																																																																																																														52 },
																																																																																																																												new double[] {
																																																																																																																														6 }), KEBBIT_TEETH_DUST(
																																																																																																																																10111,
																																																																																																																																new short[] {
																																																																																																																																		103 },
																																																																																																																																new short[] {
																																																																																																																																		10000 },
																																																																																																																																new byte[] {
																																																																																																																																		53 },
																																																																																																																																new double[] {
																																																																																																																																		120 }), CRUSHED_GORAK_CLAW(
																																																																																																																																				9018,
																																																																																																																																				new short[] {
																																																																																																																																						9019 },
																																																																																																																																				new short[] {
																																																																																																																																						9022 },
																																																																																																																																				new byte[] {
																																																																																																																																						57 },
																																																																																																																																				new double[] {
																																																																																																																																						130 }), WIMPY_FEATHER(
																																																																																																																																								11525,
																																																																																																																																								new short[] {
																																																																																																																																										14856 },
																																																																																																																																								new short[] {
																																																																																																																																										14848 },
																																																																																																																																								new byte[] {
																																																																																																																																										58 },
																																																																																																																																								new double[] {
																																																																																																																																										132 }), DRAGON_SCALE_DUST(
																																																																																																																																												241,
																																																																																																																																												new short[] {
																																																																																																																																														105,
																																																																																																																																														2483 },
																																																																																																																																												new short[] {
																																																																																																																																														187,
																																																																																																																																														2454 },
																																																																																																																																												new byte[] {
																																																																																																																																														60,
																																																																																																																																														69 },
																																																																																																																																												new double[] {
																																																																																																																																														137.5,
																																																																																																																																														157.5 }), YEW_ROOTS(
																																																																																																																																																6049,
																																																																																																																																																new short[] {
																																																																																																																																																		5942 },
																																																																																																																																																new short[] {
																																																																																																																																																		5945 },
																																																																																																																																																new byte[] {
																																																																																																																																																		68 },
																																																																																																																																																new double[] {
																																																																																																																																																		155 }), WINE_OF_ZAMORAK(
																																																																																																																																																				245,
																																																																																																																																																				new short[] {
																																																																																																																																																						109 },
																																																																																																																																																				new short[] {
																																																																																																																																																						169 },
																																																																																																																																																				new byte[] {
																																																																																																																																																						72 },
																																																																																																																																																				new double[] {
																																																																																																																																																						162.5 }), POTATO_CACTUS(
																																																																																																																																																								3138,
																																																																																																																																																								new short[] {
																																																																																																																																																										2483 },
																																																																																																																																																								new short[] {
																																																																																																																																																										3042 },
																																																																																																																																																								new byte[] {
																																																																																																																																																										76 },
																																																																																																																																																								new double[] {
																																																																																																																																																										172.5 }), JANGERBERRIES(
																																																																																																																																																												247,
																																																																																																																																																												new short[] {
																																																																																																																																																														111 },
																																																																																																																																																												new short[] {
																																																																																																																																																														189 },
																																																																																																																																																												new byte[] {
																																																																																																																																																														78 },
																																																																																																																																																												new double[] {
																																																																																																																																																														175 }), MAGIC_ROOTS(
																																																																																																																																																																6051,
																																																																																																																																																																new short[] {
																																																																																																																																																																		5951 },
																																																																																																																																																																new short[] {
																																																																																																																																																																		5954 },
																																																																																																																																																																new byte[] {
																																																																																																																																																																		79 },
																																																																																																																																																																new double[] {
																																																																																																																																																																		177.5 }), CRUSHED_BIRD_NEST(
																																																																																																																																																																				6693,
																																																																																																																																																																				new short[] {
																																																																																																																																																																						3002 },
																																																																																																																																																																				new short[] {
																																																																																																																																																																						6687 },
																																																																																																																																																																				new byte[] {
																																																																																																																																																																						81 },
																																																																																																																																																																				new double[] {
																																																																																																																																																																						180 }), POISON_IVY_BERRIES(
																																																																																																																																																																								6018,
																																																																																																																																																																								new short[] {
																																																																																																																																																																										5939 },
																																																																																																																																																																								new short[] {
																																																																																																																																																																										5940 },
																																																																																																																																																																								new byte[] {
																																																																																																																																																																										82 },
																																																																																																																																																																								new double[] {
																																																																																																																																																																										190 });

		private static Map<Short, Ingredients> ingredients = new HashMap<Short, Ingredients>();

		static {
			for (Ingredients ingredient : Ingredients.values()) {
				ingredients.put(ingredient.itemId, ingredient);
			}
		}

		public static Ingredients forId(int itemId) {
			return ingredients.get((short) itemId);
		}

		private final short itemId;
		private final short[] otherItems;
		private final short[] rewards;
		private final byte[] levels;
		private final double[] experience;

		private Ingredients(int itemId, short[] otherItems, short[] rewards, byte[] levels, double[] experience) {
			this.itemId = (short) itemId;
			this.otherItems = otherItems;
			this.rewards = rewards;
			this.levels = levels;
			this.experience = experience;
		}

		public double[] getExperience() {
			return experience;
		}

		public byte[] getLevels() {
			return levels;
		}

		public short[] getRewards() {
			return rewards;
		}

		public byte getSlot(int itemId) {
			for (byte i = 0; i < otherItems.length; i++) {
				if (itemId == otherItems[i]) {
					return i;
				}
			}
			return -1;
		}

	}

	public enum RawIngredient {

		UNICORN_HORN(237, new Item(235, 1)), CHOCOLATE_BAR(1973, new Item(1975, 1)), KEBBIT_TEETH(10109, new Item(10111,
				1)), GORAK_CLAW(9016, new Item(9018, 1)), BIRDS_NEST(5075, new Item(6693, 1)), DESERT_GOAT_HORN(
						9735,
						new Item(9736, 1)), BLUE_DRAGON_SCALES(243, new Item(241, 1)), SPRING_SQ_IRK(10844, new Item(
								10848, 1)), SUMMER_SQ_IRK(10845, new Item(10849, 1)), AUTUMN_SQ_IRK(10846, new Item(
										10850,
										1)), WINTER_SQ_IRK(10847, new Item(10851, 1)), CHARCOAL(973, new Item(704,
												1)), RUNE_SHARDS(6466, new Item(6467, 1)), ASHES(592, new Item(8865,
														1)), POISON_KARAMBWAN(3146, new Item(3152, 1)), SUQAH_TOOTH(
																9079,
																new Item(9082, 1)), FISHING_BAIT(313, new Item(12129,
																		1)), DIAMOND_ROOT(14703, new Item(14704,
																				1)), BLACK_MUSHROOM(4620, new Item(4622,
																						1)), MUD_RUNES(4698, new Item(
																								9594, 1)), WYVERN_BONES(
																										6812,
																										new Item(6810,
																												1));

		private static Map<Short, RawIngredient> rawIngredients = new HashMap<Short, RawIngredient>();

		static {
			for (RawIngredient rawIngredient : RawIngredient.values()) {
				rawIngredients.put(rawIngredient.rawId, rawIngredient);
			}
		}

		public static RawIngredient forId(int itemId) {
			return rawIngredients.get((short) itemId);
		}

		private final short rawId;
		private final Item crushedItem;

		private RawIngredient(int rawId, Item crushedItem) {
			this.rawId = (short) rawId;
			this.crushedItem = crushedItem;
		}

		public Item getCrushedItem() {
			return crushedItem;
		}

		public short getRawId() {
			return rawId;
		}
	}

	private static final short VIAL = 227;
	private static final short COCONUT_MILK = 5935;
	public static final short PESTLE_AND_MORTAR = 233;
	private static final short SWAMP_TAR = 1939;

	public static int isHerbloreSkill(Item first, Item other) {
		Item swap = first;
		Ingredients ingredient = Ingredients.forId(first.getId());
		if (ingredient == null) {
			ingredient = Ingredients.forId(other.getId());
			first = other;
			other = swap;
		}
		if (ingredient != null) {
			int slot = ingredient.getSlot(other.getId());
			return slot > -1 ? ingredient.getRewards()[slot] : -1;
		}
		swap = first;
		RawIngredient raw = RawIngredient.forId(first.getId());
		if (raw == null) {
			raw = RawIngredient.forId(other.getId());
			first = other;
			other = swap;
		}
		if (raw != null)
			return other.getId() == PESTLE_AND_MORTAR ? raw.getCrushedItem().getId() : -1;
		return -1;
	}

	private Item node;
	private Item otherItem;
	private Ingredients ingredients;
	private RawIngredient rawIngredient;
	private int ticks;

	private byte slot;

	public Herblore(Item node, Item otherNode, int amount) {
		this.otherItem = otherNode;
		this.ticks = amount;
		if (node.getId() == PESTLE_AND_MORTAR || otherNode.getId() == PESTLE_AND_MORTAR) {
			this.rawIngredient = RawIngredient.forId(node.getId());
			if (rawIngredient == null) {
				rawIngredient = RawIngredient.forId(otherNode.getId());
				this.node = node;
				this.otherItem = otherNode;
			} else {
				rawIngredient = RawIngredient.forId(node.getId());
				this.node = otherNode;
				this.otherItem = node;
			}
		} else {
			this.ingredients = Ingredients.forId(node.getId());
			if (ingredients == null) {
				ingredients = Ingredients.forId(otherNode.getId());
				this.node = node;
				this.otherItem = otherNode;
			} else {
				ingredients = Ingredients.forId(node.getId());
				this.node = otherNode;
				this.otherItem = node;
			}
		}
	}

	@Override
	public boolean process(Player player) {
		if (player.getInterfaceManager().containsScreenInter()
				|| player.getInterfaceManager().containsInventoryInter()) {
			player.getPackets().sendGameMessage("Please finish what you're doing before doing this action.");
			return false;
		}
		if (!player.getInventory().containsItem(node.getId(), 1)
				|| !player.getInventory().containsItem(otherItem.getId(), 1)) {
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (node.getId() == PESTLE_AND_MORTAR || otherItem.getId() == PESTLE_AND_MORTAR) {
			player.setNextAnimation(new Animation(364));
		} else {
			player.setNextAnimation(new Animation(363));
		}
		ticks--;
		if (otherItem.getId() == SWAMP_TAR || node.getId() == SWAMP_TAR)
			player.sendMessage("You add the " + otherItem.getDefinitions().getName().toLowerCase().replace("clean ", "")
					+ " on the swamp tar.");
		else if (otherItem.getId() == PESTLE_AND_MORTAR || node.getId() == PESTLE_AND_MORTAR)
			player.sendMessage("You crush the " + otherItem.getDefinitions().getName().toLowerCase()
					+ " with your pestle and mortar.");
		else
			player.getPackets().sendGameMessage(
					"You mix the " + otherItem.getDefinitions().getName().toLowerCase() + " into your potion.");
		if (otherItem.getId() == PESTLE_AND_MORTAR || node.getId() == PESTLE_AND_MORTAR)
			player.getInventory().removeItems(new Item(otherItem.getId(), 1));
		if (ingredients != Ingredients.TORSTOL)
			player.getInventory().removeItems(new Item(otherItem.getId(), 1),
					rawIngredient == null ? new Item(node.getId(), 1) : null);

		player.getInventory().addItem(
				rawIngredient != null ? rawIngredient.getCrushedItem() : new Item(ingredients.getRewards()[slot], 1));
		player.getSkills().addXp(Skills.HERBLORE, rawIngredient != null ? 0 : ingredients.getExperience()[slot] / 5);
		if (ticks > 0)
			return 1;
		return -1;
	}

	@Override
	public boolean start(Player player) {
		if (player.getInterfaceManager().containsScreenInter()
				|| player.getInterfaceManager().containsInventoryInter()) {
			player.getPackets().sendGameMessage("Please finish what you're doing before doing this action.");
			return false;
		}
		if (player == null || node == null)
			return false;
		if ((ingredients == null && rawIngredient == null) || otherItem == null)
			return false;
		if (ingredients != null) {
			this.slot = ingredients.getSlot(otherItem.getId());
			if (slot == -1)
				this.slot = ingredients.getSlot(node.getId());
			if (player.getSkills().getLevel(Skills.HERBLORE) < ingredients.getLevels()[slot]) {
				player.sendMessage("You need a herblore level of " + ingredients.getLevels()[slot]
						+ " to combine these ingredients.");
				return false;
			}
			return true;
		}
		return true;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}
