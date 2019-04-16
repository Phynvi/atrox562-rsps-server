package com.rs.game.item;

import java.io.Serializable;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemEquipIds;

/**
 * Represents a single item.
 * <p/>
 *
 * @author Graham / edited by Dragonkk(Alex)
 */
public class Item implements Serializable {

    private static final long serialVersionUID = -6485003878697568087L;

    private short id;
    protected int amount;
	private int charges;

	public int length;

    public short getId() {
            return id;
    }
	
	public int getCharges() {
		return charges;
	}
    
    public Item clone() {
        return new Item(id, amount);
    }

    public Item(int id) {
    	this(id, 1);
    }

    public Item(int id, int amount) {
        this(id, amount, false);
    }
	
	public Item(int id, int amount, int charges) {
		this(id, amount, false, charges);
	}

    public Item(int id, int amount, boolean amt0) {
        this.id = (short) id;
        this.amount = amount;
        if (this.amount <= 0 && !amt0) {
            this.amount = 1;
        }
    }
	
	public Item(int id, int amount, boolean amt0, int charges) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0)
			this.amount = 1;
		this.charges = charges;
	}

	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(id);
	}
    
    public int getEquipId() {
    	return ItemEquipIds.getEquipId(id);
    }
    
    public void setId(int id) {
    	this.id = (short) id;
    }
	
	public void setCharges(int amount) {
		this.charges = amount;
	}

    public int getAmount() {
            return amount;
    }
    
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return getDefinitions().getName();
	}

}
