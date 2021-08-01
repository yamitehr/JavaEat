package Controllers;

import java.io.File;
import java.util.ArrayList;

import Model.Component;
import Model.Customer;
import Model.Dish;
import Model.Order;
import Model.State;
import Utils.Neighberhood;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class CustomerLandingPageController extends ControllerWrapper{
	Customer current = State.getCurrentCustomer();
	
	@FXML
	private Label messageLbl;
	
	@FXML
	private Button dashboardBtn;
	
	@FXML
	private Button ordersBtn;
	
	@FXML
	private Button shoppingCartBtn;
	
	@FXML
	private Button menuBtn;
	
	@FXML
	private Button addOrderBtn;
	
	@FXML
	private Button personalDetailsBtn;
	
	@FXML
	private AnchorPane toReplacePane;
	@FXML
	private MediaView restaurantVideo;
	@FXML
	private Button logOutBtn;
	
	private File file;
	private MediaPlayer mediaPlayer;
	private Media media;
	
	@FXML
	private Label dishName;

    @FXML
    private AnchorPane navList;
    private ArrayList<Pair<CheckBox, Component>> componentList;
    @FXML
    private Pane componentsContainer;
	
    @FXML
    private Button addDishToOrder;
    @FXML
	private GridPane componentGrid;
	
	@FXML
	public void initialize() {
		messageLbl.setText("Hello " + current.getFirstName());
		componentList = new ArrayList<Pair<CheckBox, Component>>();
		dashboardBtn.setAlignment(Pos.BASELINE_LEFT);
		ordersBtn.setAlignment(Pos.BASELINE_LEFT);
		shoppingCartBtn.setAlignment(Pos.BASELINE_LEFT);
		menuBtn.setAlignment(Pos.BASELINE_LEFT);
		addOrderBtn.setAlignment(Pos.BASELINE_LEFT);
		personalDetailsBtn.setAlignment(Pos.BASELINE_LEFT);
		
		file = new File("restaurantVideoHD.mp4");
		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		restaurantVideo.setMediaPlayer(mediaPlayer);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		initializeCompGrid();
        prepareSlideMenuAnimation();
        initializeAddDishButton();
		
	}

	public void moveToDashboardScene(ActionEvent e) {
		
	}
	public void moveToOrdersScene(ActionEvent e) {
		
	}
	public void moveToShoppingCartScene(ActionEvent e) {
		
	}
	public void moveToMenuScene(ActionEvent e) {
		messageLbl.setText("Menu");
		mediaPlayer.pause();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Customer_Menu.fxml"));
			AnchorPane pane = loader.load();
			CustomerMenuController controller = (CustomerMenuController)loader.getController();
			controller.setLandingController(this);
			toReplacePane.getChildren().removeAll(toReplacePane.getChildren());
			toReplacePane.getChildren().add(pane);
			
		}catch(Exception er) 
		{
			
		}
	}
	public void moveToAddOrderScene(ActionEvent e) {
		
	}
	public void moveToPersonalDetailsScene(ActionEvent e) {
		
	}
	
	public void MoveToLoginScene(ActionEvent e) {
		mediaPlayer.pause();
		moveToScene("/View/Login.fxml", (Stage)logOutBtn.getScene().getWindow());
	}
	
	private void prepareSlideMenuAnimation() {
        Screen screen = Screen.getScreens().get(0);
        double hiddenPlace = screen.getBounds().getMaxX();
        
        navList.setLayoutX(hiddenPlace);
        
        personalDetailsBtn.setOnAction((ActionEvent evt)->{
        	toggleEditDish();
        });
    }
	
	public void toggleEditDish() {
		setEditDishData(State.getCurrentDish());
		
        TranslateTransition openNav = new TranslateTransition(new Duration(350), navList);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), navList);
		if(navList.getTranslateX() != -(navList.getWidth())){
        	openNav.setToX(-(navList.getWidth()));
        	openNav.play();
        }else{
            closeNav.setToX(navList.getWidth());
            closeNav.play();
        }
	}
	
	private void setEditDishData(Dish dish) {
		clearNav();
		if (dish == null) {
			dishName.setText("No dish found");
		} else {
			dishName.setText(dish.getDishName());
			generateComponentsCheckboxes(dish);
		}
	}
	
	private void generateComponentsCheckboxes(Dish dish) {
		for (Component comp : dish.getComponenets()) {
			CheckBox cb = new CheckBox(comp.getComponentName());
			cb.setSelected(true);
			componentList.add(new Pair<CheckBox, Component>(cb, comp));
		}
		
		int i = 0;
		int j = 0;
		for (Pair<CheckBox, Component> cb : componentList) {
			componentGrid.add(cb.getKey(), j, i, 1, 1);
			if (j == 3) {
				j = 0;
				i ++;
			} else {
				j ++;
			}
		}
	}
	
	private void clearNav() {
		componentList.clear();
		componentGrid.getChildren().clear();
	}
	
	private void initializeCompGrid() {
		componentGrid.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		componentGrid.setHgap(12);
		componentGrid.setVgap(12);
	}
	
	private void initializeAddDishButton() {
		addDishToOrder.setOnAction((ActionEvent evt)->{
        	Dish dish = State.getCurrentDish();
        	
        	//Remove components based on selection
        	for(Pair<CheckBox, Component> compPair : componentList) {
        		if (!compPair.getKey().isSelected()) {
        			dish.removeComponent(compPair.getValue());
        		}
        	}
        	
        	Order order = State.getCurrentOrder();
        	
        	//If order exists, add dish. otherwise create a new order
        	if (order != null) {
        		order.addDish(dish);
        	} else {
            	ArrayList<Dish> dishes = new ArrayList<Dish>();
            	dishes.add(dish);
        		State.setCurrentOrder(new Order(State.getCurrentCustomer(), dishes, null));
        	}
        	
        	//close side menu
        	toggleEditDish();
        });
	}
}
