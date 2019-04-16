package com.rs.game.player.skills.summoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.rs.game.Animation;
import com.rs.cores.CoresManager;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.skills.summoning.SummoningPouches.SummoningPouch;
import com.rs.game.player.skills.summoning.SummoningScrolls.SummoningScroll;
import com.rs.game.WorldTile;

public class Summoning {

	public enum Pouches {
		SPIRIT_WOLF("SPIRIT_WOLF", 0, 6829, 67, 12047, 1, 0.1D, 4.8D, 0x57e40L, 1, 12425), 
		DREADFOWL("DREADFOWL", 1, 6825, 6825, 12043, 4, 0.1D, 9.3D, 0x3a980L, 1, 12445), 
		SPIRIT_SPIDER("SPIRIT_SPIDER", 2, 6841, 83, 12059, 8, 0.2D, 12.6D, 0xdbba0L, 2, 12428), 
		THORNY_SNAIL("THORNY_SNAIL", 3, 6806, 119, 12019,13, 0.2D, 12.6D, 0xea600L, 2, 12459), 
		GRANITE_CRAB("GRANITE_CRAB", 4, 6796, 75, 12009, 16, 0.2D, 21.6D, 0x107ac0L, 2, 12533), 
		SPIRIT_MOSQUITO("SPIRIT_MOSQUITO", 5, 7331, 177, 12778, 17, 0.2D, 46.5D, 0xafc80L, 2, 12838), 
		DESERT_WYRM("DESERT_WYRM", 6, 6831, 121, 12049, 18, 0.4D, 31.2D, 0x116520L, 1, 12460), 
		SPIRIT_SCORPIAN("SPIRIT_SCORPIAN", 7, 6837, 101, 12055, 19, 0.9D, 83.200000000000003D, 0xf9060L, 2, 12432), 
		SPIRIT_TZ_KIH("SPIRIT_TZ_KIH", 8, 7361, 179, 12808, 22, 1.1D, 96.8D, 0x107ac0L, 3, 12839), 
		ALBINO_RAT("ALBINO_RAT", 9, 6847, 103, 12067, 23, 2.3D, 202.4D, 0x142440L, 3, 12430), 
		SPIRIT_KALPHITE("SPIRIT_KALPHITE", 10, 6994, 99, 12063, 25, 2.5D, 220D, 0x142440L, 3, 12446), 
		COMPOST_MOUNT("COMPOST_MOUNT", 11, 6871, 137, 12091, 28, 0.6D, 49.8D, 0x15f900L, 6, 12440), 
		GIANT_CHINCHOMPA("GIANT_CHINCHOMPA", 12, 7353, 165, 12800, 29, 2.5D, 255.19999999999999D, 0x1c61a0L, 1, 12834), 
		VAMPYRE_BAT("VAMPYRE_BAT", 13, 6835, 71, 12053, 31, 1.6D, 136D, 0x1e3660L, 4, 12447), 
		HONEY_BADGER("HONEY_BADGER", 14, 6845, 105, 12065, 32, 1.6D, 140.8D, 0x16e360L, 4, 12433), 
		BEAVER("BEAVER", 15, 6808, 89, 12021, 33, 0.7D, 57.6D, 0x18b820L, 4, 12429), 
		VOID_RAVAGER("VOID_RAVAGER", 16, 7370, 157, 12818, 34, 0.7D, 59.6D, 0x18b820L, 4, 12443), 
		VOID_SPINNER("VOID_SPINNER", 17, 7333, 157, 12780, 34, 0.7D, 59.6D, 0x18b820L, 4, 12443), 
		VOID_TORCHER("VOID_TORCHER", 18, 7351, 157, 12798, 34, 0.7D, 59.6D, 0x560f40L, 4, 12443), 
		VOID_SHIFTER("VOID_SHIFTER", 19, 7367, 157, 12814, 34, 0.7D, 59.6D, 0x560f40L, 4, 12443), 
		BULL_ANT("BULL_ANT", 20, 6867, 91, 12087, 40, 0.6D, 52.8D, 0x1b7740L, 5, 12431), 
		BRONZE_MINOTAUR("BRONZE_MINOTAUR", 21, 6853, 149, 12073, 36, 2.4D, 316.8D, 0x1b7740L, 9, 12461), 
		MACAW("MACAW", 22, 6851, 73, 12071, 41, 0.8D, 72.4D, 0x1c61a0L, 5, 12422), 
		EVIL_TURNIP("EVIL_TURNIP", 23, 6833, 77, 12051, 42, 2.1D, 184.8D, 0x1b7740L, 5, 12448), 
		SPIRIT_COCKATRICE("SPIRIT_COCKATRICE", 24, 6875, 149, 12095, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		SPIRIT_GUTHATRICE("SPIRIT_GUTHATRICE", 25, 6877, 149, 12097, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		SPIRIT_SARATRICE("SPIRIT_SARATRICE", 26, 6879, 149, 12099, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		SPIRIT_ZAMATRICE("SPIRIT_ZAMATRICE", 27, 6881, 149, 12101, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		SPIRIT_PENGATRICE("SPIRIT_PENGATRICE", 28, 6883, 149, 12103, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		SPIRIT_CORAXATRICE("SPIRIT_CORAXATRICE", 29, 6885, 149, 12105, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		SPIRIT_VULATRICE("SPIRIT_VULATRICE", 30, 6887, 149, 12107, 43, 0.9D, 75.2D, 0x20f580L, 5, 12458), 
		IRON_MINOTAUR("IRON_MINOTAUR", 31, 6855, 149, 12075, 46, 4.6D, 404.8D, 0x21dfe0L, 9, 12462), 
		PYRELORD("PYRELORD", 32, 7377, 185, 12816, 46, 2.3D, 202.4D, 0x1d4c00L, 5, 12829), 
		MAGPIE("MAGPIE", 33, 6824, 81, 12041, 47, 0.9D, 83.2D, 0x1f20c0L, 5, 12426), 
		BLOATED_LEECH("BLOATED_LEECH", 34, 6843, 131, 12061, 49, 2.4D, 215.2D, 0x1f20c0L, 5, 12444), 
		SPIRIT_TERRORBIRD("SPIRIT_TERRORBIRD", 35, 6794, 129, 12007, 52, 0.7D, 68.4D, 0x20f580L, 6, 12441), 
		ABYSSAL_PARASITE("ABYSSAL_PARASITE", 36, 6818, 125, 12035, 54, 1.1D, 94.8D, 0x1b7740L, 6, 12454), 
		SPIRIT_JELLY("SPIRIT_JELLY", 37, 6992, 123, 12027, 55, 5.5D, 484D, 0x275e20L, 6, 12453), 
		STEEL_MINOTAUR("STEEL_MINOTAUR", 38, 6857, 149, 12077, 56, 5.6D, 492.8D, 0x2a1d40L, 9, 12463), 
		IBIS("IBIS", 39, 6991, 85, 12531, 56, 1.1D, 98.8D, 0x22ca40L, 6, 12424), 
		SPIRIT_KYATT(
																																																																																		"SPIRIT_KYATT",
																																																																																		40,
																																																																																		7365,
																																																																																		169,
																																																																																		12812,
																																																																																		57,
																																																																																		5.7D,
																																																																																		501.6D,
																																																																																		0x2cdc60L,
																																																																																		6,
																																																																																		12836), SPIRIT_LARUPIA(
																																																																																				"SPIRIT_LARUPIA",
																																																																																				41,
																																																																																				7337,
																																																																																				181,
																																																																																				12784,
																																																																																				57,
																																																																																				5.7D,
																																																																																				501.6D,
																																																																																				0x2cdc60L,
																																																																																				6,
																																																																																				12840), SPIRIT_GRAAHK(
																																																																																						"SPIRIT_GRAAHK",
																																																																																						42,
																																																																																						7363,
																																																																																						167,
																																																																																						12810,
																																																																																						57,
																																																																																						5.6D,
																																																																																						501.6D,
																																																																																						0x2cdc60L,
																																																																																						6,
																																																																																						12835), KARAMTHULU_OVERLOAD(
																																																																																								"KARAMTHULU_OVERLOAD",
																																																																																								43,
																																																																																								6809,
																																																																																								135,
																																																																																								12023,
																																																																																								58,
																																																																																								5.8D,
																																																																																								510.4D,
																																																																																								0x284880L,
																																																																																								6,
																																																																																								12455), SMOKE_DEVIL(
																																																																																										"SMOKE_DEVIL",
																																																																																										44,
																																																																																										6865,
																																																																																										133,
																																																																																										12085,
																																																																																										61,
																																																																																										3.1D,
																																																																																										268D,
																																																																																										0x2bf200L,
																																																																																										7,
																																																																																										12468), ABYSSAL_LURKER(
																																																																																												"ABYSSAL_LURKER",
																																																																																												45,
																																																																																												6820,
																																																																																												87,
																																																																																												12037,
																																																																																												62,
																																																																																												1.9D,
																																																																																												109.6D,
																																																																																												0x258960L,
																																																																																												7,
																																																																																												12427), SPIRIT_COBRA(
																																																																																														"SPIRIT_COBRA",
																																																																																														46,
																																																																																														6802,
																																																																																														115,
																																																																																														12015,
																																																																																														63,
																																																																																														3.1D,
																																																																																														276.8D,
																																																																																														0x334500L,
																																																																																														7,
																																																																																														12436), STRANGER_PLANT(
																																																																																																"STRANGER_PLANT",
																																																																																																47,
																																																																																																6827,
																																																																																																141,
																																																																																																12045,
																																																																																																64,
																																																																																																3.2D,
																																																																																																281.6D,
																																																																																																0x2cdc60L,
																																																																																																7,
																																																																																																12467), MITHRIL_MINOTAUR(
																																																																																																		"MITHRIL_MINOTAUR",
																																																																																																		48,
																																																																																																		6859,
																																																																																																		149,
																																																																																																		12079,
																																																																																																		66,
																																																																																																		6.6D,
																																																																																																		580.8D,
																																																																																																		0x325aa0L,
																																																																																																		9,
																																																																																																		12464), BARKER_TOAD(
																																																																																																				"BARKER_TOAD",
																																																																																																				49,
																																																																																																				6889,
																																																																																																				107,
																																																																																																				12123,
																																																																																																				66,
																																																																																																				1.0D,
																																																																																																				87D,
																																																																																																				0x75300L,
																																																																																																				7,
																																																																																																				12452), WAR_TORTOISE(
																																																																																																						"WAR_TORTOISE",
																																																																																																						50,
																																																																																																						6815,
																																																																																																						117,
																																																																																																						12031,
																																																																																																						67,
																																																																																																						0.7D,
																																																																																																						58.6D,
																																																																																																						0x275e20L,
																																																																																																						7,
																																																																																																						12439), BUNYIP(
																																																																																																								"BUNYIP",
																																																																																																								51,
																																																																																																								6813,
																																																																																																								153,
																																																																																																								12029,
																																																																																																								68,
																																																																																																								1.4D,
																																																																																																								119.2D,
																																																																																																								0x284880L,
																																																																																																								7,
																																																																																																								12438), FRUIT_BAT(
																																																																																																										"FRUIT_BAT",
																																																																																																										52,
																																																																																																										6817,
																																																																																																										79,
																																																																																																										12033,
																																																																																																										69,
																																																																																																										1.4D,
																																																																																																										121.2D,
																																																																																																										0x2932e0L,
																																																																																																										7,
																																																																																																										12423), RAVENOUS_LOCUST(
																																																																																																												"RAVENOUS_LOCUST",
																																																																																																												53,
																																																																																																												7372,
																																																																																																												97,
																																																																																																												12820,
																																																																																																												70,
																																																																																																												1.5D,
																																																																																																												132D,
																																																																																																												0x15f900L,
																																																																																																												4,
																																																																																																												12830), ARCTIC_BEAR(
																																																																																																														"ARCTIC_BEAR",
																																																																																																														54,
																																																																																																														6839,
																																																																																																														109,
																																																																																																														12057,
																																																																																																														71,
																																																																																																														1.1D,
																																																																																																														93.2D,
																																																																																																														0x19a280L,
																																																																																																														8,
																																																																																																														12451), PHEONIX(
																																																																																																																"PHEONIX",
																																																																																																																55,
																																																																																																																8575,
																																																																																																																-1,
																																																																																																																14623,
																																																																																																																72,
																																																																																																																3D,
																																																																																																																301D,
																																																																																																																0x1b7740L,
																																																																																																																8,
																																																																																																																14622), OBSIDIAN_GOLEM(
																																																																																																																		"OBSIDIAN_GOLEM",
																																																																																																																		56,
																																																																																																																		7345,
																																																																																																																		173,
																																																																																																																		12792,
																																																																																																																		73,
																																																																																																																		7.3D,
																																																																																																																		642.4D,
																																																																																																																		0x325aa0L,
																																																																																																																		8,
																																																																																																																		12826), GRANITE_LOBSTER(
																																																																																																																				"GRANITE_LOBSTER",
																																																																																																																				57,
																																																																																																																				6849,
																																																																																																																				93,
																																																																																																																				12069,
																																																																																																																				74,
																																																																																																																				3.7D,
																																																																																																																				325.6D,
																																																																																																																				0x2c8e40L,
																																																																																																																				8,
																																																																																																																				12449), PRAYING_MANTIS(
																																																																																																																						"PRAYING_MANTIS",
																																																																																																																						58,
																																																																																																																						6798,
																																																																																																																						95,
																																																																																																																						12011,
																																																																																																																						75,
																																																																																																																						3.6D,
																																																																																																																						329.6D,
																																																																																																																						0x3f2be0L,
																																																																																																																						8,
																																																																																																																						12450), FORGE_REGENT(
																																																																																																																								"FORGE_REGENT",
																																																																																																																								59,
																																																																																																																								7335,
																																																																																																																								187,
																																																																																																																								12782,
																																																																																																																								76,
																																																																																																																								1.5D,
																																																																																																																								134D,
																																																																																																																								0x2932e0L,
																																																																																																																								9,
																																																																																																																								12841), ADAMANT_MINOTAUR(
																																																																																																																										"ADAMANT_MINOTAUR",
																																																																																																																										60,
																																																																																																																										6861,
																																																																																																																										149,
																																																																																																																										12081,
																																																																																																																										76,
																																																																																																																										8.6D,
																																																																																																																										668.8D,
																																																																																																																										0x3c6cc0L,
																																																																																																																										9,
																																																																																																																										12465), TALON_BEAST(
																																																																																																																												"TALON_BEAST",
																																																																																																																												61,
																																																																																																																												7347,
																																																																																																																												143,
																																																																																																																												12794,
																																																																																																																												77,
																																																																																																																												3.7999999999999998D,
																																																																																																																												1015.2D,
																																																																																																																												0x2cdc60L,
																																																																																																																												9,
																																																																																																																												12831), GIANT_ENT(
																																																																																																																														"GIANT_ENT",
																																																																																																																														62,
																																																																																																																														6800,
																																																																																																																														139,
																																																																																																																														12013,
																																																																																																																														78,
																																																																																																																														1.6D,
																																																																																																																														136.8D,
																																																																																																																														0x2cdc60L,
																																																																																																																														8,
																																																																																																																														12457), FIRE_TITAN(
																																																																																																																																"FIRE_TITAN",
																																																																																																																																63,
																																																																																																																																7355,
																																																																																																																																159,
																																																																																																																																12802,
																																																																																																																																79,
																																																																																																																																7.9D,
																																																																																																																																695.2D,
																																																																																																																																0x38c340L,
																																																																																																																																9,
																																																																																																																																12824), MOSS_TITAN(
																																																																																																																																		"MOSS_TITAN",
																																																																																																																																		64,
																																																																																																																																		7357,
																																																																																																																																		159,
																																																																																																																																		12804,
																																																																																																																																		79,
																																																																																																																																		7.9D,
																																																																																																																																		695.2D,
																																																																																																																																		0x38c340L,
																																																																																																																																		9,
																																																																																																																																		12824), ICE_TITAN(
																																																																																																																																				"ICE_TITAN",
																																																																																																																																				65,
																																																																																																																																				7359,
																																																																																																																																				159,
																																																																																																																																				12806,
																																																																																																																																				79,
																																																																																																																																				7.9D,
																																																																																																																																				695.2D,
																																																																																																																																				0x38c340L,
																																																																																																																																				9,
																																																																																																																																				12824), HYDRA(
																																																																																																																																						"HYDRA",
																																																																																																																																						66,
																																																																																																																																						6811,
																																																																																																																																						145,
																																																																																																																																						12025,
																																																																																																																																						80,
																																																																																																																																						1.6D,
																																																																																																																																						140.8D,
																																																																																																																																						0x2cdc60L,
																																																																																																																																						8,
																																																																																																																																						12442), SPIRIT_DAGANNOTH(
																																																																																																																																								"SPIRIT_DAGANNOTH",
																																																																																																																																								67,
																																																																																																																																								6804,
																																																																																																																																								147,
																																																																																																																																								12017,
																																																																																																																																								83,
																																																																																																																																								4.1D,
																																																																																																																																								364.8D,
																																																																																																																																								0x342f60L,
																																																																																																																																								9,
																																																																																																																																								12456), LAVA_TITAN(
																																																																																																																																										"LAVA_TITAN",
																																																																																																																																										68,
																																																																																																																																										7341,
																																																																																																																																										171,
																																																																																																																																										12788,
																																																																																																																																										83,
																																																																																																																																										8.3D,
																																																																																																																																										730.4D,
																																																																																																																																										0x37d8e0L,
																																																																																																																																										9,
																																																																																																																																										12837), SWAMP_TITAN(
																																																																																																																																												"SWAMP_TITAN",
																																																																																																																																												69,
																																																																																																																																												7329,
																																																																																																																																												155,
																																																																																																																																												12776,
																																																																																																																																												85,
																																																																																																																																												4.2D,
																																																																																																																																												373.6D,
																																																																																																																																												0x334500L,
																																																																																																																																												9,
																																																																																																																																												12832), RUNE_MINOTAUR(
																																																																																																																																														"RUNE_MINOTAUR",
																																																																																																																																														70,
																																																																																																																																														6863,
																																																																																																																																														149,
																																																																																																																																														12083,
																																																																																																																																														86,
																																																																																																																																														8.6D,
																																																																																																																																														756.8D,
																																																																																																																																														0x8a3ea0L,
																																																																																																																																														9,
																																																																																																																																														12466), UNICORN_STALLION(
																																																																																																																																																"UNICORN_STALLION",
																																																																																																																																																71,
																																																																																																																																																6822,
																																																																																																																																																113,
																																																																																																																																																12039,
																																																																																																																																																88,
																																																																																																																																																1.8D,
																																																																																																																																																154.4D,
																																																																																																																																																0x317040L,
																																																																																																																																																9,
																																																																																																																																																12434), GEYSER_TITAN(
																																																																																																																																																		"GEYSER_TITAN",
																																																																																																																																																		72,
																																																																																																																																																		7339,
																																																																																																																																																		161,
																																																																																																																																																		12786,
																																																																																																																																																		89,
																																																																																																																																																		8.9D,
																																																																																																																																																		783.2D,
																																																																																																																																																		0x3f2be0L,
																																																																																																																																																		10,
																																																																																																																																																		12833), WOLPERTINGER(
																																																																																																																																																				"WOLPERTINGER",
																																																																																																																																																				73,
																																																																																																																																																				6869,
																																																																																																																																																				151,
																																																																																																																																																				12089,
																																																																																																																																																				92,
																																																																																																																																																				4.6D,
																																																																																																																																																				404.8D,
																																																																																																																																																				0x38c340L,
																																																																																																																																																				10,
																																																																																																																																																				12437), ABYSSAL_TITAN(
																																																																																																																																																						"ABYSSAL_TITAN",
																																																																																																																																																						74,
																																																																																																																																																						7349,
																																																																																																																																																						175,
																																																																																																																																																						12796,
																																																																																																																																																						93,
																																																																																																																																																						1.9D,
																																																																																																																																																						163.2D,
																																																																																																																																																						0x1d4c00L,
																																																																																																																																																						10,
																																																																																																																																																						12827), IRON_TITAN(
																																																																																																																																																								"IRON_TITAN",
																																																																																																																																																								75,
																																																																																																																																																								7375,
																																																																																																																																																								183,
																																																																																																																																																								12822,
																																																																																																																																																								95,
																																																																																																																																																								8.6D,
																																																																																																																																																								417.6D,
																																																																																																																																																								0x36ee80L,
																																																																																																																																																								10,
																																																																																																																																																								12828), PACK_YAK(
																																																																																																																																																										"PACK_YAK",
																																																																																																																																																										76,
																																																																																																																																																										6873,
																																																																																																																																																										111,
																																																																																																																																																										12093,
																																																																																																																																																										96,
																																																																																																																																																										4.8D,
																																																																																																																																																										422.2D,
																																																																																																																																																										0x3519c0L,
																																																																																																																																																										10,
																																																																																																																																																										12435), STEEL_TITAN(
																																																																																																																																																												"STEEL_TITAN",
																																																																																																																																																												77,
																																																																																																																																																												7343,
																																																																																																																																																												163,
																																																																																																																																																												12790,
																																																																																																																																																												99,
																																																																																																																																																												4.9D,
																																																																																																																																																												435.2D,
																																																																																																																																																												0x3a9800L,
																																																																																																																																																												10,
																																																																																																																																																												12825);

		private int npcId;
		private int pouchId;
		private int level;
		private int spawnCost;
		private double useExp;
		private double creationExp;
		private int configId;
		private long time;
		private int scrollId;

		private Pouches(String s, int i, int npcId, int configId, int pouchId, int level, double useExp,
				double creationExp, long time, int spawnCost, int scrollId) {
			this.npcId = npcId;
			this.pouchId = pouchId;
			this.level = level;
			this.spawnCost = spawnCost;
			this.useExp = useExp;
			this.creationExp = creationExp;
			this.time = time;
			this.scrollId = scrollId;
		}

		private static final HashMap<Integer, Pouches> POUCHES = new HashMap<Integer, Pouches>();

		static {
			for (Pouches p : values()) {
				POUCHES.put(p.getPouchId(), p);
			}
		}

		public static Pouches forId(int pouchId) {
			return POUCHES.get(pouchId);
		}

		public int getConfigId() {
			return configId;
		}

		public double getCreationExp() {
			return creationExp;
		}

		public int getLevel() {
			return level;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getPouchId() {
			return pouchId;
		}

		public int getScrollId() {
			return scrollId;
		}

		public int getSpawnCost() {
			return spawnCost;
		}

		public long getTime() {
			return time;
		}

		public double getUseExp() {
			return useExp;
		}
	}

	private static Familiar createFamiliar(Player player, Pouches pouch) {
		String loc = "com.rs.game.npc.familiar.";
		try {
			Familiar fam = (Familiar) Class
					.forName(loc
							+ NPCDefinitions.getNPCDefinitions(pouch.getNpcId()).name.replace(" ", "").replace("-", ""))
					.getConstructor(
							new Class[] { Player.class, Pouches.class, WorldTile.class, Integer.TYPE, Boolean.TYPE })
					.newInstance(new Object[] { player, pouch, player, Integer.valueOf(-1), Boolean.valueOf(true) });
			if (fam != null) {
				return fam;
			}
		} catch (Throwable e) {
			// Logger.handle(e);
			System.out.println("Missing: "
					+ NPCDefinitions.getNPCDefinitions(pouch.getNpcId()).name.replace(" ", "").replace("-", "")
					+ " - NPC Id: " + pouch.getNpcId());
		}
		return null;
	}

	private static final int OK = 0, END = 1, SPACE = 2;

	@SuppressWarnings("unlikely-arg-type")
	public static void createPouch(final Player player, int itemId, int amount) {
		SummoningPouch pouch = SummoningPouch.get(itemId);
		if (pouch == null) {
			player.getPackets().sendGameMessage("You do not have the items required to create this pouch.");
			return;
		}
		if (player.getSkills().getLevelForXp(Skills.SUMMONING) < pouch.getLevelRequired()) {
			player.getPackets()
					.sendGameMessage("Your summoning level is not high enough to make this pouch. You need a level of "
							+ pouch.getLevelRequired() + ".");
			return;
		}
		int realId = pouch.getPouchId();
		player.getInterfaceManager().closeScreenInterface();
		if (amount > 28)
			amount = 28;
		int status = OK;
		int created = 0;
		int skip[] = { 12158, 12160, 12163, 12159, 12183, 12155 };
		ArrayList<Integer> skipOver = new ArrayList<Integer>();
		for (int i = 0; i < skip.length; i++)
			skipOver.add(skip[i]);
		for (int i = 0; i < amount; i++) {
			for (int x = 0; x < pouch.getItems().length; x++) {
				if (!player.getInventory().containsItem(pouch.getItems()[x].getId(), pouch.getItems()[x].getAmount())) {
					status = END;
					break;
				}
				if (pouch.getItems()[x].getDefinitions().isStackable()
						&& !skipOver.contains(pouch.getItems()[x].getId())) {
					if (!player.getInventory().hasFreeSlots()) {
						status = SPACE;
						break;
					}
				}
			}
			if (status > 0)
				break;
			for (int y = 0; y < pouch.getItems().length; y++)
				player.getInventory().deleteItem(pouch.getItems()[y].getId(), pouch.getItems()[y].getAmount());
			player.getInventory().addItem(new Item(pouch.getPouchId()));
			player.getSkills().addXp(Skills.SUMMONING, pouch.getSummonExperience() * 63);
			created++;
		}
		if (created == 1)
			player.getPackets().sendGameMessage(
					"You infuse a " + ItemDefinitions.getItemDefinitions(realId).getName().toLowerCase() + ".");
		else if (created > 0)
			player.getPackets().sendGameMessage(
					"You infuse some " + ItemDefinitions.getItemDefinitions(realId).getName().toLowerCase() + "es.");
		else {
			if (status >= SPACE)
				player.getPackets().sendGameMessage("You do not have enough inventory space to do this.");
			else
				player.getPackets().sendGameMessage("You do not have enough of the required items to create this pouch.");
			return;
		}
		player.setNextAnimation(new Animation(9068));
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				/**
				 * Has to send this, otherwise the animation will loop on and on
				 * for some reason.
				 */
				player.setNextAnimation(new Animation(-1));
			}
		}, 3800, TimeUnit.MILLISECONDS);
	}

	public static void transformScrolls(final Player player, int itemId, int amount) {
		SummoningScroll scroll = SummoningScroll.get(itemId);
		if (scroll == null) {
			player.getPackets().sendGameMessage("You do not have the pouch required to make this scroll.");
			return;
		}
		if (player.getSkills().getLevelForXp(Skills.SUMMONING) < scroll.getLevelRequired()) {
			player.getPackets().sendGameMessage("You do not have the level required to make this scroll.");
			return;
		}
		int realId = scroll.getItemId();
		player.getInterfaceManager().closeScreenInterface();
		int created = 0;
		for (int i = 0; i < amount; i++) {
			if (!player.getInventory().containsItem(scroll.getPouch().getId(), 1))
				break;
			player.getInventory().deleteItem(scroll.getPouch().getId(), 1);
			player.getInventory().addItem(new Item(scroll.getItemId(), 10));
			player.getSkills().addXp(Skills.SUMMONING, scroll.getExperience());
			created++;
		}
		if (created > 0)
			player.getPackets().sendGameMessage(
					"You transform some " + ItemDefinitions.getItemDefinitions(realId).getName().toLowerCase() + ".");
		else {
			player.getPackets().sendGameMessage("You do not have enough of the required pouch to create this scroll.");
			return;
		}
		player.setNextAnimation(new Animation(9068));
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				/**
				 * Has to send this, otherwise the animation will loop on and on
				 * for some reason.
				 */
				player.setNextAnimation(new Animation(-1));
			}
		}, 3800, TimeUnit.MILLISECONDS);
	}

