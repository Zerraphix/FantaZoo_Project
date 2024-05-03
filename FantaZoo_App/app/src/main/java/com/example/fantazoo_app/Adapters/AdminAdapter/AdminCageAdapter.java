package com.example.fantazoo_app.Adapters.AdminAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fantazoo_app.Models.CageModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class AdminCageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CageModel> mCages;
    private AdminCageAdapter.EditButtonClickListener editButtonClickListener;
    private AdminCageAdapter.DeleteButtonClickListener deleteButtonClickListener;

    public AdminCageAdapter(Context context, ArrayList<CageModel> cages) {
        mContext = context;
        mCages = cages;
    }

    @Override
    public int getCount() {
        return mCages.size();
    }

    @Override
    public Object getItem(int position) {
        return mCages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface EditButtonClickListener {
        void onEditButtonClick(CageModel selectedItem);
    }

    public void setEditButtonClickListener(AdminCageAdapter.EditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(CageModel selectedItem);
    }

    public void setDeleteButtonClickListener(AdminCageAdapter.DeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdminCageAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.admin_cage_list_item, parent, false);
            holder = new AdminCageAdapter.ViewHolder();
            holder.idTextView = convertView.findViewById(R.id.cage_id);
            holder.nameTextView = convertView.findViewById(R.id.cage_name);
            holder.keeperTextView = convertView.findViewById(R.id.cage_keeper);
            holder.animalTextView = convertView.findViewById(R.id.cage_animal);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);

            // Set click listener for edit button
            holder.editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    CageModel selectedItem = mCages.get(position);
                    editButtonClickListener.onEditButtonClick(selectedItem);
                }
            });

            // Set click listener for delete button
            holder.deleteButton.setOnClickListener(v -> {
                if (deleteButtonClickListener != null) {
                    CageModel selectedItem = mCages.get(position);
                    deleteButtonClickListener.onDeleteButtonClick(selectedItem);
                }
            });
        } else {
            holder = (AdminCageAdapter.ViewHolder) convertView.getTag();
        }

        CageModel cage = mCages.get(position);
        holder.idTextView.setText(String.valueOf(cage.getId()));
        holder.nameTextView.setText(cage.getName());

        if (cage.getZookeepers() != null) {
            int keeperCount = cage.getZookeepers().size();
            String countString = String.valueOf(keeperCount);
            holder.keeperTextView.setText(countString);
        } else {
            holder.keeperTextView.setText("0");
        }

        if (cage.getAnimals() != null) {
            int animalCount = cage.getAnimals().size();
            String countString = String.valueOf(animalCount);
            holder.animalTextView.setText(countString);
        } else {
            holder.animalTextView.setText("0");
        }

        // Implement click listeners for edit and delete buttons if needed
        holder.deleteButton.setTag(position);
        return convertView;
    }

    private static class ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView keeperTextView;
        TextView animalTextView;
        ImageButton editButton;
        ImageButton deleteButton;
    }
}
