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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Adapters.AdminAdapter.AdminWeaponAdapter;
import com.example.fantazoo_app.Models.WeaponModel;
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
 * Use the {@link WeaponAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeaponAdminFragment extends Fragment implements AdminWeaponAdapter.EditButtonClickListener , AdminWeaponAdapter.DeleteButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<WeaponModel> weapons;

    private AdminWeaponAdapter weaponadapter;
    private int selectedWeaponId;
    public static RequestQueue rq;

    private GridView gridView;
    private EditText weaponNameEditText;
    private EditText weaponDmgEditText;

    public WeaponAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeaponAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeaponAdminFragment newInstance(String param1, String param2) {
        WeaponAdminFragment fragment = new WeaponAdminFragment();
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
        View view = inflater.inflate(R.layout.fragment_weapon_admin, container, false);

        // Initializers
        gridView = view.findViewById(R.id.admin_weapon_list);
        weapons = new ArrayList<>();
        weaponadapter = new AdminWeaponAdapter(getContext(), weapons);
        gridView.setAdapter(weaponadapter);

        // All our edit text initialized
        weaponNameEditText = view.findViewById(R.id.editor_weapon_name);
        weaponDmgEditText = view.findViewById(R.id.editor_weapon_dmg);

        // Gets
        getWeapons();

        // The admin buttons
        weaponadapter.setEditButtonClickListener(this);
        weaponadapter.setDeleteButtonClickListener(this);

        Button saveButton = view.findViewById(R.id.btn_admin_weapon_save);

        // Set OnClickListener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeapon();
            }
        });

        return view;
    }

    // Edit button
    @Override
    public void onEditButtonClick(WeaponModel selectedItem) {
        // Populate the edit views with data from selectedItem
        populateEditViews(selectedItem);

        // Set the selected animal ID
        selectedWeaponId = selectedItem.getId();
    }

    // Delete Button
    @Override
    public void onDeleteButtonClick(WeaponModel selectedItem) {
        // Set the selected animal ID
        deleteWeapon(selectedItem.getId());
    }

    // Method for formatting existing data into our editor
    private void populateEditViews(WeaponModel selectedItem) {

        weaponNameEditText.setText(selectedItem.getName());
        weaponDmgEditText.setText(selectedItem.getDamage());
    }

    // Method to get all the weapons from the server
    public void getWeapons() {
        String url = Host + "/api/wpc";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            String json = response;
            Gson gson = new Gson();

            // Specify the type of the list elements
            Type listType = new TypeToken<ArrayList<WeaponModel>>(){}.getType();
            ArrayList<WeaponModel> weaponModel = gson.fromJson(json, listType);

            if (weaponModel != null) {
                // Add new data to the existing list
                weapons.addAll(weaponModel);
                weaponadapter.notifyDataSetChanged(); // Notify adapter of data change
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

    // Method to save/edit an weapon from the server
    private void saveWeapon() {
        // Retrieve values from views
        String name = weaponNameEditText.getText().toString();
        String dmg = weaponDmgEditText.getText().toString();

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(weaponNameEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(weaponDmgEditText.getWindowToken(), 0);

        // Create JSON object with the cage data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("damage", dmg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Determine the URL and request method based on whether the user is editing an existing weapon or creating a new one
        String url;
        int method;

        if (selectedWeaponId != 0) {
            // Editing existing weapon: use PUT request
            url = Host + "/api/wpc/id/" + selectedWeaponId;
            method = Request.Method.PUT;
        } else {
            // Creating new weapon: use POST request
            url = Host + "/api/wpc";
            method = Request.Method.POST;
        }

        // Create a request
        JsonObjectRequest request = new JsonObjectRequest(method, url, requestBody,
                response -> {
                    // Handle response
                    Log.d("SaveWeapon", "Response: " + response.toString());
                    // Handle success response from server
                },
                error -> {
                    // Handle error
                    Log.e("SaveWeapon", "Error: " + error.toString());
                    // Handle error response from server
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to delete an weapon from the server
    private void deleteWeapon(int cageId) {
        // Construct the URL for deleting the weapon
        String url = Host + "/api/wpc/id/" + cageId;

        // Create a DELETE request using Volley
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Weapon successfully deleted, handle response if needed
                },
                error -> {
                    // Handle error response, if any
                    Log.e("Volley", "Error deleting weapon: " + error.toString());
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to reload the fragment
    private void reloadFragment() {
        getWeapons();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment_container, new WeaponAdminFragment());
        fragmentTransaction.commit();
    }
}