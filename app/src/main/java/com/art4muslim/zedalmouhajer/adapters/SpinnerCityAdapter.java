package com.art4muslim.zedalmouhajer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.models.City;

import java.util.ArrayList;



/**
 * Created by kaoutherbouguerra on 27/01/2018.
 */

public class SpinnerCityAdapter extends ArrayAdapter<City> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<City> values;

    public SpinnerCityAdapter(Context context, int textViewResourceId,
                              ArrayList<City> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;

    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public City getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);

        label.setTextColor(Color.BLACK);

        label.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);


        label.setText(values.get(position).getName());

        return label;
    }
}
