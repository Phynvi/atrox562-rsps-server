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

public class WolfWhistle extends Cutscene {

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<CutsceneAction>();

		actionsList.add(new CreateNPCAction(1, 6990, 2921, 3444, 1, -1));
		actionsList.add(new PlayerForceTalkAction("Come on then little, fluffy", 2));
		actionsList.add(new PlayerFaceTileAction(2921, 3446, 1));
		actionsList.add(new PlayerForceTalkAction("Gigantic...", 2));
		actionsList.add(new PlayerForceTalkAction("scary-looking...", 2));
		actionsList.add(new PlayerForceTalkAction("razor-fanged....", 2));
		actionsList.add(new PlayerForceTalkAction("bunny", 2));
		actionsList.add(new NPCForceTalkAction(1, "Mrooowr?", -1));
		actionsList.add(new PlayerForceTalkAction("What is that thing?", 2));
		actionsList.add(new PlayerForceTalkAction("Maybe if I leave quietly, it won't notice me.", 2));
		actionsList.add(new MoveNPCAction(1, 2921, 3442, false, 2));
		actionsList.add(new NPCForceTalkAction(1, "Raaarw!", 2));
		actionsList.add(new NPCGraphicAction(1, new Graphics(1522), 2));
		actionsList.add(new NPCFaceTileAction(1, player.getX(), player.getY(), 1));
		actionsList.add(new PlayerForceTalkAction("It's coming to get me!", 2));
		actionsList.add(new PlayerAnimationAction(new Animation(2836), 2));
		actionsList.add(new PlayerFaceTileAction(2925, 3442, 1));
		actionsList.add(new PlayerAnimationAction(new Animation(828), 2));
		actionsList.add(new MovePlayerAction(2924, 3442, 0, Player.TELE_MOVE_TYPE, 0));
		player.wolfWhistle = 2;

		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

}
