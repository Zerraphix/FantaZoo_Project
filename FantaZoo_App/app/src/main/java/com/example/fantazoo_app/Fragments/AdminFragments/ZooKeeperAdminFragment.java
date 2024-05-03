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
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Adapters.AdminAdapter.AdminKeeperAdapter;
import com.example.fantazoo_app.Adapters.CageSpinnerAdapter;
import com.example.fantazoo_app.Adapters.WeaponSpinnerAdapter;
import com.example.fantazoo_app.Models.CageModel;
import com.example.fantazoo_app.Models.WeaponModel;
import com.example.fantazoo_app.Models.ZookeeperModel;
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
 * Use the {@link ZooKeeperAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZooKeeperAdminFragment extends Fragment implements AdminKeeperAdapter.EditButtonClickListener , AdminKeeperAdapter.DeleteButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<CageModel> cages;
    private CageSpinnerAdapter cageadapter;
    private ArrayList<WeaponModel> weapons;
    private WeaponSpinnerAdapter weaponadapter;

    private ArrayList<ZookeeperModel> keepers;

    private AdminKeeperAdapter keeperadapter;
    private int selectedKeeperId;
    public static RequestQueue rq;

    private GridView gridView;
    private EditText keeperNameEditText;
    private Spinner keeperCageSpinner;
    private Spinner keeperWeaponSpinner;

    public ZooKeeperAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZooKeeperAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZooKeeperAdminFragment newInstance(String param1, String param2) {
        ZooKeeperAdminFragment fragment = new ZooKeeperAdminFragment();
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
        View view = inflater.inflate(R.layout.fragment_zoo_keeper_admin, container, false);

        // Initializers
        gridView = view.findViewById(R.id.admin_keeper_list);
        keepers = new ArrayList<>();
        keeperadapter = new AdminKeeperAdapter(getContext(), keepers);
        gridView.setAdapter(keeperadapter);

        // All our edit text initialized
        keeperNameEditText = view.findViewById(R.id.editor_keeper_name);
        keeperCageSpinner = view.findViewById(R.id.editor_keeper_cage);
        cages = new ArrayList<>();
        cageadapter = new CageSpinnerAdapter(getContext(), cages);
        keeperCageSpinner.setAdapter(cageadapter);
        keeperWeaponSpinner = view.findViewById(R.id.editor_keeper_weapon);
        weapons = new ArrayList<>();
        weaponadapter = new WeaponSpinnerAdapter(getContext(), weapons);
        keeperWeaponSpinner.setAdapter(weaponadapter);

        // Gets
        getKeepers();
        getCages();
        getWeapons();

        // The admin buttons
        keeperadapter.setEditButtonClickListener(this);
        keeperadapter.setDeleteButtonClickListener(this);

        Button saveButton = view.findViewById(R.id.btn_admin_keeper_save);

        // Set OnClickListener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveKeeper();
            }
        });

        return view;
    }

    // Edit button
    @Override
    public void onEditButtonClick(ZookeeperModel selectedItem) {
        // Populate the edit views with data from selectedItem
        populateEditViews(selectedItem);

        // Set the selected animal ID
        selectedKeeperId = selectedItem.getId();
    }

    // Delete Button
    @Override
    public void onDeleteButtonClick(ZookeeperModel selectedItem) {
        // Set the selected animal ID
        deleteKeeper(selectedItem.getId());
    }

    // Method for formatting existing data into our editor
    private void populateEditViews(ZookeeperModel selectedItem) {

        keeperNameEditText.setText(selectedItem.getName());

        // Find the position of the cage in the list of cages
        CageModel selectedCage = selectedItem.getCage();
        int cagePosition = getCagePosition(selectedCage);

        // Set the cage spinner to the position of the selected cage
        keeperCageSpinner.setSelection(cagePosition);

        // Find the position of the weapon in the list of weapons
        WeaponModel selectedWeapon = selectedItem.getWeapon();
        int weaponPosition = getWeaponPosition(selectedWeapon);

        // Set the cage spinner to the position of the selected cage
        keeperWeaponSpinner.setSelection(weaponPosition);
    }

    // Method to get the cage position
    private int getCagePosition(CageModel selectedCage) {
        if (selectedCage != null) {
            for (int i = 0; i < cages.size(); i++) {
                CageModel cage = cages.get(i);
                if (cage != null && cage.getId() == selectedCage.getId()) {
                    return i;
                }
            }
        }
        return 0; // Default to the first position if not found
    }

    // Method to get the weapon position
    private int getWeaponPosition(WeaponModel selectedWeapon) {
        if (selectedWeapon != null) {
            for (int i = 0; i < weapons.size(); i++) {
                WeaponModel weapon = weapons.get(i);
                if (weapon != null && weapon.getId() == selectedWeapon.getId()) {
                    return i;
                }
            }
        }
        return 0; // Default to the first position if not found
    }

    // Method to get all the zoo keepers from the server
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
                keeperadapter.notifyDataSetChanged(); // Notify adapter of data change
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

    // Method to save/edit an keeper from the server
    private void saveKeeper() {
        // Retrieve values from views
        String name = keeperNameEditText.getText().toString();
        CageModel selectedCage = (CageModel) keeperCageSpinner.getSelectedItem();
        WeaponModel selectedWeapon = (WeaponModel) keeperWeaponSpinner.getSelectedItem();


        int cageId = 0;
        if (selectedCage != null){
            cageId = selectedCage.getId();
        }
        int weaponId = 0;
        if (selectedWeapon != null){
            weaponId = selectedWeapon.getId();
        }

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(keeperNameEditText.getWindowToken(), 0);

        // Create JSON object with the keeper data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            if (cageId != 0) {
                JSONObject cageObject = new JSONObject();
                cageObject.put("id", cageId);
                requestBody.put("cage", cageObject);
            }
            if (weaponId != 0) {
                JSONObject cageObject = new JSONObject();
                cageObject.put("id", weaponId);
                requestBody.put("weapon", cageObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Determine the URL and request method based on whether the user is editing an existing cage or creating a new one
        String url;
        int method;

        if (selectedKeeperId != 0) {
            // Editing existing keeper: use PUT request
            url = Host + "/api/zkc/id/" + selectedKeeperId;
            method = Request.Method.PUT;
        } else {
            // Creating new keeper: use POST request
            url = Host + "/api/zkc";
            method = Request.Method.POST;
        }

        // Create a request
        JsonObjectRequest request = new JsonObjectRequest(method, url, requestBody,
                response -> {
                    // Handle response
                    Log.d("SaveKeeper", "Response: " + response.toString());
                    // Handle success response from server
                },
                error -> {
                    // Handle error
                    Log.e("SaveKeeper", "Error: " + error.toString());
                    // Handle error response from server
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to delete an keeper from the server
    private void deleteKeeper(int cageId) {
        // Construct the URL for deleting the animal
        String url = Host + "/api/zkc/id/" + cageId;

        // Create a DELETE request using Volley
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Keeper successfully deleted, handle response if needed
                },
                error -> {
                    // Handle error response, if any
                    Log.e("Volley", "Error deleting keeper: " + error.toString());
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to get all the keepers from the server
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

    // Method to get all the keeepers from the server
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

    // Method to reload the fragment
    private void reloadFragment() {
        getKeepers();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment_container, new ZooKeeperAdminFragment());
        fragmentTransaction.commit();
    }
}