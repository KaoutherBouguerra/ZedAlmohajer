package com.art4muslim.zedalmouhajer.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.models.Association;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.art4muslim.zedalmouhajer.session.Constants.baseUrlImages;


/**
 * Created by macbook on 29/12/2017.
 */

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    String languageToLoad;
    ArrayList<Association> associations;
    FragmentTransaction fragmentTransaction;
    public CustomGrid(Context c, ArrayList<Association> associations, FragmentTransaction fragmentTransaction ) {
        mContext = c;
        this.associations = associations;
        this.fragmentTransaction = fragmentTransaction;
      //  languageToLoad = BaseApplication.session.getKey_LANGUAGE();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return associations.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Association cat = associations.get(position);
        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);

            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
          //  imageView.setImageResource(cat.getImage());

            Picasso.with(mContext)
                    .load(cat.getImage())
                    .fit()
                    .into(imageView);

            assert convertView != null;

        } else {
            grid = (View) convertView;
        }




        return grid;
    }
}
