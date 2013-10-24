package com.peter.rogue.inventory;

import com.peter.rogue.entities.Entity;

public class Item extends Entity{
	private int weight;
	private boolean pickedUp;
	public Item(String name, int weight, String filename){
		super(filename, "item");
		this.name = name;
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
	@Override
	public void setPosition(float y, float x){
		setY(y * 32);
		setX(x * 32);
		pickedUp = false;
		setMap((int)getY(), (int)getX(), this.entry);
	}
	public void pickedUp(boolean pickedUp){
		this.pickedUp = pickedUp;
	}
	public boolean isPickedUp(){
		return pickedUp;
	}
}