	public static void spawnFamiliar(Player player, Pouches pouch) {
		if (player.getFamiliar() != null || player.getPet() != null) {
			player.getPackets().sendGameMessage("You already have a follower.");
			return;
		}
		if (!player.getControlerManager().canSummonFamiliar())
			return;
		if (player.getSkills().getLevelForXp(Skills.SUMMONING) < pouch.getLevel()) {
			player.getPackets()
					.sendGameMessage("You need to have a summoning level of " + pouch.getLevel() + " to summon this.");
			return;
		}
		if (player.getSkills().getLevel(Skills.SUMMONING) < pouch.getSpawnCost()) {
			player.getPackets().sendGameMessage("You don't have enough summoning points to summon that familiar.");
			return;
		}
		final Familiar npc = createFamiliar(player, pouch);
		if (npc == null) {
			player.getPackets().sendGameMessage("This familiar is not added yet.");
			return;
		}
		player.getInterfaceManager().sendSummoning();
		player.getInventory().deleteItem(pouch.getPouchId(), 1);
		player.getSkills().drainSummoning(pouch.getSpawnCost());
		player.setFamiliar(npc);
		player.getPackets().sendButtonConfig(168, 8);
	}

	public static void sendScrollInterface(Player player) {
		player.getInterfaceManager().sendInterface(673);
		Object[] options = new Object[] { "Infuse-X<col=FF9040>", "Infuse-All<col=FF9040>", "Infuse-10<col=FF9040>",
				"Infuse-5<col=FF9040>", "Infuse<col=FF9040>", 20, 4, 673 << 16 | 15 };
		player.getPackets().sendRunScript(763, options);
		player.getPackets().sendIComponentSettings(673, 15, 0, 462, 62);
	}

	public static void sendPouchInterface(Player player) {
		player.getInterfaceManager().sendInterface(669);
		Object[] options = new Object[] { "Infuse-X<col=FF9040>", "Infuse-All<col=FF9040>", "Infuse-10<col=FF9040>",
				"Infuse-5<col=FF9040>", "Infuse<col=FF9040>", 20, 4, 669 << 16 | 15 };
		player.getPackets().sendIComponentSettings(669, 15, 0, 190, 62);
		player.getPackets().sendRunScript(757, options);
	}

	public static boolean hasPouch(Player player) {
		for (Pouches pouch : Pouches.values())
			if (player.getInventory().containsOneItem(pouch.pouchId))
				return true;
		return false;
	}
}