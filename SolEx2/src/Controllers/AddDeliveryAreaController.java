package Controllers;

import java.util.ArrayList;
import java.util.HashSet;

import Model.DeliveryArea;
import Model.Restaurant;
import Utils.Neighberhood;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class AddDeliveryAreaController extends ControllerWrapper{
	@FXML
	private Pane neighberhood_pane;
	@FXML
	private TextField deliveryAreaName;
	@FXML
	private TextField deliveryTime;
	
	private ArrayList<Pair<CheckBox, Neighberhood>> neighberhoodList;
	
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
	}
	
	public void addDeliveryArea(ActionEvent e) {
		
		//Get selected neighborhoods
		HashSet<Neighberhood> selectedNeighberhoods = new HashSet<Neighberhood>();
		for (Pair<CheckBox, Neighberhood> cbp : neighberhoodList) {
			CheckBox cb = cbp.getKey();
			if (cb.isSelected()) {
				selectedNeighberhoods.add(cbp.getValue());
			}
			
		}
		
		//Create delivery area
		String daName = deliveryAreaName.getText();
		int daTime = Integer.parseInt(deliveryTime.getText()); 
		DeliveryArea da = new DeliveryArea(daName, selectedNeighberhoods, daTime);
		Restaurant.getInstance().addDeliveryArea(da);
	}
	
	public void moveToManagerDeliveryAreaScene(ActionEvent e) {
		
	}
	
	
}
