package software.wgu428;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;

import static software.wgu428.Inventory.lookUpPart;

/** adds a product to the main product table on the mainwindow
 *
 */

public class addProductController implements Initializable {
    private Stage stage;
    private Object scene;
    public TextField addProductID;
    public TextField addProductName;
    public TextField addProductInv;
    public TextField addProductPrice;
    public TextField addProductMin;
    public TextField addProductMax;
    public TableColumn availpartID;
    public TableColumn availPartName;
    public TableColumn availPartInventory;
    public TableColumn availPartCost;
    public TableColumn ascPartID;
    public TableColumn ascPartName;
    public TableColumn ascPartInventory;
    public TableColumn ascPartCost;
    public TextField ascPartSearch;
    public TableView availpartsTableView;
    public TableView ascPartsTableView;
    private ObservableList<Parts> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Parts> originalParts = FXCollections.observableArrayList();


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

        /**show alert  if search produced no results.
         *
         */
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

    public void saveProduct(ActionEvent actionEvent)throws IOException {

        /**Test if product is not null
         *
         */
        if (associatedParts.isEmpty()){
            MainController.infoDialog("Input Error", "Please add one ore more parts", "A product must have one or more parts associated with it.");
            return;}

        if (addProductName.getText().isEmpty() || addProductMin.getText().isEmpty() || addProductMax.getText().isEmpty() || addProductMax.getText().isEmpty() || addProductPrice.getText().isEmpty()) {
            MainController.infoDialog("Input Error", "Cannot have blank fields", "Check all the fields.");
            return;
        }
/**check for proper inventory values
 *
 */

        Integer inv = Integer.parseInt(this.addProductInv.getText());
        Integer min = Integer.parseInt(this.addProductMin.getText());
        Integer max = Integer.parseInt(this.addProductMax.getText());

        if (max < min) {
            MainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            return;
        }

        if (inv < min || inv > max) {
            MainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            return;
        }

        /**Add Product with entered information
         *
         */
        if (MainController.confirmDialog("Save?", "Would you like to save this part?")) {
            Products product = new Products();
            product.setProductID(getNewID());
            product.setName(this.addProductName.getText());
            product.setStock(Integer.parseInt(this.addProductInv.getText()));
            product.setMin(Integer.parseInt(this.addProductMin.getText()));
            product.setMax(Integer.parseInt(this.addProductMax.getText()));
            product.setProductPrice(Double.parseDouble(this.addProductPrice.getText()));
            product.addAssociatedPart(associatedParts);
            Inventory.addProduct(product);

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }

    }

//auto generated ID for disabled textfield
    private int getNewID(){
        int newID = 1;
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (Inventory.getAllProducts().get(i).getProductID() == newID) {
                newID++;
            }
        }
        return newID;
    }

    public void updatePartTable() {
        availpartsTableView.setItems(Inventory.getAllParts());
    }

    private void updateAssociatedPartTable() {
        ascPartsTableView.setItems(associatedParts);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        originalParts = Inventory.getAllParts();

        //Table for un-associated parts.
        availpartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        availPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availPartCost.setCellValueFactory(new PropertyValueFactory<>("partCost"));
        availpartsTableView.setItems(originalParts);

        //Table for associated parts
        ascPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        ascPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ascPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ascPartCost.setCellValueFactory(new PropertyValueFactory<>("partCost"));
        ascPartsTableView.setItems(associatedParts);

        updatePartTable();
        updateAssociatedPartTable();
    }
    public static void searchProducedNoResults(String searchInput) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Item not found");
        alert.setHeaderText("Search produced no results.");
        alert.setContentText("\"" + searchInput +"\""  +" found no results.");
        alert.showAndWait();
    }
}

