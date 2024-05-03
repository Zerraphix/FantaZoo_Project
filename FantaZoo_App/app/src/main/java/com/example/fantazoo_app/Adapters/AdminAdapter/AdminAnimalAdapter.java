package com.example.fantazoo_app.Adapters.AdminAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fantazoo_app.Models.AnimalModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class AdminAnimalAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AnimalModel> mAnimals;
    private EditButtonClickListener editButtonClickListener;
    private DeleteButtonClickListener deleteButtonClickListener;

    public AdminAnimalAdapter(Context context, ArrayList<AnimalModel> animals) {
        mContext = context;
        mAnimals = animals;
    }

    @Override
    public int getCount() {
        return mAnimals.size();
    }

    @Override
    public Object getItem(int position) {
        return mAnimals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface EditButtonClickListener {
        void onEditButtonClick(AnimalModel selectedItem);
    }

    public void setEditButtonClickListener(EditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    public interface DeleteButtonClickListener {
        void onDeleteButtonClick(AnimalModel selectedItem);
    }

    public void setDeleteButtonClickListener(DeleteButtonClickListener listener) {
        this.deleteButtonClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.admin_animal_list_item, parent, false);
            holder = new ViewHolder();
            holder.idTextView = convertView.findViewById(R.id.animal_id);
            holder.nameTextView = convertView.findViewById(R.id.animal_name);
            holder.ageTextView = convertView.findViewById(R.id.animal_age);
            holder.genderTextView = convertView.findViewById(R.id.animal_gender);
            holder.cageTextView = convertView.findViewById(R.id.animal_cage);
            holder.editButton = convertView.findViewById(R.id.edit);
            holder.deleteButton = convertView.findViewById(R.id.delete);
            convertView.setTag(holder);

            // Set click listener for edit button
            holder.editButton.setOnClickListener(v -> {
                if (editButtonClickListener != null) {
                    AnimalModel selectedItem = mAnimals.get(position);
                    editButtonClickListener.onEditButtonClick(selectedItem);
                }
            });

            // Set click listener for delete button
            holder.deleteButton.setOnClickListener(v -> {
                if (deleteButtonClickListener != null) {
                    AnimalModel selectedItem = mAnimals.get(position);
                    deleteButtonClickListener.onDeleteButtonClick(selectedItem);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AnimalModel animal = mAnimals.get(position);
        holder.idTextView.setText(String.valueOf(animal.getId()));
        holder.nameTextView.setText(animal.getName());
        holder.ageTextView.setText(String.valueOf(animal.getAge()));
        holder.genderTextView.setText(animal.getGender().toString());

        if (animal.getCage() != null && animal.getCage().getName() != null) {
            holder.cageTextView.setText(animal.getCage().getName());
        }
        else {
            holder.cageTextView.setText("No Cage");
        }

        // Implement click listeners for edit and delete buttons if needed
        holder.deleteButton.setTag(position);
        return convertView;
    }

    private static class ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView ageTextView;
        TextView genderTextView;
        TextView cageTextView;
        ImageButton editButton;
        ImageButton deleteButton;
    }
}
