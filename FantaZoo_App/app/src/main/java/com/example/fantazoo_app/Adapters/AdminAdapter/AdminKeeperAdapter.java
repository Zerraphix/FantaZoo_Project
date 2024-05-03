package com.example.fantazoo_app.Adapters.AdminAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fantazoo_app.Models.ZookeeperModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class AdminKeeperAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ZookeeperModel> mKeepers;
    private AdminKeeperAdapter.EditButtonClickListener editButtonClickListener;
    private AdminKeeperAdapter.DeleteButtonClickListener deleteButtonClickListener;

    public AdminKeeperAdapter(Context context, ArrayList<ZookeeperModel> keepers) {
        mContext = context;
        mKeepers = keepers;
    }

    @Override
    public int getCount() {
        return mKeepers.size();
    }

    @Override
    public Object getItem(int position) {
        return mKeepers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface EditButtonClickListener {
        void onEditButtonClick(ZookeeperModel selectedItem);
    }

    public void setEditButtonClickListener(AdminKeeperAdapter.EditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(ZookeeperModel selectedItem);
    }

    public void setDeleteButtonClickListener(AdminKeeperAdapter.DeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdminKeeperAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.admin_keeper_list_item, parent, false);
            holder = new AdminKeeperAdapter.ViewHolder();
            holder.idTextView = convertView.findViewById(R.id.keeper_id);
            holder.nameTextView = convertView.findViewById(R.id.keeper_name);
            holder.cageTextView = convertView.findViewById(R.id.keeper_cage);
            holder.weaponTextView = convertView.findViewById(R.id.keeper_weapon);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);

            // Set click listener for edit button
            holder.editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    ZookeeperModel selectedItem = mKeepers.get(position);
                    editButtonClickListener.onEditButtonClick(selectedItem);
                }
            });

            // Set click listener for delete button
            holder.deleteButton.setOnClickListener(v -> {
                if (deleteButtonClickListener != null) {
                    ZookeeperModel selectedItem = mKeepers.get(position);
                    deleteButtonClickListener.onDeleteButtonClick(selectedItem);
                }
            });
        } else {
            holder = (AdminKeeperAdapter.ViewHolder) convertView.getTag();
        }

        ZookeeperModel keeper = mKeepers.get(position);
        holder.idTextView.setText(String.valueOf(keeper.getId()));
        holder.nameTextView.setText(keeper.getName());

        if (keeper.getCage() != null) {
            holder.cageTextView.setText(keeper.getCage().getName());
        } else {
            holder.cageTextView.setText("No Cage");
        }

        if (keeper.getWeapon() != null) {
            holder.weaponTextView.setText(keeper.getWeapon().getName());
        } else {
            holder.weaponTextView.setText("No Weapon");
        }

        // Implement click listeners for edit and delete buttons if needed
        holder.deleteButton.setTag(position);
        return convertView;
    }

    private static class ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView cageTextView;
        TextView weaponTextView;
        ImageButton editButton;
        ImageButton deleteButton;
    }
}

