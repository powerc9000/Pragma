package com.peter.entities;

import java.util.HashMap;

public class Inbox{
	public class Message{
		private String ID;
		private String type;
		
		public Message(){
			this.ID = "null";
			this.type = "null";
		}
		public void setID(String ID) {
			if(this.ID == "null")
				this.ID = ID;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getID() {
			return ID;
		}
		public String getType() {
			return type;
		}
	}
	private HashMap<String, Message> messageBox;
	private String temp;
	
	public Inbox(){
		messageBox = new HashMap<String, Message>();
		temp = new String();
	}
	
	public void setupMail(String ID){
		messageBox.put(ID, new Message());
	}
	
	//public void printClients(){
	//	for(int i=0; i<messageBoxSize; i++)
	//		System.out.println(messageBox[i].getID());
	//}
	
	public void sendMail(String ID, String type){
		messageBox.get(ID).setType(type);
	}
	public String checkMail(String ID){
		temp = messageBox.get(ID).getType();
		messageBox.get(ID).setType("null");
		return temp;
	}
}