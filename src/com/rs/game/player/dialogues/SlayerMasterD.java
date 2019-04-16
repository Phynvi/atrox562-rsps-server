package com.rs.game.player.dialogues;

import com.rs.game.player.Skills;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.utils.ShopsHandler;
import com.rs.game.player.skills.slayer.Slayer.SlayerMaster;

/**
 * 
 * File -> SlayerMasterD.java
 *
 * Package -> com.projectx.game.content.dialogues.impl
 * 
 * Created for -> Project X Live
 *
 * @author Austin
 * @since Jan 4, 2017 - 11:55:40 PM
 */
public class SlayerMasterD extends Dialogue {

	private int npcId;
	private SlayerMaster master;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		master = (SlayerMaster) parameters[1];
		sendNPCDialogue(npcId, NORMAL, "'Ello and what are you after then?");
		if (player.getSlayerManager().getCurrentMaster() != master)
			stage = 2;
		else
			stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			if (player.getSlayerManager().getCurrentTask() != null) {
				sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "How many monsters do I have left?", "What do you have in your shop?", "Give me a tip.");
				stage = 0;
			} else {
				sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Please give me a task.", "What do you have in your shop?");
				stage = 1;
				break;
			}
		case 0:
			switch (componentId) {
			case OPTION_1:
				player.getSlayerManager().checkKillsLeft();
				end();
				break;
			case OPTION_2:
				sendNPCDialogue(npcId, NORMAL, "Only the best slayer equipment money could buy.", "Come check it out.");
				stage = 4;
				break;
			case OPTION_3:
				if (player.getSlayerManager().getCurrentTask() == null) {
					sendNPCDialogue(npcId, NORMAL, "You currently don't have a task.");
					return;
				}
				String[] tipDialogues = player.getSlayerManager().getCurrentTask().getTips();
				if (tipDialogues != null && tipDialogues.length != 0) {
					String chosenDialogue2 = null;
					String chosenDialogue3 = null;
					String chosenDialogue4 = null;
					String chosenDialogue5 = null;
					String chosenDialogue = tipDialogues[0];
					if (tipDialogues.length > 1)
						chosenDialogue2 = tipDialogues[1];
					if (tipDialogues.length > 2)
						chosenDialogue3 = tipDialogues[2];
					if (tipDialogues.length > 3)
						chosenDialogue4 = tipDialogues[3];
					if (tipDialogues.length > 4)
						chosenDialogue5 = tipDialogues[4];
					if (chosenDialogue == null || chosenDialogue.equals(""))
						sendNPCDialogue(npcId, 9827, "I don't have any tips for you currently.");
					else {
						if (chosenDialogue5 != null)
							sendNPCDialogue(npcId, NORMAL, chosenDialogue, chosenDialogue2, chosenDialogue3, chosenDialogue4, chosenDialogue5);
						else if (chosenDialogue4 != null)
							sendNPCDialogue(npcId, NORMAL, chosenDialogue, chosenDialogue2, chosenDialogue3, chosenDialogue4);
						else if (chosenDialogue3 != null)
							sendNPCDialogue(npcId, NORMAL, chosenDialogue, chosenDialogue2, chosenDialogue3);
						else if (chosenDialogue2 != null)
							sendNPCDialogue(npcId, NORMAL, chosenDialogue, chosenDialogue2);
						else
							sendNPCDialogue(npcId, NORMAL, chosenDialogue);
					}
					stage = -1;
				} else {
					sendNPCDialogue(npcId, NORMAL, "I don't have any tips for you currently.");
					stage = -1;
				}
				stage = -2;
				break;
			}
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				player.getSlayerManager().setCurrentTask(true);
				sendNPCDialogue(npcId, NORMAL, "Your new assignment is: " + player.getSlayerManager().getCurrentTask().getName() + ".", "Only " + player.getSlayerManager().getCount() + " more to go.");
				stage = -2;
				break;
			case OPTION_2:
				sendNPCDialogue(npcId, 9827, "Only the best slayer equipment money could buy.", "Come check it out.");
				stage = 4;
				break;
			}
			break;
		case 2:
			sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Can you become my master?", "What do you have in your shop?");
			stage = 3;
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				if (player.getSlayerManager().getCurrentTask() != null) {
					sendNPCDialogue(npcId, NORMAL, "I don't think that " + player.getSlayerManager().getCurrentMaster().toString().toLowerCase() + " would be", "happy if I took one of his students just like that.", "Complete your task then return to me.");
				} else if (player.getSkills().getCombatLevelWithSummoning() < master.getRequiredCombatLevel())
					sendNPCDialogue(npcId, NORMAL, "You are too weak overall,", "come back when you've become stronger.");
				else if (player.getSkills().getLevel(Skills.SLAYER) < master.getRequiredSlayerLevel()) {
					sendNPCDialogue(npcId, NORMAL, "Your slayer level is too low to take on", "my challenges, come back when you have a", "level of at least " + master.getRequiredSlayerLevel() + " Slayer.");
				} else {
					sendNPCDialogue(npcId, NORMAL, "You meet my requirements, so therefore I will", "agree to leading you. Good luck adventurer, you will need it.");
					player.getSlayerManager().setCurrentMaster(master);
				}
				stage = -2;
				break;
			case OPTION_2:
				sendNPCDialogue(npcId, NORMAL, "Only the best slayer equipment money could buy.", "Come check it out.");
				stage = 4;
				break;
			}
			break;
		case 4:
			ShopsHandler.openShop(player, 54);
			end();
			break;
		case -2:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
