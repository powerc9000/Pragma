package com.peter.server;

import com.peter.map.Tile;

public class ObjectToPacket{
	
	public static byte tileConverter(Tile tile){
		if(tile == Tile.BLANK)
			return 0;
		else if(tile == Tile.GROUND)
			return 1;
		else if(tile == Tile.WALL)
			return 2;
		else if(tile == Tile.DOOR)
			return 3;
		else if(tile == Tile.WATER)
			return 4;
		else if(tile == Tile.DOWN)
			return 5;
		else if(tile == Tile.UP)
			return 6;
		return 1;
	}
}
