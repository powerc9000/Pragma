package com.peter.packets;

public class NPCPacket {
	public Integer ID;
	public int x, y;
	public int oldX, oldY;
	public NPCPacket(){};
	public NPCPacket(int x, int y, Integer ID){
		this.x = x;
		this.y = y;
		this.ID = ID;
	}
}
