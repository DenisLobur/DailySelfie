package com.example.selfie;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.selfie.model.mediator.webdata.Selfie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 3/17/15.
 */
public class SelfieListFragment extends Fragment {

    private static final String TAG = SelfieListFragment.class.getSimpleName();

    private ListView listView;
    private SelfieListAdapter adapter;
    private List<Selfie> stubList;

    private ICompat callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            //callback = (MainActivity) activity;
        } catch (ClassCastException cce) {
            Log.d(TAG, "activity " + activity.getClass().getSimpleName() + " must implement " + ICompat.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle bundle = getArguments();
        //String bitmapPath = bundle.getString("path");
        stubList = new ArrayList<Selfie>();
        //stubList.add(new Selfie("one", "path2"));
        //stubList.add(new Selfie("two", "path2"));
        //adapter = new SelfieListAdapter(getActivity(), stubList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_fragment, container, false);
        listView = (ListView) root.findViewById(R.id.photos_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.call(position);
            }
        });


        return root;
    }

    public SelfieListAdapter getAdapter() {
        return adapter;
    }

}
