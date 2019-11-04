
package Classes;


public interface Check {
   
    /**
     *
     * @param username
     * @return false if user name is null or empty, true otherwise
     */
    public default boolean validUsername(String username){
        return (username!= null && !username.isEmpty());
    }
    
    /**********************************************************************************************************/
    
    /**
     * 
     * @param price
     * @return true if the price is not null, not empty and is integer or double, false otherwise
     */
    
    public default boolean validPrice(String price){
        return (price != null && !price.isEmpty() && price.matches("([0-9]*([.][0-9]{1,})?)|([0-9]{1,}([.][0-9]*)?)"));
        
    }
    
    /**********************************************************************************************************/
    /**
     * 
     * @param barcode
     * @return true if barcode is not null or empty, false otherwise
     */
    public default boolean validBarcode(String barcode){
        return (barcode != null && !barcode.isEmpty());
    }
    
    /**********************************************************************************************************/
    
    public default boolean validProductName(String productName){
     
        return (productName != null && !productName.isEmpty());
    }
    
    /**********************************************************************************************************/
    
     /**
     * 
     * @param password 
     * @return true if password is not null or empty, false otherwise
     */
    public default boolean validPassword(String password){
        return (password != null && !password.isEmpty());
    }
    
    
    
}
