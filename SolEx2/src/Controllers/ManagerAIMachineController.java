package Controllers;

import java.util.TreeSet;

import Exceptions.InvalidInputException;
import Model.Delivery;
import Model.DeliveryPerson;
import Model.ExpressDelivery;
import Model.Order;
import Model.RegularDelivery;
import Model.Restaurant;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ManagerAIMachineController extends ControllerWrapper {
	@FXML
	private ComboBox<DeliveryPerson> dpBox;
	@FXML
	private TextField areaField;
	@FXML
	private ListView<Order> ordersList;
	@FXML
	private TableView<Delivery> allDeliveriesTable;
	@FXML
	private TableColumn<Delivery, Integer> deliveryIdCol;
	@FXML
	private TableColumn<Delivery, String> deliveryTypeCol;
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
	private TableColumn<Delivery, String> postageCol;
	
	@FXML
	public void initialize() {
		init();
	}

	private void init() {
		ObservableList<DeliveryPerson> deliveryPersons = FXCollections.observableArrayList(Restaurant.getInstance().getDeliveryPersons().values());
		dpBox.getItems().clear();				
		dpBox.setItems(FXCollections.observableArrayList(deliveryPersons));
		
		dpBox.setOnAction(dp -> {
			areaField.setText(dpBox.getValue().getArea().toString());
		});
		
		ObservableList<Order> orders = FXCollections.observableArrayList(Restaurant.getInstance().getOrders().values());
		ordersList.setItems(orders);
		ordersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
	}
	
	public void getResults(ActionEvent e) {
		allDeliveriesTable.setVisible(true);
		allDeliveriesTable.getItems().clear();
		try {
			DeliveryPerson dp = dpBox.getValue();
			if(dp == null) {
				throw new InvalidInputException("Please choose delivery Person");
			}
			
			
			TreeSet<Order> deliveryOrders = new TreeSet<Order>();
			deliveryOrders.addAll(ordersList.getSelectionModel().getSelectedItems());	
			if(deliveryOrders.isEmpty())
				throw new InvalidInputException("Please choose at least one order");
			
			TreeSet<Delivery> results = Restaurant.getInstance().createAIMacine(dp, dp.getArea(), deliveryOrders);
			
			deliveryIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
			
			deliveryTypeCol.setCellValueFactory(delivery -> new ReadOnlyObjectWrapper<String>(delivery.getValue().getClass().getSimpleName()));
			
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
			
			postageCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Delivery, String>, ObservableValue<String>>() {
			    @Override
			    public ObservableValue<String> call(TableColumn.CellDataFeatures<Delivery, String> p) {
			        if (p.getValue() instanceof ExpressDelivery) {
			            return new SimpleStringProperty(String.valueOf(((ExpressDelivery) p.getValue()).getPostage()));
			        } else {
			        	 return null;
			        }
			    }
			});
					
			allDeliveriesTable.getItems().addAll(results);
			
		} catch(InvalidInputException ex) {
			
		}
	}

}
