package com.rs.game.player.dialogues;

import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.item.Item;
import com.rs.game.World;

/**
 *
 * File -> ExplorerJackDialogue.java
 *
 * Package -> com.projectx.game.content.dialogues.impl.towns.lumbridge
 *
 * Created for -> Project X Live
 *
 * @author Austin
 * @since Jan 4, 2017 - 7:46:25 PM
 */
public class ExplorerJackDialogue extends Dialogue {

	private int npcId;

	//private AchievementDiary diary;

	@Override
	public void start() {
		npcId = (int) parameters[0];
		sendNPCDialogue(npcId, NORMAL, "Stupendous! Look what Foreman George and his team of", "builders have done! This house is perfect! I just need to", "get a carpet and a few decorations and those months of", "sleeping rough in the ruins of my house will become a");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		/*if (diary == null) {
			diary = player.getAchievementDiaryManager().getDiary(CityAchievements.LUMBRIDGE);
		}*/
		switch (stage) {
		case -1:
			sendNPCDialogue(npcId, NORMAL, "distant memory.");
			stage = 0;
			break;
		case 0:
			sendNPCDialogue(npcId, NORMAL, "So, how can I help you?");
			stage = 1;
			break;
		case 1:
			sendDialogue(SEND_2_OPTIONS,"Select an Option", "Tell me about the Task System.", "Nothing.");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case 1:
				sendDialogue(SEND_3_OPTIONS,"Select an Option", "Tell me about the Task System.", "Can I claim any rewards from you?", "Sorry, I was just leaving.");
				stage = 3;
				break;
			case 2:
				end();
				break;
			}
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(npcId, NORMAL, "Well, the Task System is a method of guiding yourself", "while you explore RuneScape.");
				stage = 4;
				break;
			case OPTION_2:
				sendNPCDialogue(npcId, NORMAL, "You certainly can!");
				stage = 10;
				break;
			case OPTION_3:
				end();
				break;
			}
			break;
		case 4:
			sendNPCDialogue(npcId, NORMAL, "You can earn special rewards for completing Tasks; at", "the very least, each is worth a cash bounty from me.");
			stage = 5;
			break;
		case 5:
			sendNPCDialogue(npcId, NORMAL, "Some also give items that will help complete other Tasks,", "and many count as progress towards the set for the area", "they're in.");
			stage = 6;
			break;
		case 6:
		sendDialogue(SEND_3_OPTIONS,"Select an Option", "Tell me about the set reward for this locality.", "How do I claim these rewards?", "Sorry, I was just leaving.");
			stage = 7;
			break;
		case 7:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(npcId, NORMAL, "For completing the Lumbridge and Draynor set, you are", "presented with an explorer's ring.");
				stage = 8;
				break;
			case OPTION_2:
				sendNPCDialogue(npcId, NORMAL, "You need to complete all of the Tasks in the set of a", "particular difficulty, then you can claim your reward. Most", "of the Lumbridge set's Tasks are straightforward,", "although you might find some require quests to be");
				stage = 12;
				break;
			case OPTION_3:
				end();
				break;
			}
			break;
		case 8:
			sendNPCDialogue(npcId, NORMAL, "This ring will become increasingly useful with each", "difficulty level of the set that you complete.");
			stage = 9;
			break;
		case 9:
			sendNPCDialogue(npcId, NORMAL, "When you are presented with your rewards, you will be", "told of their uses.");
			stage = 6;
			break;
		case 10:
			sendNPCDialogue(npcId, NORMAL, "I'll just fill your inventory with what you need, then.");
			stage = 11;
			break;
		case 11:
			/*if (!diary.isComplete()) {
				sendNPCDialogue(npcId, NORMAL, "Sorry, you're not owed any Task rewards at the moment.", "Look at your Task System for things to do to earn more.");
				stage = 1;
			}
			if (diary.isComplete(0) && !diary.hasReward(0)) {
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I've done all the beginner tasks in my Lumbridge",
						"Achievement Diary."
						}, IS_PLAYER, player.getIndex(), 9847);
				stage = 50;
			}
			if (diary.hasReward(0) && diary.isComplete(0) && !player.hasItem(diary.getCityAchievements().getRewards(0)[0].getId())) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"I've seemed to have lost my explorer's ring..."
						}, IS_PLAYER, player.getIndex(), 9847);
				stage = 60;
			}*/
			break;
		case 12:
			sendNPCDialogue(npcId, NORMAL, "started, if not finished.");
			stage = 13;
			break;
		case 13:
			sendNPCDialogue(npcId, NORMAL, "To claim the Lumbridge set reward, speak to Bob in Bob's", "Axes in Lumbridge, Ned in Draynor Village, or myself.");
			stage = 1;
			break;

		/**
		 * TASK SYSTEM REWARDING
		 */
		case 50:
			sendNPCDialogue(npcId, NORMAL, "Yes I see that, you'll be wanting your", "reward then I assume?");
			stage = 51;
			break;
		case 51:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Yes please."
						}, IS_PLAYER, player.getIndex(), 9847);
			stage = 52;
			break;
		case 52:
			/*diary.setRewarded(0);
			for (Item i : diary.getCityAchievements().getRewards(0)) {
				player.getInventory().addItemDrop(i.getId(), 1, player);
				World.sendWorldMessage("<col=ff6600><img=4>Diary Task: "+ player.getDisplayName() +" has been rewarded: "+ i.getDefinitions().getName() +",", false);
				World.sendWorldMessage("<col=ff6600><img=4>by completing all <col=db0000>"+ diary.getCityAchievements().getName() +"</col> <col=ff6600>achievements.", false);
			}*/
			sendNPCDialogue(npcId, NORMAL, "This ring is a representation of the adventures you", "went on to complete your tasks.");
			//World.sendWorldMessage("<col=ff6600><img=4>Diary Task: "+ player.getDisplayName() +" has been rewarded: "+ i.getId().getDefinitions().getName() +".", false);
			stage = 53;
			break;
		case 53:
			sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] {
						player.getDisplayName(),
						"Wow, thanks!"
						}, IS_PLAYER, player.getIndex(), 9847);
			stage = 1;
			break;

		/**
		 * OBTAINING LOST EXPLORERS RING
		 */
		case 60:
			//player.getInventory().addItemDrop(diary.getCityAchievements().getRewards(0)[0].getId(), 1, player);
			sendNPCDialogue(npcId, NORMAL, "You better be more careful this time.");
			stage = 1;
			break;
		case -2:
			end();
			break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}

}
