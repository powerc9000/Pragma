package com.peter.rogue.lighting;

import com.badlogic.gdx.math.Vector2;

public class Point 
{
	private float x;
	private float y;
	
	private Vector2 vect;
	
	public Point(){
		x = 0;
		y = 0;
		vect = new Vector2(x,y);
	}
	
	public Point(float new_x, float new_y){
		x = new_x;
		y = new_y;
		vect = new Vector2(x,y);
	}
	
	public float distance(Point p){
		return (float)Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.y - y, 2));
	}
	
	public Point midpoint(Point p){
		return new Point((p.getY() + y)/2 , (p.getX() + x)/2);
	}
	
	public float angleRelativeToPoint(Point p){
		float rotation = (float)Math.toDegrees(Math.atan2(p.getY() - y, p.getX() - x));
		return rotation;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setXY(float new_x, float new_y){
		x = new_x;
		y = new_y;
	}
	
	public Vector2 returnVect(){
		return vect;
	}
}