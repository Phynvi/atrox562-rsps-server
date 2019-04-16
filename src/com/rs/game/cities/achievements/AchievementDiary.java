package com.rs.game.cities.achievements;

import java.io.Serializable;

import com.rs.game.player.Player;
import com.rs.cache.loaders.NPCDefinitions;

public class AchievementDiary implements Serializable {

    /**
     * The cities and their achievements.
     */
    private CityAchievements cityAchievements;

    /**
     * The component id of the diary.
     */
    public static final int DIARY_COMPONENT = 275;

    /**
     * Represents the red color code string.
     */
    private static final String RED = "<col=8A0808>";

    /**
     * Represents the blue color code string.
     */
    private static final String BLUE = "<col=08088A>";

    /**
     * Represents the yellow color code string.
     */
    private static final String YELLOW = "<col=F7FE2E>";

    /**
     * Represets the green color code string.
     */
    private static final String GREEN = "<col=3ADF00>";

    /**
     * The task started.
     */
    private final boolean[] started = new boolean[3];

    /**
     * If the rewards have been given.
     */
    private final boolean[] rewarded = new boolean[3];

    /**
     * The completed achievements.
     */
    private boolean[][] completed;

    private Player player;

    public AchievementDiary() {
        for (CityAchievements ach : CityAchievements.values()) {
        	this.setCityAchievements(ach);
        	  this.completed = new boolean[getCityAchievements().getAchievements().length][50];
        }
        
    }

    public AchievementDiary(CityAchievements cityAchievements) {
        this.setCityAchievements(cityAchievements);
        this.completed = new boolean[getCityAchievements().getAchievements().length][50];
    }

    /**
     * Open the achievement diary.
     *
     * @param player the player.
     */
    public void open(Player player) {
    	if (player.getInterfaceManager().containsScreenInter())
    		player.getInterfaceManager().closeScreenInterface();
    	drawStatus(player);
        clear(player);
        sendString(player, "<red>Achievement Diary - " + cityAchievements.getName(), 2);
        sendString(player, (isComplete() ? GREEN : hasStarted() ? YELLOW : "<red>") + cityAchievements.getName() + " Area Tasks", 11);
        boolean complete;
        String line;
        int child = 17;
        for (int level = 0; level < cityAchievements.getAchievements().length; level++) {
            sendString(player, getStatus(level) + getLevel(level) + "", child);
            child++;
            for (int i = 0; i < cityAchievements.getAchievements(level).length; i++) {
                complete = isComplete(level, i);
                line = (complete ? "<str>" : "") + (complete ? "<str>" + cityAchievements.getAchievements(level)[i] : cityAchievements.getAchievements(level)[i]);
                if (line.contains("<br><br>")) {
                    String[] lines = line.split("<br><br>");
                    for (String l : lines) {
                        sendString(player, l, child);
                        child++;
                    }
                } else {
                    sendString(player, line, child);
                    child++;
                }
            }
            child++;
        }
        player.getInterfaceManager().sendInterface(DIARY_COMPONENT);
    }
    
    /**
     * Open the achievement diary.
     *
     * @param player the player.
     */
    public void openSubgroup(Player player, int difficulty) {
    	if (player.getInterfaceManager().containsScreenInter())
    		player.getInterfaceManager().closeScreenInterface();
    	drawStatus(player);
        clear(player);
        sendString(player, "<red>Achievement Diary - " + cityAchievements.getName(), 2);
        sendString(player, (isComplete() ? GREEN : hasStarted() ? YELLOW : "<red>") + cityAchievements.getName() + " Area Tasks", 11);
        boolean complete;
        String line;
        int child = 17;
        //for (int level = 0; level < cityAchievements.getAchievements().length; level++) {
            sendString(player, getStatus(difficulty) + getLevel(difficulty) + "", child);
            child++;
            for (int i = 0; i < cityAchievements.getAchievements(difficulty).length; i++) {
                complete = isComplete(difficulty, i);
                line = (complete ? "<str>" : "") + (complete ? "<str>" + cityAchievements.getAchievements(difficulty)[i] : cityAchievements.getAchievements(difficulty)[i]);
                if (line.contains("<br><br>")) {
                    String[] lines = line.split("<br><br>");
                    for (String l : lines) {
                        sendString(player, l, child);
                        child++;
                    }
                } else {
                    sendString(player, line, child);
                    child++;
                }
            }
           // child++;
       // }
        player.getInterfaceManager().sendInterface(DIARY_COMPONENT);
    }

    /**
     * Clears the diary screen.
     *
     * @param player the player.
     */
    private void clear(Player player) {
        for (int i = 0; i < 311; i++) {
            player.getPackets().sendIComponentText(DIARY_COMPONENT, i, "");
        }
    }

