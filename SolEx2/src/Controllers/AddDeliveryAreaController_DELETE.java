package Controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import Exceptions.InvalidPersonInputException;
import Model.DeliveryArea;
import Model.Restaurant;
import Utils.Neighberhood;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

public class AddDeliveryAreaController_DELETE extends ControllerWrapper{
	@FXML
	private Pane neighberhood_pane;
	@FXML
	private TextField deliveryAreaName;
	@FXML
	private TextField deliveryTime;
	
	private ArrayList<Pair<CheckBox, Neighberhood>> neighberhoodList;
	@FXML
	private Text messageToUser;
	@FXML
	private Button addDeliveryAreaBtn;
	@FXML
	private Button removeDeliveryAreaBtn;
	@FXML
	private ListView<DeliveryArea> allDeliveryAreas;
	@FXML
	private Text areaNameField;
	@FXML
	private Text deliveryTimeField;
	@FXML
	private Text neighborhoodField;
	
	
	@FXML
    public void initialize() {
		generateNeighborhoodGrid();
		addDeliveryTimeEventListener();
    }
	
	private void generateNeighborhoodGrid() {
		neighberhoodList = new ArrayList<Pair<CheckBox, Neighberhood>>();
		GridPane grid = new GridPane();
		grid.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		grid.setHgap(12);
		grid.setVgap(12);
		
		
		for (Neighberhood n : Neighberhood.values()) {
			CheckBox cb = new CheckBox(n.name());
			neighberhoodList.add(new Pair<CheckBox, Neighberhood>(cb, n));
		}
		
		int i = 0;
		int j = 0;
		for (Pair<CheckBox, Neighberhood> cb : neighberhoodList) {
			grid.add(cb.getKey(), j, i, 1, 1);
			if (j == 3) {
				j = 0;
				i ++;
			} else {
				j ++;
			}
		}
		
		neighberhood_pane.getChildren().add(grid);
	}
	
	public void addDeliveryTimeEventListener() {
		deliveryTime.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	if (newValue != "") {
			    	try {
						Integer.parseInt(newValue);
					} catch(NumberFormatException nfe) {
						deliveryTime.setText(oldValue);
					}	
		    	}
		    }
		});
		
		////////All delivery people list view
		//Set the listview cell factory to show the right delivery person name
		allDeliveryAreas.setCellFactory(param -> new ListCell<DeliveryArea>() {
		    @Override
		    protected void updateItem(DeliveryArea item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null) {
		            setText(null);
		        } else {
		            setText(item.getAreaName());
		        }
		    }
		});
				
		//Add all delivery people
		allDeliveryAreas.getItems().addAll(FXCollections.observableArrayList(
				Restaurant.getInstance().getAreas().entrySet().stream().map(c -> c.getValue()).collect(Collectors.toList())));
		
		//Event listener for listview
		allDeliveryAreas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DeliveryArea>() {
		    @Override
		    public void changed(ObservableValue<? extends DeliveryArea> observable, DeliveryArea oldValue, DeliveryArea newValue) {
		    	updateDeliveryAreaDetailsFields();
		    }
		});
	}
	
	public void updateDeliveryAreaDetailsFields() {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
		// fill text fields with values about the selected delivery area on the list
		if(selectedDeliveryArea != null) {
			areaNameField.setText(selectedDeliveryArea.getAreaName());
			deliveryTimeField.setText(String.valueOf(selectedDeliveryArea.getDeliverTime()));
			String neighborhoodsStr = "";
			if (selectedDeliveryArea.getNeighberhoods().size() > 0) {
				for(Neighberhood n : selectedDeliveryArea.getNeighberhoods()) {
					neighborhoodsStr += n.name() + ", ";
				}
				neighborhoodsStr = neighborhoodsStr.substring(0, neighborhoodsStr.length() - 3);
			}
			neighborhoodField.setText(neighborhoodsStr);
			
		//clean the text fields if there is no selection
		} else if(selectedDeliveryArea == null) {
			areaNameField.setText("");
			deliveryTimeField.setText("");
			neighborhoodField.setText("");
		}
	}
	
	public void addDeliveryArea(ActionEvent e) {
		try {
			
			String daName = deliveryAreaName.getText();
			if(daName.isEmpty()) {
				throw new InvalidPersonInputException("Please fill Area Name");
			}
			
			HashSet<Neighberhood> selectedNeighberhoods = new HashSet<Neighberhood>();
			for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
				CheckBox cb = cbp.getKey();
				if (cb.isSelected()) {
					selectedNeighberhoods.add(cbp.getValue());
				}
			}
			
			if(selectedNeighberhoods.isEmpty()) {
				throw new InvalidPersonInputException("Please select Neighberhoods");
			}
			
			int daTime = 0;
			try {
				daTime = Integer.parseInt(deliveryTime.getText()); 
			} catch(Exception exc) {
				throw new InvalidPersonInputException("Please fill Devliery Time");
			}
			
			DeliveryArea da = new DeliveryArea(daName, selectedNeighberhoods, daTime);
			//Add delivery area to the restaurant and clear fields
			if(Restaurant.getInstance().addDeliveryArea(da)) {
				messageToUser.setFill(Color.BLUE);
				messageToUser.setText("Cook added successfully");
				deliveryAreaName.clear();
				deliveryTime.clear();
				for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
					CheckBox cb = cbp.getKey();
					cb.setSelected(false);
				}
			} else {
				messageToUser.setFill(Color.RED);
				messageToUser.setText("an error has accured please try again");
			}
		} catch(InvalidPersonInputException ipe) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText(ipe.getMessage());
		} catch(Exception exc) {
			messageToUser.setFill(Color.RED);
			messageToUser.setText("an error has accured please try again");
		}
		
	}
		
	public void removeDeliveryArea(ActionEvent e) {
		DeliveryArea selectedDeliveryArea = allDeliveryAreas.getSelectionModel().getSelectedItem();
		if(selectedDeliveryArea !=  null) {
		
		}
	}
	
	
}
