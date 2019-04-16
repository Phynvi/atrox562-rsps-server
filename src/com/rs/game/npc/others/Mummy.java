package com.rs.game.npc.others;

import com.rs.game.WorldTile;
import com.rs.game.player.skills.thieving.PyramidPlunderControler;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;

@SuppressWarnings("serial")
public class Mummy extends NPC {

	private PyramidPlunderControler mummy;

	public Mummy(int id, WorldTile tile, PyramidPlunderControler mummy) {
		super(id, tile, -1, true, true);
		this.mummy = mummy;
	}

	public void disapear() {
		mummy = null;
		finish();
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		if (mummy != null) {
			mummy.targetFinishedWithoutDie();
			mummy = null;
		}
		super.finish();
	}

	@Override
	public void sendDeath(Entity source) {
		if (mummy != null) {
			mummy.targetDied();
			mummy = null;
		}
		super.sendDeath(source);
	}

}
