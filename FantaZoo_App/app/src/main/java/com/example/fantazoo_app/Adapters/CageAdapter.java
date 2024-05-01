package com.example.fantazoo_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fantazoo_app.Models.CageModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class CageAdapter extends ArrayAdapter<CageModel> {

    private Context mContext;
    private ArrayList<CageModel> mResults;

    public  CageAdapter( Context context, ArrayList<CageModel> results){
        super(context,0, results);
        mContext = context;
        mResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        CageAdapter.ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new CageAdapter.ViewHolder();

            listItemView = inflater.inflate(R.layout.cage_list_item, parent, false);

            viewHolder.textViewName = listItemView.findViewById(R.id.cagename);
            viewHolder.textViewCount = listItemView.findViewById(R.id.animalcount);
            // viewHolder.imageView = listItemView.findViewById(R.id.img_card);

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (CageAdapter.ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        CageModel cage = mResults.get(position);

        if (cage.getName() != null) {
            viewHolder.textViewName.setText(cage.getName());
        }
        if (cage.getAnimals() != null) {
            int animalCount = cage.getAnimals().size();
            String countString = String.valueOf(animalCount);
            viewHolder.textViewCount.setText(countString);
        }
        // Load backdrop image using Picasso
//        if (animal.getBackdrop_path() != null) {
//            String imageUrl = "https://image.tmdb.org/t/p/original/" + result.getPoster_path();
//            Picasso.get().load(imageUrl).into(viewHolder.imageView);
//        }

//        viewHolder.imageView.setOnClickListener(v -> {
//            // Pass the selected movie directly to DetailedMovieFragment.newInstance()
//            DetailedMovieFragment fragment = DetailedMovieFragment.newInstance(result);
//
//            // Get the fragment manager and start the transaction
//            FragmentManager fragmentManager = ((AppCompatActivity) this.mContext).getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .addToBackStack("name")
//                    .commit();
//        });


        return listItemView;
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewCount;
        ImageView imageView;
    }

}