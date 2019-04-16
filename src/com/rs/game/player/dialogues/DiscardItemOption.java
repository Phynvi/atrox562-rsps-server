package com.rs.game.player.dialogues;

import com.rs.game.player.LendingManager;
import com.rs.game.player.content.Lend;

public class DiscardItemOption extends Dialogue {

	Lend lend;
	
	@Override
	public void start() {
		lend = (Lend) parameters[0];
		int hours = LendingManager.getHoursLeft(lend.getTime());
		int minutes = LendingManager.getMinutesLeft(lend.getTime());
		player.getInterfaceManager().sendChatBoxInterface(94);
		player.getPackets().sendIComponentText(94, 8, lend.getItem().getName());
		player.getPackets().sendIComponentText(94, 7, "<col=00007f>~ Loan expires in " + (hours > 0 ? hours + " hour" + (hours > 1 ? "s" : "") : "") + " " + (minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") : "") + " ~</col><br>" + "If you give this item back, it will disappear.<br>" + "You won't be able to get it back.");
		player.getPackets().sendItemOnIComponent(94, 9, lend.getItem().getId(), 1);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if(interfaceId == 94 && componentId == 3)
		LendingManager.unLend(lend);	
		end();
	}

	@Override
	public void finish() {
		
	}

}

