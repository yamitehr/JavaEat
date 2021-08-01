package Model;

public class State {
	
	private static Customer _currentCustomer;
	private static Order _currentOrder;
	private static Dish _currentDish;
	
	public static void setCurrentDish(Dish dish) {
		_currentDish = dish;
	}
	
	public static Dish getCurrentDish() {
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
}
