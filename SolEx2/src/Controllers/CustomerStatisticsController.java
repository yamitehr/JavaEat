package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Exceptions.InvalidInputException;
import Model.Component;
import Model.Cook;
import Model.Dish;
import Model.Order;
import Model.Restaurant;
import Model.State;
import Utils.Expertise;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class CustomerStatisticsController {
	//getRelevantDish
	@FXML
	private TableView<Dish> relevantDishesTable;
	@FXML
	private TableColumn<Dish, Integer> dishIdCol;
	@FXML
	private TableColumn<Dish, String> dishNameCol;
	@FXML
	private TableColumn<Dish, String> componentsCol;
	@FXML
	private TableColumn<Dish, String> dishPriceCol;
	@FXML
	private TableColumn<Dish, String> dishTimeCol;
	@FXML
	private TableColumn<Dish, String> dishTypeCol;
	
	//getCookByExpertise
	@FXML
	private ComboBox<Expertise> expertiseBox;
	@FXML
	private TableView<Cook> cookByExpertiseTable;
	@FXML
	private TableColumn<Cook, Integer> cookIdCol;
	@FXML
	private TableColumn<Cook, String> cookNameCol;
	@FXML
	private TableColumn<Cook, String> cookDobCol;
	@FXML
	private TableColumn<Cook, String> cookGenderCol;
	@FXML
	private TableColumn<Cook, String> isChefCol;

	//popularComponents
	@FXML
	private TableView<Component> popularComponentsTable;
	@FXML
	private TableColumn<Component, Integer> componentIdCol;
	@FXML
	private TableColumn<Component, String> componentNameCol;
	@FXML
	private TableColumn<Component, String> sensitivitiesCol;
	@FXML
	private TableColumn<Component, Double> priceCol;
	@FXML
	private TableColumn<Component, Integer> popularityCol;

	private CustomerLandingPageController landingController;

	@FXML
	private TabPane tabPaneStats;
	@FXML
	private Button addDishBtn;
	
	@FXML
    public void initialize() {
		initRelevantDishes();
		
		//init expertise combo box
		ObservableList<Expertise> expertiseList = FXCollections.observableArrayList(Expertise.values());
		expertiseBox.getItems().clear();				
		expertiseBox.setItems(FXCollections.observableArrayList(expertiseList));
		
		initCookByExpertise();
		initPopularComps();
		
		initializeAddDishButton();
    }
	public void setLandingController(CustomerLandingPageController c) {
		landingController = c;
	}
	
	private void initRelevantDishes() {
		dishIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
		
		componentsCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(
				dish.getValue().getComponenets()
				.stream()
				.map(d -> d.toString())
				.reduce((a, b) -> a + ", " + b).get()
				));
		dishPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		dishTimeCol.setCellValueFactory(new PropertyValueFactory<>("timeToMake"));
		dishTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		List<Dish> relevantDishes = new ArrayList<Dish>();
		relevantDishes = Restaurant.getInstance().getReleventDishList(State.getCurrentCustomer()).stream()
				.collect(Collectors.toList());
		
		relevantDishesTable.getItems().addAll(relevantDishes);
	}
	
	private void initPopularComps() {
		
		componentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
				
		componentNameCol.setCellValueFactory(component -> new ReadOnlyObjectWrapper<String>(component.getValue().getComponentName()));
		
		sensitivitiesCol.setCellValueFactory(component -> {
            boolean isGluten = component.getValue().isHasGluten();
            boolean isLactose = component.getValue().isHasLactose();
            String isSensitiveAsString = "";
            if(isGluten == true)
            {
            	isSensitiveAsString += "Gluten ";
            }
            if(isLactose == true)
            {
            	isSensitiveAsString += "Lactose";
            }

         return new ReadOnlyStringWrapper(isSensitiveAsString);
        });
		
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

		popularityCol.setCellValueFactory(component -> new ReadOnlyObjectWrapper<Integer>(popularComponentsTable.getItems().indexOf(component.getValue())+1));
		
		popularComponentsTable.getItems().addAll(Restaurant.getInstance().getPopularComponents());
	}
	
	private void initCookByExpertise() {
		
		expertiseBox.setOnAction(e -> {
			cookByExpertiseTable.getItems().clear();
			cookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			
			cookNameCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getFirstName() + cook.getValue().getLastName()));
			
			cookDobCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getBirthDay().toString()));
			
			cookGenderCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getGender().name()));
			
			isChefCol.setCellValueFactory(cook -> {
	            boolean isChef = cook.getValue().isChef();
	            String isChefAsString;
	            if(isChef == true)
	            {
	                isChefAsString = "Yes";
	            }
	            else
	            {
	                isChefAsString = "No";
	            }

	         return new ReadOnlyStringWrapper(isChefAsString);
	        });
			
			List<Cook> relevantCooks = new ArrayList<Cook>();
			relevantCooks = Restaurant.getInstance().GetCooksByExpertise(expertiseBox.getValue()).stream()
					.collect(Collectors.toList());
			
			cookByExpertiseTable.getItems().addAll(relevantCooks);
		});
	}
	
	private void initializeAddDishButton(){
		addDishBtn.setOnAction(e -> {
		Dish dish = relevantDishesTable.getSelectionModel().getSelectedItem();
		
		if(dish != null) {
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
			
			Order order = State.getCurrentOrder();
	    	
	    	if(State.getCurrentDish().isNew()) {
	    		//If order exists, add dish. otherwise create a new order
	        	if (order != null) {
	        		order.addDish(newDish);
	        	} else {
	            	ArrayList<Dish> dishes = new ArrayList<Dish>();
	            	dishes.add(newDish);
	        		State.setCurrentOrder(new Order(State.getCurrentCustomer(), dishes, null));
	        	}
	    	}
		}
		
    	landingController.initShoppingCart();
		});
	}
}
