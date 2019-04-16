package com.rs.game.player.skills.slayer;

import com.rs.game.player.Player;
import com.rs.Settings;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.utils.ShopsHandler;
import com.rs.utils.Utils;
import com.rs.game.Entity;

public class Slayer {

	public enum SlayerMaster {

		SPRIA(8462, 85, 1, new int[] { 0, 0, 0 }, new int[] { 15, 50 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.BEAR, SlayerTask.COW, SlayerTask.BIRD, SlayerTask.CAVE_BUG, SlayerTask.CAVE_SLIME,
				SlayerTask.DWARF, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DWARF,
				SlayerTask.GHOST, SlayerTask.GOBLIN, SlayerTask.MINOTAUR, SlayerTask.MONKEY, SlayerTask.SCORPION,
				SlayerTask.SKELETON, SlayerTask.SPIDER, SlayerTask.WOLF, SlayerTask.ZOMBIE),

		TURAEL(8461, 3, 1, new int[] { 0, 0, 0 }, new int[] { 15, 50 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.BEAR, SlayerTask.COW, SlayerTask.BIRD, SlayerTask.CAVE_BUG, SlayerTask.CAVE_SLIME,
				SlayerTask.DWARF, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DWARF,
				SlayerTask.GHOST, SlayerTask.GOBLIN, SlayerTask.MINOTAUR, SlayerTask.MONKEY, SlayerTask.SCORPION,
				SlayerTask.SKELETON, SlayerTask.SPIDER, SlayerTask.WOLF, SlayerTask.ZOMBIE),

		MAZCHNA(8464, 20, 1, new int[] { 2, 5, 15 }, new int[] { 20, 60 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.CATABLEPON, SlayerTask.CAVE_CRAWLER, SlayerTask.COCKATRICE, SlayerTask.CRAWLING_HAND,
				SlayerTask.CYCLOPS, SlayerTask.DESERT_LIZARD, SlayerTask.DOG, SlayerTask.FLESH_CRAWLER,
				SlayerTask.GHOUL, SlayerTask.GHOST, SlayerTask.HILL_GIANT, SlayerTask.HOBGOBLIN, SlayerTask.ICE_WARRIOR,
				SlayerTask.KALPHITE, SlayerTask.PYREFIEND, SlayerTask.ROCKSLUG, SlayerTask.SKELETON, SlayerTask.VAMPYRE,
				SlayerTask.WOLF, SlayerTask.ZOMBIE),

		ACHTRYN(8465, 20, 1, new int[] { 2, 5, 15 }, new int[] { 20, 60 }, SlayerTask.BANSHEE, SlayerTask.BAT,
				SlayerTask.CATABLEPON, SlayerTask.CAVE_CRAWLER, SlayerTask.COCKATRICE, SlayerTask.CRAWLING_HAND,
				SlayerTask.CYCLOPS, SlayerTask.DESERT_LIZARD, SlayerTask.DOG, SlayerTask.FLESH_CRAWLER,
				SlayerTask.GHOUL, SlayerTask.GHOST, SlayerTask.HILL_GIANT, SlayerTask.HOBGOBLIN, SlayerTask.ICE_WARRIOR,
				SlayerTask.KALPHITE, SlayerTask.PYREFIEND, SlayerTask.ROCKSLUG, SlayerTask.SKELETON, SlayerTask.VAMPYRE,
				SlayerTask.WOLF, SlayerTask.ZOMBIE),

		VANNAKA(1597, 40, 1, new int[] { 4, 20, 60 }, new int[] { 30, 70 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ANKOU, SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLOODVELD, SlayerTask.BRINE_RAT,
				SlayerTask.COCKATRICE, SlayerTask.CROCODILE, SlayerTask.CYCLOPS, SlayerTask.DUST_DEVIL,
				SlayerTask.EARTH_WARRIOR, SlayerTask.GHOUL, SlayerTask.GREEN_DRAGON, SlayerTask.HARPIE_BUG_SWARM,
				SlayerTask.HILL_GIANT, SlayerTask.ICE_GIANT, SlayerTask.ICE_WARRIOR, SlayerTask.INFERNAL_MAGE,
				SlayerTask.JELLY, SlayerTask.JUNGLE_HORROR, SlayerTask.LESSER_DEMON, SlayerTask.MOLANISK,
				SlayerTask.MOSS_GIANT, SlayerTask.OGRE, SlayerTask.OTHERWORLDLY_BEING, SlayerTask.PYREFIEND,
				SlayerTask.SHADE, SlayerTask.SHADOW_WARRIOR, SlayerTask.TUROTH, SlayerTask.VAMPYRE,
				SlayerTask.WEREWOLF),

		CHAELDAR(1598, 70, 1, new int[] { 10, 50, 100 }, new int[] { 40, 80 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON,
				SlayerTask.BRINE_RAT, SlayerTask.BRONZE_DRAGON, SlayerTask.CAVE_CRAWLER, SlayerTask.CAVE_HORROR,
				SlayerTask.CRAWLING_HAND, SlayerTask.DAGANNOTH, SlayerTask.DUST_DEVIL, SlayerTask.ELF_WARRIOR,
				SlayerTask.FEVER_SPIDER, SlayerTask.FIRE_GIANT, SlayerTask.GARGOYLE,
				SlayerTask.HARPIE_BUG_SWARM, SlayerTask.INFERNAL_MAGE, SlayerTask.JELLY, SlayerTask.JUNGLE_HORROR,
				SlayerTask.KALPHITE, SlayerTask.KURASK, SlayerTask.LESSER_DEMON, SlayerTask.ZYGOMITE,
				SlayerTask.SHADOW_WARRIOR, SlayerTask.TUROTH, SlayerTask.VYREWATCH, SlayerTask.WARPED_TORTOISE),

		SUMONA(7780, 85, 35, new int[] { 12, 60, 180 }, new int[] { 50, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLACK_DEMON,
				SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON, SlayerTask.CAVE_CRAWLER, SlayerTask.CAVE_HORROR,
				SlayerTask.DAGANNOTH, SlayerTask.DUST_DEVIL, SlayerTask.ELF_WARRIOR, SlayerTask.GARGOYLE,
				SlayerTask.GREATER_DEMON, SlayerTask.HELLHOUND, SlayerTask.IRON_DRAGON, SlayerTask.KALPHITE,
				SlayerTask.KURASK, SlayerTask.NECHRYAEL, SlayerTask.RED_DRAGON, SlayerTask.SCABARITE,
				SlayerTask.SPIRITUAL_MAGE, SlayerTask.SPIRITUAL_WARRIOR, SlayerTask.TERROR_DOG, SlayerTask.TROLL,
				SlayerTask.TUROTH, SlayerTask.VYREWATCH),

		DURADEL(8466, 100, 50, new int[] { 15, 75, 225 }, new int[] { 50, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD,
				SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT,
				SlayerTask.GARGOYLE, SlayerTask.GORAK, SlayerTask.GREATER_DEMON, SlayerTask.HELLHOUND,
				SlayerTask.IRON_DRAGON, SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON, SlayerTask.NECHRYAEL,
				SlayerTask.SCABARITE, SlayerTask.SKELETAL_WYVERN, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON,
				SlayerTask.SUQAH, SlayerTask.VYREWATCH, SlayerTask.WARPED_TERRORBIRD, SlayerTask.WATERFIEND),

		LAPALOK(8467, 100, 50, new int[] { 15, 75, 225 }, new int[] { 50, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD,
				SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT,
				SlayerTask.GARGOYLE, SlayerTask.GORAK, SlayerTask.GREATER_DEMON, SlayerTask.HELLHOUND,
				SlayerTask.IRON_DRAGON, SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON, SlayerTask.NECHRYAEL,
				SlayerTask.SCABARITE, SlayerTask.SKELETAL_WYVERN, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON,
				SlayerTask.SUQAH, SlayerTask.VYREWATCH, SlayerTask.WARPED_TERRORBIRD, SlayerTask.WATERFIEND),

		KURADAL(9085, 110, 75, new int[] { 18, 90, 270 }, new int[] { 25, 100 }, SlayerTask.ABERRANT_SPECTRE,
				SlayerTask.ABYSSAL_DEMON, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD,
				SlayerTask.BLUE_DRAGON, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DUST_DEVIL,
				SlayerTask.FIRE_GIANT, SlayerTask.GARGOYLE, SlayerTask.HELLHOUND, SlayerTask.IRON_DRAGON,
				SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON, SlayerTask.NECHRYAEL,
				SlayerTask.SKELETAL_WYVERN, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON, SlayerTask.SUQAH,
				SlayerTask.TERROR_DOG, SlayerTask.TZHAAR, SlayerTask.VYREWATCH, SlayerTask.WARPED_TORTOISE,
				SlayerTask.WATERFIEND);

		private SlayerTask[] task;
		private int[] tasksRange, pointsRange;
		private int requriedCombatLevel, requiredSlayerLevel, npcId;

		SlayerMaster(int npcId, int requriedCombatLevel, int requiredSlayerLevel, int[] pointsRange,
				int[] tasksRange, SlayerTask... task) {
			this.npcId = npcId;
			this.requriedCombatLevel = requriedCombatLevel;
			this.requiredSlayerLevel = requiredSlayerLevel;
			this.pointsRange = pointsRange;
			this.tasksRange = tasksRange;
			this.task = task;
		}

		public int getNPCId() {
			return npcId;
		}

		public int getRequiredCombatLevel() {
			return requriedCombatLevel;
		}

		public int getRequiredSlayerLevel() {
			return requiredSlayerLevel;
		}

		public SlayerTask[] getTask() {
			return task;
		}

		public int[] getTasksRange() {
			return tasksRange;
		}

		public int[] getPointsRange() {
			return pointsRange;
		}

		public static boolean startInteractionForId(Player player, int npcId, int option) {
			for (SlayerMaster master : SlayerMaster.values()) {
				if (master.getNPCId() == npcId) {
					if (option == 1)
						player.getDialogueManager().startDialogue("SlayerMasterD", npcId, master);
					else if (option == 2)
						player.getDialogueManager().startDialogue("QuickTaskD", master);
					else if (option == 3)
						ShopsHandler.openShop(player, 29);
					else if (option == 4)
						player.getSlayerManager().sendSlayerInterface();
					return true;
				}
			}
			return false;
		}
	}

	public enum SlayerTask {// 79 matches out of 117

		MIGHTY_BANSHEE(37, 38,
				new String[] { "Banshees are fearsome creatures with a mighty scream.",
						"You need something to cover your ears", "Beware of their scream. Banshees are found",
						"in the Slayer Tower." }),

		MIGHTY_BANSEE(37, 38,
				new String[] { "Banshees are fearsome creatures with a mighty scream.",
						"You need something to cover your ears", "Beware of their scream. Banshees are found",
						"in the Slayer Tower." },
				MIGHTY_BANSHEE),

		BANSHEE(15, 38,
				new String[] { "Banshees are fearsome creatures with a mighty scream.",
						" You need something to cover your ears", "Beware of their scream. Banshees are found",
						"in the Slayer Tower." },
				MIGHTY_BANSEE),

		BAT(1, 8, new String[] { "Bats can be found within the darkest depths",
						"of "+ Settings.SERVER_NAME +".",
						"They're relatively easy to slay.",
						"As a substitute, giant bats may be slain." }),

		AVIANSIE(1, 5, new String[] {}),

		CHICKEN(1, 5, new String[] {}),

		CHOMPY_BIRD(1, 5, new String[] {}),

		DUCK(1, 5, new String[] {}),

		BIRD(1, 5, new String[] {
				"Birds are a type of species found throughout " + Settings.SERVER_NAME + " in different forms.",
				"It's recomended that you bring range weapons to fight these monsters.",
				"Avansies are the strongest and most widely known type of bird.",
				"Chickens are great for a fast task." },
				AVIANSIE, CHICKEN, CHOMPY_BIRD, DUCK),

		BEAR(1, 13, new String[] {
				"Bears can be found all across " + Settings.SERVER_NAME + ", however in very small quantities.",
				"It is suggested to slay them between the Legends' guild and Ardougne,",
				"as you can find the most there." }),

		CAVE_BUG(7, 63, new String[] { "Cave bugs can be found within the darkest dungeons, such as",
				"the Lumbridge swamp cavern. Make sure to bring a source of light when you go slaying",
				"them. They require level 7 Slayer to be harmed. Watch out though, as they regenerate very quickly." }),

		CAVE_SLIME(17, 62,
				new String[] { "Cave slimes are poisonous monsters found",
						"in the Lumbridge swamp cavern, as well as within the Dorgesh-Kaan",
						"southern dungeons. They require level 17 slayed to be harmed." }),

		COW(1, 6,
				new String[] { "Cows can be found near many farms all across " + Settings.SERVER_NAME + ".",
						"They're fairly easy to slay and they always drop cowhides,",
						"which are often used to crafters." }),

		ZOMBIE_HAND(5, 39, new String[] {}),

		SKELETAL_HAND(5, 39, new String[] {}),

		CRAWLING_HAND(5, 39,
				new String[] { "Crawling hands are monsters that",
						"require level 5 Slayer to be harmed. They are one of the few",
						"monsters that may sometimes die in a perfect form, allowing you to",
						"mount them in your house. Zombie and skeletal hands may be slain",
						"as a substitute." },
				ZOMBIE_HAND, SKELETAL_HAND),

		DWARF(1, 57,
				new String[] { "Dwarves are known to drop items related to",
						"mining and smithing. They're relatively easy to slay.",
						"You may find them within the mining guild and Keldagrim." }),

		LIZARD(22, 68, null),

		DESERT_LIZARD(22, 68,
				new String[] { "Desert lizards can be found in the",
						"Kharidian desert. They require level 22 slayer to be harmed,",
						"as well as some ice coolers to finish them off. It is advised",
						"to bring a water source when travelling within the desert,",
						"to avoid the negative effects of the heat." },
				LIZARD),

		REVENANT(1, 12, new String[] {}),

		GHOST(1, 12,
				new String[] { "Ghosts can be found within the darkest ",
						"dungeons of " + Settings.SERVER_NAME + ". As undead monsters, ",
						"the salve amulet will be used while fighting them.",
						"As an alternative for the experienced players,",
						"all kinds of revenants found within the Forinthry dungeon",
						"may also be slain." },
				REVENANT),

		GOBLIN(1, 2, new String[] { "Goblins are ugly green creatures found all across" + Settings.SERVER_NAME + ".",
				"They are often slain by newcomers all around Lumbridge. You shouldn't have problems slaying them." }),

		ICEFIEND(1, 75, new String[] { "Icefiends can be found on top of the Ice mountain.",
				"They are known to be fairly powerful as compared to other monsters of ",
				"their level." }),

		MINOTAUR(1, 76,
				new String[] { "Minotaurs are furry ungulates found in the Vault of War",
						"within the Stronghold of Security. They are known for their iron arrow and",
						"right skull half drops, of which the latter is used in combining the skull sceptre." }),

		MONKEY(1, 1,
				new String[] { "Monkeys can be found in most jungle areas, such as Karamja.",
						"They are very easily slain. However beware of the surrounding monsters,",
						"as jungles can often lead to fatal injuries." }),

		SCORPION(1, 7, new String[] { "Scorpions can often be found in desert areas, but not exclusively.",
				"You may also spot them in the mining guild, as well as their poisonous relatives",
				"within the Taverley dungeon. Don't forget to bring an antipoison when slaying the latter." }),

		SKELETON(1, 11,
				new String[] { "Skeletons are a common undead monster found all over",
						"" + Settings.SERVER_NAME + ". Like zombies, they are the result of a",
						"necromancer reanimating the bodies of the dead. As such, they are",
						"commonly found in tombs, graveyards and dungeons." },
				SKELETAL_HAND),

		SPIDER(1, 4,
				new String[] { "Spiders are found all over " + Settings.SERVER_NAME + ",",
						"but most notably in the third floor of the Stronghold of Security.",
						"They are known to be agressive." }),

		WOLVE(1, 9, new String[] {}),

		WOLF(1, 9,
				new String[] { "Wolves can be found all across " + Settings.SERVER_NAME + ".",
						"They're known to vary from low levels to some-what high. It's suggested",
						"to slay them on White Wolf mountain, as the most lay there." },
				WOLVE),

		ZOMBIE(1, 10,
				new String[] { "Zombies are most often found in graveyards,",
						"dungeons and tombs. Like Skeletons, they are the result of",
						"Necromancers reanimating the bodies of the dead.",
						"Different types of zombies may be slain as a substitute,",
						"such as armoured or undead zombies." }),

		CATABLEPON(1, 78,
				new String[] { "Catablepon are monsters that look like green bulls",
						"with reptilian tails. They are found in the Pit of Pestilence,",
						"the third level of the Stronghold of Security. They use the magic spell",
						"Weaken to drain up to 15% of their opponent's maximum Strength level, so",
						"melee isn't advised for lower levels." }),

		CAVE_CRAWLER(10, 37,
				new String[] { "Cave crawlers are poisonous creatures",
						"that require level 10 Slayer to be harmed. They're known to have",
						"a fast healing rate, so killing them as a low level can be quite tricky." }),

		DOG(1, 22,
				new String[] { "Dogs can be found by McGrobor's woods. They're",
						"agressive creatures that may cause difficulties for lower levels.",
						"Terror dogs will NOT count towards this task." }),

		FLESH_CRAWLER(1, 77,
				new String[] { "Flesh crawlers are monsters known to",
						"hit fast, but inaccurately. They can be found within the second floor",
						"of the Stronghold of Security. They often drop large numbers of",
						"herbs, which may be used by or sold to people looking to train Herblore." }),

		HOBGOBLIN(1, 21,
				new String[] { "Hobgoblins are Bandosian monsters who share",
						"characteristics with both goblins and orks. They can be found",
						"within the Godwars dungeon, as well as the Edgeville dungeon.",
						"They're known to drop limpwurt roots, which can be used by or sold to",
						"people looking to train Herblore." }),

		KALPHITE(1, 53,
				new String[] { "Kalphites, also known as Kalphiscarabeinae,",
						"are a species that resemble a mix of scarab beetles and cockroaches.",
						"They can be found within their hive north of Bedabin camp." }),

		ROCKSLUG(20, 51,
				new String[] { "Rockslugs are slayer monsters that require",
						"level 20 Slayer to be harmed. A bag of salt is necessary to slay them.",
						"They can be found within the Fremennik slayer dungeon, Lumbridge swamp cavern or",
						"Dorgesh-Kaan southern dungeon." }),

		ROCK_SLUG(20, 51,
				new String[] { "Rockslugs are slayer monsters that require",
						"level 20 Slayer to be harmed. A bag of salt is necessary to slay them.",
						"They can be found within the Fremennik slayer dungeon, Lumbridge swamp cavern or",
						"Dorgesh-Kaan southern dungeon." },
				ROCKSLUG),

		ABERRANT_SPECTRE(60, 12,
				new String[] { "Aberrant spectres are known to be an excellent",
						"source of herbs, as well as high-priced seeds. They can be found within the Slayer",
						"tower. They require level 60 Slayer to be harmed. A face protection against their",
						"stench is necessary to avoid quick death." }),

		ANKOU(1, 79, new String[] { "Ankou are undead monsters found on the fourth level of the ",
				"Stronghold of Security, and in the Wilderness Volcano. They are neither a",
				"skeleton nor a ghost, but a combination of both. Salve amulet will aid in killing ",
				"them." }),

		BASILISK(40, 43,
				new String[] { "Basilisks are monsters that require",
						" at least level 40 Slayer to be harmed. Like cockatrices, ",
						"players must equip a mirror shield in order to fight them. ",
						"They can be found within the Fremennik slayer dungeon." }),

		BLOODVELD(50, 48,
				new String[] { "Bloodveld are monsters requiring at least",
						"level 50 Slayer to be harmed. They are most often slain within the",
						"Godwars dungeon, by the Zamorak chamber, as well as at the Slayer tower." }),

		BRINE_RAT(47, 84,
				new String[] { "Brine rats are monsters that require at least",
						"level 47 Slayer to be harmed. They can be found in the Brine Rat cavern,",
						"which requires a spade to enter. They're notable for their",
						"brine sabre drops." }),

		COCKATRICE(25, 44,
				new String[] { "Cockatrice are winged reptile that require at least",
						"level 25 Slayer to be harmed. A mirror shield is",
						"necessary to avoid their stat-draining attacks.",
						"They can be found within the Fremennik dungeon." }),

		CROCODILE(1, 65,
				new String[] { "Crocodiles can be found in and near water in the",
						"Kharidian desert. Due to the strong desert heat, it is advised to ",
						"carry a water source with you while travelling in desert." }),

		CYCLOPS(1, 108,
				new String[] { "Cyclopes are monsters primarily found on the ",
						"top floor of the Warriors' guild, but also within the Godwars dungeon.",
						"They are known to be the followers of Bandos" }),

		CYCLOPSE(1, 108,
				new String[] { "Cyclopes are monsters primarily found on the ",
						"top floor of the Warriors' guild, but also within the Godwars dungeon.",
						"They are known to be the followers of Bandos" },
				CYCLOPS),

		DUST_DEVIL(65, 49,
				new String[] { "Dust devils use clouds of dust, sand, ash ",
						"and whatever else they can inhale to blind and disorientate their victims. ",
						"Good luck on obtaining a dragon chainmail from their dusty remains." }),

		EARTH_WARRIOR(1, 54,
				new String[] { "Earth warriors are often slain by people trying",
						"to get their hands on the Earth warrior Champion scroll. They can be found ",
						"within the Edgeville dungeon and Chaos tunnels." }),

		GHOUL(1, 23,
				new String[] { "Ghouls are a humanoid race and the descendants of a",
						"long-dead society that degraded to the point that its people ate their dead.",
						"They can be found just north of Canifis. They are not undead." }),

		GREEN_DRAGON(1, 24,
				new String[] { "Green dragons are relatives to the infamous Elvarg. ",
						"They can be found in multiple locations over the Wilderness.",
						" Protection from dragonfire is necessary when fighting them." }),

		HARPIE_BUG_SWARM(33, 70,
				new String[] { "Harpie bug swarms are insectoid monsters that",
						"require at least level 33 Slayer to be harmed. A lit bug lantern is necessary to harm them,",
						"for that reason, level 33 Firemaking is also considered a requirement. They can be ",
						"found east of the Jogre dungeon." }),

		HILL_GIANT(1, 14,
				new String[] { "Hill giants are large foes most highly concentrated in",
						"the Edgeville dungeon. They're known to drop lots of limpwurt roots, which ",
						"are highly sought after by people looking to train Herblore." }),

		ICE_GIANT(1, 15, new String[] { "Ice giants are generally found within the Asgarnian ice dungeon,",
				"but also in Chaos tunnels and at the Frozen Waste Plateau. They're weak to fire spells." }),

		ICE_WARRIOR(1, 19, new String[] { "Ice warriors are monsters similar to a knight in blue in their",
				"appearance, wearing somewhat translucent armour. They're known to live around icy areas.",
				"Lower level players should beware as they use fast slashing attacks that deal fairly high damage." }),

		INFERNAL_MAGE(45, 40,
				new String[] { "Infernal magi are monsters that require at least",
						"level 45 slayer to be harmed. They can be found within the Slayer tower and",
						"chaos tunnels. Dragonhide equipment is advised when fighting them." }),

		JELLY(52, 50,
				new String[] { "Jellies are monsters requiriring at least level",
						"52 Slayer to be harmed. They're most often slain within the Fremennik slayer",
						"dungeon. They're commonly killed for clue scrolls." }),

		JUNGLE_HORROR(1, 81,
				new String[] { "Jungle horrors are relatives to Cave horrors. They're known",
						"to live on the island of Mos Le'Harmless. They are",
						"agressive beasts that strictly use melee." }),

		LESSER_DEMON(1, 28, new String[] { "Lesser demons are part of the Avernic race, which were",
				"tribal and primitive, and slaves to whoever was ruling",
				"Infernus at the time. They're known to be weak to magic.",
				"They can be found in Taverley dungeon, Crandor island and Karamja dungeon." }),

		MOLANISK(39, 87,
				new String[] { "Molanisks are monsters that require at least level ",
						 "39 Slayer to be harmed. A slayer bell is necessary to fight them. However, ",
						 "players shouldn't forget to bring a light source when fighting them, as they ",
						 "can only be found underground, in the Dorgesh-Kaan southern dungeon." }),

		MOSS_GIANT(1, 17, new String[] { "Moss giants are large creatures that are weak to melee.",
						 "They can be found within the Varrock sewers, Chaos tunnels,",
						 "on the Crandor isle and many more locations all over",
						 "" + Settings.SERVER_NAME + "." }),

		OGRE(1, 20,
				new String[] { "Ogres are most notably found in the ogre city of",
						"Gu'Tanoth. Ogres are known to hit fairly high, but have very low defence.",
						"They're primarily slain using Ranged." }),

		OTHERWORLDLY_BEING(1, 55,
				new String[] { "Otherworldly Beings are ethereal beings making",
						"them weak to ranged attacks. They can be found in Zanaris.",
						"They're notable for their relatively high chance of",
						"dropping dragon helmets." }),

		PYREFIEND(30, 47,
				new String[] { "Pyrefiends are monsters that require at least level",
						"30 slayer to be harmed. They're known to be stronger than",
						"their relatives icefiends. They can be found within the Fremennik",
						"slayer dungeon, as well as in Godwars dungeon." }),

		SHADE(1, 64,
				new String[] { "Shades are monsters found in the bottom floor of the",
						"Stronghold of Security. A salve amulet is effectively used to slay them. Additionally,",
						"different species of shades may be slain in Mort'ton." }),

		SHADOW_WARRIOR(1, 32,
				new String[] { "Shadow warriors are deceased knights, found in the",
						"basement of the Legends' guild. They're however not considered",
						"dead. However lower level players should beware",
						"of their relatively fast attacks, considering their weapon." }),

		TUROTH(55, 36, new String[] { "Turoths are monsters that require at least",
				"level 55 Slayer to be harmed. They're often slain for their common herb and seed",
				"drops. A leaf-bladed sword, Slayers dart, Broad arrows or bolts must be used to",
				"kill them." }),

		VAMPYRE(1, 34,
				new String[] { "Vampyres are feral vampyres that may be found fighting ",
						"for Zamorak in the God Wars Dungeon, as well as in",
						"Morytania. Most vampyres are known to have immunity",
						"towards non-silver and blisterwood weapons." }),

		WEREWOLF(1, 33,
				new String[] { "Werewolves can be found in Canifis and Godwars dungeon.",
						"They appear as humans until provoked by one, in which case they will transform into a werewolf,",
						"unless a Wolfbane dagger is wielded." }),

		BLUE_DRAGON(1, 25,
				new String[] { "Blue dragons are the second weakest of the chromatic",
						"dragons, but shouldn't be underestimated. Protection from dragonfire is essential when",
						"fighting these dragons." }),

		BRONZE_DRAGON(1, 58,
				new String[] { "Bronze dragons are the weakest of metal dragons,",
						"however they shouldn't be underestimated even by the greatest of warriors.",
						"Protection from dragonfire is essential when fighting them.",
						"Bronze dragons do NOT drop the highly sought-after draconic visage." }),

		CAVE_HORROR(58, 80,
				new String[] { "Cave horrors are slayer monsters",
						"that require at least level 58 Slayer to kill, as well as a Witchwood icon",
						"and a light source. They're often hunted for their black mask drops." }),

		DAGANNOTH(1, 35,
				new String[] { "Dagannoths are sea monsters found around the fremennik areas.",
						"They're often slain for their hides, which is used to make strong armour. A balmung",
						"is suggested when slaying them, as it hits them very hard." }),

		ELF_WARRIOR(1, 56,
				new String[] { "Elf warriors can be found in Lletya. They're",
						"known for theyr crystal teleport seed drops, which are useful for",
						"teleporting around Tirannwn, as well as their frequent gem drops." }),

		FEVER_SPIDER(42, 69,
				new String[] { "Fever spiders are monsters",
						"that require at least level 42 Slayer to be harmed.",
						"They can be found in the brewery basement on Braindeath island.",
						"Slayer gloves are necessary to avoid their high-damage attacks.",
						"They're also known to disease people." }),

		FIRE_GIANT(1, 16, new String[] { "Fire giants are the second strongest giants",
				"in "+ Settings.SERVER_NAME +".",
				"They're well known for their high",
				"lifepoints, yet low defence, which makes them an excellent",
				"choice of training for lower-level players." }),

		GARGOYLE(75, 46,
				new String[] { "Gargoyles are monsters that require at least",
						"level 75 Slayer to be harmed. They can be found in Slayer tower, as well as in",
						"Kuradal's dungeon. Although most don't know this, they're a species of",
						"demons, which makes them weak to silver- and darklight." }),

		KURASK(70, 45,
				new String[] { "Kurask are large, green, armoured monsters that",
						"can be found in the second to last chamber of the Fremennik Slayer Dungeon",
						"and on the lower level of the Pollnivneach Slayer Dungeon. A leaf-bladed sword,",
						"Slayer dart, Broad arrows or Broad bolts must be used to kill them." }),

		FUNGI(57, 74, new String[] {}),

		ZYGOMITE(57, 74,
				new String[] { "Zygomites are monsters that require at least",
						"level 57 Slayer to be harmed. They appear as fungi until provoked,",
						"after which they'll become agressive towards you." },
				FUNGI),

		VYRELORD(31, 110, new String[] {}),

		VYRELADY(31, 110, new String[] {}),

		VYREWATCH(31, 110,
				new String[] { "The Vyrewatch are the vampyric vanguard",
						"that receive blood tithes from the population of Morytania and",
						"suppress any resistance to the Drakan overlords. They're only known to",
						"be vulnerable to the Ivandis flail and Blisterwood weaponry." },
				VYRELORD, VYRELADY),

		WARPED_TORTOISE(56, 92,
				new String[] { "Warped tortoises can be found within",
						"the Poison Waste Slayer dungeon. They require at least level",
						"56 Slayer to be harmed. They're known to be immune to all damage,",
						"unless you bring a crystal chime with you." }),

		ABYSSAL_DEMON(85, 42,
				new String[] { "Abyssal demons are one of the strongest",
						"demons that require at least level 85 Slayer to be harmed.",
						"As demons, they're known to be weak to the Silverlight and Darklight.",
						"Players often kill them for much sought-after Abyssal whips." }),

		AQUANITE(78, 95,
				new String[] { "Aquanites are monsters that require",
						"at least level 78 Slayer to be harmed. Aquanites are known to",
						"drop large amounts of seeds, as well as the powerful",
						"amulet of ranging." }),

		BLACK_DEMON(1, 30,
				new String[] { "Black demons are monsters that",
						"can be found within the Taverley and Edgeville dungeons, as well",
						"as a couple other dungeons in " + Settings.SERVER_NAME + ". They're",
						"often slain for their Starved ancient effigy drops." }),

		DESERT_STRYKEWYRM(77, 104, new String[] { "Desert strykewyrms are huge worms",
				"located east of Al-Kharid. They require at least level",
				"77 Slayer to be harmed. You'll first need to gather their",
				"attention, before killing them." }),

		GREATER_DEMON(1, 29,
				new String[] { "Greater demons are known to be followers",
						"of Zamorak. As demons, they're weak to Silverlight and Darklight.",
						"They can be found within the dungeons of Forinthry and Brimhaven." }),

		HELLHOUND(1, 31,
				new String[] { "Hellhounds are fairly strong foes that can be",
						"found in the dungeons of Forinthry and Taverley. They're known for their",
						"very high drop rate for Clue scrolls." }),

		IRON_DRAGON(1, 59,
				new String[] { "Iron dragons are strong monsters",
						"that are famous for their Draconic visage drops. Protection from",
						"dragonfire is necessary when fighting against them. They can be found",
						"in Brimhaven and Kuradal's dungeon." }),

		NECHRYAEL(80, 52,
				new String[] { "Nechryael are Chthonian demons that require at least level",
						"80 Slayer to be harmed. They can be found within Slayer tower and Chaos tunnels.",
						"They're known to be summoners of their spawnlings, so players should beware",
						"when fighting them." }),

		RED_DRAGON(1, 26,
				new String[] { "Red dragons are the second strongest chromatic dragons.",
						"Protection from dragonfire is necessary when fighting them, to avoid",
						"fatal injuries. As an alternative, baby red dragons may be slain too." }),

		LOCUST(1, 85, new String[] {}),

		SCABARAS(1, 85, new String[] {}),

		SCARAB(1, 85, new String[] {}),

		SCABARITE(1, 85,
				new String[] { "Scarabites are swarms of insects that are",
						"known to hit very fast, making them extremely dangerous for lower-levelled",
						"players. They can be found within the Jaldraocht Pyramid." },
				LOCUST, SCABARAS, SCARAB),

		SPIRITUAL_MAGE(83, 91,
				new String[] { "The Spiritual mage is a potent magic user that",
						"can easily kill low-level players. They're the only monster known",
						"to drop dragon boots. They require at least level 83 Slayer to be harmed." }),

		SPIRITUAL_WARRIOR(68, 89,
				new String[] { "The Spiritual warrior is a powerful melee user",
						"inhabiting God Wars Dungeon that can easily kill low-level players. They require",
						"at least level 68 Slayer to be harmed. They can be found within the Godwars dungeon" }),

		TERROR_DOG(40, 86,
				new String[] { "Terror dogs are found at the end of Tarn's Lair.",
						"They can be harmed with at least level 40 Slayer, and players can access them",
						"very quickly using the Tarn's Lair teleport on the ring of slaying." }),

		ROCK(1, 18, new String[] {}),

		TROLL(1, 18,
				new String[] { "Trolls are one of the more unique,",
						"though not very intelligent races. They're known to be in Trollheim.",
						"They can be killed in various shapes and sizes." },
				ROCK),

		BLACK_DRAGON(1, 27,
				new String[] { "Black dragons are the strongest of",
						"chromatic dragons. They're often slain for the infamous Draconic visage drops.",
						"They can be found in the lava and Taverley dungeon." }),

		DARK_BEAST(90, 66,
				new String[] { "Dark beasts are large, bull-like creatures",
						"found in the Mourner Tunnels on the path to the Temple of Light",
						"and inside Kuradal's Dungeon. They require at least level 90 Slayer to be harmed." }),

		GORAK(1, 82,
				new String[] { "Goraks can be found within the",
						"Godwars dungeon. They're known to be followers of Zamorak. For lower-levelled",
						"players, Goraks may be a little bit difficult to fight." }),

		MITHRIL_DRAGON(1, 94, new String[] { "Mithril dragons are metal dragons found in the Ancient Cavern "
				+ "and the extended area of the Brimhaven Dungeon resource dungeon. They're one of the strongest"
				+ " metallic dragons. They're often killed for the infamous draconic visage and dragon full helm." }),

		SKELETAL_WYVERN(72, 72,
				new String[] { "Skeletal Wyverns are undead slayer monsters",
						"that require at least level 72 Slayer to be harmed. They can be found within",
						"the asgarnian ice dungeon. They're also known to drop the draconic visage." }),

		STEEL_DRAGON(1, 60,
				new String[] { "Steel dragons are metal dragons and can be found in the",
						"Brimhaven Dungeon, Kuradal's Dungeon and the Fortress of Ghorrock. They're very strong",
						"towards melee and ranged, therefore magic is suggested when fighting them.",
						"They're also known to drop the infamous draconic visage." }),

		SUQAH(1, 83,
				new String[] { "Suqah are a race of strange-looking but powerful",
						"creatures native to Lunar Isle. They're known to be fairly easy opponents,",
						"however their magic attacks can stack up and deal deadly amounts of damage." }),

		WARPED_TERRORBIRD(56, 93,
				new String[] { "The Warped terrorbird is a Slayer monster found in",
						"the Poison Waste Slayer Dungeon. They require at least level 56 Slayer and a Crystal chime",
						"to be harmed. They're known to be weak to melee." }),

		WATERFIEND(1, 88,
				new String[] { "Waterfiends are elemental creatures who lie within",
						"the ancient cavern, the chaos tunnels as well as in the Ghorrock fortress.",
						"They are weak to ranged, bolts specifically." }),

		LIVING_ROCK(1, 106,
				new String[] { "Living rock creatures can be found throughout the",
						"Living rock caverns. They're known to be surprisingly weak for their level.",
						"It is advised for great miners to bring a pickaxe with them." }),

		TZHAAR(1, 101,
				new String[] { "TzHaar are a race of golem-like creatures that inhabit",
						"the TzHaar city. Despite their fearsome and powerful appearance, they're",
						"actually a peaceful race who've existed beneath the surface of Karamja",
						"for thousands of years." });

		private String[] tips;
		private SlayerTask[] alternatives;
		private int levelRequried;
		private int neverAssignValue;

		private SlayerTask(int levelRequried, int neverAssignValue, String[] tips, SlayerTask... alternatives) {
			this.levelRequried = levelRequried;
			this.neverAssignValue = neverAssignValue;
			this.tips = tips;
			this.alternatives = alternatives;
		}

		public String[] getTips() {
			return tips;
		}

		public SlayerTask[] getAlternatives() {
			return alternatives;
		}

		public int getLevelRequried() {
			return levelRequried;
		}

		public int getNeverAssignValue() {
			return neverAssignValue;
		}

		public String getName() {
			return Utils.formatPlayerNameForDisplay(toString());
		}
	}
	
	public static boolean hasReflectiveEquipment(Entity target) {
		if (!(target instanceof Player))
			return true;
		Player targetPlayer = (Player) target;
		int shieldId = targetPlayer.getEquipment().getShieldId();
		return shieldId == 4156;
	}
	
	public static boolean hasNosepeg(Entity target) {
		if (!(target instanceof Player))
			return true;
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 4168 || hasSlayerHelmet(target);
	}
	
	public static boolean hasSlayerHelmet(Entity target) {
		if (!(target instanceof Player))
			return true;
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 13263 || hat == 14636 || hat == 14637 || hasFullSlayerHelmet(target);
	}
	
	public static boolean hasFullSlayerHelmet(Entity target) {
		if (!(target instanceof Player))
			return true;
		Player targetPlayer = (Player) target;
		int hat = targetPlayer.getEquipment().getHatId();
		return hat == 15492 || hat == 15496 || hat == 15497 || (hat >= 22528 && hat <= 22550);
	}

	public static boolean canAttackNPC(int slayerLevel, String name) {
		return slayerLevel >= getLevelRequirement(name);
	}

	public static int getLevelRequirement(String name) {
		for (SlayerTask task : SlayerTask.values()) {
			if (name.toLowerCase().contains(task.toString().replace("_", " ").toLowerCase())) {
				return task.getLevelRequried();
			}
		}
		return 1;
	}
	
	public static void killedTask(Player player, NPC npc) {
		player.getSlayerManager().setCount(player.getSlayerManager().getCount() - 1);
		player.getSkills().addXp(Skills.SLAYER, npc.getCombatDefinitions().getHitpoints() / 3);
		if (player.getSlayerManager().getCount() == 0) {
		//player.sendMessage("You have completed your slayer task, you now have: "+ player.getSlayerManager().getSlayerPoints() +" Slayer points.");
		player.getSlayerManager().resetTask(true, false);
		}
	}
}
