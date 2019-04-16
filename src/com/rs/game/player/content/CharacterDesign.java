package com.rs.game.player.content;

import com.rs.game.player.Player;



public final class CharacterDesign {

	public static final int INTERFACE = 771;

	public enum Gender {
		MALE, FEMALE;
	}

	public static void refresh(Player player) {
		player.getAppearence().generateAppearenceData();
	}

	/**
	 * @HideIComponent hide the randomize button. Couldn't be bothered to mess
	 *                 around with ConfigByFiles So I hid the button instead so
	 *                 other's wouldn't know.
	 * @param player
	 * @param button
	 */
	public static void handle(Player player, int button) {
		switch (button) {
		case 380: // male
			change(player, Gender.MALE);
			break;
		case 383: // female
			change(player, Gender.FEMALE);
			break;
		case 20:
		case 101:
		case 298:
			player.getPackets().sendHideIComponent(771, 99, true);
			player.getPackets().sendHideIComponent(771, 100, true);
			player.getPackets().sendHideIComponent(771, 300, true);
			player.getPackets().sendHideIComponent(771, 301, true);
			break;
		case 76:
			player.getAppearence().setArmsStyle(player.getAppearence().getArmsStlye());
			player.getAppearence().setTopColor(player.getAppearence().getTopColor());
			player.getAppearence().generateAppearenceData();
			player.getInterfaceManager().closeScreenInterface();
			player.sendMessage("You've finished designing your character. If you ever wish to change it again,");
			player.sendMessage("visit the Make-Over Mage South-West of Falador.");
			break;
		case 18:
		case 401:
			player.getPackets().sendHideIComponent(771, 99, true);
			player.getPackets().sendHideIComponent(771, 100, true);
			player.getPackets().sendHideIComponent(771, 300, true);
			player.getPackets().sendHideIComponent(771, 301, true);
			player.getPackets().sendIComponentAnimation(9835, 771, 374);
			player.getPackets().sendPlayerOnIComponent(771, 374);
			break;
		case 392:
			if (player.getVarBitManager().getValue(170) == 1)
				player.switchMouseButtons();
			break;
		case 395:
			if (player.getVarBitManager().getValue(170) == 0)
				player.switchMouseButtons();
			break;

		}
	}

	public static void change(Player player, Gender gender) {
		if (gender == Gender.MALE)
			player.getAppearence().male();
		else if (gender == Gender.FEMALE)
			player.getAppearence().female();
		player.getAppearence().generateAppearenceData();
		player.getVarBitManager().sendVar(1262, gender == Gender.MALE ? 1 : 8);
	}

}
