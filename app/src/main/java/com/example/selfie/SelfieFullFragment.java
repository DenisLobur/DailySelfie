package com.example.selfie;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by denis on 3/17/15.
 */
public class SelfieFullFragment extends Fragment {

    private ImageView fullPic;

    public static SelfieFullFragment newInstance(int index){
        SelfieFullFragment frag = new SelfieFullFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.full_fragment, container, false);
        fullPic = (ImageView) root.findViewById(R.id.full_image);


        return root;
    }


}
