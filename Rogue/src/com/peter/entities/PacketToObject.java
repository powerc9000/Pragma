package com.peter.entities;

import com.peter.inventory.Food;
import com.peter.inventory.Item;
import com.peter.inventory.Wearable;
import com.peter.map.Tile;
import com.peter.packets.ItemPacket;

public class PacketToObject{
	private static Item temp;
	
	public static Tile tileConverter(byte tile){
		switch(tile){
		case 0:
			return Tile.BLANK;
		case 1:
			return Tile.GROUND;
		case 2:
			return Tile.WALL;
		case 3:
			return Tile.DOOR;
		case 4:
			return Tile.WATER;
		case 5:
			return Tile.DOWN;
		case 6:
			return Tile.UP;
		}
		return null;
	}
	
	public static Item itemConverter(ItemPacket item){
		switch(item.name){
		case "Gold":
			temp = new Item(Item.GOLD);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Gem":
			temp = new Item(Item.GEM);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Breast Plate":
			temp = new Wearable(Wearable.BREAST_PLATE);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Hat":
			temp = new Wearable(Wearable.HAT);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Ring":
			temp = new Wearable(Wearable.RING);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Shoes":
			temp = new Wearable(Wearable.SHOES);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Helmet":
			temp = new Wearable(Wearable.HELMET);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Bread":
			temp = new Food(Food.BREAD);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		case "Meat":
			temp = new Food(Food.MEAT);
			temp.setPosition(item.x, item.y);
			temp.ID = item.ID;
			return temp;
		}
		return null;
	}
}
