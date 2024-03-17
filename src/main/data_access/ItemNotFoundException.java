package main.data_access;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String Id, String itemType) {
        super("No matching " + itemType + " found for ID: " + Id);
    }
}
