package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import Model.Dish;
import Model.Restaurant;
import Utils.DishType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CustomerMenuController extends ControllerWrapper{
	
	@FXML
	private AnchorPane mainPane;
	
	@FXML
	private VBox menuItemsVbox;
	
	@FXML
    public void initialize() {
		initMenu();
    }
	
	private void initMenu() {
		menuItemsVbox.setSpacing(10);
		menuItemsVbox.setPadding(new Insets(0, 20, 10, 20)); 
		menuItemsVbox.getChildren().addAll(getAllMenuItems());
	}
	
	private ArrayList<Pane> getAllMenuItems(){
		ArrayList<Pane> menuItems = new ArrayList<Pane>();
		List<Entry<Integer, Dish>> starters = Restaurant.getInstance().getDishes()
				.entrySet().stream()
					.filter(d -> d.getValue().getType()== DishType.Starter).collect(Collectors.toList());
		List<Entry<Integer, Dish>> main = Restaurant.getInstance().getDishes()
				.entrySet().stream()
					.filter(d -> d.getValue().getType()== DishType.Main).collect(Collectors.toList());
		List<Entry<Integer, Dish>> deserts = Restaurant.getInstance().getDishes()
										.entrySet().stream()
											.filter(d -> d.getValue().getType()== DishType.Dessert).collect(Collectors.toList());

		if (starters.size() > 0) {
			Pane starterPane = new Pane();
			Label starterLabel = new Label("Starters");
			starterPane.setMinSize(10, 10);
			
			starterPane.getChildren().add(starterLabel);
			menuItems.add(starterPane);
			for(Entry<Integer, Dish> d : starters) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish.getDishName(), dish.calcDishPrice(), "cool dish"));
			}
		}
		if (main.size() > 0) {
			Pane mainPane = new Pane();
			Label mainLabel = new Label("Main");
			mainPane.setMinSize(10, 10);
			
			mainPane.getChildren().add(mainLabel);
			menuItems.add(mainPane);
			for(Entry<Integer, Dish> d : main) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish.getDishName(), dish.calcDishPrice(), "cool dish"));
			}
		}
		if (deserts.size() > 0) {
			Pane desertsPane = new Pane();
			Label desertsLabel = new Label("Desserts");
			desertsPane.setMinSize(10, 10);
			
			desertsPane.getChildren().add(desertsLabel);
			menuItems.add(desertsPane);
			for(Entry<Integer, Dish> d : starters) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish.getDishName(), dish.calcDishPrice(), "cool dish"));
			}
		}
		return menuItems;
	}
	
	private Pane getMenuItem(String dishName, double dishPrice, String dishDescription) {
		Pane newMenuItem = new Pane();
		Label dishLa = new Label("Dish: " + dishName + "\nPrice: " + String.valueOf(dishPrice));
		
		dishLa.getStyleClass().add("descLabel");
		newMenuItem.getStyleClass().add("menuItem");
		
		newMenuItem.setMinSize(100, 100);
		newMenuItem.setMaxSize(200, 200);
		
		newMenuItem.getChildren().addAll(dishLa);
		return newMenuItem;
	}
}
