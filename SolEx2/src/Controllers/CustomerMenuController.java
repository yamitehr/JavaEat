package Controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
			starterLabel.setLayoutX(110);
			starterLabel.getStyleClass().add("headersLabel");
			starterPane.setMinSize(55, 55);
			
			starterPane.getChildren().add(starterLabel);
			menuItems.add(starterPane);
			for(Entry<Integer, Dish> d : starters) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish));
			}
		}
		if (main.size() > 0) {
			Pane mainPane = new Pane();
			Label mainLabel = new Label("Main Courses");
			mainLabel.setLayoutX(90);
			mainLabel.getStyleClass().add("headersLabel");
			mainPane.setMinSize(55, 55);
			
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
			desertsLabel.setLayoutX(110);
			desertsLabel.getStyleClass().add("headersLabel");
			desertsPane.setMinSize(55, 55);
			
			desertsPane.getChildren().add(desertsLabel);
			menuItems.add(desertsPane);
			for(Entry<Integer, Dish> d : deserts) {
				Dish dish = d.getValue();
				menuItems.add(getMenuItem(dish));
			}
		}
		
		//TODO: show message is menu is empty
		
		return menuItems;
	}
	
	private Pane getMenuItem(Dish dish) {
		String dishName = dish.getDishName();
		double dishPriceDoub = dish.calcDishPrice();
		String dishPrice = String.format("%.1f", dishPriceDoub);
		String dishDescription = "";
		for(Component c : dish.getComponenets()) {
			dishDescription += c.getComponentName() + ", ";
		}
		dishDescription = dishDescription.substring(0, dishDescription.length() -2);
		
		Pane newMenuItem = new Pane();
		Label dishLa = new Label(dishName);
		Label priceLa = new Label(String.valueOf(dishPrice) + "$");
		Label compsLa = new Label("Contains: " + dishDescription);
		compsLa.setWrapText(true);
		compsLa.setMaxWidth(250);
		Button addBtn = new Button();
		
		InputStream is = null;
		try {
			is = new FileInputStream("icons/plus-circle-solid.png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image addIcon = new Image(is);
		ImageView addIconView = new ImageView(addIcon);
		addBtn.setGraphic(addIconView);
      
		//Copy dish and components to a new object, so we can remove components from the dish without affecting
		// the instance of the restaurant
		
		addBtn.setOnAction((ActionEvent evt)->{
			ArrayList<Component> newComps = new ArrayList<Component>();
			for(Component c : dish.getComponenets()) {
				newComps.add(new Component(c.getComponentName(), c.isHasLactose(), c.isHasGluten(), c.getPrice()));
			}
			Dish newDish = new Dish(dish.getId());
			newDish.setDishName(dish.getDishName());
			newDish.setType(dish.getType());
			newComps.forEach(c -> newDish.addComponent(c));
			newDish.setTimeToMake(dish.getTimeToMake());
			
			CurrentDishModel newCurrentDishModel = new CurrentDishModel(newDish, true);
			
			State.setCurrentDish(newCurrentDishModel);
			landingController.toggleEditDish();
        });
		
		dishLa.getStyleClass().add("dishLabel");
		compsLa.getStyleClass().add("compsLabel");
		priceLa.getStyleClass().add("priceLabel");
		newMenuItem.getStyleClass().add("menuItem");
		addBtn.getStyleClass().add("addButton");
		
		newMenuItem.setMinSize(100, 120);
		newMenuItem.setMaxSize(200, 200);
		
		newMenuItem.getChildren().addAll(dishLa);
		newMenuItem.getChildren().addAll(priceLa);
		newMenuItem.getChildren().addAll(compsLa);
		newMenuItem.getChildren().addAll(addBtn);
		
		
		compsLa.relocate(dishLa.getLayoutX(), dishLa.getLayoutY() +20);
		priceLa.relocate(dishLa.getLayoutX()+200, dishLa.getLayoutY());
		addBtn.relocate(priceLa.getLayoutX() + 7, priceLa.getLayoutY()+50);
		
		return newMenuItem;
	}
}
