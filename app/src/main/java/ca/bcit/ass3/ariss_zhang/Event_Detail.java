package ca.bcit.ass3.ariss_zhang;

/**
 * Created by zhang on 2017-11-10.
 */

public class Event_Detail {
    private String ItemName;
    private String ItemUnit;
    private int ItemQuantity;
    private int Event_ID;

    public Event_Detail(String iName, String iUnit, int iQuantity, int eID) {
        ItemName = iName;
        ItemUnit = iUnit;
        ItemQuantity = iQuantity;
        Event_ID = eID;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public int getItemQuantity() {
        return ItemQuantity;
    }

    public int getEventID() {
        return Event_ID;
    }

}
