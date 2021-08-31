package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.Cook;
import Model.Customer;
import Model.Delivery;
import Model.DeliveryArea;
import Model.DeliveryPerson;
import Model.Dish;
import Model.ExpressDelivery;
import Model.RegularDelivery;
import Model.Restaurant;
import Model.State;
import Utils.Expertise;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ManagerStatisticsController extends ControllerWrapper {
	@FXML
	private ComboBox<DeliveryPerson> deliveryPersonBox;

	@FXML
	private ComboBox<Integer> monthBox;
	
	
	@FXML
	private Label regularDeliveries;

	@FXML
	private Label expressDeliveries;
	
	@FXML
	private ComboBox<Expertise> expertiseBox;
	
	@FXML
	private ListView<Cook> cooksByExpertise;
	
	@FXML
	private ComboBox<Customer> customerBox;
	
	@FXML
	private ListView<Dish> relevantDishList;
	
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
		private TableColumn<Dish, String> priceCol;
		
		//getCookByExpertise
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
		
		//deliveriesByPerson
		@FXML
		private TableView<Delivery> deliveriesByPersonTable;
		@FXML
		private TableColumn<Delivery, Integer> deliveryIdCol;
		@FXML
		private TableColumn<Delivery, String> deliveryPersonCol;
		@FXML
		private TableColumn<Delivery, String> areaCol;
		@FXML
		private TableColumn<Delivery, String> deliveryDateCol;
		@FXML
		private TableColumn<Delivery, String> isDeliveredCol;
		@FXML
		private TableColumn<Delivery, String> ordersCol;
	
	@FXML
    public void initialize() {
		init();
		initRelevantDishes();
		initCooksByExpertise();
	}

	private void initCooksByExpertise() {
		expertiseBox.setOnAction(e -> {
			cookByExpertiseTable.setVisible(true);
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

	private void initRelevantDishes() {
		customerBox.setOnAction(c -> {
			relevantDishesTable.setVisible(true);
			relevantDishesTable.getItems().clear();
			dishIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			
			dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
			
			componentsCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(
					dish.getValue().getComponenets()
					.stream()
					.map(d -> d.toString())
					.reduce((a, b) -> a + ", " + b).get()
					));
			priceCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(String.format("%.2f", dish.getValue().getPrice())));
			
			
			List<Dish> relevantDishes = new ArrayList<Dish>();
			relevantDishes = Restaurant.getInstance().getReleventDishList(customerBox.getValue()).stream()
					.collect(Collectors.toList());
			
			relevantDishesTable.getItems().addAll(relevantDishes);
		});
		
	}

	private void init() {
		ObservableList<DeliveryPerson> deliveryPersons = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveryPersons().values());
		deliveryPersonBox.getItems().clear();				
		deliveryPersonBox.setItems(FXCollections.observableArrayList(deliveryPersons));
		
		ObservableList<Integer> monthsNumber = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);
		monthBox.getItems().clear();				
		monthBox.setItems(FXCollections.observableArrayList(monthsNumber));
		
		regularDeliveries.setText(Restaurant.getInstance().getNumberOfDeliveriesPerType().get("Regular delivery").toString());
		expressDeliveries.setText(Restaurant.getInstance().getNumberOfDeliveriesPerType().get("Express delivery").toString());
		
		ObservableList<Expertise> expertises = FXCollections.observableArrayList(Expertise.values());
		expertiseBox.getItems().clear();				
		expertiseBox.setItems(FXCollections.observableArrayList(expertises));
		
		ObservableList<Customer> customers = FXCollections.observableArrayList(Restaurant.getInstance().getCustomers().values());
		customerBox.getItems().clear();				
		customerBox.setItems(FXCollections.observableArrayList(customers));
		
		
		//relevant dish list
		
		
	}
	
	public void getDeliveries(ActionEvent e) {
		DeliveryPerson selectedDP = deliveryPersonBox.getValue();
		Integer selectedMonth = monthBox.getValue();
		if(selectedDP != null && selectedMonth != null) {
			List<Delivery> deliveriesByPerson = new ArrayList<Delivery>();
			deliveriesByPerson = Restaurant.getInstance().getDeliveriesByPerson(selectedDP, selectedMonth).stream()
					.collect(Collectors.toList());
			
			deliveriesByPersonTable.setVisible(true);
			deliveriesByPersonTable.getItems().clear();
			
			deliveryIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			
			deliveryPersonCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(delivery.getValue().getDeliveryPerson().toString()));
			
			areaCol.setCellValueFactory(area -> new ReadOnlyObjectWrapper<String>(area.getValue().getArea().toString()));
			
			deliveryDateCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(delivery.getValue().getDeliveredDate().toString()));
			
			isDeliveredCol.setCellValueFactory(delivery -> {
	            boolean isDelivered = delivery.getValue().isDelivered();
	            String isDeliveredAsString;
	            if(isDelivered == true)
	            {
	            	isDeliveredAsString = "Yes";
	            }
	            else
	            {
	            	isDeliveredAsString = "No";
	            }

	         return new ReadOnlyStringWrapper(isDeliveredAsString);
	        });
			ordersCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Delivery, String>, ObservableValue<String>>() {
			    @Override
			    public ObservableValue<String> call(TableColumn.CellDataFeatures<Delivery, String> p) {
			        if (p.getValue() instanceof ExpressDelivery) {
			            return new SimpleStringProperty(((ExpressDelivery) p.getValue()).getOrder().toString());
			        } else {
			        	 return new SimpleStringProperty(((RegularDelivery) p.getValue()).getOrders()
			        			 .stream()
									.map(d -> d.toString())
									.reduce((a, b) -> a + ", " + b).get());
			        }
			    }
			});
					
			deliveriesByPersonTable.getItems().addAll(deliveriesByPerson);
		}
	}
	
