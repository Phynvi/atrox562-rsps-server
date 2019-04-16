package com.rs.game;

import com.rs.game.player.Player;

public final class Hit {
	
	public static enum HitLook {
		MISSED(0),
		REGULAR_DAMAGE(1),
		MELEE_DAMAGE(1), 
		RANGE_DAMAGE(1),
		MAGIC_DAMAGE(1),
	    POISON_DAMAGE(2),
		DESEASE_DAMAGE(3);
		private int mark;

		private HitLook(int mark) {
			this.mark = mark;
		}

		public int getMark() {
			return mark;
		}
	}

	private Entity source;
	private HitLook look;
	private int damage;
	private boolean critical;
	private Hit soaking;
	private int delay;

	public void setCriticalMark() {
		critical = true;
	}
/*
	public void setHealHit() {
		look = HitLook.HEALED_DAMAGE;
		critical = false;
	}*/

	public Hit(Entity source, int damage, HitLook look) {
		this(source, damage, look, 0);
	}

	public Hit(Entity source, int damage, HitLook look, int delay) {
		this.source = source;
		this.damage = damage;
		this.look = look;
		this.delay = delay;
	}

	public boolean missed() {
		return damage == 0;
	}

	public boolean interactingWith(Player player, Entity victm) {
		return player == victm || player == source;
	}

	public int getMark(Player player, Entity victm) {
		/*if (HitLook.HEALED_DAMAGE == look)
			return look.getMark();*/
		if (damage == 0) {
			return HitLook.MISSED.getMark();
		}
		int mark = look.getMark();
		/*if (critical)
			mark += 10;
		if (!interactingWith(player, victm))
			mark += 14;*/
		return mark;
	}

	public HitLook getLook() {
		return look;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}

	public boolean isCriticalHit() {
		return critical;
	}

	public Hit getSoaking() {
		return soaking;
	}

	public void setSoaking(Hit soaking) {
		this.soaking = soaking;
	}
	public int getDelay() {
		return delay;
	}



}

