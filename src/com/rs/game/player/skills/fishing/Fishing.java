package com.rs.game.player.skills.fishing;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.player.actions.Action;
import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.World;
import com.rs.game.player.Skills;
import com.rs.game.player.content.FishingSpotsHandler;
import com.rs.utils.Utils;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class Fishing extends Action {

    public enum Fish {

        CRAYFISH(13435, 1, 30), SHRIMPS(317, 1, 10), KARAMBWANJI(3150, 5, 5), SARDINE(327, 5, 20), HERRING(345, 10,
                30), ANCHOVIES(321, 15, 40), MACKEREL(353, 16, 20), TROUT(335, 20, 50), COD(341, 23, 45), PIKE(349, 25,
                60), SLIMY_EEL(3379, 28, 65), SALMON(331, 30, 70), TUNA(359, 35, 80), RAINBOW_FISH(10136, 38,
                80), CAVE_EEL(5001, 38, 80), LOBSTER(377, 40, 90), BASS(363, 46, 100), SWORDFISH(371,
                50, 100), MONKFISH(7944, 62, 120), SHARK(383, 76, 110), LAVA_EEL(2148, 53,
                30), KARAMBWAN(3142, 65, 105), SEA_TURTLE(395, 79, 38), MANTA_RAY(389,
                81, 46), LEAPING_TROUT(11328, 48, 50), LEAPING_SALMON(11330, 58,
                70), LEAPING_STURGEON(11332, 70, 80), SEAWEED(401, 1,
                1), OYSTER(407, 1, 10), LEATHER_GLOVES(1059, 1,
                1), LEATHER_BOOTS(1061, 1, 1), CASKET(
                405, 1, 0), BIG_BASS(7989, 46,
                100), BIG_SWORDFISH(
                7991, 50,
                100), BIG_SHARK(
                7993,
                76,
                110);

        private final int id, level;
        private final double xp;

        private Fish(int id, int level, double xp) {
            this.id = id;
            this.level = level;
            this.xp = xp;
        }

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public double getXp() {
            return xp;
        }
    }

    public enum FishingSpots {

        CRAYFISH_CAGE(6996, new int[]{13431}, new int[]{-1}, new Animation[]{new Animation(619)}, new Fish[][]{{Fish.CRAYFISH}, {}}), 
		
		NET_AND_BAIT(323, new int[]{303, 307}, new int[]{-1, 313}, new Animation[]{new Animation(621), new Animation(622)},
        new Fish[][]{{Fish.SHRIMPS, Fish.ANCHOVIES}, {Fish.SARDINE, Fish.HERRING}}), 
		
		NET_AND_BAIT1(327, new int[]{303, 307}, new int[]{-1, 313}, new Animation[]{new Animation(621), new Animation(622)},
        new Fish[][]{{Fish.SHRIMPS, Fish.ANCHOVIES}, {Fish.SARDINE, Fish.HERRING}}), 
		
		NET_AND_BAIT2(330, new int[]{303, 307}, new int[]{-1, 313}, new Animation[]{new Animation(621), new Animation(622)},
        new Fish[][]{{Fish.SHRIMPS, Fish.ANCHOVIES}, {Fish.SARDINE, Fish.HERRING}}), 
		
		LURE_AND_BAIT(309, new int[]{309, 307}, new int[]{314, 313}, new Animation[]{new Animation(622), new Animation(622)},
        new Fish[][]{{Fish.TROUT, Fish.SALMON}, {Fish.PIKE}}), 
		
		LURE_AND_BAIT_SPECIAL(310, new int[]{309, 307}, new int[]{314, 313},
        new Animation[]{new Animation(622), new Animation(622)}, new Fish[][]{{Fish.RAINBOW_FISH}, {Fish.PIKE}}), 
		
		LURE_AND_BAIT1(329, new int[]{309, 307}, new int[]{314, 313}, new Animation[]{new Animation(622), new Animation(622)},
        new Fish[][]{{Fish.TROUT, Fish.SALMON}, {Fish.PIKE}}),
		
		CAGE_AND_HARPOON(312, new int[]{301, 311}, new int[]{-1, -1}, new Animation[]{new Animation(619), new Animation(618)},
        new Fish[][]{{Fish.LOBSTER}, {Fish.TUNA, Fish.SWORDFISH}}), 
		
		BIG_NET_AND_HARPOON(313, new int[]{305, 311}, new int[]{-1, -1},
        new Animation[]{new Animation(620), new Animation(618)}, new Fish[][]{{Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED,
        Fish.OYSTER, Fish.CASKET, Fish.LEATHER_BOOTS, Fish.LEATHER_GLOVES}, {Fish.SHARK}}), 
		
		CAGE_AND_HARPOON1(324, new int[]{301, 311}, new int[]{-1, -1}, new Animation[]{new Animation(619), new Animation(618)},
        new Fish[][]{{Fish.LOBSTER}, {Fish.TUNA, Fish.SWORDFISH}}),
		
		SMALL_NET_AND_HARPOON(322, new int[]{303, 311}, new int[]{-1, -1}, new Animation[]{new Animation( 621),
        new Animation(618)}, new Fish[][]{ {Fish.MONKFISH}, {Fish.TUNA, Fish.SWORDFISH}}), BARBARIAN_FISHING_ROD(2722,
        new int[]{11323}, new int[]{313}, new Animation[]{new Animation(622)}, new Fish[][]{{Fish.LEAPING_TROUT,
        Fish.LEAPING_SALMON, Fish.LEAPING_STURGEON}, {}}), OILY_LAVA_BAIT(800, new int[]{1585}, new int[]{313},
        new Animation[]{ new Animation(622)}, new Fish[][]{{Fish.LAVA_EEL}, {}});

        private final int id;
        private final Fish[][] fish;
        private final int[] tool, bait;
        private final Animation[] animation;

        static final Map<Integer, FishingSpots> spot = new HashMap<Integer, FishingSpots>();

        public static FishingSpots forId(int id) {
            return spot.get(id);
        }

        static {
            for (FishingSpots spots : FishingSpots.values())
                spot.put(spots.id, spots);
        }

        private FishingSpots(int id, int[] tool, int[] bait, Animation[] animation, Fish[]... fish) {
            this.id = id;
            this.tool = tool;
            this.bait = bait;
            this.animation = animation;
            this.fish = fish;
        }

        public int getId() {
            return id;
        }

        public Fish[][] getFish() {
            return fish;
        }

        public int getTool()[] {
            return tool;
        }

        public int getBait()[] {
            return bait;
        }

        public Animation[] getAnimation() {
            return animation;
        }
    }

    private FishingSpots spot;
    private int option;
    private NPC npc;
    private WorldTile tile;
    private int fishId;

    public Fishing(FishingSpots spot, NPC npc, int option) {
        this.spot = spot;
        this.npc = npc;
        this.setOption(option);
        tile = new WorldTile(npc);
    }

    @Override
    public boolean start(Player player) {
        if (!checkAll(player))
            return false;
        fishId = getRandomFish(player);
        player.getPackets().sendGameMessage("You attempt to capture a fish...", true);
        setActionDelay(player, getFishingDelay(player));
        return true;
    }

    @Override
    public boolean process(Player player) {
        player.setNextAnimation(spot.getAnimation()[option]);
        return checkAll(player);
    }

    private int getFishingDelay(Player player) {
        int playerLevel = player.getSkills().getLevel(Skills.FISHING);
        int fishLevel = spot.getFish()[option][fishId].getLevel();
        int modifier = spot.getFish()[option][fishId].getLevel();
        int randomAmt = Utils.random(4);
        double cycleCount = 1, otherBonus = 0;
        if (player.getFamiliar() != null)
            otherBonus = getSpecialFamiliarBonus(player.getFamiliar().getId());
        cycleCount = Math.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10) / modifier * 0.25 - randomAmt * 4);
        if (cycleCount < 1)
            cycleCount = 1;
        int delay = (int) cycleCount + 1;
        delay += 7;
        return delay;
    }

    private int getSpecialFamiliarBonus(int id) {
        switch (id) {
            case 6796:
            case 6795:
                return 1;
        }
        return -1;
    }

    private int getRandomFish(Player player) {
        int random = Utils.random(spot.getFish()[option].length);
        int difference = player.getSkills().getLevel(Skills.FISHING) - spot.getFish()[option][random].getLevel();
        if (difference < -1)
            return random = 0;
        if (random < -1)
            return random = 0;
        return random;
    }

    @Override
    public int processWithDelay(Player player) {
        addFish(player);
        return getFishingDelay(player);
    }

    private void depleteFishingSpot(Player player) {
        npc.finish();
        stop(player);
        player.setNextAnimation(new Animation(-1));
        WorldTasksManager.schedule(new WorldTask() {
            int loop = 0;

            @Override
            public void run() {
                if (loop == 75) {
                    World.spawnNPC(npc.getId(), new WorldTile(npc.getX(), npc.getY(), npc.getPlane()), -1, true, false);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    private void addFish(Player player) {
        Item fish = new Item(spot.getFish()[option][fishId].getId(), 1);
        if (spot.getFish()[option][fishId].getId() == 371 && Utils.getRandom(2500) == 1)
            fish = new Item(Fish.BIG_SWORDFISH.getId(), 1);
        else if (spot.getFish()[option][fishId].getId() == 363 && Utils.getRandom(2500) == 1)
            fish = new Item(Fish.BIG_BASS.getId(), 1);
        else if (spot.getFish()[option][fishId].getId() == 383 && Utils.getRandom(2500) == 1)
            fish = new Item(Fish.BIG_SHARK.getId(), 1);
        if (spot.getFish()[option][fishId].getId() == 11332 && player.getSkills().getLevel(Skills.STRENGTH) < 45
                || spot.getFish()[option][fishId].getId() == 11330
                && player.getSkills().getLevel(Skills.AGILITY) < 45) {
            fish = new Item(Fish.LEAPING_SALMON.getId(), 1);
        }
        if (spot.getFish()[option][fishId].getId() == 11330 && player.getSkills().getLevel(Skills.STRENGTH) < 30
                || spot.getFish()[option][fishId].getId() == 11330
                && player.getSkills().getLevel(Skills.AGILITY) < 30) {
            fish = new Item(Fish.LEAPING_TROUT.getId(), 1);
        }

        player.getInventory().addItem(fish);
        player.getPackets().sendGameMessage(getMessage(fish), true);
        if (player.getInventory().containsItem(spot.getBait()[option], 1)) {
            player.getInventory().deleteItem(spot.getBait()[option], 1);
        } else if (spot.getId() == 2722 && !player.getInventory().containsItem(spot.getBait()[option], 1)) {
            if (player.getInventory().containsOneItem(314)) {
                player.getInventory().deleteItem(314, 1);
            } else if (player.getInventory().containsOneItem(11334)) {
                player.getInventory().deleteItem(11334, 1);
            } else if (player.getInventory().containsOneItem(11324)) {
                player.getInventory().deleteItem(11324, 1);
            } else if (player.getInventory().containsOneItem(11326)) {
                player.getInventory().deleteItem(11326, 1);
            } else
                stop(player);
        }
        if (spot.getFish()[option][fishId].getId() == 11328) {
            player.getSkills().addXp(Skills.STRENGTH, 5);
            player.getSkills().addXp(Skills.AGILITY, 5);
        }
        if (spot.getFish()[option][fishId].getId() == 11330) {
            player.getSkills().addXp(Skills.STRENGTH, 6);
            player.getSkills().addXp(Skills.AGILITY, 6);
        }
        if (spot.getFish()[option][fishId].getId() == 11332) {
            player.getSkills().addXp(Skills.STRENGTH, 7);
            player.getSkills().addXp(Skills.AGILITY, 7);
        }
        player.getSkills().addXp(Skills.FISHING, spot.getFish()[option][fishId].getXp());

        if (Utils.random(30) == 1)
            depleteFishingSpot(player);

        fishId = getRandomFish(player);

        /*if (player.inArea(3255, 3125, 3308, 3158)) {
            if (spot.getFish()[option][fishId] == Fish.ANCHOVIES) {
                AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
                if (!diary.isComplete(0, 6)) {
                    diary.updateTask(player, 0, 6, true);
                }
            }
        }*/

        /*if (player.inArea(3231, 3229, 3250, 3265)) {
            if (spot.getFish()[option][fishId] == Fish.SALMON) {
                AchievementDiary diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
                if (!diary.isComplete(1, 3)) {
                    diary.updateTask(player, 1, 3, true);
                }
            }
        }*/
    }

    private String getMessage(Item fish) {
        if (spot.getFish()[option][fishId] == Fish.ANCHOVIES || spot.getFish()[option][fishId] == Fish.SHRIMPS
                || spot.getFish()[option][fishId] == Fish.LEATHER_GLOVES
                || spot.getFish()[option][fishId] == Fish.LEATHER_BOOTS)
            return "You manage to catch some " + fish.getDefinitions().getName().toLowerCase() + ".";
        else
            return "You manage to catch " + (Utils.startsWithVowel(fish.getDefinitions().getName()) ? "an " : "a ")
                    + fish.getDefinitions().getName().toLowerCase() + ".";
    }

    private boolean checkAll(Player player) {
        if (spot.getId() == 2722 && player.getSkills().getLevel(Skills.STRENGTH) < 15
                || spot.getId() == 2722 && player.getSkills().getLevel(Skills.AGILITY) < 15 || spot.getId() == 2722
                && player.getSkills().getLevel(Skills.FISHING) < spot.getFish()[option][fishId].getLevel()) {
            player.sendMessage("You need a fishing level of " + spot.getFish()[option][fishId].getLevel()
                    + ", a strength level of 15 & an agility level of 15 to fish here.");
            return false;
        }
        if (player.getSkills().getLevel(Skills.FISHING) < spot.getFish()[option][fishId].getLevel()
                && spot.getId() != 2722) {
            player.sendMessage(
                    "You need a fishing level of " + spot.getFish()[option][fishId].getLevel() + " to fish here.");
            return false;
        }

        if (!player.getInventory().containsOneItem(spot.getTool()[option])) {
            player.getPackets().sendGameMessage("You need a "
                    + new Item(spot.getTool()[option]).getDefinitions().getName().toLowerCase() + " to fish here.");
            player.setNextAnimation(new Animation(-1));
            return false;
        }
        if (!player.getInventory().containsOneItem(spot.getBait()) && spot.getBait()[option] != -1
                && spot.getId() != 2722) {
            player.getPackets().sendGameMessage("You don't have "
                    + new Item(spot.getBait()[option]).getDefinitions().getName().toLowerCase() + " to fish here.");
            player.setNextAnimation(new Animation(-1));
            return false;
        } else if (!player.getInventory().containsOneItem(spot.getBait()) && spot.getId() == 2722) {
            if (player.getInventory().containsOneItem(314))
                return true;
            if (player.getInventory().containsOneItem(11334))
                return true;
            if (player.getInventory().containsOneItem(11324))
                return true;
            if (player.getInventory().containsOneItem(11326))
                return true;
            else
                player.getPackets().sendGameMessage("You don't have any bait to fish here.");
            player.setNextAnimation(new Animation(-1));
            return false;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.setNextAnimation(new Animation(-1));
            player.sendMessage("You don't have enough inventory space.");
            return false;
        }
        if (npc.hasFinished()) {
            player.setNextAnimation(new Animation(-1));
            return false;
        }
        if (tile.getX() != npc.getX() || tile.getY() != npc.getY())
            return false;
        return true;
    }

    @Override
    public void stop(final Player player) {
        setActionDelay(player, 3);
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}