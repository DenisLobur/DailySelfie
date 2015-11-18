package com.example.selfie.view.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.selfie.R;
import com.example.selfie.detail.DetailActivity;
import com.example.selfie.model.mediator.webdata.Selfie;

import java.util.ArrayList;
import java.util.List;

/**
 * Show the view for each Selfie's meta-data in a ListView.
 */
public class SelfieAdapter
       extends BaseAdapter {
    /**
     * Allows access to application-specific resources and classes.
     */
    private final Context mContext;

    /**
     * ArrayList to hold list of Selfies that is shown in ListView.
     */
    private List<Selfie> selfieList =
        new ArrayList<>();

    /**
     * Constructor that stores the Application Context.
     *
     * @param context
     */
    public SelfieAdapter(Context context) {
        super();
        mContext = context;
    }

    /**
     * Method used by the ListView to "get" the "view" for each row of
     * data in the ListView.
     *
     * @param position
     *            The position of the item within the adapter's data
     *            set of the item whose view we want. convertView The
     *            old view to reuse, if possible. Note: You should
     *            check that this view is non-null and of an
     *            appropriate type before using. If it is not possible
     *            to convert this view to display the correct data,
     *            this method can create a new view. Heterogeneous
     *            lists can specify their number of view types, so
     *            that this View is always of the right type (see
     *            getViewTypeCount() and getItemViewType(int)).
     * @param parent
     *            The parent that this view will eventually be
     *            attached to
     * @return A View corresponding to the data at the specified
     *         position.
     */
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        final Selfie selfie = selfieList.get(position);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.selfie_item, null);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.name_txt);
        titleText.setText(selfie.getTitle());
        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra(SelfieDetailActivity.SELFIE_EXTRA, selfie);

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * Adds a Selfie to the Adapter and notify the change.
     */
    public void add(Selfie selfie) {
        selfieList.add(selfie);
        notifyDataSetChanged();
    }

    /**
     * Removes a Selfie from the Adapter and notify the change.
     */
    public void remove(Selfie selfie) {
        selfieList.remove(selfie);
        notifyDataSetChanged();
    }

    /**
     * Get the List of Selfies from Adapter.
     */
    public List<Selfie> getSelfies() {
        return selfieList;
    }

    /**
     * Set the Adapter to list of Selfies.
     */
    public void setSelfies(List<Selfie> selfies) {
        this.selfieList = selfies;
        notifyDataSetChanged();
    }

    /**
     * Get the no of selfies in adapter.
     */
    public int getCount() {
        return selfieList.size();
    }

    /**
     * Get selfie from a given position.
     */
    public Selfie getItem(int position) {
        return selfieList.get(position);
    }

    /**
     * Get Id of selfie from a given position.
     */
    public long getItemId(int position) {
        return position;
    }

}
