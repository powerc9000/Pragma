package com.peter.rogue.lighting;

public class Line 
{
	public Point begin;
	public Point end;
	private float xi; // used to find intersections.
	private float yi; // used to find intersections.
	private float d;  // used to find intersections.
	private boolean line1XRange; // used to find intersections.
	private boolean line2XRange; // used to find intersections.
	private boolean line1YRange; // used to find intersections.
	private boolean line2YRange; // used to find intersections.
	
	public float slope;
	
	private float rotation;
	
	public Line(Point new_begin, Point new_end)
	{
		begin = new_begin;
		end = new_end;
		
		slope = (end.getY() - begin.getY()) / (end.getX() - begin.getX());
		
		//if(end.getX() < begin.getX())
		//{
			//Point temp = begin;
			//begin = end;
			//end = temp;
		//}
	}
	
	public boolean is_intersected(Line other){
		d = (getX() - getX2())*(other.getY() - other.getY2()) - (getY() - getY2())*(other.getX() - other.getX2());
		if(d == 0){
			return false;
		}else{
			findIntersectPoint(other);
			if(other.slope == 0){
				return checkSegmentXRange(other, xi);
			}else{
				return checkSegmentYRange(other, yi);
			}
		}
	}
	
	public Point returnIntersectedPoint(Line other){
		d = (getX() - getX2())*(other.getY() - other.getY2()) - (getY() - getY2())*(other.getX() - other.getX2());
		if(d != 0){
			findIntersectPoint(other);
			if(other.slope == 0){
				if(checkSegmentXRange(other, xi)){
					return new Point(xi, yi);
				}
			}else{
				if(checkSegmentYRange(other, yi)){
					return new Point(xi, yi);
				}
			}
		}
		return null;
	}
	
	private boolean isInRange(float bottom, float top, float value)
	{
		if(value - bottom >= 0 && value - top <= 0)
			return true;
		else
			return false;
	}
	
	private boolean checkSegmentXRange(Line other, float value)
	{
			if(getX() < getX2()){
				line1XRange = isInRange(getX(), getX2(), value);
			}else{
				line1XRange =  isInRange(getX2(), getX(), value);
			}
			if(other.getX() < other.getX2()){
				line2XRange = isInRange(other.getX(), other.getX2(), value);
			}else{
				line2XRange = isInRange(other.getX2(), other.getX(), value);
			}		
		return line1XRange && line2XRange;
	}
	
	
	private boolean checkSegmentYRange(Line other, float value)
	{
			if(getY() < getY2()){
				line1YRange = isInRange(getY(), getY2(), value);
			}else{
				line1YRange =  isInRange(getY2(), getY(), value);
			}
			if(other.getY() < other.getY2()){
				line2YRange = isInRange(other.getY(), other.getY2(), value);
			}else{
				line2YRange = isInRange(other.getY2(), other.getY(), value);
			}
		return line1YRange && line2YRange;
	}
	
	private void findIntersectPoint(Line other)
	{
		xi = ((other.getX() - other.getX2()) * (getX() * getY2() - getY() * getX2()) - (getX() - getX2()) * (other.getX() * other.getY2() - other.getY() * other.getX2()))/d;
		yi = ((other.getY() - other.getY2()) * (getX() * getY2() - getY() * getX2()) - (getY() - getY2()) * (other.getX() * other.getY2() - other.getY() * other.getX2()))/d;
	}
	
	public void rayRotate()
	{
		
	}
	
	public Point midpoint(){
		return begin.midpoint(end);
	}
	
	/*
	 * List of GET functions.
	 */
	public float getX(){
		return begin.getX();
	}
	public float getY(){
		return begin.getY();
	}
	public float getX2(){
		return end.getX();
	}
	public float getY2(){
		return end.getY();
	}
}
