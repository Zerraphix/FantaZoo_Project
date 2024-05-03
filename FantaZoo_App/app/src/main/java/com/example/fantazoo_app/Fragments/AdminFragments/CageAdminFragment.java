package com.example.fantazoo_app.Fragments.AdminFragments;

import static com.example.fantazoo_app.Secrets.Host;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Adapters.AdminAdapter.AdminAnimalAdapter;
import com.example.fantazoo_app.Adapters.AdminAdapter.AdminCageAdapter;
import com.example.fantazoo_app.Adapters.CageSpinnerAdapter;
import com.example.fantazoo_app.Extra.Gender;
import com.example.fantazoo_app.Models.AnimalModel;
import com.example.fantazoo_app.Models.CageModel;
import com.example.fantazoo_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CageAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CageAdminFragment extends Fragment  implements AdminCageAdapter.EditButtonClickListener , AdminCageAdapter.DeleteButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<CageModel> cages;

    private AdminCageAdapter cageadapter;
    private int selectedCageId;
    public static RequestQueue rq;

    private GridView gridView;
    private EditText cageNameEditText;

    public CageAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CageAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CageAdminFragment newInstance(String param1, String param2) {
        CageAdminFragment fragment = new CageAdminFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cage_admin, container, false);

        // Initializers
        gridView = view.findViewById(R.id.admin_cage_list);
        cages = new ArrayList<>();
        cageadapter = new AdminCageAdapter(getContext(), cages);
        gridView.setAdapter(cageadapter);

        // All our edit text initialized
        cageNameEditText = view.findViewById(R.id.editor_cage_name);

        // Gets
        getCages();

        // The admin buttons
        cageadapter.setEditButtonClickListener(this);
        cageadapter.setDeleteButtonClickListener(this);

        Button saveButton = view.findViewById(R.id.btn_admin_cage_save);

        // Set OnClickListener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCage();
            }
        });

        return view;
    }

    // Edit button
    @Override
    public void onEditButtonClick(CageModel selectedItem) {
        // Populate the edit views with data from selectedItem
        populateEditViews(selectedItem);

        // Set the selected animal ID
        selectedCageId = selectedItem.getId();
    }

    // Delete Button
    @Override
    public void onDeleteButtonClick(CageModel selectedItem) {
        // Set the selected animal ID
        deleteCage(selectedItem.getId());
    }

    // Method for formatting existing data into our editor
    private void populateEditViews(CageModel selectedItem) {

        cageNameEditText.setText(selectedItem.getName());
    }

    // Method to get all the cages from the server
    public void getCages() {
        String url = Host + "/api/cc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            Gson gson = new Gson();

            // Specify the type of the list elements
            Type listType = new TypeToken<ArrayList<CageModel>>(){}.getType();
            ArrayList<CageModel> cageModel = gson.fromJson(json, listType);

            if (cageModel != null) {
                // Add new data to the existing list
                cages.addAll(cageModel);
                cageadapter.notifyDataSetChanged(); // Notify adapter of data change
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

    // Method to save/edit an cage from the server
    private void saveCage() {
        // Retrieve values from views
        String name = cageNameEditText.getText().toString();

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cageNameEditText.getWindowToken(), 0);

        // Create JSON object with the cage data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Determine the URL and request method based on whether the user is editing an existing cage or creating a new one
        String url;
        int method;

        if (selectedCageId != 0) {
            // Editing existing cage: use PUT request
            url = Host + "/api/cc/id/" + selectedCageId;
            method = Request.Method.PUT;
        } else {
            // Creating new cage: use POST request
            url = Host + "/api/cc";
            method = Request.Method.POST;
        }

        // Create a request
        JsonObjectRequest request = new JsonObjectRequest(method, url, requestBody,
                response -> {
                    // Handle response
                    Log.d("SaveCage", "Response: " + response.toString());
                    // Handle success response from server
                },
                error -> {
                    // Handle error
                    Log.e("SaveCage", "Error: " + error.toString());
                    // Handle error response from server
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to delete an cage from the server
    private void deleteCage(int cageId) {
        // Construct the URL for deleting the cage
        String url = Host + "/api/cc/id/" + cageId;

        // Create a DELETE request using Volley
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Cage successfully deleted, handle response if needed
                },
                error -> {
                    // Handle error response, if any
                    Log.e("Volley", "Error deleting cage: " + error.toString());
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to reload the fragment
    private void reloadFragment() {
        getCages();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment_container, new CageAdminFragment());
        fragmentTransaction.commit();
    }
}