    /**
     * Draws the status of the diary.
     *
     * @param player the player.
     */
    public void drawStatus(Player player) {
        if (hasStarted()) {
            player.getPackets().sendIComponentText((isComplete() ? GREEN : YELLOW) + cityAchievements.getName(), cityAchievements.getChild(), 259);
            for (int i = 0; i < 3; i++) {
                player.getPackets().sendIComponentText((isComplete(i) ? GREEN : hasStarted(i) ? YELLOW : "<col=FF0000>") + getLevel(i), cityAchievements.getChild() + (i + 1), 259);
            }
        }
    }

    /**
     * Induces a task update.
     *
     * @param player   the player.
     * @param level    the level.
     * @param index    the index of the task.
     * @param complete if it's completed.
     */
    public void updateTask(Player player, int level, int index, boolean complete) {
        System.out.println("here");
        if (!started[level]) {
            started[level] = true;
        }
        if (!complete) {
            player.sendMessage("<col=0040ff>Well done! A " + cityAchievements.getName() + " task has been updated.");
        } else {
            completed[level][index] = true;
            player.sendMessage("<col=dc143c>Well done! You have completed " + (level == 0 ? "an easy" : level == 1 ? "a medium" : "a hard") + " task in the " + cityAchievements.getName() + " area.");
            player.sendMessage("<col=dc143c>Your Achievement Diary has been updated.");
        }
        if (isComplete(level)) {
            player.sendMessage("<col=dc143c>You have completed all of the " + getLevel(level).toLowerCase() + " tasks in the " + cityAchievements.getName() + " area.");
            player.sendMessage("<col=dc143c>Speak to " + (NPCDefinitions.getNPCDefinitions(cityAchievements.getNpc(level)).name) + " to claim your reward.");
        }
        drawStatus(player);
    }

    /**
     * Sends a string on the diary interface.
     *
     * @param player the player.
     * @param string the string.
     * @param child  the child.
     */
    private void sendString(Player player, String string, int child) {
    	player.getPackets().sendIComponentText(DIARY_COMPONENT, child, string.replace("<blue>", BLUE).replace("<red>", RED));
    }

    public CityAchievements getCityAchievements() {
        return cityAchievements;
    }

    public void setCityAchievements(CityAchievements cityAchievements) {
        this.cityAchievements = cityAchievements;
    }

    /**
     * Sets the diary for the level as started.
     *
     * @param level the level.
     */
    public void setStarted(int level) {
        this.started[level] = true;
    }

    /**
     * Sets an achievement as completed.
     *
     * @param level the level.
     * @param index the index.
     */
    public void setCompleted(int level, int index) {
        this.completed[level][index] = true;
    }

    /**
     * Checks if the achievement level is completed.
     *
     * @param level the level.
     * @return {@code True} if so.
     */
    public boolean isComplete(int level) {
        for (int i = 0; i < cityAchievements.getAchievements(level).length; i++) {
            if (!completed[level][i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if an achievement is complete.
     *
     * @param level the level.
     * @param index the index.
     * @return {@code True} if an achievement is completed.
     */
    public boolean isComplete(int level, int index) {
        return completed[level][index];
    }

    /**
     * Checks if an achievement diary is complete.
     *
     * @return {@code True} if completed.
     */
    public boolean isComplete() {
        for (int i = 0; i < completed.length; i++) {
            for (int x = 0; x < cityAchievements.getAchievements(i).length; x++) {
                if (!completed[i][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the level of completion.
     *
     * @return the level.
     */
    public int getLevel() {
        return isComplete(2) ? 2 : isComplete(1) ? 1 : isComplete(0) ? 0 : -1;
    }

    /**
     * Gets the level string.
     *
     * @param level the level.
     * @return the string format.
     */
    public String getLevel(int level) {
        return level == 0 ? "Easy" : level == 1 ? "Medium" : "Hard";
    }

    /**
     * Gets the status for a level of completion of the achievement.
     *
     * @param level the level.
     * @return the string color status.
     */
    public String getStatus(int level) {
        return !hasStarted(level) ? RED : isComplete(level) ? GREEN : YELLOW;
    }

    /**
     * Checks if a diary is started.
     *
     * @return {@code True} if so.
     */
    public boolean hasStarted() {
        for (int i = 0; i < 3; i++) {
            if (started[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the achievement level has been started.
     *
     * @param level the level.
     * @return {@code True} if so.
     */
    public boolean hasStarted(int level) {
        return started[level];
    }

    /**
     * Sets the level as rewarded.
     *
     * @param level the level.
     */
    public void setRewarded(int level) {
        this.rewarded[level] = true;
    }

    /**
     * Checks if the reward has been given.
     *
     * @param level the level.
     * @return {@code True} if so.
     */
    public boolean hasReward(int level) {
        return rewarded[level];
    }

    /**
     * Gets the completed.
     *
     * @return the completed
     */
    public boolean[][] getCompleted() {
        return completed;
    }

    /**
     * Gets the started.
     *
     * @return the started
     */
    public boolean[] getStarted() {
        return started;
    }

    /**
     * Gets the rewarded.
     *
     * @return the rewarded
     */
    public boolean[] getRewarded() {
        return rewarded;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
