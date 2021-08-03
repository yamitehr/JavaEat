package Controllers;

import Model.Dish;

public class CurrentDishModel {
	private Dish dish;
	private boolean isNew;
	
	
	public CurrentDishModel(Dish dish, boolean isNew) {
		super();
		this.dish = dish;
		this.isNew = isNew;
	}
	public Dish getDish() {
		return dish;
	}
	public void setDish(Dish _currentDish) {
		this.dish = _currentDish;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	
	
	
}
