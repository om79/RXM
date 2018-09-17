package viroopa.com.medikart;

//another class to handle item's id and name
public class ObjectItem {

    public int itemId;
    public String itemName;
    public String Email_id;

    public String getEmail_id() {
        return Email_id;
    }

    public void setEmail_id(String email_id) {
        Email_id = email_id;
    }



    // constructor

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ObjectItem ( int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public ObjectItem ( int itemId, String itemName,String Email_id) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.Email_id= Email_id;
    }

}