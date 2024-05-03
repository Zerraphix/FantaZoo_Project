package com.example.fantazoo_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fantazoo_app.Models.WeaponModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class WeaponSpinnerAdapter extends ArrayAdapter<WeaponModel> {

    public WeaponSpinnerAdapter(Context context, ArrayList<WeaponModel> cages) {
        super(context, 0, cages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        WeaponModel weapon = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.spinner_text);

        // Populate the data into the template view using the data object
        if (weapon != null) {
            tvName.setText(weapon.getName());
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
