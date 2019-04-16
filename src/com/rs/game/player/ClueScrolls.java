package com.rs.game.player;

import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.utils.Utils;
import com.rs.game.World;

public class ClueScrolls {
	//chest id 2717
	public static int[] ScrollIds = {2677,2678,2679,2680,2681,2682,2683,2684,2685,2686,2687,2688,2689,2690,2691,2692,2693,2694,2695,2696,2697,2698};
	public static int[] eventIds = {1050,22973,26492,15422,15423,15424,15425};
	
	public static enum Scrolls {
		Scroll1(new int[] {ScrollIds[0],1}, Maps.Map1),
		Scroll2(new int[] {ScrollIds[1],1}, Maps.Map2),
		Scroll3(new int[] {ScrollIds[2],1}, Maps.Map3),
		Scroll4(new int[] {ScrollIds[3],1}, Maps.Map4),
		Scroll5(new int[] {ScrollIds[4],1}, Maps.Map5),
		Scroll6(new int[] {ScrollIds[5],1}, Maps.Map6),
		Scroll7(new int[] {ScrollIds[6],1}, Maps.Map7),
		Scroll8(new int[] {ScrollIds[7],1}, Maps.Map8),
		Scroll9(new int[] {ScrollIds[8],1}, Maps.Map9),
		Scroll10(new int[] {ScrollIds[9],1}, Maps.Map10),
		Scroll11(new int[] {ScrollIds[10],1}, Maps.Map11),
		Scroll12(new int[] {ScrollIds[11],1}, Maps.Map12),
		Scroll13(new int[] {ScrollIds[12],1}, Maps.Map13),
		Scroll14(new int[] {ScrollIds[13],1}, Maps.Map14),
		Scroll15(new int[] {ScrollIds[14],1}, Maps.Map15),
		Scroll16(new int[] {ScrollIds[15],1}, ObjectMaps.Map1),
		Scroll17(new int[] {ScrollIds[16],1}, ObjectMaps.Map2),
		Scroll18(new int[] {ScrollIds[17],1}, Riddles.Riddle1),
		Scroll19(new int[] {ScrollIds[18],1}, Riddles.Riddle2),
		Scroll20(new int[] {ScrollIds[19],1}, Riddles.Riddle3),
		Scroll21(new int[] {ScrollIds[20],1}, Riddles.Riddle4),
		Scroll22(new int[] {ScrollIds[21],1}, Riddles.Riddle5);
		
		public static Maps getMap(int itemid){
			for (Scrolls scroll: Scrolls.values()){
				if (scroll.id == itemid){
					if (scroll.getHiding() == null)
							continue;
						else
							return scroll.getHiding();
					}
				}
			return null;
		}

		public static Riddles getRiddles(int itemid){
			for (Scrolls scroll: Scrolls.values()){
				if (scroll.id == itemid){
					if (scroll.getRiddle() == null)
							continue;
						else
							return scroll.getRiddle();
					}
				}
			return null;
		}

		public Riddles getRiddle(){
			return riddle;
		}
		
		public static ObjectMaps getObjMap(int itemid){
			for (Scrolls scroll: Scrolls.values()){
				if (scroll.id == itemid){
					if (scroll.getLocationMap() == null)
						continue;
					else
						return scroll.getLocationMap();
				}
			}
			return null;
		}

		public ObjectMaps getLocationMap(){
			return locationmap;
		}
		
		public Maps getHiding(){
			return hiding;
		}
		int[] infos;
		Maps hiding;
		int id, level;
		ObjectMaps locationmap;
		Riddles riddle;
		private Scrolls(int[] info, Riddles riddle){
			this.infos = info;
			this.riddle = riddle;
			this.id = info[0];
			this.level = info[1];
		}
		private Scrolls(int[] info, ObjectMaps map){
			this.infos = info;
			this.locationmap = map;
			this.id = info[0];
			this.level = info[1];
		}
		private Scrolls(int[] info, Maps hiden){
		this.infos = info;
		this.id = info[0];
		this.level = info[1];
		this.hiding = hiden;
		}
	}
	
