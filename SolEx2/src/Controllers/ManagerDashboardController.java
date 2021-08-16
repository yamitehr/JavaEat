package Controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
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
		proFit();
    }
	
	private void proFit() {
		//profit relation
		dishNameCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<String>(dish.getValue().getDishName()));
						
		timeToMakeCol.setCellValueFactory(dish -> new ReadOnlyObjectWrapper<Integer>(dish.getValue().getTimeToMake()));
				
		dishPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
					
		List<Dish> profitRelation = new ArrayList<Dish>();
		profitRelation = Restaurant.getInstance().getProfitRelation().stream()
				.collect(Collectors.toList());
						
		profitRelationTable.getItems().addAll(profitRelation);

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
		
		
//		selectedDate.setOnAction(d -> {
//			double SellDate = 0;
//			double revenueDate = 0;
//			for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
//				if(delivery.getDeliveredDate().equals(selectedDate.getValue())) {
//					if(delivery instanceof RegularDelivery) {
//						for(Order o: ((RegularDelivery) delivery).getOrders()) {
//							revenueDate += o.calcOrderRevenue();
//							for(Dish dish: o.getDishes()) {
//								SellDate += dish.getPrice();
//							}
//						}
//					}
//					else { //express
//						revenueDate += ((ExpressDelivery) delivery).getOrder().calcOrderRevenue();
//						for(Dish dish: ((ExpressDelivery) delivery).getOrder().getDishes()) {
//							SellDate += dish.getPrice();
//						}
//					}
//				}
//			}
//			sellByDate.setText(String.valueOf(SellDate));
//			revenueByDate.setText(String.valueOf(revenueDate));
//		});
	        
		
		
	}
	
	public double monthlyRevenue(int month) {
		double revenueDate = 0;
		for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
			if(delivery.getDeliveredDate().getMonthValue() == month) {
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
	
	public double monthlySell(int month) {
		double SellDate = 0;
		for(Delivery delivery: Restaurant.getInstance().getDeliveries().values()) {
			if(delivery.getDeliveredDate().getMonthValue() == month) {
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
			run.setText("ProFit Relation");

			XWPFParagraph para = document.createParagraph();
			XWPFRun run1 = para.createRun();
			para.setIndentationLeft(6500);
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = myDateObj.format(myFormatObj);
			run1.setText("Date: " + formattedDate);
			
			para = document.createParagraph();
			// set indentation of the paragraph
			para.setIndentationLeft(720);
			run1  = para.createRun();  
			para.setIndentationLeft(4000);
			run1.setText("To: JavaEat Manager\n");
			run1.setBold(true);
			run1.setItalic(true);
			run1.setUnderline(UnderlinePatterns.SINGLE);
			
			
			//create table
			XWPFTable table = document.createTable();
			
			XWPFTableRow tableRowOne = table.getRow(0);
			tableRowOne.getCell(0).setText("Dish Name");
			tableRowOne.addNewTableCell().setText("Time To Make");
			tableRowOne.addNewTableCell().setText("Price");
			tableRowOne.addNewTableCell().setText("Relation");
			
			for (int i=0; i<profitRelationTable.getItems().size();i++) {
				XWPFTableRow tableRow = table.createRow();
				tableRow.getCell(0).setText(profitRelationTable.getItems().get(i).getDishName());
				tableRow.getCell(1).setText(String.valueOf(profitRelationTable.getItems().get(i).getTimeToMake()));
				tableRow.getCell(2).setText(String.valueOf(profitRelationTable.getItems().get(i).getPrice()));
				tableRow.getCell(3).setText(String.valueOf(profitRelationTable.getItems().get(i).getPrice() / profitRelationTable.getItems().get(i).getTimeToMake()));
				table.getRow(i).getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				table.getRow(i).getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				table.getRow(i).getCell(2).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
				table.getRow(i).getCell(3).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2000));
			}

			//Write second Text after the table (by creating a new paragraph)
			XWPFParagraph para2 = document.createParagraph();
			XWPFRun run3 = para2.createRun();
			para2.setAlignment(ParagraphAlignment.LEFT);
			run3.setText("Bye");

			document.write(out);
			
			if (Desktop.isDesktopSupported()) {
				File myFile = new File("ProFit.docx");
				Desktop.getDesktop().open(myFile);
			}
			
			out.close();
			document.close();
			 System.out.println("WorkBook.docx written successully");
		 } catch (FileNotFoundException e1) {
		e1.printStackTrace();
		 } catch (IOException e1) {
				e1.printStackTrace();
		 }
	}
}
