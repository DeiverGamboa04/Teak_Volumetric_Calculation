package com.example.teakvolumetriccalculation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
public class TemaActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout inicio, configuracion, repositorio, tema, calculadora, repositoriomanual;

    private Switch mode_switch;
    private TextView modeStatus;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tema);

        // Lee la preferencia antes de establecer el contenido de la vista
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_tema);

        mode_switch = findViewById(R.id.mode_switch);
        modeStatus = findViewById(R.id.tv_mode);
        mode_switch.setChecked(useDarkTheme);

        // Comprueba y configura el modo según la variable global
       /*int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        updateModeStatus(currentNightMode);*/

        mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int newNightMode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(newNightMode);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREF_DARK_THEME, isChecked);
                editor.apply();

                // Actualiza la variable global y el estado de todas las actividades
                updateModeStatus(newNightMode);
            }
        });

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
                redirecActivity(TemaActivity.this, MainActivity.class);
            }
        });
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(TemaActivity.this, ConfiguracionActivity.class);
            }
        });
        tema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecActivity(TemaActivity.this, CalculoManualActivity.class);
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

    // Maneja el botón de regreso para volver a ConfiguracionActivity
    @Override
    public void onBackPressed() {
        // Intent para volver a ConfiguracionActivity
        super.onBackPressed();
        Intent intent = new Intent(TemaActivity.this, ConfiguracionActivity.class);
        startActivity(intent);
        finish();
    }

    //Tema
    private void updateModeStatus(int nightMode) {
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            modeStatus.setText("Dark Mode");
        } else {
            modeStatus.setText("Light Mode");
        }
    }
}