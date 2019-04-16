package com.rs.game.player.controlers;

public class FalconryControler extends Controler {

	@Override
	public void start() {

	}

	@Override
	public void magicTeleported(int type) {
		if (player.getEquipment().getWeaponId() == 10024 || player.getEquipment().getWeaponId() == 10023) {
			player.getEquipment().removeFalcon();
			player.sendMessage("As you leave the area, your falcon returns to Matthias.");
		}
		removeControler();
	}

	@Override
	public void forceClose() {
		if (player.getEquipment().getWeaponId() == 10024 || player.getEquipment().getWeaponId() == 10023) {
			player.getEquipment().removeFalcon();
			player.sendMessage("As you leave the area, your falcon returns to Matthias.");
		}
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		forceClose();
		return false;
	}

}
