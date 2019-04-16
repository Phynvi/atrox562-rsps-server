package com.rs.game.cities.achievements;

import java.io.Serializable;

import com.rs.game.player.Player;
import com.rs.game.item.Item;
import com.rs.game.player.Equipment;
import com.rs.game.player.skills.smithing.Smelting;

/**
 * Manages the achievement diary of a player.
 */
public class AchievementDiaryManager implements Serializable {

	private static final long serialVersionUID = 618893376219661530L;

	/**
     * The achievement diarys.
     */
    private final AchievementDiary[] diarys = new AchievementDiary[] { new AchievementDiary(CityAchievements.KARAMJA), new AchievementDiary(CityAchievements.VARROCK), new AchievementDiary(CityAchievements.LUMBRIDGE) };

	/**
	 * The player instance.
	 */
	private Player player;
	
	/**
	 * Sets the player
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}


	/**
	 * Opens the achievement diary tab.
	 */
/*	public void openTab() {
		player.getInterfaceManager().openTab(2, new Component(259));
		for (AchievementDiary diary : diarys) {
			diary.drawStatus(player);
		}
	}*/

	/**
	 * Induces a task update.
	 * @param player the player.
	 * @param type the diary type.
	 * @param level the level.
	 * @param index the index of the task.
	 * @param complete if it's completed.
	 */
	public void updateTask(Player player, CityAchievements type, int level, int index, boolean complete) {
		getDiary(type).updateTask(player, level, index, complete);
	}

	/**
	 * Checks if a task has been completed.
	 * @param type the diary type.
	 * @param level the level.
	 * @param index the index.
	 * @return {@code True} if completed.
	 */
	public boolean hasCompletedTask(CityAchievements type, int level, int index) {
		return getDiary(type).isComplete(level, index);
	}

	/**
	 * Sets the diary at a level as started.
	 * @param type the type of diary.
	 * @param level the level.
	 */
	public void setStarted(CityAchievements type, int level) {
		getDiary(type).setStarted(level);
	}

	/**
	 * Sets the diarys achievement as completed.
	 * @param type the type.
	 * @param level the level.
	 * @param index the index.
	 */
	public void setCompleted(CityAchievements type, int level, int index) {
		getDiary(type).setCompleted(level, index);
	}

	/**
	 * Gets the achievement diary for the cityAchievements.
	 * @param cityAchievements the cityAchievements.
	 * @return the diary object.
	 */
	public AchievementDiary getDiary(CityAchievements cityAchievements) {
		if (cityAchievements == null) {
			return null;
		}
		for (AchievementDiary diary : diarys) {
			System.out.println(diary.getCityAchievements());
			if (diary.getCityAchievements() == cityAchievements) {
				return diary;
			}
		}
		return null;
	}
	
	public static Object[] getGroup(int componentId) {
		switch(componentId) {
		case 2:
			return new Object[] { CityAchievements.LUMBRIDGE, -1 };
		case 14:
			return new Object[] { CityAchievements.VARROCK, -1 };
		case 10:
			return new Object[] { CityAchievements.KARAMJA, -1 };
		case 3:
		case 4:
		case 5:
			return new Object[] { CityAchievements.LUMBRIDGE, componentId - 3 };
		case 15:
		case 16:
		case 17:
			return new Object[] { CityAchievements.VARROCK, componentId - 15 };
		case 11:
		case 12:
		case 13:
			return new Object[] { CityAchievements.KARAMJA, componentId - 11 };
			default:
				return null;
		}
	}

	/**
	 * Gets the karamaja glove level.
	 * @return the level of the glove.
	 */
	public int getKaramjaGlove() {
		if (!hasGlove()) {
			return -1;
		}
		for (int i = 0; i < 3; i++) {
			if (player.getEquipment().getGlovesId() == CityAchievements.KARAMJA.getRewards()[i][0].getId()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the varrock armour level.
	 * @return the level of the armour.
	 */
	public int getArmour() {
		if (!hasArmour()) {
			return -1;
		}
		for (int i = 0; i < 3; i++) {
			if (player.getEquipment().getChestId() == CityAchievements.VARROCK.getRewards()[i][0].getId()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks the if the reward is valid for double.
	 * @param reward the reward.
	 * @return {@code True} if validated.
	 */
	public boolean checkMiningReward(int reward) {
		int level = player.getAchievementDiaryManager().getArmour();
		if (level == -1) {
			return false;
		}
		if (reward == 453) {
			return true;
		}
		return level == 0 && reward <= 442 || level == 1 && reward <= 447 || level == 2 && reward <= 449;
	}

	/**
	 * Checks the if the reward is valid for double.
	 * @param type the bar type.
	 * @return {@code True} if validated.
	 */
	public boolean checkSmithReward(Smelting.Bars type) {
		int level = player.getAchievementDiaryManager().getArmour();
		if (level == -1) {
			return false;
		}
		return level == 0 && type.ordinal() <= Smelting.Bars.STEEL_BAR.ordinal() || level == 1 && type.ordinal() <= Smelting.Bars.MITHRIL_BAR.ordinal() || level == 2 && type.ordinal() <= Smelting.Bars.ADAMANTITE_BAR.ordinal();
	}

	/**
	 * Checks if the player has karamaja gloves.
	 * @return the gloves.
	 */
	public boolean hasGlove() {
		Item glove = player.getEquipment().getItem(Equipment.SLOT_HANDS);
		return glove != null && (glove.getId() == CityAchievements.KARAMJA.getRewards()[0][0].getId() || glove.getId() == CityAchievements.KARAMJA.getRewards()[1][0].getId() || glove.getId() == CityAchievements.KARAMJA.getRewards()[2][0].getId());
	}

	/**
	 * Checks if the player has varrock armour.
	 * @return {@code True} if so.
	 */
	public boolean hasArmour() {
		Item plate = player.getEquipment().getItem(Equipment.SLOT_CHEST);
		return plate != null && (plate.getId() == CityAchievements.VARROCK.getRewards()[0][0].getId() || plate.getId() == CityAchievements.VARROCK.getRewards()[1][0].getId() || plate.getId() == CityAchievements.VARROCK.getRewards()[2][0].getId());
	}

	/**
	 * Checks if a diary is complete.
	 * @param type the diary type.
	 * @return {@code True} if so.
	 */
	public boolean isComplete(CityAchievements type) {
		return diarys[type.ordinal()].isComplete();
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the diarys.
	 * @return the diarys
	 */
	public AchievementDiary[] getDiarys() {
		return diarys;
	}

}
