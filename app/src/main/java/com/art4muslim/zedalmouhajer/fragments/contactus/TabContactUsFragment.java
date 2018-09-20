package com.art4muslim.zedalmouhajer.fragments.contactus;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.fragments.CommunicateWithAssociationFragment;

public class TabContactUsFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    boolean isRightToLeft  ;
    String from = "ASSOCIATION";
    CommunicateWithAssociationFragment communicateWithAssociationFragment;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_contact_us, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
//        isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        mTitle.setText( R.string.item_contact);
        communicateWithAssociationFragment = new CommunicateWithAssociationFragment();
        if (getArguments() != null) {
            from = getArguments().getString("FROM");
            Bundle bundle = new Bundle();
            bundle.putString("FROM",from);
             communicateWithAssociationFragment.setArguments(bundle);



        }
        Log.e("TAB CONTACT"," from what? "+from);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return v;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return communicateWithAssociationFragment;
                case 1 : return new TabFragment1();
                case 2 : return new TabFragment2();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return getResources().getString(R.string.Tab3_inquire);
                case 1 :
                    return getResources().getString(R.string.Tab2_suggest);
                case 2 :
                    return getResources().getString(R.string.Tab1_complain);
            }
            return null;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
      /*  menu.findItem(R.id.item_filter).setVisible(false);
        menu.findItem(R.id.item_attach).setVisible(false);
        if (!isRightToLeft ) {
            menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.backright));
        }else  menu.findItem(R.id.item_back).setIcon(getResources().getDrawable(R.mipmap.back));

        menu.findItem(R.id.item_back).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                MyInboxFragment schedule1 = new MyInboxFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame,schedule1,"home Fragment");
                fragmentTransaction.commit();
                return false;
            }
        });
        */
        super.onPrepareOptionsMenu(menu);
    }

}
