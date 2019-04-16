package com.rs.utils;

public class NPCCharms {
	
	public static final int GOLD_CHARM = 12158;
	public static final int GREEN_CHARM = 12159;
	public static final int CRIMSON_CHARM = 12160;
	public static final int BLUE_CHARM = 12163;
	
	private int amount;
	private double goldRate, greenRate, crimsonRate, blueRate;
	
	public NPCCharms(int amount, double goldRate, double greenRate, double crimsonRate, double blueRate) {
		this.amount = amount;
		this.goldRate = goldRate;
		this.greenRate = greenRate;
		this.crimsonRate = crimsonRate;
		this.blueRate = blueRate;
	}
	
	public int getCharmsAmount() {
		return amount;
	}
	
	public double getGoldCharmRate() {
		return goldRate;
	}
	
	public double getGreenCharmRate() {
		return greenRate;
	}
	
	public double getCrimsonCharmRate() {
		return crimsonRate;
	}
	
	public double getBlueCharmRate() {
		return blueRate;
	}
	
}
