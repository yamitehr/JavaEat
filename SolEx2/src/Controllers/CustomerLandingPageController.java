package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.Component;
import Model.Customer;
import Model.Delivery;
import Model.Dish;
import Model.ExpressDelivery;
import Model.Order;
import Model.Restaurant;
import Model.State;
import Utils.Neighberhood;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
	private Button menuBtn;
	
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
    private VBox cartVbox;
    @FXML
    private ScrollPane cartScrollPane;
    
    @FXML 
    private Text priceText;
    
    @FXML
    private Button confirmOrderBtn;
	
	@FXML
	public void initialize() {
		messageLbl.setText("Hello " + current.getFirstName());
		componentList = new ArrayList<Pair<CheckBox, Component>>();
		/*dashboardBtn.setAlignment(Pos.BASELINE_LEFT);
		ordersBtn.setAlignment(Pos.BASELINE_LEFT);
		menuBtn.setAlignment(Pos.BASELINE_LEFT);
		personalDetailsBtn.setAlignment(Pos.BASELINE_LEFT);*/
		
		
		file = new File("restaurantVideoHD.mp4");
		media = new Media(file.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		restaurantVideo.setMediaPlayer(mediaPlayer);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		
		
		cartScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		cartScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		cartVbox.setFillWidth(true);
		initializeCompGrid();
        prepareSlideMenuAnimation();
        initShoppingCart();
        initializeConfirmButton();
		
	}

	public void moveToDashboardScene(ActionEvent e) {
		messageLbl.setText("Dashboard");
		mediaPlayer.pause();
		try {
			replacePane(toReplacePane, "/View/Customer_Statistics.fxml");
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
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
			er.printStackTrace();
		}
	}
	public void moveToOrdersHistoryScene(ActionEvent e) {
		messageLbl.setText("Orders History");
		mediaPlayer.pause();
		try {
			replacePane(toReplacePane, "/View/Customer_OrdersHistory.fxml");
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	public void moveToPersonalDetailsScene(ActionEvent e) {
		mediaPlayer.pause();
		try {
			replacePane(toReplacePane, "/View/Customer_UpdatePersonalDetails.fxml");
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	
	public void MoveToLoginScene(ActionEvent e) {
		mediaPlayer.pause();
		//clean current customer and order
		State.cleanState();
		moveToScene("/View/Login.fxml", (Stage)logOutBtn.getScene().getWindow());
	}
	
	private void prepareSlideMenuAnimation() {
        Screen screen = Screen.getScreens().get(0);
        double hiddenPlace = screen.getBounds().getMaxX();
        
        navList.setLayoutX(hiddenPlace);
    }
	
	public void toggleEditDish() {

        initializeAddDishButton();
		setEditDishData(State.getCurrentDish().getDish());
		
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
		if(State.getCurrentDish() != null) {
			if (State.getCurrentDish().isNew()) {
				addDishToOrder.setText("Add Dish To Order");
			} else {
				addDishToOrder.setText("Edit");
			}
		}
		
		addDishToOrder.setOnAction((ActionEvent evt)->{
        	Dish dish = State.getCurrentDish().getDish();
        	
        	//Remove components based on selection
        	for(Pair<CheckBox, Component> compPair : componentList) {
        		if (!compPair.getKey().isSelected()) {
        			dish.removeComponent(compPair.getValue());
        		}
        	}
        	
        	Order order = State.getCurrentOrder();
        	
        	if(State.getCurrentDish().isNew()) {
        		//If order exists, add dish. otherwise create a new order
            	if (order != null) {
            		order.addDish(dish);
            	} else {
                	ArrayList<Dish> dishes = new ArrayList<Dish>();
                	dishes.add(dish);
            		State.setCurrentOrder(new Order(State.getCurrentCustomer(), dishes, null));
            	}
        	}
        	
			initShoppingCart();
        	//close side menu
        	toggleEditDish();
        });
	}
	
	public void initShoppingCart() {
		//Clear shopping cart
		cartVbox.getChildren().clear();
		
		//init values
		Order currentOrder = State.getCurrentOrder();
		if (currentOrder != null &&  State.getCurrentOrder().getDishes() != null && State.getCurrentOrder().getDishes().size() > 0) {
			List<Dish> dishes = State.getCurrentOrder().getDishes();
			cartVbox.getChildren().addAll(
					dishes
					.stream()
					.map(d -> getShoppingCartItem(d))
					.collect(Collectors.toList())
					);	

			double totalPrice = 0;
			for(Dish d : dishes) {
				totalPrice += d.calcDishPrice();
			}
			
			double totalTime = 0;
			for(Dish d : dishes) {
				totalTime += d.getTimeToMake(); //TODO: add the delivery time to the total time
			}
			
			priceText.setText(String.valueOf(totalPrice) + "$ time to prepare: " + totalTime);
		} else {
			priceText.setText("0");
			Pane emptyDishesPane = new Pane();
		//	Label noDishesLabel = new Label("No dishes");
			
			//Creating an image 
		      Image image = null;
			try {
				image = new Image(new FileInputStream("icons/shopping-cart-solid-grey.png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}  
		      
		      //Setting the image view 
			if(image != null) {
				ImageView imageView = new ImageView(image);
				emptyDishesPane.getChildren().addAll(imageView);
				imageView.relocate(57, 27);
			}
			
		//	emptyDishesPane.getChildren().add(noDishesLabel);
			cartVbox.getChildren().add(emptyDishesPane);
		}
	}
	

	private Pane getShoppingCartItem(Dish dish) {
		String dishName = dish.getDishName();
		double dishPrice = dish.calcDishPrice();
		String dishDescription = dish.getComponenets().toString();
		
		Pane newMenuItem = new Pane();
		Label dishLa = new Label("Dish: " + dishName + "\nPrice: " + String.valueOf(dishPrice) + "\nContains: " + dishDescription);
		Button editBtn = new Button("Edit");
		Button removeBtn = new Button("Remove");

		editBtn.setOnAction((ActionEvent evt)->{
			State.setCurrentDish(new CurrentDishModel(dish, false));
			toggleEditDish();
        });

		removeBtn.setOnAction((ActionEvent evt)->{
			Order order = State.getCurrentOrder();
			order.removeDish(dish);
			initShoppingCart();
        });
		
		newMenuItem.getStyleClass().add("shoppingCartItem");
		
		newMenuItem.getChildren().addAll(dishLa);
		newMenuItem.getChildren().addAll(editBtn);
		newMenuItem.getChildren().addAll(removeBtn);
		
		editBtn.relocate(280, 20);
		removeBtn.relocate(180, 20);
		return newMenuItem;
	}
	
	private void initializeConfirmButton() {
		confirmOrderBtn.setOnAction((ActionEvent evt) -> {
			
			Order currentOrder = State.getCurrentOrder();
			//add order to restaurant
			if(Restaurant.getInstance().addOrder(currentOrder)) {
				//TODO: add a delivery to the order
				
				//show pop up with message 
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Order ID " + State.getCurrentOrder().getId() + " Success");
				alert.setHeaderText("Order has been successfully add\n you now will be directed Orders page");
				alert.showAndWait();
				
				State.setCurrentDish(null);
				State.setCurrentOrder(null);
				initShoppingCart();
				
				try {
					replacePane(toReplacePane, "/View/Customer_OrdersHistory.fxml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			} else {
				//TODO: add error message to user
			}
		});
	}
}
