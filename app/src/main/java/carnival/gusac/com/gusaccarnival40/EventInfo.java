package carnival.gusac.com.gusaccarnival40;

/**
 * Created by Messi10 on 31-Jan-15.
 */

/*This class provides an abstraction for an event's title,description and its image.
These properties are set using a custom list view adapter(MyEventAdapter).
 */
public class EventInfo {
    public String title;
    public String description;
    public int image;

    //Set the respective properties in the constructor
    public EventInfo(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
