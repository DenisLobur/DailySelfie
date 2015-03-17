package com.example.selfie;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements ICompat {
    private Toolbar toolbar;

    private SelfieListFragment listFragment;
    private SelfieFullFragment fullFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Title");
        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            listFragment = new SelfieListFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.container, listFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void call(int item) {
        fullFragment = SelfieFullFragment.newInstance(item);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fullFragment);
        ft.commit();
    }
}
