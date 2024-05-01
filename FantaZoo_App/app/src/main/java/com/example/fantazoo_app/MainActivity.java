package com.example.fantazoo_app;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fantazoo_app.Fragments.AnimalsFragment;
import com.example.fantazoo_app.Fragments.CagesFragment;
import com.example.fantazoo_app.Fragments.ZooKeepersFragment;

public class MainActivity extends AppCompatActivity {
    public static RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rq = Volley.newRequestQueue(this);

        fragmentChanger(AnimalsFragment.class);

        initGui();
    }

    void initGui() {
        findViewById(R.id.btn_animals).setOnClickListener(v -> fragmentChanger(AnimalsFragment.class));
        findViewById(R.id.btn_cages).setOnClickListener(v -> fragmentChanger(CagesFragment.class));
        findViewById(R.id.btn_zookeepers).setOnClickListener(v -> fragmentChanger(ZooKeepersFragment.class));
    }

    private void fragmentChanger(Class c) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, c, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            showCloseAppDialog();
        }
    }

    private void showCloseAppDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Going back further will close the app. Are you sure you want to continue?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Close the app
                    finish();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    // Dismiss the dialog
                    dialogInterface.dismiss();
                })
                .show();
    }
}