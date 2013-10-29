package com.peter.rogue.map;

import java.util.HashMap;

import com.peter.rogue.entities.Entity;

public class Data {
    private HashMap<String, Entry> database;
    
    class Entry{
    	private Entity entity;
    	public Entry(Entity entity){
    		this.entity = entity;
    	}
    	public Entity getEntity(){
    		return entity;
    	}
    }

	public Data(){
		database = new HashMap<String, Entry>();
	}
	public void put(String ID, Entity entity){
		database.put(ID, new Entry(entity));
	}
	public void remove(String ID){
		database.remove(ID);
	}
	public Entity get(String ID){
		return database.get(ID).getEntity();
	}
}