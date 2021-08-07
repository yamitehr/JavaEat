package Model;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

import Utils.DeliveryManager;
import Utils.Gender;
import Utils.Logger;
import Utils.Vehicle;

public class DeliveryPerson extends Person {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int idCounter = 1;
	private Vehicle vehicle;
	private DeliveryArea area;
	
	public DeliveryPerson(String firstName, String lastName, LocalDate birthDay, Gender gender, Vehicle vehicle,
			DeliveryArea area) {
		super(idCounter++, firstName, lastName, birthDay, gender);
		this.vehicle = vehicle;
		this.area = area;
		getNewDelivery();
	}
	
	public DeliveryPerson(int id) {
		super(id);
	}
	
	public static int getIdCounter() {
		return idCounter;
	}
	public static void setIdCounter(int idCounter) {
		DeliveryPerson.idCounter = idCounter;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public DeliveryArea getArea() {
		return area;
	}
	public void setArea(DeliveryArea area) {
		this.area = area;
	}
	@Override
	public String toString() {
		return super.toString();
	}

	public void getNewDelivery() {
		Delivery newDelivery = DeliveryManager.getInstance().getDeliveryForDeliveryPerson(this);
		if (newDelivery != null) {
			Logger.Log("[getNewDelivery] delivery person - " + getFirstName() + " starting new delivery timer");
			newDelivery.startDeliveryTimer();	
		} else {
			DeliveryManager.getInstance().setFreeDeliveryPerson(this);
			Logger.Log("[getNewDelivery] delivery person - " + getFirstName() + " no new deliveries, adding to free dps");
		}
	}
	
	protected Object readResolve() {
		if (this.id >= idCounter) {
			idCounter = this.id + 1;
		}
	    return this;
	}
}
