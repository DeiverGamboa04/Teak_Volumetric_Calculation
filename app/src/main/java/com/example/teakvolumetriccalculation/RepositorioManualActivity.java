package com.example.teakvolumetriccalculation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teakvolumetriccalculation.adapter.VolumMAdapter;
import com.example.teakvolumetriccalculation.modelo.VolumM;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RepositorioManualActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, repositorio, tema, calculadora, repositoriomanual;

    private RecyclerView recyclerView;
    private VolumMAdapter adapter;
    private List<VolumM> listaVolumen;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositorio_manual);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.RecyclerViewManual);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaVolumen = new ArrayList<>();
        adapter = new VolumMAdapter(this, listaVolumen);
        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.home);
        configuracion = findViewById(R.id.settings2);
        tema = findViewById(R.id.theme);
        calculadora = findViewById(R.id.calculate);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioManualActivity.this, MainActivity.class);
            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioManualActivity.this, ConfiguracionActivity.class);
            }
        });
        tema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioManualActivity.this, TemaActivity.class);
            }
        });
        calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioManualActivity.this, CalculoManualActivity.class);
            }
        });

        loadData();
    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirecActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        // Intent para volver a ConfiguracionActivity
        super.onBackPressed();
        Intent intent = new Intent(RepositorioManualActivity.this, ConfiguracionActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadData() {
        db.collection("calculomanualvolumen")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaVolumen.clear(); // Limpiar la lista antes de agregar elementos nuevos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            VolumM volum = document.toObject(VolumM.class);
                            listaVolumen.add(volum);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firebase Error", "Error al obtener documentos: ", task.getException());
                    }
                });
    }
}