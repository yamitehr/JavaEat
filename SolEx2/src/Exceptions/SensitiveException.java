package Exceptions;

import Model.Component;

public class SensitiveException extends Exception {

	public SensitiveException(String customerName, String dishName) {
		super("Customer " + customerName + " is sensitive to one of the components in the dish " + dishName + "!");
		
	}
	
	public SensitiveException(Component comp, String str) {
		super(comp.getComponentName() + " has " + str);
		
	}
	
}
