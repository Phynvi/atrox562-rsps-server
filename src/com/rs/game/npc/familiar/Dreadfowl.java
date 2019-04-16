package com.rs.game.npc.familiar;

import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.skills.summoning.Summoning.Pouches;

public class Dreadfowl extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2691875962052924796L;

	public Dreadfowl(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Dreadfowl strike";
	}

	@Override
	public String getSpecialDescription() {
		return "The special move has very low probability to attack twice at the same time. A different animation and sound is heard.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1318));
		return true;
	}
}
