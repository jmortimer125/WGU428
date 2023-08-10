package software.wgu428;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static software.wgu428.Inventory.getAllParts;

/** controls to add a part to the main parts table
 *
 */

public class addPartController {
    public RadioButton inHouse;
    public ToggleGroup tgroup;
    public RadioButton outSourced;
    public TextField addPartID;
    public TextField addPartName;
    public TextField addPartInv;
    public TextField addPartCost;
    public TextField addPartMax;
    public TextField addPartMin;
    public TextField addPartMachineID;
    public Label MachineID;
    public Button save;
    public Button cancelPart;


//change text field according to toggle group
    public void inHouseSelect(ActionEvent actionEvent) {
        if (inHouse.isSelected())
            MachineID.setText("Machine ID");
    }

    public void outSourceSelect(ActionEvent actionEvent) {
        if (outSourced.isSelected())
            MachineID.setText("Company");
    }

    public void savePart(ActionEvent actionEvent) {
        try {
            /**check for proper inventory,min,max values
             * validate inventory
             */
            int partInventory = Integer.parseInt(addPartInv.getText());
            int partMin = Integer.parseInt(addPartMin.getText());
            int partMax = Integer.parseInt(addPartMax.getText());
            if(MainController.confirmDialog("Save?", "Would you like to save this part?"))
                if(partInventory < partMin || partInventory> partMax) {
                    MainController.infoDialog("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum" );
                }
                else {
                    int newID = getNewID();
                    String name = addPartName.getText();
                    int stock = partInventory;
                    double price = Double.parseDouble(addPartCost.getText());
                    int min = partMin;
                    int max = partMax;
                    /**save as an outsourced or inhouse product
                     * this will use the proper constructor
                     */
                    if (outSourced.isSelected()) {
                        String companyName = addPartMachineID.getText();
                        Inventory.getAllParts().add(new OutSourced(newID, stock, min, max, name, price, companyName));
                    } else {
                        int machineID = Integer.parseInt(addPartMachineID.getText());
                        Inventory.getAllParts().add(new InHouse(newID, stock, min, max, name, price, machineID));

                    }


                    Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene inventoryhome = new Scene(root, 1000, 400);
                    stage.setTitle("Inventory Management System");
                    stage.setScene(inventoryhome);
                    stage.show();
                }


        } catch (IOException e) {
            MainController.infoDialog("Input Error", "Error in adding part", "Check fields for correct input");

        }

    }

        public void cancelAddPart (ActionEvent actionEvent) throws IOException {
            if (MainController.confirmDialog("Cancel?", "Are you sure?")) {
                Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene inventoryhome = new Scene(root, 1000, 400);
                stage.setTitle("Inventory Management System");
                stage.setScene(inventoryhome);
                stage.show();
            }
        }
/**auto-generatedID for disabled textfield
    */
    public static int getNewID() {
        int newID = 1;
        for (int i = 0; i < getAllParts().size(); i++) {
            newID++;
        }

        return newID;
    }
}
