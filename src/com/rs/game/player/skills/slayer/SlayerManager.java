package com.rs.game.player.skills.slayer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.skills.slayer.Slayer.SlayerMaster;
import com.rs.game.player.skills.slayer.Slayer.SlayerTask;
import com.rs.utils.Utils;

public class SlayerManager implements Serializable {

	private static final long serialVersionUID = -3935672307271551069L;

	public transient static int BROAD_TIPS = 0, RING_OF_SLAYING = 1, SLAYER_HELMET = 2;

	private transient Player player;
	private transient Player socialPlayer;
	private transient int canceledTasksCount;
	public SlayerTask[] canceledTasks;
	private SlayerTask currentTask;
	private SlayerMaster currentMaster;
	private int completedTasks;

	private int slayerPoints;
	private int coslayerPoints;

	public int maximumTaskCount;

	public int currentTaskCount;
	public boolean[] learnedAbilities;

	public SlayerManager() {
		learnedAbilities = new boolean[2];
		canceledTasks = new SlayerTask[4];
		setCurrentMaster(SlayerMaster.TURAEL);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	private void addPoints() {
		double pointsIncreased = 0;
		if (completedTasks == 50) {
			pointsIncreased += currentMaster.getPointsRange()[2];
			pointsIncreased *= (currentTaskCount / maximumTaskCount);
			resetCompletedTasks();
		} else if (completedTasks == 10) {
		pointsIncreased += currentMaster.getPointsRange()[1];
		pointsIncreased *= (currentTaskCount / maximumTaskCount);
		} else
		pointsIncreased += currentMaster.getPointsRange()[0];
		slayerPoints += pointsIncreased;
	}
	
	public boolean isValidTask(String name) {
	if (currentTask == null) {
	    return false;
	}
	List<SlayerTask> tasks = new LinkedList<SlayerTask>(Arrays.asList(currentTask.getAlternatives()));
	tasks.add(currentTask);
	for (SlayerTask currentTask : tasks) {
	    if (name.toLowerCase().contains(currentTask.toString().replace("_", " ").toLowerCase()))
		return true;
	}
	return false;
    }

	public void cancelCurrentTask() {
		skipCurrentTask();
		setCurrentTask(true);
		player.sendMessage("Your slayer task has been re-assigned as requested, as a result");
		player.sendMessage("your slayer-streak has been reset to 0.");
	}

	public void addRemovedTask(int slot) {
		SlayerTask task = canceledTasks[slot];
		if (task == null)
			return;
		canceledTasks[slot] = null;
		canceledTasksCount--;
		refreshCancelledTasks();
		player.sendMessage("You have re-added " + task.getName().toLowerCase() + " to the assignment list.");
	}

	private void refreshCancelledTasks() {
		for (int index = 0; index < canceledTasks.length; index++) {
			SlayerTask task = canceledTasks[index];
			if (task != null) {
				if (index == 0)
					player.getVarBitManager().sendVar(4785, task.getNeverAssignValue());
				if (index == 1)
					player.getVarBitManager().sendVar(4783, task.getNeverAssignValue());
				if (index == 2)
					player.getVarBitManager().sendVar(4786, task.getNeverAssignValue());
				if (index == 3)
					player.getVarBitManager().sendVar(4787, task.getNeverAssignValue());
				if (index == 4)
					player.getVarBitManager().sendVar(7234, task.getNeverAssignValue());
				if (index == 5)
					player.getVarBitManager().sendVar(10241, task.getNeverAssignValue());
			} else {
				if (index == 0)
					player.getVarBitManager().sendVar(4785, 0);
				if (index == 1)
					player.getVarBitManager().sendVar(4783, 0);
				if (index == 2)
					player.getVarBitManager().sendVar(4786, 0);
				if (index == 3)
					player.getVarBitManager().sendVar(4787, 0);
				if (index == 4)
					player.getVarBitManager().sendVar(7234, 0);
				if (index == 5)
					player.getVarBitManager().sendVar(10241, 0);
			}
		}
		if (player.getSlayerManager().getCurrentTask() != null) {
			player.getVarBitManager().sendVar(4781, 1);
			player.getVarBitManager().sendVar(4782, 2);
			player.getVarBitManager().sendVar(394, 1);
		} else if (player.getSlayerManager().getCurrentTask() == null) {
			player.getVarBitManager().sendVar(394, 0);
			player.getVarBitManager().sendVar(4782, 1);

		}
		player.getVarBitManager().sendVar(4781, getSlayerPoints());
		player.getVarBitManager().sendVar(11473, getCoSlayerPoints());
	}

	public void removeCurrentTask() {
		if (canceledTasksCount != canceledTasks.length) {
			for (int index = 0; index < 6; index++) {
				SlayerTask task = canceledTasks[index];
				if (task == null) {
					canceledTasks[index] = currentTask;
					canceledTasksCount++;
					player.sendMessage("You have cancelled the task " + currentTask.getName() + " permanently.");
					skipCurrentTask();
					return;
				}
			}
		} else
			player.sendMessage("You have reached the maximum limit of tasks you may cancel.");
	}

	private Object[] calculateTask() {
		List<SlayerTask> tasks = new LinkedList<SlayerTask>(Arrays.asList(currentMaster.getTask()));
		while (true) {
			for (int i = 0; i < 3; i++)
				if (player.getSlayerManager().canceledTasks[i] != null)
					tasks.remove(canceledTasks[i]);
			SlayerTask task = tasks.get(Utils.random(tasks.size()));
			if (socialPlayer != null) {
				if (player.getSkills().getLevel(Skills.SLAYER) >= task.getLevelRequried())
					return new Object[] { task,
							Utils.random(currentMaster.getTasksRange()[0], currentMaster.getTasksRange()[1]) };
			} else {
				if (player.getSkills().getLevel(Skills.SLAYER) >= task.getLevelRequried()) {
					return new Object[] { task,
							Utils.random(currentMaster.getTasksRange()[0], currentMaster.getTasksRange()[1]) };
				}
			}
		}
	}

	public void checkKillsLeft() {
		if (currentTask == null) {
			player.sendMessage("You currently have no slayer task assigned.");
			return;
		}
		player.sendMessage(
				"Your current assignment is: " + currentTask.getName() + "; only " + getCount() + " more to go.");
		if (socialPlayer != null) {
			player.sendMessage("Your partner's current assignment is: " + currentTask.getName() + "; only "
					+ player.getSlayerManager().getCount() + " more to go.");
			int combinedTasksCount = currentTaskCount + socialPlayer.getSlayerManager().getCurrentTaskCount();
			player.sendMessage("In total you both have killed " + combinedTasksCount + " out of " + maximumTaskCount
					+ " of the task, only " + (maximumTaskCount - combinedTasksCount) + " left to go!");
		}
	}

	public int getCount() {
		return maximumTaskCount - currentTaskCount;
	}
	
	public int setCount(int currentTaskCount) {
		return maximumTaskCount = currentTaskCount;
	}

	public void resetSocialGroup(boolean initial) {
		if (socialPlayer != null) {
			if (initial) {
				socialPlayer.getSlayerManager().resetSocialGroup(false);
				player.sendMessage("You have left the social slayer group.");
			} else
				player.sendMessage("Your social slayer member has left your group.");
			socialPlayer = null;
		}
	}

	private void skipCurrentTask() {
		resetTask(false, true);
		resetCompletedTasks();
	}

	public void resetTask(boolean completed, boolean initial) {
		if (completed) {
			completedTasks++;
			addPoints();
			player.sendMessage("You have completed your slayer task, you now have: "+ getSlayerPoints() +" Slayer points.");
		}
		if (initial) {
			if (socialPlayer != null) {
				socialPlayer.getSlayerManager().resetTask(completed, false);
				if (!completed)
					resetSocialGroup(true);
			}
		}
		setCurrentTask(null, 0);
	}

	private void resetCompletedTasks() {
		this.completedTasks = 0;
	}

	public void setCurrentTask(boolean initial) {
		Object[] futureTask = calculateTask();
		setCurrentTask((SlayerTask) futureTask[0], (int) futureTask[1]);
		checkKillsLeft();
		if (initial) {
			if (socialPlayer != null)
				socialPlayer.getSlayerManager().setCurrentTask((SlayerTask) futureTask[0], (int) futureTask[1]);
		}
	}

	private void setCurrentTask(SlayerTask task, int maximumTaskCount) {
		if (task == null)
			this.currentTaskCount = 0;
		this.currentTask = task;
		this.maximumTaskCount = maximumTaskCount;
	}

	public SlayerTask getCurrentTask() {
		return currentTask;
	}

	public int getCurrentTaskCount() {
		return currentTaskCount;
	}

	public void setPoints(int i) {
		this.setSlayerPoints(i);
	}

	public void setCurrentMaster(SlayerMaster currentMaster) {
		this.currentMaster = currentMaster;
		resetCompletedTasks();
	}

	public SlayerMaster getCurrentMaster() {
		return currentMaster;
	}

	public Player getSocialPlayer() {
		return socialPlayer;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints) {
		this.slayerPoints = slayerPoints;
	}

	public int getCoSlayerPoints() {
		return coslayerPoints;
	}

	public void setCoSlayerPoints(int coSlayerPoints) {
		this.coslayerPoints = coSlayerPoints;
	}

	public void handleSlayerInterfaceButtons(int interfaceId, int componentId) {
		if (interfaceId == 164) {
			if (componentId == 16) {
				if (player.getSlayerManager().learnedAbilities[RING_OF_SLAYING])
					player.getPackets().sendHideIComponent(163, 26, false);
				if (player.getSlayerManager().learnedAbilities[SLAYER_HELMET])
					player.getPackets().sendHideIComponent(163, 27, false);
				if (player.getSlayerManager().learnedAbilities[BROAD_TIPS])
					player.getPackets().sendHideIComponent(163, 25, false);
				player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18, 163);
				player.getInterfaceManager().sendInterface(163);
			} else if (componentId == 17) {
				for (int index = 0; index < canceledTasks.length; index++) {
					SlayerTask task = canceledTasks[index];
					if (task != null) {
						canceledTasksCount++;
						if (index == 0)
							player.getPackets().sendIComponentText(161, 35, task.getName());
						else
							player.getPackets().sendIComponentText(161, 29 + index, task.getName());
					} else
						player.getPackets().sendHideIComponent(161, 36 + index, true);
				}
				player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19, 161);
				player.getInterfaceManager().sendInterface(161);
			} else if (componentId == 24 || componentId == 32 || componentId == 25) {
				if (getSlayerPoints() >= 400) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Slayer XP", 400);
					return;
				} else
					player.sendMessage("You need at least 400 Slayer points to purchase Slayer experience.");
			} else if (componentId == 26 || componentId == 33 || componentId == 27) {
				if (getSlayerPoints() >= 75) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Ring of Slaying", 75);
					return;
				} else
					player.sendMessage("You need at least 75 Slayer points to purchase a Ring of Slaying(8).");
			} else if (componentId == 28 || componentId == 36 || componentId == 29) {
				if (getSlayerPoints() >= 35) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Runes for Slayer dart", 35);
					return;
				} else
					player.sendMessage("You need at least 35 Slayer points to purchase 250 casts of Slayer dart.");
			} else if (componentId == 37 || componentId == 34 || componentId == 38) {
				if (getSlayerPoints() >= 35) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Broad bolts", 35);
					return;
				} else
					player.sendMessage("You need at least 35 Slayer points to purchase 250 Broad bolts.");
			} else if (componentId == 39 || componentId == 35 || componentId == 40) {
				if (getSlayerPoints() >= 35) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Broad arrows", 35);
					return;
				} else
					player.sendMessage("You need at least 35 Slayer points to purchase 250 Broad arrows.");
			}
		} else if (interfaceId == 163) {
			if (componentId == 14) {
				for (int index = 0; index < 4; index++) {
					SlayerTask task = canceledTasks[index];
					if (task != null) {
						canceledTasksCount++;
						if (index == 0)
							player.getPackets().sendIComponentText(161, 35, task.getName());
						else
							player.getPackets().sendIComponentText(161, 29 + index, task.getName());
					} else
						player.getPackets().sendHideIComponent(161, 36 + index, true);
				}
				player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 19, 161);
				player.getInterfaceManager().sendInterface(161);
			} else if (componentId == 15) {
				player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
				player.getInterfaceManager().sendInterface(164);
			} else if (componentId == 33 || componentId == 22 || componentId == 29) {
				if (getSlayerPoints() >= 300) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Learn how to fletch broad arrows/bolts",
							300);
					return;
				} else {
					player.sendMessage(
							"You need at least 300 Slayer points to learn how to fletch Broad arrows and bolts.");
					player.sendMessage("Additionally, at least 52 and 55 Fletching are also required respectively.");
				}
			} else if (componentId == 35 || componentId == 23 || componentId == 30) {
				if (getSlayerPoints() >= 300) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Learn how to craft rings of slaying",
							300);
					return;
				} else {
					player.sendMessage("You need at least 300 Slayer points to learn how to craft Rings of Slaying.");
					player.sendMessage("Additionally, at least 75 Crafting is also required.");
				}
			} else if (componentId == 32 || componentId == 24 || componentId == 31) {
				if (getSlayerPoints() >= 400) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Learn how to craft Slayer helmets", 400);
					return;
				} else {
					player.sendMessage("You need at least 400 Slayer points to learn how to craft Slayer helmets.");
					player.sendMessage("Additionally, at least 55 Crafting is also required.");
				}
			}
		} else if (interfaceId == 161) {
			if (componentId == 14) {
				if (player.getSlayerManager().learnedAbilities[RING_OF_SLAYING])
					player.getPackets().sendHideIComponent(163, 26, false);
				if (player.getSlayerManager().learnedAbilities[SLAYER_HELMET])
					player.getPackets().sendHideIComponent(163, 27, false);
				if (player.getSlayerManager().learnedAbilities[BROAD_TIPS])
					player.getPackets().sendHideIComponent(163, 25, false);
				player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 18, 163);
				player.getInterfaceManager().sendInterface(163);
			} else if (componentId == 15) {
				player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
				player.getInterfaceManager().sendInterface(164);
			} else if (componentId == 26 || componentId == 23) {
				if (getSlayerPoints() >= 30) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Reassign current mission", 30);
					return;
				} else
					player.sendMessage("You need at least 30 Slayer points to request a new assigment.");
			} else if (componentId == 24 || componentId == 27) {
				if (getSlayerPoints() >= 100) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Permanently remove current task", 100);
					return;
				} else {
					player.sendMessage("You need at least 100 Slayer points to permanently cancel your current task.");
					player.sendMessage(
							"You may cancel the removal of the task in the future for the cost of 30 Slayer points.");
				}
			} else if (componentId >= 36 && componentId <= 39) {
				if (getSlayerPoints() >= 30) {
					player.getDialogueManager().startDialogue("SlayerShopD", "Cancel removal", componentId - 36);
					return;
				} else
					player.sendMessage("You need at least 30 Slayer points to cancel the removal of this task.");
			}
		}
	}

	public void sendSlayerInterface() {
		player.getPackets().sendIComponentText(player.getSlayerManager().getSlayerPoints() + "", 20, 164);
		player.getInterfaceManager().sendInterface(164);
	}
}