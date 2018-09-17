package viroopa.com.medikart.model;

//another class to handle item's id and name
public class M_objectItem {

    public int itemId;
    public String itemName;

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

    public M_objectItem(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

}