package com.example.fantazoo_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fantazoo_app.Fragments.AdminFragments.AnimalAdminFragment;
import com.example.fantazoo_app.Fragments.AdminFragments.CageAdminFragment;
import com.example.fantazoo_app.Fragments.AdminFragments.WeaponAdminFragment;
import com.example.fantazoo_app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        fragmentChanger(AnimalAdminFragment.class);

        initGui(view);

        return view;
    }

    void initGui(View view) {
        view.findViewById(R.id.btn_admin_animal).setOnClickListener(v -> fragmentChanger(AnimalAdminFragment.class));
        view.findViewById(R.id.btn_admin_cage).setOnClickListener(v -> fragmentChanger(CageAdminFragment.class));
        view.findViewById(R.id.btn_admin_zookeeper).setOnClickListener(v -> fragmentChanger(ZooKeepersFragment.class));
        view.findViewById(R.id.btn_admin_weapon).setOnClickListener(v -> fragmentChanger(WeaponAdminFragment.class));
    }


    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.admin_fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}