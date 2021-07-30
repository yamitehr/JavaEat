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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
			starterLabel.setTextFill(Color.RED);
			starterPane.setMinSize(10, 10);
			
			starterPane.getChildren().add(starterLabel);
			menuItems.add(starterPane);
			for(Entry<Integer, Dish> d : starters) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish.getDishName(), dish.calcDishPrice(), dish.getComponenets().toString()));
			}
		}
		if (main.size() > 0) {
			Pane mainPane = new Pane();
			Label mainLabel = new Label("Main");
			mainLabel.setTextFill(Color.RED);
			mainPane.setMinSize(10, 10);
			
			mainPane.getChildren().add(mainLabel);
			menuItems.add(mainPane);
			for(Entry<Integer, Dish> d : main) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish.getDishName(), dish.calcDishPrice(), dish.getComponenets().toString()));
			}
		}
		if (deserts.size() > 0) {
			Pane desertsPane = new Pane();
			Label desertsLabel = new Label("Desserts");
			desertsLabel.setTextFill(Color.RED);
			desertsPane.setMinSize(10, 10);
			
			desertsPane.getChildren().add(desertsLabel);
			menuItems.add(desertsPane);
			for(Entry<Integer, Dish> d : starters) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish.getDishName(), dish.calcDishPrice(), dish.getComponenets().toString()));
			}
		}
		return menuItems;
	}
	
	private Pane getMenuItem(String dishName, double dishPrice, String dishDescription) {
		Pane newMenuItem = new Pane();
		Label dishLa = new Label("Dish: " + dishName + "\nPrice: " + String.valueOf(dishPrice) + "\nContains: " + dishDescription);
		Button addBtn = new Button("+Add");
		
		dishLa.getStyleClass().add("descLabel");
		newMenuItem.getStyleClass().add("menuItem");
		
		newMenuItem.setMinSize(100, 100);
		newMenuItem.setMaxSize(200, 200);
		
		newMenuItem.getChildren().addAll(dishLa);
		newMenuItem.getChildren().addAll(addBtn);
		
		addBtn.relocate(142, 65);
		return newMenuItem;
	}
}
