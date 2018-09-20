package com.art4muslim.zedalmouhajer.features.register;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.art4muslim.zedalmouhajer.R;

public class RegisterActivity extends AppCompatActivity {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        mTitle.setText( R.string.register);
        tabLayout = (TabLayout)  findViewById(R.id.tabs);

        viewPager = (ViewPager)  findViewById(R.id.viewpager);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

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

    }

    class MyAdapter extends FragmentStatePagerAdapter {

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
                case 0 : return new AssociationFragment();
                case 1 : return new BeneficiaryFragment();
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
                    return  getResources().getString(R.string.association);
                case 1 :
                    return getResources().getString(R.string.benefis);

            }
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
