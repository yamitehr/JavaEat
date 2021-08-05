package Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Model.Customer;
import Model.Delivery;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Order;
import Model.OrderStatus;
import Model.Restaurant;

public class DeliveryManager {
	
	private static DeliveryManager instance;
	
	private HashSet<Order> finishedOrders;
	private HashMap<DeliveryPerson, LinkedList<Delivery>> readyDeliveriesByDeliveryPerson;
	private HashSet<DeliveryPerson> freeDeliveryPersons;
	
	private DeliveryManager() {
		freeDeliveryPersons = new HashSet<DeliveryPerson>();
		finishedOrders = new HashSet<Order>();
		readyDeliveriesByDeliveryPerson = new HashMap<DeliveryPerson, LinkedList<Delivery>>();
	}
	
	public static DeliveryManager getInstance() {
		if (instance == null) {
			instance = new DeliveryManager();
		}
		return instance;
	}
	
	public void addFinishedOrder(Order order) {
		Logger.Log("[addFinishedOrder] got finished order - " + order.getId());
		finishedOrders.add(order);
	}
	
	private DeliveryArea getDeliveryAreaForOrder(Order order){
		Neighberhood n = order.getCustomer().getNeighberhood();
		
		for(DeliveryArea d : Restaurant.getInstance().getAreas().values()) {
			if(d.getNeighberhoods().contains(n)) {
				return d;
			}
		}
		return null;
	}
	
	private DeliveryPerson getDeliveryPersonByDeliveryArea(DeliveryArea area){
		
		for(DeliveryPerson dp : Restaurant.getInstance().getDeliveryPersons().values()) {
			if(dp.getArea().equals(area)) {
				return dp;
			}
		}
		return null;
	}
	
	private HashMap<DeliveryArea, TreeSet<Order>> getOrdersMapping(){
		
		HashMap<DeliveryArea, TreeSet<Order>> orderByDeliveryArea = new HashMap<DeliveryArea, TreeSet<Order>>();
		
		for(Order order : finishedOrders) {
			DeliveryArea area = getDeliveryAreaForOrder(order);
			//if there is no da that contains the customer neighborhood, cancel the order
			if(area == null) {
				order.setStatus(OrderStatus.cancelled);
			} else {
				if(!orderByDeliveryArea.containsKey(area)) {
					orderByDeliveryArea.put(area, new TreeSet<Order>());
				}
				orderByDeliveryArea.get(area).add(order);
			}
		}
		return orderByDeliveryArea;
	}
	
	public Delivery getDeliveryForDeliveryPerson(DeliveryPerson dp) {
		if (readyDeliveriesByDeliveryPerson.containsKey(dp)) {
			Delivery delivery = readyDeliveriesByDeliveryPerson.get(dp).poll();
			if (delivery != null) {
				freeDeliveryPersons.remove(dp);
				Logger.Log("[getDeliveryForDeliveryPerson] Found a delivery for delivery person - " + dp.getFirstName());
			}
			return delivery;
		}
		return null;
	}
	
	public void startDeliveriesTask() {
		Runnable ordersTask = new Runnable() {
		    public void run() {
				Logger.Log("[startDeliveriesTask] starting new task");
		    	HashMap<DeliveryArea, TreeSet<Order>> orderByDeliveryArea = getOrdersMapping();
		    	finishedOrders.clear();
		    	Restaurant.getInstance().setOrderByDeliveryArea(orderByDeliveryArea);
		    	
		    	for (Entry<DeliveryArea, TreeSet<Order>> pair : orderByDeliveryArea.entrySet()) {
					DeliveryPerson dp = getDeliveryPersonByDeliveryArea(pair.getKey());
					
					TreeSet<Delivery> deliveries = Restaurant.getInstance().createAIMacine(dp, pair.getKey(), pair.getValue());
					
					if (!readyDeliveriesByDeliveryPerson.containsKey(dp)) {
						readyDeliveriesByDeliveryPerson.put(dp, new LinkedList<Delivery>());
					}
					
		    		for (Delivery d : deliveries) {
		    			 readyDeliveriesByDeliveryPerson.get(dp).add(d);
		    		 }
		    		
		    		if (freeDeliveryPersons.contains(dp)) {
						Logger.Log("[startDeliveriesTask] new delivery for free delivery person - " + dp.getFirstName());
		    			dp.getNewDelivery();
		    		}
		    	}
		    	
		    }
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(ordersTask, 0, 15, TimeUnit.SECONDS);
		Restaurant.getInstance().getDeliveryPersons().values().forEach(dp -> dp.getNewDelivery());
	}
	
	public void setFreeDeliveryPerson(DeliveryPerson dp) {
		freeDeliveryPersons.add(dp);
	}
}
