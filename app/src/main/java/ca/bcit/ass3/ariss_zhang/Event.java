package ca.bcit.ass3.ariss_zhang;

/**
 * Created by Morgan on 2017-11-02.
 */

public class Event  {
    private String eName;
    private String eDate;
    private String eTime;
    private String eDescription;

    public static final Event[] eventsMaster = {
            new Event("Halloween Party", "October 30th", "6:30PM", "A description of the Halloween Event"),
            new Event("Christmas Party", "December 20th", "12:30PM", "A description of the Christmas Event"),
            new Event("Boxing Day", "December 26st", "4:30PM", "A description of the Boxing Day Event"),
            new Event("New Year's Eve Party", "December 31st", "8:00PM", "A description of the New Years Eve Event")
    };

    // Each country has a name, description and an image resource
    public Event(String name, String date, String time, String description) {
        eName = name;
        eDate = date;
        eTime = time;
        eDescription = description;
    }

    public String getName() {
        return eName;
    }

    public String getDate() {
        return eDate;
    }

    public String getTime() {
        return eTime;
    }

    public String getDescription() {
        return eDescription;
    }
}

