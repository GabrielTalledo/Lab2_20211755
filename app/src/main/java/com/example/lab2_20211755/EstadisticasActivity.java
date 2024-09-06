package com.example.lab2_20211755;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class EstadisticasActivity extends AppCompatActivity {

    public Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        // Activar botón para atrás:
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Cambiar el titulo del AppBar:
        getSupportActionBar().setTitle("TeleGame");

        // Controlar el intent del Juego
        Intent intentJuego = getIntent();
        data = intentJuego.getExtras();
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

        findViewById(R.id.button_nuevo_juego2).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtras(data);
            resultIntent.putExtra("NuevoJuego","Este texto no importa XD");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // Gestionamos el back button:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence xd = item.getTitle();
        if(item.getItemId() == android.R.id.home){
            // GoodBye xD:
            Intent resultIntent = new Intent();
            resultIntent.putExtras(data);
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        }else{
            return false;
        }

    }

}