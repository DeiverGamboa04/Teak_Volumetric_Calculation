package com.example.teakvolumetriccalculation;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class RepositorioActivity3 extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, compartir, repositorio, tema, calculadora, repositoriomanual;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositorio3);

        Toolbar toolbar = findViewById(R.id.punto);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.home);
        configuracion = findViewById(R.id.settings2);
        compartir = findViewById(R.id.share);
        repositorio = findViewById(R.id.report2);
        tema = findViewById(R.id.theme);
        calculadora = findViewById(R.id.calculate);
        repositoriomanual = findViewById(R.id.repositorio);

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
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioActivity3.this, CompartirActivity.class);
            }
        });
        repositorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
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
        repositoriomanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(RepositorioActivity3.this, RepositorioManualActivity.class);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            Toast.makeText(this, "Eliminar", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.ssharre) {
            Toast.makeText(this, "Compartir", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Observa esta aplicación");
            intent.putExtra(Intent.EXTRA_TEXT, "En este lugar va el link de la aplicación");
            startActivity(Intent.createChooser(intent, "Compartír"));
        }
        if (id == R.id.create) {
            Toast.makeText(this, "Crear nueva hectárea", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.down) {
            Toast.makeText(this, "Descargar", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void verDatosFirebase() {
        db.collection("Hectáreas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            // Aquí puedes manejar los datos, por ejemplo, mostrándolos en un ListView o RecyclerView
                        }
                    } else {
                        Log.w(TAG, "Error al obtener documentos.", task.getException());
                    }
                });
    }

    public void descargarDatosComoPDF() {
        String dest = getExternalFilesDir(null) + "/DatosHectareas.pdf"; // Ruta del archivo PDF

        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Aquí debes recuperar los datos de Firestore y añadirlos al documento
            // Por ejemplo:
            db.collection("Hectáreas")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                document.add(new Paragraph(documentSnapshot.getId() + " => " + documentSnapshot.getData().toString()));
                            }
                            document.close(); // Cierra el documento después de añadir todos los datos
                        } else {
                            Log.w(TAG, "Error al obtener documentos.", task.getException());
                        }
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}