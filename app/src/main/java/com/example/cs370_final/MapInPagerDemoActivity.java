package com.example.cs370_final;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This shows how to add a map to a ViewPager. Note the use of
 * {@link ViewGroup#requestTransparentRegion(View)} to reduce jankiness.
 */
public class MapInPagerDemoActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private MyAdapter mAdapter;

    private ViewPager mPager;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_in_pager_demo);
        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mPager);
    }



    /** A simple fragment that displays a TextView. */
    public static class TextFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
            return inflater.inflate(R.layout.settings_fragment, container, false);
        }

    }

    /** Fragment for list view **/
    public class ListDisplay extends ListFragment {
        //implements AdapterView.OnItemClickListener
        String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
                "WebOS","Ubuntu","Windows7","Max OS X"};

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, mobileArray);
            setListAdapter(listAdapter);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
            return inflater.inflate(R.layout.list_fragment, container, false);
        }


        @Override
        public void onListItemClick(ListView list, View v, int position, long id) {
            Toast.makeText(getActivity(),
                    getListView().getItemAtPosition(position).toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    //TODO add classes for recycler view and list

    /** A simple FragmentPagerAdapter that returns two TextFragment and a SupportMapFragment. */
    public class MyAdapter extends FragmentPagerAdapter {

        @StringRes
        private final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(TAB_TITLES[position]);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TextFragment();
                case 1:
                    enableMyLocation();
                    MapsActivity fragment = new MapsActivity();
                    return fragment;
                case 2:

                    return new TextFragment();
                default:
                    return null;
            }
        }
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else  {
            // Access to the location has been granted to the app.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}

