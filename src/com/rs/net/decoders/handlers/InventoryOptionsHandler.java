package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.pet.Pet;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.Pots.Pot;
import com.rs.game.player.skills.herblore.Herblore;
import com.rs.game.player.skills.herblore.Herblore.RawIngredient;
import com.rs.game.player.Equipment;
import com.rs.game.player.skills.firemaking.GnomishFirelighters;
import com.rs.game.player.skills.firemaking.GnomishFirelighters.Colourables;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.skills.firemaking.Firemaking;
import com.rs.game.player.skills.summoning.Summoning;
import com.rs.game.player.skills.summoning.Summoning.Pouches;
import com.rs.game.player.skills.runecrafting.Runecrafting;
import com.rs.game.player.content.BonesOnAltar;
import com.rs.game.player.content.BonesOnAltar.Bones;
import com.rs.game.player.skills.crafting.GemCutting;
import com.rs.game.player.skills.crafting.GemCutting.Gem;
import com.rs.game.player.skills.crafting.Pottery;
import com.rs.game.player.actions.FillAction.Filler;
import com.rs.game.player.actions.FillAction;
import com.rs.game.player.skills.fletching.Fletching;
import com.rs.game.player.skills.fletching.Fletching.Fletch;
import com.rs.game.player.skills.smithing.Smithing.ForgingBar;
import com.rs.game.player.skills.smithing.Smithing.ForgingInterface;
import com.rs.game.player.content.GodswordCreating;
import com.rs.game.player.skills.crafting.JewelleryCrafting;
import com.rs.game.player.controlers.Barrows;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.game.player.skills.cooking.Cooking;
import com.rs.game.player.skills.cooking.Cooking.Cookables;
import com.rs.game.player.skills.crafting.GlassBlowing;
import com.rs.game.player.skills.crafting.LeatherCrafting;
import com.rs.game.player.skills.farming.TreeSaplings;

