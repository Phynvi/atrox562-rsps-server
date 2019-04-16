package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;
import com.rs.game.item.Item;

public class ItemConstants {

	public static int getDegradeItemWhenWear(int id) {
		// pvp armors
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967 || id == 13970 || id == 13973 || id == 13858
				|| id == 13861 || id == 13864 || id == 13867 || id == 13870 || id == 13873 || id == 13876 || id == 13884
				|| id == 13887 || id == 13890 || id == 13893 || id == 13896 || id == 13899 || id == 13902 || id == 13905
				|| id == 13908 || id == 13911 || id == 13914 || id == 13917 || id == 13920 || id == 13923 || id == 13926
				|| id == 13929 || id == 13932 || id == 13935 || id == 13938 || id == 13941 || id == 13944 || id == 13947
				|| id == 13950 || id == 13958)
			return id + 2; // if you wear it it becomes corrupted LOL
		return -1;
	}

	// return amt of charges
	public static int getItemDefaultCharges(int id) {
		// pvp armors
		if (id == 13910 || id == 13913 || id == 13916 || id == 13919 || id == 13922 || id == 13925 || id == 13928
				|| id == 13931 || id == 13934 || id == 13937 || id == 13940 || id == 13943 || id == 13946 || id == 13949
				|| id == 13952)
			return 1500;
		if (id == 13960 || id == 13963 || id == 13966 || id == 13969 || id == 13972 || id == 13975)
			return 3000;
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869 || id == 13872 || id == 13875 || id == 13878
				|| id == 13886 || id == 13889 || id == 13892 || id == 13895 || id == 13898 || id == 13901 || id == 13904
				|| id == 13907 || id == 13960

				|| id == 4880 || id == 4886 || id == 4892 || id == 4898)
			return 6000; // 1hour
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161
				|| id == 20165 || id == 20169 || id == 20173)
			return 60000;
		return -1;
	}

	// return what id it degrades to, -1 for disapear which is default so we
	// dont add -1
	public static int getItemDegrade(int id) {
		if (id == 11285) // DFS
			return 11283;
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149 || id == 20153 || id == 20157 || id == 20161
				|| id == 20165 || id == 20169 || id == 20173)
			return id + 1;
		return -1;
	}

	public static int getDegradeItemWhenCombating(int id) {
		// nex armors
		if (id == 20135 || id == 20139 || id == 20143 || id == 20147 || id == 20151 || id == 20155 || id == 20159
				|| id == 20163 || id == 20167 || id == 20171)
			return id + 2;
		return -1;
	}

	public static int getDegradeItemWhenHit(int id) {
		// For barrows and shits
		if (id == 4716)
			return 4880;
		if (id == 4718)
			return 4886;
		if (id == 4720)
			return 4892;
		if (id == 4722)
			return 4898;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 2550)
			return true;
		return false;
	}

	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		if (name.contains("c. dragon") || name.contains("corrupt dragon") || name.contains("vesta")
				|| name.contains("statius'") || name.contains("morrigan's") || name.contains("zuriel's"))
			return true;
		return false;
	}

	public static boolean itemDegradesWhileCombating(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName().toLowerCase();
		// nex armors
		if (name.contains("torva") || name.contains("pernix") || name.contains("virtux") || name.contains("zaryte"))
			return true;
		return false;
	}
	
	public static boolean turnCoins(Item item) {
		if (item.getDefinitions().getName().toLowerCase().contains("(deg)"))
			return true;
		if (item.getDefinitions().getName().toLowerCase()
				.contains("strength cape"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("max cape"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("max hood"))
			return true;
		if (item.getDefinitions().getName().toLowerCase()
				.contains("completionist cape"))
			return true;
		if (item.getDefinitions().getName().toLowerCase()
				.contains("completionist hood"))
			return true;
		switch (item.getId()) {
		case 10887:
		case 7462:
		case 7461:
		case 18349:
		case 18351:
		case 18353:
		case 18355:
		case 18357:
		case 18359:
		case 18361:
		case 18363:
		case 18335:
		case 18334:
		case 18333:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean keptOnDeath(Item item) {
		if (item.getDefinitions().isLended())
			return true;
		/*
		 * if (item.getId() >= 18330 && item.getId() <= 18374) return true; if
		 * (item.getId() >= 19669 && item.getId() <= 19675) return true;
		 */
		if (item.getId() == 19888)
			return true;
		if (item.getId() == 18839)
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("sneak"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains(" charm"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("sneak"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("overload"))
			return true;
		if (item.getDefinitions().getName().toLowerCase().contains("slayer "))
			return true;
		switch (item.getId()) {
		case 22899:
		case 22901:
		case 22904:
		case 23876:
		case 22905:
		case 22907:
		case 22909:
		case 23874:
		case 23848:
		case 23850:
		case 23852:
		case 23854:
		case 23856:
		case 23862:
		case 22897:
		case 23866:
		case 23864:
		case 22298:
		case 22300:
		case 23860:
		case 23868:
		case 23858:
		case 2412: // god cape
		case 2413:
		case 2414:
		case 23659:
		case 6570:
		case 23660:
		case 18346:
		case 18335:
		case 10551:
		case 10548:
		case 20072:
		case 8850:
		case 8849:
		case 8848:
		case 8847:
		case 3839:
		case 3840:
		case 3841:
		case 3842:
		case 3843:
		case 3844:
		case 7460:
		case 7459:
		case 7458:
		case 7457:
		case 7456:
		case 7455:
		case 7454:
		case 7453:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
		case 8839:
		case 8840:
		case 6665:
		case 6666:
			return true;
		default:
			return false;
		}
	}

	public static boolean canWear(Item item, Player player) {
		if (player.getRights() == 2)
			return true;
		/*
		 * if((item.getId() == 20769 || item.getId() == 20771)) { if
		 * (item.getId() == 20771 &&
		 * player.getDominionTower().getKilledBossesCount() < 100) {
		 * player.getPackets().
		 * sendGameMessage("You need to have kill atleast 100 bosses in the Dominion tower to use this cape."
		 * ); return false; }
		 */
		if (item.getId() == 6570 || item.getId() == 10566 || item.getId() == 10637) { // temporary
			if (!player.isCompletedFightCaves()) {
				player.getPackets()
						.sendGameMessage("You need to complete at least once fight cave minigame to use this cape.");
				return false;
			}
		} /*
			 * else if (item.getId() == 14642 || item.getId() == 14645 ||
			 * item.getId() == 15433 || item.getId() == 15435 || item.getId() ==
			 * 14641 || item.getId() == 15432 || item.getId() == 15434) {
			 * if(!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM
			 * )) { player.getPackets().
			 * sendGameMessage("You need to have completed Nomad's Requiem miniquest to use this cape."
			 * ); return false; } }
			 */
		String itemName = item.getName();
		if (itemName.contains("goliath gloves") || itemName.contains("spellcaster glove")
				|| itemName.contains("swift glove")) {
			/*
			 * if (player.getDominionTower().getKilledBossesCount() < 50) {
			 * player.getPackets().
			 * sendGameMessage("You need to have kill atleast 50 bosses in the Dominion tower to wear these gloves."
			 * ); return true; }
			 */
		}
		return true;
	}

	public static boolean isTradeable(Item item) {
		if (item.getDefinitions().isDestroyItem()
				|| item.getDefinitions().isLended()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1)
			return false;
		if (item.getDefinitions().getName().toLowerCase().contains("master cape"))
			return false;
		switch (item.getId()) {
		case 6570: // firecape
		case 8851: // Warrior guild tokens
		case 6529: // tokkul
		case 7462: // barrow gloves
		case 15297: // Pet dagannoth supreme
		case 15298: // Pet dagannoth prime
		case 15299: // Pet dagannoth rex
		case 15300: // Kalphite princess
		case 15301: // Prince black dragon
		case 15302: // Pet kraken
		case 15303: // Pet penance queen
		case 15304: // Pet chaos elemental
			return false;
		default:
			return true;
		}
	}
}
