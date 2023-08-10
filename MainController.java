package software.wgu428;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** initializes the main table values and activates main window buttons. buttons throw io exceptions
 
 */
public class MainController implements Initializable {
    public TableView partsTable;
    public TableColumn partID;
    public TableColumn partName;
    public TableColumn partInventory;
    public TableColumn partCost;
    public Button addPart;
    public Button modifyPart;
    public Button deletePart;
    public TextField lookupPart;
    public TableView productsTable;
    public TableColumn productID;
    public TableColumn productName;
    public TableColumn productInventory;
    public TableColumn productCost;
    public TextField lookupProduct;

    /** initializes the main table values and activates main window buttons. buttons throw io exceptions
     *
     * @param location
     * @param resources
     */

    @Override
    //initial tables to view
        public void initialize(URL location, ResourceBundle resources) {
            partsTable.setItems(Inventory.getAllParts());
            partID.setCellValueFactory(new PropertyValueFactory<Parts, Integer>("partID"));
            partName.setCellValueFactory(new PropertyValueFactory<Parts, String>("name"));
            partInventory.setCellValueFactory(new PropertyValueFactory<Parts, Integer>("stock"));
            partCost.setCellValueFactory(new PropertyValueFactory<Parts, Double>("partCost"));

            productsTable.setItems((Inventory.getAllProducts()));
            productID.setCellValueFactory(new PropertyValueFactory<Products, Integer>("productID"));
            productName.setCellValueFactory(new PropertyValueFactory<Products, String>("name"));
            productInventory.setCellValueFactory(new PropertyValueFactory<Products, Integer>("stock"));
            productCost.setCellValueFactory(new PropertyValueFactory<Products, Double>("productPrice"));

    }

    public void addPartClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene addpart = new Scene(root, 400, 600);
        stage.setTitle("Add Part");
        stage.setScene(addpart);
        stage.show();
    }

    public void modifyPartBtn(ActionEvent actionEvent)throws IOException {

        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyPart.fxml"));
        loader.load();
        modifyPartController modcontroller = loader.getController();
        modcontroller.setParts((Parts) partsTable.getSelectionModel().getSelectedItem());


        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Part");
        stage.setScene(new Scene(scene));
        stage.showAndWait();



    }

    public void deletePartBtn(ActionEvent actionEvent) {
        if (partsTable.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No Part Selected", "Please choose part from the above list");
            return;
        }
        if (confirmDialog("Warning!", "Would you like to delete this part?")) {
            int selectedPart = partsTable.getSelectionModel().getSelectedIndex();
            partsTable.getItems().remove(selectedPart);
        }
    }

    public void lookUpPart(ActionEvent actionEvent) {
        String term = lookupPart.getText();
        ObservableList foundPartNames = Inventory.lookUpPart(term);

        partsTable.setItems(foundPartNames);
        {
            //error message if no results found
          if (partsTable.getItems().size()==0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("No Match");
                alert.setHeaderText("Unable to locate a part name with: " + term);
                alert.showAndWait();
            }
        }
    }

    public void lookUpProduct(ActionEvent actionEvent) {
        String searchInput = lookupProduct.getText();

        ObservableList<Products> foundProducts = Inventory.lookUpProducts(searchInput);
        productsTable.setItems(foundProducts);
        //Error message if no results found
        if (productsTable.getItems().size()==0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("No Match");
            alert.setHeaderText("Unable to locate a product");
            alert.showAndWait();
        }
    }
    //confirmation of delete/save
    static boolean confirmDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    // confirm or cancel alert screen
    static void infoDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public void addProductClick(ActionEvent actionEvent)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene addproduct = new Scene(root, 800, 600);
        stage.setTitle("Add Product");
        stage.setScene(addproduct);
        stage.show();
    }

    public void modifyProductClick(ActionEvent actionEvent)throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyProduct.fxml"));
        loader.load();
        modifyProductController modcontrol = loader.getController();
        modcontrol.setProduct((Products) productsTable.getSelectionModel().getSelectedItem());


        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setTitle("Modify Product");
        stage.setScene(new Scene(scene));
        stage.showAndWait();
    }

    public void deleteProductClick(ActionEvent actionEvent) {
        Products selectedProduct;
        selectedProduct = (Products) productsTable.getSelectionModel().getSelectedItem();
        if (productsTable.getSelectionModel().isEmpty()){
            infoDialog("Warning!", "No Product Selected","Please choose product from the above list");
            return;
        }

        if (confirmDialog("Warning!", "Would you like to delete this product?")){
            int selectedPart = productsTable.getSelectionModel().getSelectedIndex();
            if (selectedProduct.getAllAssociatedParts().size() > 0) {
                infoDialog("Warning!", "Product has associated parts","can not remove product with associated parts");
                return;
            }
            productsTable.getItems().remove(selectedPart);
        }
    }

    public void exitclk(ActionEvent actionEvent) throws IOException{
        confirmDialog("Exit", "Are you sure you would like to exit the program?");
        {
            System.exit(0);
        }
    }
}