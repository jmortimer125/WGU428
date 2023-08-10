package software.wgu428;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**AUTHOR JONATHAN MORTIMER
 * javadocs located in wgu428 javadocs folder
 */

/**ONE LOGICAL RUNTIME ERROR FIXED- the search by Part/Product name and ID textfields initially checked for string
*match, then parse.int for an integer match and would only produce an error message if an invalid integer was input.
*for this i had to change the search method with an "istext" method

*ONE FUTURE ENHANCEMENT- it would be massively helpful if the changes made while the application is open could
*be permanently saved.
 **/

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }
    //Load initial table values
    {
        Inventory.addPart(new InHouse(1, 5, 1, 15,"Brakes",15.00,123));
        Inventory.addPart(new InHouse(2,15, 1, 15,"Wheel",11.00,124));
        Inventory.addPart(new InHouse(3, 10, 1, 15,"Seat",15.00,125));
        Inventory.addProduct(new Products(1000,5, 1, 10, "Giant Bike", 299.99));
        Inventory.addProduct(new Products(1001,3, 1, 10,  "Tricycle", 99.99));


    }

    public static void main(String[] args) {
        launch();
    }
}