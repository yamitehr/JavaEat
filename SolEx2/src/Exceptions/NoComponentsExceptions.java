package Exceptions;

import Model.Dish;

public class NoComponentsExceptions extends Exception{

	public NoComponentsExceptions(Dish dish) {
		super("The "+ dish + " must have \nat least one component");
		
	}
	
}
