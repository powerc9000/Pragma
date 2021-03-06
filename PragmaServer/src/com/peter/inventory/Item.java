package com.peter.inventory;

import com.peter.entities.Entity;

public class Item extends Entity{
	
	public static Item GOLD = new Item("Gold", 0, 5, "$.png");
	public static Item GEM = new Item("Gem", 0, 10, "asterisk.png");
	
	private int weight;
	private int value;
	private String filename;
	private boolean pickedUp;
	
	public Item(String name, int weight, int value, String filename){
		super("Item");
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.filename = filename;
	}
	
	public Item(Item item){
		super("Item");
		this.name = item.name;
		this.weight = item.weight;
		this.value = item.value;
		this.filename = item.filename;
	}
	
	public void setPosition(int x, int y){
		setX(x * 32);
		setY(y * 32);

		pickedUp = false;
	}
	
	public int getWeight() {
		return weight;
	}
	public int getValue(){
		return value;
	}
	public String getFilename() {
		return filename;
	}
	public void pickedUp(boolean pickedUp){
		this.pickedUp = pickedUp;
	}
	public boolean isPickedUp(){
		return pickedUp;
	}
}