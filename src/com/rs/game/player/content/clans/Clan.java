package com.rs.game.player.content.clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

import com.rs.game.player.Player;
import com.rs.utils.Utils;
import com.rs.game.World;
import com.rs.game.player.content.clans.ClanMember;
import com.rs.utils.SerializableFilesManager;



public class Clan implements Serializable {

	public enum ClanRanks {
		/**
		 * Anyone will return as "No-one" when modifying the lootshare rank.
		 */
		ANYONE(0, 216), ANY_FRIENDS(1, 19), RECRUIT(2, 193), CORPORAL(3, 76), SERGEANT(4, 173), LIEUTENANT(5, 89), CAPTAIN(6, 221), GENERAL(7, 137), ONLY_ME(8, 63);

		int option, packetId;

		ClanRanks(int option, int packetId) {
			this.option = option;
			this.packetId = packetId;
		}

		private static final HashMap<Integer, ClanRanks> RANKS = new HashMap<Integer, ClanRanks>();

		static {
			for (ClanRanks r : values()) {
				RANKS.put(r.packetId, r);
			}
		}

		public static ClanRanks getRank(int id) {
			return RANKS.get(id);
		}

		public int getPacketId() {
			return packetId;
		}

		public int getOption() {
			return option;
		}
	}

	private static final long serialVersionUID = 4062231702422979939L;

	private List<ClanMember> members;
	private List<String> bannedUsers;
	private ClanRanks kickRank, enterRank, talkRank, lootshareRank;
	private String name, leaderUsername;
	private transient String clanName;
	private boolean lootshare, coinshare;

	public Clan(String playerName, String clanName, Player leader) {
		this.members = new ArrayList<ClanMember>();
		this.bannedUsers = new ArrayList<String>();
		setClanLeaderUsername(addMember(leader, 7));
		setMinimumEnterRank(ClanRanks.ANYONE);
		setMinimumTalkRank(ClanRanks.ANYONE);
		setMinimumKickRank(ClanRanks.ONLY_ME);
		setMinimumLootshareRank(ClanRanks.ANYONE);
		this.name = clanName;
		init(playerName);
	}
	
	public Clan() {
		
	}

	public void init(String clanName) {
		this.clanName = clanName;
	}

	public void ban(String username) {
		this.bannedUsers.add(username);
	}

	public boolean unban(String username) {
		if (bannedUsers.contains(username)) {
			this.bannedUsers.remove(username);
			return true;
		}
		return false;
	}

	public ClanMember addMember(Player player, int rank) {
		ClanMember member = new ClanMember(player.getUsername(), rank);
		members.add(member);
		return member;
	}

	public ClanMember addMemberByUsername(String username, int rank) {
		ClanMember member = new ClanMember(username, rank);
		members.add(member);
		return member;
	}

	public ClanMember getMember(String username) {
		String replaced = username.replaceAll(" ", "_").toLowerCase();
		for (ClanMember member : members) {
			if (member == null || member.getUsername() == null)
				continue;
			if (member.getUsername().equals(replaced))
				return member;
		}
		return null;
	}

	public String getRank(ClanRanks rank, boolean lootshare) {
		switch (rank) {
		case ANYONE:
			return lootshare ? "No-one" : "Anyone";
		case ANY_FRIENDS:
			return "Any friends";
		case RECRUIT:
			return "Recruit+";
		case CORPORAL:
			return "Corporal+";
		case SERGEANT:
			return "Sergeant+";
		case LIEUTENANT:
			return "Lieutenant+";
		case CAPTAIN:
			return "Captain+";
		case GENERAL:
			return "General+";
		case ONLY_ME:
			return "Only me";
		default:
			return null;
		}
	}

	public int getMembersOnlineAmount() {
		int membersOnline = 0;
		for (ClanMember p : members) {
			if (World.getPlayerByDisplayName(p.getUsername()) != null
					&& World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() != null
					&& World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() == this)
				membersOnline++;
		}
		return membersOnline;
	}

	public ClanMember[] getMembersOnline() {
		int amount = 0;
		for (ClanMember p : members) {
			if (p == null || p.getUsername() == null)
				continue;
			if (World.getPlayerByDisplayName(p.getUsername().toLowerCase()) != null
					&& World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() != null
					&& World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() == this)
				amount++;
		}
		ClanMember[] member = new ClanMember[amount];
		int memberId = 0;
		for (ClanMember p : members) {
			if (p == null || p.getUsername() == null || member.length == 0
					|| World.getPlayerByDisplayName(p.getUsername().toLowerCase()) == null
					|| World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() == null)
				continue;
			if (World.getPlayerByDisplayName(p.getUsername().toLowerCase()) != null
					&& World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() != null
					&& World.getPlayerByDisplayName(p.getUsername().toLowerCase()).getCurrentClan() == this)
				member[memberId] = p;
			memberId++;
		}
		return member;
	}

	public void kickMember(String username) {
		if (World.getPlayerByDisplayName(username) != null) {
			World.getPlayerByDisplayName(username).setConnectedClanChannel(false);
			World.getPlayerByDisplayName(username).setCurrentClan(null);
		}
		Player p = null;
		for (int i = 0; i < getMembersOnline().length - 1; i++) {
			p = World.getPlayerByDisplayName(getMembersOnline()[i].getUsername());
			if (p != null && p.getCurrentClan() != null && p.getCurrentClan() == this)
				p.getPackets().sendJoinClanChat(this, true);
		}
	}

