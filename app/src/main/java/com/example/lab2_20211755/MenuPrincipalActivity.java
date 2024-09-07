package com.example.lab2_20211755;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Objects;

// -----------------------
// --- LABORATORIO N°2 ---
// -----------------------

// Código: 20211755

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Animación:
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        getWindow().setExitTransition(new Explode().setDuration(1000).excludeTarget(R.id.image_antenna,false));

        // Vista:
        setContentView(R.layout.activity_menu_principal);

        // Cambiar el titulo del AppBar:
        getSupportActionBar().setTitle("APPSIoT- Lab 2");

        // Registrar la pulsación para el Context Menu:
        registerForContextMenu(findViewById(R.id.titulo_principal));

        // Gestionar input del nombre:
        Button botonJugar = findViewById(R.id.button_jugar);
        botonJugar.setEnabled(false);
        ((EditText)findViewById(R.id.input_nombre)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                botonJugar.setEnabled(!charSequence.toString().isEmpty());
                if(!charSequence.toString().isEmpty()){
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .playOn(botonJugar);
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Lógica del botón de Jugar:
        botonJugar.setOnClickListener(v -> {
            YoYo.with(Techniques.RubberBand)
                    .duration(1500)
                    .playOn(botonJugar);
            abrirJuego();
        });

        // Antenita:
        YoYo.with(Techniques.ZoomInUp)
                .duration(3000)
                .playOn(findViewById(R.id.image_antenna));

    }

    // Abrir la actividad del juego:
    public void abrirJuego() {
        Intent intent = new Intent(MenuPrincipalActivity.this, JuegoAhorcadoActivity.class);
        intent.putExtra("Nombre",((TextView)findViewById(R.id.input_nombre)).getText().toString());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    // Crear el Context Menu:
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_titulo, menu);
    }

    // Gestionar las acciones del Context Menu:
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.titulo_verde) {
            ((TextView) findViewById(R.id.titulo_principal)).setTextColor(getColor(R.color.verde));
            return true;
        } else if (id == R.id.titulo_rojo) {
            ((TextView) findViewById(R.id.titulo_principal)).setTextColor(getColor(R.color.rojo));
            return true;
        } else if (id == R.id.titulo_morado) {
            ((TextView) findViewById(R.id.titulo_principal)).setTextColor(getColor(R.color.morado));
            return true;
        } else {
            ((TextView) findViewById(R.id.titulo_principal)).setTextColor(getColor(R.color.black));
            return super.onContextItemSelected(item);
        }
    }
}