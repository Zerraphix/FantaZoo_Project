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

import com.example.fantazoo_app.Models.AnimalModel;
import com.example.fantazoo_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimalAdapter extends ArrayAdapter<AnimalModel> {

    private Context mContext;
    private ArrayList<AnimalModel> mResults;

    public  AnimalAdapter( Context context, ArrayList<AnimalModel> results){
        super(context,0, results);
        mContext = context;
        mResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new ViewHolder();

            listItemView = inflater.inflate(R.layout.animal_list_item, parent, false);

            viewHolder.textView = listItemView.findViewById(R.id.animalname);
            // viewHolder.imageView = listItemView.findViewById(R.id.img_card);

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        AnimalModel animal = mResults.get(position);

        if (animal.getName() != null) {
            viewHolder.textView.setText(animal.getName());
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
        TextView textView;
        ImageView imageView;
    }

}
