package com.example.teakvolumetriccalculation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CalculoManualActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, repositorio, tema, calculadora, repositoriomanual;

    EditText diamet, alturac, factorf, consta;
    TextView txtresults10, results;

    private FirebaseFirestore mfirestorecalculomanual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_manual);


        diamet = findViewById(R.id.diamet);
        alturac = findViewById(R.id.alturac);
        factorf = findViewById(R.id.factorf); // Ya tiene valor predeterminado
        consta = findViewById(R.id.consta); // Ya tiene valor predeterminado
        txtresults10 = findViewById(R.id.txtresults10);
        results = findViewById(R.id.results);

        mfirestorecalculomanual = FirebaseFirestore.getInstance();

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
                redirecActivity(CalculoManualActivity.this, MainActivity.class);
            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(CalculoManualActivity.this, ConfiguracionActivity.class);;
            }
        });
        tema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(CalculoManualActivity.this, TemaActivity.class);
            }
        });
        calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
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
    public void Calculofx(View view) {
        double diametro = Double.parseDouble(diamet.getText().toString());
        double altura = Double.parseDouble(alturac.getText().toString());
        double factorForma = Double.parseDouble(factorf.getText().toString());
        double constante = Double.parseDouble(consta.getText().toString());

        // Calcular el resultado
        double resultado = Math.pow(diametro, 2) * altura * factorForma * constante;

        // Formatear el resultado a dos decimales
        String resultadoFormateado = String.format("%.2f", resultado); // Para dos decimales

        // Mostrar el resultado
        txtresults10.setText("Volumen: " + resultadoFormateado);


        // Calcular el resultado de aproximación de un año
        double result = Math.pow(diametro + 0.03, 2) * altura + 1.6 * factorForma * constante;

        // Formatear el resultado a dos decimales
        String resultFormateado = String.format("%.2f", result); // Para dos decimales

        // Mostrar el resultado de aproximación de un año
        results.setText("Volumen aproximado en 1 año: " + resultFormateado);
    }
    public void GuardarfxM(View v) {
        /*Guardar en el repositorio*/
       String diame = diamet.getText().toString().trim();
       String altur = alturac.getText().toString().trim();
       String fact = factorf.getText().toString().trim();
       String cons = consta.getText().toString().trim();
       String txtre = txtresults10.getText().toString().trim();
       String resu = results.getText().toString().trim();

        // Verifica que los campos no estén vacíos
        if(!diame.isEmpty() && !altur.isEmpty() && !fact.isEmpty() && !cons.isEmpty() && !txtre.isEmpty() && !resu.isEmpty()){
            // Si algún campo tiene datos, procede a guardar en Firestore
            postVolum(diame, altur, fact, cons, txtre, resu);
        }else {
            Toast.makeText(getApplicationContext(), "Por favor, ingresa todos los datos requeridos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void postVolum(String diame, String altur, String fact, String cons, String txtre, String resu) {
        Map<String, String> map = new HashMap<>();
        map.put("diametro", diame);
        map.put("alturaComercial", altur);
        map.put("factorForma", fact);
        map.put("constante", cons);
        map.put("volumen", txtre);
        map.put("volumenAproximado", resu);

        mfirestorecalculomanual.collection("calculomanualvolumen")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                /*Toast.makeText(getApplicationContext(), "Se ingreso los datos exitosamente", Toast.LENGTH_SHORT).show();*/
                Limpiarfx(null);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /*Toast.makeText(getApplicationContext(), "Error al ingresar los datos", Toast.LENGTH_SHORT).show();*/
                Toast.makeText(getApplicationContext(), "Error al guardar los datos: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Limpiarfx(View view) {
        diamet.setText("");
        alturac.setText("");
        //factorf.setText("");
        //consta.setText("");
        txtresults10.setText("");
        results.setText("");
    }
}