//	public void getCooks(ActionEvent e) {
//		Expertise expert = expertiseBox.getValue();
//		if(expert != null) {
//			cookByExpertiseTable.setVisible(true);
//			
//			cookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));	
//			cookNameCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getFirstName() + cook.getValue().getLastName()));	
//			cookDobCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getBirthDay().toString()));	
//			cookGenderCol.setCellValueFactory(cook -> new ReadOnlyObjectWrapper<String>(cook.getValue().getGender().name()));	
//			isChefCol.setCellValueFactory(cook -> {
//	            boolean isChef = cook.getValue().isChef();
//	            String isChefAsString;
//	            if(isChef == true)
//	            {
//	                isChefAsString = "Yes";
//	            }
//	            else
//	            {
//	                isChefAsString = "No";
//	            }
//
//	         return new ReadOnlyStringWrapper(isChefAsString);
//	        });
//			
//			List<Cook> relevantCooks = new ArrayList<Cook>();
//			relevantCooks = Restaurant.getInstance().GetCooksByExpertise(expert).stream()
//					.collect(Collectors.toList());
//			
//			cookByExpertiseTable.getItems().addAll(relevantCooks);
//		}
//	}
	
//	public void getDishes(ActionEvent e) {
//		Customer cust = customerBox.getValue();
//		if(cust != null) {
//			relevantDishesTable.setVisible(true);
//			dishIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//			
//			dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
//			
//			componentsCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(
//					dish.getValue().getComponenets()
//					.stream()
//					.map(d -> d.toString())
//					.reduce((a, b) -> a + ", " + b).get()
//					));
//			priceCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<Double>(dish.getValue().getPrice()));
//			
//			
//			List<Dish> relevantDishes = new ArrayList<Dish>();
//			relevantDishes = Restaurant.getInstance().getReleventDishList(cust).stream()
//					.collect(Collectors.toList());
//			
//			relevantDishesTable.getItems().addAll(relevantDishes);
//			
//		}
//	}

}