	public void killChannel() {
		if (World.getPlayerByDisplayName(this.getClanLeaderUsername()).getCurrentClan() != null)
			World.getPlayerByDisplayName(this.getClanLeaderUsername()).getPackets().sendJoinClanChat(this, false);
		Player p = null;
		members.clear();
		for (int i = 0; i < getMembersOnline().length - 1; i++) {
			p = World.getPlayerByDisplayName(getMembersOnline()[i].getUsername());
			p.getPackets().sendJoinClanChat(this, false);
		}
		Player player = World.getPlayerByDisplayName(this.getClanLeaderUsername());
		player.getPackets().sendIComponentText(590, 22, "Chat disabled");
		player.getPackets().sendIComponentText(590, 23, "Any friends");
		player.getPackets().sendIComponentText(590, 24, "Anyone");
		player.getPackets().sendIComponentText(590, 25, "Only me");
		player.getPackets().sendIComponentText(590, 26, "No-one");
		player.getPackets().sendConfigByFile(4466, 0);
		player.getPackets().sendConfig(1083, 0);
	}

	public void updateMembersList() {
		Player p;
		for (int i = 0; i < getMembersOnline().length; i++) {
			if (getMembersOnline()[i] == null)
				continue;
			p = World.getPlayerByDisplayName(getMembersOnline()[i].getUsername().toLowerCase());
			if (p != null) {
				p.getPackets().sendJoinClanChat(this, true);
				p.getPackets().sendConfigByFile(4466, isCoinsharing() ? 1 : 0);
			}
		}
		if (World.getPlayerByDisplayName(this.getClanLeaderUsername()).getClan() != null)
			SerializableFilesManager.saveClan(this);
	}

	public void refreshSetup(Player player) {
		player.getPackets().sendIComponentText(590, 22, Utils.formatPlayerNameForDisplay(getClanName()));
		player.getPackets().sendIComponentText(590, 23, getRank(getMinimumEnterRank(), false));
		player.getPackets().sendIComponentText(590, 24, getRank(getMinimumTalkRank(), false));
		player.getPackets().sendIComponentText(590, 25, getRank(getMinimumKickRank(), false));
		player.getPackets().sendIComponentText(590, 26, getRank(getMinimumLootshareRank(), true));
		player.getPackets().sendConfig(1083, isLootsharing() ? 1 : 0);
		player.getPackets().sendConfigByFile(4466, isCoinsharing() ? 1 : 0);
		if (player.getClan() != null)
			SerializableFilesManager.saveClan(this);
	}

	public void setClanLeaderUsername(ClanMember member) {
		leaderUsername = member.getUsername();
	}

	public int getMemberId(ClanMember member) {
		return members.indexOf(member);
	}

	public List<ClanMember> getMembers() {
		return members;
	}

	public List<String> getBannedUsers() {
		return bannedUsers;
	}

	public String getFileName() {
		return clanName;
	}

	public String getClanName() {
		return name;
	}

	public ClanRanks getMinimumKickRank() {
		return kickRank;
	}

	public void setMinimumKickRank(ClanRanks minimumRankForKick) {
		this.kickRank = minimumRankForKick;
	}

	public ClanRanks getMinimumEnterRank() {
		return enterRank;
	}

	public void setMinimumEnterRank(ClanRanks minimumEnterRank) {
		this.enterRank = minimumEnterRank;
	}

	public ClanRanks getMinimumTalkRank() {
		return talkRank;
	}

	public void setMinimumTalkRank(ClanRanks minimumTalkRank) {
		this.talkRank = minimumTalkRank;
	}

	public ClanRanks getMinimumLootshareRank() {
		return lootshareRank;
	}

	public void setMinimumLootshareRank(ClanRanks minimumLootshareRank) {
		this.lootshareRank = minimumLootshareRank;
	}

	public String getClanLeaderUsername() {
		return leaderUsername;
	}

	public boolean isLootsharing() {
		return lootshare;
	}

	public boolean isCoinsharing() {
		return coinshare;
	}

	public void setLootshare(boolean on) {
		if (isCoinsharing())
			setCoinshare(false);
		this.lootshare = on;
		for (ClanMember members : members) {
			Player play = World.getPlayer(members.getUsername());
			if (play == null)
				continue;
			play.getPackets().sendConfig(1083, lootshare ? 1 : 0);
			play.sendMessage(String.format("<col=115b0d>Lootshare is now %sactive!</col>", lootshare ? "" : "in"));
		}

	}

	public void setCoinshare(boolean on) {
		this.coinshare = on;
		for (ClanMember members : members) {
			Player play = World.getPlayer(members.getUsername());
			if (play == null)
				continue;
			if (play.getUsername().equalsIgnoreCase(this.getClanLeaderUsername()))
				play.getPackets().sendConfigByFile(4466, coinshare ? 1 : 0);
			play.getPackets().sendConfig(1083, lootshare ? 1 : 0);
			play.sendMessage(String.format("<col=115b0d>Coinshare is now %sactive!</col>", coinshare ? "" : "in"));
		}
	}
}
