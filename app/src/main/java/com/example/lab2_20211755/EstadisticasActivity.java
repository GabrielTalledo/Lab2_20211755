package com.example.lab2_20211755;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class EstadisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        // Activar botón para atrás:
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Cambiar el titulo del AppBar:
        getSupportActionBar().setTitle("TeleGame");

        // Controlar el intent del Juego
        Intent intentJuego = getIntent();
        String nombre = intentJuego.getStringExtra("nombre");
        String estadisticas = intentJuego.getStringExtra("estadisticas");
        TextView textNombre = findViewById(R.id.text_jugador);
        textNombre.setText("Jugador: "+nombre);
        TextView textEstadisticas = findViewById(R.id.text_estadisticas);
        if(estadisticas.isEmpty()){
            textEstadisticas.setText("Aún no hay estadísticas :(");
        }else{
            textEstadisticas.setText(estadisticas);
        }

    }
}