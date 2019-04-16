package com.rs.game.player.dialogues;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.Player;

public abstract class Dialogue {
	
	public static int NO_EXPRESSION = 9760, SAD = 9764, SAD_TWO = 9768, NO_EXPRESSION_TWO = 9772, WHY = 9776, SCARED = 9780, MIDLY_ANGRY = 9784, ANGRY = 9788, VERY_ANGRY = 9792, ANGRY_TWO = 9796,
	 JUST_LISTEN = 9804, CALM_TALKING = 9808, CALM = 9808, LOOK_DOWN = 9812, WHAT_THE = 9816, WHAT_THE_TWO = 9820, CROOKED_HEAD = 9828, GLANCE_DOWN = 9832, UNSURE = 9836, LISTEN_LAUGH = 9840,
	 TALK_SWING = 9844, NORMAL = 9847, GOOFY_LAUGH = 9851, DRUNK = 9835, HAPPY = 9850, ASKING = 9827, SUSPICIOUS = 9831;

	protected Player player;
	protected byte stage = -1;
	
	public static final int OPTION_1 = 2, OPTION_2 = 3, OPTION_3 = 4, OPTION_4 = 5;

	public Dialogue() {

	}

	public Object[] parameters;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public abstract void start();

	public abstract void run(int interfaceId, int componentId);

	public abstract void finish();

	protected final void end() {
		player.getDialogueManager().finishDialogue();
	}

	protected static final short SEND_1_TEXT_INFO = 210;
	protected static final short SEND_2_TEXT_INFO = 211;
	protected static final short SEND_3_TEXT_INFO = 212;
	protected static final short SEND_4_TEXT_INFO = 213;
	protected static final String SEND_DEFAULT_OPTIONS_TITLE = "Select an Option";
	protected static final short SEND_2_OPTIONS = 236;
	protected static final short SEND_3_OPTIONS = 230; //235
	protected static final short SEND_4_OPTIONS = 237;
	protected static final short SEND_5_OPTIONS = 238;
	protected static final short SEND_2_LARGE_OPTIONS = 229;
	protected static final short SEND_3_LARGE_OPTIONS = 231;
	protected static final short SEND_1_TEXT_CHAT = 241;
	protected static final short SEND_2_TEXT_CHAT = 242;
	protected static final short SEND_3_TEXT_CHAT = 243;
	protected static final short SEND_4_TEXT_CHAT = 244;
	protected static final short SEND_NO_CONTINUE_1_TEXT_CHAT = 215; //245
	protected static final short SEND_NO_CONTINUE_2_TEXT_CHAT = 216;
	protected static final short SEND_NO_CONTINUE_3_TEXT_CHAT = 247;
	protected static final short SEND_NO_CONTINUE_4_TEXT_CHAT = 248;
	protected static final short SEND_NO_EMOTE = -1;
	protected static final byte IS_NOTHING = -1;
	protected static final byte IS_PLAYER = 0;
	protected static final byte IS_NPC = 1;
	protected static final byte IS_ITEM = 2;

	private static int[] getIComponentsIds(short interId) {
		int[] childOptions;
		switch (interId) {
		case SEND_1_TEXT_INFO:
			childOptions = new int[1];
			childOptions[0] = 1;
			break;
		case SEND_2_TEXT_INFO:
			childOptions = new int[2];
			childOptions[0] = 1;
			childOptions[1] = 2;
			break;
		case SEND_3_TEXT_INFO:
			childOptions = new int[3];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			break;
		case SEND_4_TEXT_INFO:
			childOptions = new int[4];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			childOptions[3] = 4;
			break;
		case SEND_2_LARGE_OPTIONS:
			childOptions = new int[3];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			break;
		case SEND_3_LARGE_OPTIONS:
			childOptions = new int[4];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			childOptions[3] = 4;
			break;
		case SEND_2_OPTIONS:
			childOptions = new int[3];
			childOptions[0] = 0;
			childOptions[1] = 1;
			childOptions[2] = 2;
			break;
		case SEND_3_OPTIONS:
			childOptions = new int[4];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			childOptions[3] = 4;
			break;
		case SEND_4_OPTIONS:
			childOptions = new int[5];
			childOptions[0] = 0;
			childOptions[1] = 1;
			childOptions[2] = 2;
			childOptions[3] = 3;
			childOptions[4] = 4;
			break;
		case SEND_5_OPTIONS:
			childOptions = new int[6];
			childOptions[0] = 0;
			childOptions[1] = 1;
			childOptions[2] = 2;
			childOptions[3] = 3;
			childOptions[4] = 4;
			childOptions[5] = 5;
			break;
		case SEND_1_TEXT_CHAT:
		case SEND_NO_CONTINUE_1_TEXT_CHAT:
			childOptions = new int[2];
			childOptions[0] = 3;
			childOptions[1] = 4;
			break;
		case SEND_2_TEXT_CHAT:
		//case SEND_NO_CONTINUE_2_TEXT_CHAT:
			childOptions = new int[3];
			childOptions[0] = 3;
			childOptions[1] = 4;
			childOptions[2] = 5;
			break;
		case SEND_NO_CONTINUE_2_TEXT_CHAT:
			childOptions = new int[2];
			childOptions[0] = 1;
			childOptions[1] = 2;
			break;
		case SEND_3_TEXT_CHAT:
		case SEND_NO_CONTINUE_3_TEXT_CHAT:
			childOptions = new int[4];
			childOptions[0] = 3;
			childOptions[1] = 4;
			childOptions[2] = 5;
			childOptions[3] = 6;
			break;
		case SEND_4_TEXT_CHAT:
		case SEND_NO_CONTINUE_4_TEXT_CHAT:
			childOptions = new int[5];
			childOptions[0] = 3;
			childOptions[1] = 4;
			childOptions[2] = 5;
			childOptions[3] = 6;
			childOptions[4] = 7;
			break;
		default:
			return null;
		}
		return childOptions;
	}
	
