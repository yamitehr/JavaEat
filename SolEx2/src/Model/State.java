package Model;

import Controllers.CurrentDishModel;

public class State {
	
	private static Customer _currentCustomer;
	private static Order _currentOrder;
	private static CurrentDishModel _currentDish;
	
	public static void setCurrentDish(CurrentDishModel dish) {
		_currentDish = dish;
	}
	
	public static CurrentDishModel getCurrentDish() {
		return _currentDish;
	}
	
	public static void setCurrentCustomer(Customer cust) {
		_currentCustomer = cust;
	}
	
	public static Customer getCurrentCustomer() {
		return _currentCustomer;
	}
	
	public static void setCurrentOrder(Order order) {
		_currentOrder = order;
	}
	
	public static Order getCurrentOrder() {
		return _currentOrder;
	}
	
	public static void cleanState() {
		_currentCustomer = null;
		_currentOrder = null;
		_currentDish = null;
	}
}
