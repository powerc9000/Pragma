package com.peter.rogue.data;

import java.util.ArrayList;
import java.util.Stack;

import com.peter.entities.NPC;
import com.peter.inventory.Chest;
import com.peter.inventory.Item;
import com.peter.map.Tile;

public class LevelData {
    private int citizens, shopkeeps, monsters;
	public Stack<Tile[][]> tiles;
	public Stack<String[][]> visible;
	public Stack<String[][]> marker;
	public Stack<ArrayList<Item>> items;
	public Stack<ArrayList<Chest>> chests;
	public Stack<ArrayList<NPC>> npcs;
    
	/*
		citizens = 40000;
		shopkeeps = 10000;
		monsters = 20000;
		steady 30fps
	*/
	
	public LevelData(){
		tiles = new Stack<Tile[][]>();
		visible = new Stack<String[][]>();
		marker = new Stack<String[][]>();
		items = new Stack<ArrayList<Item>>();
		chests = new Stack<ArrayList<Chest>>();
		npcs = new Stack<ArrayList<NPC>>();
		citizens = 4;
		shopkeeps = 2;
		monsters = 2;
	}
	
	public int getCitizens() {
		return citizens;
	}
	public int getShopkeeps() {
		return shopkeeps;
	}
	public int getMonsters() {
		return monsters;
	}
	public int getNPCTotal() {
		return monsters + citizens + shopkeeps;
	}

	public void save(int WIDTH, int HEIGHT, Tile[][] tiles, String[][] visible, String[][] marker, ArrayList<Item> items, /*ArrayList<Chest> chests,*/ ArrayList<NPC> npcs){
		Tile[][] temp1 = new Tile[WIDTH][HEIGHT];
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				temp1[x][y] = tiles[x][y];
		this.tiles.push(temp1);
		
		String[][] temp2 = new String[WIDTH][HEIGHT];
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				temp2[x][y] = new String(visible[x][y]);
		this.visible.push(temp2);
		
		temp2 = new String[WIDTH][HEIGHT];
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				temp2[x][y] = new String(marker[x][y]);
		this.marker.push(temp2);

		ArrayList<Item> temp3 = new ArrayList<Item>();
		for(int i=0; i<items.size(); i++)
			temp3.add(items.get(i));
		this.items.push(temp3);
		
		/*ArrayList<Chest> temp4 = new ArrayList<Chest>();
		for(int i=0; i<chests.size(); i++)
			temp4.add(chests.get(i));
		this.chests.push(temp4);*/
		
		ArrayList<NPC> temp5 = new ArrayList<NPC>();
		for(int i=0; i<npcs.size(); i++)
			temp5.add(npcs.get(i));
		this.npcs.push(temp5);
	}
}
