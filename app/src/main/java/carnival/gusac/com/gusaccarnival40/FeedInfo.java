package carnival.gusac.com.gusaccarnival40;

/**
 * Created by Messi10 on 06-Mar-15.
 */

/*
This is an abstraction for the feed data i.e. title,body and an associated image.
 */
public class FeedInfo {
    public String head;
    public String body;

    public int image;

    public FeedInfo(String head, String body, int image) {
        this.head = head;
        this.body = body;

        this.image = image;
    }
}
