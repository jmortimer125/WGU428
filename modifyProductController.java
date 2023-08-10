package software.wgu428;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static software.wgu428.Inventory.lookUpPart;
import static software.wgu428.addProductController.searchProducedNoResults;
/** controller uses method to preload information from selected product in main window.

 */

public class modifyProductController implements Initializable {
    private Products selectedProduct;
    private Products modProduct;
    private int productID;
    private Stage stage;
    private Object scene;
    public TextField modProductID;
    public TextField modProductName;
    public TextField modProductInv;
    public TextField modProductPrice;
    public TextField modProductMin;
    public TextField modProductMax;
    public TableView availpartsTableView;
    public TableColumn availpartID;
    public TableColumn availPartName;
    public TableColumn availPartInventory;
    public TableColumn availPartCost;
    public TableView ascPartsTableView;
    public TableColumn ascPartID;
    public TableColumn ascPartName;
    public TableColumn ascPartInventory;
    public TableColumn ascPartCost;
    public TextField ascPartSearch;
    private ObservableList<Parts> associatedParts = FXCollections.observableArrayList();



    //void to be used by maincontroller to preload selected product information values
    public void setProduct(Products selectedProduct) {
        this.selectedProduct = selectedProduct;
        productID = Inventory.getAllProducts().indexOf(selectedProduct);
        modProductID.setText(Integer.toString(selectedProduct.getProductID()));
        modProductName.setText(selectedProduct.getName());
        modProductInv.setText(Integer.toString(selectedProduct.getStock()));
        modProductPrice.setText(Double.toString(selectedProduct.getProductPrice()));
        modProductMax.setText(Integer.toString(selectedProduct.getMax()));
        modProductMin.setText(Integer.toString(selectedProduct.getMin()));
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
    }

    public void addAscPart(ActionEvent actionEvent) {
        Parts selectedPart = (Parts) availpartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            associatedParts.add(selectedPart);
            updatePartTable();
            updateAssociatedPartTable();
        }

        else {
            MainController.infoDialog("Select a part","Select a part", "Select a part to add to the Product");
        }
    }

    public void searchAscPart(ActionEvent actionEvent) {
        String searchInput = ascPartSearch.getText();

        ObservableList<Parts> foundParts = lookUpPart(searchInput);
        availpartsTableView.setItems(foundParts);

        //shows alert message if searchInput produced 0 results.
        if (availpartsTableView.getItems().size() == 0) {
            searchProducedNoResults(searchInput);
        }
        ascPartSearch.setText("");

    }

    public void removeAscPart(ActionEvent actionEvent)  {
        Parts selectedPart = (Parts) ascPartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            MainController.confirmDialog("Deleting Parts","Are you sure you want to delete " + selectedPart.getName() + " from the Product?");
            associatedParts.remove(selectedPart);
            updatePartTable();
            updateAssociatedPartTable();
        }

        else {
            MainController.infoDialog("No Selection","No Selection", "Please choose something to remove");
        }
    }

    public void cancelProduct(ActionEvent actionEvent) throws IOException {
        if (MainController.confirmDialog("Cancel?", "Are you sure?")) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    public void saveProduct(ActionEvent actionEvent) throws IOException {
        int productInventory = Integer.parseInt(modProductInv.getText());
        int productMinimum = Integer.parseInt(modProductMin.getText());
        int productMaximum = Integer.parseInt(modProductMax.getText());
        if(MainController.confirmDialog("Save?", "Would you like to save this part?"))
            if(productMaximum < productMinimum) {
                MainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value." );
            }
            else if(productInventory < productMinimum || productInventory > productMaximum) {
                MainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum" );
            }
            else {
                this.modProduct = selectedProduct;
                selectedProduct.setProductID(Integer.parseInt(modProductID.getText()));
                selectedProduct.setName(modProductName.getText());
                selectedProduct.setStock(Integer.parseInt(modProductInv.getText()));
                selectedProduct.setProductPrice(Double.parseDouble(modProductPrice.getText()));
                selectedProduct.setMax(Integer.parseInt(modProductMax.getText()));
                selectedProduct.setMin(Integer.parseInt(modProductMin.getText()));
                selectedProduct.getAllAssociatedParts().clear();
                selectedProduct.addAssociatedPart(associatedParts);
                Inventory.updateProduct(productID, selectedProduct);

                //Back to Main window
                stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                stage.setTitle("Inventory Management System");
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
    }
    public void updatePartTable() {
        availpartsTableView.setItems(Inventory.getAllParts());
    }

    private void updateAssociatedPartTable() {
        ascPartsTableView.setItems(associatedParts);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Table for un-associated parts.
        availpartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        availPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availPartCost.setCellValueFactory(new PropertyValueFactory<>("partCost"));
        availpartsTableView.setItems(Inventory.getAllParts());

        //Table for associated parts
        ascPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        ascPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ascPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ascPartCost.setCellValueFactory(new PropertyValueFactory<>("partCost"));
        ascPartsTableView.setItems(associatedParts);

        updatePartTable();
        updateAssociatedPartTable();
    }
}
