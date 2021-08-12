package Controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.DeliveryArea;
import Model.Order;
import Model.OrderStatus;
import Model.Restaurant;
import Model.State;
import Utils.DeliveryManager;
import Utils.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CustomerOrdersHistoryController {
	@FXML
	private TableView<Order> allOrdersTable;
	@FXML
	private TableColumn<Order, Integer> orderIdCol;
	@FXML
	private TableColumn<Order, String> dishesCol;
	@FXML
	private TableColumn<Order, Double> priceCol;
	@FXML
	private TableColumn<Order, String> deliveryPersonCol;
	@FXML
	private TableColumn<Order, String> deliveryStatusCol;
	@FXML
	private TableColumn<Order, Integer> etaCol;
	@FXML
	private Button refreshBtn;
	@FXML
	private Button removeBtn;
	@FXML
	private Text message;
	
	private final ObservableList<Order> orders = FXCollections.observableArrayList();
	
	@FXML
    public void initialize() {
		init();
		refreshBtn.setOnAction(e -> initData());
		allOrdersTable.setOnMouseClicked(e -> {
			message.setText("");
		});
		initData();
		allOrdersTable.setItems(orders);
    }

	
	private void init() {
		orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		
		priceCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<Double>(order.getValue().getDishes()
				.stream()
				.map(d -> d.calcDishPrice())
				.reduce((a,b) ->  a + b)
				.get()
				));
		
		dishesCol.setCellValueFactory(order -> new ReadOnlyObjectWrapper<String>(
				order.getValue().getDishes()
				.stream()
				.map(d -> d.toString())
				.reduce((a, b) -> a + ", " + b).get()
				));
		
		deliveryPersonCol.setCellValueFactory(orderProperty ->{
			if (orderProperty.getValue().getDelivery() != null) {
				return new ReadOnlyObjectWrapper<String>(orderProperty.getValue()
						.getDelivery().getDeliveryPerson().getFirstName() 
						+ " " + orderProperty.getValue()
						.getDelivery().getDeliveryPerson().getLastName());
			} else {
				return new ReadOnlyObjectWrapper<String>("N/A");
				}
		});
		
		deliveryStatusCol.setCellValueFactory(orderProperty -> {
            String isDeliveredAsString = "";

			if (orderProperty.getValue().getDelivery() != null) {
	            boolean isDelivered = orderProperty.getValue().getDelivery().isDelivered();
	            if(isDelivered == true)
	            {
	            	isDeliveredAsString = "Delivered";
	            	
	            } else
	            {	
		            isDeliveredAsString = "Delivery is on the way";
	            	
	            }
	
	            return new ReadOnlyStringWrapper(isDeliveredAsString);
	        
			} else {
				if(orderProperty.getValue().getStatus().equals(OrderStatus.InProgress)) {
	            	isDeliveredAsString = "Order In Progress";
            	} else if(orderProperty.getValue().getStatus().equals(OrderStatus.cancelled)) {
            		isDeliveredAsString = "Cancelled";
            	} else if(orderProperty.getValue().getStatus().equals(OrderStatus.readyForDelivery)) {
            		isDeliveredAsString = "Ready for delivery";
            	}
				
				return new ReadOnlyObjectWrapper<String>(isDeliveredAsString);
			}
			
		});
		
		
		deliveryStatusCol.setCellFactory(new Callback<TableColumn<Order, String>, TableCell<Order, String>>()
	        {
	            public TableCell<Order, String> call(TableColumn<Order, String> column)
	            {
	                TableCell<Order, String> cell = new TableCell<Order, String>()
	                {
	                    protected void updateItem(String value, boolean empty)
	                    {
	                        super.updateItem(value, empty);
	                        if (value != null) {
		                        if (!value.equals("Delivered") && !value.equals("Cancelled")) {
		        	                final FlashingLabel flashingLabel = new FlashingLabel();
			                        flashingLabel.setText(value);
			                        flashingLabel.setVisible(!empty);
			                        switch(value) {
		                        		case "Delivery is on the way":
		                	                flashingLabel.setStyle("-fx-background-color: #ffaaaa");
		                        			break;
		                        		case "Order In Progress":
		                	                flashingLabel.setStyle("-fx-text-fill: #FFC300");
		                        			break;
		                        		case "Ready for delivery":
		                	                flashingLabel.setStyle("-fx-text-fill: #FF5733");
		                        			break;
	                        			default:
	                        				break;
			                        }
			                        this.setGraphic(flashingLabel);
		                        } else {
		                        	Label regularLabel = new Label(value);
		                        	switch(value) {
		                        		case "Delivered":
		                        			regularLabel.setStyle("-fx-text-fill: green");
		                        			break;
		                        		case "Cancelled":
		                        			regularLabel.setStyle("-fx-text-fill: red");
		                        			break;
	                        			default:
	                        				break;
		                        			
		                        	}
		                        	 this.setGraphic(regularLabel);
		                        }	
	                        }
	                    }
	                };
	                
	                return cell;
	            }
	        }); 
		
		etaCol.setCellValueFactory(orderProperty ->
		{
			if (orderProperty.getValue().getDelivery() != null) {
				return new ReadOnlyObjectWrapper<Integer>(orderProperty.getValue()
						.orderWaitingTime(orderProperty.getValue().getDelivery().getArea()));
			} else {
				DeliveryArea da = DeliveryManager.getInstance().getDeliveryAreaForOrder(orderProperty.getValue());
				if (da != null) {
					return new ReadOnlyObjectWrapper<Integer>(orderProperty.getValue().orderWaitingTime(da));
				} else {
					return new ReadOnlyObjectWrapper<Integer>(0);					
				}
			}
		});
		
	}
	
	
	private void initData() {
		message.setText("");
		
		orders.setAll(Restaurant.getInstance().getOrders().values().stream()
				.filter(o -> o.getCustomer().equals(State.getCurrentCustomer()))
				.collect(Collectors.toList()));	
	}
	
	 public class FlashingLabel extends Label
	    {
	        private FadeTransition animation;

	        public FlashingLabel()
	        {
	            animation = new FadeTransition(Duration.millis(1000), this);
	            animation.setFromValue(1.0);
	            animation.setToValue(0);
	            animation.setCycleCount(Timeline.INDEFINITE);
	            animation.setAutoReverse(true);
	            animation.play();

	            visibleProperty().addListener(new ChangeListener<Boolean>()
	            {
	                public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue)
	                {
	                	Logger.Log("changed " + newValue);
	                    if (newValue)
	                    {
	                        animation.playFromStart();
	                    }
	                    else
	                    {
	                        animation.stop();
	                    }
	                }
	            });
	        }
	    }
	 
	 public void cancelOrder(ActionEvent e) {
			Order selectedOrder = allOrdersTable.getSelectionModel().getSelectedItem();
			if(selectedOrder !=  null) {
				if(selectedOrder.getStatus().equals(OrderStatus.InProgress)) {
					selectedOrder.setStatus(OrderStatus.cancelled);
					initData();
				} else {
					message.setText("can delete only in progress orders");
				}
			} else {
				message.setText("no selection was detected");
			}
		}
}
