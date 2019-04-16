package com.rs.game.npc.others;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.controlers.Metals;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class MetalArms extends NPC {

	private Metals metals;

	public MetalArms(int id, WorldTile tile, Metals metals) {
		super(id, tile, -1, true, true);
		this.metals = metals;
	}

	@Override
	public void sendDeath(Entity source) {
		if(metals != null) {
			metals.targetDied();
			metals = null;
		}
		super.sendDeath(source);
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return getId() != 2030 ? 0 : Utils.random(3) == 0 ? 1 : 0;
	}
	
	
	public void disapear() {
		metals = null;
		finish();
	}
	@Override
	public void finish() {
		if(hasFinished())
			return;
		if(metals != null) {
			metals.targetFinishedWithoutDie();
			metals = null;
		}
		super.finish();
	}

}