package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import Model.Component;
import Model.Dish;
import Model.Restaurant;
import Model.State;
import Utils.DishType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
	
	private CustomerLandingPageController landingController;
	
	@FXML
    public void initialize() {
		initMenu();
    }
	
	public void setLandingController(CustomerLandingPageController c) {
		landingController = c;
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
				menuItems.add(getMenuItem(dish));
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
				menuItems.add(getMenuItem(dish));
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
				menuItems.add(getMenuItem(dish));
			}
		}
		
		//TODO: show message is menu is empty
		
		return menuItems;
	}
	
	private Pane getMenuItem(Dish dish) {
		String dishName = dish.getDishName();
		double dishPrice = dish.calcDishPrice();
		String dishDescription = dish.getComponenets().toString();
		
		Pane newMenuItem = new Pane();
		Label dishLa = new Label("Dish: " + dishName + "\nPrice: " + String.valueOf(dishPrice) + "\nContains: " + dishDescription);
		Button addBtn = new Button("+Add");

		//Copy dish and components to a new object, so we can remove components from the dish without affecting
		// the instance of the restaurant
		ArrayList<Component> newComps = new ArrayList<Component>();
		for(Component c : dish.getComponenets()) {
			newComps.add(new Component(c.getComponentName(), c.isHasLactose(), c.isHasGluten(), c.getPrice()));
		}
		addBtn.setOnAction((ActionEvent evt)->{
			Dish newDish = new Dish(dish.getId());
			newDish.setDishName(dish.getDishName());
			newDish.setType(dish.getType());
			newComps.forEach(c -> newDish.addComponent(c));
			newDish.setTimeToMake(dish.getTimeToMake());
			
			CurrentDishModel newCurrentDishModel = new CurrentDishModel(newDish, true);
			
			State.setCurrentDish(newCurrentDishModel);
			landingController.toggleEditDish();
        });
		
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