	/*if (lastloc.getX() >= riddleclue.locations[0]
					&& lastloc.getY() <= riddleclue.locations[1]
					&& lastloc.getX() <= riddleclue.locations[2]
					&& lastloc.getY() >= riddleclue.locations[3]) {*/
	
	private enum Riddles {
		Riddle1(20, new int[] {2967,4386,2970,4380}, new String[] {"There once was a villan","of grey and white", "he also had a bit of bage","do a clap outside his cave","to scare him off","","",""}),//Corp
		Riddle2(15, new int[] {3190,9827,3193,9825}, new String[] {"I am a token of the greatest love", "I have no beginning or end","Go to the place where money is lent","Perform a Jig by the gate to be my friend!","","","",""}),//Varrock Bank Basement
		Riddle3(26, new int[] {3162,3255,3171,3244}, new String[] {"For the reward you seek", "a city of lumber and bridge", "is west of a place that you", "must go to get some ham", "once outside do a lean"," to meat Mr. Mean!","",""}),//Ham Entrance
		Riddle4(12, new int[] {2996,3115,3001,3109}, new String[] {"Near a ring known to teleport","On a point full of mud", "A simple emote is needed", "An emote known as skipping or dance!","","","",""}),
		Riddle5(33, new int[] {2884,3449,2898,3438}, new String[] {"This reward will require a bit","For the first thing you will","Need to be at a den","and you have to be a rouge","You must have an idea outside","Of its entrance to get a reward!","",""});//Mudsckipper Point
		int[] locations;
		String[] riddles;
		int emoteid;
		private Riddles(int id, int[] location, String[] riddles){
			this.locations = location;
			this.riddles = riddles;
			this.emoteid = id;
		}
		//Riddle interface 345
	}

	private enum ObjectMaps {
		Map1(358, new int[] { 18506, 2457, 3182 }, "Near an observatory meant for getting a compas on RS!"), Map2(361,
				new int[] { 354, 2565, 3248 },
				"Just south of a city known for thieving and outside a tower of clock!");

		int objectid, objectx, objecty;
		String hint;
		int interid;

		private ObjectMaps(int interid, int[] object, String text) {
			this.hint = text;
			this.interid = interid;
			this.objectid = object[0];
			this.objectx = object[1];
			this.objecty = object[2];
		}
	}

	private enum Maps {
		Map1(337, 2971, 3414, "If you Fala by A Door you might need help on this one!"), Map2(338, 3021, 3912,
				"Inbeetween a lava blaze and a near Deathly Agility Course!"), Map3(339, 2722, 3339,
						"South of where legends may be placed, and east of great thieving!"), Map4(341, 3435, 3265,
								"South of a muchky mucky mucky mucky swamp lands, and barely north of Haunted Mines!"), Map5(
										344, 2665, 3561,
										"West of a murderous Mansion, and south of a city of vikings!"), Map6(346, 3166,
												3359,
												"Slightly South of a city of great knights and lots of Shops!"), Map7(
														347, 3290, 3372,
														"A mining place located near a city of great knights and lots of Shops"), Map8(
																348, 3092, 3225,
																"Slightly south of a village known for thieving masters of farming!"), Map9(
																		351, 3043, 3398,
																		"NorthEast Corner of a city based around a castle with a mort around it!"), Map10(
																				352, 2906, 3295,
																				"Rite next to a guild known for people with skilled hands! [CRAFTING]"), Map11(
																						353, 2616, 3077,
																						"In a city that Rhymes with tan i will, if you say it really fast!"), Map12(
																								354, 2612, 3482,
																								"West of some woods that sound like Mc Jagger!"), Map13(
																										356, 3110, 3152,
																										"South of a tower full of magical people!"), Map14(
																												360,
																												2652,
																												3232,
																												"North of a tower known to give life and south of a city that contains thieving!"), Map15(
																														362,
																														2923,
																														3210,
																														"West of the place best known for starting a house!");

		String chat;
		int interfaceId, xcoord, ycoord;

		private Maps(int interid, int x, int y, String hint) {
			this.interfaceId = interid;
			this.xcoord = x;
			this.ycoord = y;
			this.chat = hint;
		}
	}

