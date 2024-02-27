package ht.ShoppingCart;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ShoppingCart extends Application{

	FlowPane root = new FlowPane();
	Scene scene = new Scene(root, 800, 600);
	Pane sidebar, ui =  new Pane();
	
	TextArea receipt;
	TextField totaltxt, taxtxt, discounttxt, subtotaltxt;
	
	Button addq, minusq, purchaseBtn, testbtn, discountBtn;
	TextField quantity; 
	ComboBox<String> productCombo;
	Label pcLabel, discountlbl, testlbl, discountInfolbl;
	ArrayList<Products> productsList = new ArrayList<Products>();
	List<Double> decuctionList = new ArrayList<>();
	
	
	Products products;
	double totalPrice = 0, subTotal = 0, totalTax = 0, toDisplayTotalSaved = 0;
	final double tax = 12.5;
	double lastAddedPriceForDiscount = 0;
	String lastAddedName;

	
	
	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
		primaryStage.setScene(scene);
		primaryStage.show();
		
		sidebar = new Pane();
		sidebar.setPrefSize(200, 6000);
		sidebar.setStyle("-fx-background-color: #ffffc8");
		
		ui = new Pane();
		ui.setPrefSize(600, 800);
		ui.setStyle("-fx-background-color: #fffff0");
		
		
		root.getChildren().addAll(sidebar, ui); 
		
		pcLabel = new Label("Select your product below :)");
		pcLabel.setLayoutX(20);
		pcLabel.setLayoutY(20);
		sidebar.getChildren().add(pcLabel);
		
		
		testlbl = new Label("test");
		testlbl.setLayoutX(0);
		testlbl.setLayoutY(0);
		//ui.getChildren().add(testlbl);
		
		testbtn = new Button();
		testbtn.setLayoutX(40);
		testbtn.setLayoutY(0);
		//ui.getChildren().add(testbtn);
		testbtn.setOnAction(e -> testMethod());
		
		ObservableList<String> options = FXCollections.observableArrayList(
				"Dove",
				"Axe Deo",
				"other2"
				);		
		
		productCombo = new ComboBox<String>(options);
		productCombo.setLayoutX(20);
		productCombo.setLayoutY(80);
		productCombo.setValue("Select a product");
		
		quantity = new TextField();
		quantity.setLayoutX(20);
		quantity.setLayoutY(110);
		quantity.setPrefWidth(30);
		quantity.setText("1");
		
		addq = new Button();
		addq.setLayoutX(60);
		addq.setLayoutY(110);
		addq.setPrefHeight(25);
		addq.setPrefWidth(30);
		addq.setText("+");
		
		minusq = new Button();
		minusq.setLayoutX(100);
		minusq.setLayoutY(110);
		minusq.setPrefHeight(25);
		minusq.setPrefWidth(30);
		minusq.setText("-");
		
		purchaseBtn = new Button();
		purchaseBtn.setLayoutX(20);
		purchaseBtn.setLayoutY(140);
		purchaseBtn.setText("Add to cart");
		
		sidebar.getChildren().addAll(productCombo, quantity, addq, minusq,purchaseBtn);
		
		addq.setOnAction(e -> changeQuantity("+"));
		minusq.setOnAction(e -> changeQuantity("-"));
		purchaseBtn.setOnAction(e -> purchase());
		
		receipt = new TextArea();
		receipt.setLayoutX(10);
		receipt.setLayoutY(80);
		receipt.setPrefSize(150, 300);
		
		totaltxt = new TextField();
		totaltxt.setLayoutX(10);
		totaltxt.setLayoutY(520);
		totaltxt.setText("Total: £");
		
		//sub total test box
		subtotaltxt = new TextField();
		subtotaltxt.setLayoutX(10);
		subtotaltxt.setLayoutY(440);
		subtotaltxt.setText("Sub-total: £");
		
		
		taxtxt = new TextField();
		taxtxt.setLayoutX(10);
		taxtxt.setLayoutY(400);
		taxtxt.setText("Tax (12.5%): £" );
		
		ui.getChildren().addAll(receipt, totaltxt, taxtxt, subtotaltxt);
		
		discountlbl = new Label();
		discountlbl.setLayoutX(20);
		discountlbl.setLayoutY(180);
		discountlbl.setText("Click to activate discount");
		
		discountBtn = new Button();
		discountBtn.setLayoutX(20);
		discountBtn.setLayoutY(200);
		discountBtn.setText("Apply B2GO");
		
		//sidebar.getChildren().addAll(discountlbl, discountBtn);
		
		discountInfolbl = new Label();
		discountInfolbl.setLayoutX(160);
		discountInfolbl.setLayoutY(480);
		discountInfolbl.setText("(Tax not included)");
		ui.getChildren().add(discountInfolbl);
		
		discounttxt = new TextField();
		discounttxt.setLayoutX(10);
		discounttxt.setLayoutY(480);
		discounttxt.setText("You saved: £"); 
		ui.getChildren().add(discounttxt);
		
		productCombo.getSelectionModel().selectedItemProperty().addListener(comboListner);
		
	}
	
	
	private void changeQuantity(String operator) {
		
		
		int Qnum = Integer.parseInt(quantity.getText());
		if (operator.equals("+")) {
			Qnum = Qnum + 1;
			quantity.setText(Qnum+"");
		}
		else if (operator.equals("-") && Qnum > 1) {
			Qnum = Qnum - 1;
			quantity.setText(Qnum+"");
		}
	}

	ChangeListener<String> comboListner = new ChangeListener<String>() {
		
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

			
		}
	};
	private void purchase() {	
		String currentProduct = productCombo.getValue();
		int quantityNum = Integer.parseInt(quantity.getText());
		totalPrice = 0;
		if (valid(currentProduct, quantityNum)) {
			for (int i = 0; i < quantityNum;i++){					
				products = new Products(currentProduct);
				addProductToList(products.getName(), products.getPrice()); 					
		
			}
			calculateTotal();	
			printReceipt();	
		}				
	}
	
	
	
	public void addProductToList(String name, double price)
	{
		productsList.add(new Products(name, price));
	}
	
	
	
	
	private void printReceipt() {
		clearReceipt();
		String text="";
		for(Products c: productsList) {	
			text=text+"____________\n Name: "+c.name+"\n price: "+c.price+"\n";
			receipt.setText(text);
		}	
		printTotal();
		
	}
	
	
	
	private void printTotal() {
		
		var df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		
		totaltxt.setText("Total: £"+ df.format(totalPrice));
		subtotaltxt.setText("Sub-total: £" + df.format(subTotal));
		discounttxt.setText("You saved: £"+ df.format(toDisplayTotalSaved));
		taxtxt.setText("Tax (12.5%): £" + df.format(totalTax));
		
		//totalPrice = 0;	
	}
	
	
	
	
	public void calculateTotal() {	
		int productListItem = productsList.size();
		int quantityNum = Integer.parseInt(quantity.getText());	
			
		
		for(Products c: productsList) {	
			totalPrice = c.getPrice() + totalPrice;
			if (quantityNum >= 3) {
				lastAddedPriceForDiscount = c.getPrice();
			}
			lastAddedName = c.getName();		
		}	
		
		
		double totalForDiscount = totalPrice;
		
		totalPrice =  totalPrice + (tax * (totalPrice/100));
		subTotal = totalPrice;
		
		
		checkDiscount(productListItem, totalForDiscount, quantityNum);
		
	}
	
	
	
	private void checkDiscount(int productListItem, double totalForDiscount, int quantityNum) {
		
		if (productListItem >= 3 && lastAddedName.equalsIgnoreCase("dove")) {
			
	
			int freeProducts = productListItem / 3;	
			int selectedQuantityNum = quantityNum / 3;
			totalPrice = totalPrice - (freeProducts * (lastAddedPriceForDiscount + (lastAddedPriceForDiscount * (tax/100))));
			
			
			toDisplayTotalSaved = freeProducts * lastAddedPriceForDiscount;
			double totalDedecuted = selectedQuantityNum * (lastAddedPriceForDiscount + (toDisplayTotalSaved *(tax/100)));
			decuctionList.add(totalDedecuted);
			
			
			double currentTax = totalForDiscount * (tax/100);
			totalTax = currentTax - (freeProducts * (lastAddedPriceForDiscount * (tax/100)));
					
		}	
		else {	
			
			for (int i = 0; i < decuctionList.size(); i++) {
				totalPrice = totalPrice - decuctionList.get(i);
			}
			
			totalTax = totalForDiscount * (tax/100);
				
		}			
	}
	
	
	
	private boolean valid(String currentProduct, int quantityNum) {
		 if (currentProduct.equals("Select a product")) {
			 warningMsg("productNotSelected");
			return false;
		}
		 else {
			 if (quantityNum <= 0) {
				warningMsg("quantityIsZero");
				return false;
			}
			 return true;
		 }
	}
	
	
	
	private void warningMsg(String warningType) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setHeaderText(null);
		
		if (warningType.equals("quantityIsZero")) {
			alert.setTitle("Quantity warning");	
			alert.setContentText("Quantity must be atleast 1!");
		
		}
		if (warningType.equals("productNotSelected")) {
			alert.setTitle("Product select");
			alert.setContentText("Please select a product to add to basket!");
		}
		alert.showAndWait();
	}	
	
	
	
	private void testMethod() {
		receipt.setText("");
	}

	private void clearReceipt() {
		receipt.setText("");
	}
}







