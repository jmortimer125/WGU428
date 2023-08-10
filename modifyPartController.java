package software.wgu428;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** controller uses set method to preload values from selected part
 * of main window table
 */

public class modifyPartController implements Initializable{
    public RadioButton inHouse;
    public ToggleGroup tgroup;
    public RadioButton outSourced;
    public TextField modPartID;
    public TextField modPartName;
    public TextField modPartInv;
    public TextField modPartCost;
    public TextField modPartMax;
    public TextField modPartMin;
    public TextField modPartMachineID;
    public Label MachineID;
    public Button save;
    public Button cancelPart;
    private Parts selectedPart;
    public int partID;
    private Stage stage;
    private Object scene;

//void to be used by mainController to preload information
    public void setParts(Parts selectedPart) {
        this.selectedPart = selectedPart;
        partID = Inventory.getAllParts().indexOf(selectedPart);
        modPartID.setText(Integer.toString(selectedPart.getPartID()));
        modPartName.setText(selectedPart.getName());
        modPartInv.setText(Integer.toString(selectedPart.getStock()));
        modPartCost.setText(Double.toString(selectedPart.getPartCost()));
        modPartMax.setText(Integer.toString(selectedPart.getMax()));
        modPartMin.setText(Integer.toString(selectedPart.getMin()));
        if(selectedPart instanceof InHouse){
            InHouse ih = (InHouse) selectedPart;
            inHouse.setSelected(true);
            this.MachineID.setText("Machine ID");
            modPartMachineID.setText(Integer.toString(ih.getMachineID()));
        }
        else{
            OutSourced os = (OutSourced) selectedPart;
            outSourced.setSelected(true);
            this.MachineID.setText("Company");
            modPartMachineID.setText(os.getCompanyName());
        }
    }
//actions to change textfield based on togglegroup selection
    public void inHouseSelect(ActionEvent actionEvent) {
        if (inHouse.isSelected()){
            MachineID.setText("MachineID");
        }
    }

    public void outSourceSelect(ActionEvent actionEvent) {
        if (outSourced.isSelected()){
            MachineID.setText("Company");
        }
    }

    public void savePart(ActionEvent actionEvent)  throws IOException {
        int partInventory = Integer.parseInt(modPartInv.getText());
        int partMin = Integer.parseInt(modPartMin.getText());
        int partMax = Integer.parseInt(modPartMax.getText());

        //check for proper inventory,min,max values

        if (MainController.confirmDialog("Save?", "Would you like to save this part?"))
            if (partMax < partMin) {
                MainController.infoDialog("Input Error", "Error in min and max field", "Check Min and Max value.");
            } else if (partInventory < partMin || partInventory > partMax) {
                MainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            } else {
                int id = Integer.parseInt(modPartID.getText());
                String name = modPartName.getText();
                double price = Double.parseDouble(modPartCost.getText());
                int stock = Integer.parseInt(modPartInv.getText());
                int min = Integer.parseInt(modPartMin.getText());
                int max = Integer.parseInt(modPartMax.getText());
                if (inHouse.isSelected()) {
                    try {
                        int machineID = Integer.parseInt(modPartMachineID.getText());
                        InHouse temp = new InHouse(id, stock, min, max, name, price, machineID);
                        Inventory.updatePart(partID, temp);
                        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                        stage.setTitle("Inventory Management System");
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }
                    //check for proper integer in machine ID

                    catch (NumberFormatException e){
                        MainController.infoDialog("Input Error", "Check Machine ID ", "Machine ID can only contain numbers 0-9");
                    }
                }
                else {
                    String companyName = modPartMachineID.getText();
                    OutSourced temp = new OutSourced(id, stock, min, max, name, price, companyName);
                    Inventory.updatePart(partID, temp);
                    stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                    stage.setTitle("Inventory Management System");
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
            }
    }

    public void cancelModPart(ActionEvent actionEvent) throws IOException {
        if (MainController.confirmDialog("Cancel?", "Are you sure?")) {
            Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene inventoryhome = new Scene(root, 1000, 400);
            stage.setTitle("Inventory Management System");
            stage.setScene(inventoryhome);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
