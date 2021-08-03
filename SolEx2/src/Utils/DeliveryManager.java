package Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import Model.Delivery;
import Model.DeliveryPerson;
import Model.Order;

public class DeliveryManager {
	
	private static DeliveryManager instance;
	
	private HashSet<Order> finishedOrders;
	private HashMap<DeliveryPerson, Queue<Delivery>> readyDeliveriesByDeliveryPerson;
	
	private DeliveryManager() {
		finishedOrders = new HashSet<Order>();
		readyDeliveriesByDeliveryPerson = new HashMap<DeliveryPerson, Queue<Delivery>>();
	}
	
	public static DeliveryManager getInstance() {
		if (instance == null) {
			instance = new DeliveryManager();
		}
		return instance;
	}
	
	public void addFinishedOrder(Order order) {
		finishedOrders.add(order);
	}
	
	public Queue<Delivery> getDeliveryByDeliveryPerson(DeliveryPerson dp){
		return readyDeliveriesByDeliveryPerson.get(dp);
	}
	
}
