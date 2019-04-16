package com.rs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


import com.rs.utils.Logger;
import com.rs.game.player.content.clans.Clan;
import com.rs.utils.SerializableFilesManager;
import com.rs.game.player.Player;
import com.rs.game.player.content.grandexchange.Offer;
import com.rs.game.player.content.grandexchange.OfferHistory;

public class SerializableFilesManager {
	
	public synchronized static final boolean containsPlayer(String username) {
		return new File("./data/characters/"+username+".p").exists();
		//return new File(System.getProperty("user.home") +"/characters/"+username+".p").exists();
	}
	private static final String GE_OFFERS = "./data/GE/grandExchangeOffers.ser";
    private static final String GE_OFFERS_HISTORY = "./data/GE/grandExchangeOffersTrack.ser";
    private static final String GE_PRICES = "./data/GE/grandExchangePrices.ser";
	private static final String CLAN_PATH = "data/clans/";
    
	public synchronized static Player loadPlayer(String username) {
		try {
			return (Player) loadSerializedFile(new File("./data/characters/"+username+".p"));
			//return (Player) loadSerializedFile(new File(System.getProperty("user.home") +"/characters/"+username+".p"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized static void savePlayer(Player player) {
		try {
			storeSerializableClass(player, new File("./data/characters/"+player.getUsername()+".p"));
			//storeSerializableClass(player, new File(System.getProperty("user.home") +"/characters/"+player.getUsername()+".p"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean containsClan(String clanName) {
		File file = new File(CLAN_PATH + clanName + ".c");
		return file.exists();
	}

	public synchronized static void saveClan(Clan clan) {
		try {
			if (clan != null && clan.getFileName() != null && !clan.getFileName().equalsIgnoreCase("null"))
				storeSerializableClass(clan, new File(CLAN_PATH + clan.getFileName() + ".c"));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static Clan loadClan(String clanName) {
		try {
			return (Clan) loadSerializedFile(new File(CLAN_PATH + clanName + ".c"));
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}

	public static void deleteClan(Clan clan) {
		File file = new File(CLAN_PATH + Utils.formatPlayerNameForDisplay(clan.getClanLeaderUsername()) + ".c");
		if (!file.exists()) {
			return;
		}
		file.delete();
	}
	
    public static final Object loadSerializedFile(File f) throws IOException, ClassNotFoundException {
    	if(!f.exists())
    		return null;
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
        Object object = in.readObject();
        in.close();
        return object;
    }
	
	public synchronized static void deleteOffers() {
		try {
			new File(GE_OFFERS).delete();
		} catch (Throwable t) {
			Logger.handle(t);
		}
	}
    
    public static final void storeSerializableClass(Serializable o, File f) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
        out.writeObject(o);
        out.close();
    }
	
	private SerializableFilesManager() {
		
	}

    @SuppressWarnings("unchecked")
	public static synchronized HashMap<Long, Offer> loadGEOffers() {
    	if (new File(GE_OFFERS).exists()) {
    	    try {
    	    	return (HashMap<Long, Offer>)loadSerializedFile(new File(GE_OFFERS));
    	    }
    	    catch (Throwable t) {
    	    	Logger.handle(t);
    		return null;
    	    }
    	}
    	else {
    	    return new HashMap<Long, Offer>();
    	}
    }
    
    public static synchronized ArrayList<OfferHistory> loadGEHistory() {
    	if (new File(GE_OFFERS_HISTORY).exists()) {
    	    try {
    		return (ArrayList<OfferHistory>)loadSerializedFile(new File(GE_OFFERS_HISTORY));
    	    }
    	    catch (Throwable t) {
    		Logger.handle(t);
    		return null;
    	    }
    	}
    	else {
    	    return new ArrayList<OfferHistory>();
    	}
    }
    
    @SuppressWarnings("unchecked")
	public static synchronized HashMap<Integer, Integer> loadGEPrices() {
    	if (new File(GE_PRICES).exists()) {
    	    try {
    		return (HashMap<Integer, Integer>)loadSerializedFile(new File(GE_PRICES));
    	    }
    	    catch (Throwable t) {
    		Logger.handle(t);
    		return null;
    	    }
    	}
    	else {
    	    return new HashMap<Integer, Integer>();
    	}
    }
    
    public static synchronized void saveGEOffers(HashMap<Long, Offer> offers) {
    	try {
    	    SerializableFilesManager.storeSerializableClass(offers, new File(GE_OFFERS));
    	}
    	catch (Throwable t) {
    	    Logger.handle(t);
    	}
    }
    
    public static synchronized void saveGEHistory(ArrayList<OfferHistory> history) {
		try {
		    SerializableFilesManager.storeSerializableClass(history, new File(GE_OFFERS_HISTORY));
		}
		catch (Throwable t) {
		    Logger.handle(t);
		}
    }
    
    public static synchronized void saveGEPrices(HashMap<Integer, Integer> prices) {
		try {
		    SerializableFilesManager.storeSerializableClass(prices, new File(GE_PRICES));
		}
		catch (Throwable t) {
		    Logger.handle(t);
		}
    }
    
	
}
