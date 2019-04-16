package com.rs.game.player.content.clans;

import java.io.Serializable;

public class ClanMember implements Serializable {

	private static final long serialVersionUID = -1203833687881050886L;

	private String username;
	private int rank, potential;

	public ClanMember(String username, int rank) {
		this.username = username;
		this.rank = rank;
		this.potential = 0;
	}

	public int getRank() {
		return rank;
	}

	public String getUsername() {
		return username;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPotential() {
		return potential;
	}

	public void setPotential(int potential) {
		this.potential = potential;
	}
}
