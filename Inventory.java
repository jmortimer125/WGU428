package software.wgu428;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;
/**holds all of the static operaters and observable list dependent on parts and products classes
 *
 */

public class Inventory {

    private static ObservableList<Parts> allParts = FXCollections.observableArrayList();
    private static ObservableList<Products> allProducts = FXCollections.observableArrayList();

    //+ addPart(newPart:Part):void
    public static void addPart(Parts newPart){
        allParts.add(newPart);
    }

    //+ getAllParts():ObservableList<Part>
    public static ObservableList<Parts> getAllParts() {
        return allParts;
    }

    //+ addProduct(newProduct:Product):void
    public static void addProduct(Products newProduct){
        allProducts.add(newProduct);
    }

    //+ getAllProducts():ObservableList<Product>
    public static ObservableList<Products> getAllProducts() {
        return allProducts;
    }

    //+ lookupPart(partId:int):Part
    public static Parts lookupPartID(int partID) {
        Parts temp = null;
        for (Parts parts : allParts){
            if (partID == parts.getPartID()){
                temp = parts;
            }
        }
        return temp;
    }

    //+ lookupProduct(productId:int):Product
    public static Products lookupProductID(int productID) {
        Products temp = null;
        for (Products products : allProducts){
            if (productID == products.getProductID()){
                temp = products;
            }
        }
        return temp;
    }

    //+ lookupPart(partName:String):ObservableList<Part>
    public static ObservableList<Parts> lookUpPart(String searchInput) {
        ObservableList<Parts> foundPartNames = FXCollections.observableArrayList();
        boolean isText = true;
        if (searchInput.matches(".*\\d.*")) {
            isText = false;
        }

        if (isText) {
            for (Parts foundPart : getAllParts()) {
                //conditional statement makes both the searchInput string and the partName String lowerCase so that it can disregard capitalization
                if (foundPart.getName().toLowerCase(Locale.ROOT).contains(searchInput.toLowerCase(Locale.ROOT))) {
                    foundPartNames.add(foundPart);
                } else if (foundPart.getName().equals("")) {
                    foundPartNames = getAllParts();
                }
            }
        }
        else {
            for (Parts foundPart : getAllParts()) {
                if (foundPart.getPartID() == Integer.parseInt(searchInput)) {
                    foundPartNames.add(foundPart);
                } else if (foundPart.getName().equals("")) {
                    foundPartNames = getAllParts();
                }
            }
        }

        return foundPartNames;
    }

    //+ lookupProduct by name
    public static ObservableList<Products> lookUpProducts(String searchInput) {
        ObservableList<Products> foundProductNames = FXCollections.observableArrayList();
        boolean isText = true;
        if (searchInput.matches(".*\\d.*")) {
            isText = false;
        }

        if (isText) {
            for (Products foundProduct : getAllProducts()) {
                //conditional statement nullifys capitalization
                if (foundProduct.getName().toLowerCase(Locale.ROOT).contains(searchInput.toLowerCase(Locale.ROOT))) {
                    foundProductNames.add(foundProduct);
                } else if (foundProduct.getName().equals("")) {
                    foundProductNames = getAllProducts();
                }
            }
        }
        // if an integer is entered into the search field parseInt to search productID
        else {
            for (Products foundProduct : getAllProducts()) {
                if (foundProduct.getProductID() == Integer.parseInt(searchInput)) {
                    foundProductNames.add(foundProduct);
                } else if (foundProduct.getName().equals("")) {
                    foundProductNames = getAllProducts();
                }
            }
        }
        return foundProductNames;
    }

    // updatePart
    public static void updatePart(int index, Parts selectedPart){
        allParts.set(index, selectedPart);
    }

    //updateProduct
    public static void updateProduct(int index, Products newProduct){
        allProducts.set(index, newProduct);
    }

    // deletePart
    public static boolean deletePart(Parts selectedPart){
        return allParts.remove(selectedPart);
    }

    //
    public static boolean deleteProduct(Products selectedProduct){
        return allProducts.remove(selectedProduct);
    }
}
