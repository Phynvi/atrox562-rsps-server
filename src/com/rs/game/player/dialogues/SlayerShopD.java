package com.rs.game.player.dialogues;

import com.rs.game.player.Skills;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.skills.slayer.Slayer.SlayerTask;
import com.rs.game.player.skills.slayer.SlayerManager;

public class SlayerShopD extends Dialogue {

	private String name;
	private int npcId = 1597;
	private int cost;

	@Override
	public void start() {
		this.name = (String) parameters[0];
		this.cost = (Integer) parameters[1];
		if (name == "Learn how to fletch broad arrows/bolts")
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to learn how", "to fletch Broad arrows/bolts?");
		else if (name == "Learn how to craft rings of slaying")
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to learn how", "to craft Rings of Slaying?");
		else if (name == "Learn how to craft Slayer helmets")
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to learn how", "to craft Slayer helmets?");
		else if (name == "Permanently remove current task")
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to permanently", "cancel your current task?");
		else if (name == "Reassign current mission")
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to request", "a new Slayer task?");
		else if (name == "Cancel removal")
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to cancel", "the removal of this task?");
		else
			sendNPCDialogue(npcId, 9827, "Are you sure you'd like to purchase " + name + "?");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			if (name == "Learn how to fletch broad arrows/bolts")
				sendOptionsDialogue("Confirm purchase", "Yes, learn the ability to fletch Broad arrows and bolts.",
						"No.");
			else if (name == "Learn how to craft rings of slaying")
				sendOptionsDialogue("Confirm purchase", "Yes, learn the ability to craft Rings of Slaying.", "No.");
			else if (name == "Learn how to craft Slayer helmets")
				sendOptionsDialogue("Confirm purchase", "Yes, learn the ability to craft Slayer helmets.", "No.");
			else if (name == "Permanently remove current task")
				sendOptionsDialogue("Confirm purchase", "Yes, permanently remove my current task.", "No.");
			else if (name == "Reassign current mission")
				sendOptionsDialogue("Confirm purchase", "Yes, request a new task", "No.");
			else if (name == "Cancel removal")
				sendOptionsDialogue("Confirm purchase", "Yes, cancel the removal of this task.", "No.");
			else
				sendOptionsDialogue("Confirm purchase", "Yes, purchase '" + name + "'.", "No.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				if (name == "Slayer XP") {
					player.getSkills().addXp(Skills.SLAYER, 10000);
					player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18, 163);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19, 161);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
				} else if (name == "Ring of Slaying") {
					if (player.getInventory().hasFreeSlots()) {
						player.getInventory().addItem(13281, 1);
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage("You don't have enough inventory space to purchase a ring of slaying.");
				} else if (name == "Runes for Slayer dart") {
					if (player.getInventory().getFreeSlots() >= 2) {
						player.getInventory().addItem(560, 250);
						player.getInventory().addItem(558, 1000);
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage(
								"You don't have enough inventory space to purchase the runes for slayer dart.");
				} else if (name == "Broad bolts") {
					if (player.getInventory().hasFreeSlots()) {
						player.getInventory().addItem(13280, 250);
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage("You don't have enough inventory space to purchase the broad bolts.");
				} else if (name == "Broad arrows") {
					if (player.getInventory().hasFreeSlots()) {
						player.getInventory().addItem(4160, 250);
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage("You don't have enough inventory space to purchase the broad arrows.");
				} else if (name == "Learn how to fletch broad arrows/bolts") {
					if (player.getSkills().getLevel(Skills.FLETCHING) >= 52) {
						player.getSlayerManager().learnedAbilities[SlayerManager.BROAD_TIPS] = true;
						player.getPackets().sendHideIComponent(163, 25, false);
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage("You need at least level 52 Fletching to learn to fletch broad arrows.");
				} else if (name == "Learn how to craft rings of slaying") {
					if (player.getSkills().getLevel(Skills.CRAFTING) >= 75) {
						player.getSlayerManager().learnedAbilities[SlayerManager.RING_OF_SLAYING] = true;
						player.getPackets().sendHideIComponent(163, 26, false);
						player.sendMessage("You feel a sudden surge of knowledge flow through you.");
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage("You need at least level 75 Crafting to learn to craft rings of slaying.");
				} else if (name == "Learn how to craft Slayer helmets") {
					if (player.getSkills().getLevel(Skills.CRAFTING) >= 55) {
						player.getSlayerManager().learnedAbilities[SlayerManager.SLAYER_HELMET] = true;
						player.getPackets().sendHideIComponent(163, 27, false);
						player.sendMessage("You feel a sudden surge of knowledge flow through you.");
						player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18,
								163);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19,
								161);
						player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20,
								164);
					} else
						player.sendMessage("You need at least level 55 Crafting to learn to craft slayer helmets.");
				} else if (name == "Reassign current mission") {
					player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18, 163);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19, 161);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
					player.getSlayerManager().cancelCurrentTask();
				} else if (name == "Permanently remove current task") {
					player.getSlayerManager().removeCurrentTask();
					player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - cost);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18, 163);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19, 161);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
					for (int index = 0; index < 4; index++) {
						SlayerTask task = player.getSlayerManager().canceledTasks[index];
						if (task != null) {
							if (index == 0)
								player.getPackets().sendIComponentText(161, 35, task.getName());
							else
								player.getPackets().sendIComponentText(161, 29 + index, task.getName());
						} else
							player.getPackets().sendHideIComponent(161, 36 + index, true);
					}
				} else if (name == "Cancel removal") {
					player.getSlayerManager().setSlayerPoints(player.getSlayerManager().getSlayerPoints() - 30);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18, 163);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19, 161);
					player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
					player.getSlayerManager().addRemovedTask(cost);
					for (int index = 0; index < 4; index++) {
						SlayerTask task = player.getSlayerManager().canceledTasks[index];
						if (task != null) {
							player.getPackets().sendHideIComponent(161, 36 + index, false);
							if (index == 0)
								player.getPackets().sendIComponentText(161, 35, task.getName());
							else
								player.getPackets().sendIComponentText(161, 29 + index, task.getName());
						} else {
							if (index == 0)
								player.getPackets().sendIComponentText(161, 35, "A");
							else if (index == 1)
								player.getPackets().sendIComponentText(161, 29 + index, "B");
							else if (index == 2)
								player.getPackets().sendIComponentText(161, 29 + index, "C");
							else if (index == 3)
								player.getPackets().sendIComponentText(161, 29 + index, "D");
							player.getPackets().sendHideIComponent(161, 36 + index, true);
						}
					}

				}
			}
			end();
		}
	}

	@Override
	public void finish() {

	}

}