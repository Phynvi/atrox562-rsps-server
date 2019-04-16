package com.rs.game.player.cutscenes;

import java.util.ArrayList;

import com.rs.game.player.Player;
import com.rs.game.Animation;
import com.rs.game.player.cutscenes.actions.CutsceneAction;
import com.rs.game.player.cutscenes.actions.CreateNPCAction;
import com.rs.game.player.cutscenes.actions.PlayerFaceTileAction;
import com.rs.game.player.cutscenes.actions.PlayerForceTalkAction;
import com.rs.game.player.cutscenes.actions.NPCForceTalkAction;
import com.rs.game.player.cutscenes.actions.MoveNPCAction;
import com.rs.game.player.cutscenes.actions.NPCFaceTileAction;
import com.rs.game.player.cutscenes.actions.PlayerAnimationAction;
import com.rs.game.player.cutscenes.actions.PosCameraAction;
import com.rs.game.player.cutscenes.actions.NPCGraphicAction;
import com.rs.game.WorldTile;
import com.rs.game.Graphics;
import com.rs.game.player.cutscenes.actions.MovePlayerAction;

public class WolfWhistle1 extends Cutscene {

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();

		actionsList.add(new PlayerForceTalkAction("Mustn't look round, bunny will eat me.", 2));
		actionsList.add(new PlayerForceTalkAction("Mustn't look round, bunny will eat me.", 2));
		actionsList.add(new PlayerForceTalkAction("Mustn't look round, bunny will eat me.", 2));
		actionsList.add(new PlayerForceTalkAction("Okay, I have the spirit wolf pouch, so it can't hurt me...", 4));
		actionsList.add(new PlayerForceTalkAction("I hope.", 3));
		actionsList.add(new PlayerFaceTileAction(2921, 3446, 1));
		actionsList.add(new PlayerForceTalkAction("Oh god....", 2));
		actionsList.add(new PlayerForceTalkAction("I'll use the spirit wolf pouch. I hope this works.", 2));

		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

}
