package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Utils.DeliveryManager;
import Utils.Logger;
import Utils.MyFileLogWriter;

public class Order implements Comparable<Order>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int idCounter = 1;
	private Integer id;
	private Customer customer;
	private ArrayList<Dish> dishes;
	private Delivery delivery;
	private OrderStatus status;
	
	// constructors 
	
	public Order(Customer customer, ArrayList<Dish> dishes, Delivery delivery) {
		super();
		this.id = idCounter++;
		this.customer = customer;
		this.dishes = dishes;
		this.delivery = delivery;
		this.status = OrderStatus.InProgress;
	}
	
	public Order(int id) {
		this.id = id;
	}
	
	// getters setters
	
	public static int getIdCounter() {
		return idCounter;
	}

	public static void setIdCounter(int idCounter) {
		Order.idCounter = idCounter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Dish> getDishes() {
		return Collections.unmodifiableList(dishes);
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return id + " " + customer;
	}
	
	// methods

	public double calcOrderRevenue() {
		double revenue = 0.0;
		for(Dish d : getDishes()) {
			double price = d.calcDishPrice();
			double cost = 0.0;
			for(Component c : d.getComponenets()) {
				cost += c.getPrice();
			}
			revenue += (price - cost);
		}
		return revenue;
	}
	
	public int orderWaitingTime(DeliveryArea da) {
		int time = 0;
		time += da.getDeliverTime();
		for(Dish d : getDishes()) {
			time += d.getTimeToMake();
		}
		return time;
	}

	public void startOrderTimer() {
		long time = 0;
		for(Dish d : getDishes()) {
			time += d.getTimeToMake();
		}
		
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	status = OrderStatus.readyForDelivery;
	    		Logger.Log("[startOrderTimer] order time done for " + getId());
	        }
	    };
	    
	    Timer timer = new Timer();
	    long timeInSeconds = time * 60000 / 60;
	    timer.schedule(task, timeInSeconds);
	}
	
	public boolean addDish(Dish d) {
		return dishes.add(d);
	}
	
	public boolean removeDish(Dish d) {
		return dishes.remove(d);
	}
	
	@Override
	public int compareTo(Order o) {
		return this.id.compareTo(o.getId());
	}

	protected Object readResolve() {
		if (this.id >= idCounter) {
			idCounter = this.id + 1;
		}
	    return this;
	}
}
