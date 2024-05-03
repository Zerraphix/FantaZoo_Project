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
 * Use the {@link AnimalAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnimalAdminFragment extends Fragment implements AdminAnimalAdapter.EditButtonClickListener , AdminAnimalAdapter.DeleteButtonClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinner;
    private ArrayList<CageModel> cages;
    private CageSpinnerAdapter adapter;
    private ArrayList<AnimalModel> animals;
    private AdminAnimalAdapter animlistadapter;
    private int selectedAnimalId;
    public static RequestQueue rq;

    private GridView gridView;
    private EditText animalNameEditText;
    private EditText animalImgsrcEditText;
    private EditText animalAgeEditText;
    private Spinner animalGenderSpinner;
    private Spinner animalCageSpinner;

    public AnimalAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnimalAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnimalAdminFragment newInstance(String param1, String param2) {
        AnimalAdminFragment fragment = new AnimalAdminFragment();
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
        View view = inflater.inflate(R.layout.fragment_animal_admin, container, false);

        // Initializers
        spinner = view.findViewById(R.id.editor_animal_cage);
        cages = new ArrayList<>();
        adapter = new CageSpinnerAdapter(getContext(), cages);
        spinner.setAdapter(adapter);
        gridView = view.findViewById(R.id.admin_animal_list);
        animals = new ArrayList<>();
        animlistadapter = new AdminAnimalAdapter(getContext(), animals);
        gridView.setAdapter(animlistadapter);

        // All our edit text initialized
        animalNameEditText = view.findViewById(R.id.editor_animal_name);
        animalImgsrcEditText = view.findViewById(R.id.editor_animal_imgsrc);
        animalAgeEditText = view.findViewById(R.id.editor_animal_age);
        animalGenderSpinner = view.findViewById(R.id.editor_animal_gender);
        animalCageSpinner = view.findViewById(R.id.editor_animal_cage);

        // Gets
        getCages();
        getAnimals();



        // The admin buttons
        animlistadapter.setEditButtonClickListener(this);
        animlistadapter.setDeleteButtonClickListener(this);

        Button saveButton = view.findViewById(R.id.btn_admin_animal_save);

        // Set OnClickListener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimal();
            }
        });

        // Gender spinner array
        ArrayAdapter<Gender> genderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Gender.values());
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalGenderSpinner.setAdapter(genderAdapter);


        return view;
    }

    // Edit button
    @Override
    public void onEditButtonClick(AnimalModel selectedItem) {
        // Populate the edit views with data from selectedItem
        populateEditViews(selectedItem);

        // Set the selected animal ID
        selectedAnimalId = selectedItem.getId();
    }

    // Delete Button
    @Override
    public void onDeleteButtonClick(AnimalModel selectedItem) {
        // Set the selected animal ID
        deleteAnimal(selectedItem.getId());
    }

    // Method for formatting existing data into our editor
    private void populateEditViews(AnimalModel selectedItem) {

        animalNameEditText.setText(selectedItem.getName());
        animalAgeEditText.setText(String.valueOf(selectedItem.getAge()));
        animalImgsrcEditText.setText(selectedItem.getImgsrc());

        // Find the position of the gender in the Gender enum
        Gender gender = Gender.valueOf(selectedItem.getGender().toString());
        int genderPosition = getGenderPosition(gender);

        // Set the spinner to the position of the gender
        animalGenderSpinner.setSelection(genderPosition);

        // Find the position of the cage in the list of cages
        CageModel selectedCage = selectedItem.getCage();
        int cagePosition = getCagePosition(selectedCage);

        // Set the cage spinner to the position of the selected cage
        animalCageSpinner.setSelection(cagePosition);
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

    // Method to get the gender position
    private int getGenderPosition(Gender gender) {
        Gender[] genders = Gender.values();
        for (int i = 0; i < genders.length; i++) {
            if (genders[i] == gender) {
                return i;
            }
        }
        return 0; // Default to the first position if not found
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

    // Method to get all the animals from the server
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
                animlistadapter.notifyDataSetChanged(); // Notify adapter of data change
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

    // Method to save/edit an animal from the server
    private void saveAnimal() {
        // Retrieve values from views
        String name = animalNameEditText.getText().toString();
        String imgsrc = animalImgsrcEditText.getText().toString();
        int age = Integer.parseInt(animalAgeEditText.getText().toString());
        Gender gender = (Gender) animalGenderSpinner.getSelectedItem();
        CageModel selectedCage = (CageModel) animalCageSpinner.getSelectedItem();

        int cageId = 0;
        if (selectedCage != null){
            cageId = selectedCage.getId();
        }


        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(animalNameEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(animalImgsrcEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(animalAgeEditText.getWindowToken(), 0);

        // Create JSON object with the animal data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("imgsrc", imgsrc);
            requestBody.put("age", age);
            requestBody.put("gender", gender.toString()); // Convert enum to string
            if (cageId != 0) {
                JSONObject cageObject = new JSONObject();
                cageObject.put("id", cageId);
                requestBody.put("cage", cageObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Determine the URL and request method based on whether the user is editing an existing animal or creating a new one
        String url;
        int method;

        if (selectedAnimalId != 0) {
            // Editing existing animal: use PUT request
            url = Host + "/api/ac/id/" + selectedAnimalId;
            method = Request.Method.PUT;
        } else {
            // Creating new animal: use POST request
            url = Host + "/api/ac";
            method = Request.Method.POST;
        }

        // Create a request
        JsonObjectRequest request = new JsonObjectRequest(method, url, requestBody,
                response -> {
                    // Handle response
                    Log.d("SaveAnimal", "Response: " + response.toString());
                    // Handle success response from server
                },
                error -> {
                    // Handle error
                    Log.e("SaveAnimal", "Error: " + error.toString());
                    // Handle error response from server
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to delete an animal from the server
    private void deleteAnimal(int animalId) {
        // Construct the URL for deleting the animal
        String url = Host + "/api/ac/id/" + animalId;

        // Create a DELETE request using Volley
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Animal successfully deleted, handle response if needed
                    // For example, you might want to refresh the animal list after deletion
                },
                error -> {
                    // Handle error response, if any
                    Log.e("Volley", "Error deleting animal: " + error.toString());
                });

        // Add the request to the Volley request queue
        reloadFragment();
        rq.add(request);
    }

    // Method to reload the fragment
    private void reloadFragment() {
        getAnimals();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment_container, new AnimalAdminFragment());
        fragmentTransaction.commit();
    }
}