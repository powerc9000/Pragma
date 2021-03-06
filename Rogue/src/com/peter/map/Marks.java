package com.peter.map;


public class Marks{

	public final int HEIGHT = 40, WIDTH = 80;
	private Integer[][] marker = new Integer[WIDTH][HEIGHT];
	
	public Marks(){
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				marker[i][j] = -1;
	}
	
	public Integer get(int x, int y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return null;
		else{
			return marker[(int)(x/32)][(int)(y/32)];
		}
	}
	
	public void put(Integer ID, int x, int y){
		marker[(int)(x/32)][(int)(y/32)] = ID;
	}

	public void find(int ID, int mark) {
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				if(marker[i][j] == ID){
					marker[i][j] = -1;
					break;
				}
	}
}