	public static Scrolls hasClue(Player p) {
		for (Scrolls scroll : Scrolls.values()) {
			if (p.getInventory().containsOneItem(scroll.id)) {
				return scroll;
			}
		}
		return null;
	}

	public static ObjectMaps hasObjectMapClue(Player p, int scrollid) {
		for (Scrolls scroll : Scrolls.values()) {
			if (scroll.getLocationMap() == null)
				continue;
			else {
				if (scroll.id == scrollid) {
					if (p.getInventory().containsOneItem(scroll.id)) {
						return scroll.getLocationMap();
					}
				}
			}
		}
		return null;
	}

	public static Maps hasMapClue(Player p, int scrollid) {
		for (Scrolls scroll : Scrolls.values()) {
			if (scroll.getHiding() == null)
				continue;
			else {
				if (scroll.id == scrollid) {
					if (p.getInventory().containsOneItem(scroll.id)) {
						return scroll.getHiding();
					}
				}
			}
		}
		return null;
	}

	public static Riddles hasRiddleClue(Player p, int scrollid) {
		for (Scrolls scroll : Scrolls.values()) {
			if (scroll.getRiddle() == null)
				continue;
			else {
				if (scroll.id == scrollid) {
					if (p.getInventory().containsOneItem(scroll.id)) {
						return scroll.getRiddle();
					}
				}
			}
		}
		return null;
	}
	
	public static boolean completedRiddle(Player p, int emoteid) {
		Scrolls scroll = hasClue(p);
		if (scroll != null) {
			if (hasRiddleClue(p, scroll.id) != null) {
				Riddles riddleclue = hasRiddleClue(p, scroll.id);
				WorldTile lastloc = p.getLastWorldTile();
				if (p.getX() >= riddleclue.locations[0]
					&& p.getY() <= riddleclue.locations[1]
					&& p.getX() <= riddleclue.locations[2]
					&& p.getY() >= riddleclue.locations[3]) {
					System.out.println(riddleclue.emoteid);
					System.out.println(riddleclue.name());
					System.out.println(emoteid);
					if (emoteid == riddleclue.emoteid) {
						p.getPackets().sendGameMessage("You have succesfully completed the riddle and have been awarded a chest!");
						p.getInventory().deleteItem(scroll.id, 1);
						p.getInventory().addItem(2717, 1);
					}
				}
			}
		}
		return false;
	}

	public static boolean objectSpot(Player p, WorldObject obj) {
		Scrolls scroll = hasClue(p);
		if (scroll != null) {
			if (hasObjectMapClue(p, scroll.id) != null) {
				ObjectMaps mapclue = hasObjectMapClue(p, scroll.id);
				p.getLastWorldTile();
				if (obj.getX() == mapclue.objectx && obj.getY() == mapclue.objecty && obj.getId() == mapclue.objectid) {
					p.getPackets().sendGameMessage(
							"You have succesfully completed the riddle and have been awarded a chest!");
					p.getInventory().deleteItem(scroll.id, 1);
					p.getInventory().addItem(2717, 1);
				}
			}
		}
		return false;
	}

	public static boolean digSpot(Player p){
		Scrolls scroll = hasClue(p);
		if (scroll != null){
		if (hasMapClue(p, scroll.id) != null){
			Maps mapclue = hasMapClue(p, scroll.id);
			WorldTile lastloc = p.getLastWorldTile();
			//if (lastloc.getX() == mapclue.xcoord && lastloc.getY() == mapclue.ycoord){
			if (p.getX() == mapclue.xcoord && p.getY() == mapclue.ycoord){
					p.getPackets().sendGameMessage("You have succesfully completed the riddle and have been awarded a chest!");
					p.getInventory().deleteItem(scroll.id, 1);
					p.getInventory().addItem(2717, 1);
				}
			}
		}
		return false;
		
	}

	public static void showObjectMap(Player p, ObjectMaps objmap) {
		//p.getPackets().sendInterface(false, p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
		//p.getInterfaceManager().hasRezizableScreen() ? 28 : 27, objmap.interid);
		p.getInterfaceManager().sendInterface(objmap.interid);
		p.getPackets().sendGameMessage(objmap.hint);

	}

