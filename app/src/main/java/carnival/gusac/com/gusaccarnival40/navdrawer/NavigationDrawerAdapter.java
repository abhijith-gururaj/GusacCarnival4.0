package carnival.gusac.com.gusaccarnival40.navdrawer;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import carnival.gusac.com.gusaccarnival40.R;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private List<NavigationItem> mData;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;
    public Context context;
    private String email;


    public NavigationDrawerAdapter(Context c, List<NavigationItem> data, String EMAIL) {
        mData = data;
        context = c;

        email = EMAIL;
    }


    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks) {
        mNavigationDrawerCallbacks = navigationDrawerCallbacks;
    }

    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_row, viewGroup, false);
//        return new ViewHolder(v);
        Log.i("onCreateViewHolder,", "viewtype: " + viewType);
        if (viewType == 1) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_row, viewGroup, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == 0) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_header, viewGroup, false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder viewHolder, final int i) {

        if (viewHolder.Holderid == TYPE_ITEM) {
            viewHolder.textView.setText(mData.get(i - 1).getText());
            Log.i("onBindViewHolder,", "viewtype: " + mData.get(i - 1).getText());
            viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mData.get(i - 1).getDrawable(), null, null, null);

            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                                                       @Override
                                                       public boolean onTouch(View v, MotionEvent event) {

                                                           switch (event.getAction()) {
                                                               case MotionEvent.ACTION_DOWN:
                                                                   touchPosition(i);
                                                                   return false;
                                                               case MotionEvent.ACTION_CANCEL:
                                                                   touchPosition(-1);
                                                                   return false;
                                                               case MotionEvent.ACTION_MOVE:
                                                                   return false;
                                                               case MotionEvent.ACTION_UP:
                                                                   touchPosition(-1);
                                                                   return false;
                                                           }
                                                           return true;
                                                       }
                                                   }
            );
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           if (mNavigationDrawerCallbacks != null)
                                                               mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(i);
                                                       }
                                                   }
            );
            if (mSelectedPosition == i || mTouchedPosition == i) {
                viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.selected_gray));
            } else {
                viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            Log.i("onBindViewHolder,", "viewtype: " + i);
            // Similarly we set the resources for header view

            viewHolder.email.setText(email);
        }

    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0)
            notifyItemChanged(lastPosition);
        if (position >= 0)
            notifyItemChanged(position);
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        TextView Name;
        TextView email;
        int Holderid;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.item_name);
                Holderid = 1;
            } else {


                // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email

                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }


}
