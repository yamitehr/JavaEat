package Controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


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
	private TableColumn<Dish, String> dishPriceCol;
	@FXML
	private TableColumn<Dish, String> relationCol;
	@FXML
	private DatePicker selectedDate;
	@FXML
	private Label todaySell;
	@FXML
	private Label todayRevenue;
	
	@FXML
    public void initialize() {
		init();
		proFit();
		popularComponents();
    }
	
		

	private void popularComponents() {
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
	}



	private void init() {
		totalCustomers.setText(String.valueOf(Restaurant.getInstance().getCustomers().size()));
		totalEmployees.setText(String.valueOf(Restaurant.getInstance().getCooks().size() + 
				Restaurant.getInstance().getDeliveryPersons().size()));

		totalSell.setText(String.format("%.1f", totalSell()));
		
		totalRevenue.setText(String.format("%.1f", totalRevenue()));
		
		revenueFromExpress.setText(String.format("%.1f", Restaurant.getInstance().revenueFromExpressDeliveries()));
		
		todaySell.setText(String.format("%.1f", dailySell()));
		
		todayRevenue.setText(String.format("%.1f", dailyRevenue()));		
	}
	
	
	private void proFit() {
		dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
						
		timeToMakeCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<Integer>(dish.getValue().getTimeToMake()));
		
		dishPriceCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(String.format("%.2f", dish.getValue().getPrice())));
		
		relationCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(String.format("%.2f",dish.getValue().getPrice() / dish.getValue().getTimeToMake())));
					
		List<Dish> profitRelation = new ArrayList<Dish>();
		profitRelation = Restaurant.getInstance().getProfitRelation().stream()
				.collect(Collectors.toList());
						
		profitRelationTable.getItems().addAll(profitRelation);

	}
	
	public double dailyRevenue() {
		double revenueDate = 0;
		for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
			if(delivery.getDeliveredDate().equals(LocalDate.now())) {
				if(delivery instanceof RegularDelivery) {
					for(Order o: ((RegularDelivery) delivery).getOrders()) {
						revenueDate += o.calcOrderRevenue();
					}
				}
				else { //express
					revenueDate += ((ExpressDelivery) delivery).getOrder().calcOrderRevenue();
				}
			}
		}
		return revenueDate;
	}
	
	public double dailySell() {
		double SellDate = 0;
		for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
			if(delivery.getDeliveredDate().equals(LocalDate.now())) {
				if(delivery instanceof RegularDelivery) {
					for(Order o: ((RegularDelivery) delivery).getOrders()) {
						for(Dish dish: o.getDishes()) {
							SellDate += dish.getPrice();
						}
					}
				}
				else { //express
					for(Dish dish: ((ExpressDelivery) delivery).getOrder().getDishes()) {
						SellDate += dish.getPrice();
					}
				}
			}
		}
		return SellDate;
	}
	
	public double totalSell() {
		double totalSell = 0;
		for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
			if(delivery instanceof RegularDelivery) {
				for(Order o: ((RegularDelivery) delivery).getOrders()) {
					for(Dish dish: o.getDishes()) {
						totalSell += dish.getPrice();
					}
				}
			}
			else { //express
				for(Dish dish: ((ExpressDelivery) delivery).getOrder().getDishes()) {
					totalSell += dish.getPrice();
				}
			}
		}
		return totalSell;
	}
	
	public double totalRevenue() {
		double totalRevenue = 0;
		for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
			if(delivery instanceof RegularDelivery) {
				for(Order o: ((RegularDelivery) delivery).getOrders()) {
					totalRevenue += o.calcOrderRevenue();
				}
			}
			else { //express
				totalRevenue += ((ExpressDelivery) delivery).getOrder().calcOrderRevenue();
			}
		}
		return totalRevenue;
	}
	
	public void export(ActionEvent e) {
		 try {
			 //Blank Document
			 XWPFDocument document = new XWPFDocument();
			//Write the Document in file system
			FileOutputStream out = null;
			out = new FileOutputStream("ProFit.docx");
			//disable editing
			document.enforceReadonlyProtection();

			//Write first Text in the beginning
			// create header
			XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);

			XWPFParagraph paragraph = header.createParagraph();
			XWPFRun run = paragraph.createRun();  
			run.setText("ProFit Relation JavaEat Restaurant");
			paragraph.setIndentationLeft(4000);


			paragraph = document.createParagraph();
			run = paragraph.createRun();
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = myDateObj.format(myFormatObj);
			run.setText("Date: " + formattedDate);
			run.addBreak();
			
			paragraph = document.createParagraph();
			// set indentation of the paragraph
			paragraph.setIndentationLeft(720);
			run  = paragraph.createRun();
			paragraph.setIndentationLeft(4000);
			run.setText("To: JavaEat Manager\n");
			run.setBold(true);
			run.setItalic(true);
			run.setUnderline(UnderlinePatterns.SINGLE);
			run.addBreak();
			
			
			//create table
			XWPFTable table = document.createTable();
			
			//create first row of the table
			XWPFTableRow tableRowOne = table.getRow(0);
			tableRowOne.getCell(0).setText("Dish Name");
			tableRowOne.addNewTableCell().setText("Time To Make");
			tableRowOne.addNewTableCell().setText("Price");
			tableRowOne.addNewTableCell().setText("Relation");
			
			//insert data to the table according to the amount of the data
			for (int i = 0; i < profitRelationTable.getItems().size(); i++) {
				XWPFTableRow tableRow = table.createRow();
				tableRow.getCell(0).setText(profitRelationTable.getItems().get(i).getDishName());
				tableRow.getCell(1).setText(String.valueOf(profitRelationTable.getItems().get(i).getTimeToMake()));
				tableRow.getCell(2).setText(String.format("%.2f",profitRelationTable.getItems().get(i).getPrice()));
				tableRow.getCell(3).setText(String.format("%.2f",profitRelationTable.getItems().get(i).getPrice() / profitRelationTable.getItems().get(i).getTimeToMake()));
				table.getRow(i).getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				table.getRow(i).getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				table.getRow(i).getCell(2).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				table.getRow(i).getCell(3).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
			}

			paragraph = document.createParagraph();
			run = paragraph.createRun();
			run.addBreak();
			run.setText("Best Regards");

			document.write(out);
			
			//open the file
			if (Desktop.isDesktopSupported()) {
				File myFile = new File("ProFit.docx");
				Desktop.getDesktop().open(myFile);
			}
			
			out.close();
			document.close();
			System.out.println("ProFit.docx written successully");
		 } catch (FileNotFoundException e1) {
			 	e1.printStackTrace();
		 } catch (IOException e1) {
				e1.printStackTrace();
		 }
	}
}
