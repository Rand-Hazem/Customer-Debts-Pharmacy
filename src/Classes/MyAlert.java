
package Classes;

import javafx.scene.control.Alert;

/**
 *
 * @author rand
 */
public class MyAlert {
    
    public static void showMsg(String msg, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
    
}
