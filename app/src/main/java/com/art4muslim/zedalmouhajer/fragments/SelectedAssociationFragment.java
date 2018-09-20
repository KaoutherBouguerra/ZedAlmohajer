package com.art4muslim.zedalmouhajer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.features.LoginActivity;
import com.art4muslim.zedalmouhajer.fragments.specific_association.AssociationBeneficiaryFragment;
import com.art4muslim.zedalmouhajer.fragments.specific_association.NewsBeneficAssociationFragment;
import com.art4muslim.zedalmouhajer.models.Association;
import com.squareup.picasso.Picasso;

import static com.art4muslim.zedalmouhajer.session.SessionManager.KEY_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedAssociationFragment extends Fragment {


    View v;

    LinearLayout linearAbout, linearBen, linearContact, linearRegister;
    ImageView imgAss;
    TextView txtNameAss;

    Association association;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_selected_association, container, false);
        association = (Association) getArguments().getSerializable("ASSOCIATION");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        mTitle.setText(association.getName());

        init();
        Picasso.with(getActivity()).load(association.getImage())
                .fit()
                .into(imgAss);

        txtNameAss.setText(association.getName());

        linearAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutAssociationFragment aboutAssociationFragment = new AboutAssociationFragment();
                showFragment(aboutAssociationFragment);
            }
        });

        linearBen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo check if user connected
                Log.e("selected ass"," is logged = "+BaseApplication.session.isLoggedIn());
                if (BaseApplication.session.isLoggedIn()) {
                    NewsBeneficAssociationFragment schedule = new NewsBeneficAssociationFragment();
                    showFragment(schedule);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                }
             //   AssociationBeneficiaryFragment associationBeneficiaryFragment = new AssociationBeneficiaryFragment();
              //  showFragment(associationBeneficiaryFragment);
            }
        });

        linearContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunicateWithAssociationFragment associationBeneficiaryFragment = new CommunicateWithAssociationFragment();
                showFragment(associationBeneficiaryFragment);


            }
        });

        linearRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  RegisterToAssociationFragment associationBeneficiaryFragment = new RegisterToAssociationFragment();
              // showFragment(associationBeneficiaryFragment);



            }
        });

        return v;
    }

    private void init() {
        linearAbout = v.findViewById(R.id.linearAbout);
        linearBen = v.findViewById(R.id.linearBen);
        linearContact = v.findViewById(R.id.linearContact);
        linearRegister = v.findViewById(R.id.linearRegister);
        imgAss = v.findViewById(R.id.ass_profile_photo);
        txtNameAss = v.findViewById(R.id.txtNameAss);


    }
    private void showFragment(Fragment fragment) {

        Bundle args = new Bundle();
        args.putSerializable("ASSOCIATION", association);
        args.putString("FROM","BEN");
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, "home Fragment");

        fragmentTransaction.commit();
    }
}
