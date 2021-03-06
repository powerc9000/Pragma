package com.peter.entities;

import java.util.LinkedList;

public class HostilityList{
	LinkedList<String> type;
	LinkedList<Integer> ID;
	
	public HostilityList(){
		type = new LinkedList<String>();
		ID = new LinkedList<Integer>();
	}
	
	public boolean check(Animate entity){
		for(int i=0; i<type.size(); i++){
			if(type.get(i).equals(entity.getType()))
				return true;
		}
		for(int i=0; i<ID.size(); i++){
			if(ID.get(i).equals(entity.getID()))
				return true;
		}
		return false;
	}
	public void addType(String type){
		this.type.add(type);
	}
	public void addID(Integer ID){
		this.ID.add(ID);
	}
}