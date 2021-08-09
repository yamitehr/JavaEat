package Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.Component;
import Model.Delivery;
import Model.Dish;
import Model.ExpressDelivery;
import Model.Order;
import Model.RegularDelivery;
import Model.Restaurant;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManagerDashboardController extends ControllerWrapper {
	
	@FXML
	private Label totalCustomers;
	@FXML
	private Label totalEmployees;
	@FXML
	private Label totalSell;
	@FXML
	private Label totalRevenue;
	@FXML
	private Label revenueFromExpress;
	@FXML
	private Label profitRelation;
	@FXML
	private Button watchPopularBtn;
	@FXML
	private TableView<Component> popularComponentsTable;
	@FXML
	private TableColumn<Component, String> compNameCol;
	@FXML 
	private TableColumn<Component, Double> compPriceCol;
	@FXML
	private TableColumn<Component, String> containsCol;
	@FXML
	private TableView<Dish> profitRelationTable;
	@FXML
	private TableColumn<Dish, String> dishNameCol;
	@FXML
	private TableColumn<Dish, Integer> timeToMakeCol;
	@FXML
	private TableColumn<Dish, Double> dishPriceCol;
	@FXML
	private DatePicker selectedDate;
	@FXML
	private Label sellByDate;
	@FXML
	private Label revenueByDate;
	
	@FXML
    public void initialize() {
		init();
    }
	
	private void init() {
		totalCustomers.setText(String.valueOf(Restaurant.getInstance().getCustomers().size()));
		totalEmployees.setText(String.valueOf(Restaurant.getInstance().getCooks().size() + 
				Restaurant.getInstance().getDeliveryPersons().size()));
		double totalPrice = 0.0;
		for(Order o: Restaurant.getInstance().getOrders().values()) {
			for(Dish d: o.getDishes()) {
				totalPrice += d.getPrice();
			}
		}
		totalSell.setText(String.valueOf(totalPrice));
		
		double revenue = 0;
		for(Order o: Restaurant.getInstance().getOrders().values()) {
			revenue += o.calcOrderRevenue();
		}
		totalRevenue.setText(String.valueOf(revenue));
		
		revenueFromExpress.setText(String.valueOf(Restaurant.getInstance().revenueFromExpressDeliveries()));
		
		
		//popular Components
		compNameCol.setCellValueFactory(comp -> new ReadOnlyObjectWrapper<String>(comp.getValue().getComponentName()));
				
		containsCol.setCellValueFactory(component -> {
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
		
		compPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
				
				
		List<Component> populaComponents = new ArrayList<Component>();
		populaComponents = Restaurant.getInstance().getPopularComponents().stream()
				.collect(Collectors.toList());
				
		popularComponentsTable.getItems().addAll(populaComponents);
		
		//profit relation
		dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
				
		timeToMakeCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<Integer>(dish.getValue().getTimeToMake()));
		
		dishPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
			
		List<Dish> profitRelation = new ArrayList<Dish>();
		profitRelation = Restaurant.getInstance().getProfitRelation().stream()
				.collect(Collectors.toList());
				
		profitRelationTable.getItems().addAll(profitRelation);
		selectedDate.setOnAction(d -> {
			double SellDate = 0;
			double revenueDate = 0;
			for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
				if(delivery.getDeliveredDate().equals(selectedDate.getValue())) {
					if(delivery instanceof RegularDelivery) {
						for(Order o: ((RegularDelivery) delivery).getOrders()) {
							revenueDate += o.calcOrderRevenue();
							for(Dish dish: o.getDishes()) {
								SellDate += dish.getPrice();
							}
						}
					}
					else { //express
						revenueDate += ((ExpressDelivery) delivery).getOrder().calcOrderRevenue();
						for(Dish dish: ((ExpressDelivery) delivery).getOrder().getDishes()) {
							SellDate += dish.getPrice();
						}
					}
				}
			}
			sellByDate.setText(String.valueOf(SellDate));
			revenueByDate.setText(String.valueOf(revenueDate));
		});
	}

}
