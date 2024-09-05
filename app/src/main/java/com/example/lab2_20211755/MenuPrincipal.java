package com.example.lab2_20211755;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Cambiar el titulo del AppBar:
        Objects.requireNonNull(getSupportActionBar()).setTitle("APPSIoT- Lab 2");

        // Registrar la pulsación para el Context Menu:
        registerForContextMenu(findViewById(R.id.titulo_principal));

        // Gestionar input del nombre:
        findViewById(R.id.button_jugar).setEnabled(false);
        ((EditText)findViewById(R.id.input_nombre)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                findViewById(R.id.button_jugar).setEnabled(!charSequence.toString().isEmpty());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Lógica del botón de Jugar:
        findViewById(R.id.button_jugar).setOnClickListener(v -> {
            abrirJuego();
            Toast.makeText(this, "Bienvenido " + ((EditText)findViewById(R.id.input_nombre)).getText().toString()+"!", Toast.LENGTH_SHORT).show();
        });

    }

    // Abrir la actividad del juego:
    public void abrirJuego() {
        Intent intent = new Intent(this, JuegoAhorcado.class);
        intent.putExtra("Nombre",((TextView)findViewById(R.id.input_nombre)).getText().toString());
        startActivity(intent);
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