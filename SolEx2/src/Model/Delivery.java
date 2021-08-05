package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

import Utils.DeliveryManager;
import Utils.Logger;

public abstract class Delivery implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int idCounter = 1;
	private int id;
	private DeliveryPerson deliveryPerson;
	private DeliveryArea area;
	private boolean isDelivered;
	private LocalDate deliveredDate;
	
	public Delivery(DeliveryPerson deliveryPerson, DeliveryArea area,
			boolean isDelivered,LocalDate diliveredDate) {
		super();
		this.id = idCounter++;
		this.deliveryPerson = deliveryPerson;
		this.area = area;
		this.isDelivered = isDelivered;
		this.deliveredDate = diliveredDate;
	}
	
	public Delivery(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public DeliveryPerson getDeliveryPerson() {
		return deliveryPerson;
	}

	public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

	public DeliveryArea getArea() {
		return area;
	}

	public void setArea(DeliveryArea area) {
		this.area = area;
	}

	public boolean isDelivered() {
		return isDelivered;
	}

	public void setDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	

	public LocalDate getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(LocalDate deliveredDate) {
		this.deliveredDate = deliveredDate;
	}


	public void startDeliveryTimer() {
		Logger.Log("[startDeliveryTimer] starting delivery timer");
		long time = area.getDeliverTime();
		
		Delivery thisDelivery = this;
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	Restaurant.getInstance().deliver(thisDelivery);
	        	//Tell dp to get new delivery
	    		Logger.Log("[startDeliveryTimer] timer is done, telling dp to get new delivery");
	    		thisDelivery.deliveryPerson.getNewDelivery();
	        }
	    };
	    
	    Timer timer = new Timer();
	    long timeInSeconds = time * 60000 / 60;
	    timer.schedule(task, timeInSeconds);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Delivery other = (Delivery) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Delivery [id=" + id + ", deliveryPerson=" + deliveryPerson + ", area=" + area + ", isDelivered="
				+ isDelivered + "]";
	}
	
	protected Object readResolve() {
		if (this.id == idCounter) {
			idCounter = this.id + 1;
		}
	    return this;
	}
}