	public static void showRiddle(Player p, Riddles riddle) {
		//p.getPackets().sendInterface(false, p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
		//p.getInterfaceManager().hasRezizableScreen() ? 28 : 27, 345);
		p.getInterfaceManager().sendInterface(345);
		p.getPackets().sendIComponentText(345, 1, riddle.riddles[0]);
		p.getPackets().sendIComponentText(345, 2, riddle.riddles[1]);
		p.getPackets().sendIComponentText(345, 3, riddle.riddles[2]);
		p.getPackets().sendIComponentText(345, 4, riddle.riddles[3]);
		p.getPackets().sendIComponentText(345, 5, riddle.riddles[4]);
		p.getPackets().sendIComponentText(345, 6, riddle.riddles[5]);
		p.getPackets().sendIComponentText(345, 7, riddle.riddles[6]);
		p.getPackets().sendIComponentText(345, 8, riddle.riddles[7]);
	}

	public static void showMap(Player p, Maps map) {
		p.getPackets().sendInterface(false, p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
				p.getInterfaceManager().hasRezizableScreen() ? 28 : 27, map.interfaceId);
		p.getPackets().sendGameMessage(map.chat);
	}

	static Item[] EasyRewards = { new Item(10366), new Item(2633), new Item(2635), new Item(2637),
			new Item(2583), new Item(2585), new Item(2587), new Item(2589), new Item(2591), new Item(2593),
			new Item(2595), new Item(2597), new Item(10306), new Item(10665), new Item(10308), new Item(10668),
			new Item(10310), new Item(10671), new Item(10312), new Item(10674), new Item(7390),
			new Item(7392), new Item(7394), new Item(7396), new Item(7386), new Item(7388), new Item(10316),
			new Item(10318), new Item(10320), new Item(10322), new Item(10324), new Item(10408), new Item(10410),
			new Item(10428), new Item(10430), new Item(10412), new Item(10414), new Item(10432),
			new Item(10434), new Item(10404), new Item(10406), new Item(10424), new Item(10426), new Item(10280),
			new Item(10282), new Item(10284), new Item(2631), new Item(7364), new Item(7368),
			new Item(7362), new Item(7366), new Item(10394), new Item(10398), new Item(10392), new Item(10396),
			new Item(10458), new Item(10464), new Item(10462), new Item(10466), new Item(10460),
			new Item(10468), new Item(13095), new Item(13105), new Item(8007, 10), new Item(8008, 10), new Item(8009, 10),
			new Item(8010, 10), new Item(8011, 10), new Item(8012, 10), new Item(8013, 10), new Item(10476, 15) };
			
	static Item[] MediumRewards = { new Item(10280), new Item(10282), new Item(10284), new Item(2599), new Item(2601)
, new Item(2603), new Item(2605), new Item(2607), new Item(2609), new Item(2611), new Item(2613), new Item(2577), new Item(2579)
, new Item(2645), new Item(2647), new Item(2649), new Item(7319), new Item(7321), new Item(7323), new Item(7325), new Item(7327)
, new Item(7372), new Item(7380), new Item(7370), new Item(7378), new Item(10296), new Item(10666), new Item(10298), new Item(10669)
, new Item(10300), new Item(10672), new Item(10302), new Item(10675), new Item(10304), new Item(10678), new Item(10400), new Item(10402)
, new Item(10420), new Item(10422), new Item(10416), new Item(10418), new Item(10436), new Item(10438), new Item(10452), new Item(10454)
, new Item(10456), new Item(10446), new Item(10448), new Item(10450), new Item(13109), new Item(13107), new Item(13111), new Item(13113)
, new Item(13115), new Item(10364), new Item(13097), new Item(13103) };		

