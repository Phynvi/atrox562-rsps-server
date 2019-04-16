package com.rs.game.npc;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.player.skills.Dungeon;

@SuppressWarnings("serial")
public class DungeonBoss extends NPC {

	private Dungeon dungeon;
	
	public DungeonBoss(int id, WorldTile tile, Dungeon dungeon) {
		super(id, tile, 0, true, true);
		this.dungeon = dungeon;
	}
	
	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		dungeon.openStairs();
	}
}
