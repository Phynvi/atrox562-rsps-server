package com.rs.game.player.content.custom;

import java.util.Calendar;

import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class DailyTime {

private transient Player player;
	
	public DailyTime(Player player) {
		this.player = player;
	}
	
	  public static int dayOfWeek() {
	      Calendar cal = Calendar.getInstance();
	      return cal.get(Calendar.DAY_OF_WEEK);
	   }
	  
	  public int getRewardByDay() {
		  switch (dayOfWeek()) {
		  case 1:
			  return 995;//coins
		  case 2:
			  return 386;//noted sharks
		  case 3:
			  return 2435;//noted prayer potions
		  case 4:
			  return 995;//coins
		  case 5:
			  return 1632;//uncut dragonstone
		  case 6:
			  return 537;//dragon bones
		  case 7:
			  return 24155;//double spin ticket
		  }
		return 0;
	  }
	  
	  public int getAmount() {
		  switch (dayOfWeek()) {
		  case 1:
			  return 150000;
		  case 2:
			  return 50;
		  case 3:
			  return 25;
		  case 4:
			  return 300000;
		  case 5:
			  return 7;
		  case 6:
			  return 20;
		  case 7:
			  return 1;
		  }
		return 0;
	  }
	  
	  public void startCountdown() {
		  WorldTasksManager.schedule(new WorldTask() {
				
				int timer;
				
				@Override
				public void run() {
					if (timer == 0) {
						player.sendMessage("1 minute till a new day starts.");
					} else if (timer == 60) {
						player.sendMessage("New day starts.");
						player.runReplenish = 0;
						//player.getInventory().addItem(getRewardByDay(), getAmount());
					}
						timer++;
						}
					}, 0, 1);
				}
				
		
	  
	  public void dailyCheckIn() {
			startCountdown();
			}
		}