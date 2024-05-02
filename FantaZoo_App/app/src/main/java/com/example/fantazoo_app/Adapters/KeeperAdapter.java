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

import com.example.fantazoo_app.Models.ZookeeperModel;
import com.example.fantazoo_app.R;

import java.util.ArrayList;

public class KeeperAdapter extends ArrayAdapter<ZookeeperModel> {

    private Context mContext;
    private ArrayList<ZookeeperModel> mResults;

    public  KeeperAdapter( Context context, ArrayList<ZookeeperModel> results){
        super(context,0, results);
        mContext = context;
        mResults = results;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        KeeperAdapter.ViewHolder viewHolder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            viewHolder = new KeeperAdapter.ViewHolder();

            listItemView = inflater.inflate(R.layout.zookeeper_list_item, parent, false);

            viewHolder.textViewName = listItemView.findViewById(R.id.keepername);
            viewHolder.textViewCage = listItemView.findViewById(R.id.cage);
            viewHolder.textViewWeapon = listItemView.findViewById(R.id.weapon);
            // viewHolder.imageView = listItemView.findViewById(R.id.img_card);

            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (KeeperAdapter.ViewHolder) listItemView.getTag();
        }

        // Bind data to your grid item layout here
        ZookeeperModel keeper = mResults.get(position);

        if (keeper.getName() != null) {
            viewHolder.textViewName.setText(keeper.getName());
        }
        if (keeper.getCage() != null) {
            viewHolder.textViewCage.setText(keeper.getCage().getName());
        }
        else {
            viewHolder.textViewCage.setText("No Cage");
        }
        if (keeper.getWeapon() != null) {
            viewHolder.textViewWeapon.setText(keeper.getWeapon().getName());
        }
        else {
            viewHolder.textViewWeapon.setText("No Weapon");
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
        TextView textViewCage;
        TextView textViewWeapon;
        ImageView imageView;
    }

}