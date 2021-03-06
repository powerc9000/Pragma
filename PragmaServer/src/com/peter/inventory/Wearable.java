package com.peter.inventory;

public class Wearable extends Item{
	public static Wearable HELMET = new Wearable("Helmet", 5, 17, "at.png", "Head", 3);
	public static Wearable HAT = new Wearable("Hat", 2, 6, "^.png", "Head", 1);
	public static Wearable BREAST_PLATE = new Wearable("Breast Plate", 5, 17, "[.png", "Body", 3);
	public static Wearable SHOES = new Wearable("Shoes", 2, 6, "congruent.png", "Feet", 1);
	public static Wearable RING = new Wearable("Ring", 1, 20, "=.png", "Ring", 1);
	
	private int defense;
	private String type;
	
	public Wearable(String name, int weight, int value, String filename, String type, int defense) {
		super(name, weight, value, filename);
		this.defense = defense;
		this.type = type;
	}
	
	public Wearable(Wearable item) {
		super((Item) item);
		this.defense = item.defense;
		this.type = item.type;
	}
	public int getDefense(){
		return defense;
	}
	public String getType(){
		return type;
	}
}