import com.rs.io.InputStream;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class InventoryOptionsHandler {
	private static int amount;
	public static void handleItemOnItem(final Player player, InputStream stream) {
		//try {
		final int interfaceIndex = stream.readShort();
		stream.readShortLE128();
		int usedSlot = stream.readShortLE() >> 8;
		int usedWithSlot = stream.readShortLE() >> 8;
		final int itemUsed = stream.readShortLE128() & 0xFFFF;
		stream.readShortLE128();
		stream.readShortLE128();
		final int usedWith = stream.readShortLE() & 0xFFFF;
		Item usedItem = new Item(itemUsed, 1);
		Item usedWithItem = new Item(usedWith, 1);
		if (interfaceIndex != 149) {
			return;
		}
		System.out.println("ITEM 1: " +itemUsed+ " ITEM 2: " +usedWith);
		Fletch fletch = Fletching.isFletching(new Item(usedWith, 1), new Item(itemUsed, 1));
		if (fletch != null) {
			player.getDialogueManager().startDialogue("FletchingD", fletch);
			return;
		}
		if (itemUsed == GlassBlowing.PIPE.getId() || usedWith == GlassBlowing.PIPE.getId()) {
			if (GlassBlowing.handleItemOnItem(player, new Item(itemUsed, 1), new Item(usedWith, 1))) {
				return;
			}
		}
		if (Colourables.forItem(itemUsed, usedWith) != null) {
			if (GnomishFirelighters.ApplyDyeToItems(player, itemUsed, usedWith))
				return;
		}
		if (TreeSaplings.hasSaplingRequest(player, itemUsed, usedWith)) {
				if (itemUsed == 5354)
					TreeSaplings.plantSeed(player, usedWith, usedSlot);
				else
					TreeSaplings.plantSeed(player, itemUsed, usedWithSlot);
		} if (itemUsed == LeatherCrafting.NEEDLE.getId() || usedWith == LeatherCrafting.NEEDLE.getId()) {
			if (LeatherCrafting.handleItemOnItem(player, new Item(itemUsed, 1), new Item(usedWith, 1)))
				return;
		}
		if (Firemaking.isFiremaking(player, usedItem, usedWithItem))
			return;
		if (Pots.mixPot(player, usedItem, usedWithItem, usedSlot, usedWithSlot))
			return;
		RawIngredient raw = RawIngredient.forId(usedWith);
		if (raw != null && itemUsed == Herblore.PESTLE_AND_MORTAR) {
			if (player.isLocked() || !player.getInventory().containsItem(usedWith, 1))
				return;
			if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("You do not have enough free space in your inventory..");
				return;
			}
			player.setNextAnimation(new Animation(364));
			player.getInventory().deleteItem(raw.getRawId(), 1);
			player.getInventory().addItem(raw.getCrushedItem().getId(), 1);
			player.lock(1);
			return;
		}

		int herblore = Herblore.isHerbloreSkill(new Item(itemUsed, 1), new Item(usedWith, 1));

		if (herblore > -1) {
			player.getDialogueManager().startDialogue("HerbloreD", herblore, new Item(itemUsed, 1),
					new Item(usedWith, 1));
			return;
		}
			if (UseWith(11710, 11712, itemUsed, usedWith) || UseWith(11710, 11714, itemUsed, usedWith) || UseWith(11712, 11714, itemUsed, usedWith))
				GodswordCreating.joinPieces(player, false);
			else if (UseWith(11690, 11702, itemUsed, usedWith))
				GodswordCreating.attachHilt(player, 0);
			else if (UseWith(11690, 11704, itemUsed, usedWith))
				GodswordCreating.attachHilt(player, 1);
			else if (UseWith(11690, 11706, itemUsed, usedWith))
				GodswordCreating.attachHilt(player, 2);
			else if (UseWith(11690, 11708, itemUsed, usedWith))
				GodswordCreating.attachHilt(player, 3);
			if (UseWith(1755, Gem.OPAL.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.OPAL);
			}
			if (UseWith(1755, Gem.JADE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.JADE);
			}
			if (UseWith(1755, Gem.RED_TOPAZ.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.RED_TOPAZ);
			}
			if (UseWith(1755, Gem.SAPPHIRE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.SAPPHIRE);
			}
			if (UseWith(1755, Gem.EMERALD.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.EMERALD);
			}
			if (UseWith(1755, Gem.RUBY.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.RUBY);
			}
			if (UseWith(1755, Gem.DIAMOND.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.DIAMOND);
			}
			if (UseWith(1755, Gem.DRAGONSTONE.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.DRAGONSTONE);
			}
			if (UseWith(1755, Gem.ONYX.getUncut(), itemUsed, usedWith)) {
				GemCutting.cut(player, Gem.ONYX);
			}
if (UseWith(1540,11286,usedWith,itemUsed)) {
if (!player.getInventory().containsOneItem(2347)){
player.getPackets().sendGameMessage("You need a hammer for make an dragon fire shield.");
return;
}
player.getPackets().sendGameMessage("You made a Dragon Fire Shield and you got some smithing xp.");
player.getSkills().addXp(13, 10000);
player.getInventory().deleteItem(usedWith, 1);
player.getInventory().deleteItem(itemUsed, 1);
player.getInventory().addItem(11283, 1);
}
Item usedWith1 = player.getInventory().getItem(usedWith);
Item itemUsed1 = player.getInventory().getItem(itemUsed);
if (UseWith(946,1521,usedWith,itemUsed)) {
       if(player.getSkills().getLevelForXp(9) < 15) {
	player.getPackets().sendGameMessage("You do not have the level requirement needed to fletch this.");
      	return;
      }
      if (!player.getInventory().containsOneItem(946)) {
      	player.getPackets().sendGameMessage("You need a knife to fletch.");
     	 return;
      }  
      	player.setNextAnimation(new Animation(6702));   
      	player.getPackets().sendGameMessage("You make iron arrows.");   
      	player.getSkills().addXp(9, 1000);
     	player.getInventory().deleteItem(1521, 1);
	player.getInventory().addItem(884, 15); 
}
		if (UseWith(1755,1617,usedWith,itemUsed)) {
			GemCutting.cut(player, Gem.DIAMOND);
		}
if (UseWith(946,1519,usedWith,itemUsed)) {
       if(player.getSkills().getLevelForXp(9) < 30) {
	player.getPackets().sendGameMessage("You do not have the level requirement needed to fletch this.");
      	return;
      }
      if (!player.getInventory().containsOneItem(946)) {
      	player.getPackets().sendGameMessage("You need a knife to fletch.");
     	 return;
      }  
      	player.setNextAnimation(new Animation(6702));   
      	player.getPackets().sendGameMessage("You make steel arrows.");   
      	player.getSkills().addXp(9, 1500);
     	player.getInventory().deleteItem(1519, 1);
	player.getInventory().addItem(886, 15); 
}
if (UseWith(946,1517,usedWith,itemUsed)) {
       if(player.getSkills().getLevelForXp(9) < 45) {
	player.getPackets().sendGameMessage("You do not have the level requirement needed to fletch this.");
      	return;
      }
      if (!player.getInventory().containsOneItem(946)) {
      	player.getPackets().sendGameMessage("You need a knife to fletch.");
     	 return;
      }  
      player.setNextAnimation(new Animation(6702));   
      	player.getPackets().sendGameMessage("You make mithril arrows.");   
      	player.getSkills().addXp(9, 2000);
     	player.getInventory().deleteItem(1517, 1);
	player.getInventory().addItem(888, 15); 
}
if (UseWith(946,1515,usedWith,itemUsed)) {
       if(player.getSkills().getLevelForXp(9) < 60) {
	player.getPackets().sendGameMessage("You do not have the level requirement needed to fletch this.");
      	return;
      }
      if (!player.getInventory().containsOneItem(946)) {
      	player.getPackets().sendGameMessage("You need a knife to fletch.");
     	 return;
      }  
      	player.setNextAnimation(new Animation(6702));   
      	player.getPackets().sendGameMessage("You make adamant arrows.");   
      	player.getSkills().addXp(9, 4000);
     	player.getInventory().deleteItem(1515, 1);
	player.getInventory().addItem(890, 15); 
}
if (UseWith(946,1513,usedWith,itemUsed)) {
       if(player.getSkills().getLevelForXp(9) < 80) {
	player.getPackets().sendGameMessage("You do not have the level requirement needed to fletch this.");
      	return;
      }
      if (!player.getInventory().containsOneItem(946)) {
      	player.getPackets().sendGameMessage("You need a knife to fletch.");
     	 return;
      }  
      	player.setNextAnimation(new Animation(6702));   
      	player.getPackets().sendGameMessage("You make rune arrows.");   
      	player.getSkills().addXp(9, 8000);
     	player.getInventory().deleteItem(1513, 1);
	player.getInventory().addItem(892, 15); 
}
if (UseWith(13736,13746,usedWith,itemUsed)) {
player.getInventory().deleteItem(13736, 1);
player.getInventory().deleteItem(13746, 1);
player.getInventory().addItem(13738, 1); 
}
if (UseWith(13736,13748,usedWith,itemUsed)) {
player.getInventory().deleteItem(13736, 1);
player.getInventory().deleteItem(13748, 1);
player.getInventory().addItem(13740, 1); 
}
if (UseWith(13736,13750,usedWith,itemUsed)) {
player.getInventory().deleteItem(13736, 1);
player.getInventory().deleteItem(13750, 1);
player.getInventory().addItem(13742, 1); 
}
if (UseWith(13736,13752,usedWith,itemUsed)) {
player.getInventory().deleteItem(13736, 1);
player.getInventory().deleteItem(13752, 1);
player.getInventory().addItem(13744, 1); 
}
		//} catch(Exception e) {
		//}
	}
	public static void handleItemObject(final Player player, InputStream stream) {
		final int y = stream.readShortLE() & 0xFFFF;
		final int objectId = stream.readShortLE() & 0xFFFF;
		final int unknown = stream.readShort128() & 0xFFFF;// Value : 128 ? 200;
		final int interfaceId = stream.readShort() & 0xFFFF;
		final int itemId = stream.readShort128() & 0xFFFF;
		final int itemSlot = stream.readShort128() & 0xFFFF;
		final int x = stream.readShortLE() & 0xFFFF;
		final int slot = player.getInventory().lookupSlot(itemId);
		final Filler fills;
		

		/*if (interfaceId != 149) {// Player is not in invy screen
			return;
		}
		if (!player.getInventory().containsOneItem(itemId)) {
			return;
		}*/
		final Item item = player.getInventory().getItem(itemSlot);
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(objectId, tile);
		if (mapObject == null || mapObject.getId() != objectId)
			return;
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject : new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
		//final int item = player.getInventory().lookupSlot(itemId);
		if (player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if (player.getLockDelay() > Utils.currentTimeMillis())
			return;
		if (!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		/*if (item == null || item.getId() != itemId)
			return;*/
		player.stopAll(false); // false
		final ObjectDefinitions objectDef = object.getDefinitions();
		
		player.faceObject(object);
		if (!player.getControlerManager().handleItemOnObject(object, item))
		    return;
		else if (Pottery.handleItemOnObject(object, player, item))
			return;
		else if (player.getFarmingManager().isFarming(object.getId(), item, 0))
			return;
		else if (Runecrafting.handleTalismanOnAltar(player, object, item))
			return;
		if (object.getId() == 2783) {
						if (GodswordCreating.isShard(itemId)) {
							GodswordCreating.joinPieces(player, true);
							return;
						}
					if (player.getInventory().containsItem(2357, 1)) {
						JewelleryCrafting.openInterface(player, false);
						return;
					}
					ForgingBar bar = ForgingBar.forId(itemId);
					if (bar != null) {
						ForgingInterface.sendSmithingInterface(player, bar);
						return;
					}
				}
		WorldObject kqCave = new WorldObject(3828,
					object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getPlane());
		if ((object.getId() == 3827 || object.getId() == 23609) && item.getId() == 954) {
					if (object.getId() == 3827) {
						if (player.getInventory().containsItem(new Item(954, 1))) {
							player.getInventory().deleteItem(new Item(954, 1));
							if (object.getId() == 3827)
							World.spawnTemporaryObject(kqCave, 600000, true); //10 minutes
							player.sendMessage("You tie a rope to the tunnel entrance.");
						} else
							player.sendMessage("You need to tie a rope to the tunnel entrance.");
					} else
						player.sendMessage("You've already tied a rope to this entrance.");
					return;
				}
		if ((object.getId() == 4039) && item.getId() == 12528) {
			player.getInventory().deleteItem(12528, 1);
			player.summoningTrapdoorUnlocked = true;
			player.sendMessage("You unlock the trapdoor.");
		}
		if ((object.getId() == 11666) && item.getId() == 2353 && player.getInventory().containsItem(4, 1)) {
			player.getDialogueManager().startDialogue("CannonballsD");
			return;
		}
		if (objectId == 13197) {
					Bones bone = BonesOnAltar.isGood(item);
					if(bone != null) {
						player.getDialogueManager().startDialogue("PrayerD", bone, object);
						return;
					} else {
						player.getPackets().sendGameMessage("Nothing interesting happens.");
						return;
					}
		}
		if (objectDef.getName().toLowerCase().contains("range") || objectDef.getName().toLowerCase().contains("fire") || objectId == 2732
		|| objectId == 114 && player.cooksAssistant == 3) {
		Cookables cook = Cooking.isCookingSkill(item);
		if (cook != null) {
			player.getDialogueManager().startDialogue("CookingD", cook, object);
		}
		} else if (item.getId() == 1947 && object.getId() == 36881) {
					if (player.hasGrainInHopper == true) {
						player.getPackets().sendGameMessage("You already have grain placed in the hopper. Try using the hopper controls.");
					} else if (player.hasGrainInHopper == false) {
						final Animation PLACE_ANIMATION = new Animation(832);
						player.hasGrainInHopper = true;
						player.setNextAnimation(PLACE_ANIMATION);
						player.getPackets().sendGameMessage("You place the grain into the hopper.");
						player.getInventory().deleteItem(1947, 1);
					}
	} else if (UseWith(229, 36781, itemId, objectId)) {
		Filler fil = FillAction.isFillable(item);
		player.getActionManager().setAction(new FillAction(fil, 28));
	} else if (UseWith(1935, 36781, itemId, objectId)) {
		Filler fil = FillAction.isFillable(item);
		player.getActionManager().setAction(new FillAction(fil, 28));
	} else if (UseWith(1923, 36781, itemId, objectId)) {
		Filler fil = FillAction.isFillable(item);
		player.getActionManager().setAction(new FillAction(fil, 28));
	} else {
			player.getPackets().sendGameMessage(
					"Nothing interesting happens.");
		System.out.println("Item on object: " + item.getId() +", objectId: "+ object.getId());
	}
}
	
	
	public static void handleItemOnPlayer(final Player player,
			final Player usedOn, final int itemId) {
		player.setCoordsEvent(new CoordsEvent(usedOn, new Runnable() {
			public void run() {
				player.faceEntity(usedOn);
				if (usedOn.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage(usedOn.getDisplayName() + " is busy.");
					return;
				}
				/*if (!usedOn.hasAcceptAid()) {
					player.getPackets().sendGameMessage(usedOn.getDisplayName()
							+ " doesn't want to accept your items.");
					return;
				}*/
				switch (itemId) {
				case 962:// Christmas cracker
					if (player.getInventory().getFreeSlots() < 3
							|| usedOn.getInventory().getFreeSlots() < 3) {
						player.getPackets().sendGameMessage((player.getInventory()
								.getFreeSlots() < 3 ? "You do"
								: "The other player does")
								+ " not have enough inventory space to open this cracker.");
						return;
					}
					player.getDialogueManager().startDialogue(
							"ChristmasCrackerD", usedOn, itemId);
					break;
				default:
					player.getPackets().sendGameMessage("Nothing interesting happens.");
					break;
				}
			}
		}, usedOn.getSize()));
	}
	
	
	public static void handleItemOption6(Player player, int slotId, int itemId,
			Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getLockDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null)
			Summoning.spawnFamiliar(player, pouches);
		/*else if (itemId == 1438)
			Runecrafting.locate(player, 3127, 3405);
		else if (itemId == 1440)
			Runecrafting.locate(player, 3306, 3474);
		else if (itemId == 1442)
			Runecrafting.locate(player, 3313, 3255);
		else if (itemId == 1444)
			Runecrafting.locate(player, 3185, 3165);
		else if (itemId == 1446)
			Runecrafting.locate(player, 3053, 3445);
		else if (itemId == 1448)
			Runecrafting.locate(player, 2982, 3514);*/
		else if (itemId == 1704 || itemId == 10352)
			player.getPackets()
					.sendGameMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		else if (itemId >= 3853 && itemId <= 3867)
			player.getDialogueManager().startDialogue("Transportation",
					"Burthrope Games Room", new WorldTile(2880, 3559, 0),
					"Barbarian Outpost", new WorldTile(2519, 3571, 0),
					"Gamers' Grotto", new WorldTile(2970, 9679, 0),
					"Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
	}
	public static void itemOperate(final Player player, InputStream stream) {
		//try {
			int interfaceSet = stream.readInt();
			int slotId = stream.readShort();
			int interfaceId = interfaceSet >> 16;
			int itemId = stream.readShortLE128();
		//	int itemId = id;
			if (interfaceId == 387) {//387
				if (slotId < 0 || slotId >= Equipment.SIZE || player.getEquipment().getItems().get(slotId) == null) {
					return;
				}
				if (player.getEquipment().getItems().get(slotId).getId() != itemId) {
					return;
				}
				Item item = player.getInventory().getItem(itemId);
				if(item == null || item.getId() != itemId)
					return;
				Pouches pouches = Pouches.forId(itemId);
				if (pouches != null){
					Summoning.spawnFamiliar(player, pouches);
				}else if (itemId == 1704 || itemId == 10352){
					player.getPackets()
							.sendGameMessage(
									"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
				}else if (itemId >= 3853 && itemId <= 3867){
					player.getDialogueManager().startDialogue("Transportation",
							"Burthrope Games Room", new WorldTile(2880, 3559, 0),
							"Barbarian Outpost", new WorldTile(2519, 3571, 0),
							"Gamers' Grotto", new WorldTile(2970, 9679, 0),
							"Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
				}
			} else
				player.getDialogueManager().startDialogue("Transportation",
						"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
						new WorldTile(2918, 3176, 0), "Draynor Village",
						new WorldTile(3105, 3251, 0), "Al Kharid",
						new WorldTile(3293, 3163, 0), itemId);
				//player.getPackets().sendGameMessage("Nothing interesting happens.");
	}
	
	public static void itemOperate1(final Player player, InputStream stream) {
		//try {
			int interfaceSet = stream.readInt();
			int slotId = stream.readShort();
			int interfaceId = interfaceSet >> 16;
			int itemId = stream.readShortLE128();
		//	int itemId = id;
			if (interfaceId == 387) {//387
				if (slotId < 0 || slotId >= Equipment.SIZE || player.getEquipment().getItems().get(slotId) == null) {
					return;
				}
				if (player.getEquipment().getItems().get(slotId).getId() != itemId) {
					return;
				}
				Item item = player.getInventory().getItem(itemId);
				if(item == null || item.getId() != itemId)
					return;
				Pouches pouches = Pouches.forId(itemId);
				if (pouches != null){
					Summoning.spawnFamiliar(player, pouches);
				}else if (itemId == 1704 || itemId == 10352){
					player.getPackets()
							.sendGameMessage(
									"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
				}else if (itemId >= 3853 && itemId <= 3867){
					player.getDialogueManager().startDialogue("Transportation",
							"Burthrope Games Room", new WorldTile(2880, 3559, 0),
							"Barbarian Outpost", new WorldTile(2519, 3571, 0),
							"Gamers' Grotto", new WorldTile(2970, 9679, 0),
							"Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
				}
			} else
				player.getDialogueManager().startDialogue("Transportation",
						"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
						new WorldTile(2918, 3176, 0), "Draynor Village",
						new WorldTile(3105, 3251, 0), "Al Kharid",
						new WorldTile(3293, 3163, 0), itemId);
				//player.getPackets().sendGameMessage("Nothing interesting happens.");
	}
	
	public static void itemOperate2(final Player player, InputStream stream) {
		//try {
			int interfaceSet = stream.readInt();
			int slotId = stream.readShort();
			int interfaceId = interfaceSet >> 16;
			int itemId = stream.readShortLE128();
			if (itemId == 11694) {
			player.getInventory().deleteItem(11694, 1);
			player.getInventory().addItem(11702, 1);
			player.getInventory().addItem(11690, 1);
			} else if (itemId == 11696) {
			player.getInventory().deleteItem(11696, 1);
			player.getInventory().addItem(11704, 1);
			player.getInventory().addItem(11690, 1);
			} else if (itemId == 11698) {
			player.getInventory().deleteItem(11698, 1);
			player.getInventory().addItem(11706, 1);
			player.getInventory().addItem(11690, 1);
			} else if (itemId == 11700) {
			player.getInventory().deleteItem(11700, 1);
			player.getInventory().addItem(11708, 1);
			player.getInventory().addItem(11690, 1);
			}
			else if (itemId == 15069) {
				player.getPackets().sendGameMessage("You turn your voting hat inside out.");
				player.getInventory().deleteItem(15069, 1);
				player.getInventory().addItem(15071, 1);
			} else if (itemId == 15071) {
				player.getPackets().sendGameMessage("You turn your voting hat inside out.");
				player.getInventory().deleteItem(15071, 1);
				player.getInventory().addItem(15069, 1);
			} else if (itemId == 13560 && player.runEnergy < 50 && player.runReplenish < 2) {
				player.runReplenish += 1;
				player.runEnergy += 50;
				player.getPackets().sendGameMessage("Your Energy has been restored 50% you have used this "+player.runReplenish+" / 2 Times today.");
			} else {
				player.getPackets().sendGameMessage("You cannot use this right now.");
			}
		}
	
		@SuppressWarnings("unused")
		public static void itemsummon(final Player player, InputStream stream) {
			final int interfaceId = stream.readShort();
			final int junk = stream.readShort();
			final int id = stream.readShort();
			final int slot = stream.readShort128();
			int itemId = id;
			if (!player.getInventory().containsOneItem(id)) {
				return;
			}
			if (interfaceId == 149) {
				Pouches pouches = Pouches.forId(itemId);
				if (pouches != null)
					Summoning.spawnFamiliar(player, pouches);
			if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
					&& itemId <= 10362)
				player.getDialogueManager().startDialogue("Transportation",
						"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
						new WorldTile(2918, 3176, 0), "Draynor Village",
						new WorldTile(3105, 3251, 0), "Al Kharid",
						new WorldTile(3293, 3163, 0), itemId);
			else if (itemId >= 2552 && itemId <= 2567)
			player.getDialogueManager().startDialogue("Transportation",
					"Duel Arena", new WorldTile(3320, 3237, 0), "Castle Wars",
					new WorldTile(2442, 3090, 0), "Fist of Guthix",
					new WorldTile(1696, 5599, 0), "Clan Wars",
					new WorldTile(3272, 3686, 0), itemId);
			if (itemId == 1704 || itemId == 10352)
				player.getPackets()
						.sendGameMessage(
								"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
			 if (itemId >= 3853 && itemId <= 3867)
				player.getDialogueManager().startDialogue("Transportation",
						"Burthrope Games Room", new WorldTile(2880, 3559, 0),
						"Barbarian Outpost", new WorldTile(2519, 3571, 0),
						"Gamers' Grotto", new WorldTile(2970, 9679, 0),
						"Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);

				switch (id) {
				}
			} else {
				System.out.println("Unhandled item, interface: " + interfaceId + ".");
			}
		}
private static boolean UseWith(int Item1, int Item2,int itemUsed,int usedWith) {
if(itemUsed == Item1 && usedWith == Item2 || itemUsed == Item2 && usedWith == Item1) {
return true;
}
return false;
}
@SuppressWarnings("unused")
public static void itemOnNpc(final Player player, InputStream stream) {
	try {
		int slot = stream.readShort() & 0xFFFF;
		int junk = stream.readIntV1() & 0xFFFF;
		int junk2 = stream.readByte128() & 0xFFFF;
		int itemId = stream.readShortLE128() & 0xFFFF;
		int npcId = stream.readShortLE() & 0xFFFF;

		if (npcId < 0 ) {
			return;
		}
		NPC npc = World.getNPCs().get(npcId);
		if (npc == null) {
			return;
		}
		if (slot < 0) {
			return;
		}

		//player.turnTemporarilyTo(npc);
		switch (itemId) {

		case 962:
			player.getPackets()
			.sendGameMessage(
					"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
			break;

		}

	} catch (Exception e) {
	}
}
/*
 * returns the other
 */
public static Item contains(int id1, Item item1, Item item2) {
	if (item1.getId() == id1)
		return item2;
	if (item2.getId() == id1)
		return item1;
	return null;
}

public static boolean contains(int id1, int id2, Item... items) {
	boolean containsId1 = false;
	boolean containsId2 = false;
	for (Item item : items) {
		if (item.getId() == id1)
			containsId1 = true;
		else if (item.getId() == id2)
			containsId2 = true;
	}
	return containsId1 && containsId2;
}
public static void dig(final Player player) {
	player.resetWalkSteps();
	player.setNextAnimation(new Animation(830));
	player.lock();
	WorldTasksManager.schedule(new WorldTask() {

		@Override
		public void run() {
			player.unlock();
			if(player.getX() == 3005 && player.getY() == 3376
					|| player.getX() == 2999 && player.getY() == 3375
					|| player.getX() == 2996 && player.getY() == 3377
					|| player.getX() == 2989 && player.getY() == 3378
					|| player.getX() == 2987 && player.getY() == 3387
					|| player.getX() == 2984 && player.getY() == 3387) {
				//mole
				player.setNextWorldTile(new WorldTile(1752, 5137, 0));
				player.getPackets().sendGameMessage("You seem to have dropped down into a network of mole tunnels.");
				return;
			}
			player.getPackets().sendGameMessage("You find nothing.");
		}
		
	});
}
public static void handleItemOnNPC(final Player player, final NPC npc, final Item item) {
	if (item == null) {
		return;
	}
	player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
		@Override
		public void run() {
			if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
				return;
			}
			if (npc instanceof Pet) {
				player.faceEntity(npc);
				player.getPetManager().eat(item.getId(), (Pet) npc);
				return;
			}
		}
	}, npc.getSize()));
}

}
