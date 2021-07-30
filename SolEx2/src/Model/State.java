package Model;

public class State {
	
	private static Customer _currentCustomer;
	private static Order _currentOrder;
	
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
