/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.debts.pharmacy;

import Classes.SharedValues;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rand
 */
public class CustomerDebtsPharmacy extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        SharedValues.mainStage= stage; 
        FXMLLoader loader=new FXMLLoader();
        Parent root = loader.load(getClass().getResource("FXMLDocument.fxml"));//FXMLDocument.fxml
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Customer debts - Al Rahma");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
