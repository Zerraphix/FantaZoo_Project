package com.example.fantazoo_app.Fragments;

import static com.example.fantazoo_app.Secrets.Host;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Adapters.AnimalAdapter;
import com.example.fantazoo_app.Models.AnimalModel;
import com.example.fantazoo_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnimalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimalsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private ArrayList<AnimalModel> animals;
    private AnimalAdapter adapter;
    public static RequestQueue rq;

    public AnimalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnimalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnimalsFragment newInstance(String param1, String param2) {
        AnimalsFragment fragment = new AnimalsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        rq = Volley.newRequestQueue(requireContext());
        getAnimals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals, container, false);

        gridView = view.findViewById(R.id.animal_list);
        animals = new ArrayList<>();
        adapter = new AnimalAdapter(getContext(), animals);
        gridView.setAdapter(adapter);



        return view;
    }

    public void getAnimals() {
        String url = Host + "/api/ac";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            Gson gson = new Gson();

            // Specify the type of the list elements
            Type listType = new TypeToken<ArrayList<AnimalModel>>(){}.getType();
            ArrayList<AnimalModel> animalModel = gson.fromJson(json, listType);

            if (animalModel != null) {
                // Add new data to the existing list
                animals.addAll(animalModel);
                adapter.notifyDataSetChanged(); // Notify adapter of data change
            }
        }, error -> Log.e("Volley", error.toString()))
        {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<>();
                return params;
            }
        };
        rq.add(request);
    }
}