package com.rs.game.player.skills.summoning;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.item.Item;

/**
 * Represents all of the summoning scrolls.
 * 
 * @author Byte Me
 */

public class SummoningScrolls {

	public enum SummoningScroll {

		HOWL_SCROLL(0, 12425, 1, 0.1, new Item(12047)), DREADFOWL_STRIKE_SCROLL(1, 12445, 4, 0.1,
				new Item(12043)), EGG_SPAWN_SCROLL(2, 12428, 4, 10.2, new Item(12059)), SLIME_SPRAY_SCROLL(3, 12459, 13,
						0.2, new Item(12019)), STONY_SHELL_SCROLL(4, 12533, 16, 0.2, new Item(12009)), PESTER_SCROLL(5,
								12838, 17, 0.5, new Item(12778)), ELECTRIC_LASH_SCROLL(6, 12460, 18, 0.4,
										new Item(12049)), VENOM_SHOT_SCROLL(7, 12432, 19, 0.9,
												new Item(12055)), FIREBALL_ASSAULT_SCROLL(8, 12839, 22, 1.1,
														new Item(12808)), CHEESE_FEAST_SCROLL(9, 12430, 23, 2.3,
																new Item(12067)), SANDSTORM_SCROLL(10, 12446, 25, 2.5,
																		new Item(12064)), GENERATE_COMPOST_SCROLL(11,
																				12440, 28, 0.6,
																				new Item(12091)), EXPLODE_SCROLL(12,
																						12834, 29, 2.9,
																						new Item(
																								12800)), VAMPIRE_TOUCH_SCROLL(
																										13, 12447, 31,
																										1.5,
																										new Item(
																												12053)), INSANE_FEROCITY_SCROLL(
																														14,
																														12433,
																														32,
																														1.6,
																														new Item(
																																12065)), MULTICHOP_SCROLL(
																																		15,
																																		12429,
																																		33,
																																		0.7,
																																		new Item(
																																				12021)), CALL_TO_ARMS_SCROLL(
																																						16,
																																						12443,
																																						34,
																																						0.7,
																																						new Item(
																																								12818)), UNBURDEN_SCROLL(
																																										20,
																																										12431,
																																										40,
																																										0.6,
																																										new Item(
																																												12087)), HERBCALL_SCROLL(
																																														21,
																																														12422,
																																														41,
																																														0.8,
																																														new Item(
																																																12071)), EVIL_FLAMES_SCROLL(
																																																		22,
																																																		12448,
																																																		42,
																																																		2.1,
																																																		new Item(
																																																				12051)), PETRIFYING_GAZE_SCROLL(
																																																						23,
																																																						12458,
																																																						43,
																																																						0.9,
																																																						new Item(
																																																								12095)), PETRIFYING_GAZE_SCROLL2(
																																																										24,
																																																										12458,
																																																										43,
																																																										0.9,
																																																										new Item(
																																																												12097)), PETRIFYING_GAZE_SCROLL3(
																																																														25,
																																																														12458,
																																																														43,
																																																														0.9,
																																																														new Item(
																																																																12099)), PETRIFYING_GAZE_SCROLL4(
																																																																		26,
																																																																		12458,
																																																																		43,
																																																																		0.9,
																																																																		new Item(
																																																																				12101)), PETRIFYING_GAZE_SCROLL5(
																																																																						27,
																																																																						12458,
																																																																						43,
																																																																						0.9,
																																																																						new Item(
																																																																								12103)), PETRIFYING_GAZE_SCROLL6(
																																																																										28,
																																																																										12458,
																																																																										43,
																																																																										0.9,
																																																																										new Item(
																																																																												12105)), PETRIFYING_GAZE_SCROLL7(
																																																																														29,
																																																																														12458,
																																																																														43,
																																																																														0.9,
																																																																														new Item(
																																																																																12107)), IMMENSE_HEAT_SCROLL(
																																																																																		30,
																																																																																		12829,
																																																																																		46,
																																																																																		2.3,
																																																																																		new Item(
																																																																																				12816)), THIEVING_FINGERS_SCROLL(
																																																																																						31,
																																																																																						12426,
																																																																																						47,
																																																																																						0.9,
																																																																																						new Item(
																																																																																								12041)), BLOOD_DRAIN_SCROLL(
																																																																																										32,
																																																																																										12444,
																																																																																										49,
																																																																																										2.4,
																																																																																										new Item(
																																																																																												12061)), TIRELESS_RUN_SCROLL(
																																																																																														33,
																																																																																														12441,
																																																																																														52,
																																																																																														0.8,
																																																																																														new Item(
																																																																																																12007)), ABYSSAL_DRAIN_SCROLL(
																																																																																																		34,
																																																																																																		12454,
																																																																																																		54,
																																																																																																		1.1,
																																																																																																		new Item(
																																																																																																				12035)), DISSOLVE_SCROLL(
																																																																																																						35,
																																																																																																						12453,
																																																																																																						55,
																																																																																																						5.5,
																																																																																																						new Item(
																																																																																																								12027)), FISH_RAIN_SCROLL(
																																																																																																										36,
																																																																																																										12424,
																																																																																																										56,
																																																																																																										1.1,
																																																																																																										new Item(
																																																																																																												12531)), AMBUSH_SCROLL(
																																																																																																														37,
																																																																																																														12836,
																																																																																																														57,
																																																																																																														5.7,
																																																																																																														new Item(
																																																																																																																12812)), RENDING_SCROLL(
																																																																																																																		38,
																																																																																																																		12840,
																																																																																																																		57,
																																																																																																																		5.7,
																																																																																																																		new Item(
																																																																																																																				12784)), GOAD_SCROLL(
																																																																																																																						39,
																																																																																																																						12835,
																																																																																																																						57,
																																																																																																																						5.7,
																																																																																																																						new Item(
																																																																																																																								12710)), DOOMSPHERE_SCROLL(
																																																																																																																										40,
																																																																																																																										12455,
																																																																																																																										58,
																																																																																																																										5.8,
																																																																																																																										new Item(
																																																																																																																												12023)), DUST_CLOUD_SCROLL(
																																																																																																																														41,
																																																																																																																														12468,
																																																																																																																														61,
																																																																																																																														3.1,
																																																																																																																														new Item(
																																																																																																																																12085)), ABYSSAL_STEALTH_SCROLL(
																																																																																																																																		42,
																																																																																																																																		12427,
																																																																																																																																		62,
																																																																																																																																		1.9,
																																																																																																																																		new Item(
																																																																																																																																				12037)), OPHIDIAN_INCUBATION_SCROLL(
																																																																																																																																						43,
																																																																																																																																						12436,
																																																																																																																																						63,
																																																																																																																																						3.2,
																																																																																																																																						new Item(
																																																																																																																																								12015)), POISONOUS_BLAST_SCROLL(
																																																																																																																																										44,
																																																																																																																																										12467,
																																																																																																																																										64,
																																																																																																																																										3.2,
																																																																																																																																										new Item(
																																																																																																																																												12045)), TOAD_BARK_SCROLL(
																																																																																																																																														45,
																																																																																																																																														12452,
																																																																																																																																														66,
																																																																																																																																														1.0,
																																																																																																																																														new Item(
																																																																																																																																																12123)), TESTUDO_SCROLL(
																																																																																																																																																		46,
																																																																																																																																																		12439,
																																																																																																																																																		67,
																																																																																																																																																		0.7,
																																																																																																																																																		new Item(
																																																																																																																																																				12031)), SWALLOW_WHOLE_SCROLL(
																																																																																																																																																						47,
																																																																																																																																																						12438,
																																																																																																																																																						68,
																																																																																																																																																						1.4,
																																																																																																																																																						new Item(
																																																																																																																																																								12029)), FRUITFALL_SCROLL(
																																																																																																																																																										48,
																																																																																																																																																										12423,
																																																																																																																																																										69,
																																																																																																																																																										1.4,
																																																																																																																																																										new Item(
																																																																																																																																																												12033)), FAMINE_SCROLL(
																																																																																																																																																														49,
																																																																																																																																																														12830,
																																																																																																																																																														70,
																																																																																																																																																														1.4,
																																																																																																																																																														new Item(
																																																																																																																																																																12820)), ARCTIC_BLAST_SCROLL(
																																																																																																																																																																		50,
																																																																																																																																																																		12451,
																																																																																																																																																																		71,
																																																																																																																																																																		1.1,
																																																																																																																																																																		new Item(
																																																																																																																																																																				12057)), RISE_FROM_THE_ASHES_SCROLL(
																																																																																																																																																																						51,
																																																																																																																																																																						14622,
																																																																																																																																																																						72,
																																																																																																																																																																						8.0,
																																																																																																																																																																						new Item(
																																																																																																																																																																								14623)), VOLCANIC_STRENGTH_SCROLL(
																																																																																																																																																																										52,
																																																																																																																																																																										12826,
																																																																																																																																																																										73,
																																																																																																																																																																										7.3,
																																																																																																																																																																										new Item(
																																																																																																																																																																												12792)), CRUSHING_CLAW_SCROLL(
																																																																																																																																																																														53,
																																																																																																																																																																														12449,
																																																																																																																																																																														74,
																																																																																																																																																																														3.7,
																																																																																																																																																																														new Item(
																																																																																																																																																																																12069)), MANTIS_STRIKE_SCROLL(
																																																																																																																																																																																		54,
																																																																																																																																																																																		12459,
																																																																																																																																																																																		75,
																																																																																																																																																																																		3.7,
																																																																																																																																																																																		new Item(
																																																																																																																																																																																				12011)), INFERNO_SCROLL(
																																																																																																																																																																																						55,
																																																																																																																																																																																						12841,
																																																																																																																																																																																						76,
																																																																																																																																																																																						1.5,
																																																																																																																																																																																						new Item(
																																																																																																																																																																																								12782)), DEADLY_CLAW_SCROLL(
																																																																																																																																																																																										56,
																																																																																																																																																																																										12831,
																																																																																																																																																																																										77,
																																																																																																																																																																																										11.4,
																																																																																																																																																																																										new Item(
																																																																																																																																																																																												12794)), ACORN_MISSILE_SCROLL(
																																																																																																																																																																																														57,
																																																																																																																																																																																														12457,
																																																																																																																																																																																														78,
																																																																																																																																																																																														1.6,
																																																																																																																																																																																														new Item(
																																																																																																																																																																																																12013)), TITANS_CONSTITUTION_SCROLL(
																																																																																																																																																																																																		58,
																																																																																																																																																																																																		12824,
																																																																																																																																																																																																		79,
																																																																																																																																																																																																		7.9,
																																																																																																																																																																																																		new Item(
																																																																																																																																																																																																				12802)), TITANS_CONSTITUTION_SCROLL2(
																																																																																																																																																																																																						59,
																																																																																																																																																																																																						12824,
																																																																																																																																																																																																						79,
																																																																																																																																																																																																						7.9,
																																																																																																																																																																																																						new Item(
																																																																																																																																																																																																								12804)), TITANS_CONSTITUTION_SCROLL3(
																																																																																																																																																																																																										60,
																																																																																																																																																																																																										12824,
																																																																																																																																																																																																										79,
																																																																																																																																																																																																										7.9,
																																																																																																																																																																																																										new Item(
																																																																																																																																																																																																												12806)), REGROWTH_SCROLL(
																																																																																																																																																																																																														61,
																																																																																																																																																																																																														12442,
																																																																																																																																																																																																														80,
																																																																																																																																																																																																														1.6,
																																																																																																																																																																																																														new Item(
																																																																																																																																																																																																																12025)), SPIKE_SHOT_SCROLL(
																																																																																																																																																																																																																		62,
																																																																																																																																																																																																																		12456,
																																																																																																																																																																																																																		83,
																																																																																																																																																																																																																		4.1,
																																																																																																																																																																																																																		new Item(
																																																																																																																																																																																																																				12017)), EBON_THUNDER_SCROLL(
																																																																																																																																																																																																																						63,
																																																																																																																																																																																																																						12837,
																																																																																																																																																																																																																						83,
																																																																																																																																																																																																																						8.3,
																																																																																																																																																																																																																						new Item(
																																																																																																																																																																																																																								12788)), SWAMP_PLAGUE_SCROLL(
																																																																																																																																																																																																																										64,
																																																																																																																																																																																																																										12832,
																																																																																																																																																																																																																										85,
																																																																																																																																																																																																																										4.1,
																																																																																																																																																																																																																										new Item(
																																																																																																																																																																																																																												12776)), BRONZE_BULL_RUSH_SCROLL(
																																																																																																																																																																																																																														65,
																																																																																																																																																																																																																														12461,
																																																																																																																																																																																																																														36,
																																																																																																																																																																																																																														3.6,
																																																																																																																																																																																																																														new Item(
																																																																																																																																																																																																																																12073)), IRON_BULL_RUSH_SCROLL(
																																																																																																																																																																																																																																		66,
																																																																																																																																																																																																																																		12462,
																																																																																																																																																																																																																																		46,
																																																																																																																																																																																																																																		4.6,
																																																																																																																																																																																																																																		new Item(
																																																																																																																																																																																																																																				12075)), STEEL_BULL_RUSH_SCROLL(
																																																																																																																																																																																																																																						67,
																																																																																																																																																																																																																																						12463,
																																																																																																																																																																																																																																						56,
																																																																																																																																																																																																																																						5.6,
																																																																																																																																																																																																																																						new Item(
																																																																																																																																																																																																																																								12077)), MITHRIL_BULL_RUSH_SCROLL(
																																																																																																																																																																																																																																										68,
																																																																																																																																																																																																																																										12464,
																																																																																																																																																																																																																																										66,
																																																																																																																																																																																																																																										6.6,
																																																																																																																																																																																																																																										new Item(
																																																																																																																																																																																																																																												12079)), ADAMANT_BULL_RUSH_SCROLL(
																																																																																																																																																																																																																																														69,
																																																																																																																																																																																																																																														12465,
																																																																																																																																																																																																																																														76,
																																																																																																																																																																																																																																														7.6,
																																																																																																																																																																																																																																														new Item(
																																																																																																																																																																																																																																																12081)), RUNE_BULL_RUSH_SCROLL(
																																																																																																																																																																																																																																																		70,
																																																																																																																																																																																																																																																		12466,
																																																																																																																																																																																																																																																		86,
																																																																																																																																																																																																																																																		8.6,
																																																																																																																																																																																																																																																		new Item(
																																																																																																																																																																																																																																																				12083)), HEALING_AURA_SCROLL(
																																																																																																																																																																																																																																																						71,
																																																																																																																																																																																																																																																						12434,
																																																																																																																																																																																																																																																						88,
																																																																																																																																																																																																																																																						1.8,
																																																																																																																																																																																																																																																						new Item(
																																																																																																																																																																																																																																																								12039)), BOIL_SCROLL(
																																																																																																																																																																																																																																																										72,
																																																																																																																																																																																																																																																										12833,
																																																																																																																																																																																																																																																										89,
																																																																																																																																																																																																																																																										8.9,
																																																																																																																																																																																																																																																										new Item(
																																																																																																																																																																																																																																																												12786)), MAGIC_FOCUS_SCROLL(
																																																																																																																																																																																																																																																														73,
																																																																																																																																																																																																																																																														12437,
																																																																																																																																																																																																																																																														92,
																																																																																																																																																																																																																																																														4.6,
																																																																																																																																																																																																																																																														new Item(
																																																																																																																																																																																																																																																																12089)), ESSENCE_SHIPMENT_SCROLL(
																																																																																																																																																																																																																																																																		74,
																																																																																																																																																																																																																																																																		12827,
																																																																																																																																																																																																																																																																		93,
																																																																																																																																																																																																																																																																		1.9,
																																																																																																																																																																																																																																																																		new Item(
																																																																																																																																																																																																																																																																				12796)), IRON_WITHIN_SCROLL(
																																																																																																																																																																																																																																																																						75,
																																																																																																																																																																																																																																																																						12828,
																																																																																																																																																																																																																																																																						95,
																																																																																																																																																																																																																																																																						4.7,
																																																																																																																																																																																																																																																																						new Item(
																																																																																																																																																																																																																																																																								12822)), WINTER_STORAGE_SCROLL(
																																																																																																																																																																																																																																																																										76,
																																																																																																																																																																																																																																																																										12435,
																																																																																																																																																																																																																																																																										96,
																																																																																																																																																																																																																																																																										4.8,
																																																																																																																																																																																																																																																																										new Item(
																																																																																																																																																																																																																																																																												12093)), STEEL_OF_LEGENDS_SCROLL(
																																																																																																																																																																																																																																																																														77,
																																																																																																																																																																																																																																																																														12825,
																																																																																																																																																																																																																																																																														99,
																																																																																																																																																																																																																																																																														4.9,
																																																																																																																																																																																																																																																																														new Item(
																																																																																																																																																																																																																																																																																12790));

		public static SummoningScroll get(int itemId) {
			return SCROLLS.get(itemId);
		}

		private static final Map<Integer, SummoningScroll> SCROLLS = new HashMap<Integer, SummoningScroll>();

		static {
			for (SummoningScroll scroll : SummoningScroll.values()) {
				SCROLLS.put(scroll.slotId, scroll);
			}
		}

		private final int slotId, itemId, levelRequired;

		private final double experience;

		private final Item pouch;

		private SummoningScroll(int slotId, int itemId, int levelRequired, double experience, Item pouch) {
			this.slotId = slotId;
			this.itemId = itemId;
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.pouch = pouch;
		}

		public int getSlotId() {
			return slotId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperience() {
			return experience;
		}

		public Item getPouch() {
			return pouch;
		}

	}
}
