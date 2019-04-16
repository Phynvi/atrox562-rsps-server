package com.rs.game.player.controlers;

import com.rs.game.Animation;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.skills.Dungeon;
import com.rs.game.player.skills.Dungeonnering;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class DungControler extends Controler {

	private Dungeon dungeon;
	
	@Override
	public void start() {
	//	dungeon = (Dungeon) getParameters()[0];
		//getParameters()[0] = 0;
		showDeaths();
	}
	
	
	public void showDeaths() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 7 : 17, 945);
		refreshDeathCount();
	}
	
	
	
	public void refreshDeathCount() {
		//player.getPackets().sendIComponentText(945, 0, "Deaths "+getParameters()[0]);
	}
	
	@Override
	public void sendInterfaces() {
		showDeaths();
	}
	
	@Override
	public boolean sendDeath() {
		player.addStopDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			@Override
			public void run() {
				player.stopAll();
				if(loop == 0) {
					player.setNextAnimation(new Animation(836));
				}else if(loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				}else if(loop == 3) {
					player.reset();
					dungeon.teleHome(player);
					player.setNextAnimation(new Animation(-1));
					stop();
				//	getParameters()[0] = ((Integer)getParameters()[0])+1;
				//	refreshDeathCount();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}
	
	@Override
	public boolean login() {
		return false;
	}
	
	public boolean processMagicTeleport(WorldTile toTile) {
		if(dungeon == null || !player.getCombatDefinitions().isDungeonneringSpellBook() || !dungeon.hasLoaded())
			return false;
		return true;
	}
	
	public boolean processItemTeleport(WorldTile toTile) {
		return false;
	}
	
	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if(dungeon == null || !dungeon.hasLoaded() || dungeon.isDestroyed())
			return false;
		if(isDoor(object.getId())) {
			openDoor(object);
			return false;
		}else if (isExit(object.getId())) {
			player.getDialogueManager().startDialogue("DungeonExit", this);
			return false;
		}
		return true;
	}
	
	public boolean isExit(int objectId) {
		for(int id : Dungeonnering.DUNGEON_EXITS)
			if(id == objectId)
				return true;
		return false;
	}
	
	public boolean isDoor(int objectId) {
		for(int id : Dungeonnering.DUNGEON_DOORS)
			if(id == objectId)
				return true;
		return false;
	}
	
	public void openDoor(WorldObject object) {
		int[] room = dungeon.getCurrentRoomPos(player);
		switch(object.getRotation()) {
		case 0:
			if(!dungeon.loadRoom(player, room[0]-1, room[1], true)) 
				player.setNextWorldTile(new WorldTile(player.getX()-3, player.getY(), 0), false);
		break;
		case 1:
			if(!dungeon.loadRoom(player, room[0], room[1]+1, true)) 
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY()+3, 0), false);
		break;
		case 2:
			if(!dungeon.loadRoom(player, room[0]+1, room[1], true)) 
				player.setNextWorldTile(new WorldTile(player.getX()+3, player.getY(), 0), false);
		break;
		case 3:
			if(!dungeon.loadRoom(player, room[0], room[1]-1, true)) 
				player.setNextWorldTile(new WorldTile(player.getX(), player.getY()-3, 0), false);
		break;
		}
	}
	
	@Override
	public boolean processObjectClick2(final WorldObject object) {
		if(dungeon == null || !dungeon.hasLoaded())
			return false;
		return true;
	}
	
	@Override
	public void forceClose() {
		leaveDungeon();
	}
	
	@Override
	public boolean logout() {
		player.setMapSize(0); 
		player.setLocation(new WorldTile(new WorldTile(3460, 3720, 1), 2));
		removeControler();
		player.getControlerManager().startControler("Kalaboss");	
		return false;
	}
	
	public void leaveDungeon() {
		player.stopAll();
		dungeon.remove(player);
		player.getCombatDefinitions().removeDungeonneringBook();
		player.setMapSize(0); 
		player.setNextWorldTile(new WorldTile(new WorldTile(3460, 3720, 1), 2));
		removeControler();
		player.getControlerManager().startControler("Kalaboss");	
		player.getInterfaceManager().containsInterface(player.getInterfaceManager().hasRezizableScreen() ? 7 : 17);
	}
	
	public boolean validControler() {
		return dungeon != null && dungeon.hasLoaded() && !dungeon.isDestroyed() && player.getControlerManager().getControler() == this;
	}
	

}
