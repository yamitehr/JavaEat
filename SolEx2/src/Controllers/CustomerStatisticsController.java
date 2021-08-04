package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Exceptions.InvalidPersonInputException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class CustomerStatisticsController {
	@FXML
	private Text messageToUser;
	//getRelevantDish
	@FXML
	private TableView relevantDishesTable;
	@FXML
	private TableColumn<Dish, Integer> dishIdCol;
	@FXML
	private TableColumn<Dish, String> dishNameCol;
	@FXML
	private TableColumn<Dish, String> componentsCol;
	
	//getCookByExpertise
	@FXML
	private ComboBox<Expertise> expertiseBox;
	@FXML
	private TableView cookByExpertiseTable;
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
	//@FXML
	//private Button showCookByExpertiseBtn;
	@FXML
	private TableView popularComponentsTable;
	@FXML
	private TableColumn<Component, Integer> componentIdCol;
	@FXML
	private TableColumn<Component, String> componentNameCol;
	@FXML
	private TableColumn<Component, String> sensitivitiesCol;
	@FXML
	private TableColumn<Component, Double> priceCol;
	
	@FXML
    public void initialize() {
		initRelevantDishes();
		
		//init expertise combo box
		ObservableList<Expertise> expertiseList = FXCollections.observableArrayList(Expertise.values());
		expertiseBox.getItems().clear();				
		expertiseBox.setItems(FXCollections.observableArrayList(expertiseList));
		
		initCookByExpertise();
		initPopularComps();
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
		
		
		List<Dish> relevantDishes = new ArrayList<Dish>();
		relevantDishes = Restaurant.getInstance().getReleventDishList(State.getCurrentCustomer()).stream()
				.collect(Collectors.toList());
		
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
}