	static Item[] HardRewards = { new Item(2661), new Item(2663), new Item(3479), new Item(2665), new Item(2667), new Item(2669)
, new Item(2671), new Item(2673), new Item(2675), new Item(3480), new Item(2653), new Item(2655), new Item(2657), new Item(2659)
, new Item(3478), new Item(2615), new Item(2617), new Item(2619), new Item(2621), new Item(3476), new Item(2623), new Item(2625)
, new Item(2627), new Item(2629), new Item(3477), new Item(7376), new Item(7384), new Item(7374), new Item(7382), new Item(8950)
, new Item(2581), new Item(7398), new Item(7399), new Item(7400), new Item(2639), new Item(2641), new Item(2643), new Item(10286)
, new Item(10667), new Item(10288), new Item(10670), new Item(10290), new Item(10673), new Item(10292), new Item(10676), new Item(10294)
, new Item(10679), new Item(10362), new Item(10470), new Item(10472), new Item(10474), new Item(10440), new Item(10442), new Item(10444)
, new Item(10386), new Item(10388), new Item(10390), new Item(13101), new Item(13099), new Item(3481), new Item(3483), new Item(3485)
, new Item(3486), new Item(3488)	};		
			
	static Item[] ThirdAge = { new Item(10330), new Item(10332), new Item(10334), new Item(10336), new Item(10338),
			new Item(10340), new Item(10342), new Item(10344), new Item(10346), new Item(10348), new Item(10350),
			new Item(10352), new Item(15288), new Item(15290), new Item(15291), new Item(15293), new Item(15295) };

	public static void giveReward(Player p){
		int random = Utils.getRandom(999)+1;
		int totalWealth = 0;
		if (p.cluenoreward == 1){
			random += 100;
			p.cluenoreward += 1;
		} else if (p.cluenoreward == 2){
			random += 250;
			p.cluenoreward += 1;
		} else if (p.cluenoreward == 3){
			random += 450;
		}
		if (random > 875){
			Item[] rewards;
			if (Utils.getRandom(500) > 475 && Utils.getRandom(999) > 950){
			rewards = new Item[] {
					EasyRewards[Utils.getRandom(EasyRewards.length - 1)],
					EasyRewards[Utils.getRandom(EasyRewards.length - 1)],
					EasyRewards[Utils.getRandom(EasyRewards.length - 1)],
					ThirdAge[Utils.getRandom(ThirdAge.length - 1)]};
					
			} else if (random > 800){
			rewards = new Item[] {
					MediumRewards[Utils.getRandom(MediumRewards.length - 1)],
					HardRewards[Utils.getRandom(HardRewards.length - 1)],
					HardRewards[Utils.getRandom(HardRewards.length - 1)]};
				
			} else {
			rewards = new Item[] {
					EasyRewards[Utils.getRandom(EasyRewards.length - 1)],
					EasyRewards[Utils.getRandom(EasyRewards.length - 1)],
					MediumRewards[Utils.getRandom(MediumRewards.length - 1)]};
			}
				for (Item item: rewards){
					if (!p.getInventory().hasFreeSlots()) {
						Item dropItem = new Item(item.getId(),Utils.random(item.getDefinitions().isStackable() ? item.getAmount() : item.getAmount()) + 1);
						World.addGroundItem(dropItem, p, p, true, 180, true);
						}
					if (item.getDefinitions().getTipitPrice() >= 700000) {
					World.sendWorldMessage("<img=4><col=1AB394>" + p.getDisplayName() + "<col=1AB394> has received<col=1AB394> " + item.getName() + "<col=1AB394> from a clue scroll!<img=4>", false);
					}						
					totalWealth += item.getDefinitions().getTipitPrice();
						p.getInventory().addItem(item);
						//p.getPackets().sendGameMessage("Congrats you have won a "+item.getName()+"! (GE: "+ item.getDefinitions().getValue() +")");
						/*p.getPackets().sendGameMessage("Congrats you have completed your clue, reward has a total wealth of: "+ totalWealth +".");
						totalWealth = 0;*/
						
				}
				p.getInterfaceManager().sendInterface(364);
				p.getPackets().sendItems(364, -1, 141, rewards);
				p.getInventory().deleteItem(2717,1);
				p.cluenoreward = 0;
				p.getPackets().sendGameMessage("Congrats you have completed your clue, reward has a total wealth of: "+ totalWealth +".");
				totalWealth = 0;
		} else {
			p.getPackets().sendGameMessage("You found another clue scroll inside the casket!");
			p.getInventory().deleteItem(2717,1);
			p.getInventory().addItem(ScrollIds[Utils.getRandom(ScrollIds.length - 1)], 1);
			if (p.cluenoreward == 0){
				p.cluenoreward += 1;
			}
		}
	}

}