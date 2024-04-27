package com.example.teakvolumetriccalculation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teakvolumetriccalculation.adapter.MyAdapter;
import com.example.teakvolumetriccalculation.modelo.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RepositorioActivity3 extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, repositorio, tema, calculadora, repositoriomanual;


    private RecyclerView recyclerViewini;
    private MyAdapter adapterm;
    private List<User> userArrayList;
    private FirebaseFirestore dbd;

    TextView txtDatosFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositorio3);

        /*Toolbar toolbar = findViewById(R.id.punto);
        setSupportActionBar(toolbar);*/


        dbd = FirebaseFirestore.getInstance();

        recyclerViewini = findViewById(R.id.RecyclerInicio);
        recyclerViewini.setHasFixedSize(true);
        recyclerViewini.setLayoutManager(new LinearLayoutManager(this));

        userArrayList = new ArrayList<>();
        adapterm = new MyAdapter(this, userArrayList);
        recyclerViewini.setAdapter(adapterm);


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
                redirecActivity(RepositorioActivity3.this, MainActivity.class);
            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioActivity3.this, ConfiguracionActivity.class);
            }
        });
        tema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioActivity3.this, TemaActivity.class);
            }
        });
        calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioActivity3.this, CalculoManualActivity.class);
            }
        });

        loadDatas();
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirecActivity(Activity activity, Class secondActivity) {
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
        Intent intent = new Intent(RepositorioActivity3.this, ConfiguracionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return false;
    }

    private void loadDatas() {
        dbd.collection("datosParcela")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userArrayList.clear(); // Limpiar la lista antes de agregar elementos nuevos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            userArrayList.add(user);
                        }
                        adapterm.notifyDataSetChanged();
                    } else {
                        Log.d("Firebase Error", "Error al obtener documentos: ", task.getException());
                    }
                });
    }

}
