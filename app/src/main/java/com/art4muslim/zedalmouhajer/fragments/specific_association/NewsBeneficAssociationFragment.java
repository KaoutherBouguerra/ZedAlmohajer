package com.art4muslim.zedalmouhajer.fragments.specific_association;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;

import static com.art4muslim.zedalmouhajer.session.SessionManager.KEY_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsBeneficAssociationFragment extends Fragment {

    private static final String TAG = NewsBeneficAssociationFragment.class.getSimpleName();
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    BaseApplication baseApplication;
    View v;
    Association association;
    AssociationBeneficiaryFragment fragmentBen;
    AssociationNewsFragment fragmentNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_news_benefic_association, container, false);
        baseApplication = (BaseApplication)getActivity().getApplicationContext();

        if (BaseApplication.session.getIsFrom().equals(Constants.CONSTANT_ASSOCIATION))
            association = baseApplication.getAssociation();
        else association = (Association) getArguments().getSerializable("ASSOCIATION");


        fragmentNews = new AssociationNewsFragment();

        Bundle args = new Bundle();
        args.putSerializable("ASSOCIATION", association);

        fragmentNews.setArguments(args);


        fragmentBen = new AssociationBeneficiaryFragment();
        Bundle argsBen = new Bundle();
        argsBen.putSerializable("ASSOCIATION", association);
        fragmentBen.setArguments(argsBen);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
      //  isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        if (baseApplication.getAssociation() != null)
        mTitle.setText(baseApplication.getAssociation().getName());
        else  mTitle.setText(BaseApplication.session.getUserDetails().get(KEY_NAME));
        ImageView img_back = (ImageView)   toolbar.getRootView().findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                getActivity().onBackPressed();
                Log.e(TAG," img back clicked");
            }
        });

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
                case 0 : return fragmentNews;
                case 1 : return fragmentBen;

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
                    return getResources().getString(R.string.news_ass);
                case 1 :
                    return getResources().getString(R.string.beneficiaries);
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
