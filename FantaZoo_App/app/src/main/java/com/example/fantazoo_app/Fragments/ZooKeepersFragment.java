package com.example.fantazoo_app.Fragments;

import static com.example.fantazoo_app.Secrets.Host;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Adapters.KeeperAdapter;
import com.example.fantazoo_app.Models.ZookeeperModel;
import com.example.fantazoo_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ZooKeepersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZooKeepersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GridView gridView;
    private ArrayList<ZookeeperModel> keepers;
    private KeeperAdapter adapter;
    public static RequestQueue rq;

    public ZooKeepersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZooKeepersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZooKeepersFragment newInstance(String param1, String param2) {
        ZooKeepersFragment fragment = new ZooKeepersFragment();
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
        getKeepers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoo_keepers, container, false);

        gridView = view.findViewById(R.id.keeper_list);
        keepers = new ArrayList<>();
        adapter = new KeeperAdapter(getContext(), keepers);
        gridView.setAdapter(adapter);



        return view;
    }

    public void getKeepers() {
        String url = Host + "/api/zkc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            Gson gson = new Gson();

            // Specify the type of the list elements
            Type listType = new TypeToken<ArrayList<ZookeeperModel>>(){}.getType();
            ArrayList<ZookeeperModel> keeperModel = gson.fromJson(json, listType);

            if (keeperModel != null) {
                // Add new data to the existing list
                keepers.addAll(keeperModel);
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