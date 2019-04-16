package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public final class Combat {

	public static int getDefenceEmote(Entity target) {
		if(target instanceof NPC) {
			NPC n = (NPC) target;
			return n.getCombatDefinitions().getDefenceEmote();
		}else{
			Player p = (Player) target;
			int shieldId = p.getEquipment().getShieldId();
			if(shieldId == -1) {
				int weaponId = p.getEquipment().getWeaponId();
				if(weaponId == -1)
					return 424;
				String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
				if(weaponName != null && !weaponName.equals("null")) {
					if(weaponName.contains("scimitar") || weaponName.contains("korasi sword"))
						return 15074;
					if(weaponName.contains("whip") || weaponName.contains("tentacle"))
						return 11974;
					if(weaponName.contains("longsword"))
						return 388;
					if(weaponName.contains("dagger"))
						return 378;
					if(weaponName.contains("rapier"))
						return 13038;
					if (weaponName.contains("staff of light"))
						return 12806;
					if(weaponName.contains("staff"))
						return 420;
					if(weaponName.contains("pickaxe"))
						return 397;
					if(weaponName.contains("mace"))
						return 401;
					if(weaponName.contains("hatchet"))
						return 397;
					if(weaponName.contains("greataxe"))
						return 12004;
					if(weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em"))
						return 403;
					if(weaponName.contains("maul") || weaponName.contains("tzhaar-ket-om"))
						return 1666;
					if(weaponName.contains("zamorakian spear"))
						return 12008;
					if(weaponName.contains("spear") || weaponName.contains("halberd") || weaponName.contains("hasta"))
						return 430;
					if(weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.equals("saradomin sword")
						 || weaponName.equals("saradomin's blessed sword"))
						return 7050;
				}
				return 424;
			}
			String shieldName = ItemDefinitions.getItemDefinitions(shieldId).getName().toLowerCase();
			if(shieldName != null && !shieldName.equals("null")) {
				if(shieldName.contains("shield"))
					return 1156;
				if(shieldName.contains("defender"))
					return 4177;
			}
			switch(shieldId) {
			case -1:
			default:
				return 424;
			}
		}
			
		
	}
	
	private Combat() {
	}

	public static boolean hasAntiDragProtection(Entity target) {
		if (target instanceof NPC)
			return false;
		Player p2 = (Player) target;
		int shieldId = p2.getEquipment().getShieldId();
		return shieldId == 1540 || shieldId == 11283 || shieldId == 11284;
	}
	public static int getSlayerLevelForNPC(int id) {
		switch (id) {
		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1653:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 5;
		case 1832:
			return 7;
		case 7787:
			return 10;
		case 1612:
		case 7810:
			return 15;
		case 1831:
			return 17;
		case 1631:
		case 1632:
			return 20;
		case 2804:
		case 2805:
		case 2806:
			return 22;
		case 1620:
		case 1621:
		case 4227:
			return 25;
		case 1633:
		case 1634:
		case 1635:
		case 1636:
		case 6216:
			return 30;
		case 114:
			return 32;
		case 3153:
			return 33;
		case 7823:
			return 35;
		case 3201:
		case 3202:
			return 37;
		case 5751:
			return 39;
		case 1616:
		case 1617:
		case 4228:
		case 5417:
		case 5418:
		case 7813:
			return 40;
		case 2850:
			return 42;
		case 1643:
		case 1644:
		case 1645:
		case 1646:
		case 1647:
			return 45;
		case 3707:
			return 47;
		case 1618:
		case 1619:
		case 6215:
			return 50;
		case 1637:
		case 1638:
		case 1639:
		case 1640:
		case 1641:
		case 1642:
			return 52;
		case 1622:
		case 1623:
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 7814:
			return 55;
		case 6285:
		case 6286:
		case 6287:
		case 6288:
		case 6289:
		case 6290:
		case 6291:
		case 6292:
		case 6293:
		case 6294:
		case 6295:
		case 6296:
		case 6297:
		case 6322:
		case 6323:
		case 6324:
		case 6325:
		case 6326:
		case 6327:
		case 6328:
		case 6329:
		case 6330:
		case 6331:
		case 6332:
			return 56;
		case 3346:
		case 3347:
			return 57;
		case 4353:
		case 4354:
		case 4355:
		case 4356:
		case 4357:
			return 58;
		case 3409:
		case 3410:
		case 3411:
		case 3412:
			return 59;
		case 1604:
		case 1605:
		case 1606:
		case 1607:
		case 7801:
		case 7802:
		case 7803:
		case 7804:
			return 60;
		case 6220:
		case 6230:
		case 6256:
		case 6276:
			return 63;
		case 1624:
			return 65;
		case 6219:
		case 6229:
		case 6255:
		case 6277:
			return 68;
		case 1608:
		case 1609:
		case 4229:
		case 7811:
			return 70;
		case 3068:
		case 3069:
		case 3070:
		case 3071:
			return 72;
		case 1610:
		case 1827:
		case 6389:
			return 75;
		case 1613:
			return 80;
		case 6221:
		case 6231:
		case 6257:
		case 6278:
			return 83;
		case 1615:
		case 4230:
			return 85;
		case 2783:
			return 90;
		default:
			return 0;
		}
	}
}
