package com.example.fantazoo_app.Adapters.AdminAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fantazoo_app.Models.WeaponModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class AdminWeaponAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<WeaponModel> mWeapons;
    private AdminWeaponAdapter.EditButtonClickListener editButtonClickListener;
    private AdminWeaponAdapter.DeleteButtonClickListener deleteButtonClickListener;

    public AdminWeaponAdapter(Context context, ArrayList<WeaponModel> weapons) {
        mContext = context;
        mWeapons = weapons;
    }

    @Override
    public int getCount() {
        return mWeapons.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeapons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface EditButtonClickListener {
        void onEditButtonClick(WeaponModel selectedItem);
    }

    public void setEditButtonClickListener(AdminWeaponAdapter.EditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(WeaponModel selectedItem);
    }

    public void setDeleteButtonClickListener(AdminWeaponAdapter.DeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdminWeaponAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.admin_weapon_list_item, parent, false);
            holder = new AdminWeaponAdapter.ViewHolder();
            holder.idTextView = convertView.findViewById(R.id.weapon_id);
            holder.nameTextView = convertView.findViewById(R.id.weapon_name);
            holder.dmgTextView = convertView.findViewById(R.id.weapon_dmg);
            holder.keeperTextView = convertView.findViewById(R.id.weapon_keeper);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);

            // Set click listener for edit button
            holder.editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    WeaponModel selectedItem = mWeapons.get(position);
                    editButtonClickListener.onEditButtonClick(selectedItem);
                }
            });

            // Set click listener for delete button
            holder.deleteButton.setOnClickListener(v -> {
                if (deleteButtonClickListener != null) {
                    WeaponModel selectedItem = mWeapons.get(position);
                    deleteButtonClickListener.onDeleteButtonClick(selectedItem);
                }
            });
        } else {
            holder = (AdminWeaponAdapter.ViewHolder) convertView.getTag();
        }

        WeaponModel weapon = mWeapons.get(position);
        holder.idTextView.setText(String.valueOf(weapon.getId()));
        holder.nameTextView.setText(weapon.getName());
        holder.dmgTextView.setText(weapon.getDamage());

        if (weapon.getZookeepers() != null) {
            int keeperCount = weapon.getZookeepers().size();
            String countString = String.valueOf(keeperCount);
            holder.keeperTextView.setText(countString);
        } else {
            holder.keeperTextView.setText("0");
        }


        // Implement click listeners for edit and delete buttons if needed
        holder.deleteButton.setTag(position);
        return convertView;
    }

    private static class ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView dmgTextView;
        TextView keeperTextView;
        ImageButton editButton;
        ImageButton deleteButton;
    }
}