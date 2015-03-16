package carnival.gusac.com.gusaccarnival40.navdrawer;

import android.graphics.drawable.Drawable;

/**
 * Created by Messi10 on 30-Jan-15.
 */
public class NavigationItem {

    private String mText;
    private Drawable mDrawable;

    public NavigationItem(String text, Drawable drawable) {
        mText = text;
        mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

}