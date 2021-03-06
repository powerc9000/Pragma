package com.peter.rogue.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.peter.entities.EntityManager;
import com.peter.entities.Shopkeep;
import com.peter.packets.AddNPCPacket;
import com.peter.packets.AddPlayerPacket;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.AttackPacket;
import com.peter.packets.ChestPacket;
import com.peter.packets.ItemPacket;
import com.peter.packets.MapPacket;
import com.peter.packets.MessagePacket;
import com.peter.packets.NPCPacket;
import com.peter.packets.PlayerPacket;
import com.peter.packets.RemoveItemPacket;
import com.peter.packets.RemoveNPCPacket;
import com.peter.packets.RemovePlayerPacket;
import com.peter.packets.RemoveTradeItemPacket;
import com.peter.rogue.screens.Play;

public class Network extends Listener {
	public void connected(Connection c) {
		EntityManager.player.setID(c.getID());
	}
	public void received(Connection c, Object o){
		if(o instanceof MessagePacket){
			MessagePacket packet = (MessagePacket) o;
			if(Play.map.players.containsKey(packet.receiverID))
				Play.map.players.get(packet.receiverID).setMessage(packet.message);
			else if(Play.map.npcs.containsKey(packet.receiverID))
				Play.map.npcs.get(packet.receiverID).setMessage(packet.message);
		}
		else if(o instanceof RemoveItemPacket){
			RemoveItemPacket packet = (RemoveItemPacket) o;
			Play.map.marks.put(-1, packet.x, packet.y);
			Play.map.database.remove(packet.ID);
			Play.map.items.remove(packet.ID);
		}
		else if(o instanceof AddPlayerPacket){
			AddPlayerPacket packet = (AddPlayerPacket) o;
			EntityManager.playerQueue.add(packet);
		}
		else if(o instanceof RemovePlayerPacket){
			RemovePlayerPacket packet = (RemovePlayerPacket) o;
			Play.map.database.remove(packet.ID);
			Play.map.players.remove(packet.ID);
		}
		else if(o instanceof PlayerPacket){
			PlayerPacket packet = (PlayerPacket) o;
			if(Play.map.players.get(packet.ID) != null){
				Play.map.players.get(packet.ID).setX(packet.x);
				Play.map.players.get(packet.ID).setY(packet.y);
				Play.map.marks.put(packet.ID, packet.x, packet.y);
				Play.map.players.get(packet.ID).setOldX(packet.oldX);
				Play.map.players.get(packet.ID).setOldY(packet.oldY);
				Play.map.marks.put(-1, packet.oldX, packet.oldY);
			}
		}
		else if(o instanceof NPCPacket){
			NPCPacket packet = (NPCPacket) o;
			if(Play.map.npcs.get(packet.ID) != null){
				Play.map.npcs.get(packet.ID).setX(packet.x);
				Play.map.npcs.get(packet.ID).setY(packet.y);
				Play.map.marks.put(packet.ID, packet.x, packet.y);
				Play.map.npcs.get(packet.ID).setOldX(packet.oldX);
				Play.map.npcs.get(packet.ID).setOldY(packet.oldY);
				Play.map.marks.put(-1, packet.oldX, packet.oldY);
			}
		}
		else if(o instanceof RemoveNPCPacket){
			RemoveNPCPacket packet = (RemoveNPCPacket) o;
			Play.map.marks.put(-1, (int) Play.map.npcs.get(packet.ID).getX(), (int) Play.map.npcs.get(packet.ID).getY());
			Play.map.database.remove(packet.ID);
			Play.map.npcs.remove(packet.ID);
		}
		else if(o instanceof MapPacket){
			MapPacket packet = (MapPacket) o;
			for(ItemPacket item : packet.items.values())
				EntityManager.itemQueue.add(item);
			for(ChestPacket chest : packet.chests.values())
				EntityManager.chestQueue.add(chest);
			for(AddNPCPacket npc : packet.npcs.values())
				EntityManager.NPCQueue.add(npc);

			for(int x=0; x<Play.map.WIDTH; x++)
				for(int y=0; y<Play.map.HEIGHT; y++)
					Play.map.init[x][y] = packet.tiles[x][y];
		}
		else if(o instanceof RemoveTradeItemPacket){
			RemoveTradeItemPacket packet = (RemoveTradeItemPacket) o;
			if(Play.map.chests.containsKey(packet.ID))
				Play.map.chests.get(packet.ID).items.remove(packet.index);
			else if(Play.map.npcs.containsKey(packet.ID))
				((Shopkeep) Play.map.npcs.get(packet.ID)).items.remove(packet.index);
		}
		else if(o instanceof AddTradeItemPacket){
			AddTradeItemPacket packet = (AddTradeItemPacket) o;
			EntityManager.tradeItemQueue.add(packet);
		}
		else if(o instanceof ItemPacket){
			ItemPacket packet = (ItemPacket) o;
			EntityManager.itemQueue.add(packet);
		}
		else if(o instanceof AttackPacket){
			AttackPacket packet = (AttackPacket) o;
			if(Play.map.npcs.containsKey(packet.receiverID)){
				Play.map.npcs.get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
				Play.map.npcs.get(packet.receiverID).setStatus(packet.amount);
			}
			else if(Play.map.players.containsKey(packet.receiverID)){
				Play.map.players.get(packet.receiverID).getStats().mutateHitpoints(packet.amount);
				Play.map.players.get(packet.receiverID).setStatus(packet.amount);
			}
			else{
				EntityManager.player.getStats().mutateHitpoints(packet.amount);
				EntityManager.player.setStatus(packet.amount);
			}
		}
	}
	public void disconnected (Connection connection) {
	}
}
