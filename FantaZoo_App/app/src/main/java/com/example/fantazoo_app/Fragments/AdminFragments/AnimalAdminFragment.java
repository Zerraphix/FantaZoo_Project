package com.example.fantazoo_app.Fragments.AdminFragments;

import static com.example.fantazoo_app.Secrets.Host;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Adapters.AnimalAdapter;
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
public class AnimalAdminFragment extends Fragment {

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
    public static RequestQueue rq;

    private EditText animalNameEditText;
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

        spinner = view.findViewById(R.id.editor_animal_cage);
        cages = new ArrayList<>();
        adapter = new CageSpinnerAdapter(getContext(), cages);
        spinner.setAdapter(adapter);

        animalNameEditText = view.findViewById(R.id.editor_animal_name);
        animalAgeEditText = view.findViewById(R.id.editor_animal_age);
        animalGenderSpinner = view.findViewById(R.id.editor_animal_gender);
        animalCageSpinner = view.findViewById(R.id.editor_animal_cage);

        getCages();

        Button saveButton = view.findViewById(R.id.btn_admin_animal_save);

        // Set OnClickListener to the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimal();
            }
        });

        ArrayAdapter<Gender> genderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Gender.values());
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalGenderSpinner.setAdapter(genderAdapter);


        return view;
    }

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

    private void saveAnimal() {
        // Retrieve values from views
        String name = animalNameEditText.getText().toString();
        int age = Integer.parseInt(animalAgeEditText.getText().toString());
        Gender gender = (Gender) animalGenderSpinner.getSelectedItem();
        CageModel selectedCage = (CageModel) animalCageSpinner.getSelectedItem();


        int cageId = selectedCage.getId();

        // Create JSON object with the animal data
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", name);
            requestBody.put("age", age);
            requestBody.put("gender", gender.toString()); // Convert enum to string
            JSONObject cageObject = new JSONObject();
            cageObject.put("id", cageId);
            requestBody.put("cage", cageObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Host + "/api/ac", requestBody,
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

        // Add the request to the RequestQueue
        rq.add(request);
    }
}