	public boolean sendOptionsDialogue(String title, String... options) {
		if (options.length > 4 || options.length < 2)
			return true;
		int interfaceId = 224 + (options.length * 2);
		player.getPackets().sendIComponentText(interfaceId, 1, title);
		for (int i = 0; i < options.length; i++)
			player.getPackets().sendIComponentText(interfaceId, 2 + i, options[i]);
		player.getInterfaceManager().sendChatBoxInterface(interfaceId);
		return true;
	}

	public boolean sendDialogue(short interId, String... talkDefinitons) {
		int[] componentOptions = getIComponentsIds(interId);
		if (componentOptions == null)
			return false;
		player.getInterfaceManager().sendChatBoxInterface(interId);
		if (talkDefinitons.length != componentOptions.length)
			return false;
		for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++)
			player.getPackets().sendIComponentText(interId,
					componentOptions[childOptionId],
					talkDefinitons[childOptionId]);
		return true;
	}
	
	public boolean sendDialogue(String text) {
		player.getInterfaceManager().sendChatBoxInterface(846);
		player.getPackets().sendIComponentText(846, 0, text);
		return true;
	}

	public boolean sendEntityDialogue(short interId, String[] talkDefinitons,
			byte type, int entityId, int animationId) {
		int[] componentOptions = getIComponentsIds(interId);
		if (componentOptions == null)
			return false;
		player.getInterfaceManager().sendChatBoxInterface(interId);
		if (talkDefinitons.length != componentOptions.length)
			return false;
		for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++)
			player.getPackets().sendIComponentText(interId,
					componentOptions[childOptionId],
					talkDefinitons[childOptionId]);
		if (type == IS_PLAYER || type == IS_NPC) {
			player.getPackets().sendEntityOnIComponent(type == IS_PLAYER,
					entityId, interId, 2);
			if (animationId != -1)
				player.getPackets().sendIComponentAnimation(animationId,
						interId, 2);
		} else if (type == IS_ITEM)
			player.getPackets().sendItemOnIComponent(interId, 2, entityId,
					animationId);
		return true;
	}
	public boolean sendEntityDialogue(String title, String text, byte type,
			int npcid, int anim) {
		int inter = (type == IS_PLAYER ? 1191 : 1184);
		boolean left = inter == 1184;
		player.getInterfaceManager().sendChatBoxInterface(inter);
		player.getPackets().sendIComponentText(inter, left ? 17 : 8, title);
		player.getPackets().sendIComponentText(inter, left ? 13 : 17, text);
		if (type == 0)
			player.getPackets().sendPlayerOnIComponent(inter, left ? 11 : 15);
		else
			player.getPackets().sendNPCOnIComponent(inter, left ? 11 : 15,
					npcid);
		player.getPackets()
				.sendIComponentAnimation(anim, inter, left ? 11 : 15);
		return true;

	}
	
	private boolean sendEntityDialogue1(int type, int entityId, int animationId, String... text) {
		String title = "";
		if (type == IS_PLAYER) {
			title = player.getDisplayName();
		} else if (type == IS_NPC) {
			title = NPCDefinitions.getNPCDefinitions(entityId).name;
		} else if (type == IS_ITEM)
			title = ItemDefinitions.getItemDefinitions(entityId).getName();
		return sendEntityDialogue1(type, title, entityId, animationId, text);
	}
	
	private boolean sendEntityDialogue1(int type, String title, int entityId, int animationId, String... texts) {
		if (type == IS_NPC) {
			if (texts.length > 4 || texts.length < 1)
				return true;
			int interfaceId = 240 + texts.length;
			if (interfaceId <= 240)
				interfaceId = 241;
			player.getPackets().sendNPCOnIComponent(interfaceId, 2, entityId);
			player.getPackets().sendIComponentText(interfaceId, 3, title);
			for (int i = 0; i < texts.length; i++)
				player.getPackets().sendIComponentText(interfaceId, 4 + i, texts[i]);
			player.getPackets().sendIComponentAnimation(animationId, interfaceId, 2);
			player.getInterfaceManager().sendChatBoxInterface(interfaceId);
		} else if (type == IS_PLAYER) {
			if (texts.length > 4 || texts.length < 1)
				return true;
			int interfaceId = 63 + texts.length;
			if (interfaceId <= 63)
				interfaceId = 64;
			player.getPackets().sendPlayerOnIComponent(interfaceId, 2);
			player.getPackets().sendIComponentText(interfaceId, 3, player.getDisplayName());
			for (int i = 0; i < texts.length; i++)
				player.getPackets().sendIComponentText(interfaceId, 4 + i, texts[i]);
			player.getPackets().sendIComponentAnimation(animationId, interfaceId, 2);
			player.getInterfaceManager().sendChatBoxInterface(interfaceId);
		} else if (type == IS_ITEM) {
			player.getInterfaceManager().sendChatBoxInterface(519);
			player.getPackets().sendItemOnIComponent(519, 0, entityId, 0);
			if (texts.length >= 4)
				player.getPackets().sendIComponentText(519, 1,
						texts[0] + "<br>" + texts[1] + "<br>" + texts[2] + "<br>" + texts[3]);
			else if (texts.length >= 3)
				player.getPackets().sendIComponentText(519, 1, texts[0] + "<br>" + texts[1] + "<br>" + texts[2]);
			else if (texts.length >= 2)
				player.getPackets().sendIComponentText(519, 1, texts[0] + "<br>" + texts[1]);
			else
				player.getPackets().sendIComponentText(519, 1, texts[0]);
			player.getInterfaceManager().sendChatBoxInterface(519);
		}
		return true;
	}
	
	public boolean sendNPCDialogue(int npcId, int animationId, String... text) {
		return sendEntityDialogue1(IS_NPC, npcId, animationId, text);
	}

	public static boolean sendNPCDialogueNoContinue(Player player, int npcId, int animationId, String... text) {
		return sendEntityDialogueNoContinue(player, IS_NPC, npcId, animationId, text);
	}
	
	public static boolean sendPlayerDialogueNoContinue(Player player, int animationId, String... text) {
		return sendEntityDialogueNoContinue(player, IS_PLAYER, -1, animationId, text);
	}
	
	/*
	 * 
	 * auto selects title, new dialogues
	 */
	public static boolean sendEntityDialogueNoContinue(Player player, int type, int entityId, int animationId, String... text) {
		String title = "";
		if(type == IS_PLAYER) {
				title = player.getDisplayName();
		}else if(type == IS_NPC) {
				title = NPCDefinitions.getNPCDefinitions(entityId).name;
		}else if (type == IS_ITEM) 
			title = ItemDefinitions.getItemDefinitions(entityId).getName();
		return sendEntityDialogueNoContinue(player, type, title, entityId, animationId, text);
	}
	
	public static boolean sendEntityDialogueNoContinue(Player player, int type, String title, int entityId, int animationId, String... texts) {
		StringBuilder builder = new StringBuilder();
		for(int line = 0; line < texts.length; line++) 
				builder.append(" " + texts[line]);
		String text = builder.toString();
		player.getInterfaceManager().replaceRealChatBoxInterface(1192);
		player.getPackets().sendIComponentText(1192, 16, title);
		player.getPackets().sendIComponentText(1192, 12, text);
		player.getPackets().sendEntityOnIComponent(type == IS_PLAYER, entityId, 1192, 11);
		if(animationId != -1)
			player.getPackets().sendIComponentAnimation(animationId, 1192, 11);
		return true;
	}
	
	public static void closeNoContinueDialogue(Player player) {
		player.getInterfaceManager().closeReplacedRealChatBoxInterface();
	}